package com.everrefine.elms.presentation;

import com.everrefine.elms.infrastructure.security.JwtUtil;
import com.everrefine.elms.presentation.request.LoginRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

  private final AuthenticationManager authenticationManager;

  @PostMapping("/login")
  public ResponseEntity<Void> login(
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

      // 認証成功 → JWTとリフレッシュトークン発行
      String jwtToken = JwtUtil.generateJwtToken(authentication.getName());
      String refreshToken = JwtUtil.generateRefreshToken(authentication.getName());

      // クッキーに設定
      JwtUtil.setJwtTokenToResponseCookie(response, jwtToken);
      JwtUtil.setRefreshTokenToResponseCookie(response, refreshToken);

      // ログイン成功
      return ResponseEntity.ok().build();

    } catch (AuthenticationException e) {
      // 認証失敗
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(HttpServletResponse response) {
    JwtUtil.clearJwtAndRefreshToken(response);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/auth/refresh")
  public ResponseEntity<Void> refreshAuth() {
    // JwtFilterにより、JWTとリフレッシュトークンが再生成されるので、APIとしては何もしない。
    return ResponseEntity.ok().build();
  }
}
