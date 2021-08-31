package org.github.hoorf.dbboot.migrate.monitor;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RecordSpeedCounter {

    private static AtomicInteger dump_count = new AtomicInteger();

    private static AtomicInteger import_count = new AtomicInteger();

    private static boolean RUNNING = false;


    public static void countDumper() {
        if (RUNNING) {
            dump_count.incrementAndGet();
        } else {
            start();
            RUNNING = true;
            countDumper();
        }
    }

    public static void countImporter(int  len) {
        if (RUNNING) {
            import_count.addAndGet(len);
        } else {
            start();
            RUNNING = true;
            countImporter(len);
        }
    }


    public static void start() {

        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                log.info("dump speeds {} r/s,import speeds {} r/s", dump_count.get(), import_count.get());
                dump_count.set(0);
                import_count.set(0);
            }
        }, 0L, 1000L);

    }
}
