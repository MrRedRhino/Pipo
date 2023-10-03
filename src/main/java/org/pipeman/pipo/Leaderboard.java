package org.pipeman.pipo;

import org.pipeman.pipo.offline.Offlines;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Leaderboard {
    public static List<LeaderboardEntry> getLeaderboard(int limit) {
        List<LeaderboardEntry> leaderboard = getLeaderboard();
        return leaderboard.subList(0, Math.min(limit, leaderboard.size()));
    }

    public static List<LeaderboardEntry> getLeaderboard(int limit, int offset) {
        List<LeaderboardEntry> leaderboard = getLeaderboard();
        if (offset >= leaderboard.size()) return List.of();
        return leaderboard.subList(offset, Math.min(leaderboard.size(), limit + offset));
    }

    public static List<LeaderboardEntry> getLeaderboard() {
        List<LeaderboardEntry> list = new ArrayList<>();
        for (String name : Offlines.getPlayerNames()) {
            if (name != null) {
                list.add(new LeaderboardEntry(Utils.getPlaytime(name), name));
            }
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
