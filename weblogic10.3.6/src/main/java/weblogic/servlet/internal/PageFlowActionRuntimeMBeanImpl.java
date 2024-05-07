package weblogic.servlet.internal;

import java.util.LinkedList;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.ManagementException;
import weblogic.management.runtime.PageFlowActionRuntimeMBean;
import weblogic.management.runtime.PageFlowError;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;

public final class PageFlowActionRuntimeMBeanImpl extends RuntimeMBeanDelegate implements PageFlowActionRuntimeMBean {
   private static final long serialVersionUID = 1L;
   private String _actionName;
   private long _successCount;
   private long _exceptionCount;
   private long _handledExceptionCount;
   private long _totalSuccessDispatchTime;
   private long _minSuccessDispatchTime;
   private long _maxSuccessDispatchTime;
   private long _totalHandledExceptionDispatchTime;
   private long _minHandledExceptionDispatchTime;
   private long _maxHandledExceptionDispatchTime;
   private int _numExceptionsToKeep;
   private LinkedList _exceptionList;
   private DebugLogger _logger;
   private static int MAX_NUM_EXCEPTIONS = 5;

   public PageFlowActionRuntimeMBeanImpl(String var1, RuntimeMBean var2) throws ManagementException {
      super(var1, var2);
      this._actionName = var1;
      this._exceptionList = new LinkedList();
      this._numExceptionsToKeep = MAX_NUM_EXCEPTIONS;
      this._logger = PageFlowsRuntimeMBeanImpl.logger;
   }

   public void reset() {
      synchronized(this) {
         this._successCount = 0L;
         this._exceptionCount = 0L;
         this._handledExceptionCount = 0L;
         this._totalSuccessDispatchTime = 0L;
         this._minSuccessDispatchTime = 0L;
         this._maxSuccessDispatchTime = 0L;
         this._totalHandledExceptionDispatchTime = 0L;
         this._minHandledExceptionDispatchTime = 0L;
         this._maxHandledExceptionDispatchTime = 0L;
         this._exceptionList.clear();
      }
   }

   public String getActionName() {
      return this._actionName;
   }

   public long getSuccessCount() {
      return this._successCount;
   }

   public long getExceptionCount() {
      return this._exceptionCount;
   }

   public long getHandledExceptionCount() {
      return this._handledExceptionCount;
   }

   public long getSuccessDispatchTimeTotal() {
      return this._totalSuccessDispatchTime;
   }

   public long getSuccessDispatchTimeHigh() {
      return this._maxSuccessDispatchTime;
   }

   public long getSuccessDispatchTimeLow() {
      return this._minSuccessDispatchTime;
   }

   public long getSuccessDispatchTimeAverage() {
      return this._successCount > 0L ? this._totalSuccessDispatchTime / this._successCount : 0L;
   }

   public long getHandledExceptionDispatchTimeTotal() {
      return this._totalHandledExceptionDispatchTime;
   }

   public long getHandledExceptionDispatchTimeHigh() {
      return this._maxHandledExceptionDispatchTime;
   }

   public long getHandledExceptionDispatchTimeLow() {
      return this._minHandledExceptionDispatchTime;
   }

   public long getHandledExceptionDispatchTimeAverage() {
      return this._handledExceptionCount > 0L ? this._totalHandledExceptionDispatchTime / this._handledExceptionCount : 0L;
   }

   void reportSuccess(long var1) {
      ++this._successCount;
      this._totalSuccessDispatchTime += var1;
      if (var1 < this._minSuccessDispatchTime) {
         this._minSuccessDispatchTime = var1;
      } else if (var1 > this._maxSuccessDispatchTime) {
         this._maxSuccessDispatchTime = var1;
         if (this._minSuccessDispatchTime == 0L) {
            this._minSuccessDispatchTime = var1;
         }
      }

   }

   void reportException(PageFlowErrorImpl var1) {
      ++this._exceptionCount;
      this.enqueueException(var1);
   }

   void reportHandledException(Throwable var1, long var2) {
      ++this._handledExceptionCount;
      this._totalHandledExceptionDispatchTime += var2;
      if (this._minHandledExceptionDispatchTime == 0L) {
         this._minHandledExceptionDispatchTime = var2;
      } else if (var2 < this._minHandledExceptionDispatchTime) {
         this._minHandledExceptionDispatchTime = var2;
      }

      if (var2 > this._maxHandledExceptionDispatchTime) {
         this._maxHandledExceptionDispatchTime = var2;
      }

   }

   public PageFlowError[] getLastExceptions() {
      synchronized(this._exceptionList) {
         PageFlowError[] var2 = new PageFlowError[this._exceptionList.size()];
         this._exceptionList.toArray(var2);
         return var2;
      }
   }

   void setNumExceptionsToKeep(int var1) {
      this._numExceptionsToKeep = var1;
      if (var1 == 0) {
         this._exceptionList.clear();
      }

   }

   private void enqueueException(PageFlowErrorImpl var1) {
      if (var1 != null && this._numExceptionsToKeep > 0) {
         synchronized(this._exceptionList) {
            while(this._exceptionList.size() >= this._numExceptionsToKeep) {
               this._exceptionList.removeFirst();
            }

            this._exceptionList.addLast(var1);
         }
      }

   }
}
