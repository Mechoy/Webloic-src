package weblogic.wsee.tools.jws;

import weblogic.wsee.tools.jws.decl.WebServiceDecl;
import weblogic.wsee.wsdl.WsdlDefinitions;

public class WebServiceInfo<T extends WebServiceDecl> {
   protected final T webService;
   protected WsdlDefinitions definitions;

   public WebServiceInfo(T var1) {
      assert var1 != null;

      this.webService = var1;
   }

   public T getWebService() {
      return this.webService;
   }

   public WsdlDefinitions getDefinitions() {
      return this.definitions;
   }

   public void setDefinitions(WsdlDefinitions var1) {
      this.definitions = var1;
   }
}
