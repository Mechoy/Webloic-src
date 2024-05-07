package weblogic.security.SSL.jsseadapter;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.text.MessageFormat;
import java.util.logging.Level;

final class JaApplicationOutputStream extends OutputStream {
   private final JaAbstractSSLSocket socket;

   public void write(int var1) throws IOException {
      byte[] var2 = new byte[]{(byte)var1};
      this.write(var2, 0, 1);
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      if (null == var1) {
         throw new NullPointerException("Expected non-null array.");
      } else if (var2 >= 0 && var3 >= 0 && var2 + var3 <= var1.length) {
         if (JaLogger.isLoggable(Level.FINEST)) {
            JaLogger.log(Level.FINEST, JaLogger.Component.SSLSOCKET, "{0}[{1}]: Given {2} bytes to write.", this.getClass().getName(), this.hashCode(), var3);
         }

         if (0 != var3) {
            ByteBuffer var11 = ByteBuffer.wrap(var1, var2, var3);
            this.socket.getSslEngineRunnerContext().getSync().lock(JaSSLEngineSynchronizer.LockState.OUTBOUND);

            int var5;
            try {
               WritableByteChannel var6 = this.socket.getWritableByteChannel();
               int var7 = var6.write(var11);
               var5 = var7;
               if (var11.hasRemaining()) {
                  if (JaLogger.isLoggable(Level.FINEST)) {
                     JaLogger.log(Level.FINEST, JaLogger.Component.SSLSOCKET, "{0}[{1}]: Last write(1): Written bytes={2}, Remaining bytes={3}.", this.getClass().getName(), this.hashCode(), var7, var11.remaining());
                  }

                  while(var11.hasRemaining()) {
                     Thread.currentThread();
                     Thread.yield();
                     var7 = var6.write(var11);
                     var5 += var7;
                     if (JaLogger.isLoggable(Level.FINEST)) {
                        JaLogger.log(Level.FINEST, JaLogger.Component.SSLSOCKET, "{0}[{1}]: Last write(2): Written bytes={2}, Remaining bytes={3}.", this.getClass().getName(), this.hashCode(), var7, var11.remaining());
                     }
                  }
               }
            } finally {
               this.socket.getSslEngineRunnerContext().getSync().unlock();
            }

            if (JaLogger.isLoggable(Level.FINEST)) {
               JaLogger.log(Level.FINEST, JaLogger.Component.SSLSOCKET, "{0}[{1}]: All bytes written: Total written bytes={2}, Remaining bytes={3}", this.getClass().getName(), this.hashCode(), var5, var11.remaining());
            }

         }
      } else {
         String var4 = MessageFormat.format("Offset={0}, Length={1}, ArrayLength={2}", var2, var3, var1.length);
         throw new IndexOutOfBoundsException(var4);
      }
   }

   public void flush() throws IOException {
   }

   public void close() throws IOException {
      if (JaLogger.isLoggable(Level.FINEST)) {
         JaLogger.log(Level.FINEST, JaLogger.Component.SSLSOCKET, "{0}[{1}]: close() called.", this.getClass().getName(), this.hashCode());
      }

      this.socket.shutdownOutput();
   }

   JaApplicationOutputStream(JaAbstractSSLSocket var1) {
      if (null == var1) {
         throw new IllegalArgumentException("Expected non-null JaAbstractSSLSocket.");
      } else {
         this.socket = var1;
      }
   }
}
