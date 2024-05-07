package weblogic.wsee.deploy;

import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import weblogic.j2ee.descriptor.WebAppBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebAppBean;
import weblogic.servlet.internal.WebAppParser;

public class WseeWebappParser implements WebAppParser {
   private WebAppBean webAppBean;
   private WeblogicWebAppBean wlwebAppBean;

   public WseeWebappParser(WebAppBean var1, WeblogicWebAppBean var2) {
      this.webAppBean = var1;
      this.wlwebAppBean = var2;
   }

   public WebAppBean getWebAppBean() throws IOException, XMLStreamException {
      return this.webAppBean;
   }

   public WeblogicWebAppBean getWeblogicWebAppBean() throws IOException, XMLStreamException {
      return this.wlwebAppBean;
   }
}
