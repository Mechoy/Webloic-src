package weblogic.corba.utils;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.lang.reflect.Array;
import javax.rmi.CORBA.Util;
import javax.rmi.CORBA.ValueHandler;
import javax.rmi.CORBA.ValueHandlerMultiFormat;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.SendingContext.RunTime;
import weblogic.iiop.IIOPInputStream;
import weblogic.iiop.IIOPOutputStream;
import weblogic.utils.io.ObjectStreamClass;

public class ValueHandlerImpl implements ValueHandlerMultiFormat {
   private static final boolean DEBUG = false;

   public void writeValue(OutputStream var1, Serializable var2) {
      this.writeValue(var1, var2, (byte)1);
   }

   public Serializable readValue(InputStream var1, int var2, Class var3, String var4, RunTime var5) {
      try {
         ObjectStreamClass var6 = ObjectStreamClass.lookup(var3);
         Object var7 = allocateValue(var1, var6);
         return (Serializable)readValue((IIOPInputStream)var1, var6, var7);
      } catch (IOException var8) {
         throw (MARSHAL)(new MARSHAL(var8.getMessage())).initCause(var8);
      } catch (ClassNotFoundException var9) {
         throw (MARSHAL)(new MARSHAL(var9.getMessage())).initCause(var9);
      }
   }

   public static Object allocateValue(InputStream var0, ObjectStreamClass var1) throws IOException {
      Class var2 = var1.forClass();
      return var1.isArray() ? Array.newInstance(var2.getComponentType(), var0.read_ulong()) : var1.newInstance();
   }

   public static Object readValue(IIOPInputStream var0, ObjectStreamClass var1, Object var2) throws IOException, ClassNotFoundException {
      if (var1.isArray()) {
         return readArray(var0, var1, var2);
      } else {
         if (var1.isExternalizable()) {
            byte var3 = var0.read_octet();
            ((Externalizable)var2).readExternal(var0);
         } else {
            readValueData(var0, var2, var1);
         }

         return var1.readResolve(var2);
      }
   }

   public String getRMIRepositoryID(Class var1) {
      return getRepositoryID(var1);
   }

   public static String getRepositoryID(Class var0) {
      ClassInfo var1 = ClassInfo.findClassInfo(var0);
      return var1 == null ? null : var1.getRepositoryId().toString();
   }

   public boolean isCustomMarshaled(Class var1) {
      ObjectStreamClass var2 = ObjectStreamClass.lookup(var1);
      return var2 == null ? false : var2.isCustomMarshaled();
   }

   public RunTime getRunTimeCodeBase() {
      return null;
   }

   public Serializable writeReplace(Serializable var1) {
      try {
         return (Serializable)this.writeReplace((Object)var1);
      } catch (IOException var3) {
         throw (MARSHAL)(new MARSHAL("writeReplace()")).initCause(var3);
      }
   }

   public Object writeReplace(Object var1) throws IOException {
      Class var2 = var1.getClass();

      for(ObjectStreamClass var3 = ObjectStreamClass.lookup(var2); var3.hasWriteReplace() && (var1 = var3.writeReplace(var1)) != null && var1.getClass() != var2; var3 = ObjectStreamClass.lookup(var2)) {
         var2 = var1.getClass();
      }

      return var1;
   }

   public byte getMaximumStreamFormatVersion() {
      return 2;
   }

   public void writeValue(OutputStream var1, Serializable var2, byte var3) {
      ClassInfo var4 = ClassInfo.findClassInfo(var2.getClass());

      try {
         writeValue((IIOPOutputStream)var1, var2, var3, var4);
      } catch (IOException var6) {
         throw (MARSHAL)(new MARSHAL(var6.getMessage() + " at " + ((IIOPOutputStream)var1).pos())).initCause(var6);
      }
   }

   public static void writeValue(IIOPOutputStream var0, Object var1, byte var2, ClassInfo var3) throws IOException {
      ObjectStreamClass var4 = var3.getDescriptor();
      if (var4.isExternalizable()) {
         var0.write_octet(var2);
         ((Externalizable)var1).writeExternal(var0);
      } else if (!ObjectStreamClass.supportsUnsafeSerialization()) {
         ValueHandler var5 = Util.createValueHandler();
         if (var5 instanceof ValueHandlerMultiFormat) {
            ((ValueHandlerMultiFormat)var5).writeValue(var0, (Serializable)var1, var2);
         } else {
            var5.writeValue(var0, (Serializable)var1);
         }
      } else if (var4.isArray()) {
         writeArray(var0, var1, var4, var3.forClass());
      } else {
         writeValueData(var0, var1, var4, var2);
      }

   }

   private static void writeValueData(IIOPOutputStream var0, Object var1, ObjectStreamClass var2, byte var3) throws IOException {
      if (var2.getSuperclass() != null) {
         writeValueData(var0, var1, var2.getSuperclass(), var3);
      }

      if (var2.hasWriteObject()) {
         var0.write_octet(var3);
         ObjectOutputStream var4 = var0.getObjectOutputStream(var1, var2, var3);
         var2.writeObject(var1, var4);
         var4.close();
      } else {
         var2.writeFields(var1, var0);
      }

   }

