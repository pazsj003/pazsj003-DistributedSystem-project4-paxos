package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;

import helper.Constants;
import helper.ServerHelper;
import store.KeyStoreInterface;

public class Client {
  KeyStoreInterface stub;

  public Client(String[] args) throws Exception {

    Registry registry = LocateRegistry.getRegistry(args[0], ServerHelper.getPortNumber(args[1]));
    stub = (KeyStoreInterface) registry.lookup(args[1]);

  }

  public static void RandomShutDown(Client client) throws IOException {
    int count = 0;
    while (true) {
      count++;
      clientOperation(client, "PUT", "1001", "101");
      if (count == 500)
        break;
    }

  }

  public static void normalTest(Client client) throws Exception {

    clientOperation(client, "PUT", "1000", "101");
    clientOperation(client, "GET", "1000", "");
    clientOperation(client, "DELETE", "1000", "");
    clientOperation(client, "GET", "100", "");
    clientOperation(client, "PUT", "2000", "11");
    clientOperation(client, "DELETE", "1410", "");
    clientOperation(client, "PUT", "1510", "531");
    clientOperation(client, "DELETE", "754", "");
    clientOperation(client, "GET", "1420", "");
    clientOperation(client, "DELETE", "754", "");
    clientOperation(client, "GET", "1510", "");
    clientOperation(client, "PUT", "2000", "642");
    clientOperation(client, "GET", "2000", "");
    clientOperation(client, "DELETE", "417", "");
    clientOperation(client, "GET", "427", "");
    clientOperation(client, "PUT", "2153", "124");
    clientOperation(client, "DELETE", "2553", "");
    clientOperation(client, "GET", "810", "");
    clientOperation(client, "PUT", "1777", "5132");
    clientOperation(client, "DELETE", "1111", "");
    clientOperation(client, "PUT", "1788", "5423");

  }

  public static void input(Client client) throws Exception {
    System.out.print("Please enter machine number, function and values:");
    System.out.print("We have 5 machines, you should choose between 0-4" + "\n");
    while (true) {

      System.out.print("If it is a PUT, the input format is: PUT KEY VALUE" + "\n");
      System.out.print("If it is a GET or DEL, the format is: GET/DELETE KEY" + "\n");
      System.out.print("Example PUT 101 188 or DELETE 120" + "\n");
      System.out.print("Type EXIT to quit" + "\n");
      String input = GetStringFromTerminal();

      String[] formattedInput = input.split(" ");
      if (formattedInput.length == 2) {

        String arg2 = formattedInput[0];
        String arg3 = formattedInput[1];

        clientOperation(client, arg2, arg3, "");

      } else if (formattedInput.length == 3) {

        String arg2 = formattedInput[0];
        String arg3 = formattedInput[1];
        String arg4 = formattedInput[2];

        clientOperation(client, arg2, arg3, arg4);
      } else if (formattedInput.length == 1 && formattedInput[0].toUpperCase().equals("EXIT")) {
        System.exit(0);
      } else {
        System.out.println("Invalid input length");
      }
    }
  }

  public static void clientOperation(Client client, String operation, String key, String value) throws IOException {
    if (operation.toUpperCase().equals("PUT")) {

      String res = client.stub.put(key, value);
      System.out.println("Server response time " + Constants.FORMATTER.format(new Date().getTime()) + " " + res);

    } else if (operation.toUpperCase().equals("GET")) {
      String res = client.stub.get(key, value);
      System.out.println("Server response time " + Constants.FORMATTER.format(new Date().getTime()) + " " + res);

    } else if (operation.toUpperCase().equals("DELETE")) {
      String res = client.stub.delete(key, value);
      System.out.println("Server response time " + Constants.FORMATTER.format(new Date().getTime()) + " " + res);

    }

  }

  public static String GetStringFromTerminal() throws IOException {
    BufferedReader stringIn = new BufferedReader(new InputStreamReader(System.in));
    return stringIn.readLine();
  }

  public static void main(String args[]) throws Exception {
    try {

      Client client = new Client(args);
      // normalTest(client);
      // input(client);
      RandomShutDown(client);
    } catch (RemoteException re) {
      System.out.println("Unable to find the RMI Server");
    } catch (NotBoundException ne) {
      System.out.println("RMI Server not bound");
    }
  }
}
