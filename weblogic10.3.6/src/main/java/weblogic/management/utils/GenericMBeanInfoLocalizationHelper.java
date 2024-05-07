package weblogic.management.utils;

import java.util.Arrays;
import java.util.Locale;
import java.util.MissingResourceException;
import javax.management.Descriptor;
import javax.management.DescriptorRead;
import javax.management.ImmutableDescriptor;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.modelmbean.DescriptorSupport;
import javax.management.modelmbean.ModelMBeanAttributeInfo;
import javax.management.modelmbean.ModelMBeanConstructorInfo;
import javax.management.modelmbean.ModelMBeanNotificationInfo;
import javax.management.modelmbean.ModelMBeanOperationInfo;
import javax.management.openmbean.OpenMBeanAttributeInfoSupport;
import javax.management.openmbean.OpenMBeanConstructorInfoSupport;
import javax.management.openmbean.OpenMBeanOperationInfoSupport;
import javax.management.openmbean.OpenMBeanParameterInfoSupport;
import weblogic.diagnostics.debug.DebugLogger;

class GenericMBeanInfoLocalizationHelper {
   public static final String DESCRIPTION_RESOURCE_BUNDLE_BASE_NAME = "descriptionResourceBundleBaseName";
   public static final String DESCRIPTION_RESOURCE_KEY = "descriptionResourceKey";
   public static final String DESCRIPTION_DISPLAY_NAME_KEY = "descriptionDisplayNameKey";
   private static DebugLogger DEBUG_LOGGER = DebugLogger.getDebugLogger("DebugMBeanLocalization");

   static String localizeDescription(Descriptor var0, Locale var1, String var2, String var3) {
      String var4 = null;
      if (var0 != null) {
         String var5 = (String)var0.getFieldValue("descriptionResourceBundleBaseName");
         String var6 = (String)var0.getFieldValue("descriptionResourceKey");
         if (var5 == null || var5.length() == 0) {
            var5 = var2;
         }

         if (var6 == null || var6.length() == 0) {
            var6 = var3;
         }

         if (var5 != null && var5.length() > 0 && var6 != null && var6.length() > 0) {
            MessageLocalizationHelper var7 = null;

            try {
               var7 = new MessageLocalizationHelper(var5, var1);
            } catch (MissingResourceException var10) {
               if (DEBUG_LOGGER.isDebugEnabled()) {
                  DEBUG_LOGGER.debug("GenericMBeanInfoLocalizationHelper.localizeDescription(): : no resource bundle can be found for the specified base name " + var5);
               }
            }

            if (var7 != null) {
               try {
                  var4 = var7.getLocalizedMessage(var6);
               } catch (MissingResourceException var9) {
                  if (DEBUG_LOGGER.isDebugEnabled()) {
                     DEBUG_LOGGER.debug("GenericMBeanInfoLocalizationHelper.localizeDescription(): : no object can be found for the given key " + var6);
                  }
               }
            }
         }
      }

      return var4;
   }

   static Descriptor localizeDescriptor(DescriptorRead var0, Locale var1, String var2) {
      Descriptor var3 = var0.getDescriptor();
      String var4 = (String)var3.getFieldValue("descriptionDisplayNameKey");
      if (var4 != null && var4.length() != 0) {
         String var5 = (String)var3.getFieldValue("descriptionResourceBundleBaseName");
         if (var5 == null || var5.length() == 0) {
            var5 = var2;
         }

         if (var2 != null && var2.length() != 0) {
            MessageLocalizationHelper var6 = null;

            try {
               var6 = new MessageLocalizationHelper(var5, var1);
            } catch (MissingResourceException var12) {
               if (DEBUG_LOGGER.isDebugEnabled()) {
                  DEBUG_LOGGER.debug("MBeanInfoLocalizationHelper.localizeDescriptor(): : no resource bundle can be found for the specified base name " + var5);
               }
            }

            String var7 = null;
            if (var6 != null) {
               try {
                  var7 = var6.getLocalizedMessage(var4);
               } catch (MissingResourceException var11) {
                  if (DEBUG_LOGGER.isDebugEnabled()) {
                     DEBUG_LOGGER.debug("MBeanInfoLocalizationHelper.localizeDescriptor(): : no object can be found for the given key " + var4);
                  }
               }
            }

            String[] var8 = var3.getFieldNames();
            if (var7 != null && var3.getFieldValue("displayName") == null && var3.getFieldValue("displayname") == null) {
               var8 = (String[])Arrays.copyOf(var8, var8.length + 1);
               var8[var8.length - 1] = "displayName";
            }

            Object[] var9 = new Object[var8.length];

            for(int var10 = 0; var10 < var8.length; ++var10) {
               var9[var10] = var3.getFieldValue(var8[var10]);
               if ("displayname".equals(var8[var10])) {
                  var8[var10] = "displayName";
               }

               if (var7 != null && "displayName".equals(var8[var10])) {
                  var9[var10] = var7;
               }
            }

            Object var13 = null;
            if (var3 instanceof ImmutableDescriptor) {
               var13 = new ImmutableDescriptor(var8, var9);
            } else {
               var13 = new DescriptorSupport(var8, var9);
            }

            return (Descriptor)var13;
         } else {
            return var3;
         }
      } else {
         return var3;
      }
   }

