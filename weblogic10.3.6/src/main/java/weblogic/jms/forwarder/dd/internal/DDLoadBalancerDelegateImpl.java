package weblogic.jms.forwarder.dd.internal;

import java.security.AccessControlException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.naming.Context;
import weblogic.jms.cache.CacheEntry;
import weblogic.jms.common.DDMemberInformation;
import weblogic.jms.common.DDTxLoadBalancingOptimizer;
import weblogic.jms.common.DestinationImpl;
import weblogic.jms.common.DistributedDestinationImpl;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.LoadBalancer;
import weblogic.jms.common.MessageImpl;
import weblogic.jms.common.RRLoadBalancer;
import weblogic.jms.common.RandomLoadBalancer;
import weblogic.jms.forwarder.DestinationName;
import weblogic.jms.forwarder.SessionRuntimeContext;
import weblogic.jms.forwarder.dd.DDForwardStore;
import weblogic.jms.forwarder.dd.DDInfo;
import weblogic.jms.forwarder.dd.DDLBTable;
import weblogic.jms.forwarder.dd.DDLoadBalancerDelegate;
import weblogic.jms.forwarder.dd.DDMemberInfo;
import weblogic.jms.forwarder.dd.DDMembersCache;
import weblogic.jms.forwarder.dd.DDMembersCacheChangeListener;
import weblogic.security.subject.AbstractSubject;
import weblogic.security.subject.SubjectManager;
import weblogic.store.PersistentStoreException;
import weblogic.store.xa.PersistentStoreXA;
import weblogic.utils.StackTraceUtilsClient;

public class DDLoadBalancerDelegateImpl implements DDLoadBalancerDelegate {
   private String name;
   private DDInfo ddInfo;
   private SessionRuntimeContext jmsSessionRuntimeContext;
   private DDForwardStore ddForwardStore;
   private DDLBTable ddLBTable;
   private LoadBalancer loadBalancer = new RRLoadBalancer();
   private ArrayList ddImplArrayList = new ArrayList();
   private HashMap memberInformationDDImplMap = new HashMap();
   private HashMap ddMemberNameDImplMap = new HashMap();
   private LoadBalancerHandler loadBalancerHandler;
   private static final int ADD_MEMBER = 1;
   private static final int REMOVE_MEMBER = 2;
   private ArrayList notifications = new ArrayList();
   private boolean freezed;
   private AbstractSubject subject = null;
   private static final AbstractSubject kernelID = getKernelIdentity();
   private boolean isFirstPushedMessageNotInFailedMap = true;

   private static final AbstractSubject getKernelIdentity() {
      try {
         return (AbstractSubject)AccessController.doPrivileged(SubjectManager.getKernelIdentityAction());
      } catch (AccessControlException var1) {
         return null;
      }
   }

   public DDLoadBalancerDelegateImpl(SessionRuntimeContext var1, DDInfo var2, PersistentStoreXA var3) throws JMSException {
      this.name = var1.getName();
      this.ddInfo = var2;
      this.jmsSessionRuntimeContext = var1;
      this.createDDForwardStore(var3);
      this.createLoadBalancer();
   }

   private void createDDForwardStore(PersistentStoreXA var1) throws JMSException {
      String var2 = var1.getName() + "." + this.name + "." + this.ddInfo.getDestinationName().getConfigName();
      this.ddForwardStore = new DDForwardStoreImpl(var2, this.ddInfo, var1);
      this.ddLBTable = this.ddForwardStore.getDDLBTable();
   }

   private void createLoadBalancer() {
      switch (this.ddInfo.getLoadBalancingPolicy()) {
         case 0:
            this.loadBalancer = new RRLoadBalancer();
            break;
         case 1:
            this.loadBalancer = new RandomLoadBalancer();
      }

      this.loadBalancerHandler = new LoadBalancerHandler();
   }

   public DestinationName getDestinationName() {
      return this.ddInfo.getDestinationName();
   }

   public int getLoadBalancingPolicy() {
      return this.ddInfo.getLoadBalancingPolicy();
   }

   public int getForwardingPolicy() {
      return this.ddInfo.getForwardingPolicy();
   }

   public boolean isDDInLocalCluster() {
      return this.jmsSessionRuntimeContext.isForLocalCluster();
   }

   public synchronized Destination loadBalance() {
      DistributedDestinationImpl var1 = this.loadBalancer.getNext((DDTxLoadBalancingOptimizer)null);
      return (Destination)this.ddMemberNameDImplMap.get(var1.getMemberName());
   }

