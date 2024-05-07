package weblogic.deploy.api.spi.config;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.model.DDBeanRoot;
import javax.enterprise.deploy.model.DeployableObject;
import javax.enterprise.deploy.model.exceptions.DDBeanCreateException;
import javax.enterprise.deploy.shared.ModuleType;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.DConfigBeanRoot;
import javax.enterprise.deploy.spi.DeploymentConfiguration;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import javax.enterprise.deploy.spi.exceptions.InvalidModuleException;
import weblogic.deploy.api.internal.SPIDeployerLogger;
import weblogic.deploy.api.internal.utils.ConfigHelper;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.model.WebLogicDeployableObject;
import weblogic.deploy.api.model.internal.DDBeanRootImpl;
import weblogic.deploy.api.shared.WebLogicModuleType;
import weblogic.deploy.api.spi.WebLogicDConfigBeanRoot;
import weblogic.deploy.api.spi.WebLogicDeploymentConfiguration;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.DescriptorHelper;
import weblogic.j2ee.descriptor.EmptyBean;
import weblogic.j2ee.descriptor.wl.ConfigurationSupportBean;
import weblogic.j2ee.descriptor.wl.CustomModuleBean;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.ModuleDescriptorBean;
import weblogic.j2ee.descriptor.wl.VariableAssignmentBean;
import weblogic.j2ee.descriptor.wl.VariableBean;
import weblogic.j2ee.descriptor.wl.WeblogicExtensionBean;
import weblogic.logging.Loggable;
import weblogic.utils.ArrayUtils;

public abstract class BasicDConfigBeanRoot extends BasicDConfigBean implements WebLogicDConfigBeanRoot, PropertyChangeListener, VetoableChangeListener {
   private static final boolean debug = Debug.isDebug("config");
   private static final boolean allowNonConfigurableChanges = Boolean.getBoolean("weblogic.deploy.api.AllowNonConfigurableChanges");
   private static final String FROM_LISTENER = "FROM_LISTENER";
   protected DeployableObject dObject;
   private DescriptorParser dom = null;
   private WebLogicDeploymentConfiguration dc;
   private boolean ddProvided = true;
   private boolean isSchema = true;
   private String name;
   Map secondaryRoots = Collections.synchronizedMap(new HashMap());
   DescriptorSupport myds = null;
   private boolean external = false;
   private String namespace = null;
   private List bl = new ArrayList();
   private DescriptorHelper ddhelper = null;
   private List newBeans = new ArrayList();
   private boolean planBasedDBean = false;

   public BasicDConfigBeanRoot(DDBeanRoot var1, WebLogicDeploymentConfiguration var2, String var3, DescriptorSupport var4) throws IOException, InvalidModuleException {
      super(var1);
      ConfigHelper.checkParam("WebLogicDeploymentConfiguration", var2);
      ConfigHelper.checkParam("name", var3);
      ConfigHelper.checkParam("DescriptorSupport", var4);
      this.setDescriptorSupport(var4);
      this.setNamespace();
      this.dObject = var1.getDeployableObject();
      this.dc = var2;
      this.name = var3;
      if (var4.supportsConfigModules() || var4.isPrimary() && this.isAppRoot()) {
         this.addWebLogicExtensions();
      }

   }

   public void close() {
      Iterator var1 = this.secondaryRoots.values().iterator();

      while(var1.hasNext()) {
         WebLogicDConfigBeanRoot var2 = (WebLogicDConfigBeanRoot)var1.next();
         if (var2 != null) {
            var2.close();
         }
      }

      this.deregisterAsListener(this.getDescriptorBean());
      this.secondaryRoots.clear();
   }

   private boolean isAppRoot() {
      return this.dObject.equals(this.dc.getDeployableObject());
   }

   public BasicDConfigBeanRoot(DDBean var1) {
      super(var1);
   }

