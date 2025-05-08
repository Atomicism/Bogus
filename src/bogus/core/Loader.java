package bogus.core;

public abstract class Loader {

    // Silly shenanigans to get only the class name for loggers
    protected String loaderName = this.getClass().getName().split("\\.")[this.getClass().getName().split("\\.").length - 1];

    public abstract void load();
}
