package org.pipeman.player_info_bot;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;

import java.time.OffsetDateTime;
import java.time.Period;
import java.util.TimerTask;

public class Autorole extends TimerTask {
    private final Guild guild;
    private final Role role;

    public Autorole(Guild guild, Role role) {
        this.guild = guild;
        this.role = role;
    }

    @Override
    public void run() {
        guild.loadMembers(member -> {
            Period period = Period.between(member.getTimeJoined().toLocalDate(), OffsetDateTime.now().toLocalDate());
            Period requiredPeriod = Period.ofMonths(6);
            if (requiredPeriod.minus(period).isNegative() && !member.getRoles().contains(role)) {
                guild.addRoleToMember(member, role).queue();
            }
        });
    }
}