   public Destination loadBalance(MessageImpl var1) {
      if (JMSDebug.JMSSAF.isDebugEnabled()) {
         JMSDebug.JMSSAF.debug(this + ": loadBlance() for message = " + var1.getId());
      }

      long var2 = var1.getSAFSeqNumber();
      DDMemberInfo var4 = null;
      if (JMSDebug.JMSSAF.isDebugEnabled()) {
         JMSDebug.JMSSAF.debug(this + ": loadBalance()for sequence number :" + var2);
      }

      boolean var5 = var1.getJMSDeliveryMode() == 2;

      try {
         if ((var4 = this.ddLBTable.getFailedDDMemberInfo(var2)) != null) {
            if (JMSDebug.JMSSAF.isDebugEnabled()) {
               JMSDebug.JMSSAF.debug(this + ": loadbalance() : failedDDMeberInfo=" + var4);
            }

            this.ddLBTable.removeFailedDDMemberInfo(var2);
            Destination var21 = this.findOrCreateDestination(var4);
            if (JMSDebug.JMSSAF.isDebugEnabled()) {
               JMSDebug.JMSSAF.debug(this + ": loadbalance() : destination=" + var21);
            }

            if (var21 == null) {
               return null;
            }

            this.addDestination(var4, this.ddInfo.getDestinationName().getConfigName(), this.ddInfo.getDestinationName().getJNDIName());
            return var21;
         }
      } catch (JMSException var20) {
         if (JMSDebug.JMSSAF.isDebugEnabled()) {
            JMSDebug.JMSSAF.debug(this + ": loadbalance() : failed \n" + StackTraceUtilsClient.throwable2StackTrace(var20));
         }

         if (var4 != null) {
            this.ddLBTable.addFailedDDMemberInfo(var2, var4);
            return var4.getDestination();
         }

         return null;
      }

      DDMemberInfo[] var6 = this.ddLBTable.getInDoubtDDMemberInfos();

      label209: {
         DDMemberInfo var8;
         try {
            if (!var5 || !this.isFirstPushedMessageNotInFailedMap || var6 == null || var6.length == 0) {
               break label209;
            }

            this.ddLBTable.removeInDoubtDDMemberInfos();
            int var7 = var6.length;
            var8 = var6[(int)var2 % var7];
            Destination var9 = this.findOrCreateDestination(var8);
            return var9;
         } catch (JMSException var18) {
            var8 = null;
         } finally {
            this.isFirstPushedMessageNotInFailedMap = false;
         }

         return var8;
      }

      synchronized(this) {
         if (this.ddImplArrayList.size() == 0) {
            return null;
         } else {
            this.freezeDDLBTable();
            int var22 = (int)var2 % this.ddImplArrayList.size();
            DistributedDestinationImpl var23 = this.loadBalancer.getNext(var22);
            return (Destination)this.ddMemberNameDImplMap.get(var23.getMemberName());
         }
      }
   }

   private Destination findOrCreateDestination(DDMemberInfo var1) throws JMSException {
      DestinationImpl var2 = var1.getDestination();
      if (JMSDebug.JMSSAF.isDebugEnabled()) {
         JMSDebug.JMSSAF.debug(this + ": findOrCreateDestination() : " + " ddMeberInfo=" + var1 + " dImpl =" + var2);
         if (var2 != null) {
            JMSDebug.JMSSAF.debug(this + ": findOrCreateDestination() : " + " dImpl.isStale= " + var2.isStale() + "dImpl.id= " + var2.getId());
         }
      }

      if (var2 != null && !var2.isStale() && var2.getId() != null) {
         return var2;
      } else {
         String var3 = var1.getType();
         String var4 = var1.getJMSServerName();
         String var5 = var1.getDDMemberConfigName();
         String var6 = var4 + "/" + var5;
         if (JMSDebug.JMSSAF.isDebugEnabled()) {
            JMSDebug.JMSSAF.debug(this + ": findOrCreateDestination() : " + " about to create " + var3 + " for " + var6);
         }

         this.subject = this.jmsSessionRuntimeContext.getSubject();
         this.pushSubject();

         try {
            if (var3.equals("javax.jms.Queue")) {
               var2 = (DestinationImpl)this.jmsSessionRuntimeContext.getJMSSession().createQueue(var6);
            } else {
               var2 = (DestinationImpl)this.jmsSessionRuntimeContext.getJMSSession().createTopic(var6);
            }
         } finally {
            this.popSubject();
         }

         var1.setDestination(var2);
         if (JMSDebug.JMSSAF.isDebugEnabled()) {
            JMSDebug.JMSSAF.debug(this + ": found destination " + var2);
         }

         return var2;
      }
   }

