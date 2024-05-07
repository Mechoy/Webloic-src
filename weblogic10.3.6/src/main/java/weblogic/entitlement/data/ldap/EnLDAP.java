package weblogic.entitlement.data.ldap;

import java.util.Properties;
import netscape.ldap.LDAPAttribute;
import netscape.ldap.LDAPAttributeSet;
import netscape.ldap.LDAPConnection;
import netscape.ldap.LDAPEntry;
import netscape.ldap.LDAPException;
import netscape.ldap.LDAPSearchConstraints;
import weblogic.entitlement.data.EnCredentialException;
import weblogic.entitlement.data.EnDomainException;
import weblogic.entitlement.data.EnStorageException;
import weblogic.ldap.EmbeddedLDAPConnection;
import weblogic.security.shared.LoggerWrapper;

public class EnLDAP {
   public static final String LDAP_DOMAIN = "weblogic.entitlement.data.ldap.domain";
   public static final String LDAP_REALM = "weblogic.entitlement.data.ldap.realm";
   public static final String LDAP_HOST = "weblogic.entitlement.data.ldap.hostname";
   public static final String LDAP_PORT = "weblogic.entitlement.data.ldap.port";
   public static final String LDAP_PASSWORD = "weblogic.entitlement.data.ldap.password";
   public static final String LDAP_USELOCAL = "weblogic.entitlement.data.ldap.uselocal";
   private static String host = "localhost";
   private static int port = 7003;
   private static String password = null;
   private static String domainName = "mydomain";
   private static boolean useLocal = false;
   protected String realmName = "myrealm";
   private static final String admin = "cn=admin";
   private static LDAPConnection[] ldPool = null;
   private static int ldPoolCount = 0;
   private static LDAPSearchConstraints constraint = null;
   protected static String domainDN = null;
   protected String realmDN = null;
   protected static final String nameAttribute = "cn";
   protected static final int LDAP_VERSION = 2;
   protected static final String[] noAttrs = new String[]{"1.1"};
   protected static final String[] nameAttributeList = new String[]{"cn"};
   protected static final String NAME_DELIMITER = "::";
   protected static LoggerWrapper traceLogger = LoggerWrapper.getInstance("SecurityEEngine");

   public EnLDAP(Properties var1) {
      if (traceLogger.isDebugEnabled()) {
         traceLogger.debug("Initializing EnLDAP.");
      }

      if (var1 != null) {
         String var2;
         if ((var2 = var1.getProperty("weblogic.entitlement.data.ldap.port")) != null) {
            port = Integer.valueOf(var2);
         }

         if ((var2 = var1.getProperty("weblogic.entitlement.data.ldap.hostname")) != null) {
            host = var2;
         }

         if ((var2 = var1.getProperty("weblogic.entitlement.data.ldap.password")) != null) {
            password = var2;
         }

         if ((var2 = var1.getProperty("weblogic.entitlement.data.ldap.domain")) != null) {
            domainName = var2;
         }

         if ((var2 = var1.getProperty("weblogic.entitlement.data.ldap.realm")) != null) {
            this.realmName = var2;
         }

         if ((var2 = var1.getProperty("weblogic.entitlement.data.ldap.uselocal")) != null && "true".equals(var2)) {
            useLocal = true;
         }
      }

      domainDN = "dc=" + domainName;
      this.realmDN = "ou=" + this.realmName + "," + domainDN;
      if (ldPool == null) {
         Class var18 = EnLDAP.class;
         synchronized(EnLDAP.class) {
            if (ldPool == null) {
               ldPool = new LDAPConnection[16];
               int var3 = 3;

               while(var3 != 0) {
                  LDAPConnection var4 = null;

                  try {
                     var4 = getConnection();
                     constraint = var4.getSearchConstraints();
                     addStructuralEntry(var4, domainDN, true, domainName);
                     addStructuralEntry(var4, this.realmDN, false, this.realmName);
                     var3 = 0;
                  } catch (LDAPException var15) {
                     var4 = null;
                     Object var6 = null;
                     if (traceLogger.isDebugEnabled()) {
                        traceLogger.debug("EnLDAP(), LDAPException while trying to initialize LDAP", var15);
                     }

                     switch (var15.getLDAPResultCode()) {
                        case 49:
                           var6 = new EnCredentialException(var15.toString());
                           break;
                        case 53:
                           var6 = new EnDomainException(var15.toString());
                           break;
                        case 91:
                           if (var3 > 1) {
                              --var3;

                              try {
                                 Thread.sleep(1000L);
                              } catch (Exception var14) {
                              }
                              continue;
                           }
                        case 52:
                        case 81:
                        default:
                           var6 = new EnStorageException(var15.toString());
                     }

                     ldPool = null;
                     throw var6;
                  } finally {
                     if (var4 != null) {
                        releaseConnection(var4);
                     }

                  }
               }
            }
         }
      }

   }

   protected static LDAPConnection getConnection() throws LDAPException {
      Object var0 = null;
      if (ldPoolCount > 0) {
         Class var1 = LDAPConnection.class;
         synchronized(LDAPConnection.class) {
            if (ldPoolCount > 0) {
               var0 = ldPool[--ldPoolCount];
               ldPool[ldPoolCount] = null;
            }
         }
      }

      if (var0 == null) {
         var0 = useLocal ? new EmbeddedLDAPConnection(false) : new LDAPConnection();
         ((LDAPConnection)var0).connect(host, port);
         ((LDAPConnection)var0).bind(2, "cn=admin", password);
      }

      return (LDAPConnection)var0;
   }

   protected static synchronized void releaseConnection(LDAPConnection var0) {
      if (ldPoolCount == ldPool.length) {
         LDAPConnection[] var1 = new LDAPConnection[ldPoolCount + 16];
         System.arraycopy(ldPool, 0, var1, 0, ldPoolCount);
         ldPool = var1;
      }

      ldPool[ldPoolCount++] = var0;
   }

   protected static void addEntry(LDAPConnection var0, String var1, boolean var2, String var3) throws LDAPException {
      String[] var4 = new String[]{"top", var2 ? "domain" : "organizationalUnit"};
      LDAPAttributeSet var5 = new LDAPAttributeSet();
      var5.add(new LDAPAttribute("objectclass", var4));
      var5.add(new LDAPAttribute(var2 ? "dc" : "ou", var3));
      LDAPEntry var6 = new LDAPEntry(var1, var5);
      var0.add(var6);
   }

   protected static void addStructuralEntry(LDAPConnection var0, String var1, boolean var2, String var3) throws LDAPException {
      try {
         addEntry(var0, var1, var2, var3);
      } catch (LDAPException var5) {
         if (68 != var5.getLDAPResultCode()) {
            throw var5;
         }
      }

   }

   protected static void checkStorageException(LDAPException var0) {
      if (traceLogger.isDebugEnabled()) {
         traceLogger.debug("LDAPException: ", var0);
      }

      switch (var0.getLDAPResultCode()) {
         case 52:
         case 81:
         case 91:
            throw new EnStorageException(var0.getMessage());
         default:
      }
   }

   protected static String PK2Name(String var0, String var1) {
      if (var0 == null) {
         var0 = "*";
      }

      return var0 + "::" + var1;
   }
}
