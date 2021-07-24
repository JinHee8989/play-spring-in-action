package tacos;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(HomeController.class)
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testHomePage() throws Exception{
        mockMvc.perform(get("/"))   //get방식 "/"를 수행
                .andExpect(status().isOk())     //http 200으로 오고
                .andExpect(view().name("home")) //home뷰가 오고
                .andExpect(content().string(
                        containsString("Welcome to...") //콘텐츠에 "Welcome to..."가 포함되어야 테스트 통과

                ));
    }
}
