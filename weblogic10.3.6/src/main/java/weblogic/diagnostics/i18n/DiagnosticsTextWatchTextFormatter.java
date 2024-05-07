package weblogic.diagnostics.i18n;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Locale;
import weblogic.i18n.Localizer;
import weblogic.i18ntools.L10nLookup;

public class DiagnosticsTextWatchTextFormatter {
   private Localizer l10n;
   private boolean format = false;

   public DiagnosticsTextWatchTextFormatter() {
      this.l10n = L10nLookup.getLocalizer(Locale.getDefault(), "weblogic.diagnostics.i18n.DiagnosticsTextWatchTextLocalizer", DiagnosticsTextWatchTextFormatter.class.getClassLoader());
   }

   public DiagnosticsTextWatchTextFormatter(Locale var1) {
      this.l10n = L10nLookup.getLocalizer(var1, "weblogic.diagnostics.i18n.DiagnosticsTextWatchTextLocalizer", DiagnosticsTextWatchTextFormatter.class.getClassLoader());
   }

   public static DiagnosticsTextWatchTextFormatter getInstance() {
      return new DiagnosticsTextWatchTextFormatter();
   }

   public static DiagnosticsTextWatchTextFormatter getInstance(Locale var0) {
      return new DiagnosticsTextWatchTextFormatter(var0);
   }

   public void setExtendedFormat(boolean var1) {
      this.format = var1;
   }

   public boolean getExtendedFormat() {
      return this.format;
   }

   public String getUnknownWatchVariableExceptionText(String var1, String var2) {
      String var3 = "";
      String var4 = "UnknownWatchVariableExceptionText";
      String var5 = "DiagnosticsWatch";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String getNullWatchVariableAttributeNameText(String var1, String var2) {
      String var3 = "";
      String var4 = "NullWatchVariableAttributeNameText";
      String var5 = "DiagnosticsWatch";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String getIncompleteWatchVariableConsoleText() {
      String var1 = "";
      String var2 = "IncompleteWatchVariableConsoleText";
      String var3 = "DiagnosticsWatch";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String getEmptyWatchAttributeConsoleText() {
      String var1 = "";
      String var2 = "EmptyWatchAttributeConsoleText";
      String var3 = "DiagnosticsWatch";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }
}
