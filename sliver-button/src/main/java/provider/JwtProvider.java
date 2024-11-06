package provider;

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


@Component
public class JwtProvider {
    private final Key key;

    @Value("${jwt.expiration}")
    private int jwtExpirationMs;


    public int getExpiration() {
        return jwtExpirationMs;
    }

    public JwtProvider(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") int jwtExpirationMs) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.jwtExpirationMs = jwtExpirationMs;
    }
    public String generateJwtToken(String userId) {
        return Jwts.builder()
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

    }

    public String generateEmailValidToken(String username) {
        return Jwts.builder()
                .claim("username", username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + (1000L * 60 * 5)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String removeBearer(String bearerToken) {
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid JWT token format");
        }
        return bearerToken.substring("Bearer ".length());
    }
    public String getUserIdFromJwt(String token) {
        Claims claims = getClaims(token);

        return claims.get("userId", String.class);
    }

    public boolean isValidToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 실버버튼에 아주 중요한 로직
    public Claims getClaims(String token) {
        JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(key) // JWT 파서에 서명에 사용된 비밀키 설정
                .build();
        return jwtParser.parseClaimsJws(token).getBody();
    }


}