   private boolean checkIfDDMemberInfoHadFailedInLastRun(DDMemberInfo var1) {
      if (JMSDebug.JMSSAF.isDebugEnabled()) {
         JMSDebug.JMSSAF.debug(this + ": checkifDDMemberInfoHadFailedInlastRun()" + " ddMemberInfo= " + var1);
      }

      synchronized(this.ddLBTable) {
         List var3 = this.ddLBTable.getFailedDDMemberInfos();
         boolean var4 = var3.remove(var1);
         if (var4) {
            var3.add(var1);
         }

         return var4;
      }
   }

   private void addDestination(DDMemberInformation var1) throws JMSException {
      if (JMSDebug.JMSSAF.isDebugEnabled()) {
         JMSDebug.JMSSAF.debug(this + ": addDestination(): single: ddJNDIname= " + this.ddInfo.getDestinationName().getJNDIName() + " ddConfigName = " + var1.getDDConfigName() + " isProductionPaused= " + var1.isProductionPaused() + " isInsertionPaused= " + var1.isInsertionPaused());
      }

      if (!var1.isProductionPaused() && !var1.isInsertionPaused()) {
         String var2 = this.ddInfo.getDestinationName().getJNDIName();
         String var3 = var1.getDDConfigName();
         DestinationImpl var4 = (DestinationImpl)var1.getDestination();
         String var5 = var1.getDDType();
         DDMemberInfoImpl var6 = new DDMemberInfoImpl(var4.getServerName(), var4.getName(), var5, var4);
         if (!this.checkIfDDMemberInfoHadFailedInLastRun(var6)) {
            this.addDestination(var6, var3, var2);
         }
      }
   }

   private void addDestination(DDMemberInfo var1, String var2, String var3) throws JMSException {
      if (JMSDebug.JMSSAF.isDebugEnabled()) {
         JMSDebug.JMSSAF.debug(this + ": addDestination: ddMemberInfo= " + var1 + " ddConfigName=" + var2 + " ddJNIDName=" + var3);
      }

      String var4 = var1.getJMSServerName();
      String var5 = var1.getDDMemberConfigName();
      DestinationImpl var6 = var1.getDestination();
      String var7 = var4 + "/" + var5;
      var6 = (DestinationImpl)this.findOrCreateDestination(var1);
      DistributedDestinationImpl var8 = new DistributedDestinationImpl(var6.getType(), var6.getServerName(), var2, var6.getApplicationName(), var6.getModuleName(), this.getLoadBalancingPolicy(), this.getForwardingPolicy(), var7, var3, var6.getBackEndId(), var6.getDestinationId(), var6.getBackEndId().getDispatcherId(), true, var6.getPersistentStoreName(), this.ddInfo.getSAFExportPolicy(), false);
      synchronized(this) {
         if (this.freezed) {
            this.notifications.add(new NotificationItemAdd(var8, var7, var6, var1));
         } else {
            this.addDestinationToList(var8, var7, var6, var1);
         }

      }
   }

   private synchronized void addDestinationToList(DistributedDestinationImpl var1, String var2, DestinationImpl var3, DDMemberInfo var4) {
      int var5 = this.ddImplArrayList.indexOf(var1);
      if (var5 != -1) {
         this.ddImplArrayList.set(var5, var1);
      } else {
         this.ddImplArrayList.add(var1);
      }

      this.memberInformationDDImplMap.put(var2, var1);
      this.ddMemberNameDImplMap.put(var2, var3);
      DistributedDestinationImpl[] var6 = this.getDDImpls(this.ddImplArrayList);
      this.loadBalancer.refresh(var6);
      this.persistLoadBalancerInfo(1, var4);
   }

   private void removeDestination(DDMemberInformation var1) {
      if (JMSDebug.JMSSAF.isDebugEnabled()) {
         JMSDebug.JMSSAF.debug(this + ": removeDestination() single");
      }

      DestinationImpl var2 = (DestinationImpl)var1.getDestination();
      String var3 = var1.getDDType();
      DDMemberInfoImpl var4 = new DDMemberInfoImpl(var2.getServerName(), var2.getName(), var3, var2);
      this.removeDestination(var4, true);
   }

   private void removeDestination(DDMemberInfo var1, boolean var2) {
      if (JMSDebug.JMSSAF.isDebugEnabled()) {
         JMSDebug.JMSSAF.debug(this + ": removeDestination() : ddMemberInfo = " + var1 + " persist=" + var2);
      }

      String var3 = var1.getJMSServerName();
      String var4 = var1.getDDMemberConfigName();
      String var5 = var3 + "/" + var4;
      synchronized(this) {
         if (this.freezed) {
            this.notifications.add(new NotificationItemRemove(var5, var1, var2));
         } else {
            this.removeDestinationFromList(var5, var1, var2);
         }

      }
   }

