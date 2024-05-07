package weblogic.jms.foreign;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import weblogic.application.ModuleException;
import weblogic.descriptor.BeanUpdateRejectedException;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.wl.ForeignConnectionFactoryBean;
import weblogic.j2ee.descriptor.wl.ForeignDestinationBean;
import weblogic.j2ee.descriptor.wl.ForeignJNDIObjectBean;
import weblogic.j2ee.descriptor.wl.ForeignServerBean;
import weblogic.j2ee.descriptor.wl.JMSBean;
import weblogic.j2ee.descriptor.wl.PropertyBean;
import weblogic.jms.JMSLogger;
import weblogic.jms.JMSService;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.JMSException;
import weblogic.jms.module.JMSBeanHelper;
import weblogic.jms.module.JMSModuleManagedEntity;
import weblogic.management.ManagementException;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.utils.GenericBeanListener;

public class ForeignJMSImpl implements JMSModuleManagedEntity {
   private ForeignServerBean foreignServerBean;
   private HashMap factoryImpls;
   private HashMap destinationImpls;
   private JMSService jmsService = JMSService.getJMSService();
   private String initialContextFactory;
   private String connectionURL;
   private transient PropertyBean[] jndiPropertyBeans;
   private boolean defaultTargetingEnabled;
   private transient GenericBeanListener foreignServerBeanListener;
   private static final HashMap foreignServerBeanSignatures = new HashMap();
   private static final HashMap foreignServerAdditionSignatures = new HashMap();
   private String name;

   public ForeignJMSImpl(ForeignServerBean var1, String var2) {
      this.foreignServerBean = var1;
      this.name = JMSBeanHelper.getDecoratedName(var2, this.foreignServerBean.getName());
   }

   private void validateJNDI() throws ModuleException {
      Iterator var1 = this.factoryImpls.values().iterator();

      while(var1.hasNext()) {
         ForeignJNDIObjectImpl var2 = (ForeignJNDIObjectImpl)var1.next();
         var2.validateJNDI();
      }

      Iterator var4 = this.destinationImpls.values().iterator();

      while(var4.hasNext()) {
         ForeignJNDIObjectImpl var3 = (ForeignJNDIObjectImpl)var4.next();
         var3.validateJNDI();
      }

   }

   private void bind() throws JMSException {
      ForeignJNDIObjectImpl var2;
      for(Iterator var1 = this.factoryImpls.values().iterator(); var1.hasNext(); var2.bind(false)) {
         var2 = (ForeignJNDIObjectImpl)var1.next();
         if (JMSDebug.JMSModule.isDebugEnabled()) {
            JMSDebug.JMSModule.debug("ForeignJMSImpl: Binding factory impl=" + var2);
         }
      }

      ForeignJNDIObjectImpl var3;
      for(Iterator var4 = this.destinationImpls.values().iterator(); var4.hasNext(); var3.bind(false)) {
         var3 = (ForeignJNDIObjectImpl)var4.next();
         if (JMSDebug.JMSModule.isDebugEnabled()) {
            JMSDebug.JMSModule.debug("ForeignJMSImpl: Binding destination impl=" + var3);
         }
      }

   }

   private void unbind() {
      Iterator var1 = this.factoryImpls.values().iterator();

      while(var1.hasNext()) {
         ForeignJNDIObjectImpl var2 = (ForeignJNDIObjectImpl)var1.next();
         var2.unbind();
      }

      Iterator var4 = this.destinationImpls.values().iterator();

      while(var4.hasNext()) {
         ForeignJNDIObjectImpl var3 = (ForeignJNDIObjectImpl)var4.next();
         var3.unbind();
      }

   }

   private void prepareForeignJNDIObject(ForeignJNDIObjectImpl var1, boolean var2) throws BeanUpdateRejectedException {
      try {
         var1.bind(false);
      } catch (JMSException var4) {
         throw new BeanUpdateRejectedException(var4.getMessage(), var4);
      }

      if (var2) {
         this.factoryImpls.put(var1.getName(), var1);
      } else {
         this.destinationImpls.put(var1.getName(), var1);
      }

   }

   private void unprepareForeignJNDIObject(ForeignJNDIObjectBean var1, boolean var2) {
      ForeignJNDIObjectImpl var3;
      if (var2) {
         var3 = (ForeignJNDIObjectImpl)this.factoryImpls.remove(var1.getName());
      } else {
         var3 = (ForeignJNDIObjectImpl)this.destinationImpls.remove(var1.getName());
      }

      if (var3 == null) {
         throw new AssertionError("ERROR: ForeignJNDIObject " + var1.getName() + " previously not in prepared state");
      } else {
         var3.unbind();
         var3.close();
      }
   }

