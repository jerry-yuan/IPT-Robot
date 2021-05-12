package cn.jerryzone.proindicator.service;

import net.mamoe.mirai.contact.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
@Service
public class MiraiProfAlarmService implements ProfAlarmService {
    @Autowired
    Group broadcatGroup;

    @Override
    public void alarm(boolean state) {
        StringBuilder builder = new StringBuilder();

        builder.append(new Date().toString()).append("\n");
        builder.append("IPT=").append(state ? "true" : "false");

        broadcatGroup.sendMessage(builder.toString());
    }
}
