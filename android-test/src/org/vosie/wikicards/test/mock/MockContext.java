package org.vosie.wikicards.test.mock;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;

public class MockContext extends android.test.mock.MockContext {

  public static class ReceiverInfo {
    public BroadcastReceiver receiver;
    public IntentFilter filter;
  }

  public Resources resources;
  public ArrayList<ReceiverInfo> receivers = new ArrayList<ReceiverInfo>();

  @Override
  public Resources getResources() {
    return resources;
  }

  @Override
  public Intent registerReceiver(BroadcastReceiver receiver,
          IntentFilter filter) {
    ReceiverInfo info = new ReceiverInfo();
    info.receiver = receiver;
    info.filter = filter;
    receivers.add(info);
    return new Intent();
  }

  @Override
  public void unregisterReceiver(BroadcastReceiver receiver) {
    for (int i = 0; i < receivers.size(); i++) {
      if (receivers.get(i).receiver == receiver) {
        receivers.remove(i);
        break;
      }
    }
  }
}