   public void prepare() throws ModuleException {
      DescriptorBean var1 = (DescriptorBean)this.foreignServerBean;
      this.foreignServerBeanListener = new GenericBeanListener(var1, this, foreignServerBeanSignatures, foreignServerAdditionSignatures, false);

      try {
         this.foreignServerBeanListener.initialize();
      } catch (ManagementException var9) {
         throw new ModuleException(var9.getMessage(), var9);
      }

      ForeignConnectionFactoryBean[] var2 = this.foreignServerBean.getForeignConnectionFactories();
      this.factoryImpls = new HashMap();

      ForeignJNDIObjectImpl var3;
      for(int var4 = 0; var4 < var2.length; ++var4) {
         try {
            var3 = new ForeignJNDIObjectImpl(this.foreignServerBean, var2[var4]);
         } catch (JMSException var8) {
            throw new ModuleException(var8.getMessage(), var8);
         }

         this.factoryImpls.put(var2[var4].getName(), var3);
      }

      ForeignDestinationBean[] var10 = this.foreignServerBean.getForeignDestinations();
      this.destinationImpls = new HashMap();

      for(int var5 = 0; var5 < var10.length; ++var5) {
         try {
            var3 = new ForeignJNDIObjectImpl(this.foreignServerBean, var10[var5]);
         } catch (JMSException var7) {
            throw new ModuleException(var7.getMessage(), var7);
         }

         this.destinationImpls.put(var10[var5].getName(), var3);
      }

      this.validateJNDI();
   }

   public void activate(JMSBean var1) throws ModuleException {
      this.foreignServerBean = var1.lookupForeignServer(this.getEntityName());
      this.registerBeanUpdateListeners();

      try {
         this.bind();
         JMSLogger.logForeignJMSDeployed(this.foreignServerBean.getName());
      } catch (JMSException var3) {
         JMSLogger.logErrorBindForeignJMS(this.foreignServerBean.getName(), var3);
         throw new ModuleException("ERROR: binding the Foreign JMS Server components", var3);
      }
   }

   public void deactivate() throws ModuleException {
      this.unregisterBeanUpdateListeners();
      if (this.jmsService.isActive()) {
         this.unbind();
      }

   }

   public void unprepare() throws ModuleException {
      Iterator var1 = this.factoryImpls.values().iterator();

      while(var1.hasNext()) {
         ForeignJNDIObjectImpl var2 = (ForeignJNDIObjectImpl)var1.next();
         var2.close();
         var1.remove();
      }

      Iterator var4 = this.destinationImpls.values().iterator();

      while(var4.hasNext()) {
         ForeignJNDIObjectImpl var3 = (ForeignJNDIObjectImpl)var4.next();
         var3.close();
         var4.remove();
      }

   }

   public void remove() throws ModuleException {
   }

   public void destroy() throws ModuleException {
   }

   public String getEntityName() {
      return this.foreignServerBean.getName();
   }

   public void prepareChangeOfTargets(List var1, DomainMBean var2) {
   }

   public void activateChangeOfTargets() {
   }

   public void rollbackChangeOfTargets() {
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      throw new AssertionError("Name setter only here to satisfy interface: " + var1);
   }

   public String getNotes() {
      return this.foreignServerBean.getNotes();
   }

   public void setNotes(String var1) {
      throw new AssertionError("Notes setter only here to satisfy interface: " + var1);
   }

   public String getInitialContextFactory() {
      return this.initialContextFactory;
   }

   public void setInitialContextFactory(String var1) {
      this.initialContextFactory = var1;
   }

   public String getConnectionURL() {
      return this.connectionURL;
   }

   public void setConnectionURL(String var1) {
      this.connectionURL = var1;
   }

   public PropertyBean[] getJNDIProperties() {
      return this.jndiPropertyBeans;
   }

   public void setJNDIProperties(PropertyBean[] var1) {
      this.jndiPropertyBeans = var1;
   }

   public boolean isDefaultTargetingEnabled() {
      return this.defaultTargetingEnabled;
   }

   public void setDefaultTargetingEnabled(boolean var1) {
      this.defaultTargetingEnabled = var1;
   }

   public PropertyBean createJNDIProperty(String var1) {
      throw new AssertionError("JNDI property setter only here to satisfy interface: " + this.name);
   }

   public void destroyJNDIProperty(PropertyBean var1) {
      throw new AssertionError("JNDI Property destructor only here to satisfy interface: " + this.name);
   }

   public String getSubDeploymentName() {
      return this.foreignServerBean.getSubDeploymentName();
   }

   public void setSubDeploymentName(String var1) {
      throw new AssertionError("SubDeployment setter only here to satisfy interface: " + var1);
   }

