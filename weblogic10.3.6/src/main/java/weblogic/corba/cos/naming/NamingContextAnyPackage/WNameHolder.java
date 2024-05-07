package weblogic.corba.cos.naming.NamingContextAnyPackage;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

public final class WNameHolder implements Streamable {
   public WNameComponent[] value = null;

   public WNameHolder() {
   }

   public WNameHolder(WNameComponent[] var1) {
      this.value = var1;
   }

   public void _read(InputStream var1) {
      this.value = WNameHelper.read(var1);
   }

   public void _write(OutputStream var1) {
      WNameHelper.write(var1, this.value);
   }

   public TypeCode _type() {
      return WNameHelper.type();
   }
}
