package es.iesaguadulce.u5p1arkanoid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Ball {
    int x;
    int y;
    int size;
    int xSpeed;
    int ySpeed;
    Color color = Color.WHITE;

    public Ball(int x, int y, int size, int xSpeed, int ySpeed) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    public void update() {
        x += xSpeed;
        y += ySpeed;
        if (x < 0 || x > Gdx.graphics.getWidth()) {
            xSpeed = -xSpeed;
        }

        if (y < 0 || y > Gdx.graphics.getHeight()) {
            ySpeed = -ySpeed;
        }
    }

    public void draw(ShapeRenderer shape) {
        shape.setColor(color);
        shape.circle(x, y, size);
    }

    public void checkCollision(Paddle paddle) {
        if (collidesWith(paddle)) {
            if ((y - size) <= (paddle.y))
                xSpeed = -Math.abs(xSpeed);
            ySpeed = Math.abs(ySpeed);
        }
    }

    public void checkCollision(Block block) {
        if (collidesWith(block)) {
            ySpeed = -Math.abs(ySpeed);
            block.destroy();
        }
    }

    private boolean collidesWith(Block block) {
        int blockMinX = block.x;
        int blockMaxX = block.x + block.width;
        int blockMinY = block.y;
        int blockMaxY = block.y + block.height;
        int ballMinX = x - size;
        int ballMaxX = x + size;
        int ballMinY = y - size;
        int ballMaxY = y + size;
        return (blockMinX < ballMaxX && blockMaxX > ballMinX) && (blockMinY < ballMaxY && blockMaxY > ballMinY);
    }

    private boolean collidesWith(Paddle paddle) {
        int paddleMinX = paddle.x;
        int paddleMaxX = paddle.x + paddle.width;
        int paddleMinY = paddle.y;
        int paddleMaxY = paddle.y + paddle.height;
        int ballMinX = x - size;
        int ballMaxX = x + size;
        int ballMinY = y - size;
        int ballMaxY = y + size;
        return (paddleMinX < ballMaxX && paddleMaxX > ballMinX) && (paddleMinY < ballMaxY && paddleMaxY > ballMinY);
    }

    public boolean gameOver(Paddle paddle){
        int paddleMinY = paddle.y;
        int ballMaxY = y + size;
        return (ballMaxY < paddleMinY);
    }
}