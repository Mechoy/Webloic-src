package weblogic.wsee.tools.source;

import com.bea.util.jam.JClass;
import com.bea.util.jam.JMethod;
import java.util.HashMap;
import weblogic.wsee.bind.buildtime.BuildtimeBindings;
import weblogic.wsee.bind.buildtime.internal.WLW81SchemaAndJavaBinder;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlOperation;
import weblogic.wsee.wsdl.builder.WsdlOperationBuilder;

public class Wlw81EndpointBuilder extends EndpointBuilder {
   private JClass serviceInterfaceJClass = null;
   private HashMap<String, JMethod> methodMap = new HashMap();
   private HashMap<String, JMethod> callbackMethodMap = new HashMap();

   public Wlw81EndpointBuilder(WsdlDefinitions var1, BuildtimeBindings var2, String var3) {
      super(var1, var2, var3);
   }

   public void setServiceInterfaceJClass(JClass var1) {
      this.serviceInterfaceJClass = var1;
      if (var1 != null) {
         this.methodMap.clear();
         this.callbackMethodMap.clear();
         WLW81SchemaAndJavaBinder.populateMethodMap(var1, this.methodMap, this.callbackMethodMap);
      }

   }

   protected void addMethod(JsClass var1, WsdlOperationBuilder var2, String var3, String var4, String var5) {
      JMethod var6 = this.getJMethodThatMapsToOperation(var2);
      if (null != var6) {
         super.addMethod(var1, var2, var3, var4, var5);
      }

   }

   protected boolean detectWrappedVirtual(boolean var1, WsdlOperation var2) {
      if (this.serviceInterfaceJClass != null) {
         JMethod var3 = this.getJMethodThatMapsToOperation(var2);
         if (var3 != null) {
            return WLW81SchemaAndJavaBinder.isWrapped(var3, this.serviceInterfaceJClass);
         }
      }

      return super.detectWrappedVirtual(var1, var2);
   }

   private JMethod getJMethodThatMapsToOperation(WsdlOperation var1) {
      String var2 = var1.getName().getLocalPart();
      JMethod var3 = (JMethod)this.methodMap.get(var2);
      if (null == var3) {
         var3 = (JMethod)this.callbackMethodMap.get(var2);
      }

      return var3;
   }
}
