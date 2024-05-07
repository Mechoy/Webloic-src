package weblogic.messaging.dispatcher;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import weblogic.jms.common.JMSDebug;
import weblogic.messaging.ID;
import weblogic.messaging.common.IDImpl;
import weblogic.messaging.common.MessagingUtilities;
import weblogic.rmi.extensions.AsyncResult;
import weblogic.rmi.extensions.AsyncResultListener;
import weblogic.rmi.extensions.server.FutureResponse;
import weblogic.transaction.ClientTransactionManager;
import weblogic.transaction.TransactionHelper;
import weblogic.utils.StackTraceUtilsClient;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerFactory;

public abstract class Request extends Response implements Runnable, AsyncResultListener, Externalizable {
   static final long serialVersionUID = -3580248041850964617L;
   protected ID invocableId;
   protected int methodId;
   private static final int EXTVERSION = 1;
   private static final int VERSION_MASK = 255;
   private static final int INVOCABLE_ID_MASK = 256;
   private static final int CORRELATION_ID_MASK = 512;
   public static final int START = 0;
   public static final int FINISH = 2;
   public static final int COMPLETED = Integer.MAX_VALUE;
   public static final int REMOTE = -42;
   private int state;
   private Response result;
   private Throwable throwableResponse;
   private Request parent;
   private Request child;
   protected Request next;
   private int numWaiting;
   private int numChildren;
   private boolean isCollocated;
   private boolean running;
   private WorkManager workManager;
   private boolean isSyncRequest;
   private AsyncResult asyncResult;
   private FutureResponse futureResponse;
   private boolean needFillInStackTrace;
   private int tranInfo;
   public static final int NO_TRAN = 0;
   public static final int HAVE_TRAN = 1;
   public static final int DONT_KNOW_IF_TRAN = 2;
   private Transaction transaction;
   private Invocable invocable;
   private InvocableMonitor invocableMonitor;
   private InvocableManager appInvocableManager;
   private InteropJMSVoidResponsePreDiablo appVoidResponse;
   private boolean shortened;
   private volatile boolean parentResumeNewThread;
   private CompletionListener completionListener;
   private static WorkManager workManagerDefault = WorkManagerFactory.getInstance().getSystem();
   static boolean TODOSAGREMOVEreportOnce = true;
   static SimpleDateFormat sdf = new SimpleDateFormat("(EEEMMMdd,HH:mm:ss)");
   public static final int RMI_TRANSACTION = 1;
   public static final int RMI_FUTURE_RESPONSE = 2;
   public static final int RMI_SYNC = 16;
   public static final int RMI_ASYNC_RESULT = 32;
   public static final int RMI_ONEWAY = 64;

   public Request(ID var1, int var2, InvocableManager var3) {
      this(var1, var2, new VoidResponse(), var3);
   }

   public Request(ID var1, int var2, InteropJMSVoidResponsePreDiablo var3, InvocableManager var4) {
      this.invocableId = var1;
      this.methodId = var2;
      this.result = this;
      this.isCollocated = true;
      this.appVoidResponse = var3;
      this.appInvocableManager = var4;
   }

   public final void setAppVoidResponse(InteropJMSVoidResponsePreDiablo var1) {
      this.appVoidResponse = var1;
   }

   public final void setAppInvocableManager(InvocableManager var1) {
      this.appInvocableManager = var1;
   }

   InvocableMonitor getInvocableMonitor() {
      return this.invocableMonitor;
   }

   public final void setInvocableId(ID var1) {
      this.invocableId = var1;
   }

   public final ID getInvocableId() {
      return this.invocableId;
   }

   public final void setMethodId(int var1) {
      this.methodId = var1;
   }

   public final int getMethodId() {
      return this.methodId;
   }

   public final void clearResult() {
      this.result = this;
      this.throwableResponse = null;
   }

   public final void clearState() {
      this.result = this;
      this.throwableResponse = null;
      this.setNumChildren(0);
   }

