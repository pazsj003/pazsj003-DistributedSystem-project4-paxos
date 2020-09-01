package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import helper.Constants;
import paxos.PaxosServer;
import store.KeyStoreInterface;

/**
 * Server5
 * 
 * 
 *
 */
public class Server5 extends PaxosServer {

  public Server5(int serverNumber) throws RemoteException, InterruptedException {
    super(serverNumber);
  }

  public static void main(String args[]) throws Exception {

    try {
      Server5 server = new Server5(5);

      KeyStoreInterface stub = (KeyStoreInterface) UnicastRemoteObject.exportObject(server, 0);
      Registry registry = LocateRegistry.createRegistry(Constants.SERVER5_PORT_NO);
      registry.bind(Constants.SERVER5, stub);
      boolean active = true;
      System.out.println("Server 5 is running at port  " + Constants.SERVER5_PORT_NO);

      while (true) {
        Thread.sleep(1000);

        if (active) {

          active = server.stop(6000);

        }
        active = !active;

      }

    } catch (Exception e) {
      System.out.println("Server exception: " + e.toString());
    }

  }
}
