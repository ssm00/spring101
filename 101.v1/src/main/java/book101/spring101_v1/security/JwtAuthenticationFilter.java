package book101.spring101_v1.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            //요청에서 토큰 가져오기
            String token = parseBearerToken(request);
            log.info("Filter is running");

            //토큰 검사하기 jwt는 인가서버에 요청하지 않고도 검증 가능.
            if (token != null && !token.equalsIgnoreCase("null")) {
                //일치하지 않으면 exception터짐
                String userId = tokenProvider.validateAndGetUserId(token);
                log.info("authenticated id : " + userId);

                //securityHolder에 등록할 authentication생성
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId, null, AuthorityUtils.NO_AUTHORITIES);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                //securityContextHolder에 등록하기 쓰레드로컬에 등록 (각 스레드별 저장)
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                securityContext.setAuthentication(authentication);
                SecurityContextHolder.setContext(securityContext);
            }
        } catch (Exception e) {
            logger.error("securityHolder에 user등록 실패", e);
        }
        filterChain.doFilter(request, response);
    }

    private String parseBearerToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
