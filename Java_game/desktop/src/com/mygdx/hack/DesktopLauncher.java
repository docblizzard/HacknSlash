package com.mygdx.hack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.hack.Hackslash;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("Knight Slasher");
		config.useVsync(true);
		config.setWindowedMode(Hackslash.WINW , Hackslash.WINH );
//		config.setWindowSizeLimits(100, 100, 1920, 1080);
		new Lwjgl3Application(new Hackslash(), config);
	}
}
