package net.enabify.ourTours.manager;

import net.enabify.ourTours.data.Tour;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TourManager {
    private final Map<UUID, PlayerTourState> playerTourStates = new HashMap<>();

    public void startTour(Player player, Tour tour) {
        playerTourStates.put(player.getUniqueId(), new PlayerTourState(tour));
    }

    public PlayerTourState getPlayerTourState(Player player) {
        return playerTourStates.get(player.getUniqueId());
    }

    public boolean isPlayerOnTour(Player player) {
        return playerTourStates.containsKey(player.getUniqueId());
    }

    public void removeTour(Player player) {
        playerTourStates.remove(player.getUniqueId());
    }
}