   private int getNumChildren() {
      return this.numChildren;
   }

   private void setNumChildren(int var1) {
      this.numChildren = var1;
   }

   int incNumChildren() {
      return ++this.numChildren;
   }

   private int decNumChildren() {
      return --this.numChildren;
   }

   protected WorkManager getWorkManager() {
      return this.workManager != null ? this.workManager : this.getDefaultWorkManager();
   }

   public void setWorkManager(WorkManager var1) {
      this.workManager = var1;
   }

   protected WorkManager getDefaultWorkManager() {
      return workManagerDefault;
   }

   public final boolean hasResults() {
      return this.result != this || this.throwableResponse != null;
   }

   public final void setResult(Response var1) {
      this.result = var1;
   }

   final void notifyResult(Throwable var1, boolean var2) {
      Request var3;
      boolean var4;
      InvocableMonitor var5;
      synchronized(this) {
         var3 = this.childResult(var1, var2);
         var4 = this.parentResumeNewThread;
         var5 = this.invocableMonitor;
      }

      this.doExecute(var3, var4, var5);
   }

   private void doExecute(Request var1, boolean var2, InvocableMonitor var3) {
      if (var3 != null) {
         this.clearInvocableMonitor();
      }

      if (var1 == this) {
         if (var2) {
            this.getWorkManager().schedule(this);
         } else {
            this.run();
         }
      } else if (var1 != null) {
         var1.resumeExecution(var2);
      }

   }

   Request childResult(Response var1) {
      if (this.throwableResponse == null && (this.result == this || this.result == null && var1 != null || this.result instanceof InteropJMSVoidResponsePreDiablo && !(var1 instanceof InteropJMSVoidResponsePreDiablo))) {
         this.setResult(var1);
      }

      return this.decrementNumChildren();
   }

   private Request childResult(Throwable var1, boolean var2) {
      if (this.throwableResponse == null) {
         this.throwableResponse = var1;
         this.needFillInStackTrace = var2;
         if (this.getNumChildren() > 1 || this.futureResponse != null) {
            this.setNumChildren(1);
         }
      }

      return this.decrementNumChildren();
   }

   void complete(Throwable var1, boolean var2) {
      this.setParentResumeNewThread(true);
      this.notifyResult(var1, var2);
   }

   private Request decrementNumChildren() {
      if (this.getNumChildren() > 0) {
         if (this.decNumChildren() == 0) {
            if (this.numWaiting > 0) {
               this.notifyAll();
            }

            if (JMSDebug.JMSDispatcher.isDebugEnabled()) {
               JMSDebug.JMSDispatcher.debug("Request():: decrementNumChildren/true ZERO numChildren=" + this.getNumChildren() + " state=" + this.dbgState(this.state) + " running=" + this.isRunning() + " this=" + this + " parent=" + this.parent);
            }

            if (this.state == -42) {
               this.setState(Integer.MAX_VALUE);
               this.doCompletionListener(true);
               return this.parent;
            }

            if (this.isRunning()) {
               return null;
            }

            return this;
         }

         if (JMSDebug.JMSDispatcher.isDebugEnabled()) {
            JMSDebug.JMSDispatcher.debug("Request():: decrementNumChildren/true POSITIVE numChildren=" + this.getNumChildren() + " state=" + this.dbgState(this.state) + " running=" + this.isRunning() + " " + this);
         }
      } else {
         if (JMSDebug.JMSDispatcher.isDebugEnabled()) {
            JMSDebug.JMSDispatcher.debug("Request():: decrementNumChildren/true IGNORED numChildren=" + this.getNumChildren() + " state=" + this.dbgState(this.state) + " running=" + this.isRunning() + " " + this);
         }

         this.notifyAll();
      }

      return null;
   }

   boolean hasListener() {
      return this.completionListener != null;
   }

