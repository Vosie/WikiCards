package org.vosie.wikicards.test.mock;

import android.app.Activity;
import android.content.res.Resources;

public class MockActivity extends Activity {
  public Resources resources;

  @Override
  public Resources getResources() {
      return resources;
  }
}
