package weblogic.wsee.jaxws.tubeline;

import com.sun.xml.ws.api.pipe.ClientTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.ServerTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.Tube;

public interface TubeFactory {
   Tube createClient(Tube var1, ClientTubeAssemblerContext var2);

   Tube createServer(Tube var1, ServerTubeAssemblerContext var2);
}
