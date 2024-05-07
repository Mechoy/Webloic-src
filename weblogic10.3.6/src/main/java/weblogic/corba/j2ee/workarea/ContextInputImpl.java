package weblogic.corba.j2ee.workarea;

import java.io.IOException;
import java.io.NotSerializableException;
import weblogic.iiop.IIOPInputStream;
import weblogic.iiop.Utils;
import weblogic.workarea.WorkContext;
import weblogic.workarea.WorkContextInput;

class ContextInputImpl implements WorkContextInput {
   private IIOPInputStream delegate;

   ContextInputImpl(IIOPInputStream var1) {
      this.delegate = var1;
   }

   public String readASCII() throws IOException {
      return this.delegate.read_string();
   }

   public WorkContext readContext() throws IOException, ClassNotFoundException {
      Class var1 = Utils.loadClass(this.readASCII());

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

   public String readLine() throws IOException {
      throw new UnsupportedOperationException("readLine");
   }

   public int readInt() throws IOException {
      return this.delegate.readInt();
   }

   public String readUTF() throws IOException {
      return this.delegate.readUTF();
   }

   public long readLong() throws IOException {
      return this.delegate.readLong();
   }

   public byte readByte() throws IOException {
      return this.delegate.readByte();
   }

   public short readShort() throws IOException {
      return this.delegate.readShort();
   }

   public float readFloat() throws IOException {
      return this.delegate.readFloat();
   }

   public char readChar() throws IOException {
      return this.delegate.readChar();
   }

   public void readFully(byte[] var1) throws IOException {
      this.delegate.readFully(var1);
   }

   public void readFully(byte[] var1, int var2, int var3) throws IOException {
      this.delegate.readFully(var1, var2, var3);
   }

   public int skipBytes(int var1) throws IOException {
      return this.delegate.skipBytes(var1);
   }

   public boolean readBoolean() throws IOException {
      return this.delegate.readBoolean();
   }

   public int readUnsignedByte() throws IOException {
      return this.delegate.readUnsignedByte();
   }

   public int readUnsignedShort() throws IOException {
      return this.delegate.readUnsignedShort();
   }

   public double readDouble() throws IOException {
      return this.delegate.readDouble();
   }
}
