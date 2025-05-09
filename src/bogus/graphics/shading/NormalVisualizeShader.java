package bogus.graphics.shading;

import java.awt.Color;

import bogus.math.geom.Vec3;

/**
 * Visualizes surface normals as RGB colors
 */
public class NormalVisualizeShader extends Shader {
    private boolean worldSpace = true; // If true, visualize world-space normals, otherwise camera-space
    
    public NormalVisualizeShader() {
        super("NormalVisualizeShader");
    }
    
    /**
     * Set whether to use world-space or camera-space normals
     * 
     * @param worldSpace True for world-space, false for camera-space
     * @return This shader for chaining
     */
    public NormalVisualizeShader setWorldSpace(boolean worldSpace) {
        this.worldSpace = worldSpace;
        return this;
    }
    
    @Override
    public Color shade(Vec3 position, Vec3 normal, Vec3 viewDir, Color baseColor, Material material) {
        // Normalize the normal
        Vec3 n = normal.normalize();
        
        // Map (-1, 1) range to (0, 1)
        int r = (int)((n.x * 0.5f + 0.5f) * 255);
        int g = (int)((n.y * 0.5f + 0.5f) * 255);
        int b = (int)((n.z * 0.5f + 0.5f) * 255);
        
        // Clamp values
        r = Math.min(255, Math.max(0, r));
        g = Math.min(255, Math.max(0, g));
        b = Math.min(255, Math.max(0, b));
        
        return new Color(r, g, b, baseColor.getAlpha());
    }
}