package weblogic.diagnostics.harvester;

import weblogic.diagnostics.type.DiagnosticException;

public abstract class HarvesterException extends DiagnosticException implements I18NConstants {
   HarvesterException() {
   }

   HarvesterException(String var1) {
      super(var1);
   }

   HarvesterException(Throwable var1) {
      super(var1);
   }

   HarvesterException(String var1, Throwable var2) {
      super(var1, var2);
   }

   private static String genStringFromArray(String[] var0) {
      String var1 = "";
      int var2 = var0.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         if (var3 != 0) {
            if (var3 == var2 - 1) {
               var1 = var1 + " and ";
            } else {
               var1 = var1 + ", ";
            }
         }

         var1 = var1 + var0[var3];
      }

      return var1;
   }

   public static class WatchTypeMetricNotFound extends HarvesterException {
      public WatchTypeMetricNotFound(String var1, String var2) {
         super(I18NSupport.formatter().getWatchTypeMetricNotFoundMessage(var1, var2));
      }
   }

   public static class WatchInstanceMetricNotFound extends HarvesterException {
      public WatchInstanceMetricNotFound(String var1, String var2, String var3) {
         super(I18NSupport.formatter().getWatchInstanceMetricNotFoundMessage(var1, var2, var3));
      }
   }

   public static class InconsistentTypesInWatchInst extends HarvesterException {
      private String instanceName;
      private String specifiedTypeName;
      private String actualTypeName;

      public InconsistentTypesInWatchInst(String var1, String var2, String var3) {
         super(I18NSupport.formatter().getInconsistentTypesInWatchInstVarMessage(var1, var2, var3));
         this.instanceName = var1;
         this.specifiedTypeName = var2;
         this.actualTypeName = var3;
      }

      public String getInstanceName() {
         return this.instanceName;
      }

      public String getSpecifiedTypeName() {
         return this.specifiedTypeName;
      }

      public String getActualTypeName() {
         return this.actualTypeName;
      }
   }

   public static class InvalidPlugInState extends IllegalStateException {
      public InvalidPlugInState(String var1, String var2) {
         super(I18NSupport.formatter().getInvalidPluginStateMessage(var1, var2));
      }
   }

   public static class ValidationError extends IllegalArgumentException {
      public ValidationError(String var1, String var2, String var3) {
         super(I18NSupport.formatter().getNotHarvestableMessage(var1, var2, var3));
      }
   }

   public static class NullName extends IllegalArgumentException {
      public NullName(String var1) {
         super(I18NSupport.formatter().getNullParamMessage(var1));
      }
   }

   public abstract static class FatalTypeException extends HarvesterException {
      public FatalTypeException(String var1) {
         super(var1);
      }

      public FatalTypeException(String var1, Throwable var2) {
         super(var1, var2);
      }
   }

   public static class HarvestingNotEnabled extends HarvesterException {
      public HarvestingNotEnabled() {
         super(I18NSupport.formatter().getHarvesterNotAvailableMessage());
      }
   }

   public static class AmbiguousTypeName extends FatalTypeException {
      private String name;
      private String[] providerNames = null;

      public String getAmbiguousName() {
         return this.name;
      }

      public String[] getProviderNames() {
         return this.providerNames;
      }

      public AmbiguousTypeName(String var1, String var2, String var3) {
         super(getExplanation(var1, var2, var3));
         this.name = this.name;
         this.providerNames = new String[]{var2, var3};
      }

      private static String getExplanation(String var0, String var1, String var2) {
         return I18NSupport.formatter().getAmbiguousTypeNameMessage(var0, var1, var2);
      }
   }

   public static class AmbiguousInstanceName extends HarvesterException {
      private String name;
      private String[] providerNames = null;
      private String[] typeNames = null;

      public String getAmbiguousName() {
         return this.name;
      }

      public String[] getProviderNames() {
         return this.providerNames;
      }

      public String[] getTypeNames() {
         return this.typeNames;
      }

      public AmbiguousInstanceName(String var1, String var2, String var3, String var4, String var5) {
         super(getExplanation(var1, var2, var3, var4, var5));
         this.name = var1;
         this.providerNames = new String[]{var2, var4};
         this.typeNames = new String[]{var3, var5};
      }

      private static String getExplanation(String var0, String var1, String var2, String var3, String var4) {
         return I18NSupport.formatter().getAmbiguousInstanceNameMessage(var0, var1, var2, var3, var4);
      }
   }

   public static class TypeNotHarvestable extends FatalTypeException {
      private String typeName;

      public String getTypeName() {
         return this.typeName;
      }

      public TypeNotHarvestable(String var1) {
         this(var1, (Throwable)null);
      }

      public TypeNotHarvestable(String var1, Throwable var2) {
         super(getExplanation(var1), var2);
         this.typeName = var1;
      }

      private static String getExplanation(String var0) {
         return I18NSupport.formatter().getItemNotHarvestableMessage(TYPE_I18N, var0);
      }
   }

   public static class MissingConfigurationType extends MissingConfiguration {
      private String typeName;

      public String getTypeName() {
         return this.typeName;
      }

      public MissingConfigurationType(String var1) {
         this(var1, (Throwable)null);
      }

      public MissingConfigurationType(String var1, Throwable var2) {
         super(getExplanation(var1), var2);
         this.typeName = var1;
      }

      private static String getExplanation(String var0) {
         return I18NSupport.formatter().getNoTypeConfigMessage(var0);
      }
   }

   public abstract static class MissingConfiguration extends HarvesterException {
      public MissingConfiguration(String var1, Throwable var2) {
         super(var1, var2);
      }
   }

   public static class HarvestableTypesNotFoundException extends HarvesterException {
      private String[] types;

      public String[] getTypes() {
         return this.types;
      }

      public HarvestableTypesNotFoundException(String[] var1) {
         this(var1, (Throwable)null);
      }

      public HarvestableTypesNotFoundException(String[] var1, Throwable var2) {
         super(getExplanation(var1), var2);
         this.types = this.types != null ? this.types : new String[0];
      }

      private static String getExplanation(String var0) {
         return I18NSupport.formatter().getHarvesterTypeNotFoundMessage(var0);
      }

      private static String getExplanation(String[] var0) {
         if (var0 != null && var0.length != 0) {
            String var1 = HarvesterException.genStringFromArray(var0);
            return I18NSupport.formatter().getTypesDoNotExistMessage(var1);
         } else {
            return I18NSupport.formatter().getTypeNotDefinedMessage();
         }
      }
   }

   public static class HarvestableInstancesNotFoundException extends HarvesterException {
      private String[] instances;

      public String[] getInstances() {
         return this.instances;
      }

      public HarvestableInstancesNotFoundException(String[] var1) {
         this(var1, (Throwable)null);
      }

      public HarvestableInstancesNotFoundException(String[] var1, Throwable var2) {
         super(getExplanation(var1), var2);
         this.instances = var1 != null ? var1 : new String[0];
      }

      private static String getExplanation(String[] var0) {
         if (var0 != null && var0.length != 0) {
            String var1 = HarvesterException.genStringFromArray(var0);
            return I18NSupport.formatter().getInstancesDoNotExistMessage(var1);
         } else {
            return I18NSupport.formatter().getInstanceNotRegisteredMessage();
         }
      }
   }
}
