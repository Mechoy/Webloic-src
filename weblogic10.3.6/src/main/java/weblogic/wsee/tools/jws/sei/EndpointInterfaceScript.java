package weblogic.wsee.tools.jws.sei;

import java.util.Date;
import java.util.Iterator;
import weblogic.wsee.tools.jws.decl.WebMethodDecl;
import weblogic.wsee.tools.jws.decl.WebServiceSEIDecl;
import weblogic.wsee.util.jspgen.ScriptException;

public class EndpointInterfaceScript extends EndpointInterface {
   public void generate() throws ScriptException {
      WebServiceSEIDecl var1 = this.getWebService();
      this.out.print("package ");
      this.out.print(this.pkgName());
      this.out.print(";");
      this.out.print("\n");
      this.out.print("\n");
      this.out.print("/**");
      this.out.print("\n");
      this.out.print(" * Generated interface, please do not edit.");
      this.out.print("\n");
      this.out.print(" * Date: [");
      this.out.print(new Date());
      this.out.print("]");
      this.out.print("\n");
      this.out.print(" */");
      this.out.print("\n");
      this.out.print("\n");
      this.out.print("public interface ");
      this.out.print(this.interfaceName());
      this.out.print(" extends java.rmi.Remote {");
      this.out.print("\n");
      Iterator var2 = var1.getWebMethods();

      while(var2.hasNext()) {
         WebMethodDecl var3 = (WebMethodDecl)var2.next();
         this.out.print("\n");
         this.out.print("  /**");
         this.out.print("\n");
         this.out.print("   * Web Method: ");
         this.out.print(var3.getMethodName());
         this.out.print(" ...");
         this.out.print("\n");
         this.out.print("   */");
         this.out.print("\n");
         this.out.print("  ");
         this.out.print(this.returnType(var3));
         this.out.print(" ");
         this.out.print(var3.getMethodName());
         this.out.print("(");
         this.out.print(this.parameters(var3));
         this.out.print(")");
         this.out.print("\n");
         this.out.print("      throws java.rmi.RemoteException");
         this.out.print(this.exceptions(var3));
         this.out.print(";");
      }

      this.out.print("\n");
      this.out.print("}");
      this.out.print("\n");
   }
}
