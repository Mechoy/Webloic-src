package weblogic.application.descriptor;

import java.util.ArrayList;
import java.util.Iterator;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;

public class ReaderEventInfo {
   private int eventType;
   private Location location;
   private String elementName;
   private String prefix;
   private char[] characters;
   private Attributes attributes;
   private String charEncodingScheme;
   private String inputEncoding;
   Namespaces namespaces;
   private StringBuffer comments;
   private boolean keyCharacters;
   private boolean keyOverride;
   private char[] keyOverrideCharacters;

   public ReaderEventInfo(int var1, Location var2) {
      this.eventType = var1;
      this.location = var2;
   }

   public ReaderEventInfo(int var1, String var2, Location var3) {
      this(var1, var3);
      this.elementName = var2;
   }

   public String getElementName() {
      return this.elementName;
   }

   public void setElementName(String var1) {
      this.elementName = var1;
   }

   public char[] getCharacters() {
      return this.characters;
   }

   public void setCharacters(char[] var1) {
      if (var1 != null) {
         this.characters = new char[var1.length];
         System.arraycopy(var1, 0, this.characters, 0, var1.length);
      }
   }

   void appendCharacters(char[] var1) {
      if (var1 != null && (var1.length != 0 || this.characters == null)) {
         if (this.characters != null) {
            char[] var2 = new char[this.characters.length + var1.length];
            System.arraycopy(this.characters, 0, var2, 0, this.characters.length);
            System.arraycopy(var1, 0, var2, this.characters.length, var1.length);
            this.characters = var2;
         } else if (var1.length == 0) {
            this.characters = new char[0];
         } else {
            this.setCharacters(var1);
         }

      }
   }

   public boolean hasComment() {
      return this.comments != null;
   }

   public String getComments() {
      return this.hasComment() ? this.comments.toString() : null;
   }

   public void setComments(String var1) {
      if (this.comments == null) {
         this.comments = new StringBuffer();
      }

      this.comments.append(var1);
   }

   public String getCharactersAsString() {
      return this.characters != null ? new String(this.getCharacters()) : "";
   }

   public Location getLocation() {
      return this.location;
   }

   public String getPrefix() {
      return this.prefix;
   }

   public void setPrefix(String var1) {
      this.prefix = var1;
   }

   public int getEventType() {
      return this.eventType;
   }

   public int getAttributeCount() {
      return this.getAttributes().getAttributeCount();
   }

   Attributes getAttributes() {
      if (this.attributes == null) {
         this.attributes = new Attributes();
      }

      return this.attributes;
   }

   public void removeNillableAttribute() {
      if (this.attributes != null) {
         this.attributes.removeNillable();
      }

   }

   public void setAttributeCount(int var1) {
      this.getAttributes().setAttributeCount(var1);
   }

   public String getAttributeLocalName(int var1) {
      return this.getAttributes().getAttributeLocalName(var1);
   }

   public void setAttributeLocalName(String var1, int var2) {
      this.getAttributes().setAttributeLocalName(var1, var2);
   }

   public String getAttributeNamespace(int var1) {
      return this.getAttributes().getAttributeNamespace(var1);
   }

   public void setAttributeNamespace(String var1, int var2) {
      this.getAttributes().setAttributeNamespace(var1, var2);
   }

   public String getAttributePrefix(int var1) {
      return this.getAttributes().getAttributePrefix(var1);
   }

   public void setAttributePrefix(String var1, int var2) {
      this.getAttributes().setAttributePrefix(var1, var2);
   }

   public String getAttributeType(int var1) {
      return this.getAttributes().getAttributeType(var1);
   }

   public boolean isAttributeSpecified(int var1) {
      return this.getAttributes().isAttributeSpecified(var1);
   }

   public String getAttributeValue(int var1) {
      return this.getAttributes().getAttributeValue(var1);
   }

   public void setAttributeValue(String var1, int var2) {
      this.getAttributes().setAttributeValue(var1, var2);
   }

   public String getAttributeValue(String var1, String var2) {
      return this.getAttributes().getAttributeValue(var1, var2);
   }

   public void setAttributeValue(String var1, String var2, String var3) {
      this.getAttributes().setAttributeValue(var1, var2, var3);
   }

   public QName getAttributeName(int var1) {
      return this.getAttributes().getAttributeName(var1);
   }

   public String getCharacterEncodingScheme() {
      return this.charEncodingScheme;
   }

   public void setCharacterEncodingScheme(String var1) {
      this.charEncodingScheme = var1;
   }

   public int getNamespaceCount() {
      return this.getNamespaces().getNamespaceCount();
   }

