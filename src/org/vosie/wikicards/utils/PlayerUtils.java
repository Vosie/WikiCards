package org.vosie.wikicards.utils;

import java.io.File;

import org.vosie.wikicards.R;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class PlayerUtils {
  private static final String TAG = "PlayerUtils";

  public interface Listener {
    public void onComplete(MediaPlayer mp, int error, int extra);
  }

  protected static PlayerUtils instance;

  public static PlayerUtils get() {
    synchronized (PlayerUtils.class) {
      if (null == instance) {
        instance = new PlayerUtils();
      }
    }
    return instance;
  }

  /**
   * The constructor of this class is only used by protected scope.
   */
  protected PlayerUtils() {

  }

  public MediaPlayer createPlayer(final Context ctx, File f, final Listener l) {
    MediaPlayer mp = MediaPlayer.create(ctx, Uri.fromFile(f));
    mp.setOnCompletionListener(new OnCompletionListener() {

      @Override
      public void onCompletion(MediaPlayer mp) {
        try {
          mp.reset();
          mp.release();
        } catch (Exception e) {
          Log.e(TAG, "error while playing mp3", e);
        } finally {
          if (null != l) {
            l.onComplete(mp, 0, 0);
          }
        }
      }
    });
    mp.setOnErrorListener(new OnErrorListener() {

      @Override
      public boolean onError(MediaPlayer mp, int what, int extra) {
        try {
          mp.reset();
          mp.release();
        } catch (Exception e) {
          Log.e(TAG, "error while playing mp3: " + what + ", " + extra, e);
        } finally {
          if (null != l) {
            l.onComplete(mp, what, extra);
          }
        }
        Toast.makeText(ctx, R.string.msg_error_to_play, Toast.LENGTH_SHORT)
                .show();
        return false;
      }

    });
    return mp;
  }
}
