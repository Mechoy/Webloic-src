package weblogic.wsee.addressing.wsdl;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.wsee.addressing.AddressingHandler;
import weblogic.wsee.util.AddressingUtil;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.wsdl.WsdlExtension;
import weblogic.wsee.wsdl.WsdlWriter;

public class EndpointReferenceWsdlExtension implements WsdlExtension {
   private final boolean verbose = Verbose.isVerbose(AddressingHandler.class);
   private Element originalEndponitReferenceElement = null;
   private String addressLocation = null;
   public static final String KEY = "EndPonit-Reference";

   public EndpointReferenceWsdlExtension(Node var1) {
      this.originalEndponitReferenceElement = (Element)var1;
   }

   public String getKey() {
      return "EndPonit-Reference";
   }

   public void write(Element var1, WsdlWriter var2) {
      String var3 = var2.getEndpointURL(this.getAddressLocation());
      this.updateWSAAddressContent(this.originalEndponitReferenceElement, var3);
      Node var4 = var1.getOwnerDocument().importNode(this.originalEndponitReferenceElement, true);
      var1.appendChild(var4);
   }

   public String getAddressLocation() {
      if (this.addressLocation != null) {
         return this.addressLocation;
      } else if (this.originalEndponitReferenceElement != null) {
         Element var1 = AddressingUtil.retrieveAddressElement(this.originalEndponitReferenceElement);
         return var1 != null ? var1.getFirstChild().getNodeValue() : null;
      } else {
         return null;
      }
   }

   public void setAddressLocation(String var1) {
      this.addressLocation = var1;
   }

   private void updateWSAAddressContent(Element var1, String var2) {
      if (var2 != null) {
         Element var3 = AddressingUtil.retrieveAddressElement(var1);
         if (var3 != null) {
            if (var3.getFirstChild() == null) {
               var3.appendChild(var3.getOwnerDocument().createTextNode(var2));
            } else if (var3.getFirstChild().getNodeType() == 3) {
               int var4 = var3.getChildNodes().getLength();

               for(int var5 = 0; var5 < var4 && var3.getChildNodes() != null; ++var5) {
                  var3.removeChild(var3.getChildNodes().item(0));
               }

               var3.appendChild(var3.getOwnerDocument().createTextNode(var2));
            } else if (this.verbose) {
               Verbose.log((Object)("[WARNING] Unable to found address element or wrong address element type for [" + var3.getNamespaceURI() + "]" + var3.getLocalName()));
            }

         }
      }
   }
}
