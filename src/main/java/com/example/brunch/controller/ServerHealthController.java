package com.example.brunch.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/running")
public class ServerHealthController {

    @GetMapping
    public String upAndRunning() {
        return "<h1>Server is up and running, congratz!</h1><div><img src=\"https://thumbs.gfycat.com/EnchantedDevotedFruitbat-size_restricted.gif\"></div>";
    }
}