   void doCompletionListener(boolean var1) {
      final Response var2;
      final Throwable var3;
      final CompletionListener var4;
      synchronized(this) {
         if (this.completionListener == null) {
            return;
         }

         var4 = this.completionListener;
         this.completionListener = null;
         var2 = this.result;
         var3 = this.throwableResponse;
         if (var1) {
            WorkManagerFactory.getInstance().getSystem().schedule(new Runnable() {
               public void run() {
                  Request.runCompletionListener(var4, var3, var2);
               }
            });
            return;
         }
      }

      runCompletionListener(var4, var3, var2);
   }

   public void rememberChild(Request var1) {
      if (this.child != null) {
         if (this.child.throwableResponse != null) {
            return;
         }

         if (var1.throwableResponse == null && (this.child.result != null && var1.result == null || this.child.result instanceof InteropJMSVoidResponsePreDiablo && !(var1.result instanceof InteropJMSVoidResponsePreDiablo))) {
            return;
         }
      }

      this.child = var1;
   }

   public Request getChild() {
      return this.child;
   }

   public synchronized void waitForNotRunningResult() {
      if (!this.hasResults() || this.isRunning() || this.getState() != Integer.MAX_VALUE && this.getState() != -42 || this.throwableResponse == null && this.getNumChildren() != 0) {
         if (JMSDebug.JMSDispatcher.isDebugEnabled()) {
            JMSDebug.JMSDispatcher.debug("Request():: waitForNotRunningResult() state=" + this.dbgState(this.state) + ", numChildren=" + this.getNumChildren() + " running=" + this.isRunning() + " " + this);
         }

         this.sleepTillNotified(true);
      }
   }

   private String dbgState(int var1) {
      String var2;
      switch (var1) {
         case -42:
            var2 = "REMOTE:" + var1;
            break;
         case 0:
            var2 = "START:" + var1;
            break;
         case 1:
            var2 = "CONTINUE:" + var1;
            break;
         case 2:
            var2 = "TRY || FINISH:" + var1;
            break;
         case 3:
            var2 = "RETRY:" + var1;
            break;
         case 4:
            var2 = "AFTER_START_IP:" + var1;
            break;
         case 5:
            var2 = "AFTER_POST_AUTH_IP:" + var1;
            break;
         case 6:
            var2 = "RETURN_FROM_START_IP:" + var1;
            break;
         case 7:
            var2 = "RETURN_FROM_POST_AUTH_IP:" + var1;
            break;
         case 8:
            var2 = "RELEASE_FANOUT:" + var1;
            break;
         case 1101:
            var2 = "BEExtension.SEND_WAIT_FOR_COMPLETE:" + var1;
            break;
         case 1102:
            var2 = "SEND_ISSUE_MESSAGE";
            break;
         case 1103:
            var2 = "SEND_COMPLETE:" + var1;
            break;
         case 1104:
            var2 = "SEND_UNKNOWN:" + var1;
            break;
         case Integer.MAX_VALUE:
            var2 = "COMPLETED:" + var1;
            break;
         default:
            var2 = "unk:" + var1;
      }

      return var2;
   }

   public synchronized Response getResult() throws Throwable {
      while(this.result == this && this.throwableResponse == null) {
         this.sleepTillNotified(true);
      }

      if (this.throwableResponse != null) {
         if (this.throwableResponse instanceof DispatcherException) {
            Throwable var1 = this.throwableResponse.getCause();
            if (var1 != null) {
               this.throwableResponse = var1;
            }
         }

         if (this.needFillInStackTrace) {
            this.needFillInStackTrace = false;
            this.throwableResponse = StackTraceUtilsClient.getThrowableWithCause(this.throwableResponse);
         }

         throw this.throwableResponse;
      } else {
         return this.result;
      }
   }

