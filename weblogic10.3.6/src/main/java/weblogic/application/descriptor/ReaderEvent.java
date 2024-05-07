package weblogic.application.descriptor;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import weblogic.descriptor.DescriptorException;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.utils.Debug;
import weblogic.utils.StringUtils;

public class ReaderEvent implements Munger.ReaderEventInfo {
   private static boolean debug = Debug.getCategory("weblogic.descriptor").isEnabled();
   private String localName;
   private boolean isKey;
   private boolean isKeyComponent;
   private char[] characters;
   private ReaderEvent parent;
   private boolean discard;
   private Stack children;
   private BasicMunger munger;
   private Location location;
   private ReaderEvent beanEvent;
   private AbstractDescriptorBean bean;
   private SchemaHelper helper;
   private int eventType;
   private int attributeCount;
   private String[] attributeNames;
   private String[] attributeValues;
   private String[] attributeUris;
   private String[] attributePrefixes;
   private String charEncodingScheme;
   private String inputEncoding;
   private int namespaceCount;
   private int operation;

   public ReaderEvent(int var1, Object var2, Location var3, BasicMunger var4) {
      this.isKey = false;
      this.isKeyComponent = false;
      this.discard = false;
      this.children = new Stack();
      this.operation = -1;
      this.eventType = var1;
      if (var2 != null) {
         if (var2 instanceof String) {
            this.localName = (String)var2;
         } else {
            this.characters = (char[])((char[])var2);
         }
      }

      this.munger = var4;
      this.location = var3 != null ? var3 : new Location() {
         public int getLineNumber() {
            return 0;
         }

         public int getColumnNumber() {
            return 0;
         }

         public int getCharacterOffset() {
            return 0;
         }

         public String getPublicId() {
            return "Start of document";
         }

         public String getSystemId() {
            return "Start of document";
         }
      };
   }

   private ReaderEvent(String var1, ReaderEvent var2, BasicMunger var3, Location var4) {
      this.isKey = false;
      this.isKeyComponent = false;
      this.discard = false;
      this.children = new Stack();
      this.operation = -1;
      this.localName = var1;
      this.parent = var2;
      this.munger = var3;
      this.location = var4;
   }

   public ReaderEvent(String var1, ReaderEvent var2, BasicMunger var3, Location var4, ReaderEvent var5) {
      this(var1, var2, var3, var4);
      this.beanEvent = var5;
      if (var1 != null && var5 != null && var5.getBean() != null) {
         this.isKey = var5.getBean()._isPropertyAKey(this);
         if (!this.isKey) {
            SchemaHelper var6 = var5.getBean()._getSchemaHelper2();
            this.isKeyComponent = var6.isKeyComponent(var6.getPropertyIndex(this.getElementName()));
         }
      }

   }

   public ReaderEvent(StringBuffer var1, ReaderEvent var2, String var3, int var4, BasicMunger var5, Location var6) {
      this.isKey = false;
      this.isKeyComponent = false;
      this.discard = false;
      this.children = new Stack();
      this.operation = -1;
      this.parent = var2;
      this.munger = var5;
      this.location = var6;
      this.eventType = 1;
      this.consumeElement(var1);
      if (var1.length() > 0 && var1.charAt(0) == '/') {
         this.children.push(new ReaderEvent(var1, this, var3, var4, var5, var6));
      } else {
         if (debug) {
            System.out.println("Adding " + var3 + " to " + this.localName);
         }

         this.setOperation(var4);
         if (var3 == null) {
            return;
         }

         var3 = var3.trim();
         if (var3.indexOf("\",\"") > 0) {
            String[] var7 = StringUtils.splitCompletely(var3, ",", false);
            this.characters = var7[var7.length - 1].substring(1, var7[var7.length - 1].length() - 1).toCharArray();

            for(int var8 = 0; var8 < var7.length - 1; ++var8) {
               if (debug) {
                  System.out.println("xpath = " + var1 + ", val = " + var7[var8]);
               }

               StringBuffer var9 = new StringBuffer("/" + this.localName);
               String var10 = var7[var8].substring(1, var7[var8].length() - 1);
               var2.children.push(new ReaderEvent(var9, var2, var10, var4, var5, var6));
            }
         } else {
            if (var3.startsWith("\"")) {
               var3 = var3.substring(1, var3.length() - 1);
            }

            this.characters = var3.toCharArray();
         }
      }

   }

