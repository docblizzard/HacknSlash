package com.mygdx.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.hack.Hackslash;
import com.mygdx.handlers.GameStateManager;

public abstract class GameState {
	protected GameStateManager gsm;
	protected Hackslash game;
	
	protected SpriteBatch batch;
	protected OrthographicCamera camera;
	protected OrthographicCamera hudCam;
	
	protected GameState(GameStateManager gsm) {
		this.gsm = gsm;
		game = gsm.game();
		batch = game.getSpriteBatch();
		camera = game.getCamera();
		hudCam = game.getHUDCamera();
	}
	
	public abstract void handleInput();
	public abstract void update(float dt);
	public abstract void render();
	public abstract void dispose();

} 
