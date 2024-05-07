package weblogic.security.ldaprealmv1;

import java.lang.reflect.InvocationTargetException;
import java.security.AccessController;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.acl.Group;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.naming.CommunicationException;
import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.NamingSecurityException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import weblogic.logging.LogOutputStream;
import weblogic.management.configuration.LDAPRealmMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.SecurityLogger;
import weblogic.security.acl.ClosableEnumeration;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.utils.Factory;
import weblogic.security.utils.Pool;
import weblogic.utils.encoders.BASE64Encoder;
import weblogic.utils.enumerations.EnumerationUtils;

/** @deprecated */
class LDAPDelegate {
   private static final int POOL_SIZE = 6;
   private static String url;
   private static String auth;
   private static boolean ssl;
   private static String factory;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static String userDN;
   private static String ndresu;
   private static String userNameAttr;
   private static String userPwdAttr;
   private static boolean authUsingBind;
   private static String groupDN;
   private static String groupNameAttr;
   private static String groupUserAttr;
   private static boolean groupIsContext;
   private Pool ctxPool;
   private LDAPRealm owner;
   LogOutputStream log;

   static Properties configureProps() {
      LDAPRealmMBean var0 = (LDAPRealmMBean)ManagementService.getRuntimeAccess(kernelId).getDomain().getSecurity().getRealm().getCachingRealm().getBasicRealm();
      url = var0.getLDAPURL();
      auth = var0.getAuthProtocol();
      ssl = var0.getSSLEnable();
      factory = var0.getLdapProvider();
      userDN = var0.getUserDN();
      userNameAttr = var0.getUserNameAttribute();
      ndresu = reverseDN(userDN);
      String var1 = var0.getUserAuthentication();
      if (var1.equals("local")) {
         userPwdAttr = var0.getUserPasswordAttribute();
         authUsingBind = false;
      } else {
         if (!var1.equals("bind")) {
            throw new LDAPException("invalid user authentication mechanism \"" + var1 + "\"");
         }

         authUsingBind = true;
      }

      groupDN = var0.getGroupDN();
      groupNameAttr = var0.getGroupNameAttribute();
      groupUserAttr = var0.getGroupUsernameAttribute();
      groupIsContext = var0.getGroupIsContext();
      if (auth.equals("none")) {
         return makeProperties();
      } else if (auth.equals("EXTERNAL")) {
         if (!ssl) {
            throw new LDAPException("must use SSL if specifying external authentication");
         } else {
            return makeProperties();
         }
      } else if (!auth.equals("simple") && !auth.equals("CRAM-MD5")) {
         throw new LDAPException("authentication mechanism \"" + auth + "\" is unknown or unsupported");
      } else {
         String var2 = var0.getPrincipal();
         String var3 = var0.getCredential();
         return makeProperties(var2, var3);
      }
   }

   private static String reverseDN(String var0) {
      StringTokenizer var1 = new StringTokenizer(var0, ",");
      Vector var2 = new Vector();

      while(var1.hasMoreTokens()) {
         var2.insertElementAt(var1.nextToken().trim(), 0);
      }

      return EnumerationUtils.toString(var2.elements(), ",");
   }

   private static Properties makeProperties() {
      Properties var0 = new Properties();
      var0.put("java.naming.security.authentication", auth);
      var0.put("java.naming.factory.initial", factory);
      var0.put("java.naming.provider.url", url);
      if (ssl) {
         var0.put("java.naming.ldap.factory.socket", "weblogic.security.SSL.SSLSocketFactory");
      }

      return var0;
   }

   private static Properties makeProperties(String var0, Object var1) {
      Properties var2 = makeProperties();
      if (var0 != null && var1 != null) {
         var2.put("java.naming.security.principal", var0);
         var2.put("java.naming.security.credentials", var1);
         return var2;
      } else {
         throw new LDAPException("missing properties for simple authentication");
      }
   }

   LDAPDelegate(LDAPRealm var1) {
      this.owner = var1;
      this.log = var1.log;
      this.ctxPool = new Pool(new DFactory(configureProps(), this), 6);
   }

   private DirContext getContext() {
      try {
         return (DirContext)this.ctxPool.getInstance();
      } catch (InvocationTargetException var2) {
         throw new LDAPException("could not get context", var2);
      }
   }

