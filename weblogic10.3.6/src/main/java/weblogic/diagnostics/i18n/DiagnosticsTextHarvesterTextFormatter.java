package weblogic.diagnostics.i18n;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Locale;
import weblogic.i18n.Localizer;
import weblogic.i18ntools.L10nLookup;

public class DiagnosticsTextHarvesterTextFormatter {
   private Localizer l10n;
   private boolean format = false;

   public DiagnosticsTextHarvesterTextFormatter() {
      this.l10n = L10nLookup.getLocalizer(Locale.getDefault(), "weblogic.diagnostics.i18n.DiagnosticsTextHarvesterTextLocalizer", DiagnosticsTextHarvesterTextFormatter.class.getClassLoader());
   }

   public DiagnosticsTextHarvesterTextFormatter(Locale var1) {
      this.l10n = L10nLookup.getLocalizer(var1, "weblogic.diagnostics.i18n.DiagnosticsTextHarvesterTextLocalizer", DiagnosticsTextHarvesterTextFormatter.class.getClassLoader());
   }

   public static DiagnosticsTextHarvesterTextFormatter getInstance() {
      return new DiagnosticsTextHarvesterTextFormatter();
   }

   public static DiagnosticsTextHarvesterTextFormatter getInstance(Locale var0) {
      return new DiagnosticsTextHarvesterTextFormatter(var0);
   }

   public void setExtendedFormat(boolean var1) {
      this.format = var1;
   }

   public boolean getExtendedFormat() {
      return this.format;
   }

   public String getDomainRuntimeNamespaceWarningText(String var1) {
      String var2 = "";
      String var3 = "DomainRuntimeNamespaceWarningText";
      String var4 = "DiagnosticsHarvester";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getInvalidHarvesterNamespaceText(String var1) {
      String var2 = "";
      String var3 = "InvalidHarvesterNamespaceText";
      String var4 = "DiagnosticsHarvester";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getWatchedValuesIdNotFoundText(int var1) {
      String var2 = "";
      String var3 = "WatchedValuesIdNotFoundText";
      String var4 = "DiagnosticsHarvester";
      Object[] var5 = new Object[]{new Integer(var1)};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getInvalidAttributeSpecText() {
      String var1 = "";
      String var2 = "InvalidAttributeSpecText";
      String var3 = "DiagnosticsHarvester";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }
}
