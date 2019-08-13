package co.id.vostra.metric;


import android.app.ProgressDialog;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import co.id.vostra.metric.adapter.ServerAdapter;
import co.id.vostra.metric.event.Ringtone;
import co.id.vostra.metric.model.MetricModel;
import co.id.vostra.metric.model.ServerModel;
import co.id.vostra.metric.model.Services;
import co.id.vostra.metric.network.MetricDB;
import co.id.vostra.metric.network.IMetric;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EventBus bus = EventBus.getDefault();

    private ServerAdapter serverAdapter;

    private RecyclerView recyclerView;
    ProgressDialog progressDialog;

    private List<ServerModel> serverModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        getSupportActionBar().hide(); // hide the title bar
//        bus.register(this);

        int resId = R.anim.layout_animation_fall_down;
        final LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        serverModelList = new ArrayList<>();


        getItem();
    }

   public void getItem(){
       IMetric service = MetricDB.getClient().create(IMetric.class);
       Call<MetricModel> call = service.getAll();
       call.enqueue(new Callback<MetricModel>() {
           @Override
           public void onResponse(Call<MetricModel> call, Response<MetricModel> response) {
               generateDataList(response.body().getServers());
               serverModelList.addAll(response.body().getServers());
               progressDialog.dismiss();
           }
           @Override
           public void onFailure(Call<MetricModel> call, Throwable t) {
               progressDialog.dismiss();
               Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
           }
       });
   }

    private void generateDataList(List<ServerModel> serverList) {
        recyclerView = findViewById(R.id.customRecyclerView);
        int resId = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        recyclerView.setLayoutAnimation(animation);
        serverAdapter = new ServerAdapter(this, serverList);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(MainActivity.this, 6);
        recyclerView.setLayoutManager(mGridLayoutManager);
        recyclerView.setAdapter(serverAdapter);
    }



//    @Subscribe
//    public void onEvent(Ringtone event) {
//        if(event.isNotif()) {
//            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            android.media.Ringtone r = RingtoneManager.getRingtone(MainActivity.this, notification);
//            r.play();
//        }
//    }
}