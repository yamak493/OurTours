package net.enabify.ourTours.manager;

import net.enabify.ourTours.data.Tour;
import net.enabify.ourTours.data.Building;

public class PlayerTourState {
    private Tour tour;
    private int currentBuildingIndex;

    public PlayerTourState(Tour tour) {
        this.tour = tour;
        this.currentBuildingIndex = 0;
    }

    public Tour getTour() {
        return tour;
    }

    public int getCurrentBuildingIndex() {
        return currentBuildingIndex;
    }

    public Building getCurrentBuilding() {
        if (tour.getBuildings().isEmpty()) {
            return null;
        }
        return tour.getBuildings().get(currentBuildingIndex);
    }

    public boolean hasNext() {
        return currentBuildingIndex < tour.getBuildings().size() - 1;
    }

    public boolean hasPrevious() {
        return currentBuildingIndex > 0;
    }

    public Building next() {
        if (hasNext()) {
            currentBuildingIndex++;
            return getCurrentBuilding();
        }
        return null;
    }

    public Building previous() {
        if (hasPrevious()) {
            currentBuildingIndex--;
            return getCurrentBuilding();
        }
        return null;
    }

    public void reset() {
        currentBuildingIndex = 0;
    }
}
