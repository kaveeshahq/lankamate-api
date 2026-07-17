package lk.lankamate.api.itinerary;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lk.lankamate.api.common.entity.BaseEntity;
import lk.lankamate.api.trip.Trip;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "itineraries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Itinerary extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trip_id", nullable = false, unique = true,
            foreignKey = @ForeignKey(name = "fk_itineraries_trip"))
    private Trip trip;

    @Column(length = 2000)
    private String overview;

    @Column
    private String generatedBy;

    @OneToMany(
            mappedBy = "itinerary",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    @OrderBy("dayNumber ASC")
    @Builder.Default
    private List<DayPlan> dayPlans = new ArrayList<>();

    public void addDayPlan(DayPlan dayPlan) {
        dayPlan.setItinerary(this);
        this.dayPlans.add(dayPlan);
    }

    public void clearDays() {
        this.dayPlans.clear();
    }
}