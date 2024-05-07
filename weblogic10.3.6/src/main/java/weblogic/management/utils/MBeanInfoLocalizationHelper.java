package weblogic.management.utils;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.management.Descriptor;
import javax.management.MBeanInfo;
import javax.management.modelmbean.ModelMBeanInfo;
import javax.management.openmbean.OpenMBeanInfoSupport;

public class MBeanInfoLocalizationHelper {
   public static MBeanInfo localizeMBeanInfo(MBeanInfo var0, Locale var1) {
      Descriptor var2 = var0.getDescriptor();
      if (var2 == null) {
         return var0;
      } else {
         String var3 = (String)var2.getFieldValue("descriptionResourceBundleBaseName");
         if (var3 == null && getDefaultResourceBundle(var0, var1) == null) {
            return var0;
         } else if (var0 instanceof ModelMBeanInfo) {
            return ModelMBeanInfoLocalizationHelper.localizeModelMBeanInfo((ModelMBeanInfo)ModelMBeanInfo.class.cast(var0), var1);
         } else {
            return var0 instanceof OpenMBeanInfoSupport ? OpenMBeanInfoLocalizationHelper.localizeOpenMBeanInfo((OpenMBeanInfoSupport)OpenMBeanInfoSupport.class.cast(var0), var1) : GenericMBeanInfoLocalizationHelper.localizeGenericMBeanInfo(var0, var1);
         }
      }
   }

   static String getDefaultResourceBundleName(MBeanInfo var0) {
      Descriptor var1 = var0.getDescriptor();
      String var2 = (String)var1.getFieldValue("interfaceClassName");
      if (var2 == null) {
         var2 = var0.getClassName();
      }

      String var3 = var2.substring(0, var2.lastIndexOf(46) + 1);
      String var4 = var3 + "MBeanDescriptions";
      return var4;
   }

   static String getBaseDefaultResourceKey(MBeanInfo var0) {
      Descriptor var1 = var0.getDescriptor();
      String var2 = (String)var1.getFieldValue("interfaceClassName");
      if (var2 == null) {
         var2 = var0.getClassName();
      }

      String var3 = var2.substring(var2.lastIndexOf(46) + 1) + ".";
      return var3;
   }

   static ResourceBundle getDefaultResourceBundle(MBeanInfo var0, Locale var1) {
      try {
         return ResourceBundle.getBundle(getDefaultResourceBundleName(var0), var1, Thread.currentThread().getContextClassLoader());
      } catch (MissingResourceException var3) {
         return null;
      }
   }
}
