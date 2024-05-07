package weblogic.xml.security.utils;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import javax.xml.namespace.QName;

public abstract class AbstractXMLWriter implements XMLWriter {
   private ScopedNamespaceContext nsContext = new ScopedNamespaceContext();
   private Map defaultPrefixes;
   private int prefixCount;

   public AbstractXMLWriter() {
      this.defaultPrefixes = Collections.EMPTY_MAP;
      this.prefixCount = 0;
   }

   public abstract void writeAttribute(String var1, String var2, String var3) throws XMLWriterRuntimeException, IllegalStateException;

   public void writeAttribute(String var1, String var2, QName var3) throws XMLWriterRuntimeException, IllegalStateException {
      this.writeAttribute(var1, var2, this.convertQName(var3));
   }

   public abstract void writeCharacters(String var1) throws XMLWriterRuntimeException;

   public void writeCharacters(QName var1) throws XMLWriterRuntimeException, IllegalStateException {
      this.writeCharacters(this.convertQName(var1));
   }

   public void setPrefix(String var1, String var2) throws XMLWriterRuntimeException {
      this.bindNamespace(var1, var2);
   }

   public void setDefaultNamespace(String var1) throws XMLWriterRuntimeException {
      this.bindDefaultNamespace(var1);
   }

   public Map getNamespaceMap() {
      return this.nsContext.getNamespaceMap();
   }

   public void setNamespaceMap(Map var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("namespace map cannot be null");
      } else {
         Iterator var2 = var1.entrySet().iterator();

         while(var2.hasNext()) {
            Map.Entry var3 = (Map.Entry)var2.next();
            this.nsContext.bindNamespace((String)var3.getKey(), (String)var3.getValue());
         }

      }
   }

   public void setDefaultPrefixes(Map var1) {
      this.defaultPrefixes = var1;
   }

   private String convertQName(QName var1) throws XMLWriterRuntimeException {
      String var2 = var1.getNamespaceURI();
      if (var2 != null && !var2.equals("")) {
         String var3 = this.findOrBindNamespace(var2);
         String var4;
         if ("".equals(var3)) {
            var4 = var1.getLocalPart();
         } else {
            var4 = var3 + ":" + var1.getLocalPart();
         }

         return var4;
      } else {
         return var1.getLocalPart();
      }
   }

   protected final String findPrefix(String var1) {
      return this.nsContext.getPrefix(var1);
   }

   protected final void addNamespacePrefix(String var1, String var2) {
      this.nsContext.bindNamespace(var1, var2);
   }

   protected final void addDefaultNamespace(String var1) {
      this.nsContext.bindDefaultNamespace(var1);
   }

   protected final void openScope() {
      this.nsContext.openScope();
   }

   protected final void closeScope() {
      this.nsContext.closeScope();
   }

   protected final String generatePrefix(String var1) {
      String var2 = (String)this.defaultPrefixes.get(var1);
      return var2 != null ? var2 : "n" + this.prefixCount++;
   }

   protected abstract void bindNamespace(String var1, String var2) throws XMLWriterRuntimeException;

   protected abstract void bindDefaultNamespace(String var1) throws XMLWriterRuntimeException;

   protected final String findOrBindNamespace(String var1) throws XMLWriterRuntimeException {
      String var2 = this.findPrefix(var1);
      if (var2 == null) {
         var2 = this.generatePrefix(var1);
         this.bindNamespace(var2, var1);
      }

      return var2;
   }
}
