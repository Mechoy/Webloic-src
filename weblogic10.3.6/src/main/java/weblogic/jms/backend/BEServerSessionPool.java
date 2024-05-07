package weblogic.jms.backend;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.security.AccessController;
import java.util.HashMap;
import java.util.Iterator;
import javax.jms.Connection;
import javax.jms.ConnectionConsumer;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.ServerSession;
import javax.jms.ServerSessionPool;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.naming.Context;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import weblogic.descriptor.BeanUpdateRejectedException;
import weblogic.jms.JMSLogger;
import weblogic.jms.JMSService;
import weblogic.jms.client.ConnectionInternal;
import weblogic.jms.client.JMSConnectionFactory;
import weblogic.jms.client.JMSServerSessionPool;
import weblogic.jms.common.ConfigurationException;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.JMSDiagnosticImageSource;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSServerId;
import weblogic.jms.dispatcher.JMSDispatcherManager;
import weblogic.jms.extensions.ServerSessionPoolListener;
import weblogic.management.ManagementException;
import weblogic.management.configuration.JMSConnectionConsumerMBean;
import weblogic.management.configuration.JMSSessionPoolMBean;
import weblogic.management.runtime.JMSConsumerRuntimeMBean;
import weblogic.management.runtime.JMSServerRuntimeMBean;
import weblogic.management.runtime.JMSSessionPoolRuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.management.utils.GenericBeanListener;
import weblogic.messaging.common.PrivilegedActionUtilities;
import weblogic.messaging.dispatcher.DispatcherId;
import weblogic.messaging.runtime.DiagnosticImageTimeoutException;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public final class BEServerSessionPool extends RuntimeMBeanDelegate implements BEServerSessionPoolRemote, JMSSessionPoolRuntimeMBean {
   static final long serialVersionUID = -6384515684489802143L;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private JMSServerSessionPool remoteWrapper;
   private JMSID serverSessionPoolId;
   private BackEnd backEnd;
   private long serverSessionsMaximum;
   private int acknowledgeMode;
   private boolean transacted;
   private boolean createdFromMBean;
   private String listenerName;
   private Class listenerClass;
   private Constructor listenerConstructor;
   private Object[] listenerParameters;
   private Serializable clientData;
   private int waiters;
   private Connection connection;
   private ConnectionFactory connectionFactory;
   private BEServerSession firstServerSession;
   private long serverSessionsCurrent;
   private final HashMap connectionConsumers = new HashMap();
   private long connectionConsumersCurrentCount;
   private long connectionConsumersHighCount;
   private long connectionConsumersTotalCount;
   private JMSSessionPoolMBean mbean;
   private int state = 0;
   private boolean validConnectionFactory;
   private boolean validListener;
   private Throwable savedCreationThrowable;
   private static final HashMap sessionPoolSignatures = new HashMap();
   private static final HashMap sessionPoolAdditions = new HashMap();
   private GenericBeanListener sessionPoolListener;

   public BEServerSessionPool(String var1, JMSID var2, BackEnd var3, ConnectionFactory var4, int var5, int var6, boolean var7, String var8, Serializable var9) throws JMSException, ManagementException {
      super(var1, var3);
      this.serverSessionPoolId = var2;
      this.backEnd = var3;
      this.connectionFactory = var4;
      this.serverSessionsMaximum = (long)var5;
      this.acknowledgeMode = var6;
      this.transacted = var7;
      this.listenerName = var8;
      this.clientData = var9;
      this.initialize();
   }

   public BEServerSessionPool(String var1, JMSID var2, BackEnd var3, JMSSessionPoolMBean var4) throws JMSException, ManagementException {
      super(var1, var3);

      try {
         this.createdFromMBean = true;
         this.serverSessionPoolId = var2;
         this.backEnd = var3;
         this.mbean = var4;
         String var5;
         if ((var5 = var4.getConnectionFactory()) == null) {
            var5 = "weblogic.jms.ConnectionFactory";
         }

         this.setConnectionFactory(var5);
         this.setAcknowledgeMode(var4.getAcknowledgeMode());
         this.serverSessionsMaximum = (long)var4.getSessionsMaximum();
         if (this.serverSessionsMaximum == -1L) {
            this.serverSessionsMaximum = 10L;
         }

         this.transacted = var4.isTransacted();
         this.listenerName = var4.getListenerClass();
         this.initialize();
         JMSConnectionConsumerMBean[] var6 = var4.getJMSConnectionConsumers();

         for(int var7 = 0; var7 < var6.length; ++var7) {
            try {
               ConnectionConsumer var8 = this.connectionConsumerCreate(var6[var7]);
               this.connectionConsumerAdd(var6[var7].getName(), var8);
            } catch (ConfigurationException var9) {
               JMSLogger.logErrorCreateCC(var3.getName(), var6[var7].getName(), var9);
               throw var9;
            }
         }

         this.state = 1;
      } catch (Throwable var10) {
         this.cleanup();
         this.savedCreationThrowable = var10;
         JMSLogger.logAddedSessionPoolToBeRemoved(var3.getName(), var1, var10);
         if (JMSDebug.JMSConfig.isDebugEnabled()) {
            JMSDebug.JMSConfig.debug("Failed to start the new server session pool");
         }
      }

   }

   private void initialize() throws JMSException, ConfigurationException {
      this.setListener(this.listenerName);
      if (this.connectionFactory instanceof QueueConnectionFactory) {
         this.connection = ((QueueConnectionFactory)this.connectionFactory).createQueueConnection();
      } else if (this.connectionFactory instanceof TopicConnectionFactory) {
         this.connection = ((TopicConnectionFactory)this.connectionFactory).createTopicConnection();
      }

      this.remoteWrapper = new JMSServerSessionPool(this.backEnd.getJMSServerId(), this.serverSessionPoolId);
   }

   private void setListener(String var1) throws JMSException, ConfigurationException {
      try {
         label47: {
            if (var1 != null && var1.length() != 0) {
               this.listenerClass = Class.forName(var1);
               Constructor[] var2 = this.listenerClass.getConstructors();

               for(int var3 = 0; var3 < var2.length; ++var3) {
                  Class[] var4 = var2[var3].getParameterTypes();
                  if (var4.length == 0) {
                     this.listenerConstructor = var2[var3];
                     this.listenerParameters = new Object[0];
                  } else if (var4.length == 1 && var4[0].isAssignableFrom(Session.class)) {
                     this.listenerConstructor = var2[var3];
                     this.listenerParameters = new Object[1];
                     break;
                  }
               }

               if (this.listenerConstructor != null) {
                  break label47;
               }

               throw new weblogic.jms.common.JMSException("No constructor for MessageListener", new NoSuchMethodException("expected one of\n\t" + var1 + ".<init>(), or\n\t" + var1 + ".<init>(javax.jms.Session)"));
            }

            throw new ConfigurationException("Listener class for ServerSessionPool " + this.name + " is null");
         }
      } catch (ClassNotFoundException var5) {
         throw new ConfigurationException("Listener class, " + var1 + ", not found");
      }

      this.validListener = true;
   }

   private void setConnectionFactory(String var1) throws ConfigurationException {
      try {
         Context var2 = JMSService.getContext();
         this.connectionFactory = (ConnectionFactory)var2.lookup(var1);
      } catch (Throwable var3) {
         throw new ConfigurationException("ConnectionFactory " + var1 + " for ServerSessionPool " + this.name + " not found", var3);
      }

      if (this.connectionFactory instanceof JMSConnectionFactory) {
      }

      this.validConnectionFactory = true;
   }

   private void setAcknowledgeMode(String var1) throws ConfigurationException {
      if (var1 == null) {
         this.acknowledgeMode = 1;
      } else if (var1.equalsIgnoreCase("Auto")) {
         this.acknowledgeMode = 1;
      } else if (var1.equalsIgnoreCase("Client")) {
         this.acknowledgeMode = 2;
      } else if (var1.equalsIgnoreCase("Dups-Ok")) {
         this.acknowledgeMode = 3;
      } else {
         if (!var1.equalsIgnoreCase("None")) {
            throw new ConfigurationException("Invalid acknowledgeMode for ServerSessionPool " + this.name + ", " + var1);
         }

         this.acknowledgeMode = 4;
      }

   }

   void start() throws JMSException {
      this.checkShutdown();
      synchronized(this) {
         if ((this.state & 4) == 0) {
            if ((this.state & 1) != 0) {
               this.sessionPoolListener = new GenericBeanListener(this.mbean, this, sessionPoolSignatures, sessionPoolAdditions);
            }

            Iterator var2 = this.connectionConsumers.values().iterator();

            while(var2.hasNext()) {
               BEConnectionConsumerCommon var3 = (BEConnectionConsumerCommon)var2.next();
               var3.start();
            }

            this.state = 4;
         }
      }
   }

   void cleanup() {
      try {
         if (this.connection != null) {
            this.connection.close();
         }
      } catch (JMSException var2) {
      }

   }

   void shutdown() {
      Iterator var1 = null;
      BEServerSession var2 = null;
      synchronized(this) {
         if ((this.state & 16) != 0) {
            return;
         }

         if ((this.state & 8) == 0) {
            this.markShuttingDown();
         }

         var1 = ((HashMap)this.connectionConsumers.clone()).values().iterator();
         var2 = this.firstServerSession;
         this.firstServerSession = null;
      }

      if (this.sessionPoolListener != null) {
         this.sessionPoolListener.close();
      }

      try {
         PrivilegedActionUtilities.unregister(this, kernelId);
         if (this.savedCreationThrowable != null) {
            this.state = 16;
            return;
         }
      } catch (ManagementException var10) {
      }

      while(var1.hasNext()) {
         BEConnectionConsumerCommon var3 = (BEConnectionConsumerCommon)var1.next();

         try {
            var3.close();
         } catch (JMSException var9) {
         }
      }

      BEServerSession var12;
      for(; var2 != null; var2 = var12) {
         var12 = var2.getNext();

         try {
            var2.close();
         } catch (JMSException var8) {
         }
      }

      try {
         this.connection.close();
      } catch (JMSException var7) {
      }

      synchronized(this) {
         this.state = 16;
         this.connectionConsumers.clear();
         this.connectionConsumersCurrentCount = 0L;
         this.connectionConsumersHighCount = 0L;
         this.connectionConsumersTotalCount = 0L;
      }
   }

   private boolean isShutdown() {
      return (this.state & 24) != 0;
   }

   private synchronized void checkShutdown() throws JMSException {
      if (this.isShutdown()) {
         throw new weblogic.jms.common.JMSException("JMS server session pool is shutdown");
      }
   }

   synchronized void markShuttingDown() {
      if ((this.state & 16) == 0) {
         this.state = 8;
      }
   }

   public JMSID getId() {
      return this.serverSessionPoolId;
   }

   public JMSServerId getBackEndId() {
      return this.backEnd.getJMSServerId();
   }

   public BackEnd getBackEnd() {
      return this.backEnd;
   }

   public Object getRemoteWrapper() {
      return this.remoteWrapper;
   }

   public int getAcknowledgeMode() {
      return this.acknowledgeMode;
   }

   public boolean isTransacted() {
      return this.transacted;
   }

   public long getServerSessionsMaximum() {
      return this.serverSessionsMaximum;
   }

   public String getListenerName() {
      return this.listenerName;
   }

   boolean isCreatedFromMBean() {
      return this.createdFromMBean;
   }

   private ConnectionConsumer connectionConsumerCreate(JMSConnectionConsumerMBean var1) throws JMSException, ConfigurationException {
      String var3 = var1.getName();
      String var4 = var1.getDestination();
      if (var4 == null) {
         throw new ConfigurationException("Null destination for ConnectionConsumer " + var3);
      } else {
         Destination var2;
         try {
            var2 = (Destination)JMSService.getContext().lookup(var4);
         } catch (Exception var8) {
            throw new ConfigurationException("Error finding destination " + var4 + " for ConnectionConsumer " + var3, var8);
         }

         if (var2 == null) {
            throw new ConfigurationException("Destination " + var4 + " for ConnectionConsumer " + var3 + " not found");
         } else {
            String var5 = var1.getSelector();
            int var6 = var1.getMessagesMaximum();
            if (var6 <= 0) {
               var6 = 10;
            }

            ConnectionConsumer var7;
            if (var2 instanceof Queue) {
               var7 = ((ConnectionInternal)this.connection).createConnectionConsumer((Queue)var2, var5, (ServerSessionPool)this.getRemoteWrapper(), var6);
               if (var7 instanceof BEConnectionConsumerCommon) {
                  ((BEConnectionConsumerCommon)var7).initialize(var1);
               }

               return var7;
            } else if (var2 instanceof Topic) {
               var7 = ((ConnectionInternal)this.connection).createConnectionConsumer((Topic)var2, var5, (ServerSessionPool)this.getRemoteWrapper(), var6);
               if (var7 instanceof BEConnectionConsumerCommon) {
                  ((BEConnectionConsumerCommon)var7).initialize(var1);
               }

               return var7;
            } else {
               throw new ConfigurationException("Invalid destination type for ConnectionConsumer " + var3);
            }
         }
      }
   }

   private synchronized void connectionConsumerAdd(String var1, ConnectionConsumer var2) throws JMSException {
      this.checkShutdown();
      if (this.connectionConsumers.put(var1, var2) == null) {
         if (++this.connectionConsumersCurrentCount > this.connectionConsumersHighCount) {
            this.connectionConsumersHighCount = this.connectionConsumersCurrentCount;
         }

         ++this.connectionConsumersTotalCount;
      }

   }

   private synchronized ConnectionConsumer connectionConsumerRemove(String var1) throws JMSException {
      ConnectionConsumer var2 = (ConnectionConsumer)this.connectionConsumers.remove(var1);
      if (var2 == null) {
         throw new weblogic.jms.common.JMSException("ConnectionConsumer not found");
      } else {
         --this.connectionConsumersCurrentCount;
         return var2;
      }
   }

   private synchronized BEServerSession serverSessionCreate() throws JMSException {
      Object var1;
      if (this.connection instanceof QueueConnection) {
         var1 = ((QueueConnection)this.connection).createQueueSession(this.transacted, this.acknowledgeMode);
      } else {
         var1 = ((TopicConnection)this.connection).createTopicSession(this.transacted, this.acknowledgeMode);
      }

      if (this.listenerParameters.length > 0) {
         this.listenerParameters[0] = var1;
      }

      try {
         MessageListener var2 = (MessageListener)this.listenerConstructor.newInstance(this.listenerParameters);
         if (var2 instanceof ServerSessionPoolListener) {
            ((ServerSessionPoolListener)var2).initialize(this.clientData);
         }

         ((Session)var1).setMessageListener(var2);
      } catch (Exception var3) {
         throw new weblogic.jms.common.JMSException("Error instantiating message listener", var3);
      }

      return new BEServerSession(this.connection, (Session)var1, this);
   }

   synchronized void serverSessionPut(BEServerSession var1) {
      if (this.isShutdown()) {
         try {
            var1.close();
         } catch (JMSException var3) {
         }
      } else {
         var1.setNext(this.firstServerSession);
         this.firstServerSession = var1;
      }

      if (this.waiters > 0) {
         this.notify();
      }

   }

   public synchronized ServerSession getServerSession(DispatcherId var1) throws JMSException {
      if (!JMSDispatcherManager.getLocalDispatcher().getId().equals(var1)) {
         throw new weblogic.jms.common.JMSException("Cannot invoke getServerSession() remotely");
      } else {
         BEServerSession var2;
         while((var2 = this.firstServerSession) == null) {
            this.checkShutdown();
            if (this.serverSessionsCurrent >= this.serverSessionsMaximum) {
               try {
                  ++this.waiters;
                  this.wait();
               } catch (InterruptedException var8) {
               } finally {
                  --this.waiters;
               }
            } else {
               this.firstServerSession = this.serverSessionCreate();
               ++this.serverSessionsCurrent;
            }
         }

         this.firstServerSession = var2.getNext();
         return var2;
      }
   }

   public JMSServerRuntimeMBean getJMSServer() {
      return this.backEnd;
   }

   public synchronized JMSConsumerRuntimeMBean[] getConnectionConsumers() {
      return null;
   }

   public synchronized long getConnectionConsumersCurrentCount() {
      return this.connectionConsumersCurrentCount;
   }

   public synchronized long getConnectionConsumersHighCount() {
      return this.connectionConsumersHighCount;
   }

   public synchronized long getConnectionConsumersTotalCount() {
      return this.connectionConsumersTotalCount;
   }

   public void setSessionsMaximum(int var1) {
      this.serverSessionsMaximum = (long)var1;
   }

   public void startAddConnectionConsumers(JMSConnectionConsumerMBean var1) throws BeanUpdateRejectedException {
      try {
         ConnectionConsumer var2 = this.connectionConsumerCreate(var1);
         this.connectionConsumerAdd(var1.getName(), var2);
      } catch (JMSException var3) {
         throw new BeanUpdateRejectedException(var3.getMessage(), var3);
      }
   }

   public void finishAddConnectionConsumers(JMSConnectionConsumerMBean var1, boolean var2) {
      if (!var2) {
         try {
            ConnectionConsumer var3 = this.connectionConsumerRemove(var1.getName());
            var3.close();
         } catch (JMSException var5) {
            JMSLogger.logErrorRollingBackConnectionConsumer(var1.getName(), var5.toString());
         }

      }
   }

   public void startRemoveConnectionConsumers(JMSConnectionConsumerMBean var1) throws BeanUpdateRejectedException {
   }

   public void finishRemoveConnectionConsumers(JMSConnectionConsumerMBean var1, boolean var2) {
      if (var2) {
         try {
            ConnectionConsumer var3 = this.connectionConsumerRemove(var1.getName());
            var3.close();
         } catch (JMSException var5) {
            JMSLogger.logErrorRemovingConnectionConsumer(var1.getName(), var5.toString());
         }

      }
   }

   public void close() throws JMSException {
   }

   public void dump(JMSDiagnosticImageSource var1, XMLStreamWriter var2) throws XMLStreamException, DiagnosticImageTimeoutException {
      var1.checkTimeout();
      var2.writeStartElement("ServerSessionPool");
      var2.writeAttribute("id", this.serverSessionPoolId != null ? this.serverSessionPoolId.toString() : "");
      var2.writeAttribute("serverSessionsMaximum", String.valueOf(this.serverSessionsMaximum));
      var2.writeAttribute("acknowledgeMode", String.valueOf(this.acknowledgeMode));
      var2.writeAttribute("transacted", String.valueOf(this.transacted));
      var2.writeAttribute("listenerName", this.listenerName != null ? this.listenerName : "");
      var2.writeAttribute("listenerClassName", this.listenerClass != null ? this.listenerClass.getName() : "");
      var2.writeAttribute("state", JMSService.getStateName(this.state));
      var2.writeAttribute("serverSessionsCurrentCount", String.valueOf(this.serverSessionsCurrent));
      var2.writeAttribute("connectionConsumersCurrentCount", String.valueOf(this.connectionConsumersCurrentCount));
      var2.writeAttribute("connectionConsumersHighCount", String.valueOf(this.connectionConsumersHighCount));
      var2.writeAttribute("connectionConsumersTotalCount", String.valueOf(this.connectionConsumersTotalCount));
      var2.writeStartElement("ConnectionConsumers");
      HashMap var3 = (HashMap)this.connectionConsumers.clone();
      Iterator var4 = var3.values().iterator();

      while(var4.hasNext()) {
         BEConnectionConsumerImpl var5 = (BEConnectionConsumerImpl)var4.next();
         var5.dumpRef(var1, var2);
      }

      var2.writeEndElement();
      var2.writeEndElement();
   }

   static {
      sessionPoolSignatures.put("SessionsMaximum", Integer.TYPE);
      sessionPoolAdditions.put("ConnectionConsumers", JMSConnectionConsumerMBean.class);
   }
}
