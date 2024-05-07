package weblogic.messaging.dispatcher;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import javax.transaction.Transaction;
import weblogic.common.internal.PeerInfo;
import weblogic.messaging.ID;
import weblogic.rmi.extensions.AsyncResult;
import weblogic.rmi.extensions.AsyncResultFactory;
import weblogic.rmi.extensions.DisconnectEvent;
import weblogic.rmi.extensions.DisconnectListener;
import weblogic.rmi.extensions.DisconnectMonitor;
import weblogic.rmi.extensions.DisconnectMonitorListImpl;
import weblogic.rmi.extensions.DisconnectMonitorUnavailableException;
import weblogic.rmi.extensions.RemoteRuntimeException;
import weblogic.rmi.extensions.server.RemoteWrapper;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManager;
import weblogic.timers.TimerManagerFactory;
import weblogic.transaction.ClientTransactionManager;
import weblogic.transaction.TransactionHelper;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerFactory;

public class DispatcherWrapperState implements Dispatcher, DisconnectListener, RemoteWrapper, TimerListener, Runnable {
   static final long serialVersionUID = -360573074488373048L;
   private final String name;
   private final DispatcherId dispatcherId;
   private PeerInfo peerInfo;
   private DispatcherRemote dispatcherRemote;
   private DispatcherOneWay dispatcherOneWay;
   private boolean peerGone;
   private boolean peerGoneIsOn;
   private int refCount = 1;
   private Exception exceptionForPeerGone;
   private boolean fireListeners;
   private final transient HashMap listenersMap = new HashMap();
   private final transient HashMap stateChangeListeners = new HashMap();
   private int stateChangeListenersInExecution;
   private static final transient int PEERGONE_DELAY = 100;
   private static transient WorkManager workManager;
   private static transient TimerManager timerManager;
   private static VoidResponse voidResponse = new VoidResponse();
   private static final ClientTransactionManager tranManager = TransactionHelper.getTransactionHelper().getTransactionManager();

   protected DispatcherWrapperState(DispatcherWrapper var1) throws DispatcherException {
      this.dispatcherId = var1.getId();
      this.name = var1.getName();
      this.dispatcherRemote = var1.getRemoteDispatcher();
      this.dispatcherOneWay = var1.getOneWayDispatcher();
      this.peerInfo = var1.getPeerInfo();
      this.addPeerGoneListener();
   }

   public final synchronized boolean getPeerGoneCache() {
      return this.peerGone;
   }

   synchronized boolean removeRefCount() {
      --this.refCount;
      return this.refCount > 0;
   }

   synchronized void addRefCount() {
      ++this.refCount;
   }

   public final synchronized void deleteNotify() {
      this.removePeerGoneListener();
   }

   public final String getName() {
      return this.name;
   }

   public final DispatcherId getId() {
      return this.dispatcherId;
   }

   public final boolean isLocal() {
      return false;
   }

   public PeerInfo getPeerInfo() {
      return this.peerInfo;
   }

   public final Remote getRemoteDelegate() {
      return this.dispatcherRemote;
   }

   public void dispatchAsync(Request var1) throws DispatcherException {
      this.preAsync(var1);

      DispatcherException var3;
      try {
         if (0 != (var1.remoteSignature() & 1)) {
            AsyncResult var2 = AsyncResultFactory.getCallbackableResult(var1);
            this.dispatcherRemote.dispatchAsyncTranFuture(var1, var2);
         } else {
            Transaction var18 = null;

            try {
               var18 = tranManager.forceSuspend();
               AsyncResult var19 = AsyncResultFactory.getCallbackableResult(var1);
               this.dispatcherRemote.dispatchAsyncFuture(var1, var19);
            } finally {
               if (var18 != null) {
                  tranManager.forceResume(var18);
               }

            }
         }

      } catch (DispatcherException var13) {
         var1.complete(var13, false);
         throw var13;
      } catch (RemoteRuntimeException var14) {
         var3 = new DispatcherException(var14.getNestedException());
         var1.complete(var3, false);
         throw var3;
      } catch (RemoteException var15) {
         var3 = new DispatcherException(var15);
         var1.complete(var3, false);
         throw var3;
      } catch (Error var16) {
         var1.complete(var16, false);
         throw var16;
      } catch (RuntimeException var17) {
         var1.complete(var17, false);
         throw var17;
      }
   }

