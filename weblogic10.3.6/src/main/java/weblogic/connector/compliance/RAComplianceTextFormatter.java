package weblogic.connector.compliance;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Locale;
import weblogic.i18n.Localizer;
import weblogic.i18ntools.L10nLookup;

public class RAComplianceTextFormatter {
   private Localizer l10n;
   private boolean format = false;

   public RAComplianceTextFormatter() {
      this.l10n = L10nLookup.getLocalizer(Locale.getDefault(), "weblogic.connector.compliance.RAComplianceTextLocalizer", RAComplianceTextFormatter.class.getClassLoader());
   }

   public RAComplianceTextFormatter(Locale var1) {
      this.l10n = L10nLookup.getLocalizer(var1, "weblogic.connector.compliance.RAComplianceTextLocalizer", RAComplianceTextFormatter.class.getClassLoader());
   }

   public static RAComplianceTextFormatter getInstance() {
      return new RAComplianceTextFormatter();
   }

   public static RAComplianceTextFormatter getInstance(Locale var0) {
      return new RAComplianceTextFormatter(var0);
   }

   public void setExtendedFormat(boolean var1) {
      this.format = var1;
   }

   public boolean getExtendedFormat() {
      return this.format;
   }

   public String ELEMENT_IS_EMPTY(String var1, String var2) {
      String var3 = "";
      String var4 = "ELEMENT_IS_EMPTY";
      String var5 = "Connector Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String MUST_IMPLEMENT_INTERFACE(String var1, String var2, String var3, String var4) {
      String var5 = "";
      String var6 = "MUST_IMPLEMENT_INTERFACE";
      String var7 = "Connector Compliance";
      Object[] var8 = new Object[]{var1, var2, var3, var4};
      String var9 = MessageFormat.format(this.l10n.get(var6), var8);
      if (this.getExtendedFormat()) {
         DateFormat var10 = DateFormat.getDateTimeInstance(2, 1);
         var5 = "<" + var10.format(new Date()) + "><" + var7 + "><" + var6 + "> ";
      }

      return var5 + var9;
   }

