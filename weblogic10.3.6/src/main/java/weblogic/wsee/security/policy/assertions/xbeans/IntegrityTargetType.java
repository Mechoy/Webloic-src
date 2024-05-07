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

public interface IntegrityTargetType extends XmlObject {
   SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(IntegrityTargetType.class.getClassLoader(), "schemacom_bea_xml.system.s7BDA940CB9F04DBD9B06BD7C8459FD58").resolveHandle("integritytargettyped9f1type");

   AlgorithmType getDigestAlgorithm();

   void setDigestAlgorithm(AlgorithmType var1);

   AlgorithmType addNewDigestAlgorithm();

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

   public static final class Factory {
      public static IntegrityTargetType newInstance() {
         return (IntegrityTargetType)XmlBeans.getContextTypeLoader().newInstance(IntegrityTargetType.type, (XmlOptions)null);
      }

      public static IntegrityTargetType newInstance(XmlOptions var0) {
         return (IntegrityTargetType)XmlBeans.getContextTypeLoader().newInstance(IntegrityTargetType.type, var0);
      }

      public static IntegrityTargetType parse(String var0) throws XmlException {
         return (IntegrityTargetType)XmlBeans.getContextTypeLoader().parse(var0, IntegrityTargetType.type, (XmlOptions)null);
      }

      public static IntegrityTargetType parse(String var0, XmlOptions var1) throws XmlException {
         return (IntegrityTargetType)XmlBeans.getContextTypeLoader().parse(var0, IntegrityTargetType.type, var1);
      }

      public static IntegrityTargetType parse(File var0) throws XmlException, IOException {
         return (IntegrityTargetType)XmlBeans.getContextTypeLoader().parse(var0, IntegrityTargetType.type, (XmlOptions)null);
      }

      public static IntegrityTargetType parse(File var0, XmlOptions var1) throws XmlException, IOException {
         return (IntegrityTargetType)XmlBeans.getContextTypeLoader().parse(var0, IntegrityTargetType.type, var1);
      }

      public static IntegrityTargetType parse(URL var0) throws XmlException, IOException {
         return (IntegrityTargetType)XmlBeans.getContextTypeLoader().parse(var0, IntegrityTargetType.type, (XmlOptions)null);
      }

      public static IntegrityTargetType parse(URL var0, XmlOptions var1) throws XmlException, IOException {
         return (IntegrityTargetType)XmlBeans.getContextTypeLoader().parse(var0, IntegrityTargetType.type, var1);
      }

      public static IntegrityTargetType parse(InputStream var0) throws XmlException, IOException {
         return (IntegrityTargetType)XmlBeans.getContextTypeLoader().parse(var0, IntegrityTargetType.type, (XmlOptions)null);
      }

      public static IntegrityTargetType parse(InputStream var0, XmlOptions var1) throws XmlException, IOException {
         return (IntegrityTargetType)XmlBeans.getContextTypeLoader().parse(var0, IntegrityTargetType.type, var1);
      }

      public static IntegrityTargetType parse(Reader var0) throws XmlException, IOException {
         return (IntegrityTargetType)XmlBeans.getContextTypeLoader().parse(var0, IntegrityTargetType.type, (XmlOptions)null);
      }

      public static IntegrityTargetType parse(Reader var0, XmlOptions var1) throws XmlException, IOException {
         return (IntegrityTargetType)XmlBeans.getContextTypeLoader().parse(var0, IntegrityTargetType.type, var1);
      }

      public static IntegrityTargetType parse(XMLStreamReader var0) throws XmlException {
         return (IntegrityTargetType)XmlBeans.getContextTypeLoader().parse(var0, IntegrityTargetType.type, (XmlOptions)null);
      }

      public static IntegrityTargetType parse(XMLStreamReader var0, XmlOptions var1) throws XmlException {
         return (IntegrityTargetType)XmlBeans.getContextTypeLoader().parse(var0, IntegrityTargetType.type, var1);
      }

      public static IntegrityTargetType parse(Node var0) throws XmlException {
         return (IntegrityTargetType)XmlBeans.getContextTypeLoader().parse(var0, IntegrityTargetType.type, (XmlOptions)null);
      }

      public static IntegrityTargetType parse(Node var0, XmlOptions var1) throws XmlException {
         return (IntegrityTargetType)XmlBeans.getContextTypeLoader().parse(var0, IntegrityTargetType.type, var1);
      }

      /** @deprecated */
      public static IntegrityTargetType parse(XMLInputStream var0) throws XmlException, XMLStreamException {
         return (IntegrityTargetType)XmlBeans.getContextTypeLoader().parse(var0, IntegrityTargetType.type, (XmlOptions)null);
      }

      /** @deprecated */
      public static IntegrityTargetType parse(XMLInputStream var0, XmlOptions var1) throws XmlException, XMLStreamException {
         return (IntegrityTargetType)XmlBeans.getContextTypeLoader().parse(var0, IntegrityTargetType.type, var1);
      }

      /** @deprecated */
      public static XMLInputStream newValidatingXMLInputStream(XMLInputStream var0) throws XmlException, XMLStreamException {
         return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(var0, IntegrityTargetType.type, (XmlOptions)null);
      }

      /** @deprecated */
      public static XMLInputStream newValidatingXMLInputStream(XMLInputStream var0, XmlOptions var1) throws XmlException, XMLStreamException {
         return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(var0, IntegrityTargetType.type, var1);
      }

      private Factory() {
      }
   }
}
