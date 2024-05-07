package weblogic.ldap;

import com.asn1c.core.Int8;
import com.octetstring.ldapv3.Filter;
import com.octetstring.vde.Attribute;
import com.octetstring.vde.Credentials;
import com.octetstring.vde.Entry;
import com.octetstring.vde.EntryChange;
import com.octetstring.vde.EntrySet;
import com.octetstring.vde.backend.BackendHandler;
import com.octetstring.vde.operation.LDAPResult;
import com.octetstring.vde.schema.AttributeType;
import com.octetstring.vde.schema.SchemaChecker;
import com.octetstring.vde.syntax.BinarySyntax;
import com.octetstring.vde.syntax.DirectoryString;
import com.octetstring.vde.syntax.Syntax;
import com.octetstring.vde.util.DNUtility;
import com.octetstring.vde.util.DirectoryException;
import com.octetstring.vde.util.DirectorySchemaViolation;
import com.octetstring.vde.util.InvalidDNException;
import com.octetstring.vde.util.ParseFilter;
import com.octetstring.vde.util.PasswordEncryptor;
import com.octetstring.vde.util.ServerConfig;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.AccessController;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import netscape.ldap.LDAPAttribute;
import netscape.ldap.LDAPAttributeSet;
import netscape.ldap.LDAPCache;
import netscape.ldap.LDAPConnection;
import netscape.ldap.LDAPConstraints;
import netscape.ldap.LDAPControl;
import netscape.ldap.LDAPEntry;
import netscape.ldap.LDAPException;
import netscape.ldap.LDAPExtendedOperation;
import netscape.ldap.LDAPModification;
import netscape.ldap.LDAPModificationSet;
import netscape.ldap.LDAPResponseListener;
import netscape.ldap.LDAPSearchConstraints;
import netscape.ldap.LDAPSearchListener;
import netscape.ldap.LDAPSearchResults;
import netscape.ldap.LDAPSocketFactory;
import netscape.ldap.LDAPUrl;
import weblogic.logging.LogOutputStream;
import weblogic.management.provider.ManagementService;
import weblogic.security.SSL.CertPathTrustManager;
import weblogic.security.SSL.TrustManager;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.utils.SSLContextManager;

public class EmbeddedLDAPConnection extends LDAPConnection {
   private static final long serialVersionUID = -932303772832631138L;
   Credentials creds;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static LDAPSocketFactory embeddedLDAPSocketFactoryUseCertPathValidators = new EmbeddedLDAPSocketFactory(false);
   private static LDAPSocketFactory embeddedLDAPSocketFactoryIgnoreCertPathValidators = new EmbeddedLDAPSocketFactory(true);
   private boolean inMasterServer;
   private boolean useMasterFirst;
   private boolean writeFailover;
   private LDAPCache cache;
   private LDAPSearchConstraints defaultConstraints;
   private LDAPConnection delegate;
   private String savedHost;
   private int savedPort;
   private String savedDN;
   private String savedPasswd;
   private int protocolVersion;
   private boolean useSSL;
   private boolean ignoreCertPathValidators;
   private String masterHost;
   private int masterPort;
   private static final LogOutputStream log = new LogOutputStream("EmbeddedLDAP");
   private boolean debugEnabled;
   private static final DirectoryString EMPTY_DIRSTRING = new DirectoryString("");
   private static final DirectoryString USERPASSWORD = new DirectoryString("userPassword");
   private boolean msiMode;
   private final String MSI_NO_WRITE_FAILOVER;
   private final boolean MSI_LOCAL_LDAP_ONLY;

   public EmbeddedLDAPConnection(boolean var1) {
      this(var1, true);
   }

   public EmbeddedLDAPConnection(boolean var1, boolean var2) {
      this(var1, var2, false);
   }

   public EmbeddedLDAPConnection(boolean var1, boolean var2, boolean var3) {
      this.creds = null;
      this.inMasterServer = false;
      this.useMasterFirst = false;
      this.writeFailover = true;
      this.cache = null;
      this.defaultConstraints = new LDAPSearchConstraints();
      this.delegate = null;
      this.savedHost = null;
      this.savedPort = -1;
      this.savedDN = null;
      this.savedPasswd = null;
      this.protocolVersion = 2;
      this.useSSL = false;
      this.ignoreCertPathValidators = false;
      this.masterHost = null;
      this.masterPort = -1;
      this.msiMode = false;
      this.MSI_NO_WRITE_FAILOVER = "Admin server unavailable and write failover is not enabled.";
      this.MSI_LOCAL_LDAP_ONLY = Boolean.valueOf(System.getProperty("weblogic.security.MSILocalLDAPOnly", "false"));
      this.debugEnabled = EmbeddedLDAP.getEmbeddedLDAP().isDebugEnabled();
      this.useSSL = EmbeddedLDAP.getEmbeddedLDAPUseSSL();
      this.useMasterFirst = var1;
      this.writeFailover = var2;
      this.ignoreCertPathValidators = var3;
      this.inMasterServer = ManagementService.getRuntimeAccess(kernelId).isAdminServer();
      if (this.debugEnabled) {
         log.debug("Initialize local ldap connection ");
      }

      if (EmbeddedLDAP.getEmbeddedLDAP().isMasterFirst()) {
         this.useMasterFirst = true;
      }

      if (!this.inMasterServer) {
         this.masterHost = EmbeddedLDAP.getEmbeddedLDAPHost();
         this.masterPort = EmbeddedLDAP.getEmbeddedLDAPPort();
      }

      if (this.MSI_LOCAL_LDAP_ONLY) {
         this.msiMode = !ManagementService.getRuntimeAccess(kernelId).isAdminServerAvailable();
         if (this.useMasterFirst && !this.inMasterServer && !this.msiMode) {
            this.delegate = this.getDelegate();
         }
      } else if (this.useMasterFirst && !this.inMasterServer) {
         this.delegate = this.getDelegate();
      }

      this.creds = new Credentials();
      this.creds.setUser(EMPTY_DIRSTRING);
   }

