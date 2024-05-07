package weblogic.wtc.wls;

import com.bea.core.jatmi.common.ntrace;
import com.bea.core.jatmi.intf.TCAppKey;
import com.bea.core.jatmi.intf.TCAuthenticatedUser;
import java.security.AccessController;
import java.security.Principal;
import java.util.Enumeration;
import netscape.ldap.LDAPAttribute;
import netscape.ldap.LDAPAttributeSet;
import netscape.ldap.LDAPConnection;
import netscape.ldap.LDAPEntry;
import netscape.ldap.LDAPException;
import netscape.ldap.LDAPSearchResults;
import weblogic.ldap.EmbeddedLDAP;
import weblogic.security.WLSPrincipals;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.wtc.jatmi.DefaultUserRec;
import weblogic.wtc.jatmi.TPException;
import weblogic.wtc.jatmi.UserRec;

public final class ldapAppKey implements TCAppKey {
   public static final String DFLT_UID_KW = "TUXEDO_UID";
   public static final String DFLT_GID_KW = "TUXEDO_GID";
   private int dfltAppKey;
   private boolean allowAnon;
   private String uid_key = "TUXEDO_UID=";
   private String gid_key = "TUXEDO_GID=";
   private String anon_user = null;
   private String domain;
   private String realm;
   private String passwd;
   private String host;
   private String base;
   private int port;
   private DefaultUserRec anonUserRec = null;
   private LDAPConnection ld;
   private boolean _cached = false;
   private static final int scope = 2;
   private static final boolean attrsonly = false;
   private static final String base_prefix = "ou=people";
   private static final String ou = ",ou=";
   private static final String dc = ",dc=";
   private static final String dflt_filter = "(objectclass=*)";
   private static final String filter_prefix = "uid=";
   private static final String[] attr_query = new String[]{"uid", "description", null};
   private static final String ANONAPPKEY_KW = "DefaultAppKey=";
   private static final String admin = "cn=Admin";
   private static final AuthenticatedSubject KERNELID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public void init(String var1, boolean var2, int var3) throws TPException {
      boolean var4 = ntrace.isTraceEnabled(4);
      if (var4) {
         ntrace.doTrace("[/ldapAppKey/init(param " + var1 + ", anonAllowed " + var2 + ", dfltAppKey " + var3 + ")");
      }

      if (var1 != null) {
         this.parseParam(var1);
      }

      this.do_init();
      this.dfltAppKey = var3;
      this.allowAnon = var2;
      if (this.allowAnon) {
         this.anonUserRec = new DefaultUserRec(this.anon_user, this.dfltAppKey);
      }

      if (var4) {
         ntrace.doTrace("]/ldapAppKey/init(10) return");
      }

   }

   private void do_init() throws TPException {
      boolean var1 = ntrace.isTraceEnabled(4);
      if (var1) {
         ntrace.doTrace("[/ldapAppKey/do_init()");
      }

      this.realm = SecurityServiceManager.getDefaultRealmName();
      this.domain = EmbeddedLDAP.getEmbeddedLDAPDomain();
      this.passwd = EmbeddedLDAP.getEmbeddedLDAPCredential(KERNELID);
      this.host = EmbeddedLDAP.getEmbeddedLDAPHost();
      this.port = EmbeddedLDAP.getEmbeddedLDAPPort();
      if (ntrace.getTraceLevel() == 1000373) {
         ntrace.doTrace("domain=" + this.domain + ", realm=" + this.realm + ", host=" + this.host + ", port=" + this.port);
      }

      StringBuffer var2 = (new StringBuffer("ou=people")).append(",ou=").append(this.realm);
      var2 = var2.append(",dc=").append(this.domain);
      this.base = var2.toString();
      if (var1) {
         ntrace.doTrace("search base: " + this.base);
      }

      try {
         this.ld = new LDAPConnection();
         this.ld.connect(3, this.host, this.port, "cn=Admin", this.passwd);
      } catch (LDAPException var7) {
         this.ld = null;
         if (var1) {
            ntrace.doTrace("*]/ldapAppKey/do_init(10) return TPESYSTEM");
         }

         throw new TPException(12, "Failed to create LDAP connection object");
      } finally {
         if (this.passwd != null) {
            this.passwd = null;
         }

      }

      this.anon_user = WLSPrincipals.getAnonymousUsername();
      if (var1) {
         ntrace.doTrace("]/ldapAppKey/do_init(20) return");
      }

   }

