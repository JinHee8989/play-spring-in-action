package tacos.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
//@ConfigurationProperties(prefix="taco.orders") //이 애노테이션에 지정된 접두어는 taco.orders. pageSize 구성속성값을 변경하려면 taco.orders.pageSize라는 이름을 사용함
public class OrderController {

    private OrderRepository orderRepo;
    private OrderProps props; //이렇게 구성속성 홀더를 정의하면 여러 빈에 공통적인 구성속성을 쉽게 공유할 수 있음

    public OrderController(OrderRepository orderRepo, OrderProps orderProps){

        this.orderRepo = orderRepo;
        this.props = orderProps;
    }

//    private int pageSize = 20;
//    public void setPageSize(int pageSize){
//        this.pageSize = pageSize;
//    }





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


    @GetMapping
    public String orderForUser(@AuthenticationPrincipal User user, Model model){
//        Pageable pageable = PageRequest.of(0,pageSize); //첫번째 페이지, pageSize개씩 라는 의미
        Pageable pageable = PageRequest.of(0,props.getPageSize());
        model.addAttribute("orders",orderRepo.findByUserOrderByPlacedAtDesc(user,pageable)); //최근 20개 주문내역만 보고싶을 경우

        return "orderList" ;
    }

}
