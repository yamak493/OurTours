package net.enabify.ourTours.util;

import net.enabify.ourTours.data.Building;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;

public class FoliaUtils {
    
    public static void teleportPlayer(Plugin plugin, Player player, Building building) {
        // Foliaでは地域ベースのスケジューラーを使用する必要がある
        // 指定されたワールドを取得
        World world = plugin.getServer().getWorld(building.getWorld());
        if (world == null) {
            player.sendMessage("§cワールド '" + building.getWorld() + "' が見つかりません。管理者に連絡してください。");
            return;
        }
        Location loc = new Location(world, building.getX() + 0.5, building.getY(), building.getZ() + 0.5);
        
        // プレイヤーのスケジューラーを使用してテレポート処理を実行
        player.getScheduler().run(plugin, (ScheduledTask task) -> {
            // FoliaのリージョンスレッドではteleportAsyncを使用
            player.teleportAsync(loc);

            sendTitle(plugin, player, "§6" + building.getTitle() , "");
            
            // テレポート後にメッセージを送信
            player.sendMessage("§6=====[" + building.getTitle() + "]=====");
            player.sendMessage("§6建物名：§f" + building.getTitle());
            player.sendMessage("§6解説：§f" + building.getDescription());
            player.sendMessage("§6===============");
            player.sendMessage("§aチャット欄に「/n」と送信して次の場所へ");
        }, null);
    }
    
    public static void sendTitle(Plugin plugin, Player player, String title, String subtitle) {
        // Foliaではプレイヤーのスケジューラーを使用してタイトル表示
        player.getScheduler().run(plugin, (ScheduledTask task) -> {
            // タイトル表示もAsyncに
            player.sendTitle(title, subtitle, 10, 70, 20);
        }, null);
    }

    public static void runAsync(Plugin plugin, Runnable runnable) {
        // Foliaでは非同期タスクはAsyncSchedulerを使用
        plugin.getServer().getAsyncScheduler().runNow(plugin, (ScheduledTask task) -> {
            runnable.run();
        });
    }
}
