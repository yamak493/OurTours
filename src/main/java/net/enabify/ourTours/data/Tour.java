package net.enabify.ourTours.data;

import java.util.List;
import net.enabify.ourTours.data.Building;

public class Tour {
    private String title;
    private String code;
    private List<Building> buildings;

    public Tour(String title, String code, List<Building> buildings) {
        this.title = title;
        this.code = code;
        this.buildings = buildings;
    }

    public String getTitle() {
        return title;
    }

    public String getCode() {
        return code;
    }

    public List<Building> getBuildings() {
        return buildings;
    }
}
