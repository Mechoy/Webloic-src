package weblogic.jms.common;

import java.io.DataOutput;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.OutputStream;
import weblogic.jms.JMSClientExceptionLogger;
import weblogic.utils.StringUtils;
import weblogic.utils.io.Chunk;
import weblogic.utils.io.ChunkOutput;
import weblogic.utils.io.ChunkedDataInputStream;
import weblogic.utils.io.ChunkedDataOutputStream;
import weblogic.utils.io.DataIO;
import weblogic.utils.io.StringOutput;

final class BufferOutputStreamChunked extends BufferOutputStream implements StringOutput, ChunkOutput, ObjectOutput {
   private final ObjectIOBypass objectIOBypass;
   private boolean isBypassOutputStream;
   private boolean isJMSStoreOutputStream;
   private boolean isJMSMulticastOutputStream;
   private ChunkedDataOutputStream cdos;
   private Chunk chunk;
   private static final int VERSION = 1234;

   BufferOutputStreamChunked(ObjectIOBypass var1) {
      this.objectIOBypass = var1;
      this.cdos = new ChunkedDataOutputStream();
      this.chunk = this.cdos.getCurrentChunk();
   }

   BufferOutputStreamChunked() {
      this((ObjectIOBypass)null);
   }

   public final boolean isJMSStoreOutputStream() {
      return this.isJMSStoreOutputStream;
   }

   public final boolean isBypassOutputStream() {
      return this.isBypassOutputStream;
   }

   public final boolean isJMSMulticastOutputStream() {
      return this.isJMSMulticastOutputStream;
   }

   public final void setIsJMSStoreOutputStream() {
      this.isJMSStoreOutputStream = true;
   }

   public final void setIsBypassOutputStream() {
      this.isBypassOutputStream = true;
   }

   public final void setIsJMSMulticastOutputStream() {
      this.isJMSMulticastOutputStream = true;
   }

   public final void write(int var1) {
      this.cdos.write(var1);
   }

   public final void write(byte[] var1, int var2, int var3) {
      this.cdos.write(var1, var2, var3);
   }

   public final void reset() {
      this.cdos.reset();
   }

   public final int size() {
      return this.cdos.getPosition();
   }

   public final void writeObject(Object var1) throws IOException {
      this.writeInt(1234);
      if (this.objectIOBypass == null) {
         throw new IOException(JMSClientExceptionLogger.logRawObjectError2Loggable().getMessage());
      } else {
         this.objectIOBypass.writeObject(this, var1);
      }
   }

   public final void writeBoolean(boolean var1) throws IOException {
      this.cdos.writeBoolean(var1);
   }

   public final void writeByte(int var1) throws IOException {
      this.cdos.writeByte(var1);
   }

   public final void writeShort(int var1) throws IOException {
      this.cdos.writeShort(var1);
   }

   public final void writeChar(int var1) throws IOException {
      this.cdos.writeChar(var1);
   }

   public final void writeInt(int var1) throws IOException {
      this.cdos.writeInt(var1);
   }

   public final void writeLong(long var1) throws IOException {
      this.cdos.writeLong(var1);
   }

   public final void writeFloat(float var1) throws IOException {
      this.cdos.writeFloat(var1);
   }

   public final void writeDouble(double var1) throws IOException {
      this.cdos.writeDouble(var1);
   }

   public final void writeBytes(String var1) throws IOException {
      this.cdos.writeBytes(var1);
   }

   public final void writeChars(String var1) throws IOException {
      this.cdos.writeChars(var1);
   }

   public final void writeUTF32(String var1) throws IOException {
      writeUTF32(this, var1);
   }

   public static void writeUTF32(DataOutput var0, String var1) throws IOException {
      int var2 = StringUtils.getUTFLength(var1);
      var0.writeInt(var2);
      int var3 = var1.length();

      for(int var4 = 0; var4 < var3; ++var4) {
         DataIO.writeUTFChar(var0, var1.charAt(var4));
      }

   }

   public final void writeUTF(String var1) throws IOException {
      this.cdos.writeUTF(var1);
   }

   public int getLength() {
      Chunk var1 = this.cdos.getCurrentChunk();
      var1.end = Math.max(var1.end, this.cdos.getChunkPos());
      return Chunk.size(this.chunk);
   }

   public void writeLengthAndData(DataOutput var1) throws IOException {
      Chunk var2 = this.cdos.getCurrentChunk();
      var2.end = Math.max(var2.end, this.cdos.getChunkPos());
      PayloadChunkBase.internalWriteLengthAndData(var1, this.chunk);
   }

   public void writeTo(OutputStream var1) throws IOException {
      Chunk var2 = this.cdos.getCurrentChunk();
      var2.end = Math.max(var2.end, this.cdos.getChunkPos());

      for(Chunk var3 = this.chunk; var3 != null; var3 = var3.next) {
         if (var3.end > 0) {
            var1.write(var3.buf, 0, var3.end);
         }
      }

   }

   public BufferInputStream getInputStream() throws IOException {
      return new BufferInputStreamChunked(this.objectIOBypass, new ChunkedDataInputStream(this.cdos.getSharedBeforeCopyTail(), 0));
   }

   public Payload moveToPayload() {
      Chunk var1 = this.cdos.getCurrentChunk();
      var1.end = Math.max(var1.end, this.cdos.getChunkPos());
      Chunk var2 = this.chunk;
      this.chunk = null;
      this.cdos = null;
      return new PayloadChunkBase(var2);
   }

   PayloadStream copyPayloadWithoutSharedStream() throws JMSException {
      Chunk var1 = this.cdos.getCurrentChunk();
      var1.end = Math.max(var1.end, this.cdos.getChunkPos());
      return new PayloadChunkBase(PayloadChunkBase.copyWithoutSharedData(this.chunk));
   }

   public void writeChunks(Chunk var1) throws IOException {
      Chunk var2 = Chunk.tail(var1);
      if (var2.isReadOnlySharedBuf()) {
         var2.next = Chunk.getChunk();
      }

      this.cdos.writeChunks(var1);
   }

   public void copyBuffer() throws JMSException {
      Chunk var1 = this.cdos.getCurrentChunk();
      var1.end = Math.max(var1.end, this.cdos.getChunkPos());
      ChunkedDataOutputStream var2 = new ChunkedDataOutputStream();
      Chunk var3 = var2.getCurrentChunk();

      try {
         this.writeTo(var2);
      } catch (IOException var5) {
         throw new JMSException(var5);
      }

      this.cdos = var2;
      this.chunk = var3;
   }

   public ObjectOutput getObjectOutput() {
      return this;
   }

   public void writeASCII(String var1) throws IOException {
      this.cdos.writeASCII(var1);
   }

   public void writeUTF8(String var1) throws IOException {
      this.cdos.writeUTF8(var1);
   }
}
