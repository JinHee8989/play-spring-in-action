package tacos.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {    //사용자 입력을 처리하지 않는 간단한 컨트롤러의 경우 뷰 컨트롤러에 정의할 수 있음.
                                                        //어떤 클래스건 WebMvcConfigurer를 상속받으면 addViewControllers()메소드 오버라이딩 가능

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {   //이 메소드로 HomeController의 역할을 대신할 수 있음
        registry.addViewController("/").setViewName("home");
        registry.addViewController("/login");
    }
}
