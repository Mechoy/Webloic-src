package weblogic.management.tools;

import java.lang.reflect.Array;
import javax.management.MBeanAttributeInfo;
import weblogic.management.info.ExtendedAttributeInfo;

public class AttributeInfo extends MBeanAttributeInfo implements ExtendedAttributeInfo {
   static final long serialVersionUID = 1876948055092590334L;
   private static String[] primitiveTypes = new String[]{"byte", "short", "int", "long", "float", "double", "boolean"};
   transient Class typeClass;
   transient Class collectionTypeClass;
   private String collectionType = null;
   private Object clientDefault = null;
   private boolean configurable = true;
   private boolean isIs = false;
   private String oldProp = null;
   private boolean dynamic = false;
   private Object[] legalValues = null;
   private boolean isLegalValuesExtensible = false;
   private Long legalMax = null;
   private Long legalMin = null;
   private boolean legalNull = true;
   private Object defaultValue = null;
   private Object productionModeDefaultValue = null;
   private boolean isExcluded = false;
   private boolean isEncrypted = false;
   private String units = null;
   private String[] legalChecks = null;
   private String[] legalResponses = null;
   private Integer protectionLevel = null;
   private boolean overrideDynamic = false;
   private boolean deploymentDescriptor = false;
   private boolean isContained = false;
   private transient Boolean productionModeEnabled;
   private transient boolean initializedClasses = false;

   public AttributeInfo(String var1, String var2, String var3, Object var4, Object var5, Object var6, String var7, boolean var8, boolean var9, boolean var10, boolean var11, String var12, boolean var13, String[] var14, Object[] var15, boolean var16, Long var17, Long var18, boolean var19, boolean var20, boolean var21, String var22, String[] var23, Integer var24, boolean var25, boolean var26, boolean var27) {
      super(var1, var2, var3, var8, var9, var11);
      this.defaultValue = var4;
      this.productionModeDefaultValue = var5;
      this.clientDefault = var6;
      this.configurable = var10;
      this.collectionType = var7;
      this.oldProp = var12;
      this.isIs = var11;
      this.dynamic = var13;
      this.legalChecks = var14;
      this.legalValues = var15;
      this.isLegalValuesExtensible = var16;
      this.legalMax = var17;
      this.legalMin = var18;
      this.legalNull = var19;
      this.isExcluded = var20;
      this.isEncrypted = var21;
      this.units = var22;
      this.legalResponses = var23;
      this.protectionLevel = var24;
      this.overrideDynamic = var25;
      this.deploymentDescriptor = var26;
      this.isContained = var27;
      if (isPrimitive(var2)) {
         this.legalNull = false;
      }

      this.initializeClasses();
   }

