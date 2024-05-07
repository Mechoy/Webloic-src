package weblogic.corba.idl;

import java.util.ArrayList;
import org.omg.CORBA.Any;
import org.omg.CORBA.Bounds;
import org.omg.CORBA.NVList;
import org.omg.CORBA.NamedValue;

public class NVListImpl extends NVList {
   private ArrayList elems;

   public NVListImpl(int var1) {
      this.elems = new ArrayList(var1);
   }

   public int count() {
      return this.elems.size();
   }

   public NamedValue add(int var1) {
      NamedValueImpl var2 = new NamedValueImpl("", new AnyImpl(), var1);
      this.elems.add(var2);
      return var2;
   }

   public void remove(int var1) throws Bounds {
      try {
         this.elems.remove(var1);
      } catch (IndexOutOfBoundsException var3) {
         throw new Bounds();
      }
   }

   public NamedValue add_item(String var1, int var2) {
      NamedValueImpl var3 = new NamedValueImpl(var1, new AnyImpl(), var2);
      this.elems.add(var3);
      return var3;
   }

   public NamedValue add_value(String var1, Any var2, int var3) {
      NamedValueImpl var4 = new NamedValueImpl(var1, var2, var3);
      this.elems.add(var4);
      return var4;
   }

   public NamedValue item(int var1) throws Bounds {
      try {
         return (NamedValue)this.elems.get(var1);
      } catch (IndexOutOfBoundsException var3) {
         throw new Bounds();
      }
   }
}
