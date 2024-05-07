package weblogic.wsee.jaxws.tubeline;

import com.sun.xml.ws.api.pipe.ClientTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.ServerTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.Tube;

public abstract class AbstractTubeFactory implements TubeFactory {
   public Tube createClient(Tube var1, ClientTubeAssemblerContext var2) {
      return var1;
   }

   public Tube createServer(Tube var1, ServerTubeAssemblerContext var2) {
      return var1;
   }
}
