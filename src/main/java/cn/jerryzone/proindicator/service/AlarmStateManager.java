package cn.jerryzone.proindicator.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AlarmStateManager implements StateManager {
    private static final Logger logger = LoggerFactory.getLogger(AlarmStateManager.class);
    private static final List<String> DETECT_MACS = new LinkedList<>();
    private static final Duration DETECT_DURATION = Duration.ofMinutes(20);

    static {
        DETECT_MACS.add("C8:5B:76:94:49:1F");   // eth
        DETECT_MACS.add("F0:D5:BF:61:70:51");   // wlan
        //DETECT_MACS.add("AC:7B:A1:6A:7A:75");   // jerry
    }

    private final Map<String, Date> status = new ConcurrentHashMap<>();
    private boolean indicator = false;
    @Autowired
    private ProfAlarmService alarmService;

    @Override
    public void update(String macAddress) {
        if (logger.isTraceEnabled()) {
            logger.trace("update mac:{}", macAddress);
        }
        if (!DETECT_MACS.contains(macAddress)) {
            return;
        }
        status.put(macAddress, new Date(System.currentTimeMillis() + DETECT_DURATION.toMillis()));
    }

    @Scheduled(cron = "* * * * * *")
    public void updateAlive() {
        status.entrySet().removeIf(entry -> entry.getValue().before(new Date()));
        boolean newState = !status.entrySet().isEmpty();
        if (newState ^ indicator) {
            alarmService.alarm(newState);
        }
        indicator = newState;
    }
}
