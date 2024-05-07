package weblogic.messaging.saf.internal;

import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.naming.NamingException;
import weblogic.health.HealthState;
import weblogic.jms.saf.SAFAgentRuntimeMBeanAggregator;
import weblogic.jms.saf.SAFService;
import weblogic.management.DeploymentException;
import weblogic.management.ManagementException;
import weblogic.management.configuration.MigratableTargetMBean;
import weblogic.management.configuration.PersistentStoreMBean;
import weblogic.management.configuration.SAFAgentMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.SAFAgentRuntimeMBean;
import weblogic.management.runtime.SAFConversationRuntimeMBean;
import weblogic.management.runtime.SAFRemoteEndpointRuntimeMBean;
import weblogic.management.utils.GenericBeanListener;
import weblogic.messaging.ID;
import weblogic.messaging.kernel.Topic;
import weblogic.messaging.saf.SAFException;
import weblogic.messaging.saf.common.SAFDebug;
import weblogic.messaging.saf.store.SAFStore;
import weblogic.messaging.saf.store.SAFStoreManager;
import weblogic.messaging.saf.utils.Util;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;

public final class SAFAgentAdmin extends SAFStatisticsCommonMBeanImpl implements SAFAgentRuntimeMBean {
   static final long serialVersionUID = 4301254277290100353L;
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   public static final int SENDING_ONLY = 1;
   public static final int RECEIVING_ONLY = 2;
   public static final int BOTH = 3;
   private SAFServiceAdmin safServiceAdmin;
   private SAFAgentMBean mbean;
   private PersistentStoreMBean storeMBean;
   private SendingAgentImpl sendingAgent;
   private ReceivingAgentImpl receivingAgent;
   private int agentType;
   private HashMap remoteEndpoints;
   private int remoteEndpointsCurrentCount;
   private int remoteEndpointsHighCount;
   private int remoteEndpointsTotalCount;
   private GenericBeanListener changeListener;
   private static final HashMap agentBeanAttributes = new HashMap();
   private String agentName;

   SAFAgentAdmin(SAFServiceAdmin var1, SAFAgentMBean var2) throws ManagementException {
      super(var2.getName(), (RuntimeMBean)null, false);
      SAFService.getSAFService().getRuntimeMBean().getAgent(var2.getName()).setDelegate2(this);
      this.safServiceAdmin = var1;
      this.mbean = var2;
      this.storeMBean = var2.getStore();
      this.remoteEndpoints = new HashMap();
      SecurityServiceManager.pushSubject(KERNEL_ID, KERNEL_ID);
      String var3 = null;

      try {
         RuntimeAccess var4 = ManagementService.getRuntimeAccess(KERNEL_ID);
         var3 = var4.getServerName();
      } finally {
         SecurityServiceManager.popSubject(KERNEL_ID);
      }

      String var9 = var3;
      if (this.isMigratable(var2)) {
         var9 = var2.getTargets()[0].getName();
      }

      this.agentName = var2.getName() + "@" + var9;
      this.checkTargets();
      this.changeListener = new GenericBeanListener(var2, this, agentBeanAttributes, (Map)null);
   }

   SAFServiceAdmin getSAFServiceAdmin() {
      return this.safServiceAdmin;
   }

   int getAgentType() {
      return this.agentType;
   }

   public SAFAgentMBean getMBean() {
      return this.mbean;
   }

   private void checkTargets() throws DeploymentException {
      TargetMBean[] var1 = this.mbean.getTargets();
      PersistentStoreMBean var2 = this.mbean.getStore();
      if (var2 != null) {
         TargetMBean[] var3 = var2.getTargets();
         DeploymentException var4;
         if (var3.length > 1) {
            var4 = new DeploymentException("SAF Agent = " + this.mbean.getName() + " has a Persistent Store = " + var2.getName() + " which has multiple targets");
            throw var4;
         } else if (var3.length == 0) {
            var4 = new DeploymentException("SAF Agent= " + this.mbean.getName() + " has a Persistent Store " + var2.getName() + " which is not targetted");
            throw var4;
         } else if (var1.length > 1) {
            var4 = new DeploymentException("SAF Agent = " + this.mbean.getName() + " has multiple Tragets");
            throw var4;
         } else if (!var1[0].getName().equals(var3[0].getName())) {
            var4 = new DeploymentException("SAF Agent = " + this.mbean.getName() + " Target = " + var1[0].getName() + " is not the same as Targets of the " + "Persistent Store = " + var2.getName() + " Targets = " + var3[0].getName());
            throw var4;
         }
      }
   }

