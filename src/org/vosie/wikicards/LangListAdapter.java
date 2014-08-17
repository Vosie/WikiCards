package org.vosie.wikicards;

import org.vosie.wikicards.utils.IconFontUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

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

    Button delBtn = (Button) vwRet.findViewById(R.id.button_delete);
    Button dldBtn = (Button) vwRet.findViewById(R.id.button_download);
    Button optBtn = (Button) vwRet.findViewById(R.id.button_options);

    // set text
    delBtn.setText(IconFontUtils.get(IconFontUtils.REMOVE,
            context.getString(R.string.button_delete)));
    dldBtn.setText(IconFontUtils.get(IconFontUtils.DOWNLOAD,
            context.getString(R.string.button_download)));
    optBtn.setText(IconFontUtils.get(IconFontUtils.WRENCH,
            context.getString(R.string.button_options)));
    delBtn.setTypeface(Settings.iconFont);
    dldBtn.setTypeface(Settings.iconFont);
    optBtn.setTypeface(Settings.iconFont);

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
