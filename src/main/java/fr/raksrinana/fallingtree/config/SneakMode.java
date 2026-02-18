package fr.raksrinana.fallingtree.config;

public enum SneakMode{
	SNEAK_DISABLE,
	SNEAK_ENABLE,
	IGNORE;

	public boolean test(boolean sneaking){
		switch(this){
			case SNEAK_DISABLE:
				return !sneaking;
			case SNEAK_ENABLE:
				return sneaking;
			case IGNORE:
				return true;
			default:
				return !sneaking;
		}
	}
}
