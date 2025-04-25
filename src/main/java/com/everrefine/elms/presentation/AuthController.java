package com.everrefine.elms.presentation;

import com.everrefine.elms.infrastructure.security.JwtUtil;
import com.everrefine.elms.presentation.request.LoginRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final JwtUtil jwtUtil;

  @PostMapping("/login")
  public ResponseEntity<?> login(
      @RequestBody LoginRequest loginRequest,
      HttpServletResponse response
  ) {
    try {
      // 認証処理
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              loginRequest.getEmailAddress(),
              loginRequest.getPassword()
          )
      );

      // 認証成功 → JWT発行
      String jwt = jwtUtil.generateToken(authentication.getName());
      String refreshToken = jwtUtil.generateRefreshToken(authentication.getName());

      // アクセストークンのクッキー設定
      ResponseCookie jwtCookie = ResponseCookie.from("JWT", jwt)
          .httpOnly(false) // フロントエンドでリクエストヘッダーにJWTを付与するため、JSを扱えるようにfalseにする。
          .secure(false) // 本番環境ではtrue
          .path("/")
          .maxAge(Duration.ofMinutes(10)) // アクセストークンの有効期限（例: 10分）万が一漏洩しても被害を最小にするため短命にする。
          .sameSite("Strict") // 外部サイトからのリクエストをブロック（CSRF対策）
          .build();

      // リフレッシュトークンのクッキー設定
      ResponseCookie refreshTokenCookie = ResponseCookie.from("RefreshToken", refreshToken)
          .httpOnly(true) // XSS対策
          .secure(false) // 本番環境ではtrue
          .path("/")
          .maxAge(Duration.ofDays(30)) // リフレッシュトークンの有効期限（例: 30日）
          .sameSite("Strict") // 外部サイトからのリクエストをブロック（CSRF対策）
          .build();

      // クッキーをレスポンスに追加
      response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());
      response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

      // ログイン成功
      return ResponseEntity.ok().body(Map.of("message", "Login successful"));

    } catch (AuthenticationException e) {
      // 認証失敗
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(Map.of("error", "Invalid credentials"));
    }
  }
}
