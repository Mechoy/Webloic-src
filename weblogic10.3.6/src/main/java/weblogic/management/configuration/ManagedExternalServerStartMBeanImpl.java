package weblogic.management.configuration;

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
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.management.ManagementException;
import weblogic.management.mbeans.custom.ManagedExternalServerStart;
import weblogic.utils.collections.CombinedIterator;

public class ManagedExternalServerStartMBeanImpl extends ConfigurationMBeanImpl implements ManagedExternalServerStartMBean, Serializable {
   private String _Arguments;
   private String _BeaHome;
   private Properties _BootProperties;
   private String _ClassPath;
   private String _JavaHome;
   private String _JavaVendor;
   private String _Name;
   private String _RootDirectory;
   private Properties _StartupProperties;
   private ManagedExternalServerStart _customizer;
   private static SchemaHelper2 _schemaHelper;

   public ManagedExternalServerStartMBeanImpl() {
      try {
         this._customizer = new ManagedExternalServerStart(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public ManagedExternalServerStartMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new ManagedExternalServerStart(this);
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

   public String getArguments() {
      return this._Arguments;
   }

   public boolean isArgumentsSet() {
      return this._isSet(12);
   }

   public void touch() throws ConfigurationException {
      this._customizer.touch();
   }

   public void freezeCurrentValue(String var1) throws AttributeNotFoundException, MBeanException {
      this._customizer.freezeCurrentValue(var1);
   }

   public void setArguments(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      LegalHelper.validateArguments(var1);
      String var2 = this._Arguments;
      this._Arguments = var1;
      this._postSet(12, var2, var1);
   }

   public Properties getBootProperties() {
      return this._customizer.getBootProperties();
   }

   public boolean isBootPropertiesSet() {
      return this._isSet(13);
   }

   public void restoreDefaultValue(String var1) throws AttributeNotFoundException {
      this._customizer.restoreDefaultValue(var1);
   }

   public void setBootProperties(Properties var1) throws InvalidAttributeValueException {
      this._BootProperties = var1;
   }

   public Properties getStartupProperties() {
      return this._customizer.getStartupProperties();
   }

   public boolean isStartupPropertiesSet() {
      return this._isSet(14);
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
         var1 = 12;
      }

      try {
         switch (var1) {
            case 12:
               this._Arguments = null;
               if (var2) {
                  break;
               }
            case 10:
               this._BeaHome = null;
               if (var2) {
                  break;
               }
            case 13:
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
            case 2:
               this._customizer.setName((String)null);
               if (var2) {
                  break;
               }
            case 11:
               this._RootDirectory = null;
               if (var2) {
                  break;
               }
            case 14:
               this._StartupProperties = null;
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
      return "ManagedExternalServerStart";
   }

   public void putValue(String var1, Object var2) {
      String var5;
      if (var1.equals("Arguments")) {
         var5 = this._Arguments;
         this._Arguments = (String)var2;
         this._postSet(12, var5, this._Arguments);
      } else if (var1.equals("BeaHome")) {
         var5 = this._BeaHome;
         this._BeaHome = (String)var2;
         this._postSet(10, var5, this._BeaHome);
      } else {
         Properties var4;
         if (var1.equals("BootProperties")) {
            var4 = this._BootProperties;
            this._BootProperties = (Properties)var2;
            this._postSet(13, var4, this._BootProperties);
         } else if (var1.equals("ClassPath")) {
            var5 = this._ClassPath;
            this._ClassPath = (String)var2;
            this._postSet(9, var5, this._ClassPath);
         } else if (var1.equals("JavaHome")) {
            var5 = this._JavaHome;
            this._JavaHome = (String)var2;
            this._postSet(8, var5, this._JavaHome);
         } else if (var1.equals("JavaVendor")) {
            var5 = this._JavaVendor;
            this._JavaVendor = (String)var2;
            this._postSet(7, var5, this._JavaVendor);
         } else if (var1.equals("Name")) {
            var5 = this._Name;
            this._Name = (String)var2;
            this._postSet(2, var5, this._Name);
         } else if (var1.equals("RootDirectory")) {
            var5 = this._RootDirectory;
            this._RootDirectory = (String)var2;
            this._postSet(11, var5, this._RootDirectory);
         } else if (var1.equals("StartupProperties")) {
            var4 = this._StartupProperties;
            this._StartupProperties = (Properties)var2;
            this._postSet(14, var4, this._StartupProperties);
         } else if (var1.equals("customizer")) {
            ManagedExternalServerStart var3 = this._customizer;
            this._customizer = (ManagedExternalServerStart)var2;
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
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("RootDirectory")) {
         return this._RootDirectory;
      } else if (var1.equals("StartupProperties")) {
         return this._StartupProperties;
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
            case 17:
            default:
               break;
            case 8:
               if (var1.equals("bea-home")) {
                  return 10;
               }
               break;
            case 9:
               if (var1.equals("arguments")) {
                  return 12;
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
               break;
            case 14:
               if (var1.equals("root-directory")) {
                  return 11;
               }
               break;
            case 15:
               if (var1.equals("boot-properties")) {
                  return 13;
               }
               break;
            case 18:
               if (var1.equals("startup-properties")) {
                  return 14;
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
               return "arguments";
            case 13:
               return "boot-properties";
            case 14:
               return "startup-properties";
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
      private ManagedExternalServerStartMBeanImpl bean;

      protected Helper(ManagedExternalServerStartMBeanImpl var1) {
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
               return "Arguments";
            case 13:
               return "BootProperties";
            case 14:
               return "StartupProperties";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("Arguments")) {
            return 12;
         } else if (var1.equals("BeaHome")) {
            return 10;
         } else if (var1.equals("BootProperties")) {
            return 13;
         } else if (var1.equals("ClassPath")) {
            return 9;
         } else if (var1.equals("JavaHome")) {
            return 8;
         } else if (var1.equals("JavaVendor")) {
            return 7;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("RootDirectory")) {
            return 11;
         } else {
            return var1.equals("StartupProperties") ? 14 : super.getPropertyIndex(var1);
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

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isRootDirectorySet()) {
               var2.append("RootDirectory");
               var2.append(String.valueOf(this.bean.getRootDirectory()));
            }

            if (this.bean.isStartupPropertiesSet()) {
               var2.append("StartupProperties");
               var2.append(String.valueOf(this.bean.getStartupProperties()));
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
            ManagedExternalServerStartMBeanImpl var2 = (ManagedExternalServerStartMBeanImpl)var1;
            this.computeDiff("Arguments", this.bean.getArguments(), var2.getArguments(), true);
            this.computeDiff("BeaHome", this.bean.getBeaHome(), var2.getBeaHome(), true);
            this.computeDiff("ClassPath", this.bean.getClassPath(), var2.getClassPath(), true);
            this.computeDiff("JavaHome", this.bean.getJavaHome(), var2.getJavaHome(), true);
            this.computeDiff("JavaVendor", this.bean.getJavaVendor(), var2.getJavaVendor(), true);
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeDiff("RootDirectory", this.bean.getRootDirectory(), var2.getRootDirectory(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            ManagedExternalServerStartMBeanImpl var3 = (ManagedExternalServerStartMBeanImpl)var1.getSourceBean();
            ManagedExternalServerStartMBeanImpl var4 = (ManagedExternalServerStartMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("Arguments")) {
                  var3.setArguments(var4.getArguments());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
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
                  } else if (var5.equals("Name")) {
                     var3.setName(var4.getName());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 2);
                  } else if (var5.equals("RootDirectory")) {
                     var3.setRootDirectory(var4.getRootDirectory());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 11);
                  } else if (!var5.equals("StartupProperties")) {
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
            ManagedExternalServerStartMBeanImpl var5 = (ManagedExternalServerStartMBeanImpl)var1;
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

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if ((var3 == null || !var3.contains("RootDirectory")) && this.bean.isRootDirectorySet()) {
               var5.setRootDirectory(this.bean.getRootDirectory());
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
