package weblogic.ejb.container.utils.ddconverter;

import java.util.Hashtable;
import java.util.Vector;
import weblogic.ejb.container.dd.DDDefaults;

public class EJB10DescriptorStructure extends Structure {
   public static final boolean verbose = false;

   public EJB10DescriptorStructure(Structure var1) {
      this.name = var1.name;
      this.elements = var1.elements;
   }

   public String getEntityDescriptor() {
      return this.name.equals("EntityDescriptor") ? this.name : null;
   }

   public String getSessionDescriptor() {
      return this.name.equals("SessionDescriptor") ? this.name : null;
   }

   public String getBeanHomeName() {
      return (String)this.elements.get("beanHomeName");
   }

   public String getEnterpriseBeanClassName() {
      return (String)this.elements.get("enterpriseBeanClassName");
   }

   public String getHomeInterfaceClassName() {
      return (String)this.elements.get("homeInterfaceClassName");
   }

   public String getRemoteInterfaceClassName() {
      return (String)this.elements.get("remoteInterfaceClassName");
   }

   public String getIsReentrant() {
      return (String)this.elements.get("isReentrant");
   }

   public Hashtable getAccessControlEntries() {
      return (Hashtable)this.elements.get("accessControlEntries");
   }

   public Vector getAccessControlEntry(String var1) {
      Hashtable var2 = this.getAccessControlEntries();
      Vector var3 = null;
      if (var2 != null) {
         Object var4 = var2.get(var1);
         var3 = new Vector();
         if (var4 instanceof String) {
            var3.addElement((String)var4);
         } else {
            var3 = (Vector)var4;
         }
      }

      return var3;
   }

   public Hashtable getControlDescriptors() {
      return (Hashtable)this.elements.get("controlDescriptors");
   }

   public String getIsolationLevel(String var1) {
      Hashtable var2 = (Hashtable)this.getControlDescriptors().get(var1);
      return var2 != null ? (String)var2.get("isolationLevel") : null;
   }

   public String getTransactionAttribute(String var1) {
      Hashtable var2 = (Hashtable)this.getControlDescriptors().get(var1);
      return var2 != null ? (String)var2.get("transactionAttribute") : (new Short(DDDefaults.getTransactionAttribute())).toString();
   }

   public String getRunAsMode(String var1) {
      Hashtable var2 = (Hashtable)this.getControlDescriptors().get(var1);
      return var2 != null ? (String)var2.get("runAsMode") : null;
   }

   public String getRunAsIdentity(String var1) {
      Hashtable var2 = (Hashtable)this.getControlDescriptors().get(var1);
      return var2 != null ? (String)var2.get("runAsIdentity") : null;
   }

   public Hashtable getEnvironmentProperties() {
      return (Hashtable)this.elements.get("environmentProperties");
   }

   public String getHomeClassName() {
      Hashtable var1 = this.getEnvironmentProperties();
      return var1 != null ? (String)var1.get("homeClassName") : null;
   }

   public String getEjbObjectClassName() {
      Hashtable var1 = this.getEnvironmentProperties();
      return var1 != null ? (String)var1.get("ejbObjectClassName") : null;
   }

   public String getMaxBeansInFreePool() {
      Hashtable var1 = this.getEnvironmentProperties();
      return var1 != null ? (String)var1.get("maxBeansInFreePool") : (new Integer(DDDefaults.getMaxBeansInFreePool())).toString();
   }

   public String getMaxBeansInCache() {
      Hashtable var1 = this.getEnvironmentProperties();
      return var1 != null ? (String)var1.get("maxBeansInCache") : (new Integer(DDDefaults.getMaxBeansInCache())).toString();
   }

   public String getIdleTimeoutSeconds() {
      Hashtable var1 = this.getEnvironmentProperties();
      return var1 != null ? (String)var1.get("idleTimeoutSeconds") : (new Integer(DDDefaults.getIdleTimeoutSeconds())).toString();
   }

   public String getIsModifiedMethodName() {
      Hashtable var1 = this.getEnvironmentProperties();
      return var1 != null ? (String)var1.get("isModifiedMethodName") : null;
   }

   public Boolean getDelayUpdatesUntilEndOfTx() {
      Boolean var1 = new Boolean(DDDefaults.getDelayUpdatesUntilEndOfTx());
      Hashtable var2 = this.getEnvironmentProperties();
      if (var2 != null) {
         Object var3 = var2.get("delayUpdatesUntilEndOfTx");
         if (null != var3) {
            var1 = new Boolean((String)var3);
         }
      }

      return var1;
   }

   public Hashtable getFinderDescriptors() {
      Hashtable var1 = this.getEnvironmentProperties();
      return var1 != null ? (Hashtable)var1.get("finderDescriptors") : null;
   }

   public Hashtable getPersistentStoreProperties() {
      Hashtable var1 = this.getEnvironmentProperties();
      return var1 != null ? (Hashtable)var1.get("persistentStoreProperties") : null;
   }

