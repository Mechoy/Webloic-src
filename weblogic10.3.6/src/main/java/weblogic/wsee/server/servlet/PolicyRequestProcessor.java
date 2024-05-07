package weblogic.wsee.server.servlet;

import java.io.IOException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.w3c.dom.Element;
import weblogic.wsee.policy.framework.DOMUtils;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.framework.PolicyStatement;
import weblogic.wsee.policy.runtime.PolicyServer;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsService;

public class PolicyRequestProcessor implements Processor {
   private static final boolean verbose = Verbose.isVerbose(PolicyRequestProcessor.class);
   private static final boolean debug = false;

   public boolean process(HttpServletRequest var1, HttpServletResponse var2, BaseWSServlet var3) throws IOException {
      String var4 = var1.getQueryString();
      if (verbose) {
         Verbose.log((Object)("qString = " + var4));
         Verbose.log((Object)("name = " + var1.getParameter("name")));
         Verbose.log((Object)("inbound = " + var1.getParameter("inbound")));
      }

      if ("GET".equalsIgnoreCase(var1.getMethod()) && var4.startsWith("POLICY&")) {
         WsService var5 = var3.getPort().getEndpoint().getService();
         PolicyServer var6 = this.getPolicyServer(var5);
         PolicyStatement var7 = null;
         String var8 = var1.getParameter("name");
         var7 = this.loadPolicy(var1, var8, var6);
         if (var7 == null) {
            return false;
         } else {
            Element var9 = this.toXml(var7, var8);
            this.writePolicy(var2, var9, var8);
            return true;
         }
      } else {
         return false;
      }
   }

   private PolicyStatement loadPolicy(HttpServletRequest var1, String var2, PolicyServer var3) {
      PolicyStatement var4 = null;

      try {
         String var5 = var1.getParameter("inbound");
         boolean var6 = "true".equalsIgnoreCase(var5);
         var4 = var3.getPolicy(var2, var6);
      } catch (PolicyException var7) {
         if (verbose) {
            Verbose.log((Object)("Failed to find policy '" + var1.getParameter("name") + "'"));
            Verbose.log((Object)var7);
         }
      }

      return var4;
   }

   private Element toXml(PolicyStatement var1, String var2) {
      Element var3 = null;

      try {
         var3 = var1.toXML();
      } catch (PolicyException var5) {
         if (verbose) {
            Verbose.log((Object)("Failed to write policy to XML: '" + var2 + "'"));
            Verbose.log((Object)var5);
         }
      }

      return var3;
   }

   private void writePolicy(HttpServletResponse var1, Element var2, String var3) throws IOException {
      var1.setContentType("text/xml");
      ServletOutputStream var4 = var1.getOutputStream();
      var4.write(DOMUtils.toXMLString(var2).getBytes());
      var4.write(10);
      var4.flush();
      if (verbose) {
         Verbose.log((Object)("Wrote policy '" + var3 + "'"));
      }

   }

   public PolicyServer getPolicyServer(WsService var1) {
      return var1.getPolicyServer();
   }
}
