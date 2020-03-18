package com.meritamerica.assignment4;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;

import com.meritamerica.assignment4.accounts.BankAccount;
import com.meritamerica.assignment4.accounts.CDAccount;
import com.meritamerica.assignment4.accounts.CheckingAccount;
import com.meritamerica.assignment4.accounts.SavingsAccount;
import com.meritamerica.assignment4.exceptions.ExceedsAvailableBalanceException;
import com.meritamerica.assignment4.exceptions.ExceedsFraudSuspicionLimitException;
import com.meritamerica.assignment4.exceptions.NegativeAmountException;
import com.meritamerica.assignment4.transactions.Transaction;


/**
 * This class is the main point for storing bank information
 * All methods and variables should be static 
 * 
 * @date 3/5/2020
 *
 */

public class MeritBank {
	
	private static AccountHolder[] accountHolders = new AccountHolder[0];
	private static CDOffering[] cdOfferings = new CDOffering[0];
	private static FraudQueue fraudQueue;
	private static long nextAccountNumber = 1234567;
	
	/**
	 * Call this method from the App to save a created account holder 
	 *      to the bank's list
	 *       
	 * @param accountHolder the AccountHolder object to save
	 */
	public static void addAccountHolder(AccountHolder accountHolder) {
		//determine the size to make the new account holder array
		int arraySize = accountHolders.length;
				
		AccountHolder[] temp = new AccountHolder[arraySize + 1];
		for(int i=0; i<arraySize; i++) {
			temp[i] = accountHolders[i];
		}
		temp[arraySize] = accountHolder;
		accountHolders = temp;	
		
	}
	
	// begin getters
	public static AccountHolder[] getAccountHolders() {
		return accountHolders;
	}
	
	public static CDOffering[] getCDOfferings() {
		return cdOfferings;
	}
	
	public static FraudQueue getFraudQueue() {
		return fraudQueue;
	}
	
	/**
	 * find an existing account with the chosen number
	 * 
	 * @param id long account number to look for
	 * @return account matching the number, or null if none found
	 */
	public static BankAccount getBankAccount(long id) {
		
		for(int i=0; i<accountHolders.length; i++) {
			for(int j=0; j<accountHolders[i].getCheckingAccounts().length; j ++) {
				if(accountHolders[i].getCheckingAccounts()[j].getAccountNumber() == id) {
					return accountHolders[i].getCheckingAccounts()[j];
				}
			}
			
			for(int j=0; j<accountHolders[i].getSavingsAccounts().length; j ++) {
				if(accountHolders[i].getSavingsAccounts()[j].getAccountNumber() == id) {
					return accountHolders[i].getSavingsAccounts()[j];
				}
			}
			
			for(int j=0; j<accountHolders[i].getCDAccounts().length; j ++) {
				if(accountHolders[i].getCDAccounts()[j].getAccountNumber() == id) {
					return accountHolders[i].getCDAccounts()[j];
				}
			}
		}
		
		return null;
	}
	// end getters
	
	/**
	 * Method to find the best CD account for a customer
	 *   Because the assignment details did not specify any length of time
	 *   this only return the best interest rate, regardless of time
	 *   
	 * @param depositAmmount not used in calculation =(
	 * @return the cdOffering with the best interest rate
	 */
	static CDOffering getBestCDOffering(double depositAmmount) {
		if(cdOfferings == null) {return null;}
		double bestValue = 0;
		int bestIndex = -1;
		for(int i=0; i<cdOfferings.length; i++) {
			if(cdOfferings[i].getInterestRate() > bestValue) {
				bestValue = cdOfferings[i].getInterestRate();
				bestIndex = i;
			}
		}
		
		return cdOfferings[bestIndex];
	}
	
	/**
	 * Method to find the 2nd best CD account for a customer
	 *   Because the assignment details did not specify any length of time
	 *   this only return the best interest rate, regardless of time
	 *   
	 *   calls bestCDOffering and rejects object equal to that
	 *   
	 * @param depositAmmount not used in calculation =(
	 * @return the cdOffering with the best interest rate
	 */
	public static CDOffering getSecondBestCDOffering(double depositAmount) {
		if(cdOfferings == null) {return null;}
		CDOffering best = getBestCDOffering(depositAmount);
		
		double secondBestValue = 0;
		int secondBestIndex = -1;
		for(int i=0; i<cdOfferings.length; i++) {
			if(cdOfferings[i].getInterestRate() > secondBestValue 
					&& !best.equals(cdOfferings[i])) {
				secondBestValue = cdOfferings[i].getInterestRate();
				secondBestIndex = i;
			}
		}
		if(secondBestIndex == -1) { return null; }
		return cdOfferings[secondBestIndex];
	}
	
