package cn.jerryzone.proindicator.service.esp;

import com.pi4j.io.serial.*;

import java.io.IOException;
import java.io.InputStream;

public class Pi4JSerialPortService implements SerialPortService {
    private final SerialConfig config = new SerialConfig();
    private final Serial serial = SerialFactory.createInstance();

    public Pi4JSerialPortService(String portName) {

        config.device(portName)
                .baud(Baud._115200)
                .dataBits(DataBits._8)
                .parity(Parity.NONE)
                .stopBits(StopBits._1)
                .flowControl(FlowControl.NONE);
    }

    @Override
    public void open() throws Exception {
        serial.open(config);
    }

    @Override
    public InputStream getInputStream() throws Exception {
        return serial.getInputStream();
    }

    @Override
    public void close() throws IOException {
        if (serial.isOpen()) {
            serial.close();
        }
    }
}
