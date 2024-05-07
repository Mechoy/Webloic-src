package weblogic.management.configuration;

import java.io.IOException;
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
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.management.ManagementException;
import weblogic.management.mbeans.custom.AppDeployment;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.ArrayIterator;
import weblogic.utils.collections.CombinedIterator;

public class LibraryMBeanImpl extends AppDeploymentMBeanImpl implements LibraryMBean, Serializable {
   private String _AbsoluteInstallDir;
   private String _AbsolutePlanDir;
   private String _AbsolutePlanPath;
   private String _AbsoluteSourcePath;
   private ApplicationMBean _AppMBean;
   private String _ApplicationIdentifier;
   private String _ApplicationName;
   private boolean _AutoDeployedApp;
   private byte[] _DeploymentPlan;
   private DeploymentPlanBean _DeploymentPlanDescriptor;
   private byte[] _DeploymentPlanExternalDescriptors;
   private String _LocalInstallDir;
   private String _LocalPlanDir;
   private String _LocalPlanPath;
   private String _LocalSourcePath;
   private String _Name;
   private String _PlanDir;
   private String _PlanPath;
   private String _RootStagingDir;
   private String _SourcePath;
   private String _StagingMode;
   private String _VersionIdentifier;
   private AppDeployment _customizer;
   private static SchemaHelper2 _schemaHelper;

