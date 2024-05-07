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

public interface IntegrityDocument extends XmlObject {
   SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(IntegrityDocument.class.getClassLoader(), "schemacom_bea_xml.system.s7BDA940CB9F04DBD9B06BD7C8459FD58").resolveHandle("integritycf2cdoctype");

   Integrity getIntegrity();

   void setIntegrity(Integrity var1);

   Integrity addNewIntegrity();

   public static final class Factory {
      public static IntegrityDocument newInstance() {
         return (IntegrityDocument)XmlBeans.getContextTypeLoader().newInstance(IntegrityDocument.type, (XmlOptions)null);
      }

      public static IntegrityDocument newInstance(XmlOptions var0) {
         return (IntegrityDocument)XmlBeans.getContextTypeLoader().newInstance(IntegrityDocument.type, var0);
      }

      public static IntegrityDocument parse(String var0) throws XmlException {
         return (IntegrityDocument)XmlBeans.getContextTypeLoader().parse(var0, IntegrityDocument.type, (XmlOptions)null);
      }

      public static IntegrityDocument parse(String var0, XmlOptions var1) throws XmlException {
         return (IntegrityDocument)XmlBeans.getContextTypeLoader().parse(var0, IntegrityDocument.type, var1);
      }

      public static IntegrityDocument parse(File var0) throws XmlException, IOException {
         return (IntegrityDocument)XmlBeans.getContextTypeLoader().parse(var0, IntegrityDocument.type, (XmlOptions)null);
      }

      public static IntegrityDocument parse(File var0, XmlOptions var1) throws XmlException, IOException {
         return (IntegrityDocument)XmlBeans.getContextTypeLoader().parse(var0, IntegrityDocument.type, var1);
      }

      public static IntegrityDocument parse(URL var0) throws XmlException, IOException {
         return (IntegrityDocument)XmlBeans.getContextTypeLoader().parse(var0, IntegrityDocument.type, (XmlOptions)null);
      }

      public static IntegrityDocument parse(URL var0, XmlOptions var1) throws XmlException, IOException {
         return (IntegrityDocument)XmlBeans.getContextTypeLoader().parse(var0, IntegrityDocument.type, var1);
      }

      public static IntegrityDocument parse(InputStream var0) throws XmlException, IOException {
         return (IntegrityDocument)XmlBeans.getContextTypeLoader().parse(var0, IntegrityDocument.type, (XmlOptions)null);
      }

      public static IntegrityDocument parse(InputStream var0, XmlOptions var1) throws XmlException, IOException {
         return (IntegrityDocument)XmlBeans.getContextTypeLoader().parse(var0, IntegrityDocument.type, var1);
      }

      public static IntegrityDocument parse(Reader var0) throws XmlException, IOException {
         return (IntegrityDocument)XmlBeans.getContextTypeLoader().parse(var0, IntegrityDocument.type, (XmlOptions)null);
      }

      public static IntegrityDocument parse(Reader var0, XmlOptions var1) throws XmlException, IOException {
         return (IntegrityDocument)XmlBeans.getContextTypeLoader().parse(var0, IntegrityDocument.type, var1);
      }

      public static IntegrityDocument parse(XMLStreamReader var0) throws XmlException {
         return (IntegrityDocument)XmlBeans.getContextTypeLoader().parse(var0, IntegrityDocument.type, (XmlOptions)null);
      }

      public static IntegrityDocument parse(XMLStreamReader var0, XmlOptions var1) throws XmlException {
         return (IntegrityDocument)XmlBeans.getContextTypeLoader().parse(var0, IntegrityDocument.type, var1);
      }

      public static IntegrityDocument parse(Node var0) throws XmlException {
         return (IntegrityDocument)XmlBeans.getContextTypeLoader().parse(var0, IntegrityDocument.type, (XmlOptions)null);
      }

      public static IntegrityDocument parse(Node var0, XmlOptions var1) throws XmlException {
         return (IntegrityDocument)XmlBeans.getContextTypeLoader().parse(var0, IntegrityDocument.type, var1);
      }

      /** @deprecated */
      public static IntegrityDocument parse(XMLInputStream var0) throws XmlException, XMLStreamException {
         return (IntegrityDocument)XmlBeans.getContextTypeLoader().parse(var0, IntegrityDocument.type, (XmlOptions)null);
      }

      /** @deprecated */
      public static IntegrityDocument parse(XMLInputStream var0, XmlOptions var1) throws XmlException, XMLStreamException {
         return (IntegrityDocument)XmlBeans.getContextTypeLoader().parse(var0, IntegrityDocument.type, var1);
      }

      /** @deprecated */
      public static XMLInputStream newValidatingXMLInputStream(XMLInputStream var0) throws XmlException, XMLStreamException {
         return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(var0, IntegrityDocument.type, (XmlOptions)null);
      }

      /** @deprecated */
      public static XMLInputStream newValidatingXMLInputStream(XMLInputStream var0, XmlOptions var1) throws XmlException, XMLStreamException {
         return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(var0, IntegrityDocument.type, var1);
      }

      private Factory() {
      }
   }

   public interface Integrity extends XmlObject {
      SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(Integrity.class.getClassLoader(), "schemacom_bea_xml.system.s7BDA940CB9F04DBD9B06BD7C8459FD58").resolveHandle("integritydb33elemtype");

      AlgorithmType getSignatureAlgorithm();

      void setSignatureAlgorithm(AlgorithmType var1);

      AlgorithmType addNewSignatureAlgorithm();

      AlgorithmType getCanonicalizationAlgorithm();

      void setCanonicalizationAlgorithm(AlgorithmType var1);

      AlgorithmType addNewCanonicalizationAlgorithm();

      IntegrityTargetType[] getTargetArray();

      IntegrityTargetType getTargetArray(int var1);

      int sizeOfTargetArray();

      void setTargetArray(IntegrityTargetType[] var1);

      void setTargetArray(int var1, IntegrityTargetType var2);

      IntegrityTargetType insertNewTarget(int var1);

      IntegrityTargetType addNewTarget();

      void removeTarget(int var1);

      SupportedTokensType getSupportedTokens();

      boolean isSetSupportedTokens();

      void setSupportedTokens(SupportedTokensType var1);

      SupportedTokensType addNewSupportedTokens();

      void unsetSupportedTokens();

      boolean getSignToken();

      XmlBoolean xgetSignToken();

      boolean isSetSignToken();

      void setSignToken(boolean var1);

      void xsetSignToken(XmlBoolean var1);

      void unsetSignToken();

      boolean getSupportTrust10();

      XmlBoolean xgetSupportTrust10();

      boolean isSetSupportTrust10();

      void setSupportTrust10(boolean var1);

      void xsetSupportTrust10(XmlBoolean var1);

      void unsetSupportTrust10();

      boolean getX509AuthConditional();

      XmlBoolean xgetX509AuthConditional();

      boolean isSetX509AuthConditional();

      void setX509AuthConditional(boolean var1);

      void xsetX509AuthConditional(XmlBoolean var1);

      void unsetX509AuthConditional();

      public static final class Factory {
         public static Integrity newInstance() {
            return (Integrity)XmlBeans.getContextTypeLoader().newInstance(IntegrityDocument.Integrity.type, (XmlOptions)null);
         }

         public static Integrity newInstance(XmlOptions var0) {
            return (Integrity)XmlBeans.getContextTypeLoader().newInstance(IntegrityDocument.Integrity.type, var0);
         }

         private Factory() {
         }
      }
   }
}