   public void finalize() throws LDAPException {
   }

   public void setCache(LDAPCache var1) {
      if (this.delegate != null) {
         this.delegate.setCache(var1);
      } else {
         this.cache = var1;
      }
   }

   public LDAPCache getCache() {
      return this.delegate != null ? this.delegate.getCache() : this.cache;
   }

   public Object getProperty(String var1) throws LDAPException {
      throwUnsupported();
      return null;
   }

   public void setProperty(String var1, Object var2) throws LDAPException {
      if (this.delegate != null) {
         try {
            this.delegate.setProperty(var1, var2);
            return;
         } catch (LDAPException var4) {
            this.determineFailover(var4);
         }
      }

      if (this.debugEnabled) {
         log.debug("Setting property " + var1 + " to " + var2);
      }

   }

   public String getHost() {
      return this.savedHost;
   }

   public int getPort() {
      return this.savedPort;
   }

   public String getAuthenticationDN() {
      return null;
   }

   public String getAuthenticationPassword() {
      return null;
   }

   public int getConnectTimeout() {
      return 0;
   }

   public void setConnectTimeout(int var1) {
   }

   public int getConnSetupDelay() {
      return 0;
   }

   public void setConnSetupDelay(int var1) {
   }

   public LDAPSocketFactory getSocketFactory() {
      return null;
   }

   public void setSocketFactory(LDAPSocketFactory var1) {
   }

   public boolean isConnected() {
      return this.delegate != null ? this.delegate.isConnected() : true;
   }

   public boolean isAuthenticated() {
      return true;
   }

   public void connect(String var1, int var2) throws LDAPException {
      this.savedHost = var1;
      this.savedPort = var2;
      if (this.delegate != null) {
         try {
            this.connect(this.delegate, var1, var2);
            return;
         } catch (LDAPException var4) {
            this.determineFailover(var4);
         }
      }

   }

   public void connect(String var1, int var2, String var3, String var4) throws LDAPException {
      throwUnsupported();
   }

   public void connect(String var1, int var2, String var3, String var4, LDAPConstraints var5) throws LDAPException {
      throwUnsupported();
   }

   public void connect(String var1, int var2, String var3, String var4, LDAPSearchConstraints var5) throws LDAPException {
      throwUnsupported();
   }

   public void connect(int var1, String var2, int var3, String var4, String var5) throws LDAPException {
      throwUnsupported();
   }

   public void connect(int var1, String var2, int var3, String var4, String var5, LDAPConstraints var6) throws LDAPException {
      throwUnsupported();
   }

   public void connect(int var1, String var2, int var3, String var4, String var5, LDAPSearchConstraints var6) throws LDAPException {
      throwUnsupported();
   }

   public void abandon(LDAPSearchResults var1) throws LDAPException {
      if (this.delegate != null) {
         try {
            this.delegate.abandon(var1);
            return;
         } catch (LDAPException var3) {
            this.determineFailover(var3);
         }
      }

      if (var1 != null) {
         ;
      }
   }

   public void authenticate(String var1, String var2) throws LDAPException {
      this.bind(this.protocolVersion, var1, var2);
   }

   public void authenticate(String var1, String var2, LDAPConstraints var3) throws LDAPException {
      throwUnsupported();
   }

   public void authenticate(String var1, String var2, LDAPSearchConstraints var3) throws LDAPException {
      throwUnsupported();
   }

   public void authenticate(int var1, String var2, String var3) throws LDAPException {
      throwUnsupported();
   }

   public void authenticate(int var1, String var2, String var3, LDAPConstraints var4) throws LDAPException {
      throwUnsupported();
   }

   public void authenticate(int var1, String var2, String var3, LDAPSearchConstraints var4) throws LDAPException {
      throwUnsupported();
   }

   public void authenticate(String var1, Hashtable var2, Object var3) throws LDAPException {
      throwUnsupported();
   }

   public void authenticate(String var1, String[] var2, Hashtable var3, Object var4) throws LDAPException {
      throwUnsupported();
   }

   public void authenticate(String var1, String var2, String var3, Hashtable var4, Object var5) throws LDAPException {
      throwUnsupported();
   }

   public void authenticate(String var1, String[] var2, String var3, Hashtable var4, Object var5) throws LDAPException {
      throwUnsupported();
   }

   public LDAPResponseListener authenticate(int var1, String var2, String var3, LDAPResponseListener var4, LDAPConstraints var5) throws LDAPException {
      throwUnsupported();
      return null;
   }

   public LDAPResponseListener authenticate(int var1, String var2, String var3, LDAPResponseListener var4) throws LDAPException {
      throwUnsupported();
      return null;
   }

   public void bind(String var1, String var2) throws LDAPException {
      throwUnsupported();
   }

   public void bind(String var1, String var2, LDAPConstraints var3) throws LDAPException {
      throwUnsupported();
   }