   private static boolean isPrimitive(String var0) {
      if (var0 != null && var0.length() != 0) {
         for(int var1 = 0; var1 < primitiveTypes.length; ++var1) {
            if (var0.equals(primitiveTypes[var1])) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public Object getDefaultValue() {
      return this.defaultValue;
   }

   public Object getProductionModeDefaultValue() {
      return this.productionModeDefaultValue != null ? this.productionModeDefaultValue : this.defaultValue;
   }

   public Class getTypeClass() {
      if (!this.initializedClasses) {
         this.initializeClasses();
      }

      return this.typeClass;
   }

   public Class getCollectionTypeClass() {
      if (!this.initializedClasses) {
         this.initializeClasses();
      }

      return this.collectionTypeClass;
   }

   public boolean isDynamic() {
      return this.dynamic;
   }

   public String getLegalCheck() {
      return this.legalChecks != null && this.legalChecks.length > 0 ? this.legalChecks[0] : null;
   }

   public Object[] getLegalValues() {
      return this.legalValues;
   }

   public Long getLegalMax() {
      return this.legalMax;
   }

   public Long getLegalMin() {
      return this.legalMin;
   }

   public boolean getLegalNull() {
      return this.legalNull;
   }

   public Object getClientDefault() {
      return this.clientDefault;
   }

   public boolean isConfigurable() {
      return this.configurable;
   }

   public String getOldProp() {
      return this.oldProp;
   }

   public boolean isIs() {
      return this.isIs;
   }

   public boolean isContained() {
      return this.isContained;
   }

   public boolean isExcluded() {
      return this.isExcluded;
   }

   public boolean isEncrypted() {
      return this.isEncrypted;
   }

   public String getUnits() {
      return this.units;
   }

   public boolean isLegalValuesExtensible() {
      return this.isLegalValuesExtensible;
   }

   public String[] getLegalChecks() {
      return this.legalChecks;
   }

   public String[] getLegalResponses() {
      return this.legalResponses;
   }

   public String getLegalResponse() {
      return this.legalResponses != null && this.legalResponses.length > 0 ? this.legalResponses[0] : null;
   }

   public Integer getProtectionLevel() {
      return this.protectionLevel;
   }

   public boolean isOverrideDynamic() {
      return this.overrideDynamic;
   }

   public boolean isDeploymentDescriptor() {
      return this.deploymentDescriptor;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer("name=" + this.getName());
      var1.append(", type=" + this.getType());
      var1.append(", readable=" + this.isReadable());
      var1.append(", writable=" + this.isWritable());
      var1.append(", dynamic=" + this.isDynamic());
      var1.append(", configurable=" + this.isConfigurable());
      var1.append(", encrypted=" + this.isEncrypted());
      var1.append(", protection level=" + this.getProtectionLevel());
      var1.append(", overrideDynamic=" + this.isOverrideDynamic());
      var1.append(", deploymentDescriptor=" + this.isDeploymentDescriptor());
      var1.append(", oldprop=" + this.oldProp);
      String var2 = "<null>";
      if (this.defaultValue != null) {
         var2 = this.defaultValue.toString();
      }

      var1.append(", defaultValue=[" + var2 + "]");
      var1.append(", production-mode-default=[" + this.productionModeDefaultValue + "]");
      var1.append(", legalValuesExtensible=" + this.isLegalValuesExtensible());
      int var3;
      if (this.legalChecks != null && this.legalChecks.length > 0) {
         for(var3 = 0; var3 < this.legalChecks.length; ++var3) {
            var1.append(", @legal-" + (new Integer(var3)).toString() + "=" + this.legalChecks[var3]);
         }
      } else {
         var1.append(", @legal=" + var2);
      }

      if (this.legalResponses != null && this.legalResponses.length > 0) {
         for(var3 = 0; var3 < this.legalResponses.length; ++var3) {
            var1.append(", @legalResponse-" + (new Integer(var3)).toString() + "=" + this.legalResponses[var3]);
         }
      } else {
         var1.append(", @legalResponse=" + var2);
      }

      if (this.defaultValue != null) {
         var2 = this.defaultValue.toString();
      }

      var1.append(", max: " + this.legalMax);
      var1.append(", min: " + this.legalMin);
      var1.append(", null ok: " + this.legalNull);
      if (this.legalValues != null) {
         var1.append("legalValues: ");

         for(var3 = 0; var3 < this.legalValues.length; ++var3) {
            var1.append("" + this.legalValues[var3]);
            if (var3 != this.legalValues.length - 1) {
               var1.append(",");
            }
         }
      }

      return var1.toString();
   }

   private void initializeClasses() {
      if (!this.initializedClasses) {
         try {
            this.typeClass = AttributeInfo.Helper.findClass(this.getType());
            if (this.collectionType != null) {
               this.collectionTypeClass = AttributeInfo.Helper.findClass(this.collectionType);
            }

            this.initializedClasses = true;
         } catch (ClassNotFoundException var2) {
            throw new AssertionError(var2);
         }
      }
   }

   public static class Helper {
      public static Class findClass(String var0) throws ClassNotFoundException {
         if (var0.equals(Long.class.getName())) {
            return Long.TYPE;
         } else if (var0.equals(Double.class.getName())) {
            return Double.TYPE;
         } else if (var0.equals(Float.class.getName())) {
            return Float.TYPE;
         } else if (var0.equals(Integer.class.getName())) {
            return Integer.TYPE;
         } else if (var0.equals(Character.class.getName())) {
            return Character.TYPE;
         } else if (var0.equals(Short.class.getName())) {
            return Short.TYPE;
         } else if (var0.equals(Byte.class.getName())) {
            return Byte.TYPE;
         } else if (var0.equals(Boolean.class.getName())) {
            return Boolean.TYPE;
         } else if (var0.equals(Void.class.getName())) {
            return Void.TYPE;
         } else if (var0.equals("long")) {
            return Long.TYPE;
         } else if (var0.equals("double")) {
            return Double.TYPE;
         } else if (var0.equals("float")) {
            return Float.TYPE;
         } else if (var0.equals("int")) {
            return Integer.TYPE;
         } else if (var0.equals("char")) {
            return Character.TYPE;
         } else if (var0.equals("short")) {
            return Short.TYPE;
         } else if (var0.equals("byte")) {
            return Byte.TYPE;
         } else if (var0.equals("boolean")) {
            return Boolean.TYPE;
         } else if (var0.equals("void")) {
            return Void.TYPE;
         } else if (var0.endsWith("[]")) {
            Class var1 = findClass(var0.substring(0, var0.length() - 2));
            return Array.newInstance(var1, 0).getClass();
         } else {
            return Class.forName(var0);
         }
      }

      public static Class wrapClass(Class var0) {
         if (var0 == Long.TYPE) {
            return Long.class;
         } else if (var0 == Double.TYPE) {
            return Double.class;
         } else if (var0 == Float.TYPE) {
            return Float.class;
         } else if (var0 == Integer.TYPE) {
            return Integer.class;
         } else if (var0 == Character.TYPE) {
            return Character.class;
         } else if (var0 == Short.TYPE) {
            return Short.class;
         } else if (var0 == Byte.TYPE) {
            return Byte.class;
         } else {
            return var0 == Boolean.TYPE ? Boolean.class : var0;
         }
      }

      public static String trimPackage(String var0) {
         int var1 = var0.lastIndexOf(46);
         int var2 = var0.length();
         if (var1 != -1) {
            var0 = var0.substring(var1 + 1, var2);
         }

         return var0;
      }
   }
}