   public void uninit() throws TPException {
      boolean var1 = ntrace.isTraceEnabled(4);
      if (var1) {
         ntrace.doTrace("[/ldapAppKey/uninit()");
      }

      if (this.ld != null) {
         try {
            this.ld.disconnect();
         } catch (LDAPException var3) {
            if (var1) {
               ntrace.doTrace("*]/ldapAppKey/uninit(10) return TPESYSTEM");
            }

            throw new TPException(12, "Failed to close LDAP connection");
         }
      }

      if (var1) {
         ntrace.doTrace("]/ldapAppKey/uninit(20) return");
      }

   }

   public UserRec getTuxedoUserRecord(TCAuthenticatedUser var1) {
      boolean var3 = ntrace.isTraceEnabled(4);
      if (var3) {
         ntrace.doTrace("[/ldapAppKey/getTuxedoUserRecord(subj " + var1 + ")");
      }

      Object[] var4 = var1.getPrincipals();
      if (var4 != null && var4.length != 0) {
         for(int var8 = 0; var8 < var4.length; ++var8) {
            Principal var5 = (Principal)var4[var8];
            String var6 = var5.getName();
            if (var6.equals(this.anon_user)) {
               if (var3) {
                  ntrace.doTrace("]/ldapAppKey/getTuxedoUserRecord(30) return anonymous user: " + this.anonUserRec);
               }

               return this.anonUserRec;
            }

            UserRec var2;
            if ((var2 = this.getUserRec(var6)) != null) {
               if (var3) {
                  ntrace.doTrace("]/ldapAppKey/getTuxedoUserRecord(40) return user: " + var2);
               }

               return var2;
            }
         }

         if (var3) {
            ntrace.doTrace("]/ldapAppKey/getTuxedoUserRecord(50) return null");
         }

         return null;
      } else if (this.allowAnon) {
         if (var3) {
            ntrace.doTrace("]/ldapAppKey/getTuxedoUserRecord(10) return anonymous user: " + this.anonUserRec);
         }

         return this.anonUserRec;
      } else {
         if (var3) {
            ntrace.doTrace("]/ldapAppKey/uninit(20) return null");
         }

         return null;
      }
   }

   private void parseParam(String var1) {
      boolean var6 = ntrace.isTraceEnabled(4);
      if (var6) {
         ntrace.doTrace("[/ldapAppKey/parseParam(param " + var1 + ")");
      }

      String var3 = var1.trim();
      StringBuffer var2;
      int var5;
      if ((var5 = var3.indexOf(32)) != -1) {
         var2 = new StringBuffer(var3.substring(0, var5));
         var2.append('=');
         this.uid_key = new String(var2);
         String var4 = var3.substring(var5 + 1).trim();
         if ((var5 = var4.indexOf(32)) != -1) {
            var3 = var4.substring(0, var5);
         } else if (var4.length() != 0) {
            var3 = var4;
         }

         var2 = new StringBuffer(var3);
         var2.append('=');
         this.gid_key = new String(var2);
      } else if (var3.length() != 0) {
         var2 = new StringBuffer(var3);
         var2.append('=');
         this.uid_key = new String(var2);
      }

      if (var6) {
         ntrace.doTrace("/ldapAppKey/parseParam/(uid_key " + this.uid_key + ", gid_key " + this.gid_key);
         ntrace.doTrace("]/ldapAppKey/parseParam(10) return");
      }

   }

