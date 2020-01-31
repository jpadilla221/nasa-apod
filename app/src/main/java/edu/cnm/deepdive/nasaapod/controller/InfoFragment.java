package edu.cnm.deepdive.nasaapod.controller;

import android.app.Dialog;
import android.os.Bundle;
import android.util.EventLogTags.Description;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import edu.cnm.deepdive.nasaapod.R;

public class InfoFragment extends DialogFragment {

  public static final String TITLE_KEY = "title";
  public static final String  DESCRIPTION_KEY = "description";
  public static final String COPYRIGHT_KEY = "copyright";
  public static final String DATE_KEY = "date";

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
   String title = getArguments().getString(TITLE_KEY);
   String description = getArguments().getString(DESCRIPTION_KEY);
   //TODO Get any additional data.
    return new AlertDialog.Builder(getContext())
        .setIcon(R.drawable.ic_info_alert)
        .setTitle(title)
        .setMessage(description)
        .setNeutralButton("Ok", (dlg, which) ->{})
        .create();

  }

}