	/**
	 * Erase all existing CDOfferings
	 */
	public static void clearCDOfferings() {
		cdOfferings = null;
	}
	
	/**
	 * Define which CDOfferings are offered
	 * 
	 * @param offerings An array of CDOfferings to make available to the accountHolders
	 */
	public static void setCDOfferings(CDOffering[] offerings) {
		//determine the size to make the offerings array
		int arraySize = 0;
		for(int i=0; i<offerings.length; i++) {
			if(offerings[i] == null) {
				break;
			}
			arraySize ++;
		}
		
		cdOfferings = new CDOffering[arraySize];
		for(int i=0; i<arraySize; i++) {
			cdOfferings[i] = offerings[i];
		}
	}
	
	public static long getNextAccountNumber() {
		return nextAccountNumber;
	}

	public static void setNextAccountNumber(long accountNumber) {
		nextAccountNumber = accountNumber;
	}
	
	/**
	 * total the value of all accounts held by bank's account holders
	 * 
	 * @return sum A double value of the combined accounts 
	 */
	static double totalBalances() {
		double sum = 0;
		for(AccountHolder ah : accountHolders) {
			if(ah == null) {break;}
			
			for(CheckingAccount account: ah.getCheckingAccounts()) {
				try {
					sum += account.getBalance();
				} catch (NullPointerException expected) {
					//if account holder has less CheckingAccounts than other 
					//kinds this will catch. No additional handling needed
				}
			}
			for(SavingsAccount account: ah.getSavingsAccounts()) {
				try {
					sum += account.getBalance();
				} catch (NullPointerException expected) {
					//if account holder has less SavingsAccounts than other 
					//kinds this will catch. No additional handling needed
				}
			}
			for(CDAccount account: ah.getCDAccounts()) {
				try {
					sum += account.getBalance();
				} catch (NullPointerException expected) {
					//if account holder has less CDAccounts than other 
					//kinds this will catch. No additional handling needed
				}
			}
		}
		
		
		return sum;
	}
	
	/**
	 * Calculates the value of an account with interest applied
	 * 
	 * @param presentValue a double indicating the starting value of the account
	 * @param interestRate a double indicating the interest rate to apply
	 * @param term an int indicating the number of years the interest will be applied for
	 * 
	 * @return a double of the projected account balance
	 */
	//static double futureValue(double presentValue, double interestRate, int term) {
	//	double futureValue = presentValue * (Math.pow(1+ presentValue, term));
	//	return futureValue;
	//}
	
	/**
	 * erase the current arrays and counters to their start point
	 * 
	 * call this before reading from a file
	 */
	static void clearMemory() {
		accountHolders = new AccountHolder[0];
		cdOfferings = new CDOffering[0];
		fraudQueue = new FraudQueue();
	}
	
	
	
	
	
	
	
