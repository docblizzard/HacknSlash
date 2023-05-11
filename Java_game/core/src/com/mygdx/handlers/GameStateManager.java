package com.mygdx.handlers;

import java.util.Stack;

import com.mygdx.hack.Hackslash;
import com.mygdx.states.GameState;
import com.mygdx.states.Play;

public class GameStateManager {
	private Hackslash game;
	
	private Stack<GameState> gameStates;
	
	public static final int PLAY = 912837;
	
	public GameStateManager(Hackslash game) {
		this.game = game;
		gameStates = new Stack<GameState>();
		pushState(PLAY);
	}
	
	public Hackslash game() { return game; }
	
	public void update(float dt) {
		gameStates.peek().update(dt);
	}
	
	public void render() {
		gameStates.peek().render();
	}
	
	private GameState getState(int state) {
		if (state == PLAY) return new Play(this);
		return null;
	}
	public void setState(int state) {
		popState();
		pushState(state);
	}
	
	public void pushState(int state) {
		gameStates.push(getState(state));
	}
	
	public void popState() {
		GameState gameST = gameStates.pop();
		gameST.dispose();
	}
}
