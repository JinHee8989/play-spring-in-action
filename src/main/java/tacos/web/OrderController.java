package tacos.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import tacos.Order;
import tacos.User;
import tacos.data.OrderRepository;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/orders")
@SessionAttributes("order")
public class OrderController {

    private OrderRepository orderRepo;

    public OrderController(OrderRepository orderRepo){
        this.orderRepo = orderRepo;
    }

    @GetMapping("/current")
    public String OrderForm(@AuthenticationPrincipal User user, @ModelAttribute Order order){   //해당 사용자의 이름과 주소가 채워진 상태로 주문폼으로 전송됨
//        model.addAttribute("order",new Order());

        if(order.getDeliveryName() == null){
            order.setDeliveryName(user.getFullname());
        }

        if(order.getDeliveryStreet() == null){
            order.setDeliveryStreet(user.getStreet());
        }

        if(order.getDeliveryCity() == null){
            order.setDeliveryCity(user.getCity());
        }

        if(order.getDeliveryState() == null){
            order.setDeliveryState(user.getState());
        }

        if(order.getDeliveryZip() == null){
            order.setDeliveryZip(user.getZip());
        }
        return "orderForm";
    }

//    @PostMapping
//    public String OrderForm(@Valid Order order, Errors errors){
//
//        if(errors.hasErrors()){
//            return "orderForm";
//        }
//
//        log.info("Order submitted: "+order);
//        return "redirect:/";
//    }


    @PostMapping
    public String processOrder(@Valid Order order, Errors errors, SessionStatus sessionStatus, @AuthenticationPrincipal User user){
        //@AuthenticationPrincipal의 장점 : 타입 변환이 필요없고 Authentication과 동일하게 보안 특정 코드만 가짐.
        if(errors.hasErrors()){
            return "orderForm";
        }

        order.setUser(user);

        orderRepo.save(order);
        sessionStatus.setComplete();
        return "redirect:/";
    }



}
