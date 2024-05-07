package weblogic.auddi.uddi.datastructure;

import java.io.Serializable;
import java.net.URL;
import weblogic.auddi.uddi.FatalErrorException;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;
import weblogic.auddi.uddi.UnsupportedException;
import weblogic.auddi.util.Util;

public class DiscoveryURL extends UDDIListObject implements Serializable {
   public static final String BUSINESS_ENTITY = "businessEntity";
   public static final String BUSINESS_ENTITY_EXT = "businessEntityExt";
   private static final long serialVersionUID = 3117297632480683256L;
   private String m_useType = null;
   private String m_url = null;

   public DiscoveryURL() {
   }

   public DiscoveryURL(DiscoveryURL var1) throws UDDIException {
      if (var1 == null) {
         throw new IllegalArgumentException("parameter to copy constructor was null");
      } else {
         this.m_url = var1.m_url;
         this.m_useType = var1.m_useType;
      }
   }

   public DiscoveryURL(String var1, String var2) throws UDDIException {
      this.setUseType(var1);
      this.setUrl(var2);
   }

   public void setUseType(String var1) throws UDDIException {
      if (var1 == null) {
         throw new UnsupportedException(UDDIMessages.get("error.unsupported.type", "useType", var1));
      } else {
         this.m_useType = this.truncateString(var1, 255);
      }
   }

   public void setUrl(URL var1) throws UDDIException {
      if (var1 == null) {
         throw new FatalErrorException(UDDIMessages.get("error.fatalError.missingElement", "discoveryURL"));
      } else {
         this.setUrl(var1.toString());
      }
   }

   public void setUrl(String var1) throws UDDIException {
      if (var1 == null) {
         throw new FatalErrorException(UDDIMessages.get("error.fatalError.missingElement", "discoveryURL"));
      } else {
         this.m_url = this.truncateString(var1, 255);
      }
   }

   public String getUrl() {
      return this.m_url == null ? "" : this.m_url.toString();
   }

   public String getUseType() {
      return this.m_useType;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof DiscoveryURL)) {
         return false;
      } else {
         DiscoveryURL var2 = (DiscoveryURL)var1;
         boolean var3 = true;
         var3 &= Util.isEqual((Object)this.m_url, (Object)var2.m_url);
         var3 &= Util.isEqual((Object)this.m_useType, (Object)var2.m_useType);
         return var3;
      }
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<discoveryURL");
      if (this.m_useType != null) {
         var1.append(" useType=\"" + this.m_useType + "\">");
      }

      var1.append(this.getUrl());
      var1.append("</discoveryURL>");
      return var1.toString();
   }
}
