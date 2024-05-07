package weblogic.management.deploy.utils;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Locale;
import weblogic.i18n.Localizer;
import weblogic.i18ntools.L10nLookup;

public class DeployerHelperTextFormatter {
   private Localizer l10n;
   private boolean format = false;

   public DeployerHelperTextFormatter() {
      this.l10n = L10nLookup.getLocalizer(Locale.getDefault(), "weblogic.management.deploy.utils.DeployerHelperTextLocalizer", DeployerHelperTextFormatter.class.getClassLoader());
   }

   public DeployerHelperTextFormatter(Locale var1) {
      this.l10n = L10nLookup.getLocalizer(var1, "weblogic.management.deploy.utils.DeployerHelperTextLocalizer", DeployerHelperTextFormatter.class.getClassLoader());
   }

   public static DeployerHelperTextFormatter getInstance() {
      return new DeployerHelperTextFormatter();
   }

   public static DeployerHelperTextFormatter getInstance(Locale var0) {
      return new DeployerHelperTextFormatter(var0);
   }

   public void setExtendedFormat(boolean var1) {
      this.format = var1;
   }

   public boolean getExtendedFormat() {
      return this.format;
   }

   public String exceptionNoSuchSource(String var1) {
      String var2 = "";
      String var3 = "EXCEPTION_NO_SUCH_SOURCE";
      String var4 = "Deployer";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String exceptionArchivingFile(String var1, String var2) {
      String var3 = "";
      String var4 = "EXCEPTION_ARCHIVING_FILE";
      String var5 = "Deployer";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String exceptionUploadingSource(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "EXCEPTION_UPLOADING_SOURCE";
      String var6 = "Deployer";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String exceptionUploadingFiles(String var1, String var2) {
      String var3 = "";
      String var4 = "EXCEPTION_UPLOADING_FILES";
      String var5 = "Deployer";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }
}
