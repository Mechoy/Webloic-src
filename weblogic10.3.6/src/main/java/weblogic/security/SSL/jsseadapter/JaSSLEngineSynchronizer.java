package weblogic.security.SSL.jsseadapter;

import java.util.Stack;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;

final class JaSSLEngineSynchronizer {
   private final ThreadLocal<Stack<LockState>> lockStateStack = new ThreadLocal<Stack<LockState>>() {
      protected Stack<LockState> initialValue() {
         return new Stack();
      }
   };
   private final Lock inboundLock;
   private final Lock outboundLock;

   LockState getLockState() {
      Stack var1 = (Stack)this.lockStateStack.get();
      LockState var2;
      if (var1.empty()) {
         var2 = JaSSLEngineSynchronizer.LockState.NONE;
      } else {
         var2 = (LockState)var1.peek();
      }

      return var2;
   }

   int getLockStateStackSize() {
      Stack var1 = (Stack)this.lockStateStack.get();
      return var1.size();
   }

   void lock(LockState var1) {
      if (null == var1) {
         throw new IllegalArgumentException("Non-null nextState expected.");
      } else if (JaSSLEngineSynchronizer.LockState.NONE == var1) {
         throw new IllegalArgumentException("NONE state may not be set; use unlock instead.");
      } else {
         LockState var2 = this.getLockState();
         if (var2 != var1) {
            if (JaSSLEngineSynchronizer.LockState.NONE == var2 && JaSSLEngineSynchronizer.LockState.INBOUND == var1) {
               this.inboundLock.lock();
            } else if (JaSSLEngineSynchronizer.LockState.NONE == var2 && JaSSLEngineSynchronizer.LockState.OUTBOUND == var1) {
               this.outboundLock.lock();
            } else if (JaSSLEngineSynchronizer.LockState.INBOUND == var2 && JaSSLEngineSynchronizer.LockState.HANDSHAKE == var1) {
               this.inboundLock.unlock();
               this.outboundLock.lock();
               this.inboundLock.lock();
            } else {
               if (JaSSLEngineSynchronizer.LockState.OUTBOUND != var2 || JaSSLEngineSynchronizer.LockState.HANDSHAKE != var1) {
                  if (JaLogger.isLoggable(Level.FINE)) {
                     JaLogger.log(Level.FINE, JaLogger.Component.SSLENGINE, "[{0}] Illegal state for lock: currentLockState={1}, nextState={2}.", this.getClass().getName(), var2, var1);
                  }

                  throw new IllegalStateException("currentLockState=" + var2 + ", nextState=" + var1);
               }

               this.inboundLock.lock();
            }
         }

         ((Stack)this.lockStateStack.get()).push(var1);
         if (JaLogger.isLoggable(Level.FINEST)) {
            JaLogger.log(Level.FINEST, JaLogger.Component.SSLENGINE, "[{0}] lock completed, pushed lock state \"{1}\", previous lock state \"{2}\", post-push stack size={3}", this.getClass().getName(), var1, var2, ((Stack)this.lockStateStack.get()).size());
         }

      }
   }

   void unlock() {
      Stack var1 = (Stack)this.lockStateStack.get();
      if (var1.empty()) {
         if (JaLogger.isLoggable(Level.FINE)) {
            JaLogger.log(Level.FINE, JaLogger.Component.SSLENGINE, "[{0}] unlock called with empty stack. Not matched with lock?", this.getClass().getName());
         }

      } else {
         LockState var2 = (LockState)var1.pop();
         LockState var3 = this.getLockState();
         if (var2 == var3) {
            if (JaLogger.isLoggable(Level.FINEST)) {
               JaLogger.log(Level.FINEST, JaLogger.Component.SSLENGINE, "[{0}] unlock called with no state change, state={1}", this.getClass().getName(), var2);
            }

         } else {
            if (JaSSLEngineSynchronizer.LockState.HANDSHAKE == var2 && JaSSLEngineSynchronizer.LockState.INBOUND == var3) {
               this.outboundLock.unlock();
            } else if (JaSSLEngineSynchronizer.LockState.HANDSHAKE == var2 && JaSSLEngineSynchronizer.LockState.OUTBOUND == var3) {
               this.inboundLock.unlock();
            } else if (JaSSLEngineSynchronizer.LockState.INBOUND == var2 && JaSSLEngineSynchronizer.LockState.NONE == var3) {
               this.inboundLock.unlock();
            } else {
               if (JaSSLEngineSynchronizer.LockState.OUTBOUND != var2 || JaSSLEngineSynchronizer.LockState.NONE != var3) {
                  if (JaLogger.isLoggable(Level.FINE)) {
                     JaLogger.log(Level.FINE, JaLogger.Component.SSLENGINE, "[{0}] Illegal state for unlock: poppedState={1}, lockStack.peek()={2}.", this.getClass().getName(), var2, var3);
                  }

                  throw new IllegalStateException("poppedState=" + var2 + ", lockStack.peek=" + var3);
               }

               this.outboundLock.unlock();
            }

            if (JaLogger.isLoggable(Level.FINEST)) {
               JaLogger.log(Level.FINEST, JaLogger.Component.SSLENGINE, "[{0}] unlock completed, current lock state \"{1}\", popped lock state \"{2}\", post-pop stack size={3}", this.getClass().getName(), var3, var2, ((Stack)this.lockStateStack.get()).size());
            }

         }
      }
   }

   JaSSLEngineSynchronizer() {
      this.inboundLock = new ReentrantLock();
      this.outboundLock = new ReentrantLock();
   }

   JaSSLEngineSynchronizer(Lock var1, Lock var2) {
      this.inboundLock = var1;
      this.outboundLock = var2;
   }

   static enum LockState {
      NONE,
      INBOUND,
      OUTBOUND,
      HANDSHAKE;
   }
}
