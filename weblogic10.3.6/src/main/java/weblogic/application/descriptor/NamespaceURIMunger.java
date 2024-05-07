package weblogic.application.descriptor;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.util.StreamReaderDelegate;

public class NamespaceURIMunger extends StreamReaderDelegate {
   protected static final XMLInputFactory xiFactory = XMLInputFactory.newInstance();
   private Set<String> oldNamespaceURISet = new HashSet();
   private String newNamespaceURI = null;

   public NamespaceURIMunger(InputStream var1, String var2, String[] var3) throws XMLStreamException {
      super(xiFactory.createXMLStreamReader(var1));
      this.newNamespaceURI = var2;
      String[] var4 = var3;
      int var5 = var3.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         String var7 = var4[var6];
         this.oldNamespaceURISet.add(var7);
      }

   }

   public NamespaceContext getNamespaceContext() {
      NamespaceContext var1 = super.getNamespaceContext();
      return var1 == null ? null : new NamespaceContextWrapper(var1);
   }

   public String getNamespaceURI() {
      return this.mungeNamespaceURI(super.getNamespaceURI());
   }

   public String getNamespaceURI(int var1) {
      return this.mungeNamespaceURI(super.getNamespaceURI(var1));
   }

   public String getNamespaceURI(String var1) {
      return this.mungeNamespaceURI(super.getNamespaceURI(var1));
   }

   private String mungeNamespaceURI(String var1) {
      return this.oldNamespaceURISet.contains(var1) ? this.newNamespaceURI : var1;
   }

   private class NamespaceContextWrapper implements NamespaceContext {
      private NamespaceContext delegate;

      public NamespaceContextWrapper(NamespaceContext var2) {
         this.delegate = var2;
      }

      public String getNamespaceURI(String var1) {
         return NamespaceURIMunger.this.mungeNamespaceURI(this.delegate.getNamespaceURI(var1));
      }

      public String getPrefix(String var1) {
         throw new UnsupportedOperationException();
      }

      public Iterator getPrefixes(String var1) {
         throw new UnsupportedOperationException();
      }
   }
}
