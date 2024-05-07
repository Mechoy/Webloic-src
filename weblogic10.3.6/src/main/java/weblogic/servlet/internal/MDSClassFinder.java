package weblogic.servlet.internal;

import oracle.jsp.provider.JspResourceProvider;
import weblogic.utils.classloaders.AbstractClassFinder;
import weblogic.utils.classloaders.Source;

public class MDSClassFinder extends AbstractClassFinder {
   private JspResourceProvider provider;

   public MDSClassFinder(JspResourceProvider var1) {
      this.provider = var1;
   }

   public String getClassPath() {
      return "";
   }

   public Source getSource(String var1) {
      MDSSource var2 = new MDSSource(var1, this.provider);
      return var2.exists() ? var2 : null;
   }
}