   private void returnContext(DirContext var1) {
      if (var1 != null) {
         this.ctxPool.returnInstance(var1);
      }

   }

   private DirContext lookup(String var1) throws NamingException {
      if (this.log != null) {
         this.log.debug("lookup(\"" + var1 + "\")");
      }

      DirContext var2 = this.getContext();

      DirContext var5;
      try {
         String var3 = reverseDN(var1);
         DirContext var4 = (DirContext)var2.lookup(var3);
         var5 = var4;
      } catch (CommunicationException var9) {
         var2 = null;
         throw new LDAPException("communication failed", var9);
      } finally {
         this.returnContext(var2);
      }

      return var5;
   }

   private Stack reverseMatchDNs(String var1, String var2) {
      StringTokenizer var3 = new StringTokenizer(var2, ",");
      StringTokenizer var4 = new StringTokenizer(var1, ",");
      Stack var5 = new Stack();

      while(var4.hasMoreTokens()) {
         var5.push(var4.nextToken().trim());
      }

      while(var3.hasMoreTokens() && !var5.empty()) {
         String var6 = var3.nextToken().trim();
         String var7 = (String)var5.pop();
         if (!var7.equalsIgnoreCase(var6)) {
            return null;
         }
      }

      return var3.hasMoreTokens() ? null : var5;
   }

   private String getAttributeFromDN(String var1, Stack var2) {
      String var3 = (String)var2.pop();
      String var4;
      if (!var2.empty()) {
         for(var4 = "unexpectedly long DN: " + var3; !var2.empty() && var4.length() < 128; var4 = var4 + ", " + (String)var2.pop()) {
         }

         throw new LDAPException(var4);
      } else {
         var4 = var1 + "=";
         int var5 = var4.length();
         if (var3.regionMatches(true, 0, var4, 0, var5)) {
            return var3.substring(var5);
         } else {
            String var6;
            for(var6 = "unexpected DN head \"" + var3 + "\" on DN: " + var3; !var2.empty() && var6.length() < 128; var6 = var6 + ", " + (String)var2.pop()) {
            }

            throw new LDAPException(var6);
         }
      }
   }

   private NamingEnumeration search(String var1, String var2, String var3) throws NamingException {
      if (this.log != null) {
         this.log.debug("search(\"" + var1 + "\", \"" + var2 + "\", \"" + var3 + "\")");
      }

      SearchControls var4 = new SearchControls();
      var4.setSearchScope(1);
      DirContext var5 = this.lookup(var1);
      return var5.search("", "(" + var2 + "=" + var3 + ")", var4);
   }

   private NamingEnumeration search(String var1, String var2) throws NamingException {
      return this.search(var1, var2, "*");
   }

   private Attributes getUserAttrs(String var1) {
      try {
         DirContext var2 = this.lookup(userDN);
         if (var2 == null) {
            if (this.log != null) {
               this.log.debug("user: UNPERSON " + var1);
            }

            return null;
         } else {
            return var2.getAttributes(userNameAttr + "=" + var1);
         }
      } catch (NameNotFoundException var3) {
         if (this.log != null) {
            this.log.debug("user: UNPERSON " + var1);
         }

         return null;
      } catch (NamingException var4) {
         throw new LDAPException("search error: user " + var1, var4);
      }
   }

   boolean userExists(String var1) {
      return this.getUserAttrs(var1) != null;
   }

   boolean authenticate(String var1, String var2) {
      return authUsingBind ? this.authBind(var1, var2) : this.authLocal(var1, var2);
   }

   private boolean authBind(String var1, String var2) {
      String var3 = userNameAttr + "=" + var1 + "," + ndresu;
      Properties var4 = makeProperties(var3, var2);
      InitialDirContext var5 = null;

      boolean var7;
      try {
         var5 = new InitialDirContext(var4);
         return true;
      } catch (NamingSecurityException var18) {
         var7 = false;
      } catch (NamingException var19) {
         throw new LDAPException("unexpected naming exception", var19);
      } finally {
         if (var5 != null) {
            try {
               var5.close();
            } catch (NamingException var17) {
            }
         }

      }

      return var7;
   }

