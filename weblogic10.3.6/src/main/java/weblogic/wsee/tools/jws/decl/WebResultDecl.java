package weblogic.wsee.tools.jws.decl;

import com.bea.staxb.buildtime.WildcardUtil;
import com.bea.util.jam.JMethod;
import javax.jws.WebResult;
import weblogic.wsee.WebServiceType;
import weblogic.wsee.tools.jws.context.JwsBuildContext;
import weblogic.wsee.util.Verbose;

public class WebResultDecl extends WebTypeDecl {
   private static final boolean verbose = Verbose.isVerbose(WebResultDecl.class);
   private String defaultPartName = null;

   public WebResultDecl(JwsBuildContext var1, JMethod var2, WebMethodDecl var3) {
      super(var3, var2, var2.getReturnType(), WebResult.class, getDefaultName(var3));
      this.defaultPartName = this.getDefaultPartName(var1, var3);
   }

   private static String getDefaultName(WebMethodDecl var0) {
      return var0.getWebService().getType() == WebServiceType.JAXWS && var0.getSoapBinding().isDocLiteralBare() ? var0.getName() + "Response" : "return";
   }

   private String getDefaultPartName(JwsBuildContext var1, WebMethodDecl var2) {
      boolean var4 = true;
      Object var5 = var1.getProperties().get("jwsc.dotNetStyle");
      if (var5 != null && var5 instanceof Boolean) {
         Boolean var6 = (Boolean)var5;
         var4 = var6;
      }

      String var3;
      if (var2.getSoapBinding().isDocLiteralWrapped()) {
         if (var4) {
            var3 = "parameters";
         } else if (!WildcardUtil.WILDCARD_CLASSNAMES.contains(this.getType())) {
            var3 = "returnParameters";
         } else if (this.isBoundToAnyType()) {
            var3 = "returnParameters";
         } else {
            if (verbose) {
               Verbose.log((Object)"  for doclitwrapped <any/> partName is empty");
            }

            var3 = "";
         }
      } else {
         var3 = this.getWebTypeDeclName();
      }

      return var3;
   }

   public boolean hasReturn() {
      return this.getType() != null;
   }

   String getDefaultPartName() {
      return this.defaultPartName;
   }
}
