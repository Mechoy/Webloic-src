package weblogic.entitlement.rules;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Locale;
import weblogic.i18n.Localizer;
import weblogic.i18ntools.L10nLookup;

public class PredicateTextFormatter {
   private Localizer l10n;
   private boolean format = false;

   public PredicateTextFormatter() {
      this.l10n = L10nLookup.getLocalizer(Locale.getDefault(), "weblogic.entitlement.rules.PredicateTextLocalizer", PredicateTextFormatter.class.getClassLoader());
   }

   public PredicateTextFormatter(Locale var1) {
      this.l10n = L10nLookup.getLocalizer(var1, "weblogic.entitlement.rules.PredicateTextLocalizer", PredicateTextFormatter.class.getClassLoader());
   }

   public static PredicateTextFormatter getInstance() {
      return new PredicateTextFormatter();
   }

   public static PredicateTextFormatter getInstance(Locale var0) {
      return new PredicateTextFormatter(var0);
   }

   public void setExtendedFormat(boolean var1) {
      this.format = var1;
   }

   public boolean getExtendedFormat() {
      return this.format;
   }

   public String getInvalidDayOfWeekMessage(String var1) {
      String var2 = "";
      String var3 = "InvalidDayOfWeek";
      String var4 = "Security";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getInvalidArgumentTypeMessage(String var1) {
      String var2 = "";
      String var3 = "InvalidArgumentType";
      String var4 = "Security";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getInvalidAttributeNameMessage(String var1) {
      String var2 = "";
      String var3 = "InvalidAttributeName";
      String var4 = "Security";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getAM_PM(String var1) {
      String var2 = "";
      String var3 = "AM_PM";
      String var4 = "Security";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }
}
