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
import weblogic.descriptor.BootstrapProperties;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.ReferenceManager;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.management.ManagementException;
import weblogic.management.mbeans.custom.JMSJDBCStore;
import weblogic.utils.collections.CombinedIterator;

public class JMSJDBCStoreMBeanImpl extends JMSStoreMBeanImpl implements JMSJDBCStoreMBean, Serializable {
   private JDBCConnectionPoolMBean _ConnectionPool;
   private String _CreateTableDDLFile;
   private JDBCStoreMBean _DelegatedBean;
   private String _Name;
   private String _PrefixName;
   private JMSJDBCStore _customizer;
   private static SchemaHelper2 _schemaHelper;

   public JMSJDBCStoreMBeanImpl() {
      try {
         this._customizer = new JMSJDBCStore(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public JMSJDBCStoreMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new JMSJDBCStore(this);
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

   public String getPrefixName() {
      return this._customizer.getPrefixName();
   }

   public boolean isNameSet() {
      return this._isSet(2);
   }

   public boolean isPrefixNameSet() {
      return this._isSet(8);
   }

   public void setDelegatedBean(JDBCStoreMBean var1) {
      JDBCStoreMBean var2 = this.getDelegatedBean();
      this._customizer.setDelegatedBean(var1);
      this._postSet(10, var2, var1);
   }

   public JDBCStoreMBean getDelegatedBean() {
      return this._DelegatedBean;
   }

   public String getDelegatedBeanAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getDelegatedBean();
      return var1 == null ? null : var1._getKey().toString();
   }

   public boolean isDelegatedBeanSet() {
      return this._isSet(10);
   }

   public void setDelegatedBeanAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, JDBCStoreMBean.class, new ReferenceManager.Resolver(this, 10) {
            public void resolveReference(Object var1) {
               try {
                  JMSJDBCStoreMBeanImpl.this.setDelegatedBean((JDBCStoreMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         JDBCStoreMBean var2 = this._DelegatedBean;
         this._initializeProperty(10);
         this._postSet(10, var2, this._DelegatedBean);
      }

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

   public void setPrefixName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      JMSLegalHelper.validateJDBCPrefix(var1);
      String var2 = this.getPrefixName();
      this._customizer.setPrefixName(var1);
      this._postSet(8, var2, var1);
   }

   public JDBCConnectionPoolMBean getConnectionPool() {
      return this._customizer.getConnectionPool();
   }

   public String getConnectionPoolAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getConnectionPool();
      return var1 == null ? null : var1._getKey().toString();
   }

   public String getCreateTableDDLFile() {
      return this._customizer.getCreateTableDDLFile();
   }

   public boolean isConnectionPoolSet() {
      return this._isSet(11);
   }

   public boolean isCreateTableDDLFileSet() {
      return this._isSet(9);
   }

   public void setConnectionPoolAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, JDBCConnectionPoolMBean.class, new ReferenceManager.Resolver(this, 11) {
            public void resolveReference(Object var1) {
               try {
                  JMSJDBCStoreMBeanImpl.this.setConnectionPool((JDBCConnectionPoolMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         JDBCConnectionPoolMBean var2 = this._ConnectionPool;
         this._initializeProperty(11);
         this._postSet(11, var2, this._ConnectionPool);
      }

   }

   public void setConnectionPool(JDBCConnectionPoolMBean var1) throws InvalidAttributeValueException {
      JDBCConnectionPoolMBean var2 = this.getConnectionPool();
      this._customizer.setConnectionPool(var1);
      this._postSet(11, var2, var1);
   }

   public void setCreateTableDDLFile(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this.getCreateTableDDLFile();
      this._customizer.setCreateTableDDLFile(var1);
      this._postSet(9, var2, var1);
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
      return super._isAnythingSet();
   }

   private boolean _initializeProperty(int var1) {
      boolean var2 = var1 > -1;
      if (!var2) {
         var1 = 11;
      }

      try {
         switch (var1) {
            case 11:
               this._customizer.setConnectionPool((JDBCConnectionPoolMBean)null);
               if (var2) {
                  break;
               }
            case 9:
               this._customizer.setCreateTableDDLFile((String)null);
               if (var2) {
                  break;
               }
            case 10:
               this._customizer.setDelegatedBean((JDBCStoreMBean)null);
               if (var2) {
                  break;
               }
            case 2:
               this._customizer.setName((String)null);
               if (var2) {
                  break;
               }
            case 8:
               this._customizer.setPrefixName((String)null);
               if (var2) {
                  break;
               }
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
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
      return "JMSJDBCStore";
   }

   public void putValue(String var1, Object var2) {
      if (var1.equals("ConnectionPool")) {
         JDBCConnectionPoolMBean var6 = this._ConnectionPool;
         this._ConnectionPool = (JDBCConnectionPoolMBean)var2;
         this._postSet(11, var6, this._ConnectionPool);
      } else {
         String var4;
         if (var1.equals("CreateTableDDLFile")) {
            var4 = this._CreateTableDDLFile;
            this._CreateTableDDLFile = (String)var2;
            this._postSet(9, var4, this._CreateTableDDLFile);
         } else if (var1.equals("DelegatedBean")) {
            JDBCStoreMBean var5 = this._DelegatedBean;
            this._DelegatedBean = (JDBCStoreMBean)var2;
            this._postSet(10, var5, this._DelegatedBean);
         } else if (var1.equals("Name")) {
            var4 = this._Name;
            this._Name = (String)var2;
            this._postSet(2, var4, this._Name);
         } else if (var1.equals("PrefixName")) {
            var4 = this._PrefixName;
            this._PrefixName = (String)var2;
            this._postSet(8, var4, this._PrefixName);
         } else if (var1.equals("customizer")) {
            JMSJDBCStore var3 = this._customizer;
            this._customizer = (JMSJDBCStore)var2;
         } else {
            super.putValue(var1, var2);
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("ConnectionPool")) {
         return this._ConnectionPool;
      } else if (var1.equals("CreateTableDDLFile")) {
         return this._CreateTableDDLFile;
      } else if (var1.equals("DelegatedBean")) {
         return this._DelegatedBean;
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("PrefixName")) {
         return this._PrefixName;
      } else {
         return var1.equals("customizer") ? this._customizer : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends JMSStoreMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 4:
               if (var1.equals("name")) {
                  return 2;
               }
               break;
            case 11:
               if (var1.equals("prefix-name")) {
                  return 8;
               }
               break;
            case 14:
               if (var1.equals("delegated-bean")) {
                  return 10;
               }
               break;
            case 15:
               if (var1.equals("connection-pool")) {
                  return 11;
               }
               break;
            case 20:
               if (var1.equals("create-tableddl-file")) {
                  return 9;
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
            default:
               return super.getElementName(var1);
            case 8:
               return "prefix-name";
            case 9:
               return "create-tableddl-file";
            case 10:
               return "delegated-bean";
            case 11:
               return "connection-pool";
         }
      }

      public boolean isBean(int var1) {
         switch (var1) {
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

   protected static class Helper extends JMSStoreMBeanImpl.Helper {
      private JMSJDBCStoreMBeanImpl bean;

      protected Helper(JMSJDBCStoreMBeanImpl var1) {
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
            default:
               return super.getPropertyName(var1);
            case 8:
               return "PrefixName";
            case 9:
               return "CreateTableDDLFile";
            case 10:
               return "DelegatedBean";
            case 11:
               return "ConnectionPool";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("ConnectionPool")) {
            return 11;
         } else if (var1.equals("CreateTableDDLFile")) {
            return 9;
         } else if (var1.equals("DelegatedBean")) {
            return 10;
         } else if (var1.equals("Name")) {
            return 2;
         } else {
            return var1.equals("PrefixName") ? 8 : super.getPropertyIndex(var1);
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
            if (this.bean.isConnectionPoolSet()) {
               var2.append("ConnectionPool");
               var2.append(String.valueOf(this.bean.getConnectionPool()));
            }

            if (this.bean.isCreateTableDDLFileSet()) {
               var2.append("CreateTableDDLFile");
               var2.append(String.valueOf(this.bean.getCreateTableDDLFile()));
            }

            if (this.bean.isDelegatedBeanSet()) {
               var2.append("DelegatedBean");
               var2.append(String.valueOf(this.bean.getDelegatedBean()));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isPrefixNameSet()) {
               var2.append("PrefixName");
               var2.append(String.valueOf(this.bean.getPrefixName()));
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
            JMSJDBCStoreMBeanImpl var2 = (JMSJDBCStoreMBeanImpl)var1;
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("ConnectionPool", this.bean.getConnectionPool(), var2.getConnectionPool(), false);
            }

            this.computeDiff("CreateTableDDLFile", this.bean.getCreateTableDDLFile(), var2.getCreateTableDDLFile(), false);
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("DelegatedBean", this.bean.getDelegatedBean(), var2.getDelegatedBean(), false);
            }

            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeDiff("PrefixName", this.bean.getPrefixName(), var2.getPrefixName(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            JMSJDBCStoreMBeanImpl var3 = (JMSJDBCStoreMBeanImpl)var1.getSourceBean();
            JMSJDBCStoreMBeanImpl var4 = (JMSJDBCStoreMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("ConnectionPool")) {
                  var3.setConnectionPoolAsString(var4.getConnectionPoolAsString());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("CreateTableDDLFile")) {
                  var3.setCreateTableDDLFile(var4.getCreateTableDDLFile());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("DelegatedBean")) {
                  var3.setDelegatedBeanAsString(var4.getDelegatedBeanAsString());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("Name")) {
                  var3.setName(var4.getName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 2);
               } else if (var5.equals("PrefixName")) {
                  var3.setPrefixName(var4.getPrefixName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
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
            JMSJDBCStoreMBeanImpl var5 = (JMSJDBCStoreMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if (var2 && (var3 == null || !var3.contains("ConnectionPool")) && this.bean.isConnectionPoolSet()) {
               var5._unSet(var5, 11);
               var5.setConnectionPoolAsString(this.bean.getConnectionPoolAsString());
            }

            if ((var3 == null || !var3.contains("CreateTableDDLFile")) && this.bean.isCreateTableDDLFileSet()) {
               var5.setCreateTableDDLFile(this.bean.getCreateTableDDLFile());
            }

            if (var2 && (var3 == null || !var3.contains("DelegatedBean")) && this.bean.isDelegatedBeanSet()) {
               var5._unSet(var5, 10);
               var5.setDelegatedBeanAsString(this.bean.getDelegatedBeanAsString());
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if ((var3 == null || !var3.contains("PrefixName")) && this.bean.isPrefixNameSet()) {
               var5.setPrefixName(this.bean.getPrefixName());
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
         this.inferSubTree(this.bean.getConnectionPool(), var1, var2);
         this.inferSubTree(this.bean.getDelegatedBean(), var1, var2);
      }
   }
}
