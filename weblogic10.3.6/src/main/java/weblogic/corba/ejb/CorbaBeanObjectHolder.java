package weblogic.corba.ejb;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

public final class CorbaBeanObjectHolder implements Streamable {
   public CorbaBeanObject value = null;

   public CorbaBeanObjectHolder() {
   }

   public CorbaBeanObjectHolder(CorbaBeanObject var1) {
      this.value = var1;
   }

   public void _read(InputStream var1) {
      this.value = CorbaBeanObjectHelper.read(var1);
   }

   public void _write(OutputStream var1) {
      CorbaBeanObjectHelper.write(var1, this.value);
   }

   public TypeCode _type() {
      return CorbaBeanObjectHelper.type();
   }
}
