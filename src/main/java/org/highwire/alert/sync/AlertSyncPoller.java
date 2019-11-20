package org.highwire.alert.sync;

import org.highwire.alert.sync.service.AlertSyncService;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableScheduling
public class AlertSyncPoller {

  private boolean enabled = true;

  @Autowired
  private AlertSyncService alertSyncService;

  /**
   * Disable polling.
   */
  public void disablePolling() {
    enabled = false;
    log.warn(" Poller Stopped. ");
  }

  /**
   * Enables polling.
   */
  public void enablePolling() {
    enabled = true;
    log.info(" Poller Started ");
  }

  /**
   * return polling status.
   */
  public boolean isPollingEnabled() {
    return enabled;
  }

  @Scheduled(fixedDelayString = "${spring.poller.schedule.fixed.delay.milliseconds}", initialDelay = 10000)
  public synchronized void collectAlertResources() throws Exception {
    log.info("Collect Alert Resources - Poller Status : " + enabled);
    if (isPollingEnabled()) {
      // get alertsync records from legacy alert DB
      // - get alert records from alert_sync table
      // - call SyncService to produce kafka msg for each alert entry.
      alertSyncService.syncRecentAlerts();
    }
  }

  @Scheduled(fixedDelayString = "${spring.poller.schedule.cleanup.delay.milliseconds}",
      initialDelay = 20000)
  public synchronized void cleanupUnqualifiedAlertSyncEntries() throws Exception {
    log.info("Cleanup Unqualified Alert Sync Entries : ");
    if (isPollingEnabled()) {
      this.disablePolling();
      // Cleanup Unqualified AlertSync Entries
      try {
        alertSyncService.cleanupUnqualifiedAlertSyncEntries();
      } catch (Exception e) {
        log.error(" cleanupUnqualifiedAlertSyncEntries failed " + e.getMessage());
      } finally {
        this.enablePolling();
      }
    }
  }



}
