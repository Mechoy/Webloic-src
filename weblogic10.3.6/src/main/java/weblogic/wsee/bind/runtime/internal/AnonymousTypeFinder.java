package weblogic.wsee.bind.runtime.internal;

import com.bea.staxb.buildtime.internal.bts.XmlTypeName;
import com.bea.xml.SchemaGlobalElement;
import com.bea.xml.SchemaProperty;
import com.bea.xml.SchemaType;
import com.bea.xml.SchemaTypeSystem;
import javax.xml.namespace.QName;
import weblogic.wsee.bind.internal.FormQualifiedHelper;

class AnonymousTypeFinder {
   private static final char NS_ANON_NAME_SEPARATOR = ':';
   private static final char SEP = '>';
   private String mNamespace;
   private SchemaTypeSystem mSts;

   public AnonymousTypeFinder(SchemaTypeSystem var1) {
      this.mSts = var1;
   }

   public SchemaType getTypeNamed(String var1) {
      XmlTypeName var2 = XmlTypeName.forString(var1);
      SchemaType var3 = var2.findTypeIn(this.mSts);
      if (var3 != null) {
         return var3;
      } else if (var1.indexOf(93) != -1) {
         return null;
      } else if (var1.startsWith('y' + ".")) {
         return null;
      } else {
         int var4 = var1.lastIndexOf(58);
         this.mNamespace = var1.substring(0, var4);
         return this.findSchemaType(var1.substring(var4 + 1)).getSchemaType();
      }
   }

   public SchemaProperty getHiddenArrayElementComponentTypeNamed(String var1) {
      int var2 = var1.indexOf(91);
      if (var2 == -1) {
         return null;
      } else {
         var1 = var1.substring(0, var2);
         int var3 = var1.lastIndexOf(58);
         this.mNamespace = var1.substring(0, var3);
         ElementNode var4 = this.findSchemaElement(var1.substring(var3 + 1));

         try {
            return var4.getSchemaProperty();
         } catch (IllegalStateException var6) {
            var6.printStackTrace();
            return null;
         }
      }
   }

   private TypeNode findSchemaType(String var1) {
      return (TypeNode)(var1.startsWith(">") ? this.findSchemaElement(var1.substring(1)).getTypeNode() : new GlobalTypeNode(var1));
   }

   private ElementNode findSchemaElement(String var1) {
      int var2 = var1.lastIndexOf(62);
      if (var2 == -1) {
         return new GlobalElementNode(var1);
      } else {
         TypeNode var3 = this.findSchemaType(var1.substring(0, var2));
         return new LocalElementNode(var3, var1.substring(var2 + 1));
      }
   }

   private class GlobalElementNode extends ElementNode {
      private String mName;

      public GlobalElementNode(String var2) {
         super(null);
         if (var2 == null) {
            throw new IllegalArgumentException("null elementName");
         } else {
            this.mName = var2;
         }
      }

      public SchemaType getSchemaType() {
         SchemaGlobalElement var1 = AnonymousTypeFinder.this.mSts.findElement(this.qname(this.mName));
         if (var1 == null) {
            throw new IllegalStateException("no element named " + var1);
         } else {
            SchemaType var2 = var1.getType();
            if (var2 == null) {
               throw new IllegalStateException(var1.getName() + " has no type");
            } else {
               return var2;
            }
         }
      }

      public TypeNode getTypeNode() {
         return AnonymousTypeFinder.this.new TypeNode(this.getSchemaType());
      }

      public SchemaProperty getSchemaProperty() {
         throw new IllegalStateException();
      }
   }

   private class LocalElementNode extends ElementNode {
      private TypeNode mEnclosingType;
      private String mName;

      public LocalElementNode(TypeNode var2, String var3) {
         super(null);
         if (var2 == null) {
            throw new IllegalArgumentException("null enclosingType");
         } else if (var3 == null) {
            throw new IllegalArgumentException("null elementName");
         } else {
            this.mEnclosingType = var2;
            this.mName = var3;
         }
      }

      public SchemaType getSchemaType() {
         SchemaProperty var1 = this.getSchemaProperty();
         return var1.getType();
      }

      public SchemaProperty getSchemaProperty() {
         SchemaType var1 = this.mEnclosingType.getSchemaType();
         if (var1 == null) {
            throw new IllegalStateException("enclosing type returned null");
         } else {
            SchemaProperty var2 = FormQualifiedHelper.getInstance().getElementProperty(var1, this.mName);
            if (var2 == null) {
               throw new IllegalStateException("type named '" + var1.getName() + "' does not have local element named " + this.mName);
            } else {
               return var2;
            }
         }
      }

      public TypeNode getTypeNode() {
         return AnonymousTypeFinder.this.new TypeNode(this.getSchemaType());
      }
   }

   private class GlobalTypeNode extends TypeNode {
      String mNamed;

      public GlobalTypeNode(String var2) {
         super();
         if (var2 == null) {
            throw new IllegalArgumentException();
         } else {
            this.mNamed = var2;
         }
      }

      public SchemaType getSchemaType() {
         SchemaType var1 = AnonymousTypeFinder.this.mSts.findType(this.qname(this.mNamed));
         if (var1 == null) {
            throw new IllegalStateException("could not find schema type named " + this.qname(this.mNamed));
         } else {
            return var1;
         }
      }
   }

   private abstract class ElementNode extends ParseNode {
      private ElementNode() {
         super(null);
      }

      public abstract SchemaProperty getSchemaProperty();

      public abstract TypeNode getTypeNode();

      // $FF: synthetic method
      ElementNode(Object var2) {
         this();
      }
   }

   private class TypeNode extends ParseNode {
      private SchemaType schemaType;

      public TypeNode() {
         super(null);
      }

      public TypeNode(SchemaType var2) {
         super(null);
         this.schemaType = var2;
      }

      public SchemaType getSchemaType() {
         return this.schemaType;
      }
   }

   private abstract class ParseNode {
      private ParseNode() {
      }

      public abstract SchemaType getSchemaType();

      protected QName qname(String var1) {
         return new QName(AnonymousTypeFinder.this.mNamespace, var1);
      }

      // $FF: synthetic method
      ParseNode(Object var2) {
         this();
      }
   }
}
