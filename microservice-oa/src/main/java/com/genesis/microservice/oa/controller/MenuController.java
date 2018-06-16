package com.genesis.microservice.oa.controller;

import com.genesis.microservice.oa.service.MenuService;
import com.microservice.rest.vo.Menu;
import com.microservice.rest.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Aizhanglin on 2017/12/7.
 */
@RestController
public class MenuController {
    @Autowired
    MenuService menuService;

    @PostMapping("/menu/add")
    public Result addMenu(@RequestBody Menu menu){
        menuService.addMenu(menu);
        return Result.success(menu);
    }
}
