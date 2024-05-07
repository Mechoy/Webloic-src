package weblogic.wsee.security.policy12.assertions;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import weblogic.wsee.util.Verbose;

public class XPath2 extends XPath {
   public static final String XPATH2 = "XPath2";
   private static final String FILTER_ATTRIBUTE = "Filter";
   private String filter = null;
   private static final long serialVersionUID = 6087344143246850153L;
   private static boolean verbose = Verbose.isVerbose(XPath2.class);

   public QName getName() {
      return new QName(this.getNamespace(), "XPath2", "sp13");
   }

   public String getFilter() {
      return this.filter;
   }

   public void setFilter(String var1) {
      this.filter = var1;
   }

   void initAssertion(Element var1) {
      super.initAssertion(var1);
      this.filter = (String)this.getElementAttrs().get("Filter");
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      super.readExternal(var1);
      this.filter = var1.readUTF();
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      super.writeExternal(var1);
      var1.writeUTF(this.filter);
   }
}
