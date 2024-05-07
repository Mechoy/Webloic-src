package weblogic.wsee.jaxws.injection;

import com.oracle.pitchfork.interfaces.inject.EnricherI;
import java.util.List;
import weblogic.j2ee.descriptor.PortComponentHandlerBean;
import weblogic.j2ee.descriptor.WebAppBean;
import weblogic.j2ee.injection.PitchforkContext;

public class WSEEWebComponentContributor extends WSEEServerComponentContributor {
   private final WebAppBean webBean;
   private final Class jws;

   public WSEEWebComponentContributor(WebAppBean var1, Class var2, List<PortComponentHandlerBean> var3, PitchforkContext var4) {
      super(var3, var4);
      this.webBean = var1;
      this.jws = var2;
   }

   public void contribute(EnricherI var1) {
      this.contribute(var1, this.jws.getName(), this.jws.getName(), this.webBean);
      super.contribute(var1);
   }
}
