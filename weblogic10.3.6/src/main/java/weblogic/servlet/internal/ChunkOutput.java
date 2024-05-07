package weblogic.servlet.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ProtocolException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import weblogic.utils.Debug;
import weblogic.utils.StringUtils;
import weblogic.utils.http.BytesToString;
import weblogic.utils.io.Chunk;

public class ChunkOutput {
   private static final boolean ASSERT = true;
   protected static final int CHUNK_HEADER_SIZE = 6;
   protected static final int CHUNK_TRAILER_SIZE = 2;
   protected static final int CHUNK_SIZE;
   protected static final int END_OFFSET;
   protected static final byte[] DIGITS;
   protected static final int UNLIMITED_BUFFER = -1;
   protected Chunk head;
   protected Chunk tail;
   protected int total;
   protected int count;
   protected int buflimit;
   protected OutputStream os;
   protected ServletOutputStreamImpl sos;
   protected boolean autoflush;
   protected boolean stickyBufferSize = false;
   protected boolean chunking;
   protected boolean alwaysFlush;
   protected boolean released;
   protected BufferFlushStrategy flushStrategy;
   private static CompleteMessageTimeoutTrigger trigger;

   private void p(String var1) {
      System.err.println("[" + super.toString() + "]: " + var1);
   }

   protected ChunkOutput(int var1, boolean var2, OutputStream var3, ServletOutputStreamImpl var4) {
      this.setBufferSize(var1);
      this.autoflush = var2;
      this.os = var3;
      this.count = this.total = 0;
      this.head = this.tail = Chunk.getChunk();
      this.head.end = 6;
      this.sos = var4;
      if (var3 != null && var4 == null) {
         this.alwaysFlush = true;
      }

      this.setBufferFlushStrategy();
   }

   protected ChunkOutput() {
   }

   protected ChunkOutput(ChunkOutput var1) {
      this.head = var1.head;
      this.tail = var1.tail;
      this.total = var1.total;
      this.count = var1.count;
      this.buflimit = var1.buflimit;
      this.os = var1.os;
      this.sos = var1.sos;
      this.autoflush = var1.autoflush;
      this.stickyBufferSize = var1.stickyBufferSize;
      this.chunking = var1.chunking;
      this.alwaysFlush = var1.alwaysFlush;
      var1.head = var1.tail = null;
      this.setBufferFlushStrategy();
   }

   public static ChunkOutput create(int var0, boolean var1, OutputStream var2, ServletOutputStreamImpl var3) {
      if (var2 == null && var0 != -1) {
         throw new IllegalArgumentException("cannot have no outputstream and a limited buffer size");
      } else {
         return (ChunkOutput)(ServletOutputStreamImpl.supportsGatheredWrites(var2) ? new GatheringChunkOutput(new ChunkOutput(var0, var1, var2, var3)) : new ChunkOutput(var0, var1, var2, var3));
      }
   }

   public static ChunkOutput create(ChunkOutput var0, String var1, CharsetMap var2) throws UnsupportedEncodingException {
      var0.flushBufferedDataToChunk();
      if (BytesToString.is8BitUnicodeSubset(var1)) {
         if (var0 instanceof GatheringChunkOutput) {
            GatheringChunkOutput var7 = (GatheringChunkOutput)var0;
            return new GatheringChunkOutput(var7, new ChunkOutput(var7.getDelegate()));
         } else {
            return new ChunkOutput(var0);
         }
      } else {
         String var3 = var2 != null ? var2.getJavaCharset(var1) : var1;
         if (!Charset.isSupported(var3)) {
            throw new UnsupportedEncodingException(var3);
         } else {
            Charset var4 = Charset.forName(var3);
            Object var5 = null;
            if (var0 instanceof GatheringChunkOutput) {
               GatheringChunkOutput var6 = (GatheringChunkOutput)var0;
               var5 = new GatheringChunkOutput(var6, new CharsetChunkOutput(var6.getDelegate(), var4));
               var6.getDelegate().head = var6.getDelegate().tail = null;
            } else {
               var5 = new CharsetChunkOutput(var0, var4);
               var0.head = var0.tail = null;
            }

            return (ChunkOutput)var5;
         }
      }
   }

