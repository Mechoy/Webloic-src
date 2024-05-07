package weblogic.servlet.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import weblogic.utils.io.Chunk;

class CharsetChunkOutput extends ChunkOutput {
   private static final boolean debug = false;
   private CharsetEncoder encoder;
   private String encoding;
   private ByteBuffer headBuf;
   private ByteBuffer buf;
   private int charcount;
   private CharChunk charChunk;
   private CharBuffer charBuffer;

   protected CharsetChunkOutput(int var1, boolean var2, OutputStream var3, ServletOutputStreamImpl var4, Charset var5) {
      super(var1, var2, var3, var4);
      this.encoder = var5.newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
      this.encoding = var5.name();
      this.headBuf = makeByteBuffer(this.head);
      this.buf = this.headBuf;
      this.charChunk = CharChunk.getChunk();
      this.charBuffer = CharBuffer.wrap(this.charChunk.buf);
   }

   CharsetChunkOutput(ChunkOutput var1, Charset var2) {
      this.encoder = var2.newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
      if (var1 instanceof CharsetChunkOutput) {
         CharsetChunkOutput var3 = (CharsetChunkOutput)var1;
         this.charChunk = var3.charChunk;
         this.charBuffer = var3.charBuffer;
      } else {
         this.charChunk = CharChunk.getChunk();
         this.charBuffer = CharBuffer.wrap(this.charChunk.buf);
      }

      this.encoding = var2.name();
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
      this.setBufferFlushStrategy();
      this.headBuf = makeByteBuffer(this.head);
      if (this.head == this.tail) {
         this.buf = this.headBuf;
      } else {
         this.buf = makeByteBuffer(this.tail);
      }

      this.encoder.reset();
   }

   public String getEncoding() {
      return this.encoding;
   }

   public void reset() {
      if (this.encoder != null) {
         this.encoder.reset();
      }

      super.reset();
      this.headBuf = null;
      this.buf = null;
      this.charBuffer.clear();
   }

   public void release() {
      this.buf = null;
      this.headBuf = null;
      this.encoder = null;
      super.release();
      this.charBuffer = null;
      CharChunk.releaseChunks(this.charChunk);
   }

   public void write(int var1) throws IOException {
      if (!this.released) {
         this.flushStrategy.checkOverflow(1);
         ++this.charcount;
         this.ensureCharBufferNotFull();
         this.charBuffer.put((char)var1);
         this.flushStrategy.checkForFlush();
      }
   }

   void writeByte(int var1) throws IOException {
      if (!this.released) {
         this.ensureCharBufferEmpty();
         this.flushStrategy.checkOverflow(1);
         ++this.charcount;
         this.tail = Chunk.tail(this.tail);
         int var2 = this.buf.remaining();
         if (var2 == 0) {
            this.tail = ensureCapacity(this.tail);
            this.buf = makeByteBuffer(this.tail);
            var2 = this.buf.remaining();
         }

         this.buf.put((byte)var1);
         ++this.tail.end;
         ++this.count;
         this.flushStrategy.checkForFlush();
      }
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      if (!this.released) {
         this.ensureCharBufferEmpty();
         this.flushStrategy.checkOverflow(var3);
         this.charcount += var3;
         this.tail = Chunk.tail(this.tail);
         this.implWrite(var1, var2, var3);
         this.flushStrategy.checkForFlush();
      }
   }

   public void write(ByteBuffer var1) throws IOException {
      if (!this.released) {
         this.ensureCharBufferEmpty();
         super.write(var1);
      }
   }

   public void writeStream(InputStream var1, int var2, int var3) throws IOException {
      if (!this.released) {
         this.ensureCharBufferEmpty();
         if (var2 > 0) {
            this.flushStrategy.checkOverflow(var2);
            this.charcount += var2;
         }

         this.tail = Chunk.tail(this.tail);
         super.writeStream(var1, var2, var3);
         this.tail = Chunk.tail(this.tail);
         this.buf = makeByteBuffer(this.tail);
      }
   }

   public void write(char[] var1, int var2, int var3) throws IOException {
      if (!this.released) {
         this.flushStrategy.checkOverflow(var3);
         this.charcount += var3;
         if (this.charBuffer.position() + var3 < this.charBuffer.limit()) {
            this.charBuffer.put(var1, var2, var3);
         } else if (var3 > this.charBuffer.limit()) {
            this.flushCharBuffer();
            this.write(CharBuffer.wrap(var1, var2, var3));
         } else {
            while(var3 > 0) {
               this.ensureCharBufferNotFull();
               int var4 = this.charBuffer.remaining();
               int var5 = Math.min(var4, var3);
               this.charBuffer.put(var1, var2, var5);
               var2 += var5;
               var3 -= var5;
            }
         }

         this.flushStrategy.checkForFlush();
      }
   }

