package co.id.vostra.metric.model;
import java.util.ArrayList;

public class MetricModel implements Comparable{

    private String version;
    private ArrayList<ServerModel> servers;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public ArrayList<ServerModel> getServers() {
        return servers;
    }

    public void setServers(ArrayList<ServerModel> servers) {
        this.servers = servers;
    }

    @Override
    public int compareTo(Object o) {
        MetricModel metricModel = (MetricModel) o;

        if (metricModel.getServers().equals(this.servers)){
            return 0;
        }
        return 1;
    }
}