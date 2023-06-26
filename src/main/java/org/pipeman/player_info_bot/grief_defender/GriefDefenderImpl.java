package org.pipeman.player_info_bot.grief_defender;

import com.griefdefender.api.GriefDefender;
import com.griefdefender.api.User;
import org.bukkit.Bukkit;

import java.util.UUID;
import java.util.function.IntSupplier;

public class GriefDefenderImpl {

    public static final String ID = "GriefDefender";

    public static boolean isAvailable() {
        return Bukkit.getPluginManager().getPlugin(ID) != null;
    }

    public static int getClaimBlocks(UUID playerID) {
        IntSupplier supplier = () -> {
            User user = GriefDefender.getCore().getUser(playerID);
            return user == null ? -1 : user.getPlayerData().getInitialClaimBlocks() + user.getPlayerData().getAccruedClaimBlocks() + user.getPlayerData().getBonusClaimBlocks();
        };
        return supplier.getAsInt();
    }
}
