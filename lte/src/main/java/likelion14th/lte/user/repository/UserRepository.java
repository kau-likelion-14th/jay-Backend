package likelion14th.lte.user.repository;

import likelion14th.lte.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// [추가문제] (필수 X) 이 코드는 인터페이스일 뿐이고 구현체(implements) 클래스가 없습니다.
// 그런데 어떻게 프로그램 실행 시 DB와 통신하는 객체로 동작할 수 있나요?
// 답변: UserRepository가 JapRepository를 상속받으면 Spring Data JAP가 앱 실행 시 자동으로
// 구현체를 만들어준다. 따라서 개발자가 직접 구현 클래스를 작성하지 않아도 DB와 통신하는 객체로 동작할 수 있다.

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);
}
