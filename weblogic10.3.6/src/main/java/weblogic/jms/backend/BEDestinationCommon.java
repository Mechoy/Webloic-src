package weblogic.jms.backend;

import java.security.AccessController;
import java.text.ParseException;
import java.util.List;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.naming.Context;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import weblogic.descriptor.BeanUpdateRejectedException;
import weblogic.jms.JMSExceptionLogger;
import weblogic.jms.JMSLogger;
import weblogic.jms.JMSService;
import weblogic.jms.common.DestinationImpl;
import weblogic.jms.common.EntityName;
import weblogic.jms.common.JMSDestinationSecurity;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSMessageExpirationHelper;
import weblogic.jms.common.JMSMessageLogHelper;
import weblogic.jms.common.JMSProducerSendResponse;
import weblogic.jms.common.JMSServerUtilities;
import weblogic.jms.common.MessageImpl;
import weblogic.jms.common.MessageStatisticsLogger;
import weblogic.jms.common.WrappedDestinationImpl;
import weblogic.jms.dispatcher.Invocable;
import weblogic.jms.extensions.Schedule;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.management.runtime.JMSDestinationRuntimeMBean;
import weblogic.messaging.ID;
import weblogic.messaging.common.PrivilegedActionUtilities;
import weblogic.messaging.dispatcher.InvocableMonitor;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public abstract class BEDestinationCommon implements Invocable, MessageStatisticsLogger, ActiveMessageExpiration {
   public static final int NOT_TEMPORARY = 1;
   public static final int TEMPORARY = 0;
   public static final int SUPPORTS = 0;
   public static final int NEVER = 1;
   public static final int AUTH_PRINS = 2;
   public static final String DEFAULT_TIME_TO_DELIVER_OVERRIDE = "-1";
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   protected final BackEnd backEnd;
   protected final String name;
   protected String moduleName;
   protected BEDestinationRuntimeMBeanImpl runtimeMBean;
   private final InvocableMonitor invocableMonitor;
   private long creationTime = 1L;
   protected int expirationPolicy = 1;
   private List expirationLoggingJMSHeaders = null;
   private List expirationLoggingUserProperties = null;
   protected int maximumMessageSize = Integer.MAX_VALUE;
   private boolean messageLoggingEnabled = false;
   private String messageLoggingFormat = null;
   private List messageLoggingJMSHeaders = null;
   private List messageLoggingUserProperties = null;
   protected List destinationKeysList;
   private boolean bound;
   private boolean localBound;
   private boolean applicationBound;
   protected boolean ddBound;
   protected String jndiName;
   private String localJNDIName;
   private boolean advertised = false;
   protected DestinationImpl destinationImpl;
   protected String internalDistributedJNDIName;
   protected final JMSID id;
   private JMSID connectionId;
   protected int deliveryModeOverride = -1;
   protected int duration;
   private int priorityOverride;
   protected final Object configurationLock = new Object();
   private long redeliveryDelayOverride;
   private int redeliveryLimit;
   private boolean foundBEErrorDestination;
   private BEDestinationImpl errorDestination;
   private EntityName errorDestinationName;
   private String timeToDeliverOverride;
   private long timeToLiveOverride;
   protected int attachSenderMode = 0;
   protected String safExportPolicy = "All";
   protected boolean defaultTargetingEnabled;
   protected boolean productionPausedAtStartup;
   protected boolean insertionPausedAtStartup;
   protected boolean consumptionPausedAtStartup;
   private String jmsCreateDestinationIdentifier;
   private String expirationLoggingPolicy;
   private String defaultUnitOfOrder;
   private String applicationJNDIName;
   private Context applicationContext;
   private JMSDestinationSecurity jmsDestinationSecurity;
   private final String DEFAULT_UOO_NAME;

   protected BEDestinationCommon(BackEnd var1, String var2, boolean var3, JMSDestinationSecurity var4) {
      this.backEnd = var1;
      this.name = var2;
      this.duration = var3 ? 0 : 1;
      this.id = JMSService.getJMSService().getNextId();
      this.invocableMonitor = var1.getInvocableMonitor();
      this.creationTime = System.currentTimeMillis();
      this.jmsDestinationSecurity = var4;
      RuntimeAccess var5 = ManagementService.getRuntimeAccess(kernelId);
      String var6 = var5.getDomainName();
      this.DEFAULT_UOO_NAME = "UOO-" + var6 + ":" + var2;
   }

   public abstract void setStateFlag(int var1);

   public abstract void clearStateFlag(int var1);

   public abstract void checkShutdown(String var1) throws JMSException;

   public final BackEnd getBackEnd() {
      return this.backEnd;
   }

   final void setRuntimeMBean(BEDestinationRuntimeMBeanImpl var1) {
      this.runtimeMBean = var1;
   }

   public final JMSDestinationRuntimeMBean getRuntimeMBean() {
      return this.runtimeMBean;
   }

   public final long getCreationTime() {
      return this.creationTime;
   }

   public final void setProductionPausedAtStartup(boolean var1) {
      this.productionPausedAtStartup = var1;
   }

   public final void setInsertionPausedAtStartup(boolean var1) {
      this.insertionPausedAtStartup = var1;
   }

   public final void setConsumptionPausedAtStartup(boolean var1) {
      this.consumptionPausedAtStartup = var1;
   }

   public void setMaximumMessageSize(int var1) {
      this.maximumMessageSize = var1;
   }

   public final DestinationImpl getDestinationImpl() {
      return this.destinationImpl;
   }

   public final void setDestinationImpl(DestinationImpl var1) {
      this.destinationImpl = var1;
   }

   private final void internalValJndiName(String var1, boolean var2) throws BeanUpdateRejectedException {
      var1 = JMSServerUtilities.transformJNDIName(var1);
      if (var1 != null) {
         if (this.advertised) {
            String var3;
            if (!var2) {
               var3 = this.jndiName;
            } else {
               var3 = this.localJNDIName;
            }

            if (var3 != null && var3.equals(var1)) {
               return;
            }
         }

         Context var10 = JMSService.getContext(!var2);
         Object var4 = null;

         for(int var5 = 0; var5 < 40; ++var5) {
            try {
               var4 = var10.lookup(var1);
            } catch (NameNotFoundException var7) {
               return;
            } catch (NamingException var8) {
               throw new BeanUpdateRejectedException(var8.getMessage(), var8);
            }

            try {
               Thread.sleep(500L);
            } catch (InterruptedException var9) {
               break;
            }
         }

         throw new BeanUpdateRejectedException("The proposed " + (var2 ? "Local" : "") + " JNDI name " + var1 + " for destination " + this.name + " is already bound by another object of type " + (var4 == null ? "null" : var4.getClass().getName()));
      }
   }

   public final void valJNDIName(String var1) throws BeanUpdateRejectedException {
      this.internalValJndiName(var1, false);
   }

   public final void setJNDIName(String var1) throws IllegalArgumentException {
      var1 = JMSServerUtilities.transformJNDIName(var1);
      String var2 = var1 == null ? "" : var1;
      String var3 = this.jndiName == null ? "" : this.jndiName;
      if (this.advertised && !var2.equals(var3)) {
         boolean var4 = false;
         if (var1 != null) {
            try {
               PrivilegedActionUtilities.bindAsSU(JMSService.getContext(true), var1, new WrappedDestinationImpl(this.destinationImpl), kernelId);
               var4 = true;
            } catch (NamingException var7) {
               if (!(var7 instanceof NameAlreadyBoundException)) {
                  throw new IllegalArgumentException("Error binding destination to JNDI (jndi name = " + var1 + ")");
               }

               JMSLogger.logNameConflictChangingGlobalJNDIName(this.jndiName, var1, this.name, this.moduleName);
            }
         }

         if (this.bound) {
            try {
               PrivilegedActionUtilities.unbindAsSU(JMSService.getContext(true), this.jndiName, kernelId);
            } catch (NamingException var6) {
               JMSLogger.logCouldNotUnbindGlobalJNDIName(this.jndiName, this.name, this.moduleName);
            }
         }

         this.bound = var4;
         if (this.bound) {
            this.setStateFlag(131072);
         } else {
            this.clearStateFlag(131072);
         }
      }

      this.jndiName = var1;
   }

   public final void valLocalJNDIName(String var1) throws BeanUpdateRejectedException {
      this.internalValJndiName(var1, true);
   }

   public final String getJNDIName() {
      return this.jndiName;
   }

   public final String getLocalJNDIName() {
      return this.localJNDIName;
   }

   public final void setLocalJNDIName(String var1) throws IllegalArgumentException {
      var1 = JMSServerUtilities.transformJNDIName(var1);
      String var2 = var1 == null ? "" : var1;
      String var3 = this.localJNDIName == null ? "" : this.localJNDIName;
      if (this.advertised && !var2.equals(var3)) {
         boolean var4 = false;
         if (var1 != null) {
            try {
               PrivilegedActionUtilities.bindAsSU(JMSService.getContext(false), var1, this.destinationImpl, kernelId);
               var4 = true;
            } catch (NamingException var7) {
               if (!(var7 instanceof NameAlreadyBoundException)) {
                  throw new IllegalArgumentException("Error binding destination to local JNDI (jndi name = " + var1 + ")");
               }

               JMSLogger.logNameConflictChangingLocalJNDIName(this.localJNDIName, var1, this.name, this.moduleName);
            }
         }

         if (this.localBound) {
            try {
               PrivilegedActionUtilities.unbindAsSU(JMSService.getContext(false), this.localJNDIName, kernelId);
            } catch (NamingException var6) {
               JMSLogger.logCouldNotUnbindLocalJNDIName(this.localJNDIName, this.name, this.moduleName);
            }
         }

         this.localBound = var4;
         if (this.localBound) {
            this.setStateFlag(262144);
         } else {
            this.clearStateFlag(262144);
         }
      }

      this.localJNDIName = var1;
   }

   void setApplicationJNDIName(String var1) {
      var1 = JMSServerUtilities.transformJNDIName(var1);
      this.applicationJNDIName = var1;
   }

   void setApplicationContext(Context var1) {
      this.applicationContext = var1;
   }

   public final int getDestType() {
      return this.destinationImpl.getType();
   }

   public final void logMessagesThresholdHigh() {
      JMSLogger.logMessagesThresholdHighDestination(this.backEnd.getName(), this.name);
   }

   public final void logMessagesThresholdLow() {
      JMSLogger.logMessagesThresholdLowDestination(this.backEnd.getName(), this.name);
   }

   public final void logBytesThresholdHigh() {
      JMSLogger.logBytesThresholdHighDestination(this.backEnd.getName(), this.name);
   }

   public final void logBytesThresholdLow() {
      JMSLogger.logBytesThresholdLowDestination(this.backEnd.getName(), this.name);
   }

   public final void setPriority(int var1) {
      this.priorityOverride = var1;
   }

   public final Object getConfigurationLock() {
      return this.configurationLock;
   }

   public final long getDirtyRedeliveryDelayOverride() {
      return this.redeliveryDelayOverride;
   }

   public final int getDirtyRedeliveryLimit() {
      return this.redeliveryLimit;
   }

   public final long getRedeliveryDelay() {
      synchronized(this.configurationLock) {
         return this.redeliveryDelayOverride;
      }
   }

   public final void setRedeliveryDelay(long var1) {
      synchronized(this.configurationLock) {
         this.redeliveryDelayOverride = var1;
      }
   }

   public void setRedeliveryLimit(int var1) {
      synchronized(this.configurationLock) {
         this.redeliveryLimit = var1;
      }
   }

   public final BEDestinationImpl getBEErrorDestination() {
      synchronized(this.configurationLock) {
         if (this.foundBEErrorDestination) {
            return this.errorDestination;
         } else {
            this.errorDestination = this.errorDestinationName == null ? null : this.backEnd.findDestination(this.errorDestinationName.toString());
            this.foundBEErrorDestination = true;
            return this.errorDestination;
         }
      }
   }

   public final String getTimeToDeliver() {
      synchronized(this.configurationLock) {
         return this.timeToDeliverOverride;
      }
   }

   public final void setTimeToDeliver(String var1) {
      synchronized(this.configurationLock) {
         this.timeToDeliverOverride = var1;
      }
   }

   public final long getTimeToDeliverOverrideInMillisRelative() {
      String var1 = this.getTimeToDeliver();
      if (var1 != null && var1 != "-1" && var1.trim().length() != 0) {
         try {
            return Long.parseLong(var1);
         } catch (NumberFormatException var4) {
            try {
               return Schedule.nextScheduledTimeInMillisRelative(var1, System.currentTimeMillis());
            } catch (ParseException var3) {
               return 0L;
            }
         }
      } else {
         return -1L;
      }
   }

   public final long getTimeToLive() {
      synchronized(this.configurationLock) {
         return this.timeToLiveOverride;
      }
   }

   public final void setTimeToLive(long var1) {
      synchronized(this.configurationLock) {
         this.timeToLiveOverride = var1;
      }
   }

   public final void setDeliveryMode(String var1) {
      if (var1 == null) {
         this.deliveryModeOverride = -1;
      } else {
         if (var1.equalsIgnoreCase("Non-Persistent")) {
            this.deliveryModeOverride = 1;
         } else if (var1.equalsIgnoreCase("Persistent")) {
            this.deliveryModeOverride = 2;
         } else {
            this.deliveryModeOverride = -1;
         }

      }
   }

   public void setDestinationKeysList(List var1) {
      this.destinationKeysList = var1;
   }

   public final boolean isTemporary() {
      return this.duration == 0;
   }

   public final void setDuration(int var1) {
      this.duration = var1;
   }

   public void setDestinationKeys(String[] var1) {
   }

   public final String getName() {
      return this.name;
   }

   public final Destination getDestination() {
      return this.getDestinationImpl();
   }

   public final String getDestinationType() {
      int var1 = this.getDestType();
      return var1 != 1 && var1 != 4 ? "Topic" : "Queue";
   }

   public abstract int getDestinationTypeIndicator();

   public final void setConnectionId(JMSID var1) {
      this.connectionId = var1;
   }

   public final JMSID getConnectionId() {
      return this.connectionId;
   }

   public final void setExpirationPolicy(String var1) {
      this.expirationPolicy = expirationPolicyIntFromString(var1);
   }

   public static final int expirationPolicyIntFromString(String var0) {
      if (var0 == null) {
         return 1;
      } else if (var0.equalsIgnoreCase("Log")) {
         return 2;
      } else {
         return var0.equalsIgnoreCase("Redirect") ? 4 : 1;
      }
   }

   public final List getExpirationLoggingJMSHeaders() {
      return this.expirationLoggingJMSHeaders;
   }

   private final void setExpirationLoggingJMSHeaders(List var1) {
      this.expirationLoggingJMSHeaders = var1;
   }

   public final List getExpirationLoggingUserProperties() {
      return this.expirationLoggingUserProperties;
   }

   private final void setExpirationLoggingUserProperties(List var1) {
      this.expirationLoggingUserProperties = var1;
   }

   public final InvocableMonitor getInvocableMonitor() {
      return this.invocableMonitor;
   }

   public final JMSID getJMSID() {
      return this.id;
   }

   public ID getId() {
      return this.getJMSID();
   }

   private static int attachStringToAttachMode(String var0) {
      if (var0 == null) {
         return 0;
      } else if (var0.equalsIgnoreCase("supports")) {
         return 0;
      } else if (var0.equalsIgnoreCase("never")) {
         return 1;
      } else {
         return var0.equalsIgnoreCase("always") ? 2 : 0;
      }
   }

   public final void setAttachSender(String var1) {
      this.attachSenderMode = attachStringToAttachMode(var1);
   }

   public String getSAFExportPolicy() {
      return this.safExportPolicy;
   }

   public void setSAFExportPolicy(String var1) {
      if (var1 == null) {
         this.safExportPolicy = "All";
      } else {
         this.safExportPolicy = var1;
      }

   }

   public boolean isDefaultTargetingEnabled() {
      return this.defaultTargetingEnabled;
   }

   public void setDefaultTargetingEnabled(boolean var1) {
      this.defaultTargetingEnabled = var1;
   }

   protected void applyOverrides(MessageImpl var1, JMSProducerSendResponse var2) throws JMSException {
      long var3;
      if ((var3 = (long)this.deliveryModeOverride) != -1L) {
         var1.setJMSDeliveryMode((int)var3);
         if (var2 != null) {
            var2.setDeliveryMode((int)var3);
         }
      }

      if ((var3 = (long)this.priorityOverride) != -1L) {
         var1.setJMSPriority((int)var3);
         if (var2 != null) {
            var2.setPriority((int)var3);
         }
      }

      if (!var1.getDDForwarded()) {
         if ((var3 = this.getTimeToLive()) >= 0L) {
            if (var3 > 0L) {
               long var5 = System.currentTimeMillis() + var3;
               if (var5 <= 0L) {
                  var5 = Long.MAX_VALUE;
               }

               var1.setJMSExpiration(var5);
            } else {
               var1.setJMSExpiration(0L);
            }

            if (var2 != null) {
               var2.setTimeToLive(var3);
            }
         }

         var3 = this.getTimeToDeliverOverrideInMillisRelative();
         if (var3 >= 0L) {
            if (var3 > 0L) {
               var1.setDeliveryTime(System.currentTimeMillis() + var3);
            } else {
               var1.setDeliveryTime(0L);
            }

            if (var2 != null) {
               var2.setTimeToDeliver(var3);
            }
         }
      }

      if ((var3 = (long)this.redeliveryLimit) != -1L) {
         var1.setJMSRedeliveryLimit((int)var3);
         if (var2 != null) {
            var2.setRedeliveryLimit((int)var3);
         }
      }

   }

   protected void advertise() throws JMSException {
      this.advertiseDestinationImpl();
      this.advertised = true;
   }

   private void advertiseDestinationImpl() throws JMSException {
      this.checkShutdown("start destination");
      if (this.duration != 0) {
         if (!this.bound && this.jndiName != null) {
            try {
               PrivilegedActionUtilities.bindAsSU(JMSService.getContext(true), this.jndiName, new WrappedDestinationImpl(this.destinationImpl), kernelId);
               this.bound = true;
               this.setStateFlag(131072);
            } catch (NamingException var4) {
               if (!(var4 instanceof NameAlreadyBoundException)) {
                  throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logBindNamingExceptionLoggable(this.name, this.jndiName), var4);
               }

               JMSLogger.logNameConflictBindingGlobalJNDIName(this.jndiName, this.name, this.moduleName);
            }
         }

         if (!this.localBound && this.getLocalJNDIName() != null) {
            try {
               PrivilegedActionUtilities.bindAsSU(JMSService.getContext(false), this.getLocalJNDIName(), this.destinationImpl, kernelId);
               this.localBound = true;
               this.setStateFlag(262144);
            } catch (NamingException var3) {
               if (!(var3 instanceof NameAlreadyBoundException)) {
                  throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logLocalBindNamingExceptionLoggable(this.name, this.localJNDIName), var3);
               }

               JMSLogger.logNameConflictBindingLocalJNDIName(this.localJNDIName, this.name, this.moduleName);
            }
         }

         if (this.applicationJNDIName != null && this.applicationContext != null) {
            try {
               PrivilegedActionUtilities.bindAsSU(this.applicationContext, this.applicationJNDIName, this.destinationImpl, kernelId);
               this.applicationBound = true;
            } catch (NamingException var2) {
               throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logAppBindNamingExceptionLoggable(this.name, this.applicationJNDIName), var2);
            }
         }

      }
   }

   public void unAdvertise() {
      this.advertised = false;
      this.unAdvertiseDestinationImpl();
   }

   private void unAdvertiseDestinationImpl() {
      if (!this.isTemporary()) {
         if (this.jndiName != null && this.bound) {
            try {
               PrivilegedActionUtilities.unbindAsSU(JMSService.getContext(true), this.jndiName, kernelId);
               this.bound = false;
               this.clearStateFlag(131072);
            } catch (NamingException var4) {
            }
         }

         if (this.getLocalJNDIName() != null && this.localBound) {
            try {
               PrivilegedActionUtilities.unbindAsSU(JMSService.getContext(false), this.getLocalJNDIName(), kernelId);
               this.localBound = false;
               this.clearStateFlag(262144);
            } catch (NamingException var3) {
            }
         }

         if (this.applicationBound && this.applicationContext != null && this.applicationJNDIName != null) {
            try {
               PrivilegedActionUtilities.unbindAsSU(this.applicationContext, this.applicationJNDIName, kernelId);
               this.applicationBound = false;
            } catch (NamingException var2) {
            }
         }
      }

   }

   public void setErrorDestination(EntityName var1) {
      synchronized(this.configurationLock) {
         this.errorDestinationName = var1;
         this.foundBEErrorDestination = false;
      }
   }

   public void setExpirationLoggingPolicy(String var1) {
      this.expirationLoggingPolicy = var1;
      StringBuffer var2 = new StringBuffer(256);
      List var3 = JMSMessageExpirationHelper.extractJMSHeaderAndProperty(this.expirationLoggingPolicy, var2);
      List var4 = JMSMessageExpirationHelper.convertStringToLinkedList(var2.toString());
      this.setExpirationLoggingJMSHeaders(var3);
      this.setExpirationLoggingUserProperties(var4);
   }

   public final String getJMSCreateDestinationIdentifier() {
      return this.jmsCreateDestinationIdentifier;
   }

   public final void setJMSCreateDestinationIdentifier(String var1) {
      this.jmsCreateDestinationIdentifier = var1;
   }

   public void setMessageLoggingEnabled(boolean var1) {
      this.messageLoggingEnabled = var1;
   }

   public boolean isMessageLoggingEnabled() {
      return JMSService.getJMSService().shouldMessageLogAll() ? true : this.messageLoggingEnabled;
   }

   public final List getMessageLoggingJMSHeaders() {
      return this.messageLoggingJMSHeaders;
   }

   private final void setMessageLoggingJMSHeaders(List var1) {
      this.messageLoggingJMSHeaders = var1;
   }

   public final List getMessageLoggingUserProperties() {
      return this.messageLoggingUserProperties;
   }

   private final void setMessageLoggingUserProperties(List var1) {
      this.messageLoggingUserProperties = var1;
   }

   public void setMessageLoggingFormat(String var1) {
      this.messageLoggingFormat = var1;
      StringBuffer var2 = new StringBuffer(256);
      List var3 = JMSMessageLogHelper.extractJMSHeaderAndProperty(this.messageLoggingFormat, var2);
      List var4 = JMSMessageLogHelper.convertStringToLinkedList(var2.toString());
      this.setMessageLoggingJMSHeaders(var3);
      this.setMessageLoggingUserProperties(var4);
   }

   public void setDefaultUnitOfOrder(boolean var1) throws IllegalArgumentException {
      this.defaultUnitOfOrder = var1 ? this.DEFAULT_UOO_NAME : null;
   }

   public String getDefaultUnitOfOrder() {
      return this.defaultUnitOfOrder;
   }

   public final String getListenerName() {
      return this.getName();
   }

   public final JMSDestinationSecurity getJMSDestinationSecurity() {
      return this.jmsDestinationSecurity;
   }

   public boolean isAvailableForCreateDestination() {
      if (this.destinationImpl != null && this.jmsCreateDestinationIdentifier == null && this.destinationImpl.getModuleName() != null) {
         return this.jndiName != null || this.localJNDIName != null;
      } else {
         return true;
      }
   }
}
