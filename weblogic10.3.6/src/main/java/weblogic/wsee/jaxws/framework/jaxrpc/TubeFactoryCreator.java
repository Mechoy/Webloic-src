package weblogic.wsee.jaxws.framework.jaxrpc;

import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.Handler;
import javax.xml.rpc.handler.HandlerInfo;

public class TubeFactoryCreator implements weblogic.wsee.jaxws.tubeline.TubeFactoryCreator {
   public weblogic.wsee.jaxws.tubeline.TubeFactory create(Class var1) {
      return Handler.class.isAssignableFrom(var1) ? new TubeFactory(new HandlerInfo(var1, (Map)null, (QName[])null)) : null;
   }
}
