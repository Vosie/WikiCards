package org.vosie.wikicards.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.vosie.wikicards.MainActivity;

import android.media.MediaPlayer;
import android.test.ActivityInstrumentationTestCase2;

public class PlayerUtilsTest extends ActivityInstrumentationTestCase2<MainActivity> {

  private boolean playCompleted = false;
  private MediaPlayer player = null;

  public PlayerUtilsTest() {
    super(MainActivity.class);
  }

  private File createTestResource() throws IOException {
    InputStream is = this.getInstrumentation().getContext().getAssets()
            .open("ZWE.mp3");
    File tmpFile = new File(this.getActivity().getCacheDir(), "tmp.mp3");
    if (tmpFile.exists()) {
      tmpFile.delete();
    }
    FileOutputStream fos = new FileOutputStream(tmpFile);
    try {
      IOUtils.copy(is, fos);
    } finally {
      is.close();
      fos.close();
    }
    return tmpFile;
  }

  public void testCreatePlayer() throws Throwable {
    final File testFile = createTestResource();
    playCompleted = false;
    // to get event fired, we should create player at a thread with Looper.
    this.runTestOnUiThread(new Runnable() {

      @Override
      public void run() {
        player = PlayerUtils.get().createPlayer(getActivity(),
                testFile,
                new PlayerUtils.Listener() {

                  @Override
                  public void onComplete(MediaPlayer mp, int error, int extra) {
                    playCompleted = true;
                    synchronized (player) {
                      player.notify();
                    }
                  }
                });
      }

    });

    this.getInstrumentation().waitForIdleSync();
    // let's play it on emulator.
    player.start();
    assertNotNull(player);
    synchronized (player) {
      // we wait for 20 sec to play the file, it should only take 5 sec to do
      // play ZWE.mp3.
      player.wait(20 * 1000);
    }
    assertTrue(playCompleted);
    testFile.delete();

  }
}
