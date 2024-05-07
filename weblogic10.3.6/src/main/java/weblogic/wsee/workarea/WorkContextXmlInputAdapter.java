package weblogic.wsee.workarea;

import java.beans.XMLDecoder;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.NotSerializableException;
import weblogic.workarea.WorkContext;
import weblogic.workarea.WorkContextInput;

public final class WorkContextXmlInputAdapter implements WorkContextInput {
   private final XMLDecoder xmlDecoder;

   public WorkContextXmlInputAdapter(InputStream var1) {
      this.xmlDecoder = new XMLDecoder(var1);
   }

   public WorkContextXmlInputAdapter(XMLDecoder var1) {
      this.xmlDecoder = var1;
   }

   public String readASCII() throws IOException {
      return (String)this.xmlDecoder.readObject();
   }

   public WorkContext readContext() throws IOException, ClassNotFoundException {
      Class var1 = Class.forName(this.readASCII());

      try {
         WorkContext var2 = (WorkContext)var1.newInstance();
         var2.readContext(this);
         return var2;
      } catch (InstantiationException var3) {
         throw (IOException)(new NotSerializableException("WorkContext must have a public no-arg constructor")).initCause(var3);
      } catch (IllegalAccessException var4) {
         throw (IOException)(new NotSerializableException("WorkContext must have a public no-arg constructor")).initCause(var4);
      }
   }

   public void readFully(byte[] var1) throws IOException {
      byte[] var2 = (byte[])((byte[])this.xmlDecoder.readObject());
      System.arraycopy(var2, 0, var1, 0, var2.length);
   }

   public void readFully(byte[] var1, int var2, int var3) throws IOException {
      byte[] var4 = (byte[])((byte[])this.xmlDecoder.readObject());
      System.arraycopy(var4, 0, var1, var2, var3);
   }

   public int skipBytes(int var1) throws IOException {
      throw new UnsupportedOperationException();
   }

   public boolean readBoolean() throws IOException {
      return (Boolean)this.xmlDecoder.readObject();
   }

   public byte readByte() throws IOException {
      return (Byte)this.xmlDecoder.readObject();
   }

   public int readUnsignedByte() throws IOException {
      return (Integer)this.xmlDecoder.readObject();
   }

   public short readShort() throws IOException {
      return (Short)this.xmlDecoder.readObject();
   }

   public int readUnsignedShort() throws IOException {
      return (Integer)this.xmlDecoder.readObject();
   }

   public char readChar() throws IOException {
      return (Character)this.xmlDecoder.readObject();
   }

   public int readInt() throws IOException {
      return (Integer)this.xmlDecoder.readObject();
   }

   public long readLong() throws IOException {
      return (Long)this.xmlDecoder.readObject();
   }

   public float readFloat() throws IOException {
      return (Float)this.xmlDecoder.readObject();
   }

   public double readDouble() throws IOException {
      return (Double)this.xmlDecoder.readObject();
   }

   public String readLine() throws IOException {
      return (String)this.xmlDecoder.readObject();
   }

   public String readUTF() throws IOException {
      return (String)this.xmlDecoder.readObject();
   }

   public static void main(String[] var0) throws Exception {
      XMLDecoder var1 = new XMLDecoder(new FileInputStream(var0[0]));
      WorkContextXmlInputAdapter var2 = new WorkContextXmlInputAdapter(var1);
      System.out.println(var2.readASCII());
      System.out.println(var2.readInt());
      byte[] var3 = new byte[20];
      var2.readFully(var3);
      System.out.println(var3);
      System.out.println(var2.readBoolean());
      System.out.println(var2.readByte());
      System.out.println(var2.readShort());
      System.out.println(var2.readChar());
      System.out.println(var2.readInt());
      System.out.println(var2.readLong());
      System.out.println(var2.readFloat());
      System.out.println(var2.readDouble());
      System.out.println(var2.readUTF());
      System.out.println(var2.readUTF());
      System.out.println(var2.readUTF());
   }
}
