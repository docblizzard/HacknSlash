package com.mygdx.states;

import static com.mygdx.handlers.B2DVars.PPM;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.entities.Enemy;
import com.mygdx.entities.Player;
import com.mygdx.hack.Hackslash;
import com.mygdx.handlers.B2DVars;
import com.mygdx.handlers.GameStateManager;
import com.mygdx.handlers.MyContactListener;
import com.mygdx.handlers.MyInput;

public class Play extends GameState {
	
	private boolean collisionTest = true;
	private World world;
	private Box2DDebugRenderer b2dr;
	private OrthographicCamera b2dCam;
	
	private Player player;
	private Enemy enemy;
	
	private MyContactListener cl;
	private TiledMap tileMap;
	private OrthogonalTiledMapRenderer tmr;
	
	private Array<Enemy> enemiesAlive = new Array<>();
	private Array<Body> attackRemove = new Array<>();
	
	private float timeSeconds = 0f;
	private float period = 0.2f;

	
	public Play(GameStateManager gsm) {
		
		super(gsm);
		
		// Set up world and Collision event listener
		world = new World(new Vector2(0,-9.81f), true);
		cl = new MyContactListener();
		world.setContactListener(cl);
		b2dr = new Box2DDebugRenderer();
		
		// Create the different objects on the map
		createPlatforms();
		createEnemy();
		createEnemy2();
		createPlayer();
		
		//cam setup
		b2dCam = new OrthographicCamera();
		b2dCam.setToOrtho(false, Hackslash.WINW /PPM, Hackslash.WINH/PPM);
		
	}

	
	public  void handleInput() {
		Vector2 velocity_player = player.getBody().getLinearVelocity().cpy();
		
		// Player is jumping
		if (MyInput.isPressed(MyInput.BUTTON3) && (cl.isPlayerOnGround()) && (timeSeconds > period)) { 
				player.getBody().applyForceToCenter(velocity_player.x, 440, true);
				timeSeconds = 0;
				
		}
		
		// Player is running left
		if (MyInput.isDown(MyInput.BUTTON1) ) {
			
			player.getBody().setLinearVelocity(-3,velocity_player.y);	
			// Checks that either the player is not already looking in the direction he wants to go, or if he is idle and looking at direction he wants to go
			// The purpose of this check is for the animation to transition to left/right even when pressing both buttons at once, but also not looping the animation too fast
			if (player.getLookingDirection().equals(player.RIGHT) || player.getLookingDirection().equals(player.LEFT) && player.getState().equals(player.IDLE)) {
				player.setLookingDirection(player.LEFT);			
				player.moving();
			}				
		}
		
		// Player is running right
		if (MyInput.isDown(MyInput.BUTTON2) ) {
			player.getBody().setLinearVelocity(3,velocity_player.y);
			
			// Checks that either the player is not already looking in the direction he wants to go, or if he is idle and looking at direction he wants to go
			// The purpose of this check is for the animation to transition to left/right even when pressing both buttons at once, but also not looping the animation too fast
			if (player.getLookingDirection().equals(player.LEFT) || player.getLookingDirection().equals(player.RIGHT) && player.getState().equals(player.IDLE)) {
				player.setLookingDirection(player.RIGHT);
				player.moving();
			}			
			
		}
		
		// Player is not pressing on any buttons, idle animation is set
		if (!(MyInput.isDown(MyInput.BUTTON1) || MyInput.isDown(MyInput.BUTTON2))){
			if (!(player.getState().equals(player.IDLE) )) {
				player.getBody().setLinearVelocity(0,velocity_player.y);
				player.idle();
			}
		}
		
		// Registering player attacking
		if (MyInput.isPressed(MyInput.BUTTON4)){
			
		}
		
		if (MyInput.isPressed(MyInput.BUTTON5)){
			player.attack(player, world, attackRemove);
			timeSeconds = 0;
		}
	}
	
	public  void update(float dt) {
		timeSeconds +=Gdx.graphics.getDeltaTime();
		handleInput();
		
		// Enemies moving around ability
		// Calculates also if ennemies get hit
		Array <Body> enemiesHit = cl.getEnemiesHit();
		for (int i = 0; i < enemiesAlive.size; i++ ) {
			enemiesAlive.get(i).mobmovingTest(enemiesAlive.get(i), player);
			enemiesAlive.get(i).mobJumpingTest(enemiesAlive.get(i), player, cl);
			for (int j = 0; j < enemiesHit.size; j++) {
				if (enemiesAlive.get(i).getBody().getPosition().x == enemiesHit.get(j).getPosition().x && 
					enemiesAlive.get(i).getBody().getPosition().y == enemiesHit.get(j).getPosition().y ) {
					System.out.println("Ennemy has lost one hp");
					enemiesAlive.get(i).HEALTH -= 1;
				}
			}
		}
		enemiesHit.clear();
		
		world.step(dt, 6, 2);
		player.update(dt);
		
		// Updating enemies 
		for (int i= 0; i < enemiesAlive.size; i++ ) {
			enemiesAlive.get(i).update(dt);
		}
				
		removeEnemy(enemiesAlive, world);
		player.removeAttack(world, attackRemove, timeSeconds, period);
		
	}
	
