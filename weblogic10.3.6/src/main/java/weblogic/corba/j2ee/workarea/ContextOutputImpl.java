package weblogic.corba.j2ee.workarea;

import java.io.IOException;
import weblogic.iiop.EndPoint;
import weblogic.iiop.IIOPOutputStream;
import weblogic.workarea.WorkContext;
import weblogic.workarea.WorkContextOutput;

class ContextOutputImpl implements WorkContextOutput {
   private IIOPOutputStream delegate;

   ContextOutputImpl(IIOPOutputStream var1) {
      this.delegate = var1;
   }

   IIOPOutputStream getDelegate() {
      return this.delegate;
   }

   ContextOutputImpl(EndPoint var1) {
      this.delegate = new IIOPOutputStream(false, var1);
      this.delegate.startUnboundedEncapsulation();
   }

   public void write(byte[] var1) throws IOException {
      this.delegate.write(var1);
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      this.delegate.write(var1, var2, var3);
   }

   public void write(int var1) throws IOException {
      this.delegate.write(var1);
   }

   public void writeInt(int var1) throws IOException {
      this.delegate.writeInt(var1);
   }

   public void writeUTF(String var1) throws IOException {
      this.delegate.writeUTF(var1);
   }

   public void writeLong(long var1) throws IOException {
      this.delegate.writeLong(var1);
   }

   public void writeByte(int var1) throws IOException {
      this.delegate.writeByte(var1);
   }

   public void writeShort(int var1) throws IOException {
      this.delegate.writeShort(var1);
   }

   public void writeBytes(String var1) throws IOException {
      this.delegate.writeBytes(var1);
   }

   public void writeFloat(float var1) throws IOException {
      this.delegate.writeFloat(var1);
   }

   public void writeChar(int var1) throws IOException {
      this.delegate.writeChar(var1);
   }

   public void writeBoolean(boolean var1) throws IOException {
      this.delegate.writeBoolean(var1);
   }

   public void writeDouble(double var1) throws IOException {
      this.delegate.writeDouble(var1);
   }

   public void writeChars(String var1) throws IOException {
      this.delegate.writeChars(var1);
   }

   public void writeASCII(String var1) throws IOException {
      this.delegate.write_string(var1);
   }

   public void writeContext(WorkContext var1) throws IOException {
      this.writeASCII(var1.getClass().getName());
      var1.writeContext(this);
   }
}
