package weblogic.auddi.uddi.datastructure;

import java.net.MalformedURLException;
import java.net.URL;
import weblogic.auddi.uddi.FatalErrorException;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;
import weblogic.auddi.util.Util;

public class OverviewDoc extends UDDIElement {
   private Descriptions m_descriptions = null;
   private URL m_overviewURL = null;

   public OverviewDoc() {
   }

   public OverviewDoc(OverviewDoc var1) throws UDDIException {
      if (var1 == null) {
         throw new IllegalArgumentException(UDDIMessages.get("error.runtime.constructor.null"));
      } else {
         if (var1.m_descriptions != null) {
            this.m_descriptions = new Descriptions(var1.m_descriptions);
         }

         if (var1.m_overviewURL != null) {
            this.setOverviewURL(var1.m_overviewURL.toString());
         }

      }
   }

   public void addDescription(Description var1) throws UDDIException {
      if (var1 != null) {
         if (this.m_descriptions == null) {
            this.m_descriptions = new Descriptions();
         }

         this.m_descriptions.add(var1);
      }
   }

   public void setOverviewURL(URL var1) {
      this.m_overviewURL = var1;
   }

   public void setOverviewURL(String var1) throws FatalErrorException {
      try {
         var1 = this.truncateString(var1, 255);
         this.m_overviewURL = new URL(var1);
      } catch (MalformedURLException var3) {
         throw new FatalErrorException(UDDIMessages.get("error.fatalError.malformedURL", var1), var3);
      }
   }

   public void setDescriptions(Descriptions var1) {
      this.m_descriptions = var1;
   }

   public Descriptions getDescriptions() {
      return this.m_descriptions;
   }

   public URL getOverviewURL() {
      return this.m_overviewURL;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof OverviewDoc)) {
         return false;
      } else {
         OverviewDoc var2 = (OverviewDoc)var1;
         boolean var3 = true;
         var3 &= Util.isEqual((Object)this.m_descriptions, (Object)var2.m_descriptions);
         var3 &= Util.isEqual((Object)this.m_overviewURL, (Object)var2.m_overviewURL);
         return var3;
      }
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<overviewDoc>");
      if (this.m_descriptions != null) {
         var1.append(this.m_descriptions.toXML());
      }

      if (this.m_overviewURL != null) {
         var1.append("<overviewURL>").append(this.m_overviewURL.toString()).append("</overviewURL>");
      }

      var1.append("</overviewDoc>");
      return var1.toString();
   }
}
