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
import weblogic.diagnostics.descriptor.WLDFResourceBean;
import weblogic.management.ManagementException;
import weblogic.management.mbeans.custom.WLDFSystemResource;
import weblogic.utils.FileUtils;
import weblogic.utils.collections.ArrayIterator;
import weblogic.utils.collections.CombinedIterator;

public class WLDFSystemResourceMBeanImpl extends SystemResourceMBeanImpl implements WLDFSystemResourceMBean, Serializable {
   private String _Description;
   private String _DescriptorFileName;
   private String _Name;
   private DescriptorBean _Resource;
   private WLDFResourceBean _WLDFResource;
   private WLDFSystemResource _customizer;
   private static SchemaHelper2 _schemaHelper;

   public WLDFSystemResourceMBeanImpl() {
      try {
         this._customizer = new WLDFSystemResource(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public WLDFSystemResourceMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new WLDFSystemResource(this);
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         }

         throw new UndeclaredThrowableException(var4);
      }

      this._initializeProperty(-1);
   }

   public String getDescriptorFileName() {
      if (!this._isSet(14)) {
         try {
            return "diagnostics/" + FileUtils.mapNameToFileName(this.getName()) + ".xml";
         } catch (NullPointerException var2) {
         }
      }

      return this._customizer.getDescriptorFileName();
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

   public WLDFResourceBean getWLDFResource() {
      return this._customizer.getWLDFResource();
   }

   public boolean isDescriptorFileNameSet() {
      return this._isSet(14);
   }

   public boolean isNameSet() {
      return this._isSet(2);
   }

   public boolean isWLDFResourceSet() {
      return this._isSet(16);
   }

   public void setWLDFResource(WLDFResourceBean var1) throws InvalidAttributeValueException {
      this._WLDFResource = var1;
   }

   public DescriptorBean getResource() {
      return this._customizer.getResource();
   }

   public boolean isResourceSet() {
      return this._isSet(15);
   }

   public void setDescriptorFileName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this.getDescriptorFileName();
      this._customizer.setDescriptorFileName(var1);
      this._postSet(14, var2, var1);
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

   public void setResource(DescriptorBean var1) throws InvalidAttributeValueException {
      this._Resource = var1;
   }

   public String getDescription() {
      return this._Description;
   }

   public boolean isDescriptionSet() {
      return this._isSet(17);
   }

   public void setDescription(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._Description;
      this._Description = var1;
      this._postSet(17, var2, var1);
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

   protected void _preDestroy() {
      this._customizer._preDestroy();
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
         var1 = 17;
      }

      try {
         switch (var1) {
            case 17:
               this._Description = null;
               if (var2) {
                  break;
               }
            case 14:
               this._customizer.setDescriptorFileName((String)null);
               if (var2) {
                  break;
               }
            case 2:
               this._customizer.setName((String)null);
               if (var2) {
                  break;
               }
            case 15:
               this._Resource = null;
               if (var2) {
                  break;
               }
            case 16:
               this._WLDFResource = null;
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
      return "WLDFSystemResource";
   }

   public void putValue(String var1, Object var2) {
      String var6;
      if (var1.equals("Description")) {
         var6 = this._Description;
         this._Description = (String)var2;
         this._postSet(17, var6, this._Description);
      } else if (var1.equals("DescriptorFileName")) {
         var6 = this._DescriptorFileName;
         this._DescriptorFileName = (String)var2;
         this._postSet(14, var6, this._DescriptorFileName);
      } else if (var1.equals("Name")) {
         var6 = this._Name;
         this._Name = (String)var2;
         this._postSet(2, var6, this._Name);
      } else if (var1.equals("Resource")) {
         DescriptorBean var5 = this._Resource;
         this._Resource = (DescriptorBean)var2;
         this._postSet(15, var5, this._Resource);
      } else if (var1.equals("WLDFResource")) {
         WLDFResourceBean var4 = this._WLDFResource;
         this._WLDFResource = (WLDFResourceBean)var2;
         this._postSet(16, var4, this._WLDFResource);
      } else if (var1.equals("customizer")) {
         WLDFSystemResource var3 = this._customizer;
         this._customizer = (WLDFSystemResource)var2;
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("Description")) {
         return this._Description;
      } else if (var1.equals("DescriptorFileName")) {
         return this._DescriptorFileName;
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("Resource")) {
         return this._Resource;
      } else if (var1.equals("WLDFResource")) {
         return this._WLDFResource;
      } else {
         return var1.equals("customizer") ? this._customizer : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends SystemResourceMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 4:
               if (var1.equals("name")) {
                  return 2;
               }
               break;
            case 8:
               if (var1.equals("resource")) {
                  return 15;
               }
               break;
            case 11:
               if (var1.equals("description")) {
                  return 17;
               }
               break;
            case 13:
               if (var1.equals("wldf-resource")) {
                  return 16;
               }
               break;
            case 20:
               if (var1.equals("descriptor-file-name")) {
                  return 14;
               }
         }

         return super.getPropertyIndex(var1);
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            case 11:
               return new SubDeploymentMBeanImpl.SchemaHelper2();
            default:
               return super.getSchemaHelper(var1);
         }
      }

      public String getElementName(int var1) {
         switch (var1) {
            case 2:
               return "name";
            case 14:
               return "descriptor-file-name";
            case 15:
               return "resource";
            case 16:
               return "wldf-resource";
            case 17:
               return "description";
            default:
               return super.getElementName(var1);
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 7:
               return true;
            case 11:
               return true;
            default:
               return super.isArray(var1);
         }
      }

      public boolean isBean(int var1) {
         switch (var1) {
            case 11:
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

   protected static class Helper extends SystemResourceMBeanImpl.Helper {
      private WLDFSystemResourceMBeanImpl bean;

      protected Helper(WLDFSystemResourceMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 2:
               return "Name";
            case 14:
               return "DescriptorFileName";
            case 15:
               return "Resource";
            case 16:
               return "WLDFResource";
            case 17:
               return "Description";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("Description")) {
            return 17;
         } else if (var1.equals("DescriptorFileName")) {
            return 14;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("Resource")) {
            return 15;
         } else {
            return var1.equals("WLDFResource") ? 16 : super.getPropertyIndex(var1);
         }
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         var1.add(new ArrayIterator(this.bean.getSubDeployments()));
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
            if (this.bean.isDescriptionSet()) {
               var2.append("Description");
               var2.append(String.valueOf(this.bean.getDescription()));
            }

            if (this.bean.isDescriptorFileNameSet()) {
               var2.append("DescriptorFileName");
               var2.append(String.valueOf(this.bean.getDescriptorFileName()));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isResourceSet()) {
               var2.append("Resource");
               var2.append(String.valueOf(this.bean.getResource()));
            }

            if (this.bean.isWLDFResourceSet()) {
               var2.append("WLDFResource");
               var2.append(String.valueOf(this.bean.getWLDFResource()));
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
            WLDFSystemResourceMBeanImpl var2 = (WLDFSystemResourceMBeanImpl)var1;
            this.computeDiff("Description", this.bean.getDescription(), var2.getDescription(), true);
            this.computeDiff("DescriptorFileName", this.bean.getDescriptorFileName(), var2.getDescriptorFileName(), false);
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            WLDFSystemResourceMBeanImpl var3 = (WLDFSystemResourceMBeanImpl)var1.getSourceBean();
            WLDFSystemResourceMBeanImpl var4 = (WLDFSystemResourceMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("Description")) {
                  var3.setDescription(var4.getDescription());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 17);
               } else if (var5.equals("DescriptorFileName")) {
                  var3.setDescriptorFileName(var4.getDescriptorFileName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 14);
               } else if (var5.equals("Name")) {
                  var3.setName(var4.getName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 2);
               } else if (!var5.equals("Resource") && !var5.equals("WLDFResource")) {
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
            WLDFSystemResourceMBeanImpl var5 = (WLDFSystemResourceMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("Description")) && this.bean.isDescriptionSet()) {
               var5.setDescription(this.bean.getDescription());
            }

            if ((var3 == null || !var3.contains("DescriptorFileName")) && this.bean.isDescriptorFileNameSet()) {
               var5.setDescriptorFileName(this.bean.getDescriptorFileName());
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
         this.inferSubTree(this.bean.getResource(), var1, var2);
      }
   }
}
