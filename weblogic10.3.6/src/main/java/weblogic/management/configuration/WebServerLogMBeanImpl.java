package weblogic.management.configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorValidateException;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.management.DistributedManagementException;
import weblogic.utils.collections.CombinedIterator;

public class WebServerLogMBeanImpl extends LogFileMBeanImpl implements WebServerLogMBean, Serializable {
   private String _ELFFields;
   private String _FileName;
   private String _LogFileFormat;
   private boolean _LogMilliSeconds;
   private boolean _LogTimeInGMT;
   private boolean _LoggingEnabled;
   private static SchemaHelper2 _schemaHelper;

   public WebServerLogMBeanImpl() {
      this._initializeProperty(-1);
   }

   public WebServerLogMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public String getFileName() {
      if (!this._isSet(8)) {
         try {
            return this.getParent() instanceof VirtualHostMBean ? "logs/virtualHosts/" + this.getName() + "/" + "access.log" : "logs/access.log";
         } catch (NullPointerException var2) {
         }
      }

      return this._FileName;
   }

   public boolean isFileNameSet() {
      return this._isSet(8);
   }

   public void setLoggingEnabled(boolean var1) {
      boolean var2 = this._LoggingEnabled;
      this._LoggingEnabled = var1;
      this._postSet(20, var2, var1);
   }

   public boolean isLoggingEnabled() {
      return this._LoggingEnabled;
   }

   public boolean isLoggingEnabledSet() {
      return this._isSet(20);
   }

