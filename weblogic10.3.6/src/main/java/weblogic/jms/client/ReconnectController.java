package weblogic.jms.client;

import java.io.IOException;
import java.net.SocketException;
import java.rmi.ConnectException;
import java.rmi.ConnectIOException;
import java.rmi.NoSuchObjectException;
import java.rmi.UnknownHostException;
import java.rmi.UnmarshalException;
import javax.jms.JMSException;
import javax.naming.NameNotFoundException;
import weblogic.jms.common.DestroyConnectionException;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.LostServerException;
import weblogic.messaging.dispatcher.DispatcherException;
import weblogic.utils.NestedThrowable;

abstract class ReconnectController {
   private static final int STATE_NO_RETRY = -2304;
   private static final int STATE_USER_CLOSED = -1280;
   private static final int STATE_CONNECTED = 0;
   private static final int STATE_HAVE_RECON_INFO_PHYSICAL_CLOSE_DONE = 512;
   private static final int STATE_HAVE_RECON_INFO_PEERGONEE_IN_PROGRESS = 1028;
   private static final int STATE_HAVE_RECON_INFO_CLOSE_IN_PROGRESS = 1040;
   private static final int STATE_RECON_SCHEDULED = 1280;
   private static final int STATE_RECON_IN_PROGRESS = 1536;
   private int state;
   private Throwable lastProblem;
   ReconnectController firstChild;
   ReconnectController lastChild;
   ReconnectController prevChild;
   ReconnectController nextChild;
   ReconnectController parent;
   private volatile Reconnectable physical;
   private static int RETRIES = 12;
   private static long LARGE_TIME_FOR_EXCEPTION_MSG = 14400000L;
   static boolean TODOREMOVEDebug = false;

   protected ReconnectController(ReconnectController var1, Reconnectable var2) {
      this.physical = var2;

      assert this.getState() == 0;

      if (var1 != null) {
         int var3 = var1.getState();
         if (var3 != -1280) {
            this.parent = var1;
            this.setState(var3);
            var1.addChild(this);
         }
      }

   }

   abstract Object getConnectionStateLock();

   protected abstract ReconnectController getParent();

   protected abstract JMSConnection getPhysicalJMSConnection();

   protected abstract WLConnectionImpl getWLConnectionImpl();

   int getState() {
      return this.state;
   }

   private void setState(int var1) {
      this.state = var1;
   }

   Throwable getLastProblem() {
      return this.lastProblem;
   }

   protected void setLastProblem(Throwable var1) {
      this.lastProblem = var1;
      if (TODOREMOVEDebug) {
         if (var1 == null) {
            System.err.println("Debug ReconnectController lastProblem cleared");
         } else {
            JMSConnection.displayExceptionCauses("Debug ReconnectController lastProblem set", var1);
         }
      }

   }

   protected Reconnectable getPhysical() {
      return this.physical;
   }

   boolean isClosed() {
      synchronized(this.getConnectionStateLock()) {
         return this.getState() == -1280;
      }
   }

   protected void addChild(ReconnectController var1) {
      assert var1 != this;

      synchronized(this.getConnectionStateLock()) {
         if (this.getState() != -1280) {
            if (this.lastChild == null) {
               this.firstChild = this.lastChild = var1;
            } else {
               var1.prevChild = this.lastChild;
               this.lastChild.nextChild = var1;
               this.lastChild = var1;
            }

         }
      }
   }

   protected void removeChild(ReconnectController var1) {
      if (var1 != this) {
         if (var1.prevChild == null) {
            this.firstChild = var1.nextChild;
         } else {
            var1.prevChild.nextChild = var1.nextChild;
         }

         if (var1.nextChild == null) {
            this.lastChild = var1.prevChild;
         } else {
            var1.nextChild.prevChild = var1.prevChild;
         }

         var1.nextChild = var1.prevChild = null;
      }
   }

   protected void setRecursiveStateUserClosed() {
      this.setState(-1280);

      while(true) {
         ReconnectController var1 = this.firstChild;
         if (var1 == null) {
            return;
         }

         this.removeChild(var1);
         var1.setRecursiveStateUserClosed();
      }
   }

