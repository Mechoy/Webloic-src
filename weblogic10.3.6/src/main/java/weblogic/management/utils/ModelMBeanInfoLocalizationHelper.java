package weblogic.management.utils;

import java.util.Locale;
import javax.management.Descriptor;
import javax.management.DescriptorRead;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.modelmbean.ModelMBeanAttributeInfo;
import javax.management.modelmbean.ModelMBeanConstructorInfo;
import javax.management.modelmbean.ModelMBeanInfo;
import javax.management.modelmbean.ModelMBeanInfoSupport;
import javax.management.modelmbean.ModelMBeanNotificationInfo;
import javax.management.modelmbean.ModelMBeanOperationInfo;
import weblogic.diagnostics.debug.DebugLogger;

public class ModelMBeanInfoLocalizationHelper {
   private static DebugLogger DEBUG_LOGGER = DebugLogger.getDebugLogger("DebugMBeanLocalization");

   static MBeanInfo localizeModelMBeanInfo(ModelMBeanInfo var0, Locale var1) {
      Descriptor var2 = null;

      try {
         var2 = var0.getMBeanDescriptor();
      } catch (MBeanException var15) {
         if (DEBUG_LOGGER.isDebugEnabled()) {
            DEBUG_LOGGER.debug("ModelMBeanInfoLocalizationHelper.localizeModelMBeanInfo(): : Error getting ModelMBeanInfo Descriptor for:" + var0);
         }

         return (MBeanInfo)MBeanInfo.class.cast(var0);
      }

      String var3 = (String)var2.getFieldValue("descriptionResourceBundleBaseName");
      if ((var3 == null || var3.length() == 0) && MBeanInfoLocalizationHelper.getDefaultResourceBundle((MBeanInfo)MBeanInfo.class.cast(var0), var1) != null) {
         var3 = MBeanInfoLocalizationHelper.getDefaultResourceBundleName((MBeanInfo)MBeanInfo.class.cast(var0));
      }

      String var4 = MBeanInfoLocalizationHelper.getBaseDefaultResourceKey((MBeanInfo)MBeanInfo.class.cast(var0));
      String var5 = var4 + "mbean";
      String var6 = GenericMBeanInfoLocalizationHelper.localizeDescription(var2, var1, var3, var5);
      if (var6 == null) {
         var6 = var0.getDescription();
      }

      var2 = GenericMBeanInfoLocalizationHelper.localizeDescriptor((DescriptorRead)DescriptorRead.class.cast(var0), var1, var3);
      MBeanAttributeInfo[] var7 = var0.getAttributes();
      MBeanConstructorInfo[] var8 = var0.getConstructors();
      MBeanOperationInfo[] var9 = var0.getOperations();
      MBeanNotificationInfo[] var10 = var0.getNotifications();
      ModelMBeanAttributeInfo[] var11 = localizeModelMBeanAttributes(var7, var1, var3, var4 + "attribute.");
      ModelMBeanConstructorInfo[] var12 = localizeModelMBeanConstructors(var8, var1, var3, var4 + "constructor.");
      ModelMBeanOperationInfo[] var13 = localizeModelMBeanOperations(var9, var1, var3, var4 + "operation.");
      ModelMBeanNotificationInfo[] var14 = localizeModelMBeanNotifications(var10, var1, var3, var4 + "notification.");
      return new ModelMBeanInfoSupport(var0.getClassName(), var6, var11, var12, var13, var14, var2);
   }

   private static ModelMBeanAttributeInfo[] localizeModelMBeanAttributes(MBeanAttributeInfo[] var0, Locale var1, String var2, String var3) {
      if (var0 == null) {
         return null;
      } else {
         ModelMBeanAttributeInfo[] var4 = new ModelMBeanAttributeInfo[var0.length];

         for(int var5 = 0; var5 < var0.length; ++var5) {
            var4[var5] = localizeModelMBeanAttribute((ModelMBeanAttributeInfo)ModelMBeanAttributeInfo.class.cast(var0[var5]), var1, var2, var3);
         }

         return var4;
      }
   }

   static ModelMBeanAttributeInfo localizeModelMBeanAttribute(ModelMBeanAttributeInfo var0, Locale var1, String var2, String var3) {
      if (var0 == null) {
         return null;
      } else {
         Descriptor var4 = GenericMBeanInfoLocalizationHelper.localizeDescriptor(var0, var1, var2);
         String var5 = GenericMBeanInfoLocalizationHelper.localizeDescription(var0.getDescriptor(), var1, var2, var3 + var0.getName());
         if (var5 == null) {
            var5 = var0.getDescription();
         }

         return new ModelMBeanAttributeInfo(var0.getName(), var0.getType(), var5, var0.isReadable(), var0.isWritable(), var0.isIs(), var4);
      }
   }

