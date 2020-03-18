package com.meritamerica.assignment4.transactions;

import java.util.Date;

import com.meritamerica.assignment4.accounts.BankAccount;

public class WithdrawTransaction extends Transaction{

	public WithdrawTransaction(BankAccount targetAccount, double amount) {
		super(targetAccount, targetAccount, amount, new Date());
	}
	
	public WithdrawTransaction(long sourceAccount, long targetAccount, double amount, Date transactionDate) {
		super(targetAccount, targetAccount, amount, transactionDate);
	}
	
}
