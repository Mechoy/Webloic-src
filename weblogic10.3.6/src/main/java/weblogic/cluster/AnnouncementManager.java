package weblogic.cluster;

import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Iterator;
import weblogic.protocol.LocalServerIdentity;
import weblogic.rmi.spi.HostID;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;

public final class AnnouncementManager implements RecoverListener, MulticastSessionIDConstants {
   private static AnnouncementManager theAnnouncementManager = null;
   private MemberServices localServices;
   private MulticastSession announcementSender;
   private boolean blocked = true;
   private ArrayList blockedItems = new ArrayList();
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public static AnnouncementManager theOne() {
      return theAnnouncementManager;
   }

   static void initialize(long var0) {
      theAnnouncementManager = new AnnouncementManager(var0);
   }

   private AnnouncementManager(long var1) {
      TreeManager.initialize(var1);
      this.localServices = new MemberServices(LocalServerIdentity.getIdentity());
      this.announcementSender = ClusterService.getClusterService().createMulticastSession(2, this, -1, true);
   }

   synchronized void unblockAnnouncements() {
      this.blocked = false;
      this.announcementSender = ClusterService.getClusterService().createMulticastSession(2, this, -1, true);
      if (!this.blockedItems.isEmpty()) {
         this.sendAnnouncement(this.blockedItems);
         this.blockedItems = null;
      }

   }

   synchronized void blockAnnouncements() {
      this.blocked = true;
      this.announcementSender = null;
      this.blockedItems = new ArrayList();
   }

   synchronized void shutdown() {
      this.localServices.retractAllOffers(true);
      this.blocked = true;
   }

   public synchronized void announce(ServiceRetract var1, ServiceOffer var2) {
      if (var1 != null) {
         this.localServices.processRetract(var1, true);
      }

      if (var2 != null) {
         this.localServices.processOffer(var2, true);
      }

      if (this.blocked) {
         if (var1 != null) {
            int var3 = var1.id();
            ServiceOffer var4 = null;
            Iterator var5 = this.blockedItems.iterator();

            while(var5.hasNext()) {
               Object var6 = var5.next();
               if (var6 instanceof ServiceOffer) {
                  ServiceOffer var7 = (ServiceOffer)var6;
                  if (var7.id() == var3) {
                     var4 = var7;
                     break;
                  }
               }
            }

            if (var4 != null && !var1.ignoreRetract()) {
               this.blockedItems.remove(var4);
            } else {
               this.blockedItems.add(var1);
            }
         }

         if (var2 != null) {
            this.blockedItems.add(var2);
         }
      } else {
         ArrayList var8 = new ArrayList();
         if (var1 != null) {
            var8.add(var1);
         }

         if (var2 != null) {
            var8.add(var2);
         }

         this.sendAnnouncement(var8);
      }

   }

   private void sendAnnouncement(ArrayList var1) {
      AnnouncementMessage var2 = new AnnouncementMessage(var1);
      if (ClusterAnnouncementsDebugLogger.isDebugEnabled()) {
         ClusterAnnouncementsDebugLogger.debug("Sending " + var2);
      }

      try {
         this.announcementSender.send(var2);
      } catch (IOException var4) {
         ClusterLogger.logMulticastSendError(var4);
      }

   }

   void receiveAnnouncement(final HostID var1, final AnnouncementMessage var2) {
      SecurityServiceManager.runAs(kernelId, kernelId, new PrivilegedAction() {
         public Object run() {
            if (ClusterAnnouncementsDebugLogger.isDebugEnabled()) {
               ClusterAnnouncementsDebugLogger.debug("Received " + var2 + " from " + var1);
            }

            RemoteMemberInfo var1x = MemberManager.theOne().findOrCreate(var1);

            try {
               var1x.processAnnouncement(var2.items);
            } finally {
               MemberManager.theOne().done(var1x);
            }

            return null;
         }
      });
   }

   public synchronized GroupMessage createRecoverMessage() {
      StateDumpMessage var1 = new StateDumpMessage(this.localServices.getAllOffers(), 2, this.getCurrentSeqNum());
      if (ClusterAnnouncementsDebugLogger.isDebugEnabled()) {
         ClusterAnnouncementsDebugLogger.debug("Sending " + var1);
      }

      return var1;
   }

   long getCurrentSeqNum() {
      return MulticastManager.theOne().findSender(2).getCurrentSeqNum();
   }

   public synchronized MemberServices getLocalOffers() {
      return this.localServices;
   }

   void receiveStateDump(final HostID var1, final StateDumpMessage var2) {
      SecurityServiceManager.runAs(kernelId, kernelId, new PrivilegedAction() {
         public Object run() {
            if (ClusterAnnouncementsDebugLogger.isDebugEnabled()) {
               ClusterAnnouncementsDebugLogger.debug("Received " + var2 + " from " + var1);
            }

            RemoteMemberInfo var1x = MemberManager.theOne().findOrCreate(var1);

            try {
               var1x.processStateDump(var2.offers, var2.senderNum, var2.currentSeqNum);
            } finally {
               MemberManager.theOne().done(var1x);
            }

            return null;
         }
      });
   }

   public boolean isBlocked() {
      return this.blocked;
   }
}
