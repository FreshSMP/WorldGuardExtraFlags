package net.goldtreeservers.worldguardextraflags.wg;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.tcoded.folialib.FoliaLib;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldGuardUtils
{
	public static final String PREVENT_TELEPORT_LOOP_META = "WGEFP: TLP";

    @Getter
    private static FoliaLib scheduler;

	public static void initializeScheduler(JavaPlugin plugin) {
		Objects.requireNonNull(plugin, "plugin cannot be null!");
		scheduler = new FoliaLib(plugin);
	}

	@SuppressWarnings("unchecked")
	public static boolean hasNoTeleportLoop(Plugin plugin, Player player, Object location)
	{
		MetadataValue result = player.getMetadata(WorldGuardUtils.PREVENT_TELEPORT_LOOP_META).stream()
				.filter((p) -> p.getOwningPlugin().equals(plugin))
				.findFirst()
				.orElse(null);
		
		if (result == null)
		{
			result = new FixedMetadataValue(plugin, new HashSet<>());
			
			player.setMetadata(WorldGuardUtils.PREVENT_TELEPORT_LOOP_META, result);

			getScheduler().getScheduler().runAtEntity(player, (task) -> player.removeMetadata(WorldGuardUtils.PREVENT_TELEPORT_LOOP_META, plugin));
		}
		
		Set<Object> set = (Set<Object>) result.value();
        return set.add(location);
    }
}
