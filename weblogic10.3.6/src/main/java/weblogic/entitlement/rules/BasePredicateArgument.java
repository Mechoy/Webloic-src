package weblogic.entitlement.rules;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import weblogic.security.providers.authorization.IllegalPredicateArgumentException;
import weblogic.security.providers.authorization.PredicateArgument;

public abstract class BasePredicateArgument implements PredicateArgument {
   protected String displayNameId;
   private String descriptionId;
   private Class type;
   private Object defaultValue;
   private boolean optional;
   public static final int CALENDAR_YEAR = 2006;
   public static final int CALENDAR_MONTH = 3;
   public static final int CALENDAR_DAY_OF_MONTH = 25;
   public static final int CALENDAR_HOUR = 20;
   public static final int CALENDAR_MINUTE = 45;
   public static final int CALENDAR_SECOND = 35;
   public static final int EST_HOUR_OFFSET = 5;

   public BasePredicateArgument(String var1, String var2, Class var3, Object var4) {
      this(var1, var2, var3, var4, false);
   }

   public BasePredicateArgument(String var1, String var2, Class var3, Object var4, boolean var5) {
      this.displayNameId = var1;
      this.descriptionId = var2;
      this.type = var3;
      this.defaultValue = var4;
      this.optional = var5;
   }

   public String getType() {
      return this.type.getName();
   }

   public String getDisplayName(Locale var1) {
      return Localizer.getText(this.displayNameId, var1);
   }

   public String getDescription(Locale var1) {
      return Localizer.getText(this.descriptionId, var1);
   }

   public Object getDefaultValue() {
      return this.defaultValue;
   }

   public boolean isOptional() {
      return this.optional;
   }

   public void validateValue(Object var1, Locale var2) throws IllegalPredicateArgumentException {
      if (var1 == null) {
         throw new IllegalPredicateArgumentException(Localizer.getText("NullArgumentValue", var2));
      } else if (!this.type.isAssignableFrom(var1.getClass())) {
         String var3 = (new PredicateTextFormatter(var2)).getInvalidArgumentTypeMessage(this.getType());
         throw new IllegalPredicateArgumentException(var3);
      }
   }

   public Object parseValue(String var1, Locale var2) throws IllegalPredicateArgumentException {
      return var1;
   }

   public String formatValue(Object var1, Locale var2) throws IllegalPredicateArgumentException {
      return var1.toString();
   }

   public Object parseExprValue(String var1) throws IllegalPredicateArgumentException {
      return this.parseValue(var1, Locale.US);
   }

   public String formatExprValue(Object var1) throws IllegalPredicateArgumentException {
      return this.formatValue(var1, Locale.US);
   }

   protected static void test(PredicateArgument var0, String var1) throws Exception {
      Locale var2 = Locale.getDefault();
      System.out.println("Name: " + var0.getDisplayName(var2));
      System.out.println("Description: " + var0.getDescription(var2));
      System.out.println("Original string: " + var1);
      Object var3 = var0.parseValue(var1, var2);
      String var4 = var0.formatExprValue(var3);
      System.out.println("Formatted expression value: " + var4);
      Object var5 = var0.parseExprValue(var4);
      if (!var3.equals(var5)) {
         System.out.println("Values 1/expr are different: " + var3 + ", " + var5);
      }

      var1 = var0.formatValue(var5, var2);
      System.out.println("Formatted localized value: " + var1);
      Object var6 = var0.parseValue(var1, var2);
      if (!var3.equals(var6)) {
         System.out.println("Values 1/2 are different: " + var3 + ", " + var6);
      }

   }

   public String getDatePattern(Locale var1) {
      DateFormat var2 = DateFormat.getDateInstance(3, var1);
      if (var2 instanceof SimpleDateFormat) {
         SimpleDateFormat var3 = (SimpleDateFormat)var2;
         return var3.toPattern();
      } else {
         return new String(" ");
      }
   }

   public String getTimePattern(Locale var1) {
      return this.getTimePattern(var1, 2);
   }

   public String getTimePattern(Locale var1, int var2) {
      DateFormat var3 = DateFormat.getTimeInstance(var2, var1);
      if (var3 instanceof SimpleDateFormat) {
         SimpleDateFormat var4 = (SimpleDateFormat)var3;
         return var4.toPattern();
      } else {
         return new String(" ");
      }
   }

   public String getDateTimePattern(Locale var1) {
      DateFormat var2 = DateFormat.getDateTimeInstance(3, 2, var1);
      if (var2 instanceof SimpleDateFormat) {
         SimpleDateFormat var3 = (SimpleDateFormat)var2;
         return var3.toPattern();
      } else {
         return new String(" ");
      }
   }

   public String getFormatedDateTimePattern(Locale var1) {
      DateFormat var2 = DateFormat.getDateTimeInstance(3, 2, var1);
      SimpleDateFormat var3 = (SimpleDateFormat)var2;
      StringBuffer var4 = new StringBuffer(var3.toPattern());
      int var5 = var4.indexOf("a");
      if (var5 >= 0) {
         var4 = var4.replace(var5, var5 + 1, Localizer.getText("AM_PM", var1));
      }

      return var4.toString();
   }

   public String getFormatedTimePattern(Locale var1) {
      return this.getFormatedTimePattern(var1, 3);
   }

   public String getFormatedTimePattern(Locale var1, int var2) {
      DateFormat var3 = DateFormat.getTimeInstance(var2, var1);
      SimpleDateFormat var4 = (SimpleDateFormat)var3;
      String var5 = var4.toPattern();
      StringBuffer var6 = new StringBuffer(var4.toPattern());
      int var7 = var6.indexOf("a");
      if (var7 >= 0) {
         var6 = var6.replace(var7, var7 + 1, Localizer.getText("AM_PM", var1));
      }

      return var6.toString();
   }

   public String getFormatedDateString(String var1, Locale var2, Date var3) {
      SimpleDateFormat var4 = new SimpleDateFormat(var1, var2);
      StringBuffer var5 = var4.format(var3, new StringBuffer(), new FieldPosition(0));
      return var5.toString();
   }

   public String parseMessage(StringBuffer var1, ArrayList var2) {
      String var3 = "{";
      String var4 = "}";

      for(int var5 = 0; var5 < var2.size(); ++var5) {
         int var6 = var1.indexOf(var3);
         int var7 = var1.indexOf(var4);
         String var8 = var1.substring(var6, var7 + 1);
         String var9 = var1.substring(var6 + 1, var7);
         if (var8.length() >= 0) {
            var1 = var1.replace(var6, var7 + 1, (String)var2.get(Integer.parseInt(var9)));
         }
      }

      return var1.toString();
   }
}
