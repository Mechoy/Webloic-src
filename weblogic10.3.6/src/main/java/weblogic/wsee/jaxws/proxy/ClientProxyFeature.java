package weblogic.wsee.jaxws.proxy;

import java.io.Serializable;
import java.net.Proxy;
import java.net.Proxy.Type;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;

public class ClientProxyFeature extends WebServiceFeature implements Serializable {
   private static final long serialVersionUID = 8704678112454179010L;
   public static final String ID = "http://jax-ws.dev.java.net/features/clientproxy";
   private transient boolean usedInPort = true;
   private Proxy.Type type;
   private String proxyHost;
   private int proxyPort;
   private String proxyUserName;
   private String proxyPassword;
   private boolean ignoreSystemNonProxyHosts;

   public ClientProxyFeature() {
      this.type = Type.HTTP;
      this.ignoreSystemNonProxyHosts = false;
   }

   public String getProxyHost() {
      return this.proxyHost;
   }

   public void setProxyHost(String var1) {
      this.proxyHost = var1;
   }

   public String getProxyPassword() {
      return this.proxyPassword;
   }

   public void setProxyPassword(String var1) {
      this.proxyPassword = var1;
   }

   public int getProxyPort() {
      return this.proxyPort;
   }

   public void setProxyPort(int var1) {
      this.proxyPort = var1;
   }

   public String getProxyUserName() {
      return this.proxyUserName;
   }

   public void setProxyUserName(String var1) {
      this.proxyUserName = var1;
   }

   public Proxy.Type getType() {
      return this.type;
   }

   public void setType(Proxy.Type var1) {
      this.type = var1;
   }

   public String getID() {
      return "http://jax-ws.dev.java.net/features/clientproxy";
   }

   public boolean isUsedInPort() {
      return this.usedInPort;
   }

   public void setUsedInPort(boolean var1) {
      this.usedInPort = var1;
   }

   public void attachsPort(Object var1) {
      if (var1 instanceof BindingProvider) {
         ((BindingProvider)var1).getRequestContext().put("weblogic.wsee.jaxws.proxy.PersistentProxyInfo", this);
      } else {
         throw new WebServiceException("port [" + var1 + "] can not classcast to WSBindingProvider");
      }
   }

   public void setIgnoreSystemNonProxyHosts(boolean var1) {
      this.ignoreSystemNonProxyHosts = var1;
   }

   public boolean isIgnoreSystemNonProxyHosts() {
      return this.ignoreSystemNonProxyHosts;
   }
}
