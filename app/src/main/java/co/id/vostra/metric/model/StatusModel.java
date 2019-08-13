package co.id.vostra.metric.model;

import java.util.ArrayList;

public class StatusModel {
    ArrayList<Float> cpu;
    private float memory;
    private float disk;

    public ArrayList<Float> getCpu() {
        return cpu;
    }

    public void setCpu(ArrayList<Float> cpu) {
        this.cpu = cpu;
    }

    public float getMemory() {
        return memory;
    }

    public void setMemory(float memory) {
        this.memory = memory;
    }

    public float getDisk() {
        return disk;
    }

    public void setDisk(float disk) {
        this.disk = disk;
    }
}
