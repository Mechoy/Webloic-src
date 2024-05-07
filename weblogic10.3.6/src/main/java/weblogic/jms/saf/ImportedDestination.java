package weblogic.jms.saf;

import java.security.AccessController;
import javax.jms.JMSException;
import weblogic.application.ModuleException;
import weblogic.descriptor.BeanUpdateFailedException;
import weblogic.j2ee.descriptor.wl.SAFErrorHandlingBean;
import weblogic.j2ee.descriptor.wl.SAFImportedDestinationsBean;
import weblogic.j2ee.descriptor.wl.SAFRemoteContextBean;
import weblogic.jms.backend.BEDestinationImpl;
import weblogic.jms.backend.BackEnd;
import weblogic.jms.common.DestinationImpl;
import weblogic.jms.common.EntityName;
import weblogic.jms.forwarder.RuntimeHandler;
import weblogic.management.ManagementException;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.messaging.common.PrivilegedActionUtilities;
import weblogic.messaging.kernel.Queue;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

class ImportedDestination {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private final String name;
   private final ImportedDestinationGroup idGroup;
   private final IDBeanHandler idBeanHandler;
   private SAFQueueImpl managedDestination;
   private String remoteJNDIName;
   private int nonPersistentQos = 1;
   private int persistentQos = 2;
   private SAFRemoteEndpointCustomizer runtimeMBean;
   private final SAFAgentAdmin safAgent;
   private String agentName;
   private String applicationId;
   private String earModuleName;
   private RemoteContextAgent remoteContext;
   private long timeToLiveDefault;
   private boolean useSafTimeToLiveDefault;
   private long agentTimeToLiveOverride;
   private EntityName entityName;
   private String destinationType;
   private String messageLoggingFormat;
   private boolean messageLoggingEnabled = false;

   ImportedDestination(IDBeanHandler var1, ImportedDestinationGroup var2, String var3, EntityName var4, String var5, String var6, long var7, boolean var9, String var10, String var11, String var12, boolean var13, String var14) throws ModuleException {
      this.idGroup = var2;
      this.applicationId = var3;
      this.entityName = var4;
      this.earModuleName = var4.getEARModuleName();
      this.name = var5;
      this.agentName = var6;
      this.timeToLiveDefault = var7;
      this.useSafTimeToLiveDefault = var9;
      this.safAgent = SAFService.getSAFService().getDeployer().getAgent(var6);
      this.idBeanHandler = var1;
      this.destinationType = var10;
      this.setNonPersistentQos(var11);
      this.setPersistentQos(var14);
      this.messageLoggingFormat = var12;
      this.messageLoggingEnabled = var13;
   }

   public void prepare() throws ModuleException {
      this.safAgent.addImportedDestination(this);
      BackEnd var1 = this.safAgent.getBackEnd();

      try {
         this.managedDestination = new SAFQueueImpl(this.safAgent, var1.getFullSAFDestinationName(this.name), false, this.entityName, this.destinationType);
      } catch (JMSException var11) {
         throw new ModuleException(var11);
      }

      this.agentTimeToLiveOverride = this.safAgent.getDefaultTimeToLive();
      this.applyTimeToLive();
      this.managedDestination.setPriority(-1);
      this.managedDestination.setTimeToLive(-1L);
      this.managedDestination.setRedeliveryLimit(Integer.MAX_VALUE);
      this.managedDestination.setNonPersistentQos(this.nonPersistentQos);
      this.managedDestination.setPersistentQos(this.persistentQos);
      this.managedDestination.setLocalJNDIName(this.name + "@" + this.agentName + "@SAF");
      if (this.idBeanHandler.getSafErrorHandling() == null) {
         this.managedDestination.initializeErrorHandling((String)null);
      } else {
         this.managedDestination.initializeErrorHandling(this.idBeanHandler.getSafErrorHandling().getName());
      }

      DestinationImpl var2 = new DestinationImpl(this.managedDestination.getDestinationTypeIndicator(), var1.getName(), var1.getPersistentStore() != null ? var1.getPersistentStore().getName() : null, this.managedDestination.getName(), this.applicationId, this.earModuleName, var1.getJMSServerId(), this.managedDestination.getJMSID(), this.managedDestination.getCreationTime(), this.managedDestination.getSAFExportPolicy());
      this.managedDestination.setDestinationImpl(var2);

      try {
         this.managedDestination.setQuota(var1.getQuota());
      } catch (BeanUpdateFailedException var10) {
         throw new ModuleException(var10.getMessage(), var10);
      }

      try {
         var1.addDestination(this.managedDestination);
      } catch (JMSException var9) {
         throw new ModuleException("ERROR: Unable to add destination " + this.managedDestination.getName() + " to the back end " + var1.getName(), var9);
      }

      SAFImportedDestinationsBean var3 = this.idGroup.getBean();
      SAFRemoteContextBean var4 = var3.getSAFRemoteContext();
      String var5 = var4 != null ? var4.getSAFLoginContext().getLoginURL() : null;

      try {
         SAFRemoteEndpointRuntimeMBeanImpl var6 = new SAFRemoteEndpointRuntimeMBeanImpl(var1.getFullSAFDestinationName(this.name), var5 + "/" + this.remoteJNDIName, this.managedDestination, SAFService.getSAFService().getRuntimeMBean().getAgent(this.agentName).getJMSSAFAgentRuntime(), this.managedDestination.getErrorHandler());
         this.runtimeMBean = new SAFRemoteEndpointCustomizer(var6.getName(), (RuntimeMBean)null, var6);
         this.managedDestination.setSAFRuntimeMBean(var6);
         this.safAgent.addRemoteEndpointRuntimeMBean(this.runtimeMBean);
      } catch (ManagementException var8) {
         throw new ModuleException(var8);
      }

      try {
         this.managedDestination.open();
      } catch (JMSException var7) {
         throw new ModuleException(var7);
      }
   }

