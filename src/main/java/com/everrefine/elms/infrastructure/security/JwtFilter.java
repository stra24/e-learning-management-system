package com.everrefine.elms.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  private static final String BEARER_PREFIX = "Bearer ";

  private final JwtUtil jwtUtil;
  private final CustomUserDetailsService customUserDetailsService;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain
  ) throws ServletException, IOException {

    // リクエストヘッダーからJWTを取得
    String jwt = getJwtFromHeader(request);

    // JWTがヘッダーにセットされていた場合、JWTのユーザー情報が存在するか、JWTが有効期限内であるかを確認し、OKだったら、SecurityContextに登録。
    // JWTがヘッダーにセットされていない場合は、SecurityContextに認証情報が保存されないため、未認証、つまりrequestMatchers("~~").authenticated()に反することになるので、403が返される。
    if (jwt != null) {
      String username = jwtUtil.extractUsername(jwt);

      if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails = customUserDetailsService.loadUserById(username);

        if (jwtUtil.validateToken(jwt, userDetails)) {
          // JWTがまだ有効であれば、そのまま認証情報をSecurityContextに設定
          UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
              userDetails,
              null,
              userDetails.getAuthorities()
          );
          authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authToken);
        } else {
          // JWTが無効（期限切れなど）であれば、リフレッシュトークンを使って新しいJWTを発行する
          String refreshToken = request.getHeader("Refresh-Token");
          if (refreshToken != null) {
            String newJwt = jwtUtil.refreshToken(refreshToken);
            if (newJwt != null) {
              // 新しいJWTをクッキーにセット
              ResponseCookie jwtCookie = ResponseCookie.from("JWT", newJwt)
                  .httpOnly(false)
                  .secure(true)
                  .path("/")
                  .maxAge(Duration.ofMinutes(10))
                  .sameSite("None")
                  .build();
              response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());

              // 新しいJWTで認証情報を設定
              UserDetails newUserDetails = customUserDetailsService.loadUserById(
                  jwtUtil.extractUsername(newJwt));
              UsernamePasswordAuthenticationToken newAuthToken = new UsernamePasswordAuthenticationToken(
                  newUserDetails,
                  null,
                  newUserDetails.getAuthorities()
              );
              newAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
              SecurityContextHolder.getContext().setAuthentication(newAuthToken);
            }
          }
        }
      }
    }

    filterChain.doFilter(request, response);
  }

  /**
   * リクエストヘッダーからJWTを取得する。
   *
   * @param request リクエスト情報
   * @return JWTの値
   */
  private String getJwtFromHeader(HttpServletRequest request) {
    String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
      return authorizationHeader.substring(BEARER_PREFIX.length());
    }
    return null;
  }
}