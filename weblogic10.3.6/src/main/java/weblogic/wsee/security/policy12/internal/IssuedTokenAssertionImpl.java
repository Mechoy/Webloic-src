package weblogic.wsee.security.policy12.internal;

import java.util.Map;
import weblogic.wsee.security.policy12.assertions.IssuedToken;
import weblogic.wsee.security.policy12.assertions.RequestSecurityTokenTemplate;
import weblogic.wsee.security.wssp.IssuedTokenAssertion;
import weblogic.wsee.security.wst.internal.v13.WSTConstants;

public class IssuedTokenAssertionImpl extends TokenAssertionImpl implements IssuedTokenAssertion {
   private IssuedToken issuedToken;
   private String namespaceURI;
   private String tokenType;
   private String dkTokenType;
   private Map templateMap = null;

   IssuedTokenAssertionImpl(IssuedToken var1) {
      super(var1);
      this.issuedToken = var1;
      this.namespaceURI = var1.getName().getNamespaceURI();
      if (null != var1.getRequestSecurityTokenTemplate()) {
         this.templateMap = var1.getRequestSecurityTokenTemplate().getTemplateMap();
      }

      if (null != this.templateMap) {
         this.tokenType = (String)this.templateMap.get(WSTConstants.T13_TOKEN_TYPE);
         if (null != this.tokenType) {
            this.dkTokenType = "http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/dk";
         }
      }

   }

   public String getIssuerString() {
      return this.issuedToken.getIssuer();
   }

   public boolean hasRequestSecurityTokenTemplate() {
      if (null == this.issuedToken.getRequestSecurityTokenTemplate()) {
         return false;
      } else {
         return this.issuedToken.getRequestSecurityTokenTemplate().getTemplateMap() != null;
      }
   }

   public RequestSecurityTokenTemplate getRequestSecurityTokenTemplate() {
      return this.issuedToken.getRequestSecurityTokenTemplate();
   }

   public String getIssuedTokenType() {
      return this.tokenType;
   }

   public String getDkTokenType() {
      return this.dkTokenType;
   }
}
