package com.vborodin.exchangerates.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LandingPageController {

    @RequestMapping("/")
    public String getIndex(Model model){
        return "index";
    }
}
