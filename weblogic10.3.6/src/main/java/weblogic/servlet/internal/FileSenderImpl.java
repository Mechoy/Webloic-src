package weblogic.servlet.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import weblogic.servlet.FileSender;
import weblogic.socket.WeblogicSocket;
import weblogic.utils.io.Chunk;

public abstract class FileSenderImpl implements FileSender {
   protected final long maxMessageSize;
   protected long bytesWritten;
   protected ServletRequestImpl request;
   protected ServletResponseImpl response;

   private FileSenderImpl(ServletRequestImpl var1, ServletResponseImpl var2) {
      this.response = var2;
      this.request = var1;
      VirtualConnection var3 = var1.getConnection();
      this.maxMessageSize = (long)var3.getChannel().getMaxMessageSize();
      var2.setFileSender(this);
   }

   public long sendFile(File var1) throws IOException {
      if (!var1.exists()) {
         throw new FileNotFoundException(var1.getName());
      } else if (!var1.isFile()) {
         throw new IOException(var1.getName() + " is not a file");
      } else {
         FileChannel var2 = null;

         long var3;
         try {
            var2 = (new FileInputStream(var1)).getChannel();
            var3 = this.sendFile(var2, 0L, var1.length());
         } finally {
            if (var2 != null) {
               var2.close();
            }

         }

         return var3;
      }
   }

   public long getBytesSent() {
      return this.bytesWritten;
   }

   public abstract long sendFile(FileChannel var1, long var2, long var4) throws IOException;

   public abstract boolean usesServletOutputStream();

   public static FileSender getZeroCopyFileSender(HttpServletResponse var0) {
      if (var0 instanceof HttpServletResponseWrapper) {
         return null;
      } else {
         ServletResponseImpl var1 = (ServletResponseImpl)var0;
         ServletRequestImpl var2 = var1.getRequest();
         VirtualConnection var3 = var2.getConnection();
         Socket var4 = var3.getSocket();
         if (var4 instanceof WeblogicSocket) {
            var4 = ((WeblogicSocket)var4).getSocket();
         }

         if (var4.getChannel() == null) {
            return null;
         } else if (var2.getAttribute("javax.servlet.include.request_uri") != null) {
            return null;
         } else if (var1.getContext().getFilterManager().hasFilters()) {
            return null;
         } else {
            ZeroCopyFileSender var5 = (ZeroCopyFileSender)var1.getFileSender();
            return var5 != null ? var5 : new ZeroCopyFileSender(var2, var1);
         }
      }
   }

   public static FileSender getFileSender(HttpServletResponse var0) {
      FileSender var1 = getZeroCopyFileSender(var0);
      if (var1 != null) {
         return var1;
      } else {
         ServletResponseImpl var2 = (ServletResponseImpl)var0;
         ServletRequestImpl var3 = var2.getRequest();
         DefaultFileSender var4 = (DefaultFileSender)var2.getFileSender();
         return var4 != null ? var4 : new DefaultFileSender(var3, var2);
      }
   }

   // $FF: synthetic method
   FileSenderImpl(ServletRequestImpl var1, ServletResponseImpl var2, Object var3) {
      this(var1, var2);
   }

   private static final class ChunkHeaderMaker {
      private static final byte[] DIGITS = new byte[]{48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102};
      private static final int ARR_LEN = 18;

      public static byte[] getChunkHeader(long var0) {
         byte[] var2 = new byte[18];
         long var3 = -1152921504606846976L;
         long var5 = 60L;

         for(int var7 = 0; var7 < 18; ++var7) {
            var2[var7] = DIGITS[(int)((var0 & var3) >> (int)var5)];
            var3 = var3 >> 4 & 1152921504606846975L;
            var5 -= 4L;
         }

         var2[16] = 13;
         var2[17] = 10;
         return var2;
      }
   }

   private static class DefaultFileSender extends FileSenderImpl {
      public DefaultFileSender(ServletRequestImpl var1, ServletResponseImpl var2) {
         super(var1, var2, null);
      }

      public boolean usesServletOutputStream() {
         return true;
      }

      public long sendFile(FileChannel var1, long var2, long var4) throws IOException {
         var1.position(var2);
         int var6 = this.response.getBufferSize();
         var6 = var6 != -1 && var6 != 0 ? var6 : Chunk.CHUNK_SIZE;
         Object var7 = null;
         Chunk var8 = null;

         long var18;
         try {
            byte[] var19;
            if (var6 == Chunk.CHUNK_SIZE) {
               var8 = Chunk.getChunk();
               var19 = var8.buf;
            } else {
               var19 = new byte[var6];
            }

            ByteBuffer var9 = ByteBuffer.wrap(var19);

            assert var9.array() != null;

            long var10 = 0L;

            while(var10 != var4) {
               int var12 = var1.read(var9);
               long var13 = (long)(var12 + this.response.getServletOutputStream().getTotal() + this.response.getServletOutputStream().getCount());
               if (var13 > this.maxMessageSize) {
                  throw new IOException("Outgoing message size will exceed the configured maximum message size of " + this.maxMessageSize + " bytes");
               }

               if (var12 == 0 || var12 == -1) {
                  break;
               }

               this.response.getOutputStream().write(var9.array(), 0, var12);
               this.response.incrementBytesSentCount((long)var12);
               var10 += (long)var12;
               this.bytesWritten += (long)var12;
               var9.clear();
            }

            var18 = var10;
         } finally {
            if (var8 != null) {
               Chunk.releaseChunk(var8);
            }

         }

         return var18;
      }
   }

   private static class ZeroCopyFileSender extends FileSenderImpl {
      public ZeroCopyFileSender(ServletRequestImpl var1, ServletResponseImpl var2) {
         super(var1, var2, null);
      }

      public boolean usesServletOutputStream() {
         return false;
      }

      public long sendFile(FileChannel var1, long var2, long var4) throws IOException {
         this.response.flushBuffer();
         VirtualConnection var6 = this.request.getConnection();
         long var7 = (long)this.response.getServletOutputStream().getTotal();
         Socket var9 = var6.getSocket();
         if (var9 instanceof WeblogicSocket) {
            var9 = ((WeblogicSocket)var9).getSocket();
         }

         byte[] var10 = new byte[0];
         if ("HTTP/1.1".equals(this.request.getProtocol()) && this.response.getContentLength() == 0) {
            var10 = FileSenderImpl.ChunkHeaderMaker.getChunkHeader(var4);
            OutputStream var11 = var9.getOutputStream();
            var11.write(var10);
            var11.flush();
         }

         assert var9.getChannel() != null;

         long var13;
         long var19;
         for(var19 = 0L; var4 > var19; var2 += var13) {
            var13 = var4 - var19;
            long var15 = var13 > 2147483647L ? 2147483647L : var13;
            long var17 = var15 + this.bytesWritten + var7 + (long)var10.length;
            if (var17 > this.maxMessageSize) {
               throw new IOException("Outgoing message size will exceed the configured maximum message size of " + this.maxMessageSize + " bytes");
            }

            var13 = var1.transferTo(var2, var15, var9.getChannel());
            if (var13 == 0L) {
               break;
            }

            this.response.incrementBytesSentCount(var13);
            this.bytesWritten += var13;
            var19 += var13;
         }

         if ("HTTP/1.1".equals(this.request.getProtocol()) && this.response.getContentLength() == 0 && var19 != var4) {
            throw new IOException("Expected to send " + var4 + " bytes, but " + var19 + " bytes sent");
         } else {
            return var19;
         }
      }
   }
}