   public ForeignDestinationBean[] getForeignDestinations() {
      return this.foreignServerBean.getForeignDestinations();
   }

   public void setForeignDestinations(ForeignDestinationBean[] var1) {
      throw new AssertionError("Foreign Destination setter only here to satisfy interface");
   }

   public ForeignDestinationBean createForeignDestination(String var1) {
      throw new AssertionError("Foreign Destination creator only here to satisfy interface");
   }

   public void destroyForeignDestination(ForeignDestinationBean var1) {
      throw new AssertionError("Foreign Destination destroyer only here to satisfy interface");
   }

   public ForeignConnectionFactoryBean[] getForeignConnectionFactories() {
      return this.foreignServerBean.getForeignConnectionFactories();
   }

   public void setForeignConnectionFactories(ForeignConnectionFactoryBean[] var1) {
      throw new AssertionError("Foreign Connection Factory setter only here to satisfy interface");
   }

   public ForeignConnectionFactoryBean createForeignConnectionFactory(String var1) {
      throw new AssertionError("Foreign Connection Factory creator only here to satisfy interface");
   }

   public void destroyForeignConnectionFactory(ForeignConnectionFactoryBean var1) {
      throw new AssertionError("Foreign Connection Factory destroyer only here to satisfy interface");
   }

   private void registerBeanUpdateListeners() {
      if (this.foreignServerBeanListener != null) {
         this.foreignServerBeanListener.close();
         this.foreignServerBeanListener = null;
      }

      DescriptorBean var1 = (DescriptorBean)this.foreignServerBean;
      this.foreignServerBeanListener = new GenericBeanListener(var1, this, foreignServerBeanSignatures, foreignServerAdditionSignatures);
   }

   private void unregisterBeanUpdateListeners() {
      if (this.foreignServerBeanListener != null) {
         this.foreignServerBeanListener.close();
         this.foreignServerBeanListener = null;
      }

   }

   public void startAddForeignDestinations(ForeignDestinationBean var1) throws BeanUpdateRejectedException {
      ForeignJNDIObjectImpl var2;
      try {
         var2 = new ForeignJNDIObjectImpl(this.foreignServerBean, var1);
      } catch (JMSException var4) {
         throw new BeanUpdateRejectedException(var4.getMessage(), var4);
      }

      this.prepareForeignJNDIObject(var2, false);
   }

   public void startAddForeignConnectionFactories(ForeignConnectionFactoryBean var1) throws BeanUpdateRejectedException {
      ForeignJNDIObjectImpl var2;
      try {
         var2 = new ForeignJNDIObjectImpl(this.foreignServerBean, var1);
      } catch (JMSException var4) {
         throw new BeanUpdateRejectedException(var4.getMessage(), var4);
      }

      this.prepareForeignJNDIObject(var2, true);
   }

   public void finishAddForeignDestinations(ForeignDestinationBean var1, boolean var2) {
      if (!var2) {
         this.unprepareForeignJNDIObject(var1, false);
      }

   }

   public void finishAddForeignConnectionFactories(ForeignConnectionFactoryBean var1, boolean var2) {
      if (!var2) {
         this.unprepareForeignJNDIObject(var1, true);
      }

   }

   public void startRemoveForeignDestinations(ForeignDestinationBean var1) throws BeanUpdateRejectedException {
   }

   public void startRemoveForeignConnectionFactories(ForeignConnectionFactoryBean var1) throws BeanUpdateRejectedException {
   }

   public void finishRemoveForeignDestinations(ForeignDestinationBean var1, boolean var2) {
      if (var2) {
         this.unprepareForeignJNDIObject(var1, false);
      }

   }

   public void finishRemoveForeignConnectionFactories(ForeignConnectionFactoryBean var1, boolean var2) {
      if (var2) {
         this.unprepareForeignJNDIObject(var1, true);
      }

   }

   public ForeignDestinationBean lookupForeignDestination(String var1) {
      return null;
   }

   public ForeignConnectionFactoryBean lookupForeignConnectionFactory(String var1) {
      return null;
   }

   static {
      foreignServerBeanSignatures.put("InitialContextFactory", String.class);
      foreignServerBeanSignatures.put("ConnectionURL", String.class);
      foreignServerBeanSignatures.put("JNDIProperties", PropertyBean[].class);
      foreignServerBeanSignatures.put("DefaultTargetingEnabled", Boolean.TYPE);
      foreignServerAdditionSignatures.put("ForeignDestinations", ForeignDestinationBean.class);
      foreignServerAdditionSignatures.put("ForeignConnectionFactories", ForeignConnectionFactoryBean.class);
   }
}
