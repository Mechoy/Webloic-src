package weblogic.jms.saf;

import java.io.IOException;
import java.security.AccessController;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.jms.JMSException;
import weblogic.application.ModuleException;
import weblogic.j2ee.descriptor.wl.SAFRemoteContextBean;
import weblogic.jms.JMSService;
import weblogic.jms.backend.BackEnd;
import weblogic.jndi.internal.ClientEnvironmentFactoryImpl;
import weblogic.logging.jms.JMSMessageLoggerFactory;
import weblogic.logging.jms.JMSSAFMessageLogger;
import weblogic.management.DeploymentException;
import weblogic.management.DomainDir;
import weblogic.management.ManagementException;
import weblogic.management.UndeploymentException;
import weblogic.management.configuration.JMSSAFMessageLogFileMBean;
import weblogic.management.configuration.MigratableTargetMBean;
import weblogic.management.configuration.SAFAgentMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.management.runtime.SAFRemoteEndpointRuntimeMBean;
import weblogic.management.utils.GenericBeanListener;
import weblogic.messaging.common.PrivilegedActionUtilities;
import weblogic.messaging.saf.internal.SAFThresholdHandler;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.store.PersistentStoreManager;
import weblogic.store.admin.FileAdminHandler;
import weblogic.store.xa.PersistentStoreXA;

public class SAFAgentAdmin {
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final HashMap BACKEND_ATTRIBUTES = new HashMap();
   private static final HashMap SAF_AGENT_ATTRIBUTES = new HashMap();
   private BackEnd backEnd;
   private GenericBeanListener backendChangeListener;
   private GenericBeanListener safChangeListener;
   private HashSet endpointRuntimes = new HashSet();
   private Set importedDestinations = Collections.synchronizedSet(new HashSet());
   private final boolean receivingOnly;
   private long defaultRetryDelayBase;
   private long defaultRetryDelayMaximum;
   private double defaultRetryDelayMultiplier;
   private int windowSize;
   private long defaultTimeToLive;
   private long windowInterval;
   private final HashMap remoteContexts = new HashMap();
   private boolean loggingEnabled;
   private long remoteEndpointsTotalCount;
   private long remoteEndpointsHighCount;
   private SAFAgentRuntimeMBeanAggregator runtimeMBean;

   SAFAgentAdmin(boolean var1) {
      this.receivingOnly = var1;
   }

   void prepare(SAFAgentMBean var1) throws DeploymentException {
      this.loggingEnabled = var1.isLoggingEnabled();
      if (!this.receivingOnly) {
         SecurityServiceManager.pushSubject(KERNEL_ID, KERNEL_ID);
         RuntimeAccess var2 = ManagementService.getRuntimeAccess(KERNEL_ID);

         try {
            TargetMBean[] var3 = var1.getTargets();
            if (var3[0] instanceof MigratableTargetMBean) {
               this.backEnd = new BackEnd(var1.getName() + "@" + var3[0].getName(), "SAFAgent");
            } else {
               this.backEnd = new BackEnd(var1.getName() + "@" + var2.getServerName(), "SAFAgent");
            }

            this.backEnd.setJMSMessageLogger(findJMSSAFMessageLogger(var1));
         } catch (ManagementException var12) {
            throw new DeploymentException(var12);
         } finally {
            SecurityServiceManager.popSubject(KERNEL_ID);
         }

         this.backEnd.setThresholdHandler(new SAFThresholdHandler(this.backEnd.getKernel(), this.backEnd.getName()));
         this.initializeChangeListeners(var1);

         try {
            JMSService.getJMSService().getBEDeployer().addBackEnd(this.backEnd);
         } catch (JMSException var11) {
            throw new AssertionError(var11);
         }
      }

      try {
         this.runtimeMBean = new SAFAgentRuntimeMBeanAggregator(var1.getName(), this.backEnd, new SAFAgentRuntimeMBeanImpl(var1.getName(), this, SAFService.getSAFService().getRuntimeMBean()));
      } catch (ManagementException var10) {
         throw new DeploymentException(var10);
      }

      SAFService.getSAFService().getRuntimeMBean().addAgent(this.runtimeMBean);
   }