   public void bind(int var1, String var2, String var3) throws LDAPException {
      this.savedDN = var2;
      this.savedPasswd = var3;
      if (this.delegate != null) {
         try {
            this.delegate.bind(var1, var2, var3);
         } catch (LDAPException var8) {
            this.determineFailover(var8);
         }
      }

      if (this.debugEnabled) {
         log.debug("bind (version = " + var1 + ", dn = " + var2 + ")");
      }

      DirectoryString var4 = null;
      if (var2 != null) {
         var4 = new DirectoryString(var2);
      } else {
         var4 = EMPTY_DIRSTRING;
      }

      if (var4.equals(EMPTY_DIRSTRING)) {
         if (!EmbeddedLDAP.getEmbeddedLDAP().getEmbeddedLDAPMBean().isAnonymousBindAllowed()) {
            if (this.debugEnabled) {
               log.debug("Anonymous user, but anonymous bind is not allowed");
            }

            throw new LDAPException("error result", LDAPResult.INVALID_CREDENTIALS.intValue());
         }
      } else if (var3 != null && !var3.equals("")) {
         if ((new DirectoryString((String)ServerConfig.getInstance().get("vde.rootuser"))).equals(var4)) {
            if (PasswordEncryptor.compare(var3, (String)ServerConfig.getInstance().get("vde.rootpw"))) {
               this.creds.setUser(var4);
               this.creds.setRoot(true);
               if (this.debugEnabled) {
                  log.debug("binding as root user: " + var4);
               }

            } else {
               if (this.debugEnabled) {
                  log.debug("Invalid password for root user: " + var4);
               }

               throw new LDAPException("error result", LDAPResult.INVALID_CREDENTIALS.intValue());
            }
         } else if (BackendHandler.getInstance().doBind(var4)) {
            boolean var9 = BackendHandler.getInstance().bind(var4, new BinarySyntax(var3.getBytes()));
            if (var9) {
               this.creds = new Credentials();
               this.creds.setUser(var4);
               if (this.debugEnabled) {
                  log.debug("binding as user: " + var4);
               }

            } else {
               if (this.debugEnabled) {
                  log.debug("Invalid password for user " + var4);
               }

               throw new LDAPException("error result", LDAPResult.INVALID_CREDENTIALS.intValue());
            }
         } else {
            Entry var5 = null;

            try {
               var5 = BackendHandler.getInstance().getByDN((DirectoryString)null, var4);
               var5 = BackendHandler.getInstance().map(var5);
            } catch (DirectoryException var7) {
            }

            if (var5 != null && var5.containsKey(USERPASSWORD)) {
               String var6 = new String(((Syntax)var5.get(USERPASSWORD).elementAt(0)).getValue());
               if (PasswordEncryptor.compare(var3, var6)) {
                  if (this.debugEnabled) {
                     log.debug("Binding as user: " + var4);
                  }

                  this.creds.setUser(var4);
               } else {
                  if (this.debugEnabled) {
                     log.debug("Invalid password for user: " + var4);
                  }

                  throw new LDAPException("error result", LDAPResult.INVALID_CREDENTIALS.intValue());
               }
            } else {
               if (this.debugEnabled) {
                  log.debug("Invalid password for user: " + var4);
               }

               throw new LDAPException("error result", LDAPResult.INVALID_CREDENTIALS.intValue());
            }
         }
      } else {
         this.creds.setUser(EMPTY_DIRSTRING);
         if (EmbeddedLDAP.getEmbeddedLDAP().getEmbeddedLDAPMBean().isAnonymousBindAllowed()) {
            if (this.debugEnabled) {
               log.debug("binding as anonymous user");
            }

         } else {
            if (this.debugEnabled) {
               log.debug("binding as anonymous user is not allowed");
            }

            throw new LDAPException("error result", LDAPResult.INVALID_CREDENTIALS.intValue());
         }
      }
   }

   public void bind(int var1, String var2, String var3, LDAPConstraints var4) throws LDAPException {
      throwUnsupported();
   }

   public void bind(String var1, Hashtable var2, Object var3) throws LDAPException {
      throwUnsupported();
   }

   public void bind(String var1, String[] var2, Hashtable var3, Object var4) throws LDAPException {
      throwUnsupported();
   }

   public String getAuthenticationMethod() {
      return null;
   }

   public void reconnect() throws LDAPException {
      throwUnsupported();
   }

   public synchronized void disconnect() throws LDAPException {
      if (this.delegate != null) {
         try {
            this.delegate.disconnect();
            return;
         } catch (LDAPException var2) {
            this.determineFailover(var2);
         }
      }

   }

   public LDAPEntry read(String var1) throws LDAPException {
      if (this.useMasterFirst && this.delegate != null) {
         try {
            return this.delegate.read(var1);
         } catch (LDAPException var3) {
            this.determineFailover(var3);
         }
      }

      if (this.debugEnabled) {
         log.debug("read (" + var1 + ")");
      }

      return this.read(var1, (String[])null, this.defaultConstraints);
   }

   public LDAPEntry read(String var1, LDAPSearchConstraints var2) throws LDAPException {
      throwUnsupported();
      return null;
   }

   public LDAPEntry read(String var1, String[] var2) throws LDAPException {
      if (this.useMasterFirst && this.delegate != null) {
         try {
            return this.delegate.read(var1, var2);
         } catch (LDAPException var4) {
            this.determineFailover(var4);
         }
      }

      if (this.debugEnabled) {
         log.debug("read (" + var1 + "," + var2 + ")");
      }

      return this.read(var1, var2, this.defaultConstraints);
   }

   public LDAPEntry read(String var1, String[] var2, LDAPSearchConstraints var3) throws LDAPException {
      if (this.useMasterFirst && this.delegate != null) {
         try {
            return this.delegate.read(var1, var2, var3);
         } catch (LDAPException var5) {
            this.determineFailover(var5);
         }
      }

      if (this.debugEnabled) {
         log.debug("read (" + var1 + "," + var2 + "," + var3 + ")");
      }

      LDAPSearchResults var4 = this.search(var1, 0, "(|(objectclass=*)(objectclass=ldapsubentry))", var2, false, (LDAPSearchConstraints)var3);
      return var4 == null ? null : var4.next();
   }

   public static LDAPEntry read(LDAPUrl var0) throws LDAPException {
      throwUnsupported();
      return null;
   }

