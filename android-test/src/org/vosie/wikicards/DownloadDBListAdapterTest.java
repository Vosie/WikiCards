package org.vosie.wikicards;

import org.vosie.wikicards.DownloadDBListAdapter.DBItem;
import org.vosie.wikicards.utils.CategoryUtils;
import org.vosie.wikicards.utils.LanguageUtils;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.TextView;

public class DownloadDBListAdapterTest extends
                                      ActivityInstrumentationTestCase2<CardActivity> {

  private DownloadDBListAdapter adapter;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    Activity act = getActivity();
    adapter = new DownloadDBListAdapter(act);
  }

  public DownloadDBListAdapterTest() {
    super(CardActivity.class);
  }

  public void testGetCount() {
    int expected = Constants.SUPPORTED_LANGUAGES.length
            * CategoryUtils.CATEGORY_NAMES_ID.length;
    assertEquals(expected, adapter.getCount());
  }

  public void testGetItem() {
    DBItem item;
    item = (DBItem) adapter.getItem(0);
    assertEquals(Constants.SUPPORTED_LANGUAGES[0], item.lang);
    assertEquals(1, item.categroy);

    item = (DBItem) adapter.getItem(1);
    assertEquals(Constants.SUPPORTED_LANGUAGES[0], item.lang);
    assertEquals(2, item.categroy);

    item = (DBItem) adapter.getItem(2);
    assertEquals(Constants.SUPPORTED_LANGUAGES[1], item.lang);
    assertEquals(1, item.categroy);

    item = (DBItem) adapter.getItem(3);
    assertEquals(Constants.SUPPORTED_LANGUAGES[1], item.lang);
    assertEquals(2, item.categroy);
  }

  public void testGetHeader() {
    View view;
    String expectLangName;
    TextView txtView;

    expectLangName =
            LanguageUtils.getLocalizedLanguageName(
                    Constants.SUPPORTED_LANGUAGES[0]);
    view = adapter.getHeaderView(0, null, null);
    txtView = (TextView) view.findViewById(R.id.db_header);
    assertEquals(expectLangName, txtView.getText());

    expectLangName =
            LanguageUtils.getLocalizedLanguageName(
                    Constants.SUPPORTED_LANGUAGES[0]);
    view = adapter.getHeaderView(0, null, null);
    txtView = (TextView) view.findViewById(R.id.db_header);
    assertEquals(expectLangName, txtView.getText());
  }

  public void testGetView() {
    View view;
    TextView txtView;
    String excepted;

    view = adapter.getView(0, null, null);
    txtView = (TextView) view.findViewById(R.id.label_category_name);
    excepted = getActivity().getString(CategoryUtils.CATEGORY_NAMES_ID[0]);
    assertEquals(excepted, txtView.getText());

    view = adapter.getView(1, null, null);
    txtView = (TextView) view.findViewById(R.id.label_category_name);
    excepted = getActivity().getString(CategoryUtils.CATEGORY_NAMES_ID[1]);
    assertEquals(excepted, txtView.getText());

    view = adapter.getView(2, null, null);
    txtView = (TextView) view.findViewById(R.id.label_category_name);
    excepted = getActivity().getString(CategoryUtils.CATEGORY_NAMES_ID[0]);
    assertEquals(excepted, txtView.getText());

    view = adapter.getView(3, null, null);
    txtView = (TextView) view.findViewById(R.id.label_category_name);
    excepted = getActivity().getString(CategoryUtils.CATEGORY_NAMES_ID[1]);
    assertEquals(excepted, txtView.getText());

  }
}
