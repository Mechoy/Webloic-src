package weblogic.wsee.jws.container;

import weblogic.wsee.message.WlMessageContext;

interface ContainerMarker {
   WlMessageContext getUnfilteredMessageContext();

   void setMessageContext(WlMessageContext var1);

   Object getTargetJWS();

   String getId();

   void destroy();
}