   private synchronized void removeDestinationFromList(String var1, DDMemberInfo var2, boolean var3) {
      DistributedDestinationImpl var4 = (DistributedDestinationImpl)this.memberInformationDDImplMap.remove(var1);
      this.ddImplArrayList.remove(var4);
      this.ddMemberNameDImplMap.remove(var1);
      DistributedDestinationImpl[] var5 = this.getDDImpls(this.ddImplArrayList);
      this.loadBalancer.refresh(var5);
      if (var3) {
         this.persistLoadBalancerInfo(2, var2);
      }

   }

   public void close() {
      this.loadBalancerHandler.close();
      this.ddForwardStore.close();
   }

   public boolean hasNonFailedDDMembers() {
      DDMemberInfo[] var1 = this.ddLBTable.getDDMemberInfos();
      if (var1 == null) {
         return false;
      } else {
         return var1.length != 0;
      }
   }

   public void addFailedEndPoint(MessageImpl var1, DestinationImpl var2) throws PersistentStoreException {
      String var3 = null;
      if (var2.isQueue()) {
         var3 = new String("javax.jms.Queue");
      } else if (var2.isTopic()) {
         var3 = new String("javax.jms.Topic");
      }

      var2.markStale();
      DDMemberInfoImpl var4 = new DDMemberInfoImpl(var2.getServerName(), var2.getName(), var3, var2);
      this.ddLBTable.addFailedDDMemberInfo(var1.getSAFSeqNumber(), var4);
      this.removeDestination(var4, var1.getJMSDeliveryMode() == 2);
   }

   private DistributedDestinationImpl[] getDDImpls(Collection var1) {
      Iterator var2 = var1.iterator();
      int var3 = var1.size();
      DistributedDestinationImpl[] var4 = new DistributedDestinationImpl[var3];

      for(int var5 = 0; var2.hasNext(); ++var5) {
         var4[var5] = (DistributedDestinationImpl)var2.next();
      }

      return var4;
   }

