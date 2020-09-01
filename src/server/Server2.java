package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import helper.Constants;
import paxos.PaxosServer;
import store.KeyStoreInterface;

/**
 * Server2
 * 
 * 
 *
 */
public class Server2 extends PaxosServer {

  public Server2(int serverNumber) throws RemoteException, InterruptedException {
    super(serverNumber);
  }

  public static void main(String args[]) throws Exception {

    try {
      Server2 server = new Server2(2);
      KeyStoreInterface stub = (KeyStoreInterface) UnicastRemoteObject.exportObject(server, 0);
      Registry registry = LocateRegistry.createRegistry(Constants.SERVER2_PORT_NO);
      registry.bind(Constants.SERVER2, stub);
      boolean active = true;
      System.out.println("Server 2 is running at port  " + Constants.SERVER2_PORT_NO);

      while (true) {
        Thread.sleep(1000);

        if (active) {

          active = server.stop(3000);

        }
        active = !active;

      }

    } catch (Exception e) {
      System.out.println("Server exception: " + e.toString());
    }

  }
}