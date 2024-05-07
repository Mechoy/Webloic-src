package weblogic.time.common.internal;

import weblogic.common.T3MiscLogger;
import weblogic.utils.AssertionError;
import weblogic.work.WorkManager;

public final class TimeTable extends TimeEvent {
   private long wakeupTime;
   private int executeCount;
   private int exceptionCount;
   private TimeEvent[] array;
   private int index;
   private long granularity;
   private TimeTable nextTable;

   public TimeTable() {
      this(100, 1000L, System.currentTimeMillis());
   }

   TimeTable(int var1, long var2, long var4) {
      this.executeCount = 0;
      this.exceptionCount = 0;
      this.index = 0;
      this.array = new TimeEvent[var1];
      this.granularity = var2;
      this.time = var4 / var2 * var2;
      this.next = this.prev = this;
      this.array[this.index] = this;
   }

   private final TimeEvent next() {
      TimeEvent var1 = this.next;
      this.next = var1.next;
      var1.next.prev = this;
      var1.prev = null;
      return var1;
   }

   public synchronized void insert(TimeEvent var1) {
      long var2 = var1.time;
      if (var2 < this.time) {
         var2 = var1.time = this.time;
      }

      int var4 = (int)((var2 - this.time) / this.granularity);
      long var5 = (var2 - this.time) / this.granularity;
      if (var5 > 2147483647L) {
         var4 = Integer.MAX_VALUE;
      }

      if (var4 < 0) {
         throw new AssertionError("Time for scheduled trigger out of bounds: " + var2);
      } else {
         if (var1.next != null) {
            var1.next.prev = null;
         }

         if (var1.prev != null) {
            var1.prev.next = null;
         }

         if (var4 >= this.array.length) {
            if (this.nextTable == null) {
               this.nextTable = new TimeTable(this.array.length, this.granularity * (long)this.array.length, this.time);
            }

            this.nextTable.insert(var1);
         } else {
            int var7 = (this.index + var4) % this.array.length;
            if (this.array[var7] != null) {
               if (var1.time < this.array[var7].time) {
                  this.array[var7].prev.insertAfter(var1);
                  this.array[var7] = var1;
               } else {
                  this.array[var7].insertAfter(var1);
               }
            } else {
               this.array[this.next(var7)].prev.insertAfter(var1);
               this.array[var7] = var1;
            }

            if (var2 < this.wakeupTime) {
               this.notify();
            }
         }

      }
   }

   private final int next(int var1) {
      do {
         ++var1;
         if (var1 == this.array.length) {
            var1 = 0;
         }
      } while(this.array[var1] == null);

      return var1;
   }

   public synchronized boolean delete(TimeEvent var1) {
      int var2 = (int)((var1.time - this.time) / this.granularity);
      long var3 = (var1.time - this.time) / this.granularity;
      if (var3 > 2147483647L) {
         var2 = Integer.MAX_VALUE;
      }

      if (var2 < 0) {
         return false;
      } else if (var2 >= this.array.length && this.nextTable != null) {
         return this.nextTable.delete(var1);
      } else {
         int var5 = (this.index + var2) % this.array.length;
         if (this.array[var5] == var1) {
            if (var1.next != null && (var1.next.time - this.time) / this.granularity == (long)var2) {
               this.array[var5] = var1.next;
            } else {
               this.array[var5] = null;
            }
         }

         var1.remove();
         return true;
      }
   }

   public void executeTimer(long var1, WorkManager var3, boolean var4) {
      throw new AssertionError("TimeTable executed as timer event");
   }

   public int execute(long var1, WorkManager var3, boolean var4) {
      TimeEvent var5 = null;
      TimeEvent var6 = null;
      int var7 = 0;
      synchronized(this) {
         this.revise_structures(var1);
         var5 = this.snip(this.time + this.granularity);
      }

      for(; var5 != null; var5 = var6) {
         try {
            var6 = var5.next;
            if (var5.next != null) {
               var5.next.prev = null;
               var5.next = null;
            }

            if (var5.prev != null) {
               var5.prev.next = null;
               var5.prev = null;
            }

            var5.executeTimer(var1, var3, var4);
            ++var7;
         } catch (Throwable var11) {
            Throwable var8 = var11;

            try {
               T3MiscLogger.logExecution(var5.toString(), var8);
            } catch (Exception var10) {
            }

            ++this.exceptionCount;
         }
      }

      this.executeCount += var7;
      return var7;
   }

