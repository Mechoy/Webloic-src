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
import weblogic.coherence.descriptor.wl.WeblogicCoherenceBean;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.management.ManagementException;
import weblogic.management.mbeans.custom.CoherenceClusterSystemResource;
import weblogic.utils.collections.ArrayIterator;
import weblogic.utils.collections.CombinedIterator;

public class CoherenceClusterSystemResourceMBeanImpl extends SystemResourceMBeanImpl implements CoherenceClusterSystemResourceMBean, Serializable {
   private WeblogicCoherenceBean _CoherenceClusterResource;
   private String _CustomClusterConfigurationFileName;
   private String _DescriptorFileName;
   private String _Name;
   private DescriptorBean _Resource;
   private boolean _UsingCustomClusterConfigurationFile;
   private CoherenceClusterSystemResource _customizer;
   private static SchemaHelper2 _schemaHelper;

   public CoherenceClusterSystemResourceMBeanImpl() {
      try {
         this._customizer = new CoherenceClusterSystemResource(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public CoherenceClusterSystemResourceMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new CoherenceClusterSystemResource(this);
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
            return CoherenceClusterSystemResource.constructDefaultCoherenceSystemFilename(this.getName());
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

   public boolean isDescriptorFileNameSet() {
      return this._isSet(14);
   }

   public boolean isNameSet() {
      return this._isSet(2);
   }

   public String getCustomClusterConfigurationFileName() {
      return this._customizer.getCustomClusterConfigurationFileName();
   }

   public DescriptorBean getResource() {
      return this._customizer.getResource();
   }

   public boolean isCustomClusterConfigurationFileNameSet() {
      return this._isSet(16);
   }

   public boolean isResourceSet() {
      return this._isSet(15);
   }

   public void setCustomClusterConfigurationFileName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._CustomClusterConfigurationFileName;
      this._CustomClusterConfigurationFileName = var1;
      this._postSet(16, var2, var1);
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

   public boolean isUsingCustomClusterConfigurationFile() {
      return this._customizer.isUsingCustomClusterConfigurationFile();
   }

   public boolean isUsingCustomClusterConfigurationFileSet() {
      return this._isSet(17);
   }

   public void importCustomClusterConfigurationFile(String var1) throws ManagementException {
      this._customizer.importCustomClusterConfigurationFile(var1);
   }

   public WeblogicCoherenceBean getCoherenceClusterResource() {
      return this._customizer.getCoherenceClusterResource();
   }

   public boolean isCoherenceClusterResourceSet() {
      return this._isSet(18);
   }

   public void setCoherenceClusterResource(WeblogicCoherenceBean var1) throws InvalidAttributeValueException {
      this._CoherenceClusterResource = var1;
   }

   public void setUsingCustomClusterConfigurationFile(boolean var1) throws ManagementException {
      boolean var2 = this.isUsingCustomClusterConfigurationFile();
      this._customizer.setUsingCustomClusterConfigurationFile(var1);
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

   protected void _postCreate() {
      this._customizer._postCreate();
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
         var1 = 18;
      }

      try {
         switch (var1) {
            case 18:
               this._CoherenceClusterResource = null;
               if (var2) {
                  break;
               }
            case 16:
               this._CustomClusterConfigurationFileName = null;
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
            case 17:
               this._customizer.setUsingCustomClusterConfigurationFile(false);
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
      return "CoherenceClusterSystemResource";
   }

   public void putValue(String var1, Object var2) {
      if (var1.equals("CoherenceClusterResource")) {
         WeblogicCoherenceBean var7 = this._CoherenceClusterResource;
         this._CoherenceClusterResource = (WeblogicCoherenceBean)var2;
         this._postSet(18, var7, this._CoherenceClusterResource);
      } else {
         String var6;
         if (var1.equals("CustomClusterConfigurationFileName")) {
            var6 = this._CustomClusterConfigurationFileName;
            this._CustomClusterConfigurationFileName = (String)var2;
            this._postSet(16, var6, this._CustomClusterConfigurationFileName);
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
         } else if (var1.equals("UsingCustomClusterConfigurationFile")) {
            boolean var4 = this._UsingCustomClusterConfigurationFile;
            this._UsingCustomClusterConfigurationFile = (Boolean)var2;
            this._postSet(17, var4, this._UsingCustomClusterConfigurationFile);
         } else if (var1.equals("customizer")) {
            CoherenceClusterSystemResource var3 = this._customizer;
            this._customizer = (CoherenceClusterSystemResource)var2;
         } else {
            super.putValue(var1, var2);
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("CoherenceClusterResource")) {
         return this._CoherenceClusterResource;
      } else if (var1.equals("CustomClusterConfigurationFileName")) {
         return this._CustomClusterConfigurationFileName;
      } else if (var1.equals("DescriptorFileName")) {
         return this._DescriptorFileName;
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("Resource")) {
         return this._Resource;
      } else if (var1.equals("UsingCustomClusterConfigurationFile")) {
         return new Boolean(this._UsingCustomClusterConfigurationFile);
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
            case 20:
               if (var1.equals("descriptor-file-name")) {
                  return 14;
               }
               break;
            case 26:
               if (var1.equals("coherence-cluster-resource")) {
                  return 18;
               }
               break;
            case 38:
               if (var1.equals("custom-cluster-configuration-file-name")) {
                  return 16;
               }
               break;
            case 39:
               if (var1.equals("using-custom-cluster-configuration-file")) {
                  return 17;
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
            default:
               return super.getElementName(var1);
            case 14:
               return "descriptor-file-name";
            case 15:
               return "resource";
            case 16:
               return "custom-cluster-configuration-file-name";
            case 17:
               return "using-custom-cluster-configuration-file";
            case 18:
               return "coherence-cluster-resource";
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
      private CoherenceClusterSystemResourceMBeanImpl bean;

      protected Helper(CoherenceClusterSystemResourceMBeanImpl var1) {
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
            default:
               return super.getPropertyName(var1);
            case 14:
               return "DescriptorFileName";
            case 15:
               return "Resource";
            case 16:
               return "CustomClusterConfigurationFileName";
            case 17:
               return "UsingCustomClusterConfigurationFile";
            case 18:
               return "CoherenceClusterResource";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("CoherenceClusterResource")) {
            return 18;
         } else if (var1.equals("CustomClusterConfigurationFileName")) {
            return 16;
         } else if (var1.equals("DescriptorFileName")) {
            return 14;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("Resource")) {
            return 15;
         } else {
            return var1.equals("UsingCustomClusterConfigurationFile") ? 17 : super.getPropertyIndex(var1);
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
            if (this.bean.isCoherenceClusterResourceSet()) {
               var2.append("CoherenceClusterResource");
               var2.append(String.valueOf(this.bean.getCoherenceClusterResource()));
            }

            if (this.bean.isCustomClusterConfigurationFileNameSet()) {
               var2.append("CustomClusterConfigurationFileName");
               var2.append(String.valueOf(this.bean.getCustomClusterConfigurationFileName()));
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

            if (this.bean.isUsingCustomClusterConfigurationFileSet()) {
               var2.append("UsingCustomClusterConfigurationFile");
               var2.append(String.valueOf(this.bean.isUsingCustomClusterConfigurationFile()));
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
            CoherenceClusterSystemResourceMBeanImpl var2 = (CoherenceClusterSystemResourceMBeanImpl)var1;
            this.computeDiff("CustomClusterConfigurationFileName", this.bean.getCustomClusterConfigurationFileName(), var2.getCustomClusterConfigurationFileName(), false);
            this.computeDiff("DescriptorFileName", this.bean.getDescriptorFileName(), var2.getDescriptorFileName(), false);
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeDiff("UsingCustomClusterConfigurationFile", this.bean.isUsingCustomClusterConfigurationFile(), var2.isUsingCustomClusterConfigurationFile(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            CoherenceClusterSystemResourceMBeanImpl var3 = (CoherenceClusterSystemResourceMBeanImpl)var1.getSourceBean();
            CoherenceClusterSystemResourceMBeanImpl var4 = (CoherenceClusterSystemResourceMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (!var5.equals("CoherenceClusterResource")) {
                  if (var5.equals("CustomClusterConfigurationFileName")) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 16);
                  } else if (var5.equals("DescriptorFileName")) {
                     var3.setDescriptorFileName(var4.getDescriptorFileName());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 14);
                  } else if (var5.equals("Name")) {
                     var3.setName(var4.getName());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 2);
                  } else if (!var5.equals("Resource")) {
                     if (var5.equals("UsingCustomClusterConfigurationFile")) {
                        var3.setUsingCustomClusterConfigurationFile(var4.isUsingCustomClusterConfigurationFile());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 17);
                     } else {
                        super.applyPropertyUpdate(var1, var2);
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
            CoherenceClusterSystemResourceMBeanImpl var5 = (CoherenceClusterSystemResourceMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("CustomClusterConfigurationFileName")) && this.bean.isCustomClusterConfigurationFileNameSet()) {
            }

            if ((var3 == null || !var3.contains("DescriptorFileName")) && this.bean.isDescriptorFileNameSet()) {
               var5.setDescriptorFileName(this.bean.getDescriptorFileName());
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if ((var3 == null || !var3.contains("UsingCustomClusterConfigurationFile")) && this.bean.isUsingCustomClusterConfigurationFileSet()) {
               var5.setUsingCustomClusterConfigurationFile(this.bean.isUsingCustomClusterConfigurationFile());
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
