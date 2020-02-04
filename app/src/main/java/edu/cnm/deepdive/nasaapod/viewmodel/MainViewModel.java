package edu.cnm.deepdive.nasaapod.viewmodel;

import static android.icu.text.Collator.getInstance;

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

import edu.cnm.deepdive.nasaapod.model.dao.ApodDao;
import edu.cnm.deepdive.nasaapod.model.entity.Apod;
import edu.cnm.deepdive.nasaapod.service.ApodDatabase;
import edu.cnm.deepdive.nasaapod.service.ApodService;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainViewModel extends AndroidViewModel {

  private Date apodDate;
  private MutableLiveData<Apod> apod;
  private MutableLiveData<Throwable> throwable;
  private ApodDatabase database;
  private ApodService nasa;

  public MainViewModel(@NonNull Application application) {
    super(application);
    database = ApodDatabase.getInstance();
    nasa = ApodService.getInstance();
    apod = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    Date today = new Date();
    String formattedDate = ApodService.DATE_FORMATTER.format(today);
    try {
      setApodDate(ApodService.DATE_FORMATTER.parse(formattedDate)); // TODO Investigate adjustment for NASA APOD-relevant time zone.
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }

  public LiveData<Apod> getApod() {
    return apod;
  }

  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  public void setApodDate(Date date) {
    ApodDao dao = database.getApodDao();
    dao.select(date)
        .subscribeOn(Schedulers.io())
        .subscribe(
            (apod) -> this.apod.postValue(apod),
            (throwable) -> this.throwable.postValue(throwable),
            () -> nasa.get(BuildConfig.API_KEY, ApodService.DATE_FORMATTER.format(date))
                .subscribeOn(Schedulers.io())
                .subscribe(
                    (apod) -> dao.insert(apod)
                        .subscribeOn(Schedulers.io())
                        .subscribe(
                            (id) ->{
                              apod.setId(id);
                              this.apod.postValue(apod);
                            },
                            (throwable) -> this.throwable.postValue(throwable)
                        ),
                    (throwable) -> this.throwable.postValue(throwable)
                )
        );

  }


}
