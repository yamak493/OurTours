package net.enabify.ourTours.command;

import net.enabify.ourTours.OurTours;
import net.enabify.ourTours.data.Building;
import net.enabify.ourTours.data.Tour;
import net.enabify.ourTours.manager.PlayerTourState;
import net.enabify.ourTours.service.TourApiService;
import net.enabify.ourTours.util.FoliaUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TourCommand implements CommandExecutor {
    private final OurTours plugin;
    private final TourApiService apiService;

    public TourCommand(OurTours plugin) {
        this.plugin = plugin;
        this.apiService = new TourApiService();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cこのコマンドはプレイヤーのみ実行できます。");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage("§c使用方法: /tour <ツアーコード>");
            player.sendMessage("§c使用方法: /tour next");
            player.sendMessage("§c使用方法: /tour previous");
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "next":
                handleNext(player);
                break;
            case "previous":
                handlePrevious(player);
                break;
            default:
                handleTourCode(player, args[0]);
                break;
        }

        return true;
    }

    private void handleNext(Player player) {
        PlayerTourState state = plugin.getTourManager().getPlayerTourState(player);
        if (state == null) {
            player.sendMessage("§cツアー中ではありません。");
            return;
        }

        Building nextBuilding = state.next();
        if (nextBuilding == null) {
            player.sendMessage("§cこれが最後の建物です。");
            return;
        }

        FoliaUtils.teleportPlayer(plugin, player, nextBuilding);
    }

    private void handlePrevious(Player player) {
        PlayerTourState state = plugin.getTourManager().getPlayerTourState(player);
        if (state == null) {
            player.sendMessage("§cツアー中ではありません。");
            return;
        }

        Building previousBuilding = state.previous();
        if (previousBuilding == null) {
            player.sendMessage("§cこれが最初の建物です。");
            return;
        }

        FoliaUtils.teleportPlayer(plugin, player, previousBuilding);
    }

    private void handleTourCode(Player player, String tourCode) {
        player.sendMessage("§aツアー情報を取得中...");

        // 非同期でAPI呼び出し
        FoliaUtils.runAsync(plugin, () -> {
            apiService.fetchTour(tourCode).thenAccept(result -> {
                // メインスレッドでプレイヤーに結果を通知
                player.getScheduler().run(plugin, (task) -> {
                    if (result.isSuccess()) {
                        Tour tour = result.getTour();
                        
                        if (tour.getBuildings().isEmpty()) {
                            player.sendMessage("§cこのツアーには建物が登録されていません。");
                            return;
                        }

                        // ツアー開始
                        plugin.getTourManager().startTour(player, tour);
                        Building firstBuilding = tour.getBuildings().get(0);

                        // タイトル表示
                        FoliaUtils.sendTitle(plugin, player, "§6" + tour.getTitle(), "§fツアーを開始します");
                        
                        // 最初の建物にテレポート
                        FoliaUtils.teleportPlayer(plugin, player, firstBuilding);
                        
                        player.sendMessage("§aツアー「§6" + tour.getTitle() + "§a」を開始します！");
                        player.sendMessage("§a前の場所に移動するときは、チャット欄に「/p」と送信してください.");
                        player.sendMessage("§a次の場所に移動するときは、チャット欄に「/n」と送信してください.");
                    } else {
                        player.sendMessage("§c" + result.getError());
                    }
                }, null);
            }).exceptionally(throwable -> {
                // エラーハンドリング
                player.getScheduler().run(plugin, (task) -> {
                    player.sendMessage("§cエラーが発生しました: " + throwable.getMessage());
                }, null);
                return null;
            });
        });
    }
}
