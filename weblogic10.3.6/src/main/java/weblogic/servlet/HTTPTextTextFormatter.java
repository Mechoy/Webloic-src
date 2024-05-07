package weblogic.servlet;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Locale;
import weblogic.i18n.Localizer;
import weblogic.i18ntools.L10nLookup;

public class HTTPTextTextFormatter {
   private Localizer l10n;
   private boolean format = false;

   public HTTPTextTextFormatter() {
      this.l10n = L10nLookup.getLocalizer(Locale.getDefault(), "weblogic.servlet.HTTPTextTextLocalizer", HTTPTextTextFormatter.class.getClassLoader());
   }

   public HTTPTextTextFormatter(Locale var1) {
      this.l10n = L10nLookup.getLocalizer(var1, "weblogic.servlet.HTTPTextTextLocalizer", HTTPTextTextFormatter.class.getClassLoader());
   }

   public static HTTPTextTextFormatter getInstance() {
      return new HTTPTextTextFormatter();
   }

   public static HTTPTextTextFormatter getInstance(Locale var0) {
      return new HTTPTextTextFormatter(var0);
   }

   public void setExtendedFormat(boolean var1) {
      this.format = var1;
   }

   public boolean getExtendedFormat() {
      return this.format;
   }

   public String getRefreshPageHTML(String var1, String var2) {
      String var3 = "";
      String var4 = "getRefreshPageHTML";
      String var5 = "HTTP";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }
}
