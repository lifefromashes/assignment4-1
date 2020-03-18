package com.meritamerica.assignment4.transactions;

import java.util.Date;

import com.meritamerica.assignment4.accounts.BankAccount;

public class DepositTransaction extends Transaction {
	
	public DepositTransaction() {};
	
	public DepositTransaction(BankAccount targetAccount, double amount) {
		super(targetAccount, targetAccount, amount, new Date());
	}
	
	public DepositTransaction(long sourceAccount, long targetAccount, double amount, Date transactionDate) {
		super(targetAccount, targetAccount, amount, transactionDate);
	}
	
	
	
	//super is (BankAccount sourceAccount, BankAccount targetAccount, double amount, Date transactionDate)

}
