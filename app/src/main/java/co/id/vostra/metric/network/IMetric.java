package co.id.vostra.metric.network;


import co.id.vostra.metric.model.MetricModel;
import retrofit2.Call;
import retrofit2.http.GET;


public interface IMetric {
    @GET("/api/v1/statusmetric")
    Call<MetricModel> getAll();
}
