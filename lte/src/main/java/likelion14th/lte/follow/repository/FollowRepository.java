package likelion14th.lte.follow.repository;

import likelion14th.lte.follow.entity.Follow;
import likelion14th.lte.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    boolean existsByFromUserAndToUser(User fromUser, User toUser);

    // Spring Data JPA가 메서드 이름을 분석해서 쿼리를 자동으로 만들어줌
    Optional<Follow> findByFromUserAndToUser(User fromUser, User toUser);

    List<Follow> findByToUser(User toUser);

    List<Follow> findByFromUser(User fromUser);
}
