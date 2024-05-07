package weblogic.auddi.uddi.datastructure;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import weblogic.auddi.uddi.FatalErrorException;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;
import weblogic.auddi.uddi.UnsupportedException;
import weblogic.auddi.util.Util;

public class AccessPoint extends UDDIElement implements Serializable {
   public static final String ACCESS_POINT_MAILTO = "mailto";
   public static final String ACCESS_POINT_HTTP = "http";
   public static final String ACCESS_POINT_HTTPS = "https";
   public static final String ACCESS_POINT_FTP = "ftp";
   public static final String ACCESS_POINT_FAX = "fax";
   public static final String ACCESS_POINT_PHONE = "phone";
   public static final String ACCESS_POINT_OTHER = "other";
   private String m_urlType = null;
   private String m_url = null;

   public AccessPoint(String var1, String var2) throws UDDIException {
      if (!var1.equals("mailto") && !var1.equals("http") && !var1.equals("https") && !var1.equals("ftp") && !var1.equals("fax") && !var1.equals("phone") && !var1.equals("other")) {
         throw new UnsupportedException(UDDIMessages.get("error.unsupported.type", "URLType", var1));
      } else {
         this.m_urlType = var1;
         this.m_url = this.truncateString(var2, 255);

         try {
            if (!var1.equals("ftp") && !var1.equals("http") && !var1.equals("https")) {
               if (var1.equals("mailto")) {
                  int var3 = this.m_url.indexOf("@");
                  if (var3 == -1) {
                     throw new MalformedURLException();
                  }
               }
            } else {
               new URL(this.m_url);
            }

         } catch (MalformedURLException var4) {
            throw new FatalErrorException(UDDIMessages.get("error.fatalError.malformedURL", this.m_url), var4);
         }
      }
   }

   public AccessPoint(AccessPoint var1) {
      if (var1 == null) {
         throw new IllegalArgumentException(UDDIMessages.get("error.runtime.constructor.null"));
      } else {
         this.m_urlType = var1.m_urlType;
         this.m_url = var1.m_url;
      }
   }

   public String getURL() {
      return this.m_url;
   }

   public String getUrlType() {
      return this.m_urlType;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof AccessPoint)) {
         return false;
      } else {
         AccessPoint var2 = (AccessPoint)var1;
         return Util.isEqual((Object)this.m_url, (Object)var2.m_url) && Util.isEqual((Object)this.m_urlType, (Object)var2.m_urlType);
      }
   }

   public int hashCode() {
      return Util.hashCode((Object)this.m_url) ^ Util.hashCode((Object)this.m_urlType);
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<").append("accessPoint");
      if (this.m_urlType != null) {
         var1.append(" URLType=\"").append(this.m_urlType).append("\"");
      }

      var1.append(">");
      if (this.m_url != null) {
         var1.append(this.fixStringForXML(this.m_url));
      }

      var1.append("</").append("accessPoint").append(">");
      return var1.toString();
   }
}
