package likelion14th.lte.statistic.controller;

import likelion14th.lte.global.api.ApiResponse;
import likelion14th.lte.global.api.SuccessCode;
import likelion14th.lte.statistic.dto.response.StatisticResponse;
import likelion14th.lte.statistic.service.StatisticService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistic")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class StatisticController {
    private final StatisticService statisticService;

    @GetMapping
    public ApiResponse<StatisticResponse> getStatistic(@RequestParam Long userId) {
        StatisticResponse statisticResponse = statisticService.getStatistic(userId);

        return ApiResponse.onSuccess(SuccessCode.STATISTICS_GET_SUCCESS, statisticResponse);
    }
}
