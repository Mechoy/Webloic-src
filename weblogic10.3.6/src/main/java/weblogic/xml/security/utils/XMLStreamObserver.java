package weblogic.xml.security.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import weblogic.xml.stream.Attribute;
import weblogic.xml.stream.AttributeIterator;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLName;
import weblogic.xml.stream.XMLStreamException;

public class XMLStreamObserver {
   private static final String ATTR_ID = "Id";
   private final List idNamespaces;
   private final Map eventRegistrations;
   private final Map idRegistrations;
   private LLNode activeObservers;
   private int totalRegistrations;

   public XMLStreamObserver() {
      this.eventRegistrations = new HashMap();
      this.idRegistrations = new HashMap();
      this.activeObservers = null;
      this.totalRegistrations = 0;
      this.idNamespaces = new ArrayList();
      this.idNamespaces.add((Object)null);
   }

   /** @deprecated */
   public XMLStreamObserver(String var1) {
      this();
      this.idNamespaces.add(var1);
   }

   public XMLStreamObserver(String[] var1) {
      this();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         String var3 = var1[var2];
         this.idNamespaces.add(var3);
      }

   }

   public void add(XMLEvent var1, Observer var2) {
      LLNode var3 = new LLNode(var2);
      LLNode var4 = (LLNode)this.eventRegistrations.put(var1, var3);
      var3.next = var4;
      ++this.totalRegistrations;
   }

   public void add(String var1, Observer var2) {
      LLNode var3 = new LLNode(var2);
      LLNode var4 = (LLNode)this.idRegistrations.put(var1, var3);
      var3.next = var4;
      ++this.totalRegistrations;
   }

   public boolean observe(XMLEvent var1) throws XMLStreamException {
      if (this.totalRegistrations != 0 && var1.isStartElement()) {
         if (!this.eventRegistrations.isEmpty()) {
            LLNode var2 = (LLNode)this.eventRegistrations.remove(var1);
            if (var2 != null) {
               this.addObservers(var2);
            }
         } else if (!this.idRegistrations.isEmpty()) {
            StartElement var10 = (StartElement)var1;
            AttributeIterator var3 = var10.getAttributes();

            while(var3.hasNext()) {
               Attribute var4 = var3.next();
               XMLName var5 = var4.getName();
               String var6 = var5.getLocalName();
               if ("Id".equals(var6)) {
                  String var7 = var5.getNamespaceUri();
                  if (this.idNamespaces.contains(var7)) {
                     String var8 = var4.getValue();
                     LLNode var9 = (LLNode)this.idRegistrations.remove("#" + var8);
                     if (var9 != null) {
                        this.addObservers(var9);
                        if (this.idRegistrations.isEmpty()) {
                           break;
                        }
                     }
                  }
               }
            }
         }
      }

      return this.notifyObservers(var1);
   }

   private boolean notifyObservers(XMLEvent var1) throws XMLStreamException {
      if (this.activeObservers == null) {
         return false;
      } else {
         boolean var2 = false;
         LLNode var3 = this.activeObservers;

         for(LLNode var4 = null; var3 != null; var3 = var3.next) {
            Observer var5 = var3.observer;
            if (var5.consumes()) {
               var2 = true;
            }

            if (!var5.observe(var1)) {
               if (var4 == null) {
                  this.activeObservers = var3.next;
               } else {
                  var4.next = var3.next;
               }

               --this.totalRegistrations;
            } else {
               var4 = var3;
            }
         }

         return var2;
      }
   }

   private void addObservers(LLNode var1) {
      this.activeObservers = XMLStreamObserver.LLNode.append(this.activeObservers, var1);
   }

   private static class LLNode {
      private final Observer observer;
      private LLNode next;

      public LLNode(Observer var1) {
         this.observer = var1;
      }

      public static int length(LLNode var0) {
         int var1;
         for(var1 = 0; var0 != null; var0 = var0.next) {
            ++var1;
         }

         return var1;
      }

      public static LLNode append(LLNode var0, LLNode var1) {
         if (var0 == null) {
            return var1;
         } else {
            LLNode var2;
            for(var2 = var0; var2.next != null; var2 = var2.next) {
            }

            var2.next = var1;
            return var0;
         }
      }
   }
}