   public DConfigBean getDConfigBean(DDBeanRoot var1) {
      ConfigHelper.checkParam("DDBeanRoot", var1);
      if (debug) {
         Debug.say("looking up secondary dcb for " + var1.toString());
      }

      DescriptorSupport var2 = null;
      DConfigBeanRoot var3 = (DConfigBeanRoot)this.secondaryRoots.get(var1);
      if (var3 != null) {
         return var3;
      } else {
         try {
            label57: {
               String var4 = ((DeploymentConfigurationImpl)this.dc).getRootTag(var1);
               if (debug) {
                  Debug.say("Looking up #2 ds for tag " + var4);
               }

               DescriptorSupport[] var5 = DescriptorSupportManager.getForSecondaryTag(var4);
               if (var5 != null && var5.length != 0) {
                  int var6 = 0;

                  while(true) {
                     if (var6 >= var5.length) {
                        break label57;
                     }

                     if (debug) {
                        Debug.say("Checking " + var5[var6].getBaseURI() + " against " + var1.getFilename());
                     }

                     if (var5[var6].getBaseURI().equals(var1.getFilename())) {
                        var2 = var5[var6];
                        break label57;
                     }

                     ++var6;
                  }
               }

               return null;
            }
         } catch (ConfigurationException var8) {
            SPIDeployerLogger.logNoDCB(this.name, var8.toString());
            return null;
         }

         if (var2 == null) {
            return null;
         } else {
            try {
               return this.getDConfigBean(var1, var2);
            } catch (ConfigurationException var7) {
               SPIDeployerLogger.logDDCreateError(var2.getConfigURI());
               return null;
            }
         }
      }
   }

   private WebLogicDConfigBeanRoot findRoot(DDBeanRoot var1) {
      Iterator var2 = this.secondaryRoots.keySet().iterator();

      DDBeanRoot var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (DDBeanRoot)var2.next();
      } while(!var3.equals(var1));

