package com.example.roommate.controller;

import com.example.roommate.domain.entities.Room;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class TestController {

//    @GetMapping
//    public String index() {
//        return "index";
//    }

//    @GetMapping
//    public @ResponseBody String index() {
//        return "<h1>Das ist auch eine HTML-Seite</h1>";
//    }

    @GetMapping
    public @ResponseBody List<String> index() {
        Room room1 = new Room();
        List<String> list = List.of("hallo");
        return list;
    }
}
