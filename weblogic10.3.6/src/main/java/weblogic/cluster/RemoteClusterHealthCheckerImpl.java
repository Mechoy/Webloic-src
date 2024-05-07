package weblogic.cluster;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import weblogic.jndi.Environment;
import weblogic.rmi.extensions.server.RemoteDomainSecurityHelper;
import weblogic.rmi.extensions.server.ServerHelper;
import weblogic.rmi.spi.HostID;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.subject.SubjectManager;

public final class RemoteClusterHealthCheckerImpl implements RemoteClusterHealthChecker, ClusterMembersChangeListener {
   static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private long currentVersion;
   private final HashSet set;

   public static RemoteClusterHealthCheckerImpl getInstance() {
      return RemoteClusterHealthCheckerImpl.SingletonMaker.singleton;
   }

   private RemoteClusterHealthCheckerImpl() {
      this.set = new HashSet();
      this.updateMembershipInfo(ClusterService.getClusterService().getLocalMember(), true);
   }

   public static void start() {
      try {
         ClusterService var0 = ClusterService.getClusterService();
         Iterator var1 = var0.getRemoteMembers().iterator();

         while(var1.hasNext()) {
            ClusterMemberInfo var2 = (ClusterMemberInfo)var1.next();
            getInstance().updateMembershipInfo(var2, true);
         }

         var0.addClusterMembersListener(getInstance());
         Environment var4 = new Environment();
         var4.setReplicateBindings(true);
         var4.getInitialContext().bind("weblogic/cluster/RemoteClusterHealthChecker", getInstance());
      } catch (NamingException var3) {
         throw new AssertionError("Unexpected exception" + var3.toString());
      }
   }

   public static void stop() {
      ClusterService.getClusterService().removeClusterMembersListener(getInstance());

      try {
         (new InitialContext()).unbind("weblogic/cluster/RemoteClusterHealthChecker");
         ServerHelper.unexportObject(getInstance(), false);
      } catch (NamingException var1) {
         throw new AssertionError("Unexpected exception" + var1.toString());
      } catch (NoSuchObjectException var2) {
      }

   }

   public ArrayList checkClusterMembership(long var1) throws RemoteException {
      HashSet var3 = null;
      this.verifyCaller();
      synchronized(this) {
         if (var1 == this.currentVersion) {
            return null;
         }

         var3 = (HashSet)this.set.clone();
      }

      ArrayList var4 = new ArrayList(var3);
      return var4;
   }

   private void verifyCaller() {
      try {
         HostID var1 = ServerHelper.getClientEndPoint().getHostID();
         AuthenticatedSubject var2 = (AuthenticatedSubject)SubjectManager.getSubjectManager().getCurrentSubject(KERNEL_ID);
         if (var2 == null) {
            throw new SecurityException("Null user is not permitted to perform MAN remote cluster membership operations");
         } else {
            int var3 = RemoteDomainSecurityHelper.acceptRemoteDomainCall(var1, var2);
            if (var3 == 1) {
               throw new SecurityException("user " + var2.getName() + " is not " + "permitted to perform MAN cluster membership operations");
            }
         }
      } catch (ServerNotActiveException var4) {
         throw new SecurityException("operation not permitted");
      }
   }

   public void clusterMembersChanged(ClusterMembersChangeEvent var1) {
      ClusterMemberInfo var2 = var1.getClusterMemberInfo();
      int var3 = var1.getAction();
      if (var3 == 1) {
         this.updateMembershipInfo(var2, false);
      } else if (var3 == 0) {
         this.updateMembershipInfo(var2, true);
      }

   }

   private synchronized void updateMembershipInfo(ClusterMemberInfo var1, boolean var2) {
      if (var2) {
         this.set.add(var1);
      } else {
         this.set.remove(var1);
      }

      this.currentVersion = 0L;

      ClusterMemberInfo var4;
      for(Iterator var3 = this.set.iterator(); var3.hasNext(); this.currentVersion += (long)var4.identity().hashCode()) {
         var4 = (ClusterMemberInfo)var3.next();
      }

   }

   // $FF: synthetic method
   RemoteClusterHealthCheckerImpl(Object var1) {
      this();
   }

   public static class SingletonMaker {
      private static final RemoteClusterHealthCheckerImpl singleton = new RemoteClusterHealthCheckerImpl();
   }
}
