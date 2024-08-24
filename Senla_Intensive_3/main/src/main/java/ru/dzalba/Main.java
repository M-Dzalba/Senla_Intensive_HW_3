package ru.dzalba;

import ru.dzalba.controller.Controller;
import ru.dzalba.ioc.IoCContainer;
import ru.dzalba.utils.ComponentScanner;

import java.util.Set;

public class Main {
    public static void main(String[] args) throws Exception {
        IoCContainer container = IoCContainer.getInstance();

        Set<Class<?>> components = ComponentScanner.scanForComponents("ru.dzalba");
        container.registerComponents(components);

        Controller controller = container.resolve(Controller.class);
        controller.execute();
    }
}