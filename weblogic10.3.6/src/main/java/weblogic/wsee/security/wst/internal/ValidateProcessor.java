package weblogic.wsee.security.wst.internal;

import org.w3c.dom.Node;
import weblogic.wsee.security.wst.faults.WSTFaultException;
import weblogic.wsee.security.wst.framework.TrustProcessor;
import weblogic.wsee.security.wst.framework.WSTContext;

public class ValidateProcessor implements TrustProcessor {
   public String getRequestType() {
      return "/Validate";
   }

   public Node processRequestSecurityToken(Node var1, WSTContext var2) throws WSTFaultException {
      return null;
   }
}