   public String MISSING_RA_BEAN() {
      String var1 = "";
      String var2 = "MISSING_RA_BEAN";
      String var3 = "Connector Compliance";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String SHOULD_NOT_OVERRIDE(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "SHOULD_NOT_OVERRIDE";
      String var6 = "Connector Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String ZERO_LENGTH_NAME(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "ZERO_LENGTH_NAME";
      String var6 = "Connector Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String CLASS_NOT_FOUND(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "CLASS_NOT_FOUND";
      String var6 = "Connector Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String COULDNT_LOAD_CLASS(String var1, String var2, String var3, String var4) {
      String var5 = "";
      String var6 = "COULDNT_LOAD_CLASS";
      String var7 = "Connector Compliance";
      Object[] var8 = new Object[]{var1, var2, var3, var4};
      String var9 = MessageFormat.format(this.l10n.get(var6), var8);
      if (this.getExtendedFormat()) {
         DateFormat var10 = DateFormat.getDateTimeInstance(2, 1);
         var5 = "<" + var10.format(new Date()) + "><" + var7 + "><" + var6 + "> ";
      }

      return var5 + var9;
   }

   public String MUST_OVERRIDE(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "MUST_OVERRIDE";
      String var6 = "Connector Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String SHOULD_IMPLEMENT_INTERFACE(String var1, String var2, String var3, String var4) {
      String var5 = "";
      String var6 = "SHOULD_IMPLEMENT_INTERFACE";
      String var7 = "Connector Compliance";
      Object[] var8 = new Object[]{var1, var2, var3, var4};
      String var9 = MessageFormat.format(this.l10n.get(var6), var8);
      if (this.getExtendedFormat()) {
         DateFormat var10 = DateFormat.getDateTimeInstance(2, 1);
         var5 = "<" + var10.format(new Date()) + "><" + var7 + "><" + var6 + "> ";
      }

      return var5 + var9;
   }

   public String FILE_NOT_FOUND(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "FILE_NOT_FOUND";
      String var6 = "Connector Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String NOT_A_JAVA_BEAN(String var1, String var2, String var3, String var4) {
      String var5 = "";
      String var6 = "NOT_A_JAVA_BEAN";
      String var7 = "Connector Compliance";
      Object[] var8 = new Object[]{var1, var2, var3, var4};
      String var9 = MessageFormat.format(this.l10n.get(var6), var8);
      if (this.getExtendedFormat()) {
         DateFormat var10 = DateFormat.getDateTimeInstance(2, 1);
         var5 = "<" + var10.format(new Date()) + "><" + var7 + "><" + var6 + "> ";
      }

      return var5 + var9;
   }

   public String MISSING_JNDI_NAME() {
      String var1 = "";
      String var2 = "MISSING_JNDI_NAME";
      String var3 = "Connector Compliance";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String MISSING_RA_BEAN_FOR_INBOUND() {
      String var1 = "";
      String var2 = "MISSING_RA_BEAN_FOR_INBOUND";
      String var3 = "Connector Compliance";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String MISSING_RA_XML() {
      String var1 = "";
      String var2 = "MISSING_RA_XML";
      String var3 = "Connector Compliance";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String NO_MATCHING_ADMIN_INTERFACE(String var1) {
      String var2 = "";
      String var3 = "NO_MATCHING_ADMIN_INTERFACE";
      String var4 = "Connector Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String NO_MATCHING_CONN_FACTORY_INTERFACE(String var1) {
      String var2 = "";
      String var3 = "NO_MATCHING_CONN_FACTORY_INTERFACE";
      String var4 = "Connector Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String DUPLICATE_RA_PROPERTY(String var1, String var2, String var3, String var4) {
      String var5 = "";
      String var6 = "DUPLICATE_RA_PROPERTY";
      String var7 = "Connector Compliance";
      Object[] var8 = new Object[]{var1, var2, var3, var4};
      String var9 = MessageFormat.format(this.l10n.get(var6), var8);
      if (this.getExtendedFormat()) {
         DateFormat var10 = DateFormat.getDateTimeInstance(2, 1);
         var5 = "<" + var10.format(new Date()) + "><" + var7 + "><" + var6 + "> ";
      }

      return var5 + var9;
   }

   public String DUPLICATE_RA_PROPERTY_MULTI_TYPES(String var1, String var2, String var3, String var4, String var5) {
      String var6 = "";
      String var7 = "DUPLICATE_RA_PROPERTY_MULTI_TYPES";
      String var8 = "Connector Compliance";
      Object[] var9 = new Object[]{var1, var2, var3, var4, var5};
      String var10 = MessageFormat.format(this.l10n.get(var7), var9);
      if (this.getExtendedFormat()) {
         DateFormat var11 = DateFormat.getDateTimeInstance(2, 1);
         var6 = "<" + var11.format(new Date()) + "><" + var8 + "><" + var7 + "> ";
      }

      return var6 + var10;
   }

   public String MISSING_RA_PROPERTY(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "MISSING_RA_PROPERTY";
      String var6 = "Connector Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String NULL_PROPERTY_NAME(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "NULL_PROPERTY_NAME";
      String var6 = "Connector Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String PROPERTY_TYPE_VALUE_MISMATCH(String var1, String var2, String var3, String var4, String var5, String var6) {
      String var7 = "";
      String var8 = "PROPERTY_TYPE_VALUE_MISMATCH";
      String var9 = "Connector Compliance";
      Object[] var10 = new Object[]{var1, var2, var3, var4, var5, var6};
      String var11 = MessageFormat.format(this.l10n.get(var8), var10);
      if (this.getExtendedFormat()) {
         DateFormat var12 = DateFormat.getDateTimeInstance(2, 1);
         var7 = "<" + var12.format(new Date()) + "><" + var9 + "><" + var8 + "> ";
      }

      return var7 + var11;
   }

   public String INVALID_PROPERTY_TYPE(String var1, String var2, String var3, String var4, String var5) {
      String var6 = "";
      String var7 = "INVALID_PROPERTY_TYPE";
      String var8 = "Connector Compliance";
      Object[] var9 = new Object[]{var1, var2, var3, var4, var5};
      String var10 = MessageFormat.format(this.l10n.get(var7), var9);
      if (this.getExtendedFormat()) {
         DateFormat var11 = DateFormat.getDateTimeInstance(2, 1);
         var6 = "<" + var11.format(new Date()) + "><" + var8 + "><" + var7 + "> ";
      }

      return var6 + var10;
   }

   public String NO_SET_METHOD_FOR_PROPERTY(String var1, String var2, String var3, String var4, String var5) {
      String var6 = "";
      String var7 = "NO_SET_METHOD_FOR_PROPERTY";
      String var8 = "Connector Compliance";
      Object[] var9 = new Object[]{var1, var2, var3, var4, var5};
      String var10 = MessageFormat.format(this.l10n.get(var7), var9);
      if (this.getExtendedFormat()) {
         DateFormat var11 = DateFormat.getDateTimeInstance(2, 1);
         var6 = "<" + var11.format(new Date()) + "><" + var8 + "><" + var7 + "> ";
      }

      return var6 + var10;
   }

   public String PROPERTY_TYEPE_MISMATCH(String var1, String var2, String var3, String var4, String var5, String var6) {
      String var7 = "";
      String var8 = "PROPERTY_TYEPE_MISMATCH";
      String var9 = "Connector Compliance";
      Object[] var10 = new Object[]{var1, var2, var3, var4, var5, var6};
      String var11 = MessageFormat.format(this.l10n.get(var8), var10);
      if (this.getExtendedFormat()) {
         DateFormat var12 = DateFormat.getDateTimeInstance(2, 1);
         var7 = "<" + var12.format(new Date()) + "><" + var9 + "><" + var8 + "> ";
      }

      return var7 + var11;
   }

   public String NULL_PROPERTYDESCRIPTOR_TYPE(String var1, String var2, String var3, String var4, String var5, String var6) {
      String var7 = "";
      String var8 = "NULL_PROPERTYDESCRIPTOR_TYPE";
      String var9 = "Connector Compliance";
      Object[] var10 = new Object[]{var1, var2, var3, var4, var5, var6};
      String var11 = MessageFormat.format(this.l10n.get(var8), var10);
      if (this.getExtendedFormat()) {
         DateFormat var12 = DateFormat.getDateTimeInstance(2, 1);
         var7 = "<" + var12.format(new Date()) + "><" + var9 + "><" + var8 + "> ";
      }

      return var7 + var11;
   }
}
