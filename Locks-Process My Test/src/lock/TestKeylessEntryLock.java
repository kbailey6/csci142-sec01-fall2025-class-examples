package lock;

import java.util.Arrays;

public class TestKeylessEntryLock {

	public static void main(String[] args) {
		
		// Test Setup: A keyless entry lock with a physical key and a master code.
		int[] masterCode = {1, 2, 3, 4, 5, 6};
		KeylessEntryLock keylessLock = new KeylessEntryLock(7, masterCode); // Key value is 7
		
		// Test 1: Initial state and master code unlock
		System.out.println("Test 1: Master Code Unlock ");
		if (!keylessLock.isLocked()) {
			System.out.println("Error: New lock should be locked.");
		}
		
		String masterCodeString = "";
		for (int code : masterCode) {
			masterCodeString += code;
		}
		
		for (char c : masterCodeString.toCharArray()) {
			keylessLock.pushButton(c);
		}
		
		if (!keylessLock.isLocked()) {
			System.out.println("Success: Lock unlocked with correct master code.");
		} else {
			System.out.println("Error: Lock failed to unlock with master code.");
		}
		
		keylessLock.lock(); // Relock for next test
		
		// Test 2: Key unlocking
		System.out.println("\nTest 2: Physical Key Unlock ");
		if (keylessLock.insertKey(7)) {
			keylessLock.turn();
			if (keylessLock.unlock()) {
				System.out.println("Success: Lock unlocked with physical key.");
			} else {
				System.out.println("Error: Lock failed to unlock with physical key.");
			}
		} else {
			System.out.println("Error: Correct key could not be inserted.");
		}

		keylessLock.lock(); // Relock for next test
		
		// Test 3: Create and use a new user code
		System.out.println("\nTest 3: Create and Use User Code ");
		
		String newUserCode = "1111";
		
		// First, try to unlock with the new code, should fail
		for (char c : newUserCode.toCharArray()) {
			keylessLock.pushButton(c);
		}
		if (!keylessLock.isLocked()) {
			System.out.println("Error: Lock unlocked with a code that hasn't been added yet.");
		}
		
		// Now, add the code
		for (char c : (masterCodeString + "*" + "1" + newUserCode + newUserCode).toCharArray()) {
			keylessLock.pushButton(c);
		}
		
		keylessLock.lock(); // Relock before testing new code
		
		// Now, try to unlock with the new code again, should work
		for (char c : newUserCode.toCharArray()) {
			keylessLock.pushButton(c);
		}
		
		if (!keylessLock.isLocked()) {
			System.out.println("Success: Lock unlocked with newly added user code.");
		} else {
			System.out.println("Error: Lock failed to unlock with new user code.");
		}
		
		// Test 4: Delete a user code
		System.out.println("\nTest 4: Delete User Code ");
		
		keylessLock.lock(); // Relock
		
		for (char c : (masterCodeString + "*" + "2" + newUserCode + newUserCode).toCharArray()) {
			keylessLock.pushButton(c);
		}
		
		// Try to unlock with the now-deleted code, should fail
		keylessLock.lock();
		for (char c : newUserCode.toCharArray()) {
			keylessLock.pushButton(c);
		}
		
		if (keylessLock.isLocked()) {
			System.out.println("Success: Lock remained locked after user code was deleted.");
		} else {
			System.out.println("Error: Lock was unlocked with a deleted user code.");
		}
		
		// Test 5: Change Master Code
		System.out.println("\nTest 5: Change Master Code ");
		int[] newMasterCodeArray = {6, 5, 4, 3, 2, 1};
		String newMasterCodeString = "";
		for (int code : newMasterCodeArray) {
			newMasterCodeString += code;
		}
		
		for (char c : (masterCodeString + "*" + "3" + newMasterCodeString + newMasterCodeString).toCharArray()) {
			keylessLock.pushButton(c);
		}
		
		// Try to unlock with old master code, should fail
		keylessLock.lock();
		for (char c : masterCodeString.toCharArray()) {
			keylessLock.pushButton(c);
		}
		
		if (keylessLock.isLocked()) {
			System.out.println("Success: Lock remained locked with old master code.");
		} else {
			System.out.println("Error: Lock was unlocked with old master code.");
		}
		
		// Try to unlock with new master code, should succeed
		keylessLock.lock();
		for (char c : newMasterCodeString.toCharArray()) {
			keylessLock.pushButton(c);
		}
		
		if (!keylessLock.isLocked()) {
			System.out.println("Success: Lock unlocked with new master code.");
		} else {
			System.out.println("Error: Lock failed to unlock with new master code.");
		}
	}
}