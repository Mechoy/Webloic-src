package weblogic.management.internal;

import java.io.ObjectStreamException;
import java.io.Serializable;
import javax.management.Attribute;

/** @deprecated */
public class WebLogicAttribute extends Attribute {
   public static final long serialVersionUID = 216628355171837704L;
   public static final Object NULL_VALUE;

   public WebLogicAttribute(String var1, Object var2) {
      super(var1, var2 == null ? NULL_VALUE : var2);
   }

   public Object getValue() {
      Object var1 = super.getValue();
      return var1 == NULL_VALUE ? null : var1;
   }

   static {
      NULL_VALUE = WebLogicAttribute.NullObject.it;
   }

   public static final class NullObject implements Serializable {
      static NullObject it = new NullObject();
      private static final long serialVersionUID = -4024808658342520589L;

      public boolean equals(Object var1) {
         return var1 instanceof NullObject;
      }

      public int hashCode() {
         return 0;
      }

      public Object clone() {
         return it;
      }

      private Object readResolve() throws ObjectStreamException {
         return it;
      }
   }
}
