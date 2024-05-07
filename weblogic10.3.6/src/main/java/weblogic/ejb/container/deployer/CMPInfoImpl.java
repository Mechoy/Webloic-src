package weblogic.ejb.container.deployer;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import weblogic.ejb.container.cmp.rdbms.Deployer;
import weblogic.ejb.container.cmp.rdbms.RDBMSBean;
import weblogic.ejb.container.cmp.rdbms.RDBMSPersistenceManager;
import weblogic.ejb.container.cmp.rdbms.finders.Finder;
import weblogic.ejb.container.cmp.rdbms.finders.RDBMSFinder;
import weblogic.ejb.container.interfaces.CMPInfo;
import weblogic.ejb.container.persistence.CMPBeanDescriptorImpl;
import weblogic.ejb.container.persistence.PersistenceType;
import weblogic.ejb.container.persistence.spi.CMPBeanDescriptor;
import weblogic.ejb.container.persistence.spi.CMPDeployer;
import weblogic.ejb.container.persistence.spi.EjbEntityRef;
import weblogic.ejb.container.persistence.spi.PersistenceManager;
import weblogic.ejb.container.persistence.spi.Relationships;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.j2ee.descriptor.CmpFieldBean;
import weblogic.j2ee.descriptor.EntityBeanBean;
import weblogic.utils.Debug;
import weblogic.utils.Getopt2;
import weblogic.utils.jars.VirtualJarFile;

public final class CMPInfoImpl implements CMPInfo {
   private EntityBeanInfoImpl ebi;
   private String cmpVersion = "1.x";
   private Collection containerManagedFieldNames;
   private String cmPrimaryKeyFieldName;
   private String abstractSchemaName;
   private boolean isBeanClassAbstract;
   Collection queries;
   private Collection annotatedPersistenceTypes;
   private String persistenceUseIdentifier;
   private String persistenceUseVersion;
   private String persistenceUseStorage;
   private boolean findersLoadBean;
   private Relationships relationships = null;
   private Map beanMap;
   private Map allBeanMap;
   private Map dependentMap;
   private String generatedBeanClassName;
   private PersistenceType persistenceType;
   private CMPDeployer deployer = null;
   private Set ejbEntityRefs;
   private Collection finderList;
   private Map<RDBMSFinder.FinderKey, Finder> finderMap;
   private int maxQueriesInCache = 0;

   public CMPInfoImpl(EntityBeanInfoImpl var1, CompositeMBeanDescriptor var2) {
      this.ebi = var1;
      this.ejbEntityRefs = new HashSet();
      EntityBeanBean var3 = (EntityBeanBean)var2.getBean();
      this.containerManagedFieldNames = new LinkedList();
      this.queries = Arrays.asList((Object[])var3.getQueries());
      this.cmpVersion = var3.getCmpVersion();
      this.abstractSchemaName = var3.getAbstractSchemaName();
      CmpFieldBean[] var4 = var3.getCmpFields();

      for(int var5 = 0; var5 < var4.length; ++var5) {
         this.containerManagedFieldNames.add(var4[var5].getFieldName());
      }

      this.cmPrimaryKeyFieldName = var3.getPrimkeyField();
      this.persistenceUseIdentifier = var2.getPersistenceUseIdentifier();
      this.persistenceUseVersion = var2.getPersistenceUseVersion();
      this.persistenceUseStorage = var2.getPersistenceUseStorage();
      this.findersLoadBean = var2.getFindersLoadBean();
      this.isBeanClassAbstract = !var1.isEJB30();
      this.maxQueriesInCache = var2.getMaxQueriesInCache();
   }

   public void setup(File var1, Getopt2 var2, VirtualJarFile var3) throws WLDeploymentException {
      this.deployer = this.getPersistenceType().setupDeployer(this.ebi, var1, var2, var3);
      this.deployer.initializePersistenceManager(this.ebi.getPersistenceManager());
      if (this.uses20CMP() && this.getPersistenceUseIdentifier().equals("WebLogic_CMP_RDBMS")) {
         assert this.deployer instanceof Deployer;

         Deployer var4 = (Deployer)this.deployer;
         RDBMSBean var5 = var4.getTypeSpecificData();
         this.finderList = var5.getFinderList();
      }

   }

   public boolean isBeanClassAbstract() {
      return this.isBeanClassAbstract;
   }

   public int getInstanceLockOrder() {
      if (this.uses20CMP() && this.getPersistenceUseIdentifier().equals("WebLogic_CMP_RDBMS")) {
         assert this.deployer instanceof Deployer;

         Deployer var1 = (Deployer)this.deployer;
         RDBMSBean var2 = var1.getTypeSpecificData();
         if (var2.getInstanceLockOrder().equals("AccessOrder")) {
            return 100;
         } else if (var2.getInstanceLockOrder().equals("ValueOrder")) {
            return 101;
         } else {
            throw new AssertionError("invalid value for instanceLockOrder: " + var2.getInstanceLockOrder());
         }
      } else {
         return 100;
      }
   }

   public Collection getAllContainerManagedFieldNames() {
      return this.containerManagedFieldNames;
   }

   public Collection getAllQueries() {
      return this.queries;
   }