   final void sleepTillNotified(boolean var1) {
      if (var1) {
         if (this.hasResults()) {
            return;
         }
      } else if (this.getNumChildren() < 1) {
         return;
      }

      ++this.numWaiting;

      try {
         if (this.tranInfo == 1 && this.transaction == null) {
            this.forceSuspendTransaction();
         }

         while(true) {
            this.wait();
            if (var1) {
               if (this.hasResults()) {
                  return;
               }
            } else if (this.getNumChildren() < 1) {
               return;
            }
         }
      } catch (InterruptedException var29) {
         RuntimeException var3 = new RuntimeException(var29);
         if (JMSDebug.JMSDispatcher.isDebugEnabled()) {
            JMSDebug.JMSDispatcher.debug(var3.getMessage(), var3);
         }

         throw var3;
      } finally {
         --this.numWaiting;

         try {
            if (this.transaction != null) {
               this.resumeTransaction();
            }
         } finally {
            if (this.numWaiting > 0) {
               this.notify();
            }

         }

      }
   }

   public final synchronized void setState(int var1) {
      this.state = var1;
   }

   public final int getState() {
      return this.state;
   }

   public final void resumeRequest(Throwable var1, boolean var2) {
      this.notifyResult(var1, var2);
   }

   private final void resumeRequest(Response var1) {
      if (JMSDebug.JMSDispatcher.isDebugEnabled()) {
         JMSDebug.JMSDispatcher.debug("Request():: resumeRequest() " + this + ", has completionListener " + this.completionListener);
      }

      Request var2;
      boolean var3;
      synchronized(this) {
         var2 = this.childResult(var1);
         var3 = this.parentResumeNewThread;
      }

      this.doExecute(var2, var3, (InvocableMonitor)null);
   }

   public final void resumeExecution(boolean var1) {
      Request var2;
      synchronized(this) {
         var2 = this.decrementNumChildren();
         var1 |= this.parentResumeNewThread;
      }

      this.doExecute(var2, var1, (InvocableMonitor)null);
   }

   protected abstract Throwable getAppException(String var1, Throwable var2);

   public final void handleResult(AsyncResult var1) {
      Response var2 = null;
      Object var3 = null;

      Throwable var4;
      try {
         var3 = var1.getObject();
         var2 = (Response)var3;
         var4 = null;
      } catch (ClassCastException var6) {
         if (var3 instanceof Throwable) {
            var4 = (Throwable)var3;
         } else {
            var4 = this.getAppException("Unexpected remote response" + var2, var6);
         }
      } catch (Throwable var7) {
         var4 = var7;
      }

      if (!this.getParentResumeNewThread()) {
         this.setParentResumeNewThread(true);
         if (TODOSAGREMOVEreportOnce) {
            TODOSAGREMOVEreportOnce = false;
            (new Exception("messaging.Request TODOSAGREMOVEreportOnce " + this)).printStackTrace();
         }
      }

      if (var4 != null) {
         if (JMSDebug.JMSDispatcher.isDebugEnabled()) {
            JMSDebug.JMSDispatcher.debug("Request.handleResult() : " + this + " " + var2);
         }

         this.notifyResult(var4, true);
      } else {
         this.resumeRequest(var2);
      }

   }

   public final synchronized void needOutsideResult() {
      this.incNumChildren();
      if (JMSDebug.JMSDispatcher.isDebugEnabled()) {
         JMSDebug.JMSDispatcher.debug("needOutside numChildren = " + this.getNumChildren() + " on " + this);
      }

   }

   public final synchronized boolean fanoutComplete(boolean var1) {
      if (this.decNumChildren() == 0) {
         if (!this.hasResults() && var1) {
            this.setResult((Response)this.appVoidResponse);
         }

         return false;
      } else {
         return true;
      }
   }

   public final synchronized boolean fanoutCompleteSuspendIfHaveChildren(boolean var1) {
      if (this.decNumChildren() == 0) {
         if (!this.hasResults() && var1) {
            this.setResult((Response)this.appVoidResponse);
         }

         return false;
      } else {
         if (this.transaction == null) {
            this.forceSuspendTransaction();
         }

         return true;
      }
   }

   final void setFutureResponse(FutureResponse var1) {
      this.futureResponse = var1;
   }