   void activate(SAFAgentMBean var1) throws DeploymentException {
      if (!this.receivingOnly) {
         this.closeChangeListeners();
         this.initializeChangeListeners(var1);
         this.backEnd.setPersistentStore(findPersistentStore(var1));
         this.backEnd.setPagingDirectory(findPagingDirectory(var1));

         try {
            this.backEnd.open();
         } catch (JMSException var3) {
            throw new DeploymentException(var3);
         }
      }
   }

   void deactivate() throws UndeploymentException {
      if (!this.receivingOnly) {
         this.backEnd.close();
      }
   }

   public void unprepare() throws UndeploymentException {
      if (!this.receivingOnly) {
         this.backEnd.destroy();

         try {
            PrivilegedActionUtilities.unregister(this.runtimeMBean, KERNEL_ID);
         } catch (ManagementException var2) {
            throw new UndeploymentException(var2);
         }

         SAFService.getSAFService().getRuntimeMBean().removeAgent(this.runtimeMBean);
         this.closeChangeListeners();
         JMSService.getJMSService().getBEDeployer().removeBackEnd(this.backEnd);
      }
   }

   private void initializeChangeListeners(SAFAgentMBean var1) throws DeploymentException {
      this.backendChangeListener = new GenericBeanListener(var1, this.backEnd, BACKEND_ATTRIBUTES, (Map)null);
      this.backendChangeListener.setCustomizer(this.backEnd);
      this.safChangeListener = new GenericBeanListener(var1, this, SAF_AGENT_ATTRIBUTES, (Map)null);

      try {
         this.backendChangeListener.initialize();
         this.safChangeListener.initialize();
      } catch (ManagementException var3) {
         throw new DeploymentException(var3);
      }
   }

   private void closeChangeListeners() {
      this.backendChangeListener.close();
      this.safChangeListener.close();
   }

   SAFAgentRuntimeMBeanAggregator getRuntimeMBean() {
      return this.runtimeMBean;
   }

   private static PersistentStoreXA findPersistentStore(SAFAgentMBean var0) throws DeploymentException {
      PersistentStoreXA var1;
      if (var0.getStore() != null) {
         String var2 = var0.getStore().getName();
         var1 = (PersistentStoreXA)PersistentStoreManager.getManager().getStore(var2);
         if (var1 == null) {
            throw new DeploymentException("The persistent store \"" + var2 + "\" does not exist");
         }
      } else {
         var1 = (PersistentStoreXA)PersistentStoreManager.getManager().getDefaultStore();
         if (var1 == null) {
            throw new DeploymentException("The default persistent store does not exist");
         }
      }

      return var1;
   }

   void addImportedDestination(ImportedDestination var1) {
      this.importedDestinations.add(var1);
   }

   void removeImportedDestination(ImportedDestination var1) {
      this.importedDestinations.remove(var1);
   }

   private static String findPagingDirectory(SAFAgentMBean var0) {
      String var1;
      if (var0.getPagingDirectory() != null) {
         var1 = var0.getPagingDirectory();
         var1 = FileAdminHandler.canonicalizeDirectoryName(var1);
      } else {
         String var2 = ManagementService.getRuntimeAccess(KERNEL_ID).getServer().getName();
         var1 = DomainDir.getTempDirForServer(var2);
      }

      return var1;
   }

   synchronized void addRemoteEndpointRuntimeMBean(SAFRemoteEndpointRuntimeMBean var1) throws ModuleException {
      ++this.remoteEndpointsTotalCount;
      if (this.remoteEndpointsTotalCount > this.remoteEndpointsHighCount) {
         this.remoteEndpointsHighCount = this.remoteEndpointsTotalCount;
      }

      this.endpointRuntimes.add(var1);
   }