   private boolean authLocal(String var1, String var2) {
      Attributes var3 = this.getUserAttrs(var1);
      if (var3 == null) {
         if (this.log != null) {
            this.log.debug("auth: UNPERSON " + var1);
         }

         return false;
      } else {
         Attribute var4 = var3.get(userPwdAttr);
         if (var4 == null) {
            throw new SecurityException("no password found for " + var1);
         } else {
            String var5 = null;

            try {
               var5 = (new String((byte[])((byte[])var4.get()))).trim();
            } catch (NamingException var8) {
               throw new LDAPException("password get failed", var8);
            }

            Attribute var6 = var3.get(userNameAttr);

            try {
               return var1.equals(((String)var6.get()).trim()) && this.checkPassword(var5, var2);
            } catch (NamingException var9) {
               return false;
            }
         }
      }
   }

   protected boolean checkPassword(String var1, String var2) {
      if (var1 == null) {
         return var2 == null;
      } else {
         int var3 = var1.indexOf("}");
         if (var1.charAt(0) == '{' && var3 > 0) {
            String var4 = var1.substring(1, var3);
            String var5 = this.hash(var4, var2);
            String var6 = var1.substring(var3 + 1);
            return var6.equals(var5);
         } else {
            return var1.equals(var2);
         }
      }
   }

   protected String hash(String var1, String var2) {
      try {
         MessageDigest var3 = MessageDigest.getInstance(var1.toUpperCase(Locale.ENGLISH));
         BASE64Encoder var4 = new BASE64Encoder();
         return var4.encodeBuffer(var3.digest(var2.getBytes()));
      } catch (NoSuchAlgorithmException var5) {
         return new String(var2);
      }
   }

   Group getGroup(String var1) {
      return this.getGroup(var1, new Stack());
   }

   public Hashtable getGroupMembers(String var1) {
      return this.getGroupMembers(var1, new Stack());
   }

   Group getGroup(String var1, Stack var2) {
      Hashtable var3 = this.getGroupMembers(var1, var2);
      return var3 != null ? new LDAPGroup(var1, this.owner, var3) : null;
   }

   Hashtable getGroupMembers(String var1, Stack var2) {
      Hashtable var3 = new Hashtable();

      try {
         Attribute var6;
         if (groupIsContext) {
            NamingEnumeration var4 = this.search(groupDN + ", " + groupNameAttr + "=" + var1, groupNameAttr);
            if (var4 == null) {
               if (this.log != null) {
                  this.log.debug("group: UNGROUP " + var1);
               }

               return null;
            }

            while(var4.hasMore()) {
               SearchResult var5 = (SearchResult)var4.next();
               var6 = var5.getAttributes().get(groupUserAttr);
               if (var6 != null) {
                  this.addGroupMember(var1, ((String)var6.get()).trim(), var3, var2);
               }
            }
         } else {
            DirContext var10 = this.lookup(groupDN);
            Attributes var11 = var10.getAttributes(groupNameAttr + "=" + var1);
            var6 = var11.get(groupUserAttr);
            if (var6 != null) {
               NamingEnumeration var7 = var6.getAll();
               if (var7 != null) {
                  while(var7.hasMore()) {
                     this.addGroupMember(var1, (String)var7.next(), var3, var2);
                  }
               }
            }
         }
      } catch (NameNotFoundException var8) {
         if (this.log != null) {
            this.log.debug("group: UNGROUP " + var1);
         }

         return null;
      } catch (NamingException var9) {
         throw new LDAPException("group lookup failed", var9);
      }

      if (this.log != null) {
         this.log.debug("group: FOUND " + var1);
      }

      return var3;
   }

   private void addGroupMember(String var1, String var2, Hashtable var3, Stack var4) {
      Stack var5 = this.reverseMatchDNs(var2, userDN);
      String var6;
      if (var5 != null) {
         var6 = this.getAttributeFromDN(userNameAttr, var5);
         var3.put(var6, new LDAPUser(var6, this.owner));
      } else {
         var5 = this.reverseMatchDNs(var2, groupDN);
         if (var5 == null) {
            return;
         }

         var6 = this.getAttributeFromDN(groupNameAttr, var5);
         var4.push(var1);
         if (var4.contains(var6)) {
            if (this.log != null) {
               SecurityLogger.logUnsupportedCircularGroup(var6);
            }

            StringBuffer var7 = new StringBuffer("unsupported circular group definition: ");
            var7.append(var6);

            while(!var4.empty()) {
               var7.append(" -> " + var4.pop());
            }

            throw new LDAPException(var7.toString());
         }

         var3.put(var6, this.getGroup(var6, var4));
         var4.pop();
      }

   }

