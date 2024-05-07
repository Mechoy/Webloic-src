package weblogic.jms.safclient.transaction.jta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import javax.transaction.HeuristicMixedException;
import javax.transaction.RollbackException;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import weblogic.transaction.nonxa.NonXAResource;

public final class SimpleTransaction implements Transaction, weblogic.transaction.Transaction {
   private String name;
   private SimpleTransactionManager manager;
   private SimpleXid xid = new SimpleXid();
   private int status = 0;
   private boolean suspended;
   private ArrayList resources;
   private LinkedHashSet synchronizations;
   private HashMap properties;
   private Throwable rollbackReason;
   private long startTime;

   SimpleTransaction(String var1, SimpleTransactionManager var2) {
      this.name = var1;
      this.manager = var2;
      this.startTime = System.currentTimeMillis();
      var2.addTransaction(this);
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public String getName() {
      return this.name;
   }

   public Xid getXID() {
      return this.xid;
   }

   public Xid getXid() {
      return this.xid;
   }

   public long getMillisSinceBegin() {
      return System.currentTimeMillis() - this.startTime;
   }

   public long getTimeToLiveMillis() {
      return Long.MAX_VALUE;
   }

   public boolean isTimedOut() {
      return false;
   }

   public boolean isTxAsyncTimeout() {
      return false;
   }

   private synchronized void setPropertyInternal(String var1, Object var2) {
      if (this.properties == null) {
         this.properties = new HashMap();
      }

      this.properties.put(var1, var2);
   }

   public void setProperty(String var1, Serializable var2) {
      this.setPropertyInternal(var1, var2);
   }

   public void setLocalProperty(String var1, Object var2) {
      this.setPropertyInternal(var1, var2);
   }

   public synchronized void addProperties(Map var1) {
      if (this.properties == null) {
         this.properties = new HashMap();
      }

      this.properties.putAll(var1);
   }

   public void addLocalProperties(Map var1) {
      this.addProperties(var1);
   }

   private synchronized Object getPropertyInternal(String var1) {
      return this.properties == null ? null : this.properties.get(var1);
   }

   public Serializable getProperty(String var1) {
      return (Serializable)this.getPropertyInternal(var1);
   }

   public Object getLocalProperty(String var1) {
      return this.getPropertyInternal(var1);
   }

   public synchronized Map getProperties() {
      return new HashMap(this.properties);
   }

   public Map getLocalProperties() {
      return this.getProperties();
   }

   public synchronized int getStatus() {
      return this.status;
   }

   private synchronized void setStatus(int var1) {
      this.status = var1;
   }

   synchronized boolean isSuspended() {
      return this.suspended;
   }

   private synchronized void setSuspended(boolean var1) {
      this.suspended = var1;
   }

   public synchronized void setRollbackOnly() {
      if (this.status == 0) {
         this.status = 1;
      } else {
         throw new IllegalStateException("Transaction is not active");
      }
   }

   public synchronized void setRollbackOnly(Throwable var1) {
      this.setRollbackOnly();
      this.rollbackReason = var1;
   }

   public synchronized void setRollbackOnly(String var1, Throwable var2) {
      this.setRollbackOnly();
      this.rollbackReason = new Exception(var1, var2);
   }

   public synchronized Throwable getRollbackReason() {
      return this.rollbackReason;
   }

   public void commit() throws RollbackException, HeuristicMixedException, SystemException {
      ArrayList var1 = null;
      ArrayList var2 = null;
      synchronized(this) {
         if (this.status == 1) {
            this.rollback();
            throw new RollbackException();
         }

         if (this.status != 0 && this.status != 2) {
            throw new IllegalStateException("Transaction not active");
         }

         this.status = 7;
         if (this.synchronizations != null) {
            var1 = new ArrayList(this.synchronizations);
         }

         if (this.resources != null) {
            var2 = new ArrayList(this.resources);
         }
      }

      if (var1 != null) {
         this.beforeCompletion(var1);
      }

      if (var2 != null && var2.size() > 1) {
         this.driveTwoPhaseCommit(var1, var2);
      } else {
         this.driveOnePhaseCommit(var1, var2);
      }

      this.manager.removeTransaction(this);
   }

   public boolean prepare() throws RollbackException, SystemException {
      ArrayList var1 = null;
      ArrayList var2 = null;
      synchronized(this) {
         if (this.status == 1) {
            this.rollback();
            throw new RollbackException();
         }

         if (this.status != 0) {
            throw new IllegalStateException("Transaction not active");
         }

         this.status = 7;
         if (this.synchronizations != null) {
            var1 = new ArrayList(this.synchronizations);
         }

         if (this.resources != null) {
            var2 = new ArrayList(this.resources);
         }
      }

      if (var1 != null) {
         this.beforeCompletion(var1);
      }

      boolean var3 = this.drivePrepare(var2);
      this.setStatus(2);
      return var3;
   }

   public void rollback() throws SystemException {
      ArrayList var1 = null;
      ArrayList var2 = null;
      synchronized(this) {
         if (this.status != 0 && this.status != 1 && this.status != 2) {
            throw new IllegalStateException("Transaction not active");
         }

         this.status = 9;
         if (this.synchronizations != null) {
            var1 = new ArrayList(this.synchronizations);
         }

         if (this.resources != null) {
            var2 = new ArrayList(this.resources);
         }
      }

      if (this.status != 2 && var1 != null) {
         this.beforeCompletion(var1);
      }

      this.driveRollback(var1, var2);
      this.manager.removeTransaction(this);
   }

   private void driveOnePhaseCommit(ArrayList var1, ArrayList var2) throws SystemException {
      this.setStatus(8);
      if (var2 != null) {
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            XAResourceHolder var4 = (XAResourceHolder)var3.next();

            try {
               this.delist(var4);
               var4.getResource().commit(this.xid, true);
            } catch (XAException var7) {
               SystemException var6 = new SystemException("XA error on commit: " + var7.errorCode);
               var6.initCause(var7);
               throw var6;
            }
         }
      }

      this.setStatus(3);
      if (var1 != null) {
         this.afterCompletion(var1, 3);
      }

   }

