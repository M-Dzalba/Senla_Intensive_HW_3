package ru.dzalba.controller;

import ru.dzalba.annotations.Autowire;
import ru.dzalba.annotations.Component;
import ru.dzalba.service.ServiceInterface;

@Component
public class Controller {

    private final ServiceInterface service;

    @Autowire
    public Controller(ServiceInterface service) {
        this.service = service;
    }

    public void execute() {
        service.performAction();
    }
}
