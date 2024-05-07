package weblogic.wsee.component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.holders.Holder;
import weblogic.wsee.util.Verbose;

public abstract class BaseComponent implements Component {
   private static final boolean verbose = Verbose.isVerbose(BaseComponent.class);

   public void preinvoke(String var1, MessageContext var2) throws ComponentException {
   }

   public void postinvoke(String var1, MessageContext var2) throws ComponentException {
   }

   public void destroy() {
   }

   protected Method findMethod(Class var1, String var2, Class[] var3) {
      if (verbose) {
         Verbose.log((Object)("method " + var2));
      }

      Method[] var4 = var1.getMethods();

      for(int var5 = 0; var5 < var4.length; ++var5) {
         if (var2.equals(var4[var5].getName())) {
            if (var3 == null) {
               return var4[var5];
            }

            Class[] var6 = var4[var5].getParameterTypes();
            if (isAssignable(var3, var6)) {
               return var4[var5];
            }
         }
      }

      return null;
   }

   protected static boolean isAssignable(Class[] var0, Class[] var1) {
      if (var0 != null && var1 != null) {
         if (verbose) {
            Verbose.log((Object)("Arg length " + var0.length + " & " + var1.length));
         }

         if (var0.length != var1.length) {
            return false;
         } else {
            for(int var2 = 0; var2 < var0.length; ++var2) {
               if (!isAssignable(var0[var2], var1[var2])) {
                  return false;
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }

   protected static boolean isAssignable(Class var0, Class var1) {
      Class var2 = unwrapHolderType(var0);
      Class var3 = unwrapHolderType(var1);
      if (verbose) {
         Verbose.log((Object)(var2 + " & " + var3));
      }

      if (var2.getName().equals(var3.getName())) {
         return true;
      } else if (var3.isAssignableFrom(var2)) {
         return true;
      } else if (var2.isArray() && var3.isArray()) {
         int var4;
         for(var4 = 0; var2.getComponentType() != null; var2 = var2.getComponentType()) {
            ++var4;
         }

         int var5;
         for(var5 = 0; var3.getComponentType() != null; var3 = var3.getComponentType()) {
            ++var5;
         }

         return var4 == var5 && var3.isAssignableFrom(var2);
      } else {
         return false;
      }
   }

   private static Class unwrapHolderType(Class var0) {
      if (Holder.class.isAssignableFrom(var0)) {
         try {
            Field var1 = var0.getField("value");
            return var1.getType();
         } catch (NoSuchFieldException var2) {
            throw new IllegalArgumentException(var0 + " is an instance of" + "Holder class, but it does not have a public field called value. " + var2);
         } catch (SecurityException var3) {
            throw new IllegalArgumentException(var0 + " is an instance of" + "Holder class, but it does not have a public field called value. " + var3);
         }
      } else {
         return var0;
      }
   }
}
