package com.mygdx.handlers;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;
import com.mygdx.entities.Enemy;
import com.mygdx.entities.Player;

public class MyContactListener implements ContactListener {
	
	private int playernumFootContacts;	
	private int enemynumFootContacts;
	private Array<Body> enemiesHit;
	private Enemy enemy;
	
	public MyContactListener() {
		super();
		enemiesHit = new Array<Body>();
	}
	
	public void beginContact(Contact c) {
		Fixture	fa = c.getFixtureA();
		Fixture fb = c.getFixtureB();

		if((fb.getUserData().equals("Platform") && fa.getUserData().equals("foot")) || (fa.getUserData().equals("Platform") && fb.getUserData().equals("foot"))) {
			playernumFootContacts ++;
		}
		// Enemy ground contact check
		if((fb.getUserData().equals("Platform") && fa.getUserData().equals("Enemy")) || (fa.getUserData().equals("Platform") && fb.getUserData().equals("Enemy"))) {
			enemynumFootContacts ++;
		}
		if((fb.getUserData().equals("sword") && fa.getUserData().equals("Enemy")) || (fa.getUserData().equals("Enemy") && fb.getUserData().equals("sword"))) {
			System.out.println("Contact is made");
			if (fb.getUserData().equals("Enemy")) {
				System.out.println("fb");
				enemiesHit.add(fb.getBody());			
			}
			else if (fa.getUserData().equals("Enemy")) {
				System.out.println("fb");
				enemiesHit.add(fa.getBody());	
			}
		}
		
	}
	
	public void endContact(Contact c) {
		Fixture	fa = c.getFixtureA();
		Fixture fb = c.getFixtureB();

		// Player no ground contact check
		if((fb.getUserData().equals("Platform") && fa.getUserData().equals("foot")) || (fa.getUserData().equals("Platform") && fb.getUserData().equals("foot"))) {
			playernumFootContacts --;
		}
		
		// Enemy no ground contact check
		if((fb.getUserData().equals("Platform") && fa.getUserData().equals("Enemy")) || (fa.getUserData().equals("Platform") && fb.getUserData().equals("Enemy"))) {
			enemynumFootContacts --;
		}

	}
	
	public boolean checkOnTop(Fixture platform, Fixture humanoid) {
		return humanoid.getBody().getPosition().y >= platform.getBody().getPosition().y;
	}
	
	public void preSolve(Contact c, Manifold m) {
		
	}
	
	public void postSolve(Contact c, ContactImpulse ci) {
		
	}
	
	public boolean isPlayerOnGround() {
		return playernumFootContacts > 0;
	}
		
	public boolean isEnemyOnGround() {
		return enemynumFootContacts > 0;
	}
	
	public Array<Body> getEnemiesHit() {
		return enemiesHit;
	}
}
