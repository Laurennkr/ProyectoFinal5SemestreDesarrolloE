package edu.usta.groccy.repository;

import edu.usta.groccy.entity.Alert;
import edu.usta.groccy.enums.AlertStatus;
import edu.usta.groccy.enums.AlertType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {

    List<Alert> findAllByStatus(AlertStatus status);

    List<Alert> findAllByTypeAndStatus(AlertType type, AlertStatus status);
}