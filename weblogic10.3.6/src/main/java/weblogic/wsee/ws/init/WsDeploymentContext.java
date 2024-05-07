package weblogic.wsee.ws.init;

import java.util.ArrayList;
import java.util.List;
import weblogic.ejb.spi.DynamicEJBModule;
import weblogic.wsee.WebServiceType;
import weblogic.wsee.ws.WsService;

public class WsDeploymentContext {
   private WsService wsService;
   private String[] serviceURIs;
   private String contextPath;
   private String version;
   private List<DynamicEJBModule> dynamicEjbs = new ArrayList();
   private List<String> bufferTargetURIs = new ArrayList();
   private WebServiceType type;
   private String serviceName;

   public WsDeploymentContext() {
      this.type = WebServiceType.JAXRPC;
   }

   public String getServiceName() {
      return this.serviceName;
   }

   public void setServiceName(String var1) {
      this.serviceName = var1;
   }

   public List<DynamicEJBModule> getDynamicEjbs() {
      return this.dynamicEjbs;
   }

   public void addDynamicEjb(DynamicEJBModule var1) {
      this.dynamicEjbs.add(var1);
   }

   public void addBufferTargetURI(String var1) {
      this.bufferTargetURIs.add(var1);
   }

   public List<String> getBufferTargetURIs() {
      return this.bufferTargetURIs;
   }

   public WsService getWsService() {
      return this.wsService;
   }

   public void setWsService(WsService var1) {
      this.wsService = var1;
   }

   public String[] getServiceURIs() {
      return this.serviceURIs;
   }

   public void setServiceURIs(String[] var1) {
      this.serviceURIs = var1;
   }

   public void setContextPath(String var1) {
      this.contextPath = var1;
   }

   public String getContextPath() {
      return this.contextPath;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("WS Deployment context");
      var1.append(" service = ").append(this.wsService);
      var1.append(" version = ").append(this.version);
      if (this.serviceURIs != null) {
         for(int var2 = 0; var2 < this.serviceURIs.length; ++var2) {
            var1.append("\nURI[" + var2 + "] = " + this.serviceURIs[var2]);
         }

         var1.append("\nContext Path = " + this.contextPath);
      }

      return var1.toString();
   }

   public String getVersion() {
      return this.version;
   }

   public void setVersion(String var1) {
      this.version = var1;
   }

   public WebServiceType getType() {
      return this.type;
   }

   public void setType(WebServiceType var1) {
      this.type = var1;
   }
}