   protected void setRecursiveState(int var1) {
      assert var1 != -1280;

      if (TODOREMOVEDebug) {
         if (var1 == -2304) {
            (new Exception("DEBUG ReconnectController STATE_NO_RETRY from " + this.getStringState(this.getState()))).printStackTrace();
         } else {
            System.err.println("DEBUG ReconnectController stateBecomes " + this.getStringState(var1) + " " + this.getPhysical());
         }
      }

      synchronized(this.getConnectionStateLock()) {
         this.setStateChildrenInternal(var1);
      }
   }

   private void setStateChildrenInternal(int var1) {
      if (this.getState() != -1280 && this.getState() != -2304) {
         this.setState(var1);

         for(ReconnectController var2 = this.firstChild; var2 != null; var2 = var2.nextChild) {
            var2.setStateChildrenInternal(var1);
         }

      }
   }

   protected void setRecursiveStateNotify(int var1) {
      Object var2 = this.getConnectionStateLock();
      synchronized(var2) {
         this.setRecursiveState(var1);
         var2.notifyAll();
      }
   }

   protected Reconnectable getPhysicalWaitForState(long var1) {
      Object var3 = this.getConnectionStateLock();
      synchronized(var3) {
         return this.waitForStateInternal(var1);
      }
   }

   protected Reconnectable getPhysicalWaitForState() {
      return this.getPhysicalWaitForState(System.currentTimeMillis());
   }

   boolean stateNeedsWait() {
      return 1024 <= this.getState();
   }

   protected Reconnectable waitForStateInternal(long var1) {
      while(true) {
         if (this.stateNeedsWait()) {
            long var5 = this.getWLConnectionImpl().getReconnectBlockingInternal();
            if (var5 != 0L && (var5 != -1L || this.getWLConnectionImpl().getLastReconnectTimer() != 0L)) {
               long var3 = this.remainingMillis(var1);
               if (var3 >= 1L) {
                  if (TODOREMOVEDebug) {
                     System.err.println("ReconnectController Debug waiting to change " + this.getStringState(this.getState()) + " " + var3 + " " + this.getPhysical());
                  } else if (JMSDebug.JMSCommon.isDebugEnabled()) {
                     JMSDebug.JMSCommon.debug("ReconnectController waiting to change " + this.getStringState(this.getState()) + " " + var3);
                  }

                  try {
                     this.getConnectionStateLock().wait(var3);
                     continue;
                  } catch (InterruptedException var8) {
                     throw new RuntimeException(var8);
                  }
               }
            }
         }

         return this.getPhysical();
      }
   }

   private long remainingMillis(long var1) {
      long var3 = this.getWLConnectionImpl().getReconnectBlockingInternal();
      if (var3 == -1L) {
         return LARGE_TIME_FOR_EXCEPTION_MSG;
      } else {
         long var5 = System.currentTimeMillis() - var1;
         return var3 - var5;
      }
   }

   Reconnectable waitForStateOrTime(long var1) {
      Object var3 = this.getConnectionStateLock();
      synchronized(var3) {
         long var5 = var1 - System.currentTimeMillis();

         while(var5 > 0L && this.stateNeedsWait()) {
            try {
               var3.wait(var5);
            } catch (InterruptedException var9) {
               throw new RuntimeException(var9);
            }
         }

         return this.getPhysical();
      }
   }

   protected AssertionError wrongState(int var1) {
      AssertionError var2 = new AssertionError(this.wrongStateString(var1));
      if (TODOREMOVEDebug) {
         var2.printStackTrace();
      }

      return var2;
   }

   String wrongStateString(int var1) {
      return "ReconnectController unexpected state " + var1 + " is " + this.getStringState(var1);
   }

   protected String getStringState(int var1) {
      String var2;
      switch (var1) {
         case -2304:
            var2 = "STATE_NO_RETRY";
            break;
         case -1280:
            var2 = "STATE_USER_CLOSED";
            break;
         case 0:
            var2 = "STATE_CONNECTED";
            break;
         case 512:
            var2 = "STATE_HAVE_RECON_INFO_PHYSICAL_CLOSE_DONE";
            break;
         case 1028:
            var2 = "STATE_HAVE_RECON_INFO_PEERGONEE_IN_PROGRESS";
            break;
         case 1040:
            var2 = "STATE_HAVE_RECON_INFO_CLOSE_IN_PROGRESS";
            break;
         case 1280:
            var2 = "STATE_RECON_SCHEDULED";
            break;
         case 1536:
            var2 = "STATE_RECON_IN_PROGRESS";
            break;
         default:
            var2 = "Illegal State " + var1;
      }

      return var2;
   }

