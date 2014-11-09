package org.vosie.wikicards;

import org.vosie.wikicards.utils.CategoryUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CategoryListAdapter extends BaseAdapter {

  private Context context;

  public CategoryListAdapter(Context ctx) {
    context = ctx;
  }

  @Override
  public int getCount() {
    return CategoryUtils.CATEGORY_NAMES_ID.length;
  }

  @Override
  public Object getItem(int position) {
    return context.getString(CategoryUtils.CATEGORY_NAMES_ID[position]);
  }

  @Override
  public long getItemId(int position) {
    return position + 1;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View vwRet = convertView;
    if (null == vwRet) {
      vwRet = LayoutInflater.from(context)
              .inflate(android.R.layout.simple_list_item_1, null);
    }

    TextView txtView = (TextView) vwRet.findViewById(android.R.id.text1);
    txtView.setText(context.getString(CategoryUtils.CATEGORY_NAMES_ID[position]));

    return vwRet;
  }
}