   public String getNamespacePrefix(int var1) {
      return this.getNamespaces().getNamespacePrefix(var1);
   }

   public String getNamespaceURI(int var1) {
      return this.getNamespaces().getNamespaceURI(var1);
   }

   public boolean hasNamespaceURI(String var1) {
      return this.getNamespaces().hasNamespaceURI(var1);
   }

   public void setNamespaceCount(int var1) {
      this.getNamespaces().setNamespaceCount(var1);
   }

   Namespaces getNamespaces() {
      if (this.namespaces == null) {
         this.namespaces = new Namespaces();
      }

      return this.namespaces;
   }

   public void clearNamespaces() {
      this.namespaces = new Namespaces();
   }

   public void setNamespaceURI(String var1, String var2) {
      if (var2 != null) {
         this.getNamespaces().setNamespaceURI(var1, var2);
      }
   }

   public javax.xml.namespace.NamespaceContext getNamespaceContext() {
      return new NamespaceContext(this.getNamespaces());
   }

   public String getEncoding() {
      return this.inputEncoding;
   }

   public void setEncoding(String var1) {
      this.inputEncoding = var1;
   }

   public boolean isKeyCharacters() {
      return this.keyCharacters;
   }

   public void setKeyCharacters(boolean var1) {
      this.keyCharacters = var1;
   }

   public boolean isKeyOverride() {
      return this.keyOverride;
   }

   public void setKeyOverride(boolean var1) {
      this.keyOverride = var1;
   }

   public char[] getKeyOverrideCharacters() {
      return this.keyOverrideCharacters;
   }

   public void setKeyOverrideCharacters(char[] var1) {
      if (var1 != null) {
         this.keyOverrideCharacters = new char[var1.length];
         System.arraycopy(var1, 0, this.keyOverrideCharacters, 0, var1.length);
      }
   }

   class NamespaceContext implements javax.xml.namespace.NamespaceContext {
      Namespaces namespaces;

      NamespaceContext(Namespaces var2) {
         this.namespaces = var2;
      }

      public String getNamespaceURI(String var1) {
         if (var1 == null) {
            throw new IllegalArgumentException("The prefix may not be null");
         } else {
            return this.namespaces.getNamespaceURI(var1);
         }
      }

      public String getPrefix(String var1) {
         if (var1 == null) {
            throw new IllegalArgumentException("The uri may not be null.");
         } else {
            for(int var2 = 0; var2 < this.namespaces.getNamespaceCount(); ++var2) {
               if (var1.equals(this.namespaces.getNamespaceURI(var2))) {
                  return this.namespaces.getNamespacePrefix(var2);
               }
            }

            return null;
         }
      }

      public Iterator getPrefixes(String var1) {
         ArrayList var2 = new ArrayList();

         for(int var3 = 0; var3 < this.namespaces.getNamespaceCount(); ++var3) {
            if (var1.equals(this.namespaces.getNamespaceURI(var3))) {
               var2.add(this.namespaces.getNamespacePrefix(var3));
            }
         }

         return var2.iterator();
      }
   }

   class Namespaces {
      private int namespaceCount;
      private String[] namespacePrefixes;
      private String[] namespaceValues;

      public void ensureSpaceAvail(int var1) {
         int var2 = this.namespaceValues == null ? 0 : this.namespaceValues.length;
         if (var1 > var2) {
            int var3 = var1 > 10 ? var1 * 2 : 10;
            if (this.namespaceValues == null) {
               this.namespaceValues = new String[var3];
               this.namespacePrefixes = new String[var3];
               return;
            }

            String[] var4 = new String[var3];
            System.arraycopy(this.namespaceValues, 0, var4, 0, var2);
            this.namespaceValues = var4;
            var4 = new String[var3];
            System.arraycopy(this.namespacePrefixes, 0, var4, 0, var2);
            this.namespacePrefixes = var4;
         }

      }

      public int getNamespaceCount() {
         return this.namespaceCount;
      }

      public String getNamespacePrefix(int var1) {
         return this.namespaceCount == 0 ? null : this.namespacePrefixes[var1];
      }

      public String getNamespaceURI(int var1) {
         return this.namespaceCount == 0 ? null : this.namespaceValues[var1];
      }

      public String getNamespaceURI() {
         return this.getNamespaceURI(0);
      }

      public String getNamespaceURI(String var1) {
         for(int var2 = 0; var2 < this.namespaceCount; ++var2) {
            if (this.namespacePrefixes[var2] == var1 && this.namespaceValues[var2] != null) {
               return this.namespaceValues[var2];
            }

            if (this.namespacePrefixes[var2] != null && var1 != null && var1.equals(this.namespacePrefixes[var2])) {
               return this.namespaceValues[var2];
            }
         }

         return null;
      }

