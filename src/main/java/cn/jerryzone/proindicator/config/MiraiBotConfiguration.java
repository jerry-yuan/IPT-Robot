package cn.jerryzone.proindicator.config;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.EventChannel;
import net.mamoe.mirai.event.events.BotEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.utils.BotConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MiraiBotConfiguration {
    @Value("${spring.mirai.account}")
    private Long account;
    @Value("${spring.mirai.password}")
    private String password;
    @Value("${spring.mirai.device-info}")
    private String deviceInfo;
    @Value("${spring.ipt.group}")
    private Long groupId;
    @Bean
    public Bot iptBot() {
        Bot bot=BotFactory.INSTANCE.newBot(account, password,botConfiguration());
        bot.login();
        return bot;
    }
    @Bean
    public EventChannel<BotEvent> botChannel(Bot iptBot){
        return iptBot.getEventChannel();
    }
    @Bean
    public Group pushGroup(Bot iptBot){
        return iptBot.getGroup(groupId);
    }
    @Bean
    public BotConfiguration botConfiguration(){
        BotConfiguration configuration=new BotConfiguration();
        configuration.fileBasedDeviceInfo(deviceInfo);
        configuration.setProtocol(BotConfiguration.MiraiProtocol.ANDROID_PHONE);
        return configuration;
    }
}
