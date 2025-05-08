package bogus.core;

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
    }
}