   public String getLocalName() {
      return this.localName;
   }

   public boolean isKey() {
      return this.isKey;
   }

   public boolean isKeyComponent() {
      return this.isKeyComponent;
   }

   public boolean isKeyAnAttribute() {
      if (this.getAttributeCount() == 0) {
         return false;
      } else {
         for(int var1 = 0; var1 < this.getAttributeCount(); ++var1) {
            int var2 = this.helper.getPropertyIndex(this.getAttributeLocalName(var1));
            if (this.helper.isAttribute(var2)) {
               return true;
            }
         }

         return false;
      }
   }

   public String getPath() {
      String var1 = this.localName;
      String var2;
      if (this.isKey() || this.isKeyComponent()) {
         if (this.attributeValues != null) {
            var2 = this.attributeValues[0];
         } else {
            var2 = this.getTrimmedTextCharacters();
         }

         var1 = var1 + "/" + var2;
      }

      if (this.parent != null) {
         var2 = this.parent.getPath();
         if (var2 != null) {
            var1 = var2 + "/" + var1;
         }
      }

      return var1;
   }

   public char[] getCharacters() {
      return this.characters;
   }

   public String getCharactersAsString() {
      return this.characters == null ? null : new String(this.characters);
   }

   public void setCharacters(char[] var1) {
      if (var1 != null) {
         this.characters = new char[var1.length];
         System.arraycopy(var1, 0, this.characters, 0, var1.length);
      }
   }

   public ReaderEvent getParent() {
      return this.munger.getParentReaderEvent(this);
   }

   public ReaderEvent getParentReaderEvent() {
      return this.parent;
   }

   public boolean isDiscarded() {
      return this.discard;
   }

   public void setDiscard() {
      this.discard = true;
   }

   public Stack getChildren() {
      return this.children;
   }

   public BasicMunger getBasicMunger() {
      return this.munger;
   }

   protected void setBasicMunger(BasicMunger var1) {
      this.munger = var1;
   }

   public Location getLocation() {
      return this.location;
   }

   public void setLocation(Location var1) {
      this.location = var1;
   }

   public ReaderEvent getBeanEvent() {
      return this.beanEvent;
   }

   AbstractDescriptorBean getBean() {
      return this.bean;
   }

   void setBean(AbstractDescriptorBean var1) {
      this.bean = var1;
   }

   public int getEventType() {
      return this.eventType;
   }

   public int getAttributeCount() {
      return this.attributeCount;
   }

   public void setAttributeCount(int var1) {
      this.attributeCount = var1;
      if (var1 > 0) {
         this.ensureSpaceAvail(var1);
      }

   }

   public String getAttributeLocalName(int var1) {
      if (var1 >= 0 && var1 < this.attributeCount) {
         return this.attributeNames[var1];
      } else {
         throw new IndexOutOfBoundsException("attribute position must be 0.." + (this.attributeCount - 1) + " and not " + var1);
      }
   }

   public void setAttributeLocalName(String var1, int var2) {
      if (var1.equals("name")) {
         this.isKey = true;
      }

      this.attributeNames[var2] = var1;
   }

   public String getAttributeNamespace(int var1) {
      if (var1 >= 0 && var1 < this.attributeCount) {
         return this.attributeUris[var1];
      } else {
         throw new IndexOutOfBoundsException("attribute position must be 0.." + (this.attributeCount - 1) + " and not " + var1);
      }
   }

   public void setAttributeNamespace(String var1, int var2) {
      this.attributeUris[var2] = var1;
   }

   public String getAttributePrefix(int var1) {
      if (var1 >= 0 && var1 < this.attributeCount) {
         return this.attributePrefixes[var1];
      } else {
         throw new IndexOutOfBoundsException("attribute position must be 0.." + (this.attributeCount - 1) + " and not " + var1);
      }
   }

   public void setAttributePrefix(String var1, int var2) {
      this.attributePrefixes[var2] = var1;
   }