   void start() throws NamingException, SAFException {
      String var1 = this.mbean.getServiceType();
      if ("Sending-only".equals(var1)) {
         this.findOrCreateSendingAgentImpl(this.agentName);
         this.agentType = 1;
      } else if ("Receiving-only".equals(var1)) {
         this.findOrCreateReceivingAgentImpl(this.agentName);
         this.agentType = 2;
      } else if ("Both".equals(var1)) {
         this.findOrCreateSendingAgentImpl(this.agentName);
         this.findOrCreateReceivingAgentImpl(this.agentName);
         this.agentType = 3;
      }

   }

   private SAFStore createSAFStore(String var1, boolean var2) throws SAFException {
      return SAFStoreManager.getManager().createSAFStore(this.storeMBean, var1, var2);
   }

   private void findOrCreateSendingAgentImpl(String var1) throws NamingException, SAFException {
      SAFStore var2 = this.createSAFStore(var1, false);
      this.sendingAgent = (SendingAgentImpl)var2.getSendingAgent();
      if (this.sendingAgent == null) {
         this.sendingAgent = new SendingAgentImpl(var1, this, var2);
      } else {
         this.sendingAgent.init(var1, this, var2);
      }

   }

   private void findOrCreateReceivingAgentImpl(String var1) throws NamingException, SAFException {
      SAFStore var2 = this.createSAFStore(var1, true);
      this.receivingAgent = (ReceivingAgentImpl)var2.getReceivingAgent();
      if (this.receivingAgent == null) {
         this.receivingAgent = new ReceivingAgentImpl(var1, this, var2);
      } else {
         this.receivingAgent.init(var1, this, var2);
      }

   }

   Agent getSendingAgentImpl() {
      return this.sendingAgent;
   }

   Agent getReceivingAgentImpl() {
      return this.receivingAgent;
   }

   void close() {
      SAFAgentRuntimeMBeanAggregator var1 = SAFService.getSAFService().getRuntimeMBean().getAgent(this.name);
      if (var1 != null) {
         var1.setDelegate2((SAFAgentRuntimeMBean)null);
      }

      if (this.changeListener != null) {
         this.changeListener.close();
         this.changeListener = null;
      }

      try {
         if (this.sendingAgent != null) {
            this.sendingAgent.close(false);
         }
      } catch (Exception var8) {
      }

      try {
         if (this.receivingAgent != null) {
            this.receivingAgent.close(false);
         }
      } catch (Exception var7) {
      }

      Iterator var2 = null;
      synchronized(this) {
         var2 = ((HashMap)this.remoteEndpoints.clone()).values().iterator();
         this.remoteEndpoints.clear();
      }

      while(var2.hasNext()) {
         try {
            RemoteEndpointRuntimeDelegate var3 = (RemoteEndpointRuntimeDelegate)var2.next();
            var3.close();
         } catch (Exception var5) {
         }
      }

   }

   void suspend(boolean var1) {
      if (this.sendingAgent != null) {
         this.sendingAgent.suspend(var1);
      }

      if (this.receivingAgent != null) {
         this.receivingAgent.suspend(var1);
      }

   }

   void resume() throws SAFException {
      if (this.sendingAgent != null) {
         this.sendingAgent.resume();
      }

      if (this.receivingAgent != null) {
         this.receivingAgent.resume();
      }

   }

