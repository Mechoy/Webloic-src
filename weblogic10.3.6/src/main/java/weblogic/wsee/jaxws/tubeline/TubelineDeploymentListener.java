package weblogic.wsee.jaxws.tubeline;

import com.sun.xml.ws.api.pipe.ClientTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.ServerTubeAssemblerContext;
import java.util.Set;

public interface TubelineDeploymentListener {
   void createClient(ClientTubeAssemblerContext var1, Set<TubelineAssemblerItem> var2);

   void createServer(ServerTubeAssemblerContext var1, Set<TubelineAssemblerItem> var2);
}
