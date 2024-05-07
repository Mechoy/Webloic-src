package weblogic.iiop;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.rmi.Remote;
import javax.rmi.CORBA.Util;
import org.omg.CORBA.Any;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.portable.IDLEntity;
import org.omg.CORBA.portable.Streamable;
import weblogic.utils.AssertionError;

public class IDLMsgInput extends AbstractMsgInput implements ObjectInput {
   public IDLMsgInput(IIOPInputStream var1) {
      super(var1);
   }

   public final String readLine() throws IOException {
      try {
         return this.delegate.read_string();
      } catch (SystemException var2) {
         throw Utils.mapSystemException(var2);
      }
   }

   public final String readUTF() throws IOException {
      try {
         return this.delegate.read_string();
      } catch (SystemException var2) {
         throw Utils.mapSystemException(var2);
      }
   }

   public final char readChar() throws IOException {
      try {
         return this.delegate.read_char();
      } catch (SystemException var2) {
         throw Utils.mapSystemException(var2);
      }
   }

   private final Object readArray(Class var1) throws ClassNotFoundException, IOException {
      int var2 = this.delegate.read_long();
      Class var3 = var1.getComponentType();
      Object var4 = Array.newInstance(var3, var2);
      if (!var3.isPrimitive()) {
         for(int var5 = 0; var5 < var2; ++var5) {
            ((Object[])((Object[])var4))[var5] = this.readObject(var3);
         }
      } else if (var3 == Byte.TYPE) {
         this.delegate.read_octet_array((byte[])((byte[])((byte[])var4)), 0, var2);
      } else if (var3 == Character.TYPE) {
         this.delegate.read_wchar_array((char[])((char[])var4), 0, var2);
      } else if (var3 == Float.TYPE) {
         this.delegate.read_float_array((float[])((float[])((float[])var4)), 0, var2);
      } else if (var3 == Double.TYPE) {
         this.delegate.read_double_array((double[])((double[])((double[])var4)), 0, var2);
      } else if (var3 == Integer.TYPE) {
         this.delegate.read_long_array((int[])((int[])((int[])var4)), 0, var2);
      } else if (var3 == Long.TYPE) {
         this.delegate.read_longlong_array((long[])((long[])((long[])var4)), 0, var2);
      } else if (var3 == Short.TYPE) {
         this.delegate.read_short_array((short[])((short[])((short[])var4)), 0, var2);
      } else {
         if (var3 != Boolean.TYPE) {
            throw new AssertionError("Unknown component type " + var3);
         }

         this.delegate.read_boolean_array((boolean[])((boolean[])((boolean[])var4)), 0, var2);
      }

      return var4;
   }

   public final Object readObject() throws ClassNotFoundException, IOException {
      try {
         return this.readObject((Class)null);
      } catch (SystemException var2) {
         throw Utils.mapSystemException(var2);
      }
   }

   public final Object readObject(Class var1) throws ClassNotFoundException, IOException {
      try {
         if (var1.isArray()) {
            return this.readArray(var1);
         } else if (Remote.class.isAssignableFrom(var1)) {
            return this.readRemote(var1);
         } else if (org.omg.CORBA.Object.class.isAssignableFrom(var1)) {
            return this.delegate.read_Object(var1);
         } else if (Any.class.isAssignableFrom(var1)) {
            return this.delegate.read_any();
         } else if (IDLEntity.class.isAssignableFrom(var1)) {
            return this.delegate.read_IDLEntity(var1);
         } else if (!var1.equals(Object.class) && !var1.equals(Serializable.class) && !var1.equals(Externalizable.class)) {
            return var1.equals(String.class) ? this.delegate.read_string() : this.delegate.read_value(var1);
         } else {
            return Util.readAny(this.delegate);
         }
      } catch (SystemException var3) {
         throw Utils.mapSystemException(var3);
      }
   }

   AbstractMsgOutput createMsgOutput(IIOPOutputStream var1) {
      return new IDLMsgOutput(var1);
   }

   void readStreamable(Streamable var1) {
      var1._read(this.delegate);
   }
}