   public static ChunkOutput create(int var0, boolean var1, OutputStream var2, ServletOutputStreamImpl var3, String var4, CharsetMap var5) throws UnsupportedEncodingException {
      if (BytesToString.is8BitUnicodeSubset(var4)) {
         return create(var0, var1, var2, var3);
      } else {
         String var6 = var5 != null ? var5.getJavaCharset(var4) : var4;
         if (!Charset.isSupported(var6)) {
            throw new UnsupportedEncodingException(var6);
         } else {
            Charset var7 = Charset.forName(var6);
            return (ChunkOutput)(ServletOutputStreamImpl.supportsGatheredWrites(var2) ? new GatheringChunkOutput(new CharsetChunkOutput(var0, var1, var2, var3, var7)) : new CharsetChunkOutput(var0, var1, var2, var3, var7));
         }
      }
   }

   public String getEncoding() {
      return null;
   }

   public void reset() {
      this.clearBuffer();
      this.count = this.total = 0;
   }

   public void release() {
      Chunk.releaseChunks(this.head);
      this.head = this.tail = null;
      this.released = true;
   }

   public Chunk getHead() {
      return this.head;
   }

   public int getTotal() {
      return this.total;
   }

   public int getCount() {
      return this.count;
   }

   public int getBufferSize() {
      return this.buflimit;
   }

   public void setBufferSize(int var1) {
      if (!this.stickyBufferSize) {
         if (var1 == -1) {
            this.buflimit = -1;
            this.setBufferFlushStrategy();
         } else {
            int var2 = var1 / CHUNK_SIZE;
            if (var1 % CHUNK_SIZE != 0) {
               ++var2;
            }

            this.buflimit = var2 * CHUNK_SIZE;
            this.setBufferFlushStrategy();
         }
      }
   }

   public void setStickyBufferSize(boolean var1) {
      this.stickyBufferSize = var1;
   }

   public boolean isAutoFlush() {
      return this.autoflush;
   }

   public void setAutoFlush(boolean var1) {
      if (this.os != null) {
         this.autoflush = var1;
      } else {
         this.autoflush = false;
      }

      this.setBufferFlushStrategy();
   }

   public boolean isChunking() {
      return this.chunking;
   }

   public void setChunking(boolean var1) {
      this.chunking = var1;
   }

   void writeByte(int var1) throws IOException {
      this.write(var1);
   }

   public void write(int var1) throws IOException {
      if (!this.released) {
         this.flushStrategy.checkOverflow(1);
         ++this.count;
         this.tail = ensureCapacity(this.tail);
         this.tail.buf[this.tail.end++] = (byte)var1;
         this.flushStrategy.checkForFlush();
      }
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      if (!this.released) {
         while(var3 > 0) {
            this.tail = ensureCapacity(this.tail);
            int var4 = Math.min(END_OFFSET - this.tail.end, var3);
            this.flushStrategy.checkOverflow(var4);
            System.arraycopy(var1, var2, this.tail.buf, this.tail.end, var4);
            this.count += var4;
            Chunk var10000 = this.tail;
            var10000.end += var4;
            var2 += var4;
            var3 -= var4;
            this.flushStrategy.checkForFlush();
         }

      }
   }

   public void write(ByteBuffer var1) throws IOException {
      if (var1.hasArray()) {
         this.write(var1.array(), var1.position(), var1.limit() - var1.position());
      } else {
         byte[] var2 = new byte[var1.limit() - var1.position()];
         var1.get(var2, 0, var2.length);
         this.write((byte[])var2, 0, var2.length);
      }

   }

   public void write(char[] var1, int var2, int var3) throws IOException {
      if (!this.released) {
         while(var3 > 0) {
            this.tail = ensureCapacity(this.tail);
            int var4 = Math.min(END_OFFSET - this.tail.end, var3);
            this.flushStrategy.checkOverflow(var4);

            for(int var5 = 0; var5 < var4; ++var5) {
               this.tail.buf[this.tail.end++] = (byte)var1[var2++];
            }

            this.count += var4;
            var3 -= var4;
            this.flushStrategy.checkForFlush();
         }

      }
   }

