package weblogic.management.configuration;

import weblogic.utils.NestedError;

public final class ConfigurationError extends NestedError {
   private static final long serialVersionUID = -3034774634846232555L;

   public ConfigurationError(String var1) {
      super(var1);
   }

   public ConfigurationError(Throwable var1) {
      super(var1);
   }

   public ConfigurationError(String var1, Throwable var2) {
      super(var1, var2);
   }
}
