package net.cubespace.geSuitWarps.commands;

import net.cubespace.geSuitWarps.geSuitWarps;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.cubespace.geSuitWarps.managers.WarpsManager;

import java.util.HashMap;

public class WarpCommand implements CommandExecutor {

    static HashMap<Player, Location> lastLocation = new HashMap<>();


    @Override
    public boolean onCommand(final CommandSender sender, Command command,
                             String label, final String[] args) {
        final Player player = Bukkit.getPlayer(sender.getName());
        if (args.length == 1) {
            if (!player.hasPermission("gesuit.warps.bypass.delay")) {
                lastLocation.put(player, player.getLocation());

                player.sendMessage("Teleportation in progress, don't move!");

                geSuitWarps.getInstance().getServer().getScheduler().runTaskLater(geSuitWarps.getInstance(), new Runnable() {
                    @Override
                    public void run() {

                    if (lastLocation.get(player).getBlock().equals(player.getLocation().getBlock())) {
                        WarpsManager.warpPlayer(sender, sender.getName(), args[0]);
                    } else {
                        player.sendMessage("You moved, teleportation aborted!");
                    }
                    }
                }, 100L);
                return true;
            } else {
                WarpsManager.warpPlayer(sender, sender.getName(), args[0]);
                return true;
            }

        } else if (args.length > 1 && sender.hasPermission("gesuit.warps.command.warp.other")) {
            WarpsManager.warpPlayer(sender, args[0], args[1]);
            return true;
        }
        Player p = (Player) sender;
        p.chat("/warps");
        return true;
    }
}