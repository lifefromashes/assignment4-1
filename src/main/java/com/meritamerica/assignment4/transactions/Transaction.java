package com.meritamerica.assignment4.transactions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.meritamerica.assignment4.MeritBank;
import com.meritamerica.assignment4.accounts.BankAccount;

public abstract class Transaction {
	
	BankAccount sourceAccount;
	BankAccount targetAccount;
	long sourceAccountID;
	long targetAccountID;
	double amount;
	Date transactionDate;
	
	public Transaction() {}
	
	public Transaction(BankAccount sourceAccount, BankAccount targetAccount, double amount, Date transactionDate) {
		this.sourceAccount = sourceAccount;
		this.targetAccount = targetAccount;
		this.amount = amount;
		this.transactionDate = transactionDate;
	}
	
	public Transaction(long sourceAccount, long targetAccount, double amount, Date transactionDate) {
		this.sourceAccountID = sourceAccount;
		this.targetAccountID = targetAccount;
		this.amount = amount;
		this.transactionDate = transactionDate;
	}
	
	
	
	public void findAccounts() {
		this.targetAccount = MeritBank.getBankAccount(this.targetAccountID);
		if(this.sourceAccountID == -1) {
			this.sourceAccount = targetAccount;
		} else {
			this.sourceAccount = MeritBank.getBankAccount(this.sourceAccountID);
		}
	}
	
	
	
	
	
	
	public static Transaction readFromString(String s) throws ParseException {
		Transaction transaction; // = new DepositTransaction(); ///
		
		
		
		
		
		String sourceString = "";
		String targetString = "";
		String amountString = "";
		String dateString = "";
		int position = 1;
		
		for (char c: s.toCharArray()) {
			if(c == ',') { position ++; continue; }
			if(position == 1) { sourceString += c; }
			if(position == 2) { targetString += c; }
			if(position == 3) { amountString += c; }
			if(position == 4) { dateString += c; }
		}
		
		
		if(sourceString == "" || targetString == "" || 
				amountString == "" || dateString == "" ||position != 4) {
			throw new NumberFormatException();
		}
		
		
		
		long sourceAccount = Long.parseLong(sourceString);
		long targetAccount = Long.parseLong(targetString);
		double amount = Double.parseDouble(amountString);
		Date newDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateString);
		
		
		
		if(sourceAccount == -1) {
			if(amount >= 0) {
				transaction = new DepositTransaction(sourceAccount, targetAccount, amount, newDate);
			} else {
				transaction = new WithdrawTransaction(sourceAccount, targetAccount, amount, newDate);
			}
		} else {
			transaction = new TransferTransaction(sourceAccount, targetAccount, amount, newDate);
		}
		
		
		return transaction;
	}
	
	public String writeToString() {
		String s = "";
		if(this.sourceAccount == this.targetAccount) {
			s += "-1,";
		} else {
			s += this.sourceAccountID + ",";
		}
		
		s += this.targetAccountID + ",";
		s += this.amount + ",";
		s += this.transactionDate + "\n";
		
		return s;
	}
	
	
	// begin getters and setters
	public BankAccount getSourceAccount() {
		return sourceAccount;
	}

	public void setSourceAccount(BankAccount sourceAccount) {
		this.sourceAccount = sourceAccount;
	}

	public BankAccount getTargetAccount() {
		return targetAccount;
	}

	public void setTargetAccount(BankAccount targetAccount) {
		this.targetAccount = targetAccount;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}
	// end getters and setters
	
	
	
	

}
