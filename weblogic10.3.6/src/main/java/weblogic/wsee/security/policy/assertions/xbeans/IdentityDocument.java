package weblogic.wsee.security.policy.assertions.xbeans;

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

public interface IdentityDocument extends XmlObject {
   SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(IdentityDocument.class.getClassLoader(), "schemacom_bea_xml.system.s7BDA940CB9F04DBD9B06BD7C8459FD58").resolveHandle("identityd909doctype");

   Identity getIdentity();

   void setIdentity(Identity var1);

   Identity addNewIdentity();

   public static final class Factory {
      public static IdentityDocument newInstance() {
         return (IdentityDocument)XmlBeans.getContextTypeLoader().newInstance(IdentityDocument.type, (XmlOptions)null);
      }

      public static IdentityDocument newInstance(XmlOptions var0) {
         return (IdentityDocument)XmlBeans.getContextTypeLoader().newInstance(IdentityDocument.type, var0);
      }

      public static IdentityDocument parse(String var0) throws XmlException {
         return (IdentityDocument)XmlBeans.getContextTypeLoader().parse(var0, IdentityDocument.type, (XmlOptions)null);
      }

      public static IdentityDocument parse(String var0, XmlOptions var1) throws XmlException {
         return (IdentityDocument)XmlBeans.getContextTypeLoader().parse(var0, IdentityDocument.type, var1);
      }

      public static IdentityDocument parse(File var0) throws XmlException, IOException {
         return (IdentityDocument)XmlBeans.getContextTypeLoader().parse(var0, IdentityDocument.type, (XmlOptions)null);
      }

      public static IdentityDocument parse(File var0, XmlOptions var1) throws XmlException, IOException {
         return (IdentityDocument)XmlBeans.getContextTypeLoader().parse(var0, IdentityDocument.type, var1);
      }

      public static IdentityDocument parse(URL var0) throws XmlException, IOException {
         return (IdentityDocument)XmlBeans.getContextTypeLoader().parse(var0, IdentityDocument.type, (XmlOptions)null);
      }

      public static IdentityDocument parse(URL var0, XmlOptions var1) throws XmlException, IOException {
         return (IdentityDocument)XmlBeans.getContextTypeLoader().parse(var0, IdentityDocument.type, var1);
      }

      public static IdentityDocument parse(InputStream var0) throws XmlException, IOException {
         return (IdentityDocument)XmlBeans.getContextTypeLoader().parse(var0, IdentityDocument.type, (XmlOptions)null);
      }

      public static IdentityDocument parse(InputStream var0, XmlOptions var1) throws XmlException, IOException {
         return (IdentityDocument)XmlBeans.getContextTypeLoader().parse(var0, IdentityDocument.type, var1);
      }

      public static IdentityDocument parse(Reader var0) throws XmlException, IOException {
         return (IdentityDocument)XmlBeans.getContextTypeLoader().parse(var0, IdentityDocument.type, (XmlOptions)null);
      }

      public static IdentityDocument parse(Reader var0, XmlOptions var1) throws XmlException, IOException {
         return (IdentityDocument)XmlBeans.getContextTypeLoader().parse(var0, IdentityDocument.type, var1);
      }

      public static IdentityDocument parse(XMLStreamReader var0) throws XmlException {
         return (IdentityDocument)XmlBeans.getContextTypeLoader().parse(var0, IdentityDocument.type, (XmlOptions)null);
      }

      public static IdentityDocument parse(XMLStreamReader var0, XmlOptions var1) throws XmlException {
         return (IdentityDocument)XmlBeans.getContextTypeLoader().parse(var0, IdentityDocument.type, var1);
      }

      public static IdentityDocument parse(Node var0) throws XmlException {
         return (IdentityDocument)XmlBeans.getContextTypeLoader().parse(var0, IdentityDocument.type, (XmlOptions)null);
      }

      public static IdentityDocument parse(Node var0, XmlOptions var1) throws XmlException {
         return (IdentityDocument)XmlBeans.getContextTypeLoader().parse(var0, IdentityDocument.type, var1);
      }

      /** @deprecated */
      public static IdentityDocument parse(XMLInputStream var0) throws XmlException, XMLStreamException {
         return (IdentityDocument)XmlBeans.getContextTypeLoader().parse(var0, IdentityDocument.type, (XmlOptions)null);
      }

      /** @deprecated */
      public static IdentityDocument parse(XMLInputStream var0, XmlOptions var1) throws XmlException, XMLStreamException {
         return (IdentityDocument)XmlBeans.getContextTypeLoader().parse(var0, IdentityDocument.type, var1);
      }

      /** @deprecated */
      public static XMLInputStream newValidatingXMLInputStream(XMLInputStream var0) throws XmlException, XMLStreamException {
         return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(var0, IdentityDocument.type, (XmlOptions)null);
      }

      /** @deprecated */
      public static XMLInputStream newValidatingXMLInputStream(XMLInputStream var0, XmlOptions var1) throws XmlException, XMLStreamException {
         return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(var0, IdentityDocument.type, var1);
      }

      private Factory() {
      }
   }

   public interface Identity extends XmlObject {
      SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(Identity.class.getClassLoader(), "schemacom_bea_xml.system.s7BDA940CB9F04DBD9B06BD7C8459FD58").resolveHandle("identityb583elemtype");

      SupportedTokensType getSupportedTokens();

      boolean isSetSupportedTokens();

      void setSupportedTokens(SupportedTokensType var1);

      SupportedTokensType addNewSupportedTokens();

      void unsetSupportedTokens();

      public static final class Factory {
         public static Identity newInstance() {
            return (Identity)XmlBeans.getContextTypeLoader().newInstance(IdentityDocument.Identity.type, (XmlOptions)null);
         }

         public static Identity newInstance(XmlOptions var0) {
            return (Identity)XmlBeans.getContextTypeLoader().newInstance(IdentityDocument.Identity.type, var0);
         }

         private Factory() {
         }
      }
   }
}
