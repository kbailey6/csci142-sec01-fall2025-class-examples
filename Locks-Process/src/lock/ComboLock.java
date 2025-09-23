package lock;

public class ComboLock implements Lock {
	public final int COMBO_LENGTH = 3;
	private int[] combination = new int[COMBO_LENGTH];
	private int[] attempt = new int[COMBO_LENGTH];
	private int currentAttemptIndex = 0;
	private boolean isLocked;
	
	// Constructor for a new ComboLock
	public ComboLock(int num1, int num2, int num3) {
		// As per the project description
		int remainder1 = num1 % 4;
		int remainder2 = num2 % 4;
		int remainder3 = num3 % 4;

		if (remainder1 == remainder3 && remainder2 == (remainder1 + 2) % 4) {
			this.combination[0] = num1;
			this.combination[1] = num2;
			this.combination[2] = num3;
			this.isLocked = true;
			this.isReset = true; // No reset() method in the provided file, so use a boolean field.
		} else {
			throw new IllegalArgumentException("Invalid combination provided.");
		}
	}

	public boolean turnRight(int ticks) {
		if (currentAttemptIndex < COMBO_LENGTH) {
			attempt[currentAttemptIndex++] = ticks;
			return true;
		}
		return false;
	}
	
	public boolean turnLeft(int ticks) {
		if (currentAttemptIndex < COMBO_LENGTH) {
			attempt[currentAttemptIndex++] = ticks;
			return true;
		}
		return false;
	}
	
	public void reset() {
		currentAttemptIndex = 0;
		// Reset the attempt array
		for (int i = 0; i < COMBO_LENGTH; i++) {
			attempt[i] = 0;
		}
	}
	
	public boolean isReset() {
		return currentAttemptIndex == 0;
	}
	
	@Override
	public boolean lock() {
		this.isLocked = true;
		return true;
	}

	@Override
	public boolean unlock() {
		// Check if the entered combination matches the correct one
		if (currentAttemptIndex == COMBO_LENGTH && 
		    combination[0] == attempt[0] && 
		    combination[1] == attempt[1] && 
		    combination[2] == attempt[2]) {
			this.isLocked = false;
			reset(); // Reset after a successful attempt
			return true;
		}
		reset(); // Reset after a failed attempt
		return false;
	}

	@Override
	public boolean isLocked() {
		return this.isLocked;
	}
}