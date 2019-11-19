package org.highwire.alert.sync.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.sql.Timestamp;

@Data
@JsonSerialize
public class AlertSyncMessage {
  @JsonIgnore
  private static final long serialVersionUID = -8754701078182099224L;

  @JsonProperty("alert")
  private AlertMessage alert;

  @JsonProperty("triggerAction")
  private String triggerAction;

  @JsonProperty("triggerTable")
  private String triggerTable;

  @JsonProperty("timestamp")
  private Timestamp timestamp;

  public AlertSyncMessage(org.highwire.alert.sync.domain.AlertSyncView alertSyncView) {
    this.alert = new AlertMessage(alertSyncView);
    this.timestamp = alertSyncView.getTimestamp();
    this.triggerAction = alertSyncView.getTriggerAction();
    this.triggerTable = alertSyncView.getTriggerTable();
  }

}
