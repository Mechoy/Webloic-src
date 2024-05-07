package weblogic.wsee.wsdl.internal;

import javax.xml.namespace.QName;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.wsdl.WsdlConstants;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlPart;
import weblogic.wsee.wsdl.WsdlReader;
import weblogic.wsee.wsdl.WsdlWriter;
import weblogic.wsee.wsdl.builder.WsdlPartBuilder;

public class WsdlPartImpl extends WsdlBase implements WsdlPartBuilder {
   private String name;
   private QName type;
   private QName element;

   WsdlPartImpl() {
   }

   WsdlPartImpl(String var1) {
      this();
      this.name = var1;
   }

   public String getName() {
      return this.name;
   }

   public QName getType() {
      return this.type;
   }

   public void setType(QName var1) {
      this.type = var1;
   }

   public QName getElement() {
      return this.element;
   }

   public void setElement(QName var1) {
      this.element = var1;
   }

   public void parse(Element var1, String var2) throws WsdlException {
      this.addDocumentation(var1);
      this.name = WsdlReader.getMustAttribute(var1, (String)null, "name");
      Attr var3 = var1.getAttributeNode("type");
      if (var3 != null) {
         this.type = WsdlReader.createQName(var1, var3.getValue());
      }

      Attr var4 = var1.getAttributeNode("element");
      if (var4 != null) {
         this.element = WsdlReader.createQName(var1, var4.getValue());
      }

      if (this.type == null && this.element == null) {
         throw new WsdlException("either type or element must be specified for part " + this.getName());
      } else if (this.type != null && this.element != null) {
         throw new WsdlException("both type and element specified for part " + this.getName() + ". only one can be specified");
      }
   }

   public void write(Element var1, WsdlWriter var2) {
      Element var3 = var2.addChild(var1, "part", WsdlConstants.wsdlNS);
      var2.setAttribute(var3, "name", WsdlConstants.wsdlNS, this.name);
      if (this.type != null) {
         var2.setAttribute(var3, "type", WsdlConstants.wsdlNS, this.type);
      }

      if (this.element != null) {
         var2.setAttribute(var3, "element", WsdlConstants.wsdlNS, this.element);
      }

      this.writeDocumentation(var3, var2);
   }

   public boolean equals(Object var1) {
      if (var1 == null) {
         return false;
      } else if (var1 == this) {
         return true;
      } else if (var1 instanceof WsdlPart) {
         WsdlPart var2 = (WsdlPart)var1;
         if (!this.getName().equals(var2.getName())) {
            return false;
         } else {
            return equalsWithNull(this.getElement(), var2.getElement()) && equalsWithNull(this.getType(), var2.getType());
         }
      } else {
         return false;
      }
   }

   private static boolean equalsWithNull(Object var0, Object var1) {
      if (var0 == null && var1 == null) {
         return true;
      } else {
         return var0 != null && var1 != null ? var0.equals(var1) : false;
      }
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.writeField("name", this.name);
      var1.writeField("type", this.type);
      var1.writeField("element", this.element);
      var1.end();
   }
}
