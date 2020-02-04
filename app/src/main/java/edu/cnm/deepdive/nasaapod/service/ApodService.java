package edu.cnm.deepdive.nasaapod.service;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.cnm.deepdive.nasaapod.BuildConfig;
import edu.cnm.deepdive.nasaapod.model.entity.Apod;
import io.reactivex.Single;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApodService {

  String DATE_FORMAT = "yyyy-MM-dd";
  DateFormat DATE_FORMATTER = new SimpleDateFormat(DATE_FORMAT);

  static ApodService getInstance() {
    return InstanceHolder.INSTANCE;
  }


  @GET("planetary/apod")
  Single<Apod> get(@Query("api_key") String apiKey, @Query("date") String date);

  class InstanceHolder {

    private static final ApodService INSTANCE;
    static {
      Gson gson = new GsonBuilder()
          .excludeFieldsWithoutExposeAnnotation()
          .setDateFormat(DATE_FORMAT)
          .create();
      HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
      interceptor.setLevel(Level.BODY);
      OkHttpClient client = new OkHttpClient.Builder()
          .addInterceptor(interceptor)
          .build();
      Retrofit retrofit =  new Retrofit.Builder()
          .addConverterFactory(GsonConverterFactory.create(gson))
          .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
          .client(client)
          .baseUrl(BuildConfig.BASE_URL)
          .build();
      INSTANCE = retrofit.create(ApodService.class);


    }
  }

}
