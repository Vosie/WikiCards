package org.vosie.wikicards.test.mock;

import java.io.FileDescriptor;
import java.io.PrintWriter;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;

public class ActivityWrapper extends Activity {
  private Activity parent;

  public ActivityWrapper(Activity parent) {
    this.parent = parent;
    this.attachBaseContext(parent);
  }

  @Override
  public Intent getIntent() {
    return parent.getIntent();
  }

  @Override
  public void setIntent(Intent newIntent) {
    parent.setIntent(newIntent);
  }

  @Override
  public WindowManager getWindowManager() {
    return parent.getWindowManager();
  }

  @Override
  public Window getWindow() {
    return parent.getWindow();
  }

  @Override
  public LoaderManager getLoaderManager() {
    return parent.getLoaderManager();
  }

  @Override
  public View getCurrentFocus() {
    return parent.getCurrentFocus();
  }

  @Override
  public boolean onCreateThumbnail(Bitmap outBitmap, Canvas canvas) {
    return parent.onCreateThumbnail(outBitmap, canvas);
  }

  @Override
  public CharSequence onCreateDescription() {
    return parent.onCreateDescription();
  }

  @Override
  public void onProvideAssistData(Bundle data) {
    parent.onProvideAssistData(data);
  }

  @Override
  public void reportFullyDrawn() {
    parent.reportFullyDrawn();
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    parent.onConfigurationChanged(newConfig);
  }

  @Override
  public int getChangingConfigurations() {
    return parent.getChangingConfigurations();
  }

  @Override
  @Deprecated
  public Object getLastNonConfigurationInstance() {
    return parent.getLastNonConfigurationInstance();
  }

  @Override
  @Deprecated
  public Object onRetainNonConfigurationInstance() {
    return parent.onRetainNonConfigurationInstance();
  }

  @Override
  public void onLowMemory() {
    parent.onLowMemory();
  }

  @Override
  public void onTrimMemory(int level) {
    parent.onTrimMemory(level);
  }

  @Override
  public FragmentManager getFragmentManager() {
    return parent.getFragmentManager();
  }

  @Override
  public void onAttachFragment(Fragment fragment) {
    parent.onAttachFragment(fragment);
  }

  @Override
  @Deprecated
  public void startManagingCursor(Cursor c) {
    parent.startManagingCursor(c);
  }

  @Override
  @Deprecated
  public void stopManagingCursor(Cursor c) {
    parent.stopManagingCursor(c);
  }

  @Override
  public View findViewById(int id) {
    return parent.findViewById(id);
  }

  @Override
  public ActionBar getActionBar() {
    return parent.getActionBar();
  }

  @Override
  public void setContentView(int layoutResID) {
    parent.setContentView(layoutResID);
  }

  @Override
  public void setContentView(View view) {
    parent.setContentView(view);
  }

  @Override
  public void setContentView(View view, LayoutParams params) {
    parent.setContentView(view, params);
  }

  @Override
  public void addContentView(View view, LayoutParams params) {
    parent.addContentView(view, params);
  }

  @Override
  public void setFinishOnTouchOutside(boolean finish) {
    parent.setFinishOnTouchOutside(finish);
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    return parent.onKeyDown(keyCode, event);
  }

  @Override
  public boolean onKeyLongPress(int keyCode, KeyEvent event) {
    return parent.onKeyLongPress(keyCode, event);
  }

  @Override
  public boolean onKeyUp(int keyCode, KeyEvent event) {
    return parent.onKeyUp(keyCode, event);
  }

