package weblogic.corba.cos.naming.NamingContextAnyPackage;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

public final class WNameComponentHolder implements Streamable {
   public WNameComponent value = null;

   public WNameComponentHolder() {
   }

   public WNameComponentHolder(WNameComponent var1) {
      this.value = var1;
   }

   public void _read(InputStream var1) {
      this.value = WNameComponentHelper.read(var1);
   }

   public void _write(OutputStream var1) {
      WNameComponentHelper.write(var1, this.value);
   }

   public TypeCode _type() {
      return WNameComponentHelper.type();
   }
}
