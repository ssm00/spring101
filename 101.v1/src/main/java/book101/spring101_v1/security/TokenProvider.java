package book101.spring101_v1.security;

import book101.spring101_v1.model.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@Slf4j
public class TokenProvider {

    private static final String SECRET_KEY = " c3ByaW5nLWJvb3Qtc2VjdXJpdHktand0LXR1dG9yaWFsLWppd29vbi1zcHJpbmctYm9vdC1zZWN1cml0eS1qd3QtdHV0b3JpYWwK";
    private final Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));


    public String create(UserEntity userEntity) {
        Date expiryDate = Date.from(
                Instant.now()
                        .plus(1, ChronoUnit.DAYS)
        );

        //Token 생성
        return Jwts.builder()
                //header
                .signWith(key, SignatureAlgorithm.HS512)
                //payload
                .setSubject(userEntity.getId())
                .setIssuer("demo app")
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .compact();
    }

    public String validateAndGetUserId(String token) {
        log.info("토큰: "+token);
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

        return claims.getSubject();
    }


}
