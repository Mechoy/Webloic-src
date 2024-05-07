package weblogic.ejb.container.compliance;

import java.io.IOException;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Locale;
import weblogic.i18n.Localizer;
import weblogic.i18ntools.L10nLookup;

public class EJBComplianceTextFormatter {
   private Localizer l10n;
   private boolean format = false;

   public EJBComplianceTextFormatter() {
      this.l10n = L10nLookup.getLocalizer(Locale.getDefault(), "weblogic.ejb.container.compliance.EJBComplianceTextLocalizer", EJBComplianceTextFormatter.class.getClassLoader());
   }

   public EJBComplianceTextFormatter(Locale var1) {
      this.l10n = L10nLookup.getLocalizer(var1, "weblogic.ejb.container.compliance.EJBComplianceTextLocalizer", EJBComplianceTextFormatter.class.getClassLoader());
   }

   public static EJBComplianceTextFormatter getInstance() {
      return new EJBComplianceTextFormatter();
   }

   public static EJBComplianceTextFormatter getInstance(Locale var0) {
      return new EJBComplianceTextFormatter(var0);
   }

   public void setExtendedFormat(boolean var1) {
      this.format = var1;
   }

   public boolean getExtendedFormat() {
      return this.format;
   }

   public String warning() {
      String var1 = "";
      String var2 = "WARNING";
      String var3 = "EJB Compliance";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String error() {
      String var1 = "";
      String var2 = "ERROR";
      String var3 = "EJB Compliance";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String logWriteError(String var1, String var2) {
      String var3 = "";
      String var4 = "LOG_WRITE_ERROR";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String errorMessage() {
      String var1 = "";
      String var2 = "ERROR_MESSAGE";
      String var3 = "EJB Compliance";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String PUBLIC_EJBCREATE(String var1) {
      String var2 = "";
      String var3 = "PUBLIC_EJBCREATE";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String FINAL_EJBCREATE(String var1) {
      String var2 = "";
      String var3 = "FINAL_EJBCREATE";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String STATIC_EJBCREATE(String var1) {
      String var2 = "";
      String var3 = "STATIC_EJBCREATE";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String ENTITY_IMPLEMENT_SESSIONSYNCHRONIZATION(String var1) {
      String var2 = "";
      String var3 = "ENTITY_IMPLEMENT_SESSIONSYNCHRONIZATION";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String STATELESS_IMPLEMENT_SESSIONSYNCHRONIZATION(String var1) {
      String var2 = "";
      String var3 = "STATELESS_IMPLEMENT_SESSIONSYNCHRONIZATION";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String BEAN_MANAGED_IMPLEMENT_SESSIONSYNCHRONIZATION(String var1) {
      String var2 = "";
      String var3 = "BEAN_MANAGED_IMPLEMENT_SESSIONSYNCHRONIZATION";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String PUBLIC_BEAN_CLASS(String var1) {
      String var2 = "";
      String var3 = "PUBLIC_BEAN_CLASS";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String FINAL_BEAN_CLASS(String var1) {
      String var2 = "";
      String var3 = "FINAL_BEAN_CLASS";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String PUBLIC_NOARG_BEAN_CTOR(String var1) {
      String var2 = "";
      String var3 = "PUBLIC_NOARG_BEAN_CTOR";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String NO_FINALIZE_IN_BEAN(String var1) {
      String var2 = "";
      String var3 = "NO_FINALIZE_IN_BEAN";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String NO_SYNCHRONIZED_METHODS(String var1, String var2) {
      String var3 = "";
      String var4 = "NO_SYNCHRONIZED_METHODS";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String BEAN_MUST_NOT_THROW_REMOTE_EXCEPTION(String var1, String var2) {
      String var3 = "";
      String var4 = "BEAN_MUST_NOT_THROW_REMOTE_EXCEPTION";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String BEAN_MUST_NOT_THROW_RUNTIME_EXCEPTION(String var1, String var2) {
      String var3 = "";
      String var4 = "BEAN_MUST_NOT_THROW_RUNTIME_EXCEPTION";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String NOT_ALL_BEANS_USE_SAME_PERSISTENCE(String var1) {
      String var2 = "";
      String var3 = "NOT_ALL_BEANS_USE_SAME_PERSISTENCE";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String ABSTRACT_SCHEMA_NAME_NOT_UNIQUE(String var1) {
      String var2 = "";
      String var3 = "ABSTRACT_SCHEMA_NAME_NOT_UNIQUE";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String ABSTRACT_BEAN_CLASS(String var1) {
      String var2 = "";
      String var3 = "ABSTRACT_BEAN_CLASS";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String ISMODIFIED_NOT_EXIST(String var1, String var2) {
      String var3 = "";
      String var4 = "ISMODIFIED_NOT_EXIST";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String ISMODIFIED_RETURNS_BOOL(String var1, String var2) {
      String var3 = "";
      String var4 = "ISMODIFIED_RETURNS_BOOL";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String CMP_FIELDS_MUST_BE_BEAN_FIELDS(String var1, String var2) {
      String var3 = "";
      String var4 = "CMP_FIELDS_MUST_BE_BEAN_FIELDS";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String CMP_FIELDS_MUST_BE_PUBLIC(String var1, String var2) {
      String var3 = "";
      String var4 = "CMP_FIELDS_MUST_BE_PUBLIC";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String CMP_FIELDS_MUST_NOT_BE_STATIC(String var1, String var2) {
      String var3 = "";
      String var4 = "CMP_FIELDS_MUST_NOT_BE_STATIC";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String PK_FIELD_MUST_EXIST(String var1, String var2) {
      String var3 = "";
      String var4 = "PK_FIELD_MUST_EXIST";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String PK_FIELD_WRONG_TYPE(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "PK_FIELD_WRONG_TYPE";
      String var6 = "EJB Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String CMP11_CANNOT_USE_OPTIMISTIC_CONCURRENCY(String var1) {
      String var2 = "";
      String var3 = "CMP11_CANNOT_USE_OPTIMISTIC_CONCURRENCY";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String CM_FIELD_MUST_START_WITH_LOWERCASE(String var1, String var2) {
      String var3 = "";
      String var4 = "CM_FIELD_MUST_START_WITH_LOWERCASE";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String DEFINE_CMP_ACCESSOR_METHOD_20(String var1, String var2) {
      String var3 = "";
      String var4 = "DEFINE_CMP_ACCESSOR_METHOD_20";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String SETTER_DOES_NOT_RETURN_VOID(String var1) {
      String var2 = "";
      String var3 = "SETTER_DOES_NOT_RETURN_VOID";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String CMP_ACCESSOR_NOT_PUBLIC(String var1, String var2) {
      String var3 = "";
      String var4 = "CMP_ACCESSOR_NOT_PUBLIC";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String SETTER_DOES_NOT_HAVE_SINGLE_PARAM(String var1, String var2) {
      String var3 = "";
      String var4 = "SETTER_DOES_NOT_HAVE_SINGLE_PARAM";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String SETTER_PARAM_DOES_NOT_MATCH_GETTER_RETURN(String var1, String var2) {
      String var3 = "";
      String var4 = "SETTER_PARAM_DOES_NOT_MATCH_GETTER_RETURN";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String DO_NOT_DEFINE_CMFIELD_20(String var1) {
      String var2 = "";
      String var3 = "DO_NOT_DEFINE_CMFIELD_20";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String EJB_SELECT_CANNOT_RETURN_ENUMERATION(String var1, String var2) {
      String var3 = "";
      String var4 = "EJB_SELECT_CANNOT_RETURN_ENUMERATION";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String EJB_SELECT_MUST_THROW(String var1, String var2) {
      String var3 = "";
      String var4 = "EJB_SELECT_MUST_THROW";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String MISSING_ENTITY_BEAN_METHOD(String var1, String var2) {
      String var3 = "";
      String var4 = "MISSING_ENTITY_BEAN_METHOD";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String PRIMKEY_CLASS_DOES_NOT_MATCH_ACCESSOR_FOR_GETTER(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "PRIMKEY_CLASS_DOES_NOT_MATCH_ACCESSOR_FOR_GETTER";
      String var6 = "EJB Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String PRIMKEY_CLASS_DOES_NOT_MATCH_ACCESSOR_FOR_SETTER(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "PRIMKEY_CLASS_DOES_NOT_MATCH_ACCESSOR_FOR_SETTER";
      String var6 = "EJB Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String BEAN_PK_CLASS_DOES_NOT_MATCH_PKFIELD_FOR_GETTER(String var1, String var2, String var3, String var4) {
      String var5 = "";
      String var6 = "BEAN_PK_CLASS_DOES_NOT_MATCH_PKFIELD_FOR_GETTER";
      String var7 = "EJB Compliance";
      Object[] var8 = new Object[]{var1, var2, var3, var4};
      String var9 = MessageFormat.format(this.l10n.get(var6), var8);
      if (this.getExtendedFormat()) {
         DateFormat var10 = DateFormat.getDateTimeInstance(2, 1);
         var5 = "<" + var10.format(new Date()) + "><" + var7 + "><" + var6 + "> ";
      }

      return var5 + var9;
   }

   public String BEAN_PK_CLASS_DOES_NOT_MATCH_PKFIELD_FOR_SETTER(String var1, String var2, String var3, String var4) {
      String var5 = "";
      String var6 = "BEAN_PK_CLASS_DOES_NOT_MATCH_PKFIELD_FOR_SETTER";
      String var7 = "EJB Compliance";
      Object[] var8 = new Object[]{var1, var2, var3, var4};
      String var9 = MessageFormat.format(this.l10n.get(var6), var8);
      if (this.getExtendedFormat()) {
         DateFormat var10 = DateFormat.getDateTimeInstance(2, 1);
         var5 = "<" + var10.format(new Date()) + "><" + var7 + "><" + var6 + "> ";
      }

      return var5 + var9;
   }

   public String BMP_CANNOT_USE_OPTIMISTIC_CONCURRENCY(String var1) {
      String var2 = "";
      String var3 = "BMP_CANNOT_USE_OPTIMISTIC_CONCURRENCY";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String jarFileMissing(String var1) {
      String var2 = "";
      String var3 = "jarFileMissing";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String checkingJarFile(String var1) {
      String var2 = "";
      String var3 = "checkingJarFile";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String jarFileIsDirectory(String var1) {
      String var2 = "";
      String var3 = "jarFileIsDirectory";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String compliant(String var1) {
      String var2 = "";
      String var3 = "compliant";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String notValid(String var1, IOException var2) {
      String var3 = "";
      String var4 = "notValid";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String failedToParse(String var1, Exception var2) {
      String var3 = "";
      String var4 = "failedToParse";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String failedToLoad(String var1, Exception var2) {
      String var3 = "";
      String var4 = "failedToLoad";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String complianceError(String var1) {
      String var2 = "";
      String var3 = "complianceError";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String loadFailure(String var1) {
      String var2 = "";
      String var3 = "loadFailure";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String EO_IMPLEMENTS_EJBOBJECT(String var1) {
      String var2 = "";
      String var3 = "EO_IMPLEMENTS_EJBOBJECT";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String ELO_IMPLEMENTS_EJB_LOCAL_OBJECT(String var1) {
      String var2 = "";
      String var3 = "ELO_IMPLEMENTS_EJB_LOCAL_OBJECT";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String EO_THROWS_REMOTE_EXCEPTION(String var1, String var2) {
      String var3 = "";
      String var4 = "EO_THROWS_REMOTE_EXCEPTION";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String ELO_THROWS_REMOTE_EXCEPTION(String var1, String var2) {
      String var3 = "";
      String var4 = "ELO_THROWS_REMOTE_EXCEPTION";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String EO_RETURN_TYPE_DOESNT_MATCH_BEAN(String var1, String var2) {
      String var3 = "";
      String var4 = "EO_RETURN_TYPE_DOESNT_MATCH_BEAN";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String ELO_RETURN_TYPE_DOESNT_MATCH_BEAN(String var1, String var2) {
      String var3 = "";
      String var4 = "ELO_RETURN_TYPE_DOESNT_MATCH_BEAN";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String EO_METHOD_DOESNT_EXIST_IN_BEAN(String var1, String var2) {
      String var3 = "";
      String var4 = "EO_METHOD_DOESNT_EXIST_IN_BEAN";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String ELO_METHOD_DOESNT_EXIST_IN_BEAN(String var1, String var2) {
      String var3 = "";
      String var4 = "ELO_METHOD_DOESNT_EXIST_IN_BEAN";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String EO_EXCEPTION_TYPE_DOESNT_MATCH_BEAN(String var1, String var2) {
      String var3 = "";
      String var4 = "EO_EXCEPTION_TYPE_DOESNT_MATCH_BEAN";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String ELO_EXCEPTION_TYPE_DOESNT_MATCH_BEAN(String var1, String var2) {
      String var3 = "";
      String var4 = "ELO_EXCEPTION_TYPE_DOESNT_MATCH_BEAN";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String INVALID_APPLICATION_EXCEPTION(String var1, String var2, String var3, String var4) {
      String var5 = "";
      String var6 = "INVALID_APPLICATION_EXCEPTION";
      String var7 = "EJB Compliance";
      Object[] var8 = new Object[]{var1, var2, var3, var4};
      String var9 = MessageFormat.format(this.l10n.get(var6), var8);
      if (this.getExtendedFormat()) {
         DateFormat var10 = DateFormat.getDateTimeInstance(2, 1);
         var5 = "<" + var10.format(new Date()) + "><" + var7 + "><" + var6 + "> ";
      }

      return var5 + var9;
   }

   public String INVALID_APPLICATION_EXCEPTION_ON_HOME(String var1, String var2, String var3, String var4) {
      String var5 = "";
      String var6 = "INVALID_APPLICATION_EXCEPTION_ON_HOME";
      String var7 = "EJB Compliance";
      Object[] var8 = new Object[]{var1, var2, var3, var4};
      String var9 = MessageFormat.format(this.l10n.get(var6), var8);
      if (this.getExtendedFormat()) {
         DateFormat var10 = DateFormat.getDateTimeInstance(2, 1);
         var5 = "<" + var10.format(new Date()) + "><" + var7 + "><" + var6 + "> ";
      }

      return var5 + var9;
   }

   public String CANNOT_EXPOSE_RELATIONSHIP_ACCESSOR_IN_REMOTE(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "CANNOT_EXPOSE_RELATIONSHIP_ACCESSOR_IN_REMOTE";
      String var6 = "EJB Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String CREATES_MATCH_POSTCREATE(String var1, String var2) {
      String var3 = "";
      String var4 = "CREATES_MATCH_POSTCREATE";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String EXTRA_POSTCREATE(String var1, String var2) {
      String var3 = "";
      String var4 = "EXTRA_POSTCREATE";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String MUST_IMPLEMENT_ENTITYBEAN(String var1) {
      String var2 = "";
      String var3 = "MUST_IMPLEMENT_ENTITYBEAN";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String EJBCREATE_RETURNS_PK(String var1, String var2) {
      String var3 = "";
      String var4 = "EJBCREATE_RETURNS_PK";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String EJBPOSTCREATE_MUST_BE_PUBLIC(String var1, String var2) {
      String var3 = "";
      String var4 = "EJBPOSTCREATE_MUST_BE_PUBLIC";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String EJBPOSTCREATE_MUST_NOT_BE_FINAL(String var1, String var2) {
      String var3 = "";
      String var4 = "EJBPOSTCREATE_MUST_NOT_BE_FINAL";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String EJBPOSTCREATE_MUST_NOT_BE_STATIC(String var1, String var2) {
      String var3 = "";
      String var4 = "EJBPOSTCREATE_MUST_NOT_BE_STATIC";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String EJBPOSTCREATE_MUST_RETURN_VOID(String var1, String var2) {
      String var3 = "";
      String var4 = "EJBPOSTCREATE_MUST_RETURN_VOID";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String FINDER_IN_CMP_BEAN(String var1, String var2) {
      String var3 = "";
      String var4 = "FINDER_IN_CMP_BEAN";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String BEAN_MISSING_PERSISTENCE_USE(String var1) {
      String var2 = "";
      String var3 = "BEAN_MISSING_PERSISTENCE_USE";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String CMP_FIELD_CLASS_NOT_SUPPORTED_IN_CMP11(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "CMP_FIELD_CLASS_NOT_SUPPORTED_IN_CMP11";
      String var6 = "EJB Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String PRIMKEY_FIELD_MUST_BE_CMP_FIELD(String var1) {
      String var2 = "";
      String var3 = "PRIMKEY_FIELD_MUST_BE_CMP_FIELD";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String PK_FIELDS_MUST_BE_CMP_FIELDS(String var1, String var2) {
      String var3 = "";
      String var4 = "PK_FIELDS_MUST_BE_CMP_FIELDS";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String CACHE_BETWEEN_TRANS_MUST_BE_FALSE_FOR_CONCURRENCY_DB(String var1) {
      String var2 = "";
      String var3 = "CACHE_BETWEEN_TRANS_MUST_BE_FALSE_FOR_CONCURRENCY_DB";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String HOME_MUST_HAVE_FIND_PK(String var1) {
      String var2 = "";
      String var3 = "HOME_MUST_HAVE_FIND_PK";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String LOCAL_HOME_MUST_HAVE_FIND_PK(String var1) {
      String var2 = "";
      String var3 = "LOCAL_HOME_MUST_HAVE_FIND_PK";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String FIND_BY_PK_RETURNS_REMOTE_INTF(String var1) {
      String var2 = "";
      String var3 = "FIND_BY_PK_RETURNS_REMOTE_INTF";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String FIND_BY_PK_RETURNS_LOCAL_INTF(String var1) {
      String var2 = "";
      String var3 = "FIND_BY_PK_RETURNS_LOCAL_INTF";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String HOME_FIND_PK_CORRECT_PARAMETERS(String var1, String var2) {
      String var3 = "";
      String var4 = "HOME_FIND_PK_CORRECT_PARAMETERS";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String LOCAL_HOME_FIND_PK_CORRECT_PARAMETERS(String var1, String var2) {
      String var3 = "";
      String var4 = "LOCAL_HOME_FIND_PK_CORRECT_PARAMETERS";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String FINDER_MUST_THROW_FE(String var1, String var2) {
      String var3 = "";
      String var4 = "FINDER_MUST_THROW_FE";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String LOCAL_FINDER_MUST_THROW_FE(String var1, String var2) {
      String var3 = "";
      String var4 = "LOCAL_FINDER_MUST_THROW_FE";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String FINDER_RETURNS_BAD_TYPE(String var1, String var2) {
      String var3 = "";
      String var4 = "FINDER_RETURNS_BAD_TYPE";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String LOCAL_FINDER_RETURNS_BAD_TYPE(String var1, String var2) {
      String var3 = "";
      String var4 = "LOCAL_FINDER_RETURNS_BAD_TYPE";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String SCALAR_FINDER_DOESNT_RETURN_REMOTE_INTF(String var1, String var2) {
      String var3 = "";
      String var4 = "SCALAR_FINDER_DOESNT_RETURN_REMOTE_INTF";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String SCALAR_FINDER_DOESNT_RETURN_LOCAL_INTF(String var1, String var2) {
      String var3 = "";
      String var4 = "SCALAR_FINDER_DOESNT_RETURN_LOCAL_INTF";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String ENUM_FINDER_DOESNT_RETURN_REMOTE_INTF(String var1, String var2) {
      String var3 = "";
      String var4 = "ENUM_FINDER_DOESNT_RETURN_REMOTE_INTF";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String COLL_FINDER_DOESNT_RETURN_COLL(String var1, String var2) {
      String var3 = "";
      String var4 = "COLL_FINDER_DOESNT_RETURN_COLL";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String LOCAL_COLL_FINDER_DOESNT_RETURN_COLL(String var1, String var2) {
      String var3 = "";
      String var4 = "LOCAL_COLL_FINDER_DOESNT_RETURN_COLL";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String UNEXPECTED_FINDER_RETURN_TYPE(String var1, String var2) {
      String var3 = "";
      String var4 = "UNEXPECTED_FINDER_RETURN_TYPE";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String FIND_METHOD_DOESNT_EXIST_IN_BEAN(String var1, String var2) {
      String var3 = "";
      String var4 = "FIND_METHOD_DOESNT_EXIST_IN_BEAN";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String FIND_EXCEPTION_TYPE_DOESNT_MATCH_BEAN(String var1, String var2) {
      String var3 = "";
      String var4 = "FIND_EXCEPTION_TYPE_DOESNT_MATCH_BEAN";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String LOCAL_FIND_EXCEPTION_TYPE_DOESNT_MATCH_BEAN(String var1, String var2) {
      String var3 = "";
      String var4 = "LOCAL_FIND_EXCEPTION_TYPE_DOESNT_MATCH_BEAN";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String POST_CREATE_METHOD_DOESNT_EXIST_IN_BEAN(String var1, String var2) {
      String var3 = "";
      String var4 = "POST_CREATE_METHOD_DOESNT_EXIST_IN_BEAN";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String POST_CREATE_EXCEPTION_TYPE_DOESNT_MATCH_BEAN(String var1, String var2) {
      String var3 = "";
      String var4 = "POST_CREATE_EXCEPTION_TYPE_DOESNT_MATCH_BEAN";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String EXTRA_HOME_METHOD_20(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "EXTRA_HOME_METHOD_20";
      String var6 = "EJB Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String EXTRA_LOCAL_HOME_METHOD_20(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "EXTRA_LOCAL_HOME_METHOD_20";
      String var6 = "EJB Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String HOME_METHOD_NAME_IN_BEAN_CLASS_LOWER_CASE_20(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "HOME_METHOD_NAME_IN_BEAN_CLASS_LOWER_CASE_20";
      String var6 = "EJB Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String HOME_METHOD_NAME_IN_BEAN_CLASS_INCOMPLETE_20(String var1) {
      String var2 = "";
      String var3 = "HOME_METHOD_NAME_IN_BEAN_CLASS_INCOMPLETE_20";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String DUPLICATE_CMR_FIELD(String var1) {
      String var2 = "";
      String var3 = "DUPLICATE_CMR_FIELD";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String HOME_EXTENDS_EJBHOME(String var1, String var2) {
      String var3 = "";
      String var4 = "HOME_EXTENDS_EJBHOME";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String LOCAL_HOME_EXTENDS_EJBLOCALHOME(String var1, String var2) {
      String var3 = "";
      String var4 = "LOCAL_HOME_EXTENDS_EJBLOCALHOME";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String NOT_RMIIIOP_LEGAL_TYPE_20(String var1) {
      String var2 = "";
      String var3 = "NOT_RMIIIOP_LEGAL_TYPE_20";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String HOME_METHOD_NOT_THROW_REMOTE_EXCEPTION(String var1, String var2) {
      String var3 = "";
      String var4 = "HOME_METHOD_NOT_THROW_REMOTE_EXCEPTION";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String LOCAL_HOME_METHOD_THROW_REMOTE_EXCEPTION(String var1, String var2) {
      String var3 = "";
      String var4 = "LOCAL_HOME_METHOD_THROW_REMOTE_EXCEPTION";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String CREATE_METHOD_RETURNS_COMPONENT_INTERFACE(String var1, String var2) {
      String var3 = "";
      String var4 = "CREATE_METHOD_RETURNS_COMPONENT_INTERFACE";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String CREATE_METHOD_RETURNS_LOCAL_COMPONENT_INTERFACE(String var1, String var2) {
      String var3 = "";
      String var4 = "CREATE_METHOD_RETURNS_LOCAL_COMPONENT_INTERFACE";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String CREATE_METHOD_THROWS_CREATE_EXCEPTION(String var1, String var2) {
      String var3 = "";
      String var4 = "CREATE_METHOD_THROWS_CREATE_EXCEPTION";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String LOCAL_CREATE_METHOD_THROWS_CREATE_EXCEPTION(String var1, String var2) {
      String var3 = "";
      String var4 = "LOCAL_CREATE_METHOD_THROWS_CREATE_EXCEPTION";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String CREATE_METHOD_DOESNT_EXIST_IN_BEAN(String var1, String var2) {
      String var3 = "";
      String var4 = "CREATE_METHOD_DOESNT_EXIST_IN_BEAN";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String LOCAL_CREATE_METHOD_DOESNT_EXIST_IN_BEAN(String var1, String var2) {
      String var3 = "";
      String var4 = "LOCAL_CREATE_METHOD_DOESNT_EXIST_IN_BEAN";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String CREATE_EXCEPTION_TYPE_DOESNT_MATCH_BEAN(String var1, String var2) {
      String var3 = "";
      String var4 = "CREATE_EXCEPTION_TYPE_DOESNT_MATCH_BEAN";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String LOCAL_CREATE_EXCEPTION_TYPE_DOESNT_MATCH_BEAN(String var1, String var2) {
      String var3 = "";
      String var4 = "LOCAL_CREATE_EXCEPTION_TYPE_DOESNT_MATCH_BEAN";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String BEAN_CLASS_IMPLEMENTS_MESSAGE_DRIVEN(String var1) {
      String var2 = "";
      String var3 = "BEAN_CLASS_IMPLEMENTS_MESSAGE_DRIVEN";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String BEAN_CLASS_IMPLEMENTS_MESSAGE_LISTENER(String var1, String var2) {
      String var3 = "";
      String var4 = "BEAN_CLASS_IMPLEMENTS_MESSAGE_LISTENER";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String PUBLIC_ONMESSAGE(String var1) {
      String var2 = "";
      String var3 = "PUBLIC_ONMESSAGE";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String FINAL_ONMESSAGE(String var1) {
      String var2 = "";
      String var3 = "FINAL_ONMESSAGE";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String STATIC_ONMESSAGE(String var1) {
      String var2 = "";
      String var3 = "STATIC_ONMESSAGE";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String SINGLE_ONMESSAGE_REQUIRED(String var1) {
      String var2 = "";
      String var3 = "SINGLE_ONMESSAGE_REQUIRED";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String ONMESSAGE_TAKES_MESSAGE(String var1) {
      String var2 = "";
      String var3 = "ONMESSAGE_TAKES_MESSAGE";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String ONMESSAGE_RETURNS_VOID(String var1) {
      String var2 = "";
      String var3 = "ONMESSAGE_RETURNS_VOID";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String ONMESSAGE_THROWS_APP_EXCEPTION(String var1) {
      String var2 = "";
      String var3 = "ONMESSAGE_THROWS_APP_EXCEPTION";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String BEAN_MUST_HAVE_ONMESSAGE(String var1) {
      String var2 = "";
      String var3 = "BEAN_MUST_HAVE_ONMESSAGE";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String MT_METHOD_DOESNT_EXIST_IN_BEAN(String var1, String var2) {
      String var3 = "";
      String var4 = "MT_METHOD_DOESNT_EXIST_IN_BEAN";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String EJBCREATE_RETURNS_VOID(String var1) {
      String var2 = "";
      String var3 = "EJBCREATE_RETURNS_VOID";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String MESSAGE_NOARG_EJBCREATE(String var1) {
      String var2 = "";
      String var3 = "MESSAGE_NOARG_EJBCREATE";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String MESSAGE_EJBCREATE_THROWS_APP_EXCEPTION(String var1) {
      String var2 = "";
      String var3 = "MESSAGE_EJBCREATE_THROWS_APP_EXCEPTION";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String MESSAGE_DEFINES_EJBCREATE(String var1) {
      String var2 = "";
      String var3 = "MESSAGE_DEFINES_EJBCREATE";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String PUBLIC_EJBREMOVE(String var1) {
      String var2 = "";
      String var3 = "PUBLIC_EJBREMOVE";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String FINAL_EJBREMOVE(String var1) {
      String var2 = "";
      String var3 = "FINAL_EJBREMOVE";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String STATIC_EJBREMOVE(String var1) {
      String var2 = "";
      String var3 = "STATIC_EJBREMOVE";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String EJBREMOVE_RETURNS_VOID(String var1) {
      String var2 = "";
      String var3 = "EJBREMOVE_RETURNS_VOID";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String MESSAGE_NOARG_EJBREMOVE(String var1) {
      String var2 = "";
      String var3 = "MESSAGE_NOARG_EJBREMOVE";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String MESSAGE_EJBREMOVE_THROWS_APP_EXCEPTION(String var1) {
      String var2 = "";
      String var3 = "MESSAGE_EJBREMOVE_THROWS_APP_EXCEPTION";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String MESSAGE_DEFINES_EJBREMOVE(String var1) {
      String var2 = "";
      String var3 = "MESSAGE_DEFINES_EJBREMOVE";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String MESSAGE_BAD_TX_ATTRIBUTE(String var1) {
      String var2 = "";
      String var3 = "MESSAGE_BAD_TX_ATTRIBUTE";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String PK_MUST_IMPLEMENT_HASHCODE(String var1) {
      String var2 = "";
      String var3 = "PK_MUST_IMPLEMENT_HASHCODE";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String PK_MUST_IMPLEMENT_EQUALS(String var1) {
      String var2 = "";
      String var3 = "PK_MUST_IMPLEMENT_EQUALS";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String CMP_PK_MUST_IMPLEMENT_SERIALIZABLE(String var1) {
      String var2 = "";
      String var3 = "CMP_PK_MUST_IMPLEMENT_SERIALIZABLE";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String CMP_PK_CANNOT_BE_JAVA_LANG_OBJECT(String var1) {
      String var2 = "";
      String var3 = "CMP_PK_CANNOT_BE_JAVA_LANG_OBJECT";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String CMP_PK_MUST_BE_PUBLIC(String var1, String var2) {
      String var3 = "";
      String var4 = "CMP_PK_MUST_BE_PUBLIC";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String CMP_PK_MUST_HAVE_NOARG_CONSTRUCTOR(String var1, String var2) {
      String var3 = "";
      String var4 = "CMP_PK_MUST_HAVE_NOARG_CONSTRUCTOR";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String PK_FIELDS_MUST_BE_PUBLIC(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "PK_FIELDS_MUST_BE_PUBLIC";
      String var6 = "EJB Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String PK_FIELDS_MUST_NOT_BE_STATIC(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "PK_FIELDS_MUST_NOT_BE_STATIC";
      String var6 = "EJB Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String N1_RELATION_HAS_DUP_FIELD_FOR_SAME_BEAN(String var1) {
      String var2 = "";
      String var3 = "N1_RELATION_HAS_DUP_FIELD_FOR_SAME_BEAN";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String BOTH_ROLES_REMOTE(String var1) {
      String var2 = "";
      String var3 = "BOTH_ROLES_REMOTE";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String NON_EXISTENT_BEAN_IN_ROLE(String var1, String var2) {
      String var3 = "";
      String var4 = "NON_EXISTENT_BEAN_IN_ROLE";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String NON_ENTITY_BEAN_IN_ROLE(String var1) {
      String var2 = "";
      String var3 = "NON_ENTITY_BEAN_IN_ROLE";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String BM_BEAN_IN_ROLE(String var1) {
      String var2 = "";
      String var3 = "BM_BEAN_IN_ROLE";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String CMP11_BEAN_IN_ROLE(String var1) {
      String var2 = "";
      String var3 = "CMP11_BEAN_IN_ROLE";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String FIELD_NOT_DEFINED_FOR_ROLE(String var1) {
      String var2 = "";
      String var3 = "FIELD_NOT_DEFINED_FOR_ROLE";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String COLLECTION_FIELD_HAS_NO_TYPE(String var1) {
      String var2 = "";
      String var3 = "COLLECTION_FIELD_HAS_NO_TYPE";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String SINGLETON_FIELD_HAS_TYPE(String var1) {
      String var2 = "";
      String var3 = "SINGLETON_FIELD_HAS_TYPE";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String GET_METHOD_NOT_DEFINED_FOR_ROLE(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "GET_METHOD_NOT_DEFINED_FOR_ROLE";
      String var6 = "EJB Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String GET_METHOD_HAS_WRONG_RETURN_TYPE(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "GET_METHOD_HAS_WRONG_RETURN_TYPE";
      String var6 = "EJB Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String GET_METHOD_IS_NOT_ABSTRACT(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "GET_METHOD_IS_NOT_ABSTRACT";
      String var6 = "EJB Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String GET_METHOD_IS_NOT_PUBLIC(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "GET_METHOD_IS_NOT_PUBLIC";
      String var6 = "EJB Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String SET_METHOD_NOT_DEFINED_FOR_ROLE(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "SET_METHOD_NOT_DEFINED_FOR_ROLE";
      String var6 = "EJB Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String SET_METHOD_HAS_WRONG_RETURN_TYPE(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "SET_METHOD_HAS_WRONG_RETURN_TYPE";
      String var6 = "EJB Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String SET_METHOD_IS_NOT_ABSTRACT(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "SET_METHOD_IS_NOT_ABSTRACT";
      String var6 = "EJB Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String SET_METHOD_IS_NOT_PUBLIC(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "SET_METHOD_IS_NOT_PUBLIC";
      String var6 = "EJB Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String CMR_FIELD_SAME_AS_CMP_FIELD(String var1) {
      String var2 = "";
      String var3 = "CMR_FIELD_SAME_AS_CMP_FIELD";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String CASCADE_DELETE_CANNOT_BE_SPECIFIED(String var1) {
      String var2 = "";
      String var3 = "CASCADE_DELETE_CANNOT_BE_SPECIFIED";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String EJBCONTEXT_IS_TRANSIENT(String var1) {
      String var2 = "";
      String var3 = "EJBCONTEXT_IS_TRANSIENT";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String BEAN_IMPLEMENT_SESSIONBEAN(String var1) {
      String var2 = "";
      String var3 = "BEAN_IMPLEMENT_SESSIONBEAN";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String STATELESS_NOARG_EJBCREATE(String var1) {
      String var2 = "";
      String var3 = "STATELESS_NOARG_EJBCREATE";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String STATEFUL_DEFINE_EJBCREATE(String var1) {
      String var2 = "";
      String var3 = "STATEFUL_DEFINE_EJBCREATE";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String STATEFUL_HOME_CREATE(String var1) {
      String var2 = "";
      String var3 = "STATEFUL_HOME_CREATE";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String STATEFUL_LOCAL_HOME_CREATE(String var1) {
      String var2 = "";
      String var3 = "STATEFUL_LOCAL_HOME_CREATE";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String STATELESS_HOME_NOARG_CREATE(String var1) {
      String var2 = "";
      String var3 = "STATELESS_HOME_NOARG_CREATE";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String STATELESS_LOCAL_HOME_NOARG_CREATE(String var1) {
      String var2 = "";
      String var3 = "STATELESS_LOCAL_HOME_NOARG_CREATE";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String HOME_METHODS_NOT_ALLOWED_ON_SESSION_20(String var1, String var2) {
      String var3 = "";
      String var4 = "HOME_METHODS_NOT_ALLOWED_ON_SESSION_20";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String NO_MATCHING_BEAN(String var1) {
      String var2 = "";
      String var3 = "NO_MATCHING_BEAN";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String OVERLOADED_ABSTRACT_METHOD(String var1, String var2) {
      String var3 = "";
      String var4 = "OVERLOADED_ABSTRACT_METHOD";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String EXTRA_ABSTRACT_METHOD(String var1, String var2) {
      String var3 = "";
      String var4 = "EXTRA_ABSTRACT_METHOD";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String NO_MATCHING_FIELD_MAP(String var1, String var2) {
      String var3 = "";
      String var4 = "NO_MATCHING_FIELD_MAP";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String FIELDCLASSTYPE_MUST_BE_STRING_FOR_ORACLECLOB_COLUMNTYPE(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "FIELDCLASSTYPE_MUST_BE_STRING_FOR_ORACLECLOB_COLUMNTYPE";
      String var6 = "EJB Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String NO_MATCHING_CMP_FIELD(String var1, String var2) {
      String var3 = "";
      String var4 = "NO_MATCHING_CMP_FIELD";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String GROUP_CONTAINS_UNDEFINED_CMP_FIELD(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "GROUP_CONTAINS_UNDEFINED_CMP_FIELD";
      String var6 = "EJB Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String GROUP_CONTAINS_UNDEFINED_CMR_FIELD(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "GROUP_CONTAINS_UNDEFINED_CMR_FIELD";
      String var6 = "EJB Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String RELATIONSHIP_CACHING_CANNOT_BE_SPECIFIED(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "RELATIONSHIP_CACHING_CANNOT_BE_SPECIFIED";
      String var6 = "EJB Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String RELATIONSHIP_CACHING_CONTAINS_UNDEFINED_CMR_FIELD(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "RELATIONSHIP_CACHING_CONTAINS_UNDEFINED_CMR_FIELD";
      String var6 = "EJB Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String RELATIONSHIP_CACHING_CONTAINS_UNDEFINED_GROUP_NAME(String var1, String var2, String var3, String var4) {
      String var5 = "";
      String var6 = "RELATIONSHIP_CACHING_CONTAINS_UNDEFINED_GROUP_NAME";
      String var7 = "EJB Compliance";
      Object[] var8 = new Object[]{var1, var2, var3, var4};
      String var9 = MessageFormat.format(this.l10n.get(var6), var8);
      if (this.getExtendedFormat()) {
         DateFormat var10 = DateFormat.getDateTimeInstance(2, 1);
         var5 = "<" + var10.format(new Date()) + "><" + var7 + "><" + var6 + "> ";
      }

      return var5 + var9;
   }

   public String RELATIONSHIP_CACHING_INCONSISTENT_CONCURRENCY_STRATEGY(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "RELATIONSHIP_CACHING_INCONSISTENT_CONCURRENCY_STRATEGY";
      String var6 = "EJB Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String RELATIONSHIP_CACHING_REQUIRE_DATABASETYPE(String var1) {
      String var2 = "";
      String var3 = "RELATIONSHIP_CACHING_REQUIRE_DATABASETYPE";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String DelayDatabaseInsertUntilConflictEnableBatchOperations(String var1) {
      String var2 = "";
      String var3 = "DelayDatabaseInsertUntilConflictEnableBatchOperations";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String GROUP_CONTAINS_PK_SUBSET(String var1, String var2) {
      String var3 = "";
      String var4 = "GROUP_CONTAINS_PK_SUBSET";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String QUERY_CONTAINS_UNDEFINED_GROUP(String var1, String var2) {
      String var3 = "";
      String var4 = "QUERY_CONTAINS_UNDEFINED_GROUP";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String QUERY_CONTAINS_UNDEFINED_CACHING_NAME(String var1, String var2) {
      String var3 = "";
      String var4 = "QUERY_CONTAINS_UNDEFINED_CACHING_NAME";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String GENKEY_PK_IS_INTEGER(String var1) {
      String var2 = "";
      String var3 = "GENKEY_PK_IS_INTEGER";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String GENKEY_PK_SEQUENCE_WITH_UNSUPPORTED_DB(String var1, String var2) {
      String var3 = "";
      String var4 = "GENKEY_PK_SEQUENCE_WITH_UNSUPPORTED_DB";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String GENKEY_PK_IDENTITY_WITH_UNSUPPORTED_DB(String var1, String var2) {
      String var3 = "";
      String var4 = "GENKEY_PK_IDENTITY_WITH_UNSUPPORTED_DB";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String GENKEY_PK_IS_INTEGER_OR_LONG(String var1) {
      String var2 = "";
      String var3 = "GENKEY_PK_IS_INTEGER_OR_LONG";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String AUTO_PK_KEY_CACHE_SIZE_NOT_SPECIFIED(String var1) {
      String var2 = "";
      String var3 = "AUTO_PK_KEY_CACHE_SIZE_NOT_SPECIFIED";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String VERSION_FIELD_WRONG_TYPE(String var1, String var2, String var3, String var4, String var5) {
      String var6 = "";
      String var7 = "VERSION_FIELD_WRONG_TYPE";
      String var8 = "EJB Compliance";
      Object[] var9 = new Object[]{var1, var2, var3, var4, var5};
      String var10 = MessageFormat.format(this.l10n.get(var7), var9);
      if (this.getExtendedFormat()) {
         DateFormat var11 = DateFormat.getDateTimeInstance(2, 1);
         var6 = "<" + var11.format(new Date()) + "><" + var8 + "><" + var7 + "> ";
      }

      return var6 + var10;
   }

   public String TIMESTAMP_FIELD_WRONG_TYPE(String var1, String var2, String var3, String var4, String var5) {
      String var6 = "";
      String var7 = "TIMESTAMP_FIELD_WRONG_TYPE";
      String var8 = "EJB Compliance";
      Object[] var9 = new Object[]{var1, var2, var3, var4, var5};
      String var10 = MessageFormat.format(this.l10n.get(var7), var9);
      if (this.getExtendedFormat()) {
         DateFormat var11 = DateFormat.getDateTimeInstance(2, 1);
         var6 = "<" + var11.format(new Date()) + "><" + var8 + "><" + var7 + "> ";
      }

      return var6 + var10;
   }

   public String MISSING_VERIFY_COLUMNS(String var1, String var2) {
      String var3 = "";
      String var4 = "MISSING_VERIFY_COLUMNS";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String ILLEGAL_VERIFY_COLUMNS(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "ILLEGAL_VERIFY_COLUMNS";
      String var6 = "EJB Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String MISSING_OPTIMISTIC_COLUMN(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "MISSING_OPTIMISTIC_COLUMN";
      String var6 = "EJB Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String ILLEGAL_VERIFY_ROWS(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "ILLEGAL_VERIFY_ROWS";
      String var6 = "EJB Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String ILLEGAL_VERIFY_READ_MODIFIED(String var1, String var2) {
      String var3 = "";
      String var4 = "ILLEGAL_VERIFY_READ_MODIFIED";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String NoSqlSelectDistinctWithBlobClobField(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "NoSqlSelectDistinctWithBlobClobField";
      String var6 = "EJB Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String BLOB_CLOB_WITH_UNKNOWN_DB(String var1) {
      String var2 = "";
      String var3 = "BLOB_CLOB_WITH_UNKNOWN_DB";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String BLOB_CLOB_WITH_UNSUPPORTED_DB(String var1, String var2) {
      String var3 = "";
      String var4 = "BLOB_CLOB_WITH_UNSUPPORTED_DB";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String DEPENDENT_OBJECTS_NOT_SUPPORTED() {
      String var1 = "";
      String var2 = "DEPENDENT_OBJECTS_NOT_SUPPORTED";
      String var3 = "EJB Compliance";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String NO_MATCHING_CMP_BEAN(String var1) {
      String var2 = "";
      String var3 = "NO_MATCHING_CMP_BEAN";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String NO_MATCHING_EJB_RELATION(String var1) {
      String var2 = "";
      String var3 = "NO_MATCHING_EJB_RELATION";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String NO_MATCHING_EJB_RELATION_IN_EJB_DD(String var1) {
      String var2 = "";
      String var3 = "NO_MATCHING_EJB_RELATION_IN_EJB_DD";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String MISSING_RELATION_FOR_BEAN(String var1, String var2) {
      String var3 = "";
      String var4 = "MISSING_RELATION_FOR_BEAN";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String MISSING_BEAN_FOR_BEAN(String var1, String var2) {
      String var3 = "";
      String var4 = "MISSING_BEAN_FOR_BEAN";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String NO_MATCHING_WL_RELATIONSHIP_ROLE(String var1, String var2) {
      String var3 = "";
      String var4 = "NO_MATCHING_WL_RELATIONSHIP_ROLE";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String MANY_SIDE_OF_M_1_MUST_HAVE_FOREIGN_KEY_MAP(String var1) {
      String var2 = "";
      String var3 = "MANY_SIDE_OF_M_1_MUST_HAVE_FOREIGN_KEY_MAP";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String ONE_SIDE_OF_M_1_MUST_NOT_HAVE_FOREIGN_KEY_MAP(String var1) {
      String var2 = "";
      String var3 = "ONE_SIDE_OF_M_1_MUST_NOT_HAVE_FOREIGN_KEY_MAP";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String ROLE_MAY_NOT_REFER_TO_DEPENDT_OBJ(String var1) {
      String var2 = "";
      String var3 = "ROLE_MAY_NOT_REFER_TO_DEPENDT_OBJ";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String NO_MATCHING_WL_RELATION(String var1) {
      String var2 = "";
      String var3 = "NO_MATCHING_WL_RELATION";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String NO_MATCHING_EJB_RELATIONSHIP_ROLE(String var1, String var2) {
      String var3 = "";
      String var4 = "NO_MATCHING_EJB_RELATIONSHIP_ROLE";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String MANY_TO_MANY_RELATIONSHIP_MUST_HAVE_BOTH_WL_ROLES(String var1) {
      String var2 = "";
      String var3 = "MANY_TO_MANY_RELATIONSHIP_MUST_HAVE_BOTH_WL_ROLES";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String MANY_TO_MANY_RELATIONSHIP_MISSING_TABLE_NAME(String var1) {
      String var2 = "";
      String var3 = "MANY_TO_MANY_RELATIONSHIP_MISSING_TABLE_NAME";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String RELATIONSHIP_ROLE_HAS_INVALID_GROUP(String var1, String var2) {
      String var3 = "";
      String var4 = "RELATIONSHIP_ROLE_HAS_INVALID_GROUP";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String ROLE_HAS_WRONG_NUMBER_OF_COLUMNS_IN_MAP(String var1, String var2) {
      String var3 = "";
      String var4 = "ROLE_HAS_WRONG_NUMBER_OF_COLUMNS_IN_MAP";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String ONE_ONE_MISSING_COLUMN_MAP(String var1) {
      String var2 = "";
      String var3 = "ONE_ONE_MISSING_COLUMN_MAP";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String MANY_MANY_MISSING_COLUMN_MAP(String var1) {
      String var2 = "";
      String var3 = "MANY_MANY_MISSING_COLUMN_MAP";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String ROLE_HAS_INVALID_KEY_COLUMN_IN_MAP(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "ROLE_HAS_INVALID_KEY_COLUMN_IN_MAP";
      String var6 = "EJB Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String ROLE_HAS_INVALID_KEY_COLUMN_IN_MAP2(String var1, String var2, String var3, String var4) {
      String var5 = "";
      String var6 = "ROLE_HAS_INVALID_KEY_COLUMN_IN_MAP2";
      String var7 = "EJB Compliance";
      Object[] var8 = new Object[]{var1, var2, var3, var4};
      String var9 = MessageFormat.format(this.l10n.get(var6), var8);
      if (this.getExtendedFormat()) {
         DateFormat var10 = DateFormat.getDateTimeInstance(2, 1);
         var5 = "<" + var10.format(new Date()) + "><" + var7 + "><" + var6 + "> ";
      }

      return var5 + var9;
   }

   public String FKCOLUMNS_MIX_OF_PK_NONPK_COLUMNS(String var1, String var2) {
      String var3 = "";
      String var4 = "FKCOLUMNS_MIX_OF_PK_NONPK_COLUMNS";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String INVALID_FOREIGN_KEY_FIELD_CLASS(String var1, String var2, String var3, String var4) {
      String var5 = "";
      String var6 = "INVALID_FOREIGN_KEY_FIELD_CLASS";
      String var7 = "EJB Compliance";
      Object[] var8 = new Object[]{var1, var2, var3, var4};
      String var9 = MessageFormat.format(this.l10n.get(var6), var8);
      if (this.getExtendedFormat()) {
         DateFormat var10 = DateFormat.getDateTimeInstance(2, 1);
         var5 = "<" + var10.format(new Date()) + "><" + var7 + "><" + var6 + "> ";
      }

      return var5 + var9;
   }

   public String CASCADE_DELETE_MUST_SPECIFIED_IF_DB_CASCADE_DELETE_SPECIFIED(String var1) {
      String var2 = "";
      String var3 = "CASCADE_DELETE_MUST_SPECIFIED_IF_DB_CASCADE_DELETE_SPECIFIED";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String CASCADE_DELETE_MUST_HAVE_FOREIGN_KEY_MAP(String var1) {
      String var2 = "";
      String var3 = "CASCADE_DELETE_MUST_HAVE_FOREIGN_KEY_MAP";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String ENTITYBEANINFOIMPL(String var1) {
      String var2 = "";
      String var3 = "ENTITYBEANINFOIMPL_INIT";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String EO_METHOD_SIGNATURE_DOES_NOT_MATCH_BEAN(String var1, String var2) {
      String var3 = "";
      String var4 = "EO_METHOD_SIGNATURE_DOES_NOT_MATCH_BEAN";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String ELO_METHOD_SIGNATURE_DOES_NOT_MATCH_BEAN(String var1, String var2) {
      String var3 = "";
      String var4 = "ELO_METHOD_SIGNATURE_DOES_NOT_MATCH_BEAN";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String DUPLICATE_MAPPING_FOR_DBMS_COLUMN(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "DUPLICATE_MAPPING_FOR_DBMS_COLUMN";
      String var6 = "EJB Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String DUPLICATE_MAPPING_FOR_CMP_FIELD(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "DUPLICATE_MAPPING_FOR_CMP_FIELD";
      String var6 = "EJB Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String ILLEGAL_VALUE_FOR_CHECK_EXISTS(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "ILLEGAL_VALUE_FOR_CHECK_EXISTS";
      String var6 = "EJB Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String EMPTY_CLIENT_JAR_TAG() {
      String var1 = "";
      String var2 = "EMPTY_CLIENT_JAR_TAG";
      String var3 = "EJB Compliance";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String PRIMARY_KEY_WITHOUT_PRIMKEY_FIELD(String var1, String var2) {
      String var3 = "";
      String var4 = "PRIMARY_KEY_WITHOUT_PRIMKEY_FIELD";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String CANNOT_FIND_WL_DESCRIPTOR_FOR_EJB(String var1) {
      String var2 = "";
      String var3 = "CANNOT_FIND_WL_DESCRIPTOR_FOR_EJB";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String CANNOT_FIND_CMP_DESCRIPTOR_FOR_EJB(String var1) {
      String var2 = "";
      String var3 = "CANNOT_FIND_CMP_DESCRIPTOR_FOR_EJB";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String MESSAGE_ILLEGAL_MAX_BEANS_IN_FREE_POOL(String var1, int var2) {
      String var3 = "";
      String var4 = "MESSAGE_ILLEGAL_MAX_BEANS_IN_FREE_POOL";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, new Integer(var2)};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String BEAN_MISSING_LREF_JNDI_NAME(String var1, String var2) {
      String var3 = "";
      String var4 = "BEAN_MISSING_LREF_JNDI_NAME";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String BEAN_MISSING_REF_JNDI_NAME(String var1, String var2) {
      String var3 = "";
      String var4 = "BEAN_MISSING_REF_JNDI_NAME";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String INCONSISTENT_REMOTE_VIEW(String var1) {
      String var2 = "";
      String var3 = "INCONSISTENT_REMOTE_VIEW";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String INCONSISTENT_LOCAL_VIEW(String var1) {
      String var2 = "";
      String var3 = "INCONSISTENT_LOCAL_VIEW";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String NULL_SECURITY_ROLE_REF_LINK(String var1, String var2) {
      String var3 = "";
      String var4 = "NULL_SECURITY_ROLE_REF_LINK";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String INVALID_SECURITY_ROLE_REF_LINK(String var1, String var2) {
      String var3 = "";
      String var4 = "INVALID_SECURITY_ROLE_REF_LINK";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String ROLE_NOT_MAPPED_TO_PRINCIPALS(String var1) {
      String var2 = "";
      String var3 = "ROLE_NOT_MAPPED_TO_PRINCIPALS";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String NO_CLIENT_VIEW(String var1) {
      String var2 = "";
      String var3 = "NO_CLIENT_VIEW";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String MISSING_QUERY_IN_EJBJAR(String var1, String var2) {
      String var3 = "";
      String var4 = "MISSING_QUERY_IN_EJBJAR";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String INVALID_QUERY_NAME(String var1, String var2) {
      String var3 = "";
      String var4 = "INVALID_QUERY_NAME";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String QUERY_NOT_FOUND(String var1, String var2) {
      String var3 = "";
      String var4 = "QUERY_NOT_FOUND";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String INVALID_RESULT_TYPE_LOCAL(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "INVALID_RESULT_TYPE_LOCAL";
      String var6 = "EJB Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String INVALID_RESULT_TYPE_REMOTE(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "INVALID_RESULT_TYPE_REMOTE";
      String var6 = "EJB Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String INVALIDATION_TARGET_DOES_NOT_EXIST(String var1, String var2) {
      String var3 = "";
      String var4 = "INVALIDATION_TARGET_DOES_NOT_EXIST";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String INVALIDATION_TARGET_MUST_BE_RO_ENTITY(String var1, String var2) {
      String var3 = "";
      String var4 = "INVALIDATION_TARGET_MUST_BE_RO_ENTITY";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String INVALIDATION_TARGET_CANNOT_BE_SET_FOR_RO_ENTITY(String var1) {
      String var2 = "";
      String var3 = "INVALIDATION_TARGET_CANNOT_BE_SET_FOR_RO_ENTITY";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String INVALIDATION_TARGET_MUST_BE_SET_ON_CMP20(String var1) {
      String var2 = "";
      String var3 = "INVALIDATION_TARGET_MUST_BE_SET_ON_CMP20";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String COULD_NOT_DETERMINE_RUN_AS_PRINCIPAL_FROM_ROLE_ASSIGNMENT(String var1, String var2) {
      String var3 = "";
      String var4 = "COULD_NOT_DETERMINE_RUN_AS_PRINCIPAL_FROM_ROLE_ASSIGNMENT";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String INVALID_RUN_AS_PRINCIPAL_FOR_EJB(String var1, String var2) {
      String var3 = "";
      String var4 = "INVALID_RUN_AS_PRINCIPAL_FOR_EJB";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String BUS_METHOD_NOT_PUBLIC(String var1, String var2) {
      String var3 = "";
      String var4 = "BUS_METHOD_NOT_PUBLIC";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String BUS_METHOD_MUST_NOT_FINAL(String var1, String var2) {
      String var3 = "";
      String var4 = "BUS_METHOD_MUST_NOT_FINAL";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String BUS_METHOD_MUST_NOT_STATIC(String var1, String var2) {
      String var3 = "";
      String var4 = "BUS_METHOD_MUST_NOT_STATIC";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String PK_FIELD_CLASS_MUST_HAVE_ATLEAST_ONE_CMP_FIELD(String var1, String var2) {
      String var3 = "";
      String var4 = "PK_FIELD_CLASS_MUST_HAVE_ATLEAST_ONE_CMP_FIELD";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String EJB_REF_MUST_HAVE_EJB_LINK_OR_REF_DESC(String var1, String var2) {
      String var3 = "";
      String var4 = "EJB_REF_MUST_HAVE_EJB_LINK_OR_REF_DESC";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String ILLEGAL_LOCAL_EJB_LINK_TO_MDB(String var1, String var2) {
      String var3 = "";
      String var4 = "ILLEGAL_LOCAL_EJB_LINK_TO_MDB";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String LOCAL_INTERFACE_TYPES_EXPOSE_THROUGH_REMOTE_INTERFACE(String var1, String var2) {
      String var3 = "";
      String var4 = "LOCAL_INTERFACE_TYPES_EXPOSE_THROUGH_REMOTE_INTERFACE";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String LOCAL_INTERFACE_TYPES_EXPOSE_THROUGH_HOME_INTERFACE(String var1, String var2) {
      String var3 = "";
      String var4 = "LOCAL_INTERFACE_TYPES_EXPOSE_THROUGH_HOME_INTERFACE";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String DUPLICATE_JNDI_NAME(String var1, String var2) {
      String var3 = "";
      String var4 = "DUPLICATE_JNDI_NAME";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String EJB_HOME_METHOD_RETURN_TYPE_SHOULD_MATCH(String var1, String var2, String var3, String var4, String var5, String var6, String var7) {
      String var8 = "";
      String var9 = "EJB_HOME_METHOD_RETURN_TYPE_SHOULD_MATCH";
      String var10 = "EJB Compliance";
      Object[] var11 = new Object[]{var1, var2, var3, var4, var5, var6, var7};
      String var12 = MessageFormat.format(this.l10n.get(var9), var11);
      if (this.getExtendedFormat()) {
         DateFormat var13 = DateFormat.getDateTimeInstance(2, 1);
         var8 = "<" + var13.format(new Date()) + "><" + var10 + "><" + var9 + "> ";
      }

      return var8 + var12;
   }

   public String METHOD_PERMISSION_ROLE_NAME_NOT_DECLARED(String var1) {
      String var2 = "";
      String var3 = "METHOD_PERMISSION_ROLE_NAME_NOT_DECLARED";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String NON_PK_CMP_FIELD_MAPPED_TO_MORE_THAN_ONE_TABLE(String var1, String var2, int var3, String var4) {
      String var5 = "";
      String var6 = "NON_PK_CMP_FIELD_MAPPED_TO_MORE_THAN_ONE_TABLE";
      String var7 = "EJB Compliance";
      Object[] var8 = new Object[]{var1, var2, new Integer(var3), var4};
      String var9 = MessageFormat.format(this.l10n.get(var6), var8);
      if (this.getExtendedFormat()) {
         DateFormat var10 = DateFormat.getDateTimeInstance(2, 1);
         var5 = "<" + var10.format(new Date()) + "><" + var7 + "><" + var6 + "> ";
      }

      return var5 + var9;
   }

   public String MISSING_MULTITABLE_PK_FIELD_MAP(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "MISSING_MULTITABLE_PK_FIELD_MAP";
      String var6 = "EJB Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String ROLE_ON_MULTITABLE_BEAN_MUST_SPECIFY_FK_TABLE_NAME(String var1, String var2) {
      String var3 = "";
      String var4 = "ROLE_ON_MULTITABLE_BEAN_MUST_SPECIFY_FK_TABLE_NAME";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String MISSING_FK_TABLE_NAME_FOR_MULTITABLE_BEAN(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "MISSING_FK_TABLE_NAME_FOR_MULTITABLE_BEAN";
      String var6 = "EJB Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String ROLE_HAS_NO_COLUMN_MAP(String var1, String var2) {
      String var3 = "";
      String var4 = "ROLE_HAS_NO_COLUMN_MAP";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String ROLE_11_SPECIFIES_INVALID_PK_TABLE_NAME(String var1, String var2, String var3, String var4, String var5) {
      String var6 = "";
      String var7 = "ROLE_11_SPECIFIES_INVALID_PK_TABLE_NAME";
      String var8 = "EJB Compliance";
      Object[] var9 = new Object[]{var1, var2, var3, var4, var5};
      String var10 = MessageFormat.format(this.l10n.get(var7), var9);
      if (this.getExtendedFormat()) {
         DateFormat var11 = DateFormat.getDateTimeInstance(2, 1);
         var6 = "<" + var11.format(new Date()) + "><" + var8 + "><" + var7 + "> ";
      }

      return var6 + var10;
   }

   public String ROLE_1N_SPECIFIES_INVALID_PK_TABLE_NAME(String var1, String var2, String var3, String var4) {
      String var5 = "";
      String var6 = "ROLE_1N_SPECIFIES_INVALID_PK_TABLE_NAME";
      String var7 = "EJB Compliance";
      Object[] var8 = new Object[]{var1, var2, var3, var4};
      String var9 = MessageFormat.format(this.l10n.get(var6), var8);
      if (this.getExtendedFormat()) {
         DateFormat var10 = DateFormat.getDateTimeInstance(2, 1);
         var5 = "<" + var10.format(new Date()) + "><" + var7 + "><" + var6 + "> ";
      }

      return var5 + var9;
   }

   public String ROLE_MN_SPECIFIES_INVALID_PK_TABLE_NAME(String var1, String var2, String var3, String var4) {
      String var5 = "";
      String var6 = "ROLE_MN_SPECIFIES_INVALID_PK_TABLE_NAME";
      String var7 = "EJB Compliance";
      Object[] var8 = new Object[]{var1, var2, var3, var4};
      String var9 = MessageFormat.format(this.l10n.get(var6), var8);
      if (this.getExtendedFormat()) {
         DateFormat var10 = DateFormat.getDateTimeInstance(2, 1);
         var5 = "<" + var10.format(new Date()) + "><" + var7 + "><" + var6 + "> ";
      }

      return var5 + var9;
   }

   public String ROLE_11_SPECIFIES_INVALID_FK_TABLE_NAME(String var1, String var2, String var3, String var4, String var5) {
      String var6 = "";
      String var7 = "ROLE_11_SPECIFIES_INVALID_FK_TABLE_NAME";
      String var8 = "EJB Compliance";
      Object[] var9 = new Object[]{var1, var2, var3, var4, var5};
      String var10 = MessageFormat.format(this.l10n.get(var7), var9);
      if (this.getExtendedFormat()) {
         DateFormat var11 = DateFormat.getDateTimeInstance(2, 1);
         var6 = "<" + var11.format(new Date()) + "><" + var8 + "><" + var7 + "> ";
      }

      return var6 + var10;
   }

   public String ROLE_1N_SPECIFIES_INVALID_FK_TABLE_NAME(String var1, String var2, String var3, String var4) {
      String var5 = "";
      String var6 = "ROLE_1N_SPECIFIES_INVALID_FK_TABLE_NAME";
      String var7 = "EJB Compliance";
      Object[] var8 = new Object[]{var1, var2, var3, var4};
      String var9 = MessageFormat.format(this.l10n.get(var6), var8);
      if (this.getExtendedFormat()) {
         DateFormat var10 = DateFormat.getDateTimeInstance(2, 1);
         var5 = "<" + var10.format(new Date()) + "><" + var7 + "><" + var6 + "> ";
      }

      return var5 + var9;
   }

   public String ROLE_MN_SPECIFIES_INVALID_FK_TABLE_NAME(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "ROLE_MN_SPECIFIES_INVALID_FK_TABLE_NAME";
      String var6 = "EJB Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String ROLE_11_SPECIFIES_FK_AND_PK_TABLE_NAMES_NOT_IN_DIFFERENT_ROLES(String var1, String var2, String var3, String var4) {
      String var5 = "";
      String var6 = "ROLE_11_SPECIFIES_FK_AND_PK_TABLE_NAMES_NOT_IN_DIFFERENT_ROLES";
      String var7 = "EJB Compliance";
      Object[] var8 = new Object[]{var1, var2, var3, var4};
      String var9 = MessageFormat.format(this.l10n.get(var6), var8);
      if (this.getExtendedFormat()) {
         DateFormat var10 = DateFormat.getDateTimeInstance(2, 1);
         var5 = "<" + var10.format(new Date()) + "><" + var7 + "><" + var6 + "> ";
      }

      return var5 + var9;
   }

   public String RELATED_BEANS_MUST_SHARE_DATASOURCE(String var1, String var2, String var3, String var4, String var5, String var6, String var7) {
      String var8 = "";
      String var9 = "RELATED_BEANS_MUST_SHARE_DATASOURCE";
      String var10 = "EJB Compliance";
      Object[] var11 = new Object[]{var1, var2, var3, var4, var5, var6, var7};
      String var12 = MessageFormat.format(this.l10n.get(var9), var11);
      if (this.getExtendedFormat()) {
         DateFormat var13 = DateFormat.getDateTimeInstance(2, 1);
         var8 = "<" + var13.format(new Date()) + "><" + var10 + "><" + var9 + "> ";
      }

      return var8 + var12;
   }

   public String getExpandJar(String var1, String var2) {
      String var3 = "";
      String var4 = "EXPAND_JAR";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String getExtractingDesc(String var1) {
      String var2 = "";
      String var3 = "EXTRACT_DESC";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getCompilingJar(String var1) {
      String var2 = "";
      String var3 = "COMPILING_JAR";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getCreatingOutputJar(String var1) {
      String var2 = "";
      String var3 = "CREATING_JAR";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getEJBCSuccess() {
      String var1 = "";
      String var2 = "EJBC_SUCCESS";
      String var3 = "EJB Compliance";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String getProcessDesc() {
      String var1 = "";
      String var2 = "PROCESS_DESC";
      String var3 = "EJB Compliance";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String getErrorProcessDesc() {
      String var1 = "";
      String var2 = "ERROR_PROCESS_DESC";
      String var3 = "EJB Compliance";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String getCheckCompliance() {
      String var1 = "";
      String var2 = "CHECK_COMPLIANCE";
      String var3 = "EJB Compliance";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String getBeansCompliant() {
      String var1 = "";
      String var2 = "BEANS_COMPLIANT";
      String var3 = "EJB Compliance";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String getErrorCheckingCompliance() {
      String var1 = "";
      String var2 = "ERROR_CHECKING_COMPLIANCE";
      String var3 = "EJB Compliance";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String getGeneratedBeanSources(String var1) {
      String var2 = "";
      String var3 = "GEN_BEAN_SRC";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getErrorGeneratingBeanSources(String var1) {
      String var2 = "";
      String var3 = "ERROR_GEN_BEAN_SRC";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getGeneratedPersSources(String var1) {
      String var2 = "";
      String var3 = "GEN_PERS_SRC";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getErrorGeneratingPersSources(String var1) {
      String var2 = "";
      String var3 = "ERROR_GEN_PERS_SRC";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getCompilingUnnecessary() {
      String var1 = "";
      String var2 = "COMPILING_UNECESSARY";
      String var3 = "EJB Compliance";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String getRMICUnnecessary() {
      String var1 = "";
      String var2 = "RMI_COMPILING_UNECESSARY";
      String var3 = "EJB Compliance";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String getCreatedClientJar(String var1) {
      String var2 = "";
      String var3 = "CREATED_CLIENT_JAR";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getErrorCreatingClientJar(String var1) {
      String var2 = "";
      String var3 = "ERROR_CREATING_CLIENT_JAR";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getCompilingEJBSources() {
      String var1 = "";
      String var2 = "COMPILING_EJB_SOURCES";
      String var3 = "EJB Compliance";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String getErrorCompilingEJBSources() {
      String var1 = "";
      String var2 = "ERROR_COMPILING_EJB_SOURCES";
      String var3 = "EJB Compliance";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String getFinishCompilingEJBSources() {
      String var1 = "";
      String var2 = "FINISH_COMPILING_EJB_SOURCES";
      String var3 = "EJB Compliance";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String getCreatedChecksum(String var1) {
      String var2 = "";
      String var3 = "CREATE_CHECKSUM";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getErrorCreatingChecksum(String var1) {
      String var2 = "";
      String var3 = "ERROR_CREATING_CHECKSUM";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getFindRMICClasses(String var1) {
      String var2 = "";
      String var3 = "FINDING_RMIC_CLASSES";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getNoRMICClasses(String var1) {
      String var2 = "";
      String var3 = "NO_RMIC_CLASSES";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getCompilingRMIC() {
      String var1 = "";
      String var2 = "COMPILING_RMIC";
      String var3 = "EJB Compliance";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String getErrorCompilingRMIC() {
      String var1 = "";
      String var2 = "ERROR_COMPILING_RMIC";
      String var3 = "EJB Compliance";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String ILLEGAL_DATABASETYPE_VALUE(String var1) {
      String var2 = "";
      String var3 = "ILLEGAL_DATABASETYPE_VALUE";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String ILLEGAL_DATABASETYPE_VALUE_VER71(String var1) {
      String var2 = "";
      String var3 = "ILLEGAL_DATABASETYPE_VALUE_VER71";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String ILLEGAL_AUTOKEY_GENERATOR_VALUE(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "ILLEGAL_AUTOKEY_GENERATOR_VALUE";
      String var6 = "EJB Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String EJBQLCANNOTBENULL(String var1, String var2) {
      String var3 = "";
      String var4 = "EJBQLCANNOTBENULL";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String ENV_VALUE_CANNOT_BE_NULL(String var1) {
      String var2 = "";
      String var3 = "ENV_VALUE_CANNOT_BE_NULL";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String EJBANDWLQLCANNOTBENULL(String var1, String var2) {
      String var3 = "";
      String var4 = "EJBANDWLQLCANNOTBENULL";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String WLQL_CANNOT_OVERRIDE_FINDBYPK_QL(String var1, String var2) {
      String var3 = "";
      String var4 = "WLQL_CANNOT_OVERRIDE_FINDBYPK_QL";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String FIELDS_IN_PK_CLASS_SHOULD_BE_CMP_FIELDS(String var1, String var2) {
      String var3 = "";
      String var4 = "FIELDS_IN_PK_CLASS_SHOULD_BE_CMP_FIELDS";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String noDuplicateEjbRefNamesAllowed(String var1, String var2) {
      String var3 = "";
      String var4 = "noDuplicateEjbRefNamesAllowed";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String noDuplicateServiceRefNamesAllowed(String var1, String var2) {
      String var3 = "";
      String var4 = "noDuplicateServiceRefNamesAllowed";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String noDuplicateServiceReferenceDescriptionNamesAllowed(String var1, String var2) {
      String var3 = "";
      String var4 = "noDuplicateServiceReferenceDescriptionNamesAllowed";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String noServiceRefForReferenceDescription(String var1, String var2) {
      String var3 = "";
      String var4 = "noServiceRefForReferenceDescription";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String noMethodFoundForEJBDeploymentDescriptorSetting(String var1, String var2, String var3, String var4, String var5) {
      String var6 = "";
      String var7 = "noMethodFoundForEJBDeploymentDescriptorSetting";
      String var8 = "EJB Compliance";
      Object[] var9 = new Object[]{var1, var2, var3, var4, var5};
      String var10 = MessageFormat.format(this.l10n.get(var7), var9);
      if (this.getExtendedFormat()) {
         DateFormat var11 = DateFormat.getDateTimeInstance(2, 1);
         var6 = "<" + var11.format(new Date()) + "><" + var8 + "><" + var7 + "> ";
      }

      return var6 + var10;
   }

   public String vendorPersistenceTypeNotInstalled(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "vendorPersistenceTypeNotInstalled";
      String var6 = "EJB Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String incompatibleCmpVersion(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "incompatibleCmpVersion";
      String var6 = "EJB Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String incompatibleVendorPersistenceType(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "incompatibleVendorPersistenceType";
      String var6 = "EJB Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String resourceRefNotMapped(String var1, String var2) {
      String var3 = "";
      String var4 = "resourceRefNotMapped";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String resourceEnvRefNotMapped(String var1, String var2) {
      String var3 = "";
      String var4 = "resourceEnvRefNotMapped";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String noEJBRefForReferenceDescription(String var1, String var2) {
      String var3 = "";
      String var4 = "noEJBRefForReferenceDescription";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String noEJBLocalRefForReferenceDescription(String var1, String var2) {
      String var3 = "";
      String var4 = "noEJBLocalRefForReferenceDescription";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String roleNotFound(String var1) {
      String var2 = "";
      String var3 = "roleNotFound";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String cmpFileNotFound(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "cmpFileNotFound";
      String var6 = "EJB Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String destinationNotFound(String var1) {
      String var2 = "";
      String var3 = "destinationNotFound";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String missingRelationshipRoleMap(String var1) {
      String var2 = "";
      String var3 = "missingRelationshipRoleMap";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String COMMIT_OPTION_FOR_delayDatabaseInsertUntil_IGNORED() {
      String var1 = "";
      String var2 = "COMMIT_OPTION_FOR_delayDatabaseInsertUntil_IGNORED";
      String var3 = "EJB Compliance";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String WRONG_VALUE_FOR_DBMS_TABLE() {
      String var1 = "";
      String var2 = "WRONG_VALUE_FOR_DBMS_TABLE";
      String var3 = "EJB Compliance";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String JNDI_NAME_MUST_HAVE_REMOTE_INTERFACE(String var1) {
      String var2 = "";
      String var3 = "JNDI_NAME_MUST_HAVE_REMOTE_INTERFACE";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String LOCAL_JNDI_NAME_MUST_HAVE_LOCAL_INTERFACE(String var1) {
      String var2 = "";
      String var3 = "LOCAL_JNDI_NAME_MUST_HAVE_LOCAL_INTERFACE";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String INCORRECT_JNDI_NAME(String var1, String var2) {
      String var3 = "";
      String var4 = "INCORRECT_JNDI_NAME";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String RESERVED_WORD_USED_FOR_COLUMN_OR_TABLE(String var1, String var2) {
      String var3 = "";
      String var4 = "RESERVED_WORD_USED_FOR_COLUMN_OR_TABLE";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String DuplicateWeblogicQueryElementsDetected(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "DUPLICATE_WEBLOGIC-QUERY_ELEMENTS_DETECTED";
      String var6 = "EJB Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String DuplicateRelationshipRoleNamesDetected(String var1, String var2) {
      String var3 = "";
      String var4 = "DUPLICATE_RELATIONSHIP_ROLE_NAMES_DETECTED";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String INVALID_INSTANCE_LOCK_ORDER(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "INVALID_INSTANCE_LOCK_ORDER";
      String var6 = "EJB Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String MESSAGE_DESTINATION_REF_NOT_LINKED(String var1, String var2) {
      String var3 = "";
      String var4 = "MESSAGE_DESTINATION_REF_NOT_LINKED";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String DUPLICATE_SQL_CACHING_NAME(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "DUPLICATE_SQL_CACHING_NAME";
      String var6 = "EJB Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String EJB_TIMEOUT_BAD_TX_ATTRIBUTE(String var1) {
      String var2 = "";
      String var3 = "EJB_TIMEOUT_BAD_TX_ATTRIBUTE";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String OptimisticWithReadTimeoutSecondsNoCacheBetweenTx(String var1) {
      String var2 = "";
      String var3 = "OptimisticWithReadTimeoutSecondsNoCacheBetweenTx";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String SQL_SHAPE_DOES_NOT_EXIST(String var1, String var2, String var3, String var4) {
      String var5 = "";
      String var6 = "SQL_SHAPE_DOES_NOT_EXIST";
      String var7 = "EJB Compliance";
      Object[] var8 = new Object[]{var1, var2, var3, var4};
      String var9 = MessageFormat.format(this.l10n.get(var6), var8);
      if (this.getExtendedFormat()) {
         DateFormat var10 = DateFormat.getDateTimeInstance(2, 1);
         var5 = "<" + var10.format(new Date()) + "><" + var7 + "><" + var6 + "> ";
      }

      return var5 + var9;
   }

   public String SQL_QUERY_NOT_SPECIFIED(String var1, String var2) {
      String var3 = "";
      String var4 = "SQL_QUERY_NOT_SPECIFIED";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String UNKNOWN_PK_NEVER_ASSIGNED(String var1) {
      String var2 = "";
      String var3 = "UNKNOWN_PK_NEVER_ASSIGNED";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String UNKNOWN_PK_WITHOUT_AUTO_KEY(String var1) {
      String var2 = "";
      String var3 = "UNKNOWN_PK_WITHOUT_AUTO_KEY";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String QUERY_CACHING_SUPPORTED_FOR_RO_BEANS_ONLY(String var1, String var2) {
      String var3 = "";
      String var4 = "QUERY_CACHING_SUPPORTED_FOR_RO_BEANS_ONLY";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String QUERY_CACHING_UNNECESSARY_FOR_FIND_BY_PK(String var1) {
      String var2 = "";
      String var3 = "QUERY_CACHING_UNNECESSARY_FOR_FIND_BY_PK";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String QUERY_CACHING_ENABLED_FOR_CMR_TO_RW_BEAN(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "QUERY_CACHING_ENABLED_FOR_CMR_TO_RW_BEAN";
      String var6 = "EJB Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String QUERY_CACHING_NOT_SUPPORTED_FOR_EJBSELECTS(String var1, String var2) {
      String var3 = "";
      String var4 = "QUERY_CACHING_NOT_SUPPORTED_FOR_EJBSELECTS";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String QUERY_CACHING_NOT_SUPPORTED_FOR_ENUMFINDERS(String var1, String var2) {
      String var3 = "";
      String var4 = "QUERY_CACHING_NOT_SUPPORTED_FOR_ENUMFINDERS";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String QUERY_CACHING_FINDER_HAS_RW_CACHING_ELEMENT_CMR_FIELD(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "QUERY_CACHING_FINDER_HAS_RW_CACHING_ELEMENT_CMR_FIELD";
      String var6 = "EJB Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String QUERY_CACHING_NOT_SUPORTED_FOR_PREPARED_QUERY_FINDER(String var1, String var2) {
      String var3 = "";
      String var4 = "QUERY_CACHING_NOT_SUPORTED_FOR_PREPARED_QUERY_FINDER";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String QUERY_CACHING_SQLFINDER_RETURNS_RW_BEAN(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "QUERY_CACHING_SQLFINDER_RETURNS_RW_BEAN";
      String var6 = "EJB Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String QUERY_CACHING_SQLFINDER_HAS_RW_RELATED_BEAN(String var1, String var2, String var3, String var4) {
      String var5 = "";
      String var6 = "QUERY_CACHING_SQLFINDER_HAS_RW_RELATED_BEAN";
      String var7 = "EJB Compliance";
      Object[] var8 = new Object[]{var1, var2, var3, var4};
      String var9 = MessageFormat.format(this.l10n.get(var6), var8);
      if (this.getExtendedFormat()) {
         DateFormat var10 = DateFormat.getDateTimeInstance(2, 1);
         var5 = "<" + var10.format(new Date()) + "><" + var7 + "><" + var6 + "> ";
      }

      return var5 + var9;
   }

   public String NO_JNDI_NAME_DEFINED_FOR_REMOTE_VIEW(String var1) {
      String var2 = "";
      String var3 = "NO_JNDI_NAME_DEFINED_FOR_REMOTE_VIEW";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String EJB_TIMEOUT_METHOD_NOT_FOUND(String var1, String var2) {
      String var3 = "";
      String var4 = "EJB_TIMEOUT_METHOD_NOT_FOUND";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String EXCEPTION_CANNOT_EXTEND_REMOTEEXCEPTION(String var1) {
      String var2 = "";
      String var3 = "EXCEPTION_CANNOT_EXTEND_REMOTEEXCEPTION";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String LOCAL_INTERFACE_CANNOT_REMOTE(String var1) {
      String var2 = "";
      String var3 = "LOCAL_INTERFACE_CANNOT_REMOTE";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String LOCAL_INTERFACE_NOT_FOUND_IN_BEAN(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "LOCAL_INTERFACE_NOT_FOUND_IN_BEAN";
      String var6 = "EJB Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String LOCAL_INTERFACE_CANNOT_EXTEND_EJBLocalObject(String var1) {
      String var2 = "";
      String var3 = "LOCAL_INTERFACE_CANNOT_EXTEND_EJBLocalObject";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String BUSINESS_INTERFACE_WITHOUT_METHOD(String var1) {
      String var2 = "";
      String var3 = "BUSINESS_INTERFACE_WITHOUT_METHOD";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String TWO_ARROUNDINVOKE_METHOD(String var1) {
      String var2 = "";
      String var3 = "TWO_ARROUNDINVOKE_METHOD";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String ARROUNDINVOKE_METHOD_CANNOT_BUSINESS_METHOD(String var1, String var2) {
      String var3 = "";
      String var4 = "ARROUNDINVOKE_METHOD_CANNOT_BUSINESS_METHOD";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String ARROUNDINVOKE_METHOD_CANNOT_BE_FINAL(String var1, String var2) {
      String var3 = "";
      String var4 = "ARROUNDINVOKE_METHOD_CANNOT_BE_FINAL";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String ARROUNDINVOKE_METHOD_CANNOT_BE_STATIC(String var1, String var2) {
      String var3 = "";
      String var4 = "ARROUNDINVOKE_METHOD_CANNOT_BE_STATIC";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String ARROUNDINVOKE_METHOD_IS_INVALID(String var1, String var2) {
      String var3 = "";
      String var4 = "ARROUNDINVOKE_METHOD_IS_INVALID";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String TEMOTE_INTERFACE_IS_LOCAL(String var1) {
      String var2 = "";
      String var3 = "TEMOTE_INTERFACE_IS_LOCAL";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String REMOTE_INTERFACE_IS_LOCAL(String var1) {
      String var2 = "";
      String var3 = "REMOTE_INTERFACE_IS_LOCAL";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String REMOTE_METHOD_NOT_FOUND_IN_BEAN(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "REMOTE_METHOD_NOT_FOUND_IN_BEAN";
      String var6 = "EJB Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String REMOTE_INTERFACE_EXTEND_EJBOBJECT(String var1) {
      String var2 = "";
      String var3 = "REMOTE_INTERFACE_EXTEND_EJBOBJECT";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String REMOTE_BUSINESS_INTERFACE_NO_METHOD(String var1) {
      String var2 = "";
      String var3 = "REMOTE_BUSINESS_INTERFACE_NO_METHOD";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String REMOTE_BUSINESS_INTERFACE_THROW_REMOTEEXCEPTION(String var1, String var2) {
      String var3 = "";
      String var4 = "REMOTE_BUSINESS_INTERFACE_THROW_REMOTEEXCEPTION";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String REMOTE_INTERFACE_NOT_THROW_REMOTEEXCEPTION(String var1, String var2) {
      String var3 = "";
      String var4 = "REMOTE_INTERFACE_NOT_THROW_REMOTEEXCEPTION";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String MDB_PREDESTROY_NOT_APPLY_EJBREMOVE(String var1) {
      String var2 = "";
      String var3 = "MDB_PREDESTROY_NOT_APPLY_EJBREMOVE";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String SESSION_BEAN_PREDESTROY_NOT_APPLY_EJBREMOVE(String var1) {
      String var2 = "";
      String var3 = "SESSION_BEAN_PREDESTROY_NOT_APPLY_EJBREMOVE";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String SESSION_BEAN_POSTACTIVATE_NOT_APPLY_EJBACTIVE(String var1) {
      String var2 = "";
      String var3 = "SESSION_BEAN_POSTACTIVATE_NOT_APPLY_EJBACTIVE";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String SESSION_BEAN_PREPASSIVATE_NOT_APPLY_EJBPASSIVATE(String var1) {
      String var2 = "";
      String var3 = "SESSION_BEAN_PREPASSIVATE_NOT_APPLY_EJBPASSIVATE";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String METHOD_CANNOT_START_WITH_EJB(String var1, String var2) {
      String var3 = "";
      String var4 = "METHOD_CANNOT_START_WITH_EJB";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String BUSINESS_METHOD_MUST_BE_PUBLIC(String var1, String var2) {
      String var3 = "";
      String var4 = "BUSINESS_METHOD_MUST_BE_PUBLIC";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String BUSINESS_METHOD_MUST_NOT_BE_FINAL(String var1, String var2) {
      String var3 = "";
      String var4 = "BUSINESS_METHOD_MUST_NOT_BE_FINAL";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String BUSINESS_METHOD_MUST_NOT_BE_STATIC(String var1, String var2) {
      String var3 = "";
      String var4 = "BUSINESS_METHOD_MUST_NOT_BE_STATIC";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String REMOVE_METHOD_NOT_BE_BUSINESS_METHOD(String var1, String var2) {
      String var3 = "";
      String var4 = "REMOVE_METHOD_NOT_BE_BUSINESS_METHOD";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String INTERCEPTOR_CLASS_DECLARED_IN_DD(String var1) {
      String var2 = "";
      String var3 = "INTERCEPTOR_CLASS_DECLARED_IN_DD";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String INTERCEPTOR_CLASS_WITHOUT_NOARG_CONSTRUCTOR(String var1) {
      String var2 = "";
      String var3 = "INTERCEPTOR_CLASS_WITHOUT_NOARG_CONSTRUCTOR";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String AROUNDINVOKE_METHOD_CANNOT_BE_FINAL(String var1) {
      String var2 = "";
      String var3 = "AROUNDINVOKE_METHOD_CANNOT_BE_FINAL";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String AROUNDINVOKE_METHOD_WITH_INVALIDE_SIGNATURE(String var1, String var2) {
      String var3 = "";
      String var4 = "AROUNDINVOKE_METHOD_WITH_INVALIDE_SIGNATURE";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String POSTCONSTRUCT_METHOD_WITH_INVALIDE_SIGNATURE(String var1, String var2) {
      String var3 = "";
      String var4 = "POSTCONSTRUCT_METHOD_WITH_INVALIDE_SIGNATURE";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String PREDESTROY_METHOD_WITH_INVALIDE_SIGNATURE(String var1, String var2) {
      String var3 = "";
      String var4 = "PREDESTROY_METHOD_WITH_INVALIDE_SIGNATURE";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String POSTACTIVE_METHOD_WITH_INVALIDE_SIGNATURE(String var1, String var2) {
      String var3 = "";
      String var4 = "POSTACTIVE_METHOD_WITH_INVALIDE_SIGNATURE";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String PREPASSIVATE_METHOD_WITH_INVALIDE_SIGNATURE(String var1, String var2) {
      String var3 = "";
      String var4 = "PREPASSIVATE_METHOD_WITH_INVALIDE_SIGNATURE";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String INTERCEPTOR_POSTCONSTRUCT_METHOD_WITH_INVALIDE_SIGNATURE(String var1, String var2) {
      String var3 = "";
      String var4 = "INTERCEPTOR_POSTCONSTRUCT_METHOD_WITH_INVALIDE_SIGNATURE";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String INTERCEPTOR_PREDESTROY_METHOD_WITH_INVALIDE_SIGNATURE(String var1, String var2) {
      String var3 = "";
      String var4 = "INTERCEPTOR_PREDESTROY_METHOD_WITH_INVALIDE_SIGNATURE";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String INTERCEPTOR_POSTACTIVE_METHOD_WITH_INVALIDE_SIGNATURE(String var1, String var2) {
      String var3 = "";
      String var4 = "INTERCEPTOR_POSTACTIVE_METHOD_WITH_INVALIDE_SIGNATURE";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String INTERCEPTOR_PREPASSIVATE_METHOD_WITH_INVALIDE_SIGNATURE(String var1, String var2) {
      String var3 = "";
      String var4 = "INTERCEPTOR_PREPASSIVATE_METHOD_WITH_INVALIDE_SIGNATURE";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String LIFECYCLE_INTERCEPTOR_METHOD_NOT_BE_FINAL(String var1, String var2) {
      String var3 = "";
      String var4 = "LIFECYCLE_INTERCEPTOR_METHOD_NOT_BE_FINAL";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String LIFECYCLE_INTERCEPTOR_METHOD_NOT_BE_STATIC(String var1, String var2) {
      String var3 = "";
      String var4 = "LIFECYCLE_INTERCEPTOR_METHOD_NOT_BE_STATIC";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String LIFECYCLE_INTERCEPTOR_METHOD_WITH_INVALID_SIGNATURE(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "LIFECYCLE_INTERCEPTOR_METHOD_WITH_INVALID_SIGNATURE";
      String var6 = "EJB Compliance";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String TWO_LIFECYCLE_INTERCEPTOR_METHOD_IN_BEAN(String var1, String var2) {
      String var3 = "";
      String var4 = "TWO_LIFECYCLE_INTERCEPTOR_METHOD_IN_BEAN";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String BUSINESS_INTERFACE_NOT_FOUND_IN_SESSION_BEAN(String var1) {
      String var2 = "";
      String var3 = "BUSINESS_INTERFACE_NOT_FOUND_IN_SESSION_BEAN";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String COMPONENT_INTERFACE_NOT_FOUND_IN_SESSION_BEAN(String var1) {
      String var2 = "";
      String var3 = "COMPONENT_INTERFACE_NOT_FOUND_IN_SESSION_BEAN";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String TIMEOUT_IN_DD_NOT_COMPATIBLE_WITH_ANNOTATION() {
      String var1 = "";
      String var2 = "TIMEOUT_IN_DD_NOT_COMPATIBLE_WITH_ANNOTATION";
      String var3 = "EJB Compliance";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String BEAN_CAN_HAVE_ONE_TIMEOUT_METHOD() {
      String var1 = "";
      String var2 = "BEAN_CAN_HAVE_ONE_TIMEOUT_METHOD";
      String var3 = "EJB Compliance";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String TIMEOUT_CAN_ONLY_SPECIFY_EJBTIMEOUT_METHOD(String var1) {
      String var2 = "";
      String var3 = "TIMEOUT_CAN_ONLY_SPECIFY_EJBTIMEOUT_METHOD";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String STATEFULE_BEAN_CANNOT_IMPLEMENTS_TIMEOUT() {
      String var1 = "";
      String var2 = "STATEFULE_BEAN_CANNOT_IMPLEMENTS_TIMEOUT";
      String var3 = "EJB Compliance";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String TIMEOUT_METHOD_CANNOT_BE_FINAL_OR_STATIC(String var1) {
      String var2 = "";
      String var3 = "TIMEOUT_METHOD_CANNOT_BE_FINAL_OR_STATIC";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String TIMEOUT_METHOD_WITH_INVALID_SIGNATURE(String var1) {
      String var2 = "";
      String var3 = "TIMEOUT_METHOD_WITH_INVALID_SIGNATURE";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String TIMEOUT_METHOD_CANNOT_THROW_APPLICATION_EXCEPTION(String var1) {
      String var2 = "";
      String var3 = "TIMEOUT_METHOD_CANNOT_THROW_APPLICATION_EXCEPTION";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String NO_EJBS_FOUND_IN_DD(String var1) {
      String var2 = "";
      String var3 = "NO_EJBS_FOUND_IN_DD";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String NO_SESSION_BEAN_CLASS_FOUND_FOR_EJB(String var1) {
      String var2 = "";
      String var3 = "NO_SESSION_BEAN_CLASS_FOUND_FOR_EJB";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String SESSION_BEAN_NO_SESSION_TYPE(String var1) {
      String var2 = "";
      String var3 = "SESSION_BEAN_NO_SESSION_TYPE";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String NO_MDB_CLASS_FOUND_FOR_EJB(String var1) {
      String var2 = "";
      String var3 = "NO_MDB_CLASS_FOUND_FOR_EJB";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String MDB_POSTCONSTRUCT_NOT_APPLY_EJBCREATE(String var1) {
      String var2 = "";
      String var3 = "MDB_POSTCONSTRUCT_NOT_APPLY_EJBCREATE";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String SLSB_POSTCONSTRUCT_NOT_APPLY_EJBCREATE(String var1) {
      String var2 = "";
      String var3 = "SLSB_POSTCONSTRUCT_NOT_APPLY_CREATE";
      String var4 = "EJB Compliance";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String EJB_ANNOTATION_VALUE_IS_DUPLICATE(String var1, String var2, String var3, String var4, String var5) {
      String var6 = "";
      String var7 = "EJB_ANNOTATION_VALUE_IS_DUPLICATE";
      String var8 = "EJB Compliance";
      Object[] var9 = new Object[]{var1, var2, var3, var4, var5};
      String var10 = MessageFormat.format(this.l10n.get(var7), var9);
      if (this.getExtendedFormat()) {
         DateFormat var11 = DateFormat.getDateTimeInstance(2, 1);
         var6 = "<" + var11.format(new Date()) + "><" + var8 + "><" + var7 + "> ";
      }

      return var6 + var10;
   }

   public String EJB_MAY_BE_MISSING_BRIDGE_METHOD(String var1, String var2) {
      String var3 = "";
      String var4 = "EJB_MAY_BE_MISSING_BRIDGE_METHOD";
      String var5 = "EJB Compliance";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }
}
