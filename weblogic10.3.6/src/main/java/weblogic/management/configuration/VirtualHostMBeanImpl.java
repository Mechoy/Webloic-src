package weblogic.management.configuration;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.zip.CRC32;
import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BootstrapProperties;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.management.DistributedManagementException;
import weblogic.management.ManagementException;
import weblogic.management.mbeans.custom.VirtualHost;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.ArrayIterator;
import weblogic.utils.collections.CombinedIterator;

public class VirtualHostMBeanImpl extends WebServerMBeanImpl implements VirtualHostMBean, Serializable {
   private int _LogFileCount;
   private String _LogFileFormat;
   private boolean _LogFileLimitEnabled;
   private String _LogFileName;
   private int _LogRotationPeriodMins;
   private String _LogRotationTimeBegin;
   private String _LogRotationType;
   private boolean _LogTimeInGMT;
   private boolean _LoggingEnabled;
   private String _Name;
   private String _NetworkAccessPoint;
   private Set _ServerNames;
   private String[] _VirtualHostNames;
   private VirtualHost _customizer;
   private static SchemaHelper2 _schemaHelper;

   public VirtualHostMBeanImpl() {
      try {
         this._customizer = new VirtualHost(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public VirtualHostMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new VirtualHost(this);
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         }

         throw new UndeclaredThrowableException(var4);
      }

      this._initializeProperty(-1);
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

   public Set getServerNames() {
      return this._customizer.getServerNames();
   }

   public String[] getVirtualHostNames() {
      return this._VirtualHostNames;
   }

   public boolean isNameSet() {
      return this._isSet(2);
   }

   public boolean isServerNamesSet() {
      return this._isSet(49);
   }

   public boolean isVirtualHostNamesSet() {
      return this._isSet(50);
   }

   public void setServerNames(Set var1) throws InvalidAttributeValueException {
      this._ServerNames = var1;
   }

   public void setLoggingEnabled(boolean var1) {
      boolean var2 = this.isLoggingEnabled();
      this._customizer.setLoggingEnabled(var1);
      this._postSet(10, var2, var1);
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

   public void setVirtualHostNames(String[] var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? new String[0] : var1;
      var1 = this._getHelper()._trimElements(var1);
      String[] var2 = this._VirtualHostNames;
      this._VirtualHostNames = var1;
      this._postSet(50, var2, var1);
   }

   public String getNetworkAccessPoint() {
      return this._NetworkAccessPoint;
   }

   public boolean isLoggingEnabled() {
      return this._customizer.isLoggingEnabled();
   }

   public boolean isLoggingEnabledSet() {
      return this._isSet(10);
   }

   public boolean isNetworkAccessPointSet() {
      return this._isSet(51);
   }

   public String getLogFileFormat() {
      return this._customizer.getLogFileFormat();
   }

   public boolean isLogFileFormatSet() {
      return this._isSet(11);
   }

   public void setNetworkAccessPoint(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._NetworkAccessPoint;
      this._NetworkAccessPoint = var1;
      this._postSet(51, var2, var1);
   }

   public void setLogFileFormat(String var1) throws InvalidAttributeValueException, DistributedManagementException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"common", "extended"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("LogFileFormat", var1, var2);
      String var3 = this.getLogFileFormat();
      this._customizer.setLogFileFormat(var1);
      this._postSet(11, var3, var1);
   }

   public boolean getLogTimeInGMT() {
      return this._customizer.getLogTimeInGMT();
   }

   public boolean isLogTimeInGMTSet() {
      return this._isSet(12);
   }

   public void setLogTimeInGMT(boolean var1) {
      boolean var2 = this.getLogTimeInGMT();
      this._customizer.setLogTimeInGMT(var1);
      this._postSet(12, var2, var1);
   }

   public String getLogFileName() {
      return this._customizer.getLogFileName();
   }

   public boolean isLogFileNameSet() {
      return this._isSet(13);
   }

   public void setLogFileName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this.getLogFileName();
      this._customizer.setLogFileName(var1);
      this._postSet(13, var2, var1);
   }

   public void touch() throws ConfigurationException {
      this._customizer.touch();
   }

