package weblogic.uddi.client.service;

import java.io.IOException;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.uddi.client.structures.exception.XML_SoapException;
import weblogic.webservice.binding.Binding;
import weblogic.webservice.binding.BindingFactory;
import weblogic.webservice.binding.BindingInfo;
import weblogic.webservice.core.DefaultMessageContext;
import weblogic.webservice.core.soap.SOAPTextElement;

public class UDDISoapMessage {
   private Element result = null;

   public Document createDOMDoc() throws XML_SoapException {
      try {
         DocumentBuilder var1 = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         return var1.newDocument();
      } catch (ParserConfigurationException var2) {
         throw new XML_SoapException(var2.getMessage());
      }
   }

   public void sendMessage(Element var1, String var2) throws XML_SoapException {
      System.setProperty("javax.xml.soap.MessageFactory", "weblogic.webservice.core.soap.MessageFactoryImpl");

      try {
         DefaultMessageContext var3 = new DefaultMessageContext();
         SOAPMessage var4 = var3.getMessage();
         MimeHeaders var5 = var4.getMimeHeaders();
         var5.addHeader("soapAction", "\"\"");
         SOAPPart var6 = var4.getSOAPPart();
         SOAPEnvelope var7 = var6.getEnvelope();
         SOAPBody var8 = var7.getBody();
         SOAPElement var9 = var8.addChildElement(var1.getTagName());
         this.populateSOAPElement(var7, var9, var1);
         BindingFactory var10 = BindingFactory.getInstance();
         BindingInfo var11 = new BindingInfo();
         var11.setAddress(var2);
         Binding var12 = var10.create(var11);
         var12.send(var3);
         var12.receive(var3);
         var4 = var3.getMessage();
         var6 = var4.getSOAPPart();
         var7 = var6.getEnvelope();
         var8 = var7.getBody();
         Iterator var13 = var8.getChildElements();

         Object var14;
         do {
            if (!var13.hasNext()) {
               return;
            }

            var14 = var13.next();
         } while(!(var14 instanceof SOAPElement));

         SOAPElement var15 = (SOAPElement)var14;
         Document var16 = this.createDOMDoc();
         this.result = this.populateDOMElement(var16, var15);
      } catch (SOAPException var17) {
         throw new XML_SoapException(var17.getMessage());
      } catch (IOException var18) {
         throw new XML_SoapException(var18.getMessage());
      }
   }

   public Element populateDOMElement(Document var1, SOAPElement var2) throws XML_SoapException {
      Element var3 = var1.createElement(var2.getElementName().getLocalName());
      Iterator var4 = var2.getAllAttributes();

      while(var4.hasNext()) {
         Name var5 = (Name)var4.next();
         String var6 = var2.getAttributeValue(var5);
         var3.setAttribute(var5.getLocalName(), var6);
      }

      Iterator var9 = var2.getChildElements();

      while(var9.hasNext()) {
         Object var10 = var9.next();
         if (var10 instanceof SOAPElement) {
            SOAPElement var7 = (SOAPElement)var10;
            Element var8 = this.populateDOMElement(var1, var7);
            var3.appendChild(var8);
         }

         if (var10 instanceof SOAPTextElement) {
            SOAPTextElement var11 = (SOAPTextElement)var10;
            var3.appendChild(var1.createTextNode(var11.getText()));
         }
      }

      return var3;
   }

   public SOAPElement populateSOAPElement(SOAPEnvelope var1, SOAPElement var2, Element var3) throws SOAPException {
      NamedNodeMap var6 = var3.getAttributes();

      int var5;
      for(var5 = 0; var5 < var6.getLength(); ++var5) {
         Node var7 = var6.item(var5);
         var2.addAttribute(var1.createName(var7.getNodeName()), var3.getAttribute(var7.getNodeName()));
      }

      NodeList var8 = var3.getChildNodes();

      for(var5 = 0; var5 < var8.getLength(); ++var5) {
         short var10 = var8.item(var5).getNodeType();
         String var11 = "";
         if (var8.item(var5).getNodeType() == 3) {
            var2.addTextNode(var8.item(var5).getNodeValue());
         }

         if (var8.item(var5).getNodeType() == 1) {
            SOAPElement var9 = var2.addChildElement(((Element)var8.item(var5)).getTagName());
            this.populateSOAPElement(var1, var9, (Element)var8.item(var5));
         }
      }

      return var2;
   }

   public boolean isFault() {
      if (this.result != null) {
         String var1 = new String("Fault");
         String var2 = this.result.getTagName();
         int var3 = this.result.getTagName().indexOf(":");
         return var3 == -1 ? var1.equals(this.result.getTagName()) : var2.substring(var3 + 1, var2.length()).equals(var1);
      } else {
         return false;
      }
   }

   public Element getResult() {
      return this.result;
   }

   private void printXML(Element var1, int var2) {
      if (var1 != null) {
         if (var2 == 0) {
            System.out.println("");
            System.out.println("");
         }

         int var3;
         for(var3 = 0; var3 < var2; ++var3) {
            System.out.print("    ");
         }

         System.out.print("<" + var1.getTagName());
         NamedNodeMap var5 = var1.getAttributes();

         int var4;
         for(var4 = 0; var4 < var5.getLength(); ++var4) {
            System.out.println("");
            Node var6 = var5.item(var4);

            for(var3 = 0; var3 < var2; ++var3) {
               System.out.print("    ");
            }

            System.out.print("  " + var6.getNodeName() + "=\"" + var1.getAttribute(var6.getNodeName()) + "\"");
         }

         System.out.println(">");
         NodeList var7 = var1.getChildNodes();
         String var8 = "";

         for(var3 = 0; var3 < var7.getLength(); ++var3) {
            if (var7.item(var3).getNodeType() == 3) {
               var8 = var8 + var7.item(var3).getNodeValue();
            }
         }

         if (!var8.equals("")) {
            for(var3 = 0; var3 < var2; ++var3) {
               System.out.print("    ");
            }

            System.out.println("    " + var8.trim());
         }

         var7 = var1.getChildNodes();

         for(var4 = 0; var4 < var7.getLength(); ++var4) {
            if (var7.item(var4).getNodeType() == 1) {
               this.printXML((Element)var7.item(var4), var2 + 1);
            }
         }

         for(var3 = 0; var3 < var2; ++var3) {
            System.out.print("    ");
         }

         System.out.println("</" + var1.getTagName() + ">");
         if (var2 == 0) {
            System.out.println("");
            System.out.println("");
         }

      }
   }
}
