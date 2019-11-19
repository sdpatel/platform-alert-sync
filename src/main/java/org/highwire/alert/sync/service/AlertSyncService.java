package org.highwire.alert.sync.service;

import org.highwire.alert.sync.AlertSyncRepository;
import org.highwire.alert.sync.AlertSyncViewRepository;
import org.highwire.alert.sync.domain.AlertSyncView;
import org.highwire.alert.sync.domain.AlertSyncMessage;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

@Slf4j
@Service
public class AlertSyncService {

  @Autowired
  private AlertSyncViewRepository alertSyncViewRepo;

  @Autowired
  private AlertSyncRepository alertSyncRepo;

  @Autowired
  private KafkaMessageService kafkaMessageService;

  private Gson gson = new GsonBuilder()
                          .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS a")
                          .create();

  @Value("${spring.kafka.producer.alert.sync.topic}")
  private String alertLegacySyncTopicName;

  public List<AlertSyncView> findAll() {
    return this.alertSyncViewRepo.findAll();
  }

  public String getAlertSyncMsgKey(AlertSyncView alert) {
    return String.format("%s:%s", Strings.nullToEmpty(alert.getContext()),
        alert.getId());
  }

  /**
   *  Sync Recent Alert changes, produce  alerts.legacy.sync msgs
   *
   * @return alertsync msg sent counts
   */
  @Transactional
  public int syncRecentAlerts() {
    List<AlertSyncView> alertSyncViewList = new ArrayList<>();

    // get alertsync entries from legacy alert DB
    alertSyncViewList = findAll();
    log.info(" Number of AlertSync entries found:  " + alertSyncViewList.size());
    // remove alert entries without context
    for (AlertSyncView alert : alertSyncViewList) {
      if (Strings.isNullOrEmpty(alert.getContext())) {
        alertSyncViewList.remove(alert);
      }
    }
    log.info(" Number of AlertSync entries to be synced:  " + alertSyncViewList.size());

    if (alertSyncViewList == null || alertSyncViewList.isEmpty()) {
      log.info("No Alerts to be synced at this time.");
    }

    int counter = 0;
    for (AlertSyncView alert : alertSyncViewList) {
      log.debug(" AlertSyncMsg entry: " + alert.toString());
      String key = "";
      boolean msgSent = true;
      try {
        // get alertsync kafka msg key
        key = getAlertSyncMsgKey(alert);
        log.debug(" AlertSyncMsg Key: " + key);
        final AlertSyncMessage asMsg = new AlertSyncMessage(alert);
        log.debug(" AlertSyncMsg: " + asMsg.toString());
        final String jsonObj = gson.toJson(asMsg);
        log.info(" AlertSyncMsg: " + jsonObj);

        // produce kafka msg for topic alertLegacySyncTopicName i.e. "alerts.legacy.sync"
        this.kafkaMessageService.sendMessageToKafka(alertLegacySyncTopicName, key, jsonObj);
        if (msgSent) {
          counter++;
          // delete entry from alert_sync table
          this.deleteAlertSyncById(alert.getId());
        }
      } catch(Exception e) {
        log.error(" Failed to produce msg for alertsync record " + alert.getId() + " " + e.getMessage());
        msgSent = false;
      }
    }
    log.info(" Number of alertSync reconds found: " + alertSyncViewList.size() +
                 " Number of legacy.alerts.sync topic msgs produced: " + counter);
    return counter;
  }

  @Transactional
  public void deleteAlertSyncById(int id) {
    this.alertSyncRepo.deleteAllByFieldAlertId(id);
  }


}
