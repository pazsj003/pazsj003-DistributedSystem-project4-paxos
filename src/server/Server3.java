package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import helper.Constants;
import paxos.PaxosServer;
import store.KeyStoreInterface;

/**
 * Server3
 * 
 * 
 *
 */
public class Server3 extends PaxosServer {

  public Server3(int serverNumber) throws RemoteException, InterruptedException {
    super(serverNumber);
  }

  public static void main(String args[]) throws Exception {

    try {
      Server3 server = new Server3(3);

      KeyStoreInterface stub = (KeyStoreInterface) UnicastRemoteObject.exportObject(server, 0);
      Registry registry = LocateRegistry.createRegistry(Constants.SERVER3_PORT_NO);
      registry.bind(Constants.SERVER3, stub);
      boolean active = true;
      System.out.println("Server 3 is running at port  " + Constants.SERVER3_PORT_NO);

      while (true) {
        Thread.sleep(1000);

        if (active) {

          active = server.stop(4000);

        }
        active = !active;
      }

    } catch (Exception e) {
      System.out.println("Server exception: " + e.toString());
    }

  }
}