  @Override
  public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
    return parent.onKeyMultiple(keyCode, repeatCount, event);
  }

  @Override
  public void onBackPressed() {
    parent.onBackPressed();
  }

  @Override
  public boolean onKeyShortcut(int keyCode, KeyEvent event) {
    return parent.onKeyShortcut(keyCode, event);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    return parent.onTouchEvent(event);
  }

  @Override
  public boolean onTrackballEvent(MotionEvent event) {
    return parent.onTrackballEvent(event);
  }

  @Override
  public boolean onGenericMotionEvent(MotionEvent event) {
    return parent.onGenericMotionEvent(event);
  }

  @Override
  public void onUserInteraction() {
    parent.onUserInteraction();
  }

  @Override
  public void onWindowAttributesChanged(android.view.WindowManager.LayoutParams params) {
    parent.onWindowAttributesChanged(params);
  }

  @Override
  public void onContentChanged() {
    parent.onContentChanged();
  }

  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    parent.onWindowFocusChanged(hasFocus);
  }

  @Override
  public void onAttachedToWindow() {
    parent.onAttachedToWindow();
  }

  @Override
  public void onDetachedFromWindow() {
    parent.onDetachedFromWindow();
  }

  @Override
  public boolean hasWindowFocus() {
    return parent.hasWindowFocus();
  }

  @Override
  public boolean dispatchKeyEvent(KeyEvent event) {
    return parent.dispatchKeyEvent(event);
  }

  @Override
  public boolean dispatchKeyShortcutEvent(KeyEvent event) {
    return parent.dispatchKeyShortcutEvent(event);
  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
    return parent.dispatchTouchEvent(ev);
  }

  @Override
  public boolean dispatchTrackballEvent(MotionEvent ev) {
    return parent.dispatchTrackballEvent(ev);
  }

  @Override
  public boolean dispatchGenericMotionEvent(MotionEvent ev) {
    return parent.dispatchGenericMotionEvent(ev);
  }

  @Override
  public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
    return parent.dispatchPopulateAccessibilityEvent(event);
  }

  @Override
  public View onCreatePanelView(int featureId) {
    return parent.onCreatePanelView(featureId);
  }

  @Override
  public boolean onCreatePanelMenu(int featureId, Menu menu) {
    return parent.onCreatePanelMenu(featureId, menu);
  }

  @Override
  public boolean onPreparePanel(int featureId, View view, Menu menu) {
    return parent.onPreparePanel(featureId, view, menu);
  }

  @Override
  public boolean onMenuOpened(int featureId, Menu menu) {
    return parent.onMenuOpened(featureId, menu);
  }

  @Override
  public boolean onMenuItemSelected(int featureId, MenuItem item) {
    return parent.onMenuItemSelected(featureId, item);
  }

  @Override
  public void onPanelClosed(int featureId, Menu menu) {
    parent.onPanelClosed(featureId, menu);
  }

  @Override
  public void invalidateOptionsMenu() {
    parent.invalidateOptionsMenu();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    return parent.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    return parent.onPrepareOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    return parent.onOptionsItemSelected(item);
  }

  @Override
  public boolean onNavigateUp() {
    return parent.onNavigateUp();
  }

  @Override
  public boolean onNavigateUpFromChild(Activity child) {
    return parent.onNavigateUpFromChild(child);
  }

  @Override
  public void onCreateNavigateUpTaskStack(TaskStackBuilder builder) {
    parent.onCreateNavigateUpTaskStack(builder);
  }

  @Override
  public void onPrepareNavigateUpTaskStack(TaskStackBuilder builder) {
    parent.onPrepareNavigateUpTaskStack(builder);
  }

  @Override
  public void onOptionsMenuClosed(Menu menu) {
    parent.onOptionsMenuClosed(menu);
  }

  @Override
  public void openOptionsMenu() {
    parent.openOptionsMenu();
  }

  @Override
  public void closeOptionsMenu() {
    parent.closeOptionsMenu();
  }

  @Override
  public void onCreateContextMenu(ContextMenu menu, View v,
          ContextMenuInfo menuInfo) {
    parent.onCreateContextMenu(menu, v, menuInfo);
  }

  @Override
  public void registerForContextMenu(View view) {
    parent.registerForContextMenu(view);
  }

  @Override
  public void unregisterForContextMenu(View view) {
    parent.unregisterForContextMenu(view);
  }

  @Override
  public void openContextMenu(View view) {
    parent.openContextMenu(view);
  }

  @Override
  public void closeContextMenu() {
    parent.closeContextMenu();
  }

  @Override
  public boolean onContextItemSelected(MenuItem item) {
    return parent.onContextItemSelected(item);
  }

  @Override
  public void onContextMenuClosed(Menu menu) {
    parent.onContextMenuClosed(menu);
  }

  @Override
  public boolean onSearchRequested() {
    return parent.onSearchRequested();
  }

  @Override
  public void startSearch(String initialQuery, boolean selectInitialQuery,
          Bundle appSearchData, boolean globalSearch) {
    parent.startSearch(initialQuery, selectInitialQuery, appSearchData,
            globalSearch);
  }

  @Override
  public void triggerSearch(String query, Bundle appSearchData) {
    parent.triggerSearch(query, appSearchData);
  }

  @Override
  public void takeKeyEvents(boolean get) {
    parent.takeKeyEvents(get);
  }

  @Override
  public LayoutInflater getLayoutInflater() {
    return parent.getLayoutInflater();
  }

  @Override
  public MenuInflater getMenuInflater() {
    return parent.getMenuInflater();
  }

  @Override
  public void startActivityForResult(Intent intent, int requestCode) {
    parent.startActivityForResult(intent, requestCode);
  }

  @Override
  public void startActivityForResult(Intent intent, int requestCode,
          Bundle options) {
    parent.startActivityForResult(intent, requestCode, options);
  }

  @Override
  public void startIntentSenderForResult(IntentSender intent, int requestCode,
          Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags)
          throws SendIntentException {
    parent.startIntentSenderForResult(intent, requestCode, fillInIntent,
            flagsMask, flagsValues, extraFlags);
  }

  @Override
  public void startIntentSenderForResult(IntentSender intent, int requestCode,
          Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags,
          Bundle options) throws SendIntentException {
    parent.startIntentSenderForResult(intent, requestCode, fillInIntent,
            flagsMask, flagsValues, extraFlags, options);
  }

  @Override
  public void startActivity(Intent intent) {
    parent.startActivity(intent);
  }

  @Override
  public void startActivity(Intent intent, Bundle options) {
    parent.startActivity(intent, options);
  }

  @Override
  public void startActivities(Intent[] intents) {
    parent.startActivities(intents);
  }

  @Override
  public void startActivities(Intent[] intents, Bundle options) {
    parent.startActivities(intents, options);
  }

  @Override
  public void startIntentSender(IntentSender intent, Intent fillInIntent,
          int flagsMask, int flagsValues, int extraFlags)
          throws SendIntentException {
    parent.startIntentSender(intent, fillInIntent, flagsMask, flagsValues,
            extraFlags);
  }

  @Override
  public void startIntentSender(IntentSender intent, Intent fillInIntent,
          int flagsMask, int flagsValues, int extraFlags, Bundle options)
          throws SendIntentException {
    parent.startIntentSender(intent, fillInIntent, flagsMask, flagsValues,
            extraFlags, options);
  }

  @Override
  public boolean startActivityIfNeeded(Intent intent, int requestCode) {
    return parent.startActivityIfNeeded(intent, requestCode);
  }

  @Override
  public boolean startActivityIfNeeded(Intent intent, int requestCode,
          Bundle options) {
    return parent.startActivityIfNeeded(intent, requestCode, options);
  }

  @Override
  public boolean startNextMatchingActivity(Intent intent) {
    return parent.startNextMatchingActivity(intent);
  }

  @Override
  public boolean startNextMatchingActivity(Intent intent, Bundle options) {
    return parent.startNextMatchingActivity(intent, options);
  }

  @Override
  public void startActivityFromChild(Activity child, Intent intent,
          int requestCode) {
    parent.startActivityFromChild(child, intent, requestCode);
  }

  @Override
  public void startActivityFromChild(Activity child, Intent intent,
          int requestCode, Bundle options) {
    parent.startActivityFromChild(child, intent, requestCode, options);
  }

  @Override
  public void startActivityFromFragment(Fragment fragment, Intent intent,
          int requestCode) {
    parent.startActivityFromFragment(fragment, intent, requestCode);
  }

  @Override
  public void startActivityFromFragment(Fragment fragment, Intent intent,
          int requestCode, Bundle options) {
    parent.startActivityFromFragment(fragment, intent, requestCode, options);
  }

  @Override
  public void startIntentSenderFromChild(Activity child, IntentSender intent,
          int requestCode, Intent fillInIntent, int flagsMask, int flagsValues,
          int extraFlags) throws SendIntentException {
    parent.startIntentSenderFromChild(child, intent, requestCode, fillInIntent,
            flagsMask, flagsValues, extraFlags);
  }

  @Override
  public void startIntentSenderFromChild(Activity child, IntentSender intent,
          int requestCode, Intent fillInIntent, int flagsMask, int flagsValues,
          int extraFlags, Bundle options) throws SendIntentException {
    parent.startIntentSenderFromChild(child, intent, requestCode, fillInIntent,
            flagsMask, flagsValues, extraFlags, options);
  }

  @Override
  public void overridePendingTransition(int enterAnim, int exitAnim) {
    parent.overridePendingTransition(enterAnim, exitAnim);
  }

  @Override
  public String getCallingPackage() {
    return parent.getCallingPackage();
  }

  @Override
  public ComponentName getCallingActivity() {
    return parent.getCallingActivity();
  }

  @Override
  public void setVisible(boolean visible) {
    parent.setVisible(visible);
  }

  @Override
  public boolean isFinishing() {
    return parent.isFinishing();
  }

  @Override
  public boolean isDestroyed() {
    return parent.isDestroyed();
  }

  @Override
  public boolean isChangingConfigurations() {
    return parent.isChangingConfigurations();
  }

  @Override
  public void recreate() {
    parent.recreate();
  }

  @Override
  public void finish() {
    parent.finish();
  }

  @Override
  public void finishAffinity() {
    parent.finishAffinity();
  }

  @Override
  public void finishFromChild(Activity child) {
    parent.finishFromChild(child);
  }

  @Override
  public void finishActivity(int requestCode) {
    parent.finishActivity(requestCode);
  }

  @Override
  public void finishActivityFromChild(Activity child, int requestCode) {
    parent.finishActivityFromChild(child, requestCode);
  }

  @Override
  public PendingIntent createPendingResult(int requestCode, Intent data, int flags) {
    return parent.createPendingResult(requestCode, data, flags);
  }

  @Override
  public void setRequestedOrientation(int requestedOrientation) {
    parent.setRequestedOrientation(requestedOrientation);
  }

  @Override
  public int getRequestedOrientation() {
    return parent.getRequestedOrientation();
  }

  @Override
  public int getTaskId() {
    return parent.getTaskId();
  }

  @Override
  public boolean isTaskRoot() {
    return parent.isTaskRoot();
  }

  @Override
  public boolean moveTaskToBack(boolean nonRoot) {
    return parent.moveTaskToBack(nonRoot);
  }

  @Override
  public String getLocalClassName() {
    return parent.getLocalClassName();
  }

  @Override
  public ComponentName getComponentName() {
    return parent.getComponentName();
  }

  @Override
  public SharedPreferences getPreferences(int mode) {
    return parent.getPreferences(mode);
  }

  @Override
  public Object getSystemService(String name) {
    return parent.getSystemService(name);
  }

  @Override
  public void setTitle(CharSequence title) {
    parent.setTitle(title);
  }

  @Override
  public void setTitle(int titleId) {
    parent.setTitle(titleId);
  }

  @Override
  public void setTitleColor(int textColor) {
    parent.setTitleColor(textColor);
  }

  @Override
  public View onCreateView(String name, Context context, AttributeSet attrs) {
    return parent.onCreateView(name, context, attrs);
  }

  @Override
  public View onCreateView(View parent, String name, Context context,
          AttributeSet attrs) {
    return this.parent.onCreateView(parent, name, context, attrs);
  }

  @Override
  public void dump(String prefix, FileDescriptor fd, PrintWriter writer,
          String[] args) {
    parent.dump(prefix, fd, writer, args);
  }

  @Override
  public boolean isImmersive() {
    return parent.isImmersive();
  }

  @Override
  public void setImmersive(boolean i) {
    parent.setImmersive(i);
  }

  @Override
  public ActionMode startActionMode(Callback callback) {
    return parent.startActionMode(callback);
  }

  @Override
  public ActionMode onWindowStartingActionMode(Callback callback) {
    return parent.onWindowStartingActionMode(callback);
  }

  @Override
  public void onActionModeStarted(ActionMode mode) {
    parent.onActionModeStarted(mode);
  }

  @Override
  public void onActionModeFinished(ActionMode mode) {
    parent.onActionModeFinished(mode);
  }

  @Override
  public boolean shouldUpRecreateTask(Intent targetIntent) {
    return parent.shouldUpRecreateTask(targetIntent);
  }

  @Override
  public boolean navigateUpTo(Intent upIntent) {
    return parent.navigateUpTo(upIntent);
  }

  @Override
  public boolean navigateUpToFromChild(Activity child, Intent upIntent) {
    return parent.navigateUpToFromChild(child, upIntent);
  }

  @Override
  public Intent getParentActivityIntent() {
    return parent.getParentActivityIntent();
  }

}
