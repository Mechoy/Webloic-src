package weblogic.cluster;

import java.io.IOException;
import weblogic.rmi.spi.HostID;

final class AttributeManager implements RecoverListener, MulticastSessionIDConstants {
   private static AttributeManager theAttributeManager = null;
   private final MemberAttributes localAttributes;
   private MulticastSession attributeSender = ClusterService.getClusterService().createMulticastSession(1, this, 1, false);

   static AttributeManager theOne() {
      return theAttributeManager;
   }

   static void initialize(MemberAttributes var0) throws IOException {
      theAttributeManager = new AttributeManager(var0);
   }

   private AttributeManager(MemberAttributes var1) {
      this.localAttributes = var1;
   }

   void sendAttributes() throws IOException {
      if (ClusterAnnouncementsDebugLogger.isDebugEnabled()) {
         ClusterAnnouncementsDebugLogger.debug("Sending local attributes " + this.localAttributes);
      }

      this.attributeSender.send(new AttributesMessage(this.localAttributes));
   }

   void receiveAttributes(HostID var1, AttributesMessage var2) {
      if (ClusterAnnouncementsDebugLogger.isDebugEnabled()) {
         ClusterAnnouncementsDebugLogger.debug("Received " + var2 + " from " + var1);
      }

      RemoteMemberInfo var3 = MemberManager.theOne().findOrCreate(var1);

      try {
         var3.processAttributes(var2.attributes);
      } finally {
         MemberManager.theOne().done(var3);
      }

   }

   public GroupMessage createRecoverMessage() {
      return new AttributesMessage(this.localAttributes);
   }

   MemberAttributes getLocalAttributes() {
      return this.localAttributes;
   }
}
