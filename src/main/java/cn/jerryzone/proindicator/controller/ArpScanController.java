package cn.jerryzone.proindicator.controller;

import cn.jerryzone.proindicator.service.StateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Date;

@RestController
public class ArpScanController {
    @Autowired
    StateManager stateManager;

    @PostMapping("/ipt")
    public void ipt(@RequestBody String macList) throws IOException {
        macList = URLDecoder.decode(macList,"utf-8");

        Arrays.stream(macList.split("\n"))
                .map(row -> row.split("\t")[1])
                .map(String::toUpperCase)
                .forEach(stateManager::update);
    }
}
