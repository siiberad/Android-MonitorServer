package co.id.vostra.metric.network;


import com.google.gson.JsonObject;

import co.id.vostra.metric.model.StatusModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;


public interface IMetricStatusAPI {
    @GET()
    Call<StatusModel> getMetric(@Url String url);
}
