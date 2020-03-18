package com.meritamerica.assignment4.accounts;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Each instance of this class represents a savings account 
 * Every savings account must be related to a customer
 * 
 * @date 3/2/2020
 * @version 1.0
 */

public class SavingsAccount extends BankAccount{
	
	static final double INTEREST_RATE =.01;
	
	//constructor
	public SavingsAccount (double openingBalance) {
		super(openingBalance, INTEREST_RATE);
	}
	
	public SavingsAccount (BankAccount bankAccount) {
		super(bankAccount.getBalance(), bankAccount.getInterestRate(), bankAccount.getOpenedOn(), bankAccount.getAccountNumber());
	}
	
	public SavingsAccount(double balance, double interestRate, Date openedOn, long accountNumber) {
		super(balance, interestRate, openedOn, accountNumber);
	}
	
	/**
	 * Turns a string of text loaded from a file into a Bank Account object
	 * 
	 * See the MeritBank.readFromFile method for information formatting
	 * @return the created object
	 */
	public static SavingsAccount readFromString(String accountData) throws ParseException {
		String accountNumberString = "";
		String balanceString = "";
		String rateString = "";
		String dateString = "";
		int position = 1;
		
		for (char c: accountData.toCharArray()) {
			if(c == ',') { position ++; continue; }
			if(position == 1) { accountNumberString += c; }
			if(position == 2) { balanceString += c; }
			if(position == 3) { rateString += c; }
			if(position == 4) { dateString += c; }
		}
		
		if(accountNumberString == "" || balanceString == "" || 
				rateString == "" || dateString == "" ||position != 4) {
			throw new NumberFormatException();
		}
		
		long newAccountNumber = Long.parseLong(accountNumberString);
		double newBalance = Double.parseDouble(balanceString);
		double newRate = Double.parseDouble(rateString);
		Date newDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateString);
		
		//MeritBank.setNextAccountNumber(newAccountNumber); //
		SavingsAccount newAccount = new SavingsAccount(newBalance, newRate, newDate, newAccountNumber);
		
		return newAccount;
	}
	
	/**
	 * Condenses account info into a String for output
	 * 
	 * @return a String of the account information, ready for outputting
	 */
	public String toString() {
		DecimalFormat format = new DecimalFormat("0.00");
		String string = "";
		string += "Savings Account Balance: $";
		string += format.format(getBalance()) + "\n";
		string += "Savings Account Interest Rate: ";
		string += format.format(getInterestRate()) + "\n";
		string += "Savings Account Balance in 3 years: $";
		//string += format.format(futureValue(3));
		
		return string; 
	}
}