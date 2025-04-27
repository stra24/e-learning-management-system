package com.everrefine.elms.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

  // Bearerトークンの先頭文字列
  private static final String BEARER_PREFIX = "Bearer ";
  // JWTの有効期限（1時間）
  private static final Duration ACCESS_TOKEN_EXPIRATION = Duration.ofHours(1);
  // リフレッシュトークンの有効期限（30日）
  private static final Duration REFRESH_TOKEN_EXPIRATION = Duration.ofDays(30);
  // 256ビット（32バイト）のBase64エンコードされた秘密鍵
  private static String base64EncodedSecretKey;

  /**
   * 署名キーを取得する。
   *
   * @return 署名キー
   */
  private static Key getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(base64EncodedSecretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  /**
   * トークンの中からSubject（ユーザーID）を取得する。
   *
   * @param token 対象のトークン
   * @return Subject（ユーザーID）
   */
  public static String extractSubjectFromToken(String token) {
    return extractSpecificInfoFromClaim(token, Claims::getSubject);
  }

  /**
   * クレーム（情報）内の指定の情報を抽出する
   *
   * @param token           トークン
   * @param claimsTFunction 引数: クレーム、戻り値: TのFunction
   * @param <T>             クレーム（情報）内の指定の情報の型
   * @return クレーム（情報）内の指定の情報
   */
  public static <T> T extractSpecificInfoFromClaim(String token,
      Function<Claims, T> claimsTFunction) {
    Claims claims = Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
    return claimsTFunction.apply(claims);
  }

  /**
   * JWTトークンをレスポンスのクッキーにセットする。
   *
   * @param response レスポンス情報
   * @param token    JWTトークン
   */
  public static void setJwtTokenToResponseCookie(HttpServletResponse response, String token) {
    ResponseCookie jwtCookie = ResponseCookie.from("JWT", token)
        .httpOnly(false) // フロントエンドでクッキーのJWTをリクエストヘッダーに付与するため、JSで扱えるようにfalseにする。
        .secure(true) // sameSite=Noneの場合secure=trueでないとブラウザがクッキーを受け付けるのを拒否する
        .path("/")
        .maxAge(Duration.ofMinutes(1)) // アクセストークンの有効期限（例: 10分）万が一漏洩しても被害を最小にするため短命にする。
        // sameSite・・・「どんなときにブラウザがこのCookieを送るか」を決める設定。
        // Strict（CSRF対策）: 他ドメインorポートの場合は送らない。ただし、リクエストヘッダーでJWTを指定した場合は送られる。
        // Lax：GETの場合だけ送る。
        // None: 無条件に送る。違うドメインやポートに送る場合はNoneでないといけない。
        .sameSite("Strict") // CSRF対策用で、リクエストヘッダーJWT無しのリクエスト時は、クッキーではJWTを送らないようにする。
        .build();

    response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());
  }

  /**
   * リフレッシュトークンをレスポンスのクッキーにセットする。
   *
   * @param response レスポンス情報
   * @param token    リフレッシュトークン
   */
  public static void setRefreshTokenToResponseCookie(HttpServletResponse response, String token) {
    ResponseCookie refreshTokenCookie = ResponseCookie.from("RefreshToken", token)
        .httpOnly(true)
        .secure(true)
        .path("/")
        .maxAge(Duration.ofDays(30))
        .sameSite("None")
        .build();
    response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
  }

  /**
   * JWTとRefreshTokenを削除する。（ログアウト用）
   *
   * @param response レスポンス情報
   */
  public static void clearJwtAndRefreshToken(HttpServletResponse response) {
    ResponseCookie jwtCookie = ResponseCookie.from("JWT", "")
        .httpOnly(true)
        .secure(true)
        .path("/")
        .maxAge(0)
        .build();
    ResponseCookie refreshTokenCookie = ResponseCookie.from("RefreshToken", "")
        .httpOnly(true)
        .secure(true)
        .path("/")
        .maxAge(0)
        .build();

    response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());
    response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
  }

  /**
   * リクエストヘッダーからJWTを取得する。
   *
   * @param request リクエスト情報
   * @return JWTの値
   */
  public static Optional<String> getJwtFromRequestHeader(HttpServletRequest request) {
    String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
      return Optional.of(authorizationHeader.substring(BEARER_PREFIX.length()));
    }
    return Optional.empty();
  }

  /**
   * トークンのフォーマットが有効であるかをチェックする。
   *
   * @param token 対象のトークン
   * @return true: トークンのフォーマットが有効である、false: トークンのフォーマットが無効である
   */
  public static boolean isTokenFormatValid(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * トークンが有効期限切れであるかをチェックする。
   *
   * @param token 対象トークン
   * @return true: 有効期限切れ、false: 有効期間中
   */
  public static boolean isTokenExpired(String token) {
    Date expiration = extractSpecificInfoFromClaim(token, Claims::getExpiration);
    return expiration.before(new Date());
  }

  /**
   * 認証情報をSecurityContextにセットする。
   *
   * @param request     リクエスト情報
   * @param userDetails 認証情報
   */
  public static void setTokenToSecurityContext(HttpServletRequest request,
      UserDetails userDetails) {
    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
        userDetails,
        null,
        userDetails.getAuthorities()
    );
    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContextHolder.getContext().setAuthentication(authToken);
  }

  /**
   * クッキーからRefreshTokenを取得する。 クッキー自体がない or RefreshTokenのクッキーがない場合はOptional.empty()を返す。
   *
   * @param request リクエスト情報
   * @return RefreshTokenのクッキー
   */
  public static Optional<String> getRefreshTokenFromCookie(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    if (cookies == null) {
      return Optional.empty();
    }

    for (Cookie cookie : cookies) {
      if ("RefreshToken".equals(cookie.getName())) {
        return Optional.of(cookie.getValue());
      }
    }
    return Optional.empty();
  }

  /**
   * JWTトークンを生成する。
   *
   * @param subject サブジェクト
   * @return JWTトークン
   */
  public static String generateJwtToken(String subject) {
    return generateToken(subject, ACCESS_TOKEN_EXPIRATION);
  }

  /**
   * リフレッシュトークンを生成する。
   *
   * @param subject サブジェクト
   * @return リフレッシュトークン
   */
  public static String generateRefreshToken(String subject) {
    return generateToken(subject, REFRESH_TOKEN_EXPIRATION);
  }

  /**
   * トークンを生成する。
   *
   * @param subject サブジェクト
   * @return トークン
   */
  private static String generateToken(String subject, Duration expiration) {
    Instant now = Instant.now();
    return Jwts.builder()
        .setSubject(subject)
        .setIssuedAt(Date.from(now))
        .setExpiration(Date.from(now.plus(expiration)))
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  /**
   * application.ymlに定義した値をstaticフィールドに代入したいため、定義している。
   *
   * @param secretKey 秘密鍵
   */
  @Value("${jwt.secret}")
  public void setBase64EncodedSecretKey(String secretKey) {
    JwtUtil.base64EncodedSecretKey = secretKey;
  }
}
