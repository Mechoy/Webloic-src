package weblogic.management.mbeanservers.internal.utils.typing;

import java.io.Serializable;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

public interface MBeanCategorizer extends Serializable {
   String categorize(MBeanServerConnection var1, ObjectName var2);

   TypeInfo getTypeInfo(MBeanServerConnection var1, ObjectName var2);

   public static class Impl implements MBeanCategorizer {
      static final long serialVersionUID = 1L;
      private Plugin[] plugins;

      public Impl(Plugin[] var1) {
         this.plugins = var1;
      }

      public String categorize(MBeanServerConnection var1, ObjectName var2) {
         try {
            String var3 = var2.getCanonicalName();

            for(int var4 = 0; var4 < this.plugins.length; ++var4) {
               Plugin var5 = this.plugins[var4];
               if (var5.handles(var1, var2)) {
                  return var5.getCategoryName();
               }
            }

            return null;
         } catch (RuntimeException var6) {
            throw var6;
         } catch (Exception var7) {
            throw new RuntimeException(var7);
         }
      }

      public TypeInfo getTypeInfo(MBeanServerConnection var1, ObjectName var2) {
         try {
            String var3 = var2.getCanonicalName();

            for(int var4 = 0; var4 < this.plugins.length; ++var4) {
               Plugin var5 = this.plugins[var4];
               if (var5.handles(var1, var2)) {
                  String var6 = var5.getTypeName(var1, var2);
                  if (var6 != null) {
                     return new TypeInfoImpl(var6, var5.getCategoryName());
                  }
               }
            }

            return null;
         } catch (RuntimeException var7) {
            throw var7;
         } catch (Exception var8) {
            throw new RuntimeException(var8);
         }
      }
   }

   public static class TypeInfoImpl implements TypeInfo {
      String type;
      String category;

      TypeInfoImpl(String var1, String var2) {
         this.type = var1;
         this.category = var2;
      }

      public String getTypeName() {
         return this.type;
      }

      public String getCategoryName() {
         return this.category;
      }
   }

   public interface Plugin extends Serializable {
      boolean handles(MBeanServerConnection var1, ObjectName var2);

      String getTypeName(MBeanServerConnection var1, ObjectName var2);

      String getCategoryName();
   }

   public interface TypeInfo {
      String getTypeName();

      String getCategoryName();
   }
}
