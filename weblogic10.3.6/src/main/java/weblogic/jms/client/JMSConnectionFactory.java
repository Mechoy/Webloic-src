package weblogic.jms.client;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.UnmarshalException;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.PeerInfoable;
import weblogic.jms.JMSClientExceptionLogger;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.dispatcher.DispatcherWrapper;
import weblogic.jms.dispatcher.InvocableManagerDelegate;
import weblogic.jms.dispatcher.JMSDispatcherManager;
import weblogic.jms.frontend.FEConnectionCreateRequest;
import weblogic.jms.frontend.FEConnectionFactoryRemote;
import weblogic.messaging.dispatcher.DispatcherException;
import weblogic.rmi.extensions.PortableRemoteObject;
import weblogic.rmi.extensions.server.RemoteWrapper;

public class JMSConnectionFactory implements QueueConnectionFactory, TopicConnectionFactory, Externalizable, RemoteWrapper, Reconnectable {
   static final long serialVersionUID = 2752718129231506407L;
   private static final byte EXTVERSION1 = 1;
   private static final byte EXTVERSION2 = 2;
   private static final byte EXTVERSION3 = 3;
   private static final byte INTERFACE_VERSION_PRE_81 = 0;
   private static final byte INTERFACE_VERSION_81 = 1;
   private static final byte INTERFACE_VERSION_CURRENT = 1;
   private FEConnectionFactoryRemote feConnectionFactoryRemote;
   private byte interfaceVersion = 1;
   private String fullyQualifiedName;
   private static long RECONNECT_TIMEOUT_DEFAULT = 60000L;
   private static long RECONNECT_PERIOD_DEFAULT;

   public JMSConnectionFactory() {
   }

   public JMSConnectionFactory(FEConnectionFactoryRemote var1, String var2) {
      this.feConnectionFactoryRemote = var1;
      this.fullyQualifiedName = var2;
   }

   public Reconnectable getReconnectState(int var1) throws CloneNotSupportedException {
      assert false;

      return null;
   }

   public boolean isClosed() {
      return false;
   }

   public void publicCheckClosed() {
   }

   public ReconnectController getReconnectController() {
      return null;
   }

   public boolean isReconnectControllerClosed() {
      assert false;

      return false;
   }

   public Reconnectable preCreateReplacement(Reconnectable var1) throws JMSException {
      assert false;

      return null;
   }

   public void postCreateReplacement() {
      assert false;

   }

   public void forgetReconnectState() {
      assert false;

   }

   public PeerInfo getFEPeerInfo() {
      assert false;

      return null;
   }

   public void close() {
      assert false;

   }

   public final QueueConnection createQueueConnection(String var1, String var2) throws JMSException {
      ConnectionInternal var3 = this.createConnectionInternal(var1, var2, false, 1);
      return var3;
   }

   public final QueueConnection createQueueConnection() throws JMSException {
      ConnectionInternal var1 = this.createConnectionInternal((String)null, (String)null, false, 1);
      return var1;
   }

   public final TopicConnection createTopicConnection(String var1, String var2) throws JMSException {
      ConnectionInternal var3 = this.createConnectionInternal(var1, var2, false, 2);
      return var3;
   }

   public final TopicConnection createTopicConnection() throws JMSException {
      ConnectionInternal var1 = this.createConnectionInternal((String)null, (String)null, false, 2);
      return var1;
   }

   public final Connection createConnection() throws JMSException {
      return this.createConnectionInternal((String)null, (String)null, false, 0);
   }

   public final Connection createConnection(String var1, String var2) throws JMSException {
      return this.createConnectionInternal(var1, var2, false, 0);
   }

   final JMSConnection setupJMSConnection(String var1, String var2, boolean var3, int var4) throws JMSException {
      DispatcherWrapper var6 = null;

      JMSConnection var5;
      try {
         JMSDispatcherManager.exportLocalDispatcher();
         var6 = JMSDispatcherManager.getLocalDispatcherWrapper();
         if (this.interfaceVersion >= 1) {
            FEConnectionCreateRequest var7 = new FEConnectionCreateRequest(var6, var1, var2, var3);
            var5 = this.feConnectionFactoryRemote.connectionCreateRequest(var7);
         } else if (var1 != null) {
            var5 = this.feConnectionFactoryRemote.connectionCreate(var6, var1, var2);
         } else {
            var5 = this.feConnectionFactoryRemote.connectionCreate(var6);
         }
      } catch (UnmarshalException var11) {
         try {
            if (var1 != null) {
               var5 = this.feConnectionFactoryRemote.connectionCreate(var6, var1, var2);
            } else {
               var5 = this.feConnectionFactoryRemote.connectionCreate(var6);
            }

            this.interfaceVersion = 0;
         } catch (RemoteException var10) {
            JMSDispatcherManager.unexportLocalDispatcher();
            throw new weblogic.jms.common.JMSException(JMSClientExceptionLogger.logErrorCreatingConnectionLoggable(var10), var10);
         }
      } catch (RemoteException var12) {
         JMSDispatcherManager.unexportLocalDispatcher();
         throw new weblogic.jms.common.JMSException(JMSClientExceptionLogger.logErrorCreatingConnectionLoggable(var12), var12);
      }

      try {
         var5.setupDispatcher();
      } catch (DispatcherException var9) {
         JMSDispatcherManager.unexportLocalDispatcher();
         throw new weblogic.jms.common.JMSException(JMSClientExceptionLogger.logErrorFindingDispatcherLoggable(var9), var9);
      }

      InvocableManagerDelegate.delegate.invocableAdd(3, var5);
      var5.setType(var4);
      var5.rememberCredentials(var1, var2, var3);
      return var5;
   }