   public boolean hasContainerManagedFields() {
      return !this.containerManagedFieldNames.isEmpty();
   }

   public String getCMPrimaryKeyFieldName() {
      return this.cmPrimaryKeyFieldName;
   }

   public String getCMPVersion() {
      return this.cmpVersion;
   }

   public boolean uses20CMP() {
      return this.getCMPVersion().startsWith("2");
   }

   public String getAbstractSchemaName() {
      return this.abstractSchemaName;
   }

   public boolean findersLoadBean() {
      return this.findersLoadBean;
   }

   public String getPersistenceUseIdentifier() {
      return this.persistenceUseIdentifier;
   }

   public String getPersistenceUseVersion() {
      return this.persistenceUseVersion;
   }

   public String getPersistenceUseStorage() {
      return this.persistenceUseStorage;
   }

   public void setRelationships(Relationships var1) {
      this.relationships = var1;
   }

   public Relationships getRelationships() {
      return this.relationships;
   }

   public void setBeanMap(Map var1) {
      this.beanMap = var1;
   }

   public Map getBeanMap() {
      return this.beanMap;
   }

   public CMPBeanDescriptor getCMPBeanDescriptor(String var1) {
      return (CMPBeanDescriptor)this.allBeanMap.get(var1);
   }

   public void setAllBeanMap(Map var1) {
      this.allBeanMap = var1;
   }

   public Map getAllBeanMap() {
      return this.allBeanMap;
   }

   public void setDependentMap(Map var1) {
      if (var1 == null) {
         Debug.say("map is null");
      }

      this.dependentMap = var1;
   }

   public Map getDependentMap() {
      return this.dependentMap;
   }

   public void setGeneratedBeanClassName(String var1) {
      this.generatedBeanClassName = var1;
   }

   public String getGeneratedBeanClassName() {
      return this.generatedBeanClassName;
   }

   public PersistenceType getPersistenceType() {
      assert this.persistenceType != null;

      return this.persistenceType;
   }

   public void setPersistenceType(PersistenceType var1) {
      this.persistenceType = var1;
   }

   public Class getGeneratedBeanClass() throws ClassNotFoundException {
      assert this.ebi != null;

      return this.ebi.getGeneratedBeanClass();
   }

   public CMPDeployer getDeployer() {
      return this.deployer;
   }

   public Collection getAllEJBEntityReferences() {
      return this.ejbEntityRefs;
   }

   public void addEjbEntityRef(EjbEntityRef var1) {
      this.ejbEntityRefs.add(var1);
   }

   public void setupParentBeanManagers() {
      if (this.uses20CMP()) {
         PersistenceManager var1 = this.ebi.getPersistenceManager();
         if (var1 instanceof RDBMSPersistenceManager) {
            ((RDBMSPersistenceManager)var1).setupParentBeanManagers();
         }
      }

   }

   public void setCycleExists() {
      if (this.uses20CMP()) {
         PersistenceManager var1 = this.ebi.getPersistenceManager();
         if (var1 instanceof RDBMSPersistenceManager) {
            ((RDBMSPersistenceManager)var1).setCycleExists();
         }
      }

   }

   public void setupMNBeanManagers() {
      if (this.uses20CMP()) {
         PersistenceManager var1 = this.ebi.getPersistenceManager();
         if (var1 instanceof RDBMSPersistenceManager) {
            ((RDBMSPersistenceManager)var1).setupM2NBeanManagers();
         }
      }

   }

   public boolean isQueryCachingEnabled(Method var1) {
      if (!this.uses20CMP()) {
         return false;
      } else if (this.finderMap != null && this.finderMap.containsKey(new RDBMSFinder.FinderKey(var1))) {
         return true;
      } else if (this.finderMap == null) {
         boolean var2 = false;
         this.finderMap = new HashMap();
         Iterator var3 = this.finderList.iterator();

         while(var3.hasNext()) {
            Finder var4 = (Finder)var3.next();
            if (var4.isQueryCachingEnabled()) {
               RDBMSFinder.FinderKey var5 = new RDBMSFinder.FinderKey(var4);
               this.finderMap.put(var5, var4);
               if (var5.equals(new RDBMSFinder.FinderKey(var1))) {
                  var2 = true;
               }
            }
         }

         return var2;
      } else {
         return false;
      }
   }

   public String getQueryCachingEnabledFinderIndex(Method var1) {
      assert this.finderMap != null;

      Finder var2 = (Finder)this.finderMap.get(new RDBMSFinder.FinderKey(var1));
      String var3 = var2.getFinderIndex();

      assert var3 != null;

      return var3;
   }

   public int getMaxQueriesInCache() {
      return this.maxQueriesInCache;
   }

   public boolean isEnableEagerRefresh(Method var1) {
      Finder var2 = (Finder)this.finderMap.get(new RDBMSFinder.FinderKey(var1));

      assert var2 != null;

      return var2.isEagerRefreshEnabled();
   }

   public void beanImplClassChangeNotification() {
      CMPBeanDescriptorImpl var1 = (CMPBeanDescriptorImpl)this.allBeanMap.get(this.ebi.getEJBName());
      var1.beanImplClassChangeNotification();
   }
}
