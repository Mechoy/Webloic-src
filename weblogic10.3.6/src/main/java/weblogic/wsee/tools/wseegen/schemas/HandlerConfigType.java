package weblogic.wsee.tools.wseegen.schemas;

import com.bea.xml.SchemaType;
import com.bea.xml.XmlBeans;
import com.bea.xml.XmlException;
import com.bea.xml.XmlObject;
import com.bea.xml.XmlOptions;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.stream.XMLStreamReader;
import org.w3c.dom.Node;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLStreamException;

public interface HandlerConfigType extends XmlObject {
   SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(HandlerConfigType.class.getClassLoader(), "schemacom_bea_xml.system.s19C868EA707F69BC3737E9D413CDF781").resolveHandle("handlerconfigtypefd63type");

   HandlerChainType[] getHandlerChainArray();

   HandlerChainType getHandlerChainArray(int var1);

   int sizeOfHandlerChainArray();

   void setHandlerChainArray(HandlerChainType[] var1);

   void setHandlerChainArray(int var1, HandlerChainType var2);

   HandlerChainType insertNewHandlerChain(int var1);

   HandlerChainType addNewHandlerChain();

   void removeHandlerChain(int var1);

   public static final class Factory {
      public static HandlerConfigType newInstance() {
         return (HandlerConfigType)XmlBeans.getContextTypeLoader().newInstance(HandlerConfigType.type, (XmlOptions)null);
      }

      public static HandlerConfigType newInstance(XmlOptions var0) {
         return (HandlerConfigType)XmlBeans.getContextTypeLoader().newInstance(HandlerConfigType.type, var0);
      }

      public static HandlerConfigType parse(String var0) throws XmlException {
         return (HandlerConfigType)XmlBeans.getContextTypeLoader().parse(var0, HandlerConfigType.type, (XmlOptions)null);
      }

      public static HandlerConfigType parse(String var0, XmlOptions var1) throws XmlException {
         return (HandlerConfigType)XmlBeans.getContextTypeLoader().parse(var0, HandlerConfigType.type, var1);
      }

      public static HandlerConfigType parse(File var0) throws XmlException, IOException {
         return (HandlerConfigType)XmlBeans.getContextTypeLoader().parse(var0, HandlerConfigType.type, (XmlOptions)null);
      }

      public static HandlerConfigType parse(File var0, XmlOptions var1) throws XmlException, IOException {
         return (HandlerConfigType)XmlBeans.getContextTypeLoader().parse(var0, HandlerConfigType.type, var1);
      }

      public static HandlerConfigType parse(URL var0) throws XmlException, IOException {
         return (HandlerConfigType)XmlBeans.getContextTypeLoader().parse(var0, HandlerConfigType.type, (XmlOptions)null);
      }

      public static HandlerConfigType parse(URL var0, XmlOptions var1) throws XmlException, IOException {
         return (HandlerConfigType)XmlBeans.getContextTypeLoader().parse(var0, HandlerConfigType.type, var1);
      }

      public static HandlerConfigType parse(InputStream var0) throws XmlException, IOException {
         return (HandlerConfigType)XmlBeans.getContextTypeLoader().parse(var0, HandlerConfigType.type, (XmlOptions)null);
      }

      public static HandlerConfigType parse(InputStream var0, XmlOptions var1) throws XmlException, IOException {
         return (HandlerConfigType)XmlBeans.getContextTypeLoader().parse(var0, HandlerConfigType.type, var1);
      }

      public static HandlerConfigType parse(Reader var0) throws XmlException, IOException {
         return (HandlerConfigType)XmlBeans.getContextTypeLoader().parse(var0, HandlerConfigType.type, (XmlOptions)null);
      }

      public static HandlerConfigType parse(Reader var0, XmlOptions var1) throws XmlException, IOException {
         return (HandlerConfigType)XmlBeans.getContextTypeLoader().parse(var0, HandlerConfigType.type, var1);
      }

      public static HandlerConfigType parse(XMLStreamReader var0) throws XmlException {
         return (HandlerConfigType)XmlBeans.getContextTypeLoader().parse(var0, HandlerConfigType.type, (XmlOptions)null);
      }

      public static HandlerConfigType parse(XMLStreamReader var0, XmlOptions var1) throws XmlException {
         return (HandlerConfigType)XmlBeans.getContextTypeLoader().parse(var0, HandlerConfigType.type, var1);
      }

      public static HandlerConfigType parse(Node var0) throws XmlException {
         return (HandlerConfigType)XmlBeans.getContextTypeLoader().parse(var0, HandlerConfigType.type, (XmlOptions)null);
      }

      public static HandlerConfigType parse(Node var0, XmlOptions var1) throws XmlException {
         return (HandlerConfigType)XmlBeans.getContextTypeLoader().parse(var0, HandlerConfigType.type, var1);
      }

      /** @deprecated */
      public static HandlerConfigType parse(XMLInputStream var0) throws XmlException, XMLStreamException {
         return (HandlerConfigType)XmlBeans.getContextTypeLoader().parse(var0, HandlerConfigType.type, (XmlOptions)null);
      }

      /** @deprecated */
      public static HandlerConfigType parse(XMLInputStream var0, XmlOptions var1) throws XmlException, XMLStreamException {
         return (HandlerConfigType)XmlBeans.getContextTypeLoader().parse(var0, HandlerConfigType.type, var1);
      }

      /** @deprecated */
      public static XMLInputStream newValidatingXMLInputStream(XMLInputStream var0) throws XmlException, XMLStreamException {
         return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(var0, HandlerConfigType.type, (XmlOptions)null);
      }

      /** @deprecated */
      public static XMLInputStream newValidatingXMLInputStream(XMLInputStream var0, XmlOptions var1) throws XmlException, XMLStreamException {
         return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(var0, HandlerConfigType.type, var1);
      }

      private Factory() {
      }
   }
}
