package es.iesaguadulce.u5p1arkanoid;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.ArrayList;
import java.util.Random;

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
    private String effect;
	private Sound bounce;
	private Sound breaking;
	private Sound win;
	private Sound lose;
    private Sound pipe;
	private Music bgm;
	private String message;
    private float time;
    private boolean start;
    private  boolean colorful;
    private Random rand;

    private boolean clean;

	@Override
	public void create() {
		shape = new ShapeRenderer();
		batch = new SpriteBatch();
		font = new BitmapFont();
        rand = new Random();

        gameOverScene = new GameOverScene();
		bounce = Gdx.audio.newSound(Gdx.files.internal("Arkanoid1.wav"));
		breaking = Gdx.audio.newSound(Gdx.files.internal("Arkanoid2.wav"));
		win = Gdx.audio.newSound(Gdx.files.internal("Arkanoid3.wav"));
		lose = Gdx.audio.newSound(Gdx.files.internal("Arkanoid4.wav"));
        pipe = Gdx.audio.newSound(Gdx.files.internal("Arkanoid5.mp3"));
		bgm = Gdx.audio.newMusic(Gdx.files.internal("Arkanoid.mp3"));
		bgm.setLooping(true);
		newGame();
	}

	private void newGame(){
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		bgm.play();
		message = "";
        effect = "";
        colorful = false;
        clean = true;
        start = false;
		ball = new Ball(Gdx.graphics.getWidth() / 2, 200, 50, 15, 15);
		paddle = new Paddle(Gdx.graphics.getWidth() / 2 - 100, 100, 200, 20, 65);
		int blockWidth = 212;
		int blockHeight = 45;
		for (int y = Gdx.graphics.getHeight() / 2; y < Gdx.graphics.getHeight(); y += blockHeight + 10) {
			for (int x = 0; x < Gdx.graphics.getWidth(); x += blockWidth + 10) {
				blocks.add(new Block(x, y, blockWidth, blockHeight));
			}
		}
        time = 1;
	}

	@Override
	public void render() {
        if(start) {
            if(clean)
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            shape.begin(ShapeRenderer.ShapeType.Filled);

            if (blocks.isEmpty() || ball.gameOver(paddle)) {
                Gdx.gl.glClearColor(0,0,0,0);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                if (ball.getLife() > 0 && !blocks.isEmpty()) {
                    ball.setX(Gdx.graphics.getWidth() / 2);
                    ball.setY(200);
                    ball.setHeight(50);
                    ball.setxSpeed(15);
                    ball.setySpeed(15);
                    paddle.setX(Gdx.graphics.getWidth() / 2 - 100);
                    paddle.setTempX(Gdx.graphics.getWidth() / 2 - 100);
                    paddle.setWidth(200);
                    paddle.setSpeed(65);
                    paddle.setInverse(false);
                    clean = true;
                    colorful = false;
                } else {
                    gameOver();
                    return;
                }
            }

            time += Gdx.graphics.getDeltaTime();

            reColor = ball.reColor();
            if (reColor) {
                if (colorful) Gdx.gl.glClearColor(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 1);
                else Gdx.gl.glClearColor(0,0,0,0);
                bounce.setVolume(bounce.play(), 0.5f);
            }

            if (time % 5 < 1) {
                randomEffect();
                time = 1;
            }
            batch.setProjectionMatrix(shape.getProjectionMatrix());
            batch.begin();
            font.setColor(ball.getColor());
            font.getData().setScale(5);
            font.draw(batch, effect, Gdx.graphics.getWidth() / 2 - effect.getBytes().length * 17, Gdx.graphics.getHeight() / 2.5f);
            batch.end();

            blockHandle(reColor);
            paddleHandle(reColor);
            ballHandle(reColor);

            shape.end();
        } else {
            batch.begin();
            font.setColor(Color.WHITE);
            font.getData().setScale(4);
            String initial = time == 1 ? "Touch the screen to begin" : "Touch the screen to continue";
            font.draw(batch, initial, Gdx.graphics.getWidth() / 2 - initial.getBytes().length * 12, Gdx.graphics.getHeight() / 3f);
            batch.end();
            if (Gdx.input.isTouched())
                start = true;
        }
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
			newGame();
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

    private void randomEffect(){
        int value = rand.nextInt(41);
        switch (value){
            case 0:
            case 1:
            case 2:
                effect = "Shorter paddle";
                paddle.setWidth(paddle.getWidth() / 2);
                break;
            case 3:
            case 4:
            case 5:
                effect = "Bigger paddle";
                paddle.setWidth(paddle.getWidth() * 2);
                break;
            case 6:
            case 7:
            case 8:
                effect = "Shorter ball";
                ball.setHeight(ball.getHeight() / 2);
                break;
            case 9:
            case 10:
            case 11:
                effect = "Bigger ball";
                ball.setHeight((int) (ball.getHeight() * 1.5));
                break;
            case 12:
                effect = "New row";
                for (int x = 0; x < Gdx.graphics.getWidth(); x += 212 + 10) {
                    blocks.add(new Block(x, Gdx.graphics.getHeight() / 2, 212, 45));
                }
                break;
            case 13:
            case 14:
                effect = "More horizontal speed";
                ball.setxSpeed((int) (ball.getxSpeed() * 1.5f));
                break;
            case 15:
            case 16:
                effect = "More vertical speed";
                ball.setySpeed((int) (ball.getySpeed() * 1.5f));
                break;
            case 17:
            case 18:
                effect = "Less horizontal speed";
                ball.setxSpeed((int) (ball.getxSpeed() * 0.9f));
                break;
            case 19:
            case 20:
                effect = "Less vertical speed";
                ball.setySpeed((int) (ball.getySpeed() * 0.9f));
                break;
            case 21:
            case 22:
                effect = "Inverse vertical movement";
                ball.setySpeed(ball.getySpeed() * -1);
                break;
            case 23:
            case 24:
                effect = "Inverse horizontal movement";
                ball.setySpeed(ball.getxSpeed() * -1);
                break;
            case 25:
                effect = "Change controls";
                paddle.setInverse(!paddle.getInverse());
                break;
            case 26:
                effect = "Extra life";
                ball.setLife(ball.getLife() + 1);
                break;
            case 27:
                effect = "Metal pipe";
                pipe.play();
                break;
            case 28:
                colorful = !colorful;
                if(colorful)
                    effect = "Background colorful";
                else
                    effect = "Background lame";
                break;
            case 29:
            case 30:
            case 31:
                clean = !clean;
                if(clean)
                    effect = "Clean screen";
                else
                    effect = "Dirty screen";
                break;
            case 32:
            case 33:
                effect = "Decrease paddle speed";
                paddle.setSpeed((int) (paddle.getSpeed() * 0.9));
                break;
            case 34:
            case 35:
                effect = "Increase paddle speed";
                paddle.setSpeed((int) (paddle.getSpeed() * 1.5));
                break;
            default:
                effect = "Nothing :)";
                break;
        }
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
        pipe.dispose();
	}
}