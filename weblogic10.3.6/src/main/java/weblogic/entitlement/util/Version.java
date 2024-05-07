package weblogic.entitlement.util;

import java.io.Serializable;

public class Version implements Serializable {
   private String who;
   private int major;
   private int minor;
   private String build;
   private String rcsId;

   public Version() {
      this.who = this.getClass().getName();
      this.build = "";
      this.rcsId = "$Id: Version.java,v 1.3 2001/11/19 14:20:28 nboegholm Exp $";
   }

   public Version(String var1, int var2, int var3, String var4, String var5) {
      this.who = var1;
      this.major = var2;
      this.minor = var3;
      this.build = var4;
      this.rcsId = var5;
   }

   public String getVersionString() {
      int var1 = this.who.lastIndexOf(".");
      String var2 = var1 < 0 ? this.who : this.who.substring(var1);
      return var2 + ": Version " + this.major + "." + this.minor + " - build " + this.build;
   }

   public String getInfoString() {
      return "\nModule: " + this.who + "\nVersion: " + this.major + "." + this.minor + "\nBuild: " + this.build + "\nId: " + this.rcsId;
   }

   public int getMajorNumber() {
      return this.major;
   }

   public int getMinorNumber() {
      return this.minor;
   }

   public String getBuildString() {
      return this.build;
   }

   public String toString() {
      return this.getInfoString();
   }
}
