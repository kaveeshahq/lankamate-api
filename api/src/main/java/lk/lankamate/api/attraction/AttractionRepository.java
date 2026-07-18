package lk.lankamate.api.attraction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface AttractionRepository extends JpaRepository<Attraction, UUID> {

    List<Attraction> findByType(AttractionType type);

    List<Attraction> findByRegionIgnoreCase(String region);

    /**
     * Finds attractions within a radius (metres) of a point, nearest first,
     * using PostGIS geography distance. This is the payoff for installing
     * PostGIS: real spherical distance, not naive lat/lng maths.
     */
    @Query(value = """
            SELECT * FROM attractions a
            WHERE ST_DWithin(
                ST_SetSRID(ST_MakePoint(a.longitude, a.latitude), 4326)::geography,
                ST_SetSRID(ST_MakePoint(:lng, :lat), 4326)::geography,
                :radiusMeters
            )
            ORDER BY ST_Distance(
                ST_SetSRID(ST_MakePoint(a.longitude, a.latitude), 4326)::geography,
                ST_SetSRID(ST_MakePoint(:lng, :lat), 4326)::geography
            )
            """, nativeQuery = true)
    List<Attraction> findNearby(@Param("lat") double lat,
                                @Param("lng") double lng,
                                @Param("radiusMeters") double radiusMeters);
}