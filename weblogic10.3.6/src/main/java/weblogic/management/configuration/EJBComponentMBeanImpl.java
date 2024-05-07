package weblogic.management.configuration;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
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
import weblogic.management.mbeans.custom.EJBComponent;
import weblogic.management.runtime.EJBComponentRuntimeMBean;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.CombinedIterator;

public class EJBComponentMBeanImpl extends ComponentMBeanImpl implements EJBComponentMBean, Serializable {
   private TargetMBean[] _ActivatedTargets;
   private ApplicationMBean _Application;
   private EJBComponentRuntimeMBean _EJBComponentRuntime;
   private String _ExtraEjbcOptions;
   private String _ExtraRmicOptions;
   private boolean _ForceGeneration;
   private String _JavaCompiler;
   private String _JavaCompilerPostClassPath;
   private String _JavaCompilerPreClassPath;
   private boolean _KeepGenerated;
   private String _Name;
   private TargetMBean[] _Targets;
   private String _TmpPath;
   private String _VerboseEJBDeploymentEnabled;
   private EJBComponent _customizer;
   private static SchemaHelper2 _schemaHelper;

   public EJBComponentMBeanImpl() {
      try {
         this._customizer = new EJBComponent(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public EJBComponentMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new EJBComponent(this);
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         }

         throw new UndeclaredThrowableException(var4);
      }

      this._initializeProperty(-1);
   }

   public ApplicationMBean getApplication() {
      return this._customizer.getApplication();
   }

   public EJBComponentRuntimeMBean getEJBComponentRuntime() {
      return this._customizer.getEJBComponentRuntime();
   }