   Enumeration getGroups() {
      try {
         NamingEnumeration var1 = this.search(groupDN, groupNameAttr);
         if (var1 == null) {
            throw new LDAPException("could not get group list");
         } else {
            return new LDAPEnumeration(var1, new LDAPNextHandler() {
               public Object handle(Object var1) throws NamingException {
                  SearchResult var2 = (SearchResult)var1;
                  Attributes var3 = var2.getAttributes();
                  String var4 = ((String)var3.get(LDAPDelegate.groupNameAttr).get()).trim();
                  return new LDAPGroup(var4, LDAPDelegate.this.owner);
               }
            });
         }
      } catch (NamingException var2) {
         throw new LDAPException("could not get group list", var2);
      }
   }

   Enumeration getUsers() {
      try {
         NamingEnumeration var1 = this.search(userDN, userNameAttr);
         if (var1 == null) {
            throw new LDAPException("could not get user list");
         } else {
            return new LDAPEnumeration(var1, new LDAPNextHandler() {
               public Object handle(Object var1) throws NamingException {
                  SearchResult var2 = (SearchResult)var1;
                  Attributes var3 = var2.getAttributes();
                  String var4 = ((String)var3.get(LDAPDelegate.userNameAttr).get()).trim();
                  return new LDAPUser(var4, LDAPDelegate.this.owner);
               }
            });
         }
      } catch (NamingException var2) {
         throw new LDAPException("could not get user list", var2);
      }
   }

   void close() {
      this.ctxPool.close();
      this.ctxPool = null;
   }

   public void setDebugLog(LogOutputStream var1) {
      this.log = var1;
   }

   public LogOutputStream getDebugLog() {
      return this.log;
   }

   private class LDAPEnumeration implements ClosableEnumeration {
      boolean closed = false;
      NamingEnumeration list;
      LDAPNextHandler handler;

      LDAPEnumeration(NamingEnumeration var2, LDAPNextHandler var3) {
         this.list = var2;
         this.handler = var3;
      }

      public boolean hasMoreElements() {
         if (this.closed) {
            return false;
         } else {
            try {
               if (this.list.hasMore()) {
                  return true;
               } else {
                  this.close();
                  return false;
               }
            } catch (NamingException var2) {
               throw new LDAPException("LDAPEnumeration.hasMoreElements failed", var2);
            }
         }
      }

      public Object nextElement() {
         if (this.closed) {
            throw new NoSuchElementException("LDAPEnumeration.nextElement");
         } else {
            try {
               Object var1 = this.handler.handle(this.list.next());
               if (!this.list.hasMore()) {
                  this.close();
               }

               return var1;
            } catch (NamingException var2) {
               throw new LDAPException("LDAPEnumeration.nextElement failed", var2);
            }
         }
      }

      public void close() {
         if (!this.closed) {
            try {
               this.closed = true;
               this.list.close();
            } catch (NamingException var2) {
               throw new LDAPException("LDAPEnumeration.close failed", var2);
            }
         }
      }
   }

   private interface LDAPNextHandler {
      Object handle(Object var1) throws NamingException;
   }

   static class DFactory implements Factory {
      private Properties props;
      private LDAPDelegate owner;

      DFactory(Properties var1, LDAPDelegate var2) {
         this.props = var1;
         this.owner = var2;
      }

      public Object newInstance() throws InvocationTargetException {
         try {
            if (this.owner.log != null) {
               this.owner.log.debug("new JNDI context");
            }

            return new InitialDirContext(this.props);
         } catch (NamingException var2) {
            throw new InvocationTargetException(var2);
         }
      }

      public void destroyInstance(Object var1) {
         try {
            if (this.owner.log != null) {
               this.owner.log.debug("destroy JNDI context");
            }

            ((DirContext)var1).close();
         } catch (NamingException var3) {
         }

      }
   }
}
