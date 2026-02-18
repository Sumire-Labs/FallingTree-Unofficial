package fr.raksrinana.fallingtree.tree;

import fr.raksrinana.fallingtree.config.CommonConfig;
import fr.raksrinana.fallingtree.config.DurabilityMode;
import fr.raksrinana.fallingtree.config.NotificationMode;
import fr.raksrinana.fallingtree.config.ToolConfiguration;
import fr.raksrinana.fallingtree.config.TreeConfiguration;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;
import static fr.raksrinana.fallingtree.utils.FallingTreeUtils.isLeafBlock;
import static fr.raksrinana.fallingtree.utils.FallingTreeUtils.isTreeBlock;

public class TreeHandler{
	private static void notifyPlayer(EntityPlayer player, TextComponentTranslation message){
		switch(CommonConfig.getNotificationMode()){
			case CHAT:
				player.sendMessage(message);
				break;
			case ACTION_BAR:
				player.sendStatusMessage(message, true);
				break;
			case NONE:
				break;
		}
	}

	@Nonnull
	public static Optional<Tree> getTree(@Nonnull World world, @Nonnull BlockPos blockPos){
		Block logBlock = world.getBlockState(blockPos).getBlock();
		if(!isTreeBlock(logBlock)){
			return Optional.empty();
		}
		Queue<BlockPos> toAnalyzePos = new LinkedList<>();
		Set<BlockPos> analyzedPos = new HashSet<>();
		Tree tree = new Tree(world, blockPos);
		toAnalyzePos.add(blockPos);
		int maxScanSize = TreeConfiguration.getMaxScanSize();
		while(!toAnalyzePos.isEmpty()){
			BlockPos analyzingPos = toAnalyzePos.remove();
			tree.addLog(analyzingPos);
			analyzedPos.add(analyzingPos);
			if(tree.getLogCount() > maxScanSize){
				return Optional.empty();
			}
			Collection<BlockPos> nearbyPos = neighborLogs(world, logBlock, analyzingPos, analyzedPos);
			nearbyPos.removeAll(analyzedPos);
			toAnalyzePos.addAll(nearbyPos.stream().filter(pos -> !toAnalyzePos.contains(pos)).collect(Collectors.toList()));
		}
		
		if(TreeConfiguration.getBreakMode().shouldCheckLeavesAround()){
			int aroundRequired = TreeConfiguration.getMinimumLeavesAroundRequired();
			if(tree.getTopMostLog()
					.map(topLog -> getLeavesAround(world, topLog) < aroundRequired)
					.orElse(true)){
				return Optional.empty();
			}
		}

		double requiredRatio = TreeConfiguration.getMinimumLeavesRatio();
		if(requiredRatio > 0){
			long logsWithLeaves = tree.getLogs().stream()
					.filter(log -> getLeavesAround(world, log) > 0)
					.count();
			if(logsWithLeaves < tree.getLogCount() * requiredRatio){
				return Optional.empty();
			}
		}

		return Optional.of(tree);
	}
	
	private static long getLeavesAround(@Nonnull World world, @Nonnull BlockPos blockPos){
		return Arrays.stream(EnumFacing.values())
				.map(blockPos::offset)
				.filter(testPos -> {
					IBlockState state = world.getBlockState(testPos);
					if(!isLeafBlock(state.getBlock())){
						return false;
					}
					if(state.getBlock() instanceof BlockLeaves){
						return state.getValue(BlockLeaves.DECAYABLE);
					}
					return true;
				})
				.count();
	}
	
	@Nonnull
	private static Collection<BlockPos> neighborLogs(@Nonnull World world, @Nonnull Block logBlock, @Nonnull BlockPos blockPos, @Nonnull Collection<BlockPos> analyzedPos){
		List<BlockPos> neighborLogs = new LinkedList<>();
		final BlockPos.MutableBlockPos checkPos = new BlockPos.MutableBlockPos();
		for(int x = -1; x <= 1; x++){
			for(int z = -1; z <= 1; z++){
				for(int y = -1; y <= 1; y++){
					checkPos.setPos(blockPos.getX() + x, blockPos.getY() + y, blockPos.getZ() + z);
					if(!analyzedPos.contains(checkPos) && isSameTree(world, checkPos, logBlock)){
						neighborLogs.add(checkPos.toImmutable());
					}
				}
			}
		}
		neighborLogs.addAll(analyzedPos);
		return neighborLogs;
	}
	
	private static boolean isSameTree(@Nonnull World world, BlockPos checkBlockPos, Block parentLogBlock){
		Block checkBlock = world.getBlockState(checkBlockPos).getBlock();
		if(TreeConfiguration.isAllowMixedLogs()){
			return isTreeBlock(checkBlock);
		}
		else{
			return checkBlock.equals(parentLogBlock);
		}
	}
	
	public static boolean destroyInstant(@Nonnull Tree tree, @Nonnull EntityPlayer player, @Nonnull ItemStack tool){
		return destroyInstant(tree, player, tool, tree.getLogCount());
	}

