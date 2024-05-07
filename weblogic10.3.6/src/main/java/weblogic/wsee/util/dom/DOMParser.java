package weblogic.wsee.util.dom;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import weblogic.wsee.connection.transport.TransportInfo;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.util.is.InputSourceUtil;
import weblogic.xml.domimpl.Loader;
import weblogic.xml.jaxp.WebLogicDocumentBuilderFactory;

public class DOMParser {
   private static final boolean disableWLSDomImpl = Boolean.getBoolean("weblogic.wsee.disableWLSDOMImpl");
   private static final ThreadLocal<DocumentLookup> urlDocMap = new ThreadLocal();

   public static ThreadLocal<DocumentLookup> getURLDocumentMap() {
      return urlDocMap;
   }

   public static Document getDocument(String var0) throws IOException {
      return getDocument((TransportInfo)null, var0);
   }

   public static Document getDocument(TransportInfo var0, String var1) throws IOException {
      return disableWLSDomImpl ? getDefaultDocumentImpl(var1) : getWebLogicDocumentImpl(var0, var1);
   }

   public static Document getDocument(InputSource var0) throws IOException {
      return disableWLSDomImpl ? getDefaultDocumentImpl(var0) : getWebLogicDocumentImpl(var0);
   }

   private static Document getWebLogicDocumentImpl(InputSource var0) throws IOException {
      InputStream var1 = var0.getByteStream();
      if (var1 != null) {
         return Loader.load(var1);
      } else if (var0.getCharacterStream() != null) {
         return Loader.load(var0.getCharacterStream());
      } else {
         String var2 = var0.getSystemId();
         return !StringUtil.isEmpty(var2) ? getWebLogicDocumentImpl((TransportInfo)null, var2) : null;
      }
   }

   private static Document checkURLDocMap(String var0) {
      DocumentLookup var1 = (DocumentLookup)urlDocMap.get();
      return var1 != null ? var1.lookup(var0) : null;
   }

   private static Document getWebLogicDocumentImpl(TransportInfo var0, String var1) throws IOException {
      Document var2 = checkURLDocMap(var1);
      if (var2 != null) {
         return var2;
      } else {
         InputSource var3 = InputSourceUtil.loadURL(var0, var1);
         InputStream var4 = var3.getByteStream();

         Document var5;
         try {
            var5 = Loader.load(var4);
         } finally {
            if (var4 != null) {
               var4.close();
            }

         }

         return var5;
      }
   }

   public static Document getDefaultDocumentImpl(InputSource var0) throws IOException {
      DocumentBuilderFactory var1 = WebLogicDocumentBuilderFactory.newInstance();
      var1.setNamespaceAware(true);

      try {
         Document var2 = var1.newDocumentBuilder().parse(var0);
         return var2;
      } catch (RuntimeException var4) {
         throw var4;
      } catch (Exception var5) {
         throw new IOException("Failed to create XML document from InputSource due to -- " + var5);
      }
   }

   public static Document getDefaultDocumentImpl(String var0) throws IOException {
      DocumentBuilderFactory var1 = WebLogicDocumentBuilderFactory.newInstance();
      var1.setNamespaceAware(true);
      Document var2 = null;

      try {
         if (!StringUtil.isEmpty(var0) && var0.startsWith("jar:")) {
            var2 = loadWSDL(var0, var1);
         } else {
            var2 = var1.newDocumentBuilder().parse(var0);
         }

         return var2;
      } catch (RuntimeException var4) {
         throw var4;
      } catch (Exception var5) {
         throw new IOException("Failed to create XML document from URL '" + var0 + "' due to -- " + var5);
      }
   }

   private static Document loadWSDL(String var0, DocumentBuilderFactory var1) throws Exception {
      InputSource var2 = InputSourceUtil.loadURL(var0);
      InputStream var3 = var2.getByteStream();

      Document var4;
      try {
         var4 = var1.newDocumentBuilder().parse(var3);
      } finally {
         if (var3 != null) {
            var3.close();
         }

      }

      return var4;
   }
}
