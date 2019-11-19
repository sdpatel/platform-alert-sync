package org.highwire.alert.sync.domain;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Slf4j
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name="view_alert_sync")
public class AlertSyncView implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name="id")
  private int id;

  @Column(name="type")
  private String type;

  // frequency days
  @Column(name="frequency")
  private int frequency;

  @Column(name="scope")
  private String scope;

  @Column(name="context")
  private String context;

  @Column(name="active_status")
  private String status;

  @Column(name="email")
  private String email;

  @Column(name="trigger_action")
  private String triggerAction;

  @Column(name="trigger_table")
  private String triggerTable;

  @UpdateTimestamp
  private Timestamp timestamp;

}
