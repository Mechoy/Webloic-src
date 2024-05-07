package weblogic.management.configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.utils.collections.CombinedIterator;

public class EJBContainerMBeanImpl extends ConfigurationMBeanImpl implements EJBContainerMBean, Serializable {
   private String _ExtraEjbcOptions;
   private String _ExtraRmicOptions;
   private boolean _ForceGeneration;
   private String _JavaCompiler;
   private String _JavaCompilerPostClassPath;
   private String _JavaCompilerPreClassPath;
   private boolean _KeepGenerated;
   private String _TmpPath;
   private String _VerboseEJBDeploymentEnabled;
   private static SchemaHelper2 _schemaHelper;

   public EJBContainerMBeanImpl() {
      this._initializeProperty(-1);
   }

   public EJBContainerMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public String getJavaCompiler() {
      return this._JavaCompiler;
   }

   public boolean isJavaCompilerSet() {
      return this._isSet(7);
   }

   public void setJavaCompiler(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._JavaCompiler;
      this._JavaCompiler = var1;
      this._postSet(7, var2, var1);
   }

   public String getJavaCompilerPreClassPath() {
      return this._JavaCompilerPreClassPath;
   }

   public boolean isJavaCompilerPreClassPathSet() {
      return this._isSet(8);
   }

   public void setJavaCompilerPreClassPath(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._JavaCompilerPreClassPath;
      this._JavaCompilerPreClassPath = var1;
      this._postSet(8, var2, var1);
   }

   public String getJavaCompilerPostClassPath() {
      return this._JavaCompilerPostClassPath;
   }

   public boolean isJavaCompilerPostClassPathSet() {
      return this._isSet(9);
   }

   public void setJavaCompilerPostClassPath(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._JavaCompilerPostClassPath;
      this._JavaCompilerPostClassPath = var1;
      this._postSet(9, var2, var1);
   }

   public String getExtraRmicOptions() {
      return this._ExtraRmicOptions;
   }

   public boolean isExtraRmicOptionsSet() {
      return this._isSet(10);
   }

   public void setExtraRmicOptions(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ExtraRmicOptions;
      this._ExtraRmicOptions = var1;
      this._postSet(10, var2, var1);
   }

   public boolean getKeepGenerated() {
      return this._KeepGenerated;
   }

   public boolean isKeepGeneratedSet() {
      return this._isSet(11);
   }

   public void setKeepGenerated(boolean var1) {
      boolean var2 = this._KeepGenerated;
      this._KeepGenerated = var1;
      this._postSet(11, var2, var1);
   }

   public boolean getForceGeneration() {
      return this._ForceGeneration;
   }

   public boolean isForceGenerationSet() {
      return this._isSet(12);
   }

   public void setForceGeneration(boolean var1) {
      boolean var2 = this._ForceGeneration;
      this._ForceGeneration = var1;
      this._postSet(12, var2, var1);
   }

   public String getTmpPath() {
      return this._TmpPath;
   }

   public boolean isTmpPathSet() {
      return this._isSet(13);
   }

   public void setTmpPath(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._TmpPath;
      this._TmpPath = var1;
      this._postSet(13, var2, var1);
   }

   public String getVerboseEJBDeploymentEnabled() {
      return this._VerboseEJBDeploymentEnabled;
   }

   public boolean isVerboseEJBDeploymentEnabledSet() {
      return this._isSet(14);
   }

   public void setVerboseEJBDeploymentEnabled(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._VerboseEJBDeploymentEnabled;
      this._VerboseEJBDeploymentEnabled = var1;
      this._postSet(14, var2, var1);
   }

   public String getExtraEjbcOptions() {
      return this._ExtraEjbcOptions;
   }

   public boolean isExtraEjbcOptionsSet() {
      return this._isSet(15);
   }

   public void setExtraEjbcOptions(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ExtraEjbcOptions;
      this._ExtraEjbcOptions = var1;
      this._postSet(15, var2, var1);
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
         var1 = 15;
      }

