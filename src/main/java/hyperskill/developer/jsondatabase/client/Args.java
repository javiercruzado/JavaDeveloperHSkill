package hyperskill.developer.jsondatabase.client;

import com.beust.jcommander.Parameter;

@SuppressWarnings("CanBeFinal")
class Args {
    @Parameter(names = {"-t"}, description = "specifies the type of request (get, set, or delete).")
    String type = "get";

    @SuppressWarnings("unused")
    @Parameter(names = {"-k"}, description = "specifies the key.")
    String key;

    @SuppressWarnings("unused")
    @Parameter(names = {"-v"}, description = "specifies the value (only needed for set requests).")
    String value;

    @Parameter(names = {"-in"}, description = "specifies the value (only needed for set requests).")
    String fileName = "";
}
