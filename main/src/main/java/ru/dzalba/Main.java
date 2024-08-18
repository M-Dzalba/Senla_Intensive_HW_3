package ru.dzalba;

import ru.dzalba.controller.Controller;
import ru.dzalba.ioc.IoCContainer;

public class Main {
    public static void main(String[] args) throws Exception {
        IoCContainer container = IoCContainer.getInstance();

        Controller controller = container.resolve(Controller.class);
        controller.execute();
    }
}