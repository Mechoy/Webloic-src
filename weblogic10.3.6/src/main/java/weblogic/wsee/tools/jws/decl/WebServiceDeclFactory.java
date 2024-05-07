package weblogic.wsee.tools.jws.decl;

import javax.jws.WebService;
import javax.xml.ws.WebServiceProvider;
import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.jws.build.JwsInfo;
import weblogic.wsee.tools.jws.context.JwsBuildContext;

public class WebServiceDeclFactory {
   private final JwsBuildContext ctx;
   private final String contextPath;

   public WebServiceDeclFactory(JwsBuildContext var1) {
      this(var1, (String)null);
   }

   public WebServiceDeclFactory(JwsBuildContext var1, String var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("ctx must not be null");
      } else {
         this.ctx = var1;
         this.contextPath = var2;
      }
   }

   public <T extends WebServiceDecl> T newInstance(JwsInfo var1) throws WsBuildException {
      Object var2 = null;
      if (var1.getJClass().getAnnotation(WebService.class) != null) {
         var2 = new WebServiceSEIDecl(this.ctx, var1, this.contextPath);
      } else if (var1.getJClass().getAnnotation(WebServiceProvider.class) != null) {
         var2 = new WebServiceProviderDecl(this.ctx, var1, this.contextPath);
      }

      if (var2 == null) {
         throw new WsBuildException(var1.getJClass().getQualifiedName() + " is not a known web service type.");
      } else if (this.ctx.isInError()) {
         throw new WsBuildException("JWS build failed: " + this.ctx.getErrorMsgs());
      } else {
         return (WebServiceDecl)var2;
      }
   }
}
