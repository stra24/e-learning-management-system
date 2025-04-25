package com.everrefine.elms.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

  // 256ビット（32バイト）のBase64エンコードされた秘密鍵
  @Value("${jwt.secret}")
  private String base64EncodedSecretKey;

  // JWTの有効期限（1時間）
  private static final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 60; // 1時間
  // リフレッシュトークンの有効期限（30日）
  private static final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24 * 30; // 30日

  private Key getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(base64EncodedSecretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  // JWTからユーザー名を抽出
  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  // クレーム（情報）を抽出するための共通メソッド
  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    Claims claims = Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
    return claimsResolver.apply(claims);
  }

  // トークンが有効かどうかを検証
  public boolean validateToken(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
  }

  // トークンが期限切れかどうかを確認
  private boolean isTokenExpired(String token) {
    Date expiration = extractClaim(token, Claims::getExpiration);
    return expiration.before(new Date());
  }

  // アクセストークン（JWT）の生成
  public String generateToken(String username) {
    return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1時間
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  // リフレッシュトークンの生成
  public String generateRefreshToken(String username) {
    return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  // リフレッシュトークンを使用して新しいアクセストークンを生成
  public String refreshToken(String refreshToken) {
    if (!validateToken(refreshToken)) {
      return null; // 無効なリフレッシュトークンの場合、nullを返す
    }

    String username = extractUsername(refreshToken);

    // 新しいアクセストークンを生成して返す
    return generateToken(username);
  }

  // リフレッシュトークンが有効かどうかを検証
  public boolean validateToken(String token) {
    try {
      // トークンの検証
      Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
