package weblogic.diagnostics.harvester.internal;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import javax.management.DynamicMBean;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.descriptor.WLDFHarvestedTypeBean;
import weblogic.diagnostics.harvester.AttributeNameNormalizer;
import weblogic.diagnostics.harvester.HarvesterException;
import weblogic.diagnostics.harvester.I18NConstants;
import weblogic.diagnostics.harvester.InstanceNameNormalizer;
import weblogic.diagnostics.harvester.InvalidHarvesterInstanceNameException;
import weblogic.management.jmx.modelmbean.WLSModelMBean;

public class Validators implements I18NConstants {
   private static final DebugLogger DBG = DebugSupport.getDebugLogger();
   private static final DebugLogger DBG2 = DebugSupport.getLowLevelDebugLogger();
   private static final HarvesterDefaultAttributeNormalizer DEFAULT_NORMALIZER = new HarvesterDefaultAttributeNormalizer();

   public static void validateNamespace(String var0) throws IllegalArgumentException {
      if (!var0.equals("ServerRuntime") && !var0.equals("DomainRuntime")) {
         throw new IllegalArgumentException(var0);
      }
   }

   public static String getDefaultMetricNamespace() {
      return "ServerRuntime";
   }

   public static void validateHarvestedTypeBean(WLDFHarvestedTypeBean var0) throws IllegalArgumentException {
      String var1 = var0.getName();
      if (DBG.isDebugEnabled()) {
         DBG.debug("Doing bean-level velidation for configured type: " + var1);
      }

      int var2 = isTypeHandled(var1);
      if (var0.isKnownType() && var2 != 2) {
         throw new IllegalArgumentException(new HarvesterException.TypeNotHarvestable(var1));
      }
   }

   public static void validateConfiguredType(String var0) throws IllegalArgumentException {
      if (DBG.isDebugEnabled()) {
         DBG.debug("Validating configured type name: " + var0);
      }

      if (isTypeHandled(var0) == -1) {
         throw new IllegalArgumentException(new HarvesterException.TypeNotHarvestable(var0));
      }
   }

