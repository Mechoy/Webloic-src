package weblogic.servlet.internal;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.GatheringByteChannel;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import weblogic.socket.NIOConnection;
import weblogic.utils.io.Chunk;

public final class GatheringChunkOutput extends ChunkOutput {
   private static final byte[] CRCF = "\r\n".getBytes();
   private LinkedList<ByteBuffer> buffers;
   private ChunkOutput delegate;
   private Chunk currentChunk;
   private Chunk httpHeaders;
   private int currentChunkOffset;

   GatheringChunkOutput(ChunkOutput var1) {
      this.delegate = var1;
      this.currentChunk = this.delegate.head;
      this.currentChunkOffset = 6;
      this.buffers = new LinkedList();
      this.internalSetFlushStrategy();
   }

   GatheringChunkOutput(GatheringChunkOutput var1, ChunkOutput var2) {
      this.delegate = var2;
      this.currentChunk = var1.currentChunk;
      this.currentChunkOffset = var1.currentChunkOffset;
      this.buffers = var1.buffers;
      this.internalSetFlushStrategy();
   }

   public ChunkOutput getDelegate() {
      return this.delegate;
   }

   public void reset() {
      this.delegate.reset();
      this.internalClear();
   }

   public void release() {
      this.delegate.release();
      this.internalClear();
      this.internalRelease();
   }

   public void clearBuffer() {
      this.delegate.clearBuffer();
      this.internalClear();
   }

   public void setBufferSize(int var1) {
      this.delegate.setBufferSize(var1);
      this.internalSetFlushStrategy();
   }

   public void setAutoFlush(boolean var1) {
      this.delegate.setAutoFlush(var1);
      this.internalSetFlushStrategy();
   }

   protected void flushBufferedDataToChunk() {
      this.delegate.flushBufferedDataToChunk();
      this.flushChunksToByteBuffers();
   }

   public void write(ByteBuffer var1) throws IOException {
      if (!this.delegate.released) {
         this.delegate.flushStrategy.checkOverflow(var1.limit());
         this.delegate.flushBufferedDataToChunk();
         this.flushChunksToByteBuffers();
         var1.rewind();
         this.buffers.addLast(var1.duplicate());
         ChunkOutput var10000 = this.delegate;
         var10000.count += var1.limit();
         this.delegate.flushStrategy.checkForFlush();
      }
   }

   public void flush() throws IOException {
      if (!this.delegate.released) {
         this.delegate.flushBufferedDataToChunk();
         if (!this.isHeaderSent()) {
            this.delegate.sos.sendHeaders();
         }

         if (this.delegate.os != null) {
            this.flushChunksToByteBuffers();
            int var1 = this.flushBuffer();
            ChunkOutput var10000 = this.delegate;
            var10000.total += this.delegate.count;
            if (this.delegate.chunking) {
               var10000 = this.delegate;
               var10000.total += 8 * var1;
            }

            if (this.httpHeaders != null) {
               this.releaseHeaders();
            }

            this.clearBuffer();
         }

         this.delegate.postFlush();
      }
   }

   protected void setHttpHeaders(Chunk var1) {
      this.httpHeaders = var1;
   }

   private int flushBuffer() throws IOException {
      int var1 = this.addChunkingHeaderAndTail();
      if (this.httpHeaders != null) {
         this.flushHeaderToByteBuffers();
      }

      if (this.buffers.isEmpty()) {
         return 0;
      } else {
         int var2;
         try {
            registerToTrigger(this.delegate.os);
            this.writeChunks();
            var2 = var1;
         } finally {
            unregisterFromTrigger(this.delegate.os);
         }

         return var2;
      }
   }

   private boolean isHeaderSent() {
      return this.delegate.sos != null && this.delegate.sos.headersSent();
   }

   private int addChunkingHeaderAndTail() {
      if (this.delegate.chunking && !this.buffers.isEmpty()) {
         this.buffers.addFirst(getChunkHeader(this.delegate.count));
         this.buffers.addLast(ByteBuffer.wrap(CRCF));
         return 1;
      } else {
         return 0;
      }
   }

   private void writeChunks() throws IOException {
      if (this.buffers.size() == 1) {
         ByteBuffer var1 = (ByteBuffer)this.buffers.getFirst();
         if (var1.hasArray()) {
            this.delegate.os.write(var1.array(), var1.position(), var1.limit() - var1.position());
         } else {
            byte[] var2 = new byte[var1.limit() - var1.position()];
            var1.get(var2, 0, var2.length);
            this.delegate.os.write(var2, 0, var2.length);
         }
      } else {
         writeGatheredChunks(this.buffers, ((NIOConnection)this.delegate.os).getGatheringByteChannel());
      }

   }

