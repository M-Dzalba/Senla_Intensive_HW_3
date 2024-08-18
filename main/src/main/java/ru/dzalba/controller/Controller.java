package ru.dzalba.controller;

import ru.dzalba.annotations.Autowire;
import ru.dzalba.annotations.Component;
import ru.dzalba.service.ServiceInterface;

@Component
public class Controller {

    @Autowire
    private ServiceInterface service;

    public void execute() {
        service.performAction();
    }
}
