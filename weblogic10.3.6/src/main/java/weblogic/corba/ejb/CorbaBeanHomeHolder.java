package weblogic.corba.ejb;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

public final class CorbaBeanHomeHolder implements Streamable {
   public CorbaBeanHome value = null;

   public CorbaBeanHomeHolder() {
   }

   public CorbaBeanHomeHolder(CorbaBeanHome var1) {
      this.value = var1;
   }

   public void _read(InputStream var1) {
      this.value = CorbaBeanHomeHelper.read(var1);
   }

   public void _write(OutputStream var1) {
      CorbaBeanHomeHelper.write(var1, this.value);
   }

   public TypeCode _type() {
      return CorbaBeanHomeHelper.type();
   }
}
