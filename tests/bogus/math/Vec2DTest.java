package tests.bogus.math;

import bogus.math.Vec2D;
import tests.Test;

public class Vec2DTest extends Test {

    public Vec2DTest(String name) {
        super(name);
    }

    @Override
    public void run() {
        Vec2D v1 = new Vec2D();
        Vec2D v2 = new Vec2D(4.5f, 1.1f);

        System.out.println(v1.distance(v2));
        v2.normalize();
        System.out.println(v2);
    }
    
}
