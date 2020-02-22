package com.gupaoedu.vip.spring.demo.controller;

import com.gupaoedu.vip.spring.demo.service.IQueryservice;
import com.gupaoedu.vip.spring.formwork.annotation.GPAutowried;
import com.gupaoedu.vip.spring.formwork.annotation.GPController;

@GPController
public class QueryController {

    @GPAutowried
    private IQueryservice queryservice;
}
