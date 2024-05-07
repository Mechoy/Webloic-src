package weblogic.wsee.wstx.wsc.common;

import com.sun.xml.ws.developer.MemberSubmissionEndpointReference;
import java.util.ArrayList;
import java.util.List;
import javax.xml.ws.EndpointReference;
import javax.xml.ws.wsaddressing.W3CEndpointReference;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.wsee.wstx.wsat.Transactional;
import weblogic.wsee.wstx.wsat.Transactional.Version;

public abstract class EndpointReferenceBuilder<T extends EndpointReference> {
   protected String address;
   protected List<Element> referenceParameters = new ArrayList();

   public static EndpointReferenceBuilder newInstance(Transactional.Version var0) {
      if (Version.WSAT10 != var0 && Version.DEFAULT != var0) {
         if (Version.WSAT11 != var0 && Version.WSAT12 != var0) {
            throw new IllegalArgumentException(var0 + "is not a supported ws-at version");
         } else {
            return W3C();
         }
      } else {
         return MemberSubmission();
      }
   }

   public static EndpointReferenceBuilder<W3CEndpointReference> W3C() {
      return new W3CEndpointReferenceBuilder();
   }

   public static EndpointReferenceBuilder<MemberSubmissionEndpointReference> MemberSubmission() {
      return new MemberSubmissionEndpointReferenceBuilder();
   }

   public EndpointReferenceBuilder<T> address(String var1) {
      this.address = var1;
      return this;
   }

   public EndpointReferenceBuilder<T> referenceParameter(Element... var1) {
      Element[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Element var5 = var2[var4];
         this.referenceParameters.add(var5);
      }

      return this;
   }

   public EndpointReferenceBuilder<T> referenceParameter(Node... var1) {
      Node[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Node var5 = var2[var4];
         this.referenceParameters.add((Element)var5);
      }

      return this;
   }

   public EndpointReferenceBuilder<T> referenceParameter(List<Element> var1) {
      this.referenceParameters.addAll(var1);
      return this;
   }

   public abstract T build();

   static class MemberSubmissionEndpointReferenceBuilder extends EndpointReferenceBuilder<MemberSubmissionEndpointReference> {
      public MemberSubmissionEndpointReference build() {
         MemberSubmissionEndpointReference var1 = new MemberSubmissionEndpointReference();
         var1.addr = new MemberSubmissionEndpointReference.Address();
         var1.addr.uri = this.address;
         var1.referenceParameters = new MemberSubmissionEndpointReference.Elements();
         var1.referenceParameters.elements = new ArrayList();
         var1.referenceParameters.elements.addAll(this.referenceParameters);
         return var1;
      }
   }

   static class W3CEndpointReferenceBuilder extends EndpointReferenceBuilder<W3CEndpointReference> {
      public W3CEndpointReference build() {
         javax.xml.ws.wsaddressing.W3CEndpointReferenceBuilder var1 = new javax.xml.ws.wsaddressing.W3CEndpointReferenceBuilder();

         for(int var2 = 0; var2 < this.referenceParameters.size(); ++var2) {
            Element var3 = (Element)this.referenceParameters.get(var2);
            var1.referenceParameter(var3);
         }

         W3CEndpointReference var4 = var1.address(this.address).build();
         return var4;
      }
   }
}