   public void print(String var1) throws IOException {
      if (!this.released) {
         if (var1 != null) {
            int var2 = var1.length();
            int var3 = 0;
            this.flushStrategy.checkOverflow(var2);

            while(var2 > 0) {
               this.tail = ensureCapacity(this.tail);
               int var4 = Math.min(var2, END_OFFSET - this.tail.end);
               StringUtils.getBytes(var1, var3, var3 + var4, this.tail.buf, this.tail.end);
               var3 += var4;
               Chunk var10000 = this.tail;
               var10000.end += var4;
               this.count += var4;
               var2 -= var4;
               this.flushStrategy.checkForFlush();
            }

         }
      }
   }

   public void println(String var1) throws IOException {
      this.print(var1);
      this.println();
   }

   public void println() throws IOException {
      this.print("\r\n");
   }

   public void commit() throws IOException {
      if (this.sos != null) {
         this.sos.commit();
      }

   }

   public void clearBuffer() {
      if (this.head != null) {
         Chunk.releaseChunks(this.head.next);
         this.tail = this.head;
         this.head.next = null;
         this.head.end = 6;
         this.count = 0;
      }
   }

   public void flush() throws IOException {
      if (!this.released) {
         if (this.sos != null && !this.sos.headersSent()) {
            this.sos.sendHeaders();
         }

         if (this.os != null) {
            int var1 = writeChunks(this.head, this.os, this.chunking);
            this.total += this.count;
            if (this.chunking) {
               this.total += 8 * var1;
            }

            this.clearBuffer();
         }

      }
   }

   void postFlush() {
   }

   public void writeStream(InputStream var1, int var2, int var3) throws IOException {
      if (!this.released) {
         if (var3 == -1 && var2 == -1) {
            this.writeStream(var1);
         } else {
            if (var2 == -1) {
               var2 = var3;
            }

            while(var2 > 0) {
               this.tail = ensureCapacity(this.tail);
               int var4 = Math.min(var2, END_OFFSET - this.tail.end);
               if (var3 != -1 && this.total + this.count + var4 > var3) {
                  throw new ProtocolException("Exceeded stated content length of " + var3);
               }

               this.flushStrategy.checkOverflow(var4);
               int var5 = var1.read(this.tail.buf, this.tail.end, var4);
               if (var5 == -1) {
                  throw new IOException("failed to read '" + var4 + "' bytes from InputStream; clen: " + var3 + " remaining: " + var2 + " count: " + this.count);
               }

               this.count += var5;
               Chunk var10000 = this.tail;
               var10000.end += var5;
               this.flushStrategy.checkForFlush();
               var2 -= var5;
            }

         }
      }
   }

   protected void flushBufferedDataToChunk() {
   }

   protected void setHttpHeaders(Chunk var1) {
   }

   private void writeStream(InputStream var1) throws IOException {
      while(true) {
         this.tail = ensureCapacity(this.tail);
         int var2 = END_OFFSET - this.tail.end;
         this.flushStrategy.checkOverflow(var2);
         int var3 = var1.read(this.tail.buf, this.tail.end, var2);
         if (var3 == -1) {
            return;
         }

         Chunk var10000 = this.tail;
         var10000.end += var3;
         this.count += var3;
         this.flushStrategy.checkForFlush();
      }
   }

   private static void writeChunkHeader(byte[] var0, int var1) {
      Debug.assertion(var1 <= CHUNK_SIZE);
      int var2 = 4;

      do {
         --var2;
         var0[var2] = DIGITS[var1 & 15];
         var1 >>>= 4;
      } while(var1 != 0);

      for(int var3 = 0; var3 < var2; ++var3) {
         var0[var3] = 48;
      }

      var0[4] = 13;
      var0[5] = 10;
   }

