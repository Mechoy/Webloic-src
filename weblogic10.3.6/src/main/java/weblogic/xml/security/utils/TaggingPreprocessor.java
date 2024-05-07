package weblogic.xml.security.utils;

import java.util.Map;
import weblogic.xml.security.wsu.v200207.WSUConstants;
import weblogic.xml.stream.EndElement;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLName;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

public abstract class TaggingPreprocessor implements Preprocessor {
   private static final String[] WSU_DECL;
   private static final String[] DSIG_DECL;
   private static final String[] XENC_DECL;
   private static final String[][] ID_NAMESPACES;

   public final void begin(StartElement var1, XMLOutputStream var2) throws XMLStreamException {
      String[] var3 = getIdNamespace(var1);
      String var4 = getIdAttribute(var1, getNamespace(var3));
      Object var5;
      if (var4 != null) {
         var5 = var1;
      } else {
         var4 = Utils.generateId(var1.getName().getLocalName());
         MutableStart var6;
         if (var1 instanceof MutableStart) {
            var5 = var6 = (MutableStart)var1;
         } else {
            var5 = var6 = new MutableStart(var1);
            var6.markNamespaced();
         }

         String var7 = null;
         if (var2 instanceof NSOutputStream) {
            Map var8 = ((NSOutputStream)var2).getNamespaces();
            var7 = (String)var8.get(getNamespace(var3));
         }

         if (var7 == null) {
            var7 = getPrefix(var3);
         }

         XMLName var9 = ElementFactory.createXMLName(getNamespace(var3), "Id", var7);
         var6.addAttribute(ElementFactory.createAttribute(var9, var4));
      }

      this.begin((StartElement)var5, var2, var4);
   }

   private static final String getIdAttribute(StartElement var0, String var1) {
      String var2 = StreamUtils.getAttributeByName("Id", var1, var0);
      return var2;
   }

   protected abstract void begin(StartElement var1, XMLOutputStream var2, String var3) throws XMLStreamException;

   public abstract void end(EndElement var1, XMLOutputStream var2) throws XMLStreamException;

   private static String[] getIdNamespace(StartElement var0) {
      XMLName var1 = var0.getName();
      String var2 = var1.getNamespaceUri();

      for(int var4 = 0; var4 < ID_NAMESPACES.length; ++var4) {
         String[] var5 = ID_NAMESPACES[var4];
         if (getNamespace(var5).equals(var2)) {
            return var5;
         }
      }

      return WSU_DECL;
   }

   private static String getNamespace(String[] var0) {
      return var0[0];
   }

   private static String getPrefix(String[] var0) {
      return var0[1];
   }

   static {
      WSU_DECL = new String[]{WSUConstants.WSU_URI, "wsu"};
      DSIG_DECL = new String[]{"http://www.w3.org/2000/09/xmldsig#", "dsig"};
      XENC_DECL = new String[]{"http://www.w3.org/2001/04/xmlenc#", "xenc"};
      ID_NAMESPACES = new String[][]{DSIG_DECL, XENC_DECL};
   }
}
