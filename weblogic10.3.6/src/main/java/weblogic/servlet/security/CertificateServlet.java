package weblogic.servlet.security;

import com.rsa.certj.cert.AttributeValueAssertion;
import com.rsa.certj.cert.PKCS10CertRequest;
import com.rsa.certj.cert.RDN;
import com.rsa.certj.cert.X500Name;
import com.rsa.jsafe.JSAFE_KeyPair;
import com.rsa.jsafe.JSAFE_Parameters;
import com.rsa.jsafe.JSAFE_PrivateKey;
import com.rsa.jsafe.JSAFE_PublicKey;
import com.rsa.jsafe.JSAFE_SecretKey;
import com.rsa.jsafe.JSAFE_SecureRandom;
import com.rsa.jsafe.JSAFE_SymmetricCipher;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.security.SecureRandom;
import java.util.Hashtable;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.SingleThreadModel;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import weblogic.logging.LogOutputStream;
import weblogic.security.Salt;
import weblogic.utils.StackTraceUtils;
import weblogic.utils.encoders.BASE64Encoder;

/** @deprecated */
public final class CertificateServlet extends HttpServlet implements SingleThreadModel {
   private static final byte[][] oids;
   private static final int[] types;
   private static boolean checked;
   private boolean errors = false;
   private Hashtable putBack = null;
   private StringBuffer errorMessage = null;
   private boolean fullStrength = false;
   protected JSAFE_SecureRandom secRan = null;
   private CertificateServletTextFormatter formatter = CertificateServletTextFormatter.getInstance();
   public static final Locale myLocale;
   public static final String myLanguage;
   private boolean englishFlag = false;

   public void service(HttpServletRequest var1, HttpServletResponse var2) throws ServletException, IOException {
      var2.setContentType("text/html");
      PrintWriter var3 = var2.getWriter();
      String var4 = var1.getContextPath() + "/images/";
      if (myLanguage.equals("English")) {
         this.englishFlag = true;
      }

      var3.println("<html>");
      var3.println("<head>");
      var3.println("<title>" + this.formatter.getPageTitle() + "</title>");
      var3.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">");
      var3.println("</head>");
      var3.println("<BODY alink=\"#397F70\" bgcolor=\"#FFFFFF\" link=\"#640078\" vlink=\"#DE7E00\">");
      var3.println("<font face=\"Helvetica\">");
      var3.println("<table border=0>");
      var3.println("<td rowspan=2 valign=top>");
      var3.println("<img src=" + var4 + "bealogo.gif width=69 height=69");
      var3.println("valign=middle border=0 align=left></a>");
      var3.println("</td>");
      var3.println("<td>");
      var3.println("<img src=" + var4 + "trans.gif width=1 height=14 align=right>");
      var3.println("</td>");
      var3.println("<tr>");
      var3.println("<td>");
      var3.println("<h2>");
      var3.println("<font face=\"Helvetica\">");
      var3.println(this.formatter.getPageTitle());
      var3.println("</font>");
      var3.println("</h2>");
      var3.println("</td>");
      var3.println("</tr>");
      var3.println("</table>");
      var3.println("<p>");
      String var6 = var1.getContextPath() + "/help/";
      String var5;
      if (var6.charAt(var6.length() - 1) == '/') {
         var5 = var6 + "CertificateHelp.html";
      } else {
         var5 = var6 + "/CertificateHelp.html";
      }

      if (!checked) {
         this.fullStrength = true;
         var3.println("<b>" + this.formatter.getFullStrengthMsg() + "</b>");
         checked = true;
      }

