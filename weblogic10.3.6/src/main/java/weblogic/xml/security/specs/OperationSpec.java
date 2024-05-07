package weblogic.xml.security.specs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import weblogic.xml.stream.XMLName;

/** @deprecated */
public abstract class OperationSpec extends SpecBase {
   protected ArrayList unrestrictedList = new ArrayList();
   private ElementIdentifier[] unrestricted = null;
   protected ArrayList bodyList = new ArrayList();
   private ElementIdentifier[] body = null;
   protected ArrayList headerList = new ArrayList();
   private ElementIdentifier[] header = null;
   private boolean entireBody = false;

   public boolean entireBody() {
      return this.entireBody;
   }

   public XMLName[] getHeaderElementNames() {
      return getTypeNames(this.headerList);
   }

   public final ElementIdentifier[] getHeaderElementSpecs() {
      if (this.header == null) {
         this.header = listToArray(this.headerList);
      }

      return this.header;
   }

   public final ElementIdentifier[] getBodyElementSpecs() {
      if (this.body == null) {
         this.body = listToArray(this.bodyList);
      }

      return this.body;
   }

   public final ElementIdentifier[] getUnrestrictedElementSpecs() {
      if (this.unrestricted == null) {
         this.unrestricted = listToArray(this.unrestrictedList);
      }

      return this.unrestricted;
   }

   private static ElementIdentifier[] listToArray(ArrayList var0) {
      ElementIdentifier[] var1 = new ElementIdentifier[var0.size()];
      var1 = (ElementIdentifier[])((ElementIdentifier[])var0.toArray(var1));
      return var1;
   }

   public XMLName[] getBodyElementNames() {
      return getTypeNames(this.bodyList);
   }

   public XMLName[] getUnrestrictedElementNames() {
      return getTypeNames(this.unrestrictedList);
   }

   private static final XMLName[] getTypeNames(List var0) {
      XMLName[] var1 = new XMLName[var0.size()];
      Iterator var2 = var0.iterator();

      for(int var3 = 0; var2.hasNext(); ++var3) {
         var1[var3] = ((ElementIdentifier)var2.next()).getXMLName();
      }

      return var1;
   }

   public void addElement(QName var1, String var2) {
      this.addElement(var1.getNamespaceURI(), var1.getLocalPart(), var2);
   }

   public boolean contains(String var1, String var2, String var3) {
      ElementIdentifier var4 = new ElementIdentifier(var1, var2, var3);
      boolean var5;
      if (var3 == null) {
         var5 = this.unrestrictedList.contains(var4);
      } else if ("body".equals(var3)) {
         var5 = this.bodyList.contains(var4);
      } else if ("header".equals(var3)) {
         var5 = this.headerList.contains(var4);
      } else {
         var5 = false;
      }

      return var5;
   }

   public void addElement(String var1, String var2, String var3) {
      if (!ElementIdentifier.validRestriction(var3)) {
         throw new IllegalArgumentException("Illegal restriction type: " + var3);
      } else {
         ElementIdentifier var4 = new ElementIdentifier(var1, var2, var3);
         if (var3 == null) {
            this.addUnrestrictedType(var4);
         } else if ("body".equals(var3)) {
            this.addBodyElement(var4);
         } else {
            if (!"header".equals(var3)) {
               throw new AssertionError("Unsupported restriction:" + var3);
            }

            this.addHeaderElement(var4);
         }

      }
   }

   protected void addHeaderElement(ElementIdentifier var1) {
      this.headerList.add(var1);
      this.header = null;
   }

   protected void setEntireBody(boolean var1) {
      this.entireBody = var1;
   }

   protected void addBodyElement(ElementIdentifier var1) {
      this.bodyList.add(var1);
      this.body = null;
   }

   protected void addUnrestrictedType(ElementIdentifier var1) {
      this.unrestrictedList.add(var1);
      this.unrestricted = null;
   }
}
