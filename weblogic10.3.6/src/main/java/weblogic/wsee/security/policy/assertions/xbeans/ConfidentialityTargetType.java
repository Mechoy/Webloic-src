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

public interface ConfidentialityTargetType extends XmlObject {
   SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(ConfidentialityTargetType.class.getClassLoader(), "schemacom_bea_xml.system.s7BDA940CB9F04DBD9B06BD7C8459FD58").resolveHandle("confidentialitytargettype37b8type");

   AlgorithmType getEncryptionAlgorithm();

   void setEncryptionAlgorithm(AlgorithmType var1);

   AlgorithmType addNewEncryptionAlgorithm();

   TransformType[] getTransformArray();

   TransformType getTransformArray(int var1);

   int sizeOfTransformArray();

   void setTransformArray(TransformType[] var1);

   void setTransformArray(int var1, TransformType var2);

   TransformType insertNewTransform(int var1);

   TransformType addNewTransform();

   void removeTransform(int var1);

   MessagePartsType getMessageParts();

   void setMessageParts(MessagePartsType var1);

   MessagePartsType addNewMessageParts();

   boolean getEncryptContentOnly();

   XmlBoolean xgetEncryptContentOnly();

   boolean isSetEncryptContentOnly();

   void setEncryptContentOnly(boolean var1);

   void xsetEncryptContentOnly(XmlBoolean var1);

   void unsetEncryptContentOnly();

   public static final class Factory {
      public static ConfidentialityTargetType newInstance() {
         return (ConfidentialityTargetType)XmlBeans.getContextTypeLoader().newInstance(ConfidentialityTargetType.type, (XmlOptions)null);
      }

      public static ConfidentialityTargetType newInstance(XmlOptions var0) {
         return (ConfidentialityTargetType)XmlBeans.getContextTypeLoader().newInstance(ConfidentialityTargetType.type, var0);
      }

      public static ConfidentialityTargetType parse(String var0) throws XmlException {
         return (ConfidentialityTargetType)XmlBeans.getContextTypeLoader().parse(var0, ConfidentialityTargetType.type, (XmlOptions)null);
      }

      public static ConfidentialityTargetType parse(String var0, XmlOptions var1) throws XmlException {
         return (ConfidentialityTargetType)XmlBeans.getContextTypeLoader().parse(var0, ConfidentialityTargetType.type, var1);
      }

      public static ConfidentialityTargetType parse(File var0) throws XmlException, IOException {
         return (ConfidentialityTargetType)XmlBeans.getContextTypeLoader().parse(var0, ConfidentialityTargetType.type, (XmlOptions)null);
      }

      public static ConfidentialityTargetType parse(File var0, XmlOptions var1) throws XmlException, IOException {
         return (ConfidentialityTargetType)XmlBeans.getContextTypeLoader().parse(var0, ConfidentialityTargetType.type, var1);
      }

      public static ConfidentialityTargetType parse(URL var0) throws XmlException, IOException {
         return (ConfidentialityTargetType)XmlBeans.getContextTypeLoader().parse(var0, ConfidentialityTargetType.type, (XmlOptions)null);
      }

      public static ConfidentialityTargetType parse(URL var0, XmlOptions var1) throws XmlException, IOException {
         return (ConfidentialityTargetType)XmlBeans.getContextTypeLoader().parse(var0, ConfidentialityTargetType.type, var1);
      }

      public static ConfidentialityTargetType parse(InputStream var0) throws XmlException, IOException {
         return (ConfidentialityTargetType)XmlBeans.getContextTypeLoader().parse(var0, ConfidentialityTargetType.type, (XmlOptions)null);
      }

      public static ConfidentialityTargetType parse(InputStream var0, XmlOptions var1) throws XmlException, IOException {
         return (ConfidentialityTargetType)XmlBeans.getContextTypeLoader().parse(var0, ConfidentialityTargetType.type, var1);
      }

      public static ConfidentialityTargetType parse(Reader var0) throws XmlException, IOException {
         return (ConfidentialityTargetType)XmlBeans.getContextTypeLoader().parse(var0, ConfidentialityTargetType.type, (XmlOptions)null);
      }

      public static ConfidentialityTargetType parse(Reader var0, XmlOptions var1) throws XmlException, IOException {
         return (ConfidentialityTargetType)XmlBeans.getContextTypeLoader().parse(var0, ConfidentialityTargetType.type, var1);
      }

      public static ConfidentialityTargetType parse(XMLStreamReader var0) throws XmlException {
         return (ConfidentialityTargetType)XmlBeans.getContextTypeLoader().parse(var0, ConfidentialityTargetType.type, (XmlOptions)null);
      }

      public static ConfidentialityTargetType parse(XMLStreamReader var0, XmlOptions var1) throws XmlException {
         return (ConfidentialityTargetType)XmlBeans.getContextTypeLoader().parse(var0, ConfidentialityTargetType.type, var1);
      }

      public static ConfidentialityTargetType parse(Node var0) throws XmlException {
         return (ConfidentialityTargetType)XmlBeans.getContextTypeLoader().parse(var0, ConfidentialityTargetType.type, (XmlOptions)null);
      }

      public static ConfidentialityTargetType parse(Node var0, XmlOptions var1) throws XmlException {
         return (ConfidentialityTargetType)XmlBeans.getContextTypeLoader().parse(var0, ConfidentialityTargetType.type, var1);
      }

      /** @deprecated */
      public static ConfidentialityTargetType parse(XMLInputStream var0) throws XmlException, XMLStreamException {
         return (ConfidentialityTargetType)XmlBeans.getContextTypeLoader().parse(var0, ConfidentialityTargetType.type, (XmlOptions)null);
      }

      /** @deprecated */
      public static ConfidentialityTargetType parse(XMLInputStream var0, XmlOptions var1) throws XmlException, XMLStreamException {
         return (ConfidentialityTargetType)XmlBeans.getContextTypeLoader().parse(var0, ConfidentialityTargetType.type, var1);
      }

      /** @deprecated */
      public static XMLInputStream newValidatingXMLInputStream(XMLInputStream var0) throws XmlException, XMLStreamException {
         return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(var0, ConfidentialityTargetType.type, (XmlOptions)null);
      }

      /** @deprecated */
      public static XMLInputStream newValidatingXMLInputStream(XMLInputStream var0, XmlOptions var1) throws XmlException, XMLStreamException {
         return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(var0, ConfidentialityTargetType.type, var1);
      }

      private Factory() {
      }
   }
}