   public void dispatchNoReply(Request var1) throws DispatcherException {
      if ((var1.remoteSignature() & 1) == 0) {
         this.preRemote(var1);

         DispatcherException var3;
         try {
            this.dispatcherOneWay.dispatchOneWay(var1);
            this.postRMI(var1, voidResponse);
         } catch (RemoteRuntimeException var4) {
            var3 = new DispatcherException(var4.getNestedException());
            var1.complete(var3, false);
            throw var3;
         } catch (RemoteException var5) {
            var3 = new DispatcherException(var5);
            var1.complete(var3, false);
            throw var3;
         } catch (Error var6) {
            var1.complete(var6, false);
            throw var6;
         } catch (RuntimeException var7) {
            var1.complete(var7, false);
            throw var7;
         }
      } else {
         DispatcherException var2 = new DispatcherException("Transactions not supported for one-way calls");
         var1.complete(var2, false);
         throw var2;
      }
   }

   public void dispatchNoReplyWithId(Request var1, int var2) throws DispatcherException {
      this.preRemote(var1);
      if ((var1.remoteSignature() & 1) == 0) {
         DispatcherException var4;
         try {
            this.dispatcherOneWay.dispatchOneWayWithId(var1, var2);
            this.postRMI(var1, voidResponse);
         } catch (RemoteRuntimeException var5) {
            var4 = new DispatcherException(var5.getNestedException());
            var1.complete(var4, false);
            throw var4;
         } catch (RemoteException var6) {
            var4 = new DispatcherException(var6);
            var1.complete(var4, false);
            throw var4;
         } catch (Error var7) {
            var1.complete(var7, false);
            throw var7;
         } catch (RuntimeException var8) {
            var1.complete(var8, false);
            throw var8;
         }
      } else {
         DispatcherException var3 = new DispatcherException("Transactions not supported for one-way calls");
         var1.complete(var3, false);
         throw var3;
      }
   }

   public Response dispatchSync(Request var1) throws DispatcherException {
      return this.dispatchSyncNoTran(var1);
   }

   private String preSync(Request var1) {
      var1.setSyncRequest(true);
      return this.preRemote(var1);
   }

   private String preAsync(Request var1) {
      return this.preRemote(var1);
   }

   private String preRemote(Request var1) {
      var1.setPeerInfo(this.peerInfo);
      var1.setState(-42);
      var1.setParentResumeNewThread(true);
      var1.incNumChildren();
      return null;
   }

   private Response postRMI(Request var1, Response var2) {
      InvocableMonitor var3;
      synchronized(var1) {
         var1.childResult(var2);
         var3 = var1.getInvocableMonitor();
      }

      if (var3 != null) {
         var1.clearInvocableMonitor();
      }

      return var2;
   }

   public Response dispatchSyncTranWithId(Request var1, int var2) throws DispatcherException {
      this.preSync(var1);

      DispatcherException var4;
      try {
         return this.postRMI(var1, this.dispatcherRemote.dispatchSyncTranFutureWithId(var1, var2));
      } catch (DispatcherException var5) {
         var1.complete(var5, false);
         throw var5;
      } catch (RemoteRuntimeException var6) {
         var4 = new DispatcherException(var6.getNestedException());
         var1.complete(var4, false);
         throw var4;
      } catch (RemoteException var7) {
         var4 = new DispatcherException(var7);
         var1.complete(var4, false);
         throw var4;
      } catch (Error var8) {
         var1.complete(var8, false);
         throw var8;
      } catch (RuntimeException var9) {
         var1.complete(var9, false);
         throw var9;
      }
   }

   public Response dispatchSyncTran(Request var1) throws DispatcherException {
      this.preSync(var1);

      DispatcherException var3;
      try {
         return this.postRMI(var1, this.dispatcherRemote.dispatchSyncTranFuture(var1));
      } catch (DispatcherException var4) {
         var1.complete(var4, false);
         throw var4;
      } catch (RemoteRuntimeException var5) {
         var3 = new DispatcherException(var5.getNestedException());
         var1.complete(var3, false);
         throw var3;
      } catch (RemoteException var6) {
         var3 = new DispatcherException(var6);
         var1.complete(var3, false);
         throw var3;
      } catch (Error var7) {
         var1.complete(var7, false);
         throw var7;
      } catch (RuntimeException var8) {
         var1.complete(var8, false);
         throw var8;
      }
   }

