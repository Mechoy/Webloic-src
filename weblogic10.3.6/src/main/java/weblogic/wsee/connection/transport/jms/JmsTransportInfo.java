package weblogic.wsee.connection.transport.jms;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import javax.xml.rpc.JAXRPCException;
import weblogic.wsee.connection.transport.TransportInfo;
import weblogic.wsee.util.JmsUtil;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.util.Verbose;

public class JmsTransportInfo implements TransportInfo, Serializable {
   private static final long serialVersionUID = -7303084944915898634L;
   private static final boolean verbose = Verbose.isVerbose(JmsTransportInfo.class);
   private String host;
   private int port;
   private String jndiURL;
   private String factory;
   private String queue;
   private String serviceURI;
   private boolean transactional = false;
   private String username;
   private String password;
   private boolean defaultFactory = false;
   private boolean sendTo81 = false;

   public JmsTransportInfo(String var1) throws URISyntaxException {
      URI var2 = new URI(var1);
      this.host = var2.getHost();
      this.port = var2.getPort();
      String var3 = var2.getQuery();
      this.jndiURL = null;
      if (verbose) {
         Verbose.log((Object)("Query :" + var3));
      }

      if (var3 == null) {
         this.queue = "weblogic.wsee.DefaultQueue";
         this.factory = "weblogic.jms.ConnectionFactory";
         this.defaultFactory = true;
      } else {
         Map var4 = JmsUtil.asMap(var3);
         this.queue = (String)var4.get("URI");
         if (StringUtil.isEmpty(this.queue)) {
            this.queue = "weblogic.wsee.DefaultQueue";
         } else if (this.checkWlw81Uri(var2)) {
            this.sendTo81 = true;
            return;
         }

         this.factory = (String)var4.get("FACTORY");
         if (verbose) {
            Verbose.log((Object)("queue :" + this.queue + " factory = " + this.factory));
         }

         if (StringUtil.isEmpty(this.factory)) {
            this.factory = "weblogic.jms.ConnectionFactory";
            this.defaultFactory = true;
         }
      }

      this.serviceURI = var2.getPath();
   }

   private boolean checkWlw81Uri(URI var1) {
      if (this.isWlw81Uri(this.queue)) {
         this.serviceURI = this.queue;
         String var2 = var1.getPath();
         String[] var3 = var2.split("/");
         if (var3.length != 3) {
            throw new JAXRPCException("Incorrect format for path");
         } else {
            this.factory = var3[1];
            this.queue = var3[2];
            return true;
         }
      } else {
         return false;
      }
   }

   private boolean isWlw81Uri(String var1) {
      return var1.startsWith("/");
   }

   public boolean isSendTo81() {
      return this.sendTo81;
   }

   public String getServiceUri() {
      return this.serviceURI;
   }

   public String getJndiURL() {
      return this.jndiURL;
   }

   public void setJndiURL(String var1) {
      this.jndiURL = var1;
   }

   public String getUsername() {
      return this.username;
   }

   public void setUsername(String var1) {
      this.username = var1;
   }

   public String getPassword() {
      return this.password;
   }

   public void setPassword(String var1) {
      this.password = var1;
   }

   public String getHost() {
      return this.host;
   }

   public void setHost(String var1) {
      this.host = var1;
   }

   public int getPort() {
      return this.port;
   }

   public void setPort(int var1) {
      this.port = var1;
   }

   public String getFactory() {
      return this.factory;
   }

   public void setFactory(String var1) {
      this.factory = var1;
   }

   public String getQueue() {
      return this.queue;
   }

   public void setQueue(String var1) {
      this.queue = var1;
   }

   public boolean isTransactional() {
      return this.transactional;
   }

   public void setTransactional(boolean var1) {
      this.transactional = var1;
      if (var1 && this.defaultFactory) {
         this.factory = "weblogic.jms.XAConnectionFactory";
      }

   }

   public boolean isDefaultFactory() {
      return this.defaultFactory;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof JmsTransportInfo)) {
         return false;
      } else {
         JmsTransportInfo var2 = (JmsTransportInfo)var1;
         if (this.host == null && var2.getHost() != null || this.host != null && !this.host.equals(var2.getHost())) {
            return false;
         } else if (this.port != var2.getPort()) {
            return false;
         } else if ((this.jndiURL != null || var2.getJndiURL() == null) && (this.jndiURL == null || this.jndiURL.equals(var2.getJndiURL()))) {
            if (this.factory == null && var2.getFactory() != null || this.factory != null && !this.factory.equals(var2.getFactory())) {
               return false;
            } else if (this.queue == null && var2.getQueue() != null || this.queue != null && !this.queue.equals(var2.getQueue())) {
               return false;
            } else if (this.serviceURI == null && var2.getServiceUri() != null || this.serviceURI != null && !this.serviceURI.equals(var2.getServiceUri())) {
               return false;
            } else if (this.username == null && var2.getUsername() != null || this.username != null && !this.username.equals(var2.getUsername())) {
               return false;
            } else if (this.password == null && var2.getPassword() != null || this.password != null && !this.password.equals(var2.getPassword())) {
               return false;
            } else if (this.transactional != var2.isTransactional()) {
               return false;
            } else {
               return this.defaultFactory == var2.isDefaultFactory();
            }
         } else {
            return false;
         }
      }
   }

   public int hashCode() {
      int var1 = 0;
      if (this.host != null) {
         var1 = this.host.hashCode();
      }

      var1 = 29 * var1 + this.port;
      if (this.jndiURL != null) {
         var1 = 29 * var1 + this.jndiURL.hashCode();
      }

      if (this.factory != null) {
         var1 = 29 * var1 + this.factory.hashCode();
      }

      if (this.queue != null) {
         var1 = 29 * var1 + this.queue.hashCode();
      }

      if (this.serviceURI != null) {
         var1 = 29 * var1 + this.serviceURI.hashCode();
      }

      if (this.username != null) {
         var1 = 29 * var1 + this.username.hashCode();
      }

      if (this.password != null) {
         var1 = 29 * var1 + this.password.hashCode();
      }

      if (this.transactional) {
         var1 = 29 * var1 + 1;
      }

      if (this.defaultFactory) {
         var1 = 29 * var1 + 1;
      }

      return var1;
   }
}
