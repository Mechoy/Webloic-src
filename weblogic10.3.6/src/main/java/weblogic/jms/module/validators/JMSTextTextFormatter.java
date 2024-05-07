package weblogic.jms.module.validators;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Locale;
import weblogic.i18n.Localizer;
import weblogic.i18ntools.L10nLookup;

public class JMSTextTextFormatter {
   private Localizer l10n;
   private boolean format = false;

   public JMSTextTextFormatter() {
      this.l10n = L10nLookup.getLocalizer(Locale.getDefault(), "weblogic.jms.module.validators.JMSTextTextLocalizer", JMSTextTextFormatter.class.getClassLoader());
   }

   public JMSTextTextFormatter(Locale var1) {
      this.l10n = L10nLookup.getLocalizer(var1, "weblogic.jms.module.validators.JMSTextTextLocalizer", JMSTextTextFormatter.class.getClassLoader());
   }

   public static JMSTextTextFormatter getInstance() {
      return new JMSTextTextFormatter();
   }

   public static JMSTextTextFormatter getInstance(Locale var0) {
      return new JMSTextTextFormatter(var0);
   }

   public void setExtendedFormat(boolean var1) {
      this.format = var1;
   }

   public boolean getExtendedFormat() {
      return this.format;
   }

   public String getIllegalTimeToDeliverOverride() {
      String var1 = "";
      String var2 = "IllegalTimeToDeliverOverride";
      String var3 = "JMS Text";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String getIllegalTimeToDeliverOverrideWithException(String var1) {
      String var2 = "";
      String var3 = "IllegalTimeToDeliverOverrideWithException";
      String var4 = "JMS Text";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getInvalidMulticastAddressException(String var1, String var2) {
      String var3 = "";
      String var4 = "InvalidMulticastAddressException";
      String var5 = "JMS Text";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String getJMSCFJNDIConflictWithDefaultsException(String var1) {
      String var2 = "";
      String var3 = "JMSCFJNDIConflictWithDefaultsException";
      String var4 = "JMS Text";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getJMSDestJNDINameConflictException(String var1) {
      String var2 = "";
      String var3 = "JMSDestJNDINameConflictException";
      String var4 = "JMS Text";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getJMSCFJNDINameConflictException(String var1) {
      String var2 = "";
      String var3 = "JMSCFJNDINameConflictException";
      String var4 = "JMS Text";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getJMSJNDINameConflictException(String var1) {
      String var2 = "";
      String var3 = "JMSJNDINameConflictException";
      String var4 = "JMS Text";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getInvalidJMSMessagesMaximum() {
      String var1 = "";
      String var2 = "InvalidJMSMessagesMaximum";
      String var3 = "JMS Text";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String InvalidMulticastAddress(String var1) {
      String var2 = "";
      String var3 = "InvalidMulticastAddress";
      String var4 = "JMS Text";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getJMSCFConflictWithDefaultsException(String var1) {
      String var2 = "";
      String var3 = "JMSCFConflictWithDefaultsException";
      String var4 = "JMS Text";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }
}
