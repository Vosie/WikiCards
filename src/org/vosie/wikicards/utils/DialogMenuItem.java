package org.vosie.wikicards.utils;

/**
 * A data object to present a menu
 * 
 * @author hchu
 * 
 */
public class DialogMenuItem {
  /**
   * the id of this menu item
   */
  public int id;
  /**
   * the icon of this menu item. We should use 0 when we don't want to show any
   * icon.
   */
  public int icon;
  /**
   * the text of this menu item
   */
  public CharSequence text;

  public static DialogMenuItem create(int id, int icon, CharSequence t) {
    DialogMenuItem m = new DialogMenuItem();
    m.id = id;
    m.icon = icon;
    m.text = t;
    return m;
  }
}
