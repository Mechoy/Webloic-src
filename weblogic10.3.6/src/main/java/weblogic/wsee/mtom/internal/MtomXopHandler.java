package weblogic.wsee.mtom.internal;

import com.bea.xml.XmlException;
import java.util.Iterator;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import org.w3c.dom.Node;
import weblogic.wsee.util.MtomUtil;
import weblogic.xml.dom.DOMUtils;
import weblogic.xml.saaj.SOAPMessageImpl;

public abstract class MtomXopHandler extends GenericHandler {
   public static final String VERBOSE_PROPERTY = "weblogic.wsee.mtom.internal.MtomXopHandler";
   public static final boolean VERBOSE = Boolean.getBoolean("weblogic.wsee.mtom.internal.MtomXopHandler");
   private static final QName ENCRYPTED_DATA_QNAME = new QName("http://www.w3.org/2001/04/xmlenc#", "EncryptedData");

   public QName[] getHeaders() {
      return null;
   }

   protected void processXOP(SOAPMessageContext var1) throws Exception {
      SOAPMessage var2 = var1.getMessage();
      Set var3 = (Set)var1.getProperty("weblogic.wsee.xop.normal.set");
      Object var4 = var1.getProperty("weblogic.wsee.mtom.threshold");
      int var5 = var4 == null ? 0 : (Integer)var4;
      if (var3 != null) {
         Iterator var6 = var3.iterator();

         while(var6.hasNext()) {
            Node var7 = (Node)var6.next();
            if (var7.getTextContent() != null && MtomUtil.calculateRawBytesLength(var7.getTextContent().length()) > var5 && MtomUtil.isValidNode(var2.getSOAPPart().getEnvelope(), var7)) {
               MtomUtil.replaceContentWithIncludeElement(var2, (SOAPElement)var7);
            }
         }

         var3.clear();
         var1.setProperty("weblogic.wsee.xop.normal.set", (Object)null);
      }

      if ("encrypt".equals(var1.getProperty("weblogic.wsee.xop.normal")) && var2.getSOAPPart() != null) {
         SOAPEnvelope var11 = var2.getSOAPPart().getEnvelope();
         if (var11 != null) {
            SOAPBody var12 = var11.getBody();
            if (var12 != null) {
               Iterator var8 = var12.getChildElements();

               while(var8.hasNext()) {
                  Node var9 = (Node)var8.next();
                  if (var9.getNodeType() == 3) {
                     break;
                  }

                  this.process(var2, (SOAPElement)var9, var5);
               }
            }

            SOAPHeader var13 = var11.getHeader();
            if (var13 != null) {
               Iterator var14 = var13.getChildElements();

               while(var14.hasNext()) {
                  Node var10 = (Node)var14.next();
                  if (var10.getNodeType() == 3) {
                     break;
                  }

                  this.process(var2, (SOAPElement)var10, var5);
               }
            }
         }
      }

   }

   private void process(SOAPMessage var1, SOAPElement var2, int var3) throws XmlException {
      if (DOMUtils.equalsQName(var2, ENCRYPTED_DATA_QNAME)) {
         SOAPElement var4 = MtomUtil.getCipherValueFromEncryptedData(var2);
         if (var4 == null || var4.getTextContent() == null || MtomUtil.calculateRawBytesLength(var4.getTextContent().length()) < var3) {
            return;
         }

         MtomUtil.replaceContentWithIncludeElement(var1, var4);
      } else {
         Iterator var6 = var2.getChildElements();

         while(var6.hasNext()) {
            Node var5 = (Node)var6.next();
            if (var5.getNodeType() == 3) {
               break;
            }

            this.process(var1, (SOAPElement)var5, var3);
         }
      }

   }

   protected boolean isMtomEnable(SOAPMessageContext var1) {
      boolean var2 = false;
      SOAPMessage var3 = var1.getMessage();
      if (var1.getProperty("weblogic.wsee.mtom.enable") != null) {
         var2 = true;
      } else if (var3 instanceof SOAPMessageImpl) {
         var2 = ((SOAPMessageImpl)var3).getIsMTOMmessage();
      }

      return var2;
   }
}
