package weblogic.security.shared;

import weblogic.security.notshared.RuntimeUtilitiesImpl;

public class RuntimeEnvironment {
   public static RuntimeUtilities getRuntimeUtilities() {
      return RuntimeEnvironment.RuntimeUtilityHolder.utilities;
   }

   private static class RuntimeUtilityHolder {
      public static final RuntimeUtilities utilities = new RuntimeUtilitiesImpl();
   }
}
