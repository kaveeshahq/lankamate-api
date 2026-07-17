package lk.lankamate.api.itinerary;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lk.lankamate.api.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "day_plans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DayPlan extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "itinerary_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_day_plans_itinerary"))
    private Itinerary itinerary;

    @Column(nullable = false)
    private int dayNumber;

    @Column
    private LocalDate date;

    @Column
    private String title;

    @Column(length = 1000)
    private String summary;

    @OneToMany(
            mappedBy = "dayPlan",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    @OrderBy("orderIndex ASC")
    @Builder.Default
    private List<Activity> activities = new ArrayList<>();

    public void addActivity(Activity activity) {
        activity.setDayPlan(this);
        this.activities.add(activity);
    }
}