   public LibraryMBeanImpl() {
      try {
         this._customizer = new AppDeployment(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public LibraryMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new AppDeployment(this);
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

   public String getSourcePath() {
      return this._customizer.getSourcePath();
   }

   public boolean isNameSet() {
      return this._isSet(2);
   }

   public boolean isSourcePathSet() {
      return this._isSet(10);
   }

   public void setName(String var1) throws InvalidAttributeValueException, ManagementException {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonEmptyString("Name", var1);
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("Name", var1);
      String var2 = this.getName();
      this._customizer.setName(var1);
      this._postSet(2, var2, var1);
   }

   public void setSourcePath(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this.getSourcePath();
      this._customizer.setSourcePath(var1);
      this._postSet(10, var2, var1);
   }

   public String getPlanDir() {
      return this._customizer.getPlanDir();
   }

   public boolean isPlanDirSet() {
      return this._isSet(15);
   }

   public String getPlanPath() {
      return this._customizer.getPlanPath();
   }

   public boolean isPlanPathSet() {
      return this._isSet(16);
   }

   public String getVersionIdentifier() {
      return this._customizer.getVersionIdentifier();
   }

   public boolean isVersionIdentifierSet() {
      return this._isSet(17);
   }

   public void setVersionIdentifier(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._VersionIdentifier;
      this._VersionIdentifier = var1;
      this._postSet(17, var2, var1);
   }

   public String getStagingMode() {
      return this._customizer.getStagingMode();
   }

   public boolean isStagingModeSet() {
      return this._isSet(20);
   }

   public void touch() throws ConfigurationException {
      this._customizer.touch();
   }

   public void freezeCurrentValue(String var1) throws AttributeNotFoundException, MBeanException {
      this._customizer.freezeCurrentValue(var1);
   }

   public void setPlanDir(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this.getPlanDir();
      this._customizer.setPlanDir(var1);
      this._postSet(15, var2, var1);
   }

   public void restoreDefaultValue(String var1) throws AttributeNotFoundException {
      this._customizer.restoreDefaultValue(var1);
   }

   public void setPlanPath(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this.getPlanPath();
      this._customizer.setPlanPath(var1);
      this._postSet(16, var2, var1);
   }

   public void setStagingMode(String var1) throws ManagementException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{AppDeploymentMBean.DEFAULT_STAGE, "nostage", "stage", "external_stage"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("StagingMode", var1, var2);
      String var3 = this.getStagingMode();
      this._customizer.setStagingMode(var1);
      this._postSet(20, var3, var1);
   }

   public String getApplicationIdentifier() {
      return this._customizer.getApplicationIdentifier();
   }

   public boolean isApplicationIdentifierSet() {
      return this._isSet(23);
   }

   public void setApplicationIdentifier(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ApplicationIdentifier;
      this._ApplicationIdentifier = var1;
      this._postSet(23, var2, var1);
   }

   public String getApplicationName() {
      return this._customizer.getApplicationName();
   }

   public boolean isApplicationNameSet() {
      return this._isSet(24);
   }

   public void setApplicationName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ApplicationName;
      this._ApplicationName = var1;
      this._postSet(24, var2, var1);
   }

   public ApplicationMBean getAppMBean() {
      return this._customizer.getAppMBean();
   }

   public boolean isAppMBeanSet() {
      return this._isSet(25);
   }

   public void setAppMBean(ApplicationMBean var1) throws InvalidAttributeValueException {
      this._AppMBean = var1;
   }

   public boolean isAutoDeployedApp() {
      return this._customizer.isAutoDeployedApp();
   }

   public boolean isAutoDeployedAppSet() {
      return this._isSet(28);
   }

   public void setAutoDeployedApp(boolean var1) throws InvalidAttributeValueException {
      this._AutoDeployedApp = var1;
   }

   public DeploymentPlanBean getDeploymentPlanDescriptor() {
      return this._customizer.getDeploymentPlanDescriptor();
   }

   public boolean isDeploymentPlanDescriptorSet() {
      return this._isSet(29);
   }

   public void setDeploymentPlanDescriptor(DeploymentPlanBean var1) {
      this._customizer.setDeploymentPlanDescriptor(var1);
   }

   public String getAbsoluteInstallDir() {
      return this._customizer.getAbsoluteInstallDir();
   }

   public boolean isAbsoluteInstallDirSet() {
      return this._isSet(32);
   }

   public void setAbsoluteInstallDir(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this._AbsoluteInstallDir = var1;
   }

   public String getAbsolutePlanPath() {
      return this._customizer.getAbsolutePlanPath();
   }

   public boolean isAbsolutePlanPathSet() {
      return this._isSet(33);
   }

   public void setAbsolutePlanPath(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this._AbsolutePlanPath = var1;
   }

   public String getAbsolutePlanDir() {
      return this._customizer.getAbsolutePlanDir();
   }

   public boolean isAbsolutePlanDirSet() {
      return this._isSet(34);
   }

   public void setAbsolutePlanDir(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this._AbsolutePlanDir = var1;
   }

   public String getAbsoluteSourcePath() {
      return this._customizer.getAbsoluteSourcePath();
   }

   public boolean isAbsoluteSourcePathSet() {
      return this._isSet(35);
   }

   public void setAbsoluteSourcePath(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this._AbsoluteSourcePath = var1;
   }

   public String getLocalInstallDir() {
      return this._customizer.getLocalInstallDir();
   }

   public boolean isLocalInstallDirSet() {
      return this._isSet(36);
   }

   public void setLocalInstallDir(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this._LocalInstallDir = var1;
   }

   public String getLocalPlanPath() {
      return this._customizer.getLocalPlanPath();
   }

   public boolean isLocalPlanPathSet() {
      return this._isSet(37);
   }

   public void setLocalPlanPath(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this._LocalPlanPath = var1;
   }

   public String getLocalPlanDir() {
      try {
         return this._customizer.getLocalPlanDir();
      } catch (IOException var2) {
         throw new UndeclaredThrowableException(var2);
      }
   }

   public boolean isLocalPlanDirSet() {
      return this._isSet(38);
   }

   public void setLocalPlanDir(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this._LocalPlanDir = var1;
   }

   public String getLocalSourcePath() {
      return this._customizer.getLocalSourcePath();
   }

   public boolean isLocalSourcePathSet() {
      return this._isSet(39);
   }

   public void setLocalSourcePath(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this._LocalSourcePath = var1;
   }

   public String getRootStagingDir() {
      return this._customizer.getRootStagingDir();
   }

   public boolean isRootStagingDirSet() {
      return this._isSet(40);
   }

   public void setRootStagingDir(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this._RootStagingDir = var1;
   }

   public String getStagingMode(String var1) {
      return this._customizer.getStagingMode(var1);
   }

   public byte[] getDeploymentPlan() {
      return this._customizer.getDeploymentPlan();
   }

   public boolean isDeploymentPlanSet() {
      return this._isSet(41);
   }

   public void setDeploymentPlan(byte[] var1) throws InvalidAttributeValueException {
      this._DeploymentPlan = var1;
   }

   public byte[] getDeploymentPlanExternalDescriptors() {
      return this._customizer.getDeploymentPlanExternalDescriptors();
   }

   public boolean isDeploymentPlanExternalDescriptorsSet() {
      return this._isSet(42);
   }

   public void setDeploymentPlanExternalDescriptors(byte[] var1) throws InvalidAttributeValueException {
      this._DeploymentPlanExternalDescriptors = var1;
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
         var1 = 32;
      }

      try {
         switch (var1) {
            case 32:
               this._AbsoluteInstallDir = null;
               if (var2) {
                  break;
               }
            case 34:
               this._AbsolutePlanDir = null;
               if (var2) {
                  break;
               }
            case 33:
               this._AbsolutePlanPath = null;
               if (var2) {
                  break;
               }
            case 35:
               this._AbsoluteSourcePath = null;
               if (var2) {
                  break;
               }
            case 25:
               this._AppMBean = null;
               if (var2) {
                  break;
               }
            case 23:
               this._ApplicationIdentifier = null;
               if (var2) {
                  break;
               }
            case 24:
               this._ApplicationName = null;
               if (var2) {
                  break;
               }
            case 41:
               this._DeploymentPlan = new byte[0];
               if (var2) {
                  break;
               }
            case 29:
               this._customizer.setDeploymentPlanDescriptor((DeploymentPlanBean)null);
               if (var2) {
                  break;
               }
            case 42:
               this._DeploymentPlanExternalDescriptors = new byte[0];
               if (var2) {
                  break;
               }
            case 36:
               this._LocalInstallDir = null;
               if (var2) {
                  break;
               }
            case 38:
               this._LocalPlanDir = null;
               if (var2) {
                  break;
               }
            case 37:
               this._LocalPlanPath = null;
               if (var2) {
                  break;
               }
            case 39:
               this._LocalSourcePath = null;
               if (var2) {
                  break;
               }
            case 2:
               this._customizer.setName((String)null);
               if (var2) {
                  break;
               }
            case 15:
               this._customizer.setPlanDir((String)null);
               if (var2) {
                  break;
               }
            case 16:
               this._customizer.setPlanPath((String)null);
               if (var2) {
                  break;
               }
            case 40:
               this._RootStagingDir = null;
               if (var2) {
                  break;
               }
            case 10:
               this._customizer.setSourcePath((String)null);
               if (var2) {
                  break;
               }
            case 20:
               this._customizer.setStagingMode(AppDeploymentMBean.DEFAULT_STAGE);
               if (var2) {
                  break;
               }
            case 17:
               this._VersionIdentifier = null;
               if (var2) {
                  break;
               }
            case 28:
               this._AutoDeployedApp = false;
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
            case 11:
            case 12:
            case 13:
            case 14:
            case 18:
            case 19:
            case 21:
            case 22:
            case 26:
            case 27:
            case 30:
            case 31:
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
      return "Library";
   }

   public void putValue(String var1, Object var2) {
      String var4;
      if (var1.equals("AbsoluteInstallDir")) {
         var4 = this._AbsoluteInstallDir;
         this._AbsoluteInstallDir = (String)var2;
         this._postSet(32, var4, this._AbsoluteInstallDir);
      } else if (var1.equals("AbsolutePlanDir")) {
         var4 = this._AbsolutePlanDir;
         this._AbsolutePlanDir = (String)var2;
         this._postSet(34, var4, this._AbsolutePlanDir);
      } else if (var1.equals("AbsolutePlanPath")) {
         var4 = this._AbsolutePlanPath;
         this._AbsolutePlanPath = (String)var2;
         this._postSet(33, var4, this._AbsolutePlanPath);
      } else if (var1.equals("AbsoluteSourcePath")) {
         var4 = this._AbsoluteSourcePath;
         this._AbsoluteSourcePath = (String)var2;
         this._postSet(35, var4, this._AbsoluteSourcePath);
      } else if (var1.equals("AppMBean")) {
         ApplicationMBean var8 = this._AppMBean;
         this._AppMBean = (ApplicationMBean)var2;
         this._postSet(25, var8, this._AppMBean);
      } else if (var1.equals("ApplicationIdentifier")) {
         var4 = this._ApplicationIdentifier;
         this._ApplicationIdentifier = (String)var2;
         this._postSet(23, var4, this._ApplicationIdentifier);
      } else if (var1.equals("ApplicationName")) {
         var4 = this._ApplicationName;
         this._ApplicationName = (String)var2;
         this._postSet(24, var4, this._ApplicationName);
      } else if (var1.equals("AutoDeployedApp")) {
         boolean var7 = this._AutoDeployedApp;
         this._AutoDeployedApp = (Boolean)var2;
         this._postSet(28, var7, this._AutoDeployedApp);
      } else {
         byte[] var5;
         if (var1.equals("DeploymentPlan")) {
            var5 = this._DeploymentPlan;
            this._DeploymentPlan = (byte[])((byte[])var2);
            this._postSet(41, var5, this._DeploymentPlan);
         } else if (var1.equals("DeploymentPlanDescriptor")) {
            DeploymentPlanBean var6 = this._DeploymentPlanDescriptor;
            this._DeploymentPlanDescriptor = (DeploymentPlanBean)var2;
            this._postSet(29, var6, this._DeploymentPlanDescriptor);
         } else if (var1.equals("DeploymentPlanExternalDescriptors")) {
            var5 = this._DeploymentPlanExternalDescriptors;
            this._DeploymentPlanExternalDescriptors = (byte[])((byte[])var2);
            this._postSet(42, var5, this._DeploymentPlanExternalDescriptors);
         } else if (var1.equals("LocalInstallDir")) {
            var4 = this._LocalInstallDir;
            this._LocalInstallDir = (String)var2;
            this._postSet(36, var4, this._LocalInstallDir);
         } else if (var1.equals("LocalPlanDir")) {
            var4 = this._LocalPlanDir;
            this._LocalPlanDir = (String)var2;
            this._postSet(38, var4, this._LocalPlanDir);
         } else if (var1.equals("LocalPlanPath")) {
            var4 = this._LocalPlanPath;
            this._LocalPlanPath = (String)var2;
            this._postSet(37, var4, this._LocalPlanPath);
         } else if (var1.equals("LocalSourcePath")) {
            var4 = this._LocalSourcePath;
            this._LocalSourcePath = (String)var2;
            this._postSet(39, var4, this._LocalSourcePath);
         } else if (var1.equals("Name")) {
            var4 = this._Name;
            this._Name = (String)var2;
            this._postSet(2, var4, this._Name);
         } else if (var1.equals("PlanDir")) {
            var4 = this._PlanDir;
            this._PlanDir = (String)var2;
            this._postSet(15, var4, this._PlanDir);
         } else if (var1.equals("PlanPath")) {
            var4 = this._PlanPath;
            this._PlanPath = (String)var2;
            this._postSet(16, var4, this._PlanPath);
         } else if (var1.equals("RootStagingDir")) {
            var4 = this._RootStagingDir;
            this._RootStagingDir = (String)var2;
            this._postSet(40, var4, this._RootStagingDir);
         } else if (var1.equals("SourcePath")) {
            var4 = this._SourcePath;
            this._SourcePath = (String)var2;
            this._postSet(10, var4, this._SourcePath);
         } else if (var1.equals("StagingMode")) {
            var4 = this._StagingMode;
            this._StagingMode = (String)var2;
            this._postSet(20, var4, this._StagingMode);
         } else if (var1.equals("VersionIdentifier")) {
            var4 = this._VersionIdentifier;
            this._VersionIdentifier = (String)var2;
            this._postSet(17, var4, this._VersionIdentifier);
         } else if (var1.equals("customizer")) {
            AppDeployment var3 = this._customizer;
            this._customizer = (AppDeployment)var2;
         } else {
            super.putValue(var1, var2);
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("AbsoluteInstallDir")) {
         return this._AbsoluteInstallDir;
      } else if (var1.equals("AbsolutePlanDir")) {
         return this._AbsolutePlanDir;
      } else if (var1.equals("AbsolutePlanPath")) {
         return this._AbsolutePlanPath;
      } else if (var1.equals("AbsoluteSourcePath")) {
         return this._AbsoluteSourcePath;
      } else if (var1.equals("AppMBean")) {
         return this._AppMBean;
      } else if (var1.equals("ApplicationIdentifier")) {
         return this._ApplicationIdentifier;
      } else if (var1.equals("ApplicationName")) {
         return this._ApplicationName;
      } else if (var1.equals("AutoDeployedApp")) {
         return new Boolean(this._AutoDeployedApp);
      } else if (var1.equals("DeploymentPlan")) {
         return this._DeploymentPlan;
      } else if (var1.equals("DeploymentPlanDescriptor")) {
         return this._DeploymentPlanDescriptor;
      } else if (var1.equals("DeploymentPlanExternalDescriptors")) {
         return this._DeploymentPlanExternalDescriptors;
      } else if (var1.equals("LocalInstallDir")) {
         return this._LocalInstallDir;
      } else if (var1.equals("LocalPlanDir")) {
         return this._LocalPlanDir;
      } else if (var1.equals("LocalPlanPath")) {
         return this._LocalPlanPath;
      } else if (var1.equals("LocalSourcePath")) {
         return this._LocalSourcePath;
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("PlanDir")) {
         return this._PlanDir;
      } else if (var1.equals("PlanPath")) {
         return this._PlanPath;
      } else if (var1.equals("RootStagingDir")) {
         return this._RootStagingDir;
      } else if (var1.equals("SourcePath")) {
         return this._SourcePath;
      } else if (var1.equals("StagingMode")) {
         return this._StagingMode;
      } else if (var1.equals("VersionIdentifier")) {
         return this._VersionIdentifier;
      } else {
         return var1.equals("customizer") ? this._customizer : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends AppDeploymentMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 4:
               if (var1.equals("name")) {
                  return 2;
               }
            case 5:
            case 6:
            case 7:
            case 10:
            case 13:
            case 19:
            case 21:
            case 23:
            case 24:
            case 25:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            default:
               break;
            case 8:
               if (var1.equals("plan-dir")) {
                  return 15;
               }
               break;
            case 9:
               if (var1.equals("appm-bean")) {
                  return 25;
               }

               if (var1.equals("plan-path")) {
                  return 16;
               }
               break;
            case 11:
               if (var1.equals("source-path")) {
                  return 10;
               }
               break;
            case 12:
               if (var1.equals("staging-mode")) {
                  return 20;
               }
               break;
            case 14:
               if (var1.equals("local-plan-dir")) {
                  return 38;
               }
               break;
            case 15:
               if (var1.equals("deployment-plan")) {
                  return 41;
               }

               if (var1.equals("local-plan-path")) {
                  return 37;
               }
               break;
            case 16:
               if (var1.equals("application-name")) {
                  return 24;
               }

               if (var1.equals("root-staging-dir")) {
                  return 40;
               }
               break;
            case 17:
               if (var1.equals("absolute-plan-dir")) {
                  return 34;
               }

               if (var1.equals("local-install-dir")) {
                  return 36;
               }

               if (var1.equals("local-source-path")) {
                  return 39;
               }

               if (var1.equals("auto-deployed-app")) {
                  return 28;
               }
               break;
            case 18:
               if (var1.equals("absolute-plan-path")) {
                  return 33;
               }

               if (var1.equals("version-identifier")) {
                  return 17;
               }
               break;
            case 20:
               if (var1.equals("absolute-install-dir")) {
                  return 32;
               }

               if (var1.equals("absolute-source-path")) {
                  return 35;
               }
               break;
            case 22:
               if (var1.equals("application-identifier")) {
                  return 23;
               }
               break;
            case 26:
               if (var1.equals("deployment-plan-descriptor")) {
                  return 29;
               }
               break;
            case 35:
               if (var1.equals("deployment-plan-external-descriptor")) {
                  return 42;
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
            case 11:
            case 12:
            case 13:
            case 14:
            case 18:
            case 19:
            case 21:
            case 22:
            case 26:
            case 27:
            case 30:
            case 31:
            default:
               return super.getElementName(var1);
            case 10:
               return "source-path";
            case 15:
               return "plan-dir";
            case 16:
               return "plan-path";
            case 17:
               return "version-identifier";
            case 20:
               return "staging-mode";
            case 23:
               return "application-identifier";
            case 24:
               return "application-name";
            case 25:
               return "appm-bean";
            case 28:
               return "auto-deployed-app";
            case 29:
               return "deployment-plan-descriptor";
            case 32:
               return "absolute-install-dir";
            case 33:
               return "absolute-plan-path";
            case 34:
               return "absolute-plan-dir";
            case 35:
               return "absolute-source-path";
            case 36:
               return "local-install-dir";
            case 37:
               return "local-plan-path";
            case 38:
               return "local-plan-dir";
            case 39:
               return "local-source-path";
            case 40:
               return "root-staging-dir";
            case 41:
               return "deployment-plan";
            case 42:
               return "deployment-plan-external-descriptor";
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 7:
               return true;
            case 11:
               return true;
            case 30:
               return true;
            case 41:
               return true;
            case 42:
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

   protected static class Helper extends AppDeploymentMBeanImpl.Helper {
      private LibraryMBeanImpl bean;

      protected Helper(LibraryMBeanImpl var1) {
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
            case 11:
            case 12:
            case 13:
            case 14:
            case 18:
            case 19:
            case 21:
            case 22:
            case 26:
            case 27:
            case 30:
            case 31:
            default:
               return super.getPropertyName(var1);
            case 10:
               return "SourcePath";
            case 15:
               return "PlanDir";
            case 16:
               return "PlanPath";
            case 17:
               return "VersionIdentifier";
            case 20:
               return "StagingMode";
            case 23:
               return "ApplicationIdentifier";
            case 24:
               return "ApplicationName";
            case 25:
               return "AppMBean";
            case 28:
               return "AutoDeployedApp";
            case 29:
               return "DeploymentPlanDescriptor";
            case 32:
               return "AbsoluteInstallDir";
            case 33:
               return "AbsolutePlanPath";
            case 34:
               return "AbsolutePlanDir";
            case 35:
               return "AbsoluteSourcePath";
            case 36:
               return "LocalInstallDir";
            case 37:
               return "LocalPlanPath";
            case 38:
               return "LocalPlanDir";
            case 39:
               return "LocalSourcePath";
            case 40:
               return "RootStagingDir";
            case 41:
               return "DeploymentPlan";
            case 42:
               return "DeploymentPlanExternalDescriptors";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("AbsoluteInstallDir")) {
            return 32;
         } else if (var1.equals("AbsolutePlanDir")) {
            return 34;
         } else if (var1.equals("AbsolutePlanPath")) {
            return 33;
         } else if (var1.equals("AbsoluteSourcePath")) {
            return 35;
         } else if (var1.equals("AppMBean")) {
            return 25;
         } else if (var1.equals("ApplicationIdentifier")) {
            return 23;
         } else if (var1.equals("ApplicationName")) {
            return 24;
         } else if (var1.equals("DeploymentPlan")) {
            return 41;
         } else if (var1.equals("DeploymentPlanDescriptor")) {
            return 29;
         } else if (var1.equals("DeploymentPlanExternalDescriptors")) {
            return 42;
         } else if (var1.equals("LocalInstallDir")) {
            return 36;
         } else if (var1.equals("LocalPlanDir")) {
            return 38;
         } else if (var1.equals("LocalPlanPath")) {
            return 37;
         } else if (var1.equals("LocalSourcePath")) {
            return 39;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("PlanDir")) {
            return 15;
         } else if (var1.equals("PlanPath")) {
            return 16;
         } else if (var1.equals("RootStagingDir")) {
            return 40;
         } else if (var1.equals("SourcePath")) {
            return 10;
         } else if (var1.equals("StagingMode")) {
            return 20;
         } else if (var1.equals("VersionIdentifier")) {
            return 17;
         } else {
            return var1.equals("AutoDeployedApp") ? 28 : super.getPropertyIndex(var1);
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
            if (this.bean.isAbsoluteInstallDirSet()) {
               var2.append("AbsoluteInstallDir");
               var2.append(String.valueOf(this.bean.getAbsoluteInstallDir()));
            }

            if (this.bean.isAbsolutePlanDirSet()) {
               var2.append("AbsolutePlanDir");
               var2.append(String.valueOf(this.bean.getAbsolutePlanDir()));
            }

            if (this.bean.isAbsolutePlanPathSet()) {
               var2.append("AbsolutePlanPath");
               var2.append(String.valueOf(this.bean.getAbsolutePlanPath()));
            }

            if (this.bean.isAbsoluteSourcePathSet()) {
               var2.append("AbsoluteSourcePath");
               var2.append(String.valueOf(this.bean.getAbsoluteSourcePath()));
            }

            if (this.bean.isAppMBeanSet()) {
               var2.append("AppMBean");
               var2.append(String.valueOf(this.bean.getAppMBean()));
            }

            if (this.bean.isApplicationIdentifierSet()) {
               var2.append("ApplicationIdentifier");
               var2.append(String.valueOf(this.bean.getApplicationIdentifier()));
            }

            if (this.bean.isApplicationNameSet()) {
               var2.append("ApplicationName");
               var2.append(String.valueOf(this.bean.getApplicationName()));
            }

            if (this.bean.isDeploymentPlanSet()) {
               var2.append("DeploymentPlan");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getDeploymentPlan())));
            }

            if (this.bean.isDeploymentPlanDescriptorSet()) {
               var2.append("DeploymentPlanDescriptor");
               var2.append(String.valueOf(this.bean.getDeploymentPlanDescriptor()));
            }

            if (this.bean.isDeploymentPlanExternalDescriptorsSet()) {
               var2.append("DeploymentPlanExternalDescriptors");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getDeploymentPlanExternalDescriptors())));
            }

            if (this.bean.isLocalInstallDirSet()) {
               var2.append("LocalInstallDir");
               var2.append(String.valueOf(this.bean.getLocalInstallDir()));
            }

            if (this.bean.isLocalPlanDirSet()) {
               var2.append("LocalPlanDir");
               var2.append(String.valueOf(this.bean.getLocalPlanDir()));
            }

            if (this.bean.isLocalPlanPathSet()) {
               var2.append("LocalPlanPath");
               var2.append(String.valueOf(this.bean.getLocalPlanPath()));
            }

            if (this.bean.isLocalSourcePathSet()) {
               var2.append("LocalSourcePath");
               var2.append(String.valueOf(this.bean.getLocalSourcePath()));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isPlanDirSet()) {
               var2.append("PlanDir");
               var2.append(String.valueOf(this.bean.getPlanDir()));
            }

            if (this.bean.isPlanPathSet()) {
               var2.append("PlanPath");
               var2.append(String.valueOf(this.bean.getPlanPath()));
            }

            if (this.bean.isRootStagingDirSet()) {
               var2.append("RootStagingDir");
               var2.append(String.valueOf(this.bean.getRootStagingDir()));
            }

            if (this.bean.isSourcePathSet()) {
               var2.append("SourcePath");
               var2.append(String.valueOf(this.bean.getSourcePath()));
            }

            if (this.bean.isStagingModeSet()) {
               var2.append("StagingMode");
               var2.append(String.valueOf(this.bean.getStagingMode()));
            }

            if (this.bean.isVersionIdentifierSet()) {
               var2.append("VersionIdentifier");
               var2.append(String.valueOf(this.bean.getVersionIdentifier()));
            }

            if (this.bean.isAutoDeployedAppSet()) {
               var2.append("AutoDeployedApp");
               var2.append(String.valueOf(this.bean.isAutoDeployedApp()));
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
            LibraryMBeanImpl var2 = (LibraryMBeanImpl)var1;
            this.computeDiff("ApplicationIdentifier", this.bean.getApplicationIdentifier(), var2.getApplicationIdentifier(), false);
            this.computeDiff("ApplicationName", this.bean.getApplicationName(), var2.getApplicationName(), false);
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeDiff("PlanDir", this.bean.getPlanDir(), var2.getPlanDir(), true);
            this.computeDiff("PlanPath", this.bean.getPlanPath(), var2.getPlanPath(), true);
            this.computeDiff("SourcePath", this.bean.getSourcePath(), var2.getSourcePath(), false);
            this.computeDiff("StagingMode", this.bean.getStagingMode(), var2.getStagingMode(), true);
            this.computeDiff("VersionIdentifier", this.bean.getVersionIdentifier(), var2.getVersionIdentifier(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            LibraryMBeanImpl var3 = (LibraryMBeanImpl)var1.getSourceBean();
            LibraryMBeanImpl var4 = (LibraryMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (!var5.equals("AbsoluteInstallDir") && !var5.equals("AbsolutePlanDir") && !var5.equals("AbsolutePlanPath") && !var5.equals("AbsoluteSourcePath") && !var5.equals("AppMBean")) {
                  if (var5.equals("ApplicationIdentifier")) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 23);
                  } else if (var5.equals("ApplicationName")) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 24);
                  } else if (!var5.equals("DeploymentPlan") && !var5.equals("DeploymentPlanDescriptor") && !var5.equals("DeploymentPlanExternalDescriptors") && !var5.equals("LocalInstallDir") && !var5.equals("LocalPlanDir") && !var5.equals("LocalPlanPath") && !var5.equals("LocalSourcePath")) {
                     if (var5.equals("Name")) {
                        var3.setName(var4.getName());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 2);
                     } else if (var5.equals("PlanDir")) {
                        var3.setPlanDir(var4.getPlanDir());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 15);
                     } else if (var5.equals("PlanPath")) {
                        var3.setPlanPath(var4.getPlanPath());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 16);
                     } else if (!var5.equals("RootStagingDir")) {
                        if (var5.equals("SourcePath")) {
                           var3.setSourcePath(var4.getSourcePath());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 10);
                        } else if (var5.equals("StagingMode")) {
                           var3.setStagingMode(var4.getStagingMode());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 20);
                        } else if (var5.equals("VersionIdentifier")) {
                           var3._conditionalUnset(var2.isUnsetUpdate(), 17);
                        } else if (!var5.equals("AutoDeployedApp")) {
                           super.applyPropertyUpdate(var1, var2);
                        }
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
            LibraryMBeanImpl var5 = (LibraryMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("ApplicationIdentifier")) && this.bean.isApplicationIdentifierSet()) {
            }

            if ((var3 == null || !var3.contains("ApplicationName")) && this.bean.isApplicationNameSet()) {
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if ((var3 == null || !var3.contains("PlanDir")) && this.bean.isPlanDirSet()) {
               var5.setPlanDir(this.bean.getPlanDir());
            }

            if ((var3 == null || !var3.contains("PlanPath")) && this.bean.isPlanPathSet()) {
               var5.setPlanPath(this.bean.getPlanPath());
            }

            if ((var3 == null || !var3.contains("SourcePath")) && this.bean.isSourcePathSet()) {
               var5.setSourcePath(this.bean.getSourcePath());
            }

            if ((var3 == null || !var3.contains("StagingMode")) && this.bean.isStagingModeSet()) {
               var5.setStagingMode(this.bean.getStagingMode());
            }

            if ((var3 == null || !var3.contains("VersionIdentifier")) && this.bean.isVersionIdentifierSet()) {
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
         this.inferSubTree(this.bean.getAppMBean(), var1, var2);
      }
   }
}
