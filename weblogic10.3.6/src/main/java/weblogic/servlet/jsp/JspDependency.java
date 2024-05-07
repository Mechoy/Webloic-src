package weblogic.servlet.jsp;

class JspDependency {
   String uri;
   long lastMod;

   protected JspDependency(String var1, long var2) {
      this.uri = var1;
      this.lastMod = var2;
   }

   public int hashCode() {
      return (int)this.lastMod;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof JspDependency)) {
         return false;
      } else {
         JspDependency var2 = (JspDependency)var1;
         return this.uri.equals(var2.uri) && this.lastMod == var2.lastMod;
      }
   }
}