   private static ModelMBeanConstructorInfo[] localizeModelMBeanConstructors(MBeanConstructorInfo[] var0, Locale var1, String var2, String var3) {
      if (var0 == null) {
         return null;
      } else {
         ModelMBeanConstructorInfo[] var4 = new ModelMBeanConstructorInfo[var0.length];

         for(int var5 = 0; var5 < var0.length; ++var5) {
            var4[var5] = localizeModelMBeanConstructor((ModelMBeanConstructorInfo)ModelMBeanConstructorInfo.class.cast(var0[var5]), var1, var2, var3);
         }

         return var4;
      }
   }

   static ModelMBeanConstructorInfo localizeModelMBeanConstructor(ModelMBeanConstructorInfo var0, Locale var1, String var2, String var3) {
      if (var0 == null) {
         return null;
      } else {
         Descriptor var4 = GenericMBeanInfoLocalizationHelper.localizeDescriptor(var0, var1, var2);
         String var5 = GenericMBeanInfoLocalizationHelper.localizeDescription(var0.getDescriptor(), var1, var2, var3 + var0.getName());
         if (var5 == null) {
            var5 = var0.getDescription();
         }

         MBeanParameterInfo[] var6 = var0.getSignature();
         MBeanParameterInfo[] var7 = GenericMBeanInfoLocalizationHelper.localizeParameters(var6, var1, var2, var3 + var0.getName() + ".");
         return new ModelMBeanConstructorInfo(var0.getName(), var5, var7, var4);
      }
   }

   private static ModelMBeanOperationInfo[] localizeModelMBeanOperations(MBeanOperationInfo[] var0, Locale var1, String var2, String var3) {
      if (var0 == null) {
         return null;
      } else {
         ModelMBeanOperationInfo[] var4 = new ModelMBeanOperationInfo[var0.length];

         for(int var5 = 0; var5 < var0.length; ++var5) {
            var4[var5] = localizeModelMBeanOperation((ModelMBeanOperationInfo)ModelMBeanOperationInfo.class.cast(var0[var5]), var1, var2, var3);
         }

         return var4;
      }
   }

   static ModelMBeanOperationInfo localizeModelMBeanOperation(ModelMBeanOperationInfo var0, Locale var1, String var2, String var3) {
      if (var0 == null) {
         return null;
      } else {
         Descriptor var4 = GenericMBeanInfoLocalizationHelper.localizeDescriptor(var0, var1, var2);
         String var5 = GenericMBeanInfoLocalizationHelper.localizeDescription(var0.getDescriptor(), var1, var2, var3 + var0.getName());
         if (var5 == null) {
            var5 = var0.getDescription();
         }

         MBeanParameterInfo[] var6 = var0.getSignature();
         MBeanParameterInfo[] var7 = GenericMBeanInfoLocalizationHelper.localizeParameters(var6, var1, var2, var3 + var0.getName() + ".");
         return new ModelMBeanOperationInfo(var0.getName(), var5, var7, var0.getReturnType(), var0.getImpact(), var4);
      }
   }

   private static ModelMBeanNotificationInfo[] localizeModelMBeanNotifications(MBeanNotificationInfo[] var0, Locale var1, String var2, String var3) {
      if (var0 == null) {
         return null;
      } else {
         ModelMBeanNotificationInfo[] var4 = new ModelMBeanNotificationInfo[var0.length];

         for(int var5 = 0; var5 < var0.length; ++var5) {
            var4[var5] = localizeModelMBeanNotification((ModelMBeanNotificationInfo)ModelMBeanNotificationInfo.class.cast(var0[var5]), var1, var2, var3);
         }

         return var4;
      }
   }

   static ModelMBeanNotificationInfo localizeModelMBeanNotification(ModelMBeanNotificationInfo var0, Locale var1, String var2, String var3) {
      if (var0 == null) {
         return null;
      } else {
         Descriptor var4 = GenericMBeanInfoLocalizationHelper.localizeDescriptor(var0, var1, var2);
         String var5 = GenericMBeanInfoLocalizationHelper.localizeDescription(var0.getDescriptor(), var1, var2, var3 + var0.getName());
         if (var5 == null) {
            var5 = var0.getDescription();
         }

         return new ModelMBeanNotificationInfo(var0.getNotifTypes(), var0.getName(), var5, var4);
      }
   }
}
