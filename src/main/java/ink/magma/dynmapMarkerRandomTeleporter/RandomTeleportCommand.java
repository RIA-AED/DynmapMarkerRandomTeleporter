package ink.magma.dynmapMarkerRandomTeleporter;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.dynmap.DynmapCommonAPI;
import org.dynmap.markers.Marker;
import org.dynmap.markers.MarkerSet;

import java.util.Set;

// 新增命令类
public class RandomTeleportCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // 检查权限
        if (!sender.hasPermission("dynmapmarkerrandomteleporter.random")) {
            sender.sendMessage(Component.text("你没有权限使用该命令", NamedTextColor.RED));
            return true;
        }
        // 检查是否为玩家
        if (!(sender instanceof Player)) {
            sender.sendMessage(Component.text("你必须是一个玩家才能执行此命令", NamedTextColor.RED));
            return true;
        }
        // 检查 Dynmap API 是否可用
        DynmapCommonAPI dynmapAPI = DynmapMarkerRandomTeleporter.getInstance().getDynmapApiInstance();
        if (dynmapAPI == null) {
            sender.sendMessage(Component.text("错误：无法获取 Dynmap API, 可能当前服务器不支持此命令。如果您认为这是一个错误, 请反馈到技术组", NamedTextColor.RED));
            return true;
        }

        // 获取 MarkerSet 和 Marker 列表
        MarkerSet toriSet = dynmapAPI.getMarkerAPI().getMarkerSet("Tori");
        Set<Marker> toriMarkers = toriSet.getMarkers();
        if (toriMarkers == null || toriMarkers.isEmpty()) {
            sender.sendMessage(Component.text("错误: 未能获取到可用的鸟居列表, 可能当前服务器不支持此命令。如果您认为这是一个错误, 请反馈到技术组", NamedTextColor.RED));
            return true;
        }

        // 随机选择一个 Marker
        Marker randomMarker = getRandomMarker(toriMarkers);
        sender.sendMessage(Component.text("你将被传送到: " + randomMarker.getLabel(), NamedTextColor.GRAY));

        // 传送玩家到随机选择的 Marker
        if (!teleportPlayerToMarker((Player) sender, randomMarker)) {
            sender.sendMessage(Component.text("传送到目标地点失败", NamedTextColor.RED));
        }

        return true;
    }

    // 随机选择一个 Marker
    private Marker getRandomMarker(Set<Marker> markers) {
        int randomIndex = (int) (Math.random() * markers.size());
        return markers.toArray(new Marker[0])[randomIndex];
    }

    // 传送玩家到指定的 Marker
    private boolean teleportPlayerToMarker(Player player, Marker marker) {
        World targetWorld = Bukkit.getWorld(marker.getWorld());
        if (targetWorld == null) {
            player.sendMessage(Component.text("目标世界不存在", NamedTextColor.RED));
            return false;
        }
        Location targetLocation = new Location(targetWorld, marker.getX(), marker.getY(), marker.getZ());
        return player.teleport(targetLocation, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }
}
