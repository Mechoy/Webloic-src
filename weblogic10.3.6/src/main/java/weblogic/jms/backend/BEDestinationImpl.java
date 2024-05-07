package weblogic.jms.backend;

import java.security.AccessController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import javax.jms.JMSException;
import javax.jms.ServerSessionPool;
import javax.jms.TextMessage;
import javax.naming.NamingException;
import javax.transaction.Transaction;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import weblogic.descriptor.BeanUpdateFailedException;
import weblogic.jms.JMSExceptionLogger;
import weblogic.jms.JMSLogger;
import weblogic.jms.JMSService;
import weblogic.jms.common.BadSequenceNumberException;
import weblogic.jms.common.DestinationImpl;
import weblogic.jms.common.DispatcherCompletionListener;
import weblogic.jms.common.IllegalStateException;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.JMSDestinationSecurity;
import weblogic.jms.common.JMSDiagnosticImageSource;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSMessageExpirationHelper;
import weblogic.jms.common.JMSProducerSendResponse;
import weblogic.jms.common.JMSSecurityException;
import weblogic.jms.common.JMSSecurityHelper;
import weblogic.jms.common.JMSServerUtilities;
import weblogic.jms.common.MessageImpl;
import weblogic.jms.common.ResourceAllocationException;
import weblogic.jms.dd.DDManager;
import weblogic.jms.dispatcher.VoidResponse;
import weblogic.jms.extensions.WLMessage;
import weblogic.jms.server.DestinationStatus;
import weblogic.jms.server.DestinationStatusListener;
import weblogic.jms.utils.tracing.MessageTimeStamp;
import weblogic.management.ManagementException;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JMSServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.messaging.common.PrivilegedActionUtilities;
import weblogic.messaging.dispatcher.Request;
import weblogic.messaging.kernel.Destination;
import weblogic.messaging.kernel.KernelException;
import weblogic.messaging.kernel.KernelRequest;
import weblogic.messaging.kernel.Quota;
import weblogic.messaging.kernel.QuotaException;
import weblogic.messaging.kernel.RedirectionListener;
import weblogic.messaging.kernel.SendOptions;
import weblogic.messaging.kernel.Sequence;
import weblogic.messaging.kernel.Statistics;
import weblogic.messaging.path.helper.PathHelper;
import weblogic.messaging.runtime.DiagnosticImageTimeoutException;
import weblogic.security.WLSPrincipals;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.transaction.TransactionHelper;

public abstract class BEDestinationImpl extends BEDestinationCommon implements RedirectionListener, DestinationStatus {
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   protected static final String QUOTA_PROP = "Quota";
   protected Destination destination;
   private Statistics statistics;
   protected BEMessageComparator comparator;
   static final int SEND_ISSUE_MESSAGE = 1102;
   private static final int SEND_COMPLETE = 1103;
   private static final int SEND_UNKNOWN = 1104;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final char NAME_DELIMITER = '@';
   protected String fullyQualifiedDestinationName;
   public static final int LOG_MESSAGE_ALL = 15;
   private volatile int state = 0;
   protected boolean deleted;
   protected final Set<BEConsumerCommon> consumers = new HashSet();
   protected final HashMap<JMSID, BEProducerSecurityParticipantImpl> producers = new HashMap();
   private int consumersHigh;
   private long consumersTotal;
   private BEThresholdHandler thresholdHandler;
   private LinkedList<DestinationStatusListener> statusListeners = new LinkedList();
   private static LinkedList<NewDestinationListener> newDestinationListeners = new LinkedList();
   private boolean isUp = false;
   private BEExtension destExtension;
   private boolean ifReorder;
   private int throughputEmphasis = 25;
   private boolean isUOWDestination = false;
   private int incompleteWorkExpirationTime = 0;
   private byte[] signatureSecret;
   int count = 0;

   protected BEDestinationImpl(BackEnd var1, String var2, boolean var3, JMSDestinationSecurity var4) {
      super(var1, var2, var3, var4);
      this.fullyQualifiedDestinationName = this.getFullyQualifiedServerName() + '@' + var2;
      if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
         JMSDebug.JMSBackEnd.debug("Creating destination: " + var2 + " on " + var1.getName());
      }

