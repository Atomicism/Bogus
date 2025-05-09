package bogus.graphics.shading;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bogus.math.geom.Vec3;

/**
 * Manages shader instances and applies them in sequence
 */
public class ShaderManager {
    private static ShaderManager instance;
    
    private Map<String, Shader> shaders = new HashMap<>();
    private List<Shader> activeShaders = new ArrayList<>();

    public int getActiveShaderCount(){
        return activeShaders.size();
    }
    
    // Private constructor for singleton
    private ShaderManager() {
    }
    
    /**
     * Get the singleton instance
     */
    public static ShaderManager getInstance() {
        if (instance == null) {
            instance = new ShaderManager();
        }
        return instance;
    }
    
    /**
     * Register a shader
     * 
     * @param shader The shader to register
     * @return This manager for chaining
     */
    public ShaderManager registerShader(Shader shader) {
        shaders.put(shader.getName(), shader);
        return this;
    }
    
    /**
     * Get a registered shader by name
     * 
     * @param name The shader name
     * @return The shader or null if not found
     */
    public Shader getShader(String name) {
        return shaders.get(name);
    }
    
    /**
     * Add a shader to the active shader stack
     * 
     * @param shader The shader to activate
     * @return This manager for chaining
     */
    public ShaderManager addActiveShader(Shader shader) {
        if (!activeShaders.contains(shader)) {
            activeShaders.add(shader);
        }
        return this;
    }
    
    /**
     * Remove a shader from the active shader stack
     * 
     * @param shader The shader to deactivate
     * @return This manager for chaining
     */
    public ShaderManager removeActiveShader(Shader shader) {
        activeShaders.remove(shader);
        return this;
    }
    
    /**
     * Clear all active shaders
     * 
     * @return This manager for chaining
     */
    public ShaderManager clearActiveShaders() {
        activeShaders.clear();
        return this;
    }
    
    /**
     * Apply all active shaders to the given fragment
     * 
     * @param position World position
     * @param normal Surface normal
     * @param viewDir View direction
     * @param baseColor Base color at this position
     * @param material Material properties
     * @return The final shaded color
     */
    public Color applyShaders(Vec3 position, Vec3 normal, Vec3 viewDir, Color baseColor, Material material) {
        Color currentColor = baseColor;
        
        // Apply each active shader in sequence
        for (Shader shader : activeShaders) {
            if (shader.isEnabled()) {
                currentColor = shader.shade(position, normal, viewDir, currentColor, material);
            }
        }
        
        return currentColor;
    }


}