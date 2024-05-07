package weblogic.management.utils;

import java.util.Locale;
import javax.management.Descriptor;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.openmbean.OpenMBeanAttributeInfo;
import javax.management.openmbean.OpenMBeanAttributeInfoSupport;
import javax.management.openmbean.OpenMBeanConstructorInfo;
import javax.management.openmbean.OpenMBeanConstructorInfoSupport;
import javax.management.openmbean.OpenMBeanInfoSupport;
import javax.management.openmbean.OpenMBeanOperationInfo;
import javax.management.openmbean.OpenMBeanOperationInfoSupport;
import javax.management.openmbean.OpenMBeanParameterInfo;
import javax.management.openmbean.OpenMBeanParameterInfoSupport;

public class OpenMBeanInfoLocalizationHelper {
   static MBeanInfo localizeOpenMBeanInfo(OpenMBeanInfoSupport var0, Locale var1) {
      Descriptor var2 = var0.getDescriptor();
      String var3 = (String)var2.getFieldValue("descriptionResourceBundleBaseName");
      if ((var3 == null || var3.length() == 0) && MBeanInfoLocalizationHelper.getDefaultResourceBundle(var0, var1) != null) {
         var3 = MBeanInfoLocalizationHelper.getDefaultResourceBundleName(var0);
      }

      String var4 = MBeanInfoLocalizationHelper.getBaseDefaultResourceKey(var0);
      String var5 = var4 + "mbean";
      String var6 = GenericMBeanInfoLocalizationHelper.localizeDescription(var2, var1, var3, var5);
      if (var6 == null) {
         var6 = var0.getDescription();
      }

      var2 = GenericMBeanInfoLocalizationHelper.localizeDescriptor(var0, var1, var3);
      MBeanAttributeInfo[] var7 = var0.getAttributes();
      MBeanConstructorInfo[] var8 = var0.getConstructors();
      MBeanOperationInfo[] var9 = var0.getOperations();
      MBeanNotificationInfo[] var10 = var0.getNotifications();
      OpenMBeanAttributeInfo[] var11 = localizeOpenMBeanAttributes(var7, var1, var3, var4 + "attribute.");
      OpenMBeanConstructorInfo[] var12 = localizeOpenMBeanConstructors(var8, var1, var3, var4 + "constructor.");
      OpenMBeanOperationInfo[] var13 = localizeOpenMBeanOperations(var9, var1, var3, var4 + "operation.");
      MBeanNotificationInfo[] var14 = GenericMBeanInfoLocalizationHelper.localizeNotifications(var10, var1, var3, var4 + "notification.");
      return new OpenMBeanInfoSupport(var0.getClassName(), var6, var11, var12, var13, var14, var2);
   }

   private static OpenMBeanAttributeInfo[] localizeOpenMBeanAttributes(MBeanAttributeInfo[] var0, Locale var1, String var2, String var3) {
      if (var0 == null) {
         return null;
      } else {
         OpenMBeanAttributeInfo[] var4 = new OpenMBeanAttributeInfo[var0.length];

         for(int var5 = 0; var5 < var0.length; ++var5) {
            OpenMBeanAttributeInfoSupport var6 = (OpenMBeanAttributeInfoSupport)OpenMBeanAttributeInfoSupport.class.cast(var0[var5]);
            var4[var5] = localizeOpenMBeanAttribute(var6, var1, var2, var3);
         }

         return var4;
      }
   }

   static OpenMBeanAttributeInfoSupport localizeOpenMBeanAttribute(OpenMBeanAttributeInfoSupport var0, Locale var1, String var2, String var3) {
      if (var0 == null) {
         return null;
      } else {
         Descriptor var4 = GenericMBeanInfoLocalizationHelper.localizeDescriptor(var0, var1, var2);
         String var5 = GenericMBeanInfoLocalizationHelper.localizeDescription(var0.getDescriptor(), var1, var2, var3 + var0.getName());
         if (var5 == null) {
            var5 = var0.getDescription();
         }

         return new OpenMBeanAttributeInfoSupport(var0.getName(), var5, var0.getOpenType(), var0.isReadable(), var0.isWritable(), var0.isIs(), var4);
      }
   }

