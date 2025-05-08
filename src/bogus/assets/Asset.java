package bogus.assets;

import bogus.util.Logger;

public abstract class Asset {
    protected String name;
    protected String location;
    protected Logger logger = new Logger(name);

    public Asset(String name, String location) {
        this.name = name;
        this.location = location;
        logger = new Logger(name);
    }

    public Asset() {}

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String toString(){
        return this.name + "@" + location;
    }

    public abstract void load();
}
