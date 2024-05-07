package weblogic.cluster;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.naming.NamingException;
import weblogic.common.internal.PeerInfo;
import weblogic.utils.Debug;

public final class ServiceAdvertiser {
   private int nextServiceID;
   private Map serviceMap;

   public static ServiceAdvertiser theOne() {
      return ServiceAdvertiser.SingletonMaker.singleton;
   }

   private ServiceAdvertiser() {
      this.nextServiceID = 0;
      this.serviceMap = new HashMap();
   }

   public void offerService(String var1, String var2, Object var3) throws NamingException {
      this.offerService(var1, var2, var3, (AdvertisementStatusListener)null);
   }

   public void createSubcontext(String var1) throws NamingException {
      this.offerService(var1, (String)null, (Object)null, (AdvertisementStatusListener)null);
   }

   public void offerService(String var1, String var2, Object var3, AdvertisementStatusListener var4) throws NamingException {
      this.announceOffer(var1, var2, var3);
   }

   public void replaceService(String var1, String var2, Object var3, Object var4) throws NamingException {
      this.announceReplacement(var1, var2, var3, var4);
   }

   public void retractService(String var1, String var2, Object var3) throws NamingException {
      this.announceReplacement(var1, var2, var3, (Object)null);
   }

   private synchronized void announceOffer(String var1, String var2, Object var3) throws NamingException {
      int var4 = this.getNextServiceID();
      BasicServiceOffer var5 = new BasicServiceOffer(var4, var1, var2, var3);
      AnnouncementManager.theOne().announce((ServiceRetract)null, var5);
      this.addToMap(new CompositeKey(var1, var2), var3, var4);
   }

   private final synchronized void announceReplacement(String var1, String var2, Object var3, Object var4) throws NamingException {
      ServiceRetract var5 = null;
      BasicServiceOffer var6 = null;
      CompositeKey var7 = new CompositeKey(var1, var2);
      ServiceRec var8 = this.getRecordFromMap(var7, var3);
      int var9 = -1;
      int var10;
      if (var8 != null) {
         var10 = var8.id;
         var9 = var10;
         if (var4 != null) {
            var5 = new ServiceRetract(var10, true);
         } else {
            var5 = new ServiceRetract(var10, false);
         }
      }

      if (var4 != null) {
         var10 = this.getNextServiceID();
         this.addToMap(var7, var4, var10);
         var6 = new BasicServiceOffer(var10, var1, var2, var4, var9);
      }

      if (var5 != null || var6 != null) {
         AnnouncementManager.theOne().announce(var5, var6);
         if (var5 != null) {
            this.removeFromMap(var7, var8);
         }
      }

   }

   private int getNextServiceID() {
      synchronized(this.serviceMap) {
         return this.nextServiceID++;
      }
   }

   private final void addToMap(CompositeKey var1, Object var2, int var3) {
      synchronized(this.serviceMap) {
         Object var5 = this.serviceMap.get(var1);
         Object var6 = (List)var5;
         if (var5 == null) {
            var6 = new ArrayList(1);
            this.serviceMap.put(var1, var6);
         }

         ((List)var6).add(new ServiceRec(var3, var2));
      }
   }

   private final ServiceRec getRecordFromMap(CompositeKey var1, Object var2) {
      synchronized(this.serviceMap) {
         List var4 = (List)this.serviceMap.get(var1);
         if (var4 == null) {
            return null;
         } else {
            Debug.assertion(var4.size() != 0);
            if (var2 == null) {
               return (ServiceRec)var4.get(0);
            } else {
               ServiceRec var5 = new ServiceRec(-1, var2);
               int var6 = var4.indexOf(var5);
               return var6 != -1 ? (ServiceRec)var4.get(var6) : null;
            }
         }
      }
   }

   private final void removeFromMap(CompositeKey var1, ServiceRec var2) {
      synchronized(this.serviceMap) {
         List var4 = (List)this.serviceMap.get(var1);
         if (var4 != null) {
            int var5 = var4.indexOf(var2);
            if (var5 != -1) {
               var4.remove(var5);
               if (var4.size() == 0) {
                  this.serviceMap.remove(var1);
               }
            }
         }

      }
   }

   synchronized void rewriteServicesAtNewVersion(PeerInfo var1, PeerInfo var2) {
      Iterator var3 = this.serviceMap.entrySet().iterator();

      while(var3.hasNext()) {
         Map.Entry var4 = (Map.Entry)var3.next();
         CompositeKey var5 = (CompositeKey)var4.getKey();
         List var6 = (List)var4.getValue();
         if (var6 != null && var6.size() != 0) {
            ServiceRec var7 = (ServiceRec)var6.get(0);

            try {
               if (UpgradeUtils.needsRewrite(var7.service, var1, var2)) {
                  try {
                     this.announceReplacement(var5.name, var5.appId, var7.service, var7.service);
                  } catch (NamingException var9) {
                  }
               }
            } catch (IOException var10) {
            }
         }
      }

   }

   // $FF: synthetic method
   ServiceAdvertiser(Object var1) {
      this();
   }

   private static class SingletonMaker {
      private static final ServiceAdvertiser singleton = new ServiceAdvertiser();
   }
}
