package bdavanzadas.lab1.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {
    //Este código configura la política de CORS (Cross-Origin Resource Sharing) para la aplicación
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*") // Permitir todos los orígenes (no recomendado en producción)
                        .allowedMethods("*")
                        .allowedHeaders("*");
            }
        };
    }
}
