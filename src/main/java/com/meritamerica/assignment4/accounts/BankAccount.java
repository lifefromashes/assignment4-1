package com.meritamerica.assignment4.accounts;


import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.meritamerica.assignment4.MeritBank;
import com.meritamerica.assignment4.exceptions.ExceedsFraudSuspicionLimitException;
import com.meritamerica.assignment4.transactions.Transaction;

/**
 * This parent class will set the basic methods for each of the account
 * types.
 * 
 * @date 3/9/2020
 *
 */
public abstract class BankAccount {
	
	private double balance;
	private double interestRate;
	private long accountNumber;
	private Date accountOpenedOn;
	private List<Transaction> transactions;
	
	BankAccount(double balance, double interestRate){
		this.balance = balance;
		this.interestRate = interestRate;
		this.accountOpenedOn = new Date();
		this.accountNumber = MeritBank.getNextAccountNumber();
		transactions = new ArrayList <Transaction>();
	}
	
	
	BankAccount(double balance, double interestRate, Date accountOpenedOn, long accountNumber){
		this.balance = balance;
		this.interestRate = interestRate;
		this.accountOpenedOn = accountOpenedOn;
		this.accountNumber = accountNumber;
		transactions = new ArrayList <Transaction>();
	}
	
	/**
	 * Withdraw funds from the account
	 * Rejects negative numbers and overdrafts
	 * 
	 * @param amount double, the amount to withdraw
	 * @return true if the transaction took place
	 */
	public boolean withdraw(double amount) throws ExceedsFraudSuspicionLimitException { 
		// throws NegativeAmountException, 
			//ExceedsAvailableBalanceException, ExceedsFraudSuspicionLimitException {
			if (amount > this.balance) {
				return false;
				//throw new ExceedsAvailableBalanceException();
			}
			if (amount < 0) {
				return false;
				//throw new NegativeAmountException();
			}
			if(amount > 1000) {
				return false;
				//MeritBank.getFraudQueue().addTransaction(t);
				//throw new ExceedsFraudSuspicionLimitException();
			}
			this.balance = this.balance - amount; 
			return true; 

	}
	
	/**
	 * Add funds to the account
	 * Rejects negative numbers
	 * 
	 * @param amount double, the amount to deposit
	 * @returns true if the transaction took place
	 */
	public boolean deposit(double amount) throws ExceedsFraudSuspicionLimitException { 
			// throws NegativeAmountException, 
			//ExceedsAvailableBalanceException, ExceedsFraudSuspicionLimitException {
		if (amount < 0) {
			//throw new NegativeAmountException();
			return false;
		}
		this.balance = this.balance + amount;
		return true;
	}
	
	/**
	 * Calculates the total value that will be in the account after interest
	 * as accrued for a number of years
	 * 
	 * due to concerns with the Math.pow bug, it is avoided in this method
	 * 
	 * @param years int, the time the account collects interest for
	 * @return double, the projected total value of the account 
	 */
	public double futureValue(int years) throws ExceedsFraudSuspicionLimitException {
		if(years < 1) {
			throw new ExceedsFraudSuspicionLimitException();
		}
		
		/*
		float irate = 1 + (float)this.interestRate;
		float cr = 1;
		for(int i=0; i<years; i++) {
			cr *= irate;
		}
		return this.balance * cr;
		*/
		
		//return 100.0 * Math.pow(1 + this.interestRate, years);
		
		return recursiveFutureValue(years);
		//return recursiveFutureValueFloat(years);
	}
	public double recursiveFutureValue(int years) {
		//if(years == 1) { return this.balance * (1 + this.interestRate); } 
		//return recursiveFutureValue(years - 1) * (1 + this.interestRate);
		
		if(years == 0) { return this.balance; } 
		return (recursiveFutureValue(years - 1) * (1 + this.interestRate));
	}
	
	public float recursiveFutureValueFloat(int years) {
		//if(years == 1) { return this.balance * (1 + this.interestRate); } 
		//return recursiveFutureValue(years - 1) * (1 + this.interestRate);
		
		if(years == 0) { return (float)this.balance; } 
		return (recursiveFutureValueFloat(years - 1) * (1 + (float)this.interestRate));
	}
	
	
	/**
	 * The test cases are calling this with no argument, is that a 
	 * mistake?
	 * overloaded to handle 
	 */
	public double futureValue() throws ExceedsFraudSuspicionLimitException {
		throw new ExceedsFraudSuspicionLimitException();
		//return futureValue(5); 
	}
	
	/**
	 * Output a general message for when a transaction fails
	 */
	private void printError() {
		System.out.println("Error, unable to process transaction.");
	}
	
	
	public void addTransaction(Transaction t) {
		this.transactions.add(t);
	}
	
	public List<Transaction> getTransactions() {
		return this.transactions;
	}
	
	
	
	
	
	/**
	 * Represents this object as a string of text to save in a file
	 * 
	 * See the MeritBank.readFromFile method for information formatting
	 * @return the created String
	 */
	public String writeToString() {
		String s = "";
		s += this.accountNumber + ",";
		s += this.balance + ",";
		
		DecimalFormat df = new DecimalFormat("0.0000");
		s += df.format(this.interestRate) + ",";
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String date = sdf.format(this.accountOpenedOn);
		s += date;
		
		
		
		return s;
	}
	
	// begin getters
	public long getAccountNumber() {
		return this.accountNumber;
	}
	
	public double getBalance() {
		return this.balance;
	}
	
	public double getInterestRate() {
		return this.interestRate;
	}
	
	public Date getOpenedOn() {
		return this.accountOpenedOn;
	}
	// end getters
}
