package weblogic.wsee.tools.jws.build;

import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.jws.decl.WebServiceDecl;

public class CompileException extends WsBuildException {
   private WebServiceDecl webServiceDecl;

   public CompileException(WebServiceDecl var1) {
      super(buildMessage(var1));
      this.webServiceDecl = var1;
   }

   public CompileException(WebServiceDecl var1, Throwable var2) {
      super(buildMessage(var1), var2);
      this.webServiceDecl = var1;
   }

   private static String buildMessage(WebServiceDecl var0) {
      StringBuilder var1 = new StringBuilder("Error compiling web service");
      if (var0 != null) {
         var1.append(": ");
         var1.append(var0.getSourceFile());
      }

      return var1.toString();
   }

   public WebServiceDecl getWebServiceDecl() {
      return this.webServiceDecl;
   }
}
