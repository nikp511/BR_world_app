package application;

import android.app.Application;
import android.app.DownloadManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


import androidx.appcompat.app.AppCompatDelegate;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import com.movie.brworld.BuildConfig;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import constants.ConstantCodes;
import network.RetrofitInterface;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MyApplication extends Application {

    private LocalBroadcastManager mLocalBroadcastManager;
    private static DownloadManager mDownloadManager;
    private RetrofitInterface mRetrofitInterface;


    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        mDownloadManager = (DownloadManager) getSystemService
                (DOWNLOAD_SERVICE);

        initializeRetrofit();



    }


    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    private void initializeRetrofit() {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(ConstantCodes.TIMEOUT_SECONDS, TimeUnit.SECONDS);
        builder.connectTimeout(ConstantCodes.TIMEOUT_SECONDS, TimeUnit.SECONDS);
        builder.followRedirects(false);

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }

        builder.addInterceptor(
                new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        Request request = original.newBuilder()
                                .header("Content-Type", "com/movie/brworld/application/json")
                                .method(original.method(), original.body())
                                .build();
                        return chain.proceed(request);
                    }
                });

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ConstantCodes.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build()).build();
        mRetrofitInterface = retrofit.create(RetrofitInterface.class);
    }

    /**
     * Used to get retrofit interface for ws.wolfsoft.propertyplanetapp.network reference
     *
     * @return Retrofit interface reference
     */

    public RetrofitInterface getRetroFitInterface() {
        return mRetrofitInterface;
    }

    public boolean isInternetConnected() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
