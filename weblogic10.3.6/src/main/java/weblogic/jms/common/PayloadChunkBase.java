package weblogic.jms.common;

import java.io.DataOutput;
import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import weblogic.utils.io.ByteBufferObjectOutputStream;
import weblogic.utils.io.Chunk;
import weblogic.utils.io.ChunkedDataInputStream;
import weblogic.utils.io.ChunkedDataOutputStream;

class PayloadChunkBase implements PayloadText, PayloadStream {
   private static int DOUBLE_CHUNK_SIZE;
   protected final Chunk chunk;

   PayloadChunkBase(Chunk var1) {
      this.chunk = var1;
   }

   public BufferInputStream getInputStream() throws IOException {
      Chunk var1 = this.chunk.getSharedBeforeTailCopy((Chunk)null);
      ChunkedDataInputStream var2 = new ChunkedDataInputStream(var1, 0);
      return new BufferInputStreamChunked((ObjectIOBypass)null, var2);
   }

   public int getLength() {
      return Chunk.size(this.chunk);
   }

   public String readUTF8() throws IOException {
      int var1 = this.getLength();
      byte[] var2 = new byte[4];
      int var3 = 0;
      var2[var3++] = (byte)(var1 >>> 24);
      var2[var3++] = (byte)(var1 >>> 16);
      var2[var3++] = (byte)(var1 >>> 8);
      var2[var3++] = (byte)(var1 >>> 0);
      Chunk var4 = Chunk.createSharedChunk(var2, 4);
      var4.next = this.chunk.getSharedBeforeTailCopy((Chunk)null);
      ChunkedDataInputStream var5 = new ChunkedDataInputStream(var4, 0);
      var5.mark(0);
      return var5.readUTF8();
   }

   public PayloadText copyPayloadWithoutSharedText() throws JMSException {
      return new PayloadChunkBase(copyWithoutSharedData(this.chunk));
   }

   public PayloadStream copyPayloadWithoutSharedStream() throws JMSException {
      return new PayloadChunkBase(copyWithoutSharedData(this.chunk));
   }

   public void writeTo(OutputStream var1) throws IOException {
      for(Chunk var2 = this.chunk; var2 != null; var2 = var2.next) {
         if (var2.end > 0) {
            var1.write(var2.buf, 0, var2.end);
         }
      }

   }

   public void writeLengthAndData(DataOutput var1) throws IOException {
      internalWriteLengthAndData(var1, this.chunk);
   }

   static Chunk copyWithoutSharedData(Chunk var0) throws JMSException {
      int var1 = 0;

      for(Chunk var4 = var0; var4 != null; var4 = var4.next) {
         var1 += var4.end;
      }

      ChunkedDataInputStream var10 = new ChunkedDataInputStream(var0, 0);
      var10.mark(0);
      int var5 = var1 % Chunk.CHUNK_SIZE;
      int var6 = Math.min(var1, Chunk.CHUNK_SIZE * 2 + var5);
      if (var6 == 0) {
         var6 = Chunk.CHUNK_SIZE;
      }

      int var2;
      Chunk var3;
      try {
         var3 = Chunk.createOneSharedChunk(var10, var6);
         var1 -= var6;
         if (var1 == 0) {
            return var3;
         }

         var3.next = Chunk.getChunk();
         var2 = Chunk.chunk(var3.next, var10, var1);
      } catch (IOException var9) {
         throw new JMSException(var9);
      }

      if (var2 != var1) {
         Chunk.releaseChunks(var3);
         throw new JMSException(new EOFException("Short by " + (var1 - var2)));
      } else {
         int var7 = 0;

         for(Chunk var8 = var3; var8 != null; var8 = var8.next) {
            if (var8.buf.length == Chunk.CHUNK_SIZE) {
               ++var7;
            }
         }

         var7 >>= PayloadFactoryImpl.SHIFT_REPLACEMENT_STOLEN_CHUNK_COUNT;
         if (PayloadFactoryImpl.REPLACE_STOLEN_CHUNKS && var7 > 0) {
            Chunk.replaceStolenChunks(var7);
         }

         return var3;
      }
   }

