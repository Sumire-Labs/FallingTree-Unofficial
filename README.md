# Falling Tree (Unofficial 1.12.2 Fork)

This mod will allow you to break a full tree by only breaking one log off of it. By default, sneaking will disable this functionality.

Based on [FallingTree by RakSrinaNa](https://github.com/RakSrinaNa/FallingTree) (LGPL-3.0). This 1.12.2 fork is licensed under GPL-3.0.

![Demonstration of breaking a tree](https://github.com/RakSrinaNa/FallingTree/raw/1.14.4/assets/demo.gif)

## Configuration

### General
- **sneak_mode**: Controls how sneaking affects tree felling. `SNEAK_DISABLE` (default): only when not sneaking. `SNEAK_ENABLE`: only when sneaking. `IGNORE`: always active.
- **notification_mode**: How to display notifications. `CHAT`: chat message. `ACTION_BAR` (default): action bar. `NONE`: no notifications.
- **break_in_creative**: When enabled, the mod will cut down trees in creative mode too.

### Trees
- **logs_whitelisted / logs_blacklisted**: Whitelist/blacklist blocks as logs. Blocks with the log tag are already whitelisted. Blacklist wins over whitelist.
- **leaves_whitelisted / leaves_blacklisted**: Whitelist/blacklist blocks as leaves. Blocks with the leaves tag are already whitelisted. Blacklist wins over whitelist.
- **break_mode**: `INSTANTANEOUS` breaks the tree in one go. `SHIFT_DOWN` makes logs fall as you cut.
- **logs_max_count**: Maximum number of logs in a tree. Trees larger than this won't be cut (default: 100).
- **max_size_action**: What to do when a tree exceeds the max size. `ABORT` (default): cancel felling. `CUT`: fell up to the max size.
- **max_scan_size**: Maximum blocks to scan during tree detection. Structures exceeding this limit are ignored (default: 500).
- **min_size**: Minimum number of logs for a tree to be felled. Smaller trees are broken normally. Set to 0 to disable (default: 0).
- **minimum_leaves_around_required**: Minimum leaves adjacent to the topmost log to consider it a tree (default: 0).
- **minimum_leaves_ratio**: Minimum ratio of logs that must touch a leaf. e.g. 0.1 = 10%. Set to 0 to disable (default: 0.1).
- **leaves_breaking**: When enabled, leaves that should naturally break will be broken instantly (default: true).
- **leaves_breaking_force_radius**: Radius to force break leaves from the topmost log (default: 0).
- **allow_mixed_logs**: When enabled, different log types can be part of the same tree trunk.

### Tools
- **whitelisted / blacklisted**: Whitelist/blacklist tools. Axes are already whitelisted. Blacklist wins over whitelist.
- **durability_mode**: How to handle tool durability. `ABORT`: cancel if insufficient. `SAVE`: leave at 1 durability. `NORMAL` (default): fell until durability runs out. `BYPASS`: ignore durability.
- **ignore_tools**: When enabled, the mod works regardless of what you hold (or empty hand).
- **damage_multiplicand**: Durability damage multiplier per log (default: 1). Set to 0 for always 1 damage per cut.
- **speed_multiplicand**: Breaking speed modifier (default: 0 = normal speed). Only in INSTANTANEOUS mode.

## Backported Features from 1.21.11

The following features were backported from [FallingTree 1.21.11](https://github.com/RakSrinaNa/FallingTree) (LGPL-3.0, by RakSrinaNa):

- **SneakMode** enum (replaces `reverse_sneaking` boolean)
- **NotificationMode** enum (CHAT / ACTION_BAR / NONE)
- **DurabilityMode** enum (replaces `preserve` boolean, adds ABORT / SAVE / NORMAL / BYPASS)
- **maxScanSize** - BFS scan limit for tree detection
- **minSize** - Minimum log count for tree felling
- **MaxSizeAction** enum (ABORT / CUT when tree exceeds max size)

LGPL-3.0 code is compatible with GPL-3.0 per LGPL-3.0 Section 2(b).
