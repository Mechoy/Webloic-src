package weblogic.ant.taskdefs.antline;

public class AntOpt {
   private String antName;
   private String attrName;
   private ArgConverter converter;
   private String eltPath;
   private String optValue;

   public AntOpt(String var1, ArgConverter var2) {
      this.optValue = null;
      this.antName = var1;
      int var3 = this.antName.lastIndexOf(64);
      if (var3 >= 0) {
         this.attrName = this.antName.substring(var3 + 1);
         this.eltPath = this.antName.substring(0, var3);
      } else {
         this.eltPath = this.antName;
      }

      if (AntTool.debug || AntLineTask.debug) {
         System.out.println("AntOpt[" + System.identityHashCode(this) + "] = " + this.eltPath + "@" + this.attrName);
      }

   }

   public AntOpt(String var1, String var2) {
      this(var1, (ArgConverter)null);
      this.setValue(var2);
   }

   public String getAntName() {
      return this.antName;
   }

   public String getAntAttrName() {
      return this.attrName;
   }

   public String getAntElementPath() {
      return this.eltPath;
   }

   public void setValue(String var1) {
      this.optValue = var1;
   }

   public String getValue() {
      return this.optValue;
   }

   public void setConverter(ArgConverter var1) {
      this.converter = var1;
   }

   public ArgConverter getConverter() {
      return this.converter;
   }
}
