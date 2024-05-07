package weblogic.wsee;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Locale;
import weblogic.i18n.Localizer;
import weblogic.i18ntools.L10nLookup;

public class WseeRmTextTextFormatter {
   private Localizer l10n;
   private boolean format = false;

   public WseeRmTextTextFormatter() {
      this.l10n = L10nLookup.getLocalizer(Locale.getDefault(), "weblogic.wsee.WseeRmTextTextLocalizer", WseeRmTextTextFormatter.class.getClassLoader());
   }

   public WseeRmTextTextFormatter(Locale var1) {
      this.l10n = L10nLookup.getLocalizer(var1, "weblogic.wsee.WseeRmTextTextLocalizer", WseeRmTextTextFormatter.class.getClassLoader());
   }

   public static WseeRmTextTextFormatter getInstance() {
      return new WseeRmTextTextFormatter();
   }

   public static WseeRmTextTextFormatter getInstance(Locale var0) {
      return new WseeRmTextTextFormatter(var0);
   }

   public void setExtendedFormat(boolean var1) {
      this.format = var1;
   }

   public boolean getExtendedFormat() {
      return this.format;
   }

   public String termWithArgs(String var1) {
      String var2 = "";
      String var3 = "termWithArgs";
      String var4 = "WseeRm";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String sourceSideTerm() {
      String var1 = "";
      String var2 = "sourceSideTerm";
      String var3 = "WseeRm";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String destinationSideTerm() {
      String var1 = "";
      String var2 = "destinationSideTerm";
      String var3 = "WseeRm";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }
}
