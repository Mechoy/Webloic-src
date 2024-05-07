package weblogic.servlet.internal.dd;

import java.util.Iterator;
import java.util.List;
import org.w3c.dom.Element;
import weblogic.management.ManagementException;
import weblogic.management.descriptors.DescriptorValidationException;
import weblogic.management.descriptors.webapp.WelcomeFileListMBean;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public final class WelcomeFilesDescriptor extends BaseServletDescriptor implements ToXML, WelcomeFileListMBean {
   private static final long serialVersionUID = 8641270140745843773L;
   private static final String WELCOME_FILE = "welcome-file";
   private String[] fileNames;

   public WelcomeFilesDescriptor() {
      this.fileNames = new String[0];
   }

   public WelcomeFilesDescriptor(WelcomeFileListMBean var1) {
      if (var1 != null) {
         String[] var2 = var1.getWelcomeFiles();
         String[] var3 = new String[var2.length];
         System.arraycopy(var2, 0, var3, 0, var2.length);
         this.fileNames = var3;
      } else {
         this.fileNames = new String[0];
      }

   }

   public WelcomeFilesDescriptor(Element var1) throws DOMProcessingException {
      List var2 = DOMUtils.getValuesByTagName(var1, "welcome-file");
      if (var2 == null) {
         this.fileNames = new String[0];
      } else {
         String[] var3 = new String[var2.size()];
         Iterator var4 = var2.iterator();

         for(int var5 = 0; var4.hasNext(); ++var5) {
            var3[var5] = (String)var4.next();
         }

         this.fileNames = var3;
      }

   }

   public String[] getWelcomeFiles() {
      return this.fileNames;
   }

   public void setWelcomeFiles(String[] var1) {
      String[] var2 = this.fileNames;
      if (var1 == null) {
         this.fileNames = new String[0];
      } else {
         this.fileNames = var1;
      }

      if (!comp(var2, var1)) {
         this.firePropertyChange("welcomeFiles", var2, var1);
      }

   }

   public void addWelcomeFile(String var1) {
      String[] var2 = this.getWelcomeFiles();
      String[] var3 = new String[var2.length + 1];
      System.arraycopy(var2, 0, var3, 0, var2.length);
      var3[var2.length] = var1;
      this.setWelcomeFiles(var3);
   }

   public void removeWelcomeFile(String var1) {
      String[] var2 = this.getWelcomeFiles();
      if (var2 != null) {
         int var3 = -1;

         for(int var4 = 0; var4 < var2.length; ++var4) {
            if (var2[var4].equals(var1)) {
               var3 = var4;
               break;
            }
         }

         if (var3 >= 0) {
            String[] var5 = new String[var2.length - 1];
            System.arraycopy(var2, 0, var5, 0, var3);
            System.arraycopy(var2, var3 + 1, var5, var3, var2.length - (var3 + 1));
            this.setWelcomeFiles(var5);
         }

      }
   }

   public void validate() throws DescriptorValidationException {
      this.removeDescriptorErrors();
      String[] var1 = this.getWelcomeFiles();
      boolean var2 = true;

      for(int var3 = 0; var3 < var1.length; ++var3) {
         if (var1[var3] == null || var1[var3].length() == 0) {
            this.addDescriptorError("INVALID_WELCOME_FILE", "" + var3);
            var2 = false;
         }
      }

      if (!var2) {
         throw new DescriptorValidationException("INVALID_WELCOME_FILE");
      }
   }

   public void register() throws ManagementException {
      super.register();
   }

   public String toXML(int var1) {
      String var2 = "";
      if (this.fileNames != null && this.fileNames.length != 0) {
         var2 = var2 + this.indentStr(var1) + "<welcome-file-list>\n";
         var1 += 2;

         for(int var3 = 0; var3 < this.fileNames.length; ++var3) {
            String var4 = this.fileNames[var3];
            var2 = var2 + this.indentStr(var1) + "<welcome-file>" + var4 + "</welcome-file>\n";
         }

         var1 -= 2;
         var2 = var2 + this.indentStr(var1) + "</welcome-file-list>\n";
         return var2;
      } else {
         return var2;
      }
   }
}
