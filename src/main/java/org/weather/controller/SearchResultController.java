package org.weather.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/search-result")
public class SearchResultController {

    @PostMapping
    public void searchLocation() {

    }
}
