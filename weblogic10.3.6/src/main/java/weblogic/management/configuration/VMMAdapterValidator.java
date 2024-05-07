package weblogic.management.configuration;

import com.oracle.vmm.client.AdapterInfo;
import com.oracle.vmm.client.VMMConnector;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class VMMAdapterValidator {
   public static String TOO_MANY_PARTS_MSG = "Adapter may not have more than two parts separated by '_'";
   public static String NULL_EMPTY_MSG = "Adapter name and/or version may not be null or empty string";
   public static String NOT_FOUND_MSG = "Specified adapter name/version cannot be found: ";

   public static void validateAdapter(String var0) throws IllegalArgumentException {
      if (var0 != null && var0.length() != 0) {
         String[] var1 = var0.split("_");
         if (var1.length > 2) {
            throw new IllegalArgumentException(TOO_MANY_PARTS_MSG);
         } else {
            Iterator var2 = getAvailableAdapters().iterator();

            String var3;
            do {
               if (!var2.hasNext()) {
                  throw new IllegalArgumentException(NOT_FOUND_MSG + var0);
               }

               var3 = (String)var2.next();
            } while(!var0.equals(var3));

         }
      } else {
         throw new IllegalArgumentException(NULL_EMPTY_MSG);
      }
   }

   public static void validateAdapterName(String var0) throws IllegalArgumentException {
      if (var0 != null && var0.length() != 0) {
         Iterator var1 = getAvailableAdapters().iterator();

         String var2;
         do {
            if (!var1.hasNext()) {
               throw new IllegalArgumentException(NOT_FOUND_MSG + var0);
            }

            var2 = (String)var1.next();
         } while(!var2.startsWith(var0 + "_"));

      } else {
         throw new IllegalArgumentException(NULL_EMPTY_MSG);
      }
   }

   public static void validateAdapterVersion(String var0) throws IllegalArgumentException {
      if (var0 != null && var0.length() != 0) {
         Iterator var1 = getAvailableAdapters().iterator();

         String var2;
         do {
            if (!var1.hasNext()) {
               throw new IllegalArgumentException(NOT_FOUND_MSG + var0);
            }

            var2 = (String)var1.next();
         } while(!var2.endsWith("_" + var0));

      } else {
         throw new IllegalArgumentException(NULL_EMPTY_MSG);
      }
   }

   public static List<String> getAvailableAdapters() {
      List var0 = VMMConnector.getAvailableAdapters();
      ArrayList var1 = new ArrayList();
      if (var0 != null) {
         Iterator var2 = var0.iterator();

         while(var2.hasNext()) {
            AdapterInfo var3 = (AdapterInfo)var2.next();
            var1.add(var3.getVmmType() + "_" + var3.getVmmApiVersion());
         }
      }

      return var1;
   }

   public static void main(String[] var0) {
      List var1 = getAvailableAdapters();
      if (var1 != null) {
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            System.out.println(var3);
         }
      }

   }
}
