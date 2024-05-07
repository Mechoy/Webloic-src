package weblogic.wsee.wstx.wsat.tube;

import com.sun.xml.ws.api.SOAPVersion;
import com.sun.xml.ws.api.message.Header;
import com.sun.xml.ws.api.message.HeaderList;
import com.sun.xml.ws.api.message.Message;
import com.sun.xml.ws.api.message.Messages;
import com.sun.xml.ws.message.DOMHeader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.WebServiceException;
import weblogic.wsee.wstx.wsat.WSATConstants;

public class MUHeaderHelper {
   private static final String MU_FAULT_DETAIL_LOCALPART = "NotUnderstood";
   private static final QName MU_HEADER_DETAIL;
   private static final String MUST_UNDERSTAND_FAULT_MESSAGE_STRING = "One or more mandatory SOAP header blocks not understood";

   public static Set<QName> getMUWSATHeaders(HeaderList var0) {
      HashSet var1 = null;

      for(int var2 = 0; var2 < var0.size(); ++var2) {
         if (!var0.isUnderstood(var2)) {
            Header var3 = var0.get(var2);
            QName var4 = new QName(var3.getNamespaceURI(), var3.getLocalPart());
            if (WSATConstants.WSCOOR_CONTEXT_QNAME.equals(var4) || WSATConstants.WSCOOR11_CONTEXT_QNAME.equals(var4)) {
               if (var1 == null) {
                  var1 = new HashSet();
               }

               var1.add(var4);
            }
         }
      }

      return var1;
   }

   static Message createMUSOAPFaultMessage(SOAPVersion var0, Set<QName> var1) {
      try {
         SOAPFault var2 = createMUSOAPFault(var0);
         if (var0 == SOAPVersion.SOAP_11) {
            setMUFaultString(var2, var1);
         }

         Message var3 = Messages.create(var2);
         if (var0 == SOAPVersion.SOAP_12) {
            addHeader(var3, var1);
         }

         return var3;
      } catch (SOAPException var4) {
         throw new WebServiceException(var4);
      }
   }

   private static void setMUFaultString(SOAPFault var0, Set<QName> var1) throws SOAPException {
      var0.setFaultString("MustUnderstand headers:" + var1 + " are not understood");
   }

   private static void addHeader(Message var0, Set<QName> var1) throws SOAPException {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         QName var3 = (QName)var2.next();
         SOAPElement var4 = SOAPVersion.SOAP_12.getSOAPFactory().createElement(MU_HEADER_DETAIL);
         var4.addNamespaceDeclaration("abc", var3.getNamespaceURI());
         var4.setAttribute("qname", "abc:" + var3.getLocalPart());
         DOMHeader var5 = new DOMHeader(var4);
         var0.getHeaders().add(var5);
      }

   }

   private static SOAPFault createMUSOAPFault(SOAPVersion var0) throws SOAPException {
      SOAPFault var1 = var0.getSOAPFactory().createFault();
      var1.setFaultCode(var0.faultCodeMustUnderstand);
      var1.setFaultString("One or more mandatory SOAP header blocks not understood");
      return var1;
   }

   static {
      MU_HEADER_DETAIL = new QName(SOAPVersion.SOAP_12.nsUri, "NotUnderstood");
   }
}
