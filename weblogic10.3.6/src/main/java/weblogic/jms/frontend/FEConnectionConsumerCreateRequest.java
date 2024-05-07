package weblogic.jms.frontend;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import javax.jms.ServerSessionPool;
import weblogic.jms.client.JMSServerSessionPool;
import weblogic.jms.common.DestinationImpl;
import weblogic.jms.common.DistributedDestinationImpl;
import weblogic.jms.common.JMSConnectionConsumerCreateResponse;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.dispatcher.Request;
import weblogic.messaging.dispatcher.Response;

public final class FEConnectionConsumerCreateRequest extends Request implements Externalizable {
   static final long serialVersionUID = 7143098392927142826L;
   private static final int EXTVERSION = 1;
   private static final int VERSION_MASK = 255;
   private static final int DURABLE_MASK = 256;
   private static final int SELECTOR_MASK = 512;
   private static final int PERMISSION_MASK = 1024;
   private static final int DISTRIBUTEDDESTINATION_MASK = 8192;
   private ServerSessionPool serverSessionPool;
   private DestinationImpl destination;
   private boolean durable;
   private String messageSelector;
   private int messagesMaximum;
   private boolean checkPermission;

   public FEConnectionConsumerCreateRequest(JMSID var1, ServerSessionPool var2, DestinationImpl var3, boolean var4, String var5, int var6, boolean var7) {
      super(var1, 1543);
      this.serverSessionPool = var2;
      this.destination = var3;
      this.durable = var4;
      this.messageSelector = var5;
      this.messagesMaximum = var6;
      this.checkPermission = var7;
   }

   public final ServerSessionPool getServerSessionPool() {
      return this.serverSessionPool;
   }

   public final DestinationImpl getDestination() {
      return this.destination;
   }

   public final boolean isDurable() {
      return this.durable;
   }

   public final String getMessageSelector() {
      return this.messageSelector;
   }

   public final int getMessagesMaximum() {
      return this.messagesMaximum;
   }

   public final boolean checkPermission() {
      return this.checkPermission;
   }

   public int remoteSignature() {
      return 18;
   }

   public Response createResponse() {
      return new JMSConnectionConsumerCreateResponse();
   }

   public FEConnectionConsumerCreateRequest() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1 = this.getVersionedStream(var1);
      int var2 = 1;
      if (this.durable) {
         var2 |= 256;
      }

      if (this.messageSelector != null) {
         var2 |= 512;
      }

      if (this.checkPermission) {
         var2 |= 1024;
      }

      if (this.destination instanceof DistributedDestinationImpl) {
         var2 |= 8192;
      }

      var1.writeInt(var2);
      super.writeExternal(var1);
      ((JMSServerSessionPool)this.serverSessionPool).writeExternal(var1);
      this.destination.writeExternal(var1);
      if (this.messageSelector != null) {
         var1.writeUTF(this.messageSelector);
      }

      var1.writeInt(this.messagesMaximum);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 != 1) {
         throw JMSUtilities.versionIOException(var3, 1, 1);
      } else {
         super.readExternal(var1);
         this.durable = (var2 & 256) != 0;
         this.checkPermission = (var2 & 1024) != 0;
         this.serverSessionPool = new JMSServerSessionPool();
         ((JMSServerSessionPool)this.serverSessionPool).readExternal(var1);
         if ((var2 & 8192) != 0) {
            this.destination = new DistributedDestinationImpl();
            this.destination.readExternal(var1);
         } else {
            this.destination = new DestinationImpl();
            this.destination.readExternal(var1);
         }

         if ((var2 & 512) != 0) {
            this.messageSelector = var1.readUTF();
         }

         this.messagesMaximum = var1.readInt();
      }
   }
}
