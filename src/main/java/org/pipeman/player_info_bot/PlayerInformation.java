package org.pipeman.player_info_bot;

import net.minecraft.stat.Stat;
import net.minecraft.util.Identifier;
import org.pipeman.player_info_bot.grief_defender.GriefDefenderImpl;
import org.pipeman.player_info_bot.offline.Offlines;
import org.pipeman.player_info_bot.offline.OfflinesStats;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;

public record PlayerInformation(int rank, long playtime, long lastSeen, boolean online,
                                Optional<Integer> totalClaimBlocks) {
    public static Optional<PlayerInformation> of(String playerName) {
        return Optional.of(new PlayerInformation(
                Leaderboard.getRank(playerName),
                Utils.getPlaytime(playerName),
                OfflinesStats.getPlayerStat("last_played", Offlines.getUUIDbyName(playerName)),
                Arrays.asList(MinecraftServerSupplier.getServer().getPlayerNames()).contains(playerName),
                GriefDefenderImpl.isAvailable() ? Optional.of(GriefDefenderImpl.getClaimBlocks(Offlines.getUUIDbyName(playerName))) : Optional.empty()
        ));
    }
}
