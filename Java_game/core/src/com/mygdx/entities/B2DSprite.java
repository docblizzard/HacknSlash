package com.mygdx.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.handlers.Animation;
import com.mygdx.handlers.B2DVars;

public class B2DSprite {
	
	protected Body body;
	protected Animation animation;
	protected float width;
	protected float height;
	
	public B2DSprite(Body body) {
		this.body = body;
		animation = new Animation();
	}
	public void setAnimation(TextureRegion[] reg, float delay) {
		animation.setFrames(reg,delay);
		width = reg[0].getRegionWidth();
		height = reg[0].getRegionHeight();
		}
	
	public void update(float dt) {
		animation.update(dt);
	}
	
	public void render(SpriteBatch batch) {
		batch.begin();
		batch.draw(
				animation.getFrame(),
				body.getPosition().x * B2DVars.PPM - width / 2,
				body.getPosition().y * B2DVars.PPM - height / 2);

		batch.end();
	}
	public Body getBody() {
		return body;
	}
	
	public Vector2 getPosition() {
		return body.getPosition();	
	}
	
	public float getWidth() {
		return width;	
	}
	
	public float getHeight() {
		return height;	
	}
	public Animation getAnimation() {
		return animation;
	}
}
	
