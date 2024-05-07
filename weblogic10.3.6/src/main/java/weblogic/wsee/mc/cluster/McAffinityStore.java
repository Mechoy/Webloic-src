package weblogic.wsee.mc.cluster;

import java.util.logging.Level;
import java.util.logging.Logger;
import weblogic.wsee.jaxws.cluster.spi.RoutingInfo;
import weblogic.wsee.persistence.LogicalStore;
import weblogic.wsee.persistence.StoreException;

public final class McAffinityStore extends LogicalStore<String, RoutingInfo> {
   private static final String CONNECTION_NAME = McAffinityStore.class.getName();
   private static final Logger LOGGER;
   private static final McAffinityStore _instance;

   public static McAffinityStore getStore(String var0) throws StoreException {
      return (McAffinityStore)_instance.getOrCreateLogicalStore(var0, CONNECTION_NAME);
   }

   private McAffinityStore() {
   }

   private McAffinityStore(String var1) throws StoreException {
      super(var1, CONNECTION_NAME);
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine(" == McAffinityStore created for " + this.getName() + " and connection " + CONNECTION_NAME);
      }

   }

   public McAffinityStore createLogicalStore(String var1, String var2) throws StoreException {
      return new McAffinityStore(var1);
   }

   static {
      LOGGER = Logger.getLogger(CONNECTION_NAME);
      _instance = new McAffinityStore();
      _instance.toString();
   }
}
