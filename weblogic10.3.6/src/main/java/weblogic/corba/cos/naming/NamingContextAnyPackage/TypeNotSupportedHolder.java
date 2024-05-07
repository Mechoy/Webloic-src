package weblogic.corba.cos.naming.NamingContextAnyPackage;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

public final class TypeNotSupportedHolder implements Streamable {
   public TypeNotSupported value = null;

   public TypeNotSupportedHolder() {
   }

   public TypeNotSupportedHolder(TypeNotSupported var1) {
      this.value = var1;
   }

   public void _read(InputStream var1) {
      this.value = TypeNotSupportedHelper.read(var1);
   }

   public void _write(OutputStream var1) {
      TypeNotSupportedHelper.write(var1, this.value);
   }

   public TypeCode _type() {
      return TypeNotSupportedHelper.type();
   }
}
