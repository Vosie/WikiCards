package org.vosie.wikicards.test.mock;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.ActionProvider;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

public class MockMenuItem implements MenuItem {

  public int itemId;
  public int groupId;
  public int order;

  @Override
  public int getItemId() {
    return itemId;
  }

  @Override
  public int getGroupId() {
    return groupId;
  }

  @Override
  public int getOrder() {
    return order;
  }

  @Override
  public MenuItem setTitle(CharSequence title) {
    return this;
  }

  @Override
  public MenuItem setTitle(int title) {
    return this;
  }

  @Override
  public CharSequence getTitle() {
    return null;
  }

  @Override
  public MenuItem setTitleCondensed(CharSequence title) {
    return this;
  }

  @Override
  public CharSequence getTitleCondensed() {
    return null;
  }

  @Override
  public MenuItem setIcon(Drawable icon) {
    return this;
  }

  @Override
  public MenuItem setIcon(int iconRes) {
    return this;
  }

  @Override
  public Drawable getIcon() {
    return null;
  }

  @Override
  public MenuItem setIntent(Intent intent) {
    return this;
  }

  @Override
  public Intent getIntent() {
    return null;
  }

  @Override
  public MenuItem setShortcut(char numericChar, char alphaChar) {
    return this;
  }

  @Override
  public MenuItem setNumericShortcut(char numericChar) {
    return this;
  }

  @Override
  public char getNumericShortcut() {
    return 0;
  }

  @Override
  public MenuItem setAlphabeticShortcut(char alphaChar) {
    return this;
  }

  @Override
  public char getAlphabeticShortcut() {
    return 0;
  }

  @Override
  public MenuItem setCheckable(boolean checkable) {
    return this;
  }

  @Override
  public boolean isCheckable() {
    return false;
  }

  @Override
  public MenuItem setChecked(boolean checked) {
    return this;
  }

  @Override
  public boolean isChecked() {
    return false;
  }

  @Override
  public MenuItem setVisible(boolean visible) {
    return this;
  }

  @Override
  public boolean isVisible() {
    return false;
  }

  @Override
  public MenuItem setEnabled(boolean enabled) {
    return this;
  }

  @Override
  public boolean isEnabled() {
    return false;
  }

  @Override
  public boolean hasSubMenu() {
    return false;
  }

  @Override
  public SubMenu getSubMenu() {
    return null;
  }

  @Override
  public MenuItem setOnMenuItemClickListener(OnMenuItemClickListener menuItemClickListener) {
    return this;
  }

  @Override
  public ContextMenuInfo getMenuInfo() {
    return null;
  }

  @Override
  public void setShowAsAction(int actionEnum) {

  }

  @Override
  public MenuItem setShowAsActionFlags(int actionEnum) {
    return this;
  }

  @Override
  public MenuItem setActionView(View view) {
    return this;
  }

  @Override
  public MenuItem setActionView(int resId) {
    return this;
  }

  @Override
  public View getActionView() {
    return null;
  }

  @Override
  public MenuItem setActionProvider(ActionProvider actionProvider) {
    return this;
  }

  @Override
  public ActionProvider getActionProvider() {
    return null;
  }

  @Override
  public boolean expandActionView() {
    return false;
  }

  @Override
  public boolean collapseActionView() {
    return false;
  }

  @Override
  public boolean isActionViewExpanded() {
    return false;
  }

  @Override
  public MenuItem setOnActionExpandListener(OnActionExpandListener listener) {
    return this;
  }

}
