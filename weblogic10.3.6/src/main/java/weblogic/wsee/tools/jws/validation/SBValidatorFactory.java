package weblogic.wsee.tools.jws.validation;

import weblogic.wsee.WebServiceType;
import weblogic.wsee.tools.jws.context.JwsBuildContext;
import weblogic.wsee.tools.jws.decl.WebServiceSEIDecl;
import weblogic.wsee.tools.jws.validation.jaxrpc.SBValidator;

public class SBValidatorFactory {
   public static Validator newInstance(JwsBuildContext var0, WebServiceSEIDecl var1, boolean var2) {
      if (var1.getType() == WebServiceType.JAXRPC) {
         return new SBValidator(var0, var1, var2);
      } else {
         return var1.getType() == WebServiceType.JAXWS ? new weblogic.wsee.tools.jws.validation.jaxws.SBValidator(var0, var1, var2) : null;
      }
   }
}
