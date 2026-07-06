package likelion14th.lte.user.dto.response;

import likelion14th.lte.user.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfileResponse {
    private String username;
    private String profileImageUrl;
    private String introduction;

    // [Q4. Controller가 DB에서 꺼낸 원본 Entity(User)를 클라이언트 화면에 그대로 반환하지 않고,
    // 굳이 from() 메서드를 통해 한번 DTO로 변환해서 내보내는 핵심적인 이유 2가지는 무엇인가요?]
    // 답변: 첫번째 이유는 보안 때문이다. Entity를 화면에 그대로 반환하면 불필요한 정보까지 노출될 수 있어 보안이 약해진다.
    // 두번째 이유는 API 스펙 보호 때문이다. DB 테이블에 column이 추가되거나 이름이 바뀌어도, DTO에서 매핑하는 방식만 수정하면 프론트엔드에는 아무런 영향이 가지 않는다.

    public static UserProfileResponse from (User user) {
        return new UserProfileResponse(  // 순서중요
                user.getUsername() + "#" + user.getUserTag(),
                user.getProfileImage(),
                user.getIntroduction()
        );
    }
}
