package weblogic.jms.dotnet.transport.t3client;

import java.io.DataOutput;
import java.io.IOException;
import java.io.OutputStream;
import weblogic.jms.dotnet.transport.MarshalWritable;
import weblogic.jms.dotnet.transport.MarshalWriter;
import weblogic.jms.dotnet.transport.Transport;
import weblogic.utils.io.Chunk;
import weblogic.utils.io.ChunkedDataOutputStream;

public class MarshalWriterImpl implements MarshalWriter {
   private final ChunkedDataOutputStream cdos = new ChunkedDataOutputStream();
   private final Throwable throwable = null;
   private Transport transport;
   private boolean isClosed;
   private MarshalWriterImpl next;

   MarshalWriterImpl() {
      this.transport = null;
   }

   MarshalWriterImpl(Transport var1) {
      this.transport = var1;
   }

   void setNext(MarshalWriterImpl var1) {
      this.next = var1;
   }

   MarshalWriterImpl getNext() {
      return this.next;
   }

   void copyTo(OutputStream var1) throws IOException {
      this.isClosed = true;
      Chunk var2 = this.cdos.getChunks();

      while(var2 != null) {
         try {
            if (var2.end > 0) {
               var1.write(var2.buf, 0, var2.end);
            }
         } catch (IOException var5) {
            while(var2 != null) {
               Chunk var4 = var2;
               var2 = var2.next;
               Chunk.releaseChunk(var4);
            }

            throw var5;
         }

         Chunk var3 = var2;
         var2 = var2.next;
         Chunk.releaseChunk(var3);
      }

   }

   byte[] toByteArray() {
      byte[] var1 = new byte[this.getPosition()];
      int var2 = 0;
      this.isClosed = true;
      Chunk var3 = this.cdos.getChunks();

      while(var3 != null) {
         if (var3.end > 0) {
            System.arraycopy(var3.buf, 0, var1, var2, var3.end);
            var2 += var3.end;
         }

         Chunk var4 = var3;
         var3 = var3.next;
         Chunk.releaseChunk(var4);
      }

      return var1;
   }

   int getPosition() {
      return this.cdos.getPosition();
   }

   void setPosition(int var1) {
      this.cdos.setPosition(var1);
   }

   void skip(int var1) {
      this.cdos.skip(var1);
   }

   void writeUTF(String var1) {
      try {
         this.cdos.writeUTF(var1);
      } catch (IOException var3) {
         throw new RuntimeException(var3);
      }
   }

   public Transport getTransport() {
      return this.transport;
   }

   public void writeMarshalable(MarshalWritable var1) {
      this.writeInt(var1.getMarshalTypeCode());
      int var2 = this.cdos.getPosition();
      this.writeInt(Integer.MIN_VALUE);
      int var3 = this.cdos.getPosition();
      var1.marshal(this);
      int var4 = this.cdos.getPosition();
      int var5 = var4 - var3;
      this.cdos.setPosition(var2);
      this.writeInt(var5);
      this.cdos.setPosition(var4);
   }

   public void writeByte(byte var1) {
      this.cdos.writeByte(var1);
   }

   public void writeUnsignedByte(int var1) {
      this.cdos.writeByte((byte)(var1 & 255));
   }

   void write(int var1) {
      this.cdos.writeByte((byte)(var1 & 255));
   }

   public void write(byte[] var1, int var2, int var3) {
      this.cdos.write(var1, var2, var3);
   }

   public void writeBoolean(boolean var1) {
      this.cdos.writeBoolean(var1);
   }

   void writeShort(int var1) {
      this.cdos.writeShort(var1);
   }

   public void writeShort(short var1) {
      this.cdos.writeShort(var1);
   }

   public void writeChar(char var1) {
      this.cdos.writeChar(var1);
   }

   public void writeInt(int var1) {
      this.cdos.writeInt(var1);
   }

   public void writeLong(long var1) {
      this.cdos.writeLong(var1);
   }

   public void writeFloat(float var1) {
      this.cdos.writeFloat(var1);
   }

   public void writeDouble(double var1) {
      this.cdos.writeDouble(var1);
   }

   public void writeString(String var1) {
      this.cdos.writeUTF8(var1);
   }

   void closeInternal() {
      if (!this.isClosed) {
         try {
            this.cdos.close();
         } catch (IOException var2) {
         }

      }
   }

   public DataOutput getDataOutputStream() {
      return this.cdos;
   }
}
