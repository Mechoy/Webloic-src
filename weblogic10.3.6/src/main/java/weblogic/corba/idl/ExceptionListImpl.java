package weblogic.corba.idl;

import java.util.ArrayList;
import org.omg.CORBA.Bounds;
import org.omg.CORBA.ExceptionList;
import org.omg.CORBA.TypeCode;

public class ExceptionListImpl extends ExceptionList {
   private ArrayList elems = new ArrayList();

   public int count() {
      return this.elems.size();
   }

   public void add(TypeCode var1) {
      this.elems.add(var1);
   }

   public void remove(int var1) throws Bounds {
      try {
         this.elems.remove(var1);
      } catch (IndexOutOfBoundsException var3) {
         throw new Bounds();
      }
   }

   public TypeCode item(int var1) throws Bounds {
      try {
         return (TypeCode)this.elems.get(var1);
      } catch (IndexOutOfBoundsException var3) {
         throw new Bounds();
      }
   }
}
