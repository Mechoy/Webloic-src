package weblogic.ejb.container.utils.ddconverter;

import java.lang.reflect.Field;
import java.util.Hashtable;

public final class EJB10DescriptorConstants {
   public static final String ENTITY_DESCRIPTOR = "EntityDescriptor";
   public static final String SESSION_DESCRIPTOR = "SessionDescriptor";
   public static final String BEAN_HOME_NAME = "beanHomeName";
   public static final String ENTERPRISE_BEAN_CLASS_NAME = "enterpriseBeanClassName";
   public static final String HOME_INTERFACE_CLASS_NAME = "homeInterfaceClassName";
   public static final String REMOTE_INTERFACE_CLASS_NAME = "remoteInterfaceClassName";
   public static final String IS_REENTRANT = "isReentrant";
   public static final String ACCESS_CONTROL_ENTRIES = "accessControlEntries";
   public static final String CONTROL_DESCRIPTORS = "controlDescriptors";
   public static final String ISOLATION_LEVEL = "isolationLevel";
   public static final String TRANSACTION_ATTRIBUTE = "transactionAttribute";
   public static final String RUN_AS_MODE = "runAsMode";
   public static final String RUN_AS_IDENTITY = "runAsIdentity";
   public static final String ENVIRONMENT_PROPERTIES = "environmentProperties";
   public static final String HOME_CLASS_NAME = "homeClassName";
   public static final String EJB_OBJECT_CLASS_NAME = "ejbObjectClassName";
   public static final String MAX_BEANS_IN_FREE_POOL = "maxBeansInFreePool";
   public static final String MAX_BEANS_IN_CACHE = "maxBeansInCache";
   public static final String IDLE_TIMEOUT_SECONDS = "idleTimeoutSeconds";
   public static final String IS_MODIFIED_METHOD_NAME = "isModifiedMethodName";
   public static final String DELAY_UPDATES_UNTIL_END_OF_TX = "delayUpdatesUntilEndOfTx";
   public static final String FINDER_DESCRIPTORS = "finderDescriptors";
   public static final String PERSISTENT_STORE_PROPERTIES = "persistentStoreProperties";
   public static final String PERSISTENT_STORE_TYPE = "persistentStoreType";
   public static final String PERSISTENT_STORE_CLASS_NAME = "persistentStoreClassName";
   public static final String JDBC = "jdbc";
   public static final String JDBC_TABLE_NAME = "tableName";
   public static final String JDBC_DB_IS_SHARED = "dbIsShared";
   public static final String JDBC_POOL_NAME = "poolName";
   public static final String JDBC_ATTRIBUTE_MAP = "attributeMap";
   public static final String FILE = "file";
   public static final String PERSISTENT_DIRECTORY_ROOT = "persistentDirectoryRoot";
   public static final String CLUSTER_PROPERTIES = "clusterProperties";
   public static final String HOME_IS_CLUSTERABLE = "homeIsClusterable";
   public static final String HOME_LOAD_ALGORITHM = "homeLoadAlgorithm";
   public static final String HOME_CALL_ROUTER_CLASS = "homeCallRouterClassName";
   public static final String STATELESS_BEAN_IS_CLUSTERABLE = "statelessBeanIsClusterable";
   public static final String STATELESS_BEAN_LOAD_ALGORITHM = "statelessBeanLoadAlgorithm";
   public static final String STATELESS_BEAN_CALL_ROUTER_CLASS_NAME = "statelessBeanCallRouterClassName";
   public static final String STATELESS_BEAN_METHODS_ARE_IDEMPOTENT = "statelessBeanMethodsAreIdempotent";
   public static final String PRIMARY_KEY_CLASS_NAME = "primaryKeyClassName";
   public static final String CONTAINER_MANAGED_FIELDS = "containerManagedFields";
   public static final String STATE_MANAGEMENT_TYPE = "stateManagementType";
   public static final String SESSION_TIMEOUT = "sessionTimeout";
   public static final String PSTORE_GENERATOR = "persistentStoreCodeGenerator";
   public static final String PSTORE_GENERATORS = "persistentStoreCodeGenerators";
   public static final String POST_PROCESSOR_CLASS = "postProcessorClassName";
   public static final String HOME_STUB_IS_REPLICA_AWARE = "homeStubIsReplicaAware";
   public static final String HOME_STUB_REPLICA_HANDLER_CLASS_NAME = "homeStubReplicaHandlerClassName";
   public static final String STATELESS_BEAN_STUB_IS_REPLICA_AWARE = "statelessBeanStubIsReplicaAware";

   public static Hashtable getConstantsAsHashtable() {
      Hashtable var0 = null;
      Class var1 = null;
      Field[] var2 = null;
      String var3 = null;
      String var4 = null;

      try {
         var0 = new Hashtable();
         var1 = EJB10DescriptorConstants.class;
         var2 = var1.getFields();

         for(int var5 = 0; var5 < var2.length; ++var5) {
            var3 = var2[var5].getName();
            var4 = (String)var2[var5].get(var3);
            var0.put(var3, var4);
         }
      } catch (IllegalAccessException var6) {
      }

      return var0;
   }
}
