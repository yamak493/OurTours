package net.enabify.ourTours.data;

import java.util.List;

public class Building {
    private String title;
    private String description;
    private String world;
    private List<Integer> coordinate;

    // Gson 用のデフォルトコンストラクタ
    public Building() {}
    
    public Building(String title, String description, String world, List<Integer> coordinate) {
        this.title = title;
        this.description = description;
        this.world = world;
        this.coordinate = coordinate;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<Integer> getCoordinate() {
        return coordinate;
    }

    public int getX() {
        return coordinate.get(0);
    }

    public int getY() {
        return coordinate.get(1);
    }

    public int getZ() {
        return coordinate.get(2);
    }
    public String getWorld() {
        return world;
    }
}
