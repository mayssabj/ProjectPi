package tn.esprit.projet_pi.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.projet_pi.entity.Transaction;
import tn.esprit.projet_pi.entity.TransactionStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByDateTransactionBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Transaction> findByStatus(TransactionStatus status);
}
