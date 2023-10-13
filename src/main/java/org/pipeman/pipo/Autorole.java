package org.pipeman.pipo;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;

import java.util.TimerTask;
import java.util.UUID;

public class Autorole extends TimerTask {
    public final static String KRYEITOR = "1009788977894666240";
    public final static String COLLABORATOR = "1041751895062089779";

    private final Guild guild;
    private final Role role;

    public Autorole(Role role) {
        this.guild = Pipo.JDA.getGuildById(Pipo.KRYEIT_GUILD);
        this.role = role;
    }

    @Override
    public void run() {
        guild.loadMembers(member -> {
            UUID id = Utils.getMinecraftId(member);
            if (id == null) return;
            if (member.getRoles().contains(role)) {
                Utils.isPlayerOnGroup(id, role.getName().toLowerCase()).thenAcceptAsync(result -> {
                    if (!result)
                        Utils.addGroup(id, role.getName().toLowerCase());
                });
            } else {
                Utils.isPlayerOnGroup(id, role.getName().toLowerCase()).thenAcceptAsync(result -> {
                    if (result)
                        Utils.removeGroup(id, role.getName().toLowerCase());
                });
            }
        });
    }
}
