package weblogic.xml.security.transforms;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import weblogic.xml.security.utils.Observer;
import weblogic.xml.security.utils.ScopedNamespaceContext;
import weblogic.xml.stream.Attribute;
import weblogic.xml.stream.AttributeIterator;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLName;
import weblogic.xml.stream.XMLStreamException;

public class ExcC14NSignObserver implements Observer {
   private int nesting = 0;
   private final ExcC14NTransform c14n;
   private final ScopedNamespaceContext context;
   private final ScopedNamespaceContext visiblyUsed;
   private final Map nonVisiblyUsed;
   private Map parentNamespaces;
   private static final String XMLNS = "xmlns";
   private static final String PREFIX_SEP = ":";
   private static final String DEFAULT_NS_PREFIX = "";

   public ExcC14NSignObserver(ExcC14NTransform var1) {
      this.c14n = var1;
      this.nonVisiblyUsed = new HashMap();
      this.context = new ScopedNamespaceContext();
      this.visiblyUsed = new ScopedNamespaceContext();
   }

   public boolean observe(XMLEvent var1) throws XMLStreamException {
      boolean var2 = true;
      switch (var1.getType()) {
         case 2:
            StartElement var3 = (StartElement)var1;
            if (this.nesting == 0) {
               this.initNamespaces(var3);
            }

            this.openScope(var3);
            this.checkAttributes(var3.getAttributes());
            break;
         case 4:
            var2 = this.closeScope();
            if (!var2) {
               this.c14n.setInclusiveNamespaceMap(this.nonVisiblyUsed.isEmpty() ? null : this.nonVisiblyUsed);
            }
         case 16:
      }

      return var2;
   }

   private boolean closeScope() {
      --this.nesting;
      this.visiblyUsed.closeScope();
      this.context.closeScope();
      return this.nesting > 0;
   }

   private void openScope(StartElement var1) {
      ++this.nesting;
      this.context.openScope();
      this.visiblyUsed.openScope();
      this.addVisiblyUsed(var1.getName());
      AttributeIterator var2 = var1.getAttributes();

      while(var2.hasNext()) {
         Attribute var3 = var2.next();
         this.addVisiblyUsed(var3.getName());
      }

      AttributeIterator var7 = var1.getNamespaces();

      while(var7.hasNext()) {
         Attribute var4 = var7.next();
         XMLName var5 = var4.getName();
         String var6 = var5.getLocalName();
         if ("xmlns".equals(var6)) {
            this.context.bindDefaultNamespace(var4.getValue());
         } else {
            this.context.bindNamespace(var6, var4.getValue());
         }
      }

   }

   private void addVisiblyUsed(XMLName var1) {
      String var2 = var1.getPrefix();
      if (var2 != null) {
         String var3 = this.visiblyUsed.getNamespaceURI(var2);
         if (var3 == null) {
            if ("".equals(var2)) {
               this.visiblyUsed.bindDefaultNamespace(var1.getNamespaceUri());
            } else {
               this.visiblyUsed.bindNamespace(var2, var1.getNamespaceUri());
            }
         }
      }

   }

   private void initNamespaces(StartElement var1) {
      Map var2 = var1.getNamespaceMap();
      if (var2 != null) {
         this.parentNamespaces = new HashMap();
         this.parentNamespaces.putAll(var2);
      } else {
         this.parentNamespaces = Collections.EMPTY_MAP;
      }

      this.context.openScope();
      this.visiblyUsed.openScope();
      Set var3 = this.parentNamespaces.entrySet();
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         Map.Entry var5 = (Map.Entry)var4.next();
         String var6 = (String)var5.getKey();
         if ("".equals(var6)) {
            this.context.bindDefaultNamespace((String)var5.getValue());
         } else {
            this.context.bindNamespace(var6, (String)var5.getValue());
         }
      }

   }

   private void checkAttributes(AttributeIterator var1) {
      while(var1.hasNext()) {
         Attribute var2 = var1.next();
         XMLName var3 = var2.getName();
         if (ExcC14NTransform.QNAME_VALUE_ATTRIBUTES.contains(var3)) {
            this.addNonVisiblyUsed(parsePrefix(var2.getValue()));
         }
      }

   }

   private void addNonVisiblyUsed(String var1) {
      if (var1 != null) {
         String var2 = this.context.getNamespaceURI(var1);
         if (var2 != null) {
            this.nonVisiblyUsed.put(var1, var2);
         }
      }

   }

   private static String parsePrefix(String var0) {
      int var1 = var0.indexOf(":");
      if (var1 < 0) {
         return null;
      } else {
         return var1 == 0 ? "" : var0.substring(0, var1);
      }
   }

   public boolean consumes() {
      return false;
   }
}