   public static LDAPSearchResults search(LDAPUrl var0) throws LDAPException {
      throwUnsupported();
      return null;
   }

   public static LDAPSearchResults search(LDAPUrl var0, LDAPSearchConstraints var1) throws LDAPException {
      throwUnsupported();
      return null;
   }

   public LDAPSearchResults search(String var1, int var2, String var3, String[] var4, boolean var5) throws LDAPException {
      this.defaultConstraints.setMaxResults(0);
      return this.search(var1, var2, var3, var4, var5, this.defaultConstraints);
   }

   public LDAPSearchResults search(String var1, int var2, String var3, String[] var4, boolean var5, LDAPSearchConstraints var6) throws LDAPException {
      if (this.useMasterFirst && this.delegate != null) {
         try {
            return this.delegate.search(var1, var2, var3, var4, var5, var6);
         } catch (LDAPException var24) {
            this.determineFailover(var24);
         }
      }

      int var7 = 0;
      boolean var8 = false;
      DirectoryString var9 = null;
      Vector var10 = null;
      EntrySet var11 = null;
      if (this.debugEnabled) {
         log.debug("search (base=" + var1 + ", scope=" + var2 + ", searchFilter=" + ", attrs=" + var4 + ", attrsOnly=" + var5 + ", cons=" + var6);
      }

      if (var6 == null) {
         var6 = this.defaultConstraints;
      }

      EmbeddedLDAPSearchResults var12 = new EmbeddedLDAPSearchResults();
      Filter var13 = null;

      try {
         var9 = DNUtility.getInstance().normalize(new DirectoryString(var1));
         if (this.debugEnabled) {
            log.debug("search normalized base = " + var9);
         }

         var13 = ParseFilter.parse(var3);
      } catch (InvalidDNException var22) {
         throw new LDAPException("error result", var22.getLDAPErrorCode(), var22.getMessage());
      } catch (DirectoryException var23) {
         throw new LDAPException("error result", var23.getLDAPErrorCode(), var23.getMessage());
      }

      boolean var14 = var5;
      Vector var15 = new Vector();

      DirectoryString var17;
      for(int var16 = 0; var4 != null && var16 < var4.length; ++var16) {
         if (var4[var16] != null) {
            if (this.debugEnabled) {
               log.debug("search attr " + var16 + " = " + var4[var16]);
            }

            var17 = new DirectoryString(var4[var16]);
            var15.addElement(var17);
         }
      }

      int var25 = var6.getMaxResults();
      if (this.debugEnabled) {
         log.debug("search search limit = " + var25);
      }

      try {
         if (this.creds == null) {
            this.creds = new Credentials();
         }

         var10 = BackendHandler.getInstance().get(this.creds.getUser(), var9, var2, var13, var14, var15);
      } catch (DirectoryException var21) {
         throw new LDAPException("error result", var21.getLDAPErrorCode(), var21.getMessage());
      }

      Enumeration var26 = var10.elements();
      if (var26.hasMoreElements()) {
         var11 = (EntrySet)var26.nextElement();
      }

      while(var11 != null && (var11.hasMore() || var26.hasMoreElements())) {
         if (var25 != 0 && var7 >= var25) {
            if (this.debugEnabled) {
               log.debug("search exceeded limit of " + var25 + ", num entries = " + var7);
            }

            return var12;
         }

         if (!var11.hasMore()) {
            var11 = (EntrySet)var26.nextElement();
         }

         var17 = null;

         Entry var27;
         try {
            var27 = var11.getNext();
         } catch (DirectoryException var20) {
            throw new LDAPException("error result", var20.getLDAPErrorCode());
         }

         if (var27 != null) {
            var27 = BackendHandler.getInstance().postSearch(this.creds, var27, var15, var13, var2, var9);
            if (var27 != null) {
               ++var7;
               LDAPAttributeSet var18 = this.attributeSetFromEntry(var27, var14, var15);
               LDAPEntry var19 = new LDAPEntry(var27.getName().getDirectoryString(), var18);
               if (this.debugEnabled) {
                  log.debug("search adding entry " + var27.getName());
               }

               var12.add(new EmbeddedLDAPSearchResult(var19));
            }
         }
      }

      if (this.debugEnabled) {
         log.debug("search returning " + var12);
      }

      return var12;
   }

   public boolean compare(String var1, LDAPAttribute var2) throws LDAPException {
      throwUnsupported();
      return false;
   }

   public boolean compare(String var1, LDAPAttribute var2, LDAPConstraints var3) throws LDAPException {
      throwUnsupported();
      return false;
   }

   public boolean compare(String var1, LDAPAttribute var2, LDAPSearchConstraints var3) throws LDAPException {
      throwUnsupported();
      return false;
   }

