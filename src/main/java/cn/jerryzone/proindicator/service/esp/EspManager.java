package cn.jerryzone.proindicator.service.esp;

import cn.jerryzone.proindicator.service.StateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

@Service
public class EspManager implements Runnable, InitializingBean, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(EspManager.class);
    private final Thread daemonThread = new Thread(this,"esp8266");
    @Autowired
    private StateManager stateManager;
    @Autowired
    private SerialPortService serialPortService;
    private volatile boolean isRunning = true;


    @Override
    public void run() {
        InputStream espInputStream = null;
        try {
            espInputStream = serialPortService.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(espInputStream));
            while (!reader.ready()) {
                Thread.sleep(1);
            }
            ;
            // Drop First Line incase it has dirt data
            reader.readLine();
            while (isRunning) {
                if (!reader.ready()) {
                    Thread.sleep(1);
                    continue;
                }
                String line = reader.readLine();
                // skip if the line is not a valid line
                if (line == null || !line.startsWith("->")) {
                    if (logger.isTraceEnabled()) {
                        logger.trace("skip invalid line:{}.", line);
                    }
                    continue;
                }
                String[] parts = line.split("\t");

                String clientMac = parts[1];
                // skip if there are errors in clientMac
                if (!clientMac.matches("[0-9A-F]{2}(:[0-9A-F]{2}){5}")) {
                    if (logger.isTraceEnabled()) {
                        logger.trace("mac {} is not matching regex '[0-9A-F]{2}(:[0-9A-F]{2}){5}'.", clientMac);
                    }
                    continue;
                }
                // skip if the client is broadcast
                if ("FF:FF:FF:FF:FF:FF".equals(clientMac)) {
                    if (logger.isTraceEnabled()) {
                        logger.trace("skip broadcast mac.");
                    }
                    continue;
                }
                stateManager.update(clientMac);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (Objects.nonNull(espInputStream)) {
                try {
                    espInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    @Override
    public void afterPropertiesSet() throws Exception {
        serialPortService.open();
        this.daemonThread.start();
    }

    @Override
    public void destroy() throws Exception {

        this.isRunning = false;
        this.daemonThread.join();

        serialPortService.close();
    }


}