   public String getJavaCompiler() {
      return this._JavaCompiler;
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

   public TargetMBean[] getTargets() {
      return this._customizer.getTargets();
   }

   public String getTargetsAsString() {
      return this._getHelper()._serializeKeyList(this.getTargets());
   }

   public boolean isApplicationSet() {
      return this._isSet(9);
   }

   public boolean isEJBComponentRuntimeSet() {
      return this._isSet(21);
   }

   public boolean isJavaCompilerSet() {
      return this._isSet(12);
   }

   public boolean isNameSet() {
      return this._isSet(2);
   }

   public boolean isTargetsSet() {
      return this._isSet(7);
   }

   public void setTargetsAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         String[] var13 = this._getHelper()._splitKeyList(var1);
         List var3 = this._getHelper()._getKeyList(this._Targets);

         String var5;
         for(int var4 = 0; var4 < var13.length; ++var4) {
            var5 = var13[var4];
            var5 = var5 == null ? null : var5.trim();
            if (var3.contains(var5)) {
               var3.remove(var5);
            } else {
               this._getReferenceManager().registerUnresolvedReference(var5, TargetMBean.class, new ReferenceManager.Resolver(this, 7) {
                  public void resolveReference(Object var1) {
                     try {
                        EJBComponentMBeanImpl.this.addTarget((TargetMBean)var1);
                     } catch (RuntimeException var3) {
                        throw var3;
                     } catch (Exception var4) {
                        throw new AssertionError("Impossible exception: " + var4);
                     }
                  }
               });
            }
         }

         Iterator var14 = var3.iterator();

         while(true) {
            while(var14.hasNext()) {
               var5 = (String)var14.next();
               TargetMBean[] var6 = this._Targets;
               int var7 = var6.length;

               for(int var8 = 0; var8 < var7; ++var8) {
                  TargetMBean var9 = var6[var8];
                  if (var5.equals(var9.getName())) {
                     try {
                        this.removeTarget(var9);
                        break;
                     } catch (RuntimeException var11) {
                        throw var11;
                     } catch (Exception var12) {
                        throw new AssertionError("Impossible exception: " + var12);
                     }
                  }
               }
            }

            return;
         }
      } else {
         TargetMBean[] var2 = this._Targets;
         this._initializeProperty(7);
         this._postSet(7, var2, this._Targets);
      }
   }

   public void setApplication(ApplicationMBean var1) throws InvalidAttributeValueException {
      this._customizer.setApplication(var1);
   }

   public void setEJBComponentRuntime(EJBComponentRuntimeMBean var1) {
      this._customizer.setEJBComponentRuntime(var1);
   }

   public void setJavaCompiler(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._JavaCompiler;
      this._JavaCompiler = var1;
      this._postSet(12, var2, var1);
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

   public void setTargets(TargetMBean[] var1) throws InvalidAttributeValueException, DistributedManagementException {
      Object var4 = var1 == null ? new TargetMBeanImpl[0] : var1;
      var1 = (TargetMBean[])((TargetMBean[])this._getHelper()._cleanAndValidateArray(var4, TargetMBean.class));

      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var1[var2] != null) {
            ResolvedReference var3 = new ResolvedReference(this, 7, (AbstractDescriptorBean)var1[var2]) {
               protected Object getPropertyValue() {
                  return EJBComponentMBeanImpl.this.getTargets();
               }
            };
            this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1[var2], var3);
         }
      }

      TargetMBean[] var5 = this.getTargets();
      this._customizer.setTargets(var1);
      this._postSet(7, var5, var1);
   }

   public boolean addTarget(TargetMBean var1) throws InvalidAttributeValueException, DistributedManagementException {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 7)) {
         TargetMBean[] var2 = (TargetMBean[])((TargetMBean[])this._getHelper()._extendArray(this.getTargets(), TargetMBean.class, var1));

         try {
            this.setTargets(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            if (var4 instanceof InvalidAttributeValueException) {
               throw (InvalidAttributeValueException)var4;
            }

            if (var4 instanceof DistributedManagementException) {
               throw (DistributedManagementException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

      return true;
   }

   public String getJavaCompilerPreClassPath() {
      return this._JavaCompilerPreClassPath;
   }

   public boolean isJavaCompilerPreClassPathSet() {
      return this._isSet(13);
   }

   public boolean removeTarget(TargetMBean var1) throws InvalidAttributeValueException, DistributedManagementException {
      TargetMBean[] var2 = this.getTargets();
      TargetMBean[] var3 = (TargetMBean[])((TargetMBean[])this._getHelper()._removeElement(var2, TargetMBean.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setTargets(var3);
            return true;
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            } else if (var5 instanceof InvalidAttributeValueException) {
               throw (InvalidAttributeValueException)var5;
            } else if (var5 instanceof DistributedManagementException) {
               throw (DistributedManagementException)var5;
            } else {
               throw new UndeclaredThrowableException(var5);
            }
         }
      } else {
         return false;
      }
   }

   public void setJavaCompilerPreClassPath(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._JavaCompilerPreClassPath;
      this._JavaCompilerPreClassPath = var1;
      this._postSet(13, var2, var1);
   }

   public TargetMBean[] getActivatedTargets() {
      return this._customizer.getActivatedTargets();
   }

   public String getJavaCompilerPostClassPath() {
      return this._JavaCompilerPostClassPath;
   }

   public boolean isActivatedTargetsSet() {
      return this._isSet(11);
   }

   public boolean isJavaCompilerPostClassPathSet() {
      return this._isSet(14);
   }

   public void addActivatedTarget(TargetMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 11)) {
         TargetMBean[] var2 = (TargetMBean[])((TargetMBean[])this._getHelper()._extendArray(this.getActivatedTargets(), TargetMBean.class, var1));

         try {
            this.setActivatedTargets(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public void setJavaCompilerPostClassPath(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._JavaCompilerPostClassPath;
      this._JavaCompilerPostClassPath = var1;
      this._postSet(14, var2, var1);
   }

   public String getExtraRmicOptions() {
      return this._ExtraRmicOptions;
   }

   public boolean isExtraRmicOptionsSet() {
      return this._isSet(15);
   }

   public void removeActivatedTarget(TargetMBean var1) {
      TargetMBean[] var2 = this.getActivatedTargets();
      TargetMBean[] var3 = (TargetMBean[])((TargetMBean[])this._getHelper()._removeElement(var2, TargetMBean.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setActivatedTargets(var3);
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            }

            throw new UndeclaredThrowableException(var5);
         }
      }

   }

   public void setActivatedTargets(TargetMBean[] var1) {
      Object var2 = var1 == null ? new TargetMBeanImpl[0] : var1;
      this._ActivatedTargets = (TargetMBean[])var2;
   }

   public void setExtraRmicOptions(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ExtraRmicOptions;
      this._ExtraRmicOptions = var1;
      this._postSet(15, var2, var1);
   }

   public boolean activated(TargetMBean var1) {
      return this._customizer.activated(var1);
   }

   public boolean getKeepGenerated() {
      return this._KeepGenerated;
   }

   public boolean isKeepGeneratedSet() {
      return this._isSet(16);
   }

   public void refreshDDsIfNeeded(String[] var1) {
      this._customizer.refreshDDsIfNeeded(var1);
   }

   public void setKeepGenerated(boolean var1) {
      boolean var2 = this._KeepGenerated;
      this._KeepGenerated = var1;
      this._postSet(16, var2, var1);
   }

   public boolean getForceGeneration() {
      return this._ForceGeneration;
   }

   public boolean isForceGenerationSet() {
      return this._isSet(17);
   }

   public void touch() throws ConfigurationException {
      this._customizer.touch();
   }

   public void freezeCurrentValue(String var1) throws AttributeNotFoundException, MBeanException {
      this._customizer.freezeCurrentValue(var1);
   }

   public void setForceGeneration(boolean var1) {
      boolean var2 = this._ForceGeneration;
      this._ForceGeneration = var1;
      this._postSet(17, var2, var1);
   }

   public String getTmpPath() {
      return this._TmpPath;
   }

   public boolean isTmpPathSet() {
      return this._isSet(18);
   }

   public void restoreDefaultValue(String var1) throws AttributeNotFoundException {
      this._customizer.restoreDefaultValue(var1);
   }

   public void setTmpPath(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._TmpPath;
      this._TmpPath = var1;
      this._postSet(18, var2, var1);
   }

   public String getVerboseEJBDeploymentEnabled() {
      return this._VerboseEJBDeploymentEnabled;
   }

   public boolean isVerboseEJBDeploymentEnabledSet() {
      return this._isSet(19);
   }

   public void setVerboseEJBDeploymentEnabled(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._VerboseEJBDeploymentEnabled;
      this._VerboseEJBDeploymentEnabled = var1;
      this._postSet(19, var2, var1);
   }

   public String getExtraEjbcOptions() {
      return this._ExtraEjbcOptions;
   }

   public boolean isExtraEjbcOptionsSet() {
      return this._isSet(20);
   }

   public void setExtraEjbcOptions(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ExtraEjbcOptions;
      this._ExtraEjbcOptions = var1;
      this._postSet(20, var2, var1);
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
               this._ActivatedTargets = new TargetMBean[0];
               if (var2) {
                  break;
               }
            case 9:
               this._customizer.setApplication((ApplicationMBean)null);
               if (var2) {
                  break;
               }
            case 21:
               this._customizer.setEJBComponentRuntime((EJBComponentRuntimeMBean)null);
               if (var2) {
                  break;
               }
            case 20:
               this._ExtraEjbcOptions = null;
               if (var2) {
                  break;
               }
            case 15:
               this._ExtraRmicOptions = null;
               if (var2) {
                  break;
               }
            case 17:
               this._ForceGeneration = false;
               if (var2) {
                  break;
               }
            case 12:
               this._JavaCompiler = null;
               if (var2) {
                  break;
               }
            case 14:
               this._JavaCompilerPostClassPath = null;
               if (var2) {
                  break;
               }
            case 13:
               this._JavaCompilerPreClassPath = null;
               if (var2) {
                  break;
               }
            case 16:
               this._KeepGenerated = true;
               if (var2) {
                  break;
               }
            case 2:
               this._customizer.setName((String)null);
               if (var2) {
                  break;
               }
            case 7:
               this._customizer.setTargets(new TargetMBean[0]);
               if (var2) {
                  break;
               }
            case 18:
               this._TmpPath = "tmp_ejb";
               if (var2) {
                  break;
               }
            case 19:
               this._VerboseEJBDeploymentEnabled = "false";
               if (var2) {
                  break;
               }
            case 3:
            case 4:
            case 5:
            case 6:
            case 8:
            case 10:
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
      return "EJBComponent";
   }

   public void putValue(String var1, Object var2) {
      TargetMBean[] var5;
      if (var1.equals("ActivatedTargets")) {
         var5 = this._ActivatedTargets;
         this._ActivatedTargets = (TargetMBean[])((TargetMBean[])var2);
         this._postSet(11, var5, this._ActivatedTargets);
      } else if (var1.equals("Application")) {
         ApplicationMBean var8 = this._Application;
         this._Application = (ApplicationMBean)var2;
         this._postSet(9, var8, this._Application);
      } else if (var1.equals("EJBComponentRuntime")) {
         EJBComponentRuntimeMBean var7 = this._EJBComponentRuntime;
         this._EJBComponentRuntime = (EJBComponentRuntimeMBean)var2;
         this._postSet(21, var7, this._EJBComponentRuntime);
      } else {
         String var4;
         if (var1.equals("ExtraEjbcOptions")) {
            var4 = this._ExtraEjbcOptions;
            this._ExtraEjbcOptions = (String)var2;
            this._postSet(20, var4, this._ExtraEjbcOptions);
         } else if (var1.equals("ExtraRmicOptions")) {
            var4 = this._ExtraRmicOptions;
            this._ExtraRmicOptions = (String)var2;
            this._postSet(15, var4, this._ExtraRmicOptions);
         } else {
            boolean var6;
            if (var1.equals("ForceGeneration")) {
               var6 = this._ForceGeneration;
               this._ForceGeneration = (Boolean)var2;
               this._postSet(17, var6, this._ForceGeneration);
            } else if (var1.equals("JavaCompiler")) {
               var4 = this._JavaCompiler;
               this._JavaCompiler = (String)var2;
               this._postSet(12, var4, this._JavaCompiler);
            } else if (var1.equals("JavaCompilerPostClassPath")) {
               var4 = this._JavaCompilerPostClassPath;
               this._JavaCompilerPostClassPath = (String)var2;
               this._postSet(14, var4, this._JavaCompilerPostClassPath);
            } else if (var1.equals("JavaCompilerPreClassPath")) {
               var4 = this._JavaCompilerPreClassPath;
               this._JavaCompilerPreClassPath = (String)var2;
               this._postSet(13, var4, this._JavaCompilerPreClassPath);
            } else if (var1.equals("KeepGenerated")) {
               var6 = this._KeepGenerated;
               this._KeepGenerated = (Boolean)var2;
               this._postSet(16, var6, this._KeepGenerated);
            } else if (var1.equals("Name")) {
               var4 = this._Name;
               this._Name = (String)var2;
               this._postSet(2, var4, this._Name);
            } else if (var1.equals("Targets")) {
               var5 = this._Targets;
               this._Targets = (TargetMBean[])((TargetMBean[])var2);
               this._postSet(7, var5, this._Targets);
            } else if (var1.equals("TmpPath")) {
               var4 = this._TmpPath;
               this._TmpPath = (String)var2;
               this._postSet(18, var4, this._TmpPath);
            } else if (var1.equals("VerboseEJBDeploymentEnabled")) {
               var4 = this._VerboseEJBDeploymentEnabled;
               this._VerboseEJBDeploymentEnabled = (String)var2;
               this._postSet(19, var4, this._VerboseEJBDeploymentEnabled);
            } else if (var1.equals("customizer")) {
               EJBComponent var3 = this._customizer;
               this._customizer = (EJBComponent)var2;
            } else {
               super.putValue(var1, var2);
            }
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("ActivatedTargets")) {
         return this._ActivatedTargets;
      } else if (var1.equals("Application")) {
         return this._Application;
      } else if (var1.equals("EJBComponentRuntime")) {
         return this._EJBComponentRuntime;
      } else if (var1.equals("ExtraEjbcOptions")) {
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
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("Targets")) {
         return this._Targets;
      } else if (var1.equals("TmpPath")) {
         return this._TmpPath;
      } else if (var1.equals("VerboseEJBDeploymentEnabled")) {
         return this._VerboseEJBDeploymentEnabled;
      } else {
         return var1.equals("customizer") ? this._customizer : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ComponentMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 4:
               if (var1.equals("name")) {
                  return 2;
               }
            case 5:
            case 7:
            case 9:
            case 10:
            case 12:
            case 15:
            case 17:
            case 19:
            case 20:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            default:
               break;
            case 6:
               if (var1.equals("target")) {
                  return 7;
               }
               break;
            case 8:
               if (var1.equals("tmp-path")) {
                  return 18;
               }
               break;
            case 11:
               if (var1.equals("application")) {
                  return 9;
               }
               break;
            case 13:
               if (var1.equals("java-compiler")) {
                  return 12;
               }
               break;
            case 14:
               if (var1.equals("keep-generated")) {
                  return 16;
               }
               break;
            case 16:
               if (var1.equals("activated-target")) {
                  return 11;
               }

               if (var1.equals("force-generation")) {
                  return 17;
               }
               break;
            case 18:
               if (var1.equals("extra-ejbc-options")) {
                  return 20;
               }

               if (var1.equals("extra-rmic-options")) {
                  return 15;
               }
               break;
            case 21:
               if (var1.equals("ejb-component-runtime")) {
                  return 21;
               }
               break;
            case 28:
               if (var1.equals("java-compiler-pre-class-path")) {
                  return 13;
               }
               break;
            case 29:
               if (var1.equals("java-compiler-post-class-path")) {
                  return 14;
               }

               if (var1.equals("verboseejb-deployment-enabled")) {
                  return 19;
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
            case 8:
            case 10:
            default:
               return super.getElementName(var1);
            case 7:
               return "target";
            case 9:
               return "application";
            case 11:
               return "activated-target";
            case 12:
               return "java-compiler";
            case 13:
               return "java-compiler-pre-class-path";
            case 14:
               return "java-compiler-post-class-path";
            case 15:
               return "extra-rmic-options";
            case 16:
               return "keep-generated";
            case 17:
               return "force-generation";
            case 18:
               return "tmp-path";
            case 19:
               return "verboseejb-deployment-enabled";
            case 20:
               return "extra-ejbc-options";
            case 21:
               return "ejb-component-runtime";
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

   protected static class Helper extends ComponentMBeanImpl.Helper {
      private EJBComponentMBeanImpl bean;

      protected Helper(EJBComponentMBeanImpl var1) {
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
            case 8:
            case 10:
            default:
               return super.getPropertyName(var1);
            case 7:
               return "Targets";
            case 9:
               return "Application";
            case 11:
               return "ActivatedTargets";
            case 12:
               return "JavaCompiler";
            case 13:
               return "JavaCompilerPreClassPath";
            case 14:
               return "JavaCompilerPostClassPath";
            case 15:
               return "ExtraRmicOptions";
            case 16:
               return "KeepGenerated";
            case 17:
               return "ForceGeneration";
            case 18:
               return "TmpPath";
            case 19:
               return "VerboseEJBDeploymentEnabled";
            case 20:
               return "ExtraEjbcOptions";
            case 21:
               return "EJBComponentRuntime";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("ActivatedTargets")) {
            return 11;
         } else if (var1.equals("Application")) {
            return 9;
         } else if (var1.equals("EJBComponentRuntime")) {
            return 21;
         } else if (var1.equals("ExtraEjbcOptions")) {
            return 20;
         } else if (var1.equals("ExtraRmicOptions")) {
            return 15;
         } else if (var1.equals("ForceGeneration")) {
            return 17;
         } else if (var1.equals("JavaCompiler")) {
            return 12;
         } else if (var1.equals("JavaCompilerPostClassPath")) {
            return 14;
         } else if (var1.equals("JavaCompilerPreClassPath")) {
            return 13;
         } else if (var1.equals("KeepGenerated")) {
            return 16;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("Targets")) {
            return 7;
         } else if (var1.equals("TmpPath")) {
            return 18;
         } else {
            return var1.equals("VerboseEJBDeploymentEnabled") ? 19 : super.getPropertyIndex(var1);
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
            if (this.bean.isActivatedTargetsSet()) {
               var2.append("ActivatedTargets");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getActivatedTargets())));
            }

            if (this.bean.isApplicationSet()) {
               var2.append("Application");
               var2.append(String.valueOf(this.bean.getApplication()));
            }

            if (this.bean.isEJBComponentRuntimeSet()) {
               var2.append("EJBComponentRuntime");
               var2.append(String.valueOf(this.bean.getEJBComponentRuntime()));
            }

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

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isTargetsSet()) {
               var2.append("Targets");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getTargets())));
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
            EJBComponentMBeanImpl var2 = (EJBComponentMBeanImpl)var1;
            this.computeDiff("ExtraEjbcOptions", this.bean.getExtraEjbcOptions(), var2.getExtraEjbcOptions(), false);
            this.computeDiff("ExtraRmicOptions", this.bean.getExtraRmicOptions(), var2.getExtraRmicOptions(), false);
            this.computeDiff("ForceGeneration", this.bean.getForceGeneration(), var2.getForceGeneration(), false);
            this.computeDiff("JavaCompiler", this.bean.getJavaCompiler(), var2.getJavaCompiler(), true);
            this.computeDiff("JavaCompilerPostClassPath", this.bean.getJavaCompilerPostClassPath(), var2.getJavaCompilerPostClassPath(), false);
            this.computeDiff("JavaCompilerPreClassPath", this.bean.getJavaCompilerPreClassPath(), var2.getJavaCompilerPreClassPath(), false);
            this.computeDiff("KeepGenerated", this.bean.getKeepGenerated(), var2.getKeepGenerated(), false);
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeDiff("Targets", this.bean.getTargets(), var2.getTargets(), true);
            this.computeDiff("TmpPath", this.bean.getTmpPath(), var2.getTmpPath(), false);
            this.computeDiff("VerboseEJBDeploymentEnabled", this.bean.getVerboseEJBDeploymentEnabled(), var2.getVerboseEJBDeploymentEnabled(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            EJBComponentMBeanImpl var3 = (EJBComponentMBeanImpl)var1.getSourceBean();
            EJBComponentMBeanImpl var4 = (EJBComponentMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (!var5.equals("ActivatedTargets") && !var5.equals("Application") && !var5.equals("EJBComponentRuntime")) {
                  if (var5.equals("ExtraEjbcOptions")) {
                     var3.setExtraEjbcOptions(var4.getExtraEjbcOptions());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 20);
                  } else if (var5.equals("ExtraRmicOptions")) {
                     var3.setExtraRmicOptions(var4.getExtraRmicOptions());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 15);
                  } else if (var5.equals("ForceGeneration")) {
                     var3.setForceGeneration(var4.getForceGeneration());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 17);
                  } else if (var5.equals("JavaCompiler")) {
                     var3.setJavaCompiler(var4.getJavaCompiler());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 12);
                  } else if (var5.equals("JavaCompilerPostClassPath")) {
                     var3.setJavaCompilerPostClassPath(var4.getJavaCompilerPostClassPath());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 14);
                  } else if (var5.equals("JavaCompilerPreClassPath")) {
                     var3.setJavaCompilerPreClassPath(var4.getJavaCompilerPreClassPath());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 13);
                  } else if (var5.equals("KeepGenerated")) {
                     var3.setKeepGenerated(var4.getKeepGenerated());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 16);
                  } else if (var5.equals("Name")) {
                     var3.setName(var4.getName());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 2);
                  } else if (var5.equals("Targets")) {
                     var3.setTargetsAsString(var4.getTargetsAsString());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 7);
                  } else if (var5.equals("TmpPath")) {
                     var3.setTmpPath(var4.getTmpPath());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 18);
                  } else if (var5.equals("VerboseEJBDeploymentEnabled")) {
                     var3.setVerboseEJBDeploymentEnabled(var4.getVerboseEJBDeploymentEnabled());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 19);
                  } else {
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
            EJBComponentMBeanImpl var5 = (EJBComponentMBeanImpl)var1;
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

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if ((var3 == null || !var3.contains("Targets")) && this.bean.isTargetsSet()) {
               var5._unSet(var5, 7);
               var5.setTargetsAsString(this.bean.getTargetsAsString());
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
         this.inferSubTree(this.bean.getActivatedTargets(), var1, var2);
         this.inferSubTree(this.bean.getApplication(), var1, var2);
         this.inferSubTree(this.bean.getTargets(), var1, var2);
      }
   }
}