   private UserRec getUserRec(String var1) {
      boolean var2 = false;
      boolean var3 = false;
      StringBuffer var4 = (new StringBuffer("uid=")).append(var1);
      String var5 = var4.toString();
      boolean var6 = ntrace.isTraceEnabled(4);
      if (var6) {
         ntrace.doTrace("[/ldapAppKey/getUserRec(username " + var1 + ")");
      }

      if (this.ld == null) {
         try {
            this.do_init();
         } catch (TPException var23) {
            if (var6) {
               ntrace.doTrace("]/ldapAppKey/getUserRec(5) return null");
            }

            return null;
         }
      }

      boolean var7 = true;

      while(var7) {
         var7 = false;

         try {
            LDAPSearchResults var8 = this.ld.search(this.base, 2, var5, attr_query, false);

            while(var8.hasMoreElements()) {
               LDAPEntry var29 = null;
               var29 = var8.next();
               if (ntrace.getTraceLevel() == 1000373) {
                  ntrace.doTrace("DN: " + var29.getDN());
               }

               LDAPAttributeSet var10 = var29.getAttributeSet();

               for(int var11 = 0; var11 < var10.size(); ++var11) {
                  LDAPAttribute var12 = var10.elementAt(var11);
                  String var13 = var12.getName();
                  if (ntrace.getTraceLevel() == 1000373) {
                     ntrace.doTrace(var13 + ':');
                  }

                  Enumeration var14 = var12.getStringValues();
                  if (var14 != null) {
                     while(var14.hasMoreElements()) {
                        String var15 = (String)var14.nextElement();
                        if (ntrace.getTraceLevel() == 1000373) {
                           ntrace.doTrace(var15);
                        }

                        if (var13.equals("description")) {
                           int var16;
                           if ((var16 = var15.indexOf(this.uid_key)) != -1) {
                              var16 = var15.indexOf(61, var16) + 1;
                              int var17;
                              String var19;
                              if ((var17 = var15.indexOf(32, var16)) != -1) {
                                 var19 = var15.substring(var16, var17);
                              } else {
                                 var19 = var15.substring(var16);
                              }

                              String var18;
                              if ((var17 = var19.indexOf(44)) == -1 && (var17 = var19.indexOf(59)) == -1 && (var17 = var19.indexOf(58)) == -1) {
                                 var18 = var19;
                              } else {
                                 var18 = var19.substring(0, var17);
                              }

                              Integer var20;
                              int var27;
                              try {
                                 var20 = new Integer(var18);
                                 var27 = var20;
                                 if (ntrace.getTraceLevel() == 1000373) {
                                    ntrace.doTrace("uid = " + var27);
                                 }
                              } catch (NumberFormatException var24) {
                                 if (var6) {
                                    ntrace.doTrace("]/ldapAppKey/getUserRec(10) return null");
                                 }

                                 return null;
                              }

                              if ((var16 = var15.indexOf(this.gid_key)) != -1) {
                                 var16 = var15.indexOf(61, var16) + 1;
                                 if ((var17 = var15.indexOf(32, var16)) != -1) {
                                    var19 = var15.substring(var16, var17);
                                 } else {
                                    var19 = var15.substring(var16);
                                 }

                                 if ((var17 = var19.indexOf(44)) == -1 && (var17 = var19.indexOf(59)) == -1 && (var17 = var19.indexOf(58)) == -1) {
                                    var18 = var19;
                                 } else {
                                    var18 = var19.substring(var17);
                                 }

                                 int var28;
                                 try {
                                    var20 = new Integer(var18);
                                    var28 = var20;
                                    if (ntrace.getTraceLevel() == 1000373) {
                                       ntrace.doTrace("gid = " + var28);
                                    }
                                 } catch (NumberFormatException var25) {
                                    if (var6) {
                                       ntrace.doTrace("]/ldapAppKey/getUserRec(30) return null");
                                    }

                                    return null;
                                 }

                                 var27 &= 131071;
                                 var28 &= 16383;
                                 int var21 = var27 | var28 << 17;
                                 if (var6) {
                                    ntrace.doTrace("]/ldapAppKey/getUserRec(50) return user: " + var1 + ", appkey " + var21);
                                 }

                                 return new DefaultUserRec(var1, var21);
                              }

                              if (var6) {
                                 ntrace.doTrace("]/ldapAppKey/getUserRec(40) return null");
                              }

                              return null;
                           }

                           if (var6) {
                              ntrace.doTrace("]/ldapAppKey/getUserRec(20) return null");
                           }

                           return null;
                        }
                     }
                  }
               }
            }
         } catch (LDAPException var26) {
            int var9 = var26.getLDAPResultCode();
            if (var9 == 81) {
               this.ld = null;

               try {
                  this.do_init();
                  var7 = true;
               } catch (TPException var22) {
               }
            }
         }
      }

      if (var6) {
         ntrace.doTrace("]/ldapAppKey/getUserRec(60) return null");
      }

      return null;
   }

   public void doCache(boolean var1) {
      this._cached = var1;
   }

   public boolean isCached() {
      return this._cached;
   }
}
