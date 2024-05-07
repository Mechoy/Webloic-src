package weblogic.wsee.wstx.wsat.tube;

import com.sun.xml.ws.api.message.Header;
import java.util.List;
import java.util.Map;

public interface WSATClient {
   List<Header> doHandleRequest(TransactionalAttribute var1, Map<String, Object> var2);

   boolean doHandleResponse(Map<String, Object> var1);

   void doHandleException(Map<String, Object> var1);
}
