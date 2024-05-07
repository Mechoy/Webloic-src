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
import weblogic.deploy.internal.targetserver.DeployHelper;
import weblogic.descriptor.BeanRemoveRejectedException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BootstrapProperties;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.management.DistributedManagementException;
import weblogic.management.ManagementException;
import weblogic.management.deploy.DeploymentCompatibilityEventHandler;
import weblogic.management.mbeans.custom.Application;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.ArrayIterator;
import weblogic.utils.collections.CombinedIterator;

public class ApplicationMBeanImpl extends ConfigurationMBeanImpl implements ApplicationMBean, Serializable {
   private String _AltDescriptorPath;
   private String _AltWLSDescriptorPath;
   private AppDeploymentMBean _AppDeployment;
   private ComponentMBean[] _Components;
   private ConnectorComponentMBean[] _ConnectorComponents;
   private boolean _DelegationEnabled;
   private boolean _Deployed;
   private int _DeploymentTimeout;
   private String _DeploymentType;
   private EJBComponentMBean[] _EJBComponents;
   private boolean _Ear;
   private String _FullPath;
   private boolean _InternalApp;
   private int _InternalType;
   private JDBCPoolComponentMBean[] _JDBCPoolComponents;
   private int _LoadOrder;
   private String _Name;
   private String _Notes;
   private String _Path;
   private String[] _StagedTargets;
   private String _StagingMode;
   private String _StagingPath;
   private boolean _TwoPhase;
   private WebAppComponentMBean[] _WebAppComponents;
   private WebServiceComponentMBean[] _WebServiceComponents;
   private Application _customizer;
   private static SchemaHelper2 _schemaHelper;

