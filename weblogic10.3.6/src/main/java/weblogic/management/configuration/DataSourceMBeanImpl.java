package weblogic.management.configuration;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
import weblogic.management.mbeans.custom.DataSource;
import weblogic.utils.collections.ArrayIterator;
import weblogic.utils.collections.CombinedIterator;

public class DataSourceMBeanImpl extends DeploymentMBeanImpl implements DataSourceMBean, Serializable {
   private DataSourceLogFileMBean _DataSourceLogFile;
   private String _Name;
   private String _RmiJDBCSecurity;
   private DataSource _customizer;
   private static SchemaHelper2 _schemaHelper;

   public DataSourceMBeanImpl() {
      try {
         this._customizer = new DataSource(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public DataSourceMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new DataSource(this);
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         }

         throw new UndeclaredThrowableException(var4);
      }

      this._initializeProperty(-1);
   }

   public DataSourceLogFileMBean getDataSourceLogFile() {
      return this._DataSourceLogFile;
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

   public boolean isDataSourceLogFileSet() {
      return this._isSet(9) || this._isAnythingSet((AbstractDescriptorBean)this.getDataSourceLogFile());
   }

   public boolean isNameSet() {
      return this._isSet(2);
   }

   public void setDataSourceLogFile(DataSourceLogFileMBean var1) throws InvalidAttributeValueException {
      AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
      if (this._setParent(var2, this, 9)) {
         this._postCreate(var2);
      }

      DataSourceLogFileMBean var3 = this._DataSourceLogFile;
      this._DataSourceLogFile = var1;
      this._postSet(9, var3, var1);
   }

   public String getRmiJDBCSecurity() {
      return this._RmiJDBCSecurity;
   }

   public boolean isRmiJDBCSecuritySet() {
      return this._isSet(10);
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

   public void setRmiJDBCSecurity(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._RmiJDBCSecurity;
      this._RmiJDBCSecurity = var1;
      this._postSet(10, var2, var1);
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
      return super._isAnythingSet() || this.isDataSourceLogFileSet();
   }

   private boolean _initializeProperty(int var1) {
      boolean var2 = var1 > -1;
      if (!var2) {
         var1 = 9;
      }

      try {
         switch (var1) {
            case 9:
               this._DataSourceLogFile = new DataSourceLogFileMBeanImpl(this, 9);
               this._postCreate((AbstractDescriptorBean)this._DataSourceLogFile);
               if (var2) {
                  break;
               }
            case 2:
               this._customizer.setName((String)null);
               if (var2) {
                  break;
               }
            case 10:
               this._RmiJDBCSecurity = "Compatibility";
               if (var2) {
                  break;
               }
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
      return "DataSource";
   }

   public void putValue(String var1, Object var2) {
      if (var1.equals("DataSourceLogFile")) {
         DataSourceLogFileMBean var5 = this._DataSourceLogFile;
         this._DataSourceLogFile = (DataSourceLogFileMBean)var2;
         this._postSet(9, var5, this._DataSourceLogFile);
      } else {
         String var4;
         if (var1.equals("Name")) {
            var4 = this._Name;
            this._Name = (String)var2;
            this._postSet(2, var4, this._Name);
         } else if (var1.equals("RmiJDBCSecurity")) {
            var4 = this._RmiJDBCSecurity;
            this._RmiJDBCSecurity = (String)var2;
            this._postSet(10, var4, this._RmiJDBCSecurity);
         } else if (var1.equals("customizer")) {
            DataSource var3 = this._customizer;
            this._customizer = (DataSource)var2;
         } else {
            super.putValue(var1, var2);
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("DataSourceLogFile")) {
         return this._DataSourceLogFile;
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("RmiJDBCSecurity")) {
         return this._RmiJDBCSecurity;
      } else {
         return var1.equals("customizer") ? this._customizer : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends DeploymentMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 4:
               if (var1.equals("name")) {
                  return 2;
               }
               break;
            case 17:
               if (var1.equals("rmi-jdbc-security")) {
                  return 10;
               }
               break;
            case 20:
               if (var1.equals("data-source-log-file")) {
                  return 9;
               }
         }

         return super.getPropertyIndex(var1);
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            case 9:
               return new DataSourceLogFileMBeanImpl.SchemaHelper2();
            default:
               return super.getSchemaHelper(var1);
         }
      }

      public String getElementName(int var1) {
         switch (var1) {
            case 2:
               return "name";
            case 9:
               return "data-source-log-file";
            case 10:
               return "rmi-jdbc-security";
            default:
               return super.getElementName(var1);
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 7:
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

   protected static class Helper extends DeploymentMBeanImpl.Helper {
      private DataSourceMBeanImpl bean;

      protected Helper(DataSourceMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 2:
               return "Name";
            case 9:
               return "DataSourceLogFile";
            case 10:
               return "RmiJDBCSecurity";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("DataSourceLogFile")) {
            return 9;
         } else if (var1.equals("Name")) {
            return 2;
         } else {
            return var1.equals("RmiJDBCSecurity") ? 10 : super.getPropertyIndex(var1);
         }
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         if (this.bean.getDataSourceLogFile() != null) {
            var1.add(new ArrayIterator(new DataSourceLogFileMBean[]{this.bean.getDataSourceLogFile()}));
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
            var5 = this.computeChildHashValue(this.bean.getDataSourceLogFile());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isRmiJDBCSecuritySet()) {
               var2.append("RmiJDBCSecurity");
               var2.append(String.valueOf(this.bean.getRmiJDBCSecurity()));
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
            DataSourceMBeanImpl var2 = (DataSourceMBeanImpl)var1;
            this.computeSubDiff("DataSourceLogFile", this.bean.getDataSourceLogFile(), var2.getDataSourceLogFile());
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeDiff("RmiJDBCSecurity", this.bean.getRmiJDBCSecurity(), var2.getRmiJDBCSecurity(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            DataSourceMBeanImpl var3 = (DataSourceMBeanImpl)var1.getSourceBean();
            DataSourceMBeanImpl var4 = (DataSourceMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("DataSourceLogFile")) {
                  if (var6 == 2) {
                     var3.setDataSourceLogFile((DataSourceLogFileMBean)this.createCopy((AbstractDescriptorBean)var4.getDataSourceLogFile()));
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3._destroySingleton("DataSourceLogFile", var3.getDataSourceLogFile());
                  }

                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("Name")) {
                  var3.setName(var4.getName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 2);
               } else if (var5.equals("RmiJDBCSecurity")) {
                  var3.setRmiJDBCSecurity(var4.getRmiJDBCSecurity());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
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
            DataSourceMBeanImpl var5 = (DataSourceMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("DataSourceLogFile")) && this.bean.isDataSourceLogFileSet() && !var5._isSet(9)) {
               DataSourceLogFileMBean var4 = this.bean.getDataSourceLogFile();
               var5.setDataSourceLogFile((DataSourceLogFileMBean)null);
               var5.setDataSourceLogFile(var4 == null ? null : (DataSourceLogFileMBean)this.createCopy((AbstractDescriptorBean)var4, var2));
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if ((var3 == null || !var3.contains("RmiJDBCSecurity")) && this.bean.isRmiJDBCSecuritySet()) {
               var5.setRmiJDBCSecurity(this.bean.getRmiJDBCSecurity());
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
         this.inferSubTree(this.bean.getDataSourceLogFile(), var1, var2);
      }
   }
}