   private static void writeGatheredChunks(List<ByteBuffer> var0, GatheringByteChannel var1) throws IOException {
      while(!var0.isEmpty()) {
         int var2 = var0.size();
         ByteBuffer[] var3 = new ByteBuffer[var2];

         for(int var4 = 0; var4 < var2; ++var4) {
            var3[var4] = (ByteBuffer)var0.get(var4);
         }

         var1.write(var3, 0, var3.length);
         Iterator var5 = var0.iterator();

         while(var5.hasNext() && !((ByteBuffer)var5.next()).hasRemaining()) {
            var5.remove();
         }
      }

   }

   private static ByteBuffer getChunkHeader(int var0) {
      int var1 = var0 > 65535 ? 8 : 4;
      byte[] var2 = new byte[var1 + 2];
      int var3 = var1;

      do {
         --var3;
         var2[var3] = DIGITS[var0 & 15];
         var0 >>>= 4;
      } while(var0 != 0);

      for(int var4 = 0; var4 < var3; ++var4) {
         var2[var4] = 48;
      }

      var2[var1] = 13;
      var2[var1 + 1] = 10;
      return ByteBuffer.wrap(var2);
   }

   private void internalClear() {
      this.currentChunk = this.delegate.head;
      this.buffers.clear();
      this.currentChunkOffset = 6;
      this.releaseHeaders();
   }

   private void releaseHeaders() {
      if (this.httpHeaders != null) {
         Chunk.releaseChunks(this.httpHeaders);
         this.httpHeaders = null;
      }

   }

   private void internalRelease() {
      this.currentChunk = null;
      this.buffers = null;
      this.delegate = null;
   }

   private void internalSetFlushStrategy() {
      if (this.delegate.flushStrategy != null) {
         this.delegate.flushStrategy.setChunkOutput(this);
      }

   }

   private void flushChunksToByteBuffers() {
      while(this.currentChunk != null && this.currentChunk.end > this.currentChunkOffset) {
         this.buffers.addLast(ByteBuffer.wrap(this.currentChunk.buf, this.currentChunkOffset, this.currentChunk.end - this.currentChunkOffset));
         if (this.currentChunk.end == END_OFFSET) {
            this.currentChunkOffset = 6;
            this.currentChunk = this.currentChunk.next;
         } else {
            this.currentChunkOffset = this.currentChunk.end;
         }
      }

   }

   private void flushHeaderToByteBuffers() {
      Chunk var1 = this.httpHeaders;

      for(int var2 = 0; var1 != null; var1 = var1.next) {
         ByteBuffer var3 = var1.getWriteByteBuffer();
         var3.position(0);
         this.buffers.add(var2++, var3);
      }

   }

   public String getEncoding() {
      return this.delegate.getEncoding();
   }

   public Chunk getHead() {
      return this.delegate.getHead();
   }

   public int getTotal() {
      return this.delegate.getTotal();
   }

   public int getCount() {
      return this.delegate.getCount();
   }

   public int getBufferSize() {
      return this.delegate.getBufferSize();
   }

   public void setStickyBufferSize(boolean var1) {
      this.delegate.setStickyBufferSize(var1);
   }

   public boolean isAutoFlush() {
      return this.delegate.isAutoFlush();
   }

   public boolean isChunking() {
      return this.delegate.isChunking();
   }

   public void setChunking(boolean var1) {
      this.delegate.setChunking(var1);
   }

   void writeByte(int var1) throws IOException {
      this.delegate.writeByte(var1);
   }

   public void write(int var1) throws IOException {
      this.delegate.write(var1);
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      this.delegate.write(var1, var2, var3);
   }

   public void write(char[] var1, int var2, int var3) throws IOException {
      this.delegate.write(var1, var2, var3);
   }

   public void print(String var1) throws IOException {
      this.delegate.print(var1);
   }

   public void println(String var1) throws IOException {
      this.delegate.println(var1);
   }

   public void println() throws IOException {
      this.delegate.println();
   }

   public void commit() throws IOException {
      this.delegate.commit();
   }

   public void writeStream(InputStream var1, int var2, int var3) throws IOException {
      this.delegate.writeStream(var1, var2, var3);
   }

   protected int getCountForCheckOverflow() {
      return this.delegate.getCountForCheckOverflow();
   }
}