   public void print(String var1) throws IOException {
      if (!this.released) {
         if (var1 == null) {
            var1 = "null";
         }

         int var2 = var1.length();
         this.flushStrategy.checkOverflow(var2);
         this.charcount += var2;
         if (this.charBuffer.position() + var2 < this.charBuffer.limit()) {
            this.charBuffer.put(var1);
         } else {
            int var5;
            if (var2 > this.charBuffer.limit()) {
               this.flushCharBuffer();
               this.write(CharBuffer.wrap(var1));
            } else {
               for(int var3 = 0; var2 > 0; var2 -= var5) {
                  this.ensureCharBufferNotFull();
                  int var4 = this.charBuffer.remaining();
                  var5 = Math.min(var4, var2);
                  this.charBuffer.put(var1, var3, var3 + var5);
                  var3 += var5;
               }
            }
         }

         this.flushStrategy.checkForFlush();
      }
   }

   public void clearBuffer() {
      if (!this.released) {
         super.clearBuffer();
         this.charcount = 0;
         this.buf = this.headBuf;
         this.buf.position(this.head.end).limit(END_OFFSET - this.head.end);
         this.charBuffer.clear();
      }
   }

   public void flush() throws IOException {
      if (!this.released) {
         this.ensureCharBufferEmpty();
         super.flush();
         this.postFlush();
      }
   }

   void postFlush() {
      this.tail = ensureCapacity(this.tail);
      this.buf = makeByteBuffer(this.tail);
   }

   public int getCount() {
      try {
         this.ensureCharBufferEmpty();
      } catch (IOException var2) {
      }

      return this.count;
   }

   protected int getCountForCheckOverflow() {
      return this.charcount;
   }

   protected void flushBufferedDataToChunk() {
      if (this.charBuffer != null) {
         try {
            this.ensureCharBufferEmpty();
         } catch (IOException var2) {
         }

      }
   }

   private void ensureCharBufferNotFull() throws IOException {
      if (!this.charBuffer.hasRemaining()) {
         this.flushCharBuffer();
      }

   }

   private void ensureCharBufferEmpty() throws IOException {
      if (this.charBuffer.position() > 0) {
         this.flushCharBuffer();
      }

   }

   private void flushCharBuffer() throws IOException {
      this.charBuffer.flip();
      this.write(this.charBuffer);
      this.charBuffer.clear();
   }

   private void write(CharBuffer var1) throws IOException {
      if (var1.hasRemaining()) {
         this.tail = Chunk.tail(this.tail);

         while(var1.hasRemaining()) {
            int var2 = this.buf.position();
            CoderResult var3 = this.encoder.encode(var1, this.buf, true);
            int var4 = this.buf.position();
            this.tail.end = var4;
            this.count += var4 - var2;
            if (var3 == CoderResult.UNDERFLOW) {
               break;
            }

            if (var3 == CoderResult.OVERFLOW) {
               if (this.buf.hasRemaining()) {
                  byte[] var5 = new byte[(int)this.encoder.maxBytesPerChar()];
                  ByteBuffer var6 = ByteBuffer.wrap(var5);
                  this.encoder.encode(var1, var6, true);
                  var6.flip();
                  this.implWrite(var5, 0, var6.remaining());
               } else {
                  this.tail = ensureCapacity(this.tail);
                  this.buf = makeByteBuffer(this.tail);
               }
            } else {
               var3.throwException();
            }
         }

         int var7 = this.buf.position();
         CoderResult var8 = this.encoder.flush(this.buf);
         if (var8.isUnderflow()) {
            int var9 = this.buf.position();
            this.tail.end = var9;
            this.count += var9 - var7;
         } else if (var8.isOverflow()) {
            ByteBuffer var10 = ByteBuffer.allocate((int)this.encoder.maxBytesPerChar());
            this.encoder.flush(var10);
            var10.flip();
            this.implWrite(var10.array(), 0, var10.remaining());
         }

         this.encoder.reset();
      }
   }

   private void implWrite(byte[] var1, int var2, int var3) throws IOException {
      while(var3 > 0) {
         int var4 = this.buf.remaining();
         if (var4 == 0) {
            this.tail = ensureCapacity(this.tail);
            this.buf = makeByteBuffer(this.tail);
            var4 = this.buf.remaining();
         }

         int var5 = Math.min(var4, var3);
         this.buf.put(var1, var2, var5);
         var2 += var5;
         var3 -= var5;
         Chunk var10000 = this.tail;
         var10000.end += var5;
         this.count += var5;
      }

   }

   private static ByteBuffer makeByteBuffer(Chunk var0) {
      return ByteBuffer.wrap(var0.buf, var0.end, END_OFFSET - var0.end);
   }

   private static void showProps(Buffer var0) {
      p(" capacity : " + var0.capacity() + " limit : " + var0.limit() + " position: " + var0.position());
   }

   private static void p(String var0) {
      System.out.println("[CharsetChunkOutput]" + var0);
   }
}
