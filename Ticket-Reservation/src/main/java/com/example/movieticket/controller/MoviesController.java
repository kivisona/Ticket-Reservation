package com.example.movieticket.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/movies")
public class MoviesController {
    @GetMapping
    public String showMoviesPage(Model model) {
        model.addAttribute("movies", new String[]{"Movie 1", "Movie 2", "Movie 3", "Movie 4", "Movie 5","Movie 6"});
        return "movies";
    }
}
