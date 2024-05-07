package weblogic.wsee.tools.jws.type;

import com.bea.util.jam.JClass;
import com.bea.util.jam.JMethod;
import java.util.Iterator;
import weblogic.wsee.bind.buildtime.J2SBindingsBuilder;
import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.jws.decl.WebMethodDecl;
import weblogic.wsee.tools.jws.decl.WebParamDecl;
import weblogic.wsee.tools.jws.jaxrpc.JAXRPCWebServiceInfo;

public class RpcProcessor extends MethodProcessor {
   public RpcProcessor(JAXRPCWebServiceInfo var1) {
      super(var1);
   }

   public void processMethod(WebMethodDecl var1) throws WsBuildException {
      int var2 = var1.getSoapBinding().isLiteral() ? 5 : 6;
      J2SBindingsBuilder var3 = this.webServiceInfo.getBindingsBuilder();
      JMethod var4 = var1.getJMethod();
      JClass var5 = (JClass)var4.getParent();
      JClass var6 = var4.getReturnType();
      if (var6 != null && !var6.isVoidType()) {
         var3.javaTypeToSchemaType(var5, var6, var2);
         this.registerReturnTypes(var5, var1, var2);
      }

      Iterator var7 = var1.getWebParams();

      while(var7.hasNext()) {
         WebParamDecl var8 = (WebParamDecl)var7.next();
         if (var8.isHeader()) {
            var3.javaTypeToSchemaElement(var5, var8.getRealType(), var8.getElementQName(), var2);
         } else {
            var3.javaTypeToSchemaType(var5, var8.getRealType(), var2);
         }
      }

      this.registerParamTypes(var5, var1, var2);
      this.processExceptionAsType(var1, var2);
   }
}
