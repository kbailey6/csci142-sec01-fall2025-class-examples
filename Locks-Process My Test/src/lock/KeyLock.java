package lock;

public class KeyLock implements Lock {
	private final int key;
	protected boolean isLocked;
	private boolean isInserted;
	private boolean isTurned;
	
	public KeyLock(int key) {
		this.key = key;
		this.isLocked = true;
		this.isInserted = false;
		this.isTurned = false;
	}
	
	public boolean insertKey(int key) {
		if (key != this.key) {
			this.isInserted = false;
			return false;
		}
		this.isInserted = true;
		return true;
	}
	
	public boolean removeKey() {
		this.isInserted = false;
		this.isTurned = false;
		return true;
	}
	
	public boolean turn() {
		if (this.isInserted) {
			this.isTurned = true;
			return true;
		}
		return false;
	}

	@Override
	public boolean lock() {
		this.isLocked = true;
		return true;
	}

	@Override
	public boolean unlock() {
		if (this.isInserted && this.isTurned) {
			this.isLocked = false;
			return true;
		}
		return false;
	}

	@Override
	public boolean isLocked() {
		return this.isLocked;
	}
}