package lk.lankamate.api.attraction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lk.lankamate.api.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A place worth visiting in Sri Lanka. Stored with plain lat/lng columns plus a
 * PostGIS-backed spatial index created via SQL, so we can run distance queries
 * ("what's near me") without adding a Hibernate Spatial dependency.
 */
@Entity
@Table(name = "attractions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attraction extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(length = 1500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttractionType type;

    @Column(nullable = false)
    private String region;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    /** Typical rating out of 5, for display. */
    @Column
    private Double rating;

    /** Rough entry cost in USD; null or 0 means free. */
    @Column
    private Double entryFee;

    /** Best months to visit, e.g. "Jan – Apr". */
    @Column
    private String bestTime;

    @Column(length = 500)
    private String tip;

    @Column
    private String imageUrl;
}