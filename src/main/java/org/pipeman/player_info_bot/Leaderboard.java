package org.pipeman.player_info_bot;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Leaderboard {
    public static List<LeaderboardEntry> getLeaderboard(int limit) {
        List<LeaderboardEntry> leaderboard = getLeaderboard();
        return leaderboard.subList(0, Math.min(limit, leaderboard.size()));
    }

    public static List<LeaderboardEntry> getLeaderboard() {
        List<LeaderboardEntry> list = new ArrayList<>();
        for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
            if (player.getName() == null) continue;
            list.add(new LeaderboardEntry(player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20L, player.getName()));
        }
        Collections.sort(list);
        Collections.reverse(list);
        return list;
    }

    public static int getRank(String playerName) {
        List<LeaderboardEntry> leaderboard = getLeaderboard();
        for (int i = 0; i < leaderboard.size(); i++)
            if (leaderboard.get(i).name().equals(playerName))
                return i + 1;

        return -1;
    }

    public record LeaderboardEntry(long playtime, String name) implements Comparable<LeaderboardEntry> {
        @Override
        public int compareTo(LeaderboardEntry o) {
            return Long.compare(playtime(), o.playtime());
        }
    }
}
