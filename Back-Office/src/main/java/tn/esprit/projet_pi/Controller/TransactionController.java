package tn.esprit.projet_pi.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.projet_pi.Service.TransactionService;
import tn.esprit.projet_pi.entity.Transaction;

import java.util.List;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // Create a new transaction
    @PostMapping("/add")
    public Transaction createTransaction(@RequestBody Transaction transaction) {
        return transactionService.createTransaction(transaction);
    }

    // Delete a transaction by ID
    @DeleteMapping("/delete/{transactionId}")
    public void deleteTransaction(@PathVariable Long transactionId) {
        transactionService.deleteTransaction(transactionId);
    }

    // Update an existing transaction
    @PutMapping("/update/{transactionId}")
    public Transaction updateTransaction(@PathVariable Long transactionId, @RequestBody Transaction transaction) {
        return transactionService.updateTransaction(transactionId, transaction);
    }

    // Get a transaction by ID
    @GetMapping("/get/{transactionId}")
    public Transaction getTransactionById(@PathVariable Long transactionId) {
        return transactionService.getTransactionById(transactionId);
    }

    // Get transactions by type
    @GetMapping("/byType/{transactionType}")
    public List<Transaction> getTransactionsByType(@PathVariable String transactionType) {
        return transactionService.getTransactionsByType(transactionType);
    }

    // Get transactions by date range
    @GetMapping("/byDateRange")
    public List<Transaction> getTransactionsByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return transactionService.getTransactionsByDateRange(startDate, endDate);
    }
}
