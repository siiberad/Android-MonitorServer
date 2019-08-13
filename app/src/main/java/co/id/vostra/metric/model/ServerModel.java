package co.id.vostra.metric.model;

import java.util.ArrayList;

public class ServerModel {
    private String name;
    private ArrayList<Services> services;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Services> getServices() {
        return services;
    }

    public void setServices(ArrayList<Services> services) {
        this.services = services;
    }
}
