package com.mygdx.handlers;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

public class MyInputProcessor extends InputAdapter{
	
	
	public boolean keyDown(int k) {
		if (k == Keys.A) {
			MyInput.setKey(MyInput.BUTTON1, true);
		}
		
		if (k == Keys.D) {
			MyInput.setKey(MyInput.BUTTON2, true);
		}
		
		if (k == Keys.SPACE) {
			MyInput.setKey(MyInput.BUTTON3, true);
		}
		if (k == Keys.E) {
			MyInput.setKey(MyInput.BUTTON4, true);
		}
		if (k == Keys.Q) {
			MyInput.setKey(MyInput.BUTTON5, true);
		}
		return true;
	}
	
	
	public boolean keyUp(int k) {
		
		if (k == Keys.A) {
			MyInput.setKey(MyInput.BUTTON1, false);
		}
		
		if (k == Keys.D) {
			MyInput.setKey(MyInput.BUTTON2, false);
		}
		
		if (k == Keys.SPACE) {
			MyInput.setKey(MyInput.BUTTON3, false);
		}
		
		if (k == Keys.E) {
			MyInput.setKey(MyInput.BUTTON4, false);
		}
		if (k == Keys.Q) {
			MyInput.setKey(MyInput.BUTTON5, false);
		}
		return true;
	}
}
