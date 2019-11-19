package org.highwire.alert.sync;

import org.highwire.alert.sync.domain.AlertSync;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertSyncRepository extends JpaRepository<AlertSync, Integer> {

  @Modifying
  @Query(value = "delete from alert_sync where alert_id = ?1 ", nativeQuery = true)
  void deleteAllByFieldAlertId(Integer id);

  @Query (value = "select * from alert_sync where alert_id = ?1 ", nativeQuery = true)
  public List<AlertSync> findAllByFieldAlertId(Integer id);
}
