package weblogic.xml.security.signature;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import weblogic.xml.babel.stream.CanonicalWriter;
import weblogic.xml.babel.stream.ExclusiveCanonicalWriter;
import weblogic.xml.babel.stream.XMLOutputStreamBase;
import weblogic.xml.babel.stream.XMLWriter;
import weblogic.xml.stream.XMLOutputStream;

class CanonicalizationMethodW3C extends CanonicalizationMethod implements CanonicalizationMethodFactory, DSIGConstants {
   public static final String URI = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315";
   public static final String URI_WC = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments";
   public static final String URI_EXC = "http://www.w3.org/2001/10/xml-exc-c14n#";
   private final String uri;
   private final boolean withComments;
   private final boolean exclusive;

   private CanonicalizationMethodW3C(String var1, boolean var2, boolean var3) {
      this.uri = var1;
      this.withComments = var2;
      this.exclusive = var3;
   }

   public String getURI() {
      return this.uri;
   }

   static void init() {
      CanonicalizationMethod.register(new CanonicalizationMethodW3C("http://www.w3.org/TR/2001/REC-xml-c14n-20010315", false, false));
      CanonicalizationMethod.register(new CanonicalizationMethodW3C("http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments", true, false));
      CanonicalizationMethod.register(new CanonicalizationMethodW3C("http://www.w3.org/2001/10/xml-exc-c14n#", false, true));
   }

   public CanonicalizationMethod newCanonicalizationMethod() {
      return this;
   }

   public XMLOutputStream canonicalize(OutputStream var1, Map var2) {
      OutputStreamWriter var3;
      try {
         var3 = new OutputStreamWriter(var1, "UTF-8");
      } catch (UnsupportedEncodingException var5) {
         throw new AssertionError(var5);
      }

      Object var4;
      if (this.exclusive) {
         var4 = new ExclusiveCanonicalWriter(var3, this.getInclusiveNamesspacesPrefixList());
      } else {
         var4 = new CanonicalWriter(var3, var2);
         if (this.withComments) {
            ((CanonicalWriter)var4).setWriteComments(this.withComments);
         }
      }

      return new XMLOutputStreamBase((XMLWriter)var4);
   }
}
