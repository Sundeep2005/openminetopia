package nl.openminetopia.modules.player.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.modules.player.utils.PlaytimeUtil;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.entity.Player;

@CommandAlias("playtime")
public class PlaytimeCommand extends BaseCommand {

    @Default
    public void onPlaytimeCommand(Player player) {
        MinetopiaPlayer minetopiaPlayer = PlayerManager.getInstance().getMinetopiaPlayer(player);
        if (minetopiaPlayer == null) return;

        player.sendMessage(ChatUtils.color("<dark_aqua>Jouw huidige speeltijd is momenteel " + PlaytimeUtil.formatPlaytime(minetopiaPlayer.getPlaytime())));
    }
}
