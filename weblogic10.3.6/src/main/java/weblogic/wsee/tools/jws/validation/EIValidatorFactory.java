package weblogic.wsee.tools.jws.validation;

import weblogic.wsee.WebServiceType;
import weblogic.wsee.tools.jws.context.JwsBuildContext;
import weblogic.wsee.tools.jws.decl.CallbackServiceDecl;
import weblogic.wsee.tools.jws.decl.WebServiceSEIDecl;
import weblogic.wsee.tools.jws.validation.jaxrpc.CallbackValidator;
import weblogic.wsee.tools.jws.validation.jaxws.EIValidator;

public class EIValidatorFactory {
   public static Validator newInstance(JwsBuildContext var0, WebServiceSEIDecl var1) {
      if (var1.getType() == WebServiceType.JAXWS) {
         return new EIValidator(var0, var1);
      } else if (var1.getType() == WebServiceType.JAXRPC) {
         return (Validator)(var1 instanceof CallbackServiceDecl ? new CallbackValidator(var0, (CallbackServiceDecl)var1) : new weblogic.wsee.tools.jws.validation.jaxrpc.EIValidator(var0, var1));
      } else {
         return null;
      }
   }
}