      return (WebLogicDConfigBeanRoot)this.secondaryRoots.get(var3);
   }

   public DConfigBean getDConfigBean(DDBeanRoot var1, DescriptorSupport var2) throws ConfigurationException {
      WebLogicDConfigBeanRoot var3 = null;

      try {
         ConfigHelper.checkParam("DDBeanRoot", var1);
         ConfigHelper.checkParam("DescriptorSupport", var2);
         var3 = this.findRoot(var1);
         if (var3 != null) {
            return var3;
         } else {
            if (debug) {
               Debug.say("Creating secondary DCB for " + this.name + " at " + var2.getConfigTag() + " on ddbeanroot: \n" + var1);
            }

            var3 = ((DeploymentConfigurationImpl)this.dc).initDConfig(var1, this.name, var2);
            this.secondaryRoots.put(var1, var3);
            if (var3 != null) {
               ConfigHelper.beanWalker((DDBeanRoot)var1, (DConfigBeanRoot)var3);
               this.dc.addToPlan((WebLogicDConfigBeanRoot)var3);
            }

            return var3;
         }
      } catch (IOException var5) {
         throw (ConfigurationException)(new ConfigurationException(var5.getMessage())).initCause(var5);
      } catch (InvalidModuleException var6) {
         throw (ConfigurationException)(new ConfigurationException(var6.getMessage())).initCause(var6);
      }
   }

   public void export(int var1) throws IllegalArgumentException {
      if (!this.isSchemaBased()) {
         SPIDeployerLogger.logDTDDDExport(this.getDescriptorSupport().getConfigURI(), this.getName());
      } else {
         DescriptorHelper var2 = this.getDescriptorHelper();
         DeploymentPlanBean var3 = this.dc.getPlan();
         ModuleDescriptorBean var4 = var3.findModuleDescriptor(this.name, this.myds.getConfigURI());
         DescriptorBean[] var5 = null;
         String[] var6 = null;
         int var7;
         DescriptorBean var8;
         switch (var1) {
            case 0:
               var5 = var2.findDependencies(this.getDescriptorBean().getDescriptor());

               for(var7 = 0; var7 < var5.length; ++var7) {
                  var8 = var5[var7];
                  var6 = var2.findDependencies(var8);
                  this.createVariable(var6, var3, var4, var8);
               }

               return;
            case 1:
               var5 = var2.findDeclarations(this.getDescriptorBean().getDescriptor());

               for(var7 = 0; var7 < var5.length; ++var7) {
                  var8 = var5[var7];
                  var6 = var2.findDeclarations(var8);
                  this.createVariable(var6, var3, var4, var8);
               }

               return;
            case 2:
               var5 = var2.findConfigurables(this.getDescriptorBean().getDescriptor());

               for(var7 = 0; var7 < var5.length; ++var7) {
                  var8 = var5[var7];
                  var6 = var2.findConfigurables(var8);
                  this.createVariable(var6, var3, var4, var8);
               }

               return;
            case 3:
               var5 = var2.findChangables(this.getDescriptorBean().getDescriptor());

               for(var7 = 0; var7 < var5.length; ++var7) {
                  var8 = var5[var7];
                  var6 = var2.findChangables(var8);
                  this.createVariable(var6, var3, var4, var8);
               }

               return;
            case 4:
               var5 = var2.findDynamics(this.getDescriptorBean().getDescriptor());

               for(var7 = 0; var7 < var5.length; ++var7) {
                  var8 = var5[var7];
                  var6 = var2.findDynamics(var8);
                  this.createVariable(var6, var3, var4, var8);
               }

               return;
            default:
               throw new IllegalArgumentException(SPIDeployerLogger.invalidExport(var1));
         }
      }
   }

   private DescriptorHelper getDescriptorHelper() {
      if (this.ddhelper == null) {
         this.ddhelper = DescriptorHelper.getInstance();
      }

      return this.ddhelper;
   }

   private void createVariable(String[] var1, DeploymentPlanBean var2, ModuleDescriptorBean var3, DescriptorBean var4) {
      for(int var5 = 0; var5 < var1.length; ++var5) {
         String var6 = var1[var5];
         VariableBean var7 = null;

         try {
            var7 = var2.findVariable(var3, var4, var6);
            if (debug && var7 != null) {
               Debug.say("Variable on Plan is: " + var7.getName() + " = " + var7.getValue());
            }

            if (var7 == null) {
               var7 = var2.findOrCreateVariable(var3, var4, var6, this.getPlanBasedDBean());
               var7.setValue((String)null);
               if (debug) {
                  Debug.say("Created variable on plan: " + var7.getName() + " = " + var7.getValue());
               }
            }
         } catch (IllegalArgumentException var9) {
         }
      }

   }

   public void export(DescriptorBean var1, String[] var2) throws IllegalArgumentException {
      ConfigHelper.checkParam("DescriptorBean", var1);
      DescriptorHelper var3 = this.getDescriptorHelper();
      DeploymentPlanBean var4 = this.dc.getPlan();
      ModuleDescriptorBean var5 = var4.findModuleDescriptor(this.name, this.myds.getConfigURI());
      DescriptorBean[] var6 = var3.findChangables(this.getDescriptorBean().getDescriptor());

      for(int var7 = 0; var7 < var6.length; ++var7) {
         DescriptorBean var8 = var6[var7];
         if (var8.equals(var1)) {
            for(int var9 = 0; var9 < var2.length; ++var9) {
               String var10 = var2[var9];
               if (!var3.isChangable(var1, var10)) {
                  throw new IllegalArgumentException(SPIDeployerLogger.notChangable(var10));
               }
            }

            this.createVariable(var2, var4, var5, var1);
            return;
         }
      }

      throw new IllegalArgumentException(SPIDeployerLogger.noSuchBean(var1.toString()));
   }

   public String getDConfigName() {
      return this.name;
   }

   public DConfigBean[] getSecondaryDescriptors() {
      return (DConfigBean[])((DConfigBean[])this.secondaryRoots.values().toArray(new DConfigBean[0]));
   }

   public String getName() {
      return this.name;
   }

   public boolean isSchemaBased() {
      if (this.isSchema) {
         return true;
      } else {
         return this.dom == null ? false : this.dom.isSchemaBased();
      }
   }

   public boolean hasDD() {
      return this.ddProvided;
   }

   public boolean isExternal() {
      return this.external;
   }

   public void setExternal(boolean var1) {
      this.external = var1;
   }

   void setSchemaBased() {
      this.isSchema = true;
   }

   public DescriptorSupport getDescriptorSupport() {
      return this.myds;
   }

   private void setDescriptorSupport(DescriptorSupport var1) {
      this.myds = var1;
   }

   private void setPlanBasedDBean(boolean var1) {
      this.planBasedDBean = var1;
   }

   private boolean getPlanBasedDBean() {
      return this.planBasedDBean;
   }

   public DeploymentConfiguration getDeploymentConfiguration() {
      return this.dc;
   }

   public void vetoableChange(PropertyChangeEvent var1) throws PropertyVetoException {
      if (this.hasDD() && !this.isSchemaBased()) {
         Loggable var2 = SPIDeployerLogger.logDTDDDUpdateLoggable(var1.getPropertyName(), this.getDescriptorSupport().getConfigURI(), this.getName());
         throw new PropertyVetoException(var2.getMessage(), var1);
      } else {
         this.propertyChange(var1);
      }
   }

   public void propertyChange(PropertyChangeEvent var1) {
      this.setModified(true);
      this.setPlanBasedDBean(false);
      DescriptorBean var2 = (DescriptorBean)var1.getSource();
      if (debug) {
         Debug.say("PropertyChangeEvent : " + var1);
         Debug.say("PropertyChangeEvent.source : " + var2);
         Debug.say("PropertyChangeEvent.prop : " + var1.getPropertyName());
         Debug.say("PropertyChangeEvent.old : " + var1.getOldValue());
         Debug.say("PropertyChangeEvent.new : " + var1.getNewValue());
      }

      if (this.hasDD() && !this.isExternal()) {
         this.handleChange(var2, var1.getPropertyName(), var1.getOldValue(), var1.getNewValue());
      } else if (this.isExternal()) {
         BasicDConfigBean var3 = this.findDConfigBean(this, var2);
         if (var3 != null) {
            var3.setModified(true);
         }
      }

      this.reregister(var2);
   }

   public void handleChange(DescriptorBean var1, String var2, Object var3, Object var4) {
      if (var2 != null) {
         PropertyDescriptor var5 = this.getDescriptorHelper().getPropertyDescriptor(var1, var2);
         if (var5 == null) {
            return;
         }

         String var6 = this.getDescriptorHelper().buildKeyXpath(var1);
         if (debug) {
            Debug.say("xpathKey = " + var6);
         }

         if (this.newBeans.contains(var6)) {
            this.setPlanBasedDBean(true);
            if (debug) {
               Debug.say("xpathkey found in newbeans");
            }
         } else if (var1 instanceof AbstractDescriptorBean && ((AbstractDescriptorBean)var1).getMetaData("FROM_LISTENER") != null) {
            this.setPlanBasedDBean(true);
            if (debug) {
               Debug.say("from listener found in bean");
            }
         }

         Method var7 = var5.getReadMethod();
         Class var8 = var7.getReturnType();
         Class var9 = var8;
         if (var8.isArray()) {
            var9 = var8.getComponentType();
         }

         if (!var9.isPrimitive() && !var9.getPackage().getName().equals("java.lang") && !EmptyBean.class.isAssignableFrom(var9)) {
            if (debug) {
               Debug.say("Variabilizing object change ");
            }

            if (debug && var4 == null) {
               Debug.say("New object is null");
            }

            this.variabilize(var1, var2, var3, var4, var8);
         } else if (this.isSchemaBased()) {
            this.variabilize(var1, var2, var3, var4, var8);
         } else {
            SPIDeployerLogger.logDTDDDUpdate(var2, this.getDescriptorSupport().getConfigURI(), this.getName());
         }
      }

   }

   private void reregister(DescriptorBean var1) {
      this.deregisterAsListener(var1);
      this.registerAsListener(var1);
   }

   public String getUri() {
      return this.myds.getConfigURI();
   }

   private void setNamespace() {
      this.namespace = ConfigHelper.getNSPrefix(this.getDDBean(), this.getDescriptorSupport().getBaseNameSpace());
   }

   protected String getNamespace() {
      return this.namespace;
   }

   public void registerAsListener(DescriptorBean var1) {
      if (!this.findBean(var1) && this.bl.add(var1)) {
         if (debug) {
            Debug.say("Registering listener for " + var1.toString());
         }

         this.getDescriptorHelper().addPropertyChangeListener(var1, this);
      }

      Iterator var2 = this.getDescriptorHelper().getChildren(var1);

      while(var2.hasNext()) {
         DescriptorBean var3 = (DescriptorBean)var2.next();
         this.registerAsListener(var3);
      }

   }

   private boolean findBean(DescriptorBean var1) {
      Iterator var2 = this.bl.iterator();

      do {
         if (!var2.hasNext()) {
            return false;
         }
      } while(var2.next() != var1);

      return true;
   }

   protected void deregisterAsListener(DescriptorBean var1) {
      if (this.bl.remove(var1)) {
         if (debug) {
            Debug.say("Deregistering listener for " + var1.toString());
         }

         this.getDescriptorHelper().removePropertyChangeListener(var1, this);
      }

      Iterator var2 = this.getDescriptorHelper().getChildren(var1);

      while(var2.hasNext()) {
         DescriptorBean var3 = (DescriptorBean)var2.next();
         this.deregisterAsListener(var3);
      }

   }

   private void variabilize(DescriptorBean var1, String var2, Object var3, Object var4, Class var5) {
      if (debug) {
         Debug.say("Getting module descriptor bean for " + this.getDConfigName() + " and " + this.getDescriptorSupport().getConfigURI() + " for prop " + var2);
      }

      ModuleDescriptorBean var6 = this.dc.getPlan().findModuleDescriptor(this.getDConfigName(), this.getDescriptorSupport().getConfigURI());
      if (var6 != null) {
         if (debug) {
            Debug.say("Changeable: " + this.getDescriptorHelper().isChangable(var1, var2) + " transient: " + this.getDescriptorHelper().isTransient(var1, var2));
         }

         if ((this.getDescriptorHelper().isChangable(var1, var2) || allowNonConfigurableChanges) && !this.getDescriptorHelper().isTransient(var1, var2)) {
            var6.setChanged(true);
            if (var4 instanceof DescriptorBean) {
               this.variabilizeBean((DescriptorBean)var4);
               return;
            }

            if (var3 instanceof DescriptorBean && var4 == null) {
               this.removeDescriptorBean(var6, (DescriptorBean)var3);
               return;
            }

            if (!this.isSeparableArray(var5)) {
               boolean var8 = this.getDescriptorHelper().isKey(var1, var2) || this.getDescriptorHelper().isKeyComponent(var1, var2);
               Object var9 = var8 ? var3 : null;
               VariableBean var10 = null;
               if (var8 && !this.getPlanBasedDBean()) {
                  if (debug) {
                     Debug.say("findOrCreateVariable from is key, old key = " + var9);
                  }

                  var10 = this.dc.getPlan().findOrCreateVariable(var6, var1, var2, this.getPlanBasedDBean(), var9);
               } else {
                  if (debug) {
                     Debug.say("findOrCreateVariable is plan based = " + this.getPlanBasedDBean());
                  }

                  var10 = this.dc.getPlan().findOrCreateVariable(var6, var1, var2, this.getPlanBasedDBean());
               }

               if (var4 == null) {
                  var10.setValue((String)null);
               } else if (var5.isArray()) {
                  var10.setValue(new String((byte[])((byte[])var4)));
               } else {
                  var10.setValue(var4.toString());
               }
            } else {
               VariableBean var11 = null;
               if (this.isArrayOfDescriptorBeans(var3, var4)) {
                  this.variabilizeDescriptorBeanArray(var6, var3, var4, false);
               } else {
                  if (debug) {
                     Debug.say("Variabilize nonbean array");
                  }

                  var11 = this.dc.getPlan().findOrCreateVariable(var6, var1, var2, this.getPlanBasedDBean());
                  String var12 = this.variabilizeArray(var3, var4, var11);
                  this.updateOperation(var12, var11);
               }
            }
         } else if (this.isArrayOfDescriptorBeans(var3, var4)) {
            this.variabilizeDescriptorBeanArray(var6, var3, var4, true);
         }
      }

   }

   private void variabilizeDescriptorBeanArray(ModuleDescriptorBean var1, Object var2, Object var3, boolean var4) {
      if (debug) {
         Debug.say("variabilizing array of descriptor beans");
      }

      Object[] var5 = this.convertArray(var2);
      if (var5 == null) {
         var5 = new Object[0];
      }

      Object[] var6 = this.convertArray(var3);
      if (var6 == null) {
         var6 = new Object[0];
      }

      ArrayList var7 = new ArrayList();
      ArrayList var8 = new ArrayList();
      ArrayUtils.computeDiff(var5, var6, this.createDiffHandler(var7, var8));
      this.clearLocalCache(var8.iterator());
      if (!var4) {
         this.addDescriptorBeans(var7);
      }

      this.removeDescriptorBeans(var1, var8);
   }

   private void clearLocalCache(Iterator var1) {
      while(var1.hasNext()) {
         this.bl.remove(var1.next());
      }

   }

   private void addDescriptorBeans(ArrayList var1) {
      for(Iterator var2 = var1.iterator(); var2.hasNext(); this.variabilizeBean((DescriptorBean)var2.next())) {
         if (debug) {
            Debug.say("Adding a descriptor bean");
         }
      }

   }

   private void removeDescriptorBeans(ModuleDescriptorBean var1, ArrayList var2) {
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         DescriptorBean var4 = (DescriptorBean)var3.next();
         this.removeDescriptorBean(var1, var4);
      }

   }

   private void removeDescriptorBean(ModuleDescriptorBean var1, DescriptorBean var2) {
      if (debug) {
         Debug.say("Removing descriptor bean: " + var2);
      }

      try {
         this.dc.getPlan().findAndRemoveAllBeanVariables(var1, var2);
      } catch (IllegalArgumentException var5) {
         Loggable var4 = SPIDeployerLogger.logUnableToRemoveDescriptorBeanLoggable(var2.toString(), var5.getMessage());
         throw new IllegalArgumentException(var4.getMessage());
      }
   }

   private void variabilizeBean(DescriptorBean var1) {
      this.setPlanBasedDBean(true);
      String var2 = this.getDescriptorHelper().buildKeyXpath(var1);
      if (debug) {
         Debug.say("xpathKey = " + var2);
      }

      this.newBeans.add(var2);
      AbstractDescriptorBean var3 = (AbstractDescriptorBean)var1;
      var3.setMetaData("FROM_LISTENER", new Boolean(true));
      if (debug) {
         Debug.say("Adding properties for new bean: " + var3);
      }

      List var4 = var3._getAlreadySetPropertyNames();
      Iterator var5 = var4.iterator();

      while(true) {
         while(var5.hasNext()) {
            String var6 = (String)var5.next();
            if (debug) {
               Debug.say("Try to set property : " + var6);
            }

            Object var7;
            if (this.getDescriptorHelper().isChangable(var1, var6) && !this.getDescriptorHelper().isTransient(var1, var6)) {
               try {
                  var7 = invokeAccessor(var1, var6);
                  if (debug) {
                     Debug.say("Got value: " + var7);
                  }

                  this.handleChange(var1, var6, (Object)null, var7);
               } catch (Exception var8) {
                  throw new AssertionError("Caught exception trying to invoke accessor on " + var1 + " - called 'get" + var6 + "()'.");
               }
            } else if (this.emptyBean(var4, var1, var6)) {
               try {
                  var7 = invokeAccessor(var1, var6);
                  this.addEmptyBeanKey(var1, var6, var7);
               } catch (Exception var9) {
                  throw new AssertionError("Caught exception trying to invoke accessor on " + var1 + " - called 'get" + var6 + "()'.");
               }
            }
         }

         return;
      }
   }

   private void addEmptyBeanKey(DescriptorBean var1, String var2, Object var3) {
      ModuleDescriptorBean var4 = this.dc.getPlan().findModuleDescriptor(this.getDConfigName(), this.getDescriptorSupport().getConfigURI());
      VariableBean var6 = this.dc.getPlan().findOrCreateVariable(var4, var1, var2, this.getPlanBasedDBean());
      var6.setValue(var3.toString());
   }

   private boolean emptyBean(List var1, DescriptorBean var2, String var3) {
      if (debug) {
         Debug.say("Checking list size: " + var1.size() + " against Bean with prop: " + var3);
      }

      return var1.size() <= 1 && this.getDescriptorHelper().isKey(var2, var3);
   }

   private static Object invokeAccessor(Object var0, String var1) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
      Class var2 = var0.getClass();

      Method var3;
      try {
         var3 = var2.getMethod("get" + var1);
      } catch (NoSuchMethodException var7) {
         try {
            var3 = var2.getMethod("is" + var1);
         } catch (NoSuchMethodException var6) {
            throw var7;
         }
      }

      return var3.invoke(var0);
   }

   private boolean isArrayOfDescriptorBeans(Object var1, Object var2) {
      int var3;
      Object var4;
      if (var2 != null && var2.getClass().isArray()) {
         var3 = Array.getLength(var2);
         if (var3 > 0) {
            var4 = Array.get(var2, 0);
            if (var4 instanceof DescriptorBean) {
               return true;
            }
         }
      }

      if (var1 != null && var1.getClass().isArray()) {
         var3 = Array.getLength(var1);
         if (var3 > 0) {
            var4 = Array.get(var1, 0);
            if (var4 instanceof DescriptorBean) {
               return true;
            }
         }
      }

      return false;
   }

   private boolean isSeparableArray(Class var1) {
      return var1.isArray() && !Byte.TYPE.isAssignableFrom(var1.getComponentType());
   }

   private void updateOperation(String var1, VariableBean var2) {
      if (var1 != null) {
         VariableAssignmentBean[] var3 = this.dc.getPlan().findVariableAssignments(var2);

         for(int var4 = 0; var4 < var3.length; ++var4) {
            VariableAssignmentBean var5 = var3[var4];
            var5.setOperation(var1);
         }
      }

   }

   private String variabilizeArray(Object var1, Object var2, VariableBean var3) {
      Object[] var4 = this.convertArray(var2);
      if (var4 == null) {
         var4 = new Object[0];
      }

      return this.handleSetOperation(var4, var3);
   }

   private Object[] convertArray(Object var1) {
      if (var1 == null) {
         return null;
      } else {
         int var2 = Array.getLength(var1);
         Object[] var3 = new Object[var2];

         for(int var4 = 0; var4 < var2; ++var4) {
            Object var5 = Array.get(var1, var4);
            if (var5.getClass().isPrimitive()) {
               var3[var4] = var5.toString();
            } else {
               var3[var4] = var5;
            }
         }

         return var3;
      }
   }

   private String handleRemoveOperation(List var1, VariableBean var2, Object[] var3) {
      List var4 = this.extractValues(var2);
      if (var4.isEmpty()) {
         this.loadVariable(var1, var2);
         return "remove";
      } else {
         for(int var5 = 0; var5 < var1.size(); ++var5) {
            Object var6 = var1.get(var5);
            if (var4.contains(var6)) {
               var1.remove(var6);
               int var7 = var4.indexOf(var6);
               var4.remove(var7);
            }
         }

         if (var1.isEmpty()) {
            this.loadVariable(var4, var2);
            return null;
         } else {
            this.loadVariable(Arrays.asList(var3), var2);
            return "replace";
         }
      }
   }

   private String handleAddOperation(List var1, VariableBean var2) {
      List var3 = this.extractValues(var2);
      var3.addAll(var1);
      this.loadVariable(var3, var2);
      return "add";
   }

   private void loadVariable(List var1, VariableBean var2) {
      String var3 = new String();

      for(int var4 = 0; var4 < var1.size(); ++var4) {
         if (var4 > 0) {
            var3 = var3 + ",";
         }

         Object var5 = var1.get(var4);
         var3 = var3 + "\"" + var5.toString() + "\"";
      }

      var2.setValue(var3);
   }

   private String handleSetOperation(Object[] var1, VariableBean var2) {
      this.loadVariable(Arrays.asList(var1), var2);
      return "replace";
   }

   private ArrayUtils.DiffHandler createDiffHandler(final ArrayList var1, final ArrayList var2) {
      return new ArrayUtils.DiffHandler() {
         public void addObject(Object var1x) {
            var1.add(var1x);
         }

         public void removeObject(Object var1x) {
            var2.add(var1x);
         }
      };
   }

   private List extractValues(VariableBean var1) {
      if (var1.getValue() == null) {
         return new ArrayList(0);
      } else {
         StringTokenizer var2 = new StringTokenizer(var1.getValue(), ",");
         int var3 = var2.countTokens();
         ArrayList var4 = new ArrayList(var3);

         for(int var5 = 0; var5 < var3; ++var5) {
            String var6 = var2.nextToken();
            if (var6.startsWith("\"")) {
               var6 = var6.substring(1, var6.length() - 1);
            }

            var4.add(var6);
         }

         return var4;
      }
   }

   private void addWebLogicExtensions() throws InvalidModuleException, IOException {
      String var1;
      if (this.dObject.getType().getValue() == ModuleType.EAR.getValue()) {
         var1 = "META-INF/weblogic-extension.xml";
      } else {
         if (this.dObject.getType().getValue() != ModuleType.WAR.getValue()) {
            return;
         }

         var1 = "WEB-INF/weblogic-extension.xml";
      }

      WeblogicExtensionBean var2 = null;
      if (this.dObject instanceof WebLogicDeployableObject) {
         WebLogicDeployableObject var3 = (WebLogicDeployableObject)this.dObject;
         if (var3.hasDDBean(var1)) {
            try {
               DDBeanRootImpl var4 = (DDBeanRootImpl)var3.getDDBeanRoot(var1);
               if (debug) {
                  Debug.say("Getting descriptorbean from ddroot :\n" + var4 + " of type: " + var4.getType());
               }

               var2 = (WeblogicExtensionBean)var4.getDescriptorBean();
            } catch (DDBeanCreateException var16) {
               if (debug) {
                  var16.printStackTrace();
               }
            }
         }
      }

      if (var2 != null) {
         try {
            CustomModuleBean[] var11 = var2.getCustomModules();
            if (var11 != null) {
               for(int var14 = 0; var14 < var11.length; ++var14) {
                  CustomModuleBean var12 = var11[var14];
                  ConfigurationSupportBean var13 = var12.getConfigurationSupport();
                  if (var13 != null) {
                     String var17 = var13.getBaseRootElement();
                     if (var17 == null) {
                        throw new InvalidModuleException(SPIDeployerLogger.getMissingExt(var1, "base-root-element", var12.getUri(), var12.getProviderName()));
                     }

                     String var18 = var13.getConfigRootElement();
                     if (var18 == null) {
                        var18 = var17;
                     }

                     String var7 = var13.getBaseNamespace();
                     if (var7 == null) {
                        throw new InvalidModuleException(SPIDeployerLogger.getMissingExt(var1, "base-namespace", var12.getUri(), var12.getProviderName()));
                     }

                     String var8 = var13.getConfigNamespace();
                     if (var8 == null) {
                        var8 = var7;
                     }

                     String var5 = var13.getBaseUri();
                     if (var5 == null) {
                        throw new InvalidModuleException(SPIDeployerLogger.getMissingExt(var1, "base-uri", var12.getUri(), var12.getProviderName()));
                     }

                     String var6 = var13.getConfigUri();
                     if (var6 == null) {
                        var6 = var5;
                     }

                     String var9 = var13.getBasePackageName();
                     if (var9 == null) {
                        throw new InvalidModuleException(SPIDeployerLogger.getMissingExt(var1, "base-package-name", var12.getUri(), var12.getProviderName()));
                     }

                     String var10 = var13.getConfigPackageName();
                     if (var10 == null) {
                        var10 = var9;
                     }

                     SPIDeployerLogger.logAddDS(var5, var6);
                     DescriptorSupportManager.add(WebLogicModuleType.CONFIG, var17, var18, var7, var8, var5, var6, var9, var10);
                  }
               }

            }
         } catch (IllegalArgumentException var15) {
            throw new InvalidModuleException(var15.toString());
         }
      }
   }

   private BasicDConfigBean findDConfigBean(BasicDConfigBean var1, DescriptorBean var2) {
      if (var2 != null && var1 != null) {
         if (var1.getDescriptorBean() == var2) {
            return var1;
         } else {
            Iterator var3 = var1.getChildBeans().iterator();

            BasicDConfigBean var5;
            do {
               if (!var3.hasNext()) {
                  return null;
               }

               BasicDConfigBean var4 = (BasicDConfigBean)var3.next();
               var5 = this.findDConfigBean(var4, var2);
            } while(var5 == null);

            return var5;
         }
      } else {
         return null;
      }
   }
}
