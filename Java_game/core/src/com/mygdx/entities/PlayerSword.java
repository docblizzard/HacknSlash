package com.mygdx.entities;

public class PlayerSword {
	private float timeSpent;
	private float LifeSpan;
	private boolean remove;
	
	public void update(float dt) {
		timeSpent += dt;
		if ( timeSpent > LifeSpan ) {
			remove = true;
		}
	}
}
