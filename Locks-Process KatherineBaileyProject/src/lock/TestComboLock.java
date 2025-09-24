package lock;

public class TestComboLock {

	public static void main(String[] args) {
		
		// Test 1: Valid combination and successful unlock
		System.out.println("Test 1: Valid Combination and Unlock ");
		ComboLock comboLock1 = new ComboLock(4, 2, 8); 
		if (!comboLock1.isLocked()) {
			System.out.println("Error: New lock should be locked.");
		}
		
		comboLock1.turnRight(4);
		comboLock1.turnLeft(2);
		comboLock1.turnRight(8);
		
		if (comboLock1.unlock()) {
			System.out.println("Success: Lock unlocked with correct combination.");
		} else {
			System.out.println("Error: Lock failed to unlock with correct combination.");
		}
		
		if (comboLock1.isLocked()) {
			System.out.println("Error: Lock should be unlocked after successful attempt.");
		}
		
		// Test 2: Incorrect combination, unlock should fail
		System.out.println("\nTest 2: Incorrect Combination ");
		ComboLock comboLock2 = new ComboLock(1, 3, 5);
		comboLock2.turnRight(1);
		comboLock2.turnLeft(3);
		comboLock2.turnRight(6); // Incorrect last number
		
		if (comboLock2.unlock()) {
			System.out.println("Error: Lock unlocked with incorrect combination.");
		} else {
			System.out.println("Success: Lock remained locked with incorrect combination.");
		}
		
		// Test 3: Reset functionality
		System.out.println("\nTest 3: Reset Functionality ");
		ComboLock comboLock3 = new ComboLock(12, 14, 16);
		comboLock3.turnRight(12);
		comboLock3.turnLeft(15); // Intentional incorrect number
		comboLock3.turnRight(16);
		
		// This attempt should fail
		comboLock3.unlock();
		
		if (!comboLock3.isLocked()) {
			System.out.println("Error: Lock should be locked after failed attempt.");
		}
		
		comboLock3.reset();
		if (!comboLock3.isReset()) {
			System.out.println("Error: Reset method failed to clear the attempt.");
		} else {
			System.out.println("Success: Reset method worked correctly.");
		}
	}
}