package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import helper.Constants;
import paxos.PaxosServer;
import store.KeyStoreInterface;

/**
 * Server1
 * 
 * 
 *
 */
public class Server1 extends PaxosServer {

  public Server1(int serverNumber) throws RemoteException, InterruptedException {
    super(serverNumber);
  }

  public static void main(String args[]) throws Exception {

    try {
      Server1 server = new Server1(1);

      KeyStoreInterface stub = (KeyStoreInterface) UnicastRemoteObject.exportObject(server, 0);
      Registry registry = LocateRegistry.createRegistry(Constants.SERVER1_PORT_NO);
      registry.bind(Constants.SERVER1, stub);
      boolean active = true;
      System.out.println("Server 1 is running at port  " + Constants.SERVER1_PORT_NO);

      while (true) {
        Thread.sleep(1000);

        if (active) {

          active = server.stop(5000);

        }
        active = !active;

      }

    } catch (Exception e) {
      System.out.println("Server exception: " + e.toString());
    }

  }
}
