package weblogic.xml.security.wsse.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import weblogic.xml.security.utils.Observer;
import weblogic.xml.security.wsse.v200207.WSSEConstants;
import weblogic.xml.stream.Attribute;
import weblogic.xml.stream.AttributeIterator;
import weblogic.xml.stream.EndElement;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLName;
import weblogic.xml.stream.XMLStreamException;

public class MappingObserver implements Observer {
   private final SoapStreamState state = new SoapStreamState();
   private final Map unrestrictedElementMap = new HashMap();
   private final Map headerElementMap = new HashMap();
   private final Map bodyElementMap = new HashMap();
   private final Map idElement = new HashMap();
   private boolean duplicateIds = false;
   private static final String ID_ATTR = "Id";
   public static final String UNMATCHABLE_ID = " \bUnidentifiedElement";

   public boolean consumes() {
      return false;
   }

   public Map getElements() {
      return this.unrestrictedElementMap;
   }

   public Map getHeaderElements() {
      return this.headerElementMap;
   }

   public Map getBodyElements() {
      return this.bodyElementMap;
   }

   public SortedSet getElementIds(XMLName var1) {
      return (SortedSet)this.unrestrictedElementMap.get(var1);
   }

   public SortedSet getElementHeaderIds(XMLName var1) {
      return (SortedSet)this.headerElementMap.get(var1);
   }

   public SortedSet getElementBodyIds(XMLName var1) {
      return (SortedSet)this.bodyElementMap.get(var1);
   }

   public XMLName getElementById(String var1) {
      return (XMLName)this.idElement.get(var1);
   }

   public final boolean duplicateIds() {
      return this.duplicateIds;
   }

   public boolean observe(XMLEvent var1) throws XMLStreamException {
      if (var1 == null) {
         return false;
      } else {
         if (var1.isStartElement()) {
            StartElement var2 = (StartElement)var1;
            this.state.update(var2);
            XMLName var3 = var2.getName();
            int var4 = 0;
            AttributeIterator var5 = var2.getAttributes();

            while(var5.hasNext()) {
               Attribute var6 = var5.next();
               XMLName var7 = var6.getName();
               String var8 = var7.getLocalName();
               if ("Id".equals(var8)) {
                  String var9 = var7.getNamespaceUri();
                  if (this.idNamespace(var9)) {
                     ++var4;
                     String var10 = var6.getValue();
                     Object var11 = this.idElement.put(var10, var3);
                     if (var11 != null) {
                        this.duplicateIds = true;
                     }

                     this.recordElement(var3, var10);
                  }
               }
            }

            if (var4 == 0) {
               this.recordElement(var3, " \bUnidentifiedElement");
            }
         } else if (var1.isEndElement()) {
            this.state.update((EndElement)var1);
         }

         return true;
      }
   }

   private boolean idNamespace(String var1) {
      boolean var2 = false;
      if (var1 == null) {
         var2 = true;
      } else {
         for(int var3 = 0; var3 < WSSEConstants.ID_NAMESPACES.length; ++var3) {
            String var4 = WSSEConstants.ID_NAMESPACES[var3];
            if (var1.equals(var4)) {
               var2 = true;
               break;
            }
         }
      }

      return var2;
   }

   private void recordElement(XMLName var1, String var2) {
      Utils.addElement(this.unrestrictedElementMap, var1, var2);
      if (this.state.atTypeLevel()) {
         if (this.state.inHeader()) {
            Utils.addElement(this.headerElementMap, var1, var2);
         } else if (this.state.inBody()) {
            Utils.addElement(this.bodyElementMap, var1, var2);
         }
      }

   }
}
