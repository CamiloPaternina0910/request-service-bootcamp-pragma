package co.com.bancolombia.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    public OpenAPI provideOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title(API_TITLE)
                        .version(API_VERSION)
                        .description(API_DESCRIPTION)
                        .contact(new Contact()
                                .email(CONTACT_EMAIL)
                                .name(CONTACT_NAME))
                        .license(new License()
                                .name(LICENSE_NAME)
                                .url(LICENSE_URL)));
    }

    private static final String API_TITLE = "Solicitudes Manager API";
    private static final String API_VERSION = "1.0.0";
    private static final String API_DESCRIPTION = "API for request management";
    private static final String CONTACT_NAME = "Camilo Paternina";
    private static final String CONTACT_EMAIL = "paterninapcamilo@gmail.com";
    private static final String LICENSE_NAME = "Apache 2.0";
    private static final String LICENSE_URL = "http://springdoc.org";
}