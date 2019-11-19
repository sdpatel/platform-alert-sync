package org.highwire.alert.sync;

import org.highwire.alert.sync.service.AlertSyncService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class AlertSyncPoller {
  public static final Logger logger = LoggerFactory.getLogger(AlertSyncPoller.class);

  private boolean enabled = true;

  @Autowired
  private AlertSyncService alertSyncService;

  /**
   * Disable polling.
   */
  public void disablePolling() {
    enabled = false;
  }

  /**
   * Enables polling.
   */
  public void enablePolling() {
    enabled = true;
  }

  /**
   * return polling status.
   */
  public boolean isPollingEnabled() {
    return enabled;
  }

  @Scheduled(fixedDelayString = "60000", initialDelay = 10000)
  public synchronized void collectAlertResources() throws Exception {
    logger.info("Collect Alert Resources - Poller Status : " + enabled);
    if (isPollingEnabled()) {
      // get alertsync records from legacy alert DB
      // - get alert records from alert_sync table
      // - call SyncService to produce kafka msg for each alert entry.
      alertSyncService.syncRecentAlerts();

    }
  }

}
