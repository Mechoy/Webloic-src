package weblogic.jms.dd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import weblogic.jms.JMSService;
import weblogic.jms.backend.BEDestinationImpl;
import weblogic.jms.backend.BEUOOQueueState;
import weblogic.jms.backend.QueueForwardingManager;
import weblogic.jms.backend.TopicForwardingManager;
import weblogic.jms.common.DistributedDestinationImpl;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSServerId;
import weblogic.jms.dispatcher.JMSDispatcher;
import weblogic.jms.dispatcher.JMSDispatcherManager;
import weblogic.jms.frontend.FEDDHandler;
import weblogic.messaging.dispatcher.DispatcherId;

public final class DDHandler implements MemberStatusListener, Runnable {
   public static final int NO_EVENT = 0;
   public static final int MEMBERSHIP_CHANGE = 1;
   public static final int MEMBER_STATUS_CHANGE = 2;
   public static final int DD_PARAM_CHANGE = 4;
   public static final int ACTIVATE = 8;
   public static final int DEACTIVATE = 16;
   public static final int ANY_CHANGE = 255;
   private List listeners = new LinkedList();
   private static List generalListeners;
   private static Object generalLock = new Object();
   private HashMap members = new HashMap();
   private List memberList = new ArrayList();
   private FEDDHandler feDDHandler;
   private DDConfig ddConfig = null;
   private DistributedDestinationImpl ddImpl = null;
   private int events = 0;
   private String name = null;
   private String safExportPolicy = null;
   private String unitOfOrderRouting = null;
   private String jndiName = null;
   private int loadBalancingPolicyAsInt;
   private String applicationName = null;
   private String EARModuleName = null;
   private String referenceName = null;
   private int queueForwardDelay;
   private boolean resetDeliveryCount = true;
   private boolean active = false;
   private boolean local = false;
   private boolean isQueue;
   private boolean setupForwarding;
   private boolean isUOWDestination = false;
   private boolean remoteUpdatePending = false;
   private int forwardingPolicy = 1;
   private boolean defaultUnitOfOrder = false;

   public DDHandler() {
   }

   public DDHandler(DDConfig var1, boolean var2) {
      this.name = var1.getName();
      this.ddConfig = var1;
      this.isQueue = var1.getType() == 0;
      this.safExportPolicy = var1.getSAFExportPolicy();
      this.unitOfOrderRouting = var1.getUnitOfOrderRouting();
      this.jndiName = var1.getJNDIName();
      this.loadBalancingPolicyAsInt = var1.getLoadBalancingPolicyAsInt();
      this.applicationName = var1.getApplicationName();
      this.EARModuleName = var1.getEARModuleName();
      this.referenceName = var1.getReferenceName();
      this.queueForwardDelay = var1.getForwardDelay();
      this.resetDeliveryCount = var1.getResetDeliveryCountOnForward();
      this.setupForwarding = var2;
      this.local = true;
      this.defaultUnitOfOrder = var1.isDefaultUnitOfOrder();
   }

   public int getForwardingPolicy() {
      return this.forwardingPolicy;
   }

   public void setForwardingPolicy(int var1) {
      if (var1 != 1 && var1 != 0) {
         throw new RuntimeException("Invalid forwarding policy value (" + var1 + ") found. Valid forwarding policies are" + " the constants from DDConstants(DDConstants.FORWARDING_POLICY_PARTITIONED(value=" + 0 + ") or DDConstants.FORWARDING_POLICY_REPLICATED" + "(value=" + 1 + ")");
      } else {
         this.forwardingPolicy = var1;
      }
   }

   public void activate() {
      if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
         JMSDebug.JMSDistTopic.debug("activating " + this.name);
      }

