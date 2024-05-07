package weblogic.servlet.internal.dd.glassfish;

import java.util.Iterator;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.apache.commons.lang.StringUtils;
import weblogic.j2ee.descriptor.wl.SecurityRoleAssignmentBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebAppBean;
import weblogic.servlet.HTTPLogger;
import weblogic.utils.collections.ArraySet;

public class SecurityRoleMappingTagParser extends BaseGlassfishTagParser {
   public void parse(XMLStreamReader var1, WeblogicWebAppBean var2) throws XMLStreamException {
      String var3 = null;
      ArraySet var4 = new ArraySet();

      int var5;
      do {
         var5 = var1.next();
         if (var5 == 1) {
            String var6 = var1.getLocalName();
            if ("role-name".equals(var6)) {
               var3 = this.parseTagData(var1);
            } else if ("principal-name".equals(var6) || "group-name".equals(var6)) {
               var4.add(this.parseTagData(var1));
            }
         }
      } while(var1.hasNext() && !this.isEndTag(var5, var1, "security-role-mapping"));

      if (StringUtils.isNotEmpty(var3) && var4 != null && var4.size() > 0) {
         SecurityRoleAssignmentBean var9 = var2.createSecurityRoleAssignment();
         var9.setRoleName(var3);
         Iterator var7 = var4.iterator();

         while(var7.hasNext()) {
            String var8 = (String)var7.next();
            var9.addPrincipalName(var8);
         }

         HTTPLogger.logGlassfishDescriptorParsed("security-role-mapping");
      }

   }
}