   static void internalWriteLengthAndData(DataOutput var0, Chunk var1) throws IOException {
      int var2 = 0;

      Chunk var3;
      for(var3 = var1; var3 != null; var3 = var3.next) {
         var2 += var3.end;
      }

      if (var2 > PayloadFactoryImpl.CHUNK_LINK_THRESHOLD) {
         var1 = var1.getSharedBeforeTailCopy((Chunk)null);
         if (var0 instanceof ChunkedDataOutputStream) {
            var3 = Chunk.tail(var1);
            if (var3.isReadOnlySharedBuf()) {
               var3.next = Chunk.getChunk();
            }

            ((ChunkedDataOutputStream)var0).writeChunks(var1);
            return;
         }

         if (var0 instanceof ByteBufferObjectOutputStream) {
            writeLengthLinkByteBuffers((ByteBufferObjectOutputStream)var0, var1);
            return;
         }
      }

      var0.writeInt(var2);

      for(; var1 != null; var1 = var1.next) {
         if (var1.end != 0) {
            var0.write(var1.buf, 0, var1.end);
         }
      }

   }

   private static void writeLengthLinkByteBuffers(ByteBufferObjectOutputStream var0, Chunk var1) throws IOException {
      int var2 = 0;
      int var3 = 0;

      for(Chunk var4 = var1; var4 != null; var4 = var4.next) {
         if (var4.end > 0) {
            var2 += var4.end;
            ++var3;
         }
      }

      var0.writeInt(var2);
      if (var2 != 0) {
         ByteBuffer[] var7 = new ByteBuffer[var3];
         var3 = 0;

         for(Chunk var5 = var1; var5 != null; var5 = var5.next) {
            if (var5.end > 0) {
               ByteBuffer var6 = var5.wrapAsReadOnlyByteBuffer();
               var6.position(var5.end);
               var7[var3++] = var6;
            }
         }

         var0.addReadOnlyBuffers(var7);
      }
   }

   static final Chunk linkAndCopyChunksWithoutWastedMemory(ChunkedDataInputStream var0, int var1) throws IOException {
      Chunk var2 = var0.getChunks();
      int var3 = var0.getChunkPos();
      Chunk var4;
      if (var3 != 0) {
         int var5 = var2.end - var3;
         var1 -= var5;
         var4 = nCustomChunks(var0, var5);
      } else {
         var4 = null;
      }

      Chunk var9 = var4;
      var2 = var0.getChunks();
      var3 = var0.getChunkPos();

      while(true) {
         while(var1 > 0) {
            if (var2.end == var3) {
               var2 = var2.next;
               var3 = 0;
            } else {
               assert var3 == 0;

               Chunk var6;
               Chunk var7;
               if (var1 >= var2.end && var2.end == var2.buf.length) {
                  var7 = var6 = var2.createOneSharedChunk();
                  var1 -= var6.end;
                  var0.skip((long)var6.end);
               } else {
                  int var8 = Math.min(var1, var2.end);
                  var1 -= var8;
                  var6 = nCustomChunks(var0, var8);
                  var7 = Chunk.tail(var6);
               }

               if (var4 == null) {
                  var4 = var6;
               } else {
                  var9.next = var6;
               }

               var9 = var7;
               var2 = var0.getChunks();
               var3 = var0.getChunkPos();
            }
         }

         if (var4 == null) {
            var4 = Chunk.createOneSharedChunk(var0, 0);
         }

         return var4;
      }
   }

   private static Chunk nCustomChunks(ChunkedDataInputStream var0, int var1) throws IOException {
      if (var1 == 0) {
         return null;
      } else {
         int var2 = var1 % Chunk.CHUNK_SIZE;
         int var3 = Math.min(var1, Chunk.CHUNK_SIZE + var2);
         var1 -= var3;
         Chunk var4 = Chunk.createOneSharedChunk(var0, var3);
         if (var1 > 0) {
            var4.next = Chunk.getChunk();
            int var5 = Chunk.chunk(var4.next, var0, var1);
            if (var5 != var1) {
               throw new EOFException();
            }
         }

         return var4;
      }
   }

   static {
      DOUBLE_CHUNK_SIZE = Chunk.CHUNK_SIZE + Chunk.CHUNK_SIZE;
   }
}
