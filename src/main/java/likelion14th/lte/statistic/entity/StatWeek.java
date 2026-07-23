package likelion14th.lte.statistic.entity;

import jakarta.persistence.*;
import likelion14th.lte.Entity.BaseEntity;
import likelion14th.lte.todo.entity.WeekEnum;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "stat_week")
public class StatWeek extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WeekEnum week;

    @Column(nullable = false)
    private int count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "statistic_id", nullable = false)
    private Statistic statistic;

    static StatWeek create(Statistic statistic, WeekEnum week) {
        StatWeek statWeek = new StatWeek();
        statWeek.statistic = statistic;
        statWeek.week = week;
        statWeek.count = 0;
        return statWeek;
    }

    public void increaseCount() {
        this.count ++;
    }
}