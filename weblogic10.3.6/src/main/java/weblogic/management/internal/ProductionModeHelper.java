package weblogic.management.internal;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class ProductionModeHelper {
   private static String globalProductionMode = new String();
   private static final String localProductionMode = System.getProperty("weblogic.ProductionModeEnabled");

   static void exportProductionMode(ObjectOutput var0) throws IOException {
      var0.writeObject(localProductionMode != null ? localProductionMode : globalProductionMode);
   }

   static void importProductionMode(ObjectInput var0) throws IOException {
      try {
         globalProductionMode = (String)var0.readObject();
      } catch (ClassNotFoundException var2) {
      }

   }

   public static boolean getGlobalProductionMode() {
      return isGlobalProductionModeSet() ? Boolean.parseBoolean(globalProductionMode) : false;
   }

   public static boolean isGlobalProductionModeSet() {
      return globalProductionMode != null && globalProductionMode.length() != 0;
   }

   public static boolean getProductionModeProperty() {
      return isProductionModePropertySet() ? Boolean.parseBoolean(localProductionMode) : false;
   }

   public static boolean isProductionModePropertySet() {
      return localProductionMode != null && localProductionMode.length() != 0;
   }
}
