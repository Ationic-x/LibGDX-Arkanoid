package es.iesaguadulce.u5p1arkanoid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.Random;

public class Ball extends GameObject {
    private int xSpeed;
    private int ySpeed;
    private int oldXSpeed;
    private int oldYSpeed;
    private int life;

    public Ball(int x, int y, int size, int xSpeed, int ySpeed) {
        super(x, y, size, size);
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.oldXSpeed = xSpeed;
        this.oldYSpeed = ySpeed;
        this.life = 3;
        setColor();
    }

    public void update() {
        x += xSpeed;
        y += ySpeed;
        if (x < 0)
            xSpeed = Math.abs(xSpeed);
        else if(x > Gdx.graphics.getWidth())
            xSpeed = -Math.abs(xSpeed);

        if (y > Gdx.graphics.getHeight())
            ySpeed = -Math.abs(ySpeed);
    }

    public void draw(ShapeRenderer shape) {
        shape.setColor(color);
        shape.circle(x, y, height);
    }

    public void checkCollision(Paddle paddle) {
        if (collidesWith(paddle)) {
            if (y - height / 1.3 <= paddle.y + ySpeed)
                xSpeed *= -1;
            ySpeed = Math.abs(ySpeed);
        }
    }

    public void checkCollision(Block block) {
        if (collidesWith(block)) {
            if (y + height / 1.3 >= block.y)
                xSpeed *= -1;
            if (y + height >= block.y + block.height)
                ySpeed = Math.abs(ySpeed);
            else
                ySpeed = -Math.abs(ySpeed);
            block.destroy();
        }
    }

    private boolean collidesWith(GameObject gameObject) {
        int blockMinX = gameObject.x;
        int blockMaxX = gameObject.x + gameObject.width;
        int blockMinY = gameObject.y;
        int blockMaxY = gameObject.y + gameObject.height;
        int ballMinX = x - height;
        int ballMaxX = x + height;
        int ballMinY = y - height;
        int ballMaxY = y + height;
        return (blockMinX < ballMaxX && blockMaxX > ballMinX) && (blockMinY < ballMaxY && blockMaxY > ballMinY);
    }

    public boolean reColor(){
        if (((xSpeed > 0 && oldXSpeed < 0 || ySpeed > 0 && oldYSpeed < 0) || (xSpeed < 0 && oldXSpeed > 0 || ySpeed < 0 && oldYSpeed > 0))){
            oldXSpeed = xSpeed;
            oldYSpeed = ySpeed;
            return true;
        }
        return false;
    }

    public boolean gameOver(Paddle paddle){
        int paddleMinY = paddle.y;
        int ballMaxY = y + height;
        if (ballMaxY < paddleMinY){
            life --;
            return true;
        }
        return false;
    }

    public int getLife(){
        return life;
    }

    public void setLife(int life){
        this.life = life;
    }

    public int getxSpeed() {
        return xSpeed;
    }

    public void setxSpeed(int xSpeed) {
        this.xSpeed = xSpeed;
    }

    public int getySpeed() {
        return ySpeed;
    }

    public void setySpeed(int ySpeed) {
        this.ySpeed = ySpeed;
    }
}