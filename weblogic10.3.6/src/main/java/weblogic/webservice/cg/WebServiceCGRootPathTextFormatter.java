package weblogic.webservice.cg;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Locale;
import weblogic.i18n.Localizer;
import weblogic.i18ntools.L10nLookup;

public class WebServiceCGRootPathTextFormatter {
   private Localizer l10n;
   private boolean format = false;

   public WebServiceCGRootPathTextFormatter() {
      this.l10n = L10nLookup.getLocalizer(Locale.getDefault(), "weblogic.webservice.cg.WebServiceCGRootPathTextLocalizer", WebServiceCGRootPathTextFormatter.class.getClassLoader());
   }

   public WebServiceCGRootPathTextFormatter(Locale var1) {
      this.l10n = L10nLookup.getLocalizer(var1, "weblogic.webservice.cg.WebServiceCGRootPathTextLocalizer", WebServiceCGRootPathTextFormatter.class.getClassLoader());
   }

   public static WebServiceCGRootPathTextFormatter getInstance() {
      return new WebServiceCGRootPathTextFormatter();
   }

   public static WebServiceCGRootPathTextFormatter getInstance(Locale var0) {
      return new WebServiceCGRootPathTextFormatter(var0);
   }

   public void setExtendedFormat(boolean var1) {
      this.format = var1;
   }

   public boolean getExtendedFormat() {
      return this.format;
   }

   public String cgToolsPagegen() {
      String var1 = "";
      String var2 = "cgToolsPagegen";
      String var3 = "WebService CG Root Path";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }
}
