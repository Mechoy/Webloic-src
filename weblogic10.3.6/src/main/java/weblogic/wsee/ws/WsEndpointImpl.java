package weblogic.wsee.ws;

import java.util.HashMap;
import java.util.Iterator;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.wsdl.WsdlPortType;

public class WsEndpointImpl extends WsEndpoint {
   private WsdlPortType portType;
   private HashMap methods = new HashMap();
   private WsService wsService;
   private Class endpointInterface;
   private Class jwsClass;
   private String defaultMethod;

   WsEndpointImpl(WsService var1, WsdlPortType var2) {
      assert var2 != null;

      assert var1 != null;

      this.portType = var2;
      this.wsService = var1;
   }

   public WsService getService() {
      return this.wsService;
   }

   public WsdlPortType getPortType() {
      return this.portType;
   }

   public Class getJwsClass() {
      return this.jwsClass;
   }

   public void setJwsClass(Class var1) {
      this.jwsClass = var1;
   }

   public WsMethod getMethod(String var1) {
      return (WsMethod)this.methods.get(var1);
   }

   public Iterator getMethods() {
      return this.methods.values().iterator();
   }

   public void setDefaultMethod(String var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Null default method");
      } else {
         this.defaultMethod = var1;
      }
   }

   public WsMethod getDefaultMethod() {
      WsMethod var1 = null;
      if (this.defaultMethod != null) {
         var1 = this.getMethod(this.defaultMethod);
         if (var1 == null) {
            throw new AssertionError("got null for defaultMethod" + this.defaultMethod);
         }
      }

      return var1;
   }

   public Class getEndpointInterface() {
      return this.endpointInterface;
   }

   void setEndpointInterface(Class var1) {
      this.endpointInterface = var1;
   }

   void addMethod(String var1, WsMethod var2) {
      this.methods.put(var1, var2);
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.writeMap("methods", this.methods);
      var1.writeField("portType", this.portType.getName());
      var1.writeField("endpointInterface", this.endpointInterface);
      var1.end();
   }
}
