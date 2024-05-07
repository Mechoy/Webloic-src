package weblogic.wsee.tools.jws.type;

import com.bea.util.jam.JClass;
import com.bea.util.jam.JMethod;
import com.bea.util.jam.JParameter;
import java.util.Iterator;
import javax.jws.WebParam;
import javax.xml.namespace.QName;
import weblogic.wsee.bind.buildtime.J2SBindingsBuilder;
import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.jws.decl.WebMethodDecl;
import weblogic.wsee.tools.jws.decl.WebParamDecl;
import weblogic.wsee.tools.jws.jaxrpc.JAXRPCWebServiceInfo;

public class DocLiteralBareProcessor extends MethodProcessor {
   public DocLiteralBareProcessor(JAXRPCWebServiceInfo var1) {
      super(var1);
   }

   public void processMethod(WebMethodDecl var1) throws WsBuildException {
      short var2 = 137;
      J2SBindingsBuilder var3 = this.webServiceInfo.getBindingsBuilder();
      Iterator var4 = var1.getWebParams();

      while(var4.hasNext()) {
         WebParamDecl var5 = (WebParamDecl)var4.next();
         JMethod var6 = var1.getJMethod();
         JClass var7 = (JClass)var6.getParent();
         if (var5.isHeader()) {
            var3.javaTypeToSchemaElement(var7, var5.getRealType(), var5.getElementQName(), var2);
         } else {
            this.processDocumentBareParam(var1, var5, var2);
         }
      }

      this.processDocumentBareReturn(var1, var2);
      this.processExceptionAsElement(var1, var2);
   }

   private void processDocumentBareReturn(WebMethodDecl var1, int var2) throws WsBuildException {
      if (var1.getWebResult().hasReturn()) {
         JMethod var3 = var1.getJMethod();
         JClass var4 = (JClass)var3.getParent();
         J2SBindingsBuilder var5 = this.webServiceInfo.getBindingsBuilder();
         var5.javaTypeToSchemaElement(var4, var1.getJMethod().getReturnType(), var1.getWebResult().getElementQName(), var2);
         this.registerReturnTypes(var4, var1, var2);
      }
   }

   private void processDocumentBareParam(WebMethodDecl var1, WebParamDecl var2, int var3) throws WsBuildException {
      JParameter var4 = var2.getJParameter();
      var2.getJParameter().getAnnotation(WebParam.class);
      QName var5 = var2.getElementQName();
      J2SBindingsBuilder var6 = this.webServiceInfo.getBindingsBuilder();
      JMethod var7 = (JMethod)var4.getParent();
      JClass var8 = (JClass)var7.getParent();
      var6.javaTypeToSchemaElement(var8, var4.getType(), var5, var3);
      this.registerParamTypes(var8, var1, var3);
   }
}
