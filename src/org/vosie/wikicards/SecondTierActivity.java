package org.vosie.wikicards;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

public abstract class SecondTierActivity extends Activity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getActionBar().setDisplayHomeAsUpEnabled(true);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (android.R.id.home == item.getItemId()) {
      this.onBackPressed();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
