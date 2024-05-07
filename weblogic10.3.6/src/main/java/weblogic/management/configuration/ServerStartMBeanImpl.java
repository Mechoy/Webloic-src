package weblogic.management.configuration;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
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
import weblogic.management.ManagementException;
import weblogic.management.mbeans.custom.ServerStart;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.CombinedIterator;

public class ServerStartMBeanImpl extends ConfigurationMBeanImpl implements ServerStartMBean, Serializable {
   private String _Arguments;
   private String _BeaHome;
   private Properties _BootProperties;
   private String _ClassPath;
   private String _JavaHome;
   private String _JavaVendor;
   private int _MaxRestartCount;
   private String _Name;
   private String _OutputFile;
   private String _Password;
   private byte[] _PasswordEncrypted;
   private String _RootDirectory;
   private String _SecurityPolicyFile;
   private Properties _StartupProperties;
   private String _Username;
   private ServerStart _customizer;
   private static SchemaHelper2 _schemaHelper;

   public ServerStartMBeanImpl() {
      try {
         this._customizer = new ServerStart(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public ServerStartMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new ServerStart(this);
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         }

         throw new UndeclaredThrowableException(var4);
      }

      this._initializeProperty(-1);
   }

   public String getJavaVendor() {
      return this._JavaVendor;
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

   public boolean isJavaVendorSet() {
      return this._isSet(7);
   }

   public boolean isNameSet() {
      return this._isSet(2);
   }

   public void setJavaVendor(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._JavaVendor;
      this._JavaVendor = var1;
      this._postSet(7, var2, var1);
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

   public String getJavaHome() {
      return this._JavaHome;
   }

   public boolean isJavaHomeSet() {
      return this._isSet(8);
   }

   public void setJavaHome(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      LegalHelper.validateJavaHome(var1);
      String var2 = this._JavaHome;
      this._JavaHome = var1;
      this._postSet(8, var2, var1);
   }

   public String getClassPath() {
      return this._ClassPath;
   }

   public boolean isClassPathSet() {
      return this._isSet(9);
   }

   public void setClassPath(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      LegalHelper.validateClasspath(var1);
      String var2 = this._ClassPath;
      this._ClassPath = var1;
      this._postSet(9, var2, var1);
   }

   public String getBeaHome() {
      return this._BeaHome;
   }

   public boolean isBeaHomeSet() {
      return this._isSet(10);
   }

   public void setBeaHome(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      LegalHelper.validateBeaHome(var1);
      String var2 = this._BeaHome;
      this._BeaHome = var1;
      this._postSet(10, var2, var1);
   }

   public String getRootDirectory() {
      return this._RootDirectory;
   }

   public boolean isRootDirectorySet() {
      return this._isSet(11);
   }

   public void setRootDirectory(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      LegalHelper.validateRootDirectory(var1);
      String var2 = this._RootDirectory;
      this._RootDirectory = var1;
      this._postSet(11, var2, var1);
   }

   public String getSecurityPolicyFile() {
      return this._SecurityPolicyFile;
   }

   public boolean isSecurityPolicyFileSet() {
      return this._isSet(12);
   }

   public void touch() throws ConfigurationException {
      this._customizer.touch();
   }

   public void freezeCurrentValue(String var1) throws AttributeNotFoundException, MBeanException {
      this._customizer.freezeCurrentValue(var1);
   }

   public void setSecurityPolicyFile(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      LegalHelper.validateSecurityPolicyFile(var1);
      String var2 = this._SecurityPolicyFile;
      this._SecurityPolicyFile = var1;
      this._postSet(12, var2, var1);
   }

   public String getArguments() {
      return this._Arguments;
   }

   public boolean isArgumentsSet() {
      return this._isSet(13);
   }

   public void restoreDefaultValue(String var1) throws AttributeNotFoundException {
      this._customizer.restoreDefaultValue(var1);
   }

   public void setArguments(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      LegalHelper.validateArguments(var1);
      String var2 = this._Arguments;
      this._Arguments = var1;
      this._postSet(13, var2, var1);
   }

   public int getMaxRestartCount() {
      return this._MaxRestartCount;
   }

   public boolean isMaxRestartCountSet() {
      return this._isSet(14);
   }

   public void setMaxRestartCount(int var1) throws InvalidAttributeValueException {
      int var2 = this._MaxRestartCount;
      this._MaxRestartCount = var1;
      this._postSet(14, var2, var1);
   }

   public String getOutputFile() {
      return this._OutputFile;
   }

   public boolean isOutputFileSet() {
      return this._isSet(15);
   }

   public void setOutputFile(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._OutputFile;
      this._OutputFile = var1;
      this._postSet(15, var2, var1);
   }

   public String getUsername() {
      return this._Username;
   }

   public boolean isUsernameSet() {
      return this._isSet(16);
   }

   public void setUsername(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._Username;
      this._Username = var1;
      this._postSet(16, var2, var1);
   }

   public String getPassword() {
      byte[] var1 = this.getPasswordEncrypted();
      return var1 == null ? null : this._decrypt("Password", var1);
   }

   public boolean isPasswordSet() {
      return this.isPasswordEncryptedSet();
   }

   public void setPassword(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this.setPasswordEncrypted(var1 == null ? null : this._encrypt("Password", var1));
   }

   public byte[] getPasswordEncrypted() {
      return this._getHelper()._cloneArray(this._PasswordEncrypted);
   }

   public String getPasswordEncryptedAsString() {
      byte[] var1 = this.getPasswordEncrypted();
      return var1 == null ? null : new String(var1);
   }

   public boolean isPasswordEncryptedSet() {
      return this._isSet(18);
   }

   public void setPasswordEncryptedAsString(String var1) {
      try {
         byte[] var2 = var1 == null ? null : var1.getBytes();
         this.setPasswordEncrypted(var2);
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public Properties getBootProperties() {
      return this._customizer.getBootProperties();
   }

   public boolean isBootPropertiesSet() {
      return this._isSet(19);
   }

   public void setBootProperties(Properties var1) throws InvalidAttributeValueException {
      this._BootProperties = var1;
   }

   public Properties getStartupProperties() {
      return this._customizer.getStartupProperties();
   }

   public boolean isStartupPropertiesSet() {
      return this._isSet(20);
   }

   public void setStartupProperties(Properties var1) throws InvalidAttributeValueException {
      this._StartupProperties = var1;
   }

   public Object _getKey() {
      return this.getName();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
   }

   public void setPasswordEncrypted(byte[] var1) {
      byte[] var2 = this._PasswordEncrypted;
      if (this._isProductionModeEnabled() && var1 != null && !this._isEncrypted(var1)) {
         throw new IllegalArgumentException("In production mode, it's not allowed to set a clear text value to the property: PasswordEncrypted of ServerStartMBean");
      } else {
         this._getHelper()._clearArray(this._PasswordEncrypted);
         this._PasswordEncrypted = this._getHelper()._cloneArray(var1);
         this._postSet(18, var2, var1);
      }
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
         if (var1 == 17) {
            this._markSet(18, false);
         }
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
         var1 = 13;
      }

      try {
         switch (var1) {
            case 13:
               this._Arguments = null;
               if (var2) {
                  break;
               }
            case 10:
               this._BeaHome = null;
               if (var2) {
                  break;
               }
            case 19:
               this._BootProperties = null;
               if (var2) {
                  break;
               }
            case 9:
               this._ClassPath = null;
               if (var2) {
                  break;
               }
            case 8:
               this._JavaHome = null;
               if (var2) {
                  break;
               }
            case 7:
               this._JavaVendor = null;
               if (var2) {
                  break;
               }
            case 14:
               this._MaxRestartCount = 0;
               if (var2) {
                  break;
               }
            case 2:
               this._customizer.setName((String)null);
               if (var2) {
                  break;
               }
            case 15:
               this._OutputFile = null;
               if (var2) {
                  break;
               }
            case 17:
               this._PasswordEncrypted = null;
               if (var2) {
                  break;
               }
            case 18:
               this._PasswordEncrypted = null;
               if (var2) {
                  break;
               }
            case 11:
               this._RootDirectory = null;
               if (var2) {
                  break;
               }
            case 12:
               this._SecurityPolicyFile = null;
               if (var2) {
                  break;
               }
            case 20:
               this._StartupProperties = null;
               if (var2) {
                  break;
               }
            case 16:
               this._Username = "";
               if (var2) {
                  break;
               }
            case 3:
            case 4:
            case 5:
            case 6:
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
      return "ServerStart";
   }

   public void putValue(String var1, Object var2) {
      String var4;
      if (var1.equals("Arguments")) {
         var4 = this._Arguments;
         this._Arguments = (String)var2;
         this._postSet(13, var4, this._Arguments);
      } else if (var1.equals("BeaHome")) {
         var4 = this._BeaHome;
         this._BeaHome = (String)var2;
         this._postSet(10, var4, this._BeaHome);
      } else {
         Properties var5;
         if (var1.equals("BootProperties")) {
            var5 = this._BootProperties;
            this._BootProperties = (Properties)var2;
            this._postSet(19, var5, this._BootProperties);
         } else if (var1.equals("ClassPath")) {
            var4 = this._ClassPath;
            this._ClassPath = (String)var2;
            this._postSet(9, var4, this._ClassPath);
         } else if (var1.equals("JavaHome")) {
            var4 = this._JavaHome;
            this._JavaHome = (String)var2;
            this._postSet(8, var4, this._JavaHome);
         } else if (var1.equals("JavaVendor")) {
            var4 = this._JavaVendor;
            this._JavaVendor = (String)var2;
            this._postSet(7, var4, this._JavaVendor);
         } else if (var1.equals("MaxRestartCount")) {
            int var7 = this._MaxRestartCount;
            this._MaxRestartCount = (Integer)var2;
            this._postSet(14, var7, this._MaxRestartCount);
         } else if (var1.equals("Name")) {
            var4 = this._Name;
            this._Name = (String)var2;
            this._postSet(2, var4, this._Name);
         } else if (var1.equals("OutputFile")) {
            var4 = this._OutputFile;
            this._OutputFile = (String)var2;
            this._postSet(15, var4, this._OutputFile);
         } else if (var1.equals("Password")) {
            var4 = this._Password;
            this._Password = (String)var2;
            this._postSet(17, var4, this._Password);
         } else if (var1.equals("PasswordEncrypted")) {
            byte[] var6 = this._PasswordEncrypted;
            this._PasswordEncrypted = (byte[])((byte[])var2);
            this._postSet(18, var6, this._PasswordEncrypted);
         } else if (var1.equals("RootDirectory")) {
            var4 = this._RootDirectory;
            this._RootDirectory = (String)var2;
            this._postSet(11, var4, this._RootDirectory);
         } else if (var1.equals("SecurityPolicyFile")) {
            var4 = this._SecurityPolicyFile;
            this._SecurityPolicyFile = (String)var2;
            this._postSet(12, var4, this._SecurityPolicyFile);
         } else if (var1.equals("StartupProperties")) {
            var5 = this._StartupProperties;
            this._StartupProperties = (Properties)var2;
            this._postSet(20, var5, this._StartupProperties);
         } else if (var1.equals("Username")) {
            var4 = this._Username;
            this._Username = (String)var2;
            this._postSet(16, var4, this._Username);
         } else if (var1.equals("customizer")) {
            ServerStart var3 = this._customizer;
            this._customizer = (ServerStart)var2;
         } else {
            super.putValue(var1, var2);
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("Arguments")) {
         return this._Arguments;
      } else if (var1.equals("BeaHome")) {
         return this._BeaHome;
      } else if (var1.equals("BootProperties")) {
         return this._BootProperties;
      } else if (var1.equals("ClassPath")) {
         return this._ClassPath;
      } else if (var1.equals("JavaHome")) {
         return this._JavaHome;
      } else if (var1.equals("JavaVendor")) {
         return this._JavaVendor;
      } else if (var1.equals("MaxRestartCount")) {
         return new Integer(this._MaxRestartCount);
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("OutputFile")) {
         return this._OutputFile;
      } else if (var1.equals("Password")) {
         return this._Password;
      } else if (var1.equals("PasswordEncrypted")) {
         return this._PasswordEncrypted;
      } else if (var1.equals("RootDirectory")) {
         return this._RootDirectory;
      } else if (var1.equals("SecurityPolicyFile")) {
         return this._SecurityPolicyFile;
      } else if (var1.equals("StartupProperties")) {
         return this._StartupProperties;
      } else if (var1.equals("Username")) {
         return this._Username;
      } else {
         return var1.equals("customizer") ? this._customizer : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 4:
               if (var1.equals("name")) {
                  return 2;
               }
            case 5:
            case 6:
            case 7:
            case 12:
            case 13:
            case 16:
            case 19:
            default:
               break;
            case 8:
               if (var1.equals("bea-home")) {
                  return 10;
               }

               if (var1.equals("password")) {
                  return 17;
               }

               if (var1.equals("username")) {
                  return 16;
               }
               break;
            case 9:
               if (var1.equals("arguments")) {
                  return 13;
               }

               if (var1.equals("java-home")) {
                  return 8;
               }
               break;
            case 10:
               if (var1.equals("class-path")) {
                  return 9;
               }
               break;
            case 11:
               if (var1.equals("java-vendor")) {
                  return 7;
               }

               if (var1.equals("output-file")) {
                  return 15;
               }
               break;
            case 14:
               if (var1.equals("root-directory")) {
                  return 11;
               }
               break;
            case 15:
               if (var1.equals("boot-properties")) {
                  return 19;
               }
               break;
            case 17:
               if (var1.equals("max-restart-count")) {
                  return 14;
               }
               break;
            case 18:
               if (var1.equals("password-encrypted")) {
                  return 18;
               }

               if (var1.equals("startup-properties")) {
                  return 20;
               }
               break;
            case 20:
               if (var1.equals("security-policy-file")) {
                  return 12;
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
            default:
               return super.getElementName(var1);
            case 7:
               return "java-vendor";
            case 8:
               return "java-home";
            case 9:
               return "class-path";
            case 10:
               return "bea-home";
            case 11:
               return "root-directory";
            case 12:
               return "security-policy-file";
            case 13:
               return "arguments";
            case 14:
               return "max-restart-count";
            case 15:
               return "output-file";
            case 16:
               return "username";
            case 17:
               return "password";
            case 18:
               return "password-encrypted";
            case 19:
               return "boot-properties";
            case 20:
               return "startup-properties";
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            default:
               return super.isArray(var1);
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

   protected static class Helper extends ConfigurationMBeanImpl.Helper {
      private ServerStartMBeanImpl bean;

      protected Helper(ServerStartMBeanImpl var1) {
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
            default:
               return super.getPropertyName(var1);
            case 7:
               return "JavaVendor";
            case 8:
               return "JavaHome";
            case 9:
               return "ClassPath";
            case 10:
               return "BeaHome";
            case 11:
               return "RootDirectory";
            case 12:
               return "SecurityPolicyFile";
            case 13:
               return "Arguments";
            case 14:
               return "MaxRestartCount";
            case 15:
               return "OutputFile";
            case 16:
               return "Username";
            case 17:
               return "Password";
            case 18:
               return "PasswordEncrypted";
            case 19:
               return "BootProperties";
            case 20:
               return "StartupProperties";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("Arguments")) {
            return 13;
         } else if (var1.equals("BeaHome")) {
            return 10;
         } else if (var1.equals("BootProperties")) {
            return 19;
         } else if (var1.equals("ClassPath")) {
            return 9;
         } else if (var1.equals("JavaHome")) {
            return 8;
         } else if (var1.equals("JavaVendor")) {
            return 7;
         } else if (var1.equals("MaxRestartCount")) {
            return 14;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("OutputFile")) {
            return 15;
         } else if (var1.equals("Password")) {
            return 17;
         } else if (var1.equals("PasswordEncrypted")) {
            return 18;
         } else if (var1.equals("RootDirectory")) {
            return 11;
         } else if (var1.equals("SecurityPolicyFile")) {
            return 12;
         } else if (var1.equals("StartupProperties")) {
            return 20;
         } else {
            return var1.equals("Username") ? 16 : super.getPropertyIndex(var1);
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
            if (this.bean.isArgumentsSet()) {
               var2.append("Arguments");
               var2.append(String.valueOf(this.bean.getArguments()));
            }

            if (this.bean.isBeaHomeSet()) {
               var2.append("BeaHome");
               var2.append(String.valueOf(this.bean.getBeaHome()));
            }

            if (this.bean.isBootPropertiesSet()) {
               var2.append("BootProperties");
               var2.append(String.valueOf(this.bean.getBootProperties()));
            }

            if (this.bean.isClassPathSet()) {
               var2.append("ClassPath");
               var2.append(String.valueOf(this.bean.getClassPath()));
            }

            if (this.bean.isJavaHomeSet()) {
               var2.append("JavaHome");
               var2.append(String.valueOf(this.bean.getJavaHome()));
            }

            if (this.bean.isJavaVendorSet()) {
               var2.append("JavaVendor");
               var2.append(String.valueOf(this.bean.getJavaVendor()));
            }

            if (this.bean.isMaxRestartCountSet()) {
               var2.append("MaxRestartCount");
               var2.append(String.valueOf(this.bean.getMaxRestartCount()));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isOutputFileSet()) {
               var2.append("OutputFile");
               var2.append(String.valueOf(this.bean.getOutputFile()));
            }

            if (this.bean.isPasswordSet()) {
               var2.append("Password");
               var2.append(String.valueOf(this.bean.getPassword()));
            }

            if (this.bean.isPasswordEncryptedSet()) {
               var2.append("PasswordEncrypted");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getPasswordEncrypted())));
            }

            if (this.bean.isRootDirectorySet()) {
               var2.append("RootDirectory");
               var2.append(String.valueOf(this.bean.getRootDirectory()));
            }

            if (this.bean.isSecurityPolicyFileSet()) {
               var2.append("SecurityPolicyFile");
               var2.append(String.valueOf(this.bean.getSecurityPolicyFile()));
            }

            if (this.bean.isStartupPropertiesSet()) {
               var2.append("StartupProperties");
               var2.append(String.valueOf(this.bean.getStartupProperties()));
            }

            if (this.bean.isUsernameSet()) {
               var2.append("Username");
               var2.append(String.valueOf(this.bean.getUsername()));
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
            ServerStartMBeanImpl var2 = (ServerStartMBeanImpl)var1;
            this.computeDiff("Arguments", this.bean.getArguments(), var2.getArguments(), true);
            this.computeDiff("BeaHome", this.bean.getBeaHome(), var2.getBeaHome(), true);
            this.computeDiff("ClassPath", this.bean.getClassPath(), var2.getClassPath(), true);
            this.computeDiff("JavaHome", this.bean.getJavaHome(), var2.getJavaHome(), true);
            this.computeDiff("JavaVendor", this.bean.getJavaVendor(), var2.getJavaVendor(), true);
            this.computeDiff("MaxRestartCount", this.bean.getMaxRestartCount(), var2.getMaxRestartCount(), true);
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("OutputFile", this.bean.getOutputFile(), var2.getOutputFile(), true);
            }

            this.computeDiff("PasswordEncrypted", this.bean.getPasswordEncrypted(), var2.getPasswordEncrypted(), true);
            this.computeDiff("RootDirectory", this.bean.getRootDirectory(), var2.getRootDirectory(), true);
            this.computeDiff("SecurityPolicyFile", this.bean.getSecurityPolicyFile(), var2.getSecurityPolicyFile(), true);
            this.computeDiff("Username", this.bean.getUsername(), var2.getUsername(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            ServerStartMBeanImpl var3 = (ServerStartMBeanImpl)var1.getSourceBean();
            ServerStartMBeanImpl var4 = (ServerStartMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("Arguments")) {
                  var3.setArguments(var4.getArguments());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 13);
               } else if (var5.equals("BeaHome")) {
                  var3.setBeaHome(var4.getBeaHome());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (!var5.equals("BootProperties")) {
                  if (var5.equals("ClassPath")) {
                     var3.setClassPath(var4.getClassPath());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 9);
                  } else if (var5.equals("JavaHome")) {
                     var3.setJavaHome(var4.getJavaHome());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 8);
                  } else if (var5.equals("JavaVendor")) {
                     var3.setJavaVendor(var4.getJavaVendor());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 7);
                  } else if (var5.equals("MaxRestartCount")) {
                     var3.setMaxRestartCount(var4.getMaxRestartCount());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 14);
                  } else if (var5.equals("Name")) {
                     var3.setName(var4.getName());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 2);
                  } else if (var5.equals("OutputFile")) {
                     var3.setOutputFile(var4.getOutputFile());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 15);
                  } else if (!var5.equals("Password")) {
                     if (var5.equals("PasswordEncrypted")) {
                        var3.setPasswordEncrypted(var4.getPasswordEncrypted());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 18);
                     } else if (var5.equals("RootDirectory")) {
                        var3.setRootDirectory(var4.getRootDirectory());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 11);
                     } else if (var5.equals("SecurityPolicyFile")) {
                        var3.setSecurityPolicyFile(var4.getSecurityPolicyFile());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 12);
                     } else if (!var5.equals("StartupProperties")) {
                        if (var5.equals("Username")) {
                           var3.setUsername(var4.getUsername());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 16);
                        } else {
                           super.applyPropertyUpdate(var1, var2);
                        }
                     }
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
            ServerStartMBeanImpl var5 = (ServerStartMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("Arguments")) && this.bean.isArgumentsSet()) {
               var5.setArguments(this.bean.getArguments());
            }

            if ((var3 == null || !var3.contains("BeaHome")) && this.bean.isBeaHomeSet()) {
               var5.setBeaHome(this.bean.getBeaHome());
            }

            if ((var3 == null || !var3.contains("ClassPath")) && this.bean.isClassPathSet()) {
               var5.setClassPath(this.bean.getClassPath());
            }

            if ((var3 == null || !var3.contains("JavaHome")) && this.bean.isJavaHomeSet()) {
               var5.setJavaHome(this.bean.getJavaHome());
            }

            if ((var3 == null || !var3.contains("JavaVendor")) && this.bean.isJavaVendorSet()) {
               var5.setJavaVendor(this.bean.getJavaVendor());
            }

            if ((var3 == null || !var3.contains("MaxRestartCount")) && this.bean.isMaxRestartCountSet()) {
               var5.setMaxRestartCount(this.bean.getMaxRestartCount());
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if (var2 && (var3 == null || !var3.contains("OutputFile")) && this.bean.isOutputFileSet()) {
               var5.setOutputFile(this.bean.getOutputFile());
            }

            if ((var3 == null || !var3.contains("PasswordEncrypted")) && this.bean.isPasswordEncryptedSet()) {
               byte[] var4 = this.bean.getPasswordEncrypted();
               var5.setPasswordEncrypted(var4 == null ? null : (byte[])((byte[])((byte[])((byte[])var4)).clone()));
            }

            if ((var3 == null || !var3.contains("RootDirectory")) && this.bean.isRootDirectorySet()) {
               var5.setRootDirectory(this.bean.getRootDirectory());
            }

            if ((var3 == null || !var3.contains("SecurityPolicyFile")) && this.bean.isSecurityPolicyFileSet()) {
               var5.setSecurityPolicyFile(this.bean.getSecurityPolicyFile());
            }

            if ((var3 == null || !var3.contains("Username")) && this.bean.isUsernameSet()) {
               var5.setUsername(this.bean.getUsername());
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
