package bogus.core;

import java.util.Arrays;

import bogus.assets.AssetLoader;
import bogus.assets.AudioAsset;
import bogus.assets.ImageAsset;
import bogus.mod.ModLoader;
import bogus.mod.ScriptModLoader;
import bogus.util.serialization.Base64Coder;

public class TestMain {
    public static void main(String[] args) {
        var modLoader = new ModLoader();
        var scriptModLoader = new ScriptModLoader();
        modLoader.load();
        scriptModLoader.load();

        SaveData test = new SaveData();
        test.overwrite(Base64Coder.encodeString("Hello!"));
        System.out.println(test);

        AssetLoader assetLoader = new AssetLoader(
            new ImageAsset("test-image", "test.png"),
            new AudioAsset("test-mp3", "test.wav")
        );
        assetLoader.load();

        ((AudioAsset)assetLoader.assets[1]).play();
        
        try {
            Thread.sleep(3000);
        } catch (Exception e){

        }
    }
}
