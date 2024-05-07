package weblogic.management;

import javax.management.ObjectName;
import weblogic.management.internal.AttributeChangeNotification;

/** @deprecated */
public final class AttributeAddNotification extends AttributeChangeNotification {
   private static final long serialVersionUID = 823184172044137571L;

   /** @deprecated */
   public AttributeAddNotification(ObjectName var1, String var2, Object var3) {
      super(var1, var2, (Object)null, var3);
   }

   public AttributeAddNotification(ObjectName var1, String var2, String var3, Object var4) {
      super(var1, var2, var3, (Object)null, var4);
   }

   public AttributeAddNotification(ObjectName var1, long var2, long var4, String var6, String var7, String var8, Object var9) {
      super(var1, var2, var4, var6, var7, var8, (Object)null, var9);
   }

   public Object getAddedValue() {
      return this.getNewValue();
   }
}