   private void addDestination(DDMemberInformation[] var1) throws JMSException {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         this.addDestination(var1[var2]);
      }

   }

   private void removeDestination(DDMemberInformation[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         this.removeDestination(var1[var2]);
      }

   }

   private void persistLoadBalancerInfo(int var1, DDMemberInfo var2) {
      if (JMSDebug.JMSSAF.isDebugEnabled()) {
         JMSDebug.JMSSAF.debug(this + ": persistLoadBalancerInfo() : event = " + var1 + " ddMemberInfo=" + var2);
      }

      try {
         switch (var1) {
            case 1:
               synchronized(this.ddLBTable) {
                  this.ddLBTable.addDDMemberInfo(var2);
                  this.ddForwardStore.addOrUpdateDDLBTable(this.ddLBTable);
                  break;
               }
            case 2:
               synchronized(this.ddLBTable) {
                  this.ddLBTable.removeDDMemberInfo(var2);
                  this.ddForwardStore.addOrUpdateDDLBTable(this.ddLBTable);
               }
         }

      } catch (PersistentStoreException var8) {
         var8.printStackTrace();
         throw new AssertionError(var8);
      }
   }

   public void refreshSessionRuntimeContext(Context var1, Connection var2, Session var3, AbstractSubject var4) {
      if (JMSDebug.JMSSAF.isDebugEnabled()) {
         JMSDebug.JMSSAF.debug(this + ": refrshSessionRuntimeContext");
      }

      this.jmsSessionRuntimeContext.refresh(var1, var2, var3, var4);
   }

   private synchronized void freezeDDLBTable() {
      this.freezed = true;
   }

   public void unfreezeDDLBTable() {
      if (JMSDebug.JMSSAF.isDebugEnabled()) {
         JMSDebug.JMSSAF.debug("DDLoadBalancerDelegateImpl: unfreeze the LB table");
      }

      synchronized(this) {
         if (this.freezed) {
            this.freezed = false;
            Iterator var2 = this.notifications.iterator();

            try {
               while(var2.hasNext()) {
                  NotificationItem var3 = (NotificationItem)var2.next();
                  var3.processNotification();
               }
            } finally {
               this.notifications.clear();
            }

         }
      }
   }

   private synchronized void pushSubject() {
      if (this.subject != null) {
         SubjectManager.getSubjectManager().pushSubject(kernelID, this.subject);
      }

   }

   private synchronized void popSubject() {
      if (this.subject != null) {
         SubjectManager.getSubjectManager().popSubject(kernelID);
      }

   }

   public String toString() {
      return "[DDLoadBalancerDelegateImpl:  ddLBTable = " + this.ddLBTable + "]";
   }

   private final class NotificationItemRemove extends NotificationItem {
      private String name;
      private DDMemberInfo ddMemberInfo;
      private boolean persist;

      public NotificationItemRemove(String var2, DDMemberInfo var3, boolean var4) {
         super(null);
         this.name = var2;
         this.ddMemberInfo = var3;
         this.persist = var4;
      }

      void processNotification() {
         DDLoadBalancerDelegateImpl.this.removeDestinationFromList(this.name, this.ddMemberInfo, this.persist);
      }
   }

   private final class NotificationItemAdd extends NotificationItem {
      private DistributedDestinationImpl ddImpl;
      private String name;
      private DestinationImpl destImpl;
      private DDMemberInfo ddMemberInfo;

      public NotificationItemAdd(DistributedDestinationImpl var2, String var3, DestinationImpl var4, DDMemberInfo var5) {
         super(null);
         this.ddImpl = var2;
         this.name = var3;
         this.destImpl = var4;
         this.ddMemberInfo = var5;
      }

      void processNotification() {
         DDLoadBalancerDelegateImpl.this.addDestinationToList(this.ddImpl, this.name, this.destImpl, this.ddMemberInfo);
      }
   }

   private abstract class NotificationItem {
      private NotificationItem() {
      }

      abstract void processNotification();

      // $FF: synthetic method
      NotificationItem(Object var2) {
         this();
      }
   }

   private class LoadBalancerHandler implements DDMembersCacheChangeListener {
      private DDMembersCache ddMembersCache;

      public LoadBalancerHandler() {
         this.ddMembersCache = new DDMembersCacheImpl(DDLoadBalancerDelegateImpl.this.jmsSessionRuntimeContext, DDLoadBalancerDelegateImpl.this.ddInfo.getDestinationName(), DDLoadBalancerDelegateImpl.this.jmsSessionRuntimeContext.isForLocalCluster());
         this.ddMembersCache.addDDMembersCacheChangeListener(this);
      }

      public JMSID getId() {
         return null;
      }

      public void close() {
         this.ddMembersCache.removeDDMembersCacheChangeListener(this);
      }

      public void onCacheEntryAdd(CacheEntry var1) {
         if (JMSDebug.JMSSAF.isDebugEnabled()) {
            JMSDebug.JMSSAF.debug(this + ": onCacheEntryAdd() single: " + " Entry = " + var1 + " isProductionPaused= " + ((DDMemberInformation)var1).isProductionPaused() + " isInsertionPaused= " + ((DDMemberInformation)var1).isInsertionPaused());
         }

         synchronized(DDLoadBalancerDelegateImpl.this) {
            try {
               DDLoadBalancerDelegateImpl.this.addDestination((DDMemberInformation)var1);
            } catch (Throwable var5) {
               var5.printStackTrace();
            }

         }
      }

      public void onCacheEntryRemove(CacheEntry var1) {
         if (JMSDebug.JMSSAF.isDebugEnabled()) {
            JMSDebug.JMSSAF.debug(this + ": onCacheEntryRemove() single");
         }

         synchronized(DDLoadBalancerDelegateImpl.this) {
            DDLoadBalancerDelegateImpl.this.removeDestination((DDMemberInformation)var1);
         }
      }

      public void onCacheEntryAdd(CacheEntry[] var1) {
         if (JMSDebug.JMSSAF.isDebugEnabled()) {
            JMSDebug.JMSSAF.debug(this + ": onCacheEntryAdd() multiple");
         }

         synchronized(DDLoadBalancerDelegateImpl.this) {
            try {
               DDLoadBalancerDelegateImpl.this.addDestination((DDMemberInformation[])((DDMemberInformation[])var1));
            } catch (JMSException var5) {
               var5.printStackTrace();
            }

         }
      }

      public void onCacheEntryRemove(CacheEntry[] var1) {
         if (JMSDebug.JMSSAF.isDebugEnabled()) {
            JMSDebug.JMSSAF.debug(this + ": onCacheEntryRemove() multiple");
         }

         synchronized(DDLoadBalancerDelegateImpl.this) {
            DDLoadBalancerDelegateImpl.this.removeDestination((DDMemberInformation[])((DDMemberInformation[])var1));
         }
      }
   }
}
