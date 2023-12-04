import java.text.SimpleDateFormat;
import java.util.HashMap;

public class Helper {

    // Create a date time formatter
    static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public static void ClientLog(String log) {
        // Prompt user to input connection information
        System.out.println("<Client log at " + formatter.format(System.currentTimeMillis()) + ">: " + log);
    }

    public static void ServerLog(String log) {
        // Prompt user to input connection information
        System.out.println("<Server log at " + formatter.format(System.currentTimeMillis()) + ">: " + log);
    }

    public static String decodeData(byte[] a)
    {
        if (a == null)
            return null;
        StringBuilder ret = new StringBuilder();
        int i = 0;
        while (a[i] != 0)
        {
            ret.append((char) a[i]);
            i++;
        }
        return ret.toString();
    }

    public static String handleClientInput(String clientInput, HashMap<Integer, Integer> keyValueStore, String client) {
        // Split the request ID from the request part
        String[] requestParts = clientInput.split("#");
        // Split the operation from the key and value
        String[] inputParts = requestParts[1].split(" ");
        // Use switch case to handle different
        try {
            switch (inputParts[0]) {
                case "PUT":
                    // PUT needs key and value
                    if (inputParts.length >= 3) {
                        Helper.ServerLog("Received request from " + client + " for PUT operation with key = " + inputParts[1] + " and value = " + inputParts[2] + ".");
                        // Store the key value in the store
                        keyValueStore.put(Integer.parseInt(inputParts[1]), Integer.parseInt(inputParts[2]));
                        String response = "Succeeded";
                        Helper.ServerLog(response);
                        return requestParts[0] + "#" + response;
                        // Not having key or value
                    } else {
                        String response = "Failed: missing key or value from input";
                        Helper.ServerLog(response);
                        return requestParts[0] + "#" + response;
                    }
                case "GET":
                    // GET needs key
                    if (inputParts.length >= 2) {
                        Helper.ServerLog("Received request from " + client + " for GET operation with key = " + inputParts[1] + ".");
                        // Get the value from the store
                        Integer value = keyValueStore.get(Integer.parseInt(inputParts[1]));
                        if (value == null) {
                            String response = "Failed: key does not exist in map";
                            Helper.ServerLog(response);
                            return requestParts[0] + "#" + response;
                        }
                        String response = "Succeeded: value is " + value;
                        Helper.ServerLog(response);
                        return requestParts[0] + "#" + response;
                        // Not having key
                    } else {
                        String response = "Failed: missing key from input";
                        Helper.ServerLog(response);
                        return requestParts[0] + "#" + response;
                    }
                case "DELETE":
                    // DELETE needs key
                    if (inputParts.length >= 2) {
                        Helper.ServerLog("Received request from " + client + " for DELETE operation with key = " + inputParts[1] + ".");
                        // If the key is in the store, remove the key
                        if (keyValueStore.containsKey(Integer.parseInt(inputParts[1]))) {
                            keyValueStore.remove(Integer.parseInt(inputParts[1]));
                            String response = "Succeeded";
                            Helper.ServerLog(response);
                            return requestParts[0] + "#" + response;
                            // Otherwise log error
                        } else {
                            String response = "Failed: key does not exist in map";
                            Helper.ServerLog(response);
                            return requestParts[0] + "#" + response;
                        }
                    } else {
                        String response = "Failed: missing key from input";
                        Helper.ServerLog(response);
                        return requestParts[0] + "#" + response;
                    }
                    // When the operation name is wrong
                default:
                    String response = "Failed: invalid operation name";
                    Helper.ServerLog(response);
                    return requestParts[0] + "#" + response;
            }
            // When parseInt throws error
        } catch (NumberFormatException e) {
            String response = "Failed: illegal key or value, needed integer";
            Helper.ServerLog(response);
            return requestParts[0] + "#" + response;
            // When put operation throws error
        } catch (Exception e) {
            String response = "Failed: hash map error";
            Helper.ServerLog(response);
            return requestParts[0] + "#" + response;
        }
    }

}
