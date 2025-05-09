package bogus.graphics.shading;

import java.awt.Color;

import bogus.math.geom.Vec3;

/**
 * A point light that emits light in all directions from a specific point
 */
public class PointLight extends Light {
    private Vec3 position;
    private float range = 1000.0f;  // The maximum range of the light
    private float falloffLinear = 0.09f;
    private float falloffQuadratic = 0.032f;
    
    public PointLight(Vec3 position, Color color, float intensity) {
        super(color, intensity);
        this.position = position;
    }
    
    public PointLight(Vec3 position, Color color, float intensity, float range) {
        super(color, intensity);
        this.position = position;
        this.range = range;
    }
    
    public Vec3 getPosition() { return position; }
    public void setPosition(Vec3 position) { this.position = position; }
    
    public float getRange() { return range; }
    public void setRange(float range) { this.range = Math.max(0, range); }
    
    public float getFalloffLinear() { return falloffLinear; }
    public void setFalloffLinear(float falloff) { this.falloffLinear = Math.max(0, falloff); }
    
    public float getFalloffQuadratic() { return falloffQuadratic; }
    public void setFalloffQuadratic(float falloff) { this.falloffQuadratic = Math.max(0, falloff); }
    
    @Override
    public Color calculateLightingAt(Vec3 position, Vec3 normal, Vec3 viewDir, Material material) {
        // Calculate direction from the surface point to the light
        Vec3 toLight = this.position.subtract(position);
        float distance = toLight.length();
        
        // If beyond range, no light contribution
        if (distance > range) {
            return new Color(0, 0, 0);
        }
        
        // Normalize direction
        Vec3 lightDir = toLight.multiply(1.0f / distance);
        
        // Calculate attenuation (light falloff with distance)
        float attenuation = 1.0f / (1.0f + falloffLinear * distance + falloffQuadratic * distance * distance);
        
        return calculatePhongLighting(position, normal, viewDir, lightDir, material, attenuation);
    }
}
