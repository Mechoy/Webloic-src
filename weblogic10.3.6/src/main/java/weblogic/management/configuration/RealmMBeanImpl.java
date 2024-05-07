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
import weblogic.descriptor.internal.ReferenceManager;
import weblogic.descriptor.internal.ResolvedReference;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.management.DistributedManagementException;
import weblogic.management.ManagementException;
import weblogic.management.mbeans.custom.Realm;
import weblogic.utils.collections.CombinedIterator;

public class RealmMBeanImpl extends ConfigurationMBeanImpl implements RealmMBean, Serializable {
   private CachingRealmMBean _CachingRealm;
   private boolean _EnumerationAllowed;
   private FileRealmMBean _FileRealm;
   private String _Name;
   private int _ResultsBatchSize;
   private Realm _customizer;
   private static SchemaHelper2 _schemaHelper;

   public RealmMBeanImpl() {
      try {
         this._customizer = new Realm(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public RealmMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new Realm(this);
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         }

         throw new UndeclaredThrowableException(var4);
      }

      this._initializeProperty(-1);
   }

   public CachingRealmMBean getCachingRealm() {
      return this._CachingRealm;
   }

   public String getCachingRealmAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getCachingRealm();
      return var1 == null ? null : var1._getKey().toString();
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

   public boolean isCachingRealmSet() {
      return this._isSet(7);
   }

   public boolean isNameSet() {
      return this._isSet(2);
   }

