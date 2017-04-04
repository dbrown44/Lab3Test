package banking.primitive.core;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import banking.primitive.core.Account.State;

public class CheckingTest {

	private static AccountServer accountServer = null;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		accountServer = AccountServerFactory.getMe().lookup();
	}

	@Before
	public void setUp() throws Exception {
		accountServer.newAccount("Checking", "CheckingAcct", 100.0f);
		accountServer.newAccount("Savings", "SavingsAcct", 100.0f);
	}
	
	
	@Test
	public void testCheckingDeposit(){
		assertFalse(accountServer.getAccount("CheckingAcct").deposit(-10.0f));
		assertTrue(accountServer.getAccount("CheckingAcct").deposit(10.0f));
		accountServer.getAccount("CheckingAcct").setState(State.CLOSED);
		assertFalse(accountServer.getAccount("CheckingAcct").deposit(10.0f));
	}
	
	/**
	 * Withdraw. After 5 withdrawals a fee of $2 is charged per transaction. You may
	 * continue to withdraw an overdrawn account until the balance is below -$100. Withdrawing funds does not work on a closed account.
     *
     * @param float is the desired positive withdraw amount
     * @return boolean stating if the transaction was successful
	 */
	@Test
	public void testCheckingWithdraw(){
		for(int i = 0; i < 6; i++){
			accountServer.getAccount("CheckingAcct").withdraw(1.0f);
		}
		assertEquals(93f, accountServer.getAccount("SavingsAcct").getBalance(), 0.0f);
		
	}
	
	@Test
	public void testSavingsDeposit(){
		accountServer.getAccount("SavingsAcct").deposit(10.0f);
		assertEquals(109.5f, accountServer.getAccount("SavingsAcct").getBalance(), 0.0f);
		assertFalse(accountServer.getAccount("SavingsAcct").deposit(-10.0f));
		accountServer.getAccount("SavingsAcct").setState(State.CLOSED);
		assertFalse(accountServer.getAccount("SavingsAcct").deposit(10.0f));
	}

}
