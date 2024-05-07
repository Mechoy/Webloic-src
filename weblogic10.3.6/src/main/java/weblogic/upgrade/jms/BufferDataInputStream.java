package weblogic.upgrade.jms;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.StreamCorruptedException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

public class BufferDataInputStream extends InputStream implements ObjectInput {
   private ByteBuffer buf;
   private UpgradeIOBypass objectIOBypass;
   private static final int VERSION = 1234;

   public BufferDataInputStream(UpgradeIOBypass var1, ByteBuffer var2) {
      this.buf = var2;
      this.objectIOBypass = var1;
   }

   public int read() {
      try {
         return this.buf.get();
      } catch (BufferUnderflowException var2) {
         return -1;
      }
   }

   public int read(byte[] var1, int var2, int var3) {
      if (this.buf.remaining() <= 0) {
         return -1;
      } else {
         int var4 = Math.min(var3, this.buf.remaining());
         this.buf.get(var1, var2, var4);
         return var4;
      }
   }

   public long skip(long var1) {
      int var3 = Math.min((int)var1, this.buf.remaining());
      this.buf.position(this.buf.position() + var3);
      return (long)var3;
   }

   public int available() {
      return this.buf.remaining();
   }

   public boolean markSupported() {
      return false;
   }

   public void mark(int var1) {
      throw new UnsupportedOperationException("mark not supported");
   }

   public void reset() {
      this.buf.rewind();
   }

   public synchronized void close() throws IOException {
   }

   public final void readFully(byte[] var1) throws IOException {
      this.readFully(var1, 0, var1.length);
   }

   public final void readFully(byte[] var1, int var2, int var3) throws IOException {
      if (this.buf.remaining() < var3) {
         throw new EOFException();
      } else {
         this.buf.get(var1, var2, var3);
      }
   }

   public final int skipBytes(int var1) throws IOException {
      int var2 = Math.min(var1, this.buf.remaining());
      this.buf.position(this.buf.position() + var2);
      return var2;
   }

   public final Object readObject() throws IOException, ClassNotFoundException {
      int var1 = this.readInt();
      if (var1 != 1234) {
         throw new StreamCorruptedException("Unknown object stream version. " + var1);
      } else if (this.objectIOBypass == null) {
         throw new StreamCorruptedException("Stream can not handle raw objects.");
      } else {
         return this.objectIOBypass.readObject(this);
      }
   }

   public final boolean readBoolean() throws IOException {
      try {
         byte var1 = this.buf.get();
         return var1 != 0;
      } catch (BufferUnderflowException var2) {
         throw new EOFException();
      }
   }

   public final byte readByte() throws IOException {
      try {
         return this.buf.get();
      } catch (BufferUnderflowException var2) {
         throw new EOFException();
      }
   }

   public final int readUnsignedByte() throws IOException {
      try {
         return this.buf.get();
      } catch (BufferUnderflowException var2) {
         throw new EOFException();
      }
   }

   public final short readShort() throws IOException {
      try {
         return this.buf.getShort();
      } catch (BufferUnderflowException var2) {
         throw new EOFException();
      }
   }

   public final int readUnsignedShort() throws IOException {
      if (this.buf.remaining() < 2) {
         throw new EOFException();
      } else {
         return ((this.buf.get() & 255) << 8) + ((this.buf.get() & 255) << 0);
      }
   }

   public final char readChar() throws IOException {
      try {
         return this.buf.getChar();
      } catch (BufferUnderflowException var2) {
         throw new EOFException();
      }
   }

   public final int readInt() throws IOException {
      try {
         return this.buf.getInt();
      } catch (BufferUnderflowException var2) {
         throw new EOFException();
      }
   }

   public final long readLong() throws IOException {
      try {
         return this.buf.getLong();
      } catch (BufferUnderflowException var2) {
         throw new EOFException();
      }
   }

   public final float readFloat() throws IOException {
      try {
         return this.buf.getFloat();
      } catch (BufferUnderflowException var2) {
         throw new EOFException();
      }
   }

   public final double readDouble() throws IOException {
      try {
         return this.buf.getDouble();
      } catch (BufferUnderflowException var2) {
         throw new EOFException();
      }
   }

   public final String readLine() throws IOException {
      throw new IOException("not implemented");
   }

   public final String readUTF() throws IOException {
      return DataInputStream.readUTF(this);
   }
}
