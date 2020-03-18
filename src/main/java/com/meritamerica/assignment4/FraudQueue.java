package com.meritamerica.assignment4;

import java.util.ArrayList;
import java.util.List;

import com.meritamerica.assignment4.transactions.Transaction;

public class FraudQueue {

	List<Transaction> transactions;
	
	public FraudQueue() {
		transactions = new ArrayList <Transaction>();
	}
	
	public void addTransaction(Transaction t) {
		this.transactions.add(t);
	}
	
	public Transaction getTransaction() {
		
		Transaction t = transactions.get(0);
		if(t != null) { transactions.remove(0); }
		
		return t;
	}
	
	public List<Transaction> getTransactions() {
		return transactions;
	}
	
	public String writeToString() {
		String s = "";
		for(Transaction t : transactions) {
			s += t.writeToString();
		}
		
		return s;
	}
}
