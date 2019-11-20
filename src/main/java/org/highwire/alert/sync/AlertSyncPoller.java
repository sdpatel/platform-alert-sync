package org.highwire.alert.sync;

import org.highwire.alert.sync.domain.AlertSync;
import org.highwire.alert.sync.service.AlertSyncService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
    List<AlertSync> deletedAlertSyncList = new ArrayList<>();
    if (isPollingEnabled()) {
      this.disablePolling();
      // Cleanup Unqualified AlertSync Entries
      try {
        deletedAlertSyncList = alertSyncService.cleanupUnqualifiedAlertSyncEntries();
      } catch (Exception e) {
        log.error(" cleanupUnqualifiedAlertSyncEntries failed " + e.getMessage());
      } finally {
        this.enablePolling();
      }
    }
  }

  public synchronized List<AlertSync> cleanupUnqualifiedAlertSyncResources() throws Exception {
    log.info("Cleanup Unqualified AlertSync Resources : ");
    List<AlertSync> deletedAlertSyncList = new ArrayList<>();
    if (isPollingEnabled()) {
      this.disablePolling();
      // Cleanup Unqualified AlertSync Entries
      try {
        deletedAlertSyncList = alertSyncService.cleanupUnqualifiedAlertSyncEntries();
      } catch (Exception e) {
        log.error(" cleanupUnqualifiedAlertSyncEntries failed " + e.getMessage());
      } finally {
        this.enablePolling();
      }
    }
    return deletedAlertSyncList;
  }



}
