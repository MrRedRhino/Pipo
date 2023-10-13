package org.pipeman.pipo;

import org.pipeman.pipo.compat.GriefDefenderImpl;
import org.pipeman.pipo.offline.Offlines;

import java.util.Optional;

public record PlayerInformation(int rank, long playtime, long lastSeen, boolean online,
                                Optional<Integer> totalClaimBlocks) {
    public static Optional<PlayerInformation> of(String playerName) {
        return Optional.of(new PlayerInformation(
                Leaderboard.getRank(playerName),
                Utils.getPlaytime(playerName),
                Utils.getLastPlayed(playerName),
                Utils.isOnline(playerName),
                GriefDefenderImpl.isAvailable() ? Optional.of(GriefDefenderImpl.getClaimBlocks(Offlines.getUUIDbyName(playerName))) : Optional.empty()
        ));
    }
}
