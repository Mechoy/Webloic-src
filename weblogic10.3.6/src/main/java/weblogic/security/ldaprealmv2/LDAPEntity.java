package weblogic.security.ldaprealmv2;

import java.security.Principal;

/** @deprecated */
public interface LDAPEntity extends Principal {
   String getDN();
}
