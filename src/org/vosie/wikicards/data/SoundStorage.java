package org.vosie.wikicards.data;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

public class SoundStorage {
  private Context ctx;
  private File cacheFolder;
  private Handler handler = new Handler();

  public interface Listener {
    public void onProgressing(int progress);

    public void onReady(File f);

    public void onError(int type, Exception ex);
  }

  public SoundStorage(Context ctx) {
    this.ctx = ctx;
    cacheFolder = this.ctx.getCacheDir();
  }

  public void setHandler(Handler h) {
    this.handler = h;
  }

  public void getSoundResource(String lang, String serverID, Listener l) {
    String fileName = "snd_cache_" + lang + serverID.hashCode();
    File fCached = new File(cacheFolder, fileName);
    if (fCached.exists()) {
      l.onReady(fCached);
    } else {
      startDownloadService(lang, serverID, fCached, l);
    }
  }

  private void startDownloadService(String lang, String serverID, File cache,
          Listener l) {
    Intent intent = new Intent(ctx, DownloadService.class);
    intent.putExtra("url", "http://api.vosie.org/wikicards/" + lang + "/" +
            serverID + ".mp3");
    intent.putExtra("receiver", new SoundReceiver(l, cache));
    intent.putExtra("destination", cache.getAbsolutePath());
    ctx.startService(intent);
  }

  private class SoundReceiver extends ResultReceiver {

    private Listener listener;
    private File cache;

    public SoundReceiver(Listener listener, File cache) {
      super(SoundStorage.this.handler);
      this.listener = listener;
      this.cache = cache;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
      super.onReceiveResult(resultCode, resultData);

      switch (resultCode) {
        case DownloadService.DOWNLOAD_COMPLETE:
          if (null != listener) {
            listener.onReady(cache);
          }
          break;

        case DownloadService.HTTP_ERROR:
          if (listener != null) {
            listener.onError(DownloadWordListener.HTTP_ERROR,
                    (Exception) resultData
                            .getSerializable(DownloadService.PARAM_EXCEPTION));
          }
          break;

        case DownloadService.INTERNAL_ERROR:
          if (listener != null) {
            listener.onError(DownloadWordListener.INTERNAL_ERROR,
                    (Exception) resultData
                            .getSerializable(DownloadService.PARAM_EXCEPTION));
          }
          break;
      }
    }
  }
}
