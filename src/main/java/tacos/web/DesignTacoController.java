package tacos.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import tacos.Ingredient;
import tacos.Ingredient.Type;
import tacos.Taco;

import javax.validation.Valid;

@Slf4j          //Logger 생성
@Controller
@RequestMapping("/design")
public class DesignTacoController {

    //주문 작성 페이지로 이동
    @GetMapping
    public String showDesignForm(Model model) {

        List<Ingredient> ingredients = Arrays.asList(
                new Ingredient("FLTO", "Flour Tortilla", Type.WRAP),
                new Ingredient("COTO", "Corn Tortilla", Type.WRAP),
                new Ingredient("GRBF", "Ground Beef", Type.PROTEIN),
                new Ingredient("CARN", "Carnitas", Type.PROTEIN),
                new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES),
                new Ingredient("LETC", "Lettuce", Type.VEGGIES),
                new Ingredient("CHED", "Cheddar Tortilla", Type.CHEESE),
                new Ingredient("JACK", "Monterrey Jack", Type.CHEESE),
                new Ingredient("SLSA", "Salsa", Type.SAUCE),
                new Ingredient("SRCR", "Sour Cream", Type.SAUCE)
        );

        Type[] types = Ingredient.Type.values();
        for (Type type : types){
            model.addAttribute(type.toString().toLowerCase(),
                    filterByType(ingredients, type));
        }

        model.addAttribute("taco", new Taco());
        return "design";
    }


    //주문서 제출
    @PostMapping
    public String processDesign(@Valid Taco design, Errors errors){ //@Valid애노테이션으로 Taco객체의 유효성을 검증하라고 스프링 MVC에 알려줌,
                                                                    //유효성검증중에 에러가 발생하면 Errors객체에 저장되어 processDesign()으로 전달됨.
        if(errors.hasErrors()){     //에러가 있는경우 "design"뷰로 이동
            return "design";
        }
        //타코 디자인(선택된 식자재 내역)을 저장
        return "redirect:/orders/current";
    }




    private List<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {
        return ingredients.stream().filter(x->x.getType().equals(type)).collect(Collectors.toList());
    }


}
