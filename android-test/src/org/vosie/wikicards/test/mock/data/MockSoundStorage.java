package org.vosie.wikicards.test.mock.data;

import org.vosie.wikicards.data.SoundStorage;

import android.content.Context;

public class MockSoundStorage extends SoundStorage {

  public String gotLang;
  public String gotServerID;
  public Listener gotListener;

  public MockSoundStorage(Context ctx) {
    super(ctx);
  }

  @Override
  public void getSoundResource(String lang, String serverID, Listener l) {
    gotLang = lang;
    gotServerID = serverID;
    gotListener = l;
  }
}
