package hyperskill.developer.fileserver.common;

public record FileEntry(int id, String name) {

    @Override
    public String toString() {
        return id + ":" + name;
    }

    public static FileEntry fromString(String line) {
        int sep = line.indexOf(':');
        if (sep == -1) throw new IllegalArgumentException("Invalid entry: " + line);
        int id = Integer.parseInt(line.substring(0, sep));
        String name = line.substring(sep + 1);
        return new FileEntry(id, name);
    }
}