   public Response dispatchSyncNoTranWithId(Request var1, int var2) throws DispatcherException {
      Transaction var3 = tranManager.forceSuspend();

      Response var4;
      try {
         var4 = this.dispatchSyncTranWithId(var1, var2);
      } finally {
         if (var3 != null) {
            tranManager.forceResume(var3);
         }

      }

      return var4;
   }

   public Response dispatchSyncNoTran(Request var1) throws DispatcherException {
      Transaction var2 = tranManager.forceSuspend();

      Response var3;
      try {
         var3 = this.dispatchSyncTran(var1);
      } finally {
         if (var2 != null) {
            tranManager.forceResume(var2);
         }

      }

      return var3;
   }

   public void onDisconnect(DisconnectEvent var1) {
      this.schedulePeerGone(new Exception(var1.getThrowable()));
   }

   public synchronized void addPeerGoneListener() throws DispatcherException {
      if (this.dispatcherRemote == null) {
         if (!this.peerGoneIsOn) {
            (new DispatcherException("TODO REMOVE SAG dispatcherRemote is null")).printStackTrace();
            throw new DispatcherException("dispatcherRemote is null");
         }
      } else {
         DisconnectMonitor var1 = DisconnectMonitorListImpl.getDisconnectMonitor();

         try {
            Object var2 = this.dispatcherRemote;
            if (var2 instanceof RemoteWrapper) {
               var2 = ((RemoteWrapper)var2).getRemoteDelegate();
            }

            var1.addDisconnectListener((Remote)var2, this);
         } catch (DisconnectMonitorUnavailableException var4) {
            DispatcherException var3 = new DispatcherException(var4.getMessage() + " for " + this.dispatcherId);
            var3.initCause(var4);
            throw var3;
         }

         this.peerGoneIsOn = true;
      }
   }

   public synchronized void removePeerGoneListener() {
      if (this.peerGoneIsOn) {
         this.peerGoneIsOn = false;
         if (this.dispatcherRemote != null) {
            DisconnectMonitor var1 = DisconnectMonitorListImpl.getDisconnectMonitor();

            try {
               Object var2 = this.dispatcherRemote;
               if (var2 instanceof RemoteWrapper) {
                  var2 = ((RemoteWrapper)var2).getRemoteDelegate();
               }

               var1.removeDisconnectListener((Remote)var2, this);
            } catch (Exception var3) {
               var3.printStackTrace();
            }

         }
      }
   }

   public DispatcherPeerGoneListener addDispatcherPeerGoneListener(DispatcherPeerGoneListener var1) {
      DispatcherPeerGoneListener var2;
      synchronized(this) {
         var2 = (DispatcherPeerGoneListener)this.listenersMap.get(var1.getId());
         if (this.exceptionForPeerGone == null) {
            if (var2 == null) {
               var2 = var1;
               this.listenersMap.put(var1.getId(), var1);
               if (var1 instanceof DispatcherStateChangeListener) {
                  this.stateChangeListeners.put(var1.getId(), var1);
               }
            }

            var2.incrementRefCount();
            return var2;
         }
      }

      try {
         var1.dispatcherPeerGone(this.exceptionForPeerGone, this);
      } catch (Throwable var7) {
      }

      synchronized(this) {
         return var2 == null ? var1 : var2;
      }
   }

   public synchronized void removeDispatcherPeerGoneListener(DispatcherPeerGoneListener var1) {
      ID var2 = var1.getId();
      DispatcherPeerGoneListener var3 = (DispatcherPeerGoneListener)this.listenersMap.get(var2);
      if (var3 != null) {
         if (var3.decrementRefCount() == 0) {
            this.listenersMap.remove(var2);
            if (var3 instanceof DispatcherStateChangeListener) {
               this.stateChangeListeners.remove(var2);
            }
         }

      }
   }