   public void add(LDAPEntry var1) throws LDAPException {
      LDAPException var2 = null;
      if (this.MSI_LOCAL_LDAP_ONLY) {
         if (this.msiMode) {
            this.msiMode = !ManagementService.getRuntimeAccess(kernelId).isAdminServerAvailable();
         }

         if (this.msiMode && !this.writeFailover) {
            throw new LDAPException("Admin server unavailable and write failover is not enabled.");
         }

         if (!this.inMasterServer && this.delegate == null && !this.msiMode) {
            try {
               this.delegate = this.getAndInitDelegate();
            } catch (LDAPException var15) {
               this.determineWriteFailover(var15);
            }
         }
      } else if (!this.inMasterServer && this.delegate == null) {
         try {
            this.delegate = this.getAndInitDelegate();
         } catch (LDAPException var14) {
            this.determineWriteFailover(var14);
         }
      }

      Credentials var3 = this.creds;
      boolean var4 = false;
      if (this.delegate != null) {
         try {
            this.delegate.add(var1);
            var4 = true;
         } catch (LDAPException var19) {
            if (var19.getLDAPResultCode() == 68) {
               var2 = var19;
            } else {
               this.determineWriteFailover(var19);
            }
         }

         var3 = new Credentials();
         var3.setUser(new DirectoryString(this.savedDN));
         if (((String)ServerConfig.getInstance().get("vde.rootuser")).equalsIgnoreCase(this.savedDN)) {
            var3.setRoot(true);
         }
      }

      Int8 var5 = LDAPResult.SUCCESS;

      try {
         if (this.debugEnabled) {
            log.debug("add entry " + var1);
         }

         Entry var6 = this.LDAPEntryToEntry(var1);
         var5 = BackendHandler.getInstance().add(var3, var6);
      } catch (DirectorySchemaViolation var16) {
         var5 = LDAPResult.OBJECT_CLASS_VIOLATION;
         if (var16.getMessage() != null) {
            throw new LDAPException("error result", var5.intValue(), var16.getMessage());
         }

         throw new LDAPException("error result", var5.intValue());
      } catch (InvalidDNException var17) {
         var5 = LDAPResult.INVALID_DN_SYNTAX;
         if (var17.getMessage() != null) {
            throw new LDAPException("error result", var5.intValue(), var17.getMessage());
         }

         throw new LDAPException("error result", var5.intValue());
      } finally {
         var3 = null;
      }

      if (var5 != LDAPResult.SUCCESS) {
         if (!var4 || var5 != LDAPResult.ENTRY_ALREADY_EXISTS) {
            throw new LDAPException("error result", var5.intValue());
         }

         if (this.debugEnabled) {
            log.debug("delegate success and local already exists");
         }
      }

      if (var2 != null) {
         throw var2;
      } else {
         if (this.debugEnabled) {
            log.debug("added entry successfully");
         }

      }
   }

   public void add(LDAPEntry var1, LDAPConstraints var2) throws LDAPException {
      throwUnsupported();
   }

   public void add(LDAPEntry var1, LDAPSearchConstraints var2) throws LDAPException {
      throwUnsupported();
   }

   public LDAPExtendedOperation extendedOperation(LDAPExtendedOperation var1) throws LDAPException {
      throwUnsupported();
      return null;
   }

   public LDAPExtendedOperation extendedOperation(LDAPExtendedOperation var1, LDAPConstraints var2) throws LDAPException {
      throwUnsupported();
      return null;
   }

   public LDAPExtendedOperation extendedOperation(LDAPExtendedOperation var1, LDAPSearchConstraints var2) throws LDAPException {
      throwUnsupported();
      return null;
   }

   public void modify(String var1, LDAPModification var2) throws LDAPException {
      this.modify(var1, new LDAPModification[]{var2});
   }

   public void modify(String var1, LDAPModification var2, LDAPConstraints var3) throws LDAPException {
      throwUnsupported();
   }

   public void modify(String var1, LDAPModification var2, LDAPSearchConstraints var3) throws LDAPException {
      throwUnsupported();
   }

   public void modify(String var1, LDAPModificationSet var2) throws LDAPException {
      if (this.MSI_LOCAL_LDAP_ONLY) {
         if (this.msiMode) {
            this.msiMode = !ManagementService.getRuntimeAccess(kernelId).isAdminServerAvailable();
         }

         if (this.msiMode && !this.writeFailover) {
            throw new LDAPException("Admin server unavailable and write failover is not enabled.");
         }

         if (!this.inMasterServer && this.delegate == null && !this.msiMode) {
            try {
               this.delegate = this.getAndInitDelegate();
            } catch (LDAPException var7) {
               this.determineWriteFailover(var7);
            }
         }
      } else if (!this.inMasterServer && this.delegate == null) {
         try {
            this.delegate = this.getAndInitDelegate();
         } catch (LDAPException var6) {
            this.determineWriteFailover(var6);
         }
      }

      if (this.delegate != null) {
         try {
            this.delegate.modify(var1, var2);
         } catch (LDAPException var5) {
            this.determineWriteFailover(var5);
         }
      }

      LDAPModification[] var3 = new LDAPModification[var2.size()];

      for(int var4 = 0; var4 < var2.size(); ++var4) {
         var3[var4] = var2.elementAt(var4);
      }

      this.modify(var1, var3);
   }

   public void modify(String var1, LDAPModificationSet var2, LDAPConstraints var3) throws LDAPException {
      throwUnsupported();
   }

   public void modify(String var1, LDAPModificationSet var2, LDAPSearchConstraints var3) throws LDAPException {
      throwUnsupported();
   }