      String var7 = var1.getMethod();
      String var8;
      if (var1.getMethod().equals("POST")) {
         var8 = this.processRequest(var1);
         String var9 = "";
         var9 = (new File(var1.getParameter("name").replace('.', '_'))).getCanonicalPath();
         StringBuffer var10 = new StringBuffer();
         var10.append("<FORM\n");
         var10.append("ACTION=\"https://digitalid.verisign.com/cgi-bin/sophia.exe\"\n");
         var10.append("METHOD=\"POST\"\n");
         var10.append("NAME=\"enrollment\"\n");
         var10.append("enctype=\"x-www-form-encoded\">\n");
         var10.append("<INPUT TYPE=\"hidden\" NAME=\"operation\" VALUE=\"C1Submit\">\n");
         var10.append("<INPUT TYPE=\"hidden\" NAME=\"originator\" VALUE=\"VeriSign Inc.\">\n");
         var10.append("<INPUT TYPE=\"hidden\" NAME=\"service\" VALUE=\"other\">\n");
         var10.append("<INPUT TYPE=\"hidden\" NAME=\"commercial\" VALUE=\"yes\">\n");
         var10.append("<INPUT TYPE=\"hidden\" NAME=\"html_file\" VALUE=\"../htmldocs/server/trial/trialStep3.html\">\n");
         var10.append("<INPUT TYPE=\"hidden\" NAME=\"form_file\" VALUE=\"../fdf/testGetCSR.fdf\">\n");
         var10.append("<INPUT TYPE=\"hidden\" NAME=\"CheckWeakKey\" VALUE=\"yes\">\n");
         var10.append("<INPUT TYPE=\"hidden\" NAME=\"billing_type\" VALUE=\"Initial\">\n");
         var10.append("<INPUT TYPE=\"hidden\" NAME=\"additional_field5\" VALUE=\"$$additional_field5$$\">\n");
         var10.append("<INPUT TYPE=\"hidden\" NAME=\"csr\" VALUE=\"" + var8 + "\">\n");
         var10.append("<p> \n");
         var10.append(this.formatter.getProtectedKeyFilename() + "<b>" + var9 + "-key.der</b><br>\n");
         var10.append(this.formatter.getRequestFilename() + "<b>" + var9 + "-request.pem</b></p>\n");
         var10.append("<p> \n");
         var10.append("<INPUT type=\"SUBMIT\" VALUE=\"Submit\" name=\"SUBMIT\"> \n");
         var10.append(this.formatter.getCertSigningRequest() + "<b> " + this.formatter.getVeriSignName() + " </b>" + this.formatter.getTestDigitalID());
         var10.append("</FORM>\n");
         var10.append("<p>" + this.formatter.getButtonProducesTestCertificate() + "\n");
         var10.append(this.formatter.getObtainFormalServerCertVeriSign() + "\n");
         var10.append(" <a href=\"http://digitalid.verisign.com/server/trial/trialIntro.htm\">http://digitalid.verisign.com/server/trial/trialIntro.htm</a> </p>\n");
         var10.append("</p>\n\n");
         var10.append("<FORM METHOD=\"POST\"\n");
         var10.append("    NAME=\"form\"\n");
         var10.append("    ACTION=\"https://www3.cybertrust.gte.com/cgi-bin/BusinessSolutions/ServerCerts/Customer/pkcs10.sh \"\n");
         var10.append("    enctype=\"x-www-form-encoded\">\n");
         var10.append("<p> \n");
         var10.append("<INPUT TYPE=\"HIDDEN\" NAME=\"asn1\" VALUE=\"" + var8 + "\">\n");
         var10.append("<INPUT type=\"SUBMIT\" VALUE=\"Submit\" name=\"SUBMIT\"> \n");
         var10.append(this.formatter.getCertSigningRequest() + "<b> " + this.formatter.getBaltimoreName() + " </b>" + this.formatter.getForAFormalWLSLogicServerCert());
         var10.append("</FORM>\n");
         var10.append("<p>" + this.formatter.getThisButtonProduces() + " " + this.formatter.getAFormalWebLogicServerCertFrom() + this.formatter.getBaltimoreName() + "\n");
         var10.append(this.formatter.getMoreInformationBaltimore() + " ");
         var10.append("<a href=\"http://www.baltimore.com/sureserver/\">");
         var10.append("http://www.baltimore.com/sureserver/ </a>");
         var10.append("</p>\n");
         var10.append("<br><b>" + this.formatter.getNote() + "</b>: " + this.formatter.getAnyCertAuthWhichProducesCerts());
         if (!this.errors) {
            var3.println("<pre>" + var8 + "</pre>" + var10.toString());
         } else if (var8 != "1" && var8 != "0") {
            var3.println("<pre>" + var8 + "</pre>");
         }
      }

