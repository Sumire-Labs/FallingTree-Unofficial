package fr.raksrinana.fallingtree.config;

import fr.raksrinana.fallingtree.FallingTree;
import net.minecraft.block.Block;
import net.minecraftforge.common.config.Config;
import java.util.Collection;
import static fr.raksrinana.fallingtree.utils.FallingTreeUtils.getAsBlocks;

@Config(modid = FallingTree.MOD_ID, category = "trees")
public class TreeConfiguration{
	@Config.Name("logs_whitelisted")
	@Config.Comment({
			"Additional list of blocks considered as logs and that will be destroyed by the mod.",
			"INFO: Blocks marked with the log tag will already be whitelisted."
	})
	@Config.LangKey("falling_tree.config.trees.logs_whitelisted")
	public static String[] whitelistedLogs = {};
	@Config.Name("logs_blacklisted")
	@Config.Comment({
			"List of blocks that should not be considered as logs.",
			"INFO: This wins over the whitelist."
	})
	@Config.LangKey("falling_tree.config.trees.logs_blacklisted")
	public static String[] blacklistedLogs = {};
	@Config.Name("leaves_whitelisted")
	@Config.Comment({
			"Additional list of blocks considered as leaves.",
			"INFO: Blocks marked with the leaves tag will already be whitelisted."
	})
	@Config.LangKey("falling_tree.config.trees.leaves_whitelisted")
	public static String[] whitelistedLeaves = {};
	@Config.Name("leaves_blacklisted")
	@Config.Comment({
			"List of blocks that should not be considered as leaves.",
			"INFO: This wins over the whitelist."
	})
	@Config.LangKey("falling_tree.config.trees.leaves_blacklisted")
	public static String[] blacklistedLeaves = {};
	@Config.Name("break_mode")
	@Config.Comment({
			"How to break the tree.",
			"Instantaneous will break it in one go.",
			"Shift down will make the tree fall down as you cut it, so you still have to break x blocks but don't have to climb the tree for them."
	})
	@Config.LangKey("falling_tree.config.trees.break_mode")
	public static BreakMode breakMode = BreakMode.INSTANTANEOUS;
	@Config.Name("logs_max_count")
	@Config.Comment({
			"The maximum size of a tree. If there's more logs than this value the tree won't be cut.",
			"INFO: Only in INSTANTANEOUS mode."
	})
	@Config.RangeInt(min = 1)
	@Config.LangKey("falling_tree.config.trees.logs_max_count")
	public static int maxSize = 100;
	@Config.Name("minimum_leaves_around_required")
	@Config.Comment({
			"The minimum amount of leaves that needs to be around the top most log in order for the mod to consider it a tree.",
			"INFO: Only in INSTANTANEOUS mode."
	})
	@Config.RangeInt(min = 0, max = 5)
	@Config.LangKey("falling_tree.config.trees.minimum_leaves_around_required")
	public static int minimumLeavesAroundRequired = 0;
	@Config.Name("minimum_leaves_ratio")
	@Config.Comment({
			"The minimum ratio of logs that have at least one adjacent leaf block.",
			"For example, 0.1 means at least 10% of logs must touch a leaf.",
			"Set to 0 to disable this check.",
			"This checks all logs in the tree, not just the top."
	})
	@Config.RangeDouble(min = 0.0, max = 1.0)
	@Config.LangKey("falling_tree.config.trees.minimum_leaves_ratio")
	public static double minimumLeavesRatio = 0.1;
	@Config.Name("leaves_breaking")
	@Config.Comment({
			"When set to true, leaves that should naturally break will be broken instantly."
	})
	@Config.LangKey("falling_tree.config.trees.leaves_breaking")
	public static boolean leavesBreaking = true;
	@Config.Name("leaves_breaking_force_radius")
	@Config.Comment({
			"Radius to force break leaves. If another tree is still holding the leaves they'll still be broken. If the leaves are persistent (placed by player) they'll also be destroyed.",
			"The radius is applied from one of the top most log blocks.",
			"INFO: break_leaves must be activated for this to take effect.",
			"INFO: Only in INSTANTANEOUS mode."
	})
	@Config.RangeInt(min = 0, max = 10)
	@Config.LangKey("falling_tree.config.trees.leaves_breaking_force_radius")
	public static int leavesBreakingForceRadius = 0;
	@Config.Name("allow_mixed_logs")
	@Config.Comment({
			"When set to true this allow to have any kind of log in a tree trunk.",
			"Otherwise (false) the trunk will be considered as being only one kind of log."
	})
	@Config.LangKey("falling_tree.config.trees.allow_mixed_logs")
	public static boolean allowMixedLogs = false;
	@Config.Name("max_scan_size")
	@Config.Comment({
			"Maximum number of blocks to scan when detecting a tree.",
			"If the BFS scan exceeds this limit, the structure won't be considered a tree.",
			"Useful for preventing lag on very large structures."
	})
	@Config.RangeInt(min = 1)
	@Config.LangKey("falling_tree.config.trees.max_scan_size")
	public static int maxScanSize = 500;
	@Config.Name("min_size")
	@Config.Comment({
			"The minimum number of logs required for a tree to be felled.",
			"Trees smaller than this will be broken normally.",
			"Set to 0 to disable."
	})
	@Config.RangeInt(min = 0)
	@Config.LangKey("falling_tree.config.trees.min_size")
	public static int minSize = 0;
	@Config.Name("max_size_action")
	@Config.Comment({
			"What to do when a tree exceeds the maximum size.",
			"ABORT: Cancel the tree felling entirely (default).",
			"CUT: Fell up to the maximum size."
	})
	@Config.LangKey("falling_tree.config.trees.max_size_action")
	public static MaxSizeAction maxSizeAction = MaxSizeAction.ABORT;

	public static Collection<Block> getBlacklistedLeaves(){
		return getAsBlocks(blacklistedLeaves);
	}

	public static Collection<Block> getBlacklistedLogs(){
		return getAsBlocks(blacklistedLogs);
	}

	public static int getLeavesBreakingForceRadius(){
		return leavesBreakingForceRadius;
	}

	public static int getMaxSize(){
		return maxSize;
	}

	public static int getMinimumLeavesAroundRequired(){
		return minimumLeavesAroundRequired;
	}

	public static Collection<Block> getWhitelistedLeaves(){
		return getAsBlocks(whitelistedLeaves);
	}

	public static Collection<Block> getWhitelistedLogs(){
		return getAsBlocks(whitelistedLogs);
	}

	public static boolean isLeavesBreaking(){
		return leavesBreaking;
	}

	public static BreakMode getBreakMode(){
		return breakMode;
	}

	public static boolean isAllowMixedLogs(){
		return allowMixedLogs;
	}

	public static double getMinimumLeavesRatio(){
		return minimumLeavesRatio;
	}

	public static int getMaxScanSize(){
		return maxScanSize;
	}

	public static int getMinSize(){
		return minSize;
	}

	public static MaxSizeAction getMaxSizeAction(){
		return maxSizeAction;
	}
}
