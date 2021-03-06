package tacos;

import lombok.Data;
import org.hibernate.validator.constraints.CreditCardNumber;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name="Taco_Order")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message="Name is required")
    private String deliveryName;

    @NotBlank(message="Street is required")
    private String deliveryStreet;

    @NotBlank(message="City is required")
    private String deliveryCity;

    @NotBlank(message = "State is required")
    private String deliveryState;

    @NotBlank(message="Zip code is required")
    private String deliveryZip;

    @CreditCardNumber(message="Not a valid credit card number") //이 애노테이션은 속상의 값이 LUHN알고리즘검사에 합격한 유효한 신용카드 번호인지 검증
    private String ccNumber;                                    //룬알고리즘검사는 사용자의 입력실수나 악성 데이터를 방지해주지만 실제로 존재하는 번호인지, 대금 지불에 사용될수 있는 번호인지까지는 알수없음

    @Pattern(regexp="^(0[1-9]|1[0-2])([\\/])([1-9][0-9])$" ,message="Must be formatted MM/YY")  //패턴에 정규표현식을 적용해 유효성 검증
    private String ccExpiration;

    @Digits(integer=3, fraction=0, message="Invalid CVV")   //@Digits으로 입력값이 지정된 정수와 소수자리수보다 적을경우 통과 가능
    private String ccCVV;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date placedAt;

    @ManyToOne      //한 명의 사용자는 여러 주문을 가질 수 있도록 셋팅
    private User user;

    @ManyToMany(targetEntity = Taco.class)
    private List<Taco> tacos = new ArrayList<>();

    public void addDesign(Taco design) {
        this.tacos.add(design);
    }

    @PrePersist
    void placedAt(){
        this.placedAt=new Date();
    }
}
