package weblogic.xml.security.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import weblogic.xml.security.wsse.v200207.WSSEConstants;
import weblogic.xml.security.wsu.v200207.WSUConstants;
import weblogic.xml.stream.Attribute;
import weblogic.xml.stream.AttributeIterator;
import weblogic.xml.stream.EndElement;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLName;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

public class NamespaceAwareXOS extends weblogic.xml.babel.stream.XMLOutputStreamBase implements NSOutputStream {
   private static final int INITIAL_STACK_SIZE = 32;
   private static final String XMLNS = "xmlns";
   private static final Map DEFAULT_PREFIX_MAP = new HashMap(5);
   private static final String DEFAULT_PREFIX = "ns";
   private XMLOutputStream dest;
   private int nestingLevel;
   private final Map nsMap;
   private int[] nesting;
   private String[] ns;
   private int sp;
   private Map prefixMap;
   private int prefixCount;
   private StartElement lastStart;
   private MutableStart mutableStart;

   public NamespaceAwareXOS(XMLOutputStream var1) {
      this.nestingLevel = -1;
      this.nsMap = new HashMap();
      this.nesting = new int[32];
      this.ns = new String[32];
      this.sp = 0;
      this.prefixMap = new HashMap();
      this.prefixCount = 1;
      this.dest = var1;
      this.prefixMap.putAll(DEFAULT_PREFIX_MAP);
   }

   public NamespaceAwareXOS(XMLOutputStream var1, Map var2) {
      this(var1);
      this.nsMap.putAll(this.reverse(var2));
   }

   public void add(XMLEvent var1) throws XMLStreamException {
      if (!this.cacheEmpty()) {
         super.add(this.clearCache());
      }

      switch (var1.getType()) {
         case 2:
            StartElement var2 = (StartElement)var1;
            this.openScope(var2);
            this.cache(var2);
            break;
         case 4:
            super.add(var1);
            this.closeScope();
            break;
         default:
            super.add(var1);
      }

   }

   public void add(Attribute var1) throws XMLStreamException {
      if (this.cacheEmpty()) {
         throw new AssertionError("Last event was not a StartElement event -- cannot write attribute");
      } else {
         if (this.mutableStart == null) {
            this.lastStart = this.mutableStart = this.getModifiableStart(this.lastStart);
         }

         this.mutableStart.addAttribute(var1);
      }
   }

   private void cache(StartElement var1) {
      this.lastStart = var1;
      this.mutableStart = null;
   }

   private XMLEvent clearCache() {
      StartElement var1 = this.lastStart;
      this.lastStart = this.mutableStart = null;
      return var1;
   }

   private boolean cacheEmpty() {
      return this.lastStart == null;
   }

   protected void addXMLEvent(XMLEvent var1) throws XMLStreamException {
      Object var2;
      switch (var1.getType()) {
         case 2:
            if (!(var1 instanceof MutableStart)) {
               var2 = var1;
            } else {
               MutableStart var8 = (MutableStart)var1;
               XMLName var4 = var1.getName();
               var2 = var8;
               if (!var8.attributesNamespaced()) {
                  AttributeIterator var5 = var8.getAttributes();
                  ArrayList var6 = new ArrayList();

                  while(var5.hasNext()) {
                     Attribute var7 = this.fixAttributeNamespace(var5.next(), var8, var4);
                     var6.add(var7);
                  }

                  var8.setAttributes(var6);
                  var8.markAttributesNamespaced();
               }

               if (!var8.namespaced()) {
                  var8.setName(this.fillPrefix(var4, var8));
                  var8.markNamespaced();
               }
            }
            break;
         case 4:
            var2 = var1;
            if (var1 instanceof MutableEnd) {
               MutableEnd var3 = (MutableEnd)var1;
               if (!var3.namespaced()) {
                  var3.setName(this.fillPrefix(var3.getName(), (MutableStart)null));
                  var3.markNamespaced();
               }
            }
            break;
         default:
            var2 = var1;
      }

      this.dest.add((XMLEvent)var2);
   }

   private Attribute fixAttributeNamespace(Attribute var1, MutableStart var2, XMLName var3) {
      if (var1 instanceof QNameAttribute) {
         QNameAttribute var4 = (QNameAttribute)var1;
         var4.setQNameValue(this.fillPrefix(var4.getQNameValue(), var2));
      }

      XMLName var6 = var1.getName();
      String var5 = var6.getNamespaceUri();
      if (var5 != null) {
         var6 = this.fillPrefix(var6, var2);
         if (var1 instanceof MutableAttribute) {
            ((MutableAttribute)var1).setName(var6);
         }
      }

      return var1;
   }

   private MutableEnd getMutableEnd(EndElement var1) {
      return var1 instanceof MutableEnd ? (MutableEnd)var1 : new MutableEnd(var1);
   }

