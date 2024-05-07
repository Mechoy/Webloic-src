package weblogic.ejb.container.internal;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import weblogic.ejb.container.deployer.EJBDeployer;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.BeanManager;
import weblogic.ejb.container.interfaces.CMPInfo;
import weblogic.ejb.container.interfaces.EntityBeanInfo;

public final class DataBeanMapper {
   public static BeanManager getBeanManager(EJBDeployer var0, Class var1) {
      Map var2 = var0.getDataBeansMap();
      return var2 == null ? null : (BeanManager)var2.get(var1);
   }

   private static String getObjectSchemaName(Class var0) {
      Object var1 = null;

      try {
         var1 = Class.forName("weblogic.dbeans.internal.CmpJarCreator").newInstance();
      } catch (ClassNotFoundException var8) {
         throw new AssertionError(var8.getMessage());
      } catch (InstantiationException var9) {
         throw new AssertionError(var9.getMessage());
      } catch (IllegalAccessException var10) {
         throw new AssertionError(var10.getMessage());
      }

      Method var2 = null;

      try {
         var2 = var1.getClass().getDeclaredMethod("getObjectSchemaName", Class.class);
      } catch (NoSuchMethodException var7) {
         throw new AssertionError(var7.getMessage());
      }

      String var3 = null;

      try {
         var3 = (String)var2.invoke(var1, var0);
         return var3;
      } catch (InvocationTargetException var5) {
         throw new AssertionError(var5.getTargetException().getMessage());
      } catch (IllegalAccessException var6) {
         throw new AssertionError(var6.getMessage());
      }
   }

   public static void addBeans(EJBDeployer var0, Collection var1) {
      Map var2 = var0.getDataBeansMap();
      Iterator var3 = var1.iterator();

      CMPInfo var6;
      do {
         if (!var3.hasNext()) {
            return;
         }

         BeanInfo var4 = (BeanInfo)var3.next();
         if (!(var4 instanceof EntityBeanInfo)) {
            return;
         }

         EntityBeanInfo var5 = (EntityBeanInfo)var4;
         if (var5.getIsBeanManagedPersistence()) {
            return;
         }

         var6 = var5.getCMPInfo();
      } while(!var6.isBeanClassAbstract());

   }
}
