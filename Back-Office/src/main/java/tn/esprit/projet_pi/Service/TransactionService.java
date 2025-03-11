package tn.esprit.projet_pi.Service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.projet_pi.Repository.TransactionRepository;
import tn.esprit.projet_pi.entity.Transaction;
import tn.esprit.projet_pi.entity.TransactionStatus;
import tn.esprit.projet_pi.interfaces.ITransaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService implements ITransaction {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction createTransaction(Transaction transaction) {
        // Set the status to "PENDING" by default if no status is provided
        if (transaction.getStatus() == null) {
            transaction.setStatus(TransactionStatus.PENDING);
        }

        // Set the current date and time if no dateTransaction is provided
        if (transaction.getDateTransaction() == null) {
            transaction.setDateTransaction(LocalDateTime.now());
        }

        // Save the transaction to the repository
        return transactionRepository.save(transaction);
    }

    @Override
    public void deleteTransaction(Long transactionId) {
        // Check if the transaction exists
        Optional<Transaction> transactionOptional = transactionRepository.findById(transactionId);
        if (transactionOptional.isPresent()) {
            transactionRepository.delete(transactionOptional.get());
        } else {
            throw new RuntimeException("Transaction not found with ID: " + transactionId);
        }
    }

    @Override
    public Transaction updateTransaction(Long transactionId, Transaction updatedTransaction) {
        // Fetch the existing transaction from the repository
        Optional<Transaction> existingTransactionOptional = transactionRepository.findById(transactionId);
        if (existingTransactionOptional.isPresent()) {
            Transaction existingTransaction = existingTransactionOptional.get();

            // Update the fields of the existing transaction only if they are not null in the updatedTransaction
            if (updatedTransaction.getMontant() != null) {
                existingTransaction.setMontant(updatedTransaction.getMontant());
            }
            if (updatedTransaction.getStatus() != null) {
                existingTransaction.setStatus(updatedTransaction.getStatus());
            }
            if (updatedTransaction.getModePaiement() != null) {
                existingTransaction.setModePaiement(updatedTransaction.getModePaiement());
            }
            if (updatedTransaction.getReferencePaiement() != null) {
                existingTransaction.setReferencePaiement(updatedTransaction.getReferencePaiement());
            }
            if (updatedTransaction.getDetails() != null) {
                existingTransaction.setDetails(updatedTransaction.getDetails());
            }
            // Update other fields like abonnement if needed

            // Save and return the updated transaction
            return transactionRepository.save(existingTransaction);
        } else {
            throw new RuntimeException("Transaction not found with ID: " + transactionId);
        }
    }

    @Override
    public Transaction getTransactionById(Long transactionId) {
        // Fetch the transaction by its ID
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found with ID: " + transactionId));
    }

    @Override
    public List<Transaction> getTransactionsByType(String transactionType) {
        // Assuming transactionType corresponds to the status or mode of transaction
        return transactionRepository.findByStatus(TransactionStatus.valueOf(transactionType));
    }

    @Override
    public List<Transaction> getTransactionsByDateRange(String startDate, String endDate) {
        // You could add date range validation and logic here based on your requirements
        LocalDateTime start = LocalDateTime.parse(startDate);
        LocalDateTime end = LocalDateTime.parse(endDate);

        return transactionRepository.findByDateTransactionBetween(start, end);
    }
}
