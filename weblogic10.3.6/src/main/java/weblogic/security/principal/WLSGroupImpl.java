package weblogic.security.principal;

import weblogic.security.spi.WLSGroup;

public final class WLSGroupImpl extends WLSAbstractPrincipal implements WLSGroup {
   private static final long serialVersionUID = -8923536011547762759L;

   public WLSGroupImpl(String groupName) {
      this.setName(groupName);
   }

   public WLSGroupImpl(String groupName, boolean createSalt) {
      super(createSalt);
      this.setName(groupName);
   }

   public boolean equals(Object another) {
      return another instanceof WLSGroupImpl && super.equals(another);
   }
}
