package weblogic.servlet.internal;

import java.rmi.RemoteException;
import weblogic.cluster.replication.ROID;
import weblogic.cluster.replication.ReplicationManager;
import weblogic.servlet.internal.session.HTTPSessionLogger;
import weblogic.servlet.internal.session.ReplicatedSessionContext;
import weblogic.servlet.internal.session.SessionContext;

public final class ROIDLookupImpl implements ROIDLookup {
   HttpServer httpSrvr = null;

   public ROIDLookupImpl(HttpServer var1) {
      this.httpSrvr = var1;
   }

   public ROID lookupROID(String var1, String var2, String var3) throws RemoteException {
      WebAppServletContext[] var4 = this.httpSrvr.getServletContextManager().getAllContexts();
      if (var4 == null) {
         return null;
      } else {
         for(int var5 = 0; var5 < var4.length; ++var5) {
            SessionContext var6 = var4[var5].getSessionContext();
            if (var6 instanceof ReplicatedSessionContext && var6.getConfigMgr().getCookieName().equals(var2) && var6.getConfigMgr().getCookiePath().equals(var3)) {
               ReplicatedSessionContext var7 = (ReplicatedSessionContext)var6;
               ROID var8 = var7.getROID(var1);
               if (var8 != null) {
                  return var8;
               }
            }
         }

         return null;
      }
   }

   public void updateLastAccessTimes(ROID[] var1, long[] var2, long var3, String var5) throws RemoteException {
      if (var1 != null && var1.length >= 1) {
         long var6 = System.currentTimeMillis() - var3;
         WebAppServletContext var8 = this.httpSrvr.getServletContextManager().getContextForContextPath(var5);
         if (var8 != null) {
            try {
               ReplicatedSessionContext var9 = (ReplicatedSessionContext)var8.getSessionContext();

               for(int var10 = 0; var10 < var1.length; ++var10) {
                  var9.updateSecondaryLAT(var1[var10], var2[var10] + var6);
               }

            } catch (ClassCastException var11) {
               HTTPSessionLogger.logPersistentStoreTypeNotReplicated(var5, "updateLastAccessTimes");
            }
         }
      }
   }

   public void unregister(ROID var1, Object[] var2) throws RemoteException {
      for(int var3 = 0; var3 < var2.length; ++var3) {
         ReplicationManager.services().unregister(var1, var2[var3]);
      }

   }
}
