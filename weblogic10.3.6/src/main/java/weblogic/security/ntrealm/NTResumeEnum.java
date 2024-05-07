package weblogic.security.ntrealm;

import java.security.AccessController;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Vector;
import weblogic.kernel.Kernel;
import weblogic.management.configuration.NTRealmMBean;
import weblogic.management.configuration.SecurityMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

class NTResumeEnum implements Enumeration {
   private String[] list;
   private int preferredMaxBytes;
   private int resumeHandle;
   private String altDomain;
   private boolean useAltDomain;
   private boolean enumUsers;
   private boolean enumGlobalGroups;
   private int index;
   private boolean depleted;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   static native void initFields();

   private NTResumeEnum() {
      SecurityMBean var1 = null;
      if (Kernel.isServer()) {
         var1 = ManagementService.getRuntimeAccess(kernelId).getDomain().getSecurity();
      }

      if (var1 != null) {
         NTRealmMBean var2 = (NTRealmMBean)var1.getRealm().getCachingRealm().getBasicRealm();
         this.preferredMaxBytes = var2.getPreferredMaxBytes();
      } else {
         this.preferredMaxBytes = 10240;
      }

      this.resumeHandle = 0;
      this.depleted = false;
   }

   NTResumeEnum(String[] var1, boolean var2) {
      this();
      this.enumUsers = var2;
      this.enumGlobalGroups = true;
      this.useAltDomain = false;
      this.populate();
      if (var1 != null && var1.length > 0) {
         this.enumGlobalGroups = true;
         this.useAltDomain = true;
         Vector var3 = new Vector();

         int var4;
         for(var4 = 0; var4 < this.list.length; ++var4) {
            if (!this.list[var4].endsWith("$") && !var3.contains(this.list[var4]) && !this.list[var4].equals("None")) {
               var3.addElement(this.list[var4]);
            }
         }

         for(var4 = 0; var4 < var1.length; ++var4) {
            this.altDomain = var1[var4];
            this.depleted = false;
            this.list = null;
            this.populate();

            for(int var5 = 0; var5 < this.list.length; ++var5) {
               if (!this.list[var5].endsWith("$") && !var3.contains(this.list[var5]) && !this.list[var5].equals("None")) {
                  var3.addElement(this.list[var5]);
               }
            }
         }

         this.list = new String[var3.size()];
         var4 = 0;

         while(!var3.isEmpty()) {
            this.list[var4++] = (String)var3.firstElement();
            var3.removeElementAt(0);
         }
      }

   }

   public boolean hasMoreElements() {
      if (this.list == null) {
         return false;
      } else if (this.list != null && this.index < this.list.length) {
         return true;
      } else {
         this.populate();
         return !this.depleted;
      }
   }

   public Object nextElement() {
      if (!this.hasMoreElements()) {
         throw new NoSuchElementException();
      } else {
         String var1 = this.list[this.index++];
         if (this.depleted && this.index == this.list.length) {
            this.list = null;
         }

         return var1;
      }
   }

   private void populate() {
      if (!this.depleted) {
         if ((this.list = this.populate0()) != null) {
            if (!this.enumUsers) {
               this.enumGlobalGroups = false;
               String[] var2;
               if ((var2 = this.populate0()) == null) {
                  return;
               }

               String[] var1 = this.list;
               this.list = new String[var1.length + var2.length];

               int var3;
               for(var3 = 0; var3 < var1.length; ++var3) {
                  this.list[var3] = var1[var3];
               }

               for(var3 = 0; var3 < var2.length; ++var3) {
                  this.list[var3 + var1.length] = var2[var3];
               }
            }

            this.index = 0;
         }
      }
   }

   private native String[] populate0();
}
