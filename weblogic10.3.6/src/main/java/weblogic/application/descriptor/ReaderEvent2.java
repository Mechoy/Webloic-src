package weblogic.application.descriptor;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamConstants;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.utils.Debug;
import weblogic.utils.StringUtils;

public class ReaderEvent2 implements XMLStreamConstants {
   private static boolean debug = Debug.getCategory("weblogic.descriptor.merge").isEnabled();
   private static boolean test = Debug.getCategory("weblogic.descriptor.merge.test").isEnabled();
   private ReaderEventInfo info;
   private ReaderEvent2 parent;
   private Stack children = new Stack();
   private boolean childrenFromKey = false;
   SchemaHelper helper;
   private boolean discard = false;
   private ReaderEvent2 originalParent;

   public ReaderEvent2(int var1, Location var2) {
      this.info = new ReaderEventInfo(var1, var2);
      this.helper = new SchemaHelper() {
         SchemaHelper myHelper;

         public int getPropertyIndex(String var1) {
            return this.getRootSchemaHelper().getPropertyIndex(var1);
         }

         public SchemaHelper getSchemaHelper(int var1) {
            return this.getRootSchemaHelper().getSchemaHelper(var1);
         }

         public String getElementName(int var1) {
            return this.getRootSchemaHelper().getElementName(var1);
         }

         public boolean isArray(int var1) {
            return this.getRootSchemaHelper().isArray(var1);
         }

         public boolean isAttribute(int var1) {
            return this.getRootSchemaHelper().isAttribute(var1);
         }

         public String getAttributeName(int var1) {
            return this.getRootSchemaHelper().getAttributeName(var1);
         }

         public boolean isBean(int var1) {
            return this.getRootSchemaHelper().isBean(var1);
         }

         public boolean isConfigurable(int var1) {
            return this.getRootSchemaHelper().isConfigurable(var1);
         }

         public boolean isKey(int var1) {
            return this.getRootSchemaHelper().isKey(var1);
         }

         public boolean isKeyChoice(int var1) {
            return this.getRootSchemaHelper().isKeyChoice(var1);
         }

         public boolean isKeyComponent(int var1) {
            return this.getRootSchemaHelper().isKeyComponent(var1);
         }

         public boolean hasKey() {
            return this.getRootSchemaHelper().hasKey();
         }

         public String[] getKeyElementNames() {
            return this.getRootSchemaHelper().getKeyElementNames();
         }

         public String getRootElementName() {
            return this.getRootSchemaHelper().getRootElementName();
         }

         public boolean isMergeRulePrependDefined(int var1) {
            return this.getRootSchemaHelper().isMergeRulePrependDefined(var1);
         }

         public boolean isMergeRuleIgnoreSourceDefined(int var1) {
            return this.getRootSchemaHelper().isMergeRuleIgnoreSourceDefined(var1);
         }

         public boolean isMergeRuleIgnoreTargetDefined(int var1) {
            return this.getRootSchemaHelper().isMergeRuleIgnoreTargetDefined(var1);
         }

         private SchemaHelper getRootSchemaHelper() {
            if (this.myHelper == null) {
               Iterator var1 = ReaderEvent2.this.children.iterator();

               while(var1.hasNext()) {
                  ReaderEvent2 var2 = (ReaderEvent2)var1.next();
                  if (var2.helper != null && var2.isStartElement()) {
                     this.myHelper = var2.helper;
                     break;
                  }
               }
            }

            if (this.myHelper == null) {
               throw new MissingRootElementException();
            } else {
               return this.myHelper;
            }
         }
      };
   }

   public ReaderEvent2(int var1, String var2, ReaderEvent2 var3, Location var4) {
      this.info = new ReaderEventInfo(var1, var2, var4);
      this.parent = var3;
      if (var3.info.namespaces != null) {
         this.info.getNamespaces().copy(var3.info.namespaces);
      }

   }

   public ReaderEvent2(StringBuffer var1, ReaderEvent2 var2, SchemaHelper var3, String var4, Location var5) {
      this.parent = var2;
      this.helper = var3;
      this.consumeElements(var1, var4, var5);
   }

   public ReaderEvent2 getParent() {
      return this.parent;
   }

   public void setParent(ReaderEvent2 var1) {
      this.parent = var1;
   }

   public ReaderEvent2 getOriginalParent() {
      return this.originalParent;
   }

   public void setOriginalParent(ReaderEvent2 var1) {
      this.originalParent = var1;
   }

   public ReaderEventInfo getReaderEventInfo() {
      return this.info;
   }

   public int getEventType() {
      return this.info.getEventType();
   }

   public String getElementName() {
      return this.info.getElementName();
   }

   public void setElementName(String var1) {
      this.info.setElementName(var1);
   }

   public char[] getCharacters() {
      return this.info.getCharacters();
   }

   public void setCharacters(char[] var1) {
      this.info.setCharacters(var1);
   }

   public String getTextCharacters() {
      return this.info.getCharactersAsString();
   }

   private String getTrimmedTextCharacters() {
      String var1 = this.getTextCharacters();
      return var1 != null ? var1.trim() : var1;
   }

   public Location getLocation() {
      return this.info.getLocation();
   }

   public String toString() {
      if (this.info != null) {
         String var1 = super.toString();
         var1 = var1.substring(var1.lastIndexOf("@"));
         return this.info.getElementName() != null ? this.info.getElementName() + var1 : "ReaderEvent2-type = " + this.info.getEventType() + var1;
      } else {
         return super.toString();
      }
   }

   public boolean isDiscarded() {
      return this.discard;
   }

   public void setDiscard() {
      this.discard = true;
   }

   public void setDiscard(boolean var1) {
      this.discard = var1;
   }

   public Stack getChildren() {
      return this.children;
   }

   public void addChild(ReaderEvent2 var1) {
      this.children.add(var1);
   }

   public boolean isChildrenFromKey() {
      return this.childrenFromKey;
   }

   public void setChildrenFromKey(boolean var1) {
      this.childrenFromKey = var1;
   }

