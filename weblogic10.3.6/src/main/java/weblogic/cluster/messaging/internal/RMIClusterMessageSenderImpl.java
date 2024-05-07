package weblogic.cluster.messaging.internal;

import java.rmi.RemoteException;
import java.rmi.UnknownHostException;
import java.security.AccessController;
import java.util.HashMap;
import java.util.Hashtable;
import javax.naming.NamingException;
import weblogic.management.configuration.DatabaseLessLeasingBasisMBean;
import weblogic.management.provider.ManagementService;
import weblogic.protocol.URLManager;
import weblogic.rmi.extensions.PortableRemoteObject;
import weblogic.rmi.extensions.RemoteRuntimeException;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;
import weblogic.utils.collections.ConcurrentHashMap;

public class RMIClusterMessageSenderImpl implements ClusterMessageSender {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final DebugCategory debugMessageSender = Debug.getCategory("weblogic.cluster.leasing.MessageSender");
   private static final boolean DEBUG = debugEnabled();
   private static final ConcurrentHashMap serverNameToURL = new ConcurrentHashMap();
   private boolean isOneWay;

   private void setOneWay(boolean var1) {
      this.isOneWay = var1;
   }

   public static RMIClusterMessageSenderImpl getInstance() {
      return RMIClusterMessageSenderImpl.Factory.SEND_RECV;
   }

   public static RMIClusterMessageSenderImpl getOneWay() {
      return RMIClusterMessageSenderImpl.Factory.ONE_WAY;
   }

   public ClusterResponse[] send(ClusterMessage var1, ServerInformation[] var2) throws ClusterMessageProcessingException {
      ClusterResponse[] var3 = new ClusterResponse[var2.length];
      HashMap var4 = new HashMap();

      for(int var5 = 0; var5 < var2.length; ++var5) {
         try {
            var3[var5] = this.send(var1, var2[var5]);
         } catch (RemoteException var7) {
            var4.put(var2[var5], var7);
         }
      }

      if (var4.size() > 0) {
         throw new ClusterMessageProcessingException(var3, var4);
      } else {
         return var3;
      }
   }

   public ClusterResponse send(ClusterMessage var1, ServerInformation var2) throws RemoteException {
      return this.send(var1, var2.getServerName());
   }

   public ClusterResponse send(ClusterMessage var1, String var2) throws RemoteException {
      return this.send(var1, var2, -1);
   }

   public ClusterResponse send(ClusterMessage var1, String var2, int var3) throws RemoteException {
      if (DEBUG) {
         debug("trying to send message " + var1 + " to " + var2);
      }

      ClusterMessageEndPoint var4;
      if (var3 < 0) {
         var4 = this.getRMIMessageEndPoint(var2);
      } else {
         var4 = this.getRMIMessageEndPoint(var2, var3);
      }

      if (var4 == null) {
         throw new ClusterMessageProcessingException("MessageEndPoint not available for " + var2 + ". The server is probably dead");
      } else {
         try {
            if (DEBUG) {
               debug(Thread.currentThread() + ": receiver found for " + var2);
            }

            if (this.isOneWay) {
               var4.processOneWay(var1);
               return null;
            } else {
               ClusterResponse var5 = var4.process(var1);
               if (DEBUG) {
                  debug("response received for " + var2 + ". Response = " + var5);
               }

               return var5;
            }
         } catch (RemoteException var6) {
            if (DEBUG) {
               debug("exception processing message for " + var2);
            }

            if (DEBUG) {
               var6.printStackTrace();
            }

            throw var6;
         } catch (RemoteRuntimeException var7) {
            if (DEBUG) {
               debug("remote runtime exception processing message for " + var2);
            }

            if (DEBUG) {
               var7.printStackTrace();
            }

            throw new RemoteException("RMI stub threw a sender side exception ", var7);
         }
      }
   }

   private static void debug(String var0) {
      DebugLogger.debug("[RMIClusterMessageSender] " + var0);
   }

   private ClusterMessageEndPoint getRMIMessageEndPoint(String var1) throws RemoteException {
      DatabaseLessLeasingBasisMBean var2 = ManagementService.getRuntimeAccess(kernelId).getServer().getCluster().getDatabaseLessLeasingBasis();
      int var3 = var2.getMessageDeliveryTimeout();
      return this.getRMIMessageEndPoint(var1, var3);
   }

   private ClusterMessageEndPoint getRMIMessageEndPoint(String var1, int var2) throws RemoteException {
      try {
         weblogic.jndi.Environment var3 = new weblogic.jndi.Environment(new Hashtable());
         var3.setRequestTimeout((long)var2);
         var3.setProviderUrl(getURL(var1));
         ClusterMessageEndPoint var4 = (ClusterMessageEndPoint)PortableRemoteObject.narrow(var3.getInitialReference(RMIClusterMessageEndPointImpl.class), ClusterMessageEndPoint.class);
         return var4;
      } catch (UnknownHostException var5) {
         if (DEBUG) {
            var5.printStackTrace();
         }

         throw var5;
      } catch (NamingException var6) {
         if (DEBUG) {
            var6.printStackTrace();
         }

         return null;
      }
   }

   private static String getURL(String var0) throws UnknownHostException {
      try {
         String var1 = URLManager.findAdministrationURL(var0);
         serverNameToURL.put(var0, var1);
         return var1;
      } catch (UnknownHostException var3) {
         String var2 = (String)serverNameToURL.get(var0);
         if (var2 != null) {
            return var2;
         } else {
            throw var3;
         }
      }
   }

   private static boolean debugEnabled() {
      return debugMessageSender.isEnabled() || DebugLogger.isDebugEnabled();
   }

   private static final class Factory {
      static final RMIClusterMessageSenderImpl SEND_RECV = new RMIClusterMessageSenderImpl();
      static final RMIClusterMessageSenderImpl ONE_WAY = new RMIClusterMessageSenderImpl();

      static {
         ONE_WAY.setOneWay(true);
      }
   }
}
