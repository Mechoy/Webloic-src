package weblogic.security.principal;

import weblogic.security.spi.WLSUser;

public final class WLSUserImpl extends WLSAbstractPrincipal implements WLSUser {
   private static final long serialVersionUID = -4751797971105387435L;

   public WLSUserImpl(String userName) {
      this.setName(userName);
   }

   public WLSUserImpl(String userName, boolean createSalt) {
      super(createSalt);
      this.setName(userName);
   }

   public boolean equals(Object another) {
      return another instanceof WLSUserImpl && super.equals(another);
   }
}
