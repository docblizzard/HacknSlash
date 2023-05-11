package com.mygdx.entities;

import static com.mygdx.handlers.B2DVars.PPM;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.hack.Hackslash;
import com.mygdx.handlers.B2DVars;

public class Player extends B2DSprite{
	
	public static final String LEFT = "LEFT";
	public static final String RIGHT = "RIGHT";
	public static final String IDLE = "IDLE";
	public static final String RUNNING = "RUNNING";
	public static final String JUMPING = "JUMPING";

	private String STATE = IDLE;
	private String lookingDirection = RIGHT;
	private Vector2 velocity;
	private int HEALTH = 3;
	

	public Player(Body body) {
		super(body);
			Texture tex = Hackslash.res.getTexture("playerR");
			TextureRegion[] sprites = TextureRegion.split(tex, tex.getWidth()/2, tex.getHeight()/1)[0];
			System.out.println(animation.getCurrentFrame());
			setAnimation(sprites, 1/4f);
	
	}
	
	public void moving() {
		STATE = RUNNING;
		if (getLookingDirection().equals(LEFT)) {
			Texture tex = Hackslash.res.getTexture("playerRunningL");
			TextureRegion[] sprites = TextureRegion.split(tex, tex.getWidth()/5, tex.getHeight()/1)[0];
			setAnimation(sprites, 1/12f);
			System.out.println("Player is moving left");

		}
		else {
			Texture tex = Hackslash.res.getTexture("playerRunningR");
			TextureRegion[] sprites = TextureRegion.split(tex, tex.getWidth()/5, tex.getHeight()/1)[0];
			setAnimation(sprites, 1/12f);
			System.out.println("Player is moving right");

		}
	}
	
	public void idle() {
		STATE = IDLE;
			if (getLookingDirection().equals(LEFT)) { 
				Texture tex = Hackslash.res.getTexture("playerL");
				TextureRegion[] sprites = TextureRegion.split(tex, tex.getWidth()/2, tex.getHeight()/1)[0];
				setAnimation(sprites, 1/4f);
				System.out.println("Player is idling");
			}
			else {
				System.out.println(animation.getCurrentFrame());
				
				Texture tex = Hackslash.res.getTexture("playerR");
				TextureRegion[] sprites = TextureRegion.split(tex, tex.getWidth()/2, tex.getHeight()/1)[0];
				setAnimation(sprites, 1/4f);
				System.out.println("Player is idling");
			}		
	}
	
	// Create a box in front of the player that acts as the swords collision
	public void attack(Player player, World world, Array<Body> attackRemove) {
		 BodyDef bodyDef = new BodyDef();
		 bodyDef.type = BodyDef.BodyType.StaticBody;
		 if (player.getLookingDirection().equals(player.RIGHT)) {
			 bodyDef.position.set(player.getBody().getPosition().x+50/PPM, player.getBody().getPosition().y);
		 }
		 else {
			 bodyDef.position.set(player.getBody().getPosition().x-50/PPM, player.getBody().getPosition().y);
		 }

		 PolygonShape shape = new PolygonShape();
		 shape.setAsBox(30/PPM, 10/PPM);

		 FixtureDef fdef = new FixtureDef();
		 fdef.shape = shape;
//		 fdef.density = 1;
		 fdef.filter.categoryBits = B2DVars.BIT_SWORD;
		 fdef.filter.maskBits = B2DVars.BIT_ENNEMY;

		 Body attackBox = world.createBody(bodyDef);
		 attackBox.createFixture(fdef).setUserData("sword");
		 attackRemove.add(attackBox);
		 shape.dispose();
		 
		 // Reset timer
		}
	
	// Method to remove the attack box after a certain period
	public void removeAttack(World world, Array<Body> attackRemove, float timeSeconds, float period) {
		if (timeSeconds > period) {
			for (int i = 0; i < attackRemove.size ; i++) {
				world.destroyBody(attackRemove.get(i));
			}
			attackRemove.clear();
			timeSeconds = 0;
		}
		
	}
	
	
	public String getState() {
		return STATE;
	}
	
	public void setState(String state) {
		this.STATE = state;
	}
	
	public String getLookingDirection() {
		return lookingDirection;
	}

	public void setLookingDirection(String lookingDirection) {
		this.lookingDirection = lookingDirection;
	}

}
