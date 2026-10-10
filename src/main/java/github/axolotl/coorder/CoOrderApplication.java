package github.axolotl.coorder;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.theme.aura.Aura;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@StyleSheet(Aura.STYLESHEET)
@EnableScheduling
@SpringBootApplication
@Push
public class CoOrderApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(CoOrderApplication.class, args);

    }

}
