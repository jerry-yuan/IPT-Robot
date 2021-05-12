package cn.jerryzone.proindicator.service.esp;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.InputStream;
import java.util.Objects;

public class RxTxSerialPortService implements SerialPortService {
    private CommPortIdentifier portIdentifier = null;
    private SerialPort serialPort = null;

    public RxTxSerialPortService(String portName) throws Exception {
        portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
    }

    @Override
    public void open() throws Exception {
        if (Objects.isNull(portIdentifier)) {
            throw new IllegalStateException("port service not initialized!");
        }
        serialPort = (SerialPort) portIdentifier.open(portIdentifier.getName(), 2000);
        serialPort.setSerialPortParams(115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

    }

    @Override
    public InputStream getInputStream() throws Exception {
        if (Objects.isNull(serialPort)) {
            throw new IllegalStateException("port not open!");
        }
        return serialPort.getInputStream();
    }

    @Override
    public void close() {
        if (Objects.isNull(serialPort)) {
            throw new IllegalStateException("port not open!");
        }
        serialPort.close();
    }
}
