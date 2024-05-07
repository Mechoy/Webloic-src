package weblogic.auddi.uddi.request.publish;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.FatalErrorException;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;
import weblogic.auddi.uddi.datastructure.AuthInfo;
import weblogic.auddi.uddi.datastructure.AuthInfoHandler;
import weblogic.auddi.uddi.datastructure.CompletionStatus;
import weblogic.auddi.uddi.datastructure.CompletionStatusHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandlerMaker;

public class GetAssertionStatusReportRequestHandler extends UDDIXMLHandler {
   public Object create(Node var1) throws UDDIException {
      GetAssertionStatusReportRequest var2 = new GetAssertionStatusReportRequest();
      var2.setAPI(false);
      UDDIXMLHandlerMaker var3 = UDDIXMLHandlerMaker.getInstance();
      Element var4 = (Element)var1;
      NodeList var5 = var1.getChildNodes();
      if (var5 != null) {
         for(int var6 = 0; var6 < var5.getLength(); ++var6) {
            if (var5.item(var6).getNodeName().equals("authInfo")) {
               AuthInfoHandler var7 = (AuthInfoHandler)var3.makeHandler("authInfo");
               AuthInfo var8 = (AuthInfo)var7.create(var5.item(var6));
               var2.setAuthInfo(var8);
            }

            if (var5.item(var6).getNodeName().equals("completionStatus")) {
               CompletionStatusHandler var9 = (CompletionStatusHandler)var3.makeHandler("completionStatus");
               CompletionStatus var10 = (CompletionStatus)var9.create(var5.item(var6));
               var2.setCompletionStatus(var10);
            }
         }

         if (var2.getAuthInfo() == null) {
            throw new FatalErrorException(UDDIMessages.get("error.fatalError.missingElement", "authInfo"));
         }
      }

      return var2;
   }
}
