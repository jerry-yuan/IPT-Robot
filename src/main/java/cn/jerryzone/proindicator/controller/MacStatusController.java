package cn.jerryzone.proindicator.controller;

import cn.jerryzone.proindicator.service.AlarmStateManager;
import cn.jerryzone.proindicator.service.HistoryStateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MacStatusController {
    @Autowired
    HistoryStateManager historyStateManager;

    @Autowired
    AlarmStateManager alarmStateManager;

    @GetMapping(value = "/macs/all", produces = "text/plain")
    public String allState() {
        return historyStateManager.generateLog();
    }
}
