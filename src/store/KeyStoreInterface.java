package store;

import java.net.SocketTimeoutException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface KeyStoreInterface extends Remote {

  public String get(String key, String val) throws RemoteException;

  public String put(String key, String val) throws RemoteException;

  public String delete(String key, String val) throws RemoteException;

  public boolean prepare(int proposalId, String key, int action) throws RemoteException, SocketTimeoutException;

  public boolean accept(int proposalId, String key, int action) throws RemoteException, SocketTimeoutException;

  public String commit(String key, String val, int action) throws RemoteException, SocketTimeoutException;

}
