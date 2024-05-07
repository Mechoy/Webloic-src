package weblogic.xml.dtdc;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class XmlElement implements GeneratedElement {
   protected List dataElements = new ArrayList();
   protected Map attributeValues = new HashMap();
   protected List subElements = new ArrayList();

   public List getDataElements() {
      return this.dataElements;
   }

   public XmlElement _addDataElement(String var1) {
      DataElement var2 = new DataElement(var1);
      this.getDataElements().add(var2);
      this.getSubElements().add(var2);
      return this;
   }

   public Map getAttributeValues() {
      return this.attributeValues;
   }

   public void toXML(PrintWriter var1) throws IOException {
      this.toXML(var1, 0);
   }

   public void toXML(PrintWriter var1, int var2) throws IOException {
      for(int var3 = var2; var3 > 0; --var3) {
         var1.print("  ");
      }

      var1.print("<" + this.getElementName());
      Iterator var6 = this.getAttributeValues().keySet().iterator();

      while(var6.hasNext()) {
         String var4 = (String)var6.next();
         String var5 = (String)this.attributeValues.get(var4);
         if (var5 != null) {
            var5 = this.convert(var5);
            if (var5.indexOf("\"") != -1) {
               var1.print(" " + var4 + "='" + this.convert(var5) + "'");
            } else {
               var1.print(" " + var4 + "=\"" + this.convert(var5) + "\"");
            }
         }
      }

      if (this.isEmpty()) {
         var1.println("/>");
      } else {
         var1.println(">");
         Iterator var7 = this.getSubElements().iterator();

         while(var7.hasNext()) {
            GeneratedElement var8 = (GeneratedElement)var7.next();
            var8.toXML(var1, var2 + 1);
            if (!var7.hasNext() && var8 instanceof DataElement) {
               var1.println();
            }
         }

         for(int var9 = var2; var9 > 0; --var9) {
            var1.print("  ");
         }

         var1.println("</" + this.getElementName() + ">");
      }
   }

   protected String convert(String var1) {
      return var1;
   }

   public List getSubElements() {
      return this.subElements;
   }

   public abstract boolean isEmpty();

   public boolean equals(Object var1) {
      if (!(var1 instanceof XmlElement)) {
         return false;
      } else {
         XmlElement var2 = (XmlElement)var1;
         if (!var2.getElementName().equals(this.getElementName())) {
            return false;
         } else {
            Iterator var3 = this.getAttributeValues().keySet().iterator();
            Iterator var4 = var2.getAttributeValues().keySet().iterator();

            while(var3.hasNext() && var4.hasNext()) {
               Object var5;
               Object var6;
               if (!(var5 = var3.next()).equals(var6 = var4.next())) {
                  return false;
               }

               if (!this.getAttributeValues().get(var5).equals(this.getAttributeValues().get(var6))) {
                  return false;
               }
            }

            if (var3.hasNext() != var4.hasNext()) {
               return false;
            } else {
               Iterator var7 = this.getSubElements().iterator();
               Iterator var8 = var2.getSubElements().iterator();

               while(var7.hasNext() && var8.hasNext()) {
                  if (!var7.next().equals(var8.next())) {
                     return false;
                  }
               }

               return var7.hasNext() == var8.hasNext();
            }
         }
      }
   }
}
