package weblogic.wsee.tools.wsdlc.jaxrpc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import weblogic.wsee.conversation.ConversationUtils;
import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.source.JsBinding;
import weblogic.wsee.tools.source.JsMethod;
import weblogic.wsee.wsdl.WsdlPort;
import weblogic.wsee.wsdl.WsdlService;

public class JwsGenInfo {
   private JsBinding binding;
   private List<WsdlPort> ports = new ArrayList();
   private WsdlService service;
   private SOAPBindingInfo soapBindingInfo;
   private String wsdlLocation;
   private List<AddressInfo> addressInfos;
   private String serviceClassName;
   private String implClassName;
   private boolean callback81;
   private boolean upgraded81Jws;

   JwsGenInfo() {
   }

   void setBinding(JsBinding var1) {
      this.binding = var1;
   }

   public JsBinding getBinding() {
      return this.binding;
   }

   void setPort(WsdlPort var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("No port specified");
      } else {
         this.ports.add(var1);
         this.service = var1.getService();
      }
   }

   public boolean isCallback81() {
      return this.callback81;
   }

   public void setCallback81(boolean var1) {
      this.callback81 = var1;
   }

   void setService(WsdlService var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("No service specified");
      } else {
         this.service = var1;
         this.ports.clear();
         Iterator var2 = var1.getPorts().values().iterator();

         while(var2.hasNext()) {
            this.ports.add(var2.next());
         }

      }
   }

   public void setUpgraded81Jws(boolean var1) {
      this.upgraded81Jws = var1;
   }

   public boolean isUpgradedJWS() {
      return this.upgraded81Jws;
   }

   void setSOAPBindingInfo(SOAPBindingInfo var1) {
      this.soapBindingInfo = var1;
   }

   void setWsdlLocation(String var1) {
      this.wsdlLocation = var1;
   }

   public String getWsdlLocation() {
      return this.wsdlLocation;
   }

   public SOAPBindingInfo getSOAPBindingInfo() {
      return this.soapBindingInfo;
   }

   public WsdlService getService() {
      return this.service;
   }

   public List<AddressInfo> getAddressInfos() {
      if (this.addressInfos == null) {
         this.addressInfos = new ArrayList();
         Iterator var1 = this.ports.iterator();

         while(var1.hasNext()) {
            WsdlPort var2 = (WsdlPort)var1.next();
            this.addressInfos.add(new AddressInfo(var2));
         }
      }

      return this.addressInfos;
   }

   public boolean isConversational() {
      assert !this.ports.isEmpty();

      return ConversationUtils.isConversational(((WsdlPort)this.ports.get(0)).getBinding());
   }

   void setServiceClassName(String var1) {
      this.serviceClassName = var1;
      this.implClassName = var1 + "Impl";
   }

   public String getServiceClassName() {
      return this.serviceClassName;
   }

   public String getImplClassName() {
      return this.implClassName;
   }

   void validate() throws WsBuildException {
      this.checkSoap12StyleAndUse();
   }

   private void checkSoap12StyleAndUse() throws WsBuildException {
      if (this.soapBindingInfo.isSoap12()) {
         JsMethod[] var1 = this.binding.getMethods();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            JsMethod var4 = var1[var3];
            if ("rpc".equalsIgnoreCase(var4.getStyle()) && "encoded".equalsIgnoreCase(var4.getUse())) {
               throw new WsBuildException("rpc/encoded style soap12 webservice is not supported");
            }
         }
      }

   }
}
