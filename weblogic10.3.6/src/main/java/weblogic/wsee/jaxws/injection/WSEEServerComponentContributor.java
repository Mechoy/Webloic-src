package weblogic.wsee.jaxws.injection;

import com.oracle.pitchfork.interfaces.inject.EnricherI;
import java.util.Iterator;
import java.util.List;
import weblogic.j2ee.descriptor.PortComponentHandlerBean;
import weblogic.j2ee.injection.PitchforkContext;

public class WSEEServerComponentContributor extends WSEEComponentContributor {
   private final List<PortComponentHandlerBean> handlerBeans;

   public WSEEServerComponentContributor(List<PortComponentHandlerBean> var1, PitchforkContext var2) {
      super(var2);
      this.handlerBeans = var1;
   }

   public void contribute(EnricherI var1) {
      if (this.handlerBeans != null) {
         Iterator var2 = this.handlerBeans.iterator();

         while(var2.hasNext()) {
            PortComponentHandlerBean var3 = (PortComponentHandlerBean)var2.next();
            this.contribute(var1, var3.getHandlerClass(), var3.getHandlerClass(), var3);
         }
      }

   }
}
