package weblogic.xml.process;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class Node {
   private static final boolean debug = false;
   private static final boolean verbose = false;
   public static final String ELEMENT_NAME_SEPARATOR = ".";
   public static final String TEXT_NODE_TAG_NAME = "#text";
   public static final short ELEMENT_NODE = 1;
   public static final short TEXT_NODE = 2;
   protected String name;
   protected short type;
   protected String path;
   protected final Map attributes;
   protected StringBuffer value;
   protected Node parent;
   protected final List children;
   protected int level;

   public Node(String var1) throws XMLProcessingException {
      this.attributes = new HashMap();
      this.value = new StringBuffer();
      this.children = new LinkedList();
      this.level = 0;
      this.name = var1;
      this.path = "." + var1 + ".";
      this.parent = null;
      if (var1.equals("#text")) {
         this.type = 2;
      } else {
         this.type = 1;
      }

   }

   protected Node(Node var1, String var2) throws XMLProcessingException {
      this(var2);
      if (var1 != null) {
         this.parent = var1;
         this.path = this.parent.getPath() + var2 + ".";
         this.parent.children.add(this);
         this.level = this.parent.level + 1;
      }

   }

   public Node release() throws XMLProcessingException {
      boolean var1 = true;
      if (this.parent != null) {
         var1 = this.parent.children.remove(this);
      }

      return this.parent;
   }

   public void appendValue(char[] var1, int var2, int var3) {
      this.value.append(var1, var2, var3);
   }

   public void setAttribute(String var1, String var2) {
      this.attributes.put(var1, var2);
   }

   public Collection getAttributeNames() {
      return this.attributes.keySet();
   }

   public String getAttribute(String var1) {
      return (String)this.attributes.get(var1);
   }

   public String getName() {
      return this.name;
   }

   public String getPath() {
      return this.path;
   }

   public String getValue() {
      return this.value.toString().trim();
   }

   public void setValue(String var1) {
      this.value = new StringBuffer(var1 != null ? var1 : "");
   }

   public int getDepth() {
      return this.level;
   }

   public boolean isText() {
      return this.type == 2;
   }

   public List getChildNodes() {
      return this.children;
   }

   public Node getParent() {
      return this.parent;
   }
}