   public void validate(SchemaHelper var1) {
      this.helper = var1;
      Iterator var2 = this.children.iterator();

      while(var2.hasNext()) {
         ReaderEvent2 var3 = (ReaderEvent2)var2.next();
         int var4 = var1.getPropertyIndex(var3.getReaderEventInfo().getElementName());
         if (var4 == -1) {
            throw new AssertionError("plan xpath defines an element [" + var3.getReaderEventInfo().getElementName() + "] that is not valid!");
         }

         SchemaHelper var5 = var1.getSchemaHelper(var4);
         if (var5 != null) {
            var3.validate(var5);
         } else {
            var3.helper = var1;
         }
      }

   }

   void consumeElements(StringBuffer var1, String var2, Location var3) {
      this.consumeElement(var1, var3);
      int var4 = this.helper.getPropertyIndex(this.getReaderEventInfo().getElementName());
      if (var4 != -1) {
         this.helper = this.helper.getSchemaHelper(var4);
      }

      if (var1.length() == 1 && var1.charAt(0) == '/') {
         if (this.childrenFromKey && this.getChildren().size() == 1) {
            ReaderEvent2 var5 = (ReaderEvent2)this.getChildren().peek();
            ReaderEventInfo var6 = var5.info;
            if (var6.isKeyCharacters() && var2 != null && !Arrays.equals(var2.toCharArray(), var6.getCharacters())) {
               var6.setKeyOverride(true);
               var6.setKeyOverrideCharacters(var2.toCharArray());
            }
         }
      } else if (var1.length() > 0 && var1.charAt(0) == '/') {
         this.consumeChildren(var1, var2, var3);
      } else {
         this.consumeText(var1, var2, var3);
      }

   }

   void consumeChildren(StringBuffer var1, String var2, Location var3) {
      this.getChildren().push(new ReaderEvent2(var1, this, this.helper, var2, var3));
   }

   void consumeText(StringBuffer var1, String var2, Location var3) {
      if (debug) {
         System.out.println("Adding " + var2 + " to " + this.info.getElementName());
      }

      if (var2 != null) {
         var2 = var2.trim();
         if (var2.length() != 0 || !this.getParent().isArray(this.getPropertyIndex())) {
            if (var2.indexOf("\",\"") > 0) {
               String[] var4 = StringUtils.splitCompletely(var2, ",", false);
               this.info.setCharacters(var4[var4.length - 1].substring(1, var4[var4.length - 1].length() - 1).toCharArray());

               for(int var5 = 0; var5 < var4.length - 1; ++var5) {
                  if (debug) {
                     System.out.println("xpath = " + var1 + ", val = " + var4[var5]);
                  }

                  StringBuffer var6 = new StringBuffer("/" + this.info.getElementName());
                  this.getParent().getChildren().push(new ReaderEvent2(var6, this.parent, this.helper, var4[var5], var3));
               }
            } else {
               if (var2.startsWith("\"")) {
                  var2 = var2.substring(1, var2.length() - 1);
               }

               this.info.setCharacters(var2.toCharArray());
            }

         }
      }
   }

   void consumeElement(StringBuffer var1, Location var2) {
      int var3 = var1.indexOf("/");
      if (var3 == -1) {
         throw new AssertionError("Invalid xpath-- [" + var1 + "] -- does not start with a \"/\"");
      } else {
         int var4 = var3 + 1;
         int var5 = var1.indexOf("/", var4);
         int var6 = var1.indexOf("\"", var4);
         if (var6 != -1 && var6 < var5) {
            var4 = var6 + 1;
            var6 = var1.indexOf("\"", var4);
            var5 = var1.indexOf("/", var6);
         }

         if (debug) {
            System.out.println("... starting xpath = " + var1 + ", start = " + var3 + ", end = " + var5);
         }

         if (var5 == -1) {
            var5 = var1.length();
         }

         String var7 = var1.substring(var3 + 1, var5);
         int var8 = var7.indexOf("[");
         int var9;
         if (var8 == -1) {
            var9 = var7.indexOf(" xmlns");
            String var10 = null;
            if (var9 != -1) {
               var10 = var7.substring(var9);
               var7 = var7.substring(0, var9);
            }

            this.info = new ReaderEventInfo(1, var7, var2);
            if (var10 != null) {
               this.setNamespaces(this.info, var10);
            }
         } else {
            var9 = var1.indexOf("]");
            if (var5 < var9) {
               var5 = var1.indexOf("/", var9);
               var7 = var1.substring(var3 + 1, var5);
            }

            StringBuffer var20 = new StringBuffer(var7.substring(var8));
            var20 = var20.deleteCharAt(0).deleteCharAt(var20.length() - 1);
            var7 = var7.substring(0, var8);
            this.info = new ReaderEventInfo(1, var7, var2);
            int var11 = var20.indexOf(",");
            if (var11 > -1) {
               String[] var12 = StringUtils.splitCompletely(var20.toString(), ",", false);
               int var13 = this.info.getAttributeCount();

               for(int var14 = 0; var14 < var12.length; ++var14) {
                  this.parseAttribute(var12[var14]);
               }
            } else {
               this.parseAttribute(var20.toString());
            }
         }

         var1.delete(var3, var5);
         if (var1.length() > 0 && Character.isWhitespace(var1.charAt(0))) {
            throw new AssertionError("Whitespace not allowed: [" + var1 + "]");
         } else {
            if (var1.length() > 2 && var1.charAt(0) == '/' && var1.charAt(1) == '[') {
               this.childrenFromKey = true;
               var9 = var1.indexOf("=");
               if (var9 < 3) {
                  throw new AssertionError("Key element name not found in [" + var1 + "]");
               }

               int var22 = var1.indexOf("]");
               if (var9 == -1 || var22 == -1) {
                  throw new AssertionError("Invalid key definition-- [" + var1 + "]");
               }

               if (var1.indexOf("\",") > -1) {
                  String[] var21 = StringUtils.splitCompletely(var1.toString(), ",", false);

                  for(int var24 = 0; var24 < var21.length; ++var24) {
                     String var26 = null;
                     if (var21[var24].indexOf("\"]") > -1) {
                        var26 = var21[var24].substring(0, var21[var24].indexOf("\"]"));
                     } else {
                        var26 = var21[var24];
                     }

                     String[] var28 = StringUtils.splitCompletely(var26, "=", false);

                     for(int var15 = 0; var15 < var28.length; ++var15) {
                        String var16 = var28[var15];
                        ++var15;
                        String var17 = var28[var15];
                        if (var16.startsWith("/")) {
                           var16 = var16.substring(1);
                        }

                        if (var16.startsWith("[")) {
                           var16 = var16.substring(1);
                        }

                        if (var17.startsWith("\"")) {
                           var17 = var17.substring(1);
                        }

                        int var18 = var17.indexOf("\"");
                        if (var18 > -1) {
                           var17 = var17.substring(0, var18);
                        }

                        ReaderEvent2 var19 = new ReaderEvent2(1, var16, this, var2);
                        var19.getReaderEventInfo().setCharacters(var17.toCharArray());
                        var19.getReaderEventInfo().setKeyCharacters(true);
                        this.getChildren().add(var19);
                     }
                  }
               } else {
                  String var23 = var1.substring(var9 + 1, var22);
                  if (!var23.startsWith("\"") || var23.charAt(var23.length() - 1) != '"') {
                     throw new AssertionError("Key values must be quoted strings: [" + var23 + "]");
                  }

                  var23 = var23.substring(1, var23.length() - 1);
                  String var25 = var1.substring(2, var9);
                  ReaderEvent2 var27 = new ReaderEvent2(1, var25, this, var2);
                  var27.getReaderEventInfo().setCharacters(var23.toCharArray());
                  var27.getReaderEventInfo().setKeyCharacters(true);
                  this.getChildren().add(var27);
               }

               var1.delete(0, var22 + 1);
            }

            if (debug) {
               System.out.println(".. resulting xpath = " + var1);
            }

         }
      }
   }

