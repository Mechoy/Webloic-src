package weblogic.deploy.service.internal.adminserver;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import weblogic.deploy.common.Debug;
import weblogic.deploy.service.StatusListener;
import weblogic.deploy.service.StatusListenerManager;
import weblogic.work.WorkManagerFactory;

public final class StatusDeliverer implements StatusListenerManager {
   private static final StatusDeliverer SINGLETON = new StatusDeliverer();
   private final Map statusListeners = new HashMap();

   private StatusDeliverer() {
   }

   public static StatusDeliverer getInstance() {
      return SINGLETON;
   }

   public void registerStatusListener(String var1, StatusListener var2) {
      synchronized(this.statusListeners) {
         this.statusListeners.put(var1, var2);
         if (Debug.isServiceStatusDebugEnabled()) {
            Debug.serviceStatusDebug("added status listener for channel '" + var1 + "'");
         }

      }
   }

   public void unregisterStatusListener(String var1) {
      synchronized(this.statusListeners) {
         this.statusListeners.remove(var1);
         if (Debug.isServiceStatusDebugEnabled()) {
            Debug.serviceStatusDebug("removed status listener for channel '" + var1 + "'");
         }

      }
   }

   public void deliverStatus(final String var1, final Serializable var2, final String var3) {
      final StatusListener var7;
      synchronized(this.statusListeners) {
         var7 = (StatusListener)this.statusListeners.get(var1);
      }

      if (var7 != null) {
         WorkManagerFactory.getInstance().getSystem().schedule(new Runnable() {
            public void run() {
               var7.statusReceived(var2, var3);
               if (Debug.isServiceStatusDebugEnabled()) {
                  Debug.serviceStatusDebug("delivered status object '" + var2 + "' received on channel id '" + var1 + "' from server '" + var3 + "'");
               }

            }
         });
      }

   }

   public void deliverStatus(final long var1, final String var3, final Serializable var4, final String var5) {
      final StatusListener var11;
      synchronized(this.statusListeners) {
         var11 = (StatusListener)this.statusListeners.get(var3);
      }

      if (var11 != null) {
         WorkManagerFactory.getInstance().getSystem().schedule(new Runnable() {
            public void run() {
               var11.statusReceived(var1, var4, var5);
               if (Debug.isServiceStatusDebugEnabled()) {
                  Debug.serviceStatusDebug("delivered status object '" + var4 + "' with session id '" + var1 + "' and channelId '" + var3 + "' from server '" + var5 + "'");
               }

            }
         });
      }

   }
}
