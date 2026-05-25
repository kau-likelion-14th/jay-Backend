package likelion14th.lte.user.entity;

import jakarta.persistence.*;
import likelion14th.lte.Entity.BaseEntity;
import lombok.*;

@Entity
@Getter
// [Q1. @NoArgsConstructor는 매개변수가 없는 기본 생성자를 만듭니다.
// 그런데 왜 누구나 쓸 수 있게 PUBLIC으로 열어두지 않고, 굳이 PROTECTED로 막아두었을까요? (객체 생성의 안전성과 JPA 관점)]
// 답변: PUBLIC으로 열어두면 new User()처럼 아무값도 없는 불완전한 객체를 외부에서 마음대로 생성할 수 있어서 위험하다.
// 그렇다고 PRIVATE로 완전히 막으면 JPA가 DB에서 데이터를 꺼낼 때 내부적으로 기본 생성자를 사용하지 못 한다.
// 그래서 JPA는 접근할 수 있으면서 외부에서의 임의 생성은 막을 수 있는 PROTECTED로 설정한다.
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // [Q2. @Column(nullable = false) 어노테이션이 DB와 자바 코드 사이에서 하는 역할은 무엇인가요?]
    // 답변: @Column(nullable = false)는 두가지 역할을 한다. 첫번째로 DB레벨에서 해당 column에 NOT NULL
    // 제약을 걸어서 null 값이 DB에 저장되는 것을 막고, 두번째로는 DB에 쿼리를 보내기 전에 null인지 미리 감지해서
    // 예외를 던진다. 즉 DB와 자바 코드 양쪽에서 null을 방어하는 이중 방어 역할을 한다.
    @Column(nullable = false)
    private String username;

    @Column(length = 16, nullable = false, unique = true)
    private String userTag;

    @Column(columnDefinition = "TEXT")
    private String introduction;

    @Builder(access = AccessLevel.PUBLIC)
    private User (String username, String introduction, String userTag){
        this.username = username;
        this.userTag = userTag;
        this.introduction = introduction;
    }

    // [Q3. @Setter를 위 @Getter 처럼 사용하면 모든 맴버들에 setIntruduction() 같은 setter 메서드가 생성됩니다. 하지만 왜 @Setter를 쓰지않고 updateIntroduction() 이라는 명확한 메서드를 만든 객체지향적인 이유는 무엇인가요?]
    // 답변: @Setter를 사용하면 변경하면 안 되는 필드까지 모두 열려버린다. 하지만 updateIntroduction처럼 메서드를 만들면
    // 변경 범위를 introduction으로만 제한할 수 있다.
    public void updateIntroduction(String introduction){
        this.introduction = introduction;
    }
}