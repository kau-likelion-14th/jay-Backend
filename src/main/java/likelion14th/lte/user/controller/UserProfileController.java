package likelion14th.lte.user.controller;


import io.swagger.v3.oas.annotations.Operation;
import likelion14th.lte.global.api.ApiResponse;
import likelion14th.lte.global.api.SuccessCode;
import likelion14th.lte.user.dto.request.CreateTestUserRequest;
import likelion14th.lte.user.dto.request.UserIntroRequest;
import likelion14th.lte.user.dto.response.UserProfileResponse;
import likelion14th.lte.user.service.UserProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequestMapping("/api/profile")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfileController {
    public final UserProfileService userProfileService;

    @GetMapping
    @Operation(summary = "유저 프로필 조회", description = "유저아이디를 받아 유저 프로필을 반환하는 api")
    public ApiResponse<UserProfileResponse> getUserProfile(
            @AuthenticationPrincipal Jwt jwt){
        Long userId = Long.valueOf(jwt.getSubject());

        UserProfileResponse userProfileResponse = userProfileService.getUserProfile(userId);

        return ApiResponse.onSuccess(SuccessCode.OK, userProfileResponse);
    }

    @PostMapping
    @Operation(summary = "테스트 유저 생성", description = "이름, 한줄소개, 유저 태그")
    public ApiResponse<UserProfileResponse> createTestUserProfile(

            // [Q10. 클라이언트가 보낸 JSON 텍스트 데이터가 어떻게 자바 객체인 CreateTestUserRequest로
            // 변환 되는지 앞의 어노테이션과 연관 지어 설명해 보세요.]
            // 답변: @PostMapping은 클라이언트의 HTTP POST 요청을 받아 알맞은 자바 메서드에 매핑한다.
            // RequsetBody는 네트워크를 타고 들어온 JSON 텍스트를 스프링의 Jackson 라이브러리가 CreateTestUserRequest
            // 자바 객체로 변환해준다.

            @RequestBody CreateTestUserRequest createTestUserRequest
    ){
        UserProfileResponse response = userProfileService.createTestUser(createTestUserRequest);

        return ApiResponse.onSuccess(SuccessCode.CREATED, response);
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "유저 프로필 추가 및 수정", description = "유저 프로필 이미지를 추가하거나 수정합니다.")
    public ApiResponse<UserProfileResponse> putUserProfile(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam("image") MultipartFile file
    ) {
        Long userId = Long.valueOf(jwt.getSubject());

        UserProfileResponse response = userProfileService.putProfileImage(userId, file);

        return ApiResponse.onSuccess(SuccessCode.PROFILE_PUT_SUCCESS, response);
    }

    @DeleteMapping
    @Operation(summary = "유저 프로필 이미지 삭제", description = "유저 프로필 이미지를 삭제합니다.")
    public ApiResponse<UserProfileResponse> deleteUserProfile(@AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.valueOf(jwt.getSubject());

        UserProfileResponse response = userProfileService.deleteProfileImage(userId);

        return ApiResponse.onSuccess(SuccessCode.PROFILE_DELETE_SUCCESS, response);

    }

    @GetMapping("/touser")
    @Operation(summary = "다른 유저 프로필 조회", description = "toUserId에 해당하는 유저의 프로필을 조회합니다.")
    public ApiResponse<UserProfileResponse> getOtherUserProfile(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam Long toUserId
    ) {
        UserProfileResponse response = userProfileService.getOtherUserProfile(toUserId);

        return ApiResponse.onSuccess(SuccessCode.USER_INFO_GET_SUCCESS, response);
    }

    @PutMapping("/intro")
    @Operation(summary = "유저 한줄 소개 수정", description = "로그인한 유저의 한줄 소개를 수정합니다.")
    public ApiResponse<UserProfileResponse> updateIntroduction(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody UserIntroRequest request
    ) {
        Long userId = Long.valueOf(jwt.getSubject());

        UserProfileResponse response =
                userProfileService.updateIntroduction(userId, request.getIntroduce());

        return ApiResponse.onSuccess(SuccessCode.USER_PROFILE_UPDATE_SUCCESS, response);
    }
}
