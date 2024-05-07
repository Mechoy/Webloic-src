package weblogic.wsee.tools.wsdlc.jaxrpc;

import java.util.Iterator;
import weblogic.wsee.tools.source.JsMethod;
import weblogic.wsee.tools.wsdlc.WsdlcUtils;
import weblogic.wsee.util.jspgen.ScriptException;

public class JwsImpl extends JwsBase {
   public void generate() throws ScriptException {
      this.out.print("package ");
      this.out.print(this.jws.getBinding().getPackageName());
      this.out.print(";");
      this.out.print("\n");
      this.out.print("\n");
      this.out.print("import javax.jws.WebService;");
      this.out.print("\n");
      this.out.print("import weblogic.jws.*;");
      this.out.print("\n");
      String var1 = this.jws.getServiceClassName();
      String var2 = this.jws.getImplClassName();
      this.out.print("\n");
      this.out.print("/**");
      this.out.print("\n");
      this.out.print(" * ");
      this.out.print(var2);
      this.out.print(" class implements web service endpoint interface ");
      this.out.print(var1);
      this.out.print(" */");
      this.out.print("\n");
      this.out.print("\n");
      this.out.print("@WebService(");
      this.out.print("\n");
      this.out.print("  serviceName=\"");
      this.out.print(this.jws.getService().getName().getLocalPart());
      this.out.print("\",");
      this.out.print("\n");
      this.out.print("  targetNamespace=\"");
      this.out.print(this.jws.getService().getName().getNamespaceURI());
      this.out.print("\",");
      this.out.print("\n");
      this.out.print("  endpointInterface=\"");
      this.out.print(this.jws.getBinding().getPackageName());
      this.out.print(".");
      this.out.print(var1);
      this.out.print("\")");
      this.out.print("\n");
      Iterator var3 = this.jws.getAddressInfos().iterator();

      while(var3.hasNext()) {
         AddressInfo var4 = (AddressInfo)var3.next();
         this.out.println(var4.toString());
      }

      this.out.print("public class ");
      this.out.print(var2);
      this.out.print(" implements ");
      this.out.print(var1);
      if (this.jws.isConversational()) {
         this.out.print(", java.io.Serializable");
      }

      this.out.print(" {");
      this.out.print("\n");
      this.out.print(" ");
      this.out.print("\n");
      this.out.print("  public ");
      this.out.print(var2);
      this.out.print("() {");
      this.out.print("\n");
      this.out.print("  ");
      this.out.print("\n");
      this.out.print("  }");
      this.out.print("\n");
      JsMethod[] var7 = this.jws.getBinding().getMethods();

      for(int var8 = 0; var8 < var7.length; ++var8) {
         JsMethod var5 = var7[var8];
         String var6 = var5.getReturnType().getType();
         this.out.print("\n");
         this.out.print("  public ");
         this.out.print(var6);
         this.out.print(" ");
         this.out.print(var5.getMethodName());
         this.out.print("(");
         this.out.print(var5.getArgumentString());
         this.out.print(") ");
         this.out.print("\n");
         this.out.print("    ");
         this.out.print(WsdlcUtils.throwException(var5));
         this.out.print(" ");
         this.out.print("\n");
         this.out.print("  {");
         this.out.print("\n");
         this.out.print("    // TODO replace with your impl here");
         this.out.print("\n");
         this.out.print("     ");
         this.out.print(WsdlcUtils.getReturnString(var6));
         this.out.print("     ");
         this.out.print("\n");
         this.out.print("  }");
         this.out.print("\n");
      }

      this.out.print("}  ");
   }
}