   synchronized RemoteEndpointRuntimeDelegate findOrCreateRemoteEndpointRuntime(String var1, int var2, final Topic var3) throws ManagementException {
      RemoteEndpointRuntimeDelegate var4 = (RemoteEndpointRuntimeDelegate)this.remoteEndpoints.get(var1);
      if (var4 != null) {
         return var4;
      } else {
         final String var5 = var1;
         final int var6 = var2;
         final ID var7 = Util.generateID();
         final SAFAgentAdmin var8 = this;

         try {
            var4 = (RemoteEndpointRuntimeDelegate)SecurityServiceManager.runAs(KERNEL_ID, KERNEL_ID, new PrivilegedExceptionAction() {
               public Object run() throws ManagementException {
                  return new RemoteEndpointRuntimeDelegate(var8, var7, var5, var6, var3);
               }
            });
         } catch (PrivilegedActionException var10) {
            throw (ManagementException)var10.getException();
         }

         ++this.remoteEndpointsCurrentCount;
         ++this.remoteEndpointsTotalCount;
         if (this.remoteEndpointsCurrentCount > this.remoteEndpointsHighCount) {
            this.remoteEndpointsHighCount = this.remoteEndpointsCurrentCount;
         }

         this.remoteEndpoints.put(var1, var4);
         return var4;
      }
   }

   synchronized RemoteEndpointRuntimeDelegate getRemoteEndpoint(String var1) {
      return (RemoteEndpointRuntimeDelegate)this.remoteEndpoints.get(var1);
   }

   synchronized void removeRemoteEndpoint(RemoteEndpointRuntimeDelegate var1) {
      RemoteEndpointRuntimeDelegate var2 = (RemoteEndpointRuntimeDelegate)this.remoteEndpoints.remove(var1.getURL());
      if (var2 != null) {
         --this.remoteEndpointsCurrentCount;
         var2.close();
      }

   }

   public HealthState getHealthState() {
      HealthState var1 = null;
      Object var2 = null;
      if (this.sendingAgent != null) {
         var1 = this.sendingAgent.getHealthState();
      }

      if (this.receivingAgent != null) {
         var1 = this.receivingAgent.getHealthState();
      }

      return this.combineState(var1, (HealthState)var2);
   }

   public synchronized SAFRemoteEndpointRuntimeMBean[] getRemoteEndpoints() {
      RemoteEndpointRuntimeDelegate[] var1 = new RemoteEndpointRuntimeDelegate[this.remoteEndpoints.size()];
      this.remoteEndpoints.values().toArray(var1);
      return var1;
   }

   public synchronized long getRemoteEndpointsCurrentCount() {
      return (long)this.remoteEndpointsCurrentCount;
   }

   public synchronized long getRemoteEndpointsHighCount() {
      return (long)this.remoteEndpointsHighCount;
   }

   public synchronized long getRemoteEndpointsTotalCount() {
      return (long)this.remoteEndpointsTotalCount;
   }

   public void pauseIncoming() throws SAFException {
      if (this.sendingAgent != null) {
         if (SAFDebug.SAFSendingAgent.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
            SAFDebug.SAFAdmin.debug("Pausing sending agent " + this.getName() + " for incoming");
         }

         this.sendingAgent.pauseIncoming();
      }

   }

   public void resumeIncoming() throws SAFException {
      if (this.sendingAgent != null) {
         if (SAFDebug.SAFSendingAgent.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
            SAFDebug.SAFAdmin.debug("Resuming sending agent " + this.getName() + " for incoming");
         }

         this.sendingAgent.resumeIncoming();
      }

   }

   public boolean isPausedForIncoming() {
      return this.sendingAgent != null ? this.sendingAgent.isPausedForIncoming() : false;
   }

   public void pauseForwarding() throws SAFException {
      if (this.sendingAgent != null) {
         if (SAFDebug.SAFSendingAgent.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
            SAFDebug.SAFAdmin.debug("Pausing sending agent " + this.getName() + " for forwarding");
         }

         this.sendingAgent.pauseForwarding();
      }

   }

   public void resumeForwarding() throws SAFException {
      if (this.sendingAgent != null) {
         if (SAFDebug.SAFSendingAgent.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
            SAFDebug.SAFAdmin.debug("Resuming sending agent " + this.getName() + " for forwarding");
         }

         this.sendingAgent.resumeForwarding();
      }

   }

   public boolean isPausedForForwarding() {
      return this.sendingAgent != null ? this.sendingAgent.isPausedForForwarding() : false;
   }

