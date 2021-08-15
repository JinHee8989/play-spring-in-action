package tacos;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@RequiredArgsConstructor
@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true) //JPA에서는 인자없는 생성자를 가져야함(여기서는 인자없는 생성자를 원치않으므로 access속성을 클래스 외부에서 사용하지 못하도록 private로 설정,
                                                            // 초기화가 필요한 final 속성이 있으므로 force속성은 true로 설정)
@Entity
public class Ingredient {

    @Id
    private final String id;
    private final String name;
    private final Type type;

    public static enum Type{
        WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
    }
}
