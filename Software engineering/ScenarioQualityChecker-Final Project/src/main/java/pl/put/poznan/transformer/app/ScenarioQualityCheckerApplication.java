package pl.put.poznan.transformer.app;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Creates Spring based application that processes and manages REST endpoints.
 */
@SpringBootApplication(scanBasePackages = {"pl.put.poznan.transformer.rest"})
public class ScenarioQualityCheckerApplication {

    public static void main(String[] args) {
        System.out.println("Application starting");
        SpringApplication.run(ScenarioQualityCheckerApplication.class, args);
    }
}
