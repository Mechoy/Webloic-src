package weblogic.wsee.wstx.wsc.common;

import com.sun.xml.ws.developer.MemberSubmissionEndpointReference;
import java.lang.reflect.Field;
import java.util.List;
import javax.xml.ws.EndpointReference;
import javax.xml.ws.wsaddressing.W3CEndpointReference;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public abstract class EndpointReferenceHelper {
   public static EndpointReferenceHelper newInstance(EndpointReference var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("EndpointReference can't be null");
      } else if (var0 instanceof MemberSubmissionEndpointReference) {
         return new MemberSubmissionEndpointReferenceHelper((MemberSubmissionEndpointReference)var0);
      } else if (var0 instanceof W3CEndpointReference) {
         return new W3CEndpointReferenceHelper((W3CEndpointReference)var0);
      } else {
         throw new IllegalArgumentException(var0.getClass() + "is not a supported EndpointReference");
      }
   }

   public abstract String getAddress();

   public abstract Node[] getReferenceParameters();

   static class W3CEndpointReferenceHelper extends EndpointReferenceHelper {
      private static Field address = null;
      private static Field referenceParameters = null;
      private static Class address_class = null;
      private static Class referenceParameters_class = null;
      private static Field uri = null;
      private static Field elements = null;
      W3CEndpointReference epr;

      W3CEndpointReferenceHelper(W3CEndpointReference var1) {
         this.epr = var1;
      }

      public String getAddress() {
         try {
            return (String)uri.get(address.get(this.epr));
         } catch (IllegalAccessException var2) {
            throw new AssertionError(var2);
         }
      }

      public Node[] getReferenceParameters() {
         try {
            return (Node[])((List)elements.get(referenceParameters.get(this.epr))).toArray(new Element[0]);
         } catch (IllegalAccessException var2) {
            throw new AssertionError(var2);
         }
      }

      static {
         try {
            address = W3CEndpointReference.class.getDeclaredField("address");
            address.setAccessible(true);
            referenceParameters = W3CEndpointReference.class.getDeclaredField("referenceParameters");
            referenceParameters.setAccessible(true);
            address_class = Class.forName("javax.xml.ws.wsaddressing.W3CEndpointReference$Address");
            referenceParameters_class = Class.forName("javax.xml.ws.wsaddressing.W3CEndpointReference$Elements");
            uri = address_class.getDeclaredField("uri");
            uri.setAccessible(true);
            elements = referenceParameters_class.getDeclaredField("elements");
            elements.setAccessible(true);
         } catch (Exception var1) {
            throw new AssertionError(var1);
         }
      }
   }

   static class MemberSubmissionEndpointReferenceHelper extends EndpointReferenceHelper {
      MemberSubmissionEndpointReference epr;

      MemberSubmissionEndpointReferenceHelper(MemberSubmissionEndpointReference var1) {
         this.epr = var1;
      }

      public String getAddress() {
         return this.epr.addr.uri;
      }

      public Node[] getReferenceParameters() {
         return (Node[])this.epr.referenceParameters.elements.toArray(new Element[0]);
      }
   }
}
