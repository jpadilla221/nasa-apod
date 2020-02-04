package edu.cnm.deepdive.nasaapod;

import android.app.Application;
import com.facebook.stetho.Stetho;
import edu.cnm.deepdive.nasaapod.service.ApodDatabase;

public class ApodApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    Stetho.initializeWithDefaults(this);
    ApodDatabase.setContext(this);
    new Thread(() -> ApodDatabase.getInstance().getApodDao().delete()).start();
  }

}
