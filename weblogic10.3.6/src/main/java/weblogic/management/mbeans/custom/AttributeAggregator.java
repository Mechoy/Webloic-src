package weblogic.management.mbeans.custom;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.beaninfo.BeanInfoAccess;
import weblogic.utils.ArrayUtils;

public class AttributeAggregator {
   Class subClass;
   List getterMethods;

   public AttributeAggregator(String var1, Class var2) {
      BeanInfoAccess var3 = ManagementService.getBeanInfoAccess();
      this.subClass = var2;
      this.getterMethods = new ArrayList(8);
      BeanInfo var4 = var3.getBeanInfoForInterface(var1, true, (String)null);
      PropertyDescriptor[] var5 = var4.getPropertyDescriptors();

      for(int var6 = 0; var6 < var5.length; ++var6) {
         PropertyDescriptor var7 = var5[var6];
         Class var8 = var7.getPropertyType();
         if (var8.isArray()) {
            Class var9 = var8.getComponentType();
            if (!var2.equals(var9) && var2.isAssignableFrom(var9)) {
               this.getterMethods.add(var7.getReadMethod());
            }
         }
      }

   }

   public AttributeAggregator(Class var1, Class var2, String var3) {
      this.subClass = var2;
      this.getterMethods = new ArrayList(8);
      Method[] var4 = var1.getMethods();

      for(int var5 = 0; var5 < var4.length; ++var5) {
         Method var6 = var4[var5];
         if (var6.getName().startsWith("get") && !var6.getName().equals(var3) && (var6.getParameterTypes() == null || var6.getParameterTypes().length == 0)) {
            Class var7 = var6.getReturnType();
            if (var7.isArray() && var2.isAssignableFrom(var7.getComponentType())) {
               this.getterMethods.add(var6);
            }
         }
      }

   }

   public Object[] getAll(Object var1) {
      ArrayList var2 = new ArrayList(32);

      for(int var3 = 0; var3 < this.getterMethods.size(); ++var3) {
         Method var4 = (Method)this.getterMethods.get(var3);

         try {
            Object[] var5 = (Object[])((Object[])var4.invoke(var1, (Object[])null));
            ArrayUtils.addAll(var2, var5);
         } catch (IllegalAccessException var6) {
            throw new Error(var6);
         } catch (InvocationTargetException var7) {
            throw new Error(var7);
         }
      }

      Object[] var8 = (Object[])((Object[])Array.newInstance(this.subClass, var2.size()));
      return var2.toArray(var8);
   }

   public Object lookup(Object var1, String var2) {
      for(int var3 = 0; var3 < this.getterMethods.size(); ++var3) {
         Method var4 = (Method)this.getterMethods.get(var3);

         try {
            Object[] var5 = (Object[])((Object[])var4.invoke(var1, (Object[])null));

            for(int var6 = 0; var6 < var5.length; ++var6) {
               Object var7 = var5[var6];

               try {
                  Method var8 = var7.getClass().getMethod("getName", (Class[])null);
                  String var9 = (String)var8.invoke(var7, (Object[])null);
                  if (var9.equals(var2)) {
                     return var7;
                  }
               } catch (NoSuchMethodException var10) {
               }
            }
         } catch (IllegalAccessException var11) {
            throw new Error(var11);
         } catch (InvocationTargetException var12) {
            throw new Error(var12);
         }
      }

      return null;
   }
}
