package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class flappybird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Circle birdcircle;
	Texture gameover;
	//ShapeRenderer shapeRenderer;

	Texture[] birds;
	int flapstate = 0;
	float birdy = 0;
	float velocity = 0;
	int gamestate = 0;
	Texture toptube;
	Texture bottomtube;
	float gravity = 2;
	float gap = 400;
	float maxoffset;
	Random generator;
	float tubevelocity = 4;
	int numberoftubes = 4;
	float[] tubeoffset = new float[numberoftubes];
	float[] tubex = new float[numberoftubes];
	float distancebetweentubes;
	int score;
	BitmapFont font;
	int scoretube=0;

	Rectangle[] toptuberectangle;
	Rectangle[] bottomtuberectange;




	@Override
	public void create() {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");
		//birdy = Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2;
		toptube = new Texture("toptube.png");
		bottomtube = new Texture("bottomtube.png");
		score=0;
		font= new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);
		//shapeRenderer = new ShapeRenderer();
		birdcircle = new Circle();
		maxoffset = Gdx.graphics.getHeight() / 2 - gap / 2 - 100;
		generator = new Random();
		//tubex=Gdx.graphics.getWidth()/2-toptube.getWidth()/2;
		distancebetweentubes = Gdx.graphics.getWidth() * 3 / 4;

		gameover=new Texture("images.jpg");
		toptuberectangle = new Rectangle[numberoftubes];
		bottomtuberectange = new Rectangle[numberoftubes];

		startgame();

	}

	public void startgame()
	{
		birdy=Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2;
		for (int i = 0; i < numberoftubes; i++) {
			tubeoffset[i] = (generator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
			tubex[i] = Gdx.graphics.getWidth() / 2 - toptube.getWidth() / 2 + Gdx.graphics.getWidth()+i * distancebetweentubes;
			toptuberectangle[i] = new Rectangle();
			bottomtuberectange[i] = new Rectangle();
		}
	}

	@Override
	public void render() {
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if (gamestate == 1) {
			if (Gdx.input.justTouched()) {
				velocity = -30;
			}
			if (tubex[scoretube] < Gdx.graphics.getWidth() / 2) {
				score++;
				if (scoretube < numberoftubes - 1) {
					scoretube++;

				} else {
					scoretube = 0;
				}
			}

			for (int i = 0; i < numberoftubes; i++) {
				if (tubex[i] < -toptube.getWidth()) {
					tubex[i] += numberoftubes * distancebetweentubes;
					tubeoffset[i] = (generator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
				} else {
					tubex[i] = tubex[i] - tubevelocity;
					/*if(tubex[i]<Gdx.graphics.getWidth())
					{
						score++;
					}*/
				}
				batch.draw(toptube, tubex[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeoffset[i]);
				batch.draw(bottomtube, tubex[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeoffset[i]);

				toptuberectangle[i] = new Rectangle(tubex[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeoffset[i], toptube.getWidth(), toptube.getHeight());
				bottomtuberectange[i] = new Rectangle(tubex[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeoffset[i], bottomtube.getWidth(), bottomtube.getHeight());
			}
			if (birdy > 0) {
				velocity = velocity + gravity;
				birdy -= velocity;
			} else {
				gamestate = 2;
			}
		}
		else if(gamestate==0)
		{
			if(Gdx.input.justTouched())
			{
				gamestate=1;
			}
		}
		else if(gamestate==2)
		{
			batch.draw(gameover,Gdx.graphics.getWidth()/2-gameover.getWidth()/2,Gdx.graphics.getHeight()/2-gameover.getHeight()/2);
			if(Gdx.input.justTouched())
			{
				gamestate=1;
				startgame();
				score=0;
				scoretube=0;
				velocity=0;
			}
		}
		if (flapstate == 0) {
			flapstate = 1;
		} else {
			flapstate = 0;
		}
		batch.draw(birds[flapstate], Gdx.graphics.getWidth() / 2 - birds[flapstate].getWidth() / 2, birdy);
		//batch.draw(birds[flapstate],Gdx.graphics.getWidth()/1-birds[flapstate].getWidth()/1,birdy);
		font.draw(batch,String.valueOf(score),100,100);
		batch.end();
		birdcircle.set(Gdx.graphics.getWidth() / 2, birdy + birds[flapstate].getHeight() / 2, birds[flapstate].getWidth() / 2);
		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.RED);
		//shapeRenderer.circle(birdcircle.x, birdcircle.y, birdcircle.radius);
		for (int i = 0; i < numberoftubes; i++) {
			//shapeRenderer.rect(tubex[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeoffset[i], toptube.getWidth(), toptube.getHeight());
			//shapeRenderer.rect(tubex[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeoffset[i], bottomtube.getWidth(), bottomtube.getHeight());

			if(Intersector.overlaps(birdcircle,toptuberectangle[i]) || Intersector.overlaps(birdcircle,bottomtuberectange[i]))
			{
				//Gdx.app.log("Collision","Yes!");
				gamestate=2;
			}

		}
			//shapeRenderer.end();
	}
}
	
	/*@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}*/