   public String getAttributeType(int var1) {
      return "CDATA";
   }

   public boolean isAttributeSpecified(int var1) {
      return false;
   }

   public String getAttributeValue(int var1) {
      if (var1 >= 0 && var1 < this.attributeCount) {
         return this.attributeValues[var1];
      } else {
         throw new IndexOutOfBoundsException("attribute position must be 0.." + (this.attributeCount - 1) + " and not " + var1);
      }
   }

   public void setAttributeValue(String var1, int var2) {
      this.attributeValues[var2] = var1;
   }

   public String getAttributeValue(String var1, String var2) {
      int var3;
      if (var1 != null) {
         for(var3 = 0; var3 < this.attributeCount; ++var3) {
            if (var1.equals(this.attributeUris[var3]) && var2.equals(this.attributeNames[var3])) {
               return this.attributeValues[var3];
            }
         }
      } else {
         for(var3 = 0; var3 < this.attributeCount; ++var3) {
            if (var2.equals(this.attributeNames[var3])) {
               return this.attributeValues[var3];
            }
         }
      }

      return null;
   }

   public void setAttributeValue(String var1, String var2, String var3) {
      if (var1 != null && this.attributeCount != 0) {
         int var4;
         if (var2 != null) {
            for(var4 = 0; var4 < this.attributeCount; ++var4) {
               if (var2.equals(this.attributeUris[var4]) && var3.equals(this.attributeNames[var4]) && var1.equals(this.attributeValues[var4])) {
                  return;
               }
            }
         } else {
            for(var4 = 0; var4 < this.attributeCount; ++var4) {
               if (var3.equals(this.attributeNames[var4]) && var1.equals(this.attributeValues[var4])) {
                  return;
               }
            }
         }

         if (var2 != null) {
            this.attributeUris[this.attributeCount - 1] = var2;
         }

         this.attributeNames[this.attributeCount - 1] = var3;
         this.attributeValues[this.attributeCount - 1] = var1;
      }
   }

   public QName getAttributeName(int var1) {
      return new QName(this.getAttributeNamespace(var1), this.getAttributeLocalName(var1), this.getAttributePrefix(var1));
   }

   public String getCharacterEncodingScheme() {
      return this.charEncodingScheme;
   }

   public void setCharacterEncodingScheme(String var1) {
      this.charEncodingScheme = var1;
   }

   public int getNamespaceCount() {
      return this.namespaceCount;
   }

   public void setNamespaceCount(int var1) {
      this.namespaceCount = var1;
   }

   public String getElementText() {
      return null;
   }

   public void setElementText(String var1) {
   }

   public String getEncoding() {
      return this.inputEncoding;
   }

   public void setEncoding(String var1) {
      this.inputEncoding = var1;
   }

   private void ensureSpaceAvail(int var1) {
      int var2 = this.attributeValues == null ? 0 : this.attributeValues.length;
      if (var1 > var2) {
         int var3 = var1 > 10 ? var1 * 2 : 10;
         if (this.attributeValues == null) {
            this.attributeNames = new String[var3];
            this.attributeValues = new String[var3];
            this.attributeUris = new String[var3];
            this.attributePrefixes = new String[var3];
            return;
         }

         String[] var4 = null;
         var4 = new String[var3];
         System.arraycopy(this.attributeNames, 0, var4, 0, var2);
         this.attributeNames = var4;
         var4 = new String[var3];
         System.arraycopy(this.attributePrefixes, 0, var4, 0, var2);
         this.attributePrefixes = var4;
         var4 = new String[var3];
         System.arraycopy(this.attributeUris, 0, var4, 0, var2);
         this.attributeUris = var4;
         var4 = new String[var3];
         System.arraycopy(this.attributeValues, 0, var4, 0, var2);
         this.attributeValues = var4;
      }

   }

