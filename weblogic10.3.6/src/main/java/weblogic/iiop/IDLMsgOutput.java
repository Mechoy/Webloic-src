package weblogic.iiop;

import java.io.Externalizable;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.rmi.Remote;
import javax.rmi.CORBA.Util;
import org.omg.CORBA.Any;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.portable.IDLEntity;
import org.omg.CORBA.portable.InvokeHandler;
import org.omg.CORBA.portable.Streamable;
import weblogic.utils.AssertionError;

public class IDLMsgOutput extends AbstractMsgOutput {
   public IDLMsgOutput(IIOPOutputStream var1) {
      super(var1);
   }

   public final void writeUTF(String var1) throws IOException {
      try {
         this.delegate.write_string(var1);
      } catch (SystemException var3) {
         throw Utils.mapSystemException(var3);
      }
   }

   public final void writeChar(int var1) throws IOException {
      try {
         this.delegate.write_char((char)var1);
      } catch (SystemException var3) {
         throw Utils.mapSystemException(var3);
      }
   }

   public final void writeBytes(String var1) throws IOException {
      try {
         this.delegate.write_string(var1);
      } catch (SystemException var3) {
         throw Utils.mapSystemException(var3);
      }
   }

   public final void writeChars(String var1) throws IOException {
      try {
         this.delegate.write_string(var1);
      } catch (SystemException var3) {
         throw Utils.mapSystemException(var3);
      }
   }

   private final void writeArray(Object var1, Class var2) throws IOException {
      int var3 = Array.getLength(var1);
      this.delegate.write_long(var3);
      Class var4 = var2.getComponentType();
      if (!var4.isPrimitive()) {
         for(int var5 = 0; var5 < var3; ++var5) {
            this.writeObject(((Object[])((Object[])var1))[var5], var4);
         }
      } else if (var4 == Byte.TYPE) {
         this.delegate.write_octet_array((byte[])((byte[])var1), 0, var3);
      } else if (var4 == Character.TYPE) {
         this.delegate.write_wchar_array((char[])((char[])var1), 0, var3);
      } else if (var4 == Float.TYPE) {
         this.delegate.write_float_array((float[])((float[])var1), 0, var3);
      } else if (var4 == Double.TYPE) {
         this.delegate.write_double_array((double[])((double[])var1), 0, var3);
      } else if (var4 == Integer.TYPE) {
         this.delegate.write_long_array((int[])((int[])var1), 0, var3);
      } else if (var4 == Long.TYPE) {
         this.delegate.write_longlong_array((long[])((long[])var1), 0, var3);
      } else if (var4 == Short.TYPE) {
         this.delegate.write_short_array((short[])((short[])var1), 0, var3);
      } else {
         if (var4 != Boolean.TYPE) {
            throw new AssertionError("Unknown component type " + var4);
         }

         this.delegate.write_boolean_array((boolean[])((boolean[])var1), 0, var3);
      }

   }

   public final void writeObject(Object var1) throws IOException {
      this.writeObject(var1, var1.getClass());
   }

   public final void writeObject(Object var1, Class var2) throws IOException {
      try {
         if (var2.isArray()) {
            this.writeArray(var1, var2);
         } else if (!Remote.class.isAssignableFrom(var2) && !InvokeHandler.class.isAssignableFrom(var2) && !org.omg.CORBA.Object.class.isAssignableFrom(var2)) {
            if (Any.class.isAssignableFrom(var2)) {
               this.delegate.write_any((Any)var1);
            } else if (IDLEntity.class.isAssignableFrom(var2)) {
               this.delegate.write_IDLEntity(var1, var2);
            } else if (!var2.equals(Object.class) && !var2.equals(Serializable.class) && !var2.equals(Externalizable.class)) {
               if (var2.equals(String.class)) {
                  this.delegate.write_string((String)var1);
               } else if (Streamable.class.isAssignableFrom(var2)) {
                  this.writeStreamable((Streamable)var1);
               } else {
                  this.delegate.write_value((Serializable)var1, var2);
               }
            } else {
               Util.writeAny(this.delegate, var1);
            }
         } else {
            this.delegate.writeRemote(var1);
         }

      } catch (SystemException var4) {
         throw Utils.mapSystemException(var4);
      }
   }

   AbstractMsgInput createMsgInput(IIOPInputStream var1) {
      return new IDLMsgInput(var1);
   }

   void writeStreamable(Streamable var1) {
      var1._write(this.delegate);
   }
}