   void setNamespaces(ReaderEventInfo var1, String var2) {
      String[] var3 = StringUtils.splitCompletely(var2, "=", false);
      if (var3.length != 2) {
         throw new AssertionError("Namespaces must be defined as name value pairs, eg, name=\"value\" -- [" + var2 + "]");
      } else {
         for(int var4 = 0; var4 < var3.length; ++var4) {
            String var5 = var3[var4];
            ++var4;
            String var6 = var3[var4];
            int var7 = var5.indexOf(":");
            if (var7 == -1) {
               var5 = null;
            } else {
               var5 = var5.substring(var7 + 1);
            }

            if (!var6.startsWith("\"") || var6.charAt(var6.length() - 1) != '"') {
               throw new AssertionError("Namespaces must be defined as name and quoted value, eg, name=\"value\" -- [" + var2 + "]");
            }

            var6 = var6.substring(1, var6.length() - 1);
            var1.setNamespaceCount(var1.getNamespaceCount() + 1);
            var1.setNamespaceURI(var5, var6);
            var1.setPrefix(var5);
         }

      }
   }

   void parseAttribute(String var1) {
      String[] var2 = StringUtils.splitCompletely(var1, "=", false);
      if (var2.length != 2) {
         throw new AssertionError("Attributes must be defined as name value pairs, eg, name=\"value\" -- [" + var1 + "]");
      } else {
         String var3 = var2[0];
         String var4 = var2[1];
         if (var4.startsWith("\"") && var4.charAt(var4.length() - 1) == '"') {
            var4 = var4.substring(1, var4.length() - 1);
            this.info.setAttributeCount(this.info.getAttributeCount() + 1);
            this.info.setAttributeValue(var4, (String)null, var3);
         } else {
            throw new AssertionError("Attributes must be defined as name value pairs, eg, name=\"value\" -- [" + var1 + "]");
         }
      }
   }