   void consumeElement(StringBuffer var1) {
      int var2 = var1.indexOf("/");
      if (var2 == -1) {
         throw new AssertionError("Invalid xpath-- [" + var1 + "] -- does not start with a \"/\"");
      } else {
         int var3 = var1.indexOf("/", var2 + 1);
         if (debug) {
            System.out.println("... starting xpath = " + var1 + ", start = " + var2 + ", end = " + var3);
         }

         if (var3 == -1) {
            var3 = var1.length();
         }

         this.localName = var1.substring(var2 + 1, var3);
         int var4 = this.localName.indexOf("[");
         int var5;
         if (var4 > -1) {
            var5 = var1.indexOf("]");
            if (var3 < var5) {
               var3 = var1.indexOf("/", var5);
               this.localName = var1.substring(var2 + 1, var3);
            }

            StringBuffer var6 = new StringBuffer(this.localName.substring(var4));
            var6 = var6.deleteCharAt(0).deleteCharAt(var6.length() - 1);
            this.localName = this.localName.substring(0, var4);
            int var7 = var6.indexOf(",");
            if (var7 > -1) {
               String[] var8 = StringUtils.splitCompletely(var6.toString(), ",", false);

               for(int var9 = 0; var9 < var8.length; ++var9) {
                  this.parseAttribute(var8[var9]);
               }
            } else {
               this.parseAttribute(var6.toString());
               this.isKey = true;
            }
         }

         var1.delete(var2, var3);
         if (var1.length() > 0 && Character.isWhitespace(var1.charAt(0))) {
            throw new AssertionError("Whitespace not allowed: [" + var1 + "]");
         } else {
            if (var1.length() > 2 && var1.charAt(0) == '/' && var1.charAt(1) == '[') {
               var5 = var1.indexOf("]");
               if (var5 == -1) {
                  throw new AssertionError("Invalid key definition-- [" + var1 + "]");
               }

               String var10 = var1.substring(2, var5);
               if (var10.indexOf(",") > -1) {
                  String[] var12 = StringUtils.splitCompletely(var10, ",", false);

                  for(int var13 = 0; var13 < var12.length; ++var13) {
                     ReaderEvent var14 = this.parseChild(var12[var13]);
                     var14.isKeyComponent = true;
                     this.children.add(var14);
                  }
               } else {
                  ReaderEvent var11 = this.parseChild(var10);
                  var11.isKey = true;
                  this.children.add(var11);
               }

               var1.delete(0, var5 + 1);
               if (debug) {
                  System.out.println(".. resulting xpath = " + var1);
               }
            }

         }
      }
   }

   private ReaderEvent parseChild(String var1) {
      String[] var2 = StringUtils.splitCompletely(var1, "=", false);
      if (var2.length != 2) {
         throw new AssertionError("Keys must be defined as name value pairs, eg, name=\"value\" -- [" + var1 + "]");
      } else {
         String var3 = var2[0];
         String var4 = var2[1];
         if (var4.startsWith("\"") && var4.charAt(var4.length() - 1) == '"') {
            var4 = var4.substring(1, var4.length() - 1);
            ReaderEvent var5 = new ReaderEvent(var3, this, this.munger, this.location);
            var5.eventType = 1;
            var5.characters = var4.toCharArray();
            return var5;
         } else {
            throw new AssertionError("Key values must be quoted strings: [" + var4 + "]");
         }
      }
   }

   private void parseAttribute(String var1) {
      String[] var2 = StringUtils.splitCompletely(var1, "=", false);
      if (var2.length != 2) {
         throw new AssertionError("Attributes must be defined as name value pairs, eg, name=\"value\" -- [" + var1 + "]");
      } else {
         String var3 = var2[0];
         String var4 = var2[1];
         if (var4.startsWith("\"") && var4.charAt(var4.length() - 1) == '"') {
            var4 = var4.substring(1, var4.length() - 1);
            this.setAttributeCount(this.getAttributeCount() + 1);
            this.setAttributeValue(var4, (String)null, var3);
         } else {
            throw new AssertionError("Attributes must be defined as name value pairs, eg, name=\"value\" -- [" + var1 + "]");
         }
      }
   }

