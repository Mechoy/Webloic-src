package weblogic.wsee.ws;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.xml.namespace.QName;
import weblogic.wsee.bind.runtime.RuntimeBindings;
import weblogic.wsee.policy.runtime.PolicyServer;
import weblogic.wsee.security.configuration.WssConfiguration;
import weblogic.wsee.security.configuration.WssConfigurationException;
import weblogic.wsee.security.policy.WssPolicyContext;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.wsdl.WsdlPort;
import weblogic.wsee.wsdl.WsdlService;

public final class WsServiceImpl implements WsService {
   public static final boolean debug = false;
   private WsdlService wsdlService;
   private RuntimeBindings bindings;
   private Map endpointList = new LinkedHashMap();
   private Map<String, WsPort> portList = new HashMap();
   private boolean usingPolicy = false;
   private WssPolicyContext wssPolicyCtx;

   WsServiceImpl(WsdlService var1) {
      assert var1 != null;

      this.wsdlService = var1;
   }

   public RuntimeBindings getBindingProvider() {
      if (this.bindings == null) {
         throw new IllegalStateException("Binding provider not set");
      } else {
         return this.bindings;
      }
   }

   void setBindingProvider(RuntimeBindings var1) {
      this.bindings = var1;
   }

   public WsdlService getWsdlService() {
      return this.wsdlService;
   }

   public WsEndpoint getEndpoint(QName var1) {
      return (WsEndpoint)this.endpointList.get(var1);
   }

   public Iterator getEndpoints() {
      return this.endpointList.values().iterator();
   }

   public WsPort getPort(String var1) {
      return (WsPort)this.portList.get(var1);
   }

   public Iterator<WsPort> getPorts() {
      return this.portList.values().iterator();
   }

   public boolean isUsingPolicy() {
      return this.usingPolicy;
   }

   public void setUsingPolicy(boolean var1) {
      this.usingPolicy = var1;
   }

   public WssPolicyContext getWssPolicyContext() {
      return this.wssPolicyCtx;
   }

   public void setWssPolicyContext(WssPolicyContext var1) {
      this.wssPolicyCtx = var1;
   }

   public void addEndpoint(QName var1, WsEndpoint var2) {
      assert var1 != null;

      assert var2 != null;

      this.endpointList.put(var1, var2);
   }

   public WsPort addPort(String var1, WsdlPort var2, WsEndpoint var3) {
      assert var1 != null;

      assert var2 != null;

      assert var3 != null;

      WsPortImpl var4 = new WsPortImpl(var2, var3);
      this.portList.put(var1, var4);
      return var4;
   }

   public void initWssConfiguration() throws WssConfigurationException {
      this.wssPolicyCtx.getWssConfiguration().init();
   }

   public WssConfiguration getWssConfiguration() {
      return this.wssPolicyCtx.getWssConfiguration();
   }

   public PolicyServer getPolicyServer() {
      return this.wssPolicyCtx.getPolicyServer();
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.writeField("endpointList", this.endpointList);
      var1.writeField("wsdlService", this.wsdlService);
      var1.writeField("bindingProvider", this.bindings);
      var1.end();
   }
}