      this.thresholdHandler = new BEThresholdHandler(var1.getName(), var2);
   }

   private String getFullyQualifiedServerName() {
      RuntimeAccess var1 = ManagementService.getRuntimeAccess(kernelId);
      String var2 = var1.getDomainName();
      String var3 = var1.getServer().getCluster() != null ? var1.getServer().getCluster().getName() : null;
      String var4 = var1.getServerName();
      return var2 + '@' + var3 + '@' + var4;
   }

   public abstract void suspendMessageLogging() throws JMSException;

   public abstract void resumeMessageLogging() throws JMSException;

   public abstract boolean isMessageLogging();

   public abstract void setQuota(Quota var1) throws BeanUpdateFailedException;

   public void setModuleName(String var1) {
      this.moduleName = var1;
   }

   public synchronized void addStatusListener(DestinationStatusListener var1) {
      LinkedList var2 = new LinkedList();
      var2.add(var1);
      ListIterator var3 = this.statusListeners.listIterator();

      while(var3.hasNext()) {
         var2.add(var3.next());
      }

      this.statusListeners = var2;
   }

   public synchronized void removeStatusListener(DestinationStatusListener var1) {
      LinkedList var2 = new LinkedList();
      ListIterator var3 = this.statusListeners.listIterator();

      while(var3.hasNext()) {
         var2.add(var3.next());
      }

      var2.remove(var1);
      this.statusListeners = var2;
   }

   public static void addNewDestinationListener(NewDestinationListener var0) {
      LinkedList var1 = new LinkedList();
      var1.add(var0);
      ListIterator var2 = newDestinationListeners.listIterator();

      while(var2.hasNext()) {
         var1.add(var2.next());
      }

      newDestinationListeners = var1;
   }

   public static void removeNewDestinationListener(NewDestinationListener var0) {
      LinkedList var1 = new LinkedList();
      ListIterator var2 = newDestinationListeners.listIterator();

      while(var2.hasNext()) {
         var1.add(var2.next());
      }

      var1.remove(var0);
      newDestinationListeners = var1;
   }

   protected void setKernel(Destination var1) throws JMSException {
      this.destination = var1;

      try {
         var1.setProperty("Durable", new Boolean(!this.isTemporary() && this.backEnd.isStoreEnabled()));
      } catch (KernelException var3) {
         throw new weblogic.jms.common.JMSException(var3);
      }
   }

   public void open() throws JMSException {
      try {
         this.destination.setProperty("MaximumMessageSize", new Integer(this.maximumMessageSize));
      } catch (KernelException var2) {
         throw new weblogic.jms.common.JMSException(var2);
      }

      this.statistics = this.destination.getStatistics();
      this.thresholdHandler.setTarget(this.destination);
      ListIterator var1 = newDestinationListeners.listIterator();

      while(var1.hasNext()) {
         ((NewDestinationListener)var1.next()).newDestination(this);
      }

   }

   public synchronized void setStateFlag(int var1) {
      this.state |= var1;
      if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
         JMSDebug.JMSBackEnd.debug("New state for " + this.name + ": " + JMSService.getStateName(this.state));
      }

   }

   public synchronized void clearStateFlag(int var1) {
      this.state &= ~var1;
   }

   private synchronized boolean checkStateFlag(int var1) {
      return (this.state & var1) != 0;
   }

   private boolean checkStateFlagFast(int var1) {
      return (this.state & var1) != 0;
   }

   public synchronized void setDeleted(boolean var1) {
      this.deleted = var1;
   }

   public Destination getKernelDestination() {
      return this.destination;
   }

   public void setExtension(BEExtension var1) {
      this.destExtension = var1;
   }

   public BEExtension getExtension() {
      return this.destExtension;
   }

   public void setMessagingPerformancePreference(int var1) {
      this.throughputEmphasis = var1;
   }

   public int getMessagingPerformancePreference() {
      return this.throughputEmphasis;
   }

   public void setMaximumMessageSize(int var1) {
      if (var1 < 0) {
         var1 = Integer.MAX_VALUE;
      }

      if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
         JMSDebug.JMSBackEnd.debug("Destination " + this.name + " maximum message size " + var1);
      }

      super.setMaximumMessageSize(var1);
      if (this.destination != null) {
         try {
            this.destination.setProperty("MaximumMessageSize", new Integer(var1));
         } catch (KernelException var3) {
         }
      }

   }

   protected int getAdjustedExpirationPolicy(boolean var1) {
      return this.expirationPolicy;
   }

   public void expirationTimeReached(RedirectionListener.Info var1, boolean var2) {
      int var3 = this.getAdjustedExpirationPolicy(var2);
      MessageImpl var5;
      switch (var3) {
         case 2:
            synchronized(this.configurationLock) {
               var5 = (MessageImpl)var1.getMessage();
               var5.setJMSDestinationImpl(this.destinationImpl);
               JMSMessageExpirationHelper.logExpiredMessage(var5, this.getExpirationLoggingJMSHeaders(), this.getExpirationLoggingUserProperties());
               break;
            }
         case 4:
            BEDestinationImpl var4 = this.getBEErrorDestination();
            var5 = null;
            synchronized(this.configurationLock) {
               var5 = (MessageImpl)var1.getMessageElement().getMessage();

               try {
                  if (!var5.propertyExists("JMS_BEA_DeliveryFailureReason")) {
                     if (JMSDebug.JMSModule.isDebugEnabled()) {
                        if (var5 instanceof TextMessage) {
                           try {
                              ((TextMessage)var5).getText();
                           } catch (JMSException var11) {
                           }
                        }

                        JMSDebug.JMSModule.debug("Setting delivery failure reason for " + var5 + " on " + this.name + " to EXPIRATION_TIME_REACHED");
                     }

                     var5.setPropertiesWritable(true);
                     var5.setIntProperty("JMS_BEA_DeliveryFailureReason", 0);
                     var5.setPropertiesWritable(false);
                     var5.setSAFSequenceName((String)null);
                     var5.setSAFSeqNumber(0L);
                  }
               } catch (JMSException var12) {
               }
            }

            this.applyRedirectOverrides(var4, var5, true);
            synchronized(this.configurationLock) {
               var1.setSendOptions(this.createSendOptions(0L, (Sequence)null, var5));
               var1.setRedirectDestination(var4.getKernelDestination());
            }
      }

   }

   public void deliveryLimitReached(RedirectionListener.Info var1) {
      BEDestinationImpl var2 = this.getBEErrorDestination();
      if (var2 != null) {
         MessageImpl var3 = null;
         synchronized(this.configurationLock) {
            var3 = (MessageImpl)var1.getMessageElement().getMessage();

            try {
               if (!var3.propertyExists("JMS_BEA_DeliveryFailureReason")) {
                  if (JMSDebug.JMSModule.isDebugEnabled()) {
                     if (var3 instanceof TextMessage) {
                        try {
                           ((TextMessage)var3).getText();
                        } catch (JMSException var9) {
                        }
                     }

                     JMSDebug.JMSModule.debug("Setting delivery failure reason for " + var3 + " on " + this.name + " to DELIVERY_LIMIT_REACHED");
                  }

                  var3.setPropertiesWritable(true);
                  var3.setIntProperty("JMS_BEA_DeliveryFailureReason", 2);
                  var3.setPropertiesWritable(false);
                  var3.setSAFSequenceName((String)null);
                  var3.setSAFSeqNumber(0L);
               }
            } catch (JMSException var10) {
            }
         }

         this.applyRedirectOverrides(var2, var3, true);
         synchronized(this.configurationLock) {
            var1.setSendOptions(this.createSendOptions(0L, (Sequence)null, var3));
            var1.setRedirectDestination(var2.getKernelDestination());
         }
      }

   }

   private void applyRedirectOverrides(BEDestinationImpl var1, MessageImpl var2, boolean var3) {
      var2.setDeliveryTime(0L);
      var2._setJMSRedeliveryLimit(-1);
      if (var3) {
         var2._setJMSExpiration(0L);
      }

      try {
         var1.applyOverrides(var2, (JMSProducerSendResponse)null);
      } catch (JMSException var5) {
      }

   }

   private boolean isFlowControlRequired() {
      return this.thresholdHandler.isArmed() || this.backEnd.needsFlowControl();
   }

   public long getBytesHigh() {
      return this.thresholdHandler.getBytesThresholdHigh();
   }

   public long getBytesLow() {
      return this.thresholdHandler.getBytesThresholdLow();
   }

   public long getMessagesHigh() {
      return this.thresholdHandler.getMessagesThresholdHigh();
   }

   public long getMessagesLow() {
      return this.thresholdHandler.getMessagesThresholdLow();
   }

   public long getMessagesThresholdTime() {
      return this.thresholdHandler.getMessagesThresholdTime();
   }

   public long getBytesThresholdTime() {
      return this.thresholdHandler.getBytesThresholdTime();
   }

   public void setBytesMaximum(long var1) {
      if (var1 <= 0L) {
         var1 = 2147483647L;
      }

      if (this.destination != null) {
         Quota var3 = (Quota)this.destination.getProperty("Quota");
         var3.setBytesMaximum(var1);
      }

   }

   public void setBytesHigh(long var1) {
      this.thresholdHandler.setBytesThresholdHigh(var1);
   }

   public void setBytesLow(long var1) {
      this.thresholdHandler.setBytesThresholdLow(var1);
   }

   public void setMessagesMaximum(long var1) {
      if (var1 <= 0L || var1 > 2147483647L) {
         var1 = 2147483647L;
      }

      if (this.destination != null) {
         Quota var3 = (Quota)this.destination.getProperty("Quota");
         var3.setMessagesMaximum((int)var1);
      }

   }

   public void setMessagesHigh(long var1) {
      this.thresholdHandler.setMessagesThresholdHigh(var1);
   }

   public void setMessagesLow(long var1) {
      this.thresholdHandler.setMessagesThresholdLow(var1);
   }

   public long getMessagesPendingCount() {
      return (long)this.statistics.getMessagesPending();
   }

   public long getMessagesCurrentCount() {
      return (long)(this.statistics.getMessagesCurrent() - this.statistics.getMessagesPending());
   }

   public long getMessagesHighCount() {
      return (long)this.statistics.getMessagesHigh();
   }

   public long getMessagesReceivedCount() {
      return (long)this.statistics.getMessagesReceived();
   }

   public long getBytesCurrentCount() {
      return this.statistics.getBytesCurrent() - this.statistics.getBytesPending();
   }

   public long getBytesPendingCount() {
      return this.statistics.getBytesPending();
   }

   public long getBytesHighCount() {
      return this.statistics.getBytesHigh();
   }

   public long getBytesReceivedCount() {
      return this.statistics.getBytesReceived();
   }

   public final void resetStatistics() {
   }

   public synchronized void setDestinationKeysList(List var1) {
      this.destinationKeysList = var1;
      if (var1 != null && !var1.isEmpty()) {
         this.comparator = new BEMessageComparator(var1);
         if (this.comparator.isDefault()) {
            this.comparator = null;
         }
      } else {
         this.comparator = null;
      }

   }

   public final void start() throws JMSException {
      try {
         this.destination.setProperty("Durable", new Boolean(!this.isTemporary() && this.backEnd.isStoreEnabled()));
         this.destination.resume(16384);
      } catch (KernelException var6) {
         throw new weblogic.jms.common.JMSException(var6);
      }

      if (this.runtimeMBean != null) {
         try {
            if (JMSDebug.JMSModule.isDebugEnabled()) {
               JMSDebug.JMSModule.debug("Registering runtimeMBean " + this.runtimeMBean + " on " + this.backEnd.getName());
            }

            PrivilegedActionUtilities.register(this.runtimeMBean, kernelId);
         } catch (ManagementException var4) {
            JMSException var2 = new JMSException("Failed to register the JMSServerRuntimeMBean " + this.name);
            var2.setLinkedException(var4);
            throw var2;
         }
      }

      this.advertise();
      this.prepareSignature();
      synchronized(this) {
         this.state = 4 | this.state & 393216;
         if (JMSDebug.JMSPauseResume.isDebugEnabled()) {
            JMSDebug.JMSPauseResume.debug(this.name + " During destination start(): " + " this.ProductionPausedAtStartup=" + this.productionPausedAtStartup + ", this.InsertionPausedAtStartup= " + this.insertionPausedAtStartup + ", this.ConsumptionPausedAtStartup=" + this.consumptionPausedAtStartup);
            JMSDebug.JMSPauseResume.debug(this.name + " During destination start(): " + " backEnd.getProductionPausedAtStartup()=" + this.backEnd.getProductionPausedAtStartup() + ", backEnd.getInsertionPausedAtStartup()= " + this.backEnd.getInsertionPausedAtStartup() + ", backEnd.getConsumptionPausedAtStartup()=" + this.backEnd.getConsumptionPausedAtStartup());
         }

         if (this.backEnd.getProductionPausedAtStartup().equals("default")) {
            if (!this.productionPausedAtStartup) {
               if (this.isProductionPaused()) {
                  if (JMSDebug.JMSPauseResume.isDebugEnabled()) {
                     JMSDebug.JMSPauseResume.debug("Resuming new message production on destination(" + this.name + "), because the destination has ProductionPausedAtStartup " + "parameter is either not set or set to true and the hosting JMSServer " + this.backEnd.getName() + " has the ProductionPausedAtStartup parameter set to default");
                  }

                  this.resumeProduction();
               }
            } else if (!this.isProductionPaused()) {
               if (JMSDebug.JMSPauseResume.isDebugEnabled()) {
                  JMSDebug.JMSPauseResume.debug("Pausing new message production on destination(" + this.name + "), because the destination has ProductionPausedAtStartup " + "parameter set to true and the hosting JMSServer " + this.backEnd.getName() + " has the ProductionPausedAtStartup parameter set to default");
               }

               this.pauseProduction();
            }
         } else if (this.backEnd.getProductionPausedAtStartup().equals("true")) {
            if (!this.isProductionPaused()) {
               if (JMSDebug.JMSPauseResume.isDebugEnabled()) {
                  JMSDebug.JMSPauseResume.debug("Pausing new message production on destination(" + this.name + "), because the hosting JMSServer(" + this.backEnd.getName() + ") has ProductionPausedAtStartup parameter set to true");
               }

               this.pauseProduction();
            }
         } else if (this.backEnd.getProductionPausedAtStartup().equals("false") && this.isProductionPaused()) {
            if (JMSDebug.JMSPauseResume.isDebugEnabled()) {
               JMSDebug.JMSPauseResume.debug("Resuming new message production on destination(" + this.name + "), because the hosting JMSServer(" + this.backEnd.getName() + ") has ProductionPausedAtStartup parameter set to false");
            }

            this.resumeProduction();
         }

         if (this.backEnd.getInsertionPausedAtStartup().equals("default")) {
            if (!this.insertionPausedAtStartup) {
               if (this.isInsertionPaused()) {
                  if (JMSDebug.JMSPauseResume.isDebugEnabled()) {
                     JMSDebug.JMSPauseResume.debug("Resuming message insertion (in-flight message insertion) on destination(" + this.name + "), because the destination has InsertionPausedAtStartup " + "parameter is either not set or set to true and the hosting JMSServer " + this.backEnd.getName() + " has the InsertionPausedAtStartup parameter set to default");
                  }

                  this.resumeInsertion();
               }
            } else if (!this.isInsertionPaused()) {
               if (JMSDebug.JMSPauseResume.isDebugEnabled()) {
                  JMSDebug.JMSPauseResume.debug("Pausing message insertion (in-flight message insertion) on destination(" + this.name + "), because the destination has InsertionPausedAtStartup " + "parameter set to true and the hosting JMSServer " + this.backEnd.getName() + " has the InsertionPausedAtStartup parameter set to default");
               }

               this.pauseInsertion();
            }
         } else if (this.backEnd.getInsertionPausedAtStartup().equals("true")) {
            if (!this.isInsertionPaused()) {
               if (JMSDebug.JMSPauseResume.isDebugEnabled()) {
                  JMSDebug.JMSPauseResume.debug("Pausing message insertion (in-flight message insertion) on destination(" + this.name + "), because the hosting JMSServer(" + this.backEnd.getName() + ") has InsertionPausedAtStartup parameter set to true");
               }

               this.pauseInsertion();
            }
         } else if (this.backEnd.getInsertionPausedAtStartup().equals("false") && this.isInsertionPaused()) {
            if (JMSDebug.JMSPauseResume.isDebugEnabled()) {
               JMSDebug.JMSPauseResume.debug("Resuming message insertion (in-flight message insertion) on destination(" + this.name + "), because the hosting JMSServer(" + this.backEnd.getName() + ") has InsertionPausedAtStartup parameter set to false");
            }

            this.resumeInsertion();
         }

         if (this.backEnd.getConsumptionPausedAtStartup().equals("default")) {
            if (!this.consumptionPausedAtStartup) {
               if (this.isConsumptionPaused()) {
                  if (JMSDebug.JMSPauseResume.isDebugEnabled()) {
                     JMSDebug.JMSPauseResume.debug("Resuming new message consumption on destination(" + this.name + "), because the destination has ConsumptionPausedAtStartup " + "parameter is either not set or set to true and the hosting JMSServer " + this.backEnd.getName() + " has the ConsumptionPausedAtStartup parameter set to default");
                  }

                  this.resumeConsumption();
               }
            } else if (!this.isConsumptionPaused()) {
               if (JMSDebug.JMSPauseResume.isDebugEnabled()) {
                  JMSDebug.JMSPauseResume.debug("Pausing new message consumption on destination(" + this.name + "), because the destination has ConsumptionPausedAtStartup " + "parameter set to true and the hosting JMSServer " + this.backEnd.getName() + " has the ConsumptionPausedAtStartup parameter set to default");
               }

               this.pauseConsumption();
            }
         } else if (this.backEnd.getConsumptionPausedAtStartup().equals("true")) {
            if (!this.isConsumptionPaused()) {
               if (JMSDebug.JMSPauseResume.isDebugEnabled()) {
                  JMSDebug.JMSPauseResume.debug("Pausing new message consumption on destination(" + this.name + "), because the hosting JMSServer(" + this.backEnd.getName() + ") has ConsumptionPausedAtStartup parameter set to true");
               }

               this.pauseConsumption();
            }
         } else if (this.backEnd.getConsumptionPausedAtStartup().equals("false") && this.isConsumptionPaused()) {
            if (JMSDebug.JMSPauseResume.isDebugEnabled()) {
               JMSDebug.JMSPauseResume.debug("Resuming new message consumption on destination(" + this.name + "), because the hosting JMSServer(" + this.backEnd.getName() + ") has ConsumptionPausedAtStartup parameter set to false");
            }

            this.resumeConsumption();
         }
      }

      this.isUp = true;
      ListIterator var1 = this.statusListeners.listIterator();

      while(var1.hasNext()) {
         ((DestinationStatusListener)var1.next()).onUpStatusChange(this);
      }

   }

   public final void suspend() {
      synchronized(this) {
         if (this.checkStateFlag(25)) {
            return;
         }

         this.state = 1;
      }

      this.shutdownInternal();
   }

   public final void shutdown() {
      synchronized(this) {
         if (this.checkStateFlag(16)) {
            return;
         }

         if (this.checkStateFlag(8)) {
            this.state = 16;
         }
      }

      this.shutdownInternal();
   }

   private void shutdownInternal() {
      this.isUp = false;
      String var1;
      if (this.deleted) {
         var1 = "Consumer destination was deleted";
      } else {
         var1 = "Consumer destination was closed";
      }

      if (this.runtimeMBean != null) {
         try {
            if (JMSDebug.JMSModule.isDebugEnabled()) {
               JMSDebug.JMSModule.debug("Unregistering runtimeMBean " + this.runtimeMBean + " on " + this.backEnd.getName());
            }

            PrivilegedActionUtilities.unregister(this.runtimeMBean, kernelId);
         } catch (ManagementException var5) {
            var5.printStackTrace();
         }
      }

      this.closeAllConsumers(var1);
      this.closeAllBrowsers(var1);
      this.unAdvertise();
      if (this.deleted) {
         try {
            KernelRequest var2 = new KernelRequest();
            this.destination.delete(var2);
         } catch (KernelException var4) {
            JMSLogger.logErrorUnregisteringBackEndDestination(this.backEnd.getName(), this, var4);
         }
      } else {
         try {
            this.destination.suspend(16384);
         } catch (KernelException var3) {
            JMSLogger.logErrorUnregisteringBackEndDestination(this.backEnd.getName(), this, var3);
         }
      }

      ListIterator var6 = this.statusListeners.listIterator();

      while(var6.hasNext()) {
         ((DestinationStatusListener)var6.next()).onUpStatusChange(this);
      }

   }

   protected abstract BEConsumerImpl createConsumer(BESessionImpl var1, boolean var2, BEConsumerCreateRequest var3) throws JMSException;

   abstract BEConnectionConsumerImpl createConnectionConsumer(JMSID var1, ServerSessionPool var2, String var3, String var4, String var5, boolean var6, int var7, long var8, boolean var10, boolean var11) throws JMSException;

   public boolean hasConsumers() {
      return !this.consumers.isEmpty();
   }

   public boolean isUp() {
      return this.isUp;
   }

   public boolean isLocal() {
      return true;
   }

   public boolean isPersistent() {
      DomainMBean var1 = ManagementService.getRuntimeAccess(KERNEL_ID).getDomain();
      JMSServerMBean var2 = null;
      String var3 = this.getBackEnd().getName();

      for(int var4 = 0; var4 < var1.getJMSServers().length; ++var4) {
         if (var1.getJMSServers()[var4].getName().equals(var3)) {
            var2 = var1.getJMSServers()[var4];
            break;
         }
      }

      boolean var5 = false;
      if (var2 != null) {
         var5 = var2.getStoreEnabled();
      }

      return var5 && this.deliveryModeOverride != 1;
   }

   public void addConsumer(BEConsumerCommon var1) throws JMSException {
      boolean var2 = false;
      if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
         JMSDebug.JMSBackEnd.debug("New consumer for " + this.name);
      }

      synchronized(this) {
         if (this.consumers.isEmpty()) {
            var2 = true;
         }

         if (!this.consumers.add(var1)) {
            throw new AssertionError("Duplicate consumer");
         }

         if (this.consumers.size() > this.consumersHigh) {
            this.consumersHigh = this.consumers.size();
         }

         ++this.consumersTotal;
      }

      if (var2) {
         ListIterator var3 = this.statusListeners.listIterator();

         while(var3.hasNext()) {
            ((DestinationStatusListener)var3.next()).onHasConsumersStatusChange(this);
         }
      }

   }

   public void removeConsumer(BEConsumerImpl var1, boolean var2) throws JMSException {
      boolean var3 = false;
      synchronized(this) {
         this.consumers.remove(var1);
         if (this.consumers.size() == 0) {
            var3 = true;
         }
      }

      if (var3) {
         ListIterator var4 = this.statusListeners.listIterator();

         while(var4.hasNext()) {
            ((DestinationStatusListener)var4.next()).onHasConsumersStatusChange(this);
         }
      }

   }

   public synchronized long getConsumersCurrentCount() {
      return (long)this.consumers.size();
   }

   public synchronized long getConsumersHighCount() {
      return (long)this.consumersHigh;
   }

   public synchronized long getConsumersTotalCount() {
      return this.consumersTotal;
   }

   public BEBrowser createBrowser(BESession var1, String var2) throws JMSException {
      throw new weblogic.jms.common.JMSException("Not implemented");
   }

   public synchronized Map<Object, Object> getConsumersClone() {
      HashMap var1 = new HashMap(this.consumers.size());
      Iterator var2 = this.consumers.iterator();

      while(var2.hasNext()) {
         Object var3 = var2.next();
         var1.put(var3, var3);
      }

      return var1;
   }

   protected void closeAllConsumers(String var1) {
      ArrayList var2;
      synchronized(this) {
         var2 = new ArrayList(this.consumers);
         this.consumers.clear();
      }

      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         try {
            BEConsumerImpl var4 = (BEConsumerImpl)var3.next();
            var4.closeWithError(var1);
         } catch (JMSException var5) {
         }
      }

   }

   protected void closeAllBrowsers(String var1) {
   }

   public final boolean isStarted() {
      return this.checkStateFlag(4);
   }

   public final boolean isShutdown() {
      return this.checkStateFlag(120);
   }

   public final synchronized boolean isShutdownOrSuspended() {
      return this.state == 0 || this.checkStateFlagFast(123);
   }

   public final synchronized boolean isShutdownOrSuspending() {
      return this.state == 0 || this.checkStateFlag(106);
   }

   public final void checkShutdown(String var1) throws JMSException {
      if (this.isShutdown()) {
         throw new IllegalStateException(this.getOfflineStateMessage(var1));
      }
   }

   public final void checkShutdownOrSuspended(String var1) throws JMSException {
      if (this.isShutdownOrSuspended()) {
         throw new IllegalStateException(this.getOfflineStateMessage(var1));
      }
   }

   public final void checkShutdownOrSuspendedNeedLock(String var1) throws JMSException {
      if (this.state == 0 || this.checkStateFlagFast(123)) {
         throw new IllegalStateException(this.getOfflineStateMessage(var1));
      }
   }

   public final synchronized void markSuspending() {
      if (!this.checkStateFlag(121)) {
         this.state = 2;
      }

   }

   public final int getStateValue() {
      return this.state;
   }

   public final void setStateValue(int var1) {
      this.state = var1;
      if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
         JMSDebug.JMSBackEnd.debug("New state for " + this.name + ": " + JMSService.getStateName(var1));
      }

   }

   public final synchronized String getState() {
      int var1 = this.state;
      if ((var1 & 4) != 0) {
         var1 &= -5;
         if (var1 == 0) {
            var1 = 4;
         }
      }

      return JMSService.getStateName(var1);
   }

   private String getOfflineStateMessage(String var1) {
      StringBuffer var2 = new StringBuffer(256);
      if (var1 != null) {
         var2.append("Failed to " + var1 + "because destination " + this.name);
      } else {
         var2.append("Destination " + this.name);
      }

      if (this.deleted) {
         var2.append(" is deleted.");
      } else if (this.isShutdown()) {
         var2.append(" is shutdown.");
      } else if (this.isProductionPaused()) {
         var2.append(" is being paused for production.");
      } else if (this.isInsertionPaused()) {
         var2.append(" is being paused for insertion.");
      } else if (this.isConsumptionPaused()) {
         var2.append(" is being paused for consumption.");
      } else {
         var2.append(" is suspended.");
      }

      return var2.toString();
   }

   public void deleteTempDestination() throws JMSException {
      synchronized(this) {
         if (!this.consumers.isEmpty()) {
            throw new weblogic.jms.common.JMSException("Temporary destination cannot be deleted, it still has consumers");
         }

         this.setDeleted(true);
      }

      if (this.runtimeMBean != null) {
         try {
            PrivilegedActionUtilities.unregister(this.runtimeMBean, kernelId);
         } catch (ManagementException var3) {
            JMSLogger.logErrorUnregisteringBackEndDestination(this.backEnd.getName(), this, var3);
            throw new weblogic.jms.common.JMSException("Error deleting temporary destination", var3);
         }
      }

   }

   public final void markShuttingDown() {
      boolean var1 = false;
      synchronized(this) {
         if ((this.runtimeMBean != null && this.runtimeMBean.isRegistered() || !this.isTemporary()) && (this.getStateValue() & 3) == 0) {
            var1 = true;
            this.setStateValue(8);
         }
      }

      if (this.runtimeMBean != null && var1) {
         try {
            PrivilegedActionUtilities.unregister(this.runtimeMBean, kernelId);
         } catch (ManagementException var6) {
         }
      }

      synchronized(this) {
         if (!this.isShutdownOrSuspended()) {
            this.setStateValue(8);
         }

      }
   }

   public synchronized boolean isDeleted() {
      return this.deleted;
   }

   public void expireReadersAtShutdown() {
   }

   protected void suspendKernelDestination(int var1) throws JMSException {
      try {
         this.destination.suspend(var1);
      } catch (KernelException var3) {
         throw new weblogic.jms.common.JMSException(var3);
      }
   }

   protected void resumeKernelDestination(int var1) throws JMSException {
      try {
         this.destination.resume(var1);
      } catch (KernelException var3) {
         throw new weblogic.jms.common.JMSException(var3);
      }
   }

   public void pause() {
      try {
         this.pauseProduction();
      } catch (JMSException var2) {
         if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
            JMSDebug.JMSBackEnd.debug("Error pausing destination: " + var2);
         }
      }

   }

   public void resume() {
      try {
         this.resumeProduction();
      } catch (JMSException var2) {
         if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
            JMSDebug.JMSBackEnd.debug("Error resuming destination: " + var2);
         }
      }

   }

   public boolean isPaused() {
      return this.isProductionPaused();
   }

   public void pauseProduction() throws JMSException {
      this.pauseProduction(true);
   }

   public void pauseProduction(boolean var1) throws JMSException {
      if (!this.isProductionPaused()) {
         String var2 = "Destination " + this.name + " has paused all send requests";
         this.checkShutdownOrSuspended(var2);
         this.setStateFlag(512);
         ListIterator var3 = this.statusListeners.listIterator();

         while(var3.hasNext()) {
            ((DestinationStatusListener)var3.next()).onProductionPauseChange(this);
         }

         if (var1) {
            JMSLogger.logProductionPauseOfDestination(this.name);
         }
      }

   }

   public void resumeProduction() throws JMSException {
      this.resumeProduction(true);
   }

   public void resumeProduction(boolean var1) throws JMSException {
      if (this.isProductionPaused()) {
         String var2 = "Destination " + this.name + " is resuming all send requests";
         this.checkShutdownOrSuspended(var2);
         this.setStateFlag(1024);
         this.clearStateFlag(1536);
         ListIterator var3 = this.statusListeners.listIterator();

         while(var3.hasNext()) {
            ((DestinationStatusListener)var3.next()).onProductionPauseChange(this);
         }

         if (var1) {
            JMSLogger.logProductionResumeOfDestination(this.name);
         }
      }

   }

   public boolean isProductionPaused() {
      return this.checkStateFlag(512);
   }

   public String getProductionPausedState() {
      return this.checkStateFlag(512) ? "Production-Paused" : "Production-Enabled";
   }

   public void pauseInsertion() throws JMSException {
      if (!this.isInsertionPaused()) {
         String var1 = "Destination " + this.name + " has paused message insertion";
         this.checkShutdownOrSuspended(var1);
         this.setStateFlag(2048);
         this.suspendKernelDestination(4);
         this.clearStateFlag(2048);
         this.setStateFlag(4096);
         ListIterator var2 = this.statusListeners.listIterator();

         while(var2.hasNext()) {
            ((DestinationStatusListener)var2.next()).onInsertionPauseChange(this);
         }

         JMSLogger.logInsertionPauseOfDestination(this.name);
      }

   }

   public void resumeInsertion() throws JMSException {
      if (this.isInsertionPaused()) {
         String var1 = "Destination " + this.name + " is resuming all message insertion";
         this.checkShutdownOrSuspended(var1);
         this.setStateFlag(8192);
         this.resumeKernelDestination(4);
         this.clearStateFlag(12288);
         ListIterator var2 = this.statusListeners.listIterator();

         while(var2.hasNext()) {
            ((DestinationStatusListener)var2.next()).onInsertionPauseChange(this);
         }

         JMSLogger.logInsertionResumeOfDestination(this.name);
      }

   }

   public boolean isInsertionPaused() {
      return this.checkStateFlag(6144);
   }

   public String getInsertionPausedState() {
      if (this.checkStateFlag(2048)) {
         return "Insertion-Pausing";
      } else {
         return this.checkStateFlag(4096) ? "Insertion-Paused" : "Insertion-Enabled";
      }
   }

   public void pauseConsumption() throws JMSException {
      this.pauseConsumption(true);
   }

   public void pauseConsumption(boolean var1) throws JMSException {
      if (!this.isConsumptionPaused()) {
         String var2 = "Destination " + this.name + " is paused for consumption";
         this.checkShutdownOrSuspended(var2);
         this.setStateFlag(16384);
         this.suspendKernelDestination(2);
         this.clearStateFlag(16384);
         this.setStateFlag(32768);
         ListIterator var3 = this.statusListeners.listIterator();

         while(var3.hasNext()) {
            ((DestinationStatusListener)var3.next()).onConsumptionPauseChange(this);
         }

         if (var1) {
            JMSLogger.logConsumptionPauseOfDestination(this.name);
         }
      }

   }

   public void resumeConsumption() throws JMSException {
      this.resumeConsumption(true);
   }

   public void resumeConsumption(boolean var1) throws JMSException {
      if (this.isConsumptionPaused()) {
         String var2 = "Destination " + this.name + " is resuming for consumption";
         this.checkShutdownOrSuspended(var2);
         this.setStateFlag(65536);
         this.resumeKernelDestination(2);
         this.clearStateFlag(98304);
         ListIterator var3 = this.statusListeners.listIterator();

         while(var3.hasNext()) {
            ((DestinationStatusListener)var3.next()).onConsumptionPauseChange(this);
         }

         if (var1) {
            JMSLogger.logConsumptionResumeOfDestination(this.name);
         }
      }

   }

   public boolean isConsumptionPaused() {
      return this.checkStateFlag(49152);
   }

   public String getConsumptionPausedState() {
      if (this.checkStateFlag(16384)) {
         return "Consumption-Pausing";
      } else {
         return this.checkStateFlag(32768) ? "Consumption-Paused" : "Consumption-Enabled";
      }
   }

   public int invoke(Request var1) throws JMSException {
      switch (var1.getMethodId()) {
         case 12052:
            return this.wrappedSend((BEProducerSendRequest)var1);
         case 17684:
            return this.tranForward((BEForwardRequest)var1);
         case 17940:
            return uooUpdate(var1);
         default:
            throw new weblogic.jms.common.JMSException("No such method " + this.getClass().getName() + ".<" + var1.getMethodId() + ">");
      }
   }

   private void sendInitialize(BEProducerSendRequest var1) throws JMSException {
      MessageTimeStamp.record(2, var1.getMessage());
      if ((this.state & 512) != 0) {
         throw new IllegalStateException("Destination " + this.name + " is paused for new message production");
      } else {
         this.checkPermission(var1);
         this.checkShutdownOrSuspendedNeedLock("send message");
         MessageImpl var2 = var1.getMessage();
         var1.setWorkManager(this.getBackEnd().getWorkManager());
         if (var2.isForwardable() || this.attachSenderMode != 2 && (!var2.isJMSXUserIDRequested() || this.attachSenderMode == 1)) {
            if (this.attachSenderMode == 1) {
               var2.setJMSXUserID((String)null);
            }
         } else {
            var2.setJMSXUserID(JMSSecurityHelper.getSimpleAuthenticatedName());
         }

         var2.setJMSDestinationImpl(this.destinationImpl);

         assert var1.getSequence() == null : "START state with SEQUENCE";

         var1.setSequence(this.findOrCreateKernelSequence(var1.getMessage()));
         if (var1.getMessage().getUnitOfOrder() == null) {
            String var3 = this.getDefaultUnitOfOrder();
            if (var3 != null) {
               var1.getMessage().setUnitOfOrderName(var3);
            }
         }

      }
   }

   protected boolean clientSendResumeNeedsNewThread() {
      return false;
   }

   public Sequence findOrCreateKernelSequence(MessageImpl var1) throws JMSException {
      if (this.hasNoSequence(var1)) {
         return null;
      } else {
         String var2 = this.getSequenceName(var1);
         int var3 = this.getSequenceMode(var1);

         try {
            return this.releasingSequence(var1.getControlOpcode()) ? this.destination.findSequence(var2) : this.destination.findOrCreateSequence(var2, var3);
         } catch (KernelException var5) {
            throw new weblogic.jms.common.JMSException(var5);
         }
      }
   }

   private boolean hasNoSequence(MessageImpl var1) throws JMSException {
      return this.getSequenceMode(var1) == 0;
   }

   private boolean releasingSequence(int var1) {
      return var1 != 0 && 131072 >= var1;
   }

   protected String getSequenceName(WLMessage var1) throws JMSException {
      String var2;
      if (this.getSequenceMode(var1) == 8) {
         var2 = var1.getStringProperty("JMS_BEA_UnitOfWork");
      } else {
         var2 = var1.getSAFSequenceName();
      }

      assert var2 != null;

      return var2;
   }

   public boolean isUOWDestination() {
      return this.isUOWDestination;
   }

   public void setUnitOfWorkHandlingPolicy(String var1) {
      this.isUOWDestination = var1.equals("SingleMessageDelivery");
   }

   public void setIncompleteWorkExpirationTime(int var1) {
      this.incompleteWorkExpirationTime = var1;
   }

   public int getIncompleteWorkExpirationTime() {
      return this.incompleteWorkExpirationTime;
   }

   protected int getSequenceMode(WLMessage var1) throws JMSException {
      if (this.isUOWDestination && var1.propertyExists("JMS_BEA_UnitOfWork") && var1.getStringProperty("JMS_BEA_UnitOfWork") != null) {
         if (!var1.propertyExists("JMS_BEA_UnitOfWorkSequenceNumber")) {
            throw new BadSequenceNumberException("A JMS Unit of Work message must have a sequence number");
         } else {
            return 8;
         }
      } else {
         boolean var2 = var1.getSAFSeqNumber() != 0L;
         return this.getSequenceMode(var2, this.checkForwarded(var1), ((MessageImpl)var1).isSAFNeedReorder());
      }
   }

   protected int getSequenceMode(boolean var1, boolean var2) {
      return this.getSequenceMode(var1, var2, false);
   }

   protected int getSequenceMode(boolean var1, boolean var2, boolean var3) {
      if (var1 && (var2 || var3)) {
         return var2 && !var3 ? 2 : 4;
      } else {
         return 0;
      }
   }

   protected boolean checkForwarded(WLMessage var1) {
      boolean var2 = ((MessageImpl)var1).isForwarded();
      if (var1.getDDForwarded() && !var2) {
         throw new AssertionError("DD Forwarded msg is not marked as forwarded");
      } else {
         return var2;
      }
   }

   protected boolean isReorderNeeded(WLMessage var1) {
      return ((MessageImpl)var1).isSAFNeedReorder();
   }

   private boolean sendIssueMessage(BEProducerSendRequest var1) throws JMSException {
      MessageImpl var2 = var1.getMessage();
      JMSProducerSendResponse var3 = var1.setupSendResponse();
      if (!var2.isOldMessage() && var3 != null) {
         var3.set90StyleMessageId();
      }

      this.applyOverrides(var2, var3);
      var2.setConnectionId(var1.getConnectionId());
      var2.setJMSDestinationImpl((DestinationImpl)null);
      if (var2.getAdjustedDeliveryMode() == 2 && !this.backEnd.isStoreEnabled()) {
         if (!this.backEnd.isAllowsPersistentDowngrade()) {
            throw new JMSException(JMSExceptionLogger.logNoPersistentMessages(this.name, this.backEnd.getName()));
         }

         var2.setAdjustedDeliveryMode(1);
         var2.setJMSDeliveryMode(1);
         if (var3 != null) {
            var3.setDeliveryMode(1);
         }
      }

      if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
         JMSDebug.JMSBackEnd.debug("Putting new message " + var2.getId() + " on " + this.name);
      }

      var1.setState(1103);
      BEExtension var4 = this.destExtension;
      int var5 = var1.getMessage().getControlOpcode();
      if (var5 != 0) {
         assert var4 == null || var5 == 196608 : "opcode value is " + Integer.toHexString(var5);

         return false;
      } else {
         try {
            KernelRequest var6 = this.destination.send(var2, this.createSendOptions(var1.getSendTimeout(), var1.getSequence(), var1.getMessage()));
            if (var6 == null) {
               MessageTimeStamp.record(8, var2);
               return false;
            } else {
               var1.setKernelRequest(var6);
               synchronized(var6) {
                  if (var6.hasResult()) {
                     MessageTimeStamp.record(8, var2);
                     return false;
                  } else {
                     var1.needOutsideResult();
                     var6.addListener(new DispatcherCompletionListener(var1), this.backEnd.getWorkManager());
                     MessageTimeStamp.record(8, var2);
                     return true;
                  }
               }
            }
         } catch (QuotaException var10) {
            throw new ResourceAllocationException(var10.toString(), var10);
         } catch (weblogic.messaging.kernel.IllegalStateException var11) {
            throw new IllegalStateException("Failed to send message to the destination " + this.name + ": " + var11.getMessage(), var11);
         } catch (KernelException var12) {
            if (var12.getCause() != null && var12.getCause() instanceof JMSException) {
               throw (JMSException)var12.getCause();
            } else {
               throw new weblogic.jms.common.JMSException(var12);
            }
         }
      }
   }

   public SendOptions createSendOptions(long var1, Sequence var3, MessageImpl var4) {
      SendOptions var5 = this.createSendOptionsInternal(var1, var3, var4);
      var5.setDeliveryTime(var4.getDeliveryTime());
      return var5;
   }

   protected SendOptions createSendOptionsInternal(long var1, Sequence var3, MessageImpl var4) {
      SendOptions var5 = new SendOptions();
      var5.setPersistent(var4.getAdjustedDeliveryMode() == 2);

      try {
         if (this.isUOWDestination && var4.propertyExists("JMS_BEA_UnitOfWork")) {
            if (this.getSequenceMode(var4) == 8 && var4.getExpirationTime() == 0L) {
               var5.setExpirationTime(Long.MAX_VALUE);
            } else {
               var5.setExpirationTime(var4.getExpirationTime());
            }
         } else {
            var5.setExpirationTime(var4.getExpirationTime());
         }
      } catch (Exception var7) {
      }

      var5.setRedeliveryLimit(var4.getRedeliveryLimit());
      var5.setGroup(var4.getGroup());
      if (var1 != 0L) {
         var5.setTimeout(var1);
      }

      if (var3 != null) {
         var5.setSequence(var3);
         if (var3.getMode() != 1) {
            var5.setSequenceNum(var4.getSAFSeqNumber());
         }
      }

      return var5;
   }

   private void sendComplete(BEProducerSendRequest var1) throws JMSException {
      var1.restoreResources(true);

      try {
         if (var1.getKernelRequest() != null) {
            var1.getKernelRequest().getResult();
         }
      } catch (QuotaException var3) {
         throw new ResourceAllocationException(var3.toString(), var3);
      } catch (weblogic.messaging.kernel.IllegalStateException var4) {
         throw new IllegalStateException("Destination " + this.name + " is paused for new message production", var4);
      } catch (KernelException var5) {
         if (var5.getCause() != null && var5.getCause() instanceof JMSException) {
            throw (JMSException)var5.getCause();
         }

         throw new weblogic.jms.common.JMSException(var5);
      }

      if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
         JMSDebug.JMSBackEnd.debug("Message " + var1.getMessage().getId() + " successfully enqueued");
      }

      JMSProducerSendResponse var2 = null;
      var2 = (JMSProducerSendResponse)var1.getResult();
      if (this.isFlowControlRequired()) {
         var2.setNeedsFlowControl(true);
         var2.setFlowControlTime(-1L);
      }

   }

   private int wrappedSend(BEProducerSendRequest var1) throws JMSException {
      boolean var2 = true;
      int var3 = 1104;

      try {
         var3 = this.send(var1);
         var2 = false;
      } finally {
         if (var2) {
            var1.restoreResources(false);
         } else if (var3 == Integer.MAX_VALUE) {
            var1.restoreResources(true);
         }

      }

      return var3;
   }

   private int send(BEProducerSendRequest var1) throws JMSException {
      while(true) {
         switch (var1.getState()) {
            case 0:
               var1.setState(1102);
               this.sendInitialize(var1);
               BEExtension var2 = this.destExtension;
               if (var2 == null) {
                  break;
               }

               var2.sendExtension(var1);
               synchronized(var1) {
                  int var4 = var1.getState();
                  if (var4 != 1102) {
                     return var4;
                  }
                  break;
               }
            case 1101:
               JMSException var3 = new JMSException("BEDestinationImpl BEExtension.SEND_WAIT_FOR_COMPLETE");
               if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
                  JMSDebug.JMSBackEnd.debug(var3.getMessage(), var3);
               }

               throw var3;
            case 1102:
               if (!this.sendIssueMessage(var1)) {
                  break;
               }

               return 1103;
            case 1103:
               this.sendComplete(var1);
               var1.setState(Integer.MAX_VALUE);
               return Integer.MAX_VALUE;
            case Integer.MAX_VALUE:
               return Integer.MAX_VALUE;
            default:
               throw new AssertionError("Invalid state " + var1.getState());
         }
      }
   }

   private int tranForward(BEForwardRequest var1) throws JMSException {
      this.checkPermission(var1);
      this.checkShutdownOrSuspendedNeedLock("forward message");
      if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
         JMSDebug.JMSBackEnd.debug("Received a tranForward request for " + var1.getSize() + " messages on " + this.name);
      }

      Transaction var2 = TransactionHelper.getTransactionHelper().getTransaction();
      if (var2 == null) {
         throw new weblogic.jms.common.JMSException("tranForward was invoked without a transaction");
      } else {
         MessageImpl var3;
         do {
            var3 = var1.getCurrentRequest().getMessage();
            if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
               JMSDebug.JMSBackEnd.debug("BEDestinationImpl.tranForward() message " + var3 + " deliveryCount " + var3.getDeliveryCount());
            }

            var1.incrementPosition();
            if (!(this instanceof BEQueueImpl)) {
               var3.setDDForwarded(true);
            }

            this.applyOverrides(var3, (JMSProducerSendResponse)null);

            try {
               SendOptions var4 = this.createSendOptions(0L, (Sequence)null, var3);
               if (var3.getDeliveryCount() > 0) {
                  var4.setDeliveryCount(var3.getDeliveryCount());
               }

               if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
                  JMSDebug.JMSBackEnd.debug("BEDestinationImpl.tranForward, got message " + var3 + " with deliverycount " + var3.getDeliveryCount() + " send options " + var4.getDeliveryCount());
               }

               KernelRequest var5 = this.destination.send(var3, var4);
               if (var5 != null) {
                  synchronized(var5) {
                     if (!var5.hasResult()) {
                        var1.needOutsideResult();
                        var5.addListener(new DispatcherCompletionListener(var1), this.backEnd.getWorkManager());
                        return var1.getState();
                     }

                     var5.getResult();
                  }
               }
            } catch (KernelException var9) {
               throw new weblogic.jms.common.JMSException(var9);
            }
         } while(var1.getPosition() < var1.getSize());

         if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
            JMSDebug.JMSBackEnd.debug("Processed all forwarded messages on " + this.name);
         }

         var1.setResult(new JMSProducerSendResponse(var3.getId()));
         var1.setState(Integer.MAX_VALUE);
         return Integer.MAX_VALUE;
      }
   }

   private static int uooUpdate(Request var0) throws JMSException {
      BEOrderUpdateRequest var1 = (BEOrderUpdateRequest)var0;

      try {
         PathHelper.manager().cachedRemove(PathHelper.DEFAULT_PATH_SERVICE_JNDI, var1.getKey(), var1.getOldMember(), 584);
      } catch (PathHelper.PathServiceException var3) {
         PathHelper.PathSvc.debug(var3.getMessage(), var3);
         throw new weblogic.jms.common.JMSException(var3);
      } catch (NamingException var4) {
         PathHelper.PathSvc.debug(var4.getMessage(), var4);
         throw new weblogic.jms.common.JMSException(var4);
      }

      var0.setResult(VoidResponse.THE_ONE);
      return Integer.MAX_VALUE;
   }

   static void addPropertyFlags(Destination var0, String var1, int var2) throws JMSException {
      synchronized(var0) {
         Integer var4 = (Integer)var0.getProperty(var1);
         if (var4 == null) {
            var4 = new Integer(var2);
         } else {
            if ((var4 & var2) == var2) {
               return;
            }

            var4 = new Integer(var4 | var2);
         }

         try {
            var0.setProperty(var1, var4);
         } catch (KernelException var7) {
            throw new weblogic.jms.common.JMSException(var7);
         }

      }
   }

   protected static void removePropertyFlags(Destination var0, String var1, int var2) throws JMSException {
      synchronized(var0) {
         Integer var4 = (Integer)var0.getProperty(var1);
         if (var4 != null && (var4 & var2) != 0) {
            var4 = new Integer(var4 & ~var2);

            try {
               var0.setProperty(var1, var4);
            } catch (KernelException var7) {
               throw new weblogic.jms.common.JMSException(var7);
            }

         }
      }
   }

   public void lowMemory() throws JMSException {
      this.suspendMessageLogging();
   }

   public void normalMemory() throws JMSException {
      this.resumeMessageLogging();
   }

   public final void adminDeletion() {
      BackEnd var1 = this.getBackEnd();
      synchronized(var1.getDestinationDeletionLock()) {
         synchronized(this.getConfigurationLock()) {
            synchronized(this) {
               try {
                  JMSService.getJMSService().checkShutdownOrSuspended("delete Destination");
                  var1.checkShutdownOrSuspended("delete Destination");
               } catch (JMSException var12) {
                  return;
               }

               this.setDeleted(true);
               Map var5 = this.getConsumersClone();
               var1.removeDestination(this);
               Iterator var6 = var5.values().iterator();

               while(var6.hasNext()) {
                  try {
                     BEConsumerCommon var7 = (BEConsumerCommon)var6.next();
                     if (var7.isDurable() && !var7.isActive()) {
                        var7.delete(false, false);
                     }
                  } catch (JMSException var11) {
                  }
               }

               var5.clear();
            }
         }
      }
   }

   public void dump(JMSDiagnosticImageSource var1, XMLStreamWriter var2) throws XMLStreamException, DiagnosticImageTimeoutException {
      var1.checkTimeout();
      JMSDiagnosticImageSource.dumpDestinationImpl(var2, this.destinationImpl);
      var2.writeAttribute("state", JMSService.getStateName(this.state));
      var2.writeAttribute("creationTime", String.valueOf(this.getCreationTime()));
      var2.writeAttribute("kernelDestinationName", this.destination.getName());
   }

   public synchronized void removeProducer(JMSID var1) {
      this.producers.remove(var1);
   }

   private void checkPermission(Request var1) throws JMSSecurityException {
      JMSID var2 = null;
      if (var1 instanceof BEProducerSendRequest) {
         var2 = ((BEProducerSendRequest)var1).getProducerId();
      }

      if (var2 == null) {
         this.verify((BEForwardRequest)var1);
      } else {
         if (!JMSService.getJMSService().isSecurityCheckerStop()) {
            BEProducerSecurityParticipantImpl var3;
            synchronized(this) {
               var3 = (BEProducerSecurityParticipantImpl)this.producers.get(var2);
            }

            AuthenticatedSubject var4 = JMSSecurityHelper.getCurrentSubject();
            if (var3 == null) {
               this.getJMSDestinationSecurity().checkSendPermission(var4);
               var3 = new BEProducerSecurityParticipantImpl(var2, this, 5, var4);
               synchronized(this) {
                  this.producers.put(var3.getProducerId(), var3);
               }

               JMSService.getJMSService().registerSecurityParticipant(this.getJMSDestinationSecurity().getJMSResourceForSend(), var3);
            } else if (var3.getSubject() != var4 && (var3.getSubject() == null || !var3.getSubject().equals(var4))) {
               this.getJMSDestinationSecurity().checkSendPermission(var4);
               var3.setSubject(var4);
            }
         } else {
            this.getJMSDestinationSecurity().checkSendPermission();
         }

      }
   }

   private boolean checkMember(int var1) {
      int var2 = 16;

      boolean var3;
      while(true) {
         var3 = DDManager.handlerHasSecurityModeByMemberName(this.getDestinationImpl().getName(), var1);
         if (var3) {
            break;
         }

         --var2;
         if (var2 == 0) {
            break;
         }

         try {
            Thread.currentThread();
            Thread.sleep(1000L);
         } catch (InterruptedException var5) {
         }
      }

      return var3;
   }

   private void verify(BEForwardRequest var1) throws JMSSecurityException {
      if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
         JMSDebug.JMSBackEnd.debug("Verifying forward request: securityMode= " + var1.getSecurityMode() + " current user= " + JMSSecurityHelper.getSimpleAuthenticatedName());
      }

      String var2 = null;
      String var3 = "";

      try {
         switch (var1.getSecurityMode()) {
            case 11:
            case 13:
               var3 = "REMOTE_SIGNED";
               if (!var1.verify(this.signatureSecret)) {
                  var2 = "Verification failed.";
               }
               break;
            case 12:
               var3 = "REMOTE_UNSIGNED";
               if (!this.checkMember(12)) {
                  var2 = "Not initialized for perf mode, will retry later.";
               }
               break;
            case 14:
               var3 = "REMOTE_KERNELID";
               if (!this.checkMember(14)) {
                  var2 = "Not initialized for remote user, will retry later.";
               } else if (!WLSPrincipals.isKernelUsername(JMSSecurityHelper.getSimpleAuthenticatedName())) {
                  var2 = "Access denied for remote user, user=" + JMSSecurityHelper.getSimpleAuthenticatedName();
               }
               break;
            case 15:
               var3 = "LOCAL_KERNELID";
               if (!WLSPrincipals.isKernelUsername(JMSSecurityHelper.getSimpleAuthenticatedName())) {
                  var2 = "Access denied for local user, user=" + JMSSecurityHelper.getSimpleAuthenticatedName();
               }
               break;
            default:
               var3 = "default";
               var2 = "Unexpected mode.";
         }
      } finally {
         if (var2 != null) {
            var2 = JMSExceptionLogger.logDDForwardRequestDeniedLoggable(var2, this.getName()).getMessage();
            if (var2 == null) {
               var2 = "Access denied.";
            }
         }

         if (BEForwardingConsumer.DD_FORWARDING_DEBUG) {
            System.out.println("CHECKING FOR " + var3 + ", " + var1.getSecurityMode() + ", " + JMSSecurityHelper.getSimpleAuthenticatedName() + ", exc=" + var2);
         }

         if (var2 != null) {
            throw new JMSSecurityException(var2);
         }

      }

   }

   public void prepareSignature() {
      RuntimeAccess var1 = ManagementService.getRuntimeAccess(kernelId);
      String var2 = var1.getDomainName();
      String var3 = var1.getServer().getCluster() != null ? var1.getServer().getCluster().getName() : null;
      if (var3 != null) {
         this.signatureSecret = JMSServerUtilities.generateSecret(var2 + var3 + this.getName() + this.destinationImpl.getId());
      }

   }
}