      if (!var1.getMethod().equals("POST") || this.errors) {
         var3.println("<p>");
         var3.println("<FORM METHOD=\"POST\" ACTION=" + Utils.encodeXSS(var1.getRequestURI()) + ">");
         var3.println("<font color=#DB1260>" + (this.errors ? this.errorMessage.toString() + "<br>" : "") + "</font>");
         var3.println("<p>");
         var8 = "href=http://www.bcpl.net/~jspath/isocodes.html target=cert>country";
         var3.println(this.formatter.getDirectionsParaOne() + " <a");
         var3.println(var8 + " " + this.formatter.getDirectionsParaOneB() + "</a>");
         var3.println(this.formatter.getDirectionsParaOneA());
         var3.println("<p>");
         var3.println(this.formatter.getDirectionsParaTwo());
         var3.println("<p>");
         var3.println(this.formatter.getDirectionsParaThree());
         var3.println("<p>");
         var3.println(this.formatter.getDirectionsParaFour());
         var3.println("<p>");
         var3.println("<ul>");
         var3.println("<li>");
         var3.println(this.formatter.getDirectionsParaFourA());
         var3.println("<p>");
         var3.println("<li>");
         var3.println(this.formatter.getDirectionsParaFourB());
         var3.println("</ul>");
         var3.println("<p>");
         var3.println(this.formatter.getDirectionsParaFive());
         var3.println("<p>");
         var3.println("<center>");
         var3.println("<table cellpadding=5 bgcolor=#EEEEEE border=1> <tr> <td><font");
         if (this.englishFlag) {
            var3.println("face=\"Helvetica\"> <img src=" + var4 + "requiredorange.gif width=13");
            var3.println("height=22 alt=\"Required\" border=0> <b><a");
         } else {
            var3.println("face=\"Helvetica\"> <img src=" + var4 + "requiredorangeI18n.gif width=13 height=22 alt=");
            var3.println(this.formatter.getRequired() + " border=0> <b><a");
         }

         var3.println("href=" + var5 + "#countrycode target=cert>");
         var3.println(this.formatter.getCountryCode() + "</a></b>");
         var3.println("</font>");
         var3.println("</td>");
         var3.println("<td>");
         var3.println("<font face=\"Helvetica\">");
         var3.println("<input size=30 type=\"text\" maxlength=2");
         var3.println("name=\"countryName\" value=\"" + (this.errors ? Utils.encodeXSS((String)this.putBack.get("countryName")) : this.formatter.getCountrySample()) + "\"><br>");
         var3.println(this.formatter.getCountryExample());
         var3.println("</font>");
         var3.println("</td>");
         var3.println("</tr>");
         var3.println("<tr> <td><font face=\"Helvetica\"> <img");
         if (this.englishFlag) {
            var3.println("src=" + var4 + "requiredorange.gif width=13 height=22");
            var3.println("alt=\"Required\" border=0> <a href=" + var5 + "#organizationalunitname");
         } else {
            var3.println("src=" + var4 + "requiredorangeI18n.gif width=13 height=22 alt=");
            var3.println(this.formatter.getRequired() + " border=0> <a href=" + var5 + "#organizationalunitname");
         }

         var3.println("target=cert><b>" + this.formatter.getOrgUnitName() + "</b></a>");
         var3.println("</font>");
         var3.println("</td>");
         var3.println("<td> ");
         var3.println("<font face=\"Helvetica\">");
         var3.println("<input size=30 type=\"text\" maxlength=64");
         var3.println("value=\"" + (this.errors ? Utils.encodeXSS((String)this.putBack.get("orgUnitName")) : "") + "\" name=\"orgUnitName\"><br>");
         var3.println(this.formatter.getOrgUnitExample());
         var3.println("</font>");
         var3.println("</td>");
         var3.println("</tr>");
         var3.println("<tr> <td><font face=\"Helvetica\"> <img");
         if (this.englishFlag) {
            var3.println("src=" + var4 + "requiredorange.gif width=13 height=22");
            var3.println("alt=\"Required\" border=0> <a href=" + var5 + "#organizationname");
         } else {
            var3.println("src=" + var4 + "requiredorangeI18n.gif width=13 height=22 alt=");
            var3.println(this.formatter.getRequired() + " border=0> <a href=" + var5 + "#organizationname");
         }

         var3.println("target=cert><b>" + this.formatter.getOrgName() + "</b></a>");
         var3.println("</font>");
         var3.println("</td>");
         var3.println("<td> ");
         var3.println("<font face=\"Helvetica\">");
         var3.println("<input size=30 type=\"text\" maxlength=64");
         var3.println("name=\"organizationName\" value=\"" + (this.errors ? Utils.encodeXSS((String)this.putBack.get("organizationName")) : "") + "\"><br>");
         var3.println(this.formatter.getOrgNameExample());
         var3.println("</font>");
         var3.println("</td>");
         var3.println("</tr>");
         var3.println("<tr> <td><img");
         if (this.englishFlag) {
            var3.println("src=" + var4 + "requiredorange.gif width=13 height=22");
            var3.println("alt=\"Required\" border=0>");
         } else {
            var3.println("src=" + var4 + "requiredorangeI18n.gif width=13 height=22 alt=");
            var3.println(this.formatter.getRequired() + " border=0>");
         }

         var3.println("<font face=\"Helvetica\"><a href=" + var5 + "#emailaddress");
         var3.println("target=cert><b>" + this.formatter.getEmailAddress() + "</b></a>");
         var3.println("</font>");
         var3.println("</td>");
         var3.println("<td> ");
         var3.println("<font face=\"Helvetica\">");
         var3.println("<input size=\"30\" type=\"TEXT\" maxlength=\"64\"");
         var3.println("name=\"email\" value=\"" + (this.errors ? Utils.encodeXSS((String)this.putBack.get("email")) : "") + "\"><br>");
         var3.println(this.formatter.getEmailExample());
         var3.println("</font>");
         var3.println("</td>");
         var3.println("</tr>");
         var3.println("<tr> <td><font face=\"Helvetica\"> <img");
         if (this.englishFlag) {
            var3.println("src=" + var4 + "requiredorange.gif width=13 height=22");
            var3.println("alt=\"Required\" border=0> <a href=" + var5 + "#fullhostname");
         } else {
            var3.println("src=" + var4 + "requiredorangeI18n.gif width=13 height=22 alt=");
            var3.println(this.formatter.getRequired() + " border=0> <a href=" + var5 + "#fullhostname");
         }

         var3.println("target=cert><b>" + this.formatter.getFullHostName() + "</b></a>");
         var3.println("</font>");
         var3.println("</td>");
         var3.println("<td> ");
         var3.println("<font face=\"Helvetica\">");
         var3.println("<input size=\"30\" type=\"TEXT\" maxlength=\"64\" value=\"" + (this.errors ? Utils.encodeXSS((String)this.putBack.get("name")) : InetAddress.getLocalHost().getHostName()) + "\" name=\"name\"><br>");
         var3.println(this.formatter.getHostExample());
         var3.println("</font>");
         var3.println("</td>");
         var3.println("</tr>");
         var3.println("<tr> <td><font face=\"Helvetica\"> <img");
         if (this.englishFlag) {
            var3.println("src=" + var4 + "requiredorange.gif width=13 height=22");
            var3.println("alt=\"Required\" border=0> <a href=" + var5 + "#localityname");
         } else {
            var3.println("src=" + var4 + "requiredorangeI18n.gif width=13 height=22 alt=");
            var3.println(this.formatter.getRequired() + " border=0> <a href=" + var5 + "#localityname");
         }

         var3.println("target=cert><b>Locality name (city)</b></a>");
         var3.println("</font>");
         var3.println("</td>");
         var3.println("<td> ");
         var3.println("<font face=\"Helvetica\">");
         var3.println("<input size=30 type=\"text\" maxlength=64");
         var3.println("name=\"localityName\" value=\"" + (this.errors ? Utils.encodeXSS((String)this.putBack.get("localityName")) : "") + "\"><br>");
         var3.println(this.formatter.getLocalityExample());
         var3.println("</font>");
         var3.println("</td>");
         var3.println("</tr>");
         var3.println("<tr> <td><font face=\"Helvetica\"> <img");
         if (this.englishFlag) {
            var3.println("src=" + var4 + "requiredorange.gif width=13 height=22");
            var3.println("alt=\"Required\" border=0> <a href=" + var5 + "#statename");
         } else {
            var3.println("src=" + var4 + "requiredorangeI18n.gif width=13 height=22 alt=");
            var3.println(this.formatter.getRequired() + " border=0> <a href=" + var5 + "#statename");
         }

         var3.println("target=cert><b>State name</b></a>");
         var3.println("</font>");
         var3.println("</td>");
         var3.println("<td>");
         var3.println("<font face=\"Helvetica\">");
         var3.println("<input size=30 type=\"text\" maxlength=64");
         var3.println("name=\"stateName\" value=\"" + (this.errors ? Utils.encodeXSS((String)this.putBack.get("stateName")) : "") + "\"><br>");
         var3.println(this.formatter.getExampleStateName());
         var3.println("</font>");
         var3.println("</td>");
         var3.println("</tr>");
         var3.println("<tr> <td><font face=\"Helvetica\"> <img");
         if (this.englishFlag) {
            var3.println("src=" + var4 + "requiredorange.gif width=13 height=22");
            var3.println("alt=\"Required\" border=0> <a href=" + var5 + "#keyPassword");
         } else {
            var3.println("src=" + var4 + "requiredorangeI18n.gif width=13 height=22 alt=");
            var3.println(this.formatter.getRequired() + " border=0> <a href=" + var5 + "#keyPassword");
         }

         var3.println("target=cert><b>" + this.formatter.getPrivateKeyPassword() + "</b></a>");
         var3.println("</font>");
         var3.println("</td>");
         var3.println("<td>");
         var3.println("<font face=\"Helvetica\">");
         var3.println("<input size=30 type=\"text\" maxlength=64");
         var3.println("name=\"keyPassword\" value=\"" + (this.errors ? Utils.encodeXSS((String)this.putBack.get("keyPassword")) : "") + "\"><br>");
         var3.println("</font>");
         var3.println("</td>");
         var3.println("</tr>");
         if (this.fullStrength) {
            StringBuffer var11 = new StringBuffer();
            var11.append("<tr>    \n");
            var11.append(" <td>   \n");
            var11.append("  <font face=\"Helvetica\">     \n");
            if (this.englishFlag) {
               var11.append("  <img src=" + var4 + "requiredorange.gif width=13 height=22 alt=\"Required\" border=0>  \n");
            } else {
               var11.append("  <img src=" + var4 + "requiredorangeI18n.gif width=13 height=22 alt=");
               var11.append(this.formatter.getRequired() + " border=0> \n");
            }

            var11.append("  <a href=" + var5 + "#strength target=cert>  \n");
            var11.append("  <b>" + this.formatter.getStrength() + "</b>   \n");
            var11.append("  </a> </font>    \n");
            var11.append(" </td>      \n");
            var11.append(" <td>       \n");
            var11.append("  <font face=\"Helvetica\"> \n");
            if (this.errors) {
               int var12 = (Integer)this.putBack.get("strength");
               if (var12 == 512) {
                  var11.append("  <input type=radio name=\"strength\" value=\"512\" checked>Exportable - 512 bit key length<br>   \n");
                  var11.append("  <input type=radio name=\"strength\" value=\"1024\">Domestic - 1024 bit key length<br> \n");
                  var11.append("  <input type=radio name=\"strength\" value=\"2048\">Domestic - 2048 bit key length<br> \n");
               } else if (var12 == 1024) {
                  var11.append("  <input type=radio name=\"strength\" value=\"512\">Exportable - 512 bit key length<br>   \n");
                  var11.append("  <input type=radio name=\"strength\" value=\"1024\" checked>Domestic - 1024 bit key length<br> \n");
                  var11.append("  <input type=radio name=\"strength\" value=\"2048\">Domestic - 2048 bit key length<br> \n");
               } else if (var12 == 2048) {
                  var11.append("  <input type=radio name=\"strength\" value=\"512\">Exportable - 512 bit key length<br>   \n");
                  var11.append("  <input type=radio name=\"strength\" value=\"1024\">Domestic - 1024 bit key length<br> \n");
                  var11.append("  <input type=radio name=\"strength\" value=\"2048\" checked>Domestic - 2048 bit key length<br> \n");
               } else {
                  var11.append("  <input type=radio name=\"strength\" value=\"512\">Exportable - 512 bit key length<br>   \n");
                  var11.append("  <input type=radio name=\"strength\" value=\"1024\">Domestic - 1024 bit key length<br> \n");
                  var11.append("  <input type=radio name=\"strength\" value=\"2048\" checked>Domestic - 2048 bit key length<br> \n");
               }
            } else {
               var11.append("  <input type=radio name=\"strength\" value=\"512\">Exportable - 512 bit key length<br>   \n");
               var11.append("  <input type=radio name=\"strength\" value=\"1024\">Domestic - 1024 bit key length<br> \n");
               var11.append("  <input type=radio name=\"strength\" value=\"2048\" checked>Domestic - 2048 bit key length<br> \n");
            }

            var11.append("  </font> \n");
            var11.append(" </td>  \n");
            var11.append("</tr>   \n");
            if (this.errors) {
               var3.write(var11.toString());
               var3.flush();
            }

            if (!this.errors) {
               var3.println(var11.toString());
            }
         }

         var3.println("<tr> ");
         var3.println("<td align=center colspan=2> ");
         var3.println("<input type=\"SUBMIT\" value='" + this.formatter.getGenerateRequest());
         var3.println("'name=\"SUBMIT2\">");
         var3.println("</td>");
         var3.println("</tr>");
         var3.println("</table>");
         var3.println("</center>");
         var3.println("</form>");
         var3.println("<p>");
         var3.println("<a href=" + var5 + " target=cert><img");
         if (this.englishFlag) {
            var3.println("src=" + var4 + "admintoolbar11.gif width=37 height=25 alt=\"Online");
            var3.println("help\" border=0></a>");
         } else {
            var3.println("src=" + var4 + "admintoolbarI18n11.gif width=37 height=25 alt=");
            var3.println(this.formatter.getOnlineHelp() + " border=0> </a>");
         }

         var3.println("<p>");
         if (this.englishFlag) {
            var3.println("<img src=" + var4 + "requiredorange.gif");
            var3.println("width=20 height=20 align=left alt=\"Required\" border=0>");
         } else {
            var3.println("<img src=" + var4 + "requiredorangeI18n.gif");
            var3.println("width=20 height=20 align=left alt=");
            var3.println(this.formatter.getRequired() + " " + "border=0> </a>");
         }

         var3.println(this.formatter.getRequiredFields() + "<br>");
         var3.println("<p>");
         this.errors = false;
      }

