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
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.mygdx.hack.Hackslash;
import com.mygdx.handlers.B2DVars;
import com.mygdx.handlers.MyContactListener;

public class Enemy extends B2DSprite{

	public static final String LEFT = "LEFT";
	public static final String RIGHT = "RIGHT";
	public int HEALTH = 3;
	private boolean IDLE = true;
	
	public Enemy(Body body) {
		super(body);
		Texture tex = Hackslash.res.getTexture("enemyR");
		TextureRegion[] spritesenemy = TextureRegion.split(tex, tex.getWidth()/3, tex.getHeight()/1)[0];
		System.out.println(animation.getCurrentFrame());
		setAnimation(spritesenemy, 1/4f);
	}

	public void mobmovingTest(Enemy enemy, Player player) {
		Vector2 velocity_enemy = enemy.getBody().getLinearVelocity().cpy();
		Vector2 enemy_pos = enemy.getPosition();
		if (enemy_pos.x < player.getPosition().x - 1) {
			enemy.getBody().setLinearVelocity(1,velocity_enemy.y);
		}
		
		else if (enemy_pos.x > player.getPosition().x + 1) {
			enemy.getBody().setLinearVelocity(-1,velocity_enemy.y);

		}
		else enemy.getBody().setLinearVelocity(0,velocity_enemy.y);
		
		
	}
	
	public void mobJumpingTest(Enemy enemy, Player player, MyContactListener cl) {
		if (enemy.getBody().getPosition().y < player.getBody().getPosition().y && cl.isEnemyOnGround()) {
			enemy.getBody().applyForceToCenter(enemy.getBody().getPosition().x, 100, true);
		}
	}
	

	public int getHEALTH() {
		return HEALTH;
	}

	public void setHEALTH(int health) {
		HEALTH = health;
	}

	
}
