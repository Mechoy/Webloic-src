package weblogic.security.SSL.jsseadapter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.WritableByteChannel;
import java.util.logging.Level;

final class JaApplicationWritableByteChannel implements WritableByteChannel {
   private final JaSSLEngineRunner.Context sslEngineRunnerContext;

   public int write(ByteBuffer var1) throws IOException {
      if (null == var1) {
         throw new IllegalArgumentException("Non-null source expected.");
      } else {
         int var2 = var1.remaining();
         if (var2 <= 0) {
            return 0;
         } else {
            this.sslEngineRunnerContext.getSync().lock(JaSSLEngineSynchronizer.LockState.OUTBOUND);

            try {
               this.sslEngineRunnerContext.setBufferAppOut(var1);

               try {
                  var1.compact();
                  JaSSLEngineRunner.RunnerResult var3 = JaSSLEngineRunner.wrap(this.sslEngineRunnerContext);
                  if (null != var3 && JaSSLEngineRunner.RunnerResult.CLOSED == var3) {
                     if (JaLogger.isLoggable(Level.FINER)) {
                        JaLogger.log(Level.FINER, JaLogger.Component.SSLSOCKET, "{0}[{1}]: Unable to write: SSL channel is closed.", this.getClass().getName(), this.hashCode());
                     }

                     throw new ClosedChannelException();
                  }
               } finally {
                  var1.flip();
                  this.sslEngineRunnerContext.setBufferAppOut((ByteBuffer)null);
               }
            } finally {
               this.sslEngineRunnerContext.getSync().unlock();
            }

            int var12 = var2 - var1.remaining();
            return var12;
         }
      }
   }

   public void close() throws IOException {
      if (JaLogger.isLoggable(Level.FINEST)) {
         JaLogger.log(Level.FINEST, JaLogger.Component.SSLENGINE, "[{0}] close() called.", this.getClass().getName());
      }

      try {
         JaSSLEngineRunner.RunnerResult var1 = JaSSLEngineRunner.closeOutbound(this.sslEngineRunnerContext);
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

      return !JaSSLEngineRunner.isOutboundDone(this.sslEngineRunnerContext);
   }

   JaApplicationWritableByteChannel(JaSSLEngineRunner.Context var1) {
      if (null == var1) {
         throw new IllegalArgumentException("Expected non-null JaSSLEngineRunner.Context.");
      } else {
         this.sslEngineRunnerContext = var1;
      }
   }
}
