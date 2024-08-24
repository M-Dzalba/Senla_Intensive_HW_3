package ru.dzalba;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.dzalba.controller.Controller;
import ru.dzalba.ioc.IoCContainer;

import java.util.Set;

@Configuration
@ComponentScan(basePackages = "ru.dzalba")
public class Application {

    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(Application.class);

        IoCContainer container = IoCContainer.getInstance();


        Controller controller = container.resolve(Controller.class);
        controller.execute();

        context.close();
    }
}