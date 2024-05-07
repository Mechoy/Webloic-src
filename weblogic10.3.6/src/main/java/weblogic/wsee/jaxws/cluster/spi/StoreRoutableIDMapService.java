package weblogic.wsee.jaxws.cluster.spi;

import java.util.List;

public interface StoreRoutableIDMapService extends RoutableIDMapService {
   void startup();

   void shutdown();

   List<String> getLocalPhysicalStoresForLogicalStore(String var1);

   String getServerNameForPhysicalStore(String var1);
}
