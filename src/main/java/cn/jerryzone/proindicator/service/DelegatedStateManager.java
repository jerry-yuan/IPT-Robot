package cn.jerryzone.proindicator.service;

import java.util.Arrays;

public class DelegatedStateManager implements StateManager {
    private final StateManager[] stateManagers;

    public DelegatedStateManager(StateManager... managers) {
        stateManagers = managers;
    }

    @Override
    public void update(String macAddress) {
        Arrays.stream(stateManagers).forEach(s -> s.update(macAddress));
    }
}
