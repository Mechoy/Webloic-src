package weblogic.corba.cos.naming.NamingContextAnyPackage;

import org.omg.CORBA.Any;
import org.omg.CORBA.ORB;
import org.omg.CORBA.StructMember;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

public abstract class TypeNotSupportedHelper {
   private static String _id = "IDL:weblogic/corba/cos/naming/NamingContextAny/TypeNotSupported:1.0";
   private static TypeCode __typeCode = null;
   private static boolean __active = false;

   public static void insert(Any var0, TypeNotSupported var1) {
      OutputStream var2 = var0.create_output_stream();
      var0.type(type());
      write(var2, var1);
      var0.read_value(var2.create_input_stream(), type());
   }

   public static TypeNotSupported extract(Any var0) {
      return read(var0.create_input_stream());
   }

   public static synchronized TypeCode type() {
      if (__typeCode == null) {
         Class var0 = TypeCode.class;
         synchronized(TypeCode.class) {
            if (__typeCode == null) {
               if (__active) {
                  return ORB.init().create_recursive_tc(_id);
               }

               __active = true;
               StructMember[] var1 = new StructMember[0];
               Object var2 = null;
               __typeCode = ORB.init().create_exception_tc(id(), "TypeNotSupported", var1);
               __active = false;
            }
         }
      }

      return __typeCode;
   }

   public static String id() {
      return _id;
   }

   public static TypeNotSupported read(InputStream var0) {
      TypeNotSupported var1 = new TypeNotSupported();
      var0.read_string();
      return var1;
   }

   public static void write(OutputStream var0, TypeNotSupported var1) {
      var0.write_string(id());
   }
}
