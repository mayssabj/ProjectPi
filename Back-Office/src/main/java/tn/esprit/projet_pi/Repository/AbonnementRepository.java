package tn.esprit.projet_pi.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tn.esprit.projet_pi.entity.Abonnement;

import java.util.List;
import java.util.Optional;


public interface AbonnementRepository  extends JpaRepository<Abonnement, Long> {
    Optional<Abonnement> findByConfirmationCode(String confirmationCode);
    @Query("SELECT COUNT(a) FROM Abonnement a WHERE a.abonnementStatus = 'ACTIVE'")
    long countActiveSubscriptions();

    @Query("SELECT COUNT(a) FROM Abonnement a WHERE a.abonnementStatus = 'PENDING'")
    long countPendingSubscriptions();

    @Query("SELECT COUNT(a) FROM Abonnement a WHERE a.abonnementStatus = 'EXPIRED'")
    long countExpiredSubscriptions();

    @Query("SELECT SUM(a.cout) FROM Abonnement a WHERE a.abonnementStatus = 'ACTIVE'")
    Double calculateTotalRevenue();

    @Query("SELECT a.typeAbonnement, COUNT(a) FROM Abonnement a GROUP BY a.typeAbonnement")
    List<Object[]> countBySubscriptionType();

    @Query("SELECT FUNCTION('MONTH', a.dateDebut) as month, COUNT(a) FROM Abonnement a GROUP BY month")
    List<Object[]> countMonthlySubscriptions();

}