   private final TimeEvent snip(long var1) {
      TimeEvent var3 = this.next;

      Object var4;
      for(var4 = this; ((TimeEvent)var4).next != this && ((TimeEvent)var4).next.time < var1; var4 = ((TimeEvent)var4).next) {
      }

      if (var4 == this) {
         return null;
      } else {
         this.next = ((TimeEvent)var4).next;
         ((TimeEvent)var4).next.prev = this;
         ((TimeEvent)var4).next = null;
         var3.prev = null;
         return var3;
      }
   }

   public synchronized void snooze() {
      long var1 = System.currentTimeMillis();
      this.wakeupTime = this.wakeupTime(var1);

      try {
         long var3 = this.wakeupTime - var1;
         if (var3 <= 0L) {
            throw new AssertionError("Illegal wait time: " + var3);
         }

         this.wait(var3);
      } catch (InterruptedException var5) {
      }

   }

   private long wakeupTime(long var1) {
      long var3;
      if (this.next != this && this.next != null) {
         var3 = this.next.time;
      } else {
         var3 = var1 + this.granularity * (long)this.array.length;
      }

      if (this.nextTable != null) {
         long var5 = this.nextTable.wakeupTime(var1);
         if (var5 < var3) {
            var3 = var5;
         }
      }

      if (var3 <= var1 + 1L) {
         var3 = var1 + 1L;
      }

      return var3;
   }

   private final boolean revise_structures(long var1) {
      int var3 = (int)((var1 - this.time) / this.granularity);
      if (var3 <= 0) {
         return false;
      } else {
         int var4 = this.array.length;

         for(this.time += (long)var3 * this.granularity; var3-- > 0; this.index = (this.index + 1) % var4) {
            this.array[this.index] = null;
         }

         this.array[this.index] = this;
         if (this.nextTable != null && this.nextTable.revise_structures(var1)) {
            this.nextTable.collect(this, this.time + this.granularity * (long)var4);
         }

         return true;
      }
   }

   private void collect(TimeTable var1, long var2) {
      while(this.next != this && this.next.time < var2) {
         TimeEvent var4 = this.next();
         int var5 = (int)((var4.time - this.time) / this.granularity);
         if (var5 > 0) {
            int var6 = (this.index + var5) % this.array.length;
            if (this.array[var6] == var4) {
               if (var4.next != this && (var4.next.time - this.time) / this.granularity == (long)var5) {
                  this.array[var6] = var4.next;
               } else {
                  this.array[var6] = null;
               }
            }
         }

         var4.next = null;
         var1.insert(var4);
      }

   }

   int executeCount() {
      return this.executeCount;
   }

   int exceptionCount() {
      return this.exceptionCount;
   }

   public synchronized int showState() {
      int var1;
      for(var1 = 0; var1 < this.array.length; ++var1) {
         System.out.print(this.array[var1] == this ? "v" : "-");
      }

      System.out.println(": " + this.timeSymbol());

      for(var1 = 0; var1 < this.array.length; ++var1) {
         System.out.print(this.array[var1] == null ? " " : "*");
      }

      System.out.println("");
      Object var3 = this;

      int var2;
      for(var2 = 0; ((TimeEvent)var3).next != this; ++var2) {
         if (((TimeEvent)var3).next.prev != var3) {
            throw new AssertionError("node.next.prev != node.next");
         }

         var3 = ((TimeEvent)var3).next;
      }

      System.out.println(this.timeSymbol() + " N=" + var2 + " triggers");
      if (this.nextTable != null) {
         var2 += this.nextTable.showState();
      }

      if (this.granularity == 1000L) {
         System.out.println("Total Trigger Count = " + var2);
      }

      return var2;
   }

   private String timeSymbol() {
      int var1 = 0;

      for(long var2 = this.granularity; var2 > 1000L; var2 /= (long)this.array.length) {
         ++var1;
      }

      return "S^" + var1;
   }

   private String timeDelta(long var1) {
      long var3 = var1 - System.currentTimeMillis();
      return var3 / 60000L + ":" + var3 % 60000L / 1000L;
   }
}
