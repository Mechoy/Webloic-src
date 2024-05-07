package weblogic.management.configuration;

import java.util.Arrays;
import javax.management.Attribute;
import javax.management.InvalidAttributeValueException;
import weblogic.utils.ArrayUtils;

public class LegalChecks {
   public static String checkLegalStringSet(Attribute var0, String[] var1) throws InvalidAttributeValueException {
      String var2 = (String)var0.getValue();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         if (var2.equalsIgnoreCase(var1[var3])) {
            return var1[var3];
         }
      }

      throw new InvalidAttributeValueException(var2 + " is not a legal value for " + var0.getName() + ".  " + "The value must be one of the following: " + Arrays.asList((Object[])var1));
   }

   public static void checkLegalIntSet(Attribute var0, int[] var1) throws InvalidAttributeValueException {
      int var2 = ((Number)var0.getValue()).intValue();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         if (var2 == var1[var3]) {
            return;
         }
      }

      throw new InvalidAttributeValueException(var2 + " is not a legal value for " + var0.getName() + ".  " + "The value must be one of the following: " + ArrayUtils.toString(var1));
   }

   public static void checkLegalRange(Attribute var0, long var1, long var3) throws InvalidAttributeValueException {
      long var5 = ((Number)var0.getValue()).longValue();
      if (var5 < var1 || var5 > var3) {
         throw new InvalidAttributeValueException(var5 + " is not a legal value for " + var0.getName() + ".  " + "It does not fall in the range [" + var1 + ", " + var3 + "]");
      }
   }

   public static void checkNonNull(Attribute var0) throws InvalidAttributeValueException {
      if (var0.getValue() == null) {
         throw new InvalidAttributeValueException("null is not a legal value for " + var0.getName());
      }
   }

   public static void checkNonEmptyString(Attribute var0) throws InvalidAttributeValueException {
      Object var1 = var0.getValue();
      if (!(var1 instanceof String)) {
         throw new InvalidAttributeValueException("checkNonEmptyString called to check " + var0.getName() + ", which is a " + var1.getClass().getName());
      }
   }
}
