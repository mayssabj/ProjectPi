package tn.esprit.projet_pi.interfaces;

import tn.esprit.projet_pi.entity.Transaction;

import java.util.List;

public interface ITransaction {
    public Transaction createTransaction(Transaction transaction);
    public void deleteTransaction(Long transactionId);
    public Transaction updateTransaction(Long transactionId, Transaction transaction);
    public Transaction getTransactionById(Long transactionId);
    public List<Transaction> getTransactionsByType(String transactionType);
    public List<Transaction> getTransactionsByDateRange(String startDate, String endDate);
}
