package weblogic.wsee.tools.wsdlc.jaxws;

import com.sun.tools.ws.processor.model.Operation;
import com.sun.tools.ws.processor.model.java.JavaMethod;
import com.sun.tools.ws.util.ClassNameInfo;
import java.util.Iterator;
import weblogic.wsee.tools.wsdlc.WsdlcUtils;
import weblogic.wsee.util.jspgen.ScriptException;

public class JwsImpl extends JwsBase {
   public void generate() throws ScriptException {
      this.out.print("package ");
      this.out.print(ClassNameInfo.getQualifier(this.service.getJavaInterface().getImpl()));
      this.out.print(";");
      this.out.print("\n");
      this.out.print("\n");
      this.out.print("import javax.jws.WebService;");
      this.out.print("\n");
      this.out.print("import javax.xml.ws.BindingType;");
      this.out.print("\n");
      this.out.print("\n");
      this.out.print("/**");
      this.out.print("\n");
      this.out.print(" * ");
      this.out.print(this.service.getJavaInterface().getImpl());
      this.out.print(" class implements web service endpoint interface ");
      this.out.print(this.port.getJavaInterface().getName());
      this.out.print(" */");
      this.out.print("\n");
      this.out.print("\n");
      this.out.print("@WebService(");
      this.out.print("\n");
      this.out.print("  portName=\"");
      this.out.print(this.port.getName().getLocalPart());
      this.out.print("\",");
      this.out.print("\n");
      this.out.print("  serviceName=\"");
      this.out.print(this.service.getName().getLocalPart());
      this.out.print("\",");
      this.out.print("\n");
      this.out.print("  targetNamespace=\"");
      this.out.print(this.service.getName().getNamespaceURI());
      this.out.print("\",");
      this.out.print("\n");
      this.out.print("  endpointInterface=\"");
      this.out.print(this.port.getJavaInterface().getName());
      this.out.print("\",");
      this.out.print("\n");
      this.out.print("  wsdlLocation=\"");
      this.out.print(this.wsdlLocation);
      this.out.print("\")");
      this.out.print("\n");
      this.out.print("@BindingType(value=\"");
      this.out.print(this.bindingType);
      this.out.print("\")");
      this.out.print("\n");
      this.out.print("public class ");
      this.out.print(ClassNameInfo.getName(this.service.getJavaInterface().getImpl()));
      this.out.print(" implements ");
      this.out.print(this.port.getJavaInterface().getName());
      this.out.print(" {");
      this.out.print("\n");
      this.out.print("\n");
      this.out.print("  public ");
      this.out.print(ClassNameInfo.getName(this.service.getJavaInterface().getImpl()));
      this.out.print("() {");
      this.out.print("\n");
      this.out.print("  }");
      this.out.print("\n");
      Iterator var1 = this.port.getOperations().iterator();

      while(var1.hasNext()) {
         Operation var2 = (Operation)var1.next();
         JavaMethod var3 = var2.getJavaMethod();
         this.out.print("\n");
         this.out.print("  public ");
         this.out.print(var3.getReturnType().getName());
         this.out.print(" ");
         this.out.print(var3.getName());
         this.out.print("(");
         this.out.print(getArgumentString(var3));
         this.out.print(") ");
         this.out.print(getThrowsClause(var3));
         this.out.print(" {");
         this.out.print("\n");
         this.out.print("    //replace with your impl here");
         this.out.print("\n");
         this.out.print("     ");
         this.out.print(WsdlcUtils.getReturnString(var3.getReturnType().getName()));
         this.out.print("\n");
         this.out.print("  }");
         this.out.print("\n");
      }

      this.out.print("}  ");
   }
}
