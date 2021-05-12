package cn.jerryzone.proindicator.service;

import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HistoryStateManager implements StateManager {

    private final Map<String, Date[]> macLog = new HashMap<>();

    @Override
    public void update(String macAddress) {
        synchronized (macLog) {
            if (!macLog.containsKey(macAddress)) {
                macLog.put(macAddress, new Date[]{new Date(), new Date()});
            }
            macLog.get(macAddress)[1] = new Date();
        }
    }

    public Map<String, Date[]> getMacLog() {
        return macLog;
    }

    public String generateLog() {
        StringBuilder builder = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        builder.append("======================================================================================\n");
        builder.append("Client MAC\t\tFirst appearance\tLast appearance\taliveSeconds\n");
        builder.append("======================================================================================\n");
        getMacLog().entrySet().stream()
                .map(e -> new ClientInfo(e.getKey(), e.getValue()))
                .sorted((o2, o1) -> (int) (o1.getLiveSeconds() - o2.getLiveSeconds()))
                .forEachOrdered(clientInfo -> builder
                        .append(clientInfo.getMac())
                        .append("\t")
                        .append(sdf.format(clientInfo.getFirst()))
                        .append("\t")
                        .append(sdf.format(clientInfo.getLast()))
                        .append("\t")
                        .append(clientInfo.getLiveSeconds())
                        .append("\n"));
        return builder.toString();
    }

    @Scheduled(cron = "0 * * * * *")
    public void checkCurrent() {
        Date checkDate = new Date();
        Date invalidDate = new Date(checkDate.getTime() - Duration.ofMinutes(10).toMillis());
        synchronized (macLog) {
            Iterator<Map.Entry<String, Date[]>> iterator = macLog.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Date[]> entry = iterator.next();
                Date[] dates = entry.getValue();
                if (dates[1].before(invalidDate)) {
                    iterator.remove();
                }
            }
        }
    }

    private static class ClientInfo {
        public final String mac;
        public final Date first;
        public final Date last;
        public final long liveSeconds;

        public ClientInfo(String mac, Date[] dates) {
            this.mac = mac;
            this.first = dates[0];
            this.last = dates[1];
            this.liveSeconds = (this.last.getTime() - this.first.getTime()) / 1000;
        }

        public long getLiveSeconds() {
            return liveSeconds;
        }

        public String getMac() {
            return mac;
        }

        public Date getFirst() {
            return first;
        }

        public Date getLast() {
            return last;
        }
    }

}