      try {
         switch (var1) {
            case 15:
               this._ExtraEjbcOptions = null;
               if (var2) {
                  break;
               }
            case 10:
               this._ExtraRmicOptions = null;
               if (var2) {
                  break;
               }
            case 12:
               this._ForceGeneration = false;
               if (var2) {
                  break;
               }
            case 7:
               this._JavaCompiler = null;
               if (var2) {
                  break;
               }
            case 9:
               this._JavaCompilerPostClassPath = null;
               if (var2) {
                  break;
               }
            case 8:
               this._JavaCompilerPreClassPath = null;
               if (var2) {
                  break;
               }
            case 11:
               this._KeepGenerated = true;
               if (var2) {
                  break;
               }
            case 13:
               this._TmpPath = "tmp_ejb";
               if (var2) {
                  break;
               }
            case 14:
               this._VerboseEJBDeploymentEnabled = "false";
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
      return "EJBContainer";
   }

   public void putValue(String var1, Object var2) {
      String var3;
      if (var1.equals("ExtraEjbcOptions")) {
         var3 = this._ExtraEjbcOptions;
         this._ExtraEjbcOptions = (String)var2;
         this._postSet(15, var3, this._ExtraEjbcOptions);
      } else if (var1.equals("ExtraRmicOptions")) {
         var3 = this._ExtraRmicOptions;
         this._ExtraRmicOptions = (String)var2;
         this._postSet(10, var3, this._ExtraRmicOptions);
      } else {
         boolean var4;
         if (var1.equals("ForceGeneration")) {
            var4 = this._ForceGeneration;
            this._ForceGeneration = (Boolean)var2;
            this._postSet(12, var4, this._ForceGeneration);
         } else if (var1.equals("JavaCompiler")) {
            var3 = this._JavaCompiler;
            this._JavaCompiler = (String)var2;
            this._postSet(7, var3, this._JavaCompiler);
         } else if (var1.equals("JavaCompilerPostClassPath")) {
            var3 = this._JavaCompilerPostClassPath;
            this._JavaCompilerPostClassPath = (String)var2;
            this._postSet(9, var3, this._JavaCompilerPostClassPath);
         } else if (var1.equals("JavaCompilerPreClassPath")) {
            var3 = this._JavaCompilerPreClassPath;
            this._JavaCompilerPreClassPath = (String)var2;
            this._postSet(8, var3, this._JavaCompilerPreClassPath);
         } else if (var1.equals("KeepGenerated")) {
            var4 = this._KeepGenerated;
            this._KeepGenerated = (Boolean)var2;
            this._postSet(11, var4, this._KeepGenerated);
         } else if (var1.equals("TmpPath")) {
            var3 = this._TmpPath;
            this._TmpPath = (String)var2;
            this._postSet(13, var3, this._TmpPath);
         } else if (var1.equals("VerboseEJBDeploymentEnabled")) {
            var3 = this._VerboseEJBDeploymentEnabled;
            this._VerboseEJBDeploymentEnabled = (String)var2;
            this._postSet(14, var3, this._VerboseEJBDeploymentEnabled);
         } else {
            super.putValue(var1, var2);
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("ExtraEjbcOptions")) {
         return this._ExtraEjbcOptions;
      } else if (var1.equals("ExtraRmicOptions")) {
         return this._ExtraRmicOptions;
      } else if (var1.equals("ForceGeneration")) {
         return new Boolean(this._ForceGeneration);
      } else if (var1.equals("JavaCompiler")) {
         return this._JavaCompiler;
      } else if (var1.equals("JavaCompilerPostClassPath")) {
         return this._JavaCompilerPostClassPath;
      } else if (var1.equals("JavaCompilerPreClassPath")) {
         return this._JavaCompilerPreClassPath;
      } else if (var1.equals("KeepGenerated")) {
         return new Boolean(this._KeepGenerated);
      } else if (var1.equals("TmpPath")) {
         return this._TmpPath;
      } else {
         return var1.equals("VerboseEJBDeploymentEnabled") ? this._VerboseEJBDeploymentEnabled : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 8:
               if (var1.equals("tmp-path")) {
                  return 13;
               }
            case 9:
            case 10:
            case 11:
            case 12:
            case 15:
            case 17:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            default:
               break;
            case 13:
               if (var1.equals("java-compiler")) {
                  return 7;
               }
               break;
            case 14:
               if (var1.equals("keep-generated")) {
                  return 11;
               }
               break;
            case 16:
               if (var1.equals("force-generation")) {
                  return 12;
               }
               break;
            case 18:
               if (var1.equals("extra-ejbc-options")) {
                  return 15;
               }

               if (var1.equals("extra-rmic-options")) {
                  return 10;
               }
               break;
            case 28:
               if (var1.equals("java-compiler-pre-class-path")) {
                  return 8;
               }
               break;
            case 29:
               if (var1.equals("java-compiler-post-class-path")) {
                  return 9;
               }

               if (var1.equals("verboseejb-deployment-enabled")) {
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
            case 7:
               return "java-compiler";
            case 8:
               return "java-compiler-pre-class-path";
            case 9:
               return "java-compiler-post-class-path";
            case 10:
               return "extra-rmic-options";
            case 11:
               return "keep-generated";
            case 12:
               return "force-generation";
            case 13:
               return "tmp-path";
            case 14:
               return "verboseejb-deployment-enabled";
            case 15:
               return "extra-ejbc-options";
            default:
               return super.getElementName(var1);
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

      public String[] getKeyElementNames() {
         ArrayList var1 = new ArrayList();
         var1.add("name");
         return (String[])((String[])var1.toArray(new String[0]));
      }
   }

   protected static class Helper extends ConfigurationMBeanImpl.Helper {
      private EJBContainerMBeanImpl bean;

      protected Helper(EJBContainerMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "JavaCompiler";
            case 8:
               return "JavaCompilerPreClassPath";
            case 9:
               return "JavaCompilerPostClassPath";
            case 10:
               return "ExtraRmicOptions";
            case 11:
               return "KeepGenerated";
            case 12:
               return "ForceGeneration";
            case 13:
               return "TmpPath";
            case 14:
               return "VerboseEJBDeploymentEnabled";
            case 15:
               return "ExtraEjbcOptions";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("ExtraEjbcOptions")) {
            return 15;
         } else if (var1.equals("ExtraRmicOptions")) {
            return 10;
         } else if (var1.equals("ForceGeneration")) {
            return 12;
         } else if (var1.equals("JavaCompiler")) {
            return 7;
         } else if (var1.equals("JavaCompilerPostClassPath")) {
            return 9;
         } else if (var1.equals("JavaCompilerPreClassPath")) {
            return 8;
         } else if (var1.equals("KeepGenerated")) {
            return 11;
         } else if (var1.equals("TmpPath")) {
            return 13;
         } else {
            return var1.equals("VerboseEJBDeploymentEnabled") ? 14 : super.getPropertyIndex(var1);
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
            if (this.bean.isExtraEjbcOptionsSet()) {
               var2.append("ExtraEjbcOptions");
               var2.append(String.valueOf(this.bean.getExtraEjbcOptions()));
            }

            if (this.bean.isExtraRmicOptionsSet()) {
               var2.append("ExtraRmicOptions");
               var2.append(String.valueOf(this.bean.getExtraRmicOptions()));
            }

            if (this.bean.isForceGenerationSet()) {
               var2.append("ForceGeneration");
               var2.append(String.valueOf(this.bean.getForceGeneration()));
            }

            if (this.bean.isJavaCompilerSet()) {
               var2.append("JavaCompiler");
               var2.append(String.valueOf(this.bean.getJavaCompiler()));
            }

            if (this.bean.isJavaCompilerPostClassPathSet()) {
               var2.append("JavaCompilerPostClassPath");
               var2.append(String.valueOf(this.bean.getJavaCompilerPostClassPath()));
            }

            if (this.bean.isJavaCompilerPreClassPathSet()) {
               var2.append("JavaCompilerPreClassPath");
               var2.append(String.valueOf(this.bean.getJavaCompilerPreClassPath()));
            }

            if (this.bean.isKeepGeneratedSet()) {
               var2.append("KeepGenerated");
               var2.append(String.valueOf(this.bean.getKeepGenerated()));
            }

            if (this.bean.isTmpPathSet()) {
               var2.append("TmpPath");
               var2.append(String.valueOf(this.bean.getTmpPath()));
            }

            if (this.bean.isVerboseEJBDeploymentEnabledSet()) {
               var2.append("VerboseEJBDeploymentEnabled");
               var2.append(String.valueOf(this.bean.getVerboseEJBDeploymentEnabled()));
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
            EJBContainerMBeanImpl var2 = (EJBContainerMBeanImpl)var1;
            this.computeDiff("ExtraEjbcOptions", this.bean.getExtraEjbcOptions(), var2.getExtraEjbcOptions(), false);
            this.computeDiff("ExtraRmicOptions", this.bean.getExtraRmicOptions(), var2.getExtraRmicOptions(), false);
            this.computeDiff("ForceGeneration", this.bean.getForceGeneration(), var2.getForceGeneration(), false);
            this.computeDiff("JavaCompiler", this.bean.getJavaCompiler(), var2.getJavaCompiler(), true);
            this.computeDiff("JavaCompilerPostClassPath", this.bean.getJavaCompilerPostClassPath(), var2.getJavaCompilerPostClassPath(), false);
            this.computeDiff("JavaCompilerPreClassPath", this.bean.getJavaCompilerPreClassPath(), var2.getJavaCompilerPreClassPath(), false);
            this.computeDiff("KeepGenerated", this.bean.getKeepGenerated(), var2.getKeepGenerated(), false);
            this.computeDiff("TmpPath", this.bean.getTmpPath(), var2.getTmpPath(), false);
            this.computeDiff("VerboseEJBDeploymentEnabled", this.bean.getVerboseEJBDeploymentEnabled(), var2.getVerboseEJBDeploymentEnabled(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            EJBContainerMBeanImpl var3 = (EJBContainerMBeanImpl)var1.getSourceBean();
            EJBContainerMBeanImpl var4 = (EJBContainerMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("ExtraEjbcOptions")) {
                  var3.setExtraEjbcOptions(var4.getExtraEjbcOptions());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 15);
               } else if (var5.equals("ExtraRmicOptions")) {
                  var3.setExtraRmicOptions(var4.getExtraRmicOptions());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("ForceGeneration")) {
                  var3.setForceGeneration(var4.getForceGeneration());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("JavaCompiler")) {
                  var3.setJavaCompiler(var4.getJavaCompiler());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("JavaCompilerPostClassPath")) {
                  var3.setJavaCompilerPostClassPath(var4.getJavaCompilerPostClassPath());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("JavaCompilerPreClassPath")) {
                  var3.setJavaCompilerPreClassPath(var4.getJavaCompilerPreClassPath());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("KeepGenerated")) {
                  var3.setKeepGenerated(var4.getKeepGenerated());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("TmpPath")) {
                  var3.setTmpPath(var4.getTmpPath());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 13);
               } else if (var5.equals("VerboseEJBDeploymentEnabled")) {
                  var3.setVerboseEJBDeploymentEnabled(var4.getVerboseEJBDeploymentEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 14);
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
            EJBContainerMBeanImpl var5 = (EJBContainerMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("ExtraEjbcOptions")) && this.bean.isExtraEjbcOptionsSet()) {
               var5.setExtraEjbcOptions(this.bean.getExtraEjbcOptions());
            }

            if ((var3 == null || !var3.contains("ExtraRmicOptions")) && this.bean.isExtraRmicOptionsSet()) {
               var5.setExtraRmicOptions(this.bean.getExtraRmicOptions());
            }

            if ((var3 == null || !var3.contains("ForceGeneration")) && this.bean.isForceGenerationSet()) {
               var5.setForceGeneration(this.bean.getForceGeneration());
            }

            if ((var3 == null || !var3.contains("JavaCompiler")) && this.bean.isJavaCompilerSet()) {
               var5.setJavaCompiler(this.bean.getJavaCompiler());
            }

            if ((var3 == null || !var3.contains("JavaCompilerPostClassPath")) && this.bean.isJavaCompilerPostClassPathSet()) {
               var5.setJavaCompilerPostClassPath(this.bean.getJavaCompilerPostClassPath());
            }

            if ((var3 == null || !var3.contains("JavaCompilerPreClassPath")) && this.bean.isJavaCompilerPreClassPathSet()) {
               var5.setJavaCompilerPreClassPath(this.bean.getJavaCompilerPreClassPath());
            }

            if ((var3 == null || !var3.contains("KeepGenerated")) && this.bean.isKeepGeneratedSet()) {
               var5.setKeepGenerated(this.bean.getKeepGenerated());
            }

            if ((var3 == null || !var3.contains("TmpPath")) && this.bean.isTmpPathSet()) {
               var5.setTmpPath(this.bean.getTmpPath());
            }

            if ((var3 == null || !var3.contains("VerboseEJBDeploymentEnabled")) && this.bean.isVerboseEJBDeploymentEnabledSet()) {
               var5.setVerboseEJBDeploymentEnabled(this.bean.getVerboseEJBDeploymentEnabled());
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
