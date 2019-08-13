package co.id.vostra.metric.model;

public class Services {

    private String mode;
    private String url;
    private float response_code;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public float getResponse_code() {
        return response_code;
    }

    public void setResponse_code(float response_code) {
        this.response_code = response_code;
    }
}
