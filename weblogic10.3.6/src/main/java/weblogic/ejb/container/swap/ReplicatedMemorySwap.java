package weblogic.ejb.container.swap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Serializable;
import java.rmi.Remote;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.ejb.EJBContext;
import javax.ejb.EnterpriseBean;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import weblogic.cluster.replication.ROID;
import weblogic.cluster.replication.Replicatable;
import weblogic.cluster.replication.ReplicationManager;
import weblogic.cluster.replication.ReplicationServices;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.InternalException;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.BeanManager;
import weblogic.ejb.container.interfaces.SessionBeanInfo;
import weblogic.ejb.container.interfaces.WLEnterpriseBean;
import weblogic.ejb.container.manager.StatefulSessionManager;
import weblogic.rmi.cluster.PrimarySecondaryRemoteObject;
import weblogic.utils.ByteArrayDiff;
import weblogic.utils.ByteArrayDiffChecker;
import weblogic.utils.Debug;
import weblogic.utils.io.UnsyncByteArrayInputStream;

public final class ReplicatedMemorySwap implements EJBSwap {
   private static final boolean debug = System.getProperty("weblogic.ejb.swap.debug") != null;
   private PassivationUtils passivationUtils;
   private ReplicationServices repServ = null;
   private Map previousVersionMap = new ConcurrentHashMap();
   private Map secondaryBeanMap = new ConcurrentHashMap();
   private Map primaryROMap = new ConcurrentHashMap();
   private Map secondaryROMap = new ConcurrentHashMap();
   private EJBSwap diskSwap;
   private DiffChecker diffChecker;
   private StatefulSessionManager beanManager;
   private SessionBeanInfo beanInfo;

   private ReplicationServices getRepServ() {
      if (this.repServ == null) {
         this.repServ = ReplicationManager.services();
      }

      return this.repServ;
   }

   public void setup(BeanInfo var1, BeanManager var2, ClassLoader var3) {
      this.beanManager = (StatefulSessionManager)var2;
      this.beanInfo = (SessionBeanInfo)var1;
      this.passivationUtils = new PassivationUtils(var3);
      this.diskSwap = new DiskSwap(new File(this.beanManager.getSwapDirectoryName()), this.beanInfo.getIdleTimeoutMS(), this.beanInfo.getSessionTimeoutMS());
      this.diskSwap.setup(var1, var2, var3);
      this.diffChecker = this.getDiffChecker(this.beanInfo);
   }

   public synchronized EnterpriseBean read(Object var1) throws InternalException {
      Object var2 = this.secondaryBeanMap.remove(var1);
      return var2 == null ? this.diskSwap.read(var1) : this.diffChecker.read(var1, var2);
   }

   public void remove(Object var1) {
      if (this.secondaryROMap.remove(var1) == null) {
         this.getRepServ().unregister((ROID)var1, Replicatable.DEFAULT_KEY);
      }

      this.secondaryBeanMap.remove(var1);
      this.primaryROMap.remove(var1);
      this.previousVersionMap.remove(var1);
   }

   public void becomePrimary(Object var1) {
      if (debug) {
         Debug.say(" ** becomePrimary with key: " + var1);
      }

      if (this.secondaryROMap.get(var1) != null) {
         if (debug) {
            Debug.say(" ** becomePrimary on old secondary with key: " + var1);
         }

         this.primaryROMap.put(var1, this.secondaryROMap.remove(var1));
      }

   }

   public void savePrimaryRO(Object var1, Object var2) {
      if (debug) {
         Debug.say(" savePrimaryRO " + var1);
      }

      this.primaryROMap.put(var1, var2);
   }

   public void saveSecondaryRO(Object var1, Object var2) {
      this.secondaryROMap.put(var1, var2);
   }

   public void receiveUpdate(Object var1, Object var2) throws InternalException {
      if (debug) {
         Debug.say("** Received update for pk: " + var1);
      }

      this.diffChecker.receiveUpdate(var1, var2);
   }

   public void sendUpdate(Object var1, Object var2) throws InternalException {
      if (debug) {
         Debug.say("**  Sending an update for : " + var1);
      }

      Serializable var3 = this.diffChecker.getDiff(var1, var2);

      try {
         PrimarySecondaryRemoteObject var4 = (PrimarySecondaryRemoteObject)this.primaryROMap.get(var1);
         Remote var5 = null;
         if (var4 != null) {
            var5 = var4.getSecondary();
         }

         Remote var6 = (Remote)this.getRepServ().updateSecondary((ROID)var1, var3, Replicatable.DEFAULT_KEY);
         if (var5 != var6) {
            if (debug) {
               Debug.say("New secondary created");
            }

            var4.changeSecondary(var6);
            this.getRepServ().updateSecondary((ROID)var1, this.diffChecker.getCompleteDiff(var1, var2), Replicatable.DEFAULT_KEY);
         }
      } catch (Exception var7) {
         this.previousVersionMap.remove(var1);
         if (this.beanInfo.isEJB30()) {
            EJBLogger.logFailedToUpdateSecondaryFromBusiness(this.beanManager.getEJBHome().getDisplayName());
         } else {
            EJBLogger.logFailedToUpdateSecondary(this.beanManager.getEJBHome().getDisplayName());
         }
      }

   }

   public void write(Object var1, Object var2) throws InternalException {
      this.secondaryBeanMap.remove(var1);
      this.primaryROMap.remove(var1);
      this.secondaryROMap.remove(var1);
      this.previousVersionMap.remove(var1);
      this.diskSwap.write(var1, var2);
   }

