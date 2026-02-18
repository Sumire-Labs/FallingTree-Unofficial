package fr.raksrinana.fallingtree.config;

import fr.raksrinana.fallingtree.FallingTree;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Config;
import java.util.Collection;
import static fr.raksrinana.fallingtree.utils.FallingTreeUtils.getAsItems;

@Config(modid = FallingTree.MOD_ID, category = "tools")
public class ToolConfiguration{
	@Config.Name("whitelisted")
	@Config.Comment({
			"Additional list of tools that can be used to chop down a tree.",
			"INFO: Items marked with the axe tag will already be whitelisted."
	})
	@Config.LangKey("falling_tree.config.tools.whitelisted")
	public static String[] whitelisted = {};
	@Config.Name("blacklisted")
	@Config.Comment({
			"List of tools that should not be considered as tools.",
			"INFO: This wins over the whitelist."
	})
	@Config.LangKey("falling_tree.config.tools.blacklisted")
	public static String[] blacklisted = {};
	@Config.Name("durability_mode")
	@Config.Comment({
			"How to handle tool durability when felling trees.",
			"ABORT: Cancel felling if durability is insufficient.",
			"SAVE: Leave the tool at 1 durability and fell as many logs as possible.",
			"NORMAL: Fell logs until durability runs out (default).",
			"BYPASS: Ignore durability entirely (tool may break)."
	})
	@Config.LangKey("falling_tree.config.tools.durability_mode")
	public static DurabilityMode durabilityMode = DurabilityMode.NORMAL;
	@Config.Name("ignore_tools")
	@Config.Comment({
			"When set to true, the mod will be activated no matter what you have in your hand (or empty hand).",
			"INFO: Blacklist still can be use to restrict some tools."
	})
	@Config.LangKey("falling_tree.config.tools.ignore_tools")
	public static boolean ignoreTools = false;
	@Config.Name("damage_multiplicand")
	@Config.Comment({
			"Defines the number of times the damage is applied to the tool.",
			"ie: if set to 1 then breaking 5 logs will give 5 damage.",
			"ie: if set to 2 then breaking 5 logs will give 10 damage.",
			"If set to 0, it'll still apply 1 damage for every cut.",
			"INFO: This only applies when the tree is cut when using the mod."
	})
	@Config.RangeInt(min = 0)
	@Config.LangKey("falling_tree.config.tools.damage_multiplicand")
	public static int damageMultiplicand = 1;
	@Config.Name("speed_multiplicand")
	@Config.Comment({
			"Applies a speed modifier when breaking the tree.",
			"0 will disable this, so the speed will be the default one of breaking a block.",
			"If set to 1 each log block will be counted once, so if the tree is 5 blocks tall it'll require the time of breaking 5 logs.",
			"If set to 2 each log block will be counted twice, so if the tree is 5 blocks tall, it'll require the time of breaking 10 logs",
			"INFO: Only in INSTANTANEOUS mode.",
			"WARNING: If you are on a server, this either has to be set to 0 or every player should have the mod. Else they'll have a weird effect of breaking the block but the block is still there."
	})
	@Config.RangeDouble(min = 0, max = 50)
	@Config.LangKey("falling_tree.config.tools.speed_multiplicand")
	public static double speedMultiplicand = 0;

	public static Collection<Item> getBlacklisted(){
		return getAsItems(blacklisted);
	}

	public static Collection<Item> getWhitelisted(){
		return getAsItems(whitelisted);
	}

	public static DurabilityMode getDurabilityMode(){
		return durabilityMode;
	}

	public static int getDamageMultiplicand(){
		return damageMultiplicand;
	}

	public static boolean isIgnoreTools(){
		return ignoreTools;
	}

	public static double getSpeedMultiplicand(){
		return speedMultiplicand;
	}
}
