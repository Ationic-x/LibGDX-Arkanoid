package es.iesaguadulce.u5p1arkanoid;


public class Block extends GameObject {
    private boolean destroyed;

    public Block(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.destroyed = false;
    }

    @Override
    public void update() {

    }

    public void destroy() {
        destroyed = true;
    }

    public boolean getDestroyed(){
        return destroyed;
    }
}
