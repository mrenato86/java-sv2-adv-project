package springsskytravel;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springsskytravel.services.rules.BasicReservationRuleSet;
import springsskytravel.services.rules.ReservationRuleSet;

@SpringBootApplication
public class SpringsSkyTravelApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringsSkyTravelApplication.class, args);
    }

    @Bean
    public ModelMapper getModelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper;
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Spring's Sky Travel API")
                        .version("1.0")
                        .description("Operations with journeys and reservations"));
    }

    @Bean
    public ReservationRuleSet getReservationRuleSet() {
        return new BasicReservationRuleSet();
    }

}
