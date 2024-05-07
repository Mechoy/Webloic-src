package weblogic.management.security.internal;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Locale;
import weblogic.i18n.Localizer;
import weblogic.i18ntools.L10nLookup;

public class SecurityProviderUpgradeTextTextFormatter {
   private Localizer l10n;
   private boolean format = false;

   public SecurityProviderUpgradeTextTextFormatter() {
      this.l10n = L10nLookup.getLocalizer(Locale.getDefault(), "weblogic.management.security.internal.SecurityProviderUpgradeTextTextLocalizer", SecurityProviderUpgradeTextTextFormatter.class.getClassLoader());
   }

   public SecurityProviderUpgradeTextTextFormatter(Locale var1) {
      this.l10n = L10nLookup.getLocalizer(var1, "weblogic.management.security.internal.SecurityProviderUpgradeTextTextLocalizer", SecurityProviderUpgradeTextTextFormatter.class.getClassLoader());
   }

   public static SecurityProviderUpgradeTextTextFormatter getInstance() {
      return new SecurityProviderUpgradeTextTextFormatter();
   }

   public static SecurityProviderUpgradeTextTextFormatter getInstance(Locale var0) {
      return new SecurityProviderUpgradeTextTextFormatter(var0);
   }

   public void setExtendedFormat(boolean var1) {
      this.format = var1;
   }

   public boolean getExtendedFormat() {
      return this.format;
   }

   public String NoJarsUpgraded() {
      String var1 = "";
      String var2 = "NoJarsUpgraded";
      String var3 = "SecurityProviderUpgrade";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String completedUpgradeOf(int var1) {
      String var2 = "";
      String var3 = "completedUpgradeOf";
      String var4 = "SecurityProviderUpgrade";
      Object[] var5 = new Object[]{new Integer(var1)};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String NoMDFs(String var1) {
      String var2 = "";
      String var3 = "NoMDFs";
      String var4 = "SecurityProviderUpgrade";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String createdNew(String var1, String var2) {
      String var3 = "";
      String var4 = "createdNew";
      String var5 = "SecurityProviderUpgrade";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String started() {
      String var1 = "";
      String var2 = "started";
      String var3 = "SecurityProviderUpgrade";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }
}
