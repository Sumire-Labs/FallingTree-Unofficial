package fr.raksrinana.fallingtree.config;

public enum BreakMode{
	INSTANTANEOUS(true),
	FALL_ITEM(true),
	FALL_ITEM_STRAIGHT(true),
	FALL_BLOCK(true),
	FALL_ALL_BLOCK(true),
	SHIFT_DOWN(false);
	private final boolean checkLeavesAround;
	
	BreakMode(boolean checkLeavesAround){
		this.checkLeavesAround = checkLeavesAround;
	}
	
	public boolean shouldCheckLeavesAround(){
		return this.checkLeavesAround;
	}
}
