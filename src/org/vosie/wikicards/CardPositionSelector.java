package org.vosie.wikicards;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class CardPositionSelector {
  private Context ctx;
  private View view;
  private TextView totalTextView;
  private WheelView numberWheel;
  private int total;
  private OnSelectListener onSelectListener;
  private AlertDialog dialog;

  public CardPositionSelector(Context ctx, int total, OnSelectListener listener) {
    this.ctx = ctx;
    this.total = total;
    view = LayoutInflater.from(ctx)
            .inflate(R.layout.card_positon_selector, null);
    totalTextView = (TextView) view.findViewById(R.id.totalTextView);
    totalTextView.setText(String.valueOf(total));
    onSelectListener = listener;

    initNumberWheel();
    initDialog();
  }

  private void initNumberWheel() {
    numberWheel = (WheelView) view.findViewById(R.id.current_position);
    Integer[] indexs = new Integer[total];
    for (int i = 0; i < total; i++) {
      indexs[i] = i + 1;
    }
    ArrayWheelAdapter<Integer> adapter =
            new ArrayWheelAdapter<Integer>(ctx, indexs);
    numberWheel.setViewAdapter(adapter);
  }

  private void initDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
    builder.setIcon(R.drawable.ic_launcher);
    builder.setCancelable(true);
    builder.setTitle(ctx.getString(R.string.dialog_title_option_menu));
    builder.setView(view);
    builder.setPositiveButton(ctx.getString(android.R.string.ok),
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                onSelectListener.onSelect(numberWheel.getCurrentItem());
              }
            });
    builder.setNegativeButton(ctx.getString(android.R.string.cancel), null);
    dialog = builder.create();
  }

  public void setCurrentItem(int item) {
    numberWheel.setCurrentItem(item);
  }

  public int getCurrentItem() {
    return numberWheel.getCurrentItem();
  }

  public void show(int currentItem) {
    setCurrentItem(currentItem);
    dialog.show();
  }

  public AlertDialog getDialog() {
    return dialog;
  }

  interface OnSelectListener {
    void onSelect(int item);
  }
}
