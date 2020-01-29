package edu.cnm.deepdive.nasaapod.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.cnm.deepdive.nasaapod.BuildConfig;
import edu.cnm.deepdive.nasaapod.model.Apod;
import edu.cnm.deepdive.nasaapod.service.ApodService;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainViewModel extends AndroidViewModel {
  private Date apodDate;
  private MutableLiveData<Apod> apod;

  public MainViewModel(@NonNull Application application) {
    super(application);
    apod = new MutableLiveData<>();
    setApodDate(new Date()); // TODO Investigate adjustment for NASA APOD-relevant time zone.

  }

  public LiveData<Apod> getApod() {
    return apod;
  }
public void setApodDate(Date date) {
    apodDate = date;
    new Retriever().start();
}
  private class Retriever extends Thread {

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    @Override
    public void run() {
      @SuppressLint("SimpleDateFormat")
      DateFormat format = new SimpleDateFormat(DATE_FORMAT);
      String formattedDAte = format.format(apodDate);
      Gson gson = new GsonBuilder()
          .excludeFieldsWithoutExposeAnnotation()
          .setDateFormat("yyyy-MM-dd")
          .create();
      Retrofit retrofit = new Retrofit.Builder()
          .baseUrl(BuildConfig.BASE_URL)
          .addConverterFactory(GsonConverterFactory.create(gson))
          .build();
      ApodService service = retrofit.create(ApodService.class);
      try {
        Response<Apod> response =service.get(BuildConfig.API_KEY, "formattedDate").execute();
        if(response.isSuccessful()) {
          Apod apod = response.body();
          MainViewModel.this.apod.postValue(apod);

        } else {
          Log.e("ApodService", response.message());
        }
      } catch (IOException e) {
        Log.e("ApodService", e.getMessage(), e);
      }


    }
  }

}
