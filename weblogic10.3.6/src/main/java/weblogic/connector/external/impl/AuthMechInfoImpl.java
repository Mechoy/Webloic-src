package weblogic.connector.external.impl;

import weblogic.connector.external.AuthMechInfo;
import weblogic.j2ee.descriptor.AuthenticationMechanismBean;

public class AuthMechInfoImpl implements AuthMechInfo {
   AuthenticationMechanismBean authMechBean;

   public AuthMechInfoImpl(AuthenticationMechanismBean var1) {
      this.authMechBean = var1;
   }

   public String getDescription() {
      String[] var1 = this.authMechBean.getDescriptions();
      return var1.length > 0 ? var1[0] : "";
   }

   public String[] getDescriptions() {
      return this.authMechBean.getDescriptions();
   }

   public String getType() {
      return this.authMechBean.getAuthenticationMechanismType();
   }

   public String getCredentialInterface() {
      return this.authMechBean.getCredentialInterface();
   }
}
