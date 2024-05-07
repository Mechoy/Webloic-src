package weblogic.common;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.Hashtable;
import weblogic.rjvm.ServerURL;
import weblogic.security.acl.UserInfo;
import weblogic.utils.http.HttpParsing;

public final class T3Connection {
   private String protocol;
   private String host;
   private int port;
   private String path;
   private Hashtable queryParams;
   private UserInfo user;
   private String url;

   /** @deprecated */
   public String getHost() {
      return this.host;
   }

   /** @deprecated */
   public int getPort() {
      return this.port;
   }

   /** @deprecated */
   public String getProtocol() {
      return this.protocol;
   }

   /** @deprecated */
   public UserInfo getUser() {
      return this.user;
   }

   /** @deprecated */
   public String getURL() {
      return this.url;
   }

   /** @deprecated */
   public String getPath() {
      return this.path;
   }

   /** @deprecated */
   public Hashtable getQueryParams() {
      return this.queryParams;
   }

   /** @deprecated */
   public String getQueryParam(String var1) {
      return this.getQueryParams() == null ? null : (String)this.getQueryParams().get(var1);
   }

   /** @deprecated */
   public T3Connection(String var1) throws UnknownHostException, MalformedURLException {
      this(var1, T3User.GUEST);
   }

   /** @deprecated */
   public T3Connection(String var1, UserInfo var2) throws UnknownHostException, MalformedURLException {
      this.url = var1;
      this.user = var2;
      if (this.user == null) {
         this.user = T3User.GUEST;
      }

      ServerURL var3 = new ServerURL(var1);
      this.protocol = var3.getProtocol();
      this.host = var3.getHost();
      this.port = var3.getPort();
      this.path = var3.getFile();
      if (var3.getQuery().length() != 0) {
         this.queryParams = new Hashtable();
         HttpParsing.parseQueryString(var3.getQuery().replace('?', '&'), this.queryParams);
      }
   }

   /** @deprecated */
   public boolean isConnected() {
      return true;
   }

   /** @deprecated */
   public T3Connection connect() throws IOException, T3Exception {
      return this;
   }

   /** @deprecated */
   public void disconnect() throws IOException, T3Exception {
   }

   /** @deprecated */
   public String toString() {
      return "[T3Connection host=" + this.host + ", port=" + this.port + ", user=" + this.user + ", hashCode=" + this.hashCode() + "]";
   }
}