      var3.println("</font>");
      var3.println("</body>");
      var3.println("</html>");
   }

   private boolean isHostNameValid(String var1) {
      return true;
   }

   private void fillPutBack() {
      this.putBack = new Hashtable();
      this.putBack.put("countryName", "");
      this.putBack.put("orgUnitName", "");
      this.putBack.put("organizationName", "");
      this.putBack.put("email", "");
      this.putBack.put("name", "");
      this.putBack.put("localityName", "");
      this.putBack.put("stateName", "");
      this.putBack.put("keyPassword", "");
      this.putBack.put("strength", "");
   }

   private String processRequest(ServletRequest var1) {
      this.fillPutBack();
      this.errorMessage = new StringBuffer("<b>Errors:</b><br>");

      String var3;
      try {
         String var2 = var1.getParameter("name");
         if (var2.length() >= 4 && this.isHostNameValid(var2)) {
            this.putBack.put("name", var2);
         } else {
            this.errors = true;
            this.errorMessage.append(this.formatter.getValidHostNameError() + "<br>");
         }

         var3 = var2.replace('.', '_') + "-key.der";
         File var4 = new File(var3);
         if (var4.exists()) {
            this.errorMessage.append(this.formatter.getTheFile() + " <b>" + var4 + "</b> " + this.formatter.getOverwriteKeyFileError() + "<br>");
            this.errors = true;
         }

         String var5 = var1.getParameter("countryName");
         if (var5.length() < 2) {
            this.errors = true;
            this.errorMessage.append(this.formatter.getValidCountryNameError() + "<br>");
         } else {
            this.putBack.put("countryName", var5);
         }

         String var6 = var1.getParameter("stateName");
         if (var6.length() < 2) {
            this.errors = true;
            this.errorMessage.append(this.formatter.getValidStateNameError() + "<br>");
         } else {
            this.putBack.put("stateName", var6);
         }

         String var7 = var1.getParameter("localityName");
         if (var7.length() < 2) {
            this.errors = true;
            this.errorMessage.append(this.formatter.getValidLocalityNameError() + "<br>");
         } else {
            this.putBack.put("localityName", var7);
         }

         String var8 = var1.getParameter("organizationName");
         if (var8.length() < 1) {
            this.errors = true;
            this.errorMessage.append(this.formatter.getValidOrgNameError() + "<br>");
         } else {
            this.putBack.put("organizationName", var8);
         }

         String var9 = var1.getParameter("orgUnitName");
         if (var9.length() < 1) {
            this.errors = true;
            this.errorMessage.append("Please specify a valid organizational unit name<br>");
         } else {
            this.putBack.put("orgUnitName", var9);
         }

         String var10 = var1.getParameter("email");
         if (var10 != null && var10.length() > 1 && var10.indexOf("@") == -1) {
            this.errors = true;
            this.errorMessage.append(this.formatter.getValidEmailAddressError() + "<br>");
         } else {
            this.putBack.put("email", var10);
         }

         int var11 = 512;
         if (this.fullStrength) {
            try {
               var11 = Integer.parseInt(var1.getParameter("strength"));
               if (var11 > 768) {
                  (new LogOutputStream("certificate request servlet")).info("~~~~ using " + var11 + " bit keysize ~~~~");
               }

               this.putBack.put("strength", new Integer(var11));
            } catch (NumberFormatException var34) {
               this.errors = true;
               this.errorMessage.append(this.formatter.getSpecifyCertifiateStrengthError() + "<br>");
            }
         }

         String var12 = var1.getParameter("keyPassword");
         if (var12 == null || var12.length() == 0) {
            this.errors = true;
            this.errorMessage.append(this.formatter.getPrivateKeyPasswordError() + "<br>");
         }

         this.putBack.put("keyPassword", var12);
         if (this.errors) {
            return this.fullStrength ? "1" : "0";
         } else {
            JSAFE_KeyPair var13 = JSAFE_KeyPair.getInstance("RSA", "Java");
            int[] var14 = new int[]{var11, 65537};
            if (this.secRan == null) {
               this.secRan = (JSAFE_SecureRandom)JSAFE_SecureRandom.getInstance("MD5Random", "Java");
               this.secRan.seed(Salt.getRandomBytes(64));
            }

            var13.generateInit((JSAFE_Parameters)null, var14, this.secRan);
            var13.generate();
            JSAFE_PrivateKey var15 = var13.getPrivateKey();
            JSAFE_PublicKey var16 = var13.getPublicKey();
            byte[] var17 = new byte[]{0, 17, 34, 51, 68, 85, 102, 119};
            Object var18 = null;
            JSAFE_SymmetricCipher var19 = null;
            JSAFE_SecretKey var20 = null;

            byte[] var37;
            try {
               var19 = JSAFE_SymmetricCipher.getInstance("PBE/MD5/DES/CBC/PKCS5PBE-5-56", "Java");
               var19.setSalt(var17, 0, var17.length);
               var20 = var19.getBlankKey();
               char[] var21 = new char[var12.length()];
               var12.getChars(0, var12.length(), var21, 0);
               var20.setPassword(var21, 0, var21.length);
               var19.encryptInit(var20, (SecureRandom)null);
               var37 = var19.wrapPrivateKey(var15, true);
            } catch (Throwable var33) {
               String var22 = StackTraceUtils.throwable2StackTrace(var33);
               this.errors = true;
               this.errorMessage.append(this.formatter.getEncryptingPrivateKeyError() + "<br>");
               return var22;
            }

            X500Name var38 = new X500Name();

            String var23;
            try {
               for(int var39 = 0; var39 < types.length; ++var39) {
                  var23 = null;
                  short var24 = 5632;
                  switch (types[var39]) {
                     case 0:
                        var23 = var2;
                        break;
                     case 1:
                        var23 = var5;
                        var24 = 4864;
                        break;
                     case 2:
                        var23 = var7;
                        break;
                     case 3:
                        var23 = var6;
                        break;
                     case 4:
                        var23 = var8;
                        break;
                     case 5:
                        var23 = var9;
                     case 6:
                     default:
                        break;
                     case 7:
                        var23 = var10;
                  }

                  if (var23 != null) {
                     RDN var25 = new RDN();
                     var25.addNameAVA(new AttributeValueAssertion(types[var39], oids[var39], var24, var23));
                     var38.addRDN(var25);
                  }
               }
            } catch (Throwable var35) {
               var23 = StackTraceUtils.throwable2StackTrace(var35);
               this.errors = true;
               this.errorMessage.append(this.formatter.getErrorAddingRelativeDN() + "<br>");
               return var23;
            }

            PKCS10CertRequest var40 = new PKCS10CertRequest();

            try {
               var40.setSubjectName(var38);
               var40.setSubjectPublicKey(var16);
               var40.signCertRequest("SHA1/RSA/PKCS1Block01Pad", "Java", var15, this.secRan);
            } catch (Throwable var32) {
               String var41 = StackTraceUtils.throwable2StackTrace(var32);
               this.errors = true;
               this.errorMessage.append(this.formatter.getErrorCreatingSigningRequest() + " <br>");
               return var41;
            }

            FileOutputStream var44 = new FileOutputStream(var4);
            var44.write(var37, 0, var37.length);
            var44.close();
            byte[] var42 = new byte[var40.getDERLen(0)];
            var40.getDEREncoding(var42, 0, 0);
            ByteArrayOutputStream var43 = new ByteArrayOutputStream();
            PrintWriter var26 = new PrintWriter(var43);
            var26.println(this.formatter.getBeginCertRequest());
            var26.flush();

            int var28;
            for(int var27 = 0; var27 < var42.length; var27 = var28) {
               var28 = var27 + 54;
               if (var28 > var42.length) {
                  var28 = var42.length;
               }

               (new BASE64Encoder()).encodeBuffer(new ByteArrayInputStream(var42, var27, var28 - var27), var43);
               var43.flush();
               var26.println();
               var26.flush();
            }

            var43.flush();
            var26.println(this.formatter.getEndCertRequest());
            var26.flush();
            String var29 = new String(var43.toByteArray());
            String var30 = var2.replace('.', '_') + "-request";
            FileOutputStream var31 = new FileOutputStream(var30 + ".dem");
            var31.write(var42);
            var31.close();
            var44 = new FileOutputStream(var30 + ".pem");
            var44.write(var29.getBytes());
            var44.close();
            return var29;
         }
      } catch (Throwable var36) {
         var3 = StackTraceUtils.throwable2StackTrace(var36);
         return var3;
      }
   }

   static {
      oids = new byte[][]{AttributeValueAssertion.COMMON_NAME_OID, AttributeValueAssertion.COUNTRY_NAME_OID, AttributeValueAssertion.LOCALITY_NAME_OID, AttributeValueAssertion.STATE_NAME_OID, AttributeValueAssertion.ORGANIZATION_NAME_OID, AttributeValueAssertion.ORGANIZATIONAL_UNIT_NAME_OID, AttributeValueAssertion.TELEPHONE_NUMBER_OID, AttributeValueAssertion.EMAIL_ADDRESS_OID, AttributeValueAssertion.TITLE_OID, AttributeValueAssertion.STREET_ADDRESS_OID, AttributeValueAssertion.BUSINESS_CATEGORY_OID};
      types = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
      myLocale = Locale.getDefault();
      myLanguage = myLocale.getDisplayLanguage();
   }
}
