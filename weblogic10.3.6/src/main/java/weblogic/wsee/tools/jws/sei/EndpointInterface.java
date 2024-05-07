package weblogic.wsee.tools.jws.sei;

import com.bea.util.jam.JClass;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.rmi.RemoteException;
import java.util.Iterator;
import weblogic.wsee.WebServiceType;
import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.jws.JWSProcessor;
import weblogic.wsee.tools.jws.ModuleInfo;
import weblogic.wsee.tools.jws.WebServiceInfo;
import weblogic.wsee.tools.jws.decl.WebMethodDecl;
import weblogic.wsee.tools.jws.decl.WebParamDecl;
import weblogic.wsee.tools.jws.decl.WebServiceSEIDecl;
import weblogic.wsee.tools.jws.jaxrpc.JAXRPCWebServiceInfo;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.util.jspgen.JspGenBase;
import weblogic.wsee.util.jspgen.ScriptException;

public abstract class EndpointInterface extends JspGenBase implements JWSProcessor {
   private WebServiceSEIDecl webServiceDecl = null;
   private ModuleInfo moduleInfo = null;

   WebServiceSEIDecl getWebService() {
      return this.webServiceDecl;
   }

   String pkgName() {
      return StringUtil.getPackage(this.webServiceDecl.getEndpointInterfaceName());
   }

   String interfaceName() {
      return StringUtil.getSimpleClassName(this.webServiceDecl.getEndpointInterfaceName());
   }

   String exceptions(WebMethodDecl var1) {
      StringBuffer var2 = new StringBuffer();
      JClass[] var3 = var1.getJMethod().getExceptionTypes();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         JClass var5 = var3[var4];
         String var6 = var5.getQualifiedName();
         if (!RemoteException.class.getName().equals(var6)) {
            var2.append(", " + var6);
         }
      }

      return var2.toString();
   }

   String parameters(WebMethodDecl var1) {
      StringBuffer var2 = new StringBuffer();
      Iterator var3 = var1.getWebParams();

      while(var3.hasNext()) {
         WebParamDecl var4 = (WebParamDecl)var3.next();
         var2.append(this.convertInnerClassString(var4.getType()));
         var2.append(" ");
         var2.append(var4.getParameterName());
         if (var3.hasNext()) {
            var2.append(",");
         }
      }

      return var2.toString();
   }

   String returnType(WebMethodDecl var1) {
      return this.convertInnerClassString(var1.getJMethod().getReturnType().getQualifiedName());
   }

   public void init(ModuleInfo var1) {
      this.moduleInfo = var1;
   }

   public void finish() {
   }

   public void process(WebServiceInfo var1) throws WsBuildException {
      if (var1.getWebService().getType() == WebServiceType.JAXRPC) {
         if (!this.moduleInfo.isWsdlOnly()) {
            JAXRPCWebServiceInfo var2 = (JAXRPCWebServiceInfo)var1;
            this.webServiceDecl = var2.getWebService();

            try {
               ByteArrayOutputStream var3 = new ByteArrayOutputStream();
               String var4 = this.moduleInfo.getJwsBuildContext().getCodegenOutputEncoding();
               PrintStream var5 = var4 == null ? new PrintStream(var3, true) : new PrintStream(var3, true, var4);
               this.setOutput(var5);
               this.generate();
               var2.setEndpointInterface(var3.toByteArray());
               var3.close();
            } catch (ScriptException var6) {
               throw new WsBuildException("Failed to generate endpoint interface java file", var6);
            } catch (IOException var7) {
               throw new WsBuildException("Failed to generate endpoint interface java file", var7);
            }
         }
      }
   }

   String convertInnerClassString(String var1) {
      return var1.replace('$', '.');
   }
}
