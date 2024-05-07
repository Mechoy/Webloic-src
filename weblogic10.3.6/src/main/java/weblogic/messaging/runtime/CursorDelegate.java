package weblogic.messaging.runtime;

import javax.management.openmbean.CompositeData;
import javax.management.openmbean.OpenDataException;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManager;
import weblogic.timers.TimerManagerFactory;

public abstract class CursorDelegate implements TimerListener {
   protected String handle;
   protected CursorRuntimeImpl runtimeDelegate;
   protected OpenDataConverter openDataConverter;
   protected long startPosition;
   protected long endPosition;
   private int timeout;
   private long lastAccessTime;
   private static long counter;
   private TimerManager timerManager;

   public CursorDelegate(CursorRuntimeImpl var1, OpenDataConverter var2, int var3) {
      this.runtimeDelegate = var1;
      this.openDataConverter = var2;
      this.timeout = var3;
      this.timerManager = TimerManagerFactory.getTimerManagerFactory().getDefaultTimerManager();
      if (var3 > 0) {
         this.timerManager.schedule(this, (long)var3 * 1000L);
      }

   }

   public Long getCursorStartPosition() {
      this.updateAccessTime();
      return new Long(this.startPosition);
   }

   public Long getCursorEndPosition() {
      this.updateAccessTime();
      return new Long(this.endPosition);
   }

   public abstract CompositeData[] getNext(int var1) throws OpenDataException;

   public abstract CompositeData[] getPrevious(int var1) throws OpenDataException;

   public abstract CompositeData[] getItems(long var1, int var3) throws OpenDataException;

   public abstract Long getCursorSize();

   public void close() {
      this.runtimeDelegate.removeCursorDelegate(this.handle);
   }

   protected void setHandle(String var1) {
      this.handle = var1;
   }

   public String getHandle() {
      return this.handle;
   }

   protected synchronized void updateAccessTime() {
      this.lastAccessTime = System.currentTimeMillis();
   }

   public void timerExpired(Timer var1) {
      long var2 = System.currentTimeMillis();
      if (var2 > this.lastAccessTime + (long)this.timeout) {
         this.close();
      } else {
         long var4 = (long)this.timeout - (var2 - this.lastAccessTime);
         this.timerManager.schedule(this, var4);
      }

   }
}