   synchronized void removeRemoteEndpointRuntimeMBean(SAFRemoteEndpointRuntimeMBean var1) throws ModuleException {
      --this.remoteEndpointsTotalCount;
      this.endpointRuntimes.remove(var1);
   }

   public RemoteContextAgent findOrCreateRemoteContext(String var1, SAFRemoteContextBean var2) {
      synchronized(this.remoteContexts) {
         RemoteContextAgent var4 = (RemoteContextAgent)this.remoteContexts.get(var1);
         if (var4 == null) {
            SAFOutgoingReplyHandler var5 = var2 != null ? new SAFOutgoingReplyHandler(var2.getReplyToSAFRemoteContextName()) : new SAFOutgoingReplyHandler((String)null);
            var4 = new RemoteContextAgent(var1, var2, var5, new ClientEnvironmentFactoryImpl());
            var4.setRetryDelayBase(this.defaultRetryDelayBase);
            var4.setRetryDelayMaximum(this.defaultRetryDelayMaximum);
            var4.setRetryDelayMultiplier(this.defaultRetryDelayMultiplier);
            var4.setWindowSize(this.windowSize);
            var4.setWindowInterval(this.windowInterval);
            this.remoteContexts.put(var1, var4);
         }

         return var4;
      }
   }

   SAFRemoteEndpointRuntimeMBean[] getRemoteEndpoints() {
      SAFRemoteEndpointRuntimeMBean[] var1 = new SAFRemoteEndpointRuntimeMBean[this.endpointRuntimes.size()];
      return (SAFRemoteEndpointRuntimeMBean[])((SAFRemoteEndpointRuntimeMBean[])this.endpointRuntimes.toArray(var1));
   }

   public long getRemoteEndpointsCurrentCount() {
      return (long)this.endpointRuntimes.size();
   }

   public long getRemoteEndpointsHighCount() {
      return this.remoteEndpointsHighCount;
   }

   public long getRemoteEndpointsTotalCount() {
      return this.remoteEndpointsTotalCount;
   }

   public BackEnd getBackEnd() {
      return this.backEnd;
   }

   boolean isLoggingEnabled() {
      return this.loggingEnabled;
   }

   public void setDefaultRetryDelayBase(long var1) {
      this.defaultRetryDelayBase = var1;
      synchronized(this.remoteContexts) {
         Iterator var4 = this.remoteContexts.values().iterator();

         while(var4.hasNext()) {
            RemoteContextAgent var5 = (RemoteContextAgent)var4.next();
            var5.setRetryDelayBase(var1);
         }

      }
   }

   public void setDefaultRetryDelayMaximum(long var1) {
      this.defaultRetryDelayMaximum = var1;
      synchronized(this.remoteContexts) {
         Iterator var4 = this.remoteContexts.values().iterator();

         while(var4.hasNext()) {
            RemoteContextAgent var5 = (RemoteContextAgent)var4.next();
            var5.setRetryDelayMaximum(var1);
         }

      }
   }

   public void setDefaultRetryDelayMultiplier(double var1) {
      this.defaultRetryDelayMultiplier = var1;
      synchronized(this.remoteContexts) {
         Iterator var4 = this.remoteContexts.values().iterator();

         while(var4.hasNext()) {
            RemoteContextAgent var5 = (RemoteContextAgent)var4.next();
            var5.setRetryDelayMultiplier(var1);
         }

      }
   }

   public void setWindowSize(int var1) {
      this.windowSize = var1;
      synchronized(this.remoteContexts) {
         Iterator var3 = this.remoteContexts.values().iterator();

         while(var3.hasNext()) {
            RemoteContextAgent var4 = (RemoteContextAgent)var3.next();
            var4.setWindowSize(var1);
         }

      }
   }

   public void setLoggingEnabled(boolean var1) {
      this.loggingEnabled = var1;
   }

