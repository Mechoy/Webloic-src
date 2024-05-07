package weblogic.xml.security.signature;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import weblogic.xml.security.transforms.C14NTransform;
import weblogic.xml.security.transforms.IncompatibleTransformException;
import weblogic.xml.security.transforms.NodeTransform;
import weblogic.xml.security.transforms.Transform;
import weblogic.xml.security.transforms.Transforms;
import weblogic.xml.security.utils.NamespaceAwareXOS;
import weblogic.xml.security.utils.Observer;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

public class InternalReference extends Reference implements DSIGConstants, Observer {
   private int nesting = 0;
   protected List observed = new LinkedList();
   private Map namespaces;

   public InternalReference(String var1) {
      super(var1);
   }

   public InternalReference(String var1, DigestMethod var2) {
      super(var1, var2);
   }

   public boolean observe(XMLEvent var1) throws XMLStreamException {
      switch (var1.getType()) {
         case 2:
            ++this.nesting;
            break;
         case 4:
            --this.nesting;
      }

      this.observed.add(var1);
      return this.nesting > 0;
   }

   XMLOutputStream getXOS(Transforms var1) throws XMLStreamException {
      XMLOutputStream var2 = null;
      boolean var3 = false;

      for(int var4 = 0; var4 < var1.size(); ++var4) {
         if (var1.get(var4) instanceof NodeTransform) {
            var3 = true;
         }
      }

      if (var3) {
         return var1.getXMLOutputStream();
      } else {
         Transform var9 = var1.get(var1.size() - 1);
         C14NTransform var5 = new C14NTransform(false);
         if (this.namespaces != null) {
            var5.setNamespaces(this.namespaces);
         }

         try {
            var1.remove(var9);
            var1.add(var5);
            var1.add(var9);
            var2 = var1.getXMLOutputStream();
            NamespaceAwareXOS var8 = new NamespaceAwareXOS(var2, this.namespaces);
            ((NamespaceAwareXOS)var8).addPrefix("http://www.w3.org/2000/09/xmldsig#", "dsig");
            var1.remove(var5);
            return var8;
         } catch (IncompatibleTransformException var7) {
            throw new AssertionError(var7);
         }
      }
   }

   protected void process(Transforms var1) throws InvalidReferenceException {
      try {
         XMLOutputStream var2 = this.getXOS(var1);
         Iterator var3 = this.observed.iterator();

         while(var3.hasNext()) {
            XMLEvent var4 = (XMLEvent)var3.next();
            var2.add(var4);
         }

         var2.flush();
      } catch (XMLStreamException var5) {
         throw new InvalidReferenceException(var5, this);
      }
   }

   public boolean consumes() {
      return false;
   }

   void setNamespaces(Map var1) {
      this.namespaces = new HashMap();
      this.namespaces.putAll(var1);
   }
}
