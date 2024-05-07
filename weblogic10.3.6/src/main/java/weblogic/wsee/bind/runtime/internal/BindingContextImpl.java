package weblogic.wsee.bind.runtime.internal;

import com.bea.staxb.buildtime.internal.bts.XmlTypeName;
import com.bea.staxb.runtime.EncodingStyle;
import com.bea.xml.SchemaGlobalElement;
import com.bea.xml.SchemaProperty;
import com.bea.xml.SchemaType;
import com.bea.xml.SchemaTypeLoader;
import javax.xml.soap.SOAPMessage;
import weblogic.wsee.bind.runtime.BindingContext;

abstract class BindingContextImpl implements BindingContext {
   private int encoding = 0;
   private SOAPMessage wlMessage;
   private final SchemaTypeLoader schemaTypeLoader;

   public BindingContextImpl(SchemaTypeLoader var1) {
      this.schemaTypeLoader = var1;
   }

   public SOAPMessage getMessage() {
      return this.wlMessage;
   }

   public void setMessage(SOAPMessage var1) {
      this.wlMessage = var1;
   }

   public void setEncoding(int var1) {
      if (var1 != 0 && var1 != 1 && var1 != 2) {
         throw new IllegalArgumentException("Unknown encoding style: " + var1);
      } else {
         this.encoding = var1;
      }
   }

   public int getEncoding() {
      return this.encoding;
   }

   protected EncodingStyle convertEncodingStyle(int var1) {
      EncodingStyle var2;
      switch (var1) {
         case 1:
            var2 = EncodingStyle.SOAP11;
            return var2;
         case 2:
            var2 = EncodingStyle.SOAP12;
         default:
            throw new AssertionError("unknown encoding style: " + var1);
      }
   }

   public SchemaType getTypeOfWrappedElement(XmlTypeName var1) {
      if (!var1.isElement()) {
         throw new IllegalArgumentException(var1 + " is not an element");
      } else {
         XmlTypeName var2 = var1.getOuterComponent();
         if (var2 == null) {
            throw new IllegalArgumentException("no outer type for " + var1);
         } else if (!var2.isType()) {
            throw new IllegalArgumentException(var2 + " is not a type");
         } else {
            XmlTypeName var3 = var2.getOuterComponent();
            SchemaType var4 = null;
            if (var3 == null) {
               var4 = this.schemaTypeLoader.findType(var2.getQName());
               if (var4 == null) {
                  throw new IllegalArgumentException(var2 + " is not global type.");
               }
            } else {
               if (!var3.isElement()) {
                  throw new IllegalArgumentException(var3 + " is not an element");
               }

               SchemaGlobalElement var5 = this.schemaTypeLoader.findElement(var3.getQName());
               if (var5 == null) {
                  throw new IllegalArgumentException("no global element " + var3.getQName());
               }

               var4 = var5.getType();
            }

            SchemaProperty[] var8 = var4.getElementProperties();

            for(int var6 = 0; var6 < var8.length; ++var6) {
               String var7 = var8[var6].getName().getLocalPart();
               if (var7.equals(var1.getQName().getLocalPart())) {
                  return var8[var6].getType();
               }
            }

            throw new IllegalArgumentException("no property named '" + var1.getQName() + "' on type " + (var3 != null ? var3.getQName() : var2.getQName()));
         }
      }
   }

   protected boolean isCR205512WrappedArray(Class var1, XmlTypeName var2) {
      if (!var1.isArray()) {
         return false;
      } else {
         XmlTypeName var3 = var2.getOuterComponent();
         if (var3 == null) {
            return false;
         } else {
            SchemaType var4 = var3.findTypeIn(this.schemaTypeLoader);
            if (var4 == null) {
               return false;
            } else if (var4.getProperties()[0].getMaxOccurs() == null) {
               return true;
            } else {
               return var4.getProperties()[0].getMaxOccurs().intValue() > 1;
            }
         }
      }
   }
}