   public void updateClassLoader(ClassLoader var1) {
      this.diskSwap.updateClassLoader(var1);
      this.passivationUtils.updateClassLoader(var1);
   }

   public void updateIdleTimeoutMS(long var1) {
      this.diskSwap.updateIdleTimeoutMS(var1);
   }

   public void cancelTrigger() {
   }

   private DiffChecker getDiffChecker(SessionBeanInfo var1) {
      if (var1.getCalculateDeltaUsingReflection()) {
         if (debug) {
            Debug.say("Bean changes will be calculated using reflection");
         }

         return new BeanStateChecker(var1.getBeanClass());
      } else {
         if (debug) {
            Debug.say("Bean changes will be calculated using byte comparison");
         }

         return new ByteLevelChecker();
      }
   }

   private interface DiffChecker {
      EnterpriseBean read(Object var1, Object var2) throws InternalException;

      void receiveUpdate(Object var1, Object var2) throws InternalException;

      Serializable getDiff(Object var1, Object var2) throws InternalException;

      Serializable getCompleteDiff(Object var1, Object var2) throws InternalException;
   }

   private class ByteLevelChecker implements DiffChecker {
      private ByteLevelChecker() {
      }

      public EnterpriseBean read(Object var1, Object var2) throws InternalException {
         UnsyncByteArrayInputStream var3 = new UnsyncByteArrayInputStream((byte[])((byte[])var2));
         return ReplicatedMemorySwap.this.passivationUtils.read(ReplicatedMemorySwap.this.beanManager, var3, var1);
      }

      public void receiveUpdate(Object var1, Object var2) {
         byte[] var3 = (byte[])((byte[])ReplicatedMemorySwap.this.secondaryBeanMap.get(var1));
         if (var2 instanceof ByteArrayDiff) {
            ByteArrayDiff var4 = (ByteArrayDiff)var2;
            var3 = var4.applyDiff(var3);
         } else {
            var3 = (byte[])((byte[])var2);
         }

         ReplicatedMemorySwap.this.secondaryBeanMap.put(var1, var3);
      }

      public Serializable getDiff(Object var1, Object var2) throws InternalException {
         ByteArrayOutputStream var3 = new ByteArrayOutputStream();
         ReplicatedMemorySwap.this.passivationUtils.write(ReplicatedMemorySwap.this.beanManager, var3, var1, var2);
         byte[] var4 = var3.toByteArray();
         byte[] var5 = (byte[])((byte[])ReplicatedMemorySwap.this.previousVersionMap.put(var1, var4));
         return (new ByteArrayDiffChecker()).diffByteArrays(var5, var4);
      }

      public Serializable getCompleteDiff(Object var1, Object var2) throws InternalException {
         ByteArrayOutputStream var3 = new ByteArrayOutputStream();
         ReplicatedMemorySwap.this.passivationUtils.write(ReplicatedMemorySwap.this.beanManager, var3, var1, var2);
         return var3.toByteArray();
      }

      // $FF: synthetic method
      ByteLevelChecker(Object var2) {
         this();
      }
   }

   private class BeanStateChecker implements DiffChecker {
      BeanStateDiffChecker delegate;

      BeanStateChecker(Class var2) {
         this.delegate = new BeanStateDiffChecker(var2);
      }

      public EnterpriseBean read(Object var1, Object var2) throws InternalException {
         try {
            EnterpriseBean var3 = ReplicatedMemorySwap.this.beanManager.allocateBean();
            EJBContext var4 = ReplicatedMemorySwap.this.beanManager.allocateContext((EnterpriseBean)null, var1);
            ((SessionBean)var3).setSessionContext((SessionContext)var4);
            ((WLEnterpriseBean)var3).__WL_setEJBContext(var4);
            this.delegate.setState(var3, (BeanState)var2);
            return var3;
         } catch (Exception var5) {
            throw new InternalException("Error creating replicated bean", var5);
         }
      }

      public void receiveUpdate(Object var1, Object var2) throws InternalException {
         try {
            ArrayList var3 = (ArrayList)var2;
            if (ReplicatedMemorySwap.debug) {
               Debug.say("Received update with diff " + var3);
            }

            BeanState var4 = (BeanState)ReplicatedMemorySwap.this.secondaryBeanMap.get(var1);
            if (var4 == null) {
               var4 = new BeanState();
               ReplicatedMemorySwap.this.secondaryBeanMap.put(var1, var4);
            }

            this.delegate.mergeDiff(var4, var3);
         } catch (Exception var5) {
            throw new InternalException("Error while processing bean update", var5);
         }
      }

      public Serializable getDiff(Object var1, Object var2) throws InternalException {
         ArrayList var3 = null;

         try {
            BeanState var4 = (BeanState)ReplicatedMemorySwap.this.previousVersionMap.get(var1);
            if (var4 == null) {
               var4 = new BeanState();
               ReplicatedMemorySwap.this.previousVersionMap.put(var1, var4);
            }

            var3 = this.delegate.calculateDiff(var2, var4);
         } catch (Exception var5) {
            throw new InternalException("Error while calculating diff", var5);
         }

         if (ReplicatedMemorySwap.debug) {
            Debug.say("Calculated diff " + var3);
         }

         return var3;
      }

      public Serializable getCompleteDiff(Object var1, Object var2) throws InternalException {
         ArrayList var3 = null;
         BeanState var4 = new BeanState();

         try {
            var3 = this.delegate.calculateDiff(var2, var4);
            return var3;
         } catch (Exception var6) {
            throw new InternalException("Error while calculating diff", var6);
         }
      }
   }
}
