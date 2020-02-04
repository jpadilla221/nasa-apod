package edu.cnm.deepdive.nasaapod.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import edu.cnm.deepdive.nasaapod.BuildConfig;
import edu.cnm.deepdive.nasaapod.model.dao.ApodDao;
import edu.cnm.deepdive.nasaapod.model.entity.Apod;
import edu.cnm.deepdive.nasaapod.service.ApodDatabase;
import edu.cnm.deepdive.nasaapod.service.ApodService;
import io.reactivex.schedulers.Schedulers;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainViewModel extends AndroidViewModel {

  private ApodDatabase database;
  private ApodService nasa;
  private MutableLiveData<Apod> apod;
  private MutableLiveData<Throwable> throwable;

  public MainViewModel(@NonNull Application application) {
    super(application);
    database = ApodDatabase.getInstance();
    nasa = ApodService.getInstance();
    apod = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    Calendar today = Calendar.getInstance();
    int offset = today.get(Calendar.ZONE_OFFSET);
    today.set(Calendar.HOUR, 0);
    today.set(Calendar.MINUTE, 0);
    today.set(Calendar.SECOND, 0);
    today.set(Calendar.MILLISECOND, 0);
    today.set(Calendar.ZONE_OFFSET, offset);
    setApodDate(today.getTime()); // TODO Investigate adjustment for NASA APOD-relevant time zone.
  }

  public LiveData<Apod> getApod() {
    return apod;
  }

  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  public void setApodDate(Date date) {
    ApodDao dao = database.getApodDao();
    DateFormat formatter = ApodService.DATE_FORMATTER;
    dao.select(date)
        .subscribeOn(Schedulers.io())
        .subscribe(
            (apod) -> this.apod.postValue(apod),
            (throwable) -> this.throwable.postValue(throwable),
            () -> nasa.get(BuildConfig.API_KEY, formatter.format(date))
                .subscribeOn(Schedulers.io())
                .subscribe(
                    (apod) -> dao.insert(apod)
                        .subscribeOn(Schedulers.io())
                        .subscribe(
                            (id) -> {
                              apod.setId(id);
                              this.apod.postValue(apod);
                            },
                            (throwable) -> this.throwable.postValue(throwable)
                        ),
                    (throwable) -> this.throwable.postValue(throwable))
        );
  }

}
