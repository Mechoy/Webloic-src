package weblogic.management.configuration;

import com.bea.logging.LoggingConfigValidator;
import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.zip.CRC32;
import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.beangen.StringHelper;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.management.ManagementException;
import weblogic.management.mbeans.custom.Log;
import weblogic.utils.collections.CombinedIterator;

public class CommonLogMBeanImpl extends LogFileMBeanImpl implements CommonLogMBean, Serializable {
   private String _LogFileSeverity;
   private String _LoggerSeverity;
   private Properties _LoggerSeverityProperties;
   private String _Name;
   private int _StacktraceDepth;
   private String _StdoutFormat;
   private boolean _StdoutLogStack;
   private String _StdoutSeverity;
   private Log _customizer;
   private static SchemaHelper2 _schemaHelper;

   public CommonLogMBeanImpl() {
      try {
         this._customizer = new Log(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public CommonLogMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new Log(this);
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         }

         throw new UndeclaredThrowableException(var4);
      }

      this._initializeProperty(-1);
   }

   public String getLoggerSeverity() {
      return this._LoggerSeverity;
   }

   public String getName() {
      if (!this._isSet(2)) {
         try {
            return ((ConfigurationMBean)this.getParent()).getName();
         } catch (NullPointerException var2) {
         }
      }

      return this._customizer.getName();
   }

   public boolean isLoggerSeveritySet() {
      return this._isSet(20);
   }

   public boolean isNameSet() {
      return this._isSet(2);
   }

   public void setLoggerSeverity(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"Trace", "Debug", "Info", "Notice", "Warning"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("LoggerSeverity", var1, var2);
      String var3 = this._LoggerSeverity;
      this._LoggerSeverity = var1;
      this._postSet(20, var3, var1);
   }

   public void setName(String var1) throws InvalidAttributeValueException, ManagementException {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonEmptyString("Name", var1);
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("Name", var1);
      ConfigurationValidator.validateName(var1);
      String var2 = this.getName();
      this._customizer.setName(var1);
      this._postSet(2, var2, var1);
   }

   public Properties getLoggerSeverityProperties() {
      return this._LoggerSeverityProperties;
   }

   public String getLoggerSeverityPropertiesAsString() {
      return StringHelper.objectToString(this.getLoggerSeverityProperties());
   }

   public boolean isLoggerSeverityPropertiesSet() {
      return this._isSet(21);
   }

