package weblogic.jms.adapter;

import javax.jms.ExceptionListener;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.resource.ResourceException;
import javax.resource.spi.IllegalStateException;
import javax.transaction.xa.XAResource;
import weblogic.jms.bridge.AdapterConnection;
import weblogic.jms.bridge.AdapterConnectionMetaData;
import weblogic.jms.bridge.GenericMessage;
import weblogic.jms.bridge.LocalTransaction;
import weblogic.jms.bridge.NotificationListener;
import weblogic.jms.bridge.SourceConnection;
import weblogic.jms.bridge.TargetConnection;

public class JMSConnectionHandle implements SourceConnection, TargetConnection {
   private JMSManagedConnection managedCon;

   public JMSConnectionHandle(JMSManagedConnection var1) {
      this.managedCon = var1;
   }

   public void start() throws ResourceException {
      AdapterConnection var1 = this.getJMSBaseConnection();
      var1.start();
   }

   public void close() throws ResourceException {
      if (this.managedCon != null) {
         if (!this.managedCon.isDestroyed()) {
            this.managedCon.sendEvent(5, (Exception)null, this);
            this.managedCon = null;
         }
      }
   }

   public void pause() throws ResourceException {
      AdapterConnection var1 = this.getJMSBaseConnection();
      var1.pause();
   }

   public void resume() throws ResourceException {
      AdapterConnection var1 = this.getJMSBaseConnection();
      var1.resume();
   }

   public boolean isClosed() {
      return this.managedCon == null;
   }

   public LocalTransaction getLocalTransaction() throws ResourceException {
      AdapterConnection var1 = this.getJMSBaseConnection();
      return var1.getLocalTransaction();
   }

   public void addNotificationListener(NotificationListener var1, int var2) throws ResourceException {
      AdapterConnection var3 = this.getJMSBaseConnection();
      var3.addNotificationListener(var1, var2);
   }

   public void removeNotificationListener(NotificationListener var1, int var2) throws ResourceException {
      AdapterConnection var3 = this.getJMSBaseConnection();
      var3.removeNotificationListener(var1, var2);
   }

   public void associateTransaction(Message var1) throws ResourceException {
      AdapterConnection var2 = this.getJMSBaseConnection();
      var2.associateTransaction(var1);
   }

   public void associateTransaction(GenericMessage var1) throws ResourceException {
      AdapterConnection var2 = this.getJMSBaseConnection();
      var2.associateTransaction(var1);
   }

   public Message receive() throws ResourceException {
      SourceConnection var1 = (SourceConnection)this.getJMSBaseConnection();
      return var1.receive();
   }

   public GenericMessage receiveGenericMessage() throws ResourceException {
      SourceConnection var1 = (SourceConnection)this.getJMSBaseConnection();
      return var1.receiveGenericMessage();
   }

   public Message receive(long var1) throws ResourceException {
      SourceConnection var3 = (SourceConnection)this.getJMSBaseConnection();
      return var3.receive(var1);
   }

   public GenericMessage receiveGenericMessage(long var1) throws ResourceException {
      SourceConnection var3 = (SourceConnection)this.getJMSBaseConnection();
      return var3.receiveGenericMessage(var1);
   }

   public void setMessageListener(MessageListener var1) throws ResourceException {
      SourceConnection var2 = (SourceConnection)this.getJMSBaseConnection();
      var2.setMessageListener(var1);
   }

   public void setExceptionListener(ExceptionListener var1) throws ResourceException {
      SourceConnection var2 = (SourceConnection)this.getJMSBaseConnection();
      var2.setExceptionListener(var1);
   }

   public void send(Message var1) throws ResourceException {
      TargetConnection var2 = (TargetConnection)this.getJMSBaseConnection();
      var2.send(var1);
   }

   public void send(GenericMessage var1) throws ResourceException {
      TargetConnection var2 = (TargetConnection)this.getJMSBaseConnection();
      var2.send(var1);
   }

   public Message createMessage(Message var1) throws ResourceException {
      TargetConnection var2 = (TargetConnection)this.getJMSBaseConnection();
      return var2.createMessage(var1);
   }

   public Message createMessage(GenericMessage var1) throws ResourceException {
      TargetConnection var2 = (TargetConnection)this.getJMSBaseConnection();
      return var2.createMessage(var1);
   }

   public GenericMessage createGenericMessage(Message var1) throws ResourceException {
      TargetConnection var2 = (TargetConnection)this.getJMSBaseConnection();
      return var2.createGenericMessage(var1);
   }

   public GenericMessage createGenericMessage(GenericMessage var1) throws ResourceException {
      TargetConnection var2 = (TargetConnection)this.getJMSBaseConnection();
      return var2.createGenericMessage(var1);
   }

   public XAResource getXAResource() throws ResourceException {
      AdapterConnection var1 = this.getJMSBaseConnection();
      return var1.getXAResource();
   }

   public boolean implementsMDBTransaction() throws ResourceException {
      SourceConnection var1 = (SourceConnection)this.getJMSBaseConnection();
      return ((JMSBaseConnection)var1).implementsMDBTransaction();
   }

   public void setAcknowledgeMode(int var1) throws ResourceException {
      SourceConnection var2 = (SourceConnection)this.getJMSBaseConnection();
      var2.setAcknowledgeMode(var1);
   }

   public void recover() throws ResourceException {
      SourceConnection var1 = (SourceConnection)this.getJMSBaseConnection();
      var1.recover();
   }

   public void acknowledge(Message var1) throws ResourceException {
      SourceConnection var2 = (SourceConnection)this.getJMSBaseConnection();
      var2.acknowledge(var1);
   }

   public AdapterConnectionMetaData getMetaData() throws ResourceException {
      SourceConnection var1 = (SourceConnection)this.getJMSBaseConnection();
      return var1.getMetaData();
   }

   void associateConnection(JMSManagedConnection var1) throws ResourceException {
      this.checkIfValid();
      this.managedCon.removeJMSConnectionHandle(this);
      var1.addJMSConnectionHandle(this);
      this.managedCon = var1;
   }

   void checkIfValid() throws ResourceException {
      if (this.managedCon == null) {
         throw new IllegalStateException("Connection is invalid");
      }
   }

   AdapterConnection getJMSBaseConnection() throws ResourceException {
      this.checkIfValid();
      return this.managedCon.getJMSBaseConnection();
   }

   void invalidate() {
      this.managedCon = null;
   }
}
