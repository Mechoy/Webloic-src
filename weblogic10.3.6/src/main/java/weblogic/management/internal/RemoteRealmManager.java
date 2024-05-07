package weblogic.management.internal;

import java.rmi.Remote;
import java.rmi.RemoteException;
import weblogic.management.configuration.ListResults;

public interface RemoteRealmManager extends Remote {
   void createUser(String var1, Object var2) throws RemoteException, RemoteRealmException;

   void createGroup(String var1) throws RemoteException, RemoteRealmException;

   void createAcl(String var1) throws RemoteException, RemoteRealmException;

   void removeUser(String var1) throws RemoteException, RemoteRealmException;

   void removeGroup(String var1) throws RemoteException, RemoteRealmException;

   void removeAcl(String var1) throws RemoteException, RemoteRealmException;

   boolean userExists(String var1) throws RemoteException, RemoteRealmException;

   boolean groupExists(String var1) throws RemoteException, RemoteRealmException;

   boolean aclExists(String var1) throws RemoteException, RemoteRealmException;

   ListResults listUsers() throws RemoteException, RemoteRealmException;

   ListResults listGroups() throws RemoteException, RemoteRealmException;

   ListResults listAcls() throws RemoteException, RemoteRealmException;

   boolean changeCredential(String var1, Object var2, Object var3) throws RemoteException, RemoteRealmException;

   ListResults getMembers(String var1) throws RemoteException, RemoteRealmException;

   void addMember(String var1, String var2) throws RemoteException, RemoteRealmException;

   void removeMember(String var1, String var2) throws RemoteException, RemoteRealmException;

   String[] getPermissions(String var1) throws RemoteException, RemoteRealmException;

   ListResults getGrantees(String var1, String var2) throws RemoteException, RemoteRealmException;

   void grantPermission(String var1, String var2, String var3) throws RemoteException, RemoteRealmException;

   void revokePermission(String var1, String var2, String var3) throws RemoteException, RemoteRealmException;
}