   public void freezeCurrentValue(String var1) throws AttributeNotFoundException, MBeanException {
      this._customizer.freezeCurrentValue(var1);
   }

   public void restoreDefaultValue(String var1) throws AttributeNotFoundException {
      this._customizer.restoreDefaultValue(var1);
   }

   public String getLogRotationType() {
      return this._customizer.getLogRotationType();
   }

   public boolean isLogRotationTypeSet() {
      return this._isSet(19);
   }

   public void setLogRotationType(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"size", "date"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("LogRotationType", var1, var2);
      String var3 = this.getLogRotationType();
      this._customizer.setLogRotationType(var1);
      this._postSet(19, var3, var1);
   }

   public int getLogRotationPeriodMins() {
      return this._customizer.getLogRotationPeriodMins();
   }

   public boolean isLogRotationPeriodMinsSet() {
      return this._isSet(20);
   }

   public void setLogRotationPeriodMins(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("LogRotationPeriodMins", (long)var1, 1L, 2147483647L);
      int var2 = this.getLogRotationPeriodMins();

      try {
         this._customizer.setLogRotationPeriodMins(var1);
      } catch (DistributedManagementException var4) {
         throw new UndeclaredThrowableException(var4);
      }

      this._postSet(20, var2, var1);
   }

   public String getLogRotationTimeBegin() {
      return this._customizer.getLogRotationTimeBegin();
   }

   public boolean isLogRotationTimeBeginSet() {
      return this._isSet(23);
   }

   public void setLogRotationTimeBegin(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      LoggingLegalHelper.validateWebServerLogRotationTimeBegin(var1);
      String var2 = this.getLogRotationTimeBegin();
      this._customizer.setLogRotationTimeBegin(var1);
      this._postSet(23, var2, var1);
   }

   public boolean isLogFileLimitEnabled() {
      return this._customizer.isLogFileLimitEnabled();
   }

   public boolean isLogFileLimitEnabledSet() {
      return this._isSet(45);
   }

