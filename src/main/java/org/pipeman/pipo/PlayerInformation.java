package org.pipeman.pipo;

import org.pipeman.pipo.grief_defender.GriefDefenderImpl;
import org.pipeman.pipo.offline.Offlines;

import java.util.Arrays;
import java.util.Optional;

public record PlayerInformation(int rank, long playtime, long lastSeen, boolean online,
                                Optional<Integer> totalClaimBlocks) {
    public static Optional<PlayerInformation> of(String playerName) {
        return Optional.of(new PlayerInformation(
                Leaderboard.getRank(playerName),
                Utils.getPlaytime(playerName),
                Pipo.getInstance().lastTimePlayed.getHashMap().get(Offlines.getUUIDbyName(playerName)),
                Arrays.asList(MinecraftServerSupplier.getServer().getPlayerNames()).contains(playerName),
                GriefDefenderImpl.isAvailable() ? Optional.of(GriefDefenderImpl.getClaimBlocks(Offlines.getUUIDbyName(playerName))) : Optional.empty()
        ));
    }
}
