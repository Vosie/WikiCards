package org.vosie.wikicards;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;

public class LicenseActivity extends SecondTierActivity {
  private int[] links = {
          R.id.url_wikicards,
          R.id.url_flip,
          R.id.url_picasso,
          R.id.url_androidWheel
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.setContentView(R.layout.license);
    for (int link : links) {
      ((TextView) this.findViewById(link))
              .setMovementMethod(LinkMovementMethod.getInstance());
    }
  }

  @Override
  protected void onStart() {
    super.onStart();
    EasyTracker.getInstance(this).activityStart(this);
  }

  @Override
  protected void onStop() {
    super.onStop();
    EasyTracker.getInstance(this).activityStop(this);
  }
}