	public static boolean processTransaction(Transaction t) throws NegativeAmountException, 
			ExceedsAvailableBalanceException, ExceedsFraudSuspicionLimitException {
		
		if( t.getSourceAccount() != t.getTargetAccount() ) { //transfer transactions
			if(t.getAmount() < 0 ) { 
				throw new NegativeAmountException(); 
			}
			if(t.getAmount() > t.getSourceAccount().getBalance() ) {
				throw new ExceedsAvailableBalanceException();
			}
		}
		
		if(t.getAmount() < 0 && Math.abs( t.getAmount() ) > t.getSourceAccount().getBalance()) {
			throw new ExceedsAvailableBalanceException();
		}
		
		if(Math.abs( t.getAmount() ) > 1000) {
			fraudQueue.addTransaction(t);
			throw new ExceedsFraudSuspicionLimitException();
		}
		
		if(t.getSourceAccount() == t.getTargetAccount()) {
			if(t.getAmount() > 0) { 
				t.getTargetAccount().deposit(t.getAmount() );
				return true;
			}
			if(t.getAmount() < 0) { 
				t.getTargetAccount().withdraw(t.getAmount() );
				return true;
			}
		} else {
			t.getSourceAccount().withdraw(t.getAmount() );
			t.getTargetAccount().deposit(t.getAmount() );
			return true;
		}
		
		
		return false;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * load saved information- customers, accounts, offerings, etc
	 * 
	 */
	static boolean readFromFile(String fileName) {
		clearMemory();
		
		try {
			FileReader reader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(reader);	
			
			AccountHolder loadedHolder = new AccountHolder();		
			String line; // current line from the file
			
			line = bufferedReader.readLine(); // read account number
			MeritBank.setNextAccountNumber( Long.parseLong(line) );
			System.out.println("next account #: " + line);
			
			line = bufferedReader.readLine(); // read number of cd offerings
			int totalCDO = Integer.parseInt(line);
			CDOffering[] loadedCDOfferings = new CDOffering[totalCDO];
			System.out.println("# of CD offerings: " + line);
			
			for(int i=0; i < totalCDO; i++) {
				line = bufferedReader.readLine(); // read cd offering
				loadedCDOfferings[i] = CDOffering.readFromString(line);
				System.out.println("CD offering: " + line);
			}
			setCDOfferings(loadedCDOfferings);
			
			line = bufferedReader.readLine(); // read number of account holders
			int totalAccountHolders = Integer.parseInt(line);
			System.out.println("# of Account Holders: " + line);
			
			for(int i=0; i < totalAccountHolders; i++) {
				
				line = bufferedReader.readLine(); // read account holder
				loadedHolder = AccountHolder.readFromString(line);
				addAccountHolder(loadedHolder);
				System.out.println("Account Holder: " + line);
				
				line = bufferedReader.readLine(); // read number of checking accounts
				int totalChecking = Integer.parseInt(line);
				System.out.println("# of Checking Accounts: " + line);
				
				for(int j=0; j < totalChecking; j++) {
					line = bufferedReader.readLine(); // read checking accounts
					CheckingAccount c = CheckingAccount.readFromString(line);
					loadedHolder.addCheckingAccount(c);
					System.out.println("Account: " + line);
					
					
					line = bufferedReader.readLine();// read number of transactions
					int totalTransactions = Integer.parseInt(line);
					System.out.println("  # of transactions: " + line);
					
					for(int k=0; k<totalTransactions; k++) {
						line = bufferedReader.readLine();// read transaction info
						Transaction t = Transaction.readFromString(line);
						
						loadedHolder.getCheckingAccounts()[j].addTransaction(t);
						System.out.println("  Record: " + line);
					}
					
					
				} // end of load checking accounts
				
				
				line = bufferedReader.readLine(); // read number of savings accounts
				int totalSavings = Integer.parseInt(line);
				System.out.println("# of Savings Accounts: " + line);
				
				for(int j=0; j < totalSavings; j++) {
					line = bufferedReader.readLine(); // read savings accounts
					SavingsAccount s = SavingsAccount.readFromString(line);
					loadedHolder.addSavingsAccount(s);
					System.out.println("Account: " + line);
					
					
					line = bufferedReader.readLine();// read number of transactions
					int totalTransactions = Integer.parseInt(line);
					System.out.println("  # of transactions: " + line);
					
					for(int k=0; k<totalTransactions; k++) {
						line = bufferedReader.readLine();// read transaction info
						Transaction t = Transaction.readFromString(line);
						
						loadedHolder.getSavingsAccounts()[j].addTransaction(t);
						System.out.println("  Record: " + line);
					}
					
				} // end of load savings accounts
				
				
				
				line = bufferedReader.readLine(); // read number of cd accounts
				int totalCD = Integer.parseInt(line);
				System.out.println("# of CD Accounts: " + line);
				
				
				for(int j=0; j < totalCD; j++) {
					line = bufferedReader.readLine(); // read cd accounts
					CDAccount s = CDAccount.readFromString(line);
					loadedHolder.addCDAccount(s);
					System.out.println("Account: " + line);
					
					
					line = bufferedReader.readLine();// read number of transactions
					int totalTransactions = Integer.parseInt(line);
					System.out.println("  # of transactions: " + line);
					
					for(int k=0; k<totalTransactions; k++) {
						line = bufferedReader.readLine();// read transaction info
						Transaction t = Transaction.readFromString(line);
						
						loadedHolder.getCDAccounts()[j].addTransaction(t);
						System.out.println("  Record: " + line);
					}
				} // end of load cd accounts
				
				
			} // end of load account holders
			
			line = bufferedReader.readLine();// read number of pending possible fraud transaction
			int totalTransactions = Integer.parseInt(line);
			System.out.println("  # of pending fraud review: " + line);
			
			
			
			for(int i=0; i < totalTransactions; i++) {
				line = bufferedReader.readLine(); // read fraud transaction
				Transaction t = Transaction.readFromString(line);
				
				fraudQueue.addTransaction(t);
				System.out.println("Review Transaction: " + line);
			}
			
			
			
			//after everything is loaded, loop through all transactions exchanging id numbers for account objects
			for(AccountHolder ah : accountHolders) {
				for(CheckingAccount ch : ah.getCheckingAccounts() ) {
					for(Transaction t : ch.getTransactions() ) {
						t.findAccounts();
					}
				}
				for(SavingsAccount sa : ah.getSavingsAccounts() ) {
					for(Transaction t : sa.getTransactions() ) {
						t.findAccounts();
					}
				}
				for(CDAccount cd : ah.getCDAccounts() ) {
					for(Transaction t : cd.getTransactions() ) {
						t.findAccounts();
					}
				}
			}
			
			
			
			bufferedReader.close();
			return true;
		} catch (Exception e) {
			System.out.println("exception while reading");
		}
		return false;
	}
	
	
	/**
	 * Save current information in memory to a text file for future access
	 * 
	 * @param fileName
	 * @return true if successful
	 */
	static boolean writeToFile(String fileName) {
		try {
			String s = "";
			s += getNextAccountNumber() + "\n";
			s += getCDOfferings().length + "\n";
			
			for(int i=0; i < getCDOfferings().length; i++) {
				s += cdOfferings[i].writeToString() + "\n";
			}
			
			s += getAccountHolders().length + "\n";
			
			
			
			for(int i=0; i < getAccountHolders().length; i ++) {
				s += accountHolders[i].writeToString() + "\n";
				
				s += accountHolders[i].getCheckingAccounts().length + "\n";
				for(int j=0; j < accountHolders[i].getCheckingAccounts().length; j ++) {
					s += accountHolders[i].getCheckingAccounts()[j].writeToString() + "\n";
					
					s += accountHolders[i].getCheckingAccounts()[j].getTransactions().size() + "\n";
					for(int k=0; k < accountHolders[i].getCheckingAccounts()[j].getTransactions().size(); k ++) {
						s += accountHolders[i].getCheckingAccounts()[j].getTransactions().get(k).writeToString();
					}
					
				}
				
				
				s += accountHolders[i].getSavingsAccounts().length + "\n";
				for(int j=0; j < accountHolders[i].getSavingsAccounts().length; j ++) {
					s += accountHolders[i].getSavingsAccounts()[j].writeToString() + "\n";
					
					s += accountHolders[i].getSavingsAccounts()[j].getTransactions().size() + "\n";
					for(int k=0; k < accountHolders[i].getSavingsAccounts()[j].getTransactions().size(); k ++) {
						s += accountHolders[i].getSavingsAccounts()[j].getTransactions().get(k).writeToString();;
					}
				}
				
				s += accountHolders[i].getCDAccounts().length + "\n";
				for(int j=0; j < accountHolders[i].getCDAccounts().length; j ++) {
					s += accountHolders[i].getCDAccounts()[j].writeToString() + "\n";
					
					s += accountHolders[i].getCDAccounts()[j].getTransactions().size() + "\n";
					for(int k=0; k < accountHolders[i].getCDAccounts()[j].getTransactions().size(); k ++) {
						s += accountHolders[i].getCDAccounts()[j].getTransactions().get(k).writeToString();
					}
				}
				
			}
			s += fraudQueue.getTransactions().size() + "\n";
			s += fraudQueue.writeToString();
			
			System.out.println(s);
			System.out.println(".....");
			

			
			File file = new File(fileName);
			
			if( file.createNewFile() ) {
				System.out.println("newwwwwwwwwwwwwwww");
			} else {
				System.out.println("olllllldddddddddd");
			}
			
			
			FileWriter writer = new FileWriter(fileName, false);
            writer.write(s);
            writer.close();
			
			return true;
		} catch (Exception e) {
			System.out.println("error writing file");
		}
		
		return false;
	}
	
	/**
	 * Sorts based on combined value of all accounts
	 * See compareTo in AccountHolder for more info 
	 * 
	 * @return the sorted array of AccountHolders
	 */
	static AccountHolder[] sortAccountHolders() {
		Arrays.sort(accountHolders);
		return accountHolders;
	}
	
	

}
