package es.iesaguadulce.u5p1arkanoid;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.Gdx;

public class GameOverScene {
    public void render(ShapeRenderer shapeRenderer, SpriteBatch batch, BitmapFont font, String message) {
        batch.setProjectionMatrix(shapeRenderer.getProjectionMatrix());
        batch.begin();
        font.setColor(Color.WHITE);
        font.getData().setScale(5);
        font.draw(batch, message, Gdx.graphics.getWidth() / 2 - message.getBytes().length * 20, Gdx.graphics.getHeight() / 2 + 50);
        font.getData().setScale(2);
        font.draw(batch, "Pulsa la pantalla para reiniciar", Gdx.graphics.getWidth() / 2 - 160, Gdx.graphics.getHeight() / 2 - 30);
        batch.end();
    }
}