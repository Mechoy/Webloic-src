package weblogic.wsee.security.wst.framework;

import org.w3c.dom.Node;
import weblogic.wsee.security.wst.faults.WSTFaultException;

public interface TrustRequestor {
   Node newRequestSecurityToken(WSTContext var1) throws WSTFaultException;

   Node renewRequestSecurityToken(TrustToken var1, WSTContext var2) throws WSTFaultException;

   Node cancelRequestSecurityToken(TrustToken var1, WSTContext var2) throws WSTFaultException;

   Node validateRequestSecurityToken(TrustToken var1, WSTContext var2) throws WSTFaultException;
}
