package weblogic.jms.dotnet.transport.t3plugin;

import java.io.DataOutput;
import java.io.IOException;
import java.io.OutputStream;
import weblogic.jms.dotnet.transport.MarshalWritable;
import weblogic.jms.dotnet.transport.MarshalWriter;
import weblogic.jms.dotnet.transport.Transport;
import weblogic.utils.io.ChunkedDataOutputStream;

public class MarshalWriterImpl extends OutputStream implements MarshalWriter, DataOutput {
   private final Transport transport;
   private final ChunkedDataOutputStream cdos;
   private final Throwable throwable;

   MarshalWriterImpl(Transport var1, ChunkedDataOutputStream var2) {
      this.transport = var1;
      this.cdos = var2;
      this.throwable = null;
   }

   MarshalWriterImpl(Transport var1, Throwable var2) {
      this.transport = var1;
      this.cdos = new ChunkedDataOutputStream();
      this.throwable = var2;
   }

   Throwable getThrowable() {
      return this.throwable;
   }

   ChunkedDataOutputStream getChunkedDataOutputStream() {
      return this.cdos;
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

   public void write(byte[] var1, int var2, int var3) {
      this.cdos.write(var1, var2, var3);
   }

   public void writeBoolean(boolean var1) {
      this.cdos.writeBoolean(var1);
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
      try {
         this.cdos.close();
      } catch (IOException var2) {
      }

   }

   public DataOutput getDataOutputStream() {
      return this;
   }

   public void writeByte(int var1) throws IOException {
      throw new IOException("unresolvable");
   }

   public void writeBytes(String var1) throws IOException {
      throw new IOException("unresolvable");
   }

   public void writeChar(int var1) throws IOException {
      throw new IOException("unresolvable");
   }

   public void writeChars(String var1) throws IOException {
      throw new IOException("unresolvable");
   }

   public void writeShort(int var1) throws IOException {
      throw new IOException("unresolvable");
   }

   public void writeUTF(String var1) throws IOException {
      throw new IOException("unresolvable");
   }

   public void write(int var1) throws IOException {
      throw new IOException("unresolvable");
   }
}
