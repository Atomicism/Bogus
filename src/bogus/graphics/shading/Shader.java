package bogus.graphics.shading;

import bogus.math.geom.Vec3;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract base class for all shaders
 */
public abstract class Shader {
    // Uniform variables common to most shaders
    protected Map<String, Object> uniforms = new HashMap<>();
    
    // Shader attributes
    protected String name;
    protected boolean enabled = true;
    
    public Shader(String name) {
        this.name = name;
    }
    
    /**
     * Set a uniform value
     * 
     * @param name Uniform name
     * @param value Uniform value
     * @return This shader for chaining
     */
    public Shader setUniform(String name, Object value) {
        uniforms.put(name, value);
        return this;
    }
    
    /**
     * Get a uniform value
     * 
     * @param name Uniform name
     * @param defaultValue Default value if uniform doesn't exist
     * @return The uniform value or defaultValue if not found
     */
    @SuppressWarnings("unchecked")
    public <T> T getUniform(String name, T defaultValue) {
        Object value = uniforms.get(name);
        return value != null ? (T)value : defaultValue;
    }
    
    /**
     * Apply the shader to a specific fragment (pixel)
     * 
     * @param position World position
     * @param normal Surface normal
     * @param viewDir View direction
     * @param baseColor Base color at this position
     * @param material Material properties
     * @return The shaded color
     */
    public abstract Color shade(Vec3 position, Vec3 normal, Vec3 viewDir, Color baseColor, Material material);
    
    /**
     * Enable or disable this shader
     * @return 
     */
    public Shader setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }
    
    /**
     * Check if shader is enabled
     */
    public boolean isEnabled() {
        return enabled;
    }
    
    /**
     * Get shader name
     */
    public String getName() {
        return name;
    }
}