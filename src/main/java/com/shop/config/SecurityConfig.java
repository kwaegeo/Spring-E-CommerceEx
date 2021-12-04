package com.shop.config;


import com.shop.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    MemberService memberService;

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.formLogin()
                .loginPage("/members/login")  //로그인 페이지 url을 설정
                .defaultSuccessUrl("/") //로그인 성공 시 이동할 url
                .usernameParameter("email") //로그인 시 사용할 파라미터 이름으로 email을 지정
                .failureUrl("/members/login/error")  //로그인 실패 시 이동할 url
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout")) //로그아웃 url
                .logoutSuccessUrl("/"); //로그아웃 성공 시 이동할 url

                http.authorizeRequests()
                        .mvcMatchers("/","/members/**",
                                "/item/**", "/images/**").permitAll() //모든 사용자가 인증 없이 해당 경로에 접근할 수 있도록 설정
                        .mvcMatchers("/admin/**").hasRole("ADMIN") //admin이라는 경로는 계정이 ADMIN role인 경우에만 접근 가능하도록 설정
                        .anyRequest().authenticated(); //위에 2경로를 제외한 경로는 모두 인증을 요구한다.

                http.exceptionHandling()
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint()); //인증되지 않은 사용자가 리소스에 접근 시 수행되는 핸들러 등록
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(); //비밀번호를 암호화하여 저장하는 메서드 (해시함수를 이용하여 비밀번호를 암호화하여 저장한다. 빈으로 등록해 사용)
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(memberService)
                .passwordEncoder(passwordEncoder());
    } //Spring Security에서 인증은 저 매니저를 통해 이루어지는데 AuthenticationManagerBuilder가 매니저를 생성하며,
      //userDetailserveice를 구현하고 있는 객체로 memberService를 지정해주며, 비밀번호암호화를 위해 encoder지정

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**");
    }
}
