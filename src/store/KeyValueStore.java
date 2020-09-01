package store;

import java.util.HashMap;

import helper.ServerHelper;

public class KeyValueStore {
  private HashMap<String, String> keyValueMap = null;

  public KeyValueStore() {
    keyValueMap = new HashMap<String, String>();
    // for (int i = 1; i <= Constants.MAP_SIZE; i++) {
    // keyValueMap.put(i + "", i + "");
    // }
  }

  public String getKey(String key) {
    String val = keyValueMap.get(key);
    if (val == null) {
      val = "Fail";
    }
    String response = "Get Key: " + key + " Value: " + val;
    System.out.println(ServerHelper.getCurrentTime() + " " + response);
    return response;
  }

  public String putKey(String key, String val) {
    String response = "Added key : " + key + " Value: " + val;
    keyValueMap.put(key, val);
    System.out.println(ServerHelper.getCurrentTime() + " " + response);
    return response;
  }

  public String deleteKey(String key) {
    String val = keyValueMap.remove(key);
    String response = "";
    if (val == null) {
      val = "Fail";
    } else {
      val = "Sucess";
    }
    response = "Deleted key : " + key + " Status: " + val;
    System.out.println(ServerHelper.getCurrentTime() + " " + response);

    return response;
  }

  public boolean checkAction(String key, int action) {
    // If get the action is 1, put then action is 2 and delete then action is 3
    switch (action) {
    case 1:
      if (keyValueMap.containsKey(key))
        return true;
      break;
    case 2:
      if (!keyValueMap.containsKey(key))
        return true;
      break;
    case 3:
      if (keyValueMap.containsKey(key))
        return true;
      break;
    }
    return false;
  }
}
