package org.vosie.wikicards;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class LicenseActivity extends Activity {
  private int[] links = { R.id.url_wikicards };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.setContentView(R.layout.license);
    for (int link : links) {
      ((TextView) this.findViewById(link))
              .setMovementMethod(LinkMovementMethod.getInstance());
    }
  }
}
