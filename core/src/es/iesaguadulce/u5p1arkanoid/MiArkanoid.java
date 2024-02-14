package es.iesaguadulce.u5p1arkanoid;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.ArrayList;
import java.util.Random;

public class MiArkanoid extends ApplicationAdapter {
	ShapeRenderer shape;
	Ball ball;
	Paddle paddle;
	ArrayList<Block> blocks = new ArrayList<>();
	GameOverScene gameOverScene;
	SpriteBatch batch;
	BitmapFont font;

	@Override
	public void create() {
		shape = new ShapeRenderer();
		newGame();
		batch = new SpriteBatch();
		font = new BitmapFont();
		gameOverScene = new GameOverScene();
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		shape.begin(ShapeRenderer.ShapeType.Filled);
		if (blocks.isEmpty() || ball.gameOver(paddle)){
			String message = "";
			if (blocks.isEmpty()) message = "YOU WON";
			if (ball.gameOver(paddle)) message = "GAME OVER";
			gameOverScene.render(shape, batch, font, message);
			if (Gdx.input.justTouched()) {
				if (Gdx.input.isTouched()) {
					newGame();
				}
			}
			shape.end();
			return;
		}

		ball.update();
		ball.draw(shape);
		if(Gdx.input.isTouched())
			paddle.newPos();
		paddle.update();
		paddle.draw(shape);
		for(Block block : blocks){
			ball.checkCollision(block);
			block.draw(shape);
		}
		for(Block block : blocks){
			if (block.destroyed){
				blocks.remove(block);
				break;
			}
		}

		ball.checkCollision(paddle);
		shape.end();
	}

	private void newGame(){
		ball = new Ball(150, 150, 50, 0, 0);
		paddle = new Paddle(50, 100, 200, 20, 65);
		int blockWidth = 212;
		int blockHeight = 45;
		for (int y = Gdx.graphics.getHeight() / 2; y < Gdx.graphics.getHeight(); y += blockHeight + 10) {
			for (int x = 0; x < Gdx.graphics.getWidth(); x += blockWidth + 10) {
				blocks.add(new Block(x, y, blockWidth, blockHeight));
			}
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		font.dispose();
		batch.dispose();
		shape.dispose();
	}


}