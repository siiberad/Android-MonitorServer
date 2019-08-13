package co.id.vostra.metric.network;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MetricHttpAPI {

    public static final String BASE_URL = "########";
    private static Retrofit retrofit = null;

    public static Retrofit getClient(boolean use_https) {
        OkHttpClient client = new OkHttpClient.Builder()

                .build();

        if(use_https){
            // Add legacy cipher suite for Android 4
            List<CipherSuite> cipherSuites = ConnectionSpec.MODERN_TLS.cipherSuites();
            if (!cipherSuites.contains(CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA)) {
                cipherSuites = new ArrayList(cipherSuites);
                cipherSuites.add(CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA);
            }
            ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                    .cipherSuites(cipherSuites.toArray(new CipherSuite[0]))
                    .build();
            client = new OkHttpClient.Builder()
                    .connectionSpecs(Collections.singletonList(spec))
                    .build();
        }


        return new Retrofit.Builder()
                .client(client)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}