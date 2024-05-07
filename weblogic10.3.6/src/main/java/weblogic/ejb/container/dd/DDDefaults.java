package weblogic.ejb.container.dd;

import weblogic.ejb.container.interfaces.BeanInfo;

public final class DDDefaults {
   public static int getTxTimeoutSeconds() {
      return 0;
   }

   public static boolean getCallByReference() {
      return true;
   }

   public static boolean getUseDistributedTx() {
      return true;
   }

   /** @deprecated */
   public static short getTransactionAttribute() {
      return getTransactionAttribute((BeanInfo)null);
   }

   public static short getTransactionAttribute(BeanInfo var0) {
      return (short)(var0.isEJB30() ? 1 : 2);
   }

   /** @deprecated */
   public static short getBeanMethodTransactionAttribute() {
      return getBeanMethodTransactionAttribute((BeanInfo)null);
   }

   public static short getBeanMethodTransactionAttribute(BeanInfo var0) {
      return (short)(var0.isEJB30() ? 1 : 0);
   }

   public static boolean getIsDurableDestination() {
      return false;
   }

   public static String getInitialContextFactory() {
      return "weblogic.jndi.WLInitialContextFactory";
   }

   public static String getConnectionFactoryName() {
      return "weblogic.jms.MessageDrivenBeanConnectionFactory";
   }

   public static int getMaxConcurrentInstances() {
      return getMaxBeansInFreePool();
   }

   public static String getReplicationType() {
      return "None";
   }

   public static boolean getHomeIsClusterable() {
      return true;
   }

   public static boolean getStatelessBeanIsClusterable() {
      return true;
   }

   public static int getMaxBeansInCache() {
      return 1000;
   }

   public static int getMaxQueriesInCache() {
      return -1;
   }

   public static int getMaxBeansInFreePool() {
      return 1000;
   }

   public static int getInitialBeansInFreePool() {
      return 0;
   }

   public static int getIdleTimeoutSeconds() {
      return 600;
   }

   public static String getCacheType() {
      return "NRU";
   }

   public static String getConcurrencyStrategy() {
      return "Database";
   }

   public static int getReadTimeoutSeconds() {
      return 600;
   }

   public static String getCmpVersion() {
      return "2.x";
   }

   public static boolean getDbIsShared() {
      return true;
   }

   public static boolean getDelayUpdatesUntilEndOfTx() {
      return true;
   }

   public static boolean getFindersLoadBean() {
      return true;
   }

   public static boolean getUseServersideStubs() {
      return false;
   }

   public static boolean getPassivateDuringReplication() {
      return true;
   }
}
