package weblogic.wsee.tools.jws.decl;

import weblogic.wsee.tools.jws.build.JwsInfo;
import weblogic.wsee.tools.jws.context.JwsBuildContext;
import weblogic.wsee.tools.jws.validation.jaxws.WebServiceProviderValidator;

public class WebServiceProviderDecl extends WebServiceDecl {
   public WebServiceProviderDecl(JwsBuildContext var1, JwsInfo var2, String var3) {
      super(var1, var2, var3, (WebServiceDecl)null);
   }

   public boolean validate() {
      WebServiceProviderValidator var1 = new WebServiceProviderValidator(this.ctx, this);
      return var1.validate();
   }
}
