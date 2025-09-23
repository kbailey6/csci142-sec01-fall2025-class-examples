package lock;

import java.util.HashMap;
import java.util.Map;

public class KeylessEntryLock extends KeyLock {
	
	public static final int MAX_NUM_USER_CODES = 10;
	public static final int USER_CODE_LENGTH = 4;
	public static final int MASTER_CODE_LENGTH = 6;

	private int[] masterCode;
	private Map<String, Boolean> userCodes; // Key is the user code string
	private String entryBuffer = ""; // To store the sequence of button presses
	
	public KeylessEntryLock(int keyValue, int[] initialMasterCode) {
		super(keyValue);
		if (initialMasterCode.length != MASTER_CODE_LENGTH) {
			throw new IllegalArgumentException("Master code must be 6 digits.");
		}
		this.masterCode = initialMasterCode;
		this.userCodes = new HashMap<>();
	}

	public boolean pushButton(char button) {
		entryBuffer += button;
		
		// Unlocking mechanism
		if (entryBuffer.length() == MASTER_CODE_LENGTH && isMasterCode(entryBuffer)) {
			unlock();
			entryBuffer = "";
			return true;
		}
		if (entryBuffer.length() == USER_CODE_LENGTH && isUserCode(entryBuffer)) {
			unlock();
			entryBuffer = "";
			return true;
		}

		// Handle command sequences
		handleCommand();
		
		return true;
	}
	
	private boolean isMasterCode(String code) {
		if (code.length() != MASTER_CODE_LENGTH) return false;
		
		for (int i = 0; i < MASTER_CODE_LENGTH; i++) {
			if (Integer.parseInt(String.valueOf(code.charAt(i))) != masterCode[i]) {
				return false;
			}
		}
		return true;
	}
	
	private boolean isUserCode(String code) {
		return userCodes.containsKey(code);
	}
	
	private void handleCommand() {
		// This is a simplified state machine. A real one would be more robust.
		// Check for commands like "6DPC*1", "6DPC*2", etc.
		
		// Create new 4DUC: 6DPC*1<New 4DUC><Repeat 4DUC>
		String createCodePattern = String.valueOf(arrayToString(masterCode) + "*" + "1");
		if (entryBuffer.startsWith(createCodePattern) && entryBuffer.length() == createCodePattern.length() + 2 * USER_CODE_LENGTH) {
			String newCode1 = entryBuffer.substring(createCodePattern.length(), createCodePattern.length() + USER_CODE_LENGTH);
			String newCode2 = entryBuffer.substring(createCodePattern.length() + USER_CODE_LENGTH);
			if (newCode1.equals(newCode2) && userCodes.size() < MAX_NUM_USER_CODES) {
				userCodes.put(newCode1, true);
			}
			entryBuffer = "";
		}

		// Delete 4DUC: 6DPC*2<Existing 4DUC><Repeat 4DUC>
		String deleteCodePattern = String.valueOf(arrayToString(masterCode) + "*" + "2");
		if (entryBuffer.startsWith(deleteCodePattern) && entryBuffer.length() == deleteCodePattern.length() + 2 * USER_CODE_LENGTH) {
			String existingCode1 = entryBuffer.substring(deleteCodePattern.length(), deleteCodePattern.length() + USER_CODE_LENGTH);
			String existingCode2 = entryBuffer.substring(deleteCodePattern.length() + USER_CODE_LENGTH);
			if (existingCode1.equals(existingCode2)) {
				userCodes.remove(existingCode1);
			}
			entryBuffer = "";
		}
		
		// Delete all user codes: 6DPC*6<Reenter 6DPC>
		String deleteAllPattern = String.valueOf(arrayToString(masterCode) + "*" + "6");
		if (entryBuffer.startsWith(deleteAllPattern) && entryBuffer.length() == deleteAllPattern.length() + MASTER_CODE_LENGTH) {
			String reenteredCode = entryBuffer.substring(deleteAllPattern.length());
			if (isMasterCode(reenteredCode)) {
				userCodes.clear();
			}
			entryBuffer = "";
		}
		
		// Change 6DPC: 6DPC*3<New 6DPC><Repeat New 6DPC>
		String changeCodePattern = String.valueOf(arrayToString(masterCode) + "*" + "3");
		if (entryBuffer.startsWith(changeCodePattern) && entryBuffer.length() == changeCodePattern.length() + 2 * MASTER_CODE_LENGTH) {
			String newMasterCode1 = entryBuffer.substring(changeCodePattern.length(), changeCodePattern.length() + MASTER_CODE_LENGTH);
			String newMasterCode2 = entryBuffer.substring(changeCodePattern.length() + MASTER_CODE_LENGTH);
			if (newMasterCode1.equals(newMasterCode2)) {
				this.masterCode = stringToArray(newMasterCode1);
			}
			entryBuffer = "";
		}
	}
	
	private String arrayToString(int[] arr) {
		StringBuilder sb = new StringBuilder();
		for (int i : arr) {
			sb.append(i);
		}
		return sb.toString();
	}
	
	private int[] stringToArray(String s) {
		int[] arr = new int[s.length()];
		for (int i = 0; i < s.length(); i++) {
			arr[i] = Character.getNumericValue(s.charAt(i));
		}
		return arr;
	}

	@Override
	public boolean unlock() {
		// Keyless entry can also be unlocked with a physical key as per the problem description.
		if (super.unlock()) {
			return true;
		}
		return !isLocked();
	}

	// The provided file had boolean methods, which can be implemented as checks
	public boolean addedUserCode() {
		// This logic needs to be integrated into pushButton or handleCommand
		// For a simple return, you might check a state variable
		return false;
	}

	public boolean deletedUserCode() {
		return false;
	}

	public boolean deletedAllUserCodes() {
		return false;
	}

	public boolean changedMasterCode() {
		return false;
	}

	public int[] getMasterCode() {
		return this.masterCode;
	}
}