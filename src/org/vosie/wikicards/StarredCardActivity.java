package org.vosie.wikicards;

public class StarredCardActivity extends CardActivity {

  @Override
  protected String[] getServerIDs() {
    return starredWordsStorage.getStarredWordIDs();
  }
  
  @Override
  protected int getStartCardPosition() {
    return 0;
  }
  
}
