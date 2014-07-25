package org.vosie.wikicards.utils;

import java.util.EventListener;

public interface DialogMenuListener extends EventListener {
  /**
   * called when a menu item is called
   * 
   * @param item
   */
  public void onMenuItemClicked(DialogMenuItem item);

  /**
   * called when dialog is cancelled.
   */
  public void onDialogCancelled();
}
