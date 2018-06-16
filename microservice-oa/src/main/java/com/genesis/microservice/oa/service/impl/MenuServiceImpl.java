package com.genesis.microservice.oa.service.impl;

import com.genesis.microservice.oa.mapper.MenuMapper;
import com.genesis.microservice.oa.service.MenuService;
import com.microservice.rest.vo.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Created by Aizhanglin on 2017/12/7.
 */
@Service
public class MenuServiceImpl implements MenuService{
    @Autowired
    MenuMapper menuMapper;
    @Override
    public Menu addMenu(Menu menu){
        int n=menuMapper.insert(menu);
        Assert.isTrue(n>0,"插入菜单失败");
        return menu;
    }
}
