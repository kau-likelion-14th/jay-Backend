package likelion14th.lte.statistic.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import likelion14th.lte.global.api.ErrorCode;
import likelion14th.lte.global.exception.GeneralException;
import likelion14th.lte.statistic.dto.response.StatisticResponse;
import likelion14th.lte.statistic.entity.StatWeek;
import likelion14th.lte.statistic.entity.Statistic;
import likelion14th.lte.todo.repository.TodoDateRepository;
import likelion14th.lte.user.entity.User;
import likelion14th.lte.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class StatisticService {

    private final UserRepository userRepository;
    private final TodoDateRepository todoDateRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public StatisticResponse getStatistic(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));

        Statistic statistic = user.getStatistic();

        return StatisticResponse.from(statistic);
    }

    @Transactional
    public void updateStatistic(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));
        Statistic statistic = user.getStatistic();
        LocalDate yesterday = LocalDate.now().minusDays(1);

        boolean success = todoDateRepository.existsByTodo_User_IdAndDateAndCompleted(userId, yesterday, true)
                && !todoDateRepository.existsByTodo_User_IdAndDateAndCompleted(userId, yesterday, false);

        statistic.increaseStreakIfSuccess(success);

        if (success) {
            statistic.getStatWeeks().stream()
                    .filter(w -> w.getWeek().toDayOfWeek() == yesterday.getDayOfWeek())
                    .findFirst()
                    .ifPresent(StatWeek::increaseCount);

        }

        LocalDate start = yesterday.minusDays(30);
        long completedCount = todoDateRepository.countByTodo_User_IdAndDateBetweenAndCompleted(userId, start, yesterday, true);
        long failedCount = todoDateRepository.countByTodo_User_IdAndDateBetweenAndCompleted(userId, start, yesterday, false);
        long totalCount = completedCount + failedCount;

        int monthPercent;
        if (totalCount == 0) {
            monthPercent = 0;
        } else {
            monthPercent = (int) (completedCount*100/ totalCount);
        }

        statistic.updateMonthPercent(monthPercent);
    }

    @Transactional
    public void updateAllStatistics() {
        int page = 0;
        Page<User> userPage;

        do {
            userPage = userRepository.findAll(PageRequest.of(page, 500));
            for(User user : userPage.getContent()) {
                this.updateStatistic(user.getId());
            }
            entityManager.flush();
            entityManager.clear();
            page++;
        } while (userPage.hasNext());
    }


}
