package weblogic.logging;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.LogRecord;
import weblogic.kernel.KernelLogManager;
import weblogic.work.WorkAdapter;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerFactory;

public class LoggingOutputStream extends OutputStream {
   private static final NoOpRunnable NOOP_TASK = new NoOpRunnable();
   static final String STDOUT = "Stdout";
   static final String STDERR = "StdErr";
   private static final int MAX_LIMIT = 10;
   private ByteArrayOutputStream buffer;
   private String logStream;
   private WLLevel level;
   private boolean async;
   private WorkManager workManager;
   private ConcurrentLinkedQueue<LogRecord> logRecordQueue;
   private AtomicInteger queueSize;

   public LoggingOutputStream(String var1, WLLevel var2, boolean var3) {
      this.async = false;
      this.workManager = WorkManagerFactory.getInstance().find("weblogic.admin.RMI");
      this.logRecordQueue = new ConcurrentLinkedQueue();
      this.queueSize = new AtomicInteger();
      this.buffer = new ByteArrayOutputStream();
      this.logStream = var1;
      this.level = var2;
      this.async = var3;
   }

   public LoggingOutputStream(String var1, WLLevel var2) {
      this(var1, var2, false);
   }

   public void write(int var1) {
      this.buffer.write(var1);
   }

   public void flush() {
      String var1 = this.buffer.toString().trim();
      if (var1.length() != 0) {
         WLLogRecord var2 = new WLLogRecord(this.level, var1);
         var2.setLoggerName(this.logStream);
         if (this.async) {
            int var3 = this.queueSize.get();
            if (var3 < 10) {
               this.logRecordQueue.add(var2);
               this.queueSize.incrementAndGet();
               this.logAsync(var2);
            }
         } else {
            KernelLogManager.getLogger().log(var2);
         }

         this.buffer.reset();
      }
   }

   private void logAsync(WLLogRecord var1) {
      this.workManager.schedule(new WorkAdapter() {
         public Runnable cancel(String var1) {
            return LoggingOutputStream.NOOP_TASK;
         }

         public Runnable overloadAction(String var1) {
            return LoggingOutputStream.NOOP_TASK;
         }

         public void run() {
            LogRecord var1 = null;

            while((var1 = (LogRecord)LoggingOutputStream.this.logRecordQueue.poll()) != null) {
               LoggingOutputStream.this.queueSize.decrementAndGet();
               KernelLogManager.getLogger().log(var1);
            }

         }
      });
   }

   private static class NoOpRunnable implements Runnable {
      private NoOpRunnable() {
      }

      public void run() {
      }

      // $FF: synthetic method
      NoOpRunnable(Object var1) {
         this();
      }
   }
}
