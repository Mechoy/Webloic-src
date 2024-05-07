package weblogic.jms.utils.tracing;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import weblogic.jms.utils.Simple;
import weblogic.utils.time.Timer;

public class DataLog {
   private static Object lock = new Object();
   private static int maxCount = 6291456;
   private static boolean beenHere = false;
   private static RecordFile recordFile;
   private static final int RECORD_SIZE = 9;
   private static String base = "/tmp/timer_";
   private static boolean initDone = false;
   private static Timer timer;

   public static void init(int var0) {
      if (!initDone) {
         initDone = true;
         maxCount = var0;
         init();
      }

   }

   public static void init() {
      if (maxCount != 0) {
         timer = PreciseTimerFactory.newTimer();
      }

      recordFile = new RecordFile(base + Math.abs((new Random()).nextInt() % 10000), maxCount);
   }

   public static void addToHeader(String var0) {
      if (!beenHere) {
         synchronized(lock) {
            if (!beenHere) {
               init();
               beenHere = true;
            }
         }
      }

      recordFile.addToHeader(var0);
   }

   public static void record(int var0, int var1) {
      if (!beenHere) {
         synchronized(lock) {
            if (!beenHere) {
               init();
               beenHere = true;
            }
         }
      }

      recordFile.record(var0, var1);
   }

   public static SubBuffer newDataArea(String var0, int var1) {
      if (!beenHere) {
         synchronized(lock) {
            if (!beenHere) {
               init();
               beenHere = true;
            }
         }
      }

      return recordFile.newDataArea(var0, var1);
   }

   public static void main(String[] var0) {
      for(int var1 = 0; var1 < 100000; ++var1) {
         record(0, 1234);
         record(1, 1234);
         record(2, 1234);
         record(3, 1234);
         record(4, 1234);
         record(5, 1234);
         record(6, 1234);
      }

   }

   static {
      String var0 = Simple.getenv("weblogic.jms.PerformanceMaxCount");
      if (var0 != null) {
         maxCount = Integer.decode(var0);
      }

      var0 = Simple.getenv("weblogic.jms.PerformanceFileBase");
      if (var0 != null) {
         base = var0;
      }

   }

   private static class RecordFile {
      private MappedByteBuffer buffer;
      private AtomicInteger position = new AtomicInteger(40960);
      private AtomicInteger headerPosition = new AtomicInteger();
      private int maxPosition;
      private long baseTime;

      RecordFile(String var1, int var2) {
         this.maxPosition = var2 * 9 + 'ꀀ';
         File var3 = new File(var1 + ".out");

         int var6;
         try {
            RandomAccessFile var4 = new RandomAccessFile(var3, "rw");
            byte[] var5 = new byte[1024];

            for(var6 = 0; var6 < this.maxPosition; var6 += 1024) {
               var4.write(var5);
            }

            FileChannel var11 = var4.getChannel();
            this.buffer = var11.map(MapMode.READ_WRITE, 0L, (long)this.maxPosition);
         } catch (Exception var9) {
            System.err.println("Failed!: " + var9);
            var9.printStackTrace(System.err);
            System.exit(1);
         }

         if (var2 != 0) {
            long var10 = (new Date()).getTime();

            for(var6 = 0; var6 < 2; ++var6) {
               long var7;
               do {
                  var7 = (new Date()).getTime();
                  this.baseTime = DataLog.timer.timestamp() / 1000L;
               } while(var7 == var10);

               var10 = var7;
            }

            System.out.println("Base time is " + this.baseTime);
            this.buffer.putLong(var10 * 1000L);
         }

         this.headerPosition.addAndGet(8);
      }

      void addToHeader(String var1) {
         char[] var2 = (var1 + "\n").toCharArray();
         int var3 = this.headerPosition.addAndGet(2 + var2.length * 2);
         int var4 = var3 - 2 - var2.length * 2;
         this.buffer.putShort(var4, (short)(var2.length * 2));
         var4 += 2;

         for(int var5 = 0; var5 < var2.length; ++var5) {
            this.buffer.putChar(var4, var2[var5]);
            var4 += 2;
         }

      }

      void record(int var1, int var2) {
         int var3 = this.position.addAndGet(9);
         if (var3 <= this.maxPosition) {
            int var4 = var3 - 9;
            if (var1 == 128) {
               var1 = 127;
            } else {
               var1 -= 128;
            }

            this.buffer.put(var4++, (byte)var1);
            long var5 = DataLog.timer.timestamp() / 1000L - this.baseTime;
            this.buffer.put(var4++, (byte)((int)((var5 & 255L) - 128L)));
            var5 >>= 8;
            this.buffer.put(var4++, (byte)((int)((var5 & 255L) - 128L)));
            var5 >>= 8;
            this.buffer.put(var4++, (byte)((int)((var5 & 255L) - 128L)));
            var5 >>= 8;
            this.buffer.put(var4++, (byte)((int)((var5 & 255L) - 128L)));
            this.buffer.putInt(var4, var2);
         }

      }

      public SubBuffer newDataArea(String var1, int var2) {
         char[] var3 = (var1 + "\n").toCharArray();
         int var4 = this.headerPosition.addAndGet(2 + var3.length * 2 + 2 + var2);
         int var5 = var4 - 2 - var3.length * 2 - 2 - var2;
         this.buffer.putShort(var5, (short)(var3.length * 2));
         var5 += 2;

         for(int var6 = 0; var6 < var3.length; ++var6) {
            this.buffer.putChar(var5, var3[var6]);
            var5 += 2;
         }

         this.buffer.putShort(var5, (short)var2);
         return new SubBuffer(this.buffer, var5 + 2, var2);
      }
   }
}