	public static boolean destroyInstant(@Nonnull Tree tree, @Nonnull EntityPlayer player, @Nonnull ItemStack tool, int maxLogs){
		final World world = tree.getWorld();
		final DurabilityMode durabilityMode = ToolConfiguration.getDurabilityMode();
		final int damageMultiplicand = ToolConfiguration.getDamageMultiplicand();
		final int toolUsesLeft = tool.isItemStackDamageable() ? (tool.getMaxDamage() - tool.getItemDamage()) : Integer.MAX_VALUE;
		double rawWeightedUsesLeft = damageMultiplicand == 0 ? (toolUsesLeft - 1) : ((1d * toolUsesLeft) / damageMultiplicand);
		int logsToBreak = Math.min(tree.getLogCount(), maxLogs);
		switch(durabilityMode){
			case ABORT:
				if(rawWeightedUsesLeft < logsToBreak){
					notifyPlayer(player, new TextComponentTranslation("chat.falling_tree.prevented_break_tool"));
					return false;
				}
				break;
			case SAVE:
				if(rawWeightedUsesLeft <= 1){
					notifyPlayer(player, new TextComponentTranslation("chat.falling_tree.prevented_break_tool"));
					return false;
				}
				if(logsToBreak >= rawWeightedUsesLeft){
					logsToBreak = (int) Math.ceil(rawWeightedUsesLeft) - 1;
				}
				break;
			case BYPASS:
				logsToBreak = Math.min(tree.getLogCount(), maxLogs);
				break;
			case NORMAL:
			default:
				if(logsToBreak > rawWeightedUsesLeft){
					logsToBreak = (int) rawWeightedUsesLeft;
				}
				break;
		}
		final int finalLogsToBreak = logsToBreak;
		final boolean isTreeFullyBroken = damageMultiplicand == 0 || finalLogsToBreak >= tree.getLogCount();
		tree.getLogs().stream().limit(finalLogsToBreak).forEachOrdered(logBlock -> {
			final IBlockState logState = world.getBlockState(logBlock);
			player.addStat(StatList.getObjectBreakStats(Item.getItemFromBlock(logState.getBlock())));
			logState.getBlock().harvestBlock(world, player, logBlock, logState, world.getTileEntity(logBlock), tool);
			world.destroyBlock(logBlock, false);
		});
		if(durabilityMode != DurabilityMode.BYPASS){
			int toolDamage = (damageMultiplicand * finalLogsToBreak) - 1;
			if(toolDamage > 0){
				tool.damageItem(toolDamage, player);
			}
		}
		if(isTreeFullyBroken){
			final int radius = TreeConfiguration.getLeavesBreakingForceRadius();
			if(radius > 0){
				tree.getLogs().stream().max(Comparator.comparingInt(BlockPos::getY)).ifPresent(topLog -> {
					BlockPos.MutableBlockPos checkPos = new BlockPos.MutableBlockPos();
					for(int dx = -radius; dx < radius; dx++){
						for(int dy = -radius; dy < radius; dy++){
							for(int dz = -radius; dz < radius; dz++){
								checkPos.setPos(topLog.getX() + dx, topLog.getY() + dy, topLog.getZ() + dz);
								final IBlockState checkState = world.getBlockState(checkPos);
								final Block checkBlock = checkState.getBlock();
								if(isLeafBlock(checkBlock)){
									checkBlock.dropBlockAsItem(world, checkPos, checkState, 0);
									world.destroyBlock(checkPos, false);
								}
							}
						}
					}
				});
			}
		}
		return true;
	}
	
	public static boolean destroyShift(@Nonnull Tree tree, @Nonnull EntityPlayer player, @Nonnull ItemStack tool){
		final World world = tree.getWorld();
		final DurabilityMode durabilityMode = ToolConfiguration.getDurabilityMode();
		final int damageMultiplicand = ToolConfiguration.getDamageMultiplicand();
		final int toolUsesLeft = tool.isItemStackDamageable() ? (tool.getMaxDamage() - tool.getItemDamage()) : Integer.MAX_VALUE;
		double rawWeightedUsesLeft = damageMultiplicand == 0 ? (toolUsesLeft - 1) : ((1d * toolUsesLeft) / damageMultiplicand);
		if(durabilityMode == DurabilityMode.ABORT || durabilityMode == DurabilityMode.SAVE){
			if(rawWeightedUsesLeft <= 1){
				notifyPlayer(player, new TextComponentTranslation("chat.falling_tree.prevented_break_tool"));
				return false;
			}
		}
		tree.getTopMostFurthestLog().ifPresent(logBlock -> {
			final IBlockState logState = world.getBlockState(logBlock);
			player.addStat(StatList.getObjectBreakStats(Item.getItemFromBlock(logState.getBlock())));
			logState.getBlock().harvestBlock(world, player, tree.getHitPos(), logState, world.getTileEntity(logBlock), tool);
			world.destroyBlock(logBlock, false);
		});
		if(durabilityMode != DurabilityMode.BYPASS){
			int toolDamage = damageMultiplicand;
			if(toolDamage > 0){
				tool.damageItem(toolDamage, player);
			}
		}
		return true;
	}
}
