package org.highwire.alert.sync;

import org.highwire.alert.sync.domain.AlertSyncView;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertSyncViewRepository extends JpaRepository<AlertSyncView, Integer> {

  @Override List<AlertSyncView> findAll();

}
