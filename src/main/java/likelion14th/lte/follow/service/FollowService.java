package likelion14th.lte.follow.service;


import org.springframework.transaction.annotation.Transactional;
import likelion14th.lte.follow.dto.FollowUserResponse;
import likelion14th.lte.follow.dto.UserNameDto;
import likelion14th.lte.follow.entity.Follow;
import likelion14th.lte.follow.repository.FollowRepository;
import likelion14th.lte.global.api.ErrorCode;
import likelion14th.lte.global.exception.GeneralException;
import likelion14th.lte.user.entity.User;
import likelion14th.lte.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    // 전성환#CODA, 전성환, CODA
    private static UserNameDto nameParsing(String name) {
        if(!name.contains("#")) {
            return new UserNameDto(name, null);
        }
        String[] parts = name.split("#", 2); // [전성환, CODA]
        if(parts[1].length() != 8) {
            throw new GeneralException(ErrorCode.INVALID_HANDLE_FORMAT);
        }
        return new UserNameDto(parts[0], parts[1]);
    }

    @Transactional
    public FollowUserResponse followUser(Long fromUserId, Long toUserId) {
        User fromUser = userRepository.findById(fromUserId)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));

        User toUser = userRepository.findById(toUserId)
                .orElseThrow(() -> new GeneralException(ErrorCode.FOLLOW_TARGET_NOT_FOUND));

        if(fromUser.getId().equals(toUser.getId())) {
            throw new GeneralException(ErrorCode.FOLLOW_SELF_NOT_ALLOWED);
        }

        if(followRepository.existsByFromUserAndToUser(fromUser, toUser)) {
            throw new GeneralException(ErrorCode.FOLLOW_ALREADY_EXISTS);
        }

        Follow follow = Follow.builder()
                .toUser(toUser)
                .fromUser(fromUser)
                .build();

        followRepository.save(follow);

        return FollowUserResponse.from(follow.getToUser());
    }

    @Transactional(readOnly = true)
    public Page<FollowUserResponse> searchCanFollowers(Long userId, String targetName, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));

        UserNameDto nameDto = nameParsing(targetName);
        Page<User> users;
        if(nameDto.userTag() != null && !nameDto.userTag().isEmpty()) {
            User target = userRepository.findByUserTag(nameDto.userTag())
                    .orElse(null);

            if(target == null) {
                return Page.empty(pageable);
            }

            users = new PageImpl<>(List.of(target),pageable, 1);
        } else {
            users = userRepository.findByUsernameContainingIgnoreCase(nameDto.userName(), pageable);
        }
        // 페이지 [유저1:전성환, 유저2:전유안] -> 하나씩 끄집어오면
        List<User> canFollowUsers = users.getContent().stream()
                .filter(target -> !target.getId().equals(userId))
                .filter(target -> !followRepository.existsByFromUserAndToUser(user, target))
                .toList();

        // 리스트 페이지 같이 여러개 담는거 .map
        return new PageImpl<>(canFollowUsers, pageable, canFollowUsers.size()).map(FollowUserResponse::from);
    }

    @Transactional
    public void unfollowUser(Long fromUserId, Long toUserId) {
        User fromUser = userRepository.findById(fromUserId)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));

        User toUser = userRepository.findById(toUserId)
                .orElseThrow(() -> new GeneralException(ErrorCode.FOLLOW_TARGET_NOT_FOUND));

        if (fromUser.getId().equals(toUser.getId())) {
            throw new GeneralException(ErrorCode.FOLLOW_SELF_NOT_ALLOWED);
        }

        Follow follow = followRepository.findByFromUserAndToUser(fromUser, toUser)
                .orElseThrow(() -> new GeneralException(ErrorCode.FOLLOW_NOT_FOUND));

        followRepository.delete(follow);
    }

    @Transactional(readOnly = true)
    public List<FollowUserResponse> getFollowers(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));

        List<Follow> follows = followRepository.findByToUser(user);

        return follows.stream()
                .map(follow -> FollowUserResponse.from(follow.getFromUser()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<FollowUserResponse> getFollowings(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));

        List<Follow> follows = followRepository.findByFromUser(user);

        return follows.stream()
                .map(follow -> FollowUserResponse.from(follow.getToUser()))
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<FollowUserResponse> getCanFollowUsers(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));

        Page<User> users = userRepository.findCanFollowUsers(userId, pageable);

        return users.map(FollowUserResponse::from);
    }

}
