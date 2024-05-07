package weblogic.jms.backend;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.jms.ServerSessionPool;
import javax.jms.TopicConnection;
import weblogic.jms.client.ConnectionInternal;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSServerId;
import weblogic.jms.common.JMSServerSessionPoolCreateResponse;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.dispatcher.JMSDispatcher;
import weblogic.jms.dispatcher.JMSDispatcherManager;
import weblogic.jms.extensions.ServerSessionPoolFactory;
import weblogic.jms.frontend.FEServerSessionPoolCreateRequest;
import weblogic.messaging.dispatcher.DispatcherException;
import weblogic.messaging.dispatcher.Response;

public final class BEServerSessionPoolFactory implements ServerSessionPoolFactory, Externalizable {
   private static final byte EXTVERSION = 1;
   static final long serialVersionUID = -5270077330309148361L;
   private JMSServerId backEndId;

   public BEServerSessionPoolFactory(JMSServerId var1) {
      this.backEndId = var1;
   }

   public ServerSessionPool getServerSessionPool(QueueConnection var1, int var2, boolean var3, int var4, String var5) throws JMSException {
      return this.createServerSessionPool(var1, var2, var4, var3, var5, (Serializable)null);
   }

   public ServerSessionPool getServerSessionPool(TopicConnection var1, int var2, boolean var3, int var4, String var5) throws JMSException {
      return this.createServerSessionPool(var1, var2, var4, var3, var5, (Serializable)null);
   }

   public ServerSessionPool createServerSessionPool(Connection var1, int var2, int var3, boolean var4, String var5, Serializable var6) throws JMSException {
      if (!(var1 instanceof ConnectionInternal)) {
         throw new weblogic.jms.common.JMSException("Connection is foreign");
      } else {
         JMSID var7 = ((ConnectionInternal)var1).getJMSID();
         if (var7.getTimestamp() == this.backEndId.getTimestamp() && var7.getSeed() == this.backEndId.getSeed()) {
            Response var8;
            try {
               JMSDispatcher var9 = ((ConnectionInternal)var1).getFrontEndDispatcher();
               if (!var9.isLocal()) {
                  var9 = JMSDispatcherManager.dispatcherFindOrCreate(var9.getId());
               }

               var8 = var9.dispatchSync(new FEServerSessionPoolCreateRequest(var7, this.backEndId, var2, var3, var4, var5, var6));
            } catch (DispatcherException var10) {
               throw new weblogic.jms.common.JMSException("Error creating server session pool", var10);
            }

            return ((JMSServerSessionPoolCreateResponse)var8).getServerSessionPool();
         } else {
            throw new weblogic.jms.common.JMSException("Connection is remote");
         }
      }
   }

   public BEServerSessionPoolFactory() {
   }

   public void readExternal(ObjectInput var1) throws ClassNotFoundException, IOException {
      byte var2 = var1.readByte();
      if (var2 != 1) {
         throw JMSUtilities.versionIOException(var2, 1, 1);
      } else {
         this.backEndId = new JMSServerId();
         this.backEndId.readExternal(var1);
      }
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeByte(1);
      this.backEndId.writeExternal(var1);
   }
}