   public void toXML(PrintStream var1) {
      var1.print("<");
      var1.print(this.getLocalName());
      if (this.getAttributeCount() > 0) {
         for(int var2 = 0; var2 < this.getAttributeCount(); ++var2) {
            var1.print(" " + this.getAttributeLocalName(var2) + " = " + this.getAttributeValue(var2));
         }
      }

      SchemaHelper var5 = null;
      if (this.helper == null && this.getBean() != null) {
         var5 = this.getBean()._getSchemaHelper2();
      } else {
         var5 = this.helper;
      }

      if (var5 == null) {
         var1.print(", helper is not set!!!");
      }

      var1.print(">");
      if (this.characters != null) {
         var1.print(new String(this.characters));
      }

      if (this.children != null) {
         Iterator var3 = this.children.iterator();

         while(var3.hasNext()) {
            ReaderEvent var4 = (ReaderEvent)var3.next();
            if (!var4.isDiscarded()) {
               var4.toXML(var1);
            }
         }
      }

      var1.print("</");
      var1.print(this.getLocalName());
      var1.print(">\n");
   }

   public void discard() {
      this.discard = true;
      if (this.children != null) {
         Iterator var1 = this.children.iterator();

         while(var1.hasNext()) {
            ReaderEvent var2 = (ReaderEvent)var1.next();
            var2.discard();
         }
      }

   }

   public void fixParents(ReaderEvent var1) {
      this.parent = var1;
      if (this.children != null) {
         Iterator var2 = this.children.iterator();

         while(var2.hasNext()) {
            ReaderEvent var3 = (ReaderEvent)var2.next();
            var3.fixParents(this);
         }

      }
   }

   public String getElementName() {
      return this.localName;
   }

   public Munger.ReaderEventInfo getParentReaderInfo() {
      return this.parent;
   }

   public boolean compareXpaths(String var1) {
      if (var1.length() == this.localName.length()) {
         return var1.equals(this.localName);
      } else {
         int var2 = var1.lastIndexOf("/");
         if (var2 == -1) {
            return false;
         } else {
            String var3 = var1.substring(var2 + 1);
            return var3.length() == this.localName.length() ? var3.equals(this.localName) : false;
         }
      }
   }

   public void validate(SchemaHelper var1) throws DescriptorException {
      this.helper = var1;
      Iterator var2 = this.children.iterator();

      while(var2.hasNext()) {
         ReaderEvent var3 = (ReaderEvent)var2.next();
         int var4 = var1.getPropertyIndex(var3.getElementName());
         if (var4 == -1) {
            throw new DescriptorException("plan xpath defines an element [" + var3.getPath() + "] that is not valid!");
         }

         SchemaHelper var5 = var1.getSchemaHelper(var4);
         if (var5 != null) {
            var3.validate(var5);
         } else {
            var3.helper = var1;
         }
      }

   }

   public SchemaHelper getSchemaHelper() {
      return this.helper;
   }

   public void toQueuedEvents(ArrayList var1) {
      ReaderEvent var2 = this.getQueuedEvent(1, this.getLocalName(), this.getLocation());
      var2.beanEvent = this.beanEvent;
      var2.bean = this.bean;
      var2.helper = this.helper;
      var1.add(var2);
      if (this.characters != null) {
         var1.add(this.getQueuedEvent(4, this.getCharacters(), this.getLocation()));
      }

      if (this.children != null) {
         Iterator var3 = this.children.iterator();

         while(var3.hasNext()) {
            ReaderEvent var4 = (ReaderEvent)var3.next();
            if (!var4.isDiscarded()) {
               var4.toQueuedEvents(var1);
            }
         }
      }

      if (this.attributeCount > 0) {
         var2.attributeCount = this.attributeCount;
         var2.attributeNames = this.attributeNames;
         var2.attributeValues = this.attributeValues;
         var2.attributeUris = this.attributeUris;
         var2.attributePrefixes = this.attributePrefixes;
      }

      var1.add(this.getQueuedEvent(2, this.getLocalName(), this.getLocation()));
   }

   protected ReaderEvent getQueuedEvent(int var1, Object var2, Location var3) {
      return new ReaderEvent(var1, var2, var3, this.munger);
   }

   public void setOperation(int var1) {
      this.operation = var1;
   }

   public int getOperation() {
      return this.operation;
   }

