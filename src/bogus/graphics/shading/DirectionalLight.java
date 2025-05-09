package bogus.graphics.shading;

import java.awt.*;

import bogus.math.geom.Vec3;

/**
 * A directional light that emits parallel rays in a specific direction
 */
public class DirectionalLight extends Light {
    private Vec3 direction;
    
    public DirectionalLight(Vec3 direction, Color color, float intensity) {
        super(color, intensity);
        this.direction = direction.normalize();
    }
    
    public Vec3 getDirection() { return direction; }
    public void setDirection(Vec3 direction) { this.direction = direction.normalize(); }
    
    @Override
    public Color calculateLightingAt(Vec3 position, Vec3 normal, Vec3 viewDir, Material material) {
        // Light direction is constant for directional lights
        Vec3 lightDir = direction.multiply(-1); // Invert the direction to point toward the light
        
        // Directional lights have no attenuation
        float attenuation = 1.0f;
        
        return calculatePhongLighting(position, normal, viewDir, lightDir, material, attenuation);
    }
}