   boolean isSimpleKey() {
      int var1 = this.helper.getPropertyIndex(this.info.getElementName());
      if (this.helper.isKey(var1)) {
         return true;
      } else if (this.helper.isKeyChoice(var1)) {
         return true;
      } else {
         if (this.info.getAttributeCount() > 0) {
            for(int var2 = 0; var2 < this.info.getAttributeCount(); ++var2) {
               var1 = this.helper.getPropertyIndex(this.info.getAttributeLocalName(var2));
               if (this.helper.isKey(var1)) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   boolean isKeyComponent() {
      int var1 = this.helper.getPropertyIndex(this.info.getElementName());
      return this.helper.isKeyComponent(var1);
   }

   boolean isKeyChoice() {
      int var1 = this.helper.getPropertyIndex(this.info.getElementName());
      return this.helper.isKeyChoice(var1);
   }

   public void toXML(PrintStream var1) {
      if (!this.isDiscarded()) {
         var1.print("\n<");
         String var2 = super.toString();
         var1.print(this.info.getElementName() + var2.substring(var2.lastIndexOf("@")));
         var1.print(" " + (this.helper == null ? "null" : this.helper.toString().substring(this.helper.toString().lastIndexOf(".") + 1)));
         int var3;
         if (this.info.getNamespaceCount() > 0) {
            for(var3 = 0; var3 < this.info.getNamespaceCount(); ++var3) {
               var1.print(" ");
               String var4 = this.info.getNamespacePrefix(var3);
               if (var4 == null) {
                  var4 = "xmlns";
               } else {
                  var4 = "xmlns:" + var4;
               }

               var1.print(var4);
               var1.print("=\"");
               var1.print(this.info.getNamespaceURI(var3));
               var1.print("\"");
            }
         }

         if (this.info.getAttributeCount() > 0) {
            for(var3 = 0; var3 < this.info.getAttributeCount(); ++var3) {
               var1.print(" " + this.info.getAttributeLocalName(var3) + " = " + this.info.getAttributeValue(var3));
            }
         }

         var1.print(">");
         var1.print(this.info.getCharactersAsString());
      }

      if (this.children != null) {
         Iterator var5 = this.children.iterator();

         while(var5.hasNext()) {
            ReaderEvent2 var6 = (ReaderEvent2)var5.next();
            var6.toXML(var1);
         }
      }

      if (!this.isDiscarded()) {
         var1.print("</");
         var1.print(this.info.getElementName());
         var1.print(">\n");
      }

   }

   public void discard() {
      if (debug) {
         System.out.println("discard: " + this.getElementName());
      }

      this.discard = true;
      this.orphanChildren();
      ReaderEvent2 var1 = this.getParent();
   }

   public void orphanChild(ReaderEvent2 var1) {
      if (debug) {
         System.out.println("orphan: " + var1.getElementName());
      }

      ReaderEvent2 var2 = var1.getParent();
      if (var2 != null) {
         var2.getChildren().remove(var1);
         if (var2.getChildren().size() == 0) {
            var2.discard();
         }
      }

   }

   public void orphanChildren() {
      if (debug) {
         System.out.println("orphanChildren: " + this.getElementName());
      }

      this.getChildren().clear();
   }

   public SchemaHelper getSchemaHelper() {
      if (this.helper.getPropertyIndex(this.getElementName()) == -1) {
         return this.parent.helper != null && this.parent.helper.getPropertyIndex(this.getElementName()) == -1 ? this.helper : this.parent.helper;
      } else {
         return this.helper;
      }
   }

   public void setSchemaHelper(SchemaHelper var1) {
      this.helper = var1;
   }

   public boolean isRoot() {
      return this.getElementName() == null;
   }

   public boolean isBean() {
      assert this.helper != null && this.parent != null;

      return this.helper != this.parent.helper;
   }

   public boolean isBeanKey() {
      if (!this.isBean()) {
         return false;
      } else {
         SchemaHelper var1 = this.parent.helper;
         int var2 = var1.getPropertyIndex(this.getElementName());
         return var1.isKey(var2) || var1.isKeyChoice(var2) || var1.isKeyComponent(var2);
      }
   }

   public boolean hasKey() {
      for(ReaderEvent2 var1 = this.getNextChild(-1); var1 != null; var1 = this.getNextChild(var1)) {
         if (var1.isSimpleKey() || var1.isKeyComponent() || var1.isKeyChoice()) {
            return true;
         }
      }

      return this.helper.hasKey();
   }

   public String getKey() {
      return this.getKey(false);
   }

   public String getKey(boolean var1) {
      if (this.info.getAttributeCount() > 0) {
         for(int var2 = 0; var2 < this.info.getAttributeCount(); ++var2) {
            int var3 = this.helper.getPropertyIndex(this.info.getAttributeLocalName(var2));
            if (this.helper.isKey(var3)) {
               return this.getXpath();
            }
         }
      }

      for(ReaderEvent2 var4 = this.getNextChild(-1); var4 != null; var4 = this.getNextChild(var4)) {
         if (var4.isSimpleKey()) {
            return var4.getXpath();
         }

         if (var4.isKeyChoice()) {
            return var4.getXpath();
         }

         if (var4.isKeyComponent()) {
            return var4.getXpath(var1);
         }

         if (var4.isBeanKey()) {
            return var4.getXpath();
         }
      }

      return this.getXpath();
   }

   public ReaderEvent2 getMatchingKey(String var1) {
      if (debug) {
         System.out.println("getMatchingKey: " + var1);
      }

      ReaderEvent2 var2;
      for(var2 = this.getNextChild(-1); var2 != null; var2 = this.getNextChild(var2)) {
         if (debug) {
            System.out.print("--" + var2 + ", hasKey ? " + var2.hasKey() + ", getXpath = " + var2.getXpath());
         }

         if (debug) {
            System.out.println(var2.hasKey() ? ", getKey() = " + var2.getKey() : "");
         }

         if (var2.hasKey() && var2.getKey().equals(var1)) {
            return var2;
         }
      }

      if (this.hasKey()) {
         for(var2 = this.getNextChild(-1); var2 != null; var2 = this.getNextChild(var2)) {
            if (debug) {
               System.out.println("---" + var2 + ", getXpath = " + var2.getXpath());
            }

            if (var2.getXpath().equals(var1)) {
               return var2;
            }
         }
      }

      return null;
   }

   public ReaderEvent2 getMatchingElementName(String var1) {
      if (debug) {
         System.out.println("getMatchingElementName");
      }

      ReaderEvent2 var2 = null;

      for(ReaderEvent2 var3 = this.getNextChild(-1); var3 != null; var3 = this.getNextChild(var3)) {
         if (debug) {
            System.out.println("--" + var3 + ", getXpath = " + var3.getElementName());
         }

         if (var3.getElementName().equals(var1)) {
            if (!var3.isDiscarded()) {
               return var3;
            }

            var2 = var3;
         }
      }

      return var2;
   }

   public ReaderEvent2 getMatchingEventFromKey(ReaderEvent2 var1) {
      if (debug) {
         System.out.println("getMatchingEventFromKey");
      }

      for(ReaderEvent2 var2 = this.getNextChild(-1); var2 != null; var2 = this.getNextChild(var2)) {
         if (debug) {
            System.out.println("--" + var2 + ", getXpath = " + var2.getElementName());
         }

         if (var2.getElementName().equals(var1.getElementName())) {
            boolean var3 = true;
            Iterator var4 = var1.getChildren().iterator();

            while(var4.hasNext()) {
               ReaderEvent2 var5 = (ReaderEvent2)var4.next();
               if (debug) {
                  System.out.println("--" + var5 + ", getXpath = " + var5.getXpath());
               }

               if (var5.getReaderEventInfo().isKeyCharacters()) {
                  Iterator var6 = var2.getChildren().iterator();

                  while(var6.hasNext()) {
                     ReaderEvent2 var7 = (ReaderEvent2)var6.next();
                     if (debug) {
                        System.out.println("--" + var7 + ", getXpath = " + var7.getXpath());
                     }

                     if (var5.getElementName().equals(var7.getElementName())) {
                        char[] var8 = var5.getReaderEventInfo().getCharacters();
                        char[] var9 = var7.getReaderEventInfo().getCharacters();
                        if (!Arrays.equals(var8, var9)) {
                           var3 = false;
                        }
                     }
                  }
               }
            }

            if (var3) {
               return var2;
            }
         }
      }

      return null;
   }

   public boolean isStartDoc() {
      return this.getEventType() == 7;
   }

   public boolean isStartElement() {
      return this.getEventType() == 1;
   }

   public SchemaHelper findRootSchemaHelper() {
      Iterator var1 = this.children.iterator();

      while(var1.hasNext()) {
         ReaderEvent2 var2 = (ReaderEvent2)var1.next();
         if (var2.helper != null) {
            this.helper = var2.helper;
         }
      }

      return this.helper;
   }

   public void merge(ReaderEvent2 var1, int var2, boolean var3) {
      if (debug) {
         System.out.println("merge, this = " + this + ", other = " + var1);
      }

      if (this.getEventType() != 7) {
         throw new AssertionError("must start merge form a START_DOCUMENT event");
      } else {
         Iterator var4 = this.getChildren().iterator();

         while(var4.hasNext()) {
            ReaderEvent2 var5 = (ReaderEvent2)var4.next();
            switch (var5.getEventType()) {
               case 1:
                  if (debug) {
                     System.out.println("\n target ReaderEvent to merge: ");
                     var5.toXML(System.out);
                     System.out.println("----------- end ReaderEvent --------");
                  }

                  ReaderEvent2 var6 = null;
                  Iterator var7 = var1.getChildren().iterator();

                  while(var7.hasNext()) {
                     ReaderEvent2 var8 = (ReaderEvent2)var7.next();
                     if (var8.getEventType() == 1) {
                        var6 = var8;
                        break;
                     }
                  }

                  if (var6 == null) {
                     throw new AssertionError("Could not find start element within event: " + var1);
                  }

                  if (var5.getElementName().equals(var6.getElementName())) {
                     var5.mergeBean(var6, var2, var3);
                  }

                  return;
               case 5:
               case 11:
                  break;
               default:
                  throw new AssertionError("Unknown event: " + var5.getEventType());
            }
         }

      }
   }

   public void mergeBean(ReaderEvent2 var1, int var2, boolean var3) {
      if (debug) {
         System.out.println("mergeBean: target = " + this.getElementName() + ", source = " + var1.getElementName());
      }

      HashSet var4 = new HashSet();

      for(ReaderEvent2 var5 = var1.getNextChild(-1); var5 != null; var5 = var1.getNextChild(var5)) {
         if (debug) {
            System.out.println("mergeBean for source child: " + var5.getElementName() + ", isBean " + var5.isBean() + ", hasKey " + var5.hasKey() + " prepend? " + var5.isMergeRulePrependDefined());
            System.out.println("" + var5.getTextCharacters());
         }

         if (var5.isBean()) {
            if (var5.hasKey()) {
               this.mergeBeanWithKey(var5, var2, var3);
            } else {
               this.mergeBeanWithoutKey(var5, var2, var3);
            }
         } else {
            this.mergePropertyImpl(var5, var2, var3, var4);
         }
      }

   }

   private void mergeBeanWithKey(ReaderEvent2 var1, int var2, boolean var3) {
      boolean var4 = var2 == 3;
      ReaderEvent2 var5 = this.getMatchingKey(var1.getKey(var4));
      if (this.isArray(var1.getPropertyIndex()) && var1.isMergeRulePrependDefined()) {
         if (var5 != null) {
            var5.mergeBean(var1, var2, var3);
            return;
         }

         if (var5 == null) {
            var5 = this.getMatchingElementName(var1.getElementName());
         }

         if (var5 == null) {
            this.adopt(var1, var3);
         } else {
            this.adopt(var1, var3, true);
         }
      } else {
         if (var5 == null && var3 && var4) {
            return;
         }

         if (var5 == null) {
            this.adopt(var1, var3);
         } else if (!var3 && var1.isMergeRuleIgnoreTargetDefined()) {
            var5.discard();
            this.adopt(var1, var3);
         } else {
            if (!var3 && var1.isMergeRuleIgnoreSourceDefined()) {
               return;
            }

            var5.mergeBean(var1, var2, var3);
         }
      }

   }

   private void mergeBeanWithoutKey(ReaderEvent2 var1, int var2, boolean var3) {
      ReaderEvent2 var4 = null;
      if (var3 && this.isArray(var1.getPropertyIndex()) && var1.isChildrenFromKey() && var2 == 1) {
         var4 = this.getMatchingEventFromKey(var1);
      } else {
         var4 = this.getMatchingElementName(var1.getElementName());
      }

      if (this.isArray(var1.getPropertyIndex()) && var1.isMergeRulePrependDefined()) {
         if (var4 == null) {
            this.adopt(var1, var3);
         } else {
            this.adopt(var1, var3, true);
         }
      } else if (var4 == null) {
         this.adopt(var1, var3);
      } else if (!var3 && var1.isMergeRuleIgnoreTargetDefined()) {
         var4.discard();
         this.adopt(var1, var3);
      } else {
         if (!var3 && var1.isMergeRuleIgnoreSourceDefined()) {
            return;
         }

         if (this.isArray(var1.getPropertyIndex()) && !var3) {
            if (this.isArray(var1.getPropertyIndex())) {
               if (var4 == null) {
                  this.adopt(var1, var3);
               } else {
                  this.adopt(var1, var3, true);
               }
            } else {
               this.adopt(var1, var3);
            }
         } else {
            var4.mergeBean(var1, var2, var3);
         }
      }

   }

   private void mergePropertyImpl(ReaderEvent2 var1, int var2, boolean var3, HashSet var4) {
      ReaderEvent2 var5 = this.getMatchingElementName(var1.getElementName());
      int var6 = var1.getPropertyIndex();
      if (var2 == 3 && this.isArray(var6)) {
         if (!var4.contains(var1.getElementName())) {
            this.discardAllArrayElements(var1);
            var4.add(var1.getElementName());
         }

         var2 = 1;
      }

      if (debug) {
         System.out.println("mergeProperty t is " + var5);
      }

      if (var5 == null) {
         this.adopt(var1, var3);
      } else if (!var3 && var1.isMergeRuleIgnoreTargetDefined()) {
         if (this.isArray(var6)) {
            if (!var4.contains(var1.getElementName())) {
               this.discardAllArrayElements(var1);
               var4.add(var1.getElementName());
            }
         } else {
            var5.discard();
         }

         this.adopt(var1, var3);
      } else {
         if (!var3 && var1.isMergeRuleIgnoreSourceDefined()) {
            return;
         }

         switch (var2) {
            case -1:
            case 1:
               var5.mergeProperty(var1, var2, var3);
            case 0:
            default:
               break;
            case 2:
               if (!var5.isSimpleKey()) {
                  var5.removeProperty(var1);
               }
               break;
            case 3:
               if (var1.info.isKeyOverride()) {
                  var5.replacePropertyWithOverride(var1);
               } else {
                  var5.replaceProperty(var1);
               }
         }
      }

   }

   private void discardAllArrayElements(ReaderEvent2 var1) {
      Iterator var3 = this.getChildren().iterator();

      while(var3.hasNext()) {
         ReaderEvent2 var4 = (ReaderEvent2)var3.next();
         if (var1.getElementName().equals(var4.getElementName())) {
            var4.discard();
         }
      }

   }

   public void adopt(ReaderEvent2 var1, SchemaHelper var2, boolean var3) {
      if (var2 != null && this.getEventType() == 1) {
         this.adopt(var1, false, false, var3);
      } else {
         this.addChild(var1);
      }

   }

   public void adopt(ReaderEvent2 var1, SchemaHelper var2) {
      this.adopt(var1, var2, true);
   }

   void adopt(ReaderEvent2 var1, boolean var2) {
      this.adopt(var1, var2, false);
   }

   void adopt(ReaderEvent2 var1, boolean var2, boolean var3) {
      this.adopt(var1, var2, var3, true);
   }

   void adopt(ReaderEvent2 var1, boolean var2, boolean var3, boolean var4) {
      if (debug) {
         System.out.println("adopt: " + var1.getElementName() + " into parent = " + this.getElementName() + " isPlanMerge:" + var2 + " prependArrayElements:" + var3 + " reorderOnAdopt:" + var4);
      }

      int var5 = this.helper.getPropertyIndex(var1.getElementName());
      ReaderEvent2 var6 = this.getNextChild(var5);
      if (var6 == null) {
         var6 = this.getPrevChild(var5);
      }

      if (debug) {
         System.out.println("source is " + var1.getElementName() + ", next = " + var6);
      }

      if (var6 == null) {
         this.getChildren().add(var1);
         var1.setOriginalParent(var1.getParent());
         var1.setParent(this);
      } else {
         int var7 = this.getChildren().indexOf(var6);
         int var8 = this.helper.getPropertyIndex(var6.getElementName());
         if (debug) {
            System.out.println("indx = " + var7 + " propIndex = " + var5 + " nextPropIndex = " + var8 + " size = " + this.getChildren().size());
         }

         boolean var9 = false;
         boolean var10 = false;
         int var11;
         ReaderEvent2 var12;
         if (var5 >= var8) {
            if (this.helper.isArray(var5)) {
               for(var11 = 0; var11 < this.getChildren().size(); ++var11) {
                  var12 = (ReaderEvent2)this.getChildren().elementAt(var11);
                  if (var1.getElementName().equals(var12.getElementName())) {
                     var7 = var11;
                     var9 = true;
                     if (debug) {
                        System.out.println("Element name: " + var1.getElementName() + " indx = " + var11 + " matching element is true");
                     }

                     if (var1.getParent() != var12.getOriginalParent()) {
                        var10 = false;
                     } else {
                        var10 = true;
                     }

                     if (var3 && var1.getParent() != var12.getOriginalParent()) {
                        break;
                     }
                  }
               }
            }

            if (!var3 || !var9 || var10) {
               ++var7;
            }
         } else if (var3 && this.helper.isArray(var5)) {
            for(var11 = 0; var11 < this.getChildren().size(); ++var11) {
               var12 = (ReaderEvent2)this.getChildren().elementAt(var11);
               if (var1.getElementName().equals(var12.getElementName()) && var1.getParent() != var12.getOriginalParent()) {
                  var7 = var11;
                  break;
               }
            }
         }

         if (var7 < this.getChildren().size() && var4) {
            this.getChildren().add(var7, var1);
            if (debug) {
               System.out.println("Add element to children at indx " + var7);
            }
         } else {
            this.getChildren().add(var1);
            if (debug) {
               System.out.println("Add element to children at end ");
            }
         }

         var1.setOriginalParent(var1.getParent());
         var1.setParent(this);
         if (this.info.getPrefix() != null) {
            this.setPrefixOnAdopt(var1, this.info.getPrefix());
         }

      }
   }

   public void orderChildren() {
      int var1;
      ReaderEvent2 var2;
      int var3;
      ReaderEvent2 var4;
      for(var1 = 0; var1 < this.getChildren().size(); ++var1) {
         var2 = (ReaderEvent2)this.getChildren().elementAt(var1);
         if (var2.isDiscarded() && var2.getChildren().size() > 0) {
            this.getChildren().addAll(var2.getChildren());

            for(var3 = 0; var3 < var2.getChildren().size(); ++var3) {
               var4 = (ReaderEvent2)var2.getChildren().elementAt(var3);
               var4.setParent(var2);
            }

            var2.getChildren().clear();
         }
      }

      for(var1 = this.getChildren().size() - 1; var1 > 1; --var1) {
         var2 = (ReaderEvent2)this.getChildren().elementAt(var1);
         if (var2.isDiscarded()) {
            this.getChildren().remove(var1);
         }
      }

      SchemaHelper var9 = this.helper;
      int var10 = this.getChildren().size();

      for(var3 = 0; var3 < this.getChildren().size(); ++var3) {
         for(int var11 = var3 + 1; var11 < var10; ++var11) {
            ReaderEvent2 var5 = (ReaderEvent2)this.getChildren().elementAt(var3);
            int var6 = var9.getPropertyIndex(var5.getElementName());
            ReaderEvent2 var7 = (ReaderEvent2)this.getChildren().elementAt(var11);
            int var8 = var9.getPropertyIndex(var7.getElementName());
            if (var8 != -1 && var8 < var6) {
               this.getChildren().removeElementAt(var11);
               this.getChildren().insertElementAt(var7, var3);
            }
         }
      }

      for(var3 = 0; var3 < this.getChildren().size(); ++var3) {
         var4 = (ReaderEvent2)this.getChildren().elementAt(var3);
         var4.orderChildren();
      }

   }

   private void removeNullAttribIfValueNotNull(ReaderEvent2 var1) {
      char[] var2 = var1.getCharacters();
      if (var2 != null && String.valueOf(var2).trim().length() != 0) {
         this.info.removeNillableAttribute();
      }
   }

   void mergeProperty(ReaderEvent2 var1, int var2, boolean var3) {
      if (debug) {
         System.out.println("mergeProperty: " + var1);
      }

      int var4 = var1.getPropertyIndex();
      if (!this.isArray(var4)) {
         if (debug) {
            System.out.println("\nsimple property, not an array: " + var1);
         }

         if (var3) {
            this.removeNullAttribIfValueNotNull(var1);
         }

         this.setCharacters(var1.getCharacters());
         if (debug) {
            System.out.println(".. characters =  " + this.getTextCharacters());
         }

      } else {
         if (debug) {
            System.out.println("\narray property: " + var1);
         }

         int var5 = this.getParent().getChildren().indexOf(this);
         if (var1.getCharacters() != null) {
            if (var5 == this.getParent().getChildren().size() - 1) {
               this.getParent().getChildren().add(var1);
               var1.setOriginalParent(var1.getParent());
               var1.setParent(this.getParent());
            } else {
               ReaderEvent2 var6 = this;
               if (debug) {
                  System.out.println("evaluate, index = " + var5);
               }

               for(ReaderEvent2 var7 = this.getParent().getNextChild(this); var7 != null; var7 = this.getParent().getNextChild(var7)) {
                  if (debug) {
                     System.out.println("evaluate " + var7 + ", val = " + var7.getTextCharacters());
                  }

                  if (var7.getPropertyIndex() != this.getPropertyIndex()) {
                     break;
                  }

                  var6 = var7;
               }

               var5 = this.getParent().getChildren().indexOf(var6);
               if (debug) {
                  System.out.println("evaluate, new index = " + var5);
               }

               if (var5 == this.getParent().getChildren().size() - 1) {
                  this.getParent().getChildren().add(var1);
                  var1.setOriginalParent(var1.getParent());
                  var1.setParent(this.getParent());
               } else {
                  if (!this.getParent().isMergeRulePrependDefined()) {
                     ++var5;
                  } else if (debug) {
                     System.out.println("***** prepend is defined");
                  }

                  this.getParent().getChildren().add(var5, var1);
                  var1.setOriginalParent(var1.getParent());
                  var1.setParent(this.getParent());
               }
            }
         }
      }
   }

   void removeProperty(ReaderEvent2 var1) {
      if (debug) {
         System.out.println("removeProperty: " + this);
      }

      this.discard = true;
   }

   void replaceProperty(ReaderEvent2 var1) {
      if (debug) {
         System.out.println("replaceProperty: " + this);
      }

      this.getReaderEventInfo().setCharacters(var1.getReaderEventInfo().getCharacters());
   }

   void replacePropertyWithOverride(ReaderEvent2 var1) {
      if (debug) {
         System.out.println("replacePropertyWithOverride: " + this);
      }

      this.getReaderEventInfo().setCharacters(var1.getReaderEventInfo().getKeyOverrideCharacters());
   }

   boolean hasNextChild(int var1) {
      Iterator var2 = this.getChildren().iterator();

      ReaderEvent2 var3;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         var3 = (ReaderEvent2)var2.next();
      } while(var3.getPropertyIndex() <= var1);

      return true;
   }

   boolean isArray(int var1) {
      return this.helper.isArray(var1);
   }

   boolean isBean(int var1) {
      return this.helper.isBean(var1);
   }

   boolean hasKey(int var1) {
      return this.helper.isKey(var1);
   }

   boolean isAttribute(int var1) {
      return this.helper.isAttribute(var1);
   }

   String getXpath() {
      return this.getXpath(false);
   }

   String getXpath(boolean var1) {
      if (this.getParent() == null) {
         return "";
      } else if (this.isSimpleKey()) {
         if (this.info.getAttributeCount() > 0) {
            for(int var5 = 0; var5 < this.info.getAttributeCount(); ++var5) {
               int var3 = this.helper.getPropertyIndex(this.info.getAttributeLocalName(var5));
               if (this.helper.isKey(var3)) {
                  StringBuffer var4 = new StringBuffer(this.getParent().getXpath());
                  var4.append("/").append(this.getElementName()).append("[");
                  var4.append(this.info.getAttributeLocalName(var5)).append("=").append(this.info.getAttributeValue(var5));
                  var4.append("]");
                  return var4.toString();
               }
            }
         }

         return this.getParent().getXpath() + "/[" + this.getElementName() + "=\"" + this.getTrimmedTextCharacters() + "\"]";
      } else if (this.isKeyChoice()) {
         return this.getParent().getXpath() + "/[" + this.getElementName() + "=\"" + this.getTrimmedTextCharacters() + "\"]";
      } else if (this.isKeyComponent()) {
         return this.getParent().getXpath() + "/" + this.getParent().getCompositeXpath(var1);
      } else {
         if (this.isBeanKey()) {
            for(ReaderEvent2 var2 = this.getNextChild(-1); var2 != null; var2 = this.getNextChild(var2)) {
               if (var2.isSimpleKey() || var2.isKeyComponent() || var2.isKeyChoice()) {
                  return this.getParent().getXpath() + "/" + this.getElementName() + "/[" + var2.getElementName() + "=\"" + var2.getTrimmedTextCharacters() + "\"]";
               }
            }
         }

         return this.getParent().getXpath() + "/" + this.getElementName();
      }
   }

   String getCompositeXpath(boolean var1) {
      String[] var2 = this.helper.getKeyElementNames();
      StringBuffer var3 = new StringBuffer("[");

      for(int var4 = 0; var4 < var2.length; ++var4) {
         String var5 = var2[var4];
         var3.append("[");

         for(int var6 = 0; var6 < this.getChildren().size(); ++var6) {
            ReaderEvent2 var7 = (ReaderEvent2)this.getChildren().elementAt(var6);
            boolean var8 = var1 ? var7.getReaderEventInfo().isKeyCharacters() : true;
            if (var5.equals(var7.getElementName()) && var8) {
               var3.append("\"");
               String var9 = var7.getTrimmedTextCharacters();
               if (var9 != null) {
                  var3.append(var9);
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
      if (debug) {
         System.out.println("getCompositeXpath return: " + var3);
      }

      return var3.toString();
   }

   ReaderEvent2 findEvent(String var1) {
      Iterator var2 = this.getChildren().iterator();

      ReaderEvent2 var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (ReaderEvent2)var2.next();
      } while(!var3.getXpath().equals(var1));

      return var3;
   }

   ReaderEvent2 getChild(int var1) {
      Iterator var2 = this.getChildren().iterator();

      ReaderEvent2 var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (ReaderEvent2)var2.next();
      } while(var3.getPropertyIndex() != var1);

      return var3;
   }

   ReaderEvent2 getMatchingChild(int var1) {
      Iterator var2 = this.getChildren().iterator();

      ReaderEvent2 var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (ReaderEvent2)var2.next();
      } while(var3.getPropertyIndex() != var1);

      return var3;
   }

   ReaderEvent2 getPrevChild(int var1) {
      ReaderEvent2 var2 = null;
      Iterator var3 = this.getChildren().iterator();

      ReaderEvent2 var4;
      do {
         if (!var3.hasNext()) {
            return var2;
         }

         var4 = (ReaderEvent2)var3.next();
         if (var4.getPropertyIndex() < var1) {
            var2 = var4;
         }
      } while(var4.getPropertyIndex() < var1);

      return var2;
   }

   ReaderEvent2 getPrevChild(ReaderEvent2 var1) {
      int var2 = this.getChildren().indexOf(var1);
      --var2;
      return var2 < 0 ? null : (ReaderEvent2)this.getChildren().get(var2);
   }

   ReaderEvent2 getNextChild(int var1) {
      Iterator var2 = this.getChildren().iterator();

      ReaderEvent2 var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (ReaderEvent2)var2.next();
      } while(var3.getPropertyIndex() <= var1);

      return var3;
   }

   ReaderEvent2 getNextChild(ReaderEvent2 var1) {
      int var2 = this.getChildren().indexOf(var1);
      ++var2;
      return this.getChildren().size() == var2 ? null : (ReaderEvent2)this.getChildren().get(var2);
   }

   int getPropertyIndex() {
      return this.getSchemaHelper().getPropertyIndex(this.getElementName());
   }

   boolean isMergeRuleIgnoreSourceDefined() {
      int var1 = this.getPropertyIndex();
      return this.getSchemaHelper().isMergeRuleIgnoreSourceDefined(var1);
   }

   boolean isMergeRuleIgnoreTargetDefined() {
      int var1 = this.getPropertyIndex();
      return this.getSchemaHelper().isMergeRuleIgnoreTargetDefined(var1);
   }

   boolean isMergeRulePrependDefined() {
      int var1 = this.getPropertyIndex();
      return this.getSchemaHelper().isMergeRulePrependDefined(var1);
   }

   void adoptChildren(ReaderEvent2 var1) {
      if (debug) {
         System.out.println("\nadoptChildren from source = " + var1);
      }

      if (var1.getChildren().size() != 0) {
         Iterator var2 = var1.getChildren().iterator();

         while(var2.hasNext()) {
            ReaderEvent2 var3 = (ReaderEvent2)var2.next();
            var3.setOriginalParent(var3.getParent());
            var3.setParent(this);
         }

         this.getChildren().addAll(var1.getChildren());
      }
   }

   void adoptChild(ReaderEvent2 var1, ReaderEvent2 var2) {
      if (debug) {
         System.out.println("\nadoptChild from sourceChild = " + var1 + ", targetChild = " + var2);
      }

      int var3 = this.getChildren().indexOf(var2);
      int var4 = var1.getPropertyIndex();
      int var5 = var2.getPropertyIndex();
      if (var4 < var5) {
         this.getChildren().add(var3, var1);
      }

      assert var4 != var5;

      if (var4 > var5) {
         ++var3;
         this.getChildren().add(var3, var1);
      }

      this.orphanChild(var1);
      var1.setOriginalParent(var1.getParent());
      var1.setParent(this);
   }

   private void setPrefixOnAdopt(ReaderEvent2 var1, String var2) {
      if (var1.info.getPrefix() == null) {
         var1.info.setPrefix(var2);
      }

      Iterator var3 = var1.getChildren().iterator();

      while(var3.hasNext()) {
         ReaderEvent2 var4 = (ReaderEvent2)var3.next();
         this.setPrefixOnAdopt(var4, var2);
      }

   }

   public static void main(String[] var0) throws Exception {
      Location var1 = new Location() {
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
            return "<no public id>";
         }

         public String getSystemId() {
            return "<no system id>";
         }
      };
      StringBuffer var2 = new StringBuffer("/weblogic-connector xmlns=\"http://www.bea.com/ns/weblogic/90\"/outbound-resource-adapter/connection-definition-group/[connection-factory-interface=\"javax.sql.DataSource\"]/connection-instance/jndi-name");
      System.out.println("\nxpath = " + var2 + "\nresults in:\n");
      (new ReaderEvent2(var2, new ReaderEvent2(7, var1), (SchemaHelper)null, "val1", var1)).toXML(System.out);
   }
}