   public final void setNext(Request var1) {
      this.next = var1;
   }

   public final Request getNext() {
      return this.next;
   }

   final Request getParent() {
      return this.parent;
   }

   public boolean isCollocated() {
      return this.isCollocated;
   }

   boolean isSyncRequest() {
      return this.isSyncRequest;
   }

   void setSyncRequest(boolean var1) {
      this.isSyncRequest = var1;
   }

   void setRunning(boolean var1) {
      this.running = var1;
   }

   private boolean isRunning() {
      return this.running;
   }

   public void setListener(CompletionListener var1) {
      this.completionListener = var1;
   }

   final void setAsyncResult(AsyncResult var1) {
      this.asyncResult = var1;
   }

   public static String timeString() {
      return sdf.format(new Date(System.currentTimeMillis()));
   }

   public String TODORemoveName() {
      return this.TODORemove() + " " + this.getClass().getName() + ", numChildren= " + this.getNumChildren() + ", " + ", hasResults()=" + this.hasResults() + ", " + this;
   }

   public String TODORemove() {
      return "@_" + timeString() + " TODO SAG REMOVE Request Thread:" + Thread.currentThread().getName();
   }

   final Response wrappedFiniteStateMachine() throws Exception {
      Throwable var1 = null;
      Object var2 = this;
      Request var7 = null;
      boolean var8 = false;
      CompletionListener var9 = null;
      InvocableMonitor var10 = null;
      int var5;
      Request var11;
      synchronized(this) {
         if (this.isRunning()) {
            return this;
         }

         if (this.state != -42) {
            if (this.state == Integer.MAX_VALUE || this.throwableResponse != null && (this.futureResponse != null || this.asyncResult != null)) {
               var5 = Integer.MAX_VALUE;
               var10 = this.invocableMonitor;
            }
         } else {
            if (this.throwableResponse == null && (this.getNumChildren() != 0 || !this.hasResults())) {
               return this;
            }

            var5 = Integer.MAX_VALUE;
            this.setState(Integer.MAX_VALUE);
         }

         if (this.transaction != null) {
            this.resumeTransaction();
         }

         this.setRunning(true);
         var11 = this.parent;
         boolean var34 = true;
      }

      Request var6 = this;

      while(var6.isCollocated && var6.parent != null) {
         if (this == var6) {
            var6 = var11;
         } else {
            var6 = var6.parent;
         }
      }

      while(true) {
         var7 = null;

         try {
            if (this.invocable == null) {
               this.invocable = this.appInvocableManager.invocableFind(this.methodId & this.appInvocableManager.INVOCABLE_TYPE_MASK, this.invocableId);
               if (var10 == null) {
                  var10 = this.invocable.getInvocableMonitor();
                  if (var10 != null) {
                     var10.increment();
                     synchronized(this) {
                        this.invocableMonitor = var10;
                     }
                  }
               }
            }

            var5 = this.invocable.invoke(this);
         } catch (Throwable var30) {
            var1 = var30;
            var5 = Integer.MAX_VALUE;
         }

         AsyncResult var3;
         FutureResponse var4;
         synchronized(this) {
            if (this.state == -42) {
               if (this.throwableResponse == null && (this.getNumChildren() != 0 || !this.hasResults())) {
                  this.setRunning(false);
                  if (this.transaction == null && (this.tranInfo != 0 || this.hasTransaction())) {
                     this.forceSuspendTransaction();
                  }

                  return this;
               }

               var5 = Integer.MAX_VALUE;
               this.setState(Integer.MAX_VALUE);
               this.setRunning(false);
            }

            if (var5 != Integer.MAX_VALUE && this.state != Integer.MAX_VALUE && this.throwableResponse == null) {
               if (this.getNumChildren() == 0) {
                  if (JMSDebug.JMSDispatcher.isDebugEnabled()) {
                     JMSDebug.JMSDispatcher.debug(" *** run the fsm of " + this + " again ***");
                  }

                  if (this.transaction != null) {
                     this.resumeTransaction();
                  }
                  continue;
               }

               if (var6.isCollocated && var6.isSyncRequest) {
                  this.sleepTillNotified(false);
                  if (this.state != Integer.MAX_VALUE && this.throwableResponse == null) {
                     continue;
                  }
               }
            }

            if (var5 != Integer.MAX_VALUE && this.state != Integer.MAX_VALUE && this.throwableResponse == null) {
               if (this.transaction == null && (this.tranInfo != 0 || this.hasTransaction())) {
                  this.forceSuspendTransaction();
               }

               this.setRunning(false);
               var3 = null;
               var4 = null;
            } else {
               var5 = Integer.MAX_VALUE;
               this.setState(Integer.MAX_VALUE);
               this.setRunning(false);
               var3 = this.asyncResult;
               this.asyncResult = null;
               var4 = this.futureResponse;
               this.futureResponse = null;
               var9 = this.completionListener;
               this.completionListener = null;
               if (var1 != null) {
                  if (this.throwableResponse != null) {
                     var1 = this.throwableResponse;
                  } else {
                     this.throwableResponse = var1;
                     if (this.getNumChildren() > 0) {
                        this.setNumChildren(0);
                     }
                  }
               } else if (this.throwableResponse != null) {
                  var1 = this.throwableResponse;
               } else {
                  var2 = this.result;
               }

               if (var1 != null) {
                  if (var1 instanceof DispatcherException) {
                     Throwable var13 = var1.getCause();
                     if (var13 != null) {
                        var1 = var13;
                        this.throwableResponse = var13;
                     }
                  }

                  if (this.needFillInStackTrace) {
                     this.needFillInStackTrace = false;
                     var1 = StackTraceUtilsClient.getThrowableWithCause(var1);
                  }
               }

               if (JMSDebug.JMSDispatcher.isDebugEnabled()) {
                  JMSDebug.JMSDispatcher.debug("     -- COMPLETED " + this + "--");
               }

               if (this.parent != null) {
                  var7 = this.parent;
                  var8 = this.parentResumeNewThread;
               }

               if (this.numWaiting > 0) {
                  this.notifyAll();
               }
            }
         }

         if (var5 != Integer.MAX_VALUE) {
            return (Response)var2;
         }

         if (var7 != null) {
            var7.resumeExecution(var8 || var7.parentResumeNewThread);
         }

         if (var5 != Integer.MAX_VALUE) {
            return (Response)var2;
         }

         Response var12;
         try {
            runCompletionListener(var9, var1, (Response)var2);
            if (var3 != null || var4 != null) {
               var12 = doRMIResponse(this, var1, (Response)var2, var3, var4);
               return var12;
            }

            if (var1 == null) {
               Object var35 = var2;
               return (Response)var35;
            }

            try {
               var12 = this.getResult();
            } catch (RuntimeException var26) {
               throw var26;
            } catch (Error var27) {
               throw var27;
            } catch (Throwable var28) {
               throw (Exception)var28;
            }
         } finally {
            this.clearInvocableMonitor();
         }

         return var12;
      }
   }

