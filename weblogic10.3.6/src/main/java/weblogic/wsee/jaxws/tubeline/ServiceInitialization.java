package weblogic.wsee.jaxws.tubeline;

import com.sun.xml.ws.api.WSService;
import javax.xml.ws.Service;

public interface ServiceInitialization {
   void init(WSService var1, Class<? extends Service> var2);
}
