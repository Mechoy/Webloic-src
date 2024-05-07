package weblogic.security.ldaprealmv2;

import com.oroinc.text.regex.MalformedPatternException;
import com.oroinc.text.regex.MatchResult;
import com.oroinc.text.regex.Pattern;
import com.oroinc.text.regex.PatternMatcherInput;
import com.oroinc.text.regex.Perl5Compiler;
import com.oroinc.text.regex.Perl5Matcher;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.security.AccessController;
import java.security.acl.Group;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;
import java.util.Stack;
import java.util.Vector;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import netscape.ldap.LDAPAttribute;
import netscape.ldap.LDAPAttributeSet;
import netscape.ldap.LDAPCache;
import netscape.ldap.LDAPConnection;
import netscape.ldap.LDAPDN;
import netscape.ldap.LDAPEntry;
import netscape.ldap.LDAPException;
import netscape.ldap.LDAPSearchConstraints;
import netscape.ldap.LDAPSearchResults;
import netscape.ldap.LDAPSocketFactory;
import weblogic.logging.LogOutputStream;
import weblogic.management.configuration.CachingRealmMBean;
import weblogic.management.configuration.CustomRealmMBean;
import weblogic.management.configuration.RealmMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.utils.Factory;
import weblogic.security.utils.Pool;
import weblogic.security.utils.SSLContextManager;
import weblogic.utils.enumerations.MappingEnumerator;
import weblogic.utils.enumerations.SequencingEnumerator;

/** @deprecated */
class LDAPDelegate {
   private static final int LDAP_VERSION = 3;
   private static final int POOL_SIZE = 6;
   private static final String LDAP_PREFIX = "";
   private static final int DEFAULT_SSL_PORT = 636;
   private static final int DEFAULT_CACHE_TTL = 60;
   private static final int DEFAULT_CACHE_SIZE = 32;
   private static final int MAX_RESULTS_ENUM = 100;
   private static boolean useSSL;
   private static String serverHost;
   private static int serverPort;
   private static int cacheTTL;
   private static int cacheSize;
   private static String serverPrincipal;
   private static String serverCredential;
   private static final String[] USER_ATTRS = new String[]{"1.1"};
   private static String[] userBaseDNs;
   private static int[] userScopes;
   private static String[] userFilters;
   private static String[][] userNameAttributes;
   private static LDAPSearchConstraints userConstraints;
   private static LDAPSearchConstraints getUserConstraints;
   private static final String[] GROUP_ATTRS = new String[]{"1.1"};
   private static String[] groupBaseDNs;
   private static int[] groupScopes;
   private static String[] groupFilters;
   private static String[][] groupNameAttributes;
   private static LDAPSearchConstraints groupConstraints;
   private static LDAPSearchConstraints getGroupConstraints;
   private static final String[] MEMBER_ATTRS = new String[]{"1.1"};
   private static int[] memberScopes;
   private static String[] memberFilters;
   private static String[][] memberAttributes;
   private static boolean membershipSearch;
   private static boolean directMembershipOnly;
   private static int groupScopeDepth;
   private static LDAPSearchConstraints memberConstraints;
   private static boolean allowEnumeration;
   private Pool connPool;
   private LDAPRealm owner;
   LogOutputStream log;
   private static boolean caseSensitive = true;
   private static final boolean membershipDebug = false;
   private static LDAPSocketFactory boxOfSocks = new FocketSactory();

