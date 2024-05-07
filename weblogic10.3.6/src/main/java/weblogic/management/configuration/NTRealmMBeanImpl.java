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
import weblogic.management.mbeans.custom.NTRealm;
import weblogic.utils.collections.CombinedIterator;

public class NTRealmMBeanImpl extends BasicRealmMBeanImpl implements NTRealmMBean, Serializable {
   private boolean _IgnoreBadDomainName;
   private String _Name;
   private int _PreferredMaxBytes;
   private String _PrimaryDomain;
   private String _RealmClassName;
   private NTRealm _customizer;
   private static SchemaHelper2 _schemaHelper;

   public NTRealmMBeanImpl() {
      try {
         this._customizer = new NTRealm(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public NTRealmMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new NTRealm(this);
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

   public String getPrimaryDomain() {
      return this._PrimaryDomain;
   }

   public String getRealmClassName() {
      return this._customizer.getRealmClassName();
   }

   public boolean isNameSet() {
      return this._isSet(2);
   }

   public boolean isPrimaryDomainSet() {
      return this._isSet(8);
   }

   public boolean isRealmClassNameSet() {
      return this._isSet(7);
   }

   public void setRealmClassName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._RealmClassName;
      this._RealmClassName = var1;
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

   public void setPrimaryDomain(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._PrimaryDomain;
      this._PrimaryDomain = var1;
      this._postSet(8, var2, var1);
   }

   public int getPreferredMaxBytes() {
      return this._PreferredMaxBytes;
   }

   public boolean isPreferredMaxBytesSet() {
      return this._isSet(9);
   }

   public void setPreferredMaxBytes(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkMin("PreferredMaxBytes", var1, 0);
      int var2 = this._PreferredMaxBytes;
      this._PreferredMaxBytes = var1;
      this._postSet(9, var2, var1);
   }

   public boolean getIgnoreBadDomainName() {
      return this._IgnoreBadDomainName;
   }

   public boolean isIgnoreBadDomainNameSet() {
      return this._isSet(10);
   }

   public void setIgnoreBadDomainName(boolean var1) {
      boolean var2 = this._IgnoreBadDomainName;
      this._IgnoreBadDomainName = var1;
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
      return super._isAnythingSet();
   }

   private boolean _initializeProperty(int var1) {
      boolean var2 = var1 > -1;
      if (!var2) {
         var1 = 10;
      }

      try {
         switch (var1) {
            case 10:
               this._IgnoreBadDomainName = false;
               if (var2) {
                  break;
               }
            case 2:
               this._customizer.setName((String)null);
               if (var2) {
                  break;
               }
            case 9:
               this._PreferredMaxBytes = 10240;
               if (var2) {
                  break;
               }
            case 8:
               this._PrimaryDomain = null;
               if (var2) {
                  break;
               }
            case 7:
               this._RealmClassName = null;
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
      return "NTRealm";
   }

   public void putValue(String var1, Object var2) {
      if (var1.equals("IgnoreBadDomainName")) {
         boolean var6 = this._IgnoreBadDomainName;
         this._IgnoreBadDomainName = (Boolean)var2;
         this._postSet(10, var6, this._IgnoreBadDomainName);
      } else {
         String var4;
         if (var1.equals("Name")) {
            var4 = this._Name;
            this._Name = (String)var2;
            this._postSet(2, var4, this._Name);
         } else if (var1.equals("PreferredMaxBytes")) {
            int var5 = this._PreferredMaxBytes;
            this._PreferredMaxBytes = (Integer)var2;
            this._postSet(9, var5, this._PreferredMaxBytes);
         } else if (var1.equals("PrimaryDomain")) {
            var4 = this._PrimaryDomain;
            this._PrimaryDomain = (String)var2;
            this._postSet(8, var4, this._PrimaryDomain);
         } else if (var1.equals("RealmClassName")) {
            var4 = this._RealmClassName;
            this._RealmClassName = (String)var2;
            this._postSet(7, var4, this._RealmClassName);
         } else if (var1.equals("customizer")) {
            NTRealm var3 = this._customizer;
            this._customizer = (NTRealm)var2;
         } else {
            super.putValue(var1, var2);
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("IgnoreBadDomainName")) {
         return new Boolean(this._IgnoreBadDomainName);
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("PreferredMaxBytes")) {
         return new Integer(this._PreferredMaxBytes);
      } else if (var1.equals("PrimaryDomain")) {
         return this._PrimaryDomain;
      } else if (var1.equals("RealmClassName")) {
         return this._RealmClassName;
      } else {
         return var1.equals("customizer") ? this._customizer : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends BasicRealmMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 4:
               if (var1.equals("name")) {
                  return 2;
               }
               break;
            case 14:
               if (var1.equals("primary-domain")) {
                  return 8;
               }
               break;
            case 16:
               if (var1.equals("realm-class-name")) {
                  return 7;
               }
               break;
            case 19:
               if (var1.equals("preferred-max-bytes")) {
                  return 9;
               }
               break;
            case 22:
               if (var1.equals("ignore-bad-domain-name")) {
                  return 10;
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
               return "realm-class-name";
            case 8:
               return "primary-domain";
            case 9:
               return "preferred-max-bytes";
            case 10:
               return "ignore-bad-domain-name";
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

   protected static class Helper extends BasicRealmMBeanImpl.Helper {
      private NTRealmMBeanImpl bean;

      protected Helper(NTRealmMBeanImpl var1) {
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
               return "RealmClassName";
            case 8:
               return "PrimaryDomain";
            case 9:
               return "PreferredMaxBytes";
            case 10:
               return "IgnoreBadDomainName";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("IgnoreBadDomainName")) {
            return 10;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("PreferredMaxBytes")) {
            return 9;
         } else if (var1.equals("PrimaryDomain")) {
            return 8;
         } else {
            return var1.equals("RealmClassName") ? 7 : super.getPropertyIndex(var1);
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
            if (this.bean.isIgnoreBadDomainNameSet()) {
               var2.append("IgnoreBadDomainName");
               var2.append(String.valueOf(this.bean.getIgnoreBadDomainName()));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isPreferredMaxBytesSet()) {
               var2.append("PreferredMaxBytes");
               var2.append(String.valueOf(this.bean.getPreferredMaxBytes()));
            }

            if (this.bean.isPrimaryDomainSet()) {
               var2.append("PrimaryDomain");
               var2.append(String.valueOf(this.bean.getPrimaryDomain()));
            }

            if (this.bean.isRealmClassNameSet()) {
               var2.append("RealmClassName");
               var2.append(String.valueOf(this.bean.getRealmClassName()));
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
            NTRealmMBeanImpl var2 = (NTRealmMBeanImpl)var1;
            this.computeDiff("IgnoreBadDomainName", this.bean.getIgnoreBadDomainName(), var2.getIgnoreBadDomainName(), false);
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeDiff("PreferredMaxBytes", this.bean.getPreferredMaxBytes(), var2.getPreferredMaxBytes(), false);
            this.computeDiff("PrimaryDomain", this.bean.getPrimaryDomain(), var2.getPrimaryDomain(), false);
            this.computeDiff("RealmClassName", this.bean.getRealmClassName(), var2.getRealmClassName(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            NTRealmMBeanImpl var3 = (NTRealmMBeanImpl)var1.getSourceBean();
            NTRealmMBeanImpl var4 = (NTRealmMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("IgnoreBadDomainName")) {
                  var3.setIgnoreBadDomainName(var4.getIgnoreBadDomainName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("Name")) {
                  var3.setName(var4.getName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 2);
               } else if (var5.equals("PreferredMaxBytes")) {
                  var3.setPreferredMaxBytes(var4.getPreferredMaxBytes());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("PrimaryDomain")) {
                  var3.setPrimaryDomain(var4.getPrimaryDomain());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("RealmClassName")) {
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
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
            NTRealmMBeanImpl var5 = (NTRealmMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("IgnoreBadDomainName")) && this.bean.isIgnoreBadDomainNameSet()) {
               var5.setIgnoreBadDomainName(this.bean.getIgnoreBadDomainName());
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if ((var3 == null || !var3.contains("PreferredMaxBytes")) && this.bean.isPreferredMaxBytesSet()) {
               var5.setPreferredMaxBytes(this.bean.getPreferredMaxBytes());
            }

            if ((var3 == null || !var3.contains("PrimaryDomain")) && this.bean.isPrimaryDomainSet()) {
               var5.setPrimaryDomain(this.bean.getPrimaryDomain());
            }

            if ((var3 == null || !var3.contains("RealmClassName")) && this.bean.isRealmClassNameSet()) {
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
