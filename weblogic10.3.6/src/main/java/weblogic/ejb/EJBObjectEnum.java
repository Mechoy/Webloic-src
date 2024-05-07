package weblogic.ejb;

import java.io.Serializable;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Vector;

public final class EJBObjectEnum implements Enumeration, Serializable {
   private static final long serialVersionUID = 2670533090316944880L;
   private Vector v = new Vector();
   private transient int index;

   public EJBObjectEnum() {
   }

   public EJBObjectEnum(Collection var1) {
      this.v = new Vector(var1);
   }

   public void addElement(Object var1) {
      this.v.addElement(var1);
   }

   public boolean hasMoreElements() {
      return this.index < this.v.size();
   }

   public Object nextElement() {
      return this.v.elementAt(this.index++);
   }

   public Object clone() {
      EJBObjectEnum var1 = new EJBObjectEnum();
      var1.v = this.v;
      return var1;
   }
}
