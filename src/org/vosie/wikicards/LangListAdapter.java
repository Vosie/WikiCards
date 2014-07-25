package org.vosie.wikicards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class LangListAdapter extends BaseAdapter implements Constants,
                                                OnClickListener {

  /**
   * The UIHandler to customize UI and handle click event of each button.
   * 
   * @author hchu
   * 
   */
  public static interface UIHandler {
    public void customizeUI(View ui, String langCode);

    public void handleButtonClick(int id, String langCode);
  }

  private Context context;
  private UIHandler customizer;

  public LangListAdapter(Context ctx, UIHandler customizer) {
    this.context = ctx;
    this.customizer = customizer;
  }

  @Override
  public int getCount() {
    return SUPPORTED_LANGUAGES.length;
  }

  @Override
  public Object getItem(int position) {
    return SUPPORTED_LANGUAGES[position];
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {

    View vwRet = convertView;
    if (null == vwRet) {
      vwRet = LayoutInflater.from(context).inflate(R.layout.list_language_row,
              null);
    }

    View delBtn = vwRet.findViewById(R.id.button_delete);
    View dldBtn = vwRet.findViewById(R.id.button_download);
    View optBtn = vwRet.findViewById(R.id.button_options);
    // override listeners
    delBtn.setOnClickListener(this);
    dldBtn.setOnClickListener(this);
    optBtn.setOnClickListener(this);
    // hide all of them
    delBtn.setVisibility(View.GONE);
    dldBtn.setVisibility(View.GONE);
    optBtn.setVisibility(View.GONE);
    // change data
    delBtn.setTag(SUPPORTED_LANGUAGES[position]);
    dldBtn.setTag(SUPPORTED_LANGUAGES[position]);
    optBtn.setTag(SUPPORTED_LANGUAGES[position]);

    if (null != this.customizer) {
      this.customizer.customizeUI(vwRet, SUPPORTED_LANGUAGES[position]);
    }

    return vwRet;
  }

  @Override
  public void onClick(View v) {
    if (null != this.customizer) {
      this.customizer.handleButtonClick(v.getId(), "" + v.getTag());
    }

  }

}
