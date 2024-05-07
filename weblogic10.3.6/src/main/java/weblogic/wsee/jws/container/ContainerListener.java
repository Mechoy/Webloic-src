package weblogic.wsee.jws.container;

import java.io.Serializable;
import weblogic.wsee.jws.JwsContext;
import weblogic.wsee.message.WlMessageContext;

public interface ContainerListener extends Serializable, JwsContext.Callback {
   void preInvoke(WlMessageContext var1) throws Exception;

   void postInvoke() throws Exception;
}
