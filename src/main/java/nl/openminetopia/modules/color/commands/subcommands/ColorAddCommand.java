package nl.openminetopia.modules.color.commands.subcommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.kyori.adventure.text.Component;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.modules.color.enums.OwnableColorType;
import nl.openminetopia.modules.color.objects.PrefixColor;
import nl.openminetopia.modules.data.storm.StormDatabase;
import nl.openminetopia.modules.data.storm.models.ColorModel;
import nl.openminetopia.modules.data.utils.StormUtils;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@CommandAlias("color")
public class ColorAddCommand extends BaseCommand {

    @Subcommand("add")
    @Syntax("<player> <type> <color>")
    @CommandCompletion("@players")
    @CommandPermission("openminetopia.color.add")
    @Description("Add a color to a player.")
    public void onPrefix(Player player, OfflinePlayer offlinePlayer, OwnableColorType type, String color) {
        if (offlinePlayer.getPlayer() == null) {
            player.sendMessage(ChatUtils.color("<red>Deze speler bestaat niet."));
            return;
        }

        MinetopiaPlayer minetopiaPlayer = PlayerManager.getInstance().getMinetopiaPlayer(offlinePlayer.getPlayer());
        if (minetopiaPlayer == null) return;

        // Validate the color
        if (!isValidColor(color)) {
            player.sendMessage(ChatUtils.color("<red>Ongeldige kleur ingevoerd. Gebruik een geldige kleur zoals ").append(Component.text("<red>, <blue>, <#RRGGBB>")));
            return;
        }

        switch (type) {
            case PREFIX:
                if (minetopiaPlayer.getColors().stream().anyMatch(prefixColor -> prefixColor.getColor().equalsIgnoreCase(color))) {
                    player.sendMessage(ChatUtils.color("<red>Deze kleur bestaat al."));
                    return;
                }

                StormUtils.getNextId(ColorModel.class, ColorModel::getId).whenComplete((integer, throwable) -> {
                    if (throwable != null) {
                        throwable.printStackTrace();
                        return;
                    }
                    PrefixColor prefixColor = new PrefixColor(integer, color, -1L);
                    minetopiaPlayer.addColor(prefixColor);
                    player.sendMessage(ChatUtils.color("<dark_aqua>Je hebt de ").append(Component.text(color).append(ChatUtils.color(" kleur <dark_aqua>toegevoegd."))));
                });
                break;
            case CHAT:
                break;
            case NAME:
                break;
            case LEVEL:
                break;
        }
    }

    // Helper method to validate the color
    private boolean isValidColor(String color) {
        // Remove angle brackets if they exist
        if (color.startsWith("<") && color.endsWith(">")) {
            color = color.substring(1, color.length() - 1);
        } else {
            return false;  // If color is not wrapped in <>, it's invalid
        }

        // Check for named colors
        String[] validColors = {
                "rainbow", "red", "blue", "green", "yellow", "aqua", "dark_red", "dark_blue", "dark_green", "gold", "white", "black", "gray", "dark_gray", "light_purple", "dark_purple"
        };
        for (String validColor : validColors) {
            if (color.equalsIgnoreCase(validColor)) {
                return true;
            }
        }

        // Check for hex color (e.g., #RRGGBB)
        return color.matches("^#([A-Fa-f0-9]{6})$");
    }
}
