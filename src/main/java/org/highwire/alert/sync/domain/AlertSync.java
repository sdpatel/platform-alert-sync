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
@Table(name="alert_sync")
public class AlertSync implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name="alert_id")
  private int id;

  @Column(name="trigger_action")
  private String triggerAction;

  @Column(name="trigger_table")
  private String triggerTable;

  @UpdateTimestamp
  private Timestamp timestamp;


}
