package lk.lankamate.api.trip;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lk.lankamate.api.common.entity.BaseEntity;
import lk.lankamate.api.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "trips")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Trip extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_trips_user")
    )
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column
    private String baseLocationName;

    @Column
    private Double baseLatitude;

    @Column
    private Double baseLongitude;

    @Column(precision = 12, scale = 2)
    private BigDecimal totalBudget;

    @Column(length = 3)
    @Builder.Default
    private String currency = "USD";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private TravelStyle travelStyle = TravelStyle.BALANCED;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private TripStatus status = TripStatus.PLANNING;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "trip_interests",
            joinColumns = @JoinColumn(name = "trip_id",
                    foreignKey = @ForeignKey(name = "fk_trip_interests_trip"))
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "interest")
    @Builder.Default
    private Set<Interest> interests = new HashSet<>();
}