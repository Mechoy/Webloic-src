package weblogic.xml.dom;

import java.util.Iterator;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import weblogic.utils.collections.Iterators;

public final class AttributeMap implements NamedNodeMap {
   private Element owner;
   private int length;
   private String[] data;
   private Attr[] attData;
   private static final int WIDTH = 4;
   private static final int LOCAL_NAME = 0;
   private static final int PREFIX = 1;
   private static final int NAMESPACE_URI = 2;
   private static final int VALUE = 3;
   private int[] attributes = new int[1];
   private int[] namespaces = new int[1];
   private int numAttr = 0;
   private int numNS = 0;
   private boolean dirty = true;

   public AttributeMap() {
      this.length = 0;
      this.data = new String[0];
      this.attData = new Attr[0];
   }

   public AttributeMap(int var1) {
      this.length = var1;
      this.data = new String[this.length * 4];
      this.attData = new Attr[this.length];
   }

   public Element getOwner() {
      return this.owner;
   }

   public void setOwner(Element var1) {
      this.owner = var1;
   }

   public int length() {
      return this.length;
   }

   public String getValue(String var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("localName may not be null");
      } else {
         for(int var2 = 0; var2 < this.length; ++var2) {
            if (var1.equals(this.getLocalName(var2))) {
               return this.getValue(var2);
            }
         }

         return null;
      }
   }

   public String getValue(String var1, String var2) {
      if (var2 == null) {
         throw new IllegalArgumentException("localName may not be null");
      } else if (var1 == null) {
         return this.getValue(var2);
      } else {
         for(int var3 = 0; var3 < this.length; ++var3) {
            if (var2.equals(this.getLocalName(var3)) && var1.equals(this.getNamespaceURI(var3))) {
               return this.getValue(var3);
            }
         }

         return null;
      }
   }

   public void setLocalName(int var1, String var2) {
      this.dirty = true;
      this.data[var1 * 4 + 0] = var2;
   }

   public String getLocalName(int var1) {
      return this.data[var1 * 4 + 0];
   }

   public void setPrefix(int var1, String var2) {
      this.dirty = true;
      this.data[var1 * 4 + 1] = var2;
   }

   public String getPrefix(int var1) {
      return this.data[var1 * 4 + 1];
   }

   public void setNamespaceURI(int var1, String var2) {
      this.dirty = true;
      this.data[var1 * 4 + 2] = var2;
   }

   public String getNamespaceURI(int var1) {
      return this.data[var1 * 4 + 2];
   }

   public void setValue(int var1, String var2) {
      this.data[var1 * 4 + 3] = var2;
   }

   public String getValue(int var1) {
      return this.data[var1 * 4 + 3];
   }

   public int addAttribute(String var1, String var2, String var3, String var4) {
      this.ensureCapacity(this.length + 1);
      this.setAttribute(this.length, var1, var2, var3, var4);
      ++this.length;
      return this.length - 1;
   }

   public void setAttribute(int var1, String var2, String var3, String var4, String var5) {
      this.setNamespaceURI(var1, var2);
      this.setLocalName(var1, var3);
      this.setPrefix(var1, var4);
      this.setValue(var1, var5);
      this.dirty = true;
   }

   public int getAttributeIndex(String var1) {
      for(int var2 = 0; var2 < this.length; ++var2) {
         if (var1.equals(this.getLocalName(var2))) {
            return var2;
         }
      }

      return -1;
   }

   public int getNamespaceIndex(String var1) {
      return var1 == null ? -1 : this.getAttributeIndex(ElementNode.XMLNS_URI, var1);
   }

   public int getAttributeIndexByPrefix(String var1, String var2) {
      if (var1 == null) {
         return this.getAttributeIndex(var2);
      } else {
         for(int var3 = 0; var3 < this.length; ++var3) {
            if (var2.equals(this.getLocalName(var3)) && var1.equals(this.getPrefix(var3))) {
               return var3;
            }
         }

         return -1;
      }
   }

   public int getAttributeIndex(String var1, String var2) {
      if (var1 == null) {
         return this.getAttributeIndex(var2);
      } else {
         for(int var3 = 0; var3 < this.length; ++var3) {
            if (var2.equals(this.getLocalName(var3)) && var1.equals(this.getNamespaceURI(var3))) {
               return var3;
            }
         }

         return -1;
      }
   }

   public Attr getAttribute(int var1) {
      if (this.attData[var1] == null) {
         this.attData[var1] = new AttributeReference(this, var1);
      }

      return this.attData[var1];
   }

   public void removeAttribute(int var1) {
      if (var1 < this.length - 1) {
         System.arraycopy(this.data, (var1 + 1) * 4, this.data, var1 * 4, (this.length - var1 - 1) * 4);
         System.arraycopy(this.attData, var1 + 1, this.attData, var1, this.length - var1 - 1);
      }

      this.attData[this.length - 1] = null;
      var1 = (this.length - 1) * 4;
      this.data[var1++] = null;
      this.data[var1++] = null;
      this.data[var1++] = null;
      this.data[var1] = null;
      --this.length;
   }

   public void clear() {
      if (this.data != null) {
         for(int var1 = 0; var1 < this.length * 4; ++var1) {
            this.data[var1] = null;
            this.attData[var1] = null;
         }
      }

      this.length = 0;
   }

   private void ensureCapacity(int var1) {
      if (this.attData.length < var1) {
         int var2;
         for(var2 = this.attData.length; var2 < var1; var2 = var2 * 2 + 1) {
         }

         String[] var3 = new String[var2 * 4];
         Attr[] var4 = new Attr[var2];
         if (this.length > 0) {
            System.arraycopy(this.data, 0, var3, 0, this.length * 4);
            System.arraycopy(this.attData, 0, var4, 0, this.length);
         }

         this.data = var3;
         this.attData = var4;
      }
   }

   public int getLength() {
      return this.length;
   }

   public Node getNamedItem(String var1) {
      int var2 = this.getAttributeIndex(var1);
      return var2 == -1 ? null : this.getAttribute(var2);
   }

   public Node getNamedItemNS(String var1, String var2) {
      int var3 = this.getAttributeIndex(var1, var2);
      return var3 == -1 ? null : this.getAttribute(var3);
   }

   public Node item(int var1) {
      return this.getAttribute(var1);
   }

   public Node removeNamedItem(String var1) {
      int var2 = this.getAttributeIndex(var1);
      if (var2 == -1) {
         return null;
      } else {
         Attr var3 = (Attr)this.getAttribute(var2).cloneNode(false);
         this.removeAttribute(var2);
         return var3;
      }
   }

   public Node removeNamedItemNS(String var1, String var2) {
      int var3 = this.getAttributeIndex(var1, var2);
      if (var3 == -1) {
         return null;
      } else {
         Attr var4 = (Attr)this.getAttribute(var3).cloneNode(false);
         this.removeAttribute(var3);
         return var4;
      }
   }

   public Node setNamedItem(Node var1) {
      AttributeReference var2 = (AttributeReference)this.getNamedItemNS(var1.getNamespaceURI(), var1.getLocalName());
      Attr var3 = (Attr)var1;
      if (var2 == null) {
         int var4 = this.addAttribute(var3.getNamespaceURI(), var3.getLocalName(), var3.getPrefix(), var3.getValue());
         return this.getAttribute(var4);
      } else {
         this.setAttribute(var2.getIndex(), var3.getNamespaceURI(), var3.getLocalName(), var3.getPrefix(), var3.getValue());
         return var2;
      }
   }

   public Node setNamedItemNS(Node var1) {
      return this.setNamedItem(var1);
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      if (this.length > 0) {
         var1.append(" ");
      }

      for(int var2 = 0; var2 < this.length; ++var2) {
         String var3 = this.getNamespaceURI(var2);
         String var4 = this.getPrefix(var2);
         if (var3 != null) {
            var1.append("['" + this.getNamespaceURI(var2) + "']:");
         }

         if (var4 != null) {
            var1.append(this.getPrefix(var2) + ":");
         }

         var1.append(this.getLocalName(var2) + "='" + this.getValue(var2) + "' ");
      }

      return var1.toString();
   }

   private void setAttributeCapacity(int var1) {
      this.numAttr = 0;
      if (var1 >= this.attributes.length) {
         this.attributes = new int[var1];
      }
   }

   private void setNamespaceCapacity(int var1) {
      this.numNS = 0;
      if (var1 >= this.namespaces.length) {
         this.namespaces = new int[var1];
      }
   }

   protected void initializeAttributesAndNamespaces() {
      this.setAttributeCapacity(this.getLength());
      this.setNamespaceCapacity(this.getLength());

      for(int var1 = 0; var1 < this.getLength(); ++var1) {
         if ("xmlns".equals(this.getPrefix(var1))) {
            this.addNamespace(var1);
         } else if ("xmlns".equals(this.getLocalName(var1))) {
            this.addNamespace(var1);
         } else {
            this.addAttribute(var1);
         }
      }

      this.dirty = false;
   }

   private void addNamespace(int var1) {
      this.namespaces[this.numNS] = var1;
      ++this.numNS;
   }

   private void addAttribute(int var1) {
      this.attributes[this.numAttr] = var1;
      ++this.numAttr;
   }

   public Iterator getNamespacePrefixes() {
      if (this.dirty) {
         this.initializeAttributesAndNamespaces();
      }

      return (Iterator)(this.numNS == 0 ? Iterators.EMPTY_ITERATOR : new PrefixIterator(this, this.namespaces, this.numNS));
   }

   public Iterator getAttributeNames() {
      if (this.dirty) {
         this.initializeAttributesAndNamespaces();
      }

      return (Iterator)(this.numAttr == 0 ? Iterators.EMPTY_ITERATOR : new NameIterator(this, this.attributes, this.numAttr));
   }

   public static void main(String[] var0) throws Exception {
      AttributeMap var1 = new AttributeMap(5);
      var1.setAttribute(0, "a_uri", "a", "pa", "apple");
      var1.setAttribute(1, "b_uri", "b", "pb", "banana");
      var1.setAttribute(2, "c_uri", "c", "pc", "orange");
      var1.setAttribute(3, "d_uri", "d", "pd", "dunce");
      var1.setAttribute(4, "e_uri", "e", "pd", "eat");
      System.out.println(var1);
      int var2 = var1.length();

      for(int var3 = 0; var3 < var2; ++var3) {
         var1.removeAttribute(0);
         System.out.println("--------------" + var1.length() + "-----------");
         System.out.println(var1);
      }

      var1.addAttribute("a_uri", "a", "pa", "apple");
      var1.addAttribute("b_uri", "b", "pb", "banana");
      var1.addAttribute("c_uri", "c", "pc", "orange");
      var1.addAttribute("d_uri", "d", "pd", "dunce");
      var1.addAttribute("e_uri", "e", "pd", "eat");
      System.out.println(var1);
   }

   public class PrefixIterator implements Iterator {
      private AttributeMap map;
      private int[] data;
      private int size;
      private int current;

      public PrefixIterator(AttributeMap var2, int[] var3, int var4) {
         this.map = var2;
         this.data = var3;
         this.size = var4;
         this.current = 0;
      }

      public Object next() {
         String var1 = this.map.getPrefix(this.data[this.current]);
         ++this.current;
         return var1;
      }

      public void remove() {
         throw new UnsupportedOperationException("remove() not supported");
      }

      public boolean hasNext() {
         return this.current < this.size;
      }
   }

   public class NameIterator implements Iterator {
      private AttributeMap map;
      private int[] data;
      private int size;
      private int current;

      public NameIterator(AttributeMap var2, int[] var3, int var4) {
         this.map = var2;
         this.data = var3;
         this.size = var4;
         this.current = 0;
      }

      public Object next() {
         Node var1 = this.map.item(this.data[this.current]);
         ++this.current;
         return var1;
      }

      public void remove() {
         throw new UnsupportedOperationException("remove() not supported");
      }

      public boolean hasNext() {
         return this.current < this.size;
      }
   }
}
