package org.vosie.wikicards.test.mock.utils;

import org.vosie.wikicards.utils.NetworkUtils;

import android.content.Context;

public class MockNetworkUtils extends NetworkUtils {
  public boolean networkAvailability;
  private NetworkUtils real;

  public void mock() {
    real = NetworkUtils.instance;
    NetworkUtils.instance = this;
  }

  public void restore() {
    NetworkUtils.instance = real;
  }

  @Override
  public boolean isNetworkAvailable(Context context) {
    return networkAvailability;
  }
}
