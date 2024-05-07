package weblogic.webservice.encoding;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import javax.xml.rpc.JAXRPCException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.xml.sax.InputSource;
import weblogic.utils.CharsetMap;
import weblogic.utils.io.XMLDeclaration;
import weblogic.xml.schema.binding.util.runtime.ByteList;
import weblogic.xml.schema.binding.util.runtime.CharList;

/** @deprecated */
public class XMLSourceCodec extends AttachmentCodec {
   protected String xmlEncoding = "UTF-8";

   protected Object deserializeContent(Object var1) {
      String var2 = this.obj2String(var1);
      return new StreamSource(new StringReader(var2));
   }

   protected String getContentType() {
      return "text/xml; charset=" + this.xmlEncoding;
   }

   protected Object serializeContent(Object var1) {
      return this.obj2String(var1);
   }

   private String obj2String(Object var1) {
      if (var1 instanceof String) {
         return (String)var1;
      } else if (!(var1 instanceof SAXSource) && !(var1 instanceof StreamSource) && !(var1 instanceof DOMSource)) {
         throw new JAXRPCException("unable to s(d)erialize[" + var1 + "]. " + "input should be of type " + "javax.xml.transform.stream.StreamSource, " + "javax.xml.transform.sax.SAXSource, or " + "javax.xml.transform.dom.DOMSource");
      } else {
         String var2 = null;
         Reader var4;
         if (var1 instanceof SAXSource) {
            InputSource var3 = SAXSource.sourceToInputSource((SAXSource)var1);
            if (var3 != null) {
               var4 = var3.getCharacterStream();
               if (var4 != null) {
                  this.getXMLEncoding(var4);
                  var2 = this.getString(var4);
               } else {
                  InputStream var5 = var3.getByteStream();
                  if (var5 != null) {
                     var2 = this.getString(var5);
                  }
               }
            }
         } else if (var1 instanceof StreamSource) {
            Reader var22 = ((StreamSource)var1).getReader();
            if (var22 != null) {
               this.getXMLEncoding(var22);
               var2 = this.getString(var22);
            } else {
               InputStream var24 = ((StreamSource)var1).getInputStream();
               if (var24 != null) {
                  var2 = this.getString(var24);
               }
            }
         }

         if (var1 instanceof DOMSource || var2 == null) {
            ByteArrayOutputStream var23 = new ByteArrayOutputStream();
            var4 = null;

            Transformer var25;
            try {
               var25 = TransformerFactory.newInstance().newTransformer();
            } catch (TransformerConfigurationException var21) {
               throw new JAXRPCException(var21);
            }

            StreamResult var26 = new StreamResult(var23);

            try {
               var25.transform((Source)var1, var26);
            } catch (TransformerException var20) {
               throw new JAXRPCException("failed to transform:" + var20, var20);
            }

            byte[] var6 = var23.toByteArray();
            ByteArrayInputStream var7 = new ByteArrayInputStream(var6);

            try {
               String var8 = getXMLEncoding((InputStream)var7);
               var2 = this.getString(var6, var8);
            } catch (IOException var18) {
               throw new JAXRPCException("failed to get xml encoding:" + var18);
            } finally {
               try {
                  var7.close();
               } catch (IOException var17) {
               }

            }
         }

         return var2;
      }
   }

   private static String getXMLEncoding(InputStream var0) throws IOException {
      String var1 = null;
      if (var0.markSupported()) {
         var0.mark(4080);
         XMLDeclaration var2 = new XMLDeclaration();
         var2.parse(var0);
         var1 = var2.getEncoding();
         var0.reset();
      }

      return var1;
   }

   private void getXMLEncoding(Reader var1) {
      String var2 = null;
      if (var1.markSupported()) {
         try {
            var1.mark(4080);
            XMLDeclaration var3 = new XMLDeclaration();
            var3.parse(var1);
            var2 = var3.getEncoding();
            var1.reset();
         } catch (IOException var4) {
         }
      }

      if (var2 != null) {
         this.xmlEncoding = var2;
      }

   }

   private String getString(Reader var1) throws JAXRPCException {
      CharList var2 = new CharList();

      try {
         for(int var3 = var1.read(); var3 != -1; var3 = var1.read()) {
            var2.add((char)var3);
         }
      } catch (IOException var4) {
         throw new JAXRPCException("failed to get xml from input stream:" + var4);
      }

      return new String(var2.getMinSizedArray());
   }

   private String getString(InputStream var1) throws JAXRPCException {
      ByteList var2 = new ByteList();

      String var3;
      try {
         var3 = getXMLEncoding(var1);
      } catch (IOException var5) {
         throw new JAXRPCException("failed to get xml encoding:" + var5);
      }

      try {
         for(int var4 = var1.read(); var4 != -1; var4 = var1.read()) {
            var2.add((byte)var4);
         }
      } catch (IOException var6) {
         throw new JAXRPCException("failed to get xml from input stream:" + var6);
      }

      return this.getString(var2.getMinSizedArray(), var3);
   }

   private String getString(byte[] var1, String var2) {
      if (var2 != null) {
         this.xmlEncoding = var2;
      }

      String var3 = CharsetMap.getJavaFromIANA(this.xmlEncoding);

      try {
         String var4;
         if (var3 != null) {
            var4 = new String(var1, var3);
         } else {
            var4 = new String(var1, "UTF-8");
         }

         return var4;
      } catch (IOException var6) {
         throw new JAXRPCException("failed to get xml from bytes:" + var6);
      }
   }
}