   public void pauseReceiving() throws SAFException {
      if (this.receivingAgent != null) {
         if (SAFDebug.SAFReceivingAgent.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
            SAFDebug.SAFAdmin.debug("Pausing receiving agent " + this.getName() + " for receiving");
         }

         this.receivingAgent.pauseReceiving();
      }

   }

   public void resumeReceiving() throws SAFException {
      if (this.receivingAgent != null) {
         if (SAFDebug.SAFReceivingAgent.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
            SAFDebug.SAFAdmin.debug("Resuming receiving agent " + this.getName() + " for receiving");
         }

         this.receivingAgent.resumeReceiving();
      }

   }

   public boolean isPausedForReceiving() {
      return this.receivingAgent != null ? this.receivingAgent.isPausedForReceiving() : false;
   }

   public SAFConversationRuntimeMBean[] getConversations() {
      return this.sendingAgent != null ? this.sendingAgent.getConversationRuntimeDelegates() : new SAFConversationRuntimeMBean[0];
   }

   public long getConversationsCurrentCount() {
      return this.sendingAgent != null ? this.sendingAgent.getConversationsCurrentCount() : 0L;
   }

   public long getConversationsHighCount() {
      return this.sendingAgent != null ? this.sendingAgent.getConversationsHighCount() : 0L;
   }

   public long getConversationsTotalCount() {
      return this.sendingAgent != null ? this.sendingAgent.getConversationsTotalCount() : 0L;
   }

   public long getMessagesCurrentCount() {
      return 0L;
   }

   public long getMessagesPendingCount() {
      return 0L;
   }

   public long getMessagesHighCount() {
      return 0L;
   }

   public long getMessagesReceivedCount() {
      return 0L;
   }

   public long getMessagesThresholdTime() {
      return 0L;
   }

   public long getBytesCurrentCount() {
      return 0L;
   }

   public long getBytesPendingCount() {
      return 0L;
   }

   public long getBytesHighCount() {
      return 0L;
   }

   public long getBytesReceivedCount() {
      return 0L;
   }

   public long getBytesThresholdTime() {
      return 0L;
   }

   public long getFailedMessagesTotal() {
      return this.sendingAgent != null ? this.sendingAgent.getFailedMessagesTotal() : 0L;
   }

   public long getDefaultRetryDelayBase() {
      return this.mbean.getDefaultRetryDelayBase();
   }

   public long getDefaultRetryDelayMaximum() {
      return this.mbean.getDefaultRetryDelayMaximum();
   }

   public double getDefaultRetryDelayMultiplier() {
      return this.mbean.getDefaultRetryDelayMultiplier();
   }

   public long getDefaultTimeToLive() {
      return this.mbean.getDefaultTimeToLive();
   }

   public long getMessageBufferSize() {
      return this.mbean.getMessageBufferSize();
   }

   public long getConversationIdleTimeMaximum() {
      return this.mbean.getConversationIdleTimeMaximum();
   }

   public long getBytesMaximum() {
      return this.mbean.getBytesMaximum();
   }

   public long getBytesThresholdHigh() {
      return this.mbean.getBytesThresholdHigh();
   }

   public long getBytesThresholdLow() {
      return this.mbean.getBytesThresholdLow();
   }

   public long getMessagesMaximum() {
      return this.mbean.getMessagesMaximum();
   }

   public long getMessagesThresholdHigh() {
      return this.mbean.getMessagesThresholdHigh();
   }

   public long getMessagesThresholdLow() {
      return this.mbean.getMessagesThresholdLow();
   }

   public int getMaximumMessageSize() {
      return this.mbean.getMaximumMessageSize();
   }

   public boolean isLoggingEnabled() {
      return this.mbean.isLoggingEnabled();
   }

   public int getWindowSize() {
      return this.mbean.getWindowSize();
   }

   public long getAcknowledgeInterval() {
      return this.mbean.getAcknowledgeInterval();
   }

   public void setDefaultRetryDelayBase(long var1) {
      if (this.sendingAgent != null) {
         this.sendingAgent.setDefaultRetryDelayBase(var1);
      }

   }

   public void setDefaultRetryDelayMaximum(long var1) {
      if (this.sendingAgent != null) {
         this.sendingAgent.setDefaultRetryDelayMaximum(var1);
      }

   }