   public static void validateConfiguredAttributes(String var0, String[] var1) throws IllegalArgumentException {
      if (DBG.isDebugEnabled()) {
         DBG.debug("Validating configured attributes for type " + var0 + ":  " + stringArraytoString(var1));
      }

      if (var1 != null && var1.length != 0) {
         ArrayList var2 = new ArrayList();
         BeanInfo var3 = TreeBeanHarvestableDataProviderHelper.getBeanInfo(var0);
         String var8;
         if (var3 == null) {
            if (DBG.isDebugEnabled()) {
               DBG.debug("Unknown type, applying default normalizer");
            }

            String[] var4 = var1;
            int var5 = var1.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               String var7 = var4[var6];
               if (var7 != null && var7.length() > 0) {
                  try {
                     var8 = DEFAULT_NORMALIZER.getNormalizedAttributeName(var7);
                     if (DBG.isDebugEnabled()) {
                        DBG.debug("Normalizing " + var7 + ", result:  " + var8);
                     }
                  } catch (Exception var14) {
                     var2.add(var7);
                  }
               }
            }
         } else {
            if (DBG.isDebugEnabled()) {
               DBG.debug("Known type, looking up and applying any normalizers");
            }

            PropertyDescriptor[] var15 = var3.getPropertyDescriptors();
            HashSet var16 = new HashSet();
            HashMap var17 = new HashMap();

            String var12;
            int var18;
            for(var18 = 0; var18 < var15.length; ++var18) {
               PropertyDescriptor var19 = var15[var18];
               Boolean var9 = (Boolean)var19.getValue("unharvestable");
               boolean var10 = var9 != null ? !var9 : true;
               if (var10) {
                  String var11 = var19.getName();
                  if (!typeIsComplex(var19)) {
                     var16.add(var11);
                  } else {
                     var12 = (String)var19.getValue("harvesterAttributeNormalizerClass");
                     if (var12 == null) {
                        var12 = HarvesterDefaultAttributeNormalizer.class.getName();
                     }

                     var17.put(var11, var12);
                  }
               }
            }

            for(var18 = 0; var18 < var1.length; ++var18) {
               var8 = var1[var18];
               if (var8 != null && var8.length() != 0 && !var16.contains(var8)) {
                  String var20 = DEFAULT_NORMALIZER.getAttributeName(var8);
                  String var21 = (String)var17.get(var20);
                  if (var21 == null) {
                     var2.add(var8);
                  } else {
                     try {
                        AttributeNameNormalizer var22 = (AttributeNameNormalizer)Class.forName(var21).newInstance();
                        var12 = var22.getNormalizedAttributeName(var8);
                        if (DBG.isDebugEnabled()) {
                           DBG.debug("The normalized name is: " + var8 + ":  " + var12);
                        }
                     } catch (Exception var13) {
                        var2.add(var8);
                     }
                  }
               }
            }
         }

         if (var2.size() > 0) {
            if (DBG.isDebugEnabled()) {
               DBG.debug("The following attributes are invalid for type: " + var0 + ":  " + var2);
            }

            throw new HarvesterException.ValidationError(ATTRIBUTES_I18N, var0, var2.toString());
         } else {
            if (DBG2.isDebugEnabled()) {
               DBG2.debug("Attributes for type: " + var0 + " have been successfully validated.");
            }

         }
      }
   }

   private static boolean typeIsComplex(PropertyDescriptor var0) {
      Class var1 = var0.getPropertyType();
      if (var1.isPrimitive()) {
         return false;
      } else if (var1 != String.class && var1 != Boolean.class && var1 != Character.class) {
         return !Number.class.isAssignableFrom(var1);
      } else {
         return false;
      }
   }

   public static void validateConfiguredInstances(String[] var0) throws IllegalArgumentException {
      if (DBG.isDebugEnabled()) {
         DBG.debug("Validating configured instances:  " + stringArraytoString(var0));
      }

      String[] var1 = var0;
      int var2 = var0.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         String var4 = var1[var3];
         InstanceNameNormalizer var5 = new InstanceNameNormalizer(var4);

         try {
            var5.translateHarvesterSpec();
         } catch (InvalidHarvesterInstanceNameException var7) {
            throw new IllegalArgumentException(var7);
         }
      }

      if (DBG2.isDebugEnabled()) {
         DBG2.debug("Instances have been successfully validated.");
      }

   }

   private static String stringArraytoString(String[] var0) {
      if (var0 == null) {
         return "[]";
      } else {
         String var1 = "[";

         for(int var2 = 0; var2 < var0.length; ++var2) {
            String var3 = var0[var2];
            if (var2 != 0) {
               var1 = var1 + ",";
            }

            var1 = var1 + var3;
         }

         var1 = var1 + "]";
         return var1;
      }
   }

   private static int isTypeHandled(String var0) {
      boolean var1 = false;
      if (var0 != null && var0.length() != 0) {
         BeanTreeHarvesterImpl var2 = BeanTreeHarvesterImpl.getInstance();
         int var7 = var2.isTypeHandled(var0);
         if (var7 != 2) {
            Class var3 = null;

            try {
               var3 = Class.forName(var0);
            } catch (ClassNotFoundException var6) {
               var7 = 1;
            }

            if (var3 != null) {
               boolean var4 = WLSModelMBean.class.isAssignableFrom(var3);
               if (var4) {
                  var7 = 2;
               } else {
                  boolean var5 = DynamicMBean.class.isAssignableFrom(var3);
                  if (var5) {
                     var7 = 1;
                  } else if (!isStandardMBean(var3) && !isMXBean(var3)) {
                     var7 = -1;
                  } else {
                     var7 = 1;
                  }
               }
            }
         }

         if (DBG.isDebugEnabled()) {
            DBG.debug("Validator has has voted " + (var7 == 2 ? "yes" : (var7 == -1 ? "no" : "maybe")) + " for type " + var0);
         }

         return var7;
      } else {
         throw new HarvesterException.NullName(TYPE_I18N);
      }
   }

   private static boolean isStandardMBean(Class<?> var0) {
      String var1 = var0.getName() + "MBean";

      try {
         var0 = Class.forName(var1);
         if (var0.isInterface()) {
            return true;
         }
      } catch (ClassNotFoundException var3) {
      }

      return false;
   }

   private static boolean isMXBean(Class<?> var0) {
      Class[] var1 = var0.getInterfaces();
      Class[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Class var5 = var2[var4];
         if (var5.getCanonicalName().endsWith("MXBean")) {
            return true;
         }
      }

      return false;
   }
}
