package com.cg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("")
public class HomeController {

    @GetMapping("/temp")
    public String tempPage() {
        return "/cp/temp";
    }
}