package weblogic.ejb.container.internal;

import javax.ejb.EnterpriseBean;
import javax.transaction.Transaction;
import weblogic.utils.Debug;

public final class InvocationWrapper {
   private static boolean verbose = false;
   private Transaction callerTx;
   private Transaction invokeTx;
   private Thread thread;
   private EnterpriseBean bean;
   private Object primaryKey;
   private MethodDescriptor md;
   private boolean isLocal;
   private boolean hasSystemExceptionOccured;
   private boolean isRemoteInvocation;
   private boolean shouldLogException = true;

   public InvocationWrapper(Transaction var1, Transaction var2, Thread var3, MethodDescriptor var4) {
      this.callerTx = var1;
      this.invokeTx = var2;
      this.thread = var3;
      this.md = var4;
      this.isLocal = var4.isLocal();
   }

   public InvocationWrapper(Transaction var1, Transaction var2, Thread var3) {
      this.callerTx = var1;
      this.invokeTx = var2;
      this.thread = var3;
   }

   public final String toString() {
      return "[InvocationWrapper] callerTx: " + this.callerTx + " invokeTx: " + this.invokeTx + " bean:" + this.bean + " primaryKey: " + this.primaryKey + "MethodDescriptor: " + this.md;
   }

   public EnterpriseBean getBean() {
      return this.bean;
   }

   public void setBean(EnterpriseBean var1) {
      this.bean = var1;
   }

   public Transaction getCallerTx() {
      if (verbose) {
         Debug.say("Returning a caller tx of " + this.callerTx);
      }

      return this.callerTx;
   }

   void setCallerTx(Transaction var1) {
      this.callerTx = var1;
   }

   public Transaction getInvokeTx() {
      if (verbose) {
         Debug.say("Returning an invoke tx: " + this.invokeTx);
      }

      return this.invokeTx;
   }

   public void setInvokeTx(Transaction var1) {
      this.invokeTx = var1;
   }

   public Thread getThread() {
      if (verbose) {
         Debug.say("Returning thread: " + this.thread);
      }

      return this.thread;
   }

   public void setThread(Thread var1) {
      this.thread = var1;
   }

   public Object getInvokeTxOrThread() {
      return this.invokeTx != null ? this.invokeTx : this.thread;
   }

   public Object getPrimaryKey() {
      return this.primaryKey;
   }

   public void setPrimaryKey(Object var1) {
      this.primaryKey = var1;
   }

   public MethodDescriptor getMethodDescriptor() {
      return this.md;
   }

   void setMethodDescriptor(MethodDescriptor var1) {
      this.md = var1;
      this.isLocal = this.md.isLocal();
   }

   public boolean isLocal() {
      return this.isLocal;
   }

   public void setIsLocal(boolean var1) {
      this.isLocal = var1;
   }

   public boolean hasSystemExceptionOccured() {
      return this.hasSystemExceptionOccured;
   }

   public void setSystemExceptionOccured() {
      this.hasSystemExceptionOccured = true;
   }

   public void setIsRemoteInvocation() {
      this.isRemoteInvocation = true;
   }

   public boolean isRemoteInvocation() {
      return this.isRemoteInvocation;
   }

   public boolean shouldLogException() {
      return this.shouldLogException;
   }

   public void skipLoggingException() {
      this.shouldLogException = false;
   }
}
