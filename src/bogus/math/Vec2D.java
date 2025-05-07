package bogus.math;

import static java.lang.Math.*;

public class Vec2D {
    public float x, y;

    public double magnitude() {
        return Math.sqrt(x * x + y * y);
    }

    public void normalize() {
        double mag = magnitude();
        if (mag > 0) {
            x /= mag;
            y /= mag;
        }
    }

    public double distance(Vec2D v2){
        return abs(x - v2.x + y - v2.y);
    }

    public Vec2D(){
        x = 0f;
        y = 0f;
    }

    public Vec2D(float x, float y){
        this.x = x;
        this.y = y;
    }

    public String toString(){
        return String.format("(%s, %s)", x, y);
    }
}
