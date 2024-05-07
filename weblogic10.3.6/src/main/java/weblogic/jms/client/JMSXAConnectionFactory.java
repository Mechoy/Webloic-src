package weblogic.jms.client;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.rmi.Remote;
import javax.jms.JMSException;
import javax.jms.XAConnection;
import javax.jms.XAQueueConnection;
import javax.jms.XAQueueConnectionFactory;
import javax.jms.XATopicConnection;
import javax.jms.XATopicConnectionFactory;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.frontend.FEConnectionFactoryRemote;
import weblogic.rmi.extensions.server.RemoteWrapper;

public final class JMSXAConnectionFactory extends JMSConnectionFactory implements XAQueueConnectionFactory, XATopicConnectionFactory, Externalizable, RemoteWrapper {
   static final long serialVersionUID = 343051208017579157L;
   private static final byte EXTVERSION = 1;

   public JMSXAConnectionFactory(FEConnectionFactoryRemote var1, String var2) {
      super(var1, var2);
   }

   public XAQueueConnection createXAQueueConnection(String var1, String var2) throws JMSException {
      ConnectionInternal var3 = this.createConnectionInternal(var1, var2, true, 1);
      return (XAQueueConnection)var3;
   }

   public XAQueueConnection createXAQueueConnection() throws JMSException {
      ConnectionInternal var1 = this.createConnectionInternal((String)null, (String)null, true, 1);
      return (XAQueueConnection)var1;
   }

   public XAConnection createXAConnection(String var1, String var2) throws JMSException {
      return (XAConnection)this.createConnectionInternal(var1, var2, true, 0);
   }

   public XAConnection createXAConnection() throws JMSException {
      return (XAConnection)this.createConnectionInternal((String)null, (String)null, true, 0);
   }

   public XATopicConnection createXATopicConnection(String var1, String var2) throws JMSException {
      ConnectionInternal var3 = this.createConnectionInternal(var1, var2, true, 2);
      return (XATopicConnection)var3;
   }

   public XATopicConnection createXATopicConnection() throws JMSException {
      ConnectionInternal var1 = this.createConnectionInternal((String)null, (String)null, true, 2);
      return (XATopicConnection)var1;
   }

   public JMSXAConnectionFactory() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      super.writeExternal(var1);
      var1.writeByte(1);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      super.readExternal(var1);
      byte var2 = var1.readByte();
      if (var2 != 1) {
         throw JMSUtilities.versionIOException(var2, 1, 1);
      }
   }

   public Remote getRemoteDelegate() {
      return super.getRemoteDelegate();
   }
}