   private static OpenMBeanConstructorInfo[] localizeOpenMBeanConstructors(MBeanConstructorInfo[] var0, Locale var1, String var2, String var3) {
      if (var0 == null) {
         return null;
      } else {
         OpenMBeanConstructorInfo[] var4 = new OpenMBeanConstructorInfo[var0.length];

         for(int var5 = 0; var5 < var0.length; ++var5) {
            OpenMBeanConstructorInfoSupport var6 = (OpenMBeanConstructorInfoSupport)OpenMBeanConstructorInfoSupport.class.cast(var0[var5]);
            var4[var5] = localizeOpenMBeanConstructor(var6, var1, var2, var3);
         }

         return var4;
      }
   }

   static OpenMBeanConstructorInfoSupport localizeOpenMBeanConstructor(OpenMBeanConstructorInfoSupport var0, Locale var1, String var2, String var3) {
      if (var0 == null) {
         return null;
      } else {
         Descriptor var4 = GenericMBeanInfoLocalizationHelper.localizeDescriptor(var0, var1, var2);
         String var5 = GenericMBeanInfoLocalizationHelper.localizeDescription(var0.getDescriptor(), var1, var2, var3 + var0.getName());
         if (var5 == null) {
            var5 = var0.getDescription();
         }

         MBeanParameterInfo[] var6 = var0.getSignature();
         OpenMBeanParameterInfo[] var7 = localizeOpenMBeanParameters(var6, var1, var2, var3 + var0.getName() + ".");
         return new OpenMBeanConstructorInfoSupport(var0.getName(), var5, var7, var4);
      }
   }

   private static OpenMBeanOperationInfo[] localizeOpenMBeanOperations(MBeanOperationInfo[] var0, Locale var1, String var2, String var3) {
      if (var0 == null) {
         return null;
      } else {
         OpenMBeanOperationInfo[] var4 = new OpenMBeanOperationInfo[var0.length];

         for(int var5 = 0; var5 < var0.length; ++var5) {
            OpenMBeanOperationInfoSupport var6 = (OpenMBeanOperationInfoSupport)OpenMBeanOperationInfoSupport.class.cast(var0[var5]);
            var4[var5] = localizeOpenMBeanOperation(var6, var1, var2, var3);
         }

         return var4;
      }
   }

   static OpenMBeanOperationInfoSupport localizeOpenMBeanOperation(OpenMBeanOperationInfoSupport var0, Locale var1, String var2, String var3) {
      if (var0 == null) {
         return null;
      } else {
         Descriptor var4 = GenericMBeanInfoLocalizationHelper.localizeDescriptor(var0, var1, var2);
         String var5 = GenericMBeanInfoLocalizationHelper.localizeDescription(var0.getDescriptor(), var1, var2, var3 + var0.getName());
         if (var5 == null) {
            var5 = var0.getDescription();
         }

         MBeanParameterInfo[] var6 = var0.getSignature();
         OpenMBeanParameterInfo[] var7 = localizeOpenMBeanParameters(var6, var1, var2, var3 + var0.getName() + ".");
         return new OpenMBeanOperationInfoSupport(var0.getName(), var5, var7, var0.getReturnOpenType(), var0.getImpact(), var4);
      }
   }

   private static OpenMBeanParameterInfo[] localizeOpenMBeanParameters(MBeanParameterInfo[] var0, Locale var1, String var2, String var3) {
      if (var0 == null) {
         return null;
      } else {
         OpenMBeanParameterInfo[] var4 = new OpenMBeanParameterInfo[var0.length];

         for(int var5 = 0; var5 < var0.length; ++var5) {
            OpenMBeanParameterInfoSupport var6 = (OpenMBeanParameterInfoSupport)OpenMBeanParameterInfoSupport.class.cast(var0[var5]);
            var4[var5] = localizeOpenMBeanParameter(var6, var1, var2, var3);
         }

         return var4;
      }
   }

   static OpenMBeanParameterInfoSupport localizeOpenMBeanParameter(OpenMBeanParameterInfoSupport var0, Locale var1, String var2, String var3) {
      if (var0 == null) {
         return null;
      } else {
         Descriptor var4 = GenericMBeanInfoLocalizationHelper.localizeDescriptor(var0, var1, var2);
         String var5 = GenericMBeanInfoLocalizationHelper.localizeDescription(var0.getDescriptor(), var1, var2, var3 + var0.getName());
         if (var5 == null) {
            var5 = var0.getDescription();
         }

         return new OpenMBeanParameterInfoSupport(var0.getName(), var5, var0.getOpenType(), var4);
      }
   }
}
