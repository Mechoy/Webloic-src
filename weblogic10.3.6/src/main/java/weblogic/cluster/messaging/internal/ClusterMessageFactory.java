package weblogic.cluster.messaging.internal;

import java.util.ArrayList;
import java.util.Iterator;

public class ClusterMessageFactory {
   private final ArrayList messageReceivers = new ArrayList();
   private ClusterMessageSenderWrapper defaultSender;

   public static ClusterMessageFactory getInstance() {
      return ClusterMessageFactory.Factory.THE_ONE;
   }

   public synchronized ClusterMessageSender getDefaultMessageSender() {
      if (this.defaultSender == null) {
         this.defaultSender = new ClusterMessageSenderWrapper(RMIClusterMessageSenderImpl.getInstance());
      }

      return this.defaultSender;
   }

   public ClusterMessageSender getOneWayMessageSender() {
      return RMIClusterMessageSenderImpl.getOneWay();
   }

   public synchronized void registerReceiver(ClusterMessageReceiver var1) {
      this.messageReceivers.add(var1);
   }

   public synchronized ClusterMessageReceiver getMessageReceiver(ClusterMessage var1) {
      Iterator var2 = this.messageReceivers.iterator();

      ClusterMessageReceiver var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (ClusterMessageReceiver)var2.next();
      } while(!var3.accept(var1));

      return var3;
   }

   public void registerMessageDeliveryFailureListener(MessageDeliveryFailureListener var1) {
      this.getDefaultMessageSender();
      this.defaultSender.addMessageDeliveryFailureListener(var1);
   }

   public void removeMessageDeliveryFailureListener(MessageDeliveryFailureListener var1) {
      this.getDefaultMessageSender();
      this.defaultSender.removeMessageDeliveryFailureListener(var1);
   }

   private static final class Factory {
      static final ClusterMessageFactory THE_ONE = new ClusterMessageFactory();
   }
}
