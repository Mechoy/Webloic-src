package weblogic.wsee.tools.jws.decl.port;

import weblogic.wsee.tools.jws.context.JwsBuildContext;
import weblogic.wsee.util.PathUtil;

public abstract class Port implements PortDecl {
   private String contextPath = null;
   private String serviceUri = null;
   private String portName = null;
   private JwsBuildContext ctx = null;
   protected String protocol = "http";

   public Port() {
   }

   public Port(String var1, String var2, String var3) {
      this.contextPath = var1;
      this.serviceUri = var2;
      this.portName = var3;
   }

   public String getPortName() {
      return this.portName;
   }

   public void setPortName(String var1) {
      this.portName = var1;
   }

   public String getContextPath() {
      return this.contextPath;
   }

   public String getNormalizedPath() {
      return PathUtil.normalizePath(this.getContextPath()) + "/" + this.getServiceUri();
   }

   public void setContextPath(String var1) {
      this.contextPath = var1;
      if (this.ctx != null) {
         this.ctx.getProperties().put("jwsc.contextPathOverride", true);
      }

   }

   public String getServiceUri() {
      return this.serviceUri;
   }

   public void setServiceUri(String var1) {
      this.serviceUri = var1;
   }

   public void setProtocol(String var1) {
      this.protocol = var1;
   }

   public String getURI() {
      StringBuffer var1 = new StringBuffer(this.getProtocol());
      if ("https".equals(this.getProtocol())) {
         var1.append("://localhost:7002");
      } else {
         var1.append("://localhost:7001");
      }

      var1.append(PathUtil.normalizePath(this.getContextPath()));
      var1.append(this.getServiceUri());
      return var1.toString();
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof Port)) {
         return false;
      } else {
         Port var2;
         label44: {
            var2 = (Port)var1;
            if (this.contextPath != null) {
               if (this.contextPath.equals(var2.contextPath)) {
                  break label44;
               }
            } else if (var2.contextPath == null) {
               break label44;
            }

            return false;
         }

         if (this.portName != null) {
            if (!this.portName.equals(var2.portName)) {
               return false;
            }
         } else if (var2.portName != null) {
            return false;
         }

         if (this.serviceUri != null) {
            if (!this.serviceUri.equals(var2.serviceUri)) {
               return false;
            }
         } else if (var2.serviceUri != null) {
            return false;
         }

         return true;
      }
   }

   public int hashCode() {
      int var1 = this.contextPath != null ? this.contextPath.hashCode() : 0;
      var1 = 29 * var1 + (this.serviceUri != null ? this.serviceUri.hashCode() : 0);
      var1 = 29 * var1 + (this.portName != null ? this.portName.hashCode() : 0);
      return var1;
   }

   public String toString() {
      return "{" + this.getPortName() + "}" + this.getURI();
   }

   public void setBuildContext(JwsBuildContext var1) {
      this.ctx = var1;
   }
}
