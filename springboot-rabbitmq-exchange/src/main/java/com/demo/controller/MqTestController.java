package com.demo.controller;

import com.demo.mqproduct.FirstProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: zhukai
 * Date: 2019/1/4
 * Time: 10:16
 * Description:
 */
@RestController
public class MqTestController {
    @Autowired
    private FirstProduct firstSender;


    @GetMapping("/send")
    public String send(String message){
        String uuid = UUID.randomUUID().toString();
        firstSender.send(uuid,message);
        return uuid;
    }

    @GetMapping("/sendMsg")
    public String sendMsg(String message){
        String uuid = UUID.randomUUID().toString();
        firstSender.sendThird(uuid,message);
        return uuid;
    }
}
