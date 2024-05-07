package weblogic.t3.srvr;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Locale;
import weblogic.i18n.Localizer;
import weblogic.i18ntools.L10nLookup;

public class T3SrvrTextTextFormatter {
   private Localizer l10n;
   private boolean format = false;

   public T3SrvrTextTextFormatter() {
      this.l10n = L10nLookup.getLocalizer(Locale.getDefault(), "weblogic.t3.srvr.T3SrvrTextTextLocalizer", T3SrvrTextTextFormatter.class.getClassLoader());
   }

   public T3SrvrTextTextFormatter(Locale var1) {
      this.l10n = L10nLookup.getLocalizer(var1, "weblogic.t3.srvr.T3SrvrTextTextLocalizer", T3SrvrTextTextFormatter.class.getClassLoader());
   }

   public static T3SrvrTextTextFormatter getInstance() {
      return new T3SrvrTextTextFormatter();
   }

   public static T3SrvrTextTextFormatter getInstance(Locale var0) {
      return new T3SrvrTextTextFormatter(var0);
   }

   public void setExtendedFormat(boolean var1) {
      this.format = var1;
   }

   public boolean getExtendedFormat() {
      return this.format;
   }

   public String getServerShutdownSuccessfully(String var1) {
      String var2 = "";
      String var3 = "ServerShutdownSuccessfully";
      String var4 = "T3Srvr Text";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getStartupWithoutAdminChannel() {
      String var1 = "";
      String var2 = "StartupWithoutAdminChannel";
      String var3 = "T3Srvr Text";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }
}
