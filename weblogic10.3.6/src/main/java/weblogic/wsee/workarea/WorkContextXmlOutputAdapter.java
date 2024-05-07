package weblogic.wsee.workarea;

import java.beans.XMLEncoder;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import weblogic.workarea.WorkContext;
import weblogic.workarea.WorkContextOutput;

public class WorkContextXmlOutputAdapter implements WorkContextOutput {
   private final XMLEncoder xmlOutput;

   public WorkContextXmlOutputAdapter(OutputStream var1) {
      this.xmlOutput = new XMLEncoder(var1);
   }

   public WorkContextXmlOutputAdapter(XMLEncoder var1) {
      this.xmlOutput = var1;
   }

   public void close() {
      this.xmlOutput.close();
   }

   public void writeASCII(String var1) throws IOException {
      this.writeBytes(var1);
   }

   public void writeContext(WorkContext var1) throws IOException {
      this.writeASCII(var1.getClass().getName());
      var1.writeContext(this);
   }

   public void write(int var1) throws IOException {
      this.xmlOutput.writeObject(new Integer(var1));
   }

   public void write(byte[] var1) throws IOException {
      this.xmlOutput.writeObject(var1);
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      byte[] var4 = new byte[var3 - var2];
      System.arraycopy(var1, var2, var4, 0, var3);
      this.xmlOutput.writeObject(var4);
   }

   public void writeBoolean(boolean var1) throws IOException {
      this.xmlOutput.writeObject(new Boolean(var1));
   }

   public void writeByte(int var1) throws IOException {
      this.xmlOutput.writeObject(new Byte((byte)var1));
   }

   public void writeShort(int var1) throws IOException {
      this.xmlOutput.writeObject(new Short((short)var1));
   }

   public void writeChar(int var1) throws IOException {
      this.xmlOutput.writeObject(new Character((char)var1));
   }

   public void writeInt(int var1) throws IOException {
      this.xmlOutput.writeObject(new Integer(var1));
   }

   public void writeLong(long var1) throws IOException {
      this.xmlOutput.writeObject(new Long(var1));
   }

   public void writeFloat(float var1) throws IOException {
      this.xmlOutput.writeObject(new Float(var1));
   }

   public void writeDouble(double var1) throws IOException {
      this.xmlOutput.writeObject(new Double(var1));
   }

   public void writeBytes(String var1) throws IOException {
      this.xmlOutput.writeObject(var1);
   }

   public void writeChars(String var1) throws IOException {
      this.xmlOutput.writeObject(var1);
   }

   public void writeUTF(String var1) throws IOException {
      this.xmlOutput.writeObject(var1);
   }

   public static void main(String[] var0) throws Exception {
      XMLEncoder var1 = new XMLEncoder(new FileOutputStream("Test.xml"));
      WorkContextXmlOutputAdapter var2 = new WorkContextXmlOutputAdapter(var1);
      var2.writeASCII("Ascii HelloChris");
      var2.write(23);
      byte[] var3 = new byte[]{1, 2, 3, 4};
      var2.write(var3);
      var2.writeBoolean(true);
      var2.writeByte(25);
      var2.writeShort(26);
      var2.writeChar(99);
      var2.writeInt(27);
      var2.writeLong(28L);
      var2.writeFloat(29.0F);
      var2.writeDouble(30.0);
      var2.writeBytes("Foo");
      var2.writeChars("Foo2");
      var2.writeUTF("UTF");
      var1.close();
   }
}