   private static void readValueData(IIOPInputStream var0, Object var1, ObjectStreamClass var2) throws IOException, ClassNotFoundException {
      if (var2.getSuperclass() != null) {
         readValueData(var0, var1, var2.getSuperclass());
      }

      boolean var3 = true;
      byte var4 = 1;
      if (var2.hasWriteObject()) {
         var4 = var0.read_octet();
         var3 = var0.read_boolean();
      }

      if (var2.hasReadObject()) {
         boolean var5 = false;
         if (!var3 && var4 > 1) {
            var5 = var0.startValue();
         }

         ObjectInputStream var6 = var0.getObjectInputStream(var1, var2, var3, var4);
         var2.readObject(var1, var6);
         var6.close();
         if (var5) {
            var0.end_value();
         }
      } else {
         if (!var3) {
            throw new StreamCorruptedException("defaultWriteObject() was not called, but " + var2.forClass().getName() + " has no readObject() method");
         }

         var2.readFields(var1, var0);
      }

   }

   private static void writeArray(IIOPOutputStream var0, Object var1, ObjectStreamClass var2, Class var3) throws IOException {
      Class var4 = var3.getComponentType();
      if (var4.isPrimitive()) {
         if (var4 == Integer.TYPE) {
            int[] var5 = (int[])((int[])var1);
            var0.write_ulong(var5.length);
            var0.write_long_array(var5, 0, var5.length);
         } else if (var4 == Byte.TYPE) {
            byte[] var7 = (byte[])((byte[])var1);
            var0.write_ulong(var7.length);
            var0.write_octet_array(var7, 0, var7.length);
         } else if (var4 == Long.TYPE) {
            long[] var8 = (long[])((long[])var1);
            var0.write_ulong(var8.length);
            var0.write_longlong_array(var8, 0, var8.length);
         } else if (var4 == Float.TYPE) {
            float[] var9 = (float[])((float[])var1);
            var0.write_ulong(var9.length);
            var0.write_float_array(var9, 0, var9.length);
         } else if (var4 == Double.TYPE) {
            double[] var10 = (double[])((double[])var1);
            var0.write_ulong(var10.length);
            var0.write_double_array(var10, 0, var10.length);
         } else if (var4 == Short.TYPE) {
            short[] var11 = (short[])((short[])var1);
            var0.write_ulong(var11.length);
            var0.write_short_array(var11, 0, var11.length);
         } else if (var4 == Character.TYPE) {
            char[] var12 = (char[])((char[])var1);
            var0.write_ulong(var12.length);
            var0.write_wchar_array(var12, 0, var12.length);
         } else {
            if (var4 != Boolean.TYPE) {
               throw new StreamCorruptedException("Invalid component type");
            }

            boolean[] var13 = (boolean[])((boolean[])var1);
            var0.write_ulong(var13.length);
            var0.write_boolean_array(var13, 0, var13.length);
         }
      } else {
         Object[] var14 = (Object[])((Object[])var1);
         var0.write_ulong(var14.length);

         for(int var6 = 0; var6 < var14.length; ++var6) {
            var0.writeObject(var14[var6], var4);
         }
      }

   }

   private static Object readArray(IIOPInputStream var0, ObjectStreamClass var1, Object var2) throws IOException, ClassNotFoundException {
      Class var3 = var1.forClass().getComponentType();
      if (var3.isPrimitive()) {
         if (var3 == Integer.TYPE) {
            int[] var13 = (int[])((int[])var2);
            var0.read_long_array((int[])var13, 0, var13.length);
            return var13;
         } else if (var3 == Byte.TYPE) {
            byte[] var12 = (byte[])((byte[])var2);
            var0.read_octet_array((byte[])var12, 0, var12.length);
            return var12;
         } else if (var3 == Long.TYPE) {
            long[] var11 = (long[])((long[])var2);
            var0.read_longlong_array((long[])var11, 0, var11.length);
            return var11;
         } else if (var3 == Float.TYPE) {
            float[] var10 = (float[])((float[])var2);
            var0.read_float_array((float[])var10, 0, var10.length);
            return var10;
         } else if (var3 == Double.TYPE) {
            double[] var9 = (double[])((double[])var2);
            var0.read_double_array((double[])var9, 0, var9.length);
            return var9;
         } else if (var3 == Short.TYPE) {
            short[] var8 = (short[])((short[])var2);
            var0.read_short_array((short[])var8, 0, var8.length);
            return var8;
         } else if (var3 == Character.TYPE) {
            char[] var7 = (char[])((char[])var2);
            var0.read_wchar_array(var7, 0, var7.length);
            return var7;
         } else if (var3 == Boolean.TYPE) {
            boolean[] var6 = (boolean[])((boolean[])var2);
            var0.read_boolean_array((boolean[])var6, 0, var6.length);
            return var6;
         } else {
            throw new StreamCorruptedException("Invalid component type");
         }
      } else {
         Object[] var4 = (Object[])((Object[])var2);

         for(int var5 = 0; var5 < var4.length; ++var5) {
            var4[var5] = var0.readObject(var3);
         }

         return var4;
      }
   }

   private static void p(String var0) {
      System.err.println("<ValueHandlerImpl>: " + var0);
   }
}
