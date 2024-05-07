package weblogic.wsee.util;

import com.bea.xbean.util.Base64;
import com.bea.xml.XmlException;
import java.io.ByteArrayInputStream;
import java.util.UUID;
import javax.xml.namespace.QName;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public abstract class MtomUtil {
   public static final String MIME_TRANSFER_ENC_HEADER = "Content-Transfer-Encoding";
   public static final String DEFAULT_MIME_TYPE = "application/octet-stream";
   public static final QName XOP_INCLUDE_QNAME = new QName("http://www.w3.org/2004/08/xop/include", "Include");

   public static String getMTOMcid(QName var0) {
      StringBuffer var1 = new StringBuffer();
      var1.append(var0.getLocalPart());
      var1.append("=");
      var1.append(UUID.randomUUID());
      var1.append("@");
      var1.append(var0.getNamespaceURI());
      return var1.toString();
   }

   public static void writeHref(SOAPMessage var0, SOAPElement var1, String var2) throws XmlException {
      try {
         Name var3 = var0.getSOAPPart().getEnvelope().createName("href");
         var1.addAttribute(var3, "cid:" + var2);
      } catch (SOAPException var4) {
         throw new XmlException("Failed to add href attribute", var4);
      }
   }

   public static boolean isValidNode(Node var0, Node var1) {
      assert var1 != null;

      assert var0 != null;

      if (var1 != null && var1 != var0) {
         for(Node var2 = var1.getParentNode(); var2 != null; var2 = var2.getParentNode()) {
            if (var2 == var0) {
               return true;
            }
         }
      }

      return false;
   }

   public static SOAPElement getCipherValueFromEncryptedData(SOAPElement var0) throws XmlException {
      assert var0 != null;

      SOAPElement var1 = null;

      try {
         Element var2 = DOMUtils.getElementByTagNameNS(var0, "http://www.w3.org/2001/04/xmlenc#", "CipherData");
         if (var2 != null) {
            var1 = (SOAPElement)DOMUtils.getElementByTagNameNS(var2, "http://www.w3.org/2001/04/xmlenc#", "CipherValue");
            return var1;
         } else {
            throw new XmlException("Failed to retrieve CiphereData element");
         }
      } catch (DOMProcessingException var3) {
         throw new XmlException("Failed to retrieve CipherValue element", var3);
      }
   }

   public static void addMtomAttachment(SOAPMessage var0, byte[] var1, String var2) throws XmlException {
      AttachmentPart var3 = var0.createAttachmentPart(var1, "application/octet-stream");
      String var4 = '<' + var2 + '>';
      var3.setContentId(var4);
      var3.addMimeHeader("Content-Transfer-Encoding", "binary");

      try {
         var3.setRawContent(new ByteArrayInputStream(var1), "application/octet-stream");
      } catch (SOAPException var6) {
         throw new XmlException(var6.getMessage());
      }

      var0.addAttachmentPart(var3);
   }

   public static void replaceContentWithIncludeElement(SOAPMessage var0, SOAPElement var1) throws XmlException {
      assert var0 != null;

      assert var1 != null;

      String var2 = var1.getTextContent();
      byte[] var3 = Base64.decode(var2.getBytes());
      replaceContentWithIncludeElement(var0, var3, var1);
   }

   public static void replaceContentWithIncludeElement(SOAPMessage var0, byte[] var1, SOAPElement var2) throws XmlException {
      SOAPElement var3 = null;

      try {
         var2.removeContents();
         var3 = var2.addChildElement(XOP_INCLUDE_QNAME);
      } catch (SOAPException var5) {
         throw new XmlException(var5.getMessage(), var5);
      }

      String var4 = getMTOMcid(var2.getElementQName());
      writeHref(var0, var3, var4);
      addMtomAttachment(var0, var1, var4);
   }

   public static int calculateRawBytesLength(int var0) {
      int var1 = 0;
      if (var0 >= 4) {
         var1 = var0 * 3 / 4 - 2;
      }

      return var1;
   }
}
