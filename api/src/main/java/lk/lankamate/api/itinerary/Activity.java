package lk.lankamate.api.itinerary;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lk.lankamate.api.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "activities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Activity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "day_plan_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_activities_day_plan"))
    private DayPlan dayPlan;

    @Column(nullable = false)
    private String title;

    @Column(length = 1500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TimeOfDay timeOfDay;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ActivityCategory category = ActivityCategory.OTHER;

    @Column(nullable = false)
    @Builder.Default
    private int orderIndex = 0;

    @Column
    private String locationName;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @Column(precision = 12, scale = 2)
    private BigDecimal estimatedCost;

    @Column(length = 500)
    private String tip;
}