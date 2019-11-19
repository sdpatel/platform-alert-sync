package org.highwire.alert.sync.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Strings;
import lombok.Data;

@Data
@JsonSerialize
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlertMessage {

  @JsonIgnore
  private static final long serialVersionUID = -8754701078182099224L;

  public AlertMessage(org.highwire.alert.sync.domain.AlertSyncView alertSyncView) {
    this.id = alertSyncView.getId();
    this.type = alertSyncView.getType();
    if (alertSyncView.getFrequency() > 0) {
      if (alertSyncView.getFrequency() > 0 && alertSyncView.getFrequency() < 7) {
        this.frequency = "daily";
      } else if (alertSyncView.getFrequency() >= 7 && alertSyncView.getFrequency() < 15) {
        this.frequency = "weekly";
      } else if (alertSyncView.getFrequency() >= 15 && alertSyncView.getFrequency() < 30) {
        this.frequency = "biweekly";
      } else if (alertSyncView.getFrequency() >= 30 && alertSyncView.getFrequency() < 90) {
        this.frequency = "monthly";
      } else if (alertSyncView.getFrequency() >= 90 && alertSyncView.getFrequency() < 365) {
        this.frequency = "quarterly";
      } else {
        this.frequency = "yearly";
      }
    } else {
      this.frequency = "";
    }
    this.scope = alertSyncView.getScope();
    this.context = alertSyncView.getContext();
    if (!Strings.isNullOrEmpty(alertSyncView.getStatus())) {
      if (alertSyncView.getStatus() == "n") {
        this.status = "inactive";
      } else if (alertSyncView.getStatus() == "p") {
        this.status = "paused";
      } else {
        this.status = "active";
      }
    } else {
      this.status = "active";
    }
    this.email = alertSyncView.getEmail();
  }

  private int id;

  private String type;

  private String frequency;

  private String scope;

  private String context;

  private String status;

  private String email;

}
