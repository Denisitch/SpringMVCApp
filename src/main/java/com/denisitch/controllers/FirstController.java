package com.denisitch.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

@Controller
@RequestMapping("/first")
public class FirstController {
    @GetMapping("/hello")
    public String helloPage(@RequestParam(value = "name", required = false) String name,
                            @RequestParam(value = "surname", required = false) String surname,
                            Model model) {
        model.addAttribute("message", "Hello, " + name + " " + surname);
        return "first/hello";
    }

    @GetMapping("/goodbye")
    public String goodbyePage() {
        return "first/goodbye";
    }

    @GetMapping("/calculator")
    public String calculator(@RequestParam(value = "a", required = false) int a,
                             @RequestParam(value = "b", required = false) int b,
                             @RequestParam(value = "action", required = false) String action,
                             Model model) {
        if (Objects.equals(action, "addition")) {
            model.addAttribute("message",
                    "Result " + a + " " + action + " " + b + " equals " + (a + b));
        } else if (Objects.equals(action, "subtraction")) {
            model.addAttribute("message",
                    "Result " + a + " " + action + " " + b + " equals " + (a - b));
        } else if (Objects.equals(action, "multiplication")) {
            model.addAttribute("message",
                    "Result " + a + " " + action + " " + b + " equals " + (a * b));
        } else if (Objects.equals(action, "division")) {
            model.addAttribute("message",
                    "Result " + a + " " + action + " " + b + " equals " + (a / (double) b));
        }
        return "first/calculator";
    }
}