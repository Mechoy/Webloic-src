package weblogic.security.ntrealm;

import java.security.AccessController;
import java.util.Enumeration;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Vector;
import weblogic.kernel.Kernel;
import weblogic.management.configuration.NTRealmMBean;
import weblogic.management.configuration.SecurityMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.SecurityLogger;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.AssertionError;

public final class NTDelegate {
   private static final boolean verbose = Boolean.getBoolean("weblogic.security.ntrealm.verbose");
   private static final String NT_PROPS = "ntrealm.properties";
   private static final String NT_PREFIX = "weblogic.security.ntrealm.";
   private static final String NT_DOMAIN = ".";
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private String domain;
   private String[] altDomains;
   private boolean useAltDomain;
   private static boolean ignoreBadDomainName;
   private NTRealm owner;
   private static boolean libLoaded = false;

   public NTDelegate(NTRealm var1) {
      this.owner = var1;
      String var2 = null;

      try {
         if (Kernel.isServer()) {
            SecurityMBean var3 = ManagementService.getRuntimeAccess(kernelId).getDomain().getSecurity();
            if (var3 != null) {
               NTRealmMBean var4 = (NTRealmMBean)var3.getRealm().getCachingRealm().getBasicRealm();
               var2 = var4.getPrimaryDomain();
            }
         }
      } catch (AssertionError var7) {
      }

      this.useAltDomain = var2 != null;
      if (this.useAltDomain) {
         Vector var8 = new Vector();
         StringTokenizer var10 = new StringTokenizer(var2, ",");

         while(var10.hasMoreTokens()) {
            var8.add(var10.nextToken().trim());
         }

         int var5 = var8.size();
         if (var5 > 0) {
            this.altDomains = new String[var5];

            for(int var6 = 0; var6 < var5; ++var6) {
               this.altDomains[var6] = var8.elementAt(var6).toString();
            }
         }

         if (this.altDomains == null) {
            throw new SecurityException("Incorrectly configured NTRealmMBean, null domain.");
         }

         this.cleanAltDomains();
      }

      if (!libLoaded) {
         this.domain = loadlib();
      }

      if (this.owner.log != null) {
         this.owner.log.debug("<NTRealm><I> Primary Domain Controller = " + this.domain);
         if (this.useAltDomain) {
            StringBuffer var9 = new StringBuffer("<NTRealm><I> Alternate Primary Domain Controllers: ");

            for(int var11 = 0; var11 < this.altDomains.length; ++var11) {
               var9.append(this.altDomains[var11] + " ");
            }

            this.owner.log.debug(var9.toString());
         }
      }

   }

   private void cleanAltDomains() {
      for(int var1 = 0; var1 < this.altDomains.length; ++var1) {
         if (!this.altDomains[var1].startsWith("\\\\")) {
            if (this.altDomains[var1].startsWith("\\")) {
               this.altDomains[var1] = "\\" + this.altDomains[var1];
            } else {
               this.altDomains[var1] = "\\\\" + this.altDomains[var1];
            }
         }
      }

   }

   void setAltDomain(String var1) {
      if (this.altDomains == null) {
         this.altDomains = new String[1];
      }

      this.altDomains[0] = var1;
      if (this.altDomains[0] != null) {
         this.useAltDomain = true;
      } else {
         this.useAltDomain = false;
      }

   }

   void addAltDomains(String var1) {
      StringTokenizer var2 = new StringTokenizer(var1, ",");

      while(var2.hasMoreTokens()) {
         this.addAltDomain(var2.nextToken().trim());
      }

      this.cleanAltDomains();
   }

   private void addAltDomain(String var1) {
      if (this.altDomains == null) {
         this.altDomains = new String[1];
         this.altDomains[0] = var1;
      } else {
         String[] var2 = new String[this.altDomains.length + 1];

         for(int var3 = 0; var3 < this.altDomains.length; ++var3) {
            var2[var3] = this.altDomains[var3];
         }

         var2[this.altDomains.length + 1] = var1;
         this.altDomains = var2;
      }

      if (this.altDomains[0] != null) {
         this.useAltDomain = true;
      } else {
         this.useAltDomain = false;
      }

   }

   String getAltDomain() {
      return this.altDomains != null && this.altDomains[0] != null ? this.altDomains[0] : null;
   }

   String[] getAltDomains() {
      return this.altDomains != null ? this.altDomains : null;
   }

   String getAltDomain(int var1) {
      return this.altDomains != null && this.altDomains[var1] != null ? this.altDomains[var1] : null;
   }

   String getDomain() {
      return this.domain;
   }

   boolean getUseAltDomain() {
      return this.useAltDomain;
   }

   public native boolean authenticate(String var1, String var2);

   private native boolean nativeGetUser(String var1, String var2);

   public boolean getUser(String var1) {
      if (this.useAltDomain) {
         if (this.altDomains == null) {
            return this.nativeGetUser(var1, (String)null);
         } else {
            for(int var2 = 0; var2 < this.altDomains.length; ++var2) {
               if (this.nativeGetUser(var1, this.altDomains[var2])) {
                  return true;
               }
            }

            return false;
         }
      } else {
         return this.nativeGetUser(var1, (String)null);
      }
   }

   public Enumeration getUserNames() {
      if (this.useAltDomain) {
         NTResumeEnum var1 = null;

         try {
            var1 = new NTResumeEnum(this.altDomains, true);
         } catch (RuntimeException var3) {
            if (!ignoreBadDomainName) {
               throw var3;
            }
         }

         return var1;
      } else {
         return new NTResumeEnum((String[])null, true);
      }
   }

   public Enumeration getGroupNames() {
      if (this.useAltDomain) {
         NTResumeEnum var1 = null;

         try {
            var1 = new NTResumeEnum(this.altDomains, false);
         } catch (RuntimeException var3) {
            if (!ignoreBadDomainName) {
               throw var3;
            }
         }

         return var1;
      } else {
         return new NTResumeEnum((String[])null, false);
      }
   }

   public Enumeration getGroupsForUser(String var1) {
      return this.useAltDomain ? new NTUserGroupEnum(var1, this.altDomains) : new NTUserGroupEnum(var1, (String[])null);
   }

   public boolean isUserInGroup(String var1, String var2) {
      if (var1.endsWith("$")) {
         return false;
      } else {
         try {
            Enumeration var3 = this.getGroupsForUser(var1);

            String var4;
            do {
               if (!var3.hasMoreElements()) {
                  return false;
               }

               var4 = (String)var3.nextElement();
            } while(!var2.equals(var4));

            return true;
         } catch (Exception var5) {
            return false;
         }
      }
   }

   private static native String initFields();

   private static synchronized String loadlib() {
      String var0 = "wlntrealm";
      String var1 = System.getProperty("java.vendor");
      String var2 = null;
      if (var1 != null && var1.toLowerCase(Locale.ENGLISH).indexOf("microsoft") != -1) {
         var0 = "wlntrealm_ms";
      }

      try {
         ignoreBadDomainName = Boolean.getBoolean("weblogic.security.ntrealm.ignoreBadDomainName");
      } catch (SecurityException var4) {
      }

      try {
         System.loadLibrary(var0);
         var2 = initFields();
         NTResumeEnum.initFields();
         NTUserGroupEnum.initFields();
      } catch (SecurityException var5) {
         SecurityLogger.logStackTrace(var5);
         if (!ignoreBadDomainName) {
            throw new SecurityException(var5.getMessage() + " -- bad domain name");
         }

         System.out.println("<NTRealm><E> Ignoring Bad Domain Name: " + var5.getMessage());
      }

      libLoaded = true;
      return var2;
   }
}
