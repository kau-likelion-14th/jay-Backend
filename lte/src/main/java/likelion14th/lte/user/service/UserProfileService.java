package likelion14th.lte.user.service;


import likelion14th.lte.global.api.ErrorCode;
import likelion14th.lte.global.exception.GeneralException;
import likelion14th.lte.user.dto.request.CreateTestUserRequest;
import likelion14th.lte.user.dto.response.UserProfileResponse;
import likelion14th.lte.user.entity.User;
import likelion14th.lte.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfileService {
    // [Q5. Service 안에서 new UserRepository() 로 객체를 직접 생성하지 않고,
    // 외부에서 의존성 주입(DI)을 받는 이유는 무엇인가요? (결합도와 단위 테스트 관점)]
    // 답변: new UserRepository()로 직접 생성하면 UserProfileService가 UserRepository에 강하게 결합되어서
    // DB를 교체할 때 코드 곳곳을 수정해야한다. 또한 테스트 시 진짜 DB에 연결되어야 하므로 테스트 시 불편하다.
    // 외부에서 주입받으면 가짜 Repository로 교체가 가능해서 DB 없이도 테스트 할 수 있다.
    private final UserRepository userRepository;

    // [Q6. (코딩 문제) 만약 클래스 위의 @RequiredArgsConstructor를 지운다면,
    // 우리가 직접 작성해야 할 의존성 주입용 자바 '생성자' 코드는 어떤 모습일까요? 아래에 직접 코딩해 보세요.]
    /*
       public UserProfileService(UserRepository userRepository) {
            this.userRepository = userRepository;
       }
    */

    @Transactional
    public UserProfileResponse createTestUser(CreateTestUserRequest request) {
        // [Q7. 일반적인 생성자 new User(name, intro, tag) 방식을 쓰지 않고,
        // User.builder()...build() 라는 '빌더 패턴'을 사용하여 객체를 조립했을 때 얻는 장점은 무엇인가요?]
        // 답변: 일반 생성자는 매개변수 순서를 실수로 바꿔도 에러가 나지 않아 버그가 생길 수 있다. 빌더 패턴은
        // 어느 필드에 어떤 값이 들어가있는지 명확하게 보이고, 순서에 상관없이 필요한 필드만 선택해서 넣을 수 있다는 장점이 있다.

        User newUser = User.builder()
                .username(request.getUsername())
                .userTag(request.getUserTag())
                .introduction(request.getIntroduction())
                .build();
        User savedUser;
        try{
            // [Q8. 데이터를 저장하는 이 메서드 위에 @Transactional이 반드시 붙어야 하는 이유는 무엇인가요?
            // (저장 도중 DB 서버가 끊겼을 때의 상황을 가정해서 설명하세요)]
            // 답변: 저장 도중 DB 서버가 끊기면 일부 데이터만 저장된 불안전한 상태가 될 수 있는데 @Transactional을 붙이면
            // 작업 도중 오류가 발생했을 때 모든 데이터 변경 사항을 작업 시작 전으로 되돌리기 때문에 DB가 완전한 상태를 유지하도록 보장한다.
            savedUser = userRepository.save(newUser);
        }
        catch(Exception e) {
            throw new GeneralException(ErrorCode.BAD_REQUEST);
        }
        return UserProfileResponse.from(savedUser);
    }

    @Transactional(readOnly = true)
    public UserProfileResponse getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new GeneralException(ErrorCode.USER_NOT_FOUND));

        return UserProfileResponse.from(user);
    }
}