   public String getPersistentStoreType() {
      Hashtable var1 = this.getPersistentStoreProperties();
      return var1 != null ? (String)var1.get("persistentStoreType") : null;
   }

   public String getPersistentStoreClassName() {
      Hashtable var1 = this.getPersistentStoreProperties();
      return var1 != null ? (String)var1.get("persistentStoreClassName") : null;
   }

   public Hashtable getJdbc() {
      Hashtable var1 = this.getPersistentStoreProperties();
      return var1 != null ? (Hashtable)var1.get("jdbc") : null;
   }

   public String getJdbcTableName() {
      Hashtable var1 = this.getJdbc();
      return var1 != null ? (String)var1.get("tableName") : null;
   }

   public String getJdbcDbIsShared() {
      String var1 = null;
      Hashtable var2 = this.getJdbc();
      if (var2 != null) {
         var1 = (String)var2.get("dbIsShared");
      }

      if (var1 == null) {
         var2 = this.getPersistentStoreProperties();
         if (var2 != null) {
            var1 = (String)var2.get("dbIsShared");
         }
      }

      return var1;
   }

   public String getJdbcPoolName() {
      Hashtable var1 = this.getJdbc();
      return var1 != null ? (String)var1.get("poolName") : null;
   }

   public Hashtable getJdbcAttributeMap() {
      Hashtable var1 = this.getJdbc();
      return var1 != null ? (Hashtable)var1.get("attributeMap") : null;
   }

   public Hashtable getFile() {
      Hashtable var1 = this.getPersistentStoreProperties();
      return var1 != null ? (Hashtable)var1.get("file") : null;
   }

   public String getEntityFilePersistentDirectoryRoot() {
      Hashtable var1 = this.getFile();
      return var1 != null ? (String)var1.get("persistentDirectoryRoot") : null;
   }

   public String getSessionPersistentDirectoryRoot() {
      Hashtable var1 = this.getEnvironmentProperties();
      if (var1 != null) {
         var1 = (Hashtable)var1.get("persistentStoreProperties");
         return var1 != null ? (String)var1.get("persistentDirectoryRoot") : null;
      } else {
         return null;
      }
   }

   public Hashtable getClusterProperties() {
      Hashtable var1 = this.getEnvironmentProperties();
      return var1 != null ? (Hashtable)var1.get("clusterProperties") : null;
   }

   public String getHomeIsClusterable() {
      Hashtable var1 = this.getClusterProperties();
      return var1 != null ? (String)var1.get("homeIsClusterable") : null;
   }

   public String getHomeLoadAlgorithm() {
      Hashtable var1 = this.getClusterProperties();
      return var1 != null ? (String)var1.get("homeLoadAlgorithm") : null;
   }

   public String getHomeCallRouterClass() {
      Hashtable var1 = this.getClusterProperties();
      return var1 != null ? (String)var1.get("homeCallRouterClassName") : null;
   }

   public String getStatelessBeanIsClusterable() {
      Hashtable var1 = this.getClusterProperties();
      return var1 != null ? (String)var1.get("statelessBeanIsClusterable") : (new Boolean(DDDefaults.getStatelessBeanIsClusterable())).toString();
   }

   public String getStatelessBeanLoadAlgorithm() {
      Hashtable var1 = this.getClusterProperties();
      return var1 != null ? (String)var1.get("statelessBeanLoadAlgorithm") : null;
   }

   public String getStatelessBeanCallRouterClassName() {
      Hashtable var1 = this.getClusterProperties();
      return var1 != null ? (String)var1.get("statelessBeanCallRouterClassName") : null;
   }

   public String getStatelessBeanMethodsAreIdempotent() {
      Hashtable var1 = this.getClusterProperties();
      return var1 != null ? (String)var1.get("statelessBeanMethodsAreIdempotent") : "false";
   }

   public String getPrimaryKeyClassName() {
      return (String)this.elements.get("primaryKeyClassName");
   }

   public Vector getContainerManagedFields() {
      Vector var1 = new Vector();
      Object var2 = this.elements.get("containerManagedFields");
      if (var2 instanceof String) {
         var1.addElement((String)var2);
      } else {
         var1 = (Vector)var2;
      }

      return var1;
   }

   public String getStateManagementType() {
      return (String)this.elements.get("stateManagementType");
   }

   public String getSessionTimeout() {
      return (String)this.elements.get("sessionTimeout");
   }

   public String getHomeStubIsReplicaAware() {
      Hashtable var1 = this.getClusterProperties();
      return var1 != null ? (String)var1.get("homeStubIsReplicaAware") : null;
   }

   public String getHomeStubReplicaHandlerClassName() {
      return (String)this.getClusterProperties().get("homeStubReplicaHandlerClassName");
   }

   public String getStatelessBeanStubIsReplicaAware() {
      Hashtable var1 = this.getClusterProperties();
      return var1 != null ? (String)var1.get("statelessBeanStubIsReplicaAware") : null;
   }
}
