package weblogic.corba.cos.naming;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

public final class NamingContextAnyHolder implements Streamable {
   public NamingContextAny value = null;

   public NamingContextAnyHolder() {
   }

   public NamingContextAnyHolder(NamingContextAny var1) {
      this.value = var1;
   }

   public void _read(InputStream var1) {
      this.value = NamingContextAnyHelper.read(var1);
   }

   public void _write(OutputStream var1) {
      NamingContextAnyHelper.write(var1, this.value);
   }

   public TypeCode _type() {
      return NamingContextAnyHelper.type();
   }
}
