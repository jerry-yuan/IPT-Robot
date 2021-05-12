package cn.jerryzone.proindicator.service.arp;

import cn.jerryzone.proindicator.service.StateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
@ConditionalOnProperty(prefix = "os", name = "name", havingValue = "Linux")
public class LinuxArpScanService implements Runnable, InitializingBean, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(LinuxArpScanService.class);
    private final Thread thread = new Thread(this, "arp-scan");
    private final ProcessBuilder processBuilder = new ProcessBuilder("/usr/local/bin/arp-scan", "--interface=wlan0", "-x", "-l", "-g");
    private volatile boolean isRunning = true;

    @Autowired
    private StateManager stateManager;

    @Override
    public void run() {
        while (isRunning) {
            try {
                Process process = processBuilder.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (logger.isTraceEnabled()) {
                        logger.trace("arp scan result:{}", line);
                    }
                    String targetMac = line.split("\t")[1];
                    if("10:51:72:27:B9:AB".equalsIgnoreCase(targetMac)){
                        if (logger.isTraceEnabled()) {
                            logger.trace("ignore knowned gateway mac:{}", targetMac);
                        }
                        continue;
                    }

                    stateManager.update(targetMac.toUpperCase());
                }
                process.waitFor();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void destroy() throws Exception {
        isRunning = false;
        this.thread.join();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.thread.start();
    }
}
