package weblogic.cluster;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.naming.Context;
import javax.naming.NamingException;
import weblogic.rmi.spi.HostID;
import weblogic.utils.AssertionError;

public final class NodeInfo {
   private static final boolean debug = false;
   private Collection installedOffers = new ArrayList();
   private Collection pendingOffers = null;
   int numUnprocessedRequests;
   private String name;
   private Context initialCtx;
   private ConflictHandler conHandler;
   private long approximateAge;
   private long ageThresholdMillis;

   public NodeInfo(Context var1, ConflictHandler var2, String var3, long var4) {
      this.initialCtx = var1;
      this.conHandler = var2;
      this.name = var3;
      this.ageThresholdMillis = var4 * 1000L;
   }

   synchronized void install(ServiceOffer var1, boolean var2) {
      try {
         if (!var2) {
            var1.install(this.initialCtx);
         }

         if (this.installedOffers.size() == 0) {
            this.approximateAge = var1.approximateAge();
         }

         this.installedOffers.add(var1);
         if (this.pendingOffers != null) {
            this.conHandler.conflictStart(var1);
         }
      } catch (NamingException var4) {
         if (this.pendingOffers == null) {
            this.conflictStart(this.installedOffers);
            this.pendingOffers = new ArrayList();
         }

         if (this.approximateAge <= this.ageThresholdMillis && var1.approximateAge() > this.ageThresholdMillis) {
            this.switchWithInstalledOffers(var1);
         } else {
            this.pendingOffers.add(var1);
         }

         this.conHandler.conflictStart(var1);
      }

   }

   synchronized void update(ServiceOffer var1, boolean var2) {
      try {
         if (!var2) {
            var1.update(this.initialCtx);
         }

         if (this.installedOffers.size() == 0) {
            this.approximateAge = var1.approximateAge();
         }

         this.remove(var1.getOldID(), var1.getServerID(), this.installedOffers);
         this.installedOffers.add(var1);
         if (this.pendingOffers != null) {
            this.conHandler.conflictStart(var1);
         }
      } catch (NamingException var4) {
         if (this.pendingOffers == null) {
            this.conflictStart(this.installedOffers);
            this.pendingOffers = new ArrayList();
         }

         if (this.approximateAge <= this.ageThresholdMillis && var1.approximateAge() > this.ageThresholdMillis) {
            this.switchWithInstalledOffers(var1);
         } else {
            this.pendingOffers.add(var1);
         }

         this.conHandler.conflictStart(var1);
      }

   }

   private void switchWithInstalledOffers(ServiceOffer var1) {
      Iterator var2 = this.installedOffers.iterator();

      while(var2.hasNext()) {
         ServiceOffer var3 = (ServiceOffer)var2.next();
         var2.remove();

         try {
            var3.retract(this.initialCtx);
            this.pendingOffers.add(var3);
         } catch (NamingException var6) {
            throw new AssertionError("Impossible exception", var6);
         }
      }

      try {
         var1.install(this.initialCtx);
         this.installedOffers.add(var1);
         this.approximateAge = var1.approximateAge();
      } catch (NamingException var5) {
         ClusterLogger.logOfferReplacementError(var1.toString(), var5);
         this.pendingOffers.add(var1);
      }

   }

   synchronized void retract(ServiceOffer var1, boolean var2) {
      if (this.remove(var1.id(), var1.getServerID(), this.installedOffers)) {
         if (!var2) {
            try {
               var1.retract(this.initialCtx);
            } catch (NamingException var7) {
            }
         }

         if (this.pendingOffers != null) {
            this.conHandler.conflictStop(var1);
            if (this.installedOffers.isEmpty()) {
               Iterator var3 = this.pendingOffers.iterator();

               while(var3.hasNext()) {
                  ServiceOffer var4 = (ServiceOffer)var3.next();

                  try {
                     var4.install(this.initialCtx);
                     var3.remove();
                     this.installedOffers.add(var4);
                  } catch (NamingException var6) {
                  }
               }
            }
         }
      } else {
         boolean var8 = false;
         if (this.pendingOffers != null) {
            var8 = this.remove(var1.id(), var1.getServerID(), this.pendingOffers);
         }

         if (!var8) {
            ClusterLogger.logRetractUnrecognizedOfferError(var1.toString());
         }
      }

      if (this.pendingOffers != null && this.pendingOffers.isEmpty()) {
         this.conflictStop(this.installedOffers);
         this.pendingOffers = null;
      }

   }

   boolean isEmpty() {
      return this.pendingOffers == null && this.installedOffers.isEmpty();
   }

   private boolean remove(int var1, HostID var2, Collection var3) {
      Iterator var4 = var3.iterator();

      ServiceOffer var5;
      do {
         if (!var4.hasNext()) {
            return false;
         }

         var5 = (ServiceOffer)var4.next();
      } while(!var5.getServerID().equals(var2) || var5.id() != var1);

      var4.remove();
      return true;
   }

   private void conflictStart(Collection var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         ServiceOffer var3 = (ServiceOffer)var2.next();
         this.conHandler.conflictStart(var3);
      }

   }

   private void conflictStop(Collection var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         ServiceOffer var3 = (ServiceOffer)var2.next();
         this.conHandler.conflictStop(var3);
      }

   }
}