   public void setDefaultRetryDelayMultiplier(double var1) {
      if (this.sendingAgent != null) {
         this.sendingAgent.setDefaultRetryDelayMultiplier(var1);
      }

   }

   public void setDefaultTimeToLive(long var1) {
      if (this.sendingAgent != null) {
         this.sendingAgent.setDefaultTimeToLive(var1);
      }

      if (this.receivingAgent != null) {
         this.receivingAgent.setDefaultTimeToLive(var1);
      }

   }

   public void setMessageBufferSize(long var1) {
   }

   public void setConversationIdleTimeMaximum(long var1) {
      if (this.sendingAgent != null) {
         this.sendingAgent.setConversationIdleTimeMaximum(var1);
      }

      if (this.receivingAgent != null) {
         this.receivingAgent.setConversationIdleTimeMaximum(var1);
      }

   }

   public void setBytesMaximum(long var1) {
   }

   public void setBytesThresholdHigh(long var1) {
   }

   public void setBytesThresholdLow(long var1) {
   }

   public void setMessagesMaximum(long var1) {
   }

   public void setMessagesThresholdHigh(long var1) {
   }

   public void setMessagesThresholdLow(long var1) {
   }

   public void setMaximumMessageSize(int var1) {
   }

   public void setLoggingEnabled(boolean var1) {
      if (this.sendingAgent != null) {
         this.sendingAgent.setLoggingEnabled(var1);
      }

   }

   public void setWindowSize(int var1) {
      if (this.sendingAgent != null) {
         this.sendingAgent.setWindowSize(var1);
      }

      if (this.receivingAgent != null) {
         this.receivingAgent.setWindowSize(var1);
      }

   }

   public void setAcknowledgeInterval(long var1) {
      if (this.receivingAgent != null) {
         this.receivingAgent.setAcknowledgementInterval(var1);
      }

   }

   private HealthState combineState(HealthState var1, HealthState var2) {
      if (var1 == null) {
         return var2;
      } else if (var2 == null) {
         return var1;
      } else {
         return var1.getState() <= var2.getState() ? var2 : var1;
      }
   }

   boolean isActiveForWSRM() {
      SAFConversationRuntimeMBean[] var1 = this.getConversations();
      if (var1 != null && var1.length != 0) {
         return true;
      } else {
         if (this.sendingAgent != null) {
            this.sendingAgent.pauseIncoming();
         }

         if (this.receivingAgent != null) {
            this.receivingAgent.pauseReceiving();
         }

         return false;
      }
   }

   private boolean isMigratable(SAFAgentMBean var1) {
      TargetMBean[] var2 = var1.getTargets();
      return var2 != null && var2.length != 0 ? var2[0] instanceof MigratableTargetMBean : false;
   }

   static {
      agentBeanAttributes.put("AcknowledgeInterval", Long.TYPE);
      agentBeanAttributes.put("BytesMaximum", Long.TYPE);
      agentBeanAttributes.put("BytesThresholdHigh", Long.TYPE);
      agentBeanAttributes.put("BytesThresholdLow", Long.TYPE);
      agentBeanAttributes.put("ConversationIdleTimeMaximum", Long.TYPE);
      agentBeanAttributes.put("DefaultRetryDelayBase", Long.TYPE);
      agentBeanAttributes.put("DefaultRetryDelayMaximum", Long.TYPE);
      agentBeanAttributes.put("DefaultRetryDelayMultiplier", Double.TYPE);
      agentBeanAttributes.put("DefaultTimeToLive", Long.TYPE);
      agentBeanAttributes.put("LoggingEnabled", Boolean.TYPE);
      agentBeanAttributes.put("MaximumMessageSize", Integer.TYPE);
      agentBeanAttributes.put("MessagesMaximum", Long.TYPE);
      agentBeanAttributes.put("MessagesThresholdHigh", Long.TYPE);
      agentBeanAttributes.put("MessagesThresholdLow", Long.TYPE);
      agentBeanAttributes.put("MessageBufferSize", Long.TYPE);
      agentBeanAttributes.put("WindowSize", Integer.TYPE);
   }
}
