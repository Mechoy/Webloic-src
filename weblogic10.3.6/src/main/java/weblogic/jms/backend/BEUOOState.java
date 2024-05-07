package weblogic.jms.backend;

import java.security.AccessController;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import javax.jms.JMSException;
import javax.naming.NamingException;
import weblogic.common.CompletionListener;
import weblogic.common.CompletionRequest;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.dd.DDHandler;
import weblogic.jms.extensions.JMSOrderException;
import weblogic.jms.server.SequenceData;
import weblogic.management.provider.ManagementService;
import weblogic.messaging.kernel.Destination;
import weblogic.messaging.kernel.KernelException;
import weblogic.messaging.kernel.Sequence;
import weblogic.messaging.path.helper.KeyString;
import weblogic.messaging.path.helper.PathHelper;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.utils.collections.CircularQueue;
import weblogic.work.ContextWrap;
import weblogic.work.InheritableThreadContext;

abstract class BEUOOState implements BEExtension {
   private static long PATH_SERVICE_RESUME_RETRY_DELAY = 400L;
   private static boolean verbose = false;
   private static int QOS_STORE_OWNED_CACHE_ON_EQUAL = 49216;
   private static long PATH_SERVICE_DELETE_RETRY_DELAY = 120000L;
   public static HashMap TODOremoveDebug = new HashMap();
   private static boolean delayedRemoveRunning;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final Object retryListLock = BEUOOState.class;
   private static HashMap delayedRemoves;
   private static CircularQueue delayedRemove = new CircularQueue(4);
   private DDHandler ddHandler;
   private BEDestinationImpl destination;
   private HashMap uooStates = new HashMap();
   private HashMap uowStates = new HashMap();

   protected BEUOOState(BEDestinationImpl var1, DDHandler var2) {
      this.ddHandler = var2;
      this.destination = var1;

      assert !verbose || null == TODOremoveDebug.put(var1.getName(), this);

   }

   public final void sendExtension(BEProducerSendRequest var1) throws JMSException {
      int var2 = getUOOSequenceOpcode(var1);
      Sequence var3 = var1.getSequence();
      String var4 = var1.getMessage().getUnitOfOrder();
      byte var5 = 1;
      if (var4 == null && this.destination.isUOWDestination()) {
         var4 = var1.getMessage().getStringProperty("JMS_BEA_UnitOfWork");
         var5 = 5;
      }

      if (var2 == 0) {
         if (var4 == null) {
            return;
         }

         if (var1.getCheckUOO() == 0) {
            return;
         }
      } else {
         if (var3 == null) {
            throw new weblogic.jms.common.JMSException("no Sequence for control message " + Integer.toHexString(var2) + " for destination " + this.destination.getName());
         }

         if (!"Hash".equals(this.ddHandler.getUnitOfOrderRouting())) {
            var1.setState(Integer.MAX_VALUE);
            return;
         }

         if (var2 != 196608) {
            assert 131072 >= var2;

            this.controlSequenceRelease(var1);
            return;
         }

         if (var4 == null) {
            throw new weblogic.jms.common.JMSException("no Unit of Order for Reserve " + this.destination.getName());
         }
      }

      var1.setCheckUOO(0);

      assert "PathService".equals(this.ddHandler.getUnitOfOrderRouting());

      var1.setUOOInfo(getNewKeyString(this.ddHandler, var4, var5), new BEUOOMember(this.destination.getName(), ManagementService.getRuntimeAccess(kernelId).getServerName(), true), new PathServiceCompReqListener(var1));
      State var6;
      synchronized(this) {
         var6 = this.findOrCreateState(var4, true, var5);
      }

      var6.setupPutIfAbsent(var1);
   }