   static MBeanInfo localizeGenericMBeanInfo(MBeanInfo var0, Locale var1) {
      Descriptor var2 = var0.getDescriptor();
      String var3 = (String)var2.getFieldValue("descriptionResourceBundleBaseName");
      if ((var3 == null || var3.length() == 0) && MBeanInfoLocalizationHelper.getDefaultResourceBundle(var0, var1) != null) {
         var3 = MBeanInfoLocalizationHelper.getDefaultResourceBundleName(var0);
      }

      String var4 = MBeanInfoLocalizationHelper.getBaseDefaultResourceKey(var0);
      String var5 = var4 + "mbean";
      String var6 = localizeDescription(var0.getDescriptor(), var1, var3, var5);
      if (var6 == null) {
         var6 = var0.getDescription();
      }

      var2 = localizeDescriptor(var0, var1, var3);
      MBeanAttributeInfo[] var7 = var0.getAttributes();
      MBeanConstructorInfo[] var8 = var0.getConstructors();
      MBeanOperationInfo[] var9 = var0.getOperations();
      MBeanNotificationInfo[] var10 = var0.getNotifications();
      MBeanAttributeInfo[] var11 = localizeAttributes(var7, var1, var3, var4 + "attribute.");
      MBeanConstructorInfo[] var12 = localizeConstructors(var8, var1, var3, var4 + "constructor.");
      MBeanOperationInfo[] var13 = localizeOperations(var9, var1, var3, var4 + "operation.");
      MBeanNotificationInfo[] var14 = localizeNotifications(var10, var1, var3, var4 + "notification.");
      return new MBeanInfo(var0.getClassName(), var6, var11, var12, var13, var14, var2);
   }

   private static MBeanAttributeInfo[] localizeAttributes(MBeanAttributeInfo[] var0, Locale var1, String var2, String var3) {
      if (var0 == null) {
         return null;
      } else {
         MBeanAttributeInfo[] var4 = new MBeanAttributeInfo[var0.length];

         for(int var5 = 0; var5 < var0.length; ++var5) {
            MBeanAttributeInfo var6 = var0[var5];
            if (var6 instanceof ModelMBeanAttributeInfo) {
               var4[var5] = ModelMBeanInfoLocalizationHelper.localizeModelMBeanAttribute((ModelMBeanAttributeInfo)ModelMBeanAttributeInfo.class.cast(var6), var1, var2, var3);
            } else if (var6 instanceof OpenMBeanAttributeInfoSupport) {
               var4[var5] = OpenMBeanInfoLocalizationHelper.localizeOpenMBeanAttribute((OpenMBeanAttributeInfoSupport)OpenMBeanAttributeInfoSupport.class.cast(var6), var1, var2, var3);
            } else {
               var4[var5] = localizeMBeanAttribute(var6, var1, var2, var3);
            }
         }

         return var4;
      }
   }

   private static MBeanAttributeInfo localizeMBeanAttribute(MBeanAttributeInfo var0, Locale var1, String var2, String var3) {
      if (var0 == null) {
         return null;
      } else {
         Descriptor var4 = localizeDescriptor(var0, var1, var2);
         String var5 = localizeDescription(var0.getDescriptor(), var1, var2, var3 + var0.getName());
         if (var5 == null) {
            var5 = var0.getDescription();
         }

         return new MBeanAttributeInfo(var0.getName(), var0.getType(), var5, var0.isReadable(), var0.isWritable(), var0.isIs(), var4);
      }
   }

   private static MBeanConstructorInfo[] localizeConstructors(MBeanConstructorInfo[] var0, Locale var1, String var2, String var3) {
      if (var0 == null) {
         return null;
      } else {
         MBeanConstructorInfo[] var4 = new MBeanConstructorInfo[var0.length];

         for(int var5 = 0; var5 < var0.length; ++var5) {
            MBeanConstructorInfo var6 = var0[var5];
            if (var6 instanceof ModelMBeanConstructorInfo) {
               var4[var5] = ModelMBeanInfoLocalizationHelper.localizeModelMBeanConstructor((ModelMBeanConstructorInfo)ModelMBeanConstructorInfo.class.cast(var6), var1, var2, var3);
            } else if (var6 instanceof OpenMBeanConstructorInfoSupport) {
               var4[var5] = OpenMBeanInfoLocalizationHelper.localizeOpenMBeanConstructor((OpenMBeanConstructorInfoSupport)OpenMBeanConstructorInfoSupport.class.cast(var6), var1, var2, var3);
            } else {
               var4[var5] = localizeMBeanConstructor(var6, var1, var2, var3);
            }
         }

         return var4;
      }
   }

