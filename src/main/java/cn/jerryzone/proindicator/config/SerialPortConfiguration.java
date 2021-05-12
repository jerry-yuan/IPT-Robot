package cn.jerryzone.proindicator.config;

import cn.jerryzone.proindicator.service.esp.Pi4JSerialPortService;
import cn.jerryzone.proindicator.service.esp.RxTxSerialPortService;
import cn.jerryzone.proindicator.service.esp.SerialPortService;
import com.pi4j.io.serial.RaspberryPiSerial;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SerialPortConfiguration {
    @Value("${spring.esp8266.port}")
    private String port;

    @Bean
    @ConditionalOnProperty(prefix = "os", name = "arch", havingValue = "amd64")
    public SerialPortService rxTxSerialPortService() throws Exception {
        return new RxTxSerialPortService(port);
    }

    @Bean
    @ConditionalOnProperty(prefix = "os", name = "arch", havingValue = "arm")
    public SerialPortService pi4jSerialPortService() throws Exception {
        return new Pi4JSerialPortService(RaspberryPiSerial.S0_COM_PORT);

    }
}
