package weblogic.xml.crypto.dsig;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import org.w3c.dom.Node;
import weblogic.xml.babel.stream.CanonicalWriter;
import weblogic.xml.babel.stream.ExclusiveCanonicalWriter;
import weblogic.xml.babel.stream.XMLWriter;
import weblogic.xml.crypto.api.Data;
import weblogic.xml.crypto.api.OctetStreamData;
import weblogic.xml.crypto.api.XMLCryptoContext;
import weblogic.xml.crypto.dsig.api.XMLSignatureException;
import weblogic.xml.crypto.utils.DOMUtils;
import weblogic.xml.crypto.utils.DataUtils;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLInputStreamFactory;
import weblogic.xml.stream.XMLStreamException;

public class C14NTransform extends TransformImpl {
   private boolean exclusive;
   private boolean withComments;

   public C14NTransform(boolean var1, boolean var2) {
      this.exclusive = var1;
      this.withComments = var2;
   }

   public String getAlgorithm() {
      if (this.exclusive) {
         return "http://www.w3.org/2001/10/xml-exc-c14n#";
      } else {
         return this.withComments ? "http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments" : "http://www.w3.org/TR/2001/REC-xml-c14n-20010315";
      }
   }

   public Data transform(Data var1, XMLCryptoContext var2) throws XMLSignatureException {
      Node var3 = DataUtils.getNode(var1);
      XMLInputStream var4 = null;
      ByteArrayOutputStream var5 = null;

      try {
         if (var3 != null) {
            var4 = XMLInputStreamFactory.newInstance().newInputStream(var3);
         } else {
            InputStream var6 = DataUtils.getInputStream(var1);
            if (var6 == null) {
               return new OctetStreamData(new ByteArrayInputStream(new byte[0]));
            }

            var4 = XMLInputStreamFactory.newInstance().newInputStream(DataUtils.getInputStream(var1));
         }

         var5 = new ByteArrayOutputStream();

         OutputStreamWriter var12;
         try {
            var12 = new OutputStreamWriter(var5, "UTF-8");
         } catch (UnsupportedEncodingException var10) {
            throw new AssertionError(var10);
         }

         Map var7 = DOMUtils.getNamespaceMap(var3);
         Object var8;
         if (this.exclusive) {
            var8 = new ExclusiveCanonicalWriter(var12);
         } else {
            var8 = new CanonicalWriter(var12, var7);
            if (this.withComments) {
               ((CanonicalWriter)var8).setWriteComments(this.withComments);
            }
         }

         while(var4.hasNext()) {
            XMLEvent var9 = var4.next();
            ((XMLWriter)var8).write(var9);
         }

         ((XMLWriter)var8).flush();
         return new OctetStreamData(new ByteArrayInputStream(var5.toByteArray()));
      } catch (XMLStreamException var11) {
         throw new XMLSignatureException("Could not create xml stream for " + this.getAlgorithm() + " transform.", var11);
      }
   }
}