   public ApplicationMBeanImpl() {
      try {
         this._customizer = new Application(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public ApplicationMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new Application(this);
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

   public String getPath() {
      return this._customizer.getPath();
   }

   public boolean isNameSet() {
      return this._isSet(2);
   }

   public boolean isPathSet() {
      return this._isSet(7);
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

   public void setPath(String var1) throws ManagementException, InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this.getPath();
      this._customizer.setPath(var1);
      this._postSet(7, var2, var1);
   }

   public ComponentMBean[] getComponents() {
      return this._customizer.getComponents();
   }

   public String getNotes() {
      return this._customizer.getNotes();
   }

   public boolean isComponentsSet() {
      return this._isSet(8);
   }

   public boolean isNotesSet() {
      return this._isSet(3);
   }

   public boolean addComponent(ComponentMBean var1) throws InvalidAttributeValueException, DistributedManagementException {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 8)) {
         ComponentMBean[] var2 = (ComponentMBean[])((ComponentMBean[])this._getHelper()._extendArray(this.getComponents(), ComponentMBean.class, var1));

         try {
            this.setComponents(var2);
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

   public void setNotes(String var1) throws InvalidAttributeValueException, DistributedManagementException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this.getNotes();
      this._customizer.setNotes(var1);
      this._postSet(3, var2, var1);
   }

   public boolean removeComponent(ComponentMBean var1) throws InvalidAttributeValueException, DistributedManagementException {
      ComponentMBean[] var2 = this.getComponents();
      ComponentMBean[] var3 = (ComponentMBean[])((ComponentMBean[])this._getHelper()._removeElement(var2, ComponentMBean.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setComponents(var3);
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

   public void setComponents(ComponentMBean[] var1) {
      Object var2 = var1 == null ? new ComponentMBeanImpl[0] : var1;
      this._Components = (ComponentMBean[])var2;
   }

   public WebAppComponentMBean createWebAppComponent(String var1) {
      WebAppComponentMBeanImpl var2 = new WebAppComponentMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addWebAppComponent(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyWebAppComponent(WebAppComponentMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 9);
         WebAppComponentMBean[] var2 = this.getWebAppComponents();
         WebAppComponentMBean[] var3 = (WebAppComponentMBean[])((WebAppComponentMBean[])this._getHelper()._removeElement(var2, WebAppComponentMBean.class, var1));
         if (var2.length != var3.length) {
            this._preDestroy((AbstractDescriptorBean)var1);

            try {
               AbstractDescriptorBean var4 = (AbstractDescriptorBean)var1;
               if (var4 == null) {
                  return;
               }

               List var5 = this._getReferenceManager().getResolvedReferences(var4);
               if (var5 != null && var5.size() > 0) {
                  throw new BeanRemoveRejectedException(var4, var5);
               }

               this._getReferenceManager().unregisterBean(var4);
               this._markDestroyed(var4);
               this.setWebAppComponents(var3);
            } catch (Exception var6) {
               if (var6 instanceof RuntimeException) {
                  throw (RuntimeException)var6;
               }

               throw new UndeclaredThrowableException(var6);
            }
         }

      } catch (Exception var7) {
         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         } else {
            throw new UndeclaredThrowableException(var7);
         }
      }
   }

   public WebAppComponentMBean lookupWebAppComponent(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._WebAppComponents).iterator();

      WebAppComponentMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (WebAppComponentMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addWebAppComponent(WebAppComponentMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 9)) {
         WebAppComponentMBean[] var2;
         if (this._isSet(9)) {
            var2 = (WebAppComponentMBean[])((WebAppComponentMBean[])this._getHelper()._extendArray(this.getWebAppComponents(), WebAppComponentMBean.class, var1));
         } else {
            var2 = new WebAppComponentMBean[]{var1};
         }

         try {
            this.setWebAppComponents(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public WebAppComponentMBean[] getWebAppComponents() {
      return this._WebAppComponents;
   }

   public boolean isWebAppComponentsSet() {
      return this._isSet(9);
   }

   public void removeWebAppComponent(WebAppComponentMBean var1) {
      this.destroyWebAppComponent(var1);
   }

   public void setWebAppComponents(WebAppComponentMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new WebAppComponentMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 9)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      WebAppComponentMBean[] var5 = this._WebAppComponents;
      this._WebAppComponents = (WebAppComponentMBean[])var4;
      this._postSet(9, var5, var4);
   }

   public boolean isEar() {
      return this._customizer.isEar();
   }

   public boolean isEarSet() {
      return this._isSet(10);
   }

   public void setEar(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._Ear;
      this._Ear = var1;
      this._postSet(10, var2, var1);
   }

   public void touch() throws ConfigurationException {
      this._customizer.touch();
   }

   public void freezeCurrentValue(String var1) throws AttributeNotFoundException, MBeanException {
      this._customizer.freezeCurrentValue(var1);
   }

   public String getFullPath() {
      return this._customizer.getFullPath();
   }

   public boolean isFullPathSet() {
      return this._isSet(11);
   }

   public void setFullPath(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this._FullPath = var1;
   }

   public boolean isInternalApp() {
      return this._customizer.isInternalApp();
   }

   public boolean isInternalAppSet() {
      return this._isSet(12);
   }

   public void restoreDefaultValue(String var1) throws AttributeNotFoundException {
      this._customizer.restoreDefaultValue(var1);
   }

   public void setInternalApp(boolean var1) {
      this._customizer.setInternalApp(var1);
   }

   public String getStagingPath() {
      return this._StagingPath;
   }

   public boolean isStagingPathSet() {
      return this._isSet(13);
   }

   public void setStagingPath(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this._StagingPath = var1;
   }

   public String[] getStagedTargets() {
      return this._StagedTargets;
   }

   public boolean isStagedTargetsSet() {
      return this._isSet(14);
   }

   public void addStagedTarget(String var1) {
      this._getHelper()._ensureNonNull(var1);
      String[] var2;
      if (this._isSet(14)) {
         var2 = (String[])((String[])this._getHelper()._extendArray(this.getStagedTargets(), String.class, var1));
      } else {
         var2 = new String[]{var1};
      }

      try {
         this.setStagedTargets(var2);
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void removeStagedTarget(String var1) {
      String[] var2 = this.getStagedTargets();
      String[] var3 = (String[])((String[])this._getHelper()._removeElement(var2, String.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setStagedTargets(var3);
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            }

            throw new UndeclaredThrowableException(var5);
         }
      }

   }

   public void setStagedTargets(String[] var1) {
      var1 = var1 == null ? new String[0] : var1;
      var1 = this._getHelper()._trimElements(var1);
      String[] var2 = this._StagedTargets;
      this._StagedTargets = var1;
      this._postSet(14, var2, var1);
   }

   public void unstageTargets(String[] var1) {
      this._customizer.unstageTargets(var1);
   }

   public String getStagingMode() {
      if (!this._isSet(15)) {
         try {
            return DeployHelper.determineDefaultStagingMode(this.getParent().getName()) == "nostage" ? "nostage" : (DeployHelper.determineDefaultStagingMode(this.getParent().getName()) == ServerMBean.DEFAULT_STAGE ? ApplicationMBean.DEFAULT_STAGE : "stage");
         } catch (NullPointerException var2) {
         }
      }

      return this._customizer.getStagingMode();
   }

   public boolean isStagingModeSet() {
      return this._isSet(15);
   }

   public void setStagingMode(String var1) throws ManagementException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{ApplicationMBean.DEFAULT_STAGE, "nostage", "stage", "external_stage"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("StagingMode", var1, var2);
      String var3 = this.getStagingMode();
      this._customizer.setStagingMode(var1);
      this._postSet(15, var3, var1);
   }

   public boolean stagingEnabled(String var1) {
      return this._customizer.stagingEnabled(var1);
   }

   public boolean staged(String var1) {
      return this._customizer.staged(var1);
   }

   public boolean useStagingDirectory(String var1) {
      return this._customizer.useStagingDirectory(var1);
   }

   public void sendAppLevelNotification(String var1, String var2, String var3) {
      this._customizer.sendAppLevelNotification(var1, var2, var3);
   }

   public void sendModuleNotification(String var1, String var2, String var3, String var4, String var5, String var6, long var7) {
      this._customizer.sendModuleNotification(var1, var2, var3, var4, var5, var6, var7);
   }

   public boolean isTwoPhase() {
      return this._customizer.isTwoPhase();
   }

   public boolean isTwoPhaseSet() {
      return this._isSet(16);
   }

   public void setTwoPhase(boolean var1) {
      boolean var2 = this._TwoPhase;
      this._TwoPhase = var1;
      this._postSet(16, var2, var1);
   }

   public int getLoadOrder() {
      return this._LoadOrder;
   }

   public boolean isLoadOrderSet() {
      return this._isSet(17);
   }

   public void setLoadOrder(int var1) {
      int var2 = this._LoadOrder;
      this._LoadOrder = var1;
      this._postSet(17, var2, var1);
   }

   public String getDeploymentType() {
      return this._customizer.getDeploymentType();
   }

   public boolean isDeploymentTypeSet() {
      return this._isSet(18);
   }

   public void setDeploymentType(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{ApplicationMBean.TYPE_EAR, ApplicationMBean.TYPE_EXPLODED_EAR, ApplicationMBean.TYPE_COMPONENT, ApplicationMBean.TYPE_EXPLODED_COMPONENT, ApplicationMBean.TYPE_UNKNOWN};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("DeploymentType", var1, var2);
      this._customizer.setDeploymentType(var1);
   }

   public int getDeploymentTimeout() {
      return this._DeploymentTimeout;
   }

   public boolean isDeploymentTimeoutSet() {
      return this._isSet(19);
   }

   public void setDeploymentTimeout(int var1) {
      int var2 = this._DeploymentTimeout;
      this._DeploymentTimeout = var1;
      this._postSet(19, var2, var1);
   }

   public String getAltDescriptorPath() {
      return this._AltDescriptorPath;
   }

   public boolean isAltDescriptorPathSet() {
      return this._isSet(20);
   }

   public void setAltDescriptorPath(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._AltDescriptorPath;
      this._AltDescriptorPath = var1;
      this._postSet(20, var2, var1);
   }

   public String getAltWLSDescriptorPath() {
      return this._AltWLSDescriptorPath;
   }

   public boolean isAltWLSDescriptorPathSet() {
      return this._isSet(21);
   }

   public void setAltWLSDescriptorPath(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._AltWLSDescriptorPath;
      this._AltWLSDescriptorPath = var1;
      this._postSet(21, var2, var1);
   }

   public void refreshDDsIfNeeded(String[] var1, String[] var2) {
      this._customizer.refreshDDsIfNeeded(var1, var2);
   }

   public boolean isDeployed() {
      return this._customizer.isDeployed();
   }

   public boolean isDeployedSet() {
      return this._isSet(22);
   }

   public void setDeployed(boolean var1) {
      boolean var2 = this.isDeployed();
      this._customizer.setDeployed(var1);
      this._postSet(22, var2, var1);
   }

   public EJBComponentMBean createEJBComponent(String var1) {
      EJBComponentMBeanImpl var2 = new EJBComponentMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addEJBComponent(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyEJBComponent(EJBComponentMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 23);
         EJBComponentMBean[] var2 = this.getEJBComponents();
         EJBComponentMBean[] var3 = (EJBComponentMBean[])((EJBComponentMBean[])this._getHelper()._removeElement(var2, EJBComponentMBean.class, var1));
         if (var2.length != var3.length) {
            this._preDestroy((AbstractDescriptorBean)var1);

            try {
               AbstractDescriptorBean var4 = (AbstractDescriptorBean)var1;
               if (var4 == null) {
                  return;
               }

               List var5 = this._getReferenceManager().getResolvedReferences(var4);
               if (var5 != null && var5.size() > 0) {
                  throw new BeanRemoveRejectedException(var4, var5);
               }

               this._getReferenceManager().unregisterBean(var4);
               this._markDestroyed(var4);
               this.setEJBComponents(var3);
            } catch (Exception var6) {
               if (var6 instanceof RuntimeException) {
                  throw (RuntimeException)var6;
               }

               throw new UndeclaredThrowableException(var6);
            }
         }

      } catch (Exception var7) {
         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         } else {
            throw new UndeclaredThrowableException(var7);
         }
      }
   }

   public EJBComponentMBean lookupEJBComponent(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._EJBComponents).iterator();

      EJBComponentMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (EJBComponentMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addEJBComponent(EJBComponentMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 23)) {
         EJBComponentMBean[] var2;
         if (this._isSet(23)) {
            var2 = (EJBComponentMBean[])((EJBComponentMBean[])this._getHelper()._extendArray(this.getEJBComponents(), EJBComponentMBean.class, var1));
         } else {
            var2 = new EJBComponentMBean[]{var1};
         }

         try {
            this.setEJBComponents(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public EJBComponentMBean[] getEJBComponents() {
      return this._EJBComponents;
   }

   public boolean isEJBComponentsSet() {
      return this._isSet(23);
   }

   public void removeEJBComponent(EJBComponentMBean var1) {
      this.destroyEJBComponent(var1);
   }

   public void setEJBComponents(EJBComponentMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new EJBComponentMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 23)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      EJBComponentMBean[] var5 = this._EJBComponents;
      this._EJBComponents = (EJBComponentMBean[])var4;
      this._postSet(23, var5, var4);
   }

   public ConnectorComponentMBean createConnectorComponent(String var1) {
      ConnectorComponentMBeanImpl var2 = new ConnectorComponentMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addConnectorComponent(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyConnectorComponent(ConnectorComponentMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 24);
         ConnectorComponentMBean[] var2 = this.getConnectorComponents();
         ConnectorComponentMBean[] var3 = (ConnectorComponentMBean[])((ConnectorComponentMBean[])this._getHelper()._removeElement(var2, ConnectorComponentMBean.class, var1));
         if (var2.length != var3.length) {
            this._preDestroy((AbstractDescriptorBean)var1);

            try {
               AbstractDescriptorBean var4 = (AbstractDescriptorBean)var1;
               if (var4 == null) {
                  return;
               }

               List var5 = this._getReferenceManager().getResolvedReferences(var4);
               if (var5 != null && var5.size() > 0) {
                  throw new BeanRemoveRejectedException(var4, var5);
               }

               this._getReferenceManager().unregisterBean(var4);
               this._markDestroyed(var4);
               this.setConnectorComponents(var3);
            } catch (Exception var6) {
               if (var6 instanceof RuntimeException) {
                  throw (RuntimeException)var6;
               }

               throw new UndeclaredThrowableException(var6);
            }
         }

      } catch (Exception var7) {
         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         } else {
            throw new UndeclaredThrowableException(var7);
         }
      }
   }

   public ConnectorComponentMBean lookupConnectorComponent(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._ConnectorComponents).iterator();

      ConnectorComponentMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (ConnectorComponentMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addConnectorComponent(ConnectorComponentMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 24)) {
         ConnectorComponentMBean[] var2;
         if (this._isSet(24)) {
            var2 = (ConnectorComponentMBean[])((ConnectorComponentMBean[])this._getHelper()._extendArray(this.getConnectorComponents(), ConnectorComponentMBean.class, var1));
         } else {
            var2 = new ConnectorComponentMBean[]{var1};
         }

         try {
            this.setConnectorComponents(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public ConnectorComponentMBean[] getConnectorComponents() {
      return this._ConnectorComponents;
   }

   public boolean isConnectorComponentsSet() {
      return this._isSet(24);
   }

   public void removeConnectorComponent(ConnectorComponentMBean var1) {
      this.destroyConnectorComponent(var1);
   }

   public void setConnectorComponents(ConnectorComponentMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new ConnectorComponentMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 24)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      ConnectorComponentMBean[] var5 = this._ConnectorComponents;
      this._ConnectorComponents = (ConnectorComponentMBean[])var4;
      this._postSet(24, var5, var4);
   }

   public WebServiceComponentMBean createWebServiceComponent(String var1) {
      WebServiceComponentMBeanImpl var2 = new WebServiceComponentMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addWebServiceComponent(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyWebServiceComponent(WebServiceComponentMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 25);
         WebServiceComponentMBean[] var2 = this.getWebServiceComponents();
         WebServiceComponentMBean[] var3 = (WebServiceComponentMBean[])((WebServiceComponentMBean[])this._getHelper()._removeElement(var2, WebServiceComponentMBean.class, var1));
         if (var2.length != var3.length) {
            this._preDestroy((AbstractDescriptorBean)var1);

            try {
               AbstractDescriptorBean var4 = (AbstractDescriptorBean)var1;
               if (var4 == null) {
                  return;
               }

               List var5 = this._getReferenceManager().getResolvedReferences(var4);
               if (var5 != null && var5.size() > 0) {
                  throw new BeanRemoveRejectedException(var4, var5);
               }

               this._getReferenceManager().unregisterBean(var4);
               this._markDestroyed(var4);
               this.setWebServiceComponents(var3);
            } catch (Exception var6) {
               if (var6 instanceof RuntimeException) {
                  throw (RuntimeException)var6;
               }

               throw new UndeclaredThrowableException(var6);
            }
         }

      } catch (Exception var7) {
         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         } else {
            throw new UndeclaredThrowableException(var7);
         }
      }
   }

   public ComponentMBean createDummyComponent(String var1) {
      ComponentMBeanImpl var2 = new ComponentMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addComponent(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public WebServiceComponentMBean lookupWebServiceComponent(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._WebServiceComponents).iterator();

      WebServiceComponentMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (WebServiceComponentMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addWebServiceComponent(WebServiceComponentMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 25)) {
         WebServiceComponentMBean[] var2;
         if (this._isSet(25)) {
            var2 = (WebServiceComponentMBean[])((WebServiceComponentMBean[])this._getHelper()._extendArray(this.getWebServiceComponents(), WebServiceComponentMBean.class, var1));
         } else {
            var2 = new WebServiceComponentMBean[]{var1};
         }

         try {
            this.setWebServiceComponents(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public WebServiceComponentMBean[] getWebServiceComponents() {
      return this._WebServiceComponents;
   }

   public boolean isWebServiceComponentsSet() {
      return this._isSet(25);
   }

   public void removeWebServiceComponent(WebServiceComponentMBean var1) {
      this.destroyWebServiceComponent(var1);
   }

   public void setWebServiceComponents(WebServiceComponentMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new WebServiceComponentMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 25)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      WebServiceComponentMBean[] var5 = this._WebServiceComponents;
      this._WebServiceComponents = (WebServiceComponentMBean[])var4;
      this._postSet(25, var5, var4);
   }

   public JDBCPoolComponentMBean createJDBCPoolComponent(String var1) {
      JDBCPoolComponentMBeanImpl var2 = new JDBCPoolComponentMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addJDBCPoolComponent(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyJDBCPoolComponent(JDBCPoolComponentMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 26);
         JDBCPoolComponentMBean[] var2 = this.getJDBCPoolComponents();
         JDBCPoolComponentMBean[] var3 = (JDBCPoolComponentMBean[])((JDBCPoolComponentMBean[])this._getHelper()._removeElement(var2, JDBCPoolComponentMBean.class, var1));
         if (var2.length != var3.length) {
            this._preDestroy((AbstractDescriptorBean)var1);

            try {
               AbstractDescriptorBean var4 = (AbstractDescriptorBean)var1;
               if (var4 == null) {
                  return;
               }

               List var5 = this._getReferenceManager().getResolvedReferences(var4);
               if (var5 != null && var5.size() > 0) {
                  throw new BeanRemoveRejectedException(var4, var5);
               }

               this._getReferenceManager().unregisterBean(var4);
               this._markDestroyed(var4);
               this.setJDBCPoolComponents(var3);
            } catch (Exception var6) {
               if (var6 instanceof RuntimeException) {
                  throw (RuntimeException)var6;
               }

               throw new UndeclaredThrowableException(var6);
            }
         }

      } catch (Exception var7) {
         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         } else {
            throw new UndeclaredThrowableException(var7);
         }
      }
   }

   public JDBCPoolComponentMBean lookupJDBCPoolComponent(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._JDBCPoolComponents).iterator();

      JDBCPoolComponentMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (JDBCPoolComponentMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addJDBCPoolComponent(JDBCPoolComponentMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 26)) {
         JDBCPoolComponentMBean[] var2;
         if (this._isSet(26)) {
            var2 = (JDBCPoolComponentMBean[])((JDBCPoolComponentMBean[])this._getHelper()._extendArray(this.getJDBCPoolComponents(), JDBCPoolComponentMBean.class, var1));
         } else {
            var2 = new JDBCPoolComponentMBean[]{var1};
         }

         try {
            this.setJDBCPoolComponents(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public JDBCPoolComponentMBean[] getJDBCPoolComponents() {
      return this._JDBCPoolComponents;
   }

   public boolean isJDBCPoolComponentsSet() {
      return this._isSet(26);
   }

   public void removeJDBCPoolComponent(JDBCPoolComponentMBean var1) {
      this.destroyJDBCPoolComponent(var1);
   }

   public void setJDBCPoolComponents(JDBCPoolComponentMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new JDBCPoolComponentMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 26)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      JDBCPoolComponentMBean[] var5 = this._JDBCPoolComponents;
      this._JDBCPoolComponents = (JDBCPoolComponentMBean[])var4;
      this._postSet(26, var5, var4);
   }

   public int getInternalType() {
      return this._customizer.getInternalType();
   }

   public boolean isInternalTypeSet() {
      return this._isSet(27);
   }

   public void setInternalType(int var1) throws InvalidAttributeValueException {
      this._InternalType = var1;
   }

   public void setAppDeployment(AppDeploymentMBean var1) {
      this._AppDeployment = var1;
   }

   public AppDeploymentMBean getAppDeployment() {
      return this._customizer.getAppDeployment();
   }

   public boolean isAppDeploymentSet() {
      return this._isSet(28);
   }

   public AppDeploymentMBean returnDeployableUnit() {
      return this._customizer.returnDeployableUnit();
   }

   public void setDelegationEnabled(boolean var1) {
      this._customizer.setDelegationEnabled(var1);
   }

   public boolean isDelegationEnabled() {
      return this._customizer.isDelegationEnabled();
   }

   public boolean isDelegationEnabledSet() {
      return this._isSet(29);
   }

   public void addHandler(DeploymentCompatibilityEventHandler var1) {
      this._customizer.addHandler(var1);
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
         var1 = 20;
      }

      try {
         switch (var1) {
            case 20:
               this._AltDescriptorPath = null;
               if (var2) {
                  break;
               }
            case 21:
               this._AltWLSDescriptorPath = null;
               if (var2) {
                  break;
               }
            case 28:
               this._AppDeployment = null;
               if (var2) {
                  break;
               }
            case 8:
               this._Components = new ComponentMBean[0];
               if (var2) {
                  break;
               }
            case 24:
               this._ConnectorComponents = new ConnectorComponentMBean[0];
               if (var2) {
                  break;
               }
            case 19:
               this._DeploymentTimeout = 3600000;
               if (var2) {
                  break;
               }
            case 18:
               this._customizer.setDeploymentType(ApplicationMBean.TYPE_UNKNOWN);
               if (var2) {
                  break;
               }
            case 23:
               this._EJBComponents = new EJBComponentMBean[0];
               if (var2) {
                  break;
               }
            case 11:
               this._FullPath = null;
               if (var2) {
                  break;
               }
            case 27:
               this._InternalType = 0;
               if (var2) {
                  break;
               }
            case 26:
               this._JDBCPoolComponents = new JDBCPoolComponentMBean[0];
               if (var2) {
                  break;
               }
            case 17:
               this._LoadOrder = 100;
               if (var2) {
                  break;
               }
            case 2:
               this._customizer.setName((String)null);
               if (var2) {
                  break;
               }
            case 3:
               this._customizer.setNotes((String)null);
               if (var2) {
                  break;
               }
            case 7:
               this._customizer.setPath((String)null);
               if (var2) {
                  break;
               }
            case 14:
               this._StagedTargets = new String[0];
               if (var2) {
                  break;
               }
            case 15:
               this._customizer.setStagingMode((String)null);
               if (var2) {
                  break;
               }
            case 13:
               this._StagingPath = null;
               if (var2) {
                  break;
               }
            case 9:
               this._WebAppComponents = new WebAppComponentMBean[0];
               if (var2) {
                  break;
               }
            case 25:
               this._WebServiceComponents = new WebServiceComponentMBean[0];
               if (var2) {
                  break;
               }
            case 29:
               this._customizer.setDelegationEnabled(false);
               if (var2) {
                  break;
               }
            case 22:
               this._customizer.setDeployed(false);
               if (var2) {
                  break;
               }
            case 10:
               this._Ear = false;
               if (var2) {
                  break;
               }
            case 12:
               this._customizer.setInternalApp(false);
               if (var2) {
                  break;
               }
            case 16:
               this._TwoPhase = true;
               if (var2) {
                  break;
               }
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
      return "Application";
   }

   public void putValue(String var1, Object var2) {
      String var7;
      if (var1.equals("AltDescriptorPath")) {
         var7 = this._AltDescriptorPath;
         this._AltDescriptorPath = (String)var2;
         this._postSet(20, var7, this._AltDescriptorPath);
      } else if (var1.equals("AltWLSDescriptorPath")) {
         var7 = this._AltWLSDescriptorPath;
         this._AltWLSDescriptorPath = (String)var2;
         this._postSet(21, var7, this._AltWLSDescriptorPath);
      } else if (var1.equals("AppDeployment")) {
         AppDeploymentMBean var14 = this._AppDeployment;
         this._AppDeployment = (AppDeploymentMBean)var2;
         this._postSet(28, var14, this._AppDeployment);
      } else if (var1.equals("Components")) {
         ComponentMBean[] var13 = this._Components;
         this._Components = (ComponentMBean[])((ComponentMBean[])var2);
         this._postSet(8, var13, this._Components);
      } else if (var1.equals("ConnectorComponents")) {
         ConnectorComponentMBean[] var12 = this._ConnectorComponents;
         this._ConnectorComponents = (ConnectorComponentMBean[])((ConnectorComponentMBean[])var2);
         this._postSet(24, var12, this._ConnectorComponents);
      } else {
         boolean var6;
         if (var1.equals("DelegationEnabled")) {
            var6 = this._DelegationEnabled;
            this._DelegationEnabled = (Boolean)var2;
            this._postSet(29, var6, this._DelegationEnabled);
         } else if (var1.equals("Deployed")) {
            var6 = this._Deployed;
            this._Deployed = (Boolean)var2;
            this._postSet(22, var6, this._Deployed);
         } else {
            int var9;
            if (var1.equals("DeploymentTimeout")) {
               var9 = this._DeploymentTimeout;
               this._DeploymentTimeout = (Integer)var2;
               this._postSet(19, var9, this._DeploymentTimeout);
            } else if (var1.equals("DeploymentType")) {
               var7 = this._DeploymentType;
               this._DeploymentType = (String)var2;
               this._postSet(18, var7, this._DeploymentType);
            } else if (var1.equals("EJBComponents")) {
               EJBComponentMBean[] var11 = this._EJBComponents;
               this._EJBComponents = (EJBComponentMBean[])((EJBComponentMBean[])var2);
               this._postSet(23, var11, this._EJBComponents);
            } else if (var1.equals("Ear")) {
               var6 = this._Ear;
               this._Ear = (Boolean)var2;
               this._postSet(10, var6, this._Ear);
            } else if (var1.equals("FullPath")) {
               var7 = this._FullPath;
               this._FullPath = (String)var2;
               this._postSet(11, var7, this._FullPath);
            } else if (var1.equals("InternalApp")) {
               var6 = this._InternalApp;
               this._InternalApp = (Boolean)var2;
               this._postSet(12, var6, this._InternalApp);
            } else if (var1.equals("InternalType")) {
               var9 = this._InternalType;
               this._InternalType = (Integer)var2;
               this._postSet(27, var9, this._InternalType);
            } else if (var1.equals("JDBCPoolComponents")) {
               JDBCPoolComponentMBean[] var10 = this._JDBCPoolComponents;
               this._JDBCPoolComponents = (JDBCPoolComponentMBean[])((JDBCPoolComponentMBean[])var2);
               this._postSet(26, var10, this._JDBCPoolComponents);
            } else if (var1.equals("LoadOrder")) {
               var9 = this._LoadOrder;
               this._LoadOrder = (Integer)var2;
               this._postSet(17, var9, this._LoadOrder);
            } else if (var1.equals("Name")) {
               var7 = this._Name;
               this._Name = (String)var2;
               this._postSet(2, var7, this._Name);
            } else if (var1.equals("Notes")) {
               var7 = this._Notes;
               this._Notes = (String)var2;
               this._postSet(3, var7, this._Notes);
            } else if (var1.equals("Path")) {
               var7 = this._Path;
               this._Path = (String)var2;
               this._postSet(7, var7, this._Path);
            } else if (var1.equals("StagedTargets")) {
               String[] var8 = this._StagedTargets;
               this._StagedTargets = (String[])((String[])var2);
               this._postSet(14, var8, this._StagedTargets);
            } else if (var1.equals("StagingMode")) {
               var7 = this._StagingMode;
               this._StagingMode = (String)var2;
               this._postSet(15, var7, this._StagingMode);
            } else if (var1.equals("StagingPath")) {
               var7 = this._StagingPath;
               this._StagingPath = (String)var2;
               this._postSet(13, var7, this._StagingPath);
            } else if (var1.equals("TwoPhase")) {
               var6 = this._TwoPhase;
               this._TwoPhase = (Boolean)var2;
               this._postSet(16, var6, this._TwoPhase);
            } else if (var1.equals("WebAppComponents")) {
               WebAppComponentMBean[] var5 = this._WebAppComponents;
               this._WebAppComponents = (WebAppComponentMBean[])((WebAppComponentMBean[])var2);
               this._postSet(9, var5, this._WebAppComponents);
            } else if (var1.equals("WebServiceComponents")) {
               WebServiceComponentMBean[] var4 = this._WebServiceComponents;
               this._WebServiceComponents = (WebServiceComponentMBean[])((WebServiceComponentMBean[])var2);
               this._postSet(25, var4, this._WebServiceComponents);
            } else if (var1.equals("customizer")) {
               Application var3 = this._customizer;
               this._customizer = (Application)var2;
            } else {
               super.putValue(var1, var2);
            }
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("AltDescriptorPath")) {
         return this._AltDescriptorPath;
      } else if (var1.equals("AltWLSDescriptorPath")) {
         return this._AltWLSDescriptorPath;
      } else if (var1.equals("AppDeployment")) {
         return this._AppDeployment;
      } else if (var1.equals("Components")) {
         return this._Components;
      } else if (var1.equals("ConnectorComponents")) {
         return this._ConnectorComponents;
      } else if (var1.equals("DelegationEnabled")) {
         return new Boolean(this._DelegationEnabled);
      } else if (var1.equals("Deployed")) {
         return new Boolean(this._Deployed);
      } else if (var1.equals("DeploymentTimeout")) {
         return new Integer(this._DeploymentTimeout);
      } else if (var1.equals("DeploymentType")) {
         return this._DeploymentType;
      } else if (var1.equals("EJBComponents")) {
         return this._EJBComponents;
      } else if (var1.equals("Ear")) {
         return new Boolean(this._Ear);
      } else if (var1.equals("FullPath")) {
         return this._FullPath;
      } else if (var1.equals("InternalApp")) {
         return new Boolean(this._InternalApp);
      } else if (var1.equals("InternalType")) {
         return new Integer(this._InternalType);
      } else if (var1.equals("JDBCPoolComponents")) {
         return this._JDBCPoolComponents;
      } else if (var1.equals("LoadOrder")) {
         return new Integer(this._LoadOrder);
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("Notes")) {
         return this._Notes;
      } else if (var1.equals("Path")) {
         return this._Path;
      } else if (var1.equals("StagedTargets")) {
         return this._StagedTargets;
      } else if (var1.equals("StagingMode")) {
         return this._StagingMode;
      } else if (var1.equals("StagingPath")) {
         return this._StagingPath;
      } else if (var1.equals("TwoPhase")) {
         return new Boolean(this._TwoPhase);
      } else if (var1.equals("WebAppComponents")) {
         return this._WebAppComponents;
      } else if (var1.equals("WebServiceComponents")) {
         return this._WebServiceComponents;
      } else {
         return var1.equals("customizer") ? this._customizer : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 3:
               if (var1.equals("ear")) {
                  return 10;
               }
               break;
            case 4:
               if (var1.equals("name")) {
                  return 2;
               }

               if (var1.equals("path")) {
                  return 7;
               }
               break;
            case 5:
               if (var1.equals("notes")) {
                  return 3;
               }
            case 6:
            case 7:
            case 11:
            case 16:
            case 20:
            default:
               break;
            case 8:
               if (var1.equals("deployed")) {
                  return 22;
               }
               break;
            case 9:
               if (var1.equals("component")) {
                  return 8;
               }

               if (var1.equals("full-path")) {
                  return 11;
               }

               if (var1.equals("two-phase")) {
                  return 16;
               }
               break;
            case 10:
               if (var1.equals("load-order")) {
                  return 17;
               }
               break;
            case 12:
               if (var1.equals("staging-mode")) {
                  return 15;
               }

               if (var1.equals("staging-path")) {
                  return 13;
               }

               if (var1.equals("internal-app")) {
                  return 12;
               }
               break;
            case 13:
               if (var1.equals("ejb-component")) {
                  return 23;
               }

               if (var1.equals("internal-type")) {
                  return 27;
               }

               if (var1.equals("staged-target")) {
                  return 14;
               }
               break;
            case 14:
               if (var1.equals("app-deployment")) {
                  return 28;
               }
               break;
            case 15:
               if (var1.equals("deployment-type")) {
                  return 18;
               }
               break;
            case 17:
               if (var1.equals("web-app-component")) {
                  return 9;
               }
               break;
            case 18:
               if (var1.equals("deployment-timeout")) {
                  return 19;
               }

               if (var1.equals("delegation-enabled")) {
                  return 29;
               }
               break;
            case 19:
               if (var1.equals("alt-descriptor-path")) {
                  return 20;
               }

               if (var1.equals("connector-component")) {
                  return 24;
               }

               if (var1.equals("jdbc-pool-component")) {
                  return 26;
               }
               break;
            case 21:
               if (var1.equals("web-service-component")) {
                  return 25;
               }
               break;
            case 22:
               if (var1.equals("altwls-descriptor-path")) {
                  return 21;
               }
         }

         return super.getPropertyIndex(var1);
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            case 9:
               return new WebAppComponentMBeanImpl.SchemaHelper2();
            case 23:
               return new EJBComponentMBeanImpl.SchemaHelper2();
            case 24:
               return new ConnectorComponentMBeanImpl.SchemaHelper2();
            case 25:
               return new WebServiceComponentMBeanImpl.SchemaHelper2();
            case 26:
               return new JDBCPoolComponentMBeanImpl.SchemaHelper2();
            default:
               return super.getSchemaHelper(var1);
         }
      }

      public String getElementName(int var1) {
         switch (var1) {
            case 2:
               return "name";
            case 3:
               return "notes";
            case 4:
            case 5:
            case 6:
            default:
               return super.getElementName(var1);
            case 7:
               return "path";
            case 8:
               return "component";
            case 9:
               return "web-app-component";
            case 10:
               return "ear";
            case 11:
               return "full-path";
            case 12:
               return "internal-app";
            case 13:
               return "staging-path";
            case 14:
               return "staged-target";
            case 15:
               return "staging-mode";
            case 16:
               return "two-phase";
            case 17:
               return "load-order";
            case 18:
               return "deployment-type";
            case 19:
               return "deployment-timeout";
            case 20:
               return "alt-descriptor-path";
            case 21:
               return "altwls-descriptor-path";
            case 22:
               return "deployed";
            case 23:
               return "ejb-component";
            case 24:
               return "connector-component";
            case 25:
               return "web-service-component";
            case 26:
               return "jdbc-pool-component";
            case 27:
               return "internal-type";
            case 28:
               return "app-deployment";
            case 29:
               return "delegation-enabled";
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 8:
               return true;
            case 9:
               return true;
            case 10:
            case 11:
            case 12:
            case 13:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            default:
               return super.isArray(var1);
            case 14:
               return true;
            case 23:
               return true;
            case 24:
               return true;
            case 25:
               return true;
            case 26:
               return true;
         }
      }

      public boolean isBean(int var1) {
         switch (var1) {
            case 9:
               return true;
            case 23:
               return true;
            case 24:
               return true;
            case 25:
               return true;
            case 26:
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

   protected static class Helper extends ConfigurationMBeanImpl.Helper {
      private ApplicationMBeanImpl bean;

      protected Helper(ApplicationMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 2:
               return "Name";
            case 3:
               return "Notes";
            case 4:
            case 5:
            case 6:
            default:
               return super.getPropertyName(var1);
            case 7:
               return "Path";
            case 8:
               return "Components";
            case 9:
               return "WebAppComponents";
            case 10:
               return "Ear";
            case 11:
               return "FullPath";
            case 12:
               return "InternalApp";
            case 13:
               return "StagingPath";
            case 14:
               return "StagedTargets";
            case 15:
               return "StagingMode";
            case 16:
               return "TwoPhase";
            case 17:
               return "LoadOrder";
            case 18:
               return "DeploymentType";
            case 19:
               return "DeploymentTimeout";
            case 20:
               return "AltDescriptorPath";
            case 21:
               return "AltWLSDescriptorPath";
            case 22:
               return "Deployed";
            case 23:
               return "EJBComponents";
            case 24:
               return "ConnectorComponents";
            case 25:
               return "WebServiceComponents";
            case 26:
               return "JDBCPoolComponents";
            case 27:
               return "InternalType";
            case 28:
               return "AppDeployment";
            case 29:
               return "DelegationEnabled";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("AltDescriptorPath")) {
            return 20;
         } else if (var1.equals("AltWLSDescriptorPath")) {
            return 21;
         } else if (var1.equals("AppDeployment")) {
            return 28;
         } else if (var1.equals("Components")) {
            return 8;
         } else if (var1.equals("ConnectorComponents")) {
            return 24;
         } else if (var1.equals("DeploymentTimeout")) {
            return 19;
         } else if (var1.equals("DeploymentType")) {
            return 18;
         } else if (var1.equals("EJBComponents")) {
            return 23;
         } else if (var1.equals("FullPath")) {
            return 11;
         } else if (var1.equals("InternalType")) {
            return 27;
         } else if (var1.equals("JDBCPoolComponents")) {
            return 26;
         } else if (var1.equals("LoadOrder")) {
            return 17;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("Notes")) {
            return 3;
         } else if (var1.equals("Path")) {
            return 7;
         } else if (var1.equals("StagedTargets")) {
            return 14;
         } else if (var1.equals("StagingMode")) {
            return 15;
         } else if (var1.equals("StagingPath")) {
            return 13;
         } else if (var1.equals("WebAppComponents")) {
            return 9;
         } else if (var1.equals("WebServiceComponents")) {
            return 25;
         } else if (var1.equals("DelegationEnabled")) {
            return 29;
         } else if (var1.equals("Deployed")) {
            return 22;
         } else if (var1.equals("Ear")) {
            return 10;
         } else if (var1.equals("InternalApp")) {
            return 12;
         } else {
            return var1.equals("TwoPhase") ? 16 : super.getPropertyIndex(var1);
         }
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         var1.add(new ArrayIterator(this.bean.getConnectorComponents()));
         var1.add(new ArrayIterator(this.bean.getEJBComponents()));
         var1.add(new ArrayIterator(this.bean.getJDBCPoolComponents()));
         var1.add(new ArrayIterator(this.bean.getWebAppComponents()));
         var1.add(new ArrayIterator(this.bean.getWebServiceComponents()));
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
            if (this.bean.isAltDescriptorPathSet()) {
               var2.append("AltDescriptorPath");
               var2.append(String.valueOf(this.bean.getAltDescriptorPath()));
            }

            if (this.bean.isAltWLSDescriptorPathSet()) {
               var2.append("AltWLSDescriptorPath");
               var2.append(String.valueOf(this.bean.getAltWLSDescriptorPath()));
            }

            if (this.bean.isAppDeploymentSet()) {
               var2.append("AppDeployment");
               var2.append(String.valueOf(this.bean.getAppDeployment()));
            }

            if (this.bean.isComponentsSet()) {
               var2.append("Components");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getComponents())));
            }

            var5 = 0L;

            int var7;
            for(var7 = 0; var7 < this.bean.getConnectorComponents().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getConnectorComponents()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isDeploymentTimeoutSet()) {
               var2.append("DeploymentTimeout");
               var2.append(String.valueOf(this.bean.getDeploymentTimeout()));
            }

            if (this.bean.isDeploymentTypeSet()) {
               var2.append("DeploymentType");
               var2.append(String.valueOf(this.bean.getDeploymentType()));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getEJBComponents().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getEJBComponents()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isFullPathSet()) {
               var2.append("FullPath");
               var2.append(String.valueOf(this.bean.getFullPath()));
            }

            if (this.bean.isInternalTypeSet()) {
               var2.append("InternalType");
               var2.append(String.valueOf(this.bean.getInternalType()));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getJDBCPoolComponents().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getJDBCPoolComponents()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isLoadOrderSet()) {
               var2.append("LoadOrder");
               var2.append(String.valueOf(this.bean.getLoadOrder()));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isNotesSet()) {
               var2.append("Notes");
               var2.append(String.valueOf(this.bean.getNotes()));
            }

            if (this.bean.isPathSet()) {
               var2.append("Path");
               var2.append(String.valueOf(this.bean.getPath()));
            }

            if (this.bean.isStagedTargetsSet()) {
               var2.append("StagedTargets");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getStagedTargets())));
            }

            if (this.bean.isStagingModeSet()) {
               var2.append("StagingMode");
               var2.append(String.valueOf(this.bean.getStagingMode()));
            }

            if (this.bean.isStagingPathSet()) {
               var2.append("StagingPath");
               var2.append(String.valueOf(this.bean.getStagingPath()));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getWebAppComponents().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getWebAppComponents()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getWebServiceComponents().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getWebServiceComponents()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isDelegationEnabledSet()) {
               var2.append("DelegationEnabled");
               var2.append(String.valueOf(this.bean.isDelegationEnabled()));
            }

            if (this.bean.isDeployedSet()) {
               var2.append("Deployed");
               var2.append(String.valueOf(this.bean.isDeployed()));
            }

            if (this.bean.isEarSet()) {
               var2.append("Ear");
               var2.append(String.valueOf(this.bean.isEar()));
            }

            if (this.bean.isInternalAppSet()) {
               var2.append("InternalApp");
               var2.append(String.valueOf(this.bean.isInternalApp()));
            }

            if (this.bean.isTwoPhaseSet()) {
               var2.append("TwoPhase");
               var2.append(String.valueOf(this.bean.isTwoPhase()));
            }

            var1.update(var2.toString().getBytes());
            return var1.getValue();
         } catch (Exception var8) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var8);
         }
      }

      protected void computeDiff(AbstractDescriptorBean var1) {
         try {
            super.computeDiff(var1);
            ApplicationMBeanImpl var2 = (ApplicationMBeanImpl)var1;
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("AltDescriptorPath", this.bean.getAltDescriptorPath(), var2.getAltDescriptorPath(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("AltWLSDescriptorPath", this.bean.getAltWLSDescriptorPath(), var2.getAltWLSDescriptorPath(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeChildDiff("ConnectorComponents", this.bean.getConnectorComponents(), var2.getConnectorComponents(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("DeploymentTimeout", this.bean.getDeploymentTimeout(), var2.getDeploymentTimeout(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeChildDiff("EJBComponents", this.bean.getEJBComponents(), var2.getEJBComponents(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeChildDiff("JDBCPoolComponents", this.bean.getJDBCPoolComponents(), var2.getJDBCPoolComponents(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("LoadOrder", this.bean.getLoadOrder(), var2.getLoadOrder(), false);
            }

            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeDiff("Notes", this.bean.getNotes(), var2.getNotes(), true);
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("Path", this.bean.getPath(), var2.getPath(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("StagedTargets", this.bean.getStagedTargets(), var2.getStagedTargets(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("StagingMode", this.bean.getStagingMode(), var2.getStagingMode(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeChildDiff("WebAppComponents", this.bean.getWebAppComponents(), var2.getWebAppComponents(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeChildDiff("WebServiceComponents", this.bean.getWebServiceComponents(), var2.getWebServiceComponents(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("Deployed", this.bean.isDeployed(), var2.isDeployed(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("Ear", this.bean.isEar(), var2.isEar(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("TwoPhase", this.bean.isTwoPhase(), var2.isTwoPhase(), true);
            }

         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            ApplicationMBeanImpl var3 = (ApplicationMBeanImpl)var1.getSourceBean();
            ApplicationMBeanImpl var4 = (ApplicationMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("AltDescriptorPath")) {
                  var3.setAltDescriptorPath(var4.getAltDescriptorPath());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 20);
               } else if (var5.equals("AltWLSDescriptorPath")) {
                  var3.setAltWLSDescriptorPath(var4.getAltWLSDescriptorPath());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 21);
               } else if (!var5.equals("AppDeployment") && !var5.equals("Components")) {
                  if (var5.equals("ConnectorComponents")) {
                     if (var6 == 2) {
                        var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                        var3.addConnectorComponent((ConnectorComponentMBean)var2.getAddedObject());
                     } else {
                        if (var6 != 3) {
                           throw new AssertionError("Invalid type: " + var6);
                        }

                        var3.removeConnectorComponent((ConnectorComponentMBean)var2.getRemovedObject());
                     }

                     if (var3.getConnectorComponents() == null || var3.getConnectorComponents().length == 0) {
                        var3._conditionalUnset(var2.isUnsetUpdate(), 24);
                     }
                  } else if (var5.equals("DeploymentTimeout")) {
                     var3.setDeploymentTimeout(var4.getDeploymentTimeout());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 19);
                  } else if (!var5.equals("DeploymentType")) {
                     if (var5.equals("EJBComponents")) {
                        if (var6 == 2) {
                           var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                           var3.addEJBComponent((EJBComponentMBean)var2.getAddedObject());
                        } else {
                           if (var6 != 3) {
                              throw new AssertionError("Invalid type: " + var6);
                           }

                           var3.removeEJBComponent((EJBComponentMBean)var2.getRemovedObject());
                        }

                        if (var3.getEJBComponents() == null || var3.getEJBComponents().length == 0) {
                           var3._conditionalUnset(var2.isUnsetUpdate(), 23);
                        }
                     } else if (!var5.equals("FullPath") && !var5.equals("InternalType")) {
                        if (var5.equals("JDBCPoolComponents")) {
                           if (var6 == 2) {
                              var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                              var3.addJDBCPoolComponent((JDBCPoolComponentMBean)var2.getAddedObject());
                           } else {
                              if (var6 != 3) {
                                 throw new AssertionError("Invalid type: " + var6);
                              }

                              var3.removeJDBCPoolComponent((JDBCPoolComponentMBean)var2.getRemovedObject());
                           }

                           if (var3.getJDBCPoolComponents() == null || var3.getJDBCPoolComponents().length == 0) {
                              var3._conditionalUnset(var2.isUnsetUpdate(), 26);
                           }
                        } else if (var5.equals("LoadOrder")) {
                           var3.setLoadOrder(var4.getLoadOrder());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 17);
                        } else if (var5.equals("Name")) {
                           var3.setName(var4.getName());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 2);
                        } else if (var5.equals("Notes")) {
                           var3.setNotes(var4.getNotes());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 3);
                        } else if (var5.equals("Path")) {
                           var3.setPath(var4.getPath());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 7);
                        } else if (var5.equals("StagedTargets")) {
                           if (var6 == 2) {
                              var2.resetAddedObject(var2.getAddedObject());
                              var3.addStagedTarget((String)var2.getAddedObject());
                           } else {
                              if (var6 != 3) {
                                 throw new AssertionError("Invalid type: " + var6);
                              }

                              var3.removeStagedTarget((String)var2.getRemovedObject());
                           }

                           if (var3.getStagedTargets() == null || var3.getStagedTargets().length == 0) {
                              var3._conditionalUnset(var2.isUnsetUpdate(), 14);
                           }
                        } else if (var5.equals("StagingMode")) {
                           var3.setStagingMode(var4.getStagingMode());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 15);
                        } else if (!var5.equals("StagingPath")) {
                           if (var5.equals("WebAppComponents")) {
                              if (var6 == 2) {
                                 var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                                 var3.addWebAppComponent((WebAppComponentMBean)var2.getAddedObject());
                              } else {
                                 if (var6 != 3) {
                                    throw new AssertionError("Invalid type: " + var6);
                                 }

                                 var3.removeWebAppComponent((WebAppComponentMBean)var2.getRemovedObject());
                              }

                              if (var3.getWebAppComponents() == null || var3.getWebAppComponents().length == 0) {
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 9);
                              }
                           } else if (var5.equals("WebServiceComponents")) {
                              if (var6 == 2) {
                                 var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                                 var3.addWebServiceComponent((WebServiceComponentMBean)var2.getAddedObject());
                              } else {
                                 if (var6 != 3) {
                                    throw new AssertionError("Invalid type: " + var6);
                                 }

                                 var3.removeWebServiceComponent((WebServiceComponentMBean)var2.getRemovedObject());
                              }

                              if (var3.getWebServiceComponents() == null || var3.getWebServiceComponents().length == 0) {
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 25);
                              }
                           } else if (!var5.equals("DelegationEnabled")) {
                              if (var5.equals("Deployed")) {
                                 var3.setDeployed(var4.isDeployed());
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 22);
                              } else if (var5.equals("Ear")) {
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 10);
                              } else if (!var5.equals("InternalApp")) {
                                 if (var5.equals("TwoPhase")) {
                                    var3.setTwoPhase(var4.isTwoPhase());
                                    var3._conditionalUnset(var2.isUnsetUpdate(), 16);
                                 } else {
                                    super.applyPropertyUpdate(var1, var2);
                                 }
                              }
                           }
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
            ApplicationMBeanImpl var5 = (ApplicationMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if (var2 && (var3 == null || !var3.contains("AltDescriptorPath")) && this.bean.isAltDescriptorPathSet()) {
               var5.setAltDescriptorPath(this.bean.getAltDescriptorPath());
            }

            if (var2 && (var3 == null || !var3.contains("AltWLSDescriptorPath")) && this.bean.isAltWLSDescriptorPathSet()) {
               var5.setAltWLSDescriptorPath(this.bean.getAltWLSDescriptorPath());
            }

            int var8;
            if (var2 && (var3 == null || !var3.contains("ConnectorComponents")) && this.bean.isConnectorComponentsSet() && !var5._isSet(24)) {
               ConnectorComponentMBean[] var6 = this.bean.getConnectorComponents();
               ConnectorComponentMBean[] var7 = new ConnectorComponentMBean[var6.length];

               for(var8 = 0; var8 < var7.length; ++var8) {
                  var7[var8] = (ConnectorComponentMBean)((ConnectorComponentMBean)this.createCopy((AbstractDescriptorBean)var6[var8], var2));
               }

               var5.setConnectorComponents(var7);
            }

            if (var2 && (var3 == null || !var3.contains("DeploymentTimeout")) && this.bean.isDeploymentTimeoutSet()) {
               var5.setDeploymentTimeout(this.bean.getDeploymentTimeout());
            }

            if (var2 && (var3 == null || !var3.contains("EJBComponents")) && this.bean.isEJBComponentsSet() && !var5._isSet(23)) {
               EJBComponentMBean[] var11 = this.bean.getEJBComponents();
               EJBComponentMBean[] var14 = new EJBComponentMBean[var11.length];

               for(var8 = 0; var8 < var14.length; ++var8) {
                  var14[var8] = (EJBComponentMBean)((EJBComponentMBean)this.createCopy((AbstractDescriptorBean)var11[var8], var2));
               }

               var5.setEJBComponents(var14);
            }

            if (var2 && (var3 == null || !var3.contains("JDBCPoolComponents")) && this.bean.isJDBCPoolComponentsSet() && !var5._isSet(26)) {
               JDBCPoolComponentMBean[] var12 = this.bean.getJDBCPoolComponents();
               JDBCPoolComponentMBean[] var16 = new JDBCPoolComponentMBean[var12.length];

               for(var8 = 0; var8 < var16.length; ++var8) {
                  var16[var8] = (JDBCPoolComponentMBean)((JDBCPoolComponentMBean)this.createCopy((AbstractDescriptorBean)var12[var8], var2));
               }

               var5.setJDBCPoolComponents(var16);
            }

            if (var2 && (var3 == null || !var3.contains("LoadOrder")) && this.bean.isLoadOrderSet()) {
               var5.setLoadOrder(this.bean.getLoadOrder());
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if ((var3 == null || !var3.contains("Notes")) && this.bean.isNotesSet()) {
               var5.setNotes(this.bean.getNotes());
            }

            if (var2 && (var3 == null || !var3.contains("Path")) && this.bean.isPathSet()) {
               var5.setPath(this.bean.getPath());
            }

            if (var2 && (var3 == null || !var3.contains("StagedTargets")) && this.bean.isStagedTargetsSet()) {
               String[] var4 = this.bean.getStagedTargets();
               var5.setStagedTargets(var4 == null ? null : (String[])((String[])((String[])((String[])var4)).clone()));
            }

            if (var2 && (var3 == null || !var3.contains("StagingMode")) && this.bean.isStagingModeSet()) {
               var5.setStagingMode(this.bean.getStagingMode());
            }

            if (var2 && (var3 == null || !var3.contains("WebAppComponents")) && this.bean.isWebAppComponentsSet() && !var5._isSet(9)) {
               WebAppComponentMBean[] var13 = this.bean.getWebAppComponents();
               WebAppComponentMBean[] var17 = new WebAppComponentMBean[var13.length];

               for(var8 = 0; var8 < var17.length; ++var8) {
                  var17[var8] = (WebAppComponentMBean)((WebAppComponentMBean)this.createCopy((AbstractDescriptorBean)var13[var8], var2));
               }

               var5.setWebAppComponents(var17);
            }

            if (var2 && (var3 == null || !var3.contains("WebServiceComponents")) && this.bean.isWebServiceComponentsSet() && !var5._isSet(25)) {
               WebServiceComponentMBean[] var15 = this.bean.getWebServiceComponents();
               WebServiceComponentMBean[] var18 = new WebServiceComponentMBean[var15.length];

               for(var8 = 0; var8 < var18.length; ++var8) {
                  var18[var8] = (WebServiceComponentMBean)((WebServiceComponentMBean)this.createCopy((AbstractDescriptorBean)var15[var8], var2));
               }

               var5.setWebServiceComponents(var18);
            }

            if (var2 && (var3 == null || !var3.contains("Deployed")) && this.bean.isDeployedSet()) {
               var5.setDeployed(this.bean.isDeployed());
            }

            if (var2 && (var3 == null || !var3.contains("Ear")) && this.bean.isEarSet()) {
            }

            if (var2 && (var3 == null || !var3.contains("TwoPhase")) && this.bean.isTwoPhaseSet()) {
               var5.setTwoPhase(this.bean.isTwoPhase());
            }

            return var5;
         } catch (RuntimeException var9) {
            throw var9;
         } catch (Exception var10) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var10);
         }
      }

      protected void inferSubTree(Class var1, Object var2) {
         super.inferSubTree(var1, var2);
         Object var3 = null;
         this.inferSubTree(this.bean.getAppDeployment(), var1, var2);
         this.inferSubTree(this.bean.getComponents(), var1, var2);
         this.inferSubTree(this.bean.getConnectorComponents(), var1, var2);
         this.inferSubTree(this.bean.getEJBComponents(), var1, var2);
         this.inferSubTree(this.bean.getJDBCPoolComponents(), var1, var2);
         this.inferSubTree(this.bean.getWebAppComponents(), var1, var2);
         this.inferSubTree(this.bean.getWebServiceComponents(), var1, var2);
      }
   }
}
