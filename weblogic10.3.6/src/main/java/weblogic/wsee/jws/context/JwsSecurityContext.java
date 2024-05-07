package weblogic.wsee.jws.context;

import java.security.Principal;

public interface JwsSecurityContext {
   Principal getCallerPrincipal();

   boolean isCallerInRole(String var1);
}
