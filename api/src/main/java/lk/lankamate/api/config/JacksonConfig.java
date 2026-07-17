package lk.lankamate.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                // Support Java 8 date/time types (LocalDate, Instant, etc.)
                .registerModule(new JavaTimeModule())
                // Write dates as ISO strings, not numeric timestamps
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
}