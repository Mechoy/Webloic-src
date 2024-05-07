package weblogic.cache.management;

import java.util.Date;
import weblogic.management.ManagementException;
import weblogic.management.runtime.CacheMonitorRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;

public class CacheMonitorRuntimeMBeanImpl extends RuntimeMBeanDelegate implements CacheMonitorRuntimeMBean {
   private static final boolean debug = true;
   private static final boolean verbose = true;
   private long CurrentTotalEntries = 0L;
   private long AccessCount = 0L;
   private long HitCount = 0L;
   private long FlushesCount = 0L;
   private long InsertCount = 0L;
   private long CurrentSize = 0L;
   private long AccessTime = 0L;
   private long InsertTime = 0L;
   private Date TimeCreated = null;
   private long TimeSinceStart = 0L;

   public CacheMonitorRuntimeMBeanImpl(String var1, RuntimeMBean var2) throws ManagementException {
      super(var1, var2);
   }

   public long getAccessCount() {
      return this.AccessCount;
   }

   public long getHitCount() {
      return this.HitCount;
   }

   public long getCurrentTotalEntries() {
      return this.CurrentTotalEntries;
   }

   public long getFlushesCount() {
      return this.FlushesCount;
   }

   public long getInsertCount() {
      return this.InsertCount;
   }

   public long getCurrentSize() {
      return this.CurrentSize;
   }

   public long getAccessTime() {
      return this.AccessTime;
   }

   public long getInsertTime() {
      return this.InsertTime;
   }

   public Date getTimeCreated() {
      return this.TimeCreated;
   }

   public long getTimeSinceStart() {
      return this.TimeSinceStart;
   }

   public void incrementAccessCount() {
      ++this.AccessCount;
   }

   public void incrementHitCount() {
      ++this.HitCount;
   }

   public void changeCurrentTotalEntries(int var1, boolean var2) {
      if (var2) {
         this.CurrentTotalEntries += (long)var1;
      } else {
         this.CurrentTotalEntries -= (long)var1;
      }

   }

   public void incrementFlushesCount() {
      ++this.FlushesCount;
   }

   public void incrementInsertCount() {
      ++this.InsertCount;
   }

   public void changeCurrentSize(int var1, boolean var2) {
      if (var2) {
         this.CurrentSize += (long)var1;
      } else {
         this.CurrentSize -= (long)var1;
      }

   }

   public void changeInsertTime(long var1) {
      this.InsertTime += var1;
   }

   public void changeAccessTime(long var1) {
      this.AccessTime += var1;
   }

   public void setTimeCreated(Date var1) {
      this.TimeCreated = var1;
   }

   public static void main(String[] var0) {
   }
}