   private boolean drivePrepare(ArrayList var1) throws SystemException {
      SystemException var2 = null;
      if (var1 != null) {
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            XAResourceHolder var4 = (XAResourceHolder)var3.next();

            try {
               this.delist(var4);
            } catch (XAException var8) {
               var2 = new SystemException("XA error preparing transaction: " + var8.errorCode);
               var2.initCause(var8);
            }
         }
      }

      if (var2 != null) {
         throw var2;
      } else {
         boolean var9 = false;
         if (var1 != null) {
            Iterator var10 = var1.iterator();

            while(var10.hasNext()) {
               XAResourceHolder var5 = (XAResourceHolder)var10.next();

               try {
                  if (var5.getResource().prepare(this.xid) == 3) {
                     var5.setReadOnly(true);
                  }
               } catch (XAException var7) {
                  var9 = true;
               }
            }
         }

         return !var9;
      }
   }

   private void driveTwoPhaseCommit(ArrayList var1, ArrayList var2) throws SystemException, HeuristicMixedException, RollbackException {
      boolean var3 = !this.drivePrepare(var2);
      if (var3) {
         this.driveRollback(var1, var2);
         throw new RollbackException();
      } else {
         XAException var4 = null;
         Iterator var5 = var2.iterator();

         while(var5.hasNext()) {
            XAResourceHolder var6 = (XAResourceHolder)var5.next();

            try {
               if (!var6.isReadOnly()) {
                  var6.getResource().commit(this.xid, false);
               }
            } catch (XAException var8) {
               var4 = var8;
            }
         }

         if (var4 != null) {
            this.setStatus(5);
            HeuristicMixedException var9 = new HeuristicMixedException("Error commiting XA transaction: " + var4.errorCode);
            var9.initCause(var4);
            throw var9;
         } else {
            this.setStatus(3);
            if (var1 != null) {
               this.afterCompletion(var1, 3);
            }

         }
      }
   }

   private void driveRollback(ArrayList var1, ArrayList var2) throws SystemException {
      SystemException var3 = null;
      if (var2 != null) {
         Iterator var4 = var2.iterator();

         while(var4.hasNext()) {
            XAResourceHolder var5 = (XAResourceHolder)var4.next();

            try {
               this.delist(var5);
               var5.getResource().rollback(this.xid);
            } catch (XAException var7) {
               var3 = new SystemException("XA error on rollback: " + var7.errorCode);
               var3.initCause(var7);
            }
         }
      }

      this.setStatus(4);
      if (var1 != null) {
         this.afterCompletion(var1, 4);
      }

      if (var3 != null) {
         throw var3;
      }
   }

   private void delist(XAResourceHolder var1) throws XAException {
      if (var1.isEnlisted()) {
         var1.getResource().end(this.xid, 67108864);
         var1.setEnlisted(false);
      } else if (var1.isSuspended()) {
         var1.getResource().end(this.xid, 67108864);
         var1.setSuspended(false);
      }

   }

   private void beforeCompletion(ArrayList var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         Synchronization var3 = (Synchronization)var2.next();
         var3.beforeCompletion();
      }

   }

   private void afterCompletion(ArrayList var1, int var2) {
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         Synchronization var4 = (Synchronization)var3.next();
         var4.afterCompletion(var2);
      }

   }

   public boolean enlistResource(XAResource var1) throws RollbackException, SystemException {
      int var2 = 0;
      XAResourceHolder var3;
      synchronized(this) {
         if (this.status == 1) {
            throw new RollbackException();
         }

         if (this.status != 0) {
            throw new IllegalStateException("Transaction not active");
         }

         if (this.resources == null) {
            this.resources = new ArrayList();
         }

         var3 = this.findResource(var1);
         if (var3 == null) {
            SimpleTransactionManager.ResourceRec var5 = null;

            try {
               var5 = this.manager.findResource(var1);
            } catch (XAException var9) {
               throw new SystemException(var9.toString());
            }

            if (var5 == null) {
               throw new SystemException("XAResource was not registered");
            }

            var3 = new XAResourceHolder(var5);
            this.resources.add(var3);
         } else {
            if (var3.isEnlisted()) {
               return true;
            }

            if (var3.isSuspended()) {
               var2 = 134217728;
            }
         }
      }

      try {
         var1.start(this.xid, var2);
      } catch (XAException var8) {
         SystemException var11 = new SystemException("XA failure: " + var8.errorCode);
         var11.initCause(var8);
         throw var11;
      }

      var3.setSuspended(false);
      var3.setEnlisted(true);
      return true;
   }

   public boolean enlistResource(XAResource var1, String var2) throws IllegalStateException, SystemException {
      throw new SystemException("Not supported.");
   }

   public boolean delistResource(XAResource var1, int var2) throws SystemException {
      XAResourceHolder var3;
      synchronized(this) {
         if (this.status != 0 && this.status != 1) {
            throw new IllegalStateException("Transaction not active");
         }

         var3 = this.findResource(var1);
         if (var3 == null || !var3.isEnlisted()) {
            throw new IllegalStateException("Resource not enlisted");
         }
      }

      try {
         var1.end(this.xid, var2);
      } catch (XAException var6) {
         SystemException var5 = new SystemException("XA failure: " + var6.errorCode);
         var5.initCause(var6);
         throw var5;
      }

      var3.setEnlisted(false);
      if (var2 == 33554432) {
         var3.setSuspended(true);
      }

      return true;
   }

   void suspendAll() throws SystemException {
      ArrayList var1;
      synchronized(this) {
         var1 = this.resources == null ? new ArrayList() : new ArrayList(this.resources);
      }

      SystemException var2 = null;
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         XAResourceHolder var4 = (XAResourceHolder)var3.next();

         try {
            if (var4.isEnlisted() && !var4.isSuspended()) {
               var4.getResource().end(this.xid, 33554432);
               var4.setEnlisted(false);
               var4.setSuspended(true);
            }
         } catch (XAException var6) {
            var2 = new SystemException("XA error suspending transaction: " + var6.errorCode);
            var2.initCause(var6);
         }
      }

      this.setSuspended(true);
      if (var2 != null) {
         throw var2;
      }
   }

   void resumeAll() throws SystemException {
      ArrayList var1;
      synchronized(this) {
         var1 = this.resources == null ? new ArrayList() : new ArrayList(this.resources);
      }

      SystemException var2 = null;
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         XAResourceHolder var4 = (XAResourceHolder)var3.next();

         try {
            if (var4.isSuspended()) {
               var4.getResource().start(this.xid, 134217728);
               var4.setEnlisted(true);
               var4.setSuspended(false);
            }
         } catch (XAException var6) {
            var2 = new SystemException("XA error resuming transaction: " + var6.errorCode);
            var2.initCause(var6);
         }
      }

      this.setSuspended(false);
      if (var2 != null) {
         throw var2;
      }
   }

   public synchronized void registerSynchronization(Synchronization var1) throws RollbackException {
      if (this.status == 1) {
         throw new RollbackException();
      } else if (this.status != 0) {
         throw new IllegalStateException("Transaction not active");
      } else {
         if (this.synchronizations == null) {
            this.synchronizations = new LinkedHashSet();
         }

         this.synchronizations.add(var1);
      }
   }

   public String getStatusAsString() {
      switch (this.status) {
         case 0:
            return "Active";
         case 1:
            return "Marked Rollback";
         case 2:
            return "Prepared";
         case 3:
            return "Committed";
         case 4:
            return "Rolled Back";
         case 5:
         default:
            return "Unknown";
         case 6:
            return "No Transaction";
         case 7:
            return "Preparing";
         case 8:
            return "Committing";
         case 9:
            return "Rolling Back";
      }
   }

   public String getHeuristicErrorMessage() {
      return null;
   }

   public Object invokeCoordinatorService(String var1, Object var2) throws SystemException {
      throw new SystemException("Not implemented");
   }

   public boolean isCoordinatorLocal() {
      return true;
   }

   public boolean isCoordinatorAssigned() {
      return true;
   }

   public boolean enlistResource(NonXAResource var1) throws SystemException {
      throw new SystemException("Not implemented");
   }

   private synchronized XAResourceHolder findResource(XAResource var1) {
      try {
         Iterator var2 = this.resources.iterator();

         XAResourceHolder var3;
         do {
            if (!var2.hasNext()) {
               return null;
            }

            var3 = (XAResourceHolder)var2.next();
         } while(!var3.isSameRM(var1));

         return var3;
      } catch (XAException var4) {
         return null;
      }
   }

   private static final class XAResourceHolder {
      private SimpleTransactionManager.ResourceRec res;
      private boolean suspended;
      private boolean enlisted;
      private boolean readOnly;

      XAResourceHolder(SimpleTransactionManager.ResourceRec var1) {
         this.res = var1;
      }

      XAResource getResource() {
         return this.res.getResource();
      }

      synchronized boolean isSuspended() {
         return this.suspended;
      }

      synchronized void setSuspended(boolean var1) {
         this.suspended = var1;
      }

      synchronized boolean isEnlisted() {
         return this.enlisted;
      }

      synchronized void setEnlisted(boolean var1) {
         this.enlisted = var1;
      }

      synchronized boolean isReadOnly() {
         return this.readOnly;
      }

      synchronized void setReadOnly(boolean var1) {
         this.readOnly = var1;
      }

      public boolean isSameRM(XAResource var1) throws XAException {
         return this.res.getResource() == var1 || this.res.getResource().isSameRM(var1);
      }
   }
}
