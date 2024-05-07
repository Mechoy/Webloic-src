package weblogic.management.security;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.AbstractSchemaHelper2;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.management.commo.AbstractCommoConfigurationBean;
import weblogic.management.commo.RequiredModelMBeanWrapper;
import weblogic.utils.collections.CombinedIterator;

public class ProviderMBeanImpl extends AbstractCommoConfigurationBean implements ProviderMBean, Serializable {
   private String _CompatibilityObjectName;
   private String _Description;
   private String _Name;
   private String _ProviderClassName;
   private RealmMBean _Realm;
   private String _Version;
   private ProviderImpl _customizer;
   private static SchemaHelper2 _schemaHelper;

   public ProviderMBeanImpl() {
      try {
         this._customizer = new ProviderImpl(new RequiredModelMBeanWrapper(this));
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public ProviderMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new ProviderImpl(new RequiredModelMBeanWrapper(this));
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         }

         throw new UndeclaredThrowableException(var4);
      }

      this._initializeProperty(-1);
   }

   public String getProviderClassName() {
      return this._ProviderClassName;
   }

   public boolean isProviderClassNameSet() {
      return this._isSet(2);
   }

   public void setProviderClassName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this._ProviderClassName = var1;
   }

   public String getDescription() {
      return this._Description;
   }

   public boolean isDescriptionSet() {
      return this._isSet(3);
   }

   public void setDescription(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this._Description = var1;
   }

   public String getVersion() {
      return this._Version;
   }

   public boolean isVersionSet() {
      return this._isSet(4);
   }

   public void setVersion(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this._Version = var1;
   }

   public RealmMBean getRealm() {
      return this._customizer.getRealm();
   }

   public boolean isRealmSet() {
      return this._isSet(5);
   }

   public void setRealm(RealmMBean var1) throws InvalidAttributeValueException {
      this._Realm = var1;
   }

   public String getName() {
      return this._Name;
   }

   public boolean isNameSet() {
      return this._isSet(6);
   }

   public void setName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._Name;
      this._Name = var1;
      this._postSet(6, var2, var1);
   }

   public String getCompatibilityObjectName() {
      return this._customizer.getCompatibilityObjectName();
   }

   public boolean isCompatibilityObjectNameSet() {
      return this._isSet(7);
   }

   public void setCompatibilityObjectName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._CompatibilityObjectName;
      this._CompatibilityObjectName = var1;
      this._postSet(7, var2, var1);
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
         var1 = 7;
      }

      try {
         switch (var1) {
            case 7:
               this._CompatibilityObjectName = null;
               if (var2) {
                  break;
               }
            case 3:
               this._Description = null;
               if (var2) {
                  break;
               }
            case 6:
               this._Name = "Provider";
               if (var2) {
                  break;
               }
            case 2:
               this._ProviderClassName = null;
               if (var2) {
                  break;
               }
            case 5:
               this._Realm = null;
               if (var2) {
                  break;
               }
            case 4:
               this._Version = null;
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
      return "http://xmlns.oracle.com/weblogic/1.0/security.xsd";
   }

   protected String getTargetNamespace() {
      return "http://xmlns.oracle.com/weblogic/security";
   }

   public SchemaHelper _getSchemaHelper2() {
      if (_schemaHelper == null) {
         _schemaHelper = new SchemaHelper2();
      }

      return _schemaHelper;
   }

   public String wls_getInterfaceClassName() {
      return "weblogic.management.security.ProviderMBean";
   }

   public static class SchemaHelper2 extends AbstractSchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 4:
               if (var1.equals("name")) {
                  return 6;
               }
               break;
            case 5:
               if (var1.equals("realm")) {
                  return 5;
               }
               break;
            case 7:
               if (var1.equals("version")) {
                  return 4;
               }
               break;
            case 11:
               if (var1.equals("description")) {
                  return 3;
               }
               break;
            case 19:
               if (var1.equals("provider-class-name")) {
                  return 2;
               }
               break;
            case 25:
               if (var1.equals("compatibility-object-name")) {
                  return 7;
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
               return "provider-class-name";
            case 3:
               return "description";
            case 4:
               return "version";
            case 5:
               return "realm";
            case 6:
               return "name";
            case 7:
               return "compatibility-object-name";
            default:
               return super.getElementName(var1);
         }
      }

      public boolean isBean(int var1) {
         switch (var1) {
            default:
               return super.isBean(var1);
         }
      }
   }

   protected static class Helper extends AbstractCommoConfigurationBean.Helper {
      private ProviderMBeanImpl bean;

      protected Helper(ProviderMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 2:
               return "ProviderClassName";
            case 3:
               return "Description";
            case 4:
               return "Version";
            case 5:
               return "Realm";
            case 6:
               return "Name";
            case 7:
               return "CompatibilityObjectName";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("CompatibilityObjectName")) {
            return 7;
         } else if (var1.equals("Description")) {
            return 3;
         } else if (var1.equals("Name")) {
            return 6;
         } else if (var1.equals("ProviderClassName")) {
            return 2;
         } else if (var1.equals("Realm")) {
            return 5;
         } else {
            return var1.equals("Version") ? 4 : super.getPropertyIndex(var1);
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
            if (this.bean.isCompatibilityObjectNameSet()) {
               var2.append("CompatibilityObjectName");
               var2.append(String.valueOf(this.bean.getCompatibilityObjectName()));
            }

            if (this.bean.isDescriptionSet()) {
               var2.append("Description");
               var2.append(String.valueOf(this.bean.getDescription()));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isProviderClassNameSet()) {
               var2.append("ProviderClassName");
               var2.append(String.valueOf(this.bean.getProviderClassName()));
            }

            if (this.bean.isRealmSet()) {
               var2.append("Realm");
               var2.append(String.valueOf(this.bean.getRealm()));
            }

            if (this.bean.isVersionSet()) {
               var2.append("Version");
               var2.append(String.valueOf(this.bean.getVersion()));
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
            ProviderMBeanImpl var2 = (ProviderMBeanImpl)var1;
            this.computeDiff("CompatibilityObjectName", this.bean.getCompatibilityObjectName(), var2.getCompatibilityObjectName(), false);
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            ProviderMBeanImpl var3 = (ProviderMBeanImpl)var1.getSourceBean();
            ProviderMBeanImpl var4 = (ProviderMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("CompatibilityObjectName")) {
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (!var5.equals("Description")) {
                  if (var5.equals("Name")) {
                     var3.setName(var4.getName());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 6);
                  } else if (!var5.equals("ProviderClassName") && !var5.equals("Realm") && !var5.equals("Version")) {
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
            ProviderMBeanImpl var5 = (ProviderMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("CompatibilityObjectName")) && this.bean.isCompatibilityObjectNameSet()) {
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
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
         this.inferSubTree(this.bean.getRealm(), var1, var2);
      }
   }
}
