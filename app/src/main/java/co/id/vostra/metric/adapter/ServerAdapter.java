package co.id.vostra.metric.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


import co.id.vostra.metric.R;
import co.id.vostra.metric.event.Ringtone;
import co.id.vostra.metric.model.ServerModel;
import co.id.vostra.metric.model.Services;
import co.id.vostra.metric.model.StatusModel;
import co.id.vostra.metric.network.IMetricHttpAPI;
import co.id.vostra.metric.network.IMetricStatusAPI;
import co.id.vostra.metric.network.MetricHttpAPI;
import co.id.vostra.metric.network.MetricStatusAPI;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ServerAdapter extends RecyclerView.Adapter<ServerAdapter.KontakViewHolder> {
    private List<ServerModel> serverModelList = new ArrayList<>();
    //    private List<Services> servicesList = new ArrayList<>();
    private Context context;

    public ServerAdapter(Context context, List<ServerModel> serverModelList) {
        this.context = context;
        this.serverModelList.addAll(serverModelList);
    }

    @Override
    public KontakViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View theView = LayoutInflater.from(context).inflate(R.layout.row_layout_server, parent, false);
        return new KontakViewHolder(theView);
    }

    @Override
    public void onBindViewHolder(final KontakViewHolder holder, final int position) {
        ServerModel serverModel = serverModelList.get(position);
        holder.txtView_nama.setText(serverModel.getName());
        holder.linear_nama_background.setBackgroundColor(Color.parseColor("#DDDDDD"));
        holder.setServerModel(serverModel);
        holder.refresh();

    }

    @Override
    public int getItemCount() {
        return serverModelList.size();
    }


    public class KontakViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar progressbarhttp, progressbarstatus;
        public TextView txtView_nama, txtView_http_time, txtView_http_code, txt_exception, txt_http;
        private LinearLayout linear_nama_background, linearLayoutServiceStatus;
        private RelativeLayout rel_http;
        private int level = 0;
        private float ave_cpu;

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        android.media.Ringtone r = RingtoneManager.getRingtone(context, notification);

        public void setServerModel(ServerModel serverModel) {
            this.serverModel = serverModel;
        }

        private ServerModel serverModel;

        public KontakViewHolder(View itemView) {
            super(itemView);
            txtView_nama = itemView.findViewById(R.id.nama);
            txtView_http_code = itemView.findViewById(R.id.http_code);
            txtView_http_time = itemView.findViewById(R.id.http_time);
            txt_http = itemView.findViewById(R.id.txt_http);
            txt_exception = itemView.findViewById(R.id.txt_exception);
            rel_http = itemView.findViewById(R.id.rel_http);

            linearLayoutServiceStatus = itemView.findViewById(R.id.layout_service_status);
            linear_nama_background = itemView.findViewById(R.id.nama_background);
//            progressbarhttp = itemView.findViewById(R.id.progressbarhttp);
            progressbarstatus = itemView.findViewById(R.id.progressbarstatus);


        }

        private void refresh() {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    for(final Services s:serverModel.getServices()){
                        level = 0;
                        switch (s.getMode()){
                            case "web":
                                txtView_http_code.setVisibility(View.GONE);
                                txtView_http_time.setVisibility(View.GONE);
                                txt_exception.setVisibility(View.GONE);
                                txt_http.setVisibility(View.GONE);
                                MetricHttpAPI.getClient(s.getUrl().startsWith("https")).create(IMetricHttpAPI.class).getMetric(s.getUrl()).enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        txt_http.setTextColor(Color.parseColor("#00695c"));

                                        txtView_http_code.setText(Integer.toString(response.code()));
                                        txtView_http_code.setTextColor(Color.parseColor("#00C851"));

                                        long requestTime = response.raw().sentRequestAtMillis();
                                        long responseTime = response.raw().receivedResponseAtMillis();
                                        long apiTimeMili = responseTime - requestTime;
                                        long apiTImeSec = TimeUnit.MILLISECONDS.toSeconds(apiTimeMili);

                                        txtView_http_time.setText(Long.toString(apiTimeMili));
                                        txtView_http_time.setTextColor(Color.parseColor("#00C851"));

                                        if ((response.code() >= 200 && response.code() < 300) && apiTImeSec <= 5 && level==0) {
                                            //HIJAU
                                            level = 0;
                                            linear_nama_background.setBackgroundColor(Color.parseColor("#00C851"));
                                        }else if ((apiTImeSec > 5 && apiTImeSec <= 15)) {
                                            //KUNING
                                            level = 1;
                                            linear_nama_background.setBackgroundColor(Color.parseColor("#ffbb33"));
                                            txtView_http_time.setTextColor(Color.parseColor("#ffbb33"));
                                            txt_exception.setText("Time = " + apiTimeMili);
                                            txt_exception.setVisibility(View.VISIBLE);

                                        }else if (response.code() < 200 || response.code() >= 300 || apiTImeSec > 15) {
                                            //MERAH
                                            level = 2;
                                            linear_nama_background.setBackgroundColor(Color.parseColor("#ff4444"));
                                            txtView_http_code.setTextColor(Color.parseColor("#ff4444"));
                                            txt_exception.setText("HTTP : "+"\n"+"Code = " + response.code() + " Time = " + apiTimeMili);
                                            txt_exception.setVisibility(View.VISIBLE);
                                            r.play();
                                        }

                                        if(apiTImeSec > 15){
                                            txtView_http_time.setTextColor(Color.parseColor("#ff4444"));
                                        }

                                    }
                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        progressbarstatus.setVisibility(View.GONE);
                                        linear_nama_background.setBackgroundColor(Color.parseColor("#ff4444"));
                                        level = 2;
                                        txt_exception.setText("HTTP : " + s.getUrl()+"\n"+t.getMessage());
                                        txt_exception.setVisibility(View.VISIBLE);
                                        r.play();
                                    }
                                });
                                break;

                            case "status":
                                linearLayoutServiceStatus.setVisibility(View.GONE);
                                progressbarstatus.setVisibility(View.VISIBLE);
                                txt_exception.setVisibility(View.GONE);
                                linearLayoutServiceStatus.removeAllViews();
                                MetricStatusAPI.getClient().create(IMetricStatusAPI.class).getMetric(s.getUrl()).enqueue(new Callback<StatusModel>() {
                                    @Override
                                    public void onResponse(Call<StatusModel> call, Response<StatusModel> response) {

                                        txtView_http_code.setVisibility(View.VISIBLE);
                                        txtView_http_time.setVisibility(View.VISIBLE);
                                        txt_http.setVisibility(View.VISIBLE);
                                        linearLayoutServiceStatus.setVisibility(View.VISIBLE);
                                        progressbarstatus.setVisibility(View.GONE);

                                        TextView mode_status = new TextView(context);
                                        mode_status.setText("STATUS");
                                        mode_status.setTextSize(9);
                                        mode_status.setTextColor(Color.parseColor("#00695c"));
                                        linearLayoutServiceStatus.addView(mode_status);

                                        TextView cpu_status = new TextView(context);
                                        int cpu = response.body().getCpu().size();
                                        for(int i = 0; i < cpu; i++){
                                            ave_cpu =+ response.body().getCpu().get(i);
                                        }
                                        float total_cpu = Math.round(ave_cpu / response.body().getCpu().size());
                                        cpu_status.setText(String.format("  Cpu = %s", total_cpu));
                                        cpu_status.setTypeface(null, Typeface.BOLD);
                                        cpu_status.setTextSize(10);
                                        linearLayoutServiceStatus.addView(cpu_status);

                                        TextView mem_status = new TextView(context);
                                        mem_status.setText(String.format("  Mem = %s", Float.toString(Math.round(response.body().getMemory()))));
                                        mem_status.setTypeface(null, Typeface.BOLD);
                                        mem_status.setTextSize(10);
                                        linearLayoutServiceStatus.addView(mem_status);

                                        TextView disk_status = new TextView(context);
                                        disk_status.setText(String.format("  Disk = %s", Float.toString(Math.round(response.body().getDisk()))));
                                        disk_status.setTypeface(null, Typeface.BOLD);
                                        disk_status.setTextSize(10);
                                        linearLayoutServiceStatus.addView(disk_status);

                                        if((total_cpu <= 75) && (Math.round(response.body().getMemory()) <= 75) && (Math.round(response.body().getDisk()) <= 75) && level==0) {
                                            //HIJAU
                                            level = 0;
                                            linear_nama_background.setBackgroundColor(Color.parseColor("#00C851"));
                                        }
                                        else if((total_cpu > 75 && total_cpu <= 85) || (Math.round(response.body().getMemory()) > 75 && Math.round(response.body().getMemory()) <= 85) || (Math.round(response.body().getDisk()) > 75 && Math.round(response.body().getDisk()) <= 85)) {
                                            //KUNING
                                            level = 1;
                                            linear_nama_background.setBackgroundColor(Color.parseColor("#ffbb33"));
                                            txt_exception.setText("CPU = " + total_cpu + " Memory = " +  Math.round(response.body().getMemory()) + " Disk = " + Math.round(response.body().getDisk()));
                                            txt_exception.setVisibility(View.VISIBLE);
                                        }else if((total_cpu > 85) || (Math.round(response.body().getMemory()) > 85) || (Math.round(response.body().getDisk()) > 85) || level==2) {
                                            //MERAH
                                            level = 2;
                                            linear_nama_background.setBackgroundColor(Color.parseColor("#ff4444"));
                                            txt_exception.setText("STATUS : "+"\n"+" CPU = " + total_cpu + " Memory = " +  Math.round(response.body().getMemory()) + " Disk = " + Math.round(response.body().getDisk()));
                                            txt_exception.setVisibility(View.VISIBLE);
                                            r.play();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<StatusModel> call, Throwable t) {
                                        progressbarstatus.setVisibility(View.GONE);
                                        linear_nama_background.setBackgroundColor(Color.parseColor("#ff4444"));
                                        level = 2;
                                        txt_exception.setText("STATUS : " + s.getUrl()+" "+t.getMessage());
                                        txt_exception.setVisibility(View.VISIBLE);
                                        r.play();
                                    }
                                });
                                break;
                        }
                    }
                    handler.postDelayed(this, 30 * 1000);
                }
            }, 10);

        }
    }
}