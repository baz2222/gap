package com.juniorgames.gap;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.juniorgames.gap.GapGame;

public class AndroidLauncher extends AndroidApplication {
	AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
	@Override
	protected void onCreate(Bundle s) {
		super.onCreate(s);
		androidScreenSetup();
		switchToGDX();
	}
	@SuppressLint("SourceLockedOrientationActivity")
	private void androidScreenSetup(){
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}
	private void switchToGDX(){
		cfg.useAccelerometer=false;
		cfg.useCompass=false;
		cfg.hideStatusBar=true;
		cfg.useWakelock=true;
		initialize(new GapGame(),cfg);
	}
}
