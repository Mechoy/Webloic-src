package weblogic.wsee.jaxws.tools.processor.generator;

import com.sun.codemodel.ClassType;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JDocComment;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JVar;
import com.sun.tools.ws.processor.generator.Generator;
import com.sun.tools.ws.processor.generator.GeneratorUtil;
import com.sun.tools.ws.processor.generator.Names;
import com.sun.tools.ws.processor.model.AsyncOperation;
import com.sun.tools.ws.processor.model.AsyncOperationType;
import com.sun.tools.ws.processor.model.Model;
import com.sun.tools.ws.processor.model.Operation;
import com.sun.tools.ws.processor.model.Port;
import com.sun.tools.ws.processor.model.Service;
import com.sun.tools.ws.processor.model.java.JavaInterface;
import com.sun.tools.ws.processor.model.java.JavaMethod;
import com.sun.tools.ws.processor.model.java.JavaParameter;
import com.sun.tools.ws.processor.model.jaxb.JAXBTypeAndAnnotation;
import com.sun.tools.ws.wsdl.document.PortType;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.xml.namespace.QName;
import javax.xml.ws.Response;
import org.xml.sax.Locator;

public class AsyncHandlerGenerator extends Generator {
   private void write(Port var1) {
      JavaInterface var2 = var1.getJavaInterface();
      String var3 = Names.customJavaTypeClassName(var2) + "AsyncHandler";
      if (this.donotOverride && GeneratorUtil.classExists(this.options, var3)) {
         this.log("Class " + var3 + " exists. Not overriding.");
      } else {
         JDefinedClass var4 = null;

         try {
            var4 = this.getClass(var3, ClassType.INTERFACE);
         } catch (JClassAlreadyExistsException var19) {
            QName var6 = (QName)var1.getProperty("com.sun.xml.ws.processor.model.WSDLPortTypeName");
            Locator var7 = null;
            if (var6 != null) {
               PortType var8 = (PortType)var1.portTypes.get(var6);
               if (var8 != null) {
                  var7 = var8.getLocator();
               }
            }

            this.receiver.error(var7, "");
            return;
         }

         if (var4.methods().isEmpty()) {
            JDocComment var5 = var4.javadoc();
            String var20 = var2.getJavaDoc();
            if (var20 != null) {
               var5.add(var20);
               var5.add("\n\n");
            }

            Iterator var21 = this.getJAXWSClassComment().iterator();

            while(var21.hasNext()) {
               String var22 = (String)var21.next();
               var5.add(var22);
            }

            var21 = var1.getOperations().iterator();

            while(var21.hasNext()) {
               Operation var23 = (Operation)var21.next();
               if (var23 instanceof AsyncOperation) {
                  AsyncOperation var9 = (AsyncOperation)var23;
                  if (AsyncOperationType.CALLBACK.equals(var9.getAsyncType())) {
                     JavaMethod var10 = var9.getJavaMethod();
                     String var13 = var23.getJavaDoc();
                     String var14 = "on" + var10.getName().substring(0, 1).toUpperCase(Locale.ENGLISH) + var10.getName().substring(1, var10.getName().length() - 5) + "Response";
                     JMethod var11 = var4.method(1, Void.TYPE, var14);
                     JDocComment var12 = var11.javadoc();
                     if (var13 != null) {
                        var12.add(var13);
                     }

                     List var15 = var10.getParametersList();
                     if (!var15.isEmpty()) {
                        JAXBTypeAndAnnotation var17 = ((JavaParameter)var15.get(var15.size() - 1)).getType().getType();
                        JClass var18 = this.cm.ref(Response.class);
                        JVar var16 = var11.param(var18.narrow((JClass)((JClass)var17.getType().unboxify()).getTypeParameters().get(0)), "response");
                        var17.annotate(var16);
                        var12.addParam(var16);
                     }
                  }
               }
            }

         }
      }
   }

   public void visit(Model var1) throws Exception {
      Iterator var2 = var1.getServices().iterator();

      while(var2.hasNext()) {
         Service var3 = (Service)var2.next();
         var3.accept(this);
      }

   }

   public void visit(Service var1) throws Exception {
      Iterator var2 = var1.getPorts().iterator();

      while(var2.hasNext()) {
         Port var3 = (Port)var2.next();
         this.visitPort(var1, var3);
      }

   }

   private void visitPort(Service var1, Port var2) {
      if (!var2.isProvider()) {
         this.write(var2);
      }
   }
}
