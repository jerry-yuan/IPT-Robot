package cn.jerryzone.proindicator.config;

import cn.jerryzone.proindicator.service.AlarmStateManager;
import cn.jerryzone.proindicator.service.DelegatedStateManager;
import cn.jerryzone.proindicator.service.HistoryStateManager;
import cn.jerryzone.proindicator.service.StateManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StateManagerConfiguration {
    @Bean
    public HistoryStateManager historyStateManager() {
        return new HistoryStateManager();
    }

    @Bean
    public AlarmStateManager alarmStateManager() {
        return new AlarmStateManager();
    }

    @Bean
    public StateManager stateManager() {
        return new DelegatedStateManager(
                historyStateManager(),
                alarmStateManager()
        );
    }
}
