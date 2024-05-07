package weblogic.wsee.jws;

import java.net.MalformedURLException;
import java.net.URL;

public class ServiceHandleImpl implements ServiceHandle {
   static final long serialVersionUID = 2535609712500935143L;
   private final String _addr;
   private final String _uri;
   private final String _conversationID;
   private final String _contextPath;
   private String jndiName = null;
   private URL url = null;

   public ServiceHandleImpl(String var1, String var2, String var3, String var4) {
      this._addr = var1;
      this._contextPath = var2;
      this._uri = var3;
      this._conversationID = var4;
   }

   public int getScheme() {
      return getScheme(this._addr.substring(0, this._addr.indexOf(58)));
   }

   public static int getScheme(String var0) {
      if (var0.equals("http")) {
         return 1;
      } else if (var0.equals("jms")) {
         return 2;
      } else if (var0.equals("smtp")) {
         return 3;
      } else if (var0.equals("ftp")) {
         return 4;
      } else {
         return var0.equals("file") ? 5 : 0;
      }
   }

   public String getJNDIBaseName() {
      if (this.jndiName == null) {
         this.jndiName = getJNDIBaseName(this.getURI());
      }

      return this.jndiName;
   }

   public String getURI() {
      return this._uri;
   }

   public String getContextURI() {
      return this._contextPath;
   }

   public URL getURL(int var1) {
      try {
         return new URL(scheme(var1) + this._addr.substring(this._addr.indexOf(58)));
      } catch (MalformedURLException var3) {
         var3.printStackTrace();
         return null;
      }
   }

   private static String scheme(int var0) {
      switch (var0) {
         case 0:
         case 1:
            return "http";
         case 2:
            return "jms";
         case 3:
            return "smtp";
         case 4:
         default:
            return null;
         case 5:
            return "file";
      }
   }

   public URL getURL() {
      if (this.url == null) {
         try {
            this.url = new URL(this._addr);
         } catch (MalformedURLException var2) {
         }
      }

      return this.url;
   }

   public String getConversationID() {
      return this._conversationID;
   }

   public String getControlID() {
      return null;
   }

   private static String getJNDIBaseName(String var0) {
      StringBuffer var1 = new StringBuffer(var0);
      int var2 = var1.length();

      int var3;
      for(var3 = 0; var3 < var2; ++var3) {
         char var4 = var1.charAt(var3);
         if (var4 == '/' || var4 == '\\') {
            var1.setCharAt(var3, '.');
         }
      }

      var3 = var0.lastIndexOf(46);
      if (var3 > 0) {
         return var1.substring(1, var3);
      } else if (var1.length() > 0) {
         return var1.substring(1);
      } else {
         return var1.toString();
      }
   }
}