   public void modify(String var1, LDAPModification[] var2) throws LDAPException {
      if (this.MSI_LOCAL_LDAP_ONLY) {
         if (this.msiMode) {
            this.msiMode = !ManagementService.getRuntimeAccess(kernelId).isAdminServerAvailable();
         }

         if (this.msiMode && !this.writeFailover) {
            throw new LDAPException("Admin server unavailable and write failover is not enabled.");
         }

         if (!this.inMasterServer && this.delegate == null && !this.msiMode) {
            try {
               this.delegate = this.getAndInitDelegate();
            } catch (LDAPException var26) {
               this.determineWriteFailover(var26);
            }
         }
      } else if (!this.inMasterServer && this.delegate == null) {
         try {
            this.delegate = this.getAndInitDelegate();
         } catch (LDAPException var25) {
            this.determineWriteFailover(var25);
         }
      }

      if (this.delegate != null) {
         try {
            this.delegate.modify(var1, var2);
         } catch (LDAPException var24) {
            this.determineWriteFailover(var24);
         }
      }

      if (this.debugEnabled) {
         log.debug("modify entry " + var1 + " mods" + var2);
      }

      Vector var3 = new Vector();

      for(int var4 = 0; var4 < var2.length; ++var4) {
         int var5 = var2[var4].getOp();
         LDAPAttribute var6 = var2[var4].getAttribute();
         DirectoryString var7 = new DirectoryString(var6.getName());
         AttributeType var8 = SchemaChecker.getInstance().getAttributeType(var7);
         Class var9 = null;
         if (var8 != null) {
            var9 = var8.getSyntaxClass();
         } else {
            var9 = DirectoryString.class;
         }

         if (this.debugEnabled) {
            log.debug("modify attribute " + var6.getName() + " class " + var9);
         }

         Vector var10 = new Vector();
         Enumeration var11 = var2[var4].getAttribute().getByteValues();

         while(var11.hasMoreElements()) {
            byte[] var12 = (byte[])((byte[])var11.nextElement());
            if (var12.length > 0) {
               try {
                  Syntax var13 = (Syntax)var9.newInstance();
                  var13.setValue(var12);
                  var10.addElement(var13);
                  if (this.debugEnabled) {
                     log.debug("modify value " + var13);
                  }
               } catch (InstantiationException var22) {
                  throw new LDAPException("conversion error", LDAPResult.OTHER.intValue(), var22.getMessage());
               } catch (IllegalAccessException var23) {
                  throw new LDAPException("conversion error", LDAPResult.OTHER.intValue(), var23.getMessage());
               }
            }
         }

         EntryChange var31 = new EntryChange(var5, var7, var10);
         var3.addElement(var31);
      }

      DirectoryString var29 = new DirectoryString(var1);
      Credentials var30 = new Credentials();
      var30.setUser(new DirectoryString(this.savedDN));
      if (((String)ServerConfig.getInstance().get("vde.rootuser")).equalsIgnoreCase(this.savedDN)) {
         var30.setRoot(true);
      }

      try {
         BackendHandler.getInstance().modify(var30, var29, var3);
      } catch (DirectoryException var27) {
         if (var27.getMessage() != null) {
            throw new LDAPException("error result", var27.getLDAPErrorCode(), var27.getMessage());
         }

         throw new LDAPException("error result", var27.getLDAPErrorCode());
      } finally {
         var30 = null;
      }

   }

   public void modify(String var1, LDAPModification[] var2, LDAPConstraints var3) throws LDAPException {
      throwUnsupported();
   }

   public void modify(String var1, LDAPModification[] var2, LDAPSearchConstraints var3) throws LDAPException {
      throwUnsupported();
   }

   public void delete(String var1) throws LDAPException {
      LDAPException var2 = null;
      if (this.MSI_LOCAL_LDAP_ONLY) {
         if (this.msiMode) {
            this.msiMode = !ManagementService.getRuntimeAccess(kernelId).isAdminServerAvailable();
         }

         if (this.msiMode && !this.writeFailover) {
            throw new LDAPException("Admin server unavailable and write failover is not enabled.");
         }

         if (!this.inMasterServer && this.delegate == null && !this.msiMode) {
            try {
               this.delegate = this.getAndInitDelegate();
            } catch (LDAPException var8) {
               this.determineWriteFailover(var8);
            }
         }
      } else if (!this.inMasterServer && this.delegate == null) {
         try {
            this.delegate = this.getAndInitDelegate();
         } catch (LDAPException var7) {
            this.determineWriteFailover(var7);
         }
      }

      boolean var3 = false;
      if (this.delegate != null) {
         try {
            this.delegate.delete(var1);
            var3 = true;
         } catch (LDAPException var9) {
            if (var9.getLDAPResultCode() == 32) {
               var2 = var9;
            } else {
               this.determineWriteFailover(var9);
            }
         }
      }

      if (this.debugEnabled) {
         log.debug("delete entry " + var1);
      }

      DirectoryString var4 = new DirectoryString(var1);
      Credentials var5 = new Credentials();
      var5.setUser(new DirectoryString(this.savedDN));
      if (((String)ServerConfig.getInstance().get("vde.rootuser")).equalsIgnoreCase(this.savedDN)) {
         var5.setRoot(true);
      }

      Int8 var6 = BackendHandler.getInstance().delete(var5, var4);
      var5 = null;
      if (var6 != LDAPResult.SUCCESS) {
         if (!var3 || var6 != LDAPResult.NO_SUCH_OBJECT) {
            throw new LDAPException("error result", var6.intValue());
         }

         if (this.debugEnabled) {
            log.debug("delegate success and local no such object");
         }
      }

      if (var2 != null) {
         throw var2;
      }
   }

   public void delete(String var1, LDAPConstraints var2) throws LDAPException {
      throwUnsupported();
   }

   public void delete(String var1, LDAPSearchConstraints var2) throws LDAPException {
      throwUnsupported();
   }

   public void rename(String var1, String var2, boolean var3) throws LDAPException {
      throwUnsupported();
   }

   public void rename(String var1, String var2, boolean var3, LDAPConstraints var4) throws LDAPException {
      throwUnsupported();
   }

   public void rename(String var1, String var2, boolean var3, LDAPSearchConstraints var4) throws LDAPException {
      throwUnsupported();
   }

   public void rename(String var1, String var2, String var3, boolean var4) throws LDAPException {
      throwUnsupported();
   }

   public void rename(String var1, String var2, String var3, boolean var4, LDAPConstraints var5) throws LDAPException {
      throwUnsupported();
   }

   public void rename(String var1, String var2, String var3, boolean var4, LDAPSearchConstraints var5) throws LDAPException {
      throwUnsupported();
   }

   public LDAPResponseListener add(LDAPEntry var1, LDAPResponseListener var2) throws LDAPException {
      throwUnsupported();
      return null;
   }

