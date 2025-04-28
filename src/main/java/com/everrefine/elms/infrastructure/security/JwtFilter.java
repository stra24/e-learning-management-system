package com.everrefine.elms.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  private final CustomUserDetailsService customUserDetailsService;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain
  ) throws ServletException, IOException {

    Optional<String> jwtOptional = JwtUtil.getJwtFromRequestHeader(request);

    if (jwtOptional.isPresent()) {
      // リクエストヘッダーにJWTが付与されている場合
      String jwt = jwtOptional.get();

      if (JwtUtil.isTokenFormatValid(jwt)) {
        // リクエストヘッダーJWTの値のフォーマットが正常である場合
        String userId = JwtUtil.extractSubjectFromToken(jwt);

        UserDetails userDetails;
        try {
          userDetails = customUserDetailsService.loadUserById(userId);
        } catch (UsernameNotFoundException e) {
          // JWT内のユーザーがデータソースに存在しない場合
          filterChain.doFilter(request, response);
          return;
        }

        if (JwtUtil.isTokenExpired(jwt)) {
          // JWTの有効期限が切れた場合
          recreateJwtAndRefreshToken(request, response);
          filterChain.doFilter(request, response);
          return;
        }

        // JWTがすべてのチェックをクリアした場合
        JwtUtil.setTokenToSecurityContext(request, userDetails);
      } else {
        // リクエストヘッダーJWTの値のフォーマットが不正である場合（＝JWTのクッキーの有効期限が切れた場合）
        recreateJwtAndRefreshToken(request, response);
      }
    }

    // リクエストヘッダーにJWTが付与されていない場合（CSRFされた or 認証パス不要なAPI）は何もしない。
    filterChain.doFilter(request, response);
  }

  /**
   * JWTとリフレッシュトークンを再生成する。
   *
   * @param request  リクエスト情報
   * @param response レスポンス情報
   */
  private void recreateJwtAndRefreshToken(
      HttpServletRequest request,
      HttpServletResponse response
  ) {
    // JWTが無効（ユーザーIDが存在しないor有効期限切れ）であれば、リフレッシュトークンを使って新しいJWTを発行する。
    JwtUtil.getRefreshTokenFromCookie(request).ifPresent(refreshToken -> {
      if (JwtUtil.isTokenFormatValid(refreshToken)) {
        // リフレッシュトークンのフォーマットが正常である場合
        String userId = JwtUtil.extractSubjectFromToken(refreshToken);

        try {
          customUserDetailsService.loadUserById(userId);
        } catch (UsernameNotFoundException e) {
          // JWT内のユーザーがデータソースに存在しない場合
          return;
        }

        if (JwtUtil.isTokenExpired(refreshToken)) {
          // JWTの有効期限が切れた場合
          return;
        }

        // JWTとRefreshTokenを再生成＆クッキーにセットする。
        String newJwtToken = JwtUtil.generateJwtToken(userId);
        String newRefreshToken = JwtUtil.generateRefreshToken(userId);
        JwtUtil.setJwtTokenToResponseCookie(response, newJwtToken);
        JwtUtil.setRefreshTokenToResponseCookie(response, newRefreshToken);

        // 新しいJWTで認証情報を設定
        UserDetails newUserDetails = customUserDetailsService.loadUserById(
            JwtUtil.extractSubjectFromToken(newJwtToken)
        );
        JwtUtil.setTokenToSecurityContext(request, newUserDetails);
      }
    });
  }
}