   private XMLName fillPrefix(XMLName var1, MutableStart var2) {
      String var3 = var1.getNamespaceUri();
      XMLName var4 = var1;
      if (var3 != null) {
         String var5 = var1.getPrefix();
         if (var5 != null) {
            String var6 = (String)this.nsMap.get(var3);
            if (var6 == null) {
               this.addNamespace(var2, var5, var3);
            } else if (!var6.equals(var5)) {
               var4 = fillInPrefix(var1, var6);
            } else {
               this.bindInScope(var3, var5);
            }
         } else {
            var5 = (String)this.nsMap.get(var3);
            if (var5 == null) {
               var5 = (String)this.prefixMap.get(var3);
               if (var5 == null) {
                  var5 = this.generatePrefix();
               }

               this.addNamespace(var2, var5, var3);
            } else {
               this.bindInScope(var3, var5);
            }

            if (!"xmlns".equals(var5)) {
               var4 = fillInPrefix(var1, var5);
            }
         }
      }

      return var4;
   }

   private void addNamespace(MutableStart var1, String var2, String var3) {
      if (var1 == null) {
         throw new AssertionError("StartElement cannot be null");
      } else {
         var1.addNamespace(ElementFactory.createNamespaceAttribute(var2, var3));
         this.bindInScope(var3, var2);
      }
   }

   private MutableStart getModifiableStart(StartElement var1) {
      return var1 instanceof MutableStart ? (MutableStart)var1 : new MutableStart(var1);
   }

   private static XMLName fillInPrefix(XMLName var0, String var1) {
      PrefixableName var2;
      if (var0 instanceof PrefixableName) {
         var2 = (PrefixableName)var0;
      } else {
         var2 = new PrefixableName(var0);
      }

      var2.setPrefix(var1);
      return var2;
   }

   public void addPrefix(String var1, String var2) {
      this.prefixMap.put(var1, var2);
   }

   public Map getNamespaces() {
      return this.reverse(this.nsMap);
   }

   private Map reverse(Map var1) {
      HashMap var2 = new HashMap();

      Object var4;
      String var5;
      for(Iterator var3 = var1.keySet().iterator(); var3.hasNext(); var2.put(var5, var4)) {
         var4 = var3.next();
         var5 = (String)var1.get(var4);
         if ("xmlns".equals(var5)) {
            var5 = "";
         }
      }

      return var2;
   }

   private void openScope(StartElement var1) {
      ++this.nestingLevel;
      AttributeIterator var2 = var1.getNamespaces();

      while(var2.hasNext()) {
         Attribute var3 = var2.next();
         this.bindInScope(var3.getValue(), var3.getName().getLocalName());
      }

   }

   private void closeScope() {
      if (this.nestingLevel < 0) {
         throw new AssertionError("Attempted to close scope, but none are opend");
      } else {
         String var1;
         while((var1 = this.popNS(this.nestingLevel)) != null) {
            this.nsMap.remove(var1);
         }

         --this.nestingLevel;
      }
   }

   private void bindInScope(String var1, String var2) {
      if (this.nestingLevel < 0) {
         throw new AssertionError("Attempting to add namespace without an open scope");
      } else {
         String var3 = (String)this.nsMap.put(var1, var2);
         if (var3 == null) {
            this.pushNS(this.nestingLevel, var1);
         }

      }
   }

   private void pushNS(int var1, String var2) {
      if (this.sp >= this.nesting.length - 1) {
         int var3 = this.nesting.length * 2;
         int[] var4 = new int[var3];
         String[] var5 = new String[var3];
         System.arraycopy(this.nesting, 0, var4, 0, this.nesting.length);
         System.arraycopy(this.ns, 0, var5, 0, this.ns.length);
         this.nesting = var4;
         this.ns = var5;
      }

      this.nesting[this.sp] = var1;
      this.ns[this.sp++] = var2;
   }

   private String popNS(int var1) {
      if (this.sp == 0) {
         return null;
      } else {
         return this.nesting[this.sp - 1] == var1 ? this.ns[--this.sp] : null;
      }
   }

   private String generatePrefix() {
      return "ns" + this.prefixCount++;
   }

   public void flush() throws XMLStreamException {
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

   static {
      DEFAULT_PREFIX_MAP.put(WSSEConstants.WSSE_URI, "wsse");
      DEFAULT_PREFIX_MAP.put(WSUConstants.WSU_URI, "wsu");
      DEFAULT_PREFIX_MAP.put("http://www.w3.org/2000/09/xmldsig#", "dsig");
      DEFAULT_PREFIX_MAP.put("http://www.w3.org/2001/04/xmlenc#", "xenc");
      DEFAULT_PREFIX_MAP.put("http://www.w3.org/2001/10/xml-exc-c14n#", "c14n");
   }
}
