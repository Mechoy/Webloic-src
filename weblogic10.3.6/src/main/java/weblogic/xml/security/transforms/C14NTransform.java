package weblogic.xml.security.transforms;

import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Map;
import weblogic.xml.babel.stream.CanonicalWriter;
import weblogic.xml.babel.stream.XMLOutputStreamBase;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

public class C14NTransform extends NodeTransform {
   protected OctetTransform dest;
   protected Map nsMap;
   protected final boolean withComments;

   /** @deprecated */
   public C14NTransform(boolean var1, boolean var2) {
      this(var2);
   }

   public C14NTransform(boolean var1) {
      this.nsMap = null;
      this.withComments = var1;
   }

   public void setNamespaces(Map var1) {
      this.nsMap = var1;
   }

   public Map getNamespaces() {
      Map var1;
      if (this.nsMap == null) {
         var1 = Collections.EMPTY_MAP;
      } else {
         var1 = this.nsMap;
      }

      return var1;
   }

   public XMLOutputStream getXMLOutputStream() throws XMLStreamException {
      OutputStreamWriter var1;
      try {
         var1 = new OutputStreamWriter(this.dest.getOutputStream(), "UTF-8");
      } catch (UnsupportedEncodingException var3) {
         throw new AssertionError(var3);
      }

      CanonicalWriter var2 = new CanonicalWriter(var1, this.nsMap);
      if (this.withComments) {
         ((CanonicalWriter)var2).setWriteComments(this.withComments);
      }

      return new XMLOutputStreamBase(var2);
   }

   public String getURI() {
      return this.withComments ? "http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments" : "http://www.w3.org/TR/2001/REC-xml-c14n-20010315";
   }

   public void setDest(OctetTransform var1) {
      this.dest = var1;
   }

   public void setDest(NodeTransform var1) throws IncompatibleTransformException {
      throw new IncompatibleTransformException("Cannot chain " + this + " to " + var1);
   }
}