   public void setLoggerSeverityPropertiesAsString(String var1) {
      try {
         this.setLoggerSeverityProperties(StringHelper.stringToProperties(var1));
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public void setLoggerSeverityProperties(Properties var1) {
      LoggingConfigValidator.validateLoggerSeverityProperties(var1);
      Properties var2 = this._LoggerSeverityProperties;
      this._LoggerSeverityProperties = var1;
      this._postSet(21, var2, var1);
   }

   public String getLogFileSeverity() {
      return this._LogFileSeverity;
   }

   public boolean isLogFileSeveritySet() {
      return this._isSet(22);
   }

   public void setLogFileSeverity(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"Trace", "Debug", "Info", "Notice", "Warning"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("LogFileSeverity", var1, var2);
      String var3 = this._LogFileSeverity;
      this._LogFileSeverity = var1;
      this._postSet(22, var3, var1);
   }

   public String getStdoutSeverity() {
      return this._StdoutSeverity;
   }

   public boolean isStdoutSeveritySet() {
      return this._isSet(23);
   }

   public void setStdoutSeverity(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"Trace", "Debug", "Info", "Warning", "Error", "Notice", "Critical", "Alert", "Emergency", "Off"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("StdoutSeverity", var1, var2);
      String var3 = this._StdoutSeverity;
      this._StdoutSeverity = var1;
      this._postSet(23, var3, var1);
   }

   public String getStdoutFormat() {
      return this._StdoutFormat;
   }

   public boolean isStdoutFormatSet() {
      return this._isSet(24);
   }

   public void setStdoutFormat(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"standard", "noid"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("StdoutFormat", var1, var2);
      String var3 = this._StdoutFormat;
      this._StdoutFormat = var1;
      this._postSet(24, var3, var1);
   }

   public boolean isStdoutLogStack() {
      return this._StdoutLogStack;
   }

   public boolean isStdoutLogStackSet() {
      return this._isSet(25);
   }

   public void touch() throws ConfigurationException {
      this._customizer.touch();
   }

   public void freezeCurrentValue(String var1) throws AttributeNotFoundException, MBeanException {
      this._customizer.freezeCurrentValue(var1);
   }

   public void setStdoutLogStack(boolean var1) {
      boolean var2 = this._StdoutLogStack;
      this._StdoutLogStack = var1;
      this._postSet(25, var2, var1);
   }

   public int getStacktraceDepth() {
      return this._StacktraceDepth;
   }

   public boolean isStacktraceDepthSet() {
      return this._isSet(26);
   }

   public void restoreDefaultValue(String var1) throws AttributeNotFoundException {
      this._customizer.restoreDefaultValue(var1);
   }

   public void setStacktraceDepth(int var1) {
      int var2 = this._StacktraceDepth;
      this._StacktraceDepth = var1;
      this._postSet(26, var2, var1);
   }

   public String computeLogFilePath() {
      return this._customizer.computeLogFilePath();
   }

   public Object _getKey() {
      return this.getName();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
   }

   public boolean _hasKey() {
      return true;
   }

   public boolean _isPropertyAKey(Munger.ReaderEventInfo var1) {
      String var2 = var1.getElementName();
      switch (var2.length()) {
         case 4:
            if (var2.equals("name")) {
               return var1.compareXpaths(this._getPropertyXpath("name"));
            }

            return super._isPropertyAKey(var1);
         default:
            return super._isPropertyAKey(var1);
      }
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
         var1 = 22;
      }

      try {
         switch (var1) {
            case 22:
               this._LogFileSeverity = "Trace";
               if (var2) {
                  break;
               }
            case 20:
               this._LoggerSeverity = "Info";
               if (var2) {
                  break;
               }
            case 21:
               this._LoggerSeverityProperties = null;
               if (var2) {
                  break;
               }
            case 2:
               this._customizer.setName((String)null);
               if (var2) {
                  break;
               }
            case 26:
               this._StacktraceDepth = 5;
               if (var2) {
                  break;
               }
            case 24:
               this._StdoutFormat = "standard";
               if (var2) {
                  break;
               }
            case 23:
               this._StdoutSeverity = "Notice";
               if (var2) {
                  break;
               }
            case 25:
               this._StdoutLogStack = true;
               if (var2) {
                  break;
               }
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
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
      return "CommonLog";
   }

   public void putValue(String var1, Object var2) {
      String var4;
      if (var1.equals("LogFileSeverity")) {
         var4 = this._LogFileSeverity;
         this._LogFileSeverity = (String)var2;
         this._postSet(22, var4, this._LogFileSeverity);
      } else if (var1.equals("LoggerSeverity")) {
         var4 = this._LoggerSeverity;
         this._LoggerSeverity = (String)var2;
         this._postSet(20, var4, this._LoggerSeverity);
      } else if (var1.equals("LoggerSeverityProperties")) {
         Properties var7 = this._LoggerSeverityProperties;
         this._LoggerSeverityProperties = (Properties)var2;
         this._postSet(21, var7, this._LoggerSeverityProperties);
      } else if (var1.equals("Name")) {
         var4 = this._Name;
         this._Name = (String)var2;
         this._postSet(2, var4, this._Name);
      } else if (var1.equals("StacktraceDepth")) {
         int var6 = this._StacktraceDepth;
         this._StacktraceDepth = (Integer)var2;
         this._postSet(26, var6, this._StacktraceDepth);
      } else if (var1.equals("StdoutFormat")) {
         var4 = this._StdoutFormat;
         this._StdoutFormat = (String)var2;
         this._postSet(24, var4, this._StdoutFormat);
      } else if (var1.equals("StdoutLogStack")) {
         boolean var5 = this._StdoutLogStack;
         this._StdoutLogStack = (Boolean)var2;
         this._postSet(25, var5, this._StdoutLogStack);
      } else if (var1.equals("StdoutSeverity")) {
         var4 = this._StdoutSeverity;
         this._StdoutSeverity = (String)var2;
         this._postSet(23, var4, this._StdoutSeverity);
      } else if (var1.equals("customizer")) {
         Log var3 = this._customizer;
         this._customizer = (Log)var2;
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("LogFileSeverity")) {
         return this._LogFileSeverity;
      } else if (var1.equals("LoggerSeverity")) {
         return this._LoggerSeverity;
      } else if (var1.equals("LoggerSeverityProperties")) {
         return this._LoggerSeverityProperties;
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("StacktraceDepth")) {
         return new Integer(this._StacktraceDepth);
      } else if (var1.equals("StdoutFormat")) {
         return this._StdoutFormat;
      } else if (var1.equals("StdoutLogStack")) {
         return new Boolean(this._StdoutLogStack);
      } else if (var1.equals("StdoutSeverity")) {
         return this._StdoutSeverity;
      } else {
         return var1.equals("customizer") ? this._customizer : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends LogFileMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 4:
               if (var1.equals("name")) {
                  return 2;
               }
               break;
            case 13:
               if (var1.equals("stdout-format")) {
                  return 24;
               }
               break;
            case 15:
               if (var1.equals("logger-severity")) {
                  return 20;
               }

               if (var1.equals("stdout-severity")) {
                  return 23;
               }
               break;
            case 16:
               if (var1.equals("stacktrace-depth")) {
                  return 26;
               }

               if (var1.equals("stdout-log-stack")) {
                  return 25;
               }
               break;
            case 17:
               if (var1.equals("log-file-severity")) {
                  return 22;
               }
               break;
            case 26:
               if (var1.equals("logger-severity-properties")) {
                  return 21;
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
            case 2:
               return "name";
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
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
               return "logger-severity";
            case 21:
               return "logger-severity-properties";
            case 22:
               return "log-file-severity";
            case 23:
               return "stdout-severity";
            case 24:
               return "stdout-format";
            case 25:
               return "stdout-log-stack";
            case 26:
               return "stacktrace-depth";
         }
      }

      public boolean isConfigurable(int var1) {
         switch (var1) {
            case 8:
               return true;
            case 13:
               return true;
            case 15:
               return true;
            default:
               return super.isConfigurable(var1);
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

      public boolean hasKey() {
         return true;
      }

      public String[] getKeyElementNames() {
         ArrayList var1 = new ArrayList();
         var1.add("name");
         return (String[])((String[])var1.toArray(new String[0]));
      }
   }

   protected static class Helper extends LogFileMBeanImpl.Helper {
      private CommonLogMBeanImpl bean;

      protected Helper(CommonLogMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 2:
               return "Name";
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
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
               return "LoggerSeverity";
            case 21:
               return "LoggerSeverityProperties";
            case 22:
               return "LogFileSeverity";
            case 23:
               return "StdoutSeverity";
            case 24:
               return "StdoutFormat";
            case 25:
               return "StdoutLogStack";
            case 26:
               return "StacktraceDepth";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("LogFileSeverity")) {
            return 22;
         } else if (var1.equals("LoggerSeverity")) {
            return 20;
         } else if (var1.equals("LoggerSeverityProperties")) {
            return 21;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("StacktraceDepth")) {
            return 26;
         } else if (var1.equals("StdoutFormat")) {
            return 24;
         } else if (var1.equals("StdoutSeverity")) {
            return 23;
         } else {
            return var1.equals("StdoutLogStack") ? 25 : super.getPropertyIndex(var1);
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
            if (this.bean.isLogFileSeveritySet()) {
               var2.append("LogFileSeverity");
               var2.append(String.valueOf(this.bean.getLogFileSeverity()));
            }

            if (this.bean.isLoggerSeveritySet()) {
               var2.append("LoggerSeverity");
               var2.append(String.valueOf(this.bean.getLoggerSeverity()));
            }

            if (this.bean.isLoggerSeverityPropertiesSet()) {
               var2.append("LoggerSeverityProperties");
               var2.append(String.valueOf(this.bean.getLoggerSeverityProperties()));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isStacktraceDepthSet()) {
               var2.append("StacktraceDepth");
               var2.append(String.valueOf(this.bean.getStacktraceDepth()));
            }

            if (this.bean.isStdoutFormatSet()) {
               var2.append("StdoutFormat");
               var2.append(String.valueOf(this.bean.getStdoutFormat()));
            }

            if (this.bean.isStdoutSeveritySet()) {
               var2.append("StdoutSeverity");
               var2.append(String.valueOf(this.bean.getStdoutSeverity()));
            }

            if (this.bean.isStdoutLogStackSet()) {
               var2.append("StdoutLogStack");
               var2.append(String.valueOf(this.bean.isStdoutLogStack()));
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
            CommonLogMBeanImpl var2 = (CommonLogMBeanImpl)var1;
            this.computeDiff("LogFileSeverity", this.bean.getLogFileSeverity(), var2.getLogFileSeverity(), true);
            this.computeDiff("LoggerSeverity", this.bean.getLoggerSeverity(), var2.getLoggerSeverity(), true);
            this.computeDiff("LoggerSeverityProperties", this.bean.getLoggerSeverityProperties(), var2.getLoggerSeverityProperties(), true);
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeDiff("StacktraceDepth", this.bean.getStacktraceDepth(), var2.getStacktraceDepth(), true);
            this.computeDiff("StdoutFormat", this.bean.getStdoutFormat(), var2.getStdoutFormat(), false);
            this.computeDiff("StdoutSeverity", this.bean.getStdoutSeverity(), var2.getStdoutSeverity(), true);
            this.computeDiff("StdoutLogStack", this.bean.isStdoutLogStack(), var2.isStdoutLogStack(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            CommonLogMBeanImpl var3 = (CommonLogMBeanImpl)var1.getSourceBean();
            CommonLogMBeanImpl var4 = (CommonLogMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("LogFileSeverity")) {
                  var3.setLogFileSeverity(var4.getLogFileSeverity());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 22);
               } else if (var5.equals("LoggerSeverity")) {
                  var3.setLoggerSeverity(var4.getLoggerSeverity());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 20);
               } else if (var5.equals("LoggerSeverityProperties")) {
                  var3.setLoggerSeverityProperties(var4.getLoggerSeverityProperties() == null ? null : (Properties)var4.getLoggerSeverityProperties().clone());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 21);
               } else if (var5.equals("Name")) {
                  var3.setName(var4.getName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 2);
               } else if (var5.equals("StacktraceDepth")) {
                  var3.setStacktraceDepth(var4.getStacktraceDepth());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 26);
               } else if (var5.equals("StdoutFormat")) {
                  var3.setStdoutFormat(var4.getStdoutFormat());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 24);
               } else if (var5.equals("StdoutSeverity")) {
                  var3.setStdoutSeverity(var4.getStdoutSeverity());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 23);
               } else if (var5.equals("StdoutLogStack")) {
                  var3.setStdoutLogStack(var4.isStdoutLogStack());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 25);
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
            CommonLogMBeanImpl var5 = (CommonLogMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("LogFileSeverity")) && this.bean.isLogFileSeveritySet()) {
               var5.setLogFileSeverity(this.bean.getLogFileSeverity());
            }

            if ((var3 == null || !var3.contains("LoggerSeverity")) && this.bean.isLoggerSeveritySet()) {
               var5.setLoggerSeverity(this.bean.getLoggerSeverity());
            }

            if ((var3 == null || !var3.contains("LoggerSeverityProperties")) && this.bean.isLoggerSeverityPropertiesSet()) {
               var5.setLoggerSeverityProperties(this.bean.getLoggerSeverityProperties());
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if ((var3 == null || !var3.contains("StacktraceDepth")) && this.bean.isStacktraceDepthSet()) {
               var5.setStacktraceDepth(this.bean.getStacktraceDepth());
            }

            if ((var3 == null || !var3.contains("StdoutFormat")) && this.bean.isStdoutFormatSet()) {
               var5.setStdoutFormat(this.bean.getStdoutFormat());
            }

            if ((var3 == null || !var3.contains("StdoutSeverity")) && this.bean.isStdoutSeveritySet()) {
               var5.setStdoutSeverity(this.bean.getStdoutSeverity());
            }

            if ((var3 == null || !var3.contains("StdoutLogStack")) && this.bean.isStdoutLogStackSet()) {
               var5.setStdoutLogStack(this.bean.isStdoutLogStack());
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