   private static void setupProperties() {
      AuthenticatedSubject var0 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      RealmMBean var1 = ManagementService.getRuntimeAccess(var0).getDomain().getSecurity().getRealm();
      allowEnumeration = var1.isEnumerationAllowed();
      CachingRealmMBean var2 = var1.getCachingRealm();
      caseSensitive = var2.getCacheCaseSensitive();
      Properties var3 = null;
      CustomRealmMBean var4 = (CustomRealmMBean)var2.getBasicRealm();
      var3 = var4.getConfigurationData();
      String var5 = null;
      String var6 = var3.getProperty("server.alias");
      if (var6 == null) {
         var5 = "";
      } else {
         var5 = var6 + ".";
      }

      serverHost = var3.getProperty(var5 + "server.host", "ldapserver");
      useSSL = getBoolean(var3, var5 + "useSSL", false);
      serverPort = getInteger(var3, var5 + "server.port", useSSL ? 636 : 389);
      cacheTTL = getInteger(var3, var5 + "cache.ttl", 60);
      cacheSize = getInteger(var3, var5 + "cache.size", 32);
      membershipSearch = getBoolean(var3, var5 + "membership.search", false);
      directMembershipOnly = getBoolean(var3, var5 + "membership.directmembershiponly", false);
      directMembershipOnly = getBoolean(var3, var5 + "membership.directmembershiponly", false);
      serverPrincipal = var3.getProperty(var5 + "server.principal");
      serverCredential = var4.getPassword();
      if (serverCredential == null) {
         serverCredential = var3.getProperty(var5 + "server.credential");
      }

      if ((userBaseDNs = getProperties(var3, var5 + "user.dn")).length == 0) {
         throw new LDAPRealmException("no base DNs specified for finding users!");
      } else {
         for(int var8 = 0; var8 < userBaseDNs.length; ++var8) {
            userBaseDNs[var8] = LDAPDN.normalize(userBaseDNs[var8]);
         }

         getProperties(var3, var5 + "user.scope");
         if ((userFilters = getProperties(var3, var5 + "user.filter")).length == 0) {
            throw new LDAPRealmException("no search filters specified for finding users!");
         } else if (userFilters.length > 1 && userBaseDNs.length > 1 && userFilters.length != userBaseDNs.length) {
            throw new LDAPRealmException("number of user search filters (" + userFilters.length + ") does not match " + "number of user base DNs (" + userBaseDNs.length + "), but both are > 1");
         } else {
            userScopes = makeScopes("user", getProperties(var3, var5 + "user.scope"));
            if (userFilters.length > 1 && userScopes.length > 1 && userFilters.length != userScopes.length) {
               throw new LDAPRealmException("number of user search filters (" + userFilters.length + ") does not match " + "number of user search scopes (" + userScopes.length + "), but both are > 1");
            } else {
               int var7;
               for(var7 = 0; var7 < userFilters.length; ++var7) {
                  if (userFilters[var7].indexOf("%u") == -1) {
                     throw new LDAPRealmException("user search filter " + (var7 + 1) + " doesn't contain a '%u' clause");
                  }
               }

               userNameAttributes = makeAttributes('u', userFilters);
               if ((groupBaseDNs = getProperties(var3, var5 + "group.dn")).length == 0) {
                  throw new LDAPRealmException("no base DNs specified for finding groups!");
               } else {
                  for(var7 = 0; var7 < groupBaseDNs.length; ++var7) {
                     groupBaseDNs[var7] = LDAPDN.normalize(groupBaseDNs[var7]);
                  }

                  if ((groupFilters = getProperties(var3, var5 + "group.filter")).length == 0) {
                     throw new LDAPRealmException("no search filters specified for finding groups!");
                  } else {
                     groupNameAttributes = makeAttributes('g', groupFilters);
                     groupScopes = makeScopes("group", getProperties(var3, var5 + "group.scope"));
                     if (groupFilters.length > 1 && groupScopes.length > 1 && groupFilters.length != groupScopes.length) {
                        throw new LDAPRealmException("number of group search filters (" + groupFilters.length + ") does not match " + "number of group search scopes (" + groupScopes.length + "), but both are > 1");
                     } else if (groupFilters.length > 1 && groupBaseDNs.length > 1 && groupFilters.length != groupBaseDNs.length) {
                        throw new LDAPRealmException("number of group search filters (" + groupFilters.length + ") does not match " + "number of group base DNs (" + groupBaseDNs.length + "), but both are > 1");
                     } else {
                        for(var7 = 0; var7 < groupFilters.length; ++var7) {
                           if (groupFilters[var7].indexOf("%g") == -1) {
                              throw new LDAPRealmException("group search filter " + (var7 + 1) + " doesn't contain a '%g' clause");
                           }
                        }

                        groupScopeDepth = getInteger(var3, var5 + "membership.scope.depth", -1);
                        if ((memberFilters = getProperties(var3, var5 + "membership.filter")).length == 0) {
                           throw new LDAPRealmException("no search filters specified for finding group memberships!");
                        } else {
                           memberAttributes = makeAttributes('M', memberFilters);
                           memberScopes = makeScopes("membership", getProperties(var3, var5 + "membership.scope"));
                           if (memberFilters.length > 1 && groupBaseDNs.length > 1 && memberFilters.length != groupBaseDNs.length) {
                              throw new LDAPRealmException("number of membership search filters (" + memberFilters.length + ") does not match " + "number of group base DNs (" + groupBaseDNs.length + "), but both are > 1");
                           } else if (memberFilters.length > 1 && memberScopes.length > 1 && memberFilters.length != memberScopes.length) {
                              throw new LDAPRealmException("number of membership search filters (" + memberFilters.length + ") does not match " + "number of membership search scopes (" + memberScopes.length + "), but both are > 1");
                           } else {
                              for(var7 = 0; var7 < memberFilters.length; ++var7) {
                                 if (memberFilters[var7].indexOf("%M") == -1 && memberFilters[var7].indexOf("%G") == -1) {
                                    throw new LDAPRealmException("membership search filter " + (var7 + 1) + " contains neither a '%M' nor a '%G' clause");
                                 }
                              }

                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private static int[] makeScopes(String var0, String[] var1) {
      int[] var2;
      if (var1.length > 0) {
         var2 = new int[var1.length];

         for(int var3 = 0; var3 < var1.length; ++var3) {
            String var4 = var1[var3].toLowerCase(Locale.ENGLISH);
            if (var4.startsWith("sub")) {
               var2[var3] = 2;
            } else if (var4.startsWith("one")) {
               var2[var3] = 1;
            } else {
               if (!var4.startsWith("base")) {
                  throw new LDAPRealmException(var0 + " search scope " + (var3 + 1) + " (" + var1[var3] + ")  not recognized");
               }

               var2[var3] = 0;
            }
         }
      } else {
         var2 = new int[]{2};
      }

      return var2;
   }

   private static String[][] makeAttributes(char var0, String[] var1) {
      Perl5Compiler var2 = new Perl5Compiler();
      Perl5Matcher var3 = new Perl5Matcher();
      String[][] var4 = new String[var1.length][];
      Pattern var5 = null;

      try {
         var5 = var2.compile("\\(\\s*(\\w+)\\s*[>~<=]+([^)]*%" + var0 + "[^)]*)\\)");
      } catch (MalformedPatternException var11) {
         throw new LDAPRealmException("internal LDAPRealm bug", var11);
      }

      for(int var6 = 0; var6 < var1.length; ++var6) {
         PatternMatcherInput var7 = new PatternMatcherInput(var1[var6]);
         Vector var8 = new Vector();

         while(var3.contains(var7, var5)) {
            MatchResult var9 = var3.getMatch();
            String var10 = var9.group(1);
            var8.addElement(var10);
         }

         var4[var6] = new String[var8.size()];
         var8.copyInto(var4[var6]);
      }

      return var4;
   }

   private static String scopeToString(int var0) {
      switch (var0) {
         case 0:
            return "base DN only";
         case 1:
            return "base DN + 1";
         case 2:
            return "base DN & below";
         default:
            return "unknown or invalid scope";
      }
   }

   static int getInteger(Properties var0, String var1, int var2) {
      String var3;
      return (var3 = var0.getProperty(var1)) == null ? var2 : Integer.parseInt(var3);
   }

   static boolean getBoolean(Properties var0, String var1, boolean var2) {
      String var3;
      return (var3 = var0.getProperty(var1)) == null ? var2 : Boolean.valueOf(var3);
   }

   static String[] getProperties(Properties var0, String var1) {
      Vector var2 = new Vector();
      String var3 = var0.getProperty(var1);
      if (var3 != null) {
         var2.addElement(var3);
      }

      for(int var4 = 1; (var3 = var0.getProperty(var1 + "." + var4)) != null; ++var4) {
         var2.addElement(var3);
      }

      String[] var5 = new String[var2.size()];
      var2.copyInto(var5);
      return var5;
   }

   boolean getAllowEnumeration() {
      return allowEnumeration;
   }

   LDAPDelegate(LDAPRealm var1) {
      this.owner = var1;
      this.log = var1.log;
      this.connPool = new Pool(new LDAPFactory(), 6);
      setupProperties();
      userConstraints = new LDAPSearchConstraints();
      userConstraints.setMaxResults(1);
      groupConstraints = new LDAPSearchConstraints();
      groupConstraints.setMaxResults(1);
      getUserConstraints = new LDAPSearchConstraints();
      getUserConstraints.setMaxResults(0);
      getUserConstraints.setBatchSize(1);
      getGroupConstraints = new LDAPSearchConstraints();
      getGroupConstraints.setMaxResults(0);
      getGroupConstraints.setBatchSize(1);
      memberConstraints = new LDAPSearchConstraints();
      memberConstraints.setMaxResults(0);
      memberConstraints.setBatchSize(1);
   }

   LDAPConnection getConnection() {
      try {
         return (LDAPConnection)this.connPool.getInstance();
      } catch (InvocationTargetException var2) {
         throw new LDAPRealmException("could not get connection", var2);
      }
   }

   void returnConnection(LDAPConnection var1) {
      if (var1 != null) {
         this.connPool.returnInstance(var1);
      }

   }

   LDAPUser authenticate(String var1, String var2) {
      var1 = this.replaceChar(var1, "*", "");
      LDAPUser var3 = this.getUser(var1);
      if (var3 != null && var2 != null && !"".equals(var2)) {
         LDAPConnection var4 = this.getConnection();
         LDAPUser var5 = null;

         try {
            try {
               if (this.log != null) {
                  this.log.debug("bind(" + var3.getDN() + ")");
               }

               var4.bind(3, var3.getDN(), var2);
               var5 = var3;
            } catch (LDAPException var11) {
               switch (var11.getLDAPResultCode()) {
                  case 19:
                     if (var4 != null) {
                        this.connPool.destroyInstance(var4);
                     }

                     var4 = this.getConnection();
                  case 48:
                  case 49:
                     break;
                  case 53:
                     if (var4 != null) {
                        this.connPool.destroyInstance(var4);
                     }

                     var4 = this.getConnection();
                     throw new LDAPRealmException(var11.getLDAPErrorMessage(), var11);
                  default:
                     this.handleException(var11);
               }
            }

            if (var4 != null) {
               var4.bind(3, serverPrincipal, serverCredential);
            }
         } catch (LDAPException var12) {
            this.handleException(var12);
         } finally {
            this.returnConnection(var4);
         }

         return var5;
      } else {
         return null;
      }
   }

   LDAPUser getUser(String var1) {
      try {
         int var2 = userBaseDNs.length > userFilters.length ? userBaseDNs.length : userFilters.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            String var4 = printf(userFilters[userFilters.length > 1 ? var3 : 0], 'u', getEncodedStringRepresentation(var1));
            String var5 = userBaseDNs[userBaseDNs.length > 1 ? var3 : 0];
            var5 = getEncodedStringRepresentation(var5);
            int var6 = userScopes[userScopes.length > 1 ? var3 : 0];
            if (this.log != null) {
               this.log.debug("search(\"" + var5 + "\", \"" + var4 + "\", " + scopeToString(var6) + ")");
            }

            LDAPSearchResults var7 = this.search(var5, var6, var4, USER_ATTRS, false, userConstraints);
            if (var7.hasMoreElements()) {
               String var8 = var7.next().getDN();
               String var9 = var8.indexOf("\\,") > -1 ? this.unescapeCommaForDN(var8) : var8;
               LDAPUser var10;
               if (caseSensitive && var9.indexOf(var1) == -1) {
                  if (this.log != null && var8.toLowerCase(Locale.ENGLISH).indexOf(var1.toLowerCase(Locale.ENGLISH)) != -1) {
                     this.log.debug("incorrect case for user: " + var1);
                  }

                  var10 = null;
                  return var10;
               }

               if (this.log != null) {
                  this.log.debug("DN for user " + var1 + ": " + var8);
               }

               var10 = new LDAPUser(var1, var8, this.owner);
               return var10;
            }
         }
      } catch (LDAPException var14) {
         this.handleException(var14);
      } finally {
         ;
      }

      if (this.log != null) {
         this.log.debug("User " + var1 + " not found.");
      }

      return null;
   }

   private static String getEncodedStringRepresentation(Object var0) {
      if (var0 == null) {
         return null;
      } else {
         StringBuffer var3;
         if (!(var0 instanceof byte[])) {
            String var1;
            if (!(var0 instanceof String)) {
               var1 = var0.toString();
            } else {
               var1 = (String)var0;
            }

            int var6 = var1.length();
            var3 = new StringBuffer(var6);

            for(int var5 = 0; var5 < var6; ++var5) {
               char var7;
               switch (var7 = var1.charAt(var5)) {
                  case '\u0000':
                     var3.append("\\00");
                     break;
                  case '(':
                     var3.append("\\28");
                     break;
                  case ')':
                     var3.append("\\29");
                     break;
                  case '*':
                     var3.append("\\2a");
                     break;
                  case '\\':
                     var3.append("\\5c");
                     break;
                  default:
                     var3.append(var7);
               }
            }

            return var3.toString();
         } else {
            byte[] var2 = (byte[])((byte[])var0);
            var3 = new StringBuffer(var2.length * 3);

            for(int var4 = 0; var4 < var2.length; ++var4) {
               var3.append('\\');
               hexDigit(var3, var2[var4]);
            }

            return var3.toString();
         }
      }
   }

   private static void hexDigit(StringBuffer var0, byte var1) {
      char var2 = (char)(var1 >> 4 & 15);
      if (var2 > '\t') {
         var2 = (char)(var2 - 10 + 65);
      } else {
         var2 = (char)(var2 + 48);
      }

      var0.append(var2);
      var2 = (char)(var1 & 15);
      if (var2 > '\t') {
         var2 = (char)(var2 - 10 + 65);
      } else {
         var2 = (char)(var2 + 48);
      }

      var0.append(var2);
   }

   private String replaceChar(String var1, String var2, String var3) {
      while(var1.indexOf(var2) != -1) {
         int var4 = var1.indexOf(var2);
         var1 = var1.substring(0, var4) + var3 + var1.substring(var4 + var2.length());
      }

      return var1;
   }

   void handleException(Exception var1) {
      throw new LDAPRealmException("caught unexpected exception", var1);
   }

   static String printf(String var0, char var1, String var2) {
      StringBuffer var3 = new StringBuffer();
      int var4 = var0.length();
      boolean var5 = false;

      for(int var6 = 0; var6 < var4; ++var6) {
         char var7 = var0.charAt(var6);
         if (var5) {
            if (var7 == var1) {
               var3.append(var2);
            } else {
               switch (var7) {
                  case '%':
                     var3.append(var7);
                     break;
                  default:
                     var3.append('%');
                     var3.append(var7);
               }
            }

            var5 = false;
         } else {
            switch (var7) {
               case '%':
                  var5 = true;
                  break;
               default:
                  var3.append(var7);
            }
         }
      }

      return var3.toString();
   }

   Group getGroup(String var1) {
      try {
         try {
            int var2 = groupBaseDNs.length > groupFilters.length ? groupBaseDNs.length : groupFilters.length;

            for(int var3 = 0; var3 < var2; ++var3) {
               String var4 = printf(groupFilters[groupFilters.length > 1 ? var3 : 0], 'g', getEncodedStringRepresentation(var1));
               String var5 = groupBaseDNs[groupBaseDNs.length > 1 ? var3 : 0];
               var5 = getEncodedStringRepresentation(var5);
               int var6 = groupScopes[groupScopes.length > 1 ? var3 : 0];
               if (this.log != null) {
                  this.log.debug("search(\"" + var5 + "\", \"" + var4 + "\", " + scopeToString(var6) + ")");
               }

               LDAPSearchResults var7 = this.search(var5, var6, var4, GROUP_ATTRS, false, groupConstraints);
               if (var7.hasMoreElements()) {
                  String var8 = var7.next().getDN();
                  if (this.log != null) {
                     this.log.debug("DN for group " + var1 + ": " + var8);
                  }

                  LDAPGroup var9 = new LDAPGroup(var1, var8, this, this.owner);
                  return var9;
               }
            }
         } catch (LDAPException var13) {
            this.handleException(var13);
         }

         return null;
      } finally {
         ;
      }
   }

   Enumeration groupMembers(String var1) {
      Enumeration var2 = null;

      try {
         try {
            var2 = this.groupMembersInternal(var1);
         } catch (LDAPException var7) {
            this.handleException(var7);
         }

         return var2;
      } finally {
         ;
      }
   }

   private Enumeration groupMembersInternal(String var1) throws LDAPException {
      Vector var2 = new Vector();
      var1 = getEncodedStringRepresentation(var1);

      for(int var3 = 0; var3 < memberFilters.length; ++var3) {
         String var4 = printf(memberFilters[memberFilters.length > 1 ? var3 : 0], 'G', var1);
         var4 = printf(var4, 'M', "*");
         String[] var5 = memberAttributes[memberFilters.length > 1 ? var3 : 0];
         int var6 = memberScopes[memberScopes.length > 1 ? var3 : 0];
         if (this.log != null) {
            this.log.debug("search(\"" + var1 + "\", \"" + var4 + "\", " + scopeToString(var6) + ")");
         }

         LDAPSearchResults var7 = this.search(var1, var6, var4, var5, false, memberConstraints);

         while(var7.hasMoreElements()) {
            LDAPEntry var8 = var7.next();
            String var9 = var8.getDN();
            LDAPAttributeSet var10 = var8.getAttributeSet();
            Enumeration var11 = var10.getAttributes();

            while(var11.hasMoreElements()) {
               LDAPAttribute var12 = (LDAPAttribute)var11.nextElement();
               String var13 = var12.getName();
               Enumeration var14 = var12.getStringValues();

               while(var14.hasMoreElements()) {
                  String var15 = (String)var14.nextElement();
                  String var16 = LDAPDN.normalize(var15);
                  LDAPUser var17 = this.searchDNForUser(var15);
                  boolean var18 = false;
                  if (var17 != null) {
                     var18 = true;
                     var2.addElement(var17);
                  } else {
                     LDAPGroup var19 = this.searchDNForGroup(var15);
                     if (var19 != null) {
                        var18 = true;
                        var2.addElement(var19);
                     }
                  }

                  if (!var18 && membershipSearch) {
                     LDAPUser var20 = this.readDNForUser(var15);
                     if (var20 != null) {
                        var18 = true;
                        var2.addElement(var20);
                     }
                  }
               }
            }
         }
      }

      return var2.elements();
   }

   private LDAPUser readDNForUser(String var1) {
      try {
         try {
            String var2 = "(objectclass=*)";
            if (this.log != null) {
               this.log.debug("search(\"" + var1 + "\", \"(objectclass=*)\", " + scopeToString(2) + ")");
            }

            LDAPSearchResults var3 = null;

            try {
               var3 = this.search(var1, 2, var2, new String[]{"*"}, false, userConstraints);
            } catch (LDAPException var10) {
               if (this.log != null) {
                  this.log.debug("ignoring " + var10.getMessage());
               }
            }

            if (var3 != null) {
               while(var3.hasMoreElements()) {
                  LDAPUser var4 = this.checkEntryForUser(var3.next());
                  if (var4 != null) {
                     return var4;
                  }
               }
            }
         } catch (Exception var11) {
            this.handleException(var11);
         }

         return null;
      } finally {
         ;
      }
   }

   private LDAPUser checkEntryForUser(LDAPEntry var1) {
      Enumeration var2 = var1.getAttributeSet().getAttributes();
      if (!var2.hasMoreElements()) {
         return this.searchDNForUser(var1.getDN());
      } else {
         while(var2.hasMoreElements()) {
            LDAPAttribute var3 = (LDAPAttribute)var2.nextElement();
            String var4 = var3.getBaseName();
            Enumeration var5 = var3.getStringValues();

            for(int var6 = 0; var6 < userNameAttributes.length; ++var6) {
               for(int var7 = 0; var7 < userNameAttributes[var6].length; ++var7) {
                  if (var5.hasMoreElements()) {
                     String var8 = (String)var5.nextElement();
                     if (var4.equals(userNameAttributes[var6][var7]) && this.getUser(var8) != null) {
                        LDAPUser var9 = new LDAPUser(var8, var1.getDN(), this.owner);
                        return var9;
                     }
                  }
               }
            }
         }

         return null;
      }
   }

   private LDAPUser searchDNForUser(String var1) {
      for(int var2 = 0; var2 < userNameAttributes.length; ++var2) {
         for(int var3 = 0; var3 < userNameAttributes[var2].length; ++var3) {
            if (var1.indexOf(userNameAttributes[var2][var3]) > -1) {
               String var4 = this.getAttributeValue(userNameAttributes[var2][var3], var1);
               if (var4 != null && this.getUser(var4) != null) {
                  return new LDAPUser(var4, var1, this.owner);
               }
            }
         }
      }

      return null;
   }

   private LDAPGroup searchDNForGroup(String var1) {
      for(int var2 = 0; var2 < groupNameAttributes.length; ++var2) {
         for(int var3 = 0; var3 < groupNameAttributes[var2].length; ++var3) {
            if (var1.indexOf(groupNameAttributes[var2][var3]) > -1) {
               String var4 = this.getAttributeValue(groupNameAttributes[var2][var3], var1);
               if (var4 != null && this.getGroup(var4) != null) {
                  return new LDAPGroup(var4, var1, this, this.owner);
               }
            }
         }
      }

      return null;
   }

   private String unescapeCommaForDN(String var1) {
      StringBuffer var2 = new StringBuffer();

      for(int var3 = 0; var3 < var1.length(); ++var3) {
         char var4 = var1.charAt(var3);
         if (var4 != '\\') {
            var2.append(var4);
         }
      }

      return var2.toString();
   }

   private String getAttributeValue(String var1, String var2) {
      String var3 = var1 + "=";
      int var4 = var2.indexOf(var3);
      if (var4 == -1) {
         return null;
      } else {
         int var5 = var4 + var3.length();
         String var6 = var2.substring(var5);
         if (var6.startsWith("=")) {
            var6 = var6.substring(1);
         }

         int var7 = var6.indexOf(",");
         int var8 = var6.indexOf("\\");
         if (var8 != -1 && var7 - var8 == 1) {
            for(int var9 = 0; var9 < var6.length(); ++var9) {
               String var10 = var6.substring(0, var9);
               if (var10.endsWith(",") && !var10.endsWith("\\,")) {
                  return this.unescapeCommaForDN(var10.substring(0, var10.length() - 1));
               }
            }

            if (this.log != null) {
               this.log.debug("Could not find an unescaped comma in the following DN - " + var6);
            }

            return null;
         } else {
            return var6.substring(0, var7).trim();
         }
      }
   }

   boolean groupContains(String var1, String var2) {
      boolean var3 = false;

      try {
         try {
            var3 = this.groupContainsInternal(var1, var2, new Stack());
         } catch (LDAPException var8) {
            this.handleException(var8);
         }

         return var3;
      } finally {
         ;
      }
   }

   private boolean groupContainsInternal(String var1, String var2, Stack var3) throws LDAPException {
      if (var1.equals(var2)) {
         return true;
      } else {
         checkForCircularity(var1, var2, var3);
         var3.push(var2);
         int var4 = groupBaseDNs.length > memberFilters.length ? groupBaseDNs.length : memberFilters.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String var6 = printf(memberFilters[memberFilters.length > 1 ? var5 : 0], 'G', var1);
            var6 = printf(var6, 'M', getEncodedStringRepresentation(var2));
            String var7 = groupBaseDNs[groupBaseDNs.length > 1 ? var5 : 0];
            var7 = getEncodedStringRepresentation(var7);
            int var8 = memberScopes[memberScopes.length > 1 ? var5 : 0];
            if (this.log != null) {
               this.log.debug("search(\"" + var7 + "\", \"" + var6 + "\", " + scopeToString(var8) + ")");
            }

            LDAPSearchResults var9 = this.search(var7, var8, var6, MEMBER_ATTRS, false, memberConstraints);
            Vector var10 = new Vector();

            while(true) {
               while(var9.hasMoreElements()) {
                  LDAPEntry var11 = var9.next();
                  String var12 = var11.getDN();
                  if (groupScopeDepth > 0) {
                     for(int var13 = groupScopeDepth; var13 >= 0; --var13) {
                        if (var13 > 0) {
                           int var14 = var12.indexOf(44);
                           if (var14 == -1) {
                              throw new LDAPRealmException("group scope depth is greater than the number of RDNs in '" + var12 + "'");
                           }

                           var12 = var12.substring(var14 + 1).trim();
                        }

                        if (var1.equals(var12)) {
                           return true;
                        }

                        var10.add(var12);
                     }
                  } else {
                     if (var1.equals(var12)) {
                        return true;
                     }

                     var10.add(var12);
                  }
               }

               if (!directMembershipOnly) {
                  Enumeration var15 = var10.elements();

                  while(var15.hasMoreElements()) {
                     if (this.groupContainsInternal(var1, (String)var15.nextElement(), var3)) {
                        return true;
                     }
                  }
               }
               break;
            }
         }

         var3.pop();
         return false;
      }
   }

   private static void checkForCircularity(String var0, String var1, Stack var2) throws LDAPRealmException {
      if (var2.contains(var1)) {
         StringBuffer var3 = new StringBuffer("circular group: while checking '");
         var3.append(var0);
         var3.append("', found ");
         var3.append(var1);

         String var4;
         do {
            var3.append(" -> ");
            var3.append(var4 = (String)var2.pop());
         } while(!var4.equals(var1) && !var2.empty());

         throw new LDAPRealmException(var3.toString());
      }
   }

   Enumeration getUsers() {
      int var1 = userBaseDNs.length > userFilters.length ? userBaseDNs.length : userFilters.length;
      Enumeration[] var2 = new Enumeration[var1];
      LDAPConnection var3 = this.getConnection();
      LDAPSearchConstraints var4 = var3.getSearchConstraints();
      this.returnConnection(var3);
      int var5 = var4.getMaxResults();
      var4.setMaxResults(100);

      try {
         for(int var6 = 0; var6 < var1; ++var6) {
            String var7 = printf(userFilters[userFilters.length > 1 ? var6 : 0], 'u', "*");
            String[] var8 = userNameAttributes[userFilters.length > 1 ? var6 : 0];
            String var9 = userBaseDNs[userBaseDNs.length > 1 ? var6 : 0];
            var9 = getEncodedStringRepresentation(var9);
            int var10 = userScopes[userScopes.length > 1 ? var6 : 0];
            var2[var6] = new UserMangler(var8, this.search(var9, var10, var7, USER_ATTRS, false, getUserConstraints));
         }
      } catch (LDAPException var14) {
         this.handleException(var14);
      } finally {
         ;
      }

      return new SequencingEnumerator(var2);
   }

   Enumeration getGroups() {
      int var1 = groupBaseDNs.length > groupFilters.length ? groupBaseDNs.length : groupFilters.length;
      Enumeration[] var2 = new Enumeration[var1];

      try {
         for(int var3 = 0; var3 < var1; ++var3) {
            String var4 = printf(groupFilters[groupFilters.length > 1 ? var3 : 0], 'g', "*");
            String[] var5 = groupNameAttributes[groupFilters.length > 1 ? var3 : 0];
            String var6 = groupBaseDNs[groupBaseDNs.length > 1 ? var3 : 0];
            var6 = getEncodedStringRepresentation(var6);
            int var7 = groupScopes[groupScopes.length > 1 ? var3 : 0];
            if (this.log != null) {
               this.log.debug("search(\"" + var6 + "\", \"" + var4 + "\", " + scopeToString(var7) + ", \"" + var5[0] + "\")");
            }

            var2[var3] = new GroupMangler(var5, this.search(var6, var7, var4, var5, false, getGroupConstraints));
         }
      } catch (LDAPException var11) {
         this.handleException(var11);
      } finally {
         ;
      }

      return new SequencingEnumerator(var2);
   }

   private LDAPSearchResults search(String var1, int var2, String var3, String[] var4, boolean var5, LDAPSearchConstraints var6) throws LDAPException {
      LDAPSearchResults var7 = null;
      LDAPConnection var8 = this.getConnection();
      int var9 = 0;
      byte var10 = 2;

      while(var9 < var10) {
         try {
            var7 = var8.search(var1, var2, var3, var4, var5, var6);
            var9 = 2;
         } catch (LDAPException var15) {
            if (var15.getLDAPResultCode() != 80 && var15.getLDAPResultCode() != 81) {
               this.returnConnection(var8);
               throw var15;
            }

            ++var9;
            synchronized(this) {
               this.returnConnection(var8);
               this.connPool.close();
               this.connPool = new Pool(new LDAPFactory(), 6);
               var8 = this.getConnection();
            }
         }
      }

      this.returnConnection(var8);
      return var7;
   }

   void close() {
      this.connPool.close();
      this.connPool = null;
   }

   public void setDebugLog(LogOutputStream var1) {
      if (var1 != null) {
         this.log = var1;
      }

   }

   public LogOutputStream getDebugLog() {
      return this.log;
   }

   private static class FocketSactory implements LDAPSocketFactory {
      private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

      private FocketSactory() {
      }

      public Socket makeSocket(String var1, int var2) throws LDAPException {
         try {
            SSLSocketFactory var3 = SSLContextManager.getDefaultSSLSocketFactory(kernelId);
            SSLSocket var6 = (SSLSocket)var3.createSocket(var1, var2);
            var6.startHandshake();
            return var6;
         } catch (Exception var5) {
            LDAPException var4 = new LDAPException(var5.getMessage(), 91);
            var4.initCause(var5);
            throw var4;
         }
      }

      // $FF: synthetic method
      FocketSactory(Object var1) {
         this();
      }
   }

   class LDAPFactory implements Factory {
      public Object newInstance() throws InvocationTargetException {
         try {
            if (LDAPDelegate.this.log != null) {
               LDAPDelegate.this.log.debug("new LDAP connection");
            }

            LDAPConnection var1 = null;
            if (LDAPDelegate.useSSL) {
               var1 = new LDAPConnection(LDAPDelegate.boxOfSocks);
            } else {
               var1 = new LDAPConnection();
            }

            var1.connect(LDAPDelegate.serverHost, LDAPDelegate.serverPort);
            var1.bind(3, LDAPDelegate.serverPrincipal, LDAPDelegate.serverCredential);
            if (LDAPDelegate.cacheTTL > 0 && LDAPDelegate.cacheSize > 0) {
               var1.setCache(new LDAPCache((long)LDAPDelegate.cacheTTL, (long)(LDAPDelegate.cacheSize * 1024)));
               if (LDAPDelegate.this.log != null) {
                  LDAPDelegate.this.log.debug("connection succeeded - cacheTTL " + LDAPDelegate.cacheTTL + ", cacheSize " + LDAPDelegate.cacheSize * 1024);
               }
            } else if (LDAPDelegate.this.log != null) {
               LDAPDelegate.this.log.debug("connection succeeded");
            }

            return var1;
         } catch (LDAPException var2) {
            throw new InvocationTargetException(var2);
         }
      }

      public void destroyInstance(Object var1) {
         try {
            if (LDAPDelegate.this.log != null) {
               LDAPDelegate.this.log.debug("destroy LDAP connection");
            }

            ((LDAPConnection)var1).disconnect();
         } catch (LDAPException var3) {
         }

      }
   }

   class GroupMangler extends MappingEnumerator {
      String[] attrNames;

      GroupMangler(String[] var2, Enumeration var3) {
         super(var3);
         this.attrNames = var2;
      }

      protected Object map(Object var1) {
         Object var2 = null;
         if (var1 instanceof LDAPEntry) {
            LDAPEntry var3 = (LDAPEntry)var1;
            String var4 = var3.getDN();

            for(int var5 = 0; var5 < this.attrNames.length; ++var5) {
               LDAPAttribute var6 = var3.getAttribute(this.attrNames[var5]);
               if (var6 != null) {
                  String[] var7 = var6.getStringValueArray();
                  return new LDAPGroup(var7[0], var4, LDAPDelegate.this, LDAPDelegate.this.owner);
               }
            }
         } else if (var1 instanceof Exception) {
            LDAPDelegate.this.handleException((Exception)var1);
         }

         throw new LDAPRealmException("ClassCastException: received a " + var1.getClass().getName() + "[" + var1.toString() + "]" + ", expected a netscape.ldap.LDAPEntry");
      }
   }

   class UserMangler extends MappingEnumerator {
      String[] attrNames;

      UserMangler(String[] var2, Enumeration var3) {
         super(var3);
         this.attrNames = var2;
      }

      protected Object map(Object var1) {
         Object var2 = null;
         if (var1 instanceof LDAPEntry) {
            LDAPEntry var3 = (LDAPEntry)var1;
            String var4 = var3.getDN();
            String var5 = LDAPDelegate.this.getAttributeValue(this.attrNames[0], var4);
            if (var5 != null) {
               return new LDAPUser(var5, var4, LDAPDelegate.this.owner);
            } else {
               return LDAPDelegate.membershipSearch ? LDAPDelegate.this.readDNForUser(var4) : null;
            }
         } else {
            if (var1 instanceof Exception) {
               LDAPDelegate.this.handleException((Exception)var1);
            }

            throw new LDAPRealmException("ClassCastException: received a " + var1.getClass().getName() + "[" + var1.toString() + "]" + ", expected an netscape.ldap.LDAPEntry");
         }
      }
   }
}
