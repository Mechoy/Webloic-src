package weblogic.management;

import javax.management.ObjectName;
import weblogic.management.internal.AttributeChangeNotification;

/** @deprecated */
public final class AttributeRemoveNotification extends AttributeChangeNotification {
   private static final long serialVersionUID = -7366904839966430571L;

   public AttributeRemoveNotification(ObjectName var1, String var2, Object var3) {
      super(var1, var2, var3, (Object)null);
   }

   public AttributeRemoveNotification(ObjectName var1, String var2, String var3, Object var4) {
      super(var1, var2, var3, var4, (Object)null);
   }

   public AttributeRemoveNotification(ObjectName var1, long var2, long var4, String var6, String var7, String var8, Object var9) {
      super(var1, var2, var4, var6, var7, var8, var9, (Object)null);
   }

   public Object getRemovedValue() {
      return this.getOldValue();
   }
}
