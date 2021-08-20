package tacos.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class securityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    @Override   //HTTP보안을 구성하는 메소드
    protected void configure(HttpSecurity http) throws Exception {
        // 1. 요청 경로가 보안 처리되는 방법을 정의하는 구성 메서드로 정의
//        http.authorizeRequests()
//                .antMatchers("/design","/orders")
//                .hasRole("ROLE_USER")
//                .antMatchers("/","/**").permitAll();

        // 2. 스프링 표현식을 사용한 인증 규칙 정의
        http.authorizeRequests()
                .antMatchers("/design","/orders")
                .access("hasRole('ROLE_USER')")
                .antMatchers("/","/**").permitAll();

        // 예시 - 화요일의 타코생성은 ROLE_USER권한을 갖는 사용자에게만 허용하고 싶다면
//        http.authorizeRequests()
//                .antMatchers("/design","/orders")
//                .access("hasRole('ROLE_USER') && T(java.util.Calendar).getInstance().get(T(java.util.Calendar).DAY_OF_WEEK) == T(java.util.Calendar).TUESDAY")
//                .antMatchers("/","/**").permitAll();
    }

    @Override   //사용자 인증정보를 구성하는 메소드
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        //1.인메모리 사용자 스토어를 구현(테스트 목적으로 사용)
//        auth.inMemoryAuthentication()
//                .withUser("user1")
//                .password("{noop}password1")
//                .authorities("ROLE_USER")
//                .and()
//                .withUser("user2")
//                .password("{noop}password2")
//                .authorities("ROLE_USER");
//
//        //2.JDBC기반 사용자 스토어
//        auth.jdbcAuthentication()
//                .dataSource(dataSource)
//                .usersByUsernameQuery(
//                        "select username,password,enabled from users where username=?")
//                .authoritiesByUsernameQuery(
//                        "select username,password,authority from authorities where username=?")
//                .passwordEncoder(new BCryptPasswordEncoder());  //비밀번호 암호화 메소드
//
//        //3.LDAP기반 사용자 스토어
//        auth.ldapAuthentication()
//                .userSearchBase("ou=people")    //사용자를 찾기위한 기준점 쿼리를 제공
//                .userSearchFilter("(uid={0})")
//                .groupSearchBase("ou=groups")   //그룹을 찾기위한 기준점 쿼리를 제공
//                .groupSearchFilter("member={0}")
//                .passwordCompare() //비밀번호를 비교하는 방법으로 ldap인증
//                .passwordEncoder(new BCryptPasswordEncoder())
//                .passwordAttribute("userPasscode"); //비밀번호 속성의 이름 지정
////                .contextSource().url("ldap://tacocloud.com:389/dataSourcec=tacocloud,dc=com"); //ldap서버의 위치를 지정
        //4.사용자 인증의 커스터마이징
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(encoder());


    }
}