   void clearInvocableMonitor() {
      InvocableMonitor var1;
      synchronized(this) {
         if (this.invocableMonitor == null) {
            return;
         }

         var1 = this.invocableMonitor;
         this.invocableMonitor = null;
      }

      var1.decrement();
   }

   private static Response doRMIResponse(Request var0, Throwable var1, Response var2, AsyncResult var3, FutureResponse var4) {
      try {
         if (var1 != null && var4 != null) {
            var4.sendThrowable(new DispatcherException((Throwable)var1));
         } else if (var3 != null) {
            try {
               if (var1 != null) {
                  var3.setResult(new DispatcherException((Throwable)var1));
               } else {
                  var3.setResult(var2);
               }

               var4.send();
            } catch (Exception var9) {
               Exception var5 = var9;
               if (var1 == null) {
                  synchronized(var0) {
                     var1 = var5;
                     var0.throwableResponse = var5;
                  }
               }

               var4.sendThrowable(new DispatcherException((Throwable)var1));
            }
         } else if (var4 != null) {
            if (var1 != null) {
               var4.sendThrowable(new DispatcherException((Throwable)var1));
            } else {
               var4.getMsgOutput().writeObject(var2, var2.getClass());
               var4.send();
            }
         }
      } catch (RemoteException var10) {
         if (JMSDebug.JMSDispatcher.isDebugEnabled()) {
            var10.printStackTrace();
         }
      } catch (IOException var11) {
         if (JMSDebug.JMSDispatcher.isDebugEnabled()) {
            var11.printStackTrace();
         }
      }

      return var2;
   }