   public void setCachingRealmAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, CachingRealmMBean.class, new ReferenceManager.Resolver(this, 7) {
            public void resolveReference(Object var1) {
               try {
                  RealmMBeanImpl.this.setCachingRealm((CachingRealmMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         CachingRealmMBean var2 = this._CachingRealm;
         this._initializeProperty(7);
         this._postSet(7, var2, this._CachingRealm);
      }

   }

   public void setCachingRealm(CachingRealmMBean var1) throws InvalidAttributeValueException {
      if (var1 != null) {
         ResolvedReference var2 = new ResolvedReference(this, 7, (AbstractDescriptorBean)var1) {
            protected Object getPropertyValue() {
               return RealmMBeanImpl.this.getCachingRealm();
            }
         };
         this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1, var2);
      }

      CachingRealmMBean var3 = this._CachingRealm;
      this._CachingRealm = var1;
      this._postSet(7, var3, var1);
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

   public FileRealmMBean getFileRealm() {
      return this._FileRealm;
   }

   public String getFileRealmAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getFileRealm();
      return var1 == null ? null : var1._getKey().toString();
   }

   public boolean isFileRealmSet() {
      return this._isSet(8);
   }

   public void setFileRealmAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, FileRealmMBean.class, new ReferenceManager.Resolver(this, 8) {
            public void resolveReference(Object var1) {
               try {
                  RealmMBeanImpl.this.setFileRealm((FileRealmMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         FileRealmMBean var2 = this._FileRealm;
         this._initializeProperty(8);
         this._postSet(8, var2, this._FileRealm);
      }

   }

   public void setFileRealm(FileRealmMBean var1) throws InvalidAttributeValueException {
      if (var1 != null) {
         ResolvedReference var2 = new ResolvedReference(this, 8, (AbstractDescriptorBean)var1) {
            protected Object getPropertyValue() {
               return RealmMBeanImpl.this.getFileRealm();
            }
         };
         this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1, var2);
      }

      FileRealmMBean var3 = this._FileRealm;
      this._FileRealm = var1;
      this._postSet(8, var3, var1);
   }

   public void refresh() throws RealmException, DistributedManagementException {
      this._customizer.refresh();
   }

   public RealmManager manager() {
      return this._customizer.manager();
   }

   public int getResultsBatchSize() {
      return this._ResultsBatchSize;
   }

   public boolean isResultsBatchSizeSet() {
      return this._isSet(9);
   }

   public void setResultsBatchSize(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkMin("ResultsBatchSize", var1, 0);
      int var2 = this._ResultsBatchSize;
      this._ResultsBatchSize = var1;
      this._postSet(9, var2, var1);
   }

   public boolean isEnumerationAllowed() {
      return this._EnumerationAllowed;
   }

   public boolean isEnumerationAllowedSet() {
      return this._isSet(10);
   }

   public void setEnumerationAllowed(boolean var1) {
      boolean var2 = this._EnumerationAllowed;
      this._EnumerationAllowed = var1;
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
         var1 = 7;
      }

      try {
         switch (var1) {
            case 7:
               this._CachingRealm = null;
               if (var2) {
                  break;
               }
            case 8:
               this._FileRealm = null;
               if (var2) {
                  break;
               }
            case 2:
               this._customizer.setName((String)null);
               if (var2) {
                  break;
               }
            case 9:
               this._ResultsBatchSize = 200;
               if (var2) {
                  break;
               }
            case 10:
               this._EnumerationAllowed = true;
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
      return "Realm";
   }

   public void putValue(String var1, Object var2) {
      if (var1.equals("CachingRealm")) {
         CachingRealmMBean var8 = this._CachingRealm;
         this._CachingRealm = (CachingRealmMBean)var2;
         this._postSet(7, var8, this._CachingRealm);
      } else if (var1.equals("EnumerationAllowed")) {
         boolean var7 = this._EnumerationAllowed;
         this._EnumerationAllowed = (Boolean)var2;
         this._postSet(10, var7, this._EnumerationAllowed);
      } else if (var1.equals("FileRealm")) {
         FileRealmMBean var6 = this._FileRealm;
         this._FileRealm = (FileRealmMBean)var2;
         this._postSet(8, var6, this._FileRealm);
      } else if (var1.equals("Name")) {
         String var5 = this._Name;
         this._Name = (String)var2;
         this._postSet(2, var5, this._Name);
      } else if (var1.equals("ResultsBatchSize")) {
         int var4 = this._ResultsBatchSize;
         this._ResultsBatchSize = (Integer)var2;
         this._postSet(9, var4, this._ResultsBatchSize);
      } else if (var1.equals("customizer")) {
         Realm var3 = this._customizer;
         this._customizer = (Realm)var2;
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("CachingRealm")) {
         return this._CachingRealm;
      } else if (var1.equals("EnumerationAllowed")) {
         return new Boolean(this._EnumerationAllowed);
      } else if (var1.equals("FileRealm")) {
         return this._FileRealm;
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("ResultsBatchSize")) {
         return new Integer(this._ResultsBatchSize);
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
               break;
            case 10:
               if (var1.equals("file-realm")) {
                  return 8;
               }
               break;
            case 13:
               if (var1.equals("caching-realm")) {
                  return 7;
               }
               break;
            case 18:
               if (var1.equals("results-batch-size")) {
                  return 9;
               }
               break;
            case 19:
               if (var1.equals("enumeration-allowed")) {
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
               return "caching-realm";
            case 8:
               return "file-realm";
            case 9:
               return "results-batch-size";
            case 10:
               return "enumeration-allowed";
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

   protected static class Helper extends ConfigurationMBeanImpl.Helper {
      private RealmMBeanImpl bean;

      protected Helper(RealmMBeanImpl var1) {
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
               return "CachingRealm";
            case 8:
               return "FileRealm";
            case 9:
               return "ResultsBatchSize";
            case 10:
               return "EnumerationAllowed";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("CachingRealm")) {
            return 7;
         } else if (var1.equals("FileRealm")) {
            return 8;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("ResultsBatchSize")) {
            return 9;
         } else {
            return var1.equals("EnumerationAllowed") ? 10 : super.getPropertyIndex(var1);
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
            if (this.bean.isCachingRealmSet()) {
               var2.append("CachingRealm");
               var2.append(String.valueOf(this.bean.getCachingRealm()));
            }

            if (this.bean.isFileRealmSet()) {
               var2.append("FileRealm");
               var2.append(String.valueOf(this.bean.getFileRealm()));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isResultsBatchSizeSet()) {
               var2.append("ResultsBatchSize");
               var2.append(String.valueOf(this.bean.getResultsBatchSize()));
            }

            if (this.bean.isEnumerationAllowedSet()) {
               var2.append("EnumerationAllowed");
               var2.append(String.valueOf(this.bean.isEnumerationAllowed()));
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
            RealmMBeanImpl var2 = (RealmMBeanImpl)var1;
            this.computeDiff("CachingRealm", this.bean.getCachingRealm(), var2.getCachingRealm(), false);
            this.computeDiff("FileRealm", this.bean.getFileRealm(), var2.getFileRealm(), false);
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeDiff("ResultsBatchSize", this.bean.getResultsBatchSize(), var2.getResultsBatchSize(), false);
            this.computeDiff("EnumerationAllowed", this.bean.isEnumerationAllowed(), var2.isEnumerationAllowed(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            RealmMBeanImpl var3 = (RealmMBeanImpl)var1.getSourceBean();
            RealmMBeanImpl var4 = (RealmMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("CachingRealm")) {
                  var3.setCachingRealmAsString(var4.getCachingRealmAsString());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("FileRealm")) {
                  var3.setFileRealmAsString(var4.getFileRealmAsString());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("Name")) {
                  var3.setName(var4.getName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 2);
               } else if (var5.equals("ResultsBatchSize")) {
                  var3.setResultsBatchSize(var4.getResultsBatchSize());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("EnumerationAllowed")) {
                  var3.setEnumerationAllowed(var4.isEnumerationAllowed());
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
            RealmMBeanImpl var5 = (RealmMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("CachingRealm")) && this.bean.isCachingRealmSet()) {
               var5._unSet(var5, 7);
               var5.setCachingRealmAsString(this.bean.getCachingRealmAsString());
            }

            if ((var3 == null || !var3.contains("FileRealm")) && this.bean.isFileRealmSet()) {
               var5._unSet(var5, 8);
               var5.setFileRealmAsString(this.bean.getFileRealmAsString());
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if ((var3 == null || !var3.contains("ResultsBatchSize")) && this.bean.isResultsBatchSizeSet()) {
               var5.setResultsBatchSize(this.bean.getResultsBatchSize());
            }

            if ((var3 == null || !var3.contains("EnumerationAllowed")) && this.bean.isEnumerationAllowedSet()) {
               var5.setEnumerationAllowed(this.bean.isEnumerationAllowed());
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
         this.inferSubTree(this.bean.getCachingRealm(), var1, var2);
         this.inferSubTree(this.bean.getFileRealm(), var1, var2);
      }
   }
}
