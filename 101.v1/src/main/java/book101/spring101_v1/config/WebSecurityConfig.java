package book101.spring101_v1.config;

import book101.spring101_v1.security.JwtAuthenticationFilter;
import book101.spring101_v1.security.OAuthSuccessHandler;
import book101.spring101_v1.security.OAuthUserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final OAuthUserServiceImpl oAuthUserService;
    private final OAuthSuccessHandler oAuthSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.cors()
                .and()
                .csrf()
                    .disable()
                .httpBasic()
                    .disable()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                    /**
                     스프링부트 2.6버전부터 responsebody를 비우고 status만 반환함
                     responseBody에 error 메시지를 포함하려면 "/error" 추가
                     https://github.com/spring-projects/spring-boot/issues/28953
                     */
                    .antMatchers("/","/auth/**","/oauth2/**","/error").permitAll()
                .anyRequest()
                    .authenticated()
                .and()
                .oauth2Login()
                        .redirectionEndpoint()
                .baseUri("/oauth2/callback/*")
                .and()
                        .userInfoEndpoint()
                                .userService(oAuthUserService)
                .and()
                        .successHandler(oAuthSuccessHandler);

        //cors필터 이후 실행 설정
        http.addFilterAfter(jwtAuthenticationFilter, CorsFilter.class);
        return http.build();
    }
}
