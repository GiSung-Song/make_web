package make.web.config;

import lombok.RequiredArgsConstructor;
import make.web.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MemberService memberService;

    @Bean
    public PasswordEncoder passwordEncoder() { // 해시 함수 이용하여 비밀번호 암호화
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception { // HTTP 요청에 대한 보안 설정
        http.formLogin()
                .loginPage("/member/login") //로그인 페이지 url -> /member/login으로 로그인 url을 설정
                .defaultSuccessUrl("/") //로그인 성공 시 url -> /로 지정하여 홈으로 이동
                .usernameParameter("email") //로그인 시 사용할 파라미터를 email로 설정
                .failureUrl("/member/login/error") //로그인 실패 시 url -> /member/login/error 페이지로 이동
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout")) //로그아웃 url
                .logoutSuccessUrl("/") //로그아웃 성공 시 url
                .invalidateHttpSession(true).deleteCookies("JSESSIONID")
        ;
        http.sessionManagement()
                .maximumSessions(1) //최대 허용 가능 세션, -1 : 무제한 로그인 세션 허용
                .expiredUrl("/member/login") //세션이 만료된 경우 이동할 url
                .maxSessionsPreventsLogin(true) //true : 동시 로그인 차단, false : 기존 세션 만료(default)
                .and()
                .sessionFixation().changeSessionId() //새로운 sessionId 발급
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) //스프링 시큐리티가 필요 시 생성(기본값)
        ;

        http.authorizeRequests()
                .mvcMatchers("/", "/member/new/**", "/member/findId", "/item/**", "/images/**",
                        "/member/login/**", "/member/findPw", "/member/logout/**", "/message").permitAll() //홈 화면, 회원가입 화면은 모두 접근 가능
                .mvcMatchers("/item/new/**").hasRole("USER") //아이템 생성은 USER 권한을 가지고 있어야함.
                .mvcMatchers("/admin/**").hasRole("ADMIN") //admin 밑 페이지들은 ADMIN ROLE을 가지고 있어야 함
                .anyRequest().authenticated() //나머지 url은 모두 인증을 거쳐야 함.
        ;

        //인증되지 않은 사용자가 리소스에 접근했을 때 수행되는 핸들러
        http.exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
        ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberService)
                .passwordEncoder(passwordEncoder());
    }


    //static 디렉토리의 하위 파일은 인증을 무시하도록 설정
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**");
    }
}
