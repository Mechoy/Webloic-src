package weblogic.diagnostics.harvester;

public abstract class HarvesterLocalException extends HarvesterException {
   private HarvesterLocalException() {
   }

   private HarvesterLocalException(String var1) {
      super(var1);
   }

   private HarvesterLocalException(Throwable var1) {
      super(var1);
   }

   private HarvesterLocalException(String var1, Throwable var2) {
      super(var1, var2);
   }

   // $FF: synthetic method
   HarvesterLocalException(String var1, Object var2) {
      this(var1);
   }

   public static final class DuplicateProviderName extends HarvesterLocalException {
      private String providerName = null;

      public String getProviderNames() {
         return this.providerName;
      }

      public DuplicateProviderName(String var1) {
         super(getExplanation(var1), (<undefinedtype>)null);
         this.providerName = var1;
      }

      private static String getExplanation(String var0) {
         return I18NSupport.formatter().getDuplicateProviderMessage(var0);
      }
   }
}
