package org.pipeman.pipo.grief_defender;

import com.griefdefender.api.GriefDefender;
import com.griefdefender.api.User;
import net.fabricmc.loader.api.FabricLoader;

import java.util.UUID;
import java.util.function.IntSupplier;

public class GriefDefenderImpl {

    public static final String ID = "GriefDefender";

    public static boolean isAvailable() {
        return FabricLoader.getInstance().isModLoaded(ID);
    }

    public static int getClaimBlocks(UUID playerID) {
        IntSupplier supplier = () -> {
            User user = GriefDefender.getCore().getUser(playerID);
            return user == null ? -1 : user.getPlayerData().getInitialClaimBlocks() + user.getPlayerData().getAccruedClaimBlocks() + user.getPlayerData().getBonusClaimBlocks();
        };
        return supplier.getAsInt();
    }
}
