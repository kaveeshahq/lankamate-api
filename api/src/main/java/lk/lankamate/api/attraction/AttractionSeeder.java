package lk.lankamate.api.attraction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Seeds a starter set of real Sri Lankan attractions the first time the app
 * runs against an empty table. Coordinates are real so the map is genuinely
 * useful from day one.
 */
@Configuration
public class AttractionSeeder {

    private static final Logger log = LoggerFactory.getLogger(AttractionSeeder.class);

    @Bean
    CommandLineRunner seedAttractions(AttractionRepository repository) {
        return args -> {
            if (repository.count() > 0) {
                return;
            }

            List<Attraction> seed = List.of(
                    a("Sigiriya Rock Fortress",
                            "A 5th-century royal citadel atop a 200m granite column, with frescoes and water gardens.",
                            AttractionType.FORTRESS, "Central Province",
                            7.9570, 80.7603, 4.8, 30.0, "Jan – Apr",
                            "Start at 7am to beat the heat and the crowds."),
                    a("Temple of the Sacred Tooth Relic",
                            "Sri Lanka's most revered Buddhist site, housing a relic of the Buddha.",
                            AttractionType.TEMPLE, "Central Province",
                            7.2936, 80.6413, 4.7, 10.0, "Year-round",
                            "Time your visit with a puja ceremony for the full experience."),
                    a("Nine Arch Bridge",
                            "A striking colonial-era viaduct arching through the jungle near Ella.",
                            AttractionType.VIEWPOINT, "Uva Province",
                            6.8767, 81.0611, 4.7, 0.0, "Jan – May",
                            "Check train times locally — the photo everyone wants needs a train."),
                    a("Galle Fort",
                            "A UNESCO-listed Dutch colonial fort town wrapped by the Indian Ocean.",
                            AttractionType.FORTRESS, "Southern Province",
                            6.0269, 80.2170, 4.7, 0.0, "Nov – Apr",
                            "Walk the ramparts at sunset — it's free and unforgettable."),
                    a("Yala National Park",
                            "Sri Lanka's most famous park, with one of the world's highest leopard densities.",
                            AttractionType.NATIONAL_PARK, "Southern Province",
                            6.3728, 81.5200, 4.8, 45.0, "Feb – Jul",
                            "Book the dawn safari — wildlife is far more active early."),
                    a("Ella Rock",
                            "A rewarding hill-country hike with sweeping views over the Ella Gap.",
                            AttractionType.HIKE, "Uva Province",
                            6.8500, 81.0500, 4.6, 0.0, "Jan – May",
                            "Set off before 6am to reach the summit for sunrise."),
                    a("Diyaluma Falls",
                            "Sri Lanka's second-highest waterfall, with natural infinity pools at the top.",
                            AttractionType.WATERFALL, "Uva Province",
                            6.7333, 81.0333, 4.8, 0.0, "Feb – Jun",
                            "The upper pools need a 45-minute hike — worth every step."),
                    a("Lipton's Seat",
                            "The viewpoint where Thomas Lipton surveyed his tea empire, above Haputale.",
                            AttractionType.VIEWPOINT, "Uva Province",
                            6.8167, 80.9833, 4.7, 2.0, "Jan – Mar",
                            "Arrive by 6:30am before the clouds roll in."),
                    a("Horton Plains & World's End",
                            "A misty plateau ending in a sheer 870m cliff drop.",
                            AttractionType.HIKE, "Central Province",
                            6.8022, 80.8064, 4.7, 25.0, "Jan – Mar",
                            "Enter at dawn — after 10am the view is usually fogged in."),
                    a("Mirissa Beach",
                            "A crescent of golden sand and the launch point for blue whale watching.",
                            AttractionType.BEACH, "Southern Province",
                            5.9483, 80.4589, 4.6, 0.0, "Nov – Apr",
                            "Whale trips leave around 6:30am; take seasickness tablets."),
                    a("Arugam Bay",
                            "A legendary right-hand point break and laid-back surf town.",
                            AttractionType.SURF_SPOT, "Eastern Province",
                            6.8404, 81.8340, 4.7, 0.0, "May – Sep",
                            "Main Point suits experienced surfers; Baby Point is for beginners."),
                    a("Dambulla Cave Temple",
                            "Five caves filled with 150+ Buddha statues and painted ceilings.",
                            AttractionType.TEMPLE, "Central Province",
                            7.8567, 80.6492, 4.6, 10.0, "Year-round",
                            "It's a steep climb — bring water and cover shoulders and knees."),
                    a("Udawalawe National Park",
                            "The best place in Sri Lanka to reliably see wild elephants.",
                            AttractionType.NATIONAL_PARK, "Sabaragamuwa",
                            6.4750, 80.8983, 4.7, 40.0, "Year-round",
                            "Elephant sightings here are near-guaranteed, unlike Yala."),
                    a("Anuradhapura Sacred City",
                            "A vast ancient capital of stupas, monasteries, and sacred trees.",
                            AttractionType.TEMPLE, "North Central",
                            8.3114, 80.4037, 4.6, 25.0, "Oct – Nov",
                            "Rent a bike — the site is far bigger than it looks."),
                    a("Belihuloya",
                            "A quiet mountain village of cool streams, pine forests, and hiking trails.",
                            AttractionType.HIKE, "Sabaragamuwa",
                            6.7167, 80.7833, 4.5, 0.0, "Feb – May",
                            "A great low-key base between the hill country and the south."),
                    a("Nuwara Eliya",
                            "A cool colonial hill town surrounded by emerald tea plantations.",
                            AttractionType.TEA_ESTATE, "Central Province",
                            6.9497, 80.7891, 4.5, 0.0, "Mar – May",
                            "Nights get genuinely cold — pack a warm layer."),
                    a("Trincomalee",
                            "White-sand beaches, coral reefs, and one of the world's finest natural harbours.",
                            AttractionType.BEACH, "Eastern Province",
                            8.5874, 81.2152, 4.6, 0.0, "May – Sep",
                            "Pigeon Island snorkelling is a short boat ride away."),
                    a("Polonnaruwa Ancient City",
                            "A remarkably well-preserved medieval capital of temples and giant Buddhas.",
                            AttractionType.MUSEUM, "North Central",
                            7.9403, 81.0188, 4.6, 25.0, "Oct – Mar",
                            "The Gal Vihara rock sculptures are the highlight — don't rush them.")
            );

            repository.saveAll(seed);
            log.info("Seeded {} attractions", seed.size());
        };
    }

    private static Attraction a(String name, String desc, AttractionType type,
                                String region, double lat, double lng,
                                Double rating, Double fee, String bestTime,
                                String tip) {
        return Attraction.builder()
                .name(name)
                .description(desc)
                .type(type)
                .region(region)
                .latitude(lat)
                .longitude(lng)
                .rating(rating)
                .entryFee(fee)
                .bestTime(bestTime)
                .tip(tip)
                .build();
    }
}