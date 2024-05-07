package weblogic.wsee.security.wst.framework;

import org.w3c.dom.Node;
import weblogic.wsee.security.wst.faults.WSTFaultException;

public interface TrustProcessor {
   String getRequestType();

   Node processRequestSecurityToken(Node var1, WSTContext var2) throws WSTFaultException;
}
