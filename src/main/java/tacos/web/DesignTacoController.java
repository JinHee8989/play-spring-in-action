package tacos.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import tacos.Ingredient;
import tacos.Ingredient.Type;
import tacos.Order;
import tacos.Taco;
import tacos.User;
import tacos.data.IngredientRepository;
import tacos.data.TacoRepository;
import tacos.data.UserRepository;

import javax.validation.Valid;

@Slf4j          //Logger 생성
@Controller
@RequiredArgsConstructor
@RequestMapping("/design")
@SessionAttributes("order") //주문은 다수의 http요청에 걸쳐 존재해야하기때문에 세션에서 보존
public class DesignTacoController {

    private final IngredientRepository ingredientRepo;
    private TacoRepository tacoRepo;
    private UserRepository userRepo;

    @Autowired
    public DesignTacoController(IngredientRepository ingredientRepo, TacoRepository tacoRepo, UserRepository userRepo){
        this.ingredientRepo = ingredientRepo;
        this.tacoRepo = tacoRepo;
        this.userRepo = userRepo;
    }

    @ModelAttribute(name="order")
    public Order order(){
        return new Order();
    }

    @ModelAttribute(name="taco")
    public Taco taco(){
        return new Taco();
    }

    //주문 작성 페이지로 이동
    @GetMapping
    public String showDesignForm(Model model, Principal principal) {

//        List<Ingredient> ingredients = Arrays.asList(
//                new Ingredient("FLTO", "Flour Tortilla", Type.WRAP),
//                new Ingredient("COTO", "Corn Tortilla", Type.WRAP),
//                new Ingredient("GRBF", "Ground Beef", Type.PROTEIN),
//                new Ingredient("CARN", "Carnitas", Type.PROTEIN),
//                new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES),
//                new Ingredient("LETC", "Lettuce", Type.VEGGIES),
//                new Ingredient("CHED", "Cheddar Tortilla", Type.CHEESE),
//                new Ingredient("JACK", "Monterrey Jack", Type.CHEESE),
//                new Ingredient("SLSA", "Salsa", Type.SAUCE),
//                new Ingredient("SRCR", "Sour Cream", Type.SAUCE)
//        );

        List<Ingredient> ingredients = new ArrayList<>();
        ingredientRepo.findAll().forEach(i->ingredients.add(i));

        Type[] types = Ingredient.Type.values();
        for (Type type : types){
            model.addAttribute(type.toString().toLowerCase(),
                    filterByType(ingredients, type));
        }

//        model.addAttribute("taco", new Taco());

        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        model.addAttribute("user",user);

        return "design";
    }


    //주문서 제출
    @PostMapping
    public String processDesign(@Valid Taco design, Errors errors, @ModelAttribute Order order){ //@Valid애노테이션으로 Taco객체의 유효성을 검증하라고 스프링 MVC에 알려줌,
                                                                    //유효성검증중에 에러가 발생하면 Errors객체에 저장되어 processDesign()으로 전달됨.
                                                                    //@ModelAttribute Order order 해준이유는 매개변수의 값이 모델로부터 전달받아야하고 스프링MVC가 이 매개변수에 요청 매개변수를 바인딩히지 않도록 하기위해

        if(errors.hasErrors()){     //에러가 있는경우 "design"뷰로 이동
            return "design";
        }

        Taco saved = tacoRepo.save(design);
        order.addDesign(saved);

        //타코 디자인(선택된 식자재 내역)을 저장
        return "redirect:/orders/current";
    }




    private List<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {
        return ingredients.stream().filter(x->x.getType().equals(type)).collect(Collectors.toList());
    }


}
