package weblogic.servlet.internal;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import weblogic.utils.io.Chunk;

public final class WLOutputStreamWriter extends Writer {
   private final CharsetEncoder encoder;
   private final OutputStream out;
   private final ByteBuffer bb;
   private int pos;
   private boolean error;
   private boolean closed;
   private boolean haveLeftoverChar;
   private char leftoverChar;
   private CharBuffer lcb;
   private Chunk head;

   public WLOutputStreamWriter(OutputStream var1, String var2) {
      this(var1, Charset.forName(var2).newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE));
   }

   public WLOutputStreamWriter(OutputStream var1) {
      this(var1, Charset.defaultCharset().newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE));
   }

   private WLOutputStreamWriter(OutputStream var1, CharsetEncoder var2) {
      this.pos = 0;
      this.error = false;
      this.closed = false;
      this.haveLeftoverChar = false;
      if (var1 == null) {
         throw new NullPointerException("out is null");
      } else {
         this.out = var1;
         this.encoder = var2;
         this.head = Chunk.getChunk();
         this.bb = ByteBuffer.wrap(this.head.buf);
      }
   }

   public String getEncoding() {
      return this.encoder.charset().name();
   }

   public void write(int var1) throws IOException {
      if (!this.error) {
         char[] var2 = new char[]{(char)var1};
         this.write((char[])var2, 0, 1);
      }
   }

   public void write(char[] var1, int var2, int var3) throws IOException {
      if (!this.error) {
         if (var2 >= 0 && var2 <= var1.length && var3 >= 0 && var2 + var3 <= var1.length && var2 + var3 >= 0) {
            if (var3 != 0) {
               this.write(CharBuffer.wrap(var1, var2, var3));
            }
         } else {
            throw new IndexOutOfBoundsException();
         }
      }
   }

   public void write(String var1, int var2, int var3) throws IOException {
      if (!this.error) {
         if (var1 != null && var3 != 0) {
            int var4 = var1.length();
            if (var2 >= 0 && var2 <= var4 && var3 >= 0 && var2 + var3 <= var4 && var2 + var3 >= 0) {
               this.write(CharBuffer.wrap(var1, var2, var3));
            } else {
               throw new IndexOutOfBoundsException();
            }
         }
      }
   }

   private void write(CharBuffer var1) throws IOException {
      if (this.haveLeftoverChar) {
         this.flushLeftoverChar(var1, false);
      }

      while(var1.hasRemaining()) {
         CoderResult var2 = this.encoder.encode(var1, this.bb, true);
         if (var2.isUnderflow()) {
            if (var1.remaining() == 1) {
               this.haveLeftoverChar = true;
               this.leftoverChar = var1.get();
            }
            break;
         }

         if (var2.isOverflow()) {
            this.writeBytes();
         } else {
            var2.throwException();
         }
      }

      this.flushBuffer();
   }

   public void close() throws IOException {
      if (!this.closed) {
         this.flushLeftoverChar((CharBuffer)null, true);

         while(true) {
            CoderResult var1 = this.encoder.flush(this.bb);
            if (var1.isUnderflow()) {
               if (this.bb.position() > 0) {
                  this.writeBytes();
               }

               this.out.close();
               Chunk.releaseChunks(this.head);
               this.head = null;
               this.closed = true;
               return;
            }

            if (var1.isOverflow()) {
               this.writeBytes();
            } else {
               var1.throwException();
            }
         }
      }
   }

   public void flush() throws IOException {
      if (!this.error) {
         this.out.flush();
      }
   }

   private void writeBytes() throws IOException {
      this.bb.flip();
      int var1 = this.bb.limit();
      int var2 = this.bb.position();
      int var3 = var2 > var1 ? 0 : var1 - var2;
      this.out.write(this.bb.array(), this.bb.arrayOffset() + var2, var3);
      this.bb.clear();
   }

   private void flushLeftoverChar(CharBuffer var1, boolean var2) throws IOException {
      if (this.haveLeftoverChar || var2) {
         if (this.lcb == null) {
            this.lcb = CharBuffer.allocate(2);
         } else {
            this.lcb.clear();
         }

         if (this.haveLeftoverChar) {
            this.lcb.put(this.leftoverChar);
         }

         if (var1 != null && var1.hasRemaining()) {
            this.lcb.put(var1.get());
         }

         this.lcb.flip();

         while(this.lcb.hasRemaining() || var2) {
            CoderResult var3 = this.encoder.encode(this.lcb, this.bb, var2);
            if (var3.isUnderflow()) {
               if (this.lcb.hasRemaining()) {
                  throw new Error();
               }
               break;
            }

            if (var3.isOverflow()) {
               this.writeBytes();
            } else {
               var3.throwException();
            }
         }

         this.haveLeftoverChar = false;
      }
   }

   void flushBuffer() throws IOException {
      if (!this.error) {
         if (!this.closed) {
            if (this.bb.position() > 0) {
               this.writeBytes();
            }

         }
      }
   }
}