   private void controlSequenceRelease(BEProducerSendRequest var1) throws JMSException {
      CircularQueue var2 = new CircularQueue(4);
      weblogic.jms.common.JMSException var3 = null;

      try {
         synchronized(this) {
            Iterator var5 = this.uooStates.values().iterator();

            while(true) {
               if (!var5.hasNext()) {
                  break;
               }

               State var6 = (State)var5.next();
               synchronized(var6) {
                  PathServiceRemoveRetry var8 = this.removeLienInternal(var1.getSequence(), var6.uoo);
                  if (var8 != null) {
                     var2.add(var8);
                  }
               }
            }
         }
      } finally {
         try {
            var1.getSequence().delete(false);
         } catch (KernelException var37) {
            var3 = new weblogic.jms.common.JMSException(var37);
         } finally {
            while(!var2.isEmpty()) {
               ((PathServiceRemoveRetry)var2.remove()).processRemove();
            }

         }

      }

      if (var3 != null) {
         throw var3;
      } else {
         var1.setState(Integer.MAX_VALUE);
      }
   }

   private static int getUOOSequenceOpcode(BEProducerSendRequest var0) {
      int var1 = var0.getMessage().getControlOpcode();
      if (var1 != 0) {
         assert var1 >= 65536;

         if (var1 > 196608) {
            var1 = 0;
         }
      }

      return var1;
   }

   public final void sequenceExtension(BEProducerSendRequest var1) throws JMSException {
      Sequence var2 = var1.getSequence();
      if (var2 != null) {
         String var3 = var1.getMessage().getUnitOfOrder();
         if (verbose) {
            System.out.println("BEUOOState " + var3 + " Sequence " + var2.getName());
         }

         SequenceData var4 = (SequenceData)var2.getUserData();
         String var5;
         SequenceData var6;
         if (var4 == null) {
            if (var3 == null) {
               return;
            }

            var5 = null;
            var6 = new SequenceData();
         } else {
            var5 = var4.getUnitOfOrder();
            var6 = var4.copy();
         }

         boolean var7;
         if (var5 == var3) {
            if (var3 == null) {
               return;
            }

            var7 = true;
         } else {
            var7 = var3 != null && var3.equals(var5);
         }

         if (!var7) {
            PathServiceRemoveRetry var8 = null;

            try {
               synchronized(this) {
                  if (var5 != null) {
                     var8 = this.removeLienInternal(var2, var5);
                  }

                  var6.setUnitOfOrder(var3);

                  try {
                     var2.setUserData(var6);
                  } catch (KernelException var17) {
                     throw new weblogic.jms.common.JMSException(var17);
                  }

                  if (var3 == null) {
                     return;
                  }

                  State var10 = var1.getUooState();
                  if (var10 != null) {
                     var10.addLienInternal(var2);
                     return;
                  }
               }
            } finally {
               if (var8 != null) {
                  var8.processRemove();
               }

            }

         }
      }
   }

   public final void unitOfWorkAddEvent(String var1) {
      this.addEvent(var1, (byte)5);
   }

   public final void groupAddEvent(String var1) {
      this.addEvent(var1, (byte)1);
   }

   private final void addEvent(String var1, byte var2) {
      synchronized(this) {
         State var4 = this.findOrCreateState(var1, false, var2);
         synchronized(var4) {
            var4.sendSuccessBeforeGroupAdd = false;
            var4.hadRemoveGroup = false;
            var4.hadCreateGroup = true;
            if (var4.hadRemoveGroup && (verbose || PathHelper.PathSvc.isDebugEnabled())) {
               PathHelper.PathSvc.debug("DEBUG onGroupAdd " + var4);
            }

            var4.cancelPendingRemove();

            assert !var4.isRemovable();
         }

      }
   }

   public final void unitOfWorkRemoveEvent(String var1) {
      this.removeEvent(var1, (byte)5);
   }

   public final void groupRemoveEvent(String var1) {
      this.removeEvent(var1, (byte)1);
   }

   private final void removeEvent(String var1, byte var2) {
      PathServiceRemoveRetry var3;
      synchronized(this) {
         State var5 = this.findState(var1, var2);
         if (var5 == null) {
            if (verbose || PathHelper.PathSvc.isDebugEnabled()) {
               PathHelper.PathSvc.debug("DEBUG BEUOOState missing for " + var1);
            }

            return;
         }

         synchronized(var5) {
            assert !var5.isInvalid;

            if (!var5.hadCreateGroup && (verbose || PathHelper.PathSvc.isDebugEnabled())) {
               PathHelper.PathSvc.debug("DEBUG new state " + var1 + " without CreateGroup " + var5);
            }

            assert var5.hadCreateGroup;

            var5.hadRemoveGroup = var5.hadCreateGroup = true;
            var3 = var5.setupRemoveRetry((PathServiceRemoveRetry)null);
         }
      }

      if (var3 != null) {
         var3.processRemove();
      }
   }

