package paxos;

import java.net.SocketTimeoutException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;

import helper.Constants;
import helper.ServerHelper;
import store.KeyStoreInterface;
import store.KeyValueStore;

public class Proposer extends KeyValueStore implements Runnable {

  private static int proposalId;

  private String value;

  private int serverNumber;

  public int getServerNumber() {
    return serverNumber;
  }

  public void setServerNumber(int serverNumber) {
    this.serverNumber = serverNumber;
  }

  public int getProposalId() {
    return proposalId;
  }

  public Proposer() {
    super();
  }

  public void setProposalId(int proposalId) {
    Proposer.proposalId = proposalId;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public void start() {
    proposalId = 0;
  }

  public synchronized String propose(String key, String val, int action) {
    String response = "";
    setValue(key);
    Map<String, String> serverMap = ServerHelper.getServerMap();
    Registry registry = null;
    int count = 0;
    proposalId++;

    System.out.println(ServerHelper.getCurrentTime() + " Server " + serverNumber
        + " Proposer log: Proposer send vote to Acceptor- phase prepare");
    try {
      for (Map.Entry<String, String> entry : serverMap.entrySet()) {
        try {
          registry = LocateRegistry.getRegistry(entry.getValue(), ServerHelper.getPortNumber(entry.getKey()));
          KeyStoreInterface stub = (KeyStoreInterface) registry.lookup(entry.getKey());

          if (stub.prepare(proposalId, key, action)) {
            count++;
            System.out.println(ServerHelper.getCurrentTime() + " Server " + serverNumber + " Proposer log: received "
                + count + " servers replied with prepare ok");
          }
        } catch (SocketTimeoutException se) {
          // Continue the process even if one server times out
          continue;
        } catch (RemoteException re) {
          // Continue the process even if one server was not reachable
          continue;
        }
      }
      // Ensure atleast 3 servers reply with ok
      if (count > (Constants.NUMBER_OF_SERVERS / 2)) {
        System.out.println(ServerHelper.getCurrentTime() + " Server " + serverNumber + " Proposer log: received "
            + count + " servers replied with prepare ok, go to next phase-> accept");
        count = 0;
        for (Map.Entry<String, String> entry : serverMap.entrySet()) {
          try {
            registry = LocateRegistry.getRegistry(entry.getValue(), ServerHelper.getPortNumber(entry.getKey()));
            KeyStoreInterface stub = (KeyStoreInterface) registry.lookup(entry.getKey());
            // Check with all servers if they can accept the proposal

            if (stub.accept(proposalId, key, action)) {

              count++;
              System.out.println(ServerHelper.getCurrentTime() + " Server " + serverNumber + " Proposer log: received "
                  + count + " servers replied with accept ok");

            }
          } catch (SocketTimeoutException se) {
            // Continue the process even if one server times out
            continue;
          } catch (RemoteException re) {
            // Continue the process even if one server was not reachable
            continue;
          }
        }
      } else {
        response = ServerHelper.getCurrentTime() + " Server " + serverNumber
            + " Proposer log: Consensus could not be reached as only " + count
            + " servers replied to the prepare request";
        System.out.println(response);
        return response;
      }
      // Ensure at least 3 servers reply with ok
      if (count > (Constants.NUMBER_OF_SERVERS / 2)) {
        System.out.println(ServerHelper.getCurrentTime() + " Server " + serverNumber + " Proposer log: received "
            + count + " servers replied with accept ok, go to next phase-> commit");
        for (Map.Entry<String, String> entry : serverMap.entrySet()) {
          try {
            registry = LocateRegistry.getRegistry(entry.getValue(), ServerHelper.getPortNumber(entry.getKey()));
            KeyStoreInterface stub = (KeyStoreInterface) registry.lookup(entry.getKey());
            // Ask all servers to commit as quorum number has accepted
            response = stub.commit(key, val, action);

          } catch (SocketTimeoutException se) {
            // Continue the process even if one server times out
            continue;
          } catch (RemoteException re) {
            // Continue the process even if one server was not reachable
            continue;
          }
        }
      } else {
        response = ServerHelper.getCurrentTime() + " Server " + serverNumber
            + " Proposer log: Consensus could not be reached as only " + count
            + " servers replied to the accept request";
        System.out.println(response);
        return response;
      }
    } catch (NotBoundException nbe) {
      System.out.println("Remote Exception" + nbe);
    }
    return response;
  }

  @Override
  public void run() {
    // TODO Auto-generated method stub

  }
}
