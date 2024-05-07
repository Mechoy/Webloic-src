package weblogic.diagnostics.harvester.internal;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import weblogic.diagnostics.harvester.AttributeNameNormalizer;
import weblogic.management.mbeanservers.Service;
import weblogic.management.runtime.RuntimeMBean;

public class AttributeNormalizerUtil {
   private static final AttributeNameNormalizer ATTR_NORMALIZER = new HarvesterDefaultAttributeNormalizer();

   public static String getNormalizedAttributeName(String var0, String var1) {
      AttributeNameNormalizer var2 = ATTR_NORMALIZER;
      if (var0 != null) {
         BeanInfo var3 = TreeBeanHarvestableDataProviderHelper.getBeanInfo(var0);
         if (var3 != null) {
            PropertyDescriptor[] var4 = var3.getPropertyDescriptors();
            PropertyDescriptor[] var5 = var4;
            int var6 = var4.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               PropertyDescriptor var8 = var5[var7];
               Class var9 = var8.getPropertyType();
               if (!var9.isPrimitive() && !RuntimeMBean.class.isAssignableFrom(var9) && !Service.class.isAssignableFrom(var9)) {
                  String var10 = var8.getName();
                  if (var1.startsWith(var10)) {
                     String var11 = (String)var8.getValue("harvesterAttributeNormalizerClass");
                     if (var11 != null) {
                        try {
                           Class var12 = Class.forName(var11);
                           var2 = (AttributeNameNormalizer)var12.newInstance();
                           break;
                        } catch (Exception var14) {
                           return var1;
                        }
                     }
                  }
               }
            }
         }
      }

      return var2.getNormalizedAttributeName(var1);
   }
}