   private void schedulePeerGone(Exception var1) {
      boolean var2 = false;
      boolean var3 = false;
      RuntimeException var4 = null;
      Error var5 = null;

      boolean var6;
      while(true) {
         DispatcherStateChangeListener var7;
         synchronized(this) {
            this.peerGone = true;
            if (this.exceptionForPeerGone == null) {
               this.exceptionForPeerGone = var1;
               var2 = true;
            }

            if (var3) {
               --this.stateChangeListenersInExecution;
            }

            var6 = this.stateChangeListeners.isEmpty();
            if (var6) {
               if (this.stateChangeListenersInExecution == 0) {
                  this.notifyAll();
               }
               break;
            }

            var7 = this.removeLockedStateChangeListener();
            if (var7 == null) {
               if (this.stateChangeListenersInExecution == 0) {
                  this.notifyAll();
               }
               break;
            }

            if (!var3) {
               var3 = true;
               ++this.stateChangeListenersInExecution;
            }
         }

         try {
            var7.stateChangeListener(var7, var1);
         } catch (Error var10) {
            if (var5 == null && var4 == null) {
               var5 = var10;
            }
         } catch (RuntimeException var11) {
            if (var5 == null && var4 == null) {
               var4 = var11;
            }
         }
      }

      if (var2) {
         if (var6) {
            getDefaultWorkManager().schedule(this);
         } else {
            getDefaultTimerManager().schedule(this, 100L);
         }
      }

      if (var4 != null) {
         throw var4;
      } else if (var5 != null) {
         throw var5;
      }
   }

   private static synchronized WorkManager getDefaultWorkManager() {
      if (workManager == null) {
         workManager = WorkManagerFactory.getInstance().getDefault();
      }

      return workManager;
   }

   private static synchronized TimerManager getDefaultTimerManager() {
      if (timerManager == null) {
         timerManager = TimerManagerFactory.getTimerManagerFactory().getDefaultTimerManager();
      }

      return timerManager;
   }

   private DispatcherStateChangeListener removeLockedStateChangeListener() {
      Iterator var1 = this.stateChangeListeners.values().iterator();

      DispatcherStateChangeListener var2;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         var2 = (DispatcherStateChangeListener)var1.next();
      } while(!var2.holdsLock());

      var1.remove();
      return var2;
   }

   public void timerExpired(Timer var1) {
      this.run();
   }

   public void run() {
      Error var3 = null;
      RuntimeException var4 = null;
      Class var5 = DispatcherManager.class;
      synchronized(DispatcherManager.class) {
         DispatcherManager.removeDispatcherReference(this, true);
      }

      while(true) {
         while(true) {
            Exception var1;
            boolean var6;
            DispatcherPeerGoneListener var17;
            synchronized(this) {
               while(this.stateChangeListenersInExecution > 0) {
                  try {
                     this.wait();
                  } catch (InterruptedException var10) {
                     throw new RuntimeException(var10);
                  }
               }

               var6 = !this.stateChangeListeners.isEmpty();
               Iterator var2;
               if (var6) {
                  var2 = this.stateChangeListeners.values().iterator();
               } else {
                  if (!this.fireListeners) {
                     this.fireListeners = true;
                     this.notifyAll();
                  }

                  if (this.listenersMap.isEmpty()) {
                     if (var4 != null) {
                        throw var4;
                     }

                     if (var3 != null) {
                        throw var3;
                     }

                     return;
                  }

                  var2 = this.listenersMap.values().iterator();
               }

               var1 = this.exceptionForPeerGone;
               var17 = (DispatcherPeerGoneListener)var2.next();
               var2.remove();
            }

            if (var6) {
               try {
                  ((DispatcherStateChangeListener)var17).stateChangeListener((DispatcherStateChangeListener)var17, var1);
               } catch (Error var15) {
                  if (var3 == null && var4 == null) {
                     var3 = var15;
                  }
               } catch (RuntimeException var16) {
                  if (var3 == null && var4 == null) {
                     var4 = var16;
                  }
               }
            } else {
               try {
                  var17.dispatcherPeerGone(var1, this);
               } catch (Error var13) {
                  if (var3 == null && var4 == null) {
                     var3 = var13;
                  }
               } catch (RuntimeException var14) {
                  if (var3 == null && var4 == null) {
                     var4 = var14;
                  }
               }
            }
         }
      }
   }

   public String toString() {
      return "DispWrap #" + this.hashCode() + " " + this.dispatcherId;
   }
}
