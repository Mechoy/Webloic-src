package weblogic.jms.client;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import javax.jms.JMSException;
import javax.jms.ServerSession;
import javax.jms.ServerSessionPool;
import weblogic.jms.backend.BEServerSessionGetRequest;
import weblogic.jms.backend.BEServerSessionGetResponse;
import weblogic.jms.backend.BEServerSessionPoolCloseRequest;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSServerId;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.dispatcher.JMSDispatcherManager;
import weblogic.messaging.dispatcher.DispatcherException;
import weblogic.messaging.dispatcher.Response;

public final class JMSServerSessionPool implements ServerSessionPool, Externalizable {
   static final long serialVersionUID = -6985998084744986160L;
   private static final byte EXTVERSION = 1;
   private boolean isLocal;
   private JMSServerId backEndId;
   private JMSID serverSessionPoolId;

   public JMSServerSessionPool(JMSServerId var1, JMSID var2) {
      this.backEndId = var1;
      this.serverSessionPoolId = var2;
   }

   public synchronized void close() throws JMSException {
      try {
         Response var1 = JMSDispatcherManager.dispatcherFindOrCreate(this.backEndId.getDispatcherId()).dispatchSync(new BEServerSessionPoolCloseRequest(this.backEndId.getId(), this.serverSessionPoolId));
      } catch (DispatcherException var3) {
         throw new weblogic.jms.common.JMSException("Error closing server session pool", var3);
      }
   }

   public ServerSession getServerSession() throws JMSException {
      Response var1;
      try {
         var1 = JMSDispatcherManager.dispatcherFindOrCreate(this.backEndId.getDispatcherId()).dispatchSync(new BEServerSessionGetRequest(this.backEndId.getId(), this.serverSessionPoolId));
      } catch (DispatcherException var3) {
         throw new weblogic.jms.common.JMSException("Error getting server session", var3);
      }

      return ((BEServerSessionGetResponse)var1).getServerSession();
   }

   public JMSID getServerSessionPoolId() {
      return this.serverSessionPoolId;
   }

   public JMSServerId getBackEndId() {
      return this.backEndId;
   }

   public JMSServerSessionPool() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeByte(1);
      if (this.isLocal) {
         var1.writeBoolean(true);
      } else {
         var1.writeBoolean(false);
      }

      this.backEndId.writeExternal(var1);
      this.serverSessionPoolId.writeExternal(var1);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      byte var2 = var1.readByte();
      if (var2 != 1) {
         throw JMSUtilities.versionIOException(var2, 1, 1);
      } else {
         this.isLocal = var1.readBoolean();
         this.backEndId = new JMSServerId();
         this.backEndId.readExternal(var1);
         this.serverSessionPoolId = new JMSID();
         this.serverSessionPoolId.readExternal(var1);
      }
   }
}
