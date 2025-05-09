package bogus.graphics.shading;

import java.awt.Color;

import bogus.math.geom.Vec3;

/**
 * A spotlight that emits a cone of light in a specific direction
 */
public class SpotLight extends Light {
    private Vec3 position;
    private Vec3 direction;
    private float range = 1000.0f;
    private float innerConeAngle = 15.0f;  // In degrees
    private float outerConeAngle = 30.0f;  // In degrees
    private float falloffLinear = 0.09f;
    private float falloffQuadratic = 0.032f;
    
    public SpotLight(Vec3 position, Vec3 direction, Color color, float intensity) {
        super(color, intensity);
        this.position = position;
        this.direction = direction.normalize();
    }
    
    public Vec3 getPosition() { return position; }
    public void setPosition(Vec3 position) { this.position = position; }
    
    public Vec3 getDirection() { return direction; }
    public void setDirection(Vec3 direction) { this.direction = direction.normalize(); }
    
    public float getRange() { return range; }
    public void setRange(float range) { this.range = Math.max(0, range); }
    
    public float getInnerConeAngle() { return innerConeAngle; }
    public void setInnerConeAngle(float angle) { this.innerConeAngle = Math.max(0, Math.min(outerConeAngle, angle)); }
    
    public float getOuterConeAngle() { return outerConeAngle; }
    public void setOuterConeAngle(float angle) { 
        this.outerConeAngle = Math.max(innerConeAngle, angle);
    }
    
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
        
        // Calculate spot effect
        float cosAngle = -lightDir.dot(direction); // Negative because lightDir points toward the light
        
        // Convert cone angles to cosine space for easy comparison
        float cosInner = (float)Math.cos(Math.toRadians(innerConeAngle));
        float cosOuter = (float)Math.cos(Math.toRadians(outerConeAngle));
        
        // Check if the point is outside the spotlight cone
        if (cosAngle < cosOuter) {
            return new Color(0, 0, 0);
        }
        
        // Calculate spotlight intensity factor (smooth transition between inner and outer cones)
        float spotFactor = 1.0f;
        if (cosAngle < cosInner) {
            spotFactor = (cosAngle - cosOuter) / (cosInner - cosOuter);
        }
        
        // Calculate attenuation (light falloff with distance)
        float attenuation = 1.0f / (1.0f + falloffLinear * distance + falloffQuadratic * distance * distance);
        
        // Apply spotlight factor to attenuation
        attenuation *= spotFactor;
        
        return calculatePhongLighting(position, normal, viewDir, lightDir, material, attenuation);
    }
}