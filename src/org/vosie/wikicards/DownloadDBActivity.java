package org.vosie.wikicards;

import org.vosie.wikicards.utils.NetworkUtils;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.analytics.tracking.android.EasyTracker;

public class DownloadDBActivity extends Activity implements Constants {

  private DownloadDBListAdapter adapter;
  private BroadcastReceiver networkNotifier;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_download_db);
    initAdapter();
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

  @Override
  protected void onResume() {
    initNetworkNotifier();
    super.onResume();
  }

  @Override
  protected void onPause() {
    NetworkUtils.get().removeInternetStateNotifier(this, networkNotifier);
    super.onPause();
  }

  private void initNetworkNotifier() {
    networkNotifier = NetworkUtils.get().notifyInternetState(this,
            new Runnable() {
              @Override
              public void run() {
                adapter.notifyDataSetChanged();
              }
            }, new Runnable() {
              @Override
              public void run() {
                adapter.notifyDataSetChanged();
              }
            });
  }

  private void initAdapter() {
    // use LangListAdapter to show all supported languages.

    StickyListHeadersListView stickyList = (StickyListHeadersListView) findViewById(R.id.list);
    adapter = new DownloadDBListAdapter(this);
    stickyList.setAdapter(adapter);

  }
}