   private void retryPathServiceLater(PathServiceRemoveRetry var1) {
      synchronized(retryListLock) {
         if (delayedRemoves == null) {
            delayedRemoves = new HashMap();
         }

         PathServiceRemoveRetry var3 = (PathServiceRemoveRetry)delayedRemoves.put(var1.getKey(), var1);
         if (var3 != null) {
            delayedRemoves.put(var1.getKey(), var3);
            if (var3.member == null && var1.member != null) {
               var3.member = var1.member;
            }

            return;
         }

         if (delayedRemove.contains(var1)) {
            return;
         }

         delayedRemove.add(var1);
         if (delayedRemove.size() != 1) {
            return;
         }
      }

      this.destination.getBackEnd().getTimerManager().schedule(var1, PATH_SERVICE_DELETE_RETRY_DELAY);
   }

   private static KeyString getNewKeyString(DDHandler var0, String var1, byte var2) {
      return new KeyString(var2, var0.getName(), var1);
   }

   private static void doDebug(BEProducerSendRequest var0, BEUOOMember var1, Throwable var2) {
      if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
         JMSDebug.JMSBackEnd.debug("BESend Key:" + var0.getUOOKey() + ", got other member:" + var1 + ", guessed:" + var0.getUOOMember(), var2);
      } else if (PathHelper.PathSvc.isDebugEnabled()) {
         PathHelper.PathSvc.debug("BESend Key:" + var0.getUOOKey() + ", got other member:" + var1 + ", guessed:" + var0.getUOOMember(), var2);
      }

   }

   private static void pathBackDebug(String var0) {
      if (PathHelper.PathSvc.isDebugEnabled()) {
         PathHelper.PathSvc.debug(var0);
      } else {
         JMSDebug.JMSBackEnd.debug(var0);
      }

   }

   private static void dispatcherPathBackDebug(String var0, Throwable var1) {
      if (JMSDebug.JMSDispatcher.isDebugEnabled()) {
         JMSDebug.JMSDispatcher.debug(var0, var1);
      } else if (PathHelper.PathSvc.isDebugEnabled()) {
         PathHelper.PathSvc.debug(var0, var1);
      } else {
         JMSDebug.JMSBackEnd.debug(var0, var1);
      }

   }

   private HashMap getStates(byte var1) {
      return var1 == 1 ? this.uooStates : this.uowStates;
   }

   private State removeState(String var1, byte var2) {
      HashMap var3 = this.getStates(var2);
      return (State)var3.remove(var1);
   }

   private State findState(String var1, byte var2) {
      HashMap var3 = this.getStates(var2);
      return (State)var3.get(var1);
   }

   private void storeState(String var1, byte var2, State var3) {
      HashMap var4 = this.getStates(var2);
      var4.put(var1, var3);
   }

   private State findOrCreateState(String var1, boolean var2, byte var3) {
      State var4 = this.findState(var1, var3);
      if (var4 == null) {
         var4 = new State(var1, var2, var3);
         this.storeState(var1, var3, var4);
      } else if (var2) {
         var4.incrementSendInProgress();
      }

      assert !var4.isInvalid;

      return var4;
   }

   private PathServiceRemoveRetry removeLienInternal(Sequence var1, String var2) {
      State var3 = this.findState(var2, (byte)1);
      if (var3 == null) {
         return null;
      } else {
         synchronized(var3) {
            if (var3.isInvalid) {
               return null;
            } else {
               boolean var5 = var3.isRemovable();
               if (!var3.removeLienInternal(var1)) {
                  return null;
               } else {
                  if (PathHelper.PathSvc.isDebugEnabled()) {
                     PathHelper.PathSvc.debug("DEBUG removed " + var1.getName() + " from uoo " + var3.uoo);
                  }

                  if (var3.isRemovable() && !var5) {
                     if (PathHelper.PathSvc.isDebugEnabled()) {
                        PathHelper.PathSvc.debug("releasing " + var3.uoo);
                     }

                     return var3.setupRemoveRetry((PathServiceRemoveRetry)null);
                  } else {
                     return null;
                  }
               }
            }
         }
      }
   }

   private void addLienInternal(Sequence var1, String var2) {
      this.findOrCreateState(var2, false, (byte)1).addLienInternal(var1);
   }

   public void restorePersistentState(Destination var1) {
      Iterator var2 = var1.getSequences().iterator();
      synchronized(this) {
         while(var2.hasNext()) {
            Sequence var4 = (Sequence)var2.next();
            SequenceData var5 = (SequenceData)var4.getUserData();
            if (var5 != null) {
               String var6 = var5.getUnitOfOrder();
               if (var6 != null) {
                  this.addLienInternal(var4, var6);
               }
            }
         }

      }
   }

   private class DelayedPutIfAbsent extends ContextWrap {
      BEProducerSendRequest request;
      State state;

      DelayedPutIfAbsent(final BEProducerSendRequest var2, final State var3) {
         super(new Runnable() {
            public void run() {
               if (PathHelper.PathSvc.isDebugEnabled() || JMSDebug.JMSBackEnd.isDebugEnabled()) {
                  BEUOOState.pathBackDebug("BEDest resumed putIfAbsent: " + var2.getUOOKey() + " request: " + var2);
               }

               try {
                  var3.callPutIfAbsent(var2);
               } catch (Throwable var2x) {
                  var2.resumeRequest(var2x, false);
               }

            }
         });
         this.request = var2;
         this.state = var3;
      }
   }

   class State {
      private PathServiceRemoveRetry pendingRemove;
      private PathServiceRemoveRetry psRemoveInProgress;
      private BEProducerSendRequest putIfAbsentInProgress;
      private HashSet sequences;
      private boolean hadCreateGroup;
      private boolean hadRemoveGroup;
      private boolean sendSuccessBeforeGroupAdd;
      private boolean isInvalid;
      private int sendInProgress;
      private String uoo;
      private CircularQueue waitingSends;
      private byte indexKey;

      State(String var2, boolean var3, byte var4) {
         this.uoo = var2;
         this.indexKey = var4;
         if (var3) {
            ++this.sendInProgress;
         }

      }

      private synchronized void incrementSendInProgress() {
         ++this.sendInProgress;
      }

      BEUOOState getBEUOOState() {
         return BEUOOState.this;
      }

      boolean isRemovable() {
         return this.hadCreateGroup == this.hadRemoveGroup && this.sendInProgress == 0 && !this.sendSuccessBeforeGroupAdd && (this.sequences == null || this.sequences.isEmpty());
      }

      private boolean removableWithDefaultValues() {
         return this.pendingRemove == null && this.psRemoveInProgress == null && !this.hadCreateGroup;
      }

      synchronized void addLienInternal(Sequence var1) {
         if (this.sequences == null) {
            this.sequences = new HashSet();
         }

         if (PathHelper.PathSvc.isDebugEnabled()) {
            PathHelper.PathSvc.debug("lien " + this.uoo + " add sequence " + var1 + ", " + this);
         }

         this.sequences.add(var1);
      }

      boolean removeLienInternal(Sequence var1) {
         if (this.sequences == null) {
            return false;
         } else {
            boolean var2 = this.sequences.remove(var1);
            if (PathHelper.PathSvc.isDebugEnabled()) {
               PathHelper.PathSvc.debug("remove lien " + var1.getName() + " from uoo " + this.uoo);
            }

            if (this.sequences.isEmpty()) {
               this.sequences = null;
            }

            return var2;
         }
      }

      void removeReference(BEProducerSendRequest var1, boolean var2) {
         PathServiceRemoveRetry var3 = null;
         DelayedPutIfAbsent var4 = null;

         try {
            synchronized(BEUOOState.this) {
               synchronized(this) {
                  var4 = this.computeSendToResume(var1);
                  var1.setUooState((State)null);
                  --this.sendInProgress;
                  if (var2 && !this.hadCreateGroup && !this.isInvalid) {
                     this.sendSuccessBeforeGroupAdd = true;
                  }

                  if (var4 != null) {
                     return;
                  }

                  if (var2) {
                     return;
                  }

                  if (!this.isRemovable()) {
                     return;
                  }

                  if (this.removableWithDefaultValues()) {
                     State var7 = BEUOOState.this.removeState(this.uoo, this.indexKey);

                     assert this == var7;

                     this.isInvalid = true;
                  } else {
                     if (!this.hadRemoveGroup) {
                        return;
                     }

                     var3 = this.setupRemoveRetry((PathServiceRemoveRetry)null);
                     return;
                  }
               }
            }
         } finally {
            if (var4 != null) {
               BEUOOState.this.destination.getBackEnd().getWorkManager().schedule(var4);
            } else if (var3 != null) {
               var3.processRemove();
            }

         }

      }

      private void setupPutIfAbsent(BEProducerSendRequest var1) throws JMSException {
         synchronized(var1) {
            var1.setState(1101);
            var1.needOutsideResult();
            var1.rememberThreadContext();
            var1.getCompletionRequest().addListener(var1);
         }

         synchronized(this) {
            var1.setUooState(this);
            if (this.psRemoveInProgress != null || this.putIfAbsentInProgress != null) {
               if (this.waitingSends == null) {
                  this.waitingSends = new CircularQueue(4);
               }

               this.waitingSends.add(BEUOOState.this.new DelayedPutIfAbsent(var1, this));
               if (PathHelper.PathSvc.isDebugEnabled() || JMSDebug.JMSBackEnd.isDebugEnabled()) {
                  BEUOOState.pathBackDebug("setupPutIfAbsent sees " + this.putIfAbsentInProgress + " send with UOO: " + var1.getUOOKey() + " request: " + var1);
               }

               return;
            }

            this.putIfAbsentInProgress = var1;
         }

         if (PathHelper.PathSvc.isDebugEnabled() || JMSDebug.JMSBackEnd.isDebugEnabled()) {
            BEUOOState.pathBackDebug("BEDest inline putIfAbsent key: " + var1.getUOOKey() + ", member: " + var1.getUOOMember());
         }

         this.callPutIfAbsent(var1);
      }

      private synchronized DelayedPutIfAbsent computeSendToResume(BEProducerSendRequest var1) {
         if (var1 == null) {
            this.putIfAbsentInProgress = null;
            this.psRemoveInProgress = null;
         } else {
            if (this.putIfAbsentInProgress != var1 && this.putIfAbsentInProgress != null) {
               boolean var5 = false;
               if (this.waitingSends != null) {
                  Iterator var3 = this.waitingSends.iterator();

                  while(var3.hasNext()) {
                     Object var4 = var3.next();
                     if (var4 == var1) {
                        var5 = true;
                        var3.remove();
                        break;
                     }
                  }
               }

               if (var5 && this.waitingSends.isEmpty()) {
                  this.waitingSends = null;
               }

               if (PathHelper.PathSvc.isDebugEnabled() || JMSDebug.JMSBackEnd.isDebugEnabled()) {
                  BEUOOState.pathBackDebug("computeSendToResume found=" + var5 + " waiting has " + (this.waitingSends == null ? "zero" : "" + this.waitingSends.size()) + ", sendCompletedPutIfAbsent:" + var1 + " is not putIfAbsentInProgress:" + this.putIfAbsentInProgress);
               }

               return null;
            }

            this.putIfAbsentInProgress = null;
         }

         if (this.waitingSends == null) {
            return null;
         } else {
            DelayedPutIfAbsent var2 = (DelayedPutIfAbsent)this.waitingSends.remove();
            if (this.waitingSends.isEmpty()) {
               this.waitingSends = null;
            }

            this.putIfAbsentInProgress = var2.request;
            if (PathHelper.PathSvc.isDebugEnabled() || JMSDebug.JMSBackEnd.isDebugEnabled()) {
               BEUOOState.pathBackDebug("BEDest putIfAbsentInProgress: " + var2.request.getUOOKey() + " state: " + var2.request.getState() + " request: " + var2.request);
            }

            return var2;
         }
      }

      private void callPutIfAbsent(BEProducerSendRequest var1) {
         assert this.putIfAbsentInProgress == var1;

         try {
            PathHelper.manager().cachedPutIfAbsent(PathHelper.DEFAULT_PATH_SERVICE_JNDI, var1.getUOOKey(), var1.getUOOMember(), BEUOOState.QOS_STORE_OWNED_CACHE_ON_EQUAL, var1.getCompletionRequest());
         } catch (NamingException var7) {
            boolean var3;
            synchronized(var1.getCompletionRequest()) {
               var3 = !var1.getCompletionRequest().hasResult();
            }

            if (var3) {
               var1.getCompletionRequest().setResult(new JMSOrderException("path service not available", var7));
            }
         }

      }

      void processPathServiceResult(BEProducerSendRequest var1) {
         CompletionRequest var2 = var1.getCompletionRequest();
         BEUOOMember var3 = null;

         try {
            var3 = (BEUOOMember)var2.getResult();
            if (var3 == null) {
               if (JMSDebug.JMSBackEnd.isDebugEnabled() || PathHelper.PathSvc.isDebugEnabled()) {
                  BEUOOState.pathBackDebug("BESend stored success State:" + var1.getState() + ", Key: " + var1.getUOOKey() + ", got: " + var3 + ", guessed: " + var1.getUOOMember());
               }

               synchronized(var1) {
                  var1.setState(1102);
               }

               var1.resumeExecution(false);
               return;
            }

            if (BEUOOState.this.destination.getName().equals(var3.getMemberId())) {
               if (JMSDebug.JMSBackEnd.isDebugEnabled() || PathHelper.PathSvc.isDebugEnabled()) {
                  BEUOOState.pathBackDebug("BESend success State:" + var1.getState() + ", Key: " + var1.getUOOKey() + ", got: " + var3 + ", guessed: " + var1.getUOOMember());
               }

               var1.setUOOInfo(var1.getUOOKey(), var3, var2);
               synchronized(var1) {
                  var1.setState(1102);
               }

               var1.resumeExecution(false);
               return;
            }
         } catch (Throwable var9) {
            BEUOOState.doDebug(var1, var3, var9);
            var1.resumeRequest(var9, true);
            return;
         }

         JMSOrderException var4 = new JMSOrderException("Unit of Order on Distributed Destination " + BEUOOState.this.destination.getName() + " rather than " + var3.getMemberId());
         var4.setMember(var3);
         BEUOOState.doDebug(var1, var3, var4);
         var1.resumeRequest(var4, true);
      }

      private void cancelPendingRemove() {
         PathServiceRemoveRetry var1 = this.pendingRemove;
         if (var1 != null) {
            DelayedPutIfAbsent var2;
            synchronized(var1) {
               var1.cancel();
               this.pendingRemove = null;
               var2 = this.computeSendToResume((BEProducerSendRequest)null);

               assert this.psRemoveInProgress == null;

               synchronized(BEUOOState.retryListLock) {
                  if (BEUOOState.delayedRemoves == null) {
                     return;
                  }

                  PathServiceRemoveRetry var5 = (PathServiceRemoveRetry)BEUOOState.delayedRemoves.remove(var1.getKey());
                  if (var5 != null && var5 != var1) {
                     var5.cancel();
                     BEUOOState.delayedRemove.remove(var1);
                     break label15;
                  }
               }

               return;
            }

            if (var2 != null) {
               BEUOOState.this.destination.getBackEnd().getWorkManager().schedule(var2);
            }

         }
      }

      private PathServiceRemoveRetry setupRemoveRetry(PathServiceRemoveRetry var1) {
         PathServiceRemoveRetry var2 = BEUOOState.this.new PathServiceRemoveRetry(BEUOOState.getNewKeyString(BEUOOState.this.ddHandler, this.uoo, this.indexKey), (BEUOOMember)null);
         synchronized(this) {
            if (var1 != this.pendingRemove) {
               if (PathHelper.PathSvc.isDebugEnabled()) {
                  PathHelper.PathSvc.debug("different remove pending" + this);
               }

               return null;
            } else if (!this.isRemovable()) {
               if (PathHelper.PathSvc.isDebugEnabled()) {
                  PathHelper.PathSvc.debug("DEBUG not removed on groupRemoveEvent " + this);
               }

               this.cancelPendingRemove();
               return null;
            } else {
               if (PathHelper.PathSvc.isDebugEnabled()) {
                  PathHelper.PathSvc.debug("DEBUG removing " + this);
               }

               if (this.pendingRemove == null) {
                  var2.setState(this);
                  this.pendingRemove = var2;
               }

               return this.pendingRemove;
            }
         }
      }

      private boolean completeRemove() {
         synchronized(BEUOOState.this) {
            boolean var10000;
            synchronized(this) {
               if (this.isRemovable()) {
                  if (BEUOOState.verbose) {
                     Object var3 = BEUOOState.TODOremoveDebug.remove(BEUOOState.this.destination.getName());
                     if (var3 != BEUOOState.this && null != var3) {
                        BEUOOState.TODOremoveDebug.put(BEUOOState.this.destination.getName(), var3);
                     }
                  }

                  State var8 = BEUOOState.this.removeState(this.uoo, this.indexKey);
                  if (var8 != this && null != var8) {
                     BEUOOState.this.storeState(this.uoo, this.indexKey, var8);
                  }

                  this.isInvalid = true;
                  var10000 = true;
                  return var10000;
               }

               var10000 = false;
            }

            return var10000;
         }
      }

      public String toString() {
         return "beUOOState.state uoo=" + this.uoo + " create=" + this.hadCreateGroup + " |remove=" + this.hadRemoveGroup + " |pending=" + this.pendingRemove + " |sendInProgress=" + this.sendInProgress + " |successBeforeAdd=" + this.sendSuccessBeforeGroupAdd + " |isRemovable=" + this.isRemovable() + " |sequences=" + this.sequences;
      }
   }

   private final class PathServiceRemoveRetry implements TimerListener, Runnable {
      private State state;
      private KeyString key;
      private BEUOOMember member;
      private boolean cancelled;

      PathServiceRemoveRetry(KeyString var2, BEUOOMember var3) {
         this.key = var2;
         this.member = var3;
      }

      private void setState(State var1) {
         this.state = var1;
      }

      private void cancel() {
         this.cancelled = true;
      }

      protected boolean processRemove() {
         synchronized(this.state) {
            if (this.state.setupRemoveRetry(this) != this) {
               this.cancel();
               return true;
            }
         }

         try {
            PathHelper.manager().cachedGet(PathHelper.DEFAULT_PATH_SERVICE_JNDI, this.key, 32832, this.getCompReqListener());
            return true;
         } catch (NamingException var3) {
            BEUOOState.this.retryPathServiceLater(this);
            return false;
         }
      }

      private CompReqListener getCompReqListener() {
         return new CompReqListener() {
            public final void onException(CompletionRequest var1, Throwable var2) {
               BEUOOState.this.retryPathServiceLater(PathServiceRemoveRetry.this);
            }

            public final void onCompletion(CompletionRequest var1, Object var2) {
               PathServiceRemoveRetry.this.member = (BEUOOMember)var2;
               if (PathServiceRemoveRetry.this.member != null && PathServiceRemoveRetry.this.member.getDynamic() && PathServiceRemoveRetry.this.member.getMemberId().equals(BEUOOState.this.destination.getName())) {
                  CompReqListener var3 = new CompReqListener() {
                     public final void onException(CompletionRequest var1, Throwable var2) {
                        BEUOOState.this.retryPathServiceLater(PathServiceRemoveRetry.this);
                     }

                     public final void onCompletion(CompletionRequest var1, Object var2) {
                        PathServiceRemoveRetry.this.completeProcessing();
                     }
                  };
                  boolean var4;
                  synchronized(BEUOOState.retryListLock) {
                     var4 = PathServiceRemoveRetry.this.cancelled;
                  }

                  if (var4) {
                     var3.onCompletion(var3, Boolean.FALSE);
                  } else {
                     try {
                        PathHelper.manager().cachedRemove(PathHelper.DEFAULT_PATH_SERVICE_JNDI, PathServiceRemoveRetry.this.key, PathServiceRemoveRetry.this.member, 33352, var3);
                     } catch (NamingException var7) {
                        BEUOOState.this.retryPathServiceLater(PathServiceRemoveRetry.this);
                     }

                  }
               } else {
                  PathHelper.PathSvc.debug("DEBUG not deleting key:" + PathServiceRemoveRetry.this.key + " , value: " + PathServiceRemoveRetry.this.member + " from PathService on " + BEUOOState.this.destination.getName());
                  PathServiceRemoveRetry.this.completeProcessing();
               }
            }
         };
      }

      private KeyString getKey() {
         return this.key;
      }

      public final void timerExpired(Timer var1) {
         this.processPendingEntry();
      }

      public final void run() {
         this.processPendingEntry();
      }

      private void processPendingEntry() {
         boolean var1 = false;

         try {
            while(true) {
               PathServiceRemoveRetry var2;
               synchronized(BEUOOState.retryListLock) {
                  if (!var1 && BEUOOState.delayedRemoveRunning) {
                     return;
                  }

                  var1 = BEUOOState.delayedRemoveRunning = true;
                  if (BEUOOState.delayedRemoves == null || BEUOOState.delayedRemove.isEmpty()) {
                     return;
                  }

                  var2 = (PathServiceRemoveRetry)BEUOOState.delayedRemove.remove();
                  BEUOOState.delayedRemoves.remove(var2.key);
               }

               boolean var3 = var2.processRemove();
               synchronized(BEUOOState.retryListLock) {
                  if (var3) {
                     if (BEUOOState.this.destination.getBackEnd().getWorkManager().scheduleIfBusy(this)) {
                        var1 = false;
                        BEUOOState.delayedRemoveRunning = false;
                        break;
                     }
                  } else {
                     break;
                  }
               }
            }
         } finally {
            if (var1) {
               synchronized(BEUOOState.retryListLock) {
                  BEUOOState.delayedRemoveRunning = false;
               }
            }

         }

      }

      private void completeProcessing() {
         if (this.state.completeRemove()) {
            synchronized(BEUOOState.retryListLock) {
               if (BEUOOState.delayedRemoves == null) {
                  return;
               }

               BEUOOState.delayedRemoves.remove(this.key);
               BEUOOState.delayedRemove.remove(this);
               if (BEUOOState.delayedRemoveRunning || BEUOOState.delayedRemove.isEmpty()) {
                  return;
               }
            }

            BEUOOState.this.destination.getBackEnd().getTimerManager().schedule(this, BEUOOState.PATH_SERVICE_RESUME_RETRY_DELAY);
         }

      }
   }

   private static class PathServiceCompReqListener extends CompReqListener {
      private final InheritableThreadContext context = InheritableThreadContext.getContext();
      private final BEProducerSendRequest sendRequest;

      PathServiceCompReqListener(BEProducerSendRequest var1) {
         this.sendRequest = var1;
      }

      public final void run() {
         this.context.push();

         try {
            super.run();
         } finally {
            this.context.pop();
         }

      }

      public void setResult(Object var1) {
         super.setResult(var1);
      }

      public Object getResult() throws Throwable {
         return super.getResult();
      }

      public boolean hasResult() {
         return super.hasResult();
      }

      public void onCompletion(CompletionRequest var1, Object var2) {
         State var4 = this.sendRequest.getUooState();
         DelayedPutIfAbsent var3;
         if (var4 == null) {
            var3 = null;
         } else {
            var3 = var4.computeSendToResume(this.sendRequest);
         }

         State var5 = this.sendRequest.getUooState();

         try {
            var5.processPathServiceResult(this.sendRequest);
         } finally {
            if (var3 != null) {
               var5.getBEUOOState().destination.getBackEnd().getWorkManager().schedule(var3);
            }

         }

      }

      public void onException(CompletionRequest var1, Throwable var2) {
         DelayedPutIfAbsent var3 = this.sendRequest.getUooState().computeSendToResume(this.sendRequest);
         State var4 = this.sendRequest.getUooState();

         try {
            this.sendRequest.resumeRequest(PathHelper.wrapExtensionImpl(var2), false);
         } finally {
            if (var3 != null) {
               var4.getBEUOOState().destination.getBackEnd().getWorkManager().schedule(var3);
            }

         }

      }
   }

   private abstract static class CompReqListener extends CompletionRequest implements CompletionListener {
      CompReqListener() {
         this.addListener(this);
      }
   }
}
