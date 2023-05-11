package com.mygdx.hack;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.handlers.Content;
import com.mygdx.handlers.GameStateManager;
import com.mygdx.handlers.MyInput;
import com.mygdx.handlers.MyInputProcessor;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;


public class Hackslash extends ApplicationAdapter {
	public static final int WINH = 1000	;
	public static final int WINW = 1880;
//	public static final int SCALE = 2;
	Texture img;
	private Texture bucketImage;
	private Texture dropImage;
	
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private	OrthographicCamera hudCam;
	private GameStateManager gsm;
	private Rectangle bucket;
	public static Content res;
	
	public static final float STEP = 1 / 60f;	
	private float accum;
	
	public void create () {
		
		
		res = new Content();
		res.loadTexture("playerL.png", "playerL");
		res.loadTexture("playerR.png", "playerR");
		res.loadTexture("playerRunningL.png", "playerRunningL");
		res.loadTexture("playerRunningR.png", "playerRunningR");
		res.loadTexture("EnemyR.png", "enemyR");
		Gdx.input.setInputProcessor(new MyInputProcessor());
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Hackslash.WINW, Hackslash.WINH);
		hudCam = new OrthographicCamera();
		hudCam.setToOrtho(false, Hackslash.WINW, Hackslash.WINH);


		gsm = new GameStateManager(this);

	}
	
	public void render () {
		accum += Gdx.graphics.getDeltaTime();
		while (accum >= STEP) {
			accum -= STEP;
			gsm.update(STEP);
			gsm.render();
			MyInput.update();
		}

	}
	
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
	public void resize( int w, int h) {
		
	}
	
	public void pause() {
		
	}
	
	public void resume() {
		
	}
	
	public SpriteBatch getSpriteBatch() { return batch; }
	public OrthographicCamera getCamera() { return camera; }
	public OrthographicCamera getHUDCamera() { return hudCam; }

}
