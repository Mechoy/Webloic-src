package weblogic.jms.backend;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import weblogic.jms.client.JMSConnectionFactory;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSServerSessionPoolCreateResponse;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.dispatcher.Request;
import weblogic.messaging.dispatcher.Response;

public final class BEServerSessionPoolCreateRequest extends Request implements Externalizable {
   static final long serialVersionUID = 4148932257093406234L;
   private static final int EXTVERSION = 1;
   private static final int VERSION_MASK = 255;
   private static final int TRANSACTED_MASK = 256;
   private JMSConnectionFactory connectionFactory;
   private int sessionsMaximum;
   private int acknowledgeMode;
   private boolean transacted;
   private String messageListenerClass;
   private Serializable clientData;

   public BEServerSessionPoolCreateRequest(JMSID var1, JMSConnectionFactory var2, int var3, int var4, boolean var5, String var6, Serializable var7) {
      super(var1, 12814);
      this.connectionFactory = var2;
      this.sessionsMaximum = var3;
      this.acknowledgeMode = var4;
      this.transacted = var5;
      this.messageListenerClass = var6;
      this.clientData = var7;
   }

   public final JMSConnectionFactory getConnectionFactory() {
      return this.connectionFactory;
   }

   public final int getSessionsMaximum() {
      return this.sessionsMaximum;
   }

   public final int getAcknowledgeMode() {
      return this.acknowledgeMode;
   }

   public final boolean isTransacted() {
      return this.transacted;
   }

   public final String getMessageListenerClass() {
      return this.messageListenerClass;
   }

   public final Serializable getClientData() {
      return this.clientData;
   }

   public int remoteSignature() {
      return 18;
   }

   public Response createResponse() {
      return new JMSServerSessionPoolCreateResponse();
   }

   public BEServerSessionPoolCreateRequest() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      int var2 = 1;
      if (this.transacted) {
         var2 |= 256;
      }

      var1.writeInt(var2);
      super.writeExternal(var1);
      this.connectionFactory.writeExternal(var1);
      var1.writeInt(this.sessionsMaximum);
      var1.writeInt(this.acknowledgeMode);
      var1.writeUTF(this.messageListenerClass);
      var1.writeObject(this.clientData);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 != 1) {
         throw JMSUtilities.versionIOException(var3, 1, 1);
      } else {
         super.readExternal(var1);
         this.connectionFactory = new JMSConnectionFactory();
         this.connectionFactory.readExternal(var1);
         this.sessionsMaximum = var1.readInt();
         this.messageListenerClass = var1.readUTF();
         this.clientData = (Serializable)var1.readObject();
         this.transacted = (var2 & 256) != 0;
      }
   }
}
