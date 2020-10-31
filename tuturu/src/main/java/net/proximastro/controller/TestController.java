package net.proximastro.controller;

import net.proximastro.app.RouteController;

import java.util.HashMap;

public class TestController extends RouteController {

    public TestController(){
    }

    @Override
    protected String index() {
        return "<h1>MDR</h1>";
    }
}
