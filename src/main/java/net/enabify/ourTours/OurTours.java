package net.enabify.ourTours;

import net.enabify.ourTours.command.TourCommand;
import net.enabify.ourTours.manager.TourManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class OurTours extends JavaPlugin {
    private TourManager tourManager;

    @Override
    public void onEnable() {
        // マネージャー初期化
        tourManager = new TourManager();
        
        // コマンド登録
        getCommand("tour").setExecutor(new TourCommand(this));
        
        getLogger().info("OurToursプラグインが有効になりました！");
    }

    @Override
    public void onDisable() {
        getLogger().info("OurToursプラグインが無効になりました。");
    }
    
    public TourManager getTourManager() {
        return tourManager;
    }
}
