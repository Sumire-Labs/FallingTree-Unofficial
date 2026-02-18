package fr.raksrinana.fallingtree.config;

import fr.raksrinana.fallingtree.FallingTree;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;

@Config(modid = FallingTree.MOD_ID)
public class CommonConfig{
	@Name("sneak_mode")
	@Comment({
			"How sneaking affects tree felling.",
			"SNEAK_DISABLE: Only fell trees when NOT sneaking (default).",
			"SNEAK_ENABLE: Only fell trees when sneaking.",
			"IGNORE: Sneak state is ignored."
	})
	@Config.LangKey("falling_tree.config.sneak_mode")
	public static SneakMode sneakMode = SneakMode.SNEAK_DISABLE;
	@Name("notification_mode")
	@Comment({
			"How to display notification messages.",
			"CHAT: Send as chat message.",
			"ACTION_BAR: Display on the action bar.",
			"NONE: No notifications."
	})
	@Config.LangKey("falling_tree.config.notification_mode")
	public static NotificationMode notificationMode = NotificationMode.ACTION_BAR;
	@Name("break_in_creative")
	@Comment("When set to true, the mod will cut down trees in creative too.")
	@Config.LangKey("falling_tree.config.break_in_creative")
	public static boolean breakInCreative = false;

	public static SneakMode getSneakMode(){
		return CommonConfig.sneakMode;
	}

	public static NotificationMode getNotificationMode(){
		return CommonConfig.notificationMode;
	}

	public static boolean isBreakInCreative(){
		return CommonConfig.breakInCreative;
	}
}