   void setPhysicalReconnectable(Reconnectable var1) {
      synchronized(this.getConnectionStateLock()) {
         int var3 = this.getState();
         switch (var3) {
            case 0:
            case 512:
            case 1536:
               this.physical = var1;
            case -2304:
            case -1280:
            case 1028:
            case 1040:
            case 1280:
               return;
            default:
               throw this.wrongState(var3);
         }
      }
   }

   public final void close() throws JMSException {
      try {
         if (this.getState() == 0 && this.getPhysicalJMSConnection().isConnected()) {
            this.getPhysical().close();
         }
      } finally {
         Object var3 = this.getConnectionStateLock();
         synchronized(var3) {
            try {
               this.getPhysical().forgetReconnectState();
            } finally {
               if (this.getState() != -1280) {
                  this.setRecursiveStateUserClosed();
                  if (this.parent != null) {
                     this.parent.removeChild(this);
                  }

                  var3.notifyAll();
               }

            }

         }
      }

   }

   private Reconnectable analyzeExceptionAndReconnect(long var1, Reconnectable var3, boolean var4, weblogic.jms.common.JMSException var5) throws JMSException {
      int var6 = -256;
      int var7 = RETRIES;

      while(true) {
         label82: {
            JMSConnection var8;
            try {
               synchronized(this.getConnectionStateLock()) {
                  var6 = this.getState();
                  if (var7 == 0) {
                     return this.changedPhysicalOrThrow(var3, var5);
                  }

                  if (var7 == RETRIES) {
                     this.throwNonRecoverableException(var5, var4);
                     this.getWLConnectionImpl().updateLastReconnectTimer();
                  }

                  switch (var6) {
                     case -2304:
                     case -1280:
                     case 0:
                        return this.changedPhysicalOrThrow(var3, var5);
                     case 512:
                        if (this.getWLConnectionImpl().getReconnectPolicyInternal() == 0) {
                           this.getWLConnectionImpl().setRecursiveStateNotify(-2304);
                           return this.changedPhysicalOrThrow(var3, var5);
                        }

                        var8 = this.getPhysicalJMSConnection();
                        var6 = this.setupReconnectSchedule(var8, 1280);
                        break;
                     case 1028:
                     case 1040:
                     case 1280:
                     case 1536:
                        this.waitForStateInternal(var1);
                        if (this.stateNeedsWait()) {
                           return this.changedPhysicalOrThrow(var3, var5);
                        }
                        break label82;
                     default:
                        throw this.wrongState(var6);
                  }
               }
            } catch (LostServerException var12) {
               if (var5 != null && var12.getCause() == null) {
                  throw this.attachReasonToException(var12, var1, var6);
               }

               throw var12;
            }

            assert var6 == 1280;

            JMSConnection.getReconnectWorkManager().schedule(var8);
         }

         --var7;
      }
   }

   protected int setupReconnectSchedule(JMSConnection var1, int var2) {
      var1.setReplacementConnection((JMSConnection)null);
      this.getWLConnectionImpl().setRecursiveStateNotify(var2);
      return var2;
   }

   private Reconnectable changedPhysicalOrThrow(Reconnectable var1, weblogic.jms.common.JMSException var2) throws weblogic.jms.common.JMSException {
      Reconnectable var3 = this.getPhysical();
      if (var1 != null && var1.getFEPeerInfo() == var3.getFEPeerInfo()) {
         throw var2;
      } else {
         return var3;
      }
   }

   private LostServerException attachReasonToException(LostServerException var1, long var2, int var4) {
      String var5;
      if (var4 == 0) {
         var5 = null;
      } else {
         long var6;
         if (this.getWLConnectionImpl().getReconnectBlockingInternal() > -1L && (var6 = this.remainingMillis(var2)) <= 0L) {
            var5 = "timed out in state " + this.getStringState(var4) + " after " + (this.getWLConnectionImpl().getReconnectBlockingInternal() - var6) + " milliseconds";
         } else {
            var5 = "server connection in state " + this.getStringState(var4);
         }
      }

      Throwable var8 = this.getWLConnectionImpl().getLastProblem();
      LostServerException var9;
      if (var8 == null) {
         if (var5 == null) {
            if (TODOREMOVEDebug) {
               System.err.println("DEBUG ReconnectController in " + this.getStringState(this.getState()));
            }

            return var1;
         }

         var9 = new LostServerException(var5);
      } else {
         var9 = new LostServerException(var5, var8);
      }

      var9.setReplayLastException(true);
      var1.initCause(var9);
      return var1;
   }

