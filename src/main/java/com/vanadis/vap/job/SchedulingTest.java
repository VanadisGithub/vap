package com.vanadis.vap.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SchedulingTest {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Scheduled(cron = "0/5 * * * * ?") // 每5秒执行一次
    public void scheduler() {
        //logger.info(">>>>>>>>>>>>> scheduled test... ");
    }
}