package cn.jerryzone.proindicator.controller;

import net.mamoe.mirai.contact.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController

public class PushMessageController {
    @Autowired
    Group broadcaster;

    @GetMapping("/push")
    public void push(@RequestParam String message) {
        broadcaster.sendMessage(new Date().toString()+"广播\n"+message);
    }
}
