package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import helper.Constants;
import paxos.PaxosServer;
import store.KeyStoreInterface;

/**
 * Server4
 * 
 *
 *
 */
public class Server4 extends PaxosServer {

  public Server4(int serverNumber) throws RemoteException, InterruptedException {
    super(serverNumber);
  }

  public void randomDown() {

  }

  public static void main(String args[]) throws Exception {

    try {
      Server4 server = new Server4(4);

      KeyStoreInterface stub = (KeyStoreInterface) UnicastRemoteObject.exportObject(server, 0);
      Registry registry = LocateRegistry.createRegistry(Constants.SERVER4_PORT_NO);
      registry.bind(Constants.SERVER4, stub);
      boolean active = true;
      System.out.println("Server 4 is running at port  " + Constants.SERVER4_PORT_NO);

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