   protected JMSConnection computeJMSConnection(long var1, Reconnectable var3, weblogic.jms.common.JMSException var4) throws JMSException {
      return (JMSConnection)this.analyzeExceptionAndReconnect(var1, var3, true, var4);
   }

   protected JMSSession computeJMSSession(long var1, Reconnectable var3, weblogic.jms.common.JMSException var4) throws JMSException {
      return (JMSSession)this.analyzeExceptionAndReconnect(var1, var3, true, var4);
   }

   protected JMSProducer nonIdempotentJMSProducer(long var1, Reconnectable var3, weblogic.jms.common.JMSException var4) throws JMSException {
      return (JMSProducer)this.analyzeExceptionAndReconnect(var1, var3, false, var4);
   }

   protected JMSProducer idempotentJMSProducer(long var1, Reconnectable var3, weblogic.jms.common.JMSException var4) throws JMSException {
      return (JMSProducer)this.analyzeExceptionAndReconnect(var1, var3, true, var4);
   }

   protected JMSConsumer computeJMSConsumer(long var1, Reconnectable var3, weblogic.jms.common.JMSException var4) throws JMSException {
      return (JMSConsumer)this.analyzeExceptionAndReconnect(var1, var3, true, var4);
   }

   protected Reconnectable checkClosedReconnect(long var1, Reconnectable var3) throws JMSException {
      try {
         var3.publicCheckClosed();
         return var3;
      } catch (weblogic.jms.common.JMSException var6) {
         Reconnectable var5 = this.analyzeExceptionAndReconnect(var1, var3, true, var6);
         if (var5 != var3) {
            return var5;
         } else {
            throw var6;
         }
      }
   }

   private void throwNonRecoverableException(weblogic.jms.common.JMSException var1, boolean var2) throws JMSException {
      if (var1 != null) {
         int var4 = 40;
         boolean var5 = false;

         Object var6;
         for(var6 = var1; var6 instanceof weblogic.jms.common.JMSException && var4 != 0; --var4) {
            if (var6 instanceof DestroyConnectionException) {
               throw var1;
            }

            if (!var5 && var6 instanceof LostServerException) {
               var5 = ((LostServerException)var6).isReplayLastException();
            }

            var6 = ((Throwable)var6).getCause();
         }

         var4 = 40;
         Object var7 = var6;

         while(true) {
            if (var7 != null && var4 != 0) {
               if (!(var7 instanceof ConnectException) && !(var7 instanceof UnknownHostException) && !(var7 instanceof ConnectIOException) && !(var7 instanceof NoSuchObjectException)) {
                  if (var7 instanceof DispatcherException && ((Throwable)var7).getCause() instanceof NameNotFoundException) {
                     if (TODOREMOVEDebug) {
                        System.err.println("DEBUG ReconnectController new recover NameNotFound " + this.getStringState(this.getState()));
                        JMSConnection.displayExceptionCauses("DEBUG ReconnectController nameNotFound", var1);
                     }

                     return;
                  }

                  var7 = ((Throwable)var7).getCause();
                  --var4;
                  continue;
               }

               return;
            }

            if (var1 instanceof LostServerException && (var6 == null || var5)) {
               return;
            }

            if (!var2 || !(var1 instanceof LostServerException) || !(var6 instanceof Exception) || !(getCauseOrNested((Throwable)var6) instanceof UnmarshalException) || !(getCauseOrNested(((Throwable)var6).getCause()) instanceof IOException) && !(getCauseOrNested(((Throwable)var6).getCause()) instanceof SocketException)) {
               if (TODOREMOVEDebug) {
                  System.err.println("DEBUG ReconnectController did not recover " + this.getStringState(this.getState()) + " preInvokeFailure " + (40 - var4));
                  JMSConnection.displayExceptionCauses("DEBUG ReconnectController nonRecover", var1);
               }

               throw var1;
            }

            return;
         }
      }
   }

   private static Throwable getCauseOrNested(Throwable var0) {
      if (var0.getCause() != null) {
         return var0.getCause();
      } else {
         if (var0 instanceof NestedThrowable) {
            Throwable var1 = ((NestedThrowable)var0).getNested();
            if (var1 != var0) {
               return var1;
            }
         }

         return null;
      }
   }
}