   public void activate() throws ModuleException {
      try {
         this.managedDestination.setMessageLoggingEnabled(this.messageLoggingEnabled);
         this.managedDestination.setMessageLoggingFormat(this.messageLoggingFormat);
         this.managedDestination.start();
      } catch (JMSException var2) {
         throw new ModuleException("ERROR: Could not activate " + this.managedDestination.getName(), var2);
      }

      this.initializeRemoteContext();
   }

   private void initializeRemoteContext() {
      this.remoteContext = this.safAgent.findOrCreateRemoteContext(this.idGroup.getRemoteSAFContextFullyQualifiedName(), this.idGroup.getRemoteContextBean());
      this.remoteContext.addForwarder(this.managedDestination.getBackEnd().getPersistentStore(), this.managedDestination.getBackEnd().getAsyncPushWorkManager(), (RuntimeHandler)this.runtimeMBean.getDelegate(), (Queue)this.managedDestination.getKernelDestination(), this.remoteJNDIName, this.nonPersistentQos, this.persistentQos);
   }

   private void deinitializeRemoteContext() {
      if (this.remoteContext != null) {
         this.remoteContext.removeForwarder((Queue)this.managedDestination.getKernelDestination(), this.remoteJNDIName);
      }

   }

   public void deactivate() throws ModuleException {
      this.deinitializeRemoteContext();
   }

   public void unprepare() throws ModuleException {
      this.safAgent.removeImportedDestination(this);
      BackEnd var1 = this.safAgent.getBackEnd();
      var1.removeDestination(this.managedDestination);

      try {
         PrivilegedActionUtilities.unregister(this.runtimeMBean, kernelId);
      } catch (ManagementException var3) {
         throw new ModuleException(var3);
      }

      this.safAgent.removeRemoteEndpointRuntimeMBean(this.runtimeMBean);
      this.runtimeMBean = null;
   }

   public void destroy() throws ModuleException {
   }

   public void remove() throws ModuleException {
      this.managedDestination.adminDeletion();
   }

   public BEDestinationImpl getManagedDestination() {
      return this.managedDestination;
   }

   public void setRemoteJNDIName(String var1) {
      this.remoteJNDIName = var1;
      this.remoteContextChanged();
   }

   public void setNonPersistentQos(String var1) {
      if ("At-Most-Once".equals(var1)) {
         this.nonPersistentQos = 1;
      } else if ("Exactly-Once".equals(var1)) {
         this.nonPersistentQos = 2;
      } else {
         if (!"At-Least-Once".equals(var1)) {
            throw new IllegalArgumentException(var1);
         }

         this.nonPersistentQos = 3;
      }

      if (this.managedDestination != null) {
         this.managedDestination.setNonPersistentQos(this.nonPersistentQos);
      }

      this.remoteContextChanged();
   }

   public void setPersistentQos(String var1) {
      if ("At-Most-Once".equals(var1)) {
         this.persistentQos = 1;
      } else if ("Exactly-Once".equals(var1)) {
         this.persistentQos = 2;
      } else {
         if (!"At-Least-Once".equals(var1)) {
            throw new IllegalArgumentException(var1);
         }

         this.persistentQos = 3;
      }

      if (this.managedDestination != null) {
         this.managedDestination.setPersistentQos(this.persistentQos);
      }

      this.remoteContextChanged();
   }

   public synchronized void setSAFErrorHandling(SAFErrorHandlingBean var1) {
      if (var1 == null) {
         this.managedDestination.setSAFErrorHandlingName((String)null);
      } else {
         this.managedDestination.setSAFErrorHandlingName(var1.getName());
      }

   }

   public void setTimeToLiveDefault(long var1) {
      this.timeToLiveDefault = var1;
      this.applyTimeToLive();
   }

   public void setUseSAFTimeToLiveDefault(boolean var1) {
      this.useSafTimeToLiveDefault = var1;
      this.applyTimeToLive();
   }

   void setAgentTimeToLiveOverride(long var1) {
      this.agentTimeToLiveOverride = var1;
      this.applyTimeToLive();
   }

   private void applyTimeToLive() {
      if (this.useSafTimeToLiveDefault) {
         if (this.timeToLiveDefault == -1L) {
            this.managedDestination.setTimeToLiveDefault(this.agentTimeToLiveOverride);
         } else {
            this.managedDestination.setTimeToLiveDefault(this.timeToLiveDefault);
         }
      } else {
         this.managedDestination.setTimeToLiveDefault(-1L);
      }

   }

   public void remoteContextChanged() {
      if (this.remoteContext != null) {
         this.deinitializeRemoteContext();
         this.initializeRemoteContext();
      }

   }
}