   public void setFileName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._FileName;
      this._FileName = var1;
      this._postSet(8, var2, var1);
   }

   public String getELFFields() {
      return this._ELFFields;
   }

   public boolean isELFFieldsSet() {
      return this._isSet(21);
   }

   public void setELFFields(String var1) throws InvalidAttributeValueException, DistributedManagementException {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonEmptyString("ELFFields", var1);
      String var2 = this._ELFFields;
      this._ELFFields = var1;
      this._postSet(21, var2, var1);
   }

   public String getLogFileFormat() {
      return this._LogFileFormat;
   }

   public boolean isLogFileFormatSet() {
      return this._isSet(22);
   }

   public void setLogFileFormat(String var1) throws InvalidAttributeValueException, DistributedManagementException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"common", "extended"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("LogFileFormat", var1, var2);
      String var3 = this._LogFileFormat;
      this._LogFileFormat = var1;
      this._postSet(22, var3, var1);
   }

   public boolean isLogTimeInGMT() {
      return this._LogTimeInGMT;
   }

   public boolean isLogTimeInGMTSet() {
      return this._isSet(23);
   }

   public void setLogTimeInGMT(boolean var1) {
      boolean var2 = this._LogTimeInGMT;
      this._LogTimeInGMT = var1;
      this._postSet(23, var2, var1);
   }

   public boolean isLogMilliSeconds() {
      return this._LogMilliSeconds;
   }

   public boolean isLogMilliSecondsSet() {
      return this._isSet(24);
   }

   public void setLogMilliSeconds(boolean var1) {
      boolean var2 = this._LogMilliSeconds;
      this._LogMilliSeconds = var1;
      this._postSet(24, var2, var1);
   }

   public Object _getKey() {
      return super._getKey();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
   }

   protected void _unSet(int var1) {
      if (!this._initializeProperty(var1)) {
         super._unSet(var1);
      } else {
         this._markSet(var1, false);
      }

   }

   protected AbstractDescriptorBeanHelper _createHelper() {
      return new Helper(this);
   }

   public boolean _isAnythingSet() {
      return super._isAnythingSet();
   }

   private boolean _initializeProperty(int var1) {
      boolean var2 = var1 > -1;
      if (!var2) {
         var1 = 21;
      }

      try {
         switch (var1) {
            case 21:
               this._ELFFields = "date time cs-method cs-uri sc-status";
               if (var2) {
                  break;
               }
            case 8:
               this._FileName = null;
               if (var2) {
                  break;
               }
            case 22:
               this._LogFileFormat = "common";
               if (var2) {
                  break;
               }
            case 24:
               this._LogMilliSeconds = false;
               if (var2) {
                  break;
               }
            case 23:
               this._LogTimeInGMT = false;
               if (var2) {
                  break;
               }
            case 20:
               this._LoggingEnabled = true;
               if (var2) {
                  break;
               }
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            default:
               if (var2) {
                  return false;
               }
         }

         return true;
      } catch (RuntimeException var4) {
         throw var4;
      } catch (Exception var5) {
         throw (Error)(new AssertionError("Impossible Exception")).initCause(var5);
      }
   }

   public Munger.SchemaHelper _getSchemaHelper() {
      return null;
   }

   public String _getElementName(int var1) {
      return this._getSchemaHelper2().getElementName(var1);
   }

   protected String getSchemaLocation() {
      return "http://xmlns.oracle.com/weblogic/1.0/domain.xsd";
   }

   protected String getTargetNamespace() {
      return "http://xmlns.oracle.com/weblogic/domain";
   }

   public SchemaHelper _getSchemaHelper2() {
      if (_schemaHelper == null) {
         _schemaHelper = new SchemaHelper2();
      }

      return _schemaHelper;
   }

   public String getType() {
      return "WebServerLog";
   }

   public void putValue(String var1, Object var2) {
      String var4;
      if (var1.equals("ELFFields")) {
         var4 = this._ELFFields;
         this._ELFFields = (String)var2;
         this._postSet(21, var4, this._ELFFields);
      } else if (var1.equals("FileName")) {
         var4 = this._FileName;
         this._FileName = (String)var2;
         this._postSet(8, var4, this._FileName);
      } else if (var1.equals("LogFileFormat")) {
         var4 = this._LogFileFormat;
         this._LogFileFormat = (String)var2;
         this._postSet(22, var4, this._LogFileFormat);
      } else {
         boolean var3;
         if (var1.equals("LogMilliSeconds")) {
            var3 = this._LogMilliSeconds;
            this._LogMilliSeconds = (Boolean)var2;
            this._postSet(24, var3, this._LogMilliSeconds);
         } else if (var1.equals("LogTimeInGMT")) {
            var3 = this._LogTimeInGMT;
            this._LogTimeInGMT = (Boolean)var2;
            this._postSet(23, var3, this._LogTimeInGMT);
         } else if (var1.equals("LoggingEnabled")) {
            var3 = this._LoggingEnabled;
            this._LoggingEnabled = (Boolean)var2;
            this._postSet(20, var3, this._LoggingEnabled);
         } else {
            super.putValue(var1, var2);
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("ELFFields")) {
         return this._ELFFields;
      } else if (var1.equals("FileName")) {
         return this._FileName;
      } else if (var1.equals("LogFileFormat")) {
         return this._LogFileFormat;
      } else if (var1.equals("LogMilliSeconds")) {
         return new Boolean(this._LogMilliSeconds);
      } else if (var1.equals("LogTimeInGMT")) {
         return new Boolean(this._LogTimeInGMT);
      } else {
         return var1.equals("LoggingEnabled") ? new Boolean(this._LoggingEnabled) : super.getValue(var1);
      }
   }

   public static void validateGeneration() {
      try {
         weblogic.descriptor.beangen.LegalChecks.checkNonEmptyString("ELFFields", "date time cs-method cs-uri sc-status");
      } catch (IllegalArgumentException var1) {
         throw new DescriptorValidateException("The default value for the property  is zero-length. Properties annotated with false value on @legalZeroLength or @legalNull  should either have @required/@derivedDefault annotations or have a non-zero-length value on @default annotation. Refer annotation legalZeroLength on property ELFFields in WebServerLogMBean" + var1.getMessage());
      }
   }

   public static class SchemaHelper2 extends LogFileMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 9:
               if (var1.equals("file-name")) {
                  return 8;
               }
               break;
            case 10:
               if (var1.equals("elf-fields")) {
                  return 21;
               }
            case 11:
            case 12:
            case 13:
            case 16:
            default:
               break;
            case 14:
               if (var1.equals("log-time-ingmt")) {
                  return 23;
               }
               break;
            case 15:
               if (var1.equals("log-file-format")) {
                  return 22;
               }

               if (var1.equals("logging-enabled")) {
                  return 20;
               }
               break;
            case 17:
               if (var1.equals("log-milli-seconds")) {
                  return 24;
               }
         }

         return super.getPropertyIndex(var1);
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            default:
               return super.getSchemaHelper(var1);
         }
      }

      public String getElementName(int var1) {
         switch (var1) {
            case 8:
               return "file-name";
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            default:
               return super.getElementName(var1);
            case 20:
               return "logging-enabled";
            case 21:
               return "elf-fields";
            case 22:
               return "log-file-format";
            case 23:
               return "log-time-ingmt";
            case 24:
               return "log-milli-seconds";
         }
      }

      public boolean isConfigurable(int var1) {
         switch (var1) {
            case 8:
               return true;
            case 9:
            case 10:
            case 11:
            case 12:
            case 14:
            case 16:
            case 17:
            case 18:
            case 19:
            default:
               return super.isConfigurable(var1);
            case 13:
               return true;
            case 15:
               return true;
            case 20:
               return true;
            case 21:
               return true;
            case 22:
               return true;
            case 23:
               return true;
            case 24:
               return true;
         }
      }

      public boolean isKey(int var1) {
         switch (var1) {
            case 2:
               return true;
            default:
               return super.isKey(var1);
         }
      }

      public String[] getKeyElementNames() {
         ArrayList var1 = new ArrayList();
         var1.add("name");
         return (String[])((String[])var1.toArray(new String[0]));
      }
   }

   protected static class Helper extends LogFileMBeanImpl.Helper {
      private WebServerLogMBeanImpl bean;

      protected Helper(WebServerLogMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 8:
               return "FileName";
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            default:
               return super.getPropertyName(var1);
            case 20:
               return "LoggingEnabled";
            case 21:
               return "ELFFields";
            case 22:
               return "LogFileFormat";
            case 23:
               return "LogTimeInGMT";
            case 24:
               return "LogMilliSeconds";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("ELFFields")) {
            return 21;
         } else if (var1.equals("FileName")) {
            return 8;
         } else if (var1.equals("LogFileFormat")) {
            return 22;
         } else if (var1.equals("LogMilliSeconds")) {
            return 24;
         } else if (var1.equals("LogTimeInGMT")) {
            return 23;
         } else {
            return var1.equals("LoggingEnabled") ? 20 : super.getPropertyIndex(var1);
         }
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         return new CombinedIterator(var1);
      }

      protected long computeHashValue(CRC32 var1) {
         try {
            StringBuffer var2 = new StringBuffer();
            long var3 = super.computeHashValue(var1);
            if (var3 != 0L) {
               var2.append(String.valueOf(var3));
            }

            long var5 = 0L;
            if (this.bean.isELFFieldsSet()) {
               var2.append("ELFFields");
               var2.append(String.valueOf(this.bean.getELFFields()));
            }

            if (this.bean.isFileNameSet()) {
               var2.append("FileName");
               var2.append(String.valueOf(this.bean.getFileName()));
            }

            if (this.bean.isLogFileFormatSet()) {
               var2.append("LogFileFormat");
               var2.append(String.valueOf(this.bean.getLogFileFormat()));
            }

            if (this.bean.isLogMilliSecondsSet()) {
               var2.append("LogMilliSeconds");
               var2.append(String.valueOf(this.bean.isLogMilliSeconds()));
            }

            if (this.bean.isLogTimeInGMTSet()) {
               var2.append("LogTimeInGMT");
               var2.append(String.valueOf(this.bean.isLogTimeInGMT()));
            }

            if (this.bean.isLoggingEnabledSet()) {
               var2.append("LoggingEnabled");
               var2.append(String.valueOf(this.bean.isLoggingEnabled()));
            }

            var1.update(var2.toString().getBytes());
            return var1.getValue();
         } catch (Exception var7) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var7);
         }
      }

      protected void computeDiff(AbstractDescriptorBean var1) {
         try {
            super.computeDiff(var1);
            WebServerLogMBeanImpl var2 = (WebServerLogMBeanImpl)var1;
            this.computeDiff("ELFFields", this.bean.getELFFields(), var2.getELFFields(), true);
            this.computeDiff("FileName", this.bean.getFileName(), var2.getFileName(), false);
            this.computeDiff("LogFileFormat", this.bean.getLogFileFormat(), var2.getLogFileFormat(), false);
            this.computeDiff("LogMilliSeconds", this.bean.isLogMilliSeconds(), var2.isLogMilliSeconds(), false);
            this.computeDiff("LogTimeInGMT", this.bean.isLogTimeInGMT(), var2.isLogTimeInGMT(), false);
            this.computeDiff("LoggingEnabled", this.bean.isLoggingEnabled(), var2.isLoggingEnabled(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            WebServerLogMBeanImpl var3 = (WebServerLogMBeanImpl)var1.getSourceBean();
            WebServerLogMBeanImpl var4 = (WebServerLogMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("ELFFields")) {
                  var3.setELFFields(var4.getELFFields());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 21);
               } else if (var5.equals("FileName")) {
                  var3.setFileName(var4.getFileName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("LogFileFormat")) {
                  var3.setLogFileFormat(var4.getLogFileFormat());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 22);
               } else if (var5.equals("LogMilliSeconds")) {
                  var3.setLogMilliSeconds(var4.isLogMilliSeconds());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 24);
               } else if (var5.equals("LogTimeInGMT")) {
                  var3.setLogTimeInGMT(var4.isLogTimeInGMT());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 23);
               } else if (var5.equals("LoggingEnabled")) {
                  var3.setLoggingEnabled(var4.isLoggingEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 20);
               } else {
                  super.applyPropertyUpdate(var1, var2);
               }

            }
         } catch (RuntimeException var7) {
            throw var7;
         } catch (Exception var8) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var8);
         }
      }

      protected AbstractDescriptorBean finishCopy(AbstractDescriptorBean var1, boolean var2, List var3) {
         try {
            WebServerLogMBeanImpl var5 = (WebServerLogMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("ELFFields")) && this.bean.isELFFieldsSet()) {
               var5.setELFFields(this.bean.getELFFields());
            }

            if ((var3 == null || !var3.contains("FileName")) && this.bean.isFileNameSet()) {
               var5.setFileName(this.bean.getFileName());
            }

            if ((var3 == null || !var3.contains("LogFileFormat")) && this.bean.isLogFileFormatSet()) {
               var5.setLogFileFormat(this.bean.getLogFileFormat());
            }

            if ((var3 == null || !var3.contains("LogMilliSeconds")) && this.bean.isLogMilliSecondsSet()) {
               var5.setLogMilliSeconds(this.bean.isLogMilliSeconds());
            }

            if ((var3 == null || !var3.contains("LogTimeInGMT")) && this.bean.isLogTimeInGMTSet()) {
               var5.setLogTimeInGMT(this.bean.isLogTimeInGMT());
            }

            if ((var3 == null || !var3.contains("LoggingEnabled")) && this.bean.isLoggingEnabledSet()) {
               var5.setLoggingEnabled(this.bean.isLoggingEnabled());
            }

            return var5;
         } catch (RuntimeException var6) {
            throw var6;
         } catch (Exception var7) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var7);
         }
      }

      protected void inferSubTree(Class var1, Object var2) {
         super.inferSubTree(var1, var2);
         Object var3 = null;
      }
   }
}
