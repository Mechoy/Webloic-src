package weblogic.wsee.reliability2.store;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManager;
import weblogic.timers.TimerManagerFactory;
import weblogic.wsee.WseeRmLogger;
import weblogic.wsee.reliability2.api.SequenceState;
import weblogic.wsee.reliability2.sequence.Sequence;

public abstract class TimedSequenceMap<S extends Sequence> extends SequenceMap<S> {
   private static final Logger LOGGER = Logger.getLogger(TimedSequenceMap.class.getName());
   private static final TimerManager _timerMgr;
   private static final boolean LOCK_FAIRNESS = false;
   private final Map<String, TimedSequenceMap<S>.SequenceTimerInfo> _seqIdToTimerInfoMap = new HashMap();
   private ReentrantReadWriteLock _seqIdToTimerInfoMapLock = new ReentrantReadWriteLock(false);
   private SequenceTimerListener _sequenceTimerListener;

   public void setSequenceTimerListener(SequenceTimerListener var1) {
      this._sequenceTimerListener = var1;
   }

   boolean startupSequence(S var1) {
      if (super.startupSequence(var1)) {
         if (this._sequenceTimerListener == null) {
            throw new IllegalStateException("Attempt to add timed sequence before calling setSequenceTimerListener");
         } else {
            SequenceTimerInfo var2;
            try {
               this._seqIdToTimerInfoMapLock.writeLock().lock();
               if (this._seqIdToTimerInfoMap.containsKey(var1.getId())) {
                  boolean var3 = true;
                  return var3;
               }

               var2 = new SequenceTimerInfo(var1, this._sequenceTimerListener);
               this._seqIdToTimerInfoMap.put(var1.getId(), var2);
            } finally {
               this._seqIdToTimerInfoMapLock.writeLock().unlock();
            }

            var2.startAll();
            return true;
         }
      } else {
         return false;
      }
   }

   boolean shutdownSequence(S var1) {
      if (var1 != null) {
         SequenceTimerInfo var2;
         try {
            this._seqIdToTimerInfoMapLock.writeLock().lock();
            var2 = (SequenceTimerInfo)this._seqIdToTimerInfoMap.remove(var1.getId());
         } finally {
            this._seqIdToTimerInfoMapLock.writeLock().unlock();
         }

         if (var2 != null) {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Shutting down " + var1.getId());
            }

            var2.cancelAll();
         }
      }

      return super.shutdownSequence(var1);
   }

   protected void updated(S var1, S var2) {
      super.updated(var1, var2);

      SequenceTimerInfo var3;
      try {
         this._seqIdToTimerInfoMapLock.writeLock().lock();
         var3 = (SequenceTimerInfo)this._seqIdToTimerInfoMap.get(var1.getId());
      } finally {
         this._seqIdToTimerInfoMapLock.writeLock().unlock();
      }

      if (var3 != null) {
         var3.resetIdleTimer();
      }

   }

   protected void removing(S var1) {
      if (var1.getState() != SequenceState.TERMINATING && var1.getState() != SequenceState.TERMINATED) {
         this.timedSequenceForciblyExpired(var1);
      }

      super.removing(var1);
   }

   private void timedSequenceForciblyExpired(S var1) {
      SequenceTimerInfo var2;
      try {
         this._seqIdToTimerInfoMapLock.writeLock().lock();
         var2 = (SequenceTimerInfo)this._seqIdToTimerInfoMap.get(var1.getId());
      } finally {
         this._seqIdToTimerInfoMapLock.writeLock().unlock();
      }

      if (var2 != null) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Forcibly expiring " + var1);
         }

         try {
            var2._listener.sequenceExpiration(var1.getId());
         } catch (Exception var7) {
            WseeRmLogger.logUnexpectedException(var7.toString(), var7);
         }
      }

   }

   static {
      TimerManagerFactory var0 = TimerManagerFactory.getTimerManagerFactory();
      _timerMgr = var0.getDefaultTimerManager();
   }

   private class SequenceTimerInfo {
      private S _seq;
      private SequenceTimerListener _listener;
      private Timer _lifeTimer;
      private Timer _idleTimer;

      public SequenceTimerInfo(S var2, SequenceTimerListener var3) {
         this._seq = var2;
         this._listener = var3;
      }

      public synchronized void startAll() {
         this.setLifeTimer();
         this.resetIdleTimer();
      }

      private void setLifeTimer() {
         if (this._lifeTimer != null) {
            throw new IllegalStateException("Cannot reset a lifetime timer");
         } else {
            long var1 = this._seq.getExpires().getTimeInMillis(new Date(this._seq.getTimestamp())) + this._seq.getTimestamp();
            var1 -= System.currentTimeMillis();
            if (var1 < 0L) {
               this.cancelAll();
               this._listener.sequenceExpiration(this._seq.getId());
            } else {
               this._lifeTimer = TimedSequenceMap._timerMgr.schedule(new TimerListener() {
                  public void timerExpired(Timer var1) {
                     SequenceTimerInfo.this.cancelAll();
                     SequenceTimerInfo.this._listener.sequenceExpiration(SequenceTimerInfo.this._seq.getId());
                  }
               }, var1);
            }

         }
      }

      public synchronized void resetIdleTimer() {
         if (this._idleTimer != null) {
            this._idleTimer.cancel();
         }

         if (this._seq.getIdleTimeout() != null) {
            long var1 = this._seq.getIdleTimeout().getTimeInMillis(new Date(this._seq.getLastActivityTime())) + this._seq.getLastActivityTime() - System.currentTimeMillis();
            if (var1 < 0L) {
               this.cancelAll();
               this._listener.idleTimeout(this._seq.getId());
            } else {
               this._idleTimer = TimedSequenceMap._timerMgr.schedule(new TimerListener() {
                  public void timerExpired(Timer var1) {
                     SequenceTimerInfo.this.cancelAll();
                     SequenceTimerInfo.this._listener.idleTimeout(SequenceTimerInfo.this._seq.getId());
                  }
               }, var1);
            }

         }
      }

      public synchronized void cancelAll() {
         if (this._lifeTimer != null) {
            this._lifeTimer.cancel();
            this._lifeTimer = null;
         }

         if (this._idleTimer != null) {
            this._idleTimer.cancel();
            this._idleTimer = null;
         }

      }
   }

   public interface SequenceTimerListener {
      void sequenceExpiration(String var1);

      void idleTimeout(String var1);
   }
}
