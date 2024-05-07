package weblogic.cluster;

import java.util.ArrayList;
import java.util.Iterator;
import weblogic.rmi.spi.HostID;

final class MemberServices {
   private HostID memberID;
   private ArrayList allOffers;

   MemberServices(HostID var1) {
      this.memberID = var1;
      this.allOffers = new ArrayList();
   }

   ArrayList getAllOffers() {
      return this.allOffers;
   }

   void retractAllOffers(boolean var1) {
      synchronized(this.allOffers) {
         Iterator var3 = this.allOffers.iterator();

         while(var3.hasNext()) {
            ServiceOffer var4 = (ServiceOffer)var3.next();
            TreeManager.theOne().retract(var4, var1);
         }

         this.allOffers.clear();
      }
   }

   void processRetract(ServiceRetract var1, boolean var2) {
      synchronized(this.allOffers) {
         Iterator var4 = this.allOffers.iterator();

         ServiceOffer var5;
         do {
            if (!var4.hasNext()) {
               return;
            }

            var5 = (ServiceOffer)var4.next();
         } while(var5.id() != var1.id());

         var4.remove();
         TreeManager.theOne().retract(var5, var2);
      }
   }

   void processOffer(ServiceOffer var1, boolean var2) {
      synchronized(this.allOffers) {
         var1.setServer(this.memberID);
         TreeManager.theOne().install(var1, var2);
         this.allOffers.add(var1);
      }
   }

   void processUpdate(ServiceOffer var1, boolean var2, int var3) {
      synchronized(this.allOffers) {
         Iterator var5 = this.allOffers.iterator();

         while(var5.hasNext()) {
            ServiceOffer var6 = (ServiceOffer)var5.next();
            if (var6.id() == var1.getOldID()) {
               var5.remove();
               break;
            }
         }

         var1.setServer(this.memberID);
         TreeManager.theOne().update(var1, var2);
         this.allOffers.add(var1);
      }
   }
}