   public LDAPResponseListener add(LDAPEntry var1, LDAPResponseListener var2, LDAPConstraints var3) throws LDAPException {
      throwUnsupported();
      return null;
   }

   public LDAPResponseListener bind(int var1, String var2, String var3, LDAPResponseListener var4) throws LDAPException {
      throwUnsupported();
      return null;
   }

   public LDAPResponseListener bind(String var1, String var2, LDAPResponseListener var3) throws LDAPException {
      throwUnsupported();
      return null;
   }

   public LDAPResponseListener bind(String var1, String var2, LDAPResponseListener var3, LDAPConstraints var4) throws LDAPException {
      throwUnsupported();
      return null;
   }

   public LDAPResponseListener bind(int var1, String var2, String var3, LDAPResponseListener var4, LDAPConstraints var5) throws LDAPException {
      throwUnsupported();
      return null;
   }

   public LDAPResponseListener delete(String var1, LDAPResponseListener var2) throws LDAPException {
      throwUnsupported();
      return null;
   }

   public LDAPResponseListener delete(String var1, LDAPResponseListener var2, LDAPConstraints var3) throws LDAPException {
      throwUnsupported();
      return null;
   }

   public LDAPResponseListener modify(String var1, LDAPModification var2, LDAPResponseListener var3) throws LDAPException {
      throwUnsupported();
      return null;
   }

   public LDAPResponseListener modify(String var1, LDAPModification var2, LDAPResponseListener var3, LDAPConstraints var4) throws LDAPException {
      throwUnsupported();
      return null;
   }

   public LDAPResponseListener modify(String var1, LDAPModificationSet var2, LDAPResponseListener var3) throws LDAPException {
      throwUnsupported();
      return null;
   }

   public LDAPResponseListener modify(String var1, LDAPModificationSet var2, LDAPResponseListener var3, LDAPConstraints var4) throws LDAPException {
      throwUnsupported();
      return null;
   }

   public LDAPResponseListener rename(String var1, String var2, boolean var3, LDAPResponseListener var4) throws LDAPException {
      throwUnsupported();
      return null;
   }

   public LDAPResponseListener rename(String var1, String var2, boolean var3, LDAPResponseListener var4, LDAPConstraints var5) throws LDAPException {
      throwUnsupported();
      return null;
   }

   public LDAPSearchListener search(String var1, int var2, String var3, String[] var4, boolean var5, LDAPSearchListener var6) throws LDAPException {
      throwUnsupported();
      return null;
   }

   public LDAPSearchListener search(String var1, int var2, String var3, String[] var4, boolean var5, LDAPSearchListener var6, LDAPSearchConstraints var7) throws LDAPException {
      throwUnsupported();
      return null;
   }

   public LDAPResponseListener compare(String var1, LDAPAttribute var2, LDAPResponseListener var3) throws LDAPException {
      throwUnsupported();
      return null;
   }

   public LDAPResponseListener compare(String var1, LDAPAttribute var2, LDAPResponseListener var3, LDAPConstraints var4) throws LDAPException {
      throwUnsupported();
      return null;
   }

   public void abandon(int var1) throws LDAPException {
      throwUnsupported();
   }

   public void abandon(LDAPSearchListener var1) throws LDAPException {
      throwUnsupported();
   }

   public Object getOption(int var1) throws LDAPException {
      throwUnsupported();
      return null;
   }

   public void setOption(int var1, Object var2) throws LDAPException {
   }

   public LDAPControl[] getResponseControls() {
      return null;
   }

   public LDAPConstraints getConstraints() {
      return null;
   }

   public LDAPSearchConstraints getSearchConstraints() {
      return null;
   }

   public void setConstraints(LDAPConstraints var1) {
   }

   public void setSearchConstraints(LDAPSearchConstraints var1) {
   }

   public InputStream getInputStream() {
      return null;
   }

   public void setInputStream(InputStream var1) {
   }

   public OutputStream getOutputStream() {
      return null;
   }

   public void setOutputStream(OutputStream var1) {
   }

   public synchronized Object clone() {
      return null;
   }

   public static boolean isNetscape() {
      return false;
   }

   private LDAPAttributeSet attributeSetFromEntry(Entry var1, boolean var2, Vector var3) {
      LDAPAttributeSet var4 = new LDAPAttributeSet();
      Attribute var5 = null;
      Object var6 = null;
      Syntax var7 = null;
      Vector var8 = var1.getAttributes();
      int var9 = var8.size();

      for(int var10 = 0; var10 < var9; ++var10) {
         var5 = (Attribute)var8.elementAt(var10);
         LDAPAttribute var11 = null;
         if (!var2) {
            var11 = new LDAPAttribute(var5.type.getDirectoryString());
            int var12 = var5.values.size();

            for(int var13 = 0; var13 < var12; ++var13) {
               var7 = (Syntax)var5.values.elementAt(var13);
               if (var7 != null) {
                  var11.addValue(var7.getValue());
               }
            }
         } else {
            var11 = new LDAPAttribute(var5.type.getDirectoryString());
         }

         var4.add(var11);
      }

      return var4;
   }

