package weblogic.uddi.client.service;

public abstract class UDDIService {
   public static final String UDDI_APPNAME = "uddi";
   public static final String UDDI_EXPLORER_APPNAME = "uddiexplorer";
   protected String URL;

   public String getURL() {
      return this.URL;
   }

   public void setURL(String var1) {
      this.URL = new String(var1);
   }
}
