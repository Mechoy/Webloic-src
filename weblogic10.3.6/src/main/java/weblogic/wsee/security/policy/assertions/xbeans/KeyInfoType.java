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

public interface KeyInfoType extends XmlObject {
   SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(KeyInfoType.class.getClassLoader(), "schemacom_bea_xml.system.s7BDA940CB9F04DBD9B06BD7C8459FD58").resolveHandle("keyinfotype1ae2type");

   SecurityTokenType[] getSecurityTokenArray();

   SecurityTokenType getSecurityTokenArray(int var1);

   int sizeOfSecurityTokenArray();

   void setSecurityTokenArray(SecurityTokenType[] var1);

   void setSecurityTokenArray(int var1, SecurityTokenType var2);

   SecurityTokenType insertNewSecurityToken(int var1);

   SecurityTokenType addNewSecurityToken();

   void removeSecurityToken(int var1);

   SecurityTokenReferenceType[] getSecurityTokenReferenceArray();

   SecurityTokenReferenceType getSecurityTokenReferenceArray(int var1);

   int sizeOfSecurityTokenReferenceArray();

   void setSecurityTokenReferenceArray(SecurityTokenReferenceType[] var1);

   void setSecurityTokenReferenceArray(int var1, SecurityTokenReferenceType var2);

   SecurityTokenReferenceType insertNewSecurityTokenReference(int var1);

   SecurityTokenReferenceType addNewSecurityTokenReference();

   void removeSecurityTokenReference(int var1);

   public static final class Factory {
      public static KeyInfoType newInstance() {
         return (KeyInfoType)XmlBeans.getContextTypeLoader().newInstance(KeyInfoType.type, (XmlOptions)null);
      }

      public static KeyInfoType newInstance(XmlOptions var0) {
         return (KeyInfoType)XmlBeans.getContextTypeLoader().newInstance(KeyInfoType.type, var0);
      }

      public static KeyInfoType parse(String var0) throws XmlException {
         return (KeyInfoType)XmlBeans.getContextTypeLoader().parse(var0, KeyInfoType.type, (XmlOptions)null);
      }

      public static KeyInfoType parse(String var0, XmlOptions var1) throws XmlException {
         return (KeyInfoType)XmlBeans.getContextTypeLoader().parse(var0, KeyInfoType.type, var1);
      }

      public static KeyInfoType parse(File var0) throws XmlException, IOException {
         return (KeyInfoType)XmlBeans.getContextTypeLoader().parse(var0, KeyInfoType.type, (XmlOptions)null);
      }

      public static KeyInfoType parse(File var0, XmlOptions var1) throws XmlException, IOException {
         return (KeyInfoType)XmlBeans.getContextTypeLoader().parse(var0, KeyInfoType.type, var1);
      }

      public static KeyInfoType parse(URL var0) throws XmlException, IOException {
         return (KeyInfoType)XmlBeans.getContextTypeLoader().parse(var0, KeyInfoType.type, (XmlOptions)null);
      }

      public static KeyInfoType parse(URL var0, XmlOptions var1) throws XmlException, IOException {
         return (KeyInfoType)XmlBeans.getContextTypeLoader().parse(var0, KeyInfoType.type, var1);
      }

      public static KeyInfoType parse(InputStream var0) throws XmlException, IOException {
         return (KeyInfoType)XmlBeans.getContextTypeLoader().parse(var0, KeyInfoType.type, (XmlOptions)null);
      }

      public static KeyInfoType parse(InputStream var0, XmlOptions var1) throws XmlException, IOException {
         return (KeyInfoType)XmlBeans.getContextTypeLoader().parse(var0, KeyInfoType.type, var1);
      }

      public static KeyInfoType parse(Reader var0) throws XmlException, IOException {
         return (KeyInfoType)XmlBeans.getContextTypeLoader().parse(var0, KeyInfoType.type, (XmlOptions)null);
      }

      public static KeyInfoType parse(Reader var0, XmlOptions var1) throws XmlException, IOException {
         return (KeyInfoType)XmlBeans.getContextTypeLoader().parse(var0, KeyInfoType.type, var1);
      }

      public static KeyInfoType parse(XMLStreamReader var0) throws XmlException {
         return (KeyInfoType)XmlBeans.getContextTypeLoader().parse(var0, KeyInfoType.type, (XmlOptions)null);
      }

      public static KeyInfoType parse(XMLStreamReader var0, XmlOptions var1) throws XmlException {
         return (KeyInfoType)XmlBeans.getContextTypeLoader().parse(var0, KeyInfoType.type, var1);
      }

      public static KeyInfoType parse(Node var0) throws XmlException {
         return (KeyInfoType)XmlBeans.getContextTypeLoader().parse(var0, KeyInfoType.type, (XmlOptions)null);
      }

      public static KeyInfoType parse(Node var0, XmlOptions var1) throws XmlException {
         return (KeyInfoType)XmlBeans.getContextTypeLoader().parse(var0, KeyInfoType.type, var1);
      }

      /** @deprecated */
      public static KeyInfoType parse(XMLInputStream var0) throws XmlException, XMLStreamException {
         return (KeyInfoType)XmlBeans.getContextTypeLoader().parse(var0, KeyInfoType.type, (XmlOptions)null);
      }

      /** @deprecated */
      public static KeyInfoType parse(XMLInputStream var0, XmlOptions var1) throws XmlException, XMLStreamException {
         return (KeyInfoType)XmlBeans.getContextTypeLoader().parse(var0, KeyInfoType.type, var1);
      }

      /** @deprecated */
      public static XMLInputStream newValidatingXMLInputStream(XMLInputStream var0) throws XmlException, XMLStreamException {
         return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(var0, KeyInfoType.type, (XmlOptions)null);
      }

      /** @deprecated */
      public static XMLInputStream newValidatingXMLInputStream(XMLInputStream var0, XmlOptions var1) throws XmlException, XMLStreamException {
         return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(var0, KeyInfoType.type, var1);
      }

      private Factory() {
      }
   }
}
