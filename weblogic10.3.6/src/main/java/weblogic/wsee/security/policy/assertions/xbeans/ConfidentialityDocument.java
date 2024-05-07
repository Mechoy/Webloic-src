package weblogic.wsee.security.policy.assertions.xbeans;

import com.bea.xml.SchemaType;
import com.bea.xml.XmlBeans;
import com.bea.xml.XmlBoolean;
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

public interface ConfidentialityDocument extends XmlObject {
   SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(ConfidentialityDocument.class.getClassLoader(), "schemacom_bea_xml.system.s7BDA940CB9F04DBD9B06BD7C8459FD58").resolveHandle("confidentiality9db3doctype");

   Confidentiality getConfidentiality();

   void setConfidentiality(Confidentiality var1);

   Confidentiality addNewConfidentiality();

   public static final class Factory {
      public static ConfidentialityDocument newInstance() {
         return (ConfidentialityDocument)XmlBeans.getContextTypeLoader().newInstance(ConfidentialityDocument.type, (XmlOptions)null);
      }

      public static ConfidentialityDocument newInstance(XmlOptions var0) {
         return (ConfidentialityDocument)XmlBeans.getContextTypeLoader().newInstance(ConfidentialityDocument.type, var0);
      }

      public static ConfidentialityDocument parse(String var0) throws XmlException {
         return (ConfidentialityDocument)XmlBeans.getContextTypeLoader().parse(var0, ConfidentialityDocument.type, (XmlOptions)null);
      }

      public static ConfidentialityDocument parse(String var0, XmlOptions var1) throws XmlException {
         return (ConfidentialityDocument)XmlBeans.getContextTypeLoader().parse(var0, ConfidentialityDocument.type, var1);
      }

      public static ConfidentialityDocument parse(File var0) throws XmlException, IOException {
         return (ConfidentialityDocument)XmlBeans.getContextTypeLoader().parse(var0, ConfidentialityDocument.type, (XmlOptions)null);
      }

      public static ConfidentialityDocument parse(File var0, XmlOptions var1) throws XmlException, IOException {
         return (ConfidentialityDocument)XmlBeans.getContextTypeLoader().parse(var0, ConfidentialityDocument.type, var1);
      }

      public static ConfidentialityDocument parse(URL var0) throws XmlException, IOException {
         return (ConfidentialityDocument)XmlBeans.getContextTypeLoader().parse(var0, ConfidentialityDocument.type, (XmlOptions)null);
      }

      public static ConfidentialityDocument parse(URL var0, XmlOptions var1) throws XmlException, IOException {
         return (ConfidentialityDocument)XmlBeans.getContextTypeLoader().parse(var0, ConfidentialityDocument.type, var1);
      }

      public static ConfidentialityDocument parse(InputStream var0) throws XmlException, IOException {
         return (ConfidentialityDocument)XmlBeans.getContextTypeLoader().parse(var0, ConfidentialityDocument.type, (XmlOptions)null);
      }

      public static ConfidentialityDocument parse(InputStream var0, XmlOptions var1) throws XmlException, IOException {
         return (ConfidentialityDocument)XmlBeans.getContextTypeLoader().parse(var0, ConfidentialityDocument.type, var1);
      }

      public static ConfidentialityDocument parse(Reader var0) throws XmlException, IOException {
         return (ConfidentialityDocument)XmlBeans.getContextTypeLoader().parse(var0, ConfidentialityDocument.type, (XmlOptions)null);
      }

      public static ConfidentialityDocument parse(Reader var0, XmlOptions var1) throws XmlException, IOException {
         return (ConfidentialityDocument)XmlBeans.getContextTypeLoader().parse(var0, ConfidentialityDocument.type, var1);
      }

      public static ConfidentialityDocument parse(XMLStreamReader var0) throws XmlException {
         return (ConfidentialityDocument)XmlBeans.getContextTypeLoader().parse(var0, ConfidentialityDocument.type, (XmlOptions)null);
      }

      public static ConfidentialityDocument parse(XMLStreamReader var0, XmlOptions var1) throws XmlException {
         return (ConfidentialityDocument)XmlBeans.getContextTypeLoader().parse(var0, ConfidentialityDocument.type, var1);
      }

      public static ConfidentialityDocument parse(Node var0) throws XmlException {
         return (ConfidentialityDocument)XmlBeans.getContextTypeLoader().parse(var0, ConfidentialityDocument.type, (XmlOptions)null);
      }

      public static ConfidentialityDocument parse(Node var0, XmlOptions var1) throws XmlException {
         return (ConfidentialityDocument)XmlBeans.getContextTypeLoader().parse(var0, ConfidentialityDocument.type, var1);
      }

      /** @deprecated */
      public static ConfidentialityDocument parse(XMLInputStream var0) throws XmlException, XMLStreamException {
         return (ConfidentialityDocument)XmlBeans.getContextTypeLoader().parse(var0, ConfidentialityDocument.type, (XmlOptions)null);
      }

      /** @deprecated */
      public static ConfidentialityDocument parse(XMLInputStream var0, XmlOptions var1) throws XmlException, XMLStreamException {
         return (ConfidentialityDocument)XmlBeans.getContextTypeLoader().parse(var0, ConfidentialityDocument.type, var1);
      }

      /** @deprecated */
      public static XMLInputStream newValidatingXMLInputStream(XMLInputStream var0) throws XmlException, XMLStreamException {
         return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(var0, ConfidentialityDocument.type, (XmlOptions)null);
      }

      /** @deprecated */
      public static XMLInputStream newValidatingXMLInputStream(XMLInputStream var0, XmlOptions var1) throws XmlException, XMLStreamException {
         return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(var0, ConfidentialityDocument.type, var1);
      }

      private Factory() {
      }
   }

   public interface Confidentiality extends XmlObject {
      SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(Confidentiality.class.getClassLoader(), "schemacom_bea_xml.system.s7BDA940CB9F04DBD9B06BD7C8459FD58").resolveHandle("confidentialityf001elemtype");

      AlgorithmType getKeyWrappingAlgorithm();

      boolean isSetKeyWrappingAlgorithm();

      void setKeyWrappingAlgorithm(AlgorithmType var1);

      AlgorithmType addNewKeyWrappingAlgorithm();

      void unsetKeyWrappingAlgorithm();

      ConfidentialityTargetType[] getTargetArray();

      ConfidentialityTargetType getTargetArray(int var1);

      int sizeOfTargetArray();

      void setTargetArray(ConfidentialityTargetType[] var1);

      void setTargetArray(int var1, ConfidentialityTargetType var2);

      ConfidentialityTargetType insertNewTarget(int var1);

      ConfidentialityTargetType addNewTarget();

      void removeTarget(int var1);

      KeyInfoType getKeyInfo();

      void setKeyInfo(KeyInfoType var1);

      KeyInfoType addNewKeyInfo();

      boolean getSupportTrust10();

      XmlBoolean xgetSupportTrust10();

      boolean isSetSupportTrust10();

      void setSupportTrust10(boolean var1);

      void xsetSupportTrust10(XmlBoolean var1);

      void unsetSupportTrust10();

      public static final class Factory {
         public static Confidentiality newInstance() {
            return (Confidentiality)XmlBeans.getContextTypeLoader().newInstance(ConfidentialityDocument.Confidentiality.type, (XmlOptions)null);
         }

         public static Confidentiality newInstance(XmlOptions var0) {
            return (Confidentiality)XmlBeans.getContextTypeLoader().newInstance(ConfidentialityDocument.Confidentiality.type, var0);
         }

         private Factory() {
         }
      }
   }
}
