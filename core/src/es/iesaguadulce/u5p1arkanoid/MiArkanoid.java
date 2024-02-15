package es.iesaguadulce.u5p1arkanoid;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.ArrayList;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;

public class MiArkanoid extends ApplicationAdapter {
	private ShapeRenderer shape;
	private Ball ball;
	private Paddle paddle;
	private ArrayList<Block> blocks = new ArrayList<>();
	private GameOverScene gameOverScene;
	private SpriteBatch batch;
	private BitmapFont font;
	private boolean reColor;
	private Sound bounce;
	private Sound breaking;
	private Sound win;
	private Sound lose;
	private Music bgm;
	private String message;

	@Override
	public void create() {
		shape = new ShapeRenderer();
		batch = new SpriteBatch();
		font = new BitmapFont();
		gameOverScene = new GameOverScene();
		bounce = Gdx.audio.newSound(Gdx.files.internal("Arkanoid1.wav"));
		breaking = Gdx.audio.newSound(Gdx.files.internal("Arkanoid2.wav"));
		win = Gdx.audio.newSound(Gdx.files.internal("Arkanoid3.wav"));
		lose = Gdx.audio.newSound(Gdx.files.internal("Arkanoid4.wav"));
		bgm = Gdx.audio.newMusic(Gdx.files.internal("Arkanoid.mp3"));
		bgm.setLooping(true);
		newGame();
	}

	private void newGame(){
		bgm.play();
		message = "";
		ball = new Ball(150, 150, 50, 15, 15);
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
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		shape.begin(ShapeRenderer.ShapeType.Filled);
		if (blocks.isEmpty() || ball.gameOver(paddle)){
			gameOver();
			return;
		}

		reColor = ball.reColor();
		if (reColor) bounce.setVolume(bounce.play(), 0.5f);

		blockHandle(reColor);
		paddleHandle(reColor);
		ballHandle(reColor);

		shape.end();
	}

	private void gameOver(){
		if(message.isEmpty()){
			if (blocks.isEmpty()){
				message = "YOU WON";
				win.play();
			}
			if (ball.gameOver(paddle)) {
				message = "GAME OVER";
				lose.play();
			}
			bgm.stop();
		}
		gameOverScene.render(shape, batch, font, message);
		if (Gdx.input.justTouched()) {
			if (Gdx.input.isTouched()) {
				newGame();
			}
		}
		shape.end();
	}

	private void blockHandle(boolean reColor){
		for(Block block : blocks){
			ball.checkCollision(block);
			if (reColor) block.setColor();
			block.draw(shape);
		}
		for(Block block : blocks){
			if (block.getDestroyed()){
				blocks.remove(block);
				breaking.setVolume(breaking.play(), 0.5f);
				break;
			}
		}
	}

	private void ballHandle(boolean reColor){
		if(Gdx.input.isTouched())
			paddle.newPos();
		paddle.update();
		if (reColor) paddle.setColor();
		paddle.draw(shape);
	}

	private void paddleHandle(boolean reColor){
		ball.update();
		if (reColor) ball.setColor();
		ball.draw(shape);
		ball.checkCollision(paddle);
	}

	@Override
	public void dispose() {
		super.dispose();
		font.dispose();
		batch.dispose();
		shape.dispose();
		bgm.dispose();
		lose.dispose();
		win.dispose();
		breaking.dispose();
		bounce.dispose();
	}
}