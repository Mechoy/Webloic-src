package weblogic.wsee.tools.jws.policy;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import weblogic.utils.FileUtils;
import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.jws.decl.PolicyDecl;
import weblogic.wsee.tools.jws.decl.WebMethodDecl;
import weblogic.wsee.tools.jws.decl.WebServiceSEIDecl;
import weblogic.wsee.tools.jws.jaxrpc.JAXRPCProcessor;
import weblogic.wsee.tools.jws.jaxrpc.JAXRPCWebServiceInfo;

public class PolicyProcessor extends JAXRPCProcessor {
   protected void processImpl(JAXRPCWebServiceInfo var1) throws WsBuildException {
      if (!this.moduleInfo.isWsdlOnly()) {
         File var2 = new File(this.moduleInfo.getOutputDir(), "policies");
         var2.mkdirs();

         try {
            Iterator var3 = var1.getWebService().getPoilices();
            copyPolicies(var1.getWebService(), var2, var3);
            Iterator var4 = var1.getWebService().getWebMethods();

            while(var4.hasNext()) {
               WebMethodDecl var5 = (WebMethodDecl)var4.next();
               copyPolicies(var1.getWebService(), var2, var5.getPoilices());
            }

         } catch (IOException var6) {
            throw new WsBuildException(var6);
         }
      }
   }

   private static void copyPolicies(WebServiceSEIDecl var0, File var1, Iterator<PolicyDecl> var2) throws IOException, WsBuildException {
      while(var2.hasNext()) {
         PolicyDecl var3 = (PolicyDecl)var2.next();
         if (!var3.isBuiltInPolicy() && !var3.isAttachToWsdl() && var3.isRelativeUri()) {
            if (var0.isAsyncResponseRequired()) {
               throw new WsBuildException("attribute \"attachToWsdl\" of @Policy must be true for async call");
            }

            URL var4 = var3.getPolicyURI().toURL();
            InputStream var5 = null;

            try {
               var5 = var4.openStream();
               FileUtils.writeToFile(var5, new File(var1, var3.getUri()));
            } finally {
               if (var5 != null) {
                  var5.close();
               }

            }
         }
      }

   }
}