   public void setLogFileLimitEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this.isLogFileLimitEnabled();
      this._customizer.setLogFileLimitEnabled(var1);
      this._postSet(45, var2, var1);
   }

   public int getLogFileCount() {
      return this._customizer.getLogFileCount();
   }

   public boolean isLogFileCountSet() {
      return this._isSet(46);
   }

   public void setLogFileCount(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("LogFileCount", (long)var1, 1L, 9999L);
      int var2 = this.getLogFileCount();
      this._customizer.setLogFileCount(var1);
      this._postSet(46, var2, var1);
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
         var1 = 46;
      }

      try {
         switch (var1) {
            case 46:
               this._customizer.setLogFileCount(7);
               if (var2) {
                  break;
               }
            case 11:
               this._customizer.setLogFileFormat("common");
               if (var2) {
                  break;
               }
            case 13:
               this._customizer.setLogFileName("logs/access.log");
               if (var2) {
                  break;
               }
            case 20:
               this._customizer.setLogRotationPeriodMins(1440);
               if (var2) {
                  break;
               }
            case 23:
               this._customizer.setLogRotationTimeBegin((String)null);
               if (var2) {
                  break;
               }
            case 19:
               this._customizer.setLogRotationType("size");
               if (var2) {
                  break;
               }
            case 12:
               this._customizer.setLogTimeInGMT(false);
               if (var2) {
                  break;
               }
            case 2:
               this._customizer.setName((String)null);
               if (var2) {
                  break;
               }
            case 51:
               this._NetworkAccessPoint = null;
               if (var2) {
                  break;
               }
            case 49:
               this._ServerNames = null;
               if (var2) {
                  break;
               }
            case 50:
               this._VirtualHostNames = new String[0];
               if (var2) {
                  break;
               }
            case 45:
               this._customizer.setLogFileLimitEnabled(false);
               if (var2) {
                  break;
               }
            case 10:
               this._customizer.setLoggingEnabled(true);
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
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 21:
            case 22:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 47:
            case 48:
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
      return "VirtualHost";
   }

   public void putValue(String var1, Object var2) {
      int var8;
      if (var1.equals("LogFileCount")) {
         var8 = this._LogFileCount;
         this._LogFileCount = (Integer)var2;
         this._postSet(46, var8, this._LogFileCount);
      } else {
         String var6;
         if (var1.equals("LogFileFormat")) {
            var6 = this._LogFileFormat;
            this._LogFileFormat = (String)var2;
            this._postSet(11, var6, this._LogFileFormat);
         } else {
            boolean var7;
            if (var1.equals("LogFileLimitEnabled")) {
               var7 = this._LogFileLimitEnabled;
               this._LogFileLimitEnabled = (Boolean)var2;
               this._postSet(45, var7, this._LogFileLimitEnabled);
            } else if (var1.equals("LogFileName")) {
               var6 = this._LogFileName;
               this._LogFileName = (String)var2;
               this._postSet(13, var6, this._LogFileName);
            } else if (var1.equals("LogRotationPeriodMins")) {
               var8 = this._LogRotationPeriodMins;
               this._LogRotationPeriodMins = (Integer)var2;
               this._postSet(20, var8, this._LogRotationPeriodMins);
            } else if (var1.equals("LogRotationTimeBegin")) {
               var6 = this._LogRotationTimeBegin;
               this._LogRotationTimeBegin = (String)var2;
               this._postSet(23, var6, this._LogRotationTimeBegin);
            } else if (var1.equals("LogRotationType")) {
               var6 = this._LogRotationType;
               this._LogRotationType = (String)var2;
               this._postSet(19, var6, this._LogRotationType);
            } else if (var1.equals("LogTimeInGMT")) {
               var7 = this._LogTimeInGMT;
               this._LogTimeInGMT = (Boolean)var2;
               this._postSet(12, var7, this._LogTimeInGMT);
            } else if (var1.equals("LoggingEnabled")) {
               var7 = this._LoggingEnabled;
               this._LoggingEnabled = (Boolean)var2;
               this._postSet(10, var7, this._LoggingEnabled);
            } else if (var1.equals("Name")) {
               var6 = this._Name;
               this._Name = (String)var2;
               this._postSet(2, var6, this._Name);
            } else if (var1.equals("NetworkAccessPoint")) {
               var6 = this._NetworkAccessPoint;
               this._NetworkAccessPoint = (String)var2;
               this._postSet(51, var6, this._NetworkAccessPoint);
            } else if (var1.equals("ServerNames")) {
               Set var5 = this._ServerNames;
               this._ServerNames = (Set)var2;
               this._postSet(49, var5, this._ServerNames);
            } else if (var1.equals("VirtualHostNames")) {
               String[] var4 = this._VirtualHostNames;
               this._VirtualHostNames = (String[])((String[])var2);
               this._postSet(50, var4, this._VirtualHostNames);
            } else if (var1.equals("customizer")) {
               VirtualHost var3 = this._customizer;
               this._customizer = (VirtualHost)var2;
            } else {
               super.putValue(var1, var2);
            }
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("LogFileCount")) {
         return new Integer(this._LogFileCount);
      } else if (var1.equals("LogFileFormat")) {
         return this._LogFileFormat;
      } else if (var1.equals("LogFileLimitEnabled")) {
         return new Boolean(this._LogFileLimitEnabled);
      } else if (var1.equals("LogFileName")) {
         return this._LogFileName;
      } else if (var1.equals("LogRotationPeriodMins")) {
         return new Integer(this._LogRotationPeriodMins);
      } else if (var1.equals("LogRotationTimeBegin")) {
         return this._LogRotationTimeBegin;
      } else if (var1.equals("LogRotationType")) {
         return this._LogRotationType;
      } else if (var1.equals("LogTimeInGMT")) {
         return new Boolean(this._LogTimeInGMT);
      } else if (var1.equals("LoggingEnabled")) {
         return new Boolean(this._LoggingEnabled);
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("NetworkAccessPoint")) {
         return this._NetworkAccessPoint;
      } else if (var1.equals("ServerNames")) {
         return this._ServerNames;
      } else if (var1.equals("VirtualHostNames")) {
         return this._VirtualHostNames;
      } else {
         return var1.equals("customizer") ? this._customizer : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends WebServerMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 4:
               if (var1.equals("name")) {
                  return 2;
               }
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 16:
            case 18:
            case 19:
            case 21:
            default:
               break;
            case 12:
               if (var1.equals("server-names")) {
                  return 49;
               }
               break;
            case 13:
               if (var1.equals("log-file-name")) {
                  return 13;
               }
               break;
            case 14:
               if (var1.equals("log-file-count")) {
                  return 46;
               }

               if (var1.equals("log-time-ingmt")) {
                  return 12;
               }
               break;
            case 15:
               if (var1.equals("log-file-format")) {
                  return 11;
               }

               if (var1.equals("logging-enabled")) {
                  return 10;
               }
               break;
            case 17:
               if (var1.equals("log-rotation-type")) {
                  return 19;
               }

               if (var1.equals("virtual-host-name")) {
                  return 50;
               }
               break;
            case 20:
               if (var1.equals("network-access-point")) {
                  return 51;
               }
               break;
            case 22:
               if (var1.equals("log-file-limit-enabled")) {
                  return 45;
               }
               break;
            case 23:
               if (var1.equals("log-rotation-time-begin")) {
                  return 23;
               }
               break;
            case 24:
               if (var1.equals("log-rotation-period-mins")) {
                  return 20;
               }
         }

         return super.getPropertyIndex(var1);
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            case 9:
               return new WebServerLogMBeanImpl.SchemaHelper2();
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
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 21:
            case 22:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 47:
            case 48:
            default:
               return super.getElementName(var1);
            case 10:
               return "logging-enabled";
            case 11:
               return "log-file-format";
            case 12:
               return "log-time-ingmt";
            case 13:
               return "log-file-name";
            case 19:
               return "log-rotation-type";
            case 20:
               return "log-rotation-period-mins";
            case 23:
               return "log-rotation-time-begin";
            case 45:
               return "log-file-limit-enabled";
            case 46:
               return "log-file-count";
            case 49:
               return "server-names";
            case 50:
               return "virtual-host-name";
            case 51:
               return "network-access-point";
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 7:
               return true;
            case 44:
               return true;
            case 50:
               return true;
            default:
               return super.isArray(var1);
         }
      }

      public boolean isBean(int var1) {
         switch (var1) {
            case 9:
               return true;
            default:
               return super.isBean(var1);
         }
      }

      public boolean isConfigurable(int var1) {
         switch (var1) {
            case 10:
               return true;
            case 11:
               return true;
            case 12:
               return true;
            case 13:
               return true;
            case 14:
               return true;
            case 15:
               return true;
            case 16:
               return true;
            case 17:
               return true;
            case 18:
               return true;
            case 19:
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
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 33:
            case 34:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 49:
            default:
               return super.isConfigurable(var1);
            case 31:
               return true;
            case 32:
               return true;
            case 35:
               return true;
            case 36:
               return true;
            case 37:
               return true;
            case 38:
               return true;
            case 47:
               return true;
            case 48:
               return true;
            case 50:
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

      public boolean hasKey() {
         return true;
      }

      public String[] getKeyElementNames() {
         ArrayList var1 = new ArrayList();
         var1.add("name");
         return (String[])((String[])var1.toArray(new String[0]));
      }
   }

   protected static class Helper extends WebServerMBeanImpl.Helper {
      private VirtualHostMBeanImpl bean;

      protected Helper(VirtualHostMBeanImpl var1) {
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
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 21:
            case 22:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 47:
            case 48:
            default:
               return super.getPropertyName(var1);
            case 10:
               return "LoggingEnabled";
            case 11:
               return "LogFileFormat";
            case 12:
               return "LogTimeInGMT";
            case 13:
               return "LogFileName";
            case 19:
               return "LogRotationType";
            case 20:
               return "LogRotationPeriodMins";
            case 23:
               return "LogRotationTimeBegin";
            case 45:
               return "LogFileLimitEnabled";
            case 46:
               return "LogFileCount";
            case 49:
               return "ServerNames";
            case 50:
               return "VirtualHostNames";
            case 51:
               return "NetworkAccessPoint";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("LogFileCount")) {
            return 46;
         } else if (var1.equals("LogFileFormat")) {
            return 11;
         } else if (var1.equals("LogFileName")) {
            return 13;
         } else if (var1.equals("LogRotationPeriodMins")) {
            return 20;
         } else if (var1.equals("LogRotationTimeBegin")) {
            return 23;
         } else if (var1.equals("LogRotationType")) {
            return 19;
         } else if (var1.equals("LogTimeInGMT")) {
            return 12;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("NetworkAccessPoint")) {
            return 51;
         } else if (var1.equals("ServerNames")) {
            return 49;
         } else if (var1.equals("VirtualHostNames")) {
            return 50;
         } else if (var1.equals("LogFileLimitEnabled")) {
            return 45;
         } else {
            return var1.equals("LoggingEnabled") ? 10 : super.getPropertyIndex(var1);
         }
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         if (this.bean.getWebServerLog() != null) {
            var1.add(new ArrayIterator(new WebServerLogMBean[]{this.bean.getWebServerLog()}));
         }

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
            if (this.bean.isLogFileCountSet()) {
               var2.append("LogFileCount");
               var2.append(String.valueOf(this.bean.getLogFileCount()));
            }

            if (this.bean.isLogFileFormatSet()) {
               var2.append("LogFileFormat");
               var2.append(String.valueOf(this.bean.getLogFileFormat()));
            }

            if (this.bean.isLogFileNameSet()) {
               var2.append("LogFileName");
               var2.append(String.valueOf(this.bean.getLogFileName()));
            }

            if (this.bean.isLogRotationPeriodMinsSet()) {
               var2.append("LogRotationPeriodMins");
               var2.append(String.valueOf(this.bean.getLogRotationPeriodMins()));
            }

            if (this.bean.isLogRotationTimeBeginSet()) {
               var2.append("LogRotationTimeBegin");
               var2.append(String.valueOf(this.bean.getLogRotationTimeBegin()));
            }

            if (this.bean.isLogRotationTypeSet()) {
               var2.append("LogRotationType");
               var2.append(String.valueOf(this.bean.getLogRotationType()));
            }

            if (this.bean.isLogTimeInGMTSet()) {
               var2.append("LogTimeInGMT");
               var2.append(String.valueOf(this.bean.getLogTimeInGMT()));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isNetworkAccessPointSet()) {
               var2.append("NetworkAccessPoint");
               var2.append(String.valueOf(this.bean.getNetworkAccessPoint()));
            }

            if (this.bean.isServerNamesSet()) {
               var2.append("ServerNames");
               var2.append(String.valueOf(this.bean.getServerNames()));
            }

            if (this.bean.isVirtualHostNamesSet()) {
               var2.append("VirtualHostNames");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getVirtualHostNames())));
            }

            if (this.bean.isLogFileLimitEnabledSet()) {
               var2.append("LogFileLimitEnabled");
               var2.append(String.valueOf(this.bean.isLogFileLimitEnabled()));
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
            VirtualHostMBeanImpl var2 = (VirtualHostMBeanImpl)var1;
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("LogFileCount", this.bean.getLogFileCount(), var2.getLogFileCount(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("LogFileFormat", this.bean.getLogFileFormat(), var2.getLogFileFormat(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("LogFileName", this.bean.getLogFileName(), var2.getLogFileName(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("LogRotationPeriodMins", this.bean.getLogRotationPeriodMins(), var2.getLogRotationPeriodMins(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("LogRotationTimeBegin", this.bean.getLogRotationTimeBegin(), var2.getLogRotationTimeBegin(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("LogRotationType", this.bean.getLogRotationType(), var2.getLogRotationType(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("LogTimeInGMT", this.bean.getLogTimeInGMT(), var2.getLogTimeInGMT(), false);
            }

            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeDiff("NetworkAccessPoint", this.bean.getNetworkAccessPoint(), var2.getNetworkAccessPoint(), false);
            this.computeDiff("VirtualHostNames", this.bean.getVirtualHostNames(), var2.getVirtualHostNames(), false);
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("LogFileLimitEnabled", this.bean.isLogFileLimitEnabled(), var2.isLogFileLimitEnabled(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("LoggingEnabled", this.bean.isLoggingEnabled(), var2.isLoggingEnabled(), false);
            }

         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            VirtualHostMBeanImpl var3 = (VirtualHostMBeanImpl)var1.getSourceBean();
            VirtualHostMBeanImpl var4 = (VirtualHostMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("LogFileCount")) {
                  var3.setLogFileCount(var4.getLogFileCount());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 46);
               } else if (var5.equals("LogFileFormat")) {
                  var3.setLogFileFormat(var4.getLogFileFormat());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("LogFileName")) {
                  var3.setLogFileName(var4.getLogFileName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 13);
               } else if (var5.equals("LogRotationPeriodMins")) {
                  var3.setLogRotationPeriodMins(var4.getLogRotationPeriodMins());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 20);
               } else if (var5.equals("LogRotationTimeBegin")) {
                  var3.setLogRotationTimeBegin(var4.getLogRotationTimeBegin());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 23);
               } else if (var5.equals("LogRotationType")) {
                  var3.setLogRotationType(var4.getLogRotationType());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 19);
               } else if (var5.equals("LogTimeInGMT")) {
                  var3.setLogTimeInGMT(var4.getLogTimeInGMT());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("Name")) {
                  var3.setName(var4.getName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 2);
               } else if (var5.equals("NetworkAccessPoint")) {
                  var3.setNetworkAccessPoint(var4.getNetworkAccessPoint());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 51);
               } else if (!var5.equals("ServerNames")) {
                  if (var5.equals("VirtualHostNames")) {
                     var3.setVirtualHostNames(var4.getVirtualHostNames());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 50);
                  } else if (var5.equals("LogFileLimitEnabled")) {
                     var3.setLogFileLimitEnabled(var4.isLogFileLimitEnabled());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 45);
                  } else if (var5.equals("LoggingEnabled")) {
                     var3.setLoggingEnabled(var4.isLoggingEnabled());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 10);
                  } else {
                     super.applyPropertyUpdate(var1, var2);
                  }
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
            VirtualHostMBeanImpl var5 = (VirtualHostMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if (var2 && (var3 == null || !var3.contains("LogFileCount")) && this.bean.isLogFileCountSet()) {
               var5.setLogFileCount(this.bean.getLogFileCount());
            }

            if (var2 && (var3 == null || !var3.contains("LogFileFormat")) && this.bean.isLogFileFormatSet()) {
               var5.setLogFileFormat(this.bean.getLogFileFormat());
            }

            if (var2 && (var3 == null || !var3.contains("LogFileName")) && this.bean.isLogFileNameSet()) {
               var5.setLogFileName(this.bean.getLogFileName());
            }

            if (var2 && (var3 == null || !var3.contains("LogRotationPeriodMins")) && this.bean.isLogRotationPeriodMinsSet()) {
               var5.setLogRotationPeriodMins(this.bean.getLogRotationPeriodMins());
            }

            if (var2 && (var3 == null || !var3.contains("LogRotationTimeBegin")) && this.bean.isLogRotationTimeBeginSet()) {
               var5.setLogRotationTimeBegin(this.bean.getLogRotationTimeBegin());
            }

            if (var2 && (var3 == null || !var3.contains("LogRotationType")) && this.bean.isLogRotationTypeSet()) {
               var5.setLogRotationType(this.bean.getLogRotationType());
            }

            if (var2 && (var3 == null || !var3.contains("LogTimeInGMT")) && this.bean.isLogTimeInGMTSet()) {
               var5.setLogTimeInGMT(this.bean.getLogTimeInGMT());
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if ((var3 == null || !var3.contains("NetworkAccessPoint")) && this.bean.isNetworkAccessPointSet()) {
               var5.setNetworkAccessPoint(this.bean.getNetworkAccessPoint());
            }

            if ((var3 == null || !var3.contains("VirtualHostNames")) && this.bean.isVirtualHostNamesSet()) {
               String[] var4 = this.bean.getVirtualHostNames();
               var5.setVirtualHostNames(var4 == null ? null : (String[])((String[])((String[])((String[])var4)).clone()));
            }

            if (var2 && (var3 == null || !var3.contains("LogFileLimitEnabled")) && this.bean.isLogFileLimitEnabledSet()) {
               var5.setLogFileLimitEnabled(this.bean.isLogFileLimitEnabled());
            }

            if (var2 && (var3 == null || !var3.contains("LoggingEnabled")) && this.bean.isLoggingEnabledSet()) {
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