   public void removeNamedChildren(String var1) {
      Stack var2 = this.getChildren();
      int var3 = 0;

      while(var3 < var2.size()) {
         ReaderEvent var4 = (ReaderEvent)var2.elementAt(var3);
         if (var4.getLocalName().equals(var1)) {
            var2.removeElementAt(var3);
         } else {
            ++var3;
         }
      }

   }

   public Object getBeanKey(SchemaHelper var1) {
      if (var1.hasKey()) {
         for(int var2 = 0; var2 < this.getChildren().size(); ++var2) {
            ReaderEvent var3 = (ReaderEvent)this.getChildren().elementAt(var2);
            if (var1.isKey(var1.getPropertyIndex(var3.getElementName()))) {
               return var3.getTrimmedTextCharacters();
            }
         }
      }

      return null;
   }

   public boolean isBeanKey(SchemaHelper var1) {
      return var1.hasKey() ? var1.isKey(var1.getPropertyIndex(this.getElementName())) : var1.isKeyChoice(var1.getPropertyIndex(this.getElementName()));
   }

   public boolean hasBeanCompositeKey() {
      return this.getBean() == null ? false : this.getBean()._getSchemaHelper2().getKeyElementNames().length > 1;
   }

   public String getBeanCompositeKey(SchemaHelper var1) {
      String[] var2 = var1.getKeyElementNames();
      StringBuffer var3 = new StringBuffer("[");

      for(int var4 = 0; var4 < var2.length; ++var4) {
         String var5 = var2[var4];
         var3.append("[");

         for(int var6 = 0; var6 < this.getChildren().size(); ++var6) {
            ReaderEvent var7 = (ReaderEvent)this.getChildren().elementAt(var6);
            if (var5.equals(var7.getLocalName())) {
               var3.append("\"");
               String var8 = var7.getTrimmedTextCharacters();
               if (var8 != null) {
                  var3.append(var8);
               }

               var3.append("\",");
            }
         }

         if (var3.charAt(var3.length() - 1) == ',') {
            var3.deleteCharAt(var3.length() - 1);
         }

         var3.append("]");
         if (var4 < var2.length - 1) {
            var3.append(",");
         }
      }

      var3.append("]");
      return var3.toString();
   }

   public String getBeanCompositeKey() {
      return this.getBeanCompositeKey(this.getBean()._getSchemaHelper2());
   }

   public boolean isSingleton() {
      AbstractDescriptorBean var1 = this.munger.getCurrentOrEventBean(this);
      SchemaHelper var2 = null;
      if (var1 != null) {
         var2 = var1._getSchemaHelper2();
      } else {
         var2 = this.getSchemaHelper();
      }

      if (var2 != null && !var2.isArray(var2.getPropertyIndex(this.getElementName()))) {
         return true;
      } else {
         return var1 == null ? false : var1._isPropertyASingleton(this);
      }
   }

   public boolean isAdditive() {
      AbstractDescriptorBean var1 = this.munger.getCurrentOrEventBean(this);
      return var1 == null ? false : var1._isPropertyAdditive(this);
   }

   public static boolean compareKeys(Object var0, Object var1) {
      if (var0 == var1) {
         return true;
      } else {
         return var0 != null && var1 != null ? var0.equals(var1) : false;
      }
   }

   public boolean searchSubTree(ReaderEvent var1) {
      Stack var2 = this.getChildren();

      for(int var3 = 0; var3 < var2.size(); ++var3) {
         ReaderEvent var4 = (ReaderEvent)var2.elementAt(var3);
         if (var4 == var1) {
            return true;
         }

         if (var4.searchSubTree(var1)) {
            return true;
         }
      }

      return false;
   }

   public void replaceAndMoveChildren(ReaderEvent var1) {
      Stack var2 = this.getChildren();
      var2.removeAllElements();
      Stack var3 = var1.getChildren();

      for(int var4 = 0; var4 < var3.size(); ++var4) {
         ReaderEvent var5 = (ReaderEvent)var3.elementAt(var4);
         var5.fixParents(this);
         var2.add(var5);
      }

      var3.removeAllElements();
   }

   public String getTrimmedTextCharacters() {
      String var1 = this.getCharactersAsString();
      return var1 != null ? var1.trim() : var1;
   }
}
