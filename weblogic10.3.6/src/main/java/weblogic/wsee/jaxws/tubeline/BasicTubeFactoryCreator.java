package weblogic.wsee.jaxws.tubeline;

import com.sun.xml.ws.api.pipe.ClientTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.ServerTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.Tube;
import java.lang.reflect.Constructor;
import javax.xml.ws.WebServiceException;

public class BasicTubeFactoryCreator implements TubeFactoryCreator {
   public TubeFactory create(Class var1) {
      if (!Tube.class.isAssignableFrom(var1)) {
         return null;
      } else {
         try {
            final Constructor var2 = var1.getConstructor(Tube.class);
            return new TubeFactory() {
               public Tube createClient(Tube var1, ClientTubeAssemblerContext var2x) {
                  return this.create(var1);
               }

               public Tube createServer(Tube var1, ServerTubeAssemblerContext var2x) {
                  return this.create(var1);
               }

               private Tube create(Tube var1) {
                  try {
                     return (Tube)var2.newInstance(var1);
                  } catch (Throwable var3) {
                     throw new WebServiceException(var3);
                  }
               }
            };
         } catch (NoSuchMethodException var3) {
            return null;
         }
      }
   }
}