   protected static int writeChunks(Chunk var0, OutputStream var1, boolean var2) throws IOException {
      int var3;
      try {
         registerToTrigger(var1);
         if (!var2) {
            var3 = writeChunkNoTransfer(var0, var1);
            return var3;
         }

         var3 = writeChunkTransfer(var0, var1);
      } finally {
         unregisterFromTrigger(var1);
      }

      return var3;
   }

   protected static void registerToTrigger(OutputStream var0) {
      trigger.register(var0);
   }

   protected static void unregisterFromTrigger(OutputStream var0) {
      trigger.unregister(var0);
   }

   private static int writeChunkTransfer(Chunk var0, OutputStream var1) throws IOException {
      int var2 = 0;

      while(var0 != null) {
         int var3 = var0.end - 6;
         if (var3 <= 0) {
            var0 = var0.next;
         } else {
            writeChunkHeader(var0.buf, var3);
            var0.buf[var0.end] = 13;
            var0.buf[var0.end + 1] = 10;
            var1.write(var0.buf, 0, var0.end + 2);
            Debug.assertion(var0 != var0.next);
            var0 = var0.next;
            ++var2;
         }
      }

      return var2;
   }

   private static int writeChunkNoTransfer(Chunk var0, OutputStream var1) throws IOException {
      int var2;
      for(var2 = 0; var0 != null; ++var2) {
         int var4;
         for(int var3 = 6; var3 < var0.end; var3 += var4) {
            var4 = Math.min(Chunk.CHUNK_SIZE, var0.end - var3);
            Debug.assertion(var4 >= 0);
            var1.write(var0.buf, var3, var4);
         }

         var0 = var0.next;
      }

      return var2;
   }

   protected static Chunk ensureCapacity(Chunk var0) {
      if (var0.end == END_OFFSET) {
         var0.next = Chunk.getChunk();
         var0.next.end = 6;
         return var0.next;
      } else {
         return var0;
      }
   }

   protected void setBufferFlushStrategy() {
      if (this.buflimit == -1) {
         this.flushStrategy = new BufferFlushStrategy(this) {
            public final void checkOverflow(int var1) throws IOException {
            }

            public final void checkForFlush() throws IOException {
            }
         };
      } else if (this.autoflush) {
         this.flushStrategy = new BufferFlushStrategy(this) {
            public final void checkOverflow(int var1) throws IOException {
            }

            public final void checkForFlush() throws IOException {
               if (ChunkOutput.this.getCountForCheckOverflow() >= ChunkOutput.this.buflimit) {
                  this.out.flush();
               }

            }
         };
      } else if (this.alwaysFlush) {
         this.flushStrategy = new BufferFlushStrategy(this) {
            public final void checkOverflow(int var1) throws IOException {
            }

            public final void checkForFlush() throws IOException {
               this.out.flush();
            }
         };
      } else {
         this.flushStrategy = new BufferFlushStrategy(this) {
            public final void checkOverflow(int var1) throws IOException {
               if (ChunkOutput.this.getCountForCheckOverflow() + var1 > ChunkOutput.this.buflimit) {
                  throw new IOException("Exceeded buffer size of: '" + ChunkOutput.this.buflimit + "', and autoflush is: 'false'");
               }
            }

            public final void checkForFlush() throws IOException {
            }
         };
      }
   }

   protected int getCountForCheckOverflow() {
      return this.count;
   }

   static {
      CHUNK_SIZE = Chunk.CHUNK_SIZE - 6 - 2;
      END_OFFSET = Chunk.CHUNK_SIZE - 2;
      DIGITS = new byte[]{48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102};
      trigger = null;
      trigger = new CompleteMessageTimeoutTrigger();
   }

   protected abstract class BufferFlushStrategy {
      ChunkOutput out;

      public BufferFlushStrategy(ChunkOutput var2) {
         this.out = var2;
      }

      void setChunkOutput(ChunkOutput var1) {
         this.out = var1;
      }

      abstract void checkOverflow(int var1) throws IOException;

      abstract void checkForFlush() throws IOException;
   }
}
