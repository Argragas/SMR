package util;

public final class WebContext {

    public static final String HOST = "0.0.0.0";

    public static final int PORT = 8080;

    public static final String CONTEXT_ROOT = "example";

    public static final String BASE_URL = String.format("http://%s:%s", HOST, PORT);

    public static final String APP_BASE_URL = String.join("/", BASE_URL, CONTEXT_ROOT);

    public static final String API_BASE_URL = String.join("/", APP_BASE_URL, "api");
}