      public boolean hasNamespaceURI(String var1) {
         for(int var2 = 0; var2 < this.namespaceCount; ++var2) {
            if (this.namespaceValues[var2] != null && this.namespaceValues[var2].equals(var1)) {
               return true;
            }
         }

         return false;
      }

      public void setNamespaceCount(int var1) {
         if (var1 > 0) {
            this.ensureSpaceAvail(var1);
         }

         if (this.namespaceCount < var1) {
            this.namespaceCount = var1;
         }

      }

      public void setNamespaceURI(String var1, String var2) {
         if (var2 != null && this.namespaceCount != 0) {
            int var3;
            if (var1 != null) {
               for(var3 = 0; var3 < this.namespaceCount; ++var3) {
                  if (var1.equals(this.namespacePrefixes[var3]) && var2.equals(this.namespaceValues[var3])) {
                     return;
                  }
               }
            } else {
               for(var3 = 0; var3 < this.namespaceCount; ++var3) {
                  if (var2.equals(this.namespaceValues[var3])) {
                     return;
                  }
               }
            }

            for(var3 = 0; var3 < this.namespaceCount; ++var3) {
               if (this.namespaceValues[var3] == null) {
                  this.namespaceValues[var3] = var2;
                  this.namespacePrefixes[var3] = var1;
                  return;
               }
            }

            this.setNamespaceCount(this.getNamespaceCount() + 1);
            this.namespaceValues[this.getNamespaceCount() - 1] = var2;
            this.namespacePrefixes[this.getNamespaceCount() - 1] = var1;
         }
      }

      void copy(Namespaces var1) {
         if (var1.namespaceCount > 0) {
            this.setNamespaceCount(var1.namespaceCount);

            for(int var2 = 0; var2 < this.namespaceCount; ++var2) {
               this.namespacePrefixes[var2] = var1.namespacePrefixes[var2];
               this.namespaceValues[var2] = var1.namespaceValues[var2];
            }
         }

      }
   }

   class Attributes {
      private int attributeCount;
      private String[] attributeNames;
      private String[] attributeValues;
      private String[] attributeUris;
      private String[] attributePrefixes;

      public void ensureSpaceAvail(int var1) {
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

            for(var4 = 0; var4 < this.attributeCount; ++var4) {
               if (this.attributeValues[var4] == null) {
                  if (var2 != null) {
                     this.attributeUris[var4] = var2;
                  }

                  this.attributeNames[var4] = var3;
                  this.attributeValues[var4] = var1;
                  return;
               }
            }

            this.ensureSpaceAvail(this.attributeCount++);
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

      public void removeNillable() {
         if (this.attributeNames != null && this.attributeValues != null && this.attributeUris != null && this.attributePrefixes != null) {
            for(int var2 = 0; var2 < this.attributeNames.length; ++var2) {
               String var1 = this.attributeNames[var2];
               if ("nil".equals(var1) && "true".equals(this.attributeValues[var2]) && "http://www.w3.org/2001/XMLSchema-instance".equals(this.attributeUris[var2])) {
                  --this.attributeCount;
                  String[] var3 = new String[this.attributeNames.length];
                  System.arraycopy(this.attributeNames, 0, var3, 0, var2);
                  System.arraycopy(this.attributeNames, var2 + 1, var3, var2 + 1, this.attributeNames.length - (var2 + 1));
                  this.attributeNames = var3;
                  var3 = new String[this.attributeValues.length];
                  System.arraycopy(this.attributeValues, 0, var3, 0, var2);
                  System.arraycopy(this.attributeValues, var2 + 1, var3, var2 + 1, this.attributeValues.length - (var2 + 1));
                  this.attributeValues = var3;
                  var3 = new String[this.attributeUris.length];
                  System.arraycopy(this.attributeUris, 0, var3, 0, var2);
                  System.arraycopy(this.attributeUris, var2 + 1, var3, var2 + 1, this.attributeUris.length - (var2 + 1));
                  this.attributeUris = var3;
                  var3 = new String[this.attributePrefixes.length];
                  System.arraycopy(this.attributePrefixes, 0, var3, 0, var2);
                  System.arraycopy(this.attributePrefixes, var2 + 1, var3, var2 + 1, this.attributePrefixes.length - (var2 + 1));
                  this.attributePrefixes = var3;
               }
            }

         }
      }
   }
}
