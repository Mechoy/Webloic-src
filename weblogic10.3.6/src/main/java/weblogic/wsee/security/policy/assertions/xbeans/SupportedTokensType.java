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

public interface SupportedTokensType extends XmlObject {
   SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(SupportedTokensType.class.getClassLoader(), "schemacom_bea_xml.system.s7BDA940CB9F04DBD9B06BD7C8459FD58").resolveHandle("supportedtokenstypec987type");

   SecurityTokenType[] getSecurityTokenArray();

   SecurityTokenType getSecurityTokenArray(int var1);

   int sizeOfSecurityTokenArray();

   void setSecurityTokenArray(SecurityTokenType[] var1);

   void setSecurityTokenArray(int var1, SecurityTokenType var2);

   SecurityTokenType insertNewSecurityToken(int var1);

   SecurityTokenType addNewSecurityToken();

   void removeSecurityToken(int var1);

   public static final class Factory {
      public static SupportedTokensType newInstance() {
         return (SupportedTokensType)XmlBeans.getContextTypeLoader().newInstance(SupportedTokensType.type, (XmlOptions)null);
      }

      public static SupportedTokensType newInstance(XmlOptions var0) {
         return (SupportedTokensType)XmlBeans.getContextTypeLoader().newInstance(SupportedTokensType.type, var0);
      }

      public static SupportedTokensType parse(String var0) throws XmlException {
         return (SupportedTokensType)XmlBeans.getContextTypeLoader().parse(var0, SupportedTokensType.type, (XmlOptions)null);
      }

      public static SupportedTokensType parse(String var0, XmlOptions var1) throws XmlException {
         return (SupportedTokensType)XmlBeans.getContextTypeLoader().parse(var0, SupportedTokensType.type, var1);
      }

      public static SupportedTokensType parse(File var0) throws XmlException, IOException {
         return (SupportedTokensType)XmlBeans.getContextTypeLoader().parse(var0, SupportedTokensType.type, (XmlOptions)null);
      }

      public static SupportedTokensType parse(File var0, XmlOptions var1) throws XmlException, IOException {
         return (SupportedTokensType)XmlBeans.getContextTypeLoader().parse(var0, SupportedTokensType.type, var1);
      }

      public static SupportedTokensType parse(URL var0) throws XmlException, IOException {
         return (SupportedTokensType)XmlBeans.getContextTypeLoader().parse(var0, SupportedTokensType.type, (XmlOptions)null);
      }

      public static SupportedTokensType parse(URL var0, XmlOptions var1) throws XmlException, IOException {
         return (SupportedTokensType)XmlBeans.getContextTypeLoader().parse(var0, SupportedTokensType.type, var1);
      }

      public static SupportedTokensType parse(InputStream var0) throws XmlException, IOException {
         return (SupportedTokensType)XmlBeans.getContextTypeLoader().parse(var0, SupportedTokensType.type, (XmlOptions)null);
      }

      public static SupportedTokensType parse(InputStream var0, XmlOptions var1) throws XmlException, IOException {
         return (SupportedTokensType)XmlBeans.getContextTypeLoader().parse(var0, SupportedTokensType.type, var1);
      }

      public static SupportedTokensType parse(Reader var0) throws XmlException, IOException {
         return (SupportedTokensType)XmlBeans.getContextTypeLoader().parse(var0, SupportedTokensType.type, (XmlOptions)null);
      }

      public static SupportedTokensType parse(Reader var0, XmlOptions var1) throws XmlException, IOException {
         return (SupportedTokensType)XmlBeans.getContextTypeLoader().parse(var0, SupportedTokensType.type, var1);
      }

      public static SupportedTokensType parse(XMLStreamReader var0) throws XmlException {
         return (SupportedTokensType)XmlBeans.getContextTypeLoader().parse(var0, SupportedTokensType.type, (XmlOptions)null);
      }

      public static SupportedTokensType parse(XMLStreamReader var0, XmlOptions var1) throws XmlException {
         return (SupportedTokensType)XmlBeans.getContextTypeLoader().parse(var0, SupportedTokensType.type, var1);
      }

      public static SupportedTokensType parse(Node var0) throws XmlException {
         return (SupportedTokensType)XmlBeans.getContextTypeLoader().parse(var0, SupportedTokensType.type, (XmlOptions)null);
      }

      public static SupportedTokensType parse(Node var0, XmlOptions var1) throws XmlException {
         return (SupportedTokensType)XmlBeans.getContextTypeLoader().parse(var0, SupportedTokensType.type, var1);
      }

      /** @deprecated */
      public static SupportedTokensType parse(XMLInputStream var0) throws XmlException, XMLStreamException {
         return (SupportedTokensType)XmlBeans.getContextTypeLoader().parse(var0, SupportedTokensType.type, (XmlOptions)null);
      }

      /** @deprecated */
      public static SupportedTokensType parse(XMLInputStream var0, XmlOptions var1) throws XmlException, XMLStreamException {
         return (SupportedTokensType)XmlBeans.getContextTypeLoader().parse(var0, SupportedTokensType.type, var1);
      }

      /** @deprecated */
      public static XMLInputStream newValidatingXMLInputStream(XMLInputStream var0) throws XmlException, XMLStreamException {
         return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(var0, SupportedTokensType.type, (XmlOptions)null);
      }

      /** @deprecated */
      public static XMLInputStream newValidatingXMLInputStream(XMLInputStream var0, XmlOptions var1) throws XmlException, XMLStreamException {
         return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(var0, SupportedTokensType.type, var1);
      }

      private Factory() {
      }
   }
}