      this.feDDHandler = new FEDDHandler(this);
      new DDStatusSharer(this);
      this.active = true;
      this.addEvent(8);
   }

   public synchronized void deactivate() {
      if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
         JMSDebug.JMSDistTopic.debug("Deactivating " + this.name);
      }

      this.updateMembers(new LinkedList());
      this.addEvent(16);
      DDManager.deactivate(this);
      this.active = false;
      this.feDDHandler = null;
   }

   public synchronized void updateMembers(List var1) {
      if (var1 != null) {
         int var2 = 0;
         synchronized(this.members) {
            try {
               Iterator var4 = this.memberCloneIterator();

               while(var4.hasNext()) {
                  DDMember var5 = (DDMember)var4.next();
                  if (!var1.contains(var5.getName())) {
                     var2 |= this.removeMemberWithoutEvent(var5.getName());
                  }
               }

               String var14;
               for(ListIterator var13 = var1.listIterator(); var13.hasNext(); var2 |= this.addMemberWithoutEvent(var14)) {
                  var14 = (String)var13.next();
               }
            } finally {
               if (var2 != 0) {
                  this.addEvent(var2);
               }

            }

         }
      }
   }

   public void update(DDHandler var1) {
      if (this.local && !var1.local) {
         this.remoteUpdatePending = true;
      }

      if (var1.local) {
         this.local = true;
         this.ddConfig = var1.ddConfig;
      }

      this.setSAFExportPolicy(var1.safExportPolicy);
      this.setUnitOfOrderRouting(var1.unitOfOrderRouting);
      this.setDefaultUnitOfOrder(var1.defaultUnitOfOrder);
      this.setJNDIName(var1.jndiName);
      this.setLoadBalancingPolicyAsInt(var1.loadBalancingPolicyAsInt);
      this.setForwardingPolicy(var1.forwardingPolicy);
      this.setForwardDelay(var1.queueForwardDelay);
      this.setResetDeliveryCountOnForward(var1.resetDeliveryCount);
      this.setApplicationName(var1.applicationName);
      this.setEARModuleName(var1.EARModuleName);
      this.setReferenceName(var1.referenceName);
      this.setupForwarding |= var1.setupForwarding;
   }

   public boolean isActive() {
      return this.active;
   }

   public boolean isLocal() {
      return this.local;
   }

   public boolean isQueue() {
      return this.isQueue;
   }

   public void setIsQueue(boolean var1) {
      this.isQueue = var1;
   }

   public void setEARModuleName(String var1) {
      this.EARModuleName = var1;
   }

   public void setReferenceName(String var1) {
      this.referenceName = var1;
   }

   public FEDDHandler getFEDDHandler() {
      return this.feDDHandler;
   }

   public String getEARModuleName() {
      return this.EARModuleName;
   }

   public String getReferenceName() {
      return this.referenceName;
   }

   public void setApplicationName(String var1) {
      this.applicationName = var1;
   }

   public String getApplicationName() {
      return this.applicationName;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public String getName() {
      return this.name;
   }

   public void addMemberStatusListener(String var1, MemberStatusListener var2) {
      DDMember var3 = null;
      synchronized(this.members) {
         var3 = (DDMember)this.members.get(var1);
      }

      assert var3 != null;

      var3.addStatusListener(var2);
   }

   public static void addGeneralStatusListener(DDStatusListener var0, int var1) {
      synchronized(generalLock) {
         if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
            JMSDebug.JMSDistTopic.debug("Adding general listener: " + var0 + ", listening for " + var1 + " on everything");
         }

         LinkedList var3 = new LinkedList();
         if (generalListeners != null) {
            ListIterator var4 = generalListeners.listIterator();

            while(var4.hasNext()) {
               var3.add(var4.next());
            }
         }

         var3.add(new ListenerObject(var0, var1));
         generalListeners = var3;
      }
   }

   public synchronized void addStatusListener(DDStatusListener var1, int var2) {
      if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
         JMSDebug.JMSDistTopic.debug("Adding listener: " + var1 + ", listening for " + var2 + " on " + this.name + ", this is " + this);
      }

      this.listeners.add(new ListenerObject(var1, var2));
   }

   public synchronized void removeStatusListener(DDStatusListener var1) {
      if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
         JMSDebug.JMSDistTopic.debug("Removing listener: " + var1 + " for " + this.name + ", this is " + this);
      }

      ListIterator var2 = this.listeners.listIterator();

      while(var2.hasNext()) {
         ListenerObject var3 = (ListenerObject)var2.next();
         if (var3.listener == var1) {
            var2.remove();
         }
      }

   }

   public void run() {
      this.callListeners();
   }

   private static String append(String var0, String var1) {
      return var0 == null ? var1 : var0 + "|" + var1;
   }

   private static String eventsPrint(int var0) {
      String var1 = null;
      if ((var0 & 1) != 0) {
         var1 = append(var1, "MEMBERSHIP_CHANGE");
      }

      if ((var0 & 2) != 0) {
         var1 = append(var1, "MEMBER_STATUS_CHANGE");
      }

      if ((var0 & 4) != 0) {
         var1 = append(var1, "DD_PARAM_CHANGE");
      }

      if ((var0 & 8) != 0) {
         var1 = append(var1, "ACTIVATE");
      }

      if ((var0 & 16) != 0) {
         var1 = append(var1, "DEACTIVATE");
      }

      return var1;
   }

   private void callListener(ListenerObject var1, int var2) {
      DDStatusListener var3 = var1.listener;
      if ((var1.toWhat & var2) != 0) {
         if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
            JMSDebug.JMSBackEnd.debug("Calling out to " + var3);
         }

         try {
            var3.statusChangeNotification(this, var2);
         } catch (Throwable var5) {
            var5.printStackTrace();
         }

         if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
            JMSDebug.JMSBackEnd.debug("Back from " + var3);
         }
      }

   }

   private void callListeners() {
      int var1;
      LinkedList var2;
      synchronized(this) {
         if (this.events == 0) {
            return;
         }

         var1 = this.events;
         this.events = 0;
         var2 = new LinkedList(this.listeners);
      }

      if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
         JMSDebug.JMSDistTopic.debug("Events is " + eventsPrint(var1) + " for " + this.name + ", this is " + this);
      }

      ListIterator var3 = var2.listIterator();

      while(var3.hasNext()) {
         this.callListener((ListenerObject)var3.next(), var1);
      }

      if (generalListeners != null) {
         var3 = generalListeners.listIterator();

         while(var3.hasNext()) {
            this.callListener((ListenerObject)var3.next(), var1);
         }
      }

   }

   private synchronized void addEvent(int var1) {
      if (this.active) {
         if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
            JMSDebug.JMSDistTopic.debug("Adding event to " + this.name + ": " + eventsPrint(var1));
         }

         synchronized(this) {
            this.events |= var1;
         }

         DDScheduler.schedule(this);
      }
   }

   public void memberStatusChange(DDMember var1, int var2) {
      this.addEvent(2);
   }

   private boolean equal(Object var1, Object var2) {
      if (var1 == null) {
         return var2 == null;
      } else {
         return var1.equals(var2);
      }
   }

   public String getSAFExportPolicy() {
      return this.safExportPolicy;
   }

   public void setSAFExportPolicy(String var1) {
      if (!this.equal(var1, this.safExportPolicy)) {
         this.safExportPolicy = var1;
         this.addEvent(4);
      }

   }

   public boolean isUOWDestination() {
      return this.isUOWDestination;
   }

   public void setIsUOWDestination(boolean var1) {
      if (var1 != this.isUOWDestination) {
         this.isUOWDestination = var1;
         this.addEvent(4);
      }

   }

   public String getUnitOfOrderRouting() {
      return this.unitOfOrderRouting;
   }

   public void setUnitOfOrderRouting(String var1) {
      if (!this.equal(this.unitOfOrderRouting, var1)) {
         this.unitOfOrderRouting = var1;
         this.addEvent(4);
      }

   }

   public boolean isDefaultUnitOfOrder() {
      return this.defaultUnitOfOrder;
   }

   public void setDefaultUnitOfOrder(boolean var1) {
      if (this.defaultUnitOfOrder != var1) {
         this.defaultUnitOfOrder = var1;
         this.addEvent(4);
      }

   }

   public String getJNDIName() {
      return this.jndiName;
   }

   public void setJNDIName(String var1) {
      if (!this.equal(this.jndiName, var1)) {
         this.jndiName = var1;
         this.addEvent(4);
      }

   }

   public int getLoadBalancingPolicyAsInt() {
      return this.loadBalancingPolicyAsInt;
   }

   public void setLoadBalancingPolicyAsInt(int var1) {
      if (this.loadBalancingPolicyAsInt != var1) {
         this.loadBalancingPolicyAsInt = var1;
         this.addEvent(4);
      }

   }

   public int getForwardDelay() {
      return this.queueForwardDelay;
   }

   public void setForwardDelay(int var1) {
      if (this.queueForwardDelay != var1) {
         this.queueForwardDelay = var1;
         this.addEvent(4);
      }

   }

   public boolean getResetDeliveryCountOnForward() {
      return this.resetDeliveryCount;
   }

   public void setResetDeliveryCountOnForward(boolean var1) {
      if (this.resetDeliveryCount != var1) {
         this.resetDeliveryCount = var1;
         this.addEvent(4);
      }

   }

   public void memberUpdate(DDMember var1) {
      DDMember var2 = null;
      synchronized(this.members) {
         var2 = (DDMember)this.members.get(var1.getName());
      }

      if (var2 != null) {
         var2.update(var1);
      }
   }

   public void addMembers(String[] var1, BEDestinationImpl[] var2) {
      if (var1 != null && var1.length != 0) {
         int var3 = 0;

         try {
            for(int var4 = 0; var4 < var1.length; ++var4) {
               var3 |= this.addMemberWithoutEvent(var1[var4], var2[var4]);
            }
         } finally {
            if (var3 != 0) {
               this.addEvent(var3);
            }

         }

      }
   }

   private int addMemberWithoutEvent(String var1) {
      return JMSService.getJMSService() != null && JMSService.getJMSService().getBEDeployer() != null ? this.addMemberWithoutEvent(var1, JMSService.getJMSService().getBEDeployer().findBEDestination(var1)) : 0;
   }

   private int addMemberWithoutEvent(String var1, BEDestinationImpl var2) {
      DDMember var3 = this.findMemberByName(var1);
      if (var3 != null) {
         if (this.local && var2 != null) {
            this.setDestination(var3, var2);
            return 2;
         } else {
            return 0;
         }
      } else {
         if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
            JMSDebug.JMSDistTopic.debug("Adding member " + var1 + " to " + this.name);
         }

         var3 = DDManager.removeDeferredMember(var1);
         if (var3 == null) {
            var3 = new DDMember(var1);
         }

         synchronized(this.members) {
            this.members.put(var1, var3);
            this.memberList.add(var3);
         }

         DDManager.addDDHandlerMember(var1, this);
         if (this.local && var2 != null) {
            this.setDestination(var3, var2);
         }

         var3.setDDHandler(this);
         var3.addStatusListener(this);
         return 1;
      }
   }

   public void addMember(String var1) {
      if (JMSService.getJMSService() != null && JMSService.getJMSService().getBEDeployer() != null) {
         this.addMember(var1, JMSService.getJMSService().getBEDeployer().findBEDestination(var1));
      }

   }

   public void addMember(String var1, BEDestinationImpl var2) {
      int var3 = this.addMemberWithoutEvent(var1, var2);
      if (var3 != 0) {
         this.addEvent(var3);
      }

   }

   public void setMemberWeight(String var1, int var2) {
      DDMember var3 = this.findMemberByName(var1);
      var3.setWeight(var2);
   }

   private void setDestination(DDMember var1, BEDestinationImpl var2) {
      if (var1.getDestination() == null) {
         var1.setDestination(var2);
         this.setIsUOWDestination(var1.isUOWDestination());
         if (this.setupForwarding) {
            if (this.isQueue) {
               new QueueForwardingManager(this, var1);
            } else if (this.forwardingPolicy == 1) {
               new TopicForwardingManager(this, var1, var2);
            } else {
               var1.setIsForwardingUp(true);
            }
         } else {
            var1.setIsForwardingUp(true);
         }

         if (this.isQueue && "PathService".equals(this.getUnitOfOrderRouting())) {
            var2.setExtension(new BEUOOQueueState(var2, this));
         }

      }
   }

   public void removeMember(String var1) {
      this.addEvent(this.removeMemberWithoutEvent(var1));
   }

   private int removeMemberWithoutEvent(String var1) {
      DDManager.removeDDHandlerMember(var1);
      synchronized(this.members) {
         DDMember var3 = (DDMember)this.members.remove(var1);
         if (var3 != null) {
            if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
               JMSDebug.JMSDistTopic.debug("Removing member " + var1 + " from " + this.name);
            }

            this.memberList.remove(var3);
            var3.removeStatusListener(this);
            var3.notAMember();
         }

         return 1;
      }
   }

   public DistributedDestinationImpl getDDImpl() {
      if (this.ddImpl != null) {
         return this.ddImpl;
      } else {
         String var1 = null;
         JMSServerId var2 = null;
         JMSID var3 = null;
         synchronized(this.members) {
            if (!this.members.isEmpty()) {
               DDMember var5 = (DDMember)this.members.values().iterator().next();
               if (var5 != null) {
                  var1 = var5.getName();
                  var2 = var5.getBackEndId();
                  var3 = var5.getDestinationId();
               }
            }
         }

         JMSDispatcher var4 = JMSDispatcherManager.getLocalDispatcher();

         assert var4 != null;

         DispatcherId var8 = var4.getId();
         this.ddImpl = new DistributedDestinationImpl(this.isQueue ? 1 : 2, "", this.name, this.getApplicationName(), this.getEARModuleName(), this.getLoadBalancingPolicyAsInt(), this.getForwardingPolicy(), var1, this.jndiName, var2, var3, var8, false, "", this.safExportPolicy, false);
         return this.ddImpl;
      }
   }

   public DistributedDestinationImpl getDDIByMemberName(String var1) {
      DDMember var2 = this.findMemberByName(var1);

      assert var2 != null;

      return var2.getDDImpl();
   }

   public DDMember findMemberByName(String var1) {
      synchronized(this.members) {
         return (DDMember)this.members.get(var1);
      }
   }

   public String debugKeys() {
      Set var1 = ((HashMap)this.members.clone()).keySet();
      if (var1 == null) {
         return "DDHandler keys are null";
      } else {
         Iterator var2 = ((HashMap)this.members.clone()).values().iterator();
         String var3 = "\nvalues:";
         if (var2.hasNext()) {
            while(var2.hasNext()) {
               DDMember var4 = (DDMember)var2.next();
               var3 = var3 + "\n(" + var4 + ")";
            }
         } else {
            var3 = "\n no values";
         }

         return var1.toString() + var3;
      }
   }

   boolean memberHasSecurityMode(int var1) {
      synchronized(this.members) {
         Iterator var3 = this.members.values().iterator();

         DDMember var4;
         do {
            if (!var3.hasNext()) {
               return false;
            }

            var4 = (DDMember)var3.next();
         } while(var4.getRemoteSecurityMode() != var1);

         return true;
      }
   }

   public List memberCloneList() {
      LinkedList var1 = new LinkedList();
      synchronized(this.members) {
         Iterator var3 = this.members.values().iterator();

         while(var3.hasNext()) {
            DDMember var4 = (DDMember)var3.next();

            try {
               var1.add(var4.clone());
            } catch (CloneNotSupportedException var7) {
               throw new AssertionError("I can't clone a member");
            }
         }

         return var1;
      }
   }

   public Iterator memberCloneIterator() {
      return this.memberCloneList().listIterator();
   }

   public String toString() {
      return "DDHandler: " + this.name + ", hash: " + this.hashCode() + ", dd: " + this.ddImpl;
   }

   public int getNumberOfMembers() {
      synchronized(this.members) {
         return this.members.size();
      }
   }

   public DDMember getMemberByIndex(int var1) {
      synchronized(this.members) {
         return (DDMember)this.memberList.get(var1);
      }
   }

   public void newDestination(BEDestinationImpl var1) {
      if (this.local) {
         DDMember var2 = this.findMemberByName(var1.getName());
         if (var2 != null) {
            this.setDestination(var2, var1);
         }
      }
   }

   public boolean isRemoteUpdatePending() {
      return this.remoteUpdatePending;
   }

   public void setRemoteUpdatePending(boolean var1) {
      this.remoteUpdatePending = var1;
   }

   private static class ListenerObject {
      int toWhat;
      DDStatusListener listener;

      ListenerObject(DDStatusListener var1, int var2) {
         this.toWhat = var2;
         this.listener = var1;
      }
   }
}
