package paxos;

import java.net.SocketTimeoutException;
import java.rmi.RemoteException;

import store.KeyStoreInterface;

public class PaxosServer implements KeyStoreInterface {

  private Proposer proposer;

  private Learner learner;
  private Acceptor acceptor;
  int serverNumber;

  public PaxosServer(int serverNumber) throws RemoteException, InterruptedException {
    proposer = new Proposer();
    learner = new Learner();
    acceptor = new Acceptor();
    this.serverNumber = serverNumber;
    proposer.start();
    learner.start();
    acceptor.start();
    acceptor.setServerNumber(serverNumber);
    proposer.setServerNumber(serverNumber);

  }

  public boolean stop(int time) throws InterruptedException {

    acceptor.setActive(false);
    boolean stop = acceptor.stop(time);
    acceptor.setActive(true);
    return stop;
  }

  public String get(String key, String val) {

    return proposer.propose(key, val, 1);
  }

  public String put(String key, String val) {

    return proposer.propose(key, val, 2);
  }

  public String delete(String key, String val) {

    return proposer.propose(key, val, 3);
  }

  @Override
  public boolean prepare(int proposalId, String key, int action) throws RemoteException, SocketTimeoutException {
    return acceptor.prepare(proposalId, key, action);
  }

  @Override
  public boolean accept(int proposalId, String key, int action) throws RemoteException, SocketTimeoutException {
    return acceptor.accept(proposalId, key, action);
  }

  @Override
  public String commit(String key, String val, int action) throws RemoteException, SocketTimeoutException {
    return learner.commit(key, val, action);
  }

}
