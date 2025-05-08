package bogus.assets;

import java.util.List;

import bogus.core.*;

public class AssetLoader extends Loader {

    public Asset[] assets;

    public String location = "assets/";

    @Override
    public void load(){
        logger.log("Loading assets");

        for(Asset asset : assets){
            asset.load();
        }
    }

    public AssetLoader(){};

    public AssetLoader(Asset... assets){
        this.assets = assets;
    }

    public AssetLoader(List<Asset> assets){
        this.assets = assets.toArray(new Asset[0]);
    }
}