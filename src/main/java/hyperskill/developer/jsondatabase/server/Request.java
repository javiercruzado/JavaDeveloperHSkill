package hyperskill.developer.jsondatabase.server;

import java.util.Objects;

@SuppressWarnings({"ClassCanBeRecord", "unused"})
public final class Request {
    private final String type;
    private final String key;
    private final String value;

    public Request(String type, String key, String value) {
        this.type = type;
        this.key = key;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Request) obj;
        return Objects.equals(this.type, that.type) &&
                Objects.equals(this.key, that.key) &&
                Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, key, value);
    }

    @Override
    public String toString() {
        return "Request[" +
                "type=" + type + ", " +
                "key=" + key + ", " +
                "value=" + value + ']';
    }

}
