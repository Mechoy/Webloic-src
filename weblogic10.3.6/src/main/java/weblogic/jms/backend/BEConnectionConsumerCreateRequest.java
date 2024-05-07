package weblogic.jms.backend;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import javax.jms.ServerSessionPool;
import weblogic.jms.client.JMSServerSessionPool;
import weblogic.jms.common.JMSConnectionConsumerCreateResponse;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSServerId;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.dispatcher.Request;
import weblogic.jms.frontend.FEConnection;
import weblogic.messaging.dispatcher.Response;

public final class BEConnectionConsumerCreateRequest extends Request implements Externalizable {
   static final long serialVersionUID = 7853725251347530328L;
   private static final int EXTVERSION = 1;
   private static final int VERSION_MASK = 255;
   private static final int ISDURABLE_MASK = 256;
   private static final int SELECTOR_MASK = 512;
   private static final int STOPPED_MASK = 1024;
   private JMSServerId backEndId;
   private JMSID connectionId;
   private FEConnection connection;
   private JMSID destinationId;
   private ServerSessionPool serverSessionPool;
   private boolean isDurable;
   private String messageSelector;
   private int messagesMaximum;
   private boolean stopped;
   private long startStopSequenceNumber;

   public BEConnectionConsumerCreateRequest(JMSServerId var1, JMSID var2, ServerSessionPool var3, FEConnection var4, JMSID var5, boolean var6, String var7, int var8, boolean var9, long var10) {
      super((JMSID)null, 9218);
      this.backEndId = var1;
      this.connectionId = var2;
      this.serverSessionPool = var3;
      this.connection = var4;
      this.destinationId = var5;
      this.isDurable = var6;
      this.messageSelector = var7;
      this.messagesMaximum = var8;
      this.stopped = var9;
      this.startStopSequenceNumber = var10;
   }

   public final JMSServerId getBackEndId() {
      return this.backEndId;
   }

   public final JMSID getConnectionId() {
      return this.connectionId;
   }

   public final ServerSessionPool getServerSessionPool() {
      return this.serverSessionPool;
   }

   public final FEConnection getConnection() {
      return this.connection;
   }

   public final JMSID getDestinationId() {
      return this.destinationId;
   }

   public final boolean isDurable() {
      return this.isDurable;
   }

   public final String getMessageSelector() {
      return this.messageSelector;
   }

   public final int getMessagesMaximum() {
      return this.messagesMaximum;
   }

   public final boolean isStopped() {
      return this.stopped;
   }

   public final long getStartStopSequenceNumber() {
      return this.startStopSequenceNumber;
   }

   public int remoteSignature() {
      return 18;
   }

   public Response createResponse() {
      return new JMSConnectionConsumerCreateResponse();
   }

   public BEConnectionConsumerCreateRequest() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      int var2 = 1;
      if (this.isDurable) {
         var2 |= 256;
      }

      if (this.stopped) {
         var2 |= 1024;
      }

      if (this.messageSelector != null) {
         var2 |= 512;
      }

      var1.writeInt(var2);
      super.writeExternal(var1);
      this.backEndId.writeExternal(var1);
      this.connectionId.writeExternal(var1);
      ((JMSServerSessionPool)this.serverSessionPool).writeExternal(var1);
      this.destinationId.writeExternal(var1);
      if (this.messageSelector != null) {
         var1.writeUTF(this.messageSelector);
      }

      var1.writeInt(this.messagesMaximum);
      var1.writeLong(this.startStopSequenceNumber);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 != 1) {
         throw JMSUtilities.versionIOException(var3, 1, 1);
      } else {
         super.readExternal(var1);
         this.backEndId = new JMSServerId();
         this.backEndId.readExternal(var1);
         this.connectionId = new JMSID();
         this.connectionId.readExternal(var1);
         this.serverSessionPool = new JMSServerSessionPool();
         ((JMSServerSessionPool)this.serverSessionPool).readExternal(var1);
         this.destinationId = new JMSID();
         this.destinationId.readExternal(var1);
         if ((var2 & 512) != 0) {
            this.messageSelector = var1.readUTF();
         }

         this.messagesMaximum = var1.readInt();
         this.startStopSequenceNumber = var1.readLong();
         this.isDurable = (var2 & 256) != 0;
         this.stopped = (var2 & 1024) != 0;
      }
   }
}
