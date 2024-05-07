package weblogic.security.SSL.jsseadapter;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.logging.Level;

final class JaApplicationReadableByteChannel implements ReadableByteChannel {
   private final JaSSLEngineRunner.Context sslEngineRunnerContext;

   public int read(ByteBuffer var1) throws IOException {
      if (null == var1) {
         throw new IllegalArgumentException("Non-null destination expected.");
      } else if (var1.remaining() <= 0) {
         return 0;
      } else {
         try {
            this.sslEngineRunnerContext.getSync().lock(JaSSLEngineSynchronizer.LockState.INBOUND);

            int var5;
            try {
               ByteBuffer var2 = this.sslEngineRunnerContext.getBufferAppIn();
               int var3 = calcRemainingToRead(var2);
               if (var3 <= 0) {
                  JaSSLEngineRunner.RunnerResult var4 = JaSSLEngineRunner.unwrap(this.sslEngineRunnerContext);
                  if (var4 == JaSSLEngineRunner.RunnerResult.CLOSED) {
                     byte var31 = -1;
                     return var31;
                  }

                  var2 = this.sslEngineRunnerContext.getBufferAppIn();
                  var3 = calcRemainingToRead(var2);
               }

               int var30 = 0;
               if (var3 > 0) {
                  try {
                     var2.flip();
                     var5 = var2.remaining() - var1.remaining();
                     if (var5 <= 0) {
                        var30 = var2.remaining();
                        var1.put(var2);
                     } else {
                        int var6 = var2.limit();
                        var2.limit(var6 - var5);
                        var30 = var2.remaining();
                        var1.put(var2);
                        var2.limit(var6);
                     }
                  } finally {
                     var2.compact();
                  }
               }

               var5 = var30;
            } finally {
               this.sslEngineRunnerContext.getSync().unlock();
            }

            return var5;
         } catch (InterruptedIOException var27) {
            throw var27;
         } catch (IOException var28) {
            try {
               JaSSLEngineRunner.closeOutbound(this.sslEngineRunnerContext);
            } catch (Exception var23) {
            }

            throw var28;
         } catch (RuntimeException var29) {
            try {
               JaSSLEngineRunner.closeOutbound(this.sslEngineRunnerContext);
            } catch (Exception var24) {
            }

            throw var29;
         }
      }
   }

   public void close() throws IOException {
      if (JaLogger.isLoggable(Level.FINEST)) {
         JaLogger.log(Level.FINEST, JaLogger.Component.SSLENGINE, "[{0}] close() called.", this.getClass().getName());
      }

      try {
         JaSSLEngineRunner.RunnerResult var1 = JaSSLEngineRunner.closeInbound(this.sslEngineRunnerContext, false);
      } catch (Exception var3) {
         if (JaLogger.isLoggable(Level.FINER)) {
            JaLogger.log(Level.FINER, JaLogger.Component.SSLENGINE, var3, "[{0}] Exception occurred during close().", this.getClass().getName());
         }
      }

   }

   public boolean isOpen() {
      if (JaLogger.isLoggable(Level.FINEST)) {
         JaLogger.log(Level.FINEST, JaLogger.Component.SSLENGINE, "[{0}] isOpen() called.", this.getClass().getName());
      }

      return !JaSSLEngineRunner.isInboundDone(this.sslEngineRunnerContext);
   }

   public int available() throws IOException {
      this.sslEngineRunnerContext.getSync().lock(JaSSLEngineSynchronizer.LockState.INBOUND);

      int var2;
      try {
         ByteBuffer var1 = this.sslEngineRunnerContext.getBufferAppIn();
         var2 = calcRemainingToRead(var1);
      } finally {
         this.sslEngineRunnerContext.getSync().unlock();
      }

      return var2;
   }

   JaApplicationReadableByteChannel(JaSSLEngineRunner.Context var1) {
      if (null == var1) {
         throw new IllegalArgumentException("Expected non-null JaSSLEngineRunner.Context.");
      } else {
         this.sslEngineRunnerContext = var1;
      }
   }

   private static int calcRemainingToRead(ByteBuffer var0) {
      boolean var1 = false;

      int var5;
      try {
         var0.flip();
         var5 = var0.remaining();
      } finally {
         var0.compact();
      }

      return var5;
   }
}