   final ConnectionInternal createConnectionInternal(String var1, String var2, boolean var3, int var4) throws JMSException {
      JMSConnection var5 = this.setupJMSConnection(var1, var2, var3, var4);
      Object var6;
      if (var3) {
         var6 = new XAConnectionInternalImpl((JMSXAConnectionFactory)this, (JMSXAConnection)var5);
      } else {
         var6 = new WLConnectionImpl(this, var5);
      }

      var5.setWlConnectionImpl((WLConnectionImpl)var6);
      int var7;
      long var8;
      long var10;
      if (JMSConnection.isT3Client()) {
         var7 = var5.getReconnectPolicyInternal();
         var8 = var5.getReconnectBlockingMillisInternal();
         var10 = var5.getTotalReconnectPeriodMillisInternal();
      } else {
         var7 = 0;
         var8 = 0L;
         var10 = 0L;
      }

      ((WLConnectionImpl)var6).setReconnectPolicy(WLConnectionImpl.convertReconnectPolicy(var7));
      ((WLConnectionImpl)var6).setReconnectBlockingMillis(var8);
      ((WLConnectionImpl)var6).setTotalReconnectPeriodMillis(var10);
      return (ConnectionInternal)var6;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      PeerInfo var2 = null;
      if (var1 instanceof PeerInfoable) {
         var2 = ((PeerInfoable)var1).getPeerInfo();
      }

      byte var3 = 1;
      if (var2 != null && var2.compareTo(PeerInfo.VERSION_81) >= 0) {
         if (var2.compareTo(PeerInfo.VERSION_DIABLO) >= 0) {
            var3 = 3;
         } else {
            var3 = 2;
         }
      } else if (var2 == null) {
         var3 = 3;
      }

      var1.writeByte(var3);
      if (var3 >= 2) {
         var1.writeByte(1);
      }

      var1.writeObject(this.feConnectionFactoryRemote);
      if (var3 >= 3) {
         if (this.fullyQualifiedName == null) {
            var1.writeUTF("");
         } else {
            var1.writeUTF(this.fullyQualifiedName);
         }
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      byte var2 = var1.readByte();
      if (var2 >= 1 && var2 <= 3) {
         if (var2 >= 2) {
            this.interfaceVersion = var1.readByte();
         } else {
            this.interfaceVersion = 0;
         }

         this.feConnectionFactoryRemote = (FEConnectionFactoryRemote)PortableRemoteObject.narrow(var1.readObject(), FEConnectionFactoryRemote.class);
         if (var2 >= 3) {
            this.fullyQualifiedName = var1.readUTF();
            if (this.fullyQualifiedName.length() <= 0) {
               this.fullyQualifiedName = null;
            }
         } else {
            this.fullyQualifiedName = null;
         }

      } else {
         throw JMSUtilities.versionIOException(var2, 1, 3);
      }
   }

   public Remote getRemoteDelegate() {
      return this.feConnectionFactoryRemote;
   }

   public String getFullyQualifiedName() {
      return this.fullyQualifiedName == null ? "" : this.fullyQualifiedName;
   }

   static {
      String var0;
      long var1;
      try {
         var0 = System.getProperty("weblogic.jms.ReconnectBlockingMillis");
         if (var0 != null) {
            var1 = Long.parseLong(var0);
            WLConnectionImpl.validateReconnectMillis(var1);
            RECONNECT_TIMEOUT_DEFAULT = var1;
         }
      } catch (RuntimeException var4) {
         var4.printStackTrace();
      }

      RECONNECT_PERIOD_DEFAULT = -1L;

      try {
         var0 = System.getProperty("weblogic.jms.ReconnectPeriodMillis");
         if (var0 != null) {
            var1 = Long.parseLong(var0);
            WLConnectionImpl.validateReconnectMillis(var1);
            RECONNECT_PERIOD_DEFAULT = var1;
         }
      } catch (RuntimeException var3) {
         var3.printStackTrace();
      }

   }
}
