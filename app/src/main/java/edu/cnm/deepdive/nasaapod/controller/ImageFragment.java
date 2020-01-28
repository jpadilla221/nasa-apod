package edu.cnm.deepdive.nasaapod.controller;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ServiceWorkerWebSettings;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.cnm.deepdive.nasaapod.BuildConfig;
import edu.cnm.deepdive.nasaapod.R;
import edu.cnm.deepdive.nasaapod.model.Apod;
import edu.cnm.deepdive.nasaapod.service.ApodService;
import java.io.IOException;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ImageFragment extends Fragment {
  private static final String IMAGE_URL =
      "https://apod.nasa.gov/apod/image/2001/ic410_WISEantonucci_1824.jpg";

  private WebView contentView;

  @SuppressLint("SetJavaScriptEnabled")
  public View onCreateView(@NonNull LayoutInflater inflater,
      ViewGroup container, Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_image, container, false);
    setupWebView(root);

    return root;
  }

  private void setupWebView(View root) {
    contentView = root.findViewById(R.id.content_view);
    contentView.setWebViewClient(new WebViewClient() {
      @Override
      public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return false;

      }

      @Override
      public void onPageFinished(WebView view, String url) {

        // TODO Update view to indicate that load is complete.
      }
    });
    WebSettings settings = contentView.getSettings();
    settings.setJavaScriptEnabled(true);
    settings.setSupportZoom(true);
    settings.setBuiltInZoomControls(true);
    settings.setDisplayZoomControls(false);
    settings.setUseWideViewPort(true);
    settings.setLoadWithOverviewMode(true);
    new Retriever().start();
  }

private class Retriever extends Thread {

  @Override
  public void run() {
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
      Response<Apod> response =service.get(BuildConfig.API_KEY, "2019-01-27").execute();
      if(response.isSuccessful()) {
        Apod apod = response.body();
        String url =apod.getUrl();
        getActivity().runOnUiThread(() -> contentView.loadUrl(url));


      } else {
        Log.e("ApodService", response.message());
      }
    } catch (IOException e) {
      Log.e("ApodService", e.getMessage(), e);
    }


  }
}
}