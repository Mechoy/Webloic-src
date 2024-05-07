package weblogic.jms.client;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import javax.jms.ConnectionConsumer;
import javax.jms.JMSException;
import javax.jms.ServerSessionPool;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.dispatcher.InvocableManagerDelegate;
import weblogic.jms.frontend.FEConnectionConsumerCloseRequest;
import weblogic.messaging.dispatcher.Response;

public final class JMSConnectionConsumer implements ConnectionConsumer, Externalizable {
   static final long serialVersionUID = -4442173828993452834L;
   private static final byte EXTVERSION = 1;
   private JMSID connectionId;
   private ServerSessionPool serverSessionPool;
   private JMSID connectionConsumerId;

   public JMSConnectionConsumer(JMSID var1, ServerSessionPool var2, JMSID var3) {
      this.connectionId = var1;
      this.serverSessionPool = var2;
      this.connectionConsumerId = var3;
   }

   public JMSConnectionConsumer() {
   }

   public synchronized void close() throws JMSException {
      JMSConnection var1 = (JMSConnection)InvocableManagerDelegate.delegate.invocableFind(3, this.connectionId);
      Response var2 = var1.getFrontEndDispatcher().dispatchSync(new FEConnectionConsumerCloseRequest(this.connectionId, this.connectionConsumerId));
   }

   public ServerSessionPool getServerSessionPool() throws JMSException {
      return this.serverSessionPool;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeByte(1);
      this.connectionId.writeExternal(var1);
      ((JMSServerSessionPool)this.serverSessionPool).writeExternal(var1);
      this.connectionConsumerId.writeExternal(var1);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      byte var2 = var1.readByte();
      if (var2 != 1) {
         throw JMSUtilities.versionIOException(var2, 1, 1);
      } else {
         this.connectionId = new JMSID();
         this.connectionId.readExternal(var1);
         this.serverSessionPool = new JMSServerSessionPool();
         ((JMSServerSessionPool)this.serverSessionPool).readExternal(var1);
         this.connectionConsumerId = new JMSID();
         this.connectionConsumerId.readExternal(var1);
      }
   }
}
