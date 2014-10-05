package org.vosie.wikicards;

import org.vosie.wikicards.utils.CategoryUtils;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.TextView;

public class CategoryListAdpterTest extends
                                   ActivityInstrumentationTestCase2<CardActivity> {

  public CategoryListAdpterTest() {
    super(CardActivity.class);
  }

  public void testInitCorrectly() {
    Activity act = getActivity();
    CategoryListAdapter adapter = new CategoryListAdapter(act);
    View view = adapter.getView(0, null, null);
    TextView txtView = (TextView) view.findViewById(android.R.id.text1);
    assertEquals(CategoryUtils.CATEGORY_NAMES_ID.length, adapter.getCount());
    assertEquals(act.getString(CategoryUtils.CATEGORY_NAMES_ID[0]),
            adapter.getItem(0));
    assertEquals(1, adapter.getItemId(0));
    assertEquals(act.getString(CategoryUtils.CATEGORY_NAMES_ID[0]),
            txtView.getText());
  }
}
