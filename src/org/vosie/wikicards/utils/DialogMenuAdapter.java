package org.vosie.wikicards.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * This is the adapter used by MenuDialog.
 * 
 * @author hchu
 * 
 */
public class DialogMenuAdapter extends BaseAdapter {

  private DialogMenuItem[] menus;
  private Context context;

  public DialogMenuAdapter(DialogMenuItem[] menus, Context context) {
    super();
    if (null == menus || null == context) {
      throw new IllegalArgumentException();
    }
    this.menus = menus;
    this.context = context;
  }

  @Override
  public int getCount() {
    return menus.length;
  }

  @Override
  public Object getItem(int position) {
    return menus[position];
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    TextView tv = (TextView) convertView;
    if (null == tv) {
      tv = new TextView(context, null);
      tv.setPadding(10, 20, 10, 20);
      tv.setCompoundDrawablePadding(10);
      tv.setTextAppearance(context,
              android.R.style.TextAppearance_DeviceDefault_Large);
    }
    DialogMenuItem menu = menus[position];
    tv.setText(menu.text);
    if (menu.icon > 0) {
      tv.setCompoundDrawablesWithIntrinsicBounds(menu.icon, 0, 0, 0);
    }
    return tv;
  }
}