   private Entry LDAPEntryToEntry(LDAPEntry var1) throws InvalidDNException {
      Entry var2 = new Entry(new DirectoryString(var1.getDN()));
      LDAPAttributeSet var3 = var1.getAttributeSet();
      Enumeration var4 = var3.getAttributes();

      while(true) {
         LDAPAttribute var5;
         do {
            if (!var4.hasMoreElements()) {
               return var2;
            }

            var5 = (LDAPAttribute)var4.nextElement();
         } while(var5.getName() == null);

         DirectoryString var6 = new DirectoryString(var5.getName());
         AttributeType var7 = SchemaChecker.getInstance().getAttributeType(var6);
         Class var8 = null;
         if (var7 != null) {
            var8 = var7.getSyntaxClass();
         } else {
            var8 = DirectoryString.class;
         }

         Vector var9 = new Vector();
         Enumeration var10 = var5.getByteValues();

         while(var10.hasMoreElements()) {
            byte[] var11 = (byte[])((byte[])var10.nextElement());
            if (var11.length > 0) {
               try {
                  Syntax var12 = (Syntax)var8.newInstance();
                  var12.setValue(var11);
                  var9.addElement(var12);
               } catch (InstantiationException var13) {
                  EmbeddedLDAPLogger.logStackTrace(var13);
               } catch (IllegalAccessException var14) {
                  EmbeddedLDAPLogger.logStackTrace(var14);
               }
            }
         }

         if (var9.size() > 0) {
            var2.put(var6, var9, false);
         }
      }
   }

   private LDAPConnection getDelegate() {
      LDAPConnection var1 = null;
      if (this.debugEnabled) {
         log.debug("Creating LDAP Connection delegate, useSSL is " + this.useSSL);
      }

      if (this.useSSL) {
         LDAPSocketFactory var2 = this.ignoreCertPathValidators ? embeddedLDAPSocketFactoryIgnoreCertPathValidators : embeddedLDAPSocketFactoryUseCertPathValidators;
         var1 = new LDAPConnection(var2);
      } else {
         var1 = new LDAPConnection();
      }

      Integer var7 = Integer.getInteger("weblogic.security.embeddedLDAPConnectTimeout");
      if (null != var7) {
         var1.setConnectTimeout(var7);
      }

      int var3 = EmbeddedLDAP.getEmbeddedLDAP().getTimeout();
      if (var3 > 0) {
         Integer var4 = new Integer(var3 * 1000);

         try {
            var1.setOption(4, var4);
         } catch (LDAPException var6) {
            if (this.debugEnabled) {
               log.debug("Error setting timeout " + var6);
            }
         }
      }

      return var1;
   }

   private LDAPConnection getAndInitDelegate() throws LDAPException {
      LDAPConnection var1 = this.getDelegate();
      if (this.debugEnabled) {
         log.debug("Initializing LDAP Connection delegate");
      }

      if (this.masterHost != null) {
         if (this.debugEnabled) {
            log.debug("Connecting write delegate to " + this.masterHost + ":" + this.masterPort);
         }

         this.connect(var1, this.masterHost, this.masterPort);
      }

      if (this.savedDN != null) {
         if (this.debugEnabled) {
            log.debug("Binding delegate to " + this.savedDN);
         }

         var1.bind(this.protocolVersion, this.savedDN, this.savedPasswd);
      }

      return var1;
   }

   private static void throwUnsupported() throws LDAPException {
      Thread.dumpStack();
      throw new LDAPException("EmbeddedLDAPConnection does not support this method", 92);
   }

   private void determineFailover(LDAPException var1) throws LDAPException {
      boolean var2 = true;
      switch (var1.getLDAPResultCode()) {
         default:
            var2 = false;
         case 3:
         case 11:
         case 51:
         case 52:
         case 80:
         case 81:
         case 85:
         case 91:
            if (var2) {
               if (this.debugEnabled) {
                  log.debug("Failing over to local replicated server");
               }

               try {
                  this.delegate.disconnect();
               } catch (Exception var4) {
               }

               this.delegate = null;
               this.bind(this.protocolVersion, this.savedDN, this.savedPasswd);
            } else {
               throw var1;
            }
      }
   }

   private void determineWriteFailover(LDAPException var1) throws LDAPException {
      if (!this.writeFailover) {
         throw var1;
      } else {
         this.determineFailover(var1);
         EmbeddedLDAP.getEmbeddedLDAP().setReplicaInvalid();
      }
   }

   private void connect(LDAPConnection var1, String var2, int var3) throws LDAPException {
      ClassLoader var4 = Thread.currentThread().getContextClassLoader();
      Thread.currentThread().setContextClassLoader(EmbeddedLDAPConnection.class.getClassLoader());

      try {
         var1.connect(var2, var3);
      } finally {
         Thread.currentThread().setContextClassLoader(var4);
      }

   }

   private static class EmbeddedLDAPSocketFactory implements LDAPSocketFactory {
      private boolean ignoreCertPathValidators;
      private SSLSocketFactory socketFactory;

      private EmbeddedLDAPSocketFactory(boolean var1) {
         this.ignoreCertPathValidators = false;
         this.socketFactory = null;
         this.ignoreCertPathValidators = var1;
      }

      public Socket makeSocket(String var1, int var2) throws LDAPException {
         try {
            SSLSocket var3 = (SSLSocket)this.getFactory().createSocket(var1, var2);
            var3.startHandshake();
            return var3;
         } catch (Exception var5) {
            LDAPException var4 = new LDAPException(var5.getMessage(), 91);
            var4.initCause(var5);
            throw var4;
         }
      }

      private synchronized SSLSocketFactory getFactory() throws Exception {
         if (this.socketFactory == null) {
            if (this.ignoreCertPathValidators) {
               CertPathTrustManager var1 = new CertPathTrustManager();
               var1.setBuiltinSSLValidationOnly();
               this.socketFactory = SSLContextManager.getSSLSocketFactory((AuthenticatedSubject)EmbeddedLDAPConnection.kernelId, (TrustManager)var1);
            } else {
               this.socketFactory = SSLContextManager.getDefaultSSLSocketFactory(EmbeddedLDAPConnection.kernelId);
            }
         }

         return this.socketFactory;
      }

      // $FF: synthetic method
      EmbeddedLDAPSocketFactory(boolean var1, Object var2) {
         this(var1);
      }
   }
}
