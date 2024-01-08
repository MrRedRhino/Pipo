package org.pipeman.pipo.commands.minecraft;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.pipeman.pipo.Pipo;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Supplier;

public class CommandLinkDiscord {
    public static HashMap<Integer, UUID> codes;

    public static int execute(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        if (player == null) {
            Supplier<Text> message = () -> Text.of("Can't execute from console");
            source.sendFeedback(message, false);
            return 0;
        }

        if (Pipo.instance.minecraftToDiscord.getElement(player.getUuid()) != null) {
            Supplier<Text> message = () -> Text.of("You already linked your account, use /unlinkdiscord to unlink it");
            source.sendFeedback(message, false);
            return 0;
        }

        int random4Digits = (int) (Math.random() * 9000) + 1000;

        if (codes.containsKey(random4Digits)) {
            return execute(context);
        }

        codes.put(random4Digits, player.getUuid());

        player.sendMessage(Text.literal("Send a private message to " + " with the following code: " + random4Digits));
        return Command.SINGLE_SUCCESS;
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("linkdiscord")
                .executes(CommandLinkDiscord::execute)
        );
    }
}
