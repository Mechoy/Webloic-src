package weblogic.wsee.util;

public class ObjectUtil {
   public static final boolean equals(Object var0, Object var1) {
      return var0 == null && var1 == null || var0 != null && var0.equals(var1);
   }
}
