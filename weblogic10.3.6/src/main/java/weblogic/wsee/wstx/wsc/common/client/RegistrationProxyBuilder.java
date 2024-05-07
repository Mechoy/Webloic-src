package weblogic.wsee.wstx.wsc.common.client;

import com.sun.xml.ws.api.addressing.AddressingVersion;
import com.sun.xml.ws.api.addressing.OneWayFeature;
import com.sun.xml.ws.api.addressing.WSEndpointReference;
import java.util.ArrayList;
import java.util.List;
import javax.xml.ws.EndpointReference;
import javax.xml.ws.WebServiceFeature;
import weblogic.wsee.wstx.wsc.common.EndpointReferenceBuilder;
import weblogic.wsee.wstx.wsc.common.PendingRequestManager;
import weblogic.wsee.wstx.wsc.common.RegistrationIF;
import weblogic.wsee.wstx.wsc.common.WSCUtil;
import weblogic.wsee.wstx.wsc.common.types.BaseRegisterResponseType;
import weblogic.wsee.wstx.wsc.common.types.BaseRegisterType;
import weblogic.xml.crypto.wss.provider.CredentialProvider;

public abstract class RegistrationProxyBuilder {
   protected List<WebServiceFeature> features;
   protected List<CredentialProvider> credentialProviders;
   protected EndpointReference to;
   protected String txId;
   protected long timeout;
   protected String callbackAddress;

   public RegistrationProxyBuilder feature(WebServiceFeature var1) {
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

   public RegistrationProxyBuilder credentialProvider(CredentialProvider var1) {
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

   public RegistrationProxyBuilder txIdForReference(String var1) {
      this.txId = var1;
      return this;
   }

   public RegistrationProxyBuilder to(EndpointReference var1) {
      this.to = var1;
      return this;
   }

   public RegistrationProxyBuilder timeout(long var1) {
      this.timeout = var1;
      return this;
   }

   public RegistrationProxyBuilder callback(String var1) {
      this.callbackAddress = var1;
      return this;
   }

   protected abstract String getDefaultCallbackAddress();

   protected abstract EndpointReferenceBuilder getEndpointReferenceBuilder();

   protected WebServiceFeature[] getEnabledFeatures() {
      return (WebServiceFeature[])this.features.toArray(new WebServiceFeature[0]);
   }

   public RegistrationIF build() {
      if (this.callbackAddress == null) {
         this.callbackAddress = this.getDefaultCallbackAddress();
      }

      EndpointReference var1 = this.getEndpointReferenceBuilder().address(this.callbackAddress).referenceParameter(WSCUtil.referenceElementTxId(this.txId), WSCUtil.referenceElementRoutingInfo()).build();
      WSEndpointReference var2 = WSEndpointReference.create(var1);
      OneWayFeature var3 = new OneWayFeature(true, var2);
      this.feature(var3);
      return null;
   }

   public abstract class RegistrationProxyF<T extends EndpointReference, K, P, D> implements RegistrationIF<T, K, P> {
      public BaseRegisterResponseType<T, P> registerOperation(BaseRegisterType<T, K> var1) {
         BaseRegisterResponseType var3;
         try {
            PendingRequestManager.ResponseBox var2 = PendingRequestManager.reqisterRequest(RegistrationProxyBuilder.this.txId);
            this.asyncRegister(var1.getDelegate());
            var3 = var2.getReponse(RegistrationProxyBuilder.this.timeout);
         } finally {
            PendingRequestManager.removeRequest(RegistrationProxyBuilder.this.txId);
         }

         return var3;
      }

      public abstract D getDelegate();

      public abstract void asyncRegister(K var1);

      public abstract AddressingVersion getAddressingVersion();
   }
}