   public void setConversationIdleTimeMaximum(long var1) {
   }

   public void setAcknowledgeInterval(long var1) {
   }

   public void setDefaultTimeToLive(long var1) {
      this.defaultTimeToLive = var1;
      Iterator var3 = this.importedDestinations.iterator();

      while(var3.hasNext()) {
         ImportedDestination var4 = (ImportedDestination)var3.next();
         var4.setAgentTimeToLiveOverride(var1);
      }

   }

   long getDefaultTimeToLive() {
      return this.defaultTimeToLive;
   }

   public void setWindowInterval(long var1) {
      this.windowInterval = var1;
      synchronized(this.remoteContexts) {
         Iterator var4 = this.remoteContexts.values().iterator();

         while(var4.hasNext()) {
            RemoteContextAgent var5 = (RemoteContextAgent)var4.next();
            var5.setWindowInterval(var1);
         }

      }
   }

   long getWindowInterval() {
      return this.windowInterval;
   }

   public void setIncomingPausedAtStartup(boolean var1) {
      this.backEnd.setProductionPausedAtStartup("" + var1);
   }

   public void setForwardingPausedAtStartup(boolean var1) {
      this.backEnd.setConsumptionPausedAtStartup("" + var1);
   }

   public void setReceivingPausedAtStartup(boolean var1) {
   }

   private static JMSSAFMessageLogger findJMSSAFMessageLogger(SAFAgentMBean var0) throws DeploymentException {
      try {
         JMSSAFMessageLogFileMBean var1 = var0.getJMSSAFMessageLogFile();
         return JMSMessageLoggerFactory.findOrCreateJMSSAFMessageLogger(var1);
      } catch (IOException var2) {
         throw new DeploymentException("Cannot find or create JMS message log file for saf agent " + var0.getName(), var2);
      }
   }

   static {
      BACKEND_ATTRIBUTES.put("BytesMaximum", Long.TYPE);
      BACKEND_ATTRIBUTES.put("BytesThresholdHigh", Long.TYPE);
      BACKEND_ATTRIBUTES.put("BytesThresholdLow", Long.TYPE);
      BACKEND_ATTRIBUTES.put("MessagesMaximum", Long.TYPE);
      BACKEND_ATTRIBUTES.put("MessagesThresholdHigh", Long.TYPE);
      BACKEND_ATTRIBUTES.put("MessagesThresholdLow", Long.TYPE);
      BACKEND_ATTRIBUTES.put("MaximumMessageSize", Integer.TYPE);
      BACKEND_ATTRIBUTES.put("MessageBufferSize", Long.TYPE);
      SAF_AGENT_ATTRIBUTES.put("DefaultRetryDelayBase", Long.TYPE);
      SAF_AGENT_ATTRIBUTES.put("DefaultRetryDelayMaximum", Long.TYPE);
      SAF_AGENT_ATTRIBUTES.put("DefaultRetryDelayMultiplier", Double.TYPE);
      SAF_AGENT_ATTRIBUTES.put("WindowSize", Integer.TYPE);
      SAF_AGENT_ATTRIBUTES.put("LoggingEnabled", Boolean.TYPE);
      SAF_AGENT_ATTRIBUTES.put("ConversationIdleTimeMaximum", Long.TYPE);
      SAF_AGENT_ATTRIBUTES.put("AcknowledgeInterval", Long.TYPE);
      SAF_AGENT_ATTRIBUTES.put("DefaultTimeToLive", Long.TYPE);
      SAF_AGENT_ATTRIBUTES.put("WindowInterval", Long.TYPE);
      SAF_AGENT_ATTRIBUTES.put("IncomingPausedAtStartup", Boolean.TYPE);
      SAF_AGENT_ATTRIBUTES.put("ForwardingPausedAtStartup", Boolean.TYPE);
      SAF_AGENT_ATTRIBUTES.put("ReceivingPausedAtStartup", Boolean.TYPE);
   }
}
