package weblogic.security.SSL.jsseadapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.IllegalBlockingModeException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.text.MessageFormat;
import java.util.logging.Level;

final class JaChannelInputStream extends InputStream {
   private final ReadableByteChannel readableChannel;
   private AbstractSelectableChannel selectableChannel;
   private Selector selector;
   private final Object selectorLock = new Object();

   public int read() throws IOException {
      byte[] var1 = new byte[1];
      int var2 = this.read(var1, 0, 1);
      if (var2 < 0) {
         return -1;
      } else if (0 == var2) {
         throw new IllegalBlockingModeException();
      } else {
         return var1[0];
      }
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      if (null == var1) {
         throw new NullPointerException("Expected non-null array.");
      } else if (var2 >= 0 && var3 >= 0 && var2 + var3 <= var1.length) {
         if (0 == var3) {
            return 0;
         } else {
            ByteBuffer var6 = ByteBuffer.wrap(var1);
            var6.position(var2);
            var6.limit(var2 + var3);
            int var5 = this.readableChannel.read(var6);
            if (JaLogger.isLoggable(Level.FINEST)) {
               JaLogger.log(Level.FINEST, JaLogger.Component.SSLSOCKET, "{0}[{1}]: Given buffer length {2} bytes, actually read {3} bytes", this.getClass().getName(), this.hashCode(), var3, var5);
            }

            return var5;
         }
      } else {
         String var4 = MessageFormat.format("Offset={0}, Length={1}, ArrayLength={2}", var2, var3, var1.length);
         throw new IndexOutOfBoundsException(var4);
      }
   }

   public int available() throws IOException {
      if (this.readableChannel instanceof JaApplicationReadableByteChannel) {
         JaApplicationReadableByteChannel var4 = (JaApplicationReadableByteChannel)this.readableChannel;
         return var4.available();
      } else {
         if (this.readableChannel instanceof SocketChannel) {
            SocketChannel var1 = (SocketChannel)this.readableChannel;
            Socket var2 = var1.socket();
            if (null != var2) {
               InputStream var3 = var2.getInputStream();
               if (null != var3) {
                  return var3.available();
               }
            }
         }

         return 0;
      }
   }

   public void close() throws IOException {
      this.readableChannel.close();
   }

   protected Object clone() throws CloneNotSupportedException {
      throw new CloneNotSupportedException();
   }

   JaChannelInputStream(ReadableByteChannel var1) {
      if (null == var1) {
         throw new IllegalArgumentException("Expected non-null ReadableByteChannel.");
      } else {
         this.readableChannel = var1;
      }
   }

   void setSelectableChannel(AbstractSelectableChannel var1) {
      if (null == var1) {
         throw new IllegalArgumentException("Expected non-null AbstractSelectableChannel.");
      } else {
         this.selectableChannel = var1;
      }
   }
}