   static MBeanConstructorInfo localizeMBeanConstructor(MBeanConstructorInfo var0, Locale var1, String var2, String var3) {
      if (var0 == null) {
         return null;
      } else {
         Descriptor var4 = localizeDescriptor(var0, var1, var2);
         String var5 = localizeDescription(var0.getDescriptor(), var1, var2, var3 + var0.getName());
         if (var5 == null) {
            var5 = var0.getDescription();
         }

         MBeanParameterInfo[] var6 = var0.getSignature();
         MBeanParameterInfo[] var7 = localizeParameters(var6, var1, var2, var3 + var0.getName() + ".");
         return new MBeanConstructorInfo(var0.getName(), var5, var7, var4);
      }
   }

   private static MBeanOperationInfo[] localizeOperations(MBeanOperationInfo[] var0, Locale var1, String var2, String var3) {
      if (var0 == null) {
         return null;
      } else {
         MBeanOperationInfo[] var4 = new MBeanOperationInfo[var0.length];

         for(int var5 = 0; var5 < var0.length; ++var5) {
            MBeanOperationInfo var6 = var0[var5];
            if (var6 instanceof ModelMBeanOperationInfo) {
               var4[var5] = ModelMBeanInfoLocalizationHelper.localizeModelMBeanOperation((ModelMBeanOperationInfo)ModelMBeanOperationInfo.class.cast(var6), var1, var2, var3);
            } else if (var6 instanceof OpenMBeanOperationInfoSupport) {
               var4[var5] = OpenMBeanInfoLocalizationHelper.localizeOpenMBeanOperation((OpenMBeanOperationInfoSupport)OpenMBeanOperationInfoSupport.class.cast(var6), var1, var2, var3);
            } else {
               var4[var5] = localizeMBeanOperation(var6, var1, var2, var3);
            }
         }

         return var4;
      }
   }

   static MBeanOperationInfo localizeMBeanOperation(MBeanOperationInfo var0, Locale var1, String var2, String var3) {
      if (var0 == null) {
         return null;
      } else {
         Descriptor var4 = localizeDescriptor(var0, var1, var2);
         String var5 = localizeDescription(var0.getDescriptor(), var1, var2, var3 + var0.getName());
         if (var5 == null) {
            var5 = var0.getDescription();
         }

         MBeanParameterInfo[] var6 = var0.getSignature();
         MBeanParameterInfo[] var7 = localizeParameters(var6, var1, var2, var3 + var0.getName() + ".");
         return new MBeanOperationInfo(var0.getName(), var5, var7, var0.getReturnType(), var0.getImpact(), var4);
      }
   }

   static MBeanNotificationInfo[] localizeNotifications(MBeanNotificationInfo[] var0, Locale var1, String var2, String var3) {
      if (var0 == null) {
         return null;
      } else {
         MBeanNotificationInfo[] var4 = new MBeanNotificationInfo[var0.length];

         for(int var5 = 0; var5 < var0.length; ++var5) {
            MBeanNotificationInfo var6 = var0[var5];
            if (var6 instanceof ModelMBeanNotificationInfo) {
               var4[var5] = ModelMBeanInfoLocalizationHelper.localizeModelMBeanNotification((ModelMBeanNotificationInfo)ModelMBeanNotificationInfo.class.cast(var6), var1, var2, var3);
            } else {
               var4[var5] = localizeMBeanNotification(var6, var1, var2, var3);
            }
         }

         return var4;
      }
   }

   private static MBeanNotificationInfo localizeMBeanNotification(MBeanNotificationInfo var0, Locale var1, String var2, String var3) {
      if (var0 == null) {
         return null;
      } else {
         Descriptor var4 = localizeDescriptor(var0, var1, var2);
         String var5 = localizeDescription(var0.getDescriptor(), var1, var2, var3 + var0.getName());
         if (var5 == null) {
            var5 = var0.getDescription();
         }

         return new MBeanNotificationInfo(var0.getNotifTypes(), var0.getName(), var5, var4);
      }
   }

   static MBeanParameterInfo[] localizeParameters(MBeanParameterInfo[] var0, Locale var1, String var2, String var3) {
      if (var0 == null) {
         return null;
      } else {
         MBeanParameterInfo[] var4 = new MBeanParameterInfo[var0.length];

         for(int var5 = 0; var5 < var0.length; ++var5) {
            MBeanParameterInfo var6 = var0[var5];
            if (var6 instanceof OpenMBeanParameterInfoSupport) {
               var4[var5] = OpenMBeanInfoLocalizationHelper.localizeOpenMBeanParameter((OpenMBeanParameterInfoSupport)OpenMBeanParameterInfoSupport.class.cast(var6), var1, var2, var3);
            } else {
               var4[var5] = localizeMBeanParameter(var6, var1, var2, var3);
            }
         }

         return var4;
      }
   }

   private static MBeanParameterInfo localizeMBeanParameter(MBeanParameterInfo var0, Locale var1, String var2, String var3) {
      if (var0 == null) {
         return null;
      } else {
         Descriptor var4 = localizeDescriptor(var0, var1, var2);
         String var5 = localizeDescription(var0.getDescriptor(), var1, var2, var3 + var0.getName());
         if (var5 == null) {
            var5 = var0.getDescription();
         }

         return new MBeanParameterInfo(var0.getName(), var0.getType(), var5, var4);
      }
   }
}
