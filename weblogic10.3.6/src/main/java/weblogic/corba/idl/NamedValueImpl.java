package weblogic.corba.idl;

import org.omg.CORBA.Any;
import org.omg.CORBA.NamedValue;

public class NamedValueImpl extends NamedValue {
   private Any nvalue;
   private String nname;
   private int nflags;

   public NamedValueImpl(String var1, Any var2, int var3) {
      this.nname = var1;
      this.nflags = var3;
      this.nvalue = var2;
   }

   public Any value() {
      return this.nvalue;
   }

   public String name() {
      return this.nname;
   }

   public int flags() {
      return this.nflags;
   }
}
