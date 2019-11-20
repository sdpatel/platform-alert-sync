package org.highwire.alert.sync;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AlertSyncApplication extends SpringBootServletInitializer {

  private static final Logger LOG = LoggerFactory.getLogger(AlertSyncApplication.class);

  public static void main(String[] args) {
      LOG.info("Starting AlertSyncApplication Now ");
      SpringApplication.run(AlertSyncApplication.class, args);
      LOG.info(" AlertSyncApplication ");
  }

  @EventListener(ApplicationReadyEvent.class)
  public void startup() {
      LOG.warn("AlertSyncApplication is ready.");
  }

}
