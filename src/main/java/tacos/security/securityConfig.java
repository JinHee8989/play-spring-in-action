package tacos.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.w3c.dom.CDATASection;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class securityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Override   //HTTP보안을 구성하는 메소드
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/design","/orders")
                    .access("hasRole('ROLE_USER')")
                .antMatchers("/","/**")
                    .access("permitAll()")
                .and()
                     .httpBasic();
    }

    @Override   //사용자 인증정보를 구성하는 메소드
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //1.인메모리 사용자 스토어를 구현(테스트 목적으로 사용)
        auth.inMemoryAuthentication()
                .withUser("user1")
                .password("{noop}password1")
                .authorities("ROLE_USER")
                .and()
                .withUser("user2")
                .password("{noop}password2")
                .authorities("ROLE_USER");

        //2.JDBC기반 사용자 스토어
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery(
                        "select username,password,enabled from users where username=?")
                .authoritiesByUsernameQuery(
                        "select username,password,authority from authorities where username=?")
                .passwordEncoder(new BCryptPasswordEncoder());  //비밀번호 암호화 메소드

        //3.LDAP기반 사용자 스토어
    }
}
