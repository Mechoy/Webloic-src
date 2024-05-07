package weblogic.jms.dd;

import java.security.AccessController;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import weblogic.jms.backend.BEDestinationImpl;
import weblogic.jms.backend.BackEnd;
import weblogic.jms.common.DistributedDestinationImpl;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSServerId;
import weblogic.jms.module.JMSDeploymentHelper;
import weblogic.jms.server.DestinationStatus;
import weblogic.jms.server.DestinationStatusListener;
import weblogic.management.provider.ManagementService;
import weblogic.messaging.dispatcher.DispatcherId;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class DDMember implements DestinationStatusListener, DestinationStatus, Cloneable, Runnable {
   static final long serialVersionUID = -7244423966789145785L;
   private String name;
   private boolean isProductionPaused;
   private boolean isInsertionPaused;
   private boolean isConsumptionPaused;
   private boolean hasConsumers;
   private boolean isUp = false;
   private boolean isForwardingUp = false;
   private boolean isDestinationUp = false;
   private boolean isLocal = false;
   private boolean isPersistent;
   private int weight = 1;
   private BEDestinationImpl destination = null;
   private List statusListeners;
   private DistributedDestinationImpl ddImpl = null;
   private boolean ddImplOutOfDate = true;
   private String jmsServerName;
   private String persistentStoreName;
   private String wlsServerName;
   private String migratableTargetName;
   private String domainName = null;
   private JMSServerId backEndId;
   private JMSID destinationId;
   private DispatcherId dispatcherId;
   private short events = 0;
   public static final short HAS_CONSUMERS_CHANGE = 1;
   public static final short INSERTION_PAUSED_CHANGE = 2;
   public static final short PRODUCTION_PAUSED_CHANGE = 4;
   public static final short CONSUMPTION_PAUSED_CHANGE = 8;
   public static final short UP_CHANGE = 16;
   public static final short WEIGHT_CHANGE = 32;
   private String globalJNDIName = null;
   private String localJNDIName = null;
   private DDHandler ddHandler = null;
   public static final int SECURITY_MODE_REMOTE_SIGNED = 11;
   public static final int SECURITY_MODE_REMOTE_UNSIGNED = 12;
   public static final int SECURITY_MODE_REMOTE_SIGNEDFULL = 13;
   public static final int SECURITY_MODE_REMOTE_KERNELID = 14;
   public static final int SECURITY_MODE_LOCAL_KERNELID = 15;
   public static final int SECURITY_MODE_UNKNOWN = 16;
   private int remoteSecurityMode = 16;
   private static final int SECURITY_MODE_FOR_WIRE = getSecurityModeForWire();

   public DDMember(String var1) {
      this.name = var1;
      this.isInsertionPaused = false;
      this.isConsumptionPaused = false;
      this.isProductionPaused = false;
      this.hasConsumers = false;
      this.isPersistent = false;
      this.jmsServerName = null;
      this.wlsServerName = null;
      this.migratableTargetName = null;
      this.domainName = null;
      this.backEndId = null;
      this.destinationId = null;
      this.dispatcherId = null;
      this.remoteSecurityMode = 16;
   }

   public boolean equals(Object var1) {
      return var1 instanceof DDMember && this.name.equals(((DDMember)var1).name);
   }

   public int hashCode() {
      return this.name.hashCode();
   }

   private void determineMigratableTargetName() {
      BackEnd var1 = this.destination.getBackEnd();
      if (var1 != null) {
         this.migratableTargetName = JMSDeploymentHelper.getMigratableTargetName(var1.getName());
      }
   }

   private void determinePersistentStoreName() {
      if (this.destination != null) {
         BackEnd var1 = this.destination.getBackEnd();
         if (var1 != null) {
            if (var1.getPersistentStore() != null) {
               this.persistentStoreName = var1.getPersistentStore().getName();
            }

         }
      }
   }

   public synchronized void setDestination(BEDestinationImpl var1) {
      if (this.destination == null) {
         if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
            JMSDebug.JMSDistTopic.debug("I have not seen this destination before: " + var1.getName());
         }

         this.destination = var1;
         this.isLocal = true;
         this.remoteSecurityMode = SECURITY_MODE_FOR_WIRE;
         this.setIsInsertionPaused(var1.isInsertionPaused());
         this.setIsConsumptionPaused(var1.isConsumptionPaused());
         this.setIsProductionPaused(var1.isProductionPaused());
         this.setHasConsumers(var1.hasConsumers());
         this.isPersistent = var1.isPersistent();
         this.globalJNDIName = var1.getJNDIName();
         this.localJNDIName = var1.getLocalJNDIName();
         AuthenticatedSubject var2 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
         this.wlsServerName = ManagementService.getRuntimeAccess(var2).getServer().getName();
         this.jmsServerName = var1.getDestinationImpl().getServerName();
         this.determinePersistentStoreName();
         this.determineMigratableTargetName();
         this.domainName = JMSDeploymentHelper.getDomainName();
         this.backEndId = var1.getDestinationImpl().getBackEndId();
         this.destinationId = var1.getDestinationImpl().getId();
         if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
            JMSDebug.JMSDistTopic.debug("destinationId is now " + this.destinationId);
         }

         this.dispatcherId = var1.getDestinationImpl().getDispatcherId();
         if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
            JMSDebug.JMSDistTopic.debug("dispatcherId is now " + this.dispatcherId);
         }

         var1.addStatusListener(this);
         this.isDestinationUp = false;
         this.isUp = false;
         this.isForwardingUp = false;
         this.setIsDestinationUp(var1.isUp());
         new DDMemberStatusSharer(this);
      }
   }

   public DDMember() {
   }

   public void notAMember() {
      if (this.destination != null) {
         this.destination.removeStatusListener(this);
      }

      this.setIsUp(false);
      this.ddHandler = null;
   }

   public void update(DDMember var1) {
      if (!this.isLocal) {
         this.name = var1.name;
         this.setIsInsertionPaused(var1.isInsertionPaused);
         this.setIsConsumptionPaused(var1.isConsumptionPaused);
         this.setIsProductionPaused(var1.isProductionPaused);
         this.setHasConsumers(var1.hasConsumers);
         this.isPersistent = var1.isPersistent;
         this.wlsServerName = var1.wlsServerName;
         this.jmsServerName = var1.jmsServerName;
         this.persistentStoreName = var1.persistentStoreName;
         this.migratableTargetName = var1.migratableTargetName;
         this.domainName = var1.domainName;
         this.globalJNDIName = var1.globalJNDIName;
         this.localJNDIName = var1.localJNDIName;
         this.backEndId = var1.backEndId;
         this.destinationId = var1.destinationId;
         this.dispatcherId = var1.dispatcherId;
         this.remoteSecurityMode = var1.remoteSecurityMode;
         this.setIsUp(var1.isUp);
      }
   }

   public synchronized void addStatusListener(MemberStatusListener var1) {
      if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
         JMSDebug.JMSDistTopic.debug("Adding listener: " + var1 + " on " + this.name + ", this is " + this);
      }

      if (this.statusListeners == null) {
         this.statusListeners = new LinkedList();
      }

      this.statusListeners.add(var1);
      if (this.events != 0) {
         DDScheduler.schedule(this);
      }

   }

   public synchronized void removeStatusListener(MemberStatusListener var1) {
      if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
         JMSDebug.JMSDistTopic.debug("Removing listener: " + var1 + " on " + this.name + ", this is " + this);
      }

      if (this.statusListeners != null) {
         this.statusListeners.remove(var1);
         if (this.statusListeners.isEmpty()) {
            this.statusListeners = null;
         }
      }

   }

   boolean isUOWDestination() {
      return this.destination == null ? true : this.destination.isUOWDestination();
   }

   public void onProductionPauseChange(DestinationStatus var1) {
      this.setIsProductionPaused(var1.isProductionPaused());
   }

   public void onConsumptionPauseChange(DestinationStatus var1) {
      this.setIsConsumptionPaused(var1.isConsumptionPaused());
   }

   public void onInsertionPauseChange(DestinationStatus var1) {
      this.setIsInsertionPaused(var1.isInsertionPaused());
   }

   public void onHasConsumersStatusChange(DestinationStatus var1) {
      this.setHasConsumers(var1.hasConsumers());
   }

   public void onUpStatusChange(DestinationStatus var1) {
      this.setIsDestinationUp(var1.isUp());
   }

   public void run() {
      this.callListeners();
   }

   private void callListeners() {
      short var1;
      LinkedList var2;
      DDMember var3;
      synchronized(this) {
         if (this.events == 0) {
            return;
         }

         var1 = this.events;
         this.events = 0;
         if (this.statusListeners == null) {
            return;
         }

         var2 = new LinkedList(this.statusListeners);

         try {
            var3 = (DDMember)this.clone();
         } catch (CloneNotSupportedException var7) {
            throw new AssertionError("Clone is supported, no matter what the followoing says: " + var7);
         }
      }

      ListIterator var4 = var2.listIterator();

      while(var4.hasNext()) {
         MemberStatusListener var5 = (MemberStatusListener)var4.next();
         if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
            JMSDebug.JMSDistTopic.debug("Calling out to " + var5 + " events is " + eventsPrint(var1));
         }

         var5.memberStatusChange(var3, var1);
         if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
            JMSDebug.JMSDistTopic.debug("Back from " + var5 + " events is " + eventsPrint(var1));
         }
      }

   }

   private static String append(String var0, String var1) {
      return var0 == null ? var1 : var0 + "|" + var1;
   }

   private static String eventsPrint(int var0) {
      String var1 = null;
      if ((var0 & 1) != 0) {
         var1 = append(var1, "HAS_CONSUMERS_CHANGE");
      }

      if ((var0 & 2) != 0) {
         var1 = append(var1, "INSERTION_PAUSED_CHANGE");
      }

      if ((var0 & 4) != 0) {
         var1 = append(var1, "PRODUCTION_PAUSED_CHANGE");
      }

      if ((var0 & 8) != 0) {
         var1 = append(var1, "CONSUMPTION_PAUSED_CHANGE");
      }

      if ((var0 & 16) != 0) {
         var1 = append(var1, "UP_CHANGE");
      }

      if ((var0 & 32) != 0) {
         var1 = append(var1, "WEIGHT_CHANGE");
      }

      return var1;
   }

   private synchronized void addEvent(short var1) {
      this.events |= var1;
      if (this.statusListeners != null) {
         if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
            JMSDebug.JMSDistTopic.debug("Got an event: " + eventsPrint(var1) + " on " + this.name);
         }

         DDScheduler.schedule(this);
      }
   }

   public void setHasConsumers(boolean var1) {
      if (var1 != this.hasConsumers) {
         if (this.statusListeners != null && JMSDebug.JMSDistTopic.isDebugEnabled()) {
            JMSDebug.JMSDistTopic.debug("Got a HAS_CONSUMERS_CHANGE on " + this.name + ", it is now " + var1);
         }

         this.ddImplOutOfDate = true;
         this.hasConsumers = var1;
         this.addEvent((short)1);
      }
   }

   public void setIsPersistent(boolean var1) {
      this.isPersistent = var1;
   }

   public void setIsInsertionPaused(boolean var1) {
      if (var1 != this.isInsertionPaused) {
         this.ddImplOutOfDate = true;
         this.isInsertionPaused = var1;
         this.addEvent((short)2);
      }
   }

   public void setIsProductionPaused(boolean var1) {
      if (var1 != this.isProductionPaused) {
         this.ddImplOutOfDate = true;
         this.isProductionPaused = var1;
         this.addEvent((short)4);
      }
   }

   public void setIsConsumptionPaused(boolean var1) {
      if (var1 != this.isConsumptionPaused) {
         this.ddImplOutOfDate = true;
         this.isConsumptionPaused = var1;
         this.addEvent((short)8);
      }
   }

   public void setIsUp(boolean var1) {
      if (var1 != this.isUp) {
         if (this.statusListeners != null && JMSDebug.JMSDistTopic.isDebugEnabled()) {
            JMSDebug.JMSDistTopic.debug("Got an UP_CHANGE on " + this.name + ", it is now " + var1);
         }

         this.ddImplOutOfDate = true;
         this.isUp = var1;
         this.addEvent((short)16);
      }
   }

   public boolean isDestinationUp() {
      return this.isDestinationUp;
   }

   private synchronized void setIsDestinationUp(boolean var1) {
      if (var1 != this.isDestinationUp) {
         if (this.statusListeners != null && JMSDebug.JMSDistTopic.isDebugEnabled()) {
            JMSDebug.JMSDistTopic.debug("Got a DESTINATION_UP_CHANGE on " + this.name + ", it is now " + var1);
         }

         if (!var1) {
            if (this.destination != null) {
               this.destination.removeStatusListener(this);
               this.destination = null;
            }

            this.isLocal = false;
         }

         this.ddImplOutOfDate = true;
         this.isDestinationUp = var1;
         this.setIsUp(this.isForwardingUp && var1);
      }
   }

   public void setIsForwardingUp(boolean var1) {
      if (var1 != this.isForwardingUp) {
         if (this.statusListeners != null && JMSDebug.JMSDistTopic.isDebugEnabled()) {
            JMSDebug.JMSDistTopic.debug("Got a FORWARDING_UP_CHANGE on " + this.name + ", it is now " + var1);
         }

         this.isForwardingUp = var1;
         this.setIsUp(var1 && this.isDestinationUp);
      }
   }

   public void setWeight(int var1) {
      if (var1 != this.weight) {
         this.ddImplOutOfDate = true;
         this.weight = var1;
         this.addEvent((short)32);
      }
   }

   public BEDestinationImpl getDestination() {
      return this.destination;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public String getName() {
      return this.name;
   }

   void setRemoteSecurityMode(int var1) {
      switch (var1) {
         case 11:
         case 12:
         case 13:
         case 14:
            this.remoteSecurityMode = var1;
            return;
         default:
            throw new AssertionError();
      }
   }

   public int getRemoteSecurityMode() {
      return this.remoteSecurityMode;
   }

   public DispatcherId getDispatcherId() {
      return this.dispatcherId;
   }

   public void setDispatcherId(DispatcherId var1) {
      this.dispatcherId = var1;
   }

   public JMSID getDestinationId() {
      return this.destinationId;
   }

   public void setDestinationId(JMSID var1) {
      this.destinationId = var1;
   }

   public JMSServerId getBackEndId() {
      return this.backEndId;
   }

   public void setBackEndId(JMSServerId var1) {
      this.backEndId = var1;
   }

   public String getDomainName() {
      return this.domainName;
   }

   public void setDomainName(String var1) {
      this.domainName = var1;
   }

   public String getMigratableTargetName() {
      return this.migratableTargetName;
   }

   public void setMigratableTargetName(String var1) {
      this.migratableTargetName = var1;
   }

   public String getWLSServerName() {
      return this.wlsServerName;
   }

   public void setWLSServerName(String var1) {
      this.wlsServerName = var1;
   }

   public String getJMSServerName() {
      return this.jmsServerName;
   }

   public void setJMSServerName(String var1) {
      this.jmsServerName = var1;
   }

   public String getPersistentStoreName() {
      this.determinePersistentStoreName();
      return this.persistentStoreName;
   }

   public void setPersistentStoreName(String var1) {
      this.persistentStoreName = var1;
   }

   public String getGlobalJNDIName() {
      return this.globalJNDIName;
   }

   public void setGlobalJNDIName(String var1) {
      this.globalJNDIName = var1;
   }

   public String getLocalJNDIName() {
      return this.localJNDIName;
   }

   public void setLocalJNDIName(String var1) {
      this.localJNDIName = var1;
   }

   public boolean isLocal() {
      return this.isLocal;
   }

   public boolean isInsertionPaused() {
      return this.isInsertionPaused;
   }

   public boolean isConsumptionPaused() {
      return this.isConsumptionPaused;
   }

   public boolean isProductionPaused() {
      return this.isProductionPaused;
   }

   public boolean hasConsumers() {
      return this.hasConsumers;
   }

   public boolean isUp() {
      return this.isUp;
   }

   public boolean isPersistent() {
      return this.isPersistent;
   }

   public int getWeight() {
      return this.weight;
   }

   public void setDDHandler(DDHandler var1) {
      this.ddHandler = var1;
      this.getDDImpl();
   }

   public DistributedDestinationImpl getDDImpl() {
      if (this.ddImplOutOfDate && this.ddHandler != null) {
         this.ddImpl = new DistributedDestinationImpl(this.ddHandler.isQueue() ? 1 : 2, this.jmsServerName, this.ddHandler.getName(), this.ddHandler.getApplicationName(), this.ddHandler.getEARModuleName(), this.ddHandler.getLoadBalancingPolicyAsInt(), this.ddHandler.getForwardingPolicy(), this.getName(), this.ddHandler.getJNDIName(), this.backEndId, this.destinationId, this.dispatcherId, this.isPersistent(), this.persistentStoreName, this.ddHandler.getSAFExportPolicy(), this.isLocal);
         this.ddImpl.setNonSystemSubscriberConsumers(this.hasConsumers ? 1 : 0);
         this.ddImpl.setIsProductionPaused(this.isProductionPaused());
         this.ddImpl.setIsConsumptionPaused(this.isConsumptionPaused());
         this.ddImpl.setIsInsertionPaused(this.isInsertionPaused());
         this.ddImpl.setWeight(this.weight);
         this.ddImplOutOfDate = false;
      }

      return this.ddImpl;
   }

   public Object clone() throws CloneNotSupportedException {
      return (DDMember)super.clone();
   }

   public String toString() {
      return "DDMember: " + this.name + ", hash: " + this.hashCode() + ", dispId: " + this.dispatcherId + ", backEndId: " + this.backEndId + ", destinationId: " + this.destinationId + ", remoteSecurityMode: " + this.remoteSecurityMode;
   }

   private static int getSecurityModeForWire() {
      String var0 = "weblogic.jms.DDMemberPolicy";
      String var1 = System.getProperty(var0, "");
      var1 = var1.toLowerCase(Locale.ENGLISH).trim();
      if (var1.equals("default")) {
         return 11;
      } else if (var1.equals("perf")) {
         return 12;
      } else if (var1.equals("full")) {
         return 13;
      } else if (var1.equals("kid")) {
         return 14;
      } else {
         if (var1.length() > 0) {
            (new Exception("Unexpected value for " + var0)).printStackTrace();
         }

         return 11;
      }
   }
}
