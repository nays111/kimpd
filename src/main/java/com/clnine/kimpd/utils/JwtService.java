package com.clnine.kimpd.utils;
import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.secret.Secret;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import static com.clnine.kimpd.config.BaseResponseStatus.*;
@Service
public class JwtService {
    /**
     * JWT 생성
     * @param userIdx
     * @return String
     */
    public String createJwt(int userIdx) {
        Date now = new Date();
        return Jwts.builder()
                .claim("userIdx", userIdx)
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256, Secret.JWT_SECRET_KEY)
                .compact();
    }
    /**
     * WebAdmin JWT 생성
     * @param adminIdx
     * @return String
     */
    public String createWebAdminJwt(int adminIdx) {
        Date now = new Date();
        return Jwts.builder()
                .claim("adminIdx", adminIdx)
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256, Secret.JWT_SECRET_KEY)
                .compact();
    }
    /**
     * Header에서 X-ACCESS-TOKEN 으로 JWT 추출
     * @return String
     */
    public String getJwt() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader("X-ACCESS-TOKEN");
    }
    /**
     * JWT에서 userIdx 추출
     * @return int
     * @throws BaseException
     */
    public int getUserIdx() throws BaseException {
        // 1. JWT 추출
        String accessToken = getJwt();
        if (accessToken == null || accessToken.length() == 0) {
            throw new BaseException(EMPTY_JWT);
        }
        // 2. JWT parsing
        Jws<Claims> claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(Secret.JWT_SECRET_KEY)
                    .parseClaimsJws(accessToken);
        } catch (Exception ignored) {
            throw new BaseException(INVALID_JWT);
        }
        // 3. userIdx 추출
        return claims.getBody().get("userIdx", Integer.class);
    }
    /**
     * JWT에서 adminIdx 추출
     * @return int
     * @throws BaseException
     */
    public int getAdminIdx() throws BaseException {
        // 1. JWT 추출
        String accessToken = getJwt();
        if (accessToken == null || accessToken.length() == 0) {
            throw new BaseException(EMPTY_JWT);
        }
        // 2. JWT parsing
        Jws<Claims> claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(Secret.JWT_SECRET_KEY)
                    .parseClaimsJws(accessToken);
        } catch (Exception ignored) {
            throw new BaseException(INVALID_JWT);
        }
        // 3. userIdx 추출
        return claims.getBody().get("adminIdx", Integer.class);
    }
}