   private static void runCompletionListener(CompletionListener var0, Throwable var1, Response var2) {
      if (var0 != null) {
         if (var1 != null) {
            var0.onException(var1);
         } else {
            var0.onCompletion(var2);
         }
      }

   }

   public abstract int remoteSignature();

   public abstract Response createResponse();

   final synchronized void setParentResumeNewThread(boolean var1) {
      this.parentResumeNewThread = var1;
   }

   public final boolean getParentResumeNewThread() {
      return this.parentResumeNewThread;
   }

   public void dispatchAsync(Dispatcher var1, Request var2) throws DispatcherException {
      synchronized(this) {
         var2.parent = this;
         this.incNumChildren();
      }

      var1.dispatchAsync(var2);
   }

   public Request() {
      this.result = this;
      this.isCollocated = true;
   }

   void writeShortened(ObjectOutput var1) throws IOException {
      this.shortened = true;
      this.writeExternal(var1);
      this.shortened = false;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      int var2 = 1;
      if (!this.shortened && this.invocableId != null) {
         var2 |= 256;
      }

      var1.writeInt(var2);
      if (!this.shortened && this.invocableId != null) {
         this.invocableId.writeExternal(var1);
      }

      var1.writeInt(this.methodId);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.readExternal(var1, new IDImpl(), this.appInvocableManager);
   }

   protected final void readExternal(ObjectInput var1, ID var2, InvocableManager var3) throws IOException, ClassNotFoundException {
      int var4 = var1.readInt();
      int var5 = var4 & 255;
      this.appInvocableManager = var3;
      if (var5 != 1) {
         throw MessagingUtilities.versionIOException(var5, 1, 1);
      } else {
         if ((var4 & 256) != 0) {
            this.invocableId = var2;
            this.invocableId.readExternal(var1);
         }

         if ((var4 & 512) != 0) {
            var1.readLong();
         }

         this.methodId = var1.readInt();
         this.isCollocated = false;
      }
   }

   private boolean hasTransaction() {
      if (this.tranInfo == 2) {
         try {
            if ((this.remoteSignature() & 1) != 0 && getTranManager().getTransaction() != null) {
               this.tranInfo = 1;
            } else {
               this.tranInfo = 0;
            }
         } catch (SystemException var2) {
            this.tranInfo = 0;
         }
      }

      return this.tranInfo == 1;
   }

   public final void setTranInfo(int var1) {
      this.tranInfo = var1;
   }

   private synchronized boolean forceSuspendTransaction() {
      if (this.transaction != null) {
         throw new Error("transaction suspended twice");
      } else if ((this.transaction = getTranManager().forceSuspend()) == null) {
         this.tranInfo = 0;
         return false;
      } else {
         this.tranInfo = 1;
         return true;
      }
   }

   private static ClientTransactionManager getTranManager() {
      return TransactionHelper.getTransactionHelper().getTransactionManager();
   }

   private synchronized void resumeTransaction() {
      if (this.transaction == null) {
         throw new Error("transaction resumed twice");
      } else {
         getTranManager().forceResume(this.transaction);
         this.transaction = null;
      }
   }

   static {
      sdf.setTimeZone(TimeZone.getDefault());
   }
}
