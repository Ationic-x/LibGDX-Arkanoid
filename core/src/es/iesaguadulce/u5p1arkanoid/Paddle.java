package es.iesaguadulce.u5p1arkanoid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Paddle extends GameObject {
    private int speed;
    private int tempX;
    private int distance;

    private boolean inverse;

    public Paddle(int x, int y, int width, int height, int speed) {
        super(x, y, width, height);
        this.tempX = x;
        this.speed = speed;
        this.inverse = false;
    }

    @Override
    public void update() {
        if (tempX != x) {
            if (0 < Math.abs(distance) / speed) {
                if (distance < 0)
                    if (tempX > x)
                        x += speed;
                    else
                        x = tempX;
                else
                if (tempX < x)
                    x -= speed;
                else
                    x = tempX;
            } else
                x = tempX;
        }
    }

    public void setTempX(int tempX){
        this.tempX = tempX;
    }

    public void newPos() {
        if(inverse)
            tempX = Gdx.graphics.getWidth() - Gdx.input.getX() - width / 2;
        else
            tempX = Gdx.input.getX() - width / 2;
        if (tempX < 0) {
            tempX = 0;
        } else if (tempX + width > Gdx.graphics.getWidth()) {
            tempX = Gdx.graphics.getWidth() - width;
        }
        distance = x - tempX;
    }

    public void setInverse(boolean inverse){
        this.inverse = inverse;
    }

    public boolean getInverse(){
        return  inverse;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
