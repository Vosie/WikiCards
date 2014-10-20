package org.vosie.wikicards.test.mock.utils;

import java.io.File;

import org.vosie.wikicards.utils.PlayerUtils;

import android.content.Context;
import android.media.MediaPlayer;

public class MockPlayerUtils extends PlayerUtils {
  public static MockPlayerUtils createInstance() {
    MockPlayerUtils ret = new MockPlayerUtils();
    synchronized (PlayerUtils.class) {
      PlayerUtils.instance = ret;
    }
    return ret;
  }

  public MediaPlayer createdPlayer;

  @Override
  public MediaPlayer createPlayer(Context ctx, File f, Listener l) {
    createdPlayer = super.createPlayer(ctx, f, l);
    return createdPlayer;
  }
}
