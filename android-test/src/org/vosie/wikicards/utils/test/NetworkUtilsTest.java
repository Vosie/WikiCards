package org.vosie.wikicards.utils.test;

import junit.framework.TestCase;

import org.vosie.wikicards.test.mock.MockContext;
import org.vosie.wikicards.utils.NetworkUtils;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.net.ConnectivityManager;

public class NetworkUtilsTest extends TestCase {

  public void testIsNetworkAvailable() {
    // We cannot test it because there is no way to mock connectivity manager or
    // manipulate it. If you know how to do it, please help us.
  }

  public static class StateInfo {
    public static boolean onlineCalled = false;
    public static boolean offlineCalled = false;
  }

  public void testInternetState() {

    MockContext ctx = new MockContext();
    BroadcastReceiver receiver = NetworkUtils.get().notifyInternetState(ctx,
            new Runnable() {

              @Override
              public void run() {
                StateInfo.onlineCalled = true;
              }

            }, new Runnable() {

              @Override
              public void run() {
                StateInfo.offlineCalled = true;
              }
            });
    assertEquals(1, ctx.receivers.size());
    Intent intent = new Intent();
    // make it offline
    intent.putExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, true);
    ctx.receivers.get(0).receiver.onReceive(ctx, intent);
    // test online/offline
    assertFalse(StateInfo.onlineCalled);
    assertTrue(StateInfo.offlineCalled);
    // reset it
    StateInfo.offlineCalled = false;
    // make it online
    intent.putExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
    ctx.receivers.get(0).receiver.onReceive(ctx, intent);
    // test online/offline
    assertFalse(StateInfo.offlineCalled);
    assertTrue(StateInfo.onlineCalled);
    // remove notifier
    NetworkUtils.get().removeInternetStateNotifier(ctx, receiver);
    // check the size
    assertEquals(0, ctx.receivers.size());
  }
}
