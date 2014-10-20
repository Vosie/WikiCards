package org.vosie.wikicards.data;

import java.io.File;

import org.vosie.wikicards.MainActivity;
import org.vosie.wikicards.test.mock.ActivityWrapper;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

public class SoundStorageTest extends ActivityInstrumentationTestCase2<MainActivity> {

  private class TestActivityWrapper extends ActivityWrapper {
    public Intent gotServiceIntent;

    public TestActivityWrapper(Activity act) {
      super(act);
    }

    @Override
    public ComponentName startService(Intent service) {
      gotServiceIntent = service;
      return service.getComponent();
    }
  }

  private class DummySoundStorageListener implements SoundStorage.Listener {

    @Override
    public void onProgressing(int progress) {
    }

    @Override
    public void onReady(File f) {
    }

    @Override
    public void onError(int type, Exception ex) {
    }

  }

  public SoundStorageTest() {
    super(MainActivity.class);
  }

  public void testGetSoundResource() {
    TestActivityWrapper wrapper = new TestActivityWrapper(getActivity());
    SoundStorage ss = new SoundStorage(wrapper);
    SoundStorage.Listener eventListener = new DummySoundStorageListener();
    ss.getSoundResource("en", "country/ZWE", eventListener);

    assertEquals("http://api.vosie.org/wikicards/en/country/ZWE.mp3",
            wrapper.gotServiceIntent.getStringExtra("url"));
    assertNotNull(wrapper.gotServiceIntent.getParcelableExtra("receiver"));
    assertNotNull(wrapper.gotServiceIntent.getStringExtra("destination"));
  }
}
