package pe.edu.vallegrande.vgmsassignmentstaff.application.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI userServiceAPI() {
        return new OpenAPI()
                .info(new Info().title("API de Servicio de Assignación de Personal Institucional")
                        .description("Esta es la API REST para el Servicio de Assignación de Personal Institucional")
                        .version("v0.0.1")
                        .license(new License().name("Apache 2.0")))
                .externalDocs(new ExternalDocumentation()
                        .description("Documentación Wiki del Servicio de Assignación de Personal Institucional")
                        .url("https://vallegrande.edu.pe/docs"));
    }
}