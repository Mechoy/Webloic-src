package weblogic.wsee.bind.types;

import com.bea.xml.XmlException;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import weblogic.utils.CharsetMap;
import weblogic.utils.io.XMLDeclaration;
import weblogic.wsee.util.ToStringWriter;

public class SourceTypeMapper extends AttachmentBase {
   protected static final URL DUMMY_URL;
   private static final int STREAM_BUFFER_SIZE = 256;
   private static final int MARK_READLIMIT = 128;
   private static final String DEFAULT_ENCODING = "utf-8";

   AttachmentPart createAttachmentPart(String var1, SOAPMessage var2, Object var3, String var4) throws XmlException {
      Source var5 = (Source)var3;
      if (var5 instanceof StreamSource) {
         StreamSource var7 = (StreamSource)var5;
         AttachmentPart var6 = this.createAttachmentFromStreamSource(var7, var1, var2);
         var6.setContentId(var1);
         var6.setMimeHeader("Content-Transfer-Encoding", "binary");
         return var6;
      } else {
         throw new XmlException("unsupported source " + var5);
      }
   }

   private AttachmentPart createAttachmentFromStreamSource(StreamSource var1, String var2, SOAPMessage var3) throws XmlException {
      Reader var4 = var1.getReader();
      if (var4 != null) {
         return this.createReaderAttachment(var4, var2, var3);
      } else {
         InputStream var5 = var1.getInputStream();
         if (var5 != null) {
            return this.createInputStreamAttachment(var5, var2, var3);
         } else {
            String var6 = var1.getSystemId();
            if (var6 != null) {
               InputStream var7 = this.getInputStreamFromSystemId(var6);
               return this.createInputStreamAttachment(var7, var2, var3);
            } else {
               throw new XmlException("no valid contents found in StreamSource: " + var1);
            }
         }
      }
   }

   private InputStream getInputStreamFromSystemId(String var1) throws XmlException {
      try {
         URL var2 = new URL(var1);
         InputStream var3 = var2.openStream();
         return var3;
      } catch (IOException var4) {
         throw new XmlException("error retreiving system id " + var1, var4);
      }
   }

   private AttachmentPart createReaderAttachment(Reader var1, String var2, SOAPMessage var3) throws XmlException {
      try {
         if (!((Reader)var1).markSupported()) {
            var1 = new BufferedReader((Reader)var1, 256);
         }

         ((Reader)var1).mark(128);
         XMLDeclaration var4 = new XMLDeclaration();
         var4.parse((Reader)var1);
         String var5 = var4.getEncoding();
         if (var5 == null) {
            var5 = "utf-8";
         }

         ((Reader)var1).reset();
         ReaderDataHandler var6 = new ReaderDataHandler(var2, (Reader)var1, var5);
         AttachmentPart var7 = var3.createAttachmentPart(var6);
         var7.setMimeHeader("Content-Type", createTextXmlContentType(var5));
         return var7;
      } catch (IOException var8) {
         throw new XmlException("error creating attachment " + var2, var8);
      }
   }

   private AttachmentPart createInputStreamAttachment(InputStream var1, String var2, SOAPMessage var3) throws XmlException {
      ISDataSource var4;
      try {
         var4 = new ISDataSource(var1, var2);
      } catch (IOException var6) {
         throw new XmlException("error creating attachment for " + var2, var6);
      }

      DataHandler var5 = new DataHandler(var4);
      return var3.createAttachmentPart(var5);
   }

   Object deserializeBase64Binary(String var1) throws XmlException {
      throw new XmlException(" deserializing SourceType from xs:base64Binary type not supported");
   }

   protected static String createTextXmlContentType(String var0) {
      return "text/xml; charset=\"" + var0 + "\"";
   }

   Object deserializeAttachmentPart(AttachmentPart var1) throws XmlException {
      try {
         Object var2 = var1.getContent();
         if (var2 instanceof StreamSource) {
            return (StreamSource)var2;
         } else if (var2 instanceof InputStream) {
            return new StreamSource((InputStream)var2);
         } else {
            throw new SOAPException("the Source content from the attachment is bad class type,the type should be StreamSource orInputStream. ");
         }
      } catch (SOAPException var3) {
         throw new XmlException("Failed to get Source content from the attachment", var3);
      }
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.end();
   }

   static {
      try {
         DUMMY_URL = new URL("file:/nowhere");
      } catch (MalformedURLException var1) {
         throw new AssertionError(var1);
      }
   }

   private static final class ISDataSource implements DataSource {
      private final InputStream inputStream;
      private final String name;
      private final String contentType;

      public ISDataSource(InputStream var1, String var2) throws IOException {
         if (var1.markSupported()) {
            this.inputStream = var1;
         } else {
            this.inputStream = new BufferedInputStream(var1, 256);
         }

         assert var1.markSupported();

         this.name = var2;
         this.contentType = extractContentType(this.inputStream);

         assert this.contentType != null;

      }

      private static String extractContentType(InputStream var0) throws IOException {
         var0.mark(128);
         XMLDeclaration var1 = new XMLDeclaration(var0);
         String var2 = var1.getEncoding();
         var0.reset();
         if (var2 == null) {
            var2 = "utf-8";
         }

         return SourceTypeMapper.createTextXmlContentType(var2);
      }

      public String getContentType() {
         return this.contentType;
      }

      public InputStream getInputStream() throws IOException {
         return this.inputStream;
      }

      public String getName() {
         return this.name;
      }

      public OutputStream getOutputStream() throws IOException {
         throw new UnsupportedOperationException();
      }
   }

   private static final class ReaderDataHandler extends DataHandler {
      private final String name;
      private final Reader reader;
      private final String encoding;

      public ReaderDataHandler(String var1, Reader var2, String var3) {
         super(SourceTypeMapper.DUMMY_URL);

         assert var2 != null;

         assert var3 != null;

         this.name = var1;
         this.reader = var2;
         this.encoding = var3;
      }

      public void writeTo(OutputStream var1) throws IOException {
         assert this.encoding != null;

         String var2 = CharsetMap.getJavaFromIANA(this.encoding);
         Charset var3 = Charset.forName(var2);
         OutputStreamWriter var4 = new OutputStreamWriter(var1, var3);
         char[] var5 = new char[256];
         boolean var6 = false;

         int var7;
         while(-1 != (var7 = this.reader.read(var5))) {
            var4.write(var5, 0, var7);
         }

         var4.close();
      }

      public String getName() {
         return this.name;
      }
   }
}
