package weblogic.wsee.jaxws.cluster.spi;

import java.util.Map;

public interface RoutableIDMapService {
   Map<String, String> getCurrentRoutableIDToServerMap() throws Exception;
}
