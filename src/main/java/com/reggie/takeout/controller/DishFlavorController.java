package com.reggie.takeout.controller;

import com.reggie.takeout.service.DishFlavorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DishFlavorController {

    @Autowired
    private DishFlavorService dishFlavorService;
}
