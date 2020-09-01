package paxos;

import java.net.SocketTimeoutException;
import java.rmi.RemoteException;

import helper.Constants;
import helper.ServerHelper;
import store.KeyValueStore;

public class Acceptor extends KeyValueStore implements Runnable {

  private static int myproposalId;

  private boolean active;

  private int serverNumber;

  public int getMyproposalId() {
    return myproposalId;
  }

  public void setMyproposalId(int myproposalId) {
    Acceptor.myproposalId = myproposalId;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean isAlive) {
    this.active = isAlive;
  }

  public void start() {

    active = true;
    // run();
  }

  public void kill() {
    active = false;
  }

  public boolean accept(int proposalId, String key, int action) throws RemoteException, SocketTimeoutException {

    System.out.println(ServerHelper.getCurrentTime() + " Server " + serverNumber
        + " Acceptor log: Acceptor receive vote from Proposer: accept phase");
    return check(proposalId, key, action);
  }

  public boolean prepare(int proposalId, String key, int action) throws RemoteException, SocketTimeoutException {

    System.out.println(ServerHelper.getCurrentTime() + " Server " + serverNumber
        + " Acceptor log: Acceptor receive vote from Proposer: prepare phase");
    return check(proposalId, key, action);
  }

  public boolean stop(int time) throws InterruptedException {
    if (((int) ((Math.random() * Constants.NUMBER_OF_SERVERS) + 1)) == serverNumber) {
      System.out.println(ServerHelper.getCurrentTime() + " Server " + serverNumber + " Acceptor log: going to sleep");
      active = false;
      Thread.sleep(time);
      System.out.println(ServerHelper.getCurrentTime() + " Server " + serverNumber + " Acceptor log: wake up");
      active = true;
      return true;
    }
    return false;

  }

  private boolean check(int proposalId, String key, int action) throws RemoteException, SocketTimeoutException {
    // Randomly put a server to sleep
    // if (!active)
    // return false;
    // active = true;
    // try {
    // stop();
    // } catch (InterruptedException ie) {
    //
    // }
    if (!active) {
      System.out.println(
          ServerHelper.getCurrentTime() + " Server " + serverNumber + " Acceptor log: is sleepping, can't process");

      return false;
    }
    System.out.println(
        ServerHelper.getCurrentTime() + " Server " + serverNumber + " Acceptor log: Acceptor start to compare ID");
    if (proposalId < myproposalId) {

      System.out.println(ServerHelper.getCurrentTime() + " Server " + serverNumber + " Acceptor log: proposalId "
          + proposalId + " is not larger than current received Id " + myproposalId + " ,ignored");
      return false;
    }
    // if (super.checkAction(key, action)) {
    setMyproposalId(proposalId);

    System.out.println(ServerHelper.getCurrentTime() + " Server " + serverNumber + " Acceptor log: proposalId "
        + proposalId + " is larger than current received Id " + myproposalId + " , accepted");
    return true;
    // }

    // return false;
  }

  public int getServerNumber() {
    return serverNumber;
  }

  public void setServerNumber(int serverNumber) {
    this.serverNumber = serverNumber;
  }

  @Override
  public void run() {

    // if (!active) {
    // try {
    //
    // } catch (InterruptedException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // active = true;
    // }

  }
}
