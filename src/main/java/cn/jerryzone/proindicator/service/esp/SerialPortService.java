package cn.jerryzone.proindicator.service.esp;

import java.io.IOException;
import java.io.InputStream;

public interface SerialPortService {
    void open() throws Exception;

    InputStream getInputStream() throws Exception;

    void close() throws IOException;
}
