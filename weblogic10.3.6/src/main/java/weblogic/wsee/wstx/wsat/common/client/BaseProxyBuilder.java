package weblogic.wsee.wstx.wsat.common.client;

import com.sun.xml.ws.api.addressing.OneWayFeature;
import com.sun.xml.ws.api.addressing.WSEndpointReference;
import java.util.ArrayList;
import java.util.List;
import javax.xml.ws.EndpointReference;
import javax.xml.ws.WebServiceFeature;
import weblogic.wsee.wstx.wsat.common.WSATVersion;
import weblogic.wsee.wstx.wsc.common.WSCUtil;
import weblogic.xml.crypto.wss.provider.CredentialProvider;

public abstract class BaseProxyBuilder<T, B extends BaseProxyBuilder<T, B>> {
   protected WSATVersion<T> version;
   protected EndpointReference to;
   protected EndpointReference replyTo;
   protected List<WebServiceFeature> features;
   protected List<CredentialProvider> credentialProviders;

   protected BaseProxyBuilder(WSATVersion<T> var1) {
      this.version = var1;
      this.feature(var1.newAddressingFeature());
   }

   public B feature(WebServiceFeature var1) {
      if (var1 == null) {
         return this;
      } else {
         if (this.features == null) {
            this.features = new ArrayList();
         }

         this.features.add(var1);
         return this;
      }
   }

   public B to(EndpointReference var1) {
      this.to = var1;
      return this;
   }

   public B replyTo(EndpointReference var1) {
      this.replyTo = var1;
      if (var1 != null) {
         this.feature(new OneWayFeature(true, WSEndpointReference.create(var1)));
      }

      return this;
   }

   public B txIdForReference(String var1, String var2) {
      EndpointReference var3 = this.version.newEndpointReferenceBuilder().address(this.getDefaultCallbackAddress()).referenceParameter(WSCUtil.referenceElementTxId(var1), WSCUtil.referenceElementBranchQual(var2), WSCUtil.referenceElementRoutingInfo()).build();
      this.replyTo(var3);
      return this;
   }

   public B credentialProvider(CredentialProvider var1) {
      if (var1 == null) {
         return this;
      } else {
         if (this.credentialProviders == null) {
            this.credentialProviders = new ArrayList();
         }

         this.credentialProviders.add(var1);
         return this;
      }
   }

   protected WebServiceFeature[] getEnabledFeatures() {
      return (WebServiceFeature[])this.features.toArray(new WebServiceFeature[0]);
   }

   protected abstract String getDefaultCallbackAddress();
}
