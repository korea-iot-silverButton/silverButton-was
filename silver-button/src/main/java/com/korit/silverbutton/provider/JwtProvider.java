package com.korit.silverbutton.provider;

// : JWT토큰을 생성하고 검증하는 역할
// -HS256 암호화 알고리즘을 사용하여 JWT  서명
// 비밀키는 Base64로 인코딩 지정- 환경변수(jwt.secret)
// -JWT 만료 기간은 10시간으로 지정 - 환경 변수 36000000

import com.korit.silverbutton.service.TokenBlacklistService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * JwtProvider 클래스
 * : JWT(JSON Web Token) 토큰을 생성하고 검증하는 역할
 *  cf) JWT
 *      : 사용자 정보를 암호화된 토큰으로 저장, 서버에 매번 요청할 때 전달 가능
 *      : 주로 로그인 인증에 사용
 *
 * - HS256 암호화 알고리즘을 사용하여 JWT 서명
 * - 비밀키는 Base64로 인코딩 지정 - 환경변수(jwt.secret)
 * - JWT 만료 기간은 10시간 지정 - 환경변수(jwt.expiration)
 */

@Component // 스프링 컨테이너에서 해당 클래스를 빈으로 관리하기 위해 사용

// cf) @Bean: 메서드 레벨에서 선언, 반환되는 객체를 개발자가 수동으로 빈 등록
//     @Component: 클래스 레벨에서 선언, 스프링 런타임 시 컴포넌트 스캔을 통해
//                  자동으로 빈을 찾고 등록하는 애너테이션 (의존성 주입)
public class JwtProvider {

    // 환경 변수에 지정한 비밀키 값과 만료 시간을 가져옴
    private Key key; // JWT 서명에 사용할 암호화 키
    private final TokenBlacklistService tokenBlacklistService;

    @Value("${jwt.expiration}")
    private int jwtExpirationMs; // JWT 토큰의 만료 시간을 저장

    // 토큰 만료 시간을 밀리초로 반환하는 메소드
    public int getExpiration(){
        return jwtExpirationMs;
    }

    public JwtProvider(@Value("${jwt.secret}") String secret, TokenBlacklistService tokenBlacklistService, @Value("${jwt.expiration}") int jwtExpirationMs) {
        // 생성자: JWTProvider 객체를 생성할 때 비밀키와 만료 시간을 초기화

        //Base64로 인코딩된 비밀키를 디코딩하여 HMAC-SHA 알고리즘으로 암호화된 키 생성
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.tokenBlacklistService = tokenBlacklistService;

        // 환경 변수에서 가져온 만료 시간을 변수에 저장
        this.jwtExpirationMs = jwtExpirationMs;
    }

    /*
     * generateJwtToken
     * : JWT 생성 메서드
     * : 사용자 정보를 받아 JWT 토큰을 생성하는 메서드
     *
     * @param : 사용자 정보 (User 객체)
     * @return : 생성된 JWT 토큰 문자열
     * */
    public String generateJwtToken(String userId, boolean isDependentLogin) {
        return Jwts.builder()
                .claim("userId", userId) // 사용자 ID 추가
                .claim("isDependentLogin", isDependentLogin) // 간편 로그인 여부 추가
                .setIssuedAt(new Date()) // 발행 시각
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs)) // 만료 시간
                .signWith(key, SignatureAlgorithm.HS256) // 서명
                .compact();
    }

    /*
     * 이메일 검증용 JWT "생성" 메서드
     *
     * @param username - 사용자이름
     * @return 이메일 검증을 위한 JWT 토큰
     * */
    public String generateEmailValidToken(String username) {
        return Jwts.builder()
                .claim("username", username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + (1000L * 60 * 5)))
                // : 현재 시간에 5분을 더해 토큰 만료 시간 설정
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /*
     * JWT에서 Bearer 접두사 제거
     *
     * @param bearerToken - 접두사가 포함된 JWT 문자열
     * @return Bearer이 제거된 JWT
     *
     * cf) Bearer: 소유자
     *       - 해당 토큰의 소유자에게 권한을 부여하다.
     * */
    public String removeBearer(String bearerToken) {
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid JWT token format");
        }
        return bearerToken.substring("Bearer ".length());
    }

    /*
     * JWT 검증 & 사용자 ID 추출
     *
     * @param token - JWT 토큰
     * @return 사용자 ID - 클레임에서 추출된 값
     * */
    public String getUserIdFromJwt(String token) {
        // JWT에서 클레임 정보를 추출 - claim 객체에 저장
        Claims claims = getClaims(token);

        // 클레임에서 userId 값을 문자열 형태로 반환
        return claims.get("userId", String.class);
    }

    /*
     * JWT 유효성 검증
     * @param token - JWT 토큰
     * @return 유효하면 true, 그렇지 않으면 false
     * */
    public boolean isValidToken(String token) {
        try {
            // 블랙리스트 확인
            if (tokenBlacklistService.isTokenBlacklisted(token)) {
                return false; // 블랙리스트에 있으면 무효
            }
            getClaims(token); // JWT 클레임을 가져오면서 유효성 검증
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /*
     * JWT 클레임 정보 가져오기
     *
     * @param token - JWT 토큰
     * @return 클레임 정보
     * */
    public Claims getClaims(String token) {
        JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(key) // JWT 파서에 서명에 사용된 비밀키 설정
                .build();

        // JWT를 파싱하여 클레임 정보(body)를 반환
        return jwtParser.parseClaimsJws(token).getBody();
    }

    public Boolean getIsDependentIdFromJwt(String token) {
        Claims claims = getClaims(token); // 토큰에서 클레임을 가져옵니다.
        return claims.get("isDependentLogin", Boolean.class); // 클레임에서 "isDependentId" 값을 추출합니다.
    }
}