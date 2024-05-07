package weblogic.xml.security.encryption;

import java.util.Map;
import weblogic.xml.babel.stream.XMLOutputStreamBase;
import weblogic.xml.security.utils.NSOutputStream;
import weblogic.xml.security.utils.NamespaceAwareXOS;
import weblogic.xml.security.utils.XMLEventBuffer;
import weblogic.xml.stream.CharacterData;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLOutputStreamFactory;
import weblogic.xml.stream.XMLStreamException;

public class EncryptXMLOutputStream extends XMLOutputStreamBase implements XMLEncConstants, NSOutputStream {
   private NSOutputStream dest;
   private XMLOutputStream cipherTextStream;
   private EncryptedData encryptedData;
   private XMLEvent lastSpace;
   private int nesting;
   private int nodeCount;
   private String encoding;
   private final XMLEventBuffer debugBuffer;

   public EncryptXMLOutputStream(XMLOutputStream var1) {
      this(var1, "UTF-8");
   }

   public EncryptXMLOutputStream(XMLOutputStream var1, String var2) {
      this.debugBuffer = VERBOSE ? new XMLEventBuffer() : null;
      if (!(var1 instanceof NSOutputStream)) {
         this.dest = new NamespaceAwareXOS(var1);
      } else {
         this.dest = (NSOutputStream)var1;
      }

      this.encoding = var2;
      this.addPrefix("http://www.w3.org/2001/04/xmlenc#", "xenc");
   }

   public void beginEncrypt(EncryptedData var1) throws XMLStreamException {
      this.dest.flush();
      this.flush();
      this.encryptedData = var1;
      this.nesting = 0;
      this.nodeCount = 0;
      this.cipherTextStream = var1.getXMLOutputStream(this.encoding);
   }

   public void endEncrypt() throws XMLStreamException {
      if (VERBOSE) {
         System.out.println("<!-- -- Begin data to be encrypted ----->");
         XMLOutputStream var1 = XMLOutputStreamFactory.newInstance().newOutputStream(System.out);

         while(this.debugBuffer.hasNext()) {
            var1.add(this.debugBuffer.next());
         }

         var1.close(true);
         System.out.println("<!-----  End data to be encrypted  -- -->");
      }

      this.flush();
      this.cipherTextStream.flush();
      int var2 = this.calculateIndent();
      if (this.nodeCount <= 1 && !CONTENT_ONLY) {
         this.encryptedData.setType(1);
      } else {
         this.encryptedData.setType(2);
      }

      this.encryptedData.toXML(this.dest, "http://www.w3.org/2001/04/xmlenc#", var2);
      this.cipherTextStream = null;
   }

   private int calculateIndent() {
      if (this.lastSpace == null) {
         return 0;
      } else {
         String var1 = ((CharacterData)this.lastSpace).getContent();
         int var2 = var1.lastIndexOf(10);
         return var2 == -1 ? var1.length() : var1.length() - (var2 + 1);
      }
   }

   protected boolean writeXMLEvent(XMLEvent var1) throws XMLStreamException {
      if (this.cipherTextStream != null) {
         if (VERBOSE) {
            this.debugBuffer.add(var1);
         }

         switch (var1.getType()) {
            case 2:
               if (this.nesting == 0) {
                  ++this.nodeCount;
               }

               ++this.nesting;
               break;
            case 4:
               --this.nesting;
            case 128:
            case 512:
            case 1024:
            case 2048:
            case 4096:
               break;
            default:
               if (this.nesting == 0) {
                  ++this.nodeCount;
               }
         }

         this.cipherTextStream.add(var1);
      } else {
         this.dest.add(var1);
      }

      if (var1.isSpace()) {
         this.lastSpace = var1;
      }

      return true;
   }

   public void flush() throws XMLStreamException {
      this.lastStartElement = null;
      this.write();
      this.dest.flush();
   }

   public void close() throws XMLStreamException {
      this.flush();
      this.dest.close();
   }

   public void close(boolean var1) throws XMLStreamException {
      if (var1) {
         this.flush();
      }

      this.dest.close(var1);
   }

   public void addPrefix(String var1, String var2) {
      this.dest.addPrefix(var1, var2);
   }

   public Map getNamespaces() {
      return this.dest.getNamespaces();
   }
}