	public  void render() {
		Gdx.gl20.glClear( GL20.GL_COLOR_BUFFER_BIT );	
				
		//Set camera to follow player
//		camera.position.set(
//				player.getPosition().x * PPM + Hackslash.WINW / 4,
//				Hackslash.WINH / 2, 0
//				);
		camera.update();
		
		
		// Draw Map
		tmr.setView(camera);
		tmr.render();
				
		// Draw Player
		batch.setProjectionMatrix(camera.combined);
		player.render(batch);
		
		// Removing bodies test
		for (int i = 0; i < enemiesAlive.size; i++){
			enemiesAlive.get(i).render(batch);
		}
		
				
		// To see collision world
		if (collisionTest) {
//			b2dCam.position.set(player.getPosition().x *  Hackslash.WINW/4, Hackslash.WINW / 2 , 0);
			b2dCam.update();
			b2dr.render(world, b2dCam.combined);
		}
	}
	public  void dispose() {}
	
	private void createPlayer() {
		
		// Creation of player
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		
		bdef.position.set(400/PPM, 650/PPM);
		bdef.type = BodyType.DynamicBody;
		Body body = world.createBody(bdef);
		
		shape.setAsBox(48 /PPM, 48/PPM);
		fdef.shape = shape;
//		fdef.restitution = 0.1f;
		fdef.friction = 0;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits = B2DVars.BIT_GROUND | B2DVars.BIT_ENNEMY;
		fdef.isSensor = true;

		body.createFixture(fdef).setUserData("Joueur");
		
		// Create foot sensor for the player
		shape.setAsBox(25/PPM, 48/PPM, new Vector2(0, 0/PPM), 0);
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits = B2DVars.BIT_GROUND | B2DVars.BIT_ENNEMY ;
		fdef.isSensor = false;
		body.createFixture(fdef).setUserData("foot");
		
		// create player
		player = new Player(body);
		shape.dispose();


	}
	
	private void createEnemy() {

		BodyDef bdef = new BodyDef();
		bdef.position.set(500/PPM, 720/PPM);
		bdef.type = BodyType.DynamicBody;
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(48 /PPM, 48/PPM);
		
		
			
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_ENNEMY;
		fdef.filter.maskBits = B2DVars.BIT_GROUND | B2DVars.BIT_PLAYER | B2DVars.BIT_ENNEMY;
		
		Body body = world.createBody(bdef);	
		body.createFixture(fdef).setUserData("Enemy");
		enemy = new Enemy(body);
		body.setUserData(enemy);
		enemiesAlive.add(enemy);
		shape.dispose();

	}
	private void createEnemy2() {

		BodyDef bdef = new BodyDef();
		bdef.position.set(500/PPM, 720/PPM);
		bdef.type = BodyType.DynamicBody;
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(48 /PPM, 48/PPM);
		
		
			
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_ENNEMY;
		fdef.filter.maskBits = B2DVars.BIT_GROUND | B2DVars.BIT_PLAYER | B2DVars.BIT_ENNEMY ;
		
		Body body = world.createBody(bdef);	
		body.createFixture(fdef).setUserData("Enemy");
		Enemy enemy2 = new Enemy(body);
		body.setUserData(enemy2);
		enemiesAlive.add(enemy2);
		shape.dispose();

	}
	
	private void createPlatforms() {
		tileMap = new TmxMapLoader().load
				(System.getProperty("user.dir") + "\\map2.tmx");
		tmr = new OrthogonalTiledMapRenderer(tileMap);
				
		TiledMapTileLayer layer;
		layer= (TiledMapTileLayer) tileMap.getLayers().get("platform");
		createPlatformBox(layer, B2DVars.BIT_GROUND);						
	}
	
	private void createPlatformBox(TiledMapTileLayer layer, short bits) {
		float tileW = layer.getTileWidth();
		float tileH = layer.getTileHeight();
	
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();

		// Itera pour les cellules
		for (int row = 0; row < tileW +1 ; row++) {
			for (int col= 0; col < tileH +1; col++) {
				Cell cell = layer.getCell(col, row);
				
				if(cell == null) continue;
				if(cell.getTile() == null) continue;
				
				//Create body from cell
				bdef.type = BodyType.StaticBody;
				bdef.position.set((col + 0.5f) * tileW / PPM, (row + 0.5f) * tileH/ PPM);
				
				ChainShape cs = new ChainShape();
				Vector2[] v = new Vector2[3];
				v[0] = new Vector2(-tileH/2/PPM, -tileW/2/PPM);
				v[1] = new Vector2(-tileH/2/PPM, tileW/2/PPM);
				v[2] = new Vector2(tileH/2/PPM, tileW/2/PPM);
				cs.createChain(v);
				fdef.friction = 0;
				fdef.shape = cs;
				fdef.filter.categoryBits = bits;
				fdef.filter.maskBits = B2DVars.BIT_PLAYER | B2DVars.BIT_ENNEMY;
				fdef.isSensor = false;
				world.createBody(bdef).createFixture(fdef).setUserData("Platform");
			}
		}
	}
	
	private void removeEnemy(Array<Enemy> enemiesAlive, World world ) {
		for (int i = 0; i < enemiesAlive.size ; i++) {
			if (enemiesAlive.get(i).HEALTH == 0) {
				Body b = enemiesAlive.get(i).getBody();
				enemiesAlive.removeValue((Enemy) b.getUserData(), true);
				System.out.println("Enemy is dead");
				world.destroyBody(b);
			}
		}
	}
}


