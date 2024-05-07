package weblogic.auddi.uddi.client;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import weblogic.auddi.uddi.AuthTokenExpiredException;
import weblogic.auddi.uddi.FatalErrorException;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.AuthInfo;
import weblogic.auddi.uddi.request.UDDIRequest;
import weblogic.auddi.uddi.request.publish.GetAuthTokenRequest;
import weblogic.auddi.uddi.request.publish.UDDIPublishRequest;
import weblogic.auddi.uddi.response.AuthTokenResponse;
import weblogic.auddi.uddi.response.DispositionReportResponse;
import weblogic.auddi.uddi.response.ErrorDispositionReportResponse;
import weblogic.auddi.uddi.response.FaultResponse;
import weblogic.auddi.uddi.response.UDDIResponse;
import weblogic.auddi.uddi.soap.SOAPClient;
import weblogic.auddi.uddi.soap.UDDISOAPWrapper;
import weblogic.auddi.uddi.util.UDDIExceptionMapper;
import weblogic.auddi.uddi.xml.ParserWrapper;
import weblogic.auddi.util.Logger;
import weblogic.auddi.util.PropertyManager;
import weblogic.auddi.util.Util;
import weblogic.auddi.xml.SchemaException;

public class UDDIProxy {
   private URL m_inquiryURL = null;
   private URL m_publishURL = null;
   private String m_proxyHost = null;
   private int m_proxyPort = -1;
   private String m_username = null;
   private String m_password = null;
   private AuthInfo m_authinfo = null;
   public static final int ERROR = 0;
   public static final int INFO = 2;
   public static final int DEBUG = 3;
   public static final int TRACE = 4;

   private void initialize() {
      PropertyManager.setRuntimeProperty("uddi.schema.resource", "/weblogic/auddi/uddi/resources/uddi_v2.xsd");
      PropertyManager.setRuntimeProperty("soap.schema.resource", "/weblogic/auddi/uddi/resources/soap-envelope.xml");
      PropertyManager.setRuntimeProperty("xml.schema.resource", "/weblogic/auddi/uddi/resources/xml.xml");
      PropertyManager.setRuntimeProperty("xml.parser.class", "weblogic.auddi.xml.UDDIParser");
      String var1 = "com.sun.net.ssl.internal.www.protocol";
      String var2 = System.getProperty("java.protocol.handler.pkgs");
      if (var2 == null) {
         var2 = var1;
      } else if (var2.indexOf(var1) == -1) {
         var2 = var1 + "|" + var2;
      }

      System.setProperty("java.protocol.handler.pkgs", var2);
   }

   public UDDIProxy() {
      this.initialize();
   }

   public static void setTrustedKeyStore(String var0) {
      System.setProperty("javax.net.ssl.trustStore", var0);
   }

   public void setDebugLevel(int var1) {
      Logger.instance().setTraceLevel(var1);
   }

   public void setPublishURL(String var1) throws MalformedURLException {
      try {
         Logger.trace("+UDDIProxy.setPublishURL()");
         this.m_publishURL = new URL(var1);
      } finally {
         Logger.trace("-UDDIProxy.setPublishURL()");
      }

   }

   public void setInquiryURL(String var1) throws MalformedURLException {
      try {
         Logger.trace("+UDDIProxy.setInquiryURL()");
         this.m_inquiryURL = new URL(var1);
      } finally {
         Logger.trace("-UDDIProxy.setInquiryURL()");
      }

   }

   public void setProxy(String var1, int var2) {
      this.m_proxyHost = var1;
      this.m_proxyPort = var2;
   }

   public void setCredential(String var1, String var2) {
      Logger.trace("+UDDIProxy.setCredential()");
      this.m_username = var1;
      this.m_password = var2;
      this.m_authinfo = null;
      Logger.trace("-UDDIProxy.setCredential()");
   }

   public void authenticate() throws UDDIException {
      try {
         Logger.trace("+UDDIProxy.authenticate()");
         GetAuthTokenRequest var1 = new GetAuthTokenRequest(this.m_username, this.m_password);
         AuthTokenResponse var2 = (AuthTokenResponse)this.execute(var1);
         this.m_authinfo = var2.getAuthInfo();
      } finally {
         Logger.trace("-UDDIProxy.authenticate()");
      }

   }

   private void setAuthInfo(UDDIPublishRequest var1) throws UDDIException {
      if (this.m_authinfo == null) {
         this.authenticate();
      }

      var1.setAuthInfo(this.m_authinfo);
   }

   public UDDIResponse execute(UDDIRequest var1) throws UDDIException {
      UDDIResponse var3;
      try {
         Logger.trace("+UDDIProxy.execute(UDDIRequest)");
         URL var2 = this.m_inquiryURL;
         if (var1 instanceof UDDIPublishRequest || var1 instanceof GetAuthTokenRequest) {
            var2 = this.m_publishURL;
         }

         var3 = this.execute(var1, var2);
      } finally {
         Logger.trace("-UDDIProxy.execute(UDDIRequest)");
      }

      return var3;
   }

   public UDDIResponse execute(UDDIRequest var1, String var2) throws MalformedURLException, UDDIException {
      UDDIResponse var5;
      try {
         Logger.trace("+UDDIProxy.execute(UDDIRequest, String)");
         URL var3 = new URL(var2);
         UDDIResponse var4 = this.execute(var1, var3);
         var5 = var4;
      } finally {
         Logger.trace("-UDDIProxy.execute(UDDIRequest, String)");
      }

      return var5;
   }

   private UDDIResponse execute(UDDIRequest var1, URL var2) throws UDDIException {
      UDDIResponse var5;
      try {
         Logger.trace("+UDDIProxy.execute(UDDIRequest, URL)");
         if (var1 instanceof UDDIPublishRequest) {
            UDDIPublishRequest var3 = (UDDIPublishRequest)var1;
            if (var3.getAuthInfo() == null || var3.getAuthInfo().getValue() == null || var3.getAuthInfo().getValue().equals("") || var3.getAuthInfo().getValue().equals("$$auth_token$$")) {
               this.setAuthInfo(var3);
            }
         }

         String var11 = var1.toSOAP();
         UDDIResponse var4 = null;

         try {
            var4 = this.sendRequest(var11, var2);
         } catch (AuthTokenExpiredException var9) {
            if (this.m_username == null) {
               throw var9;
            }

            this.m_authinfo = null;
            this.setAuthInfo((UDDIPublishRequest)var1);
            var4 = this.sendRequest(var11, var2);
         }

         var5 = var4;
      } finally {
         Logger.trace("-UDDIProxy.execute(UDDIRequest, URL)");
      }

      return var5;
   }

   private UDDIResponse sendRequest(String var1, URL var2) throws UDDIException {
      try {
         String var3 = null;
         Logger.debug("UDDIProxy attempting to send\n[" + var1 + "]\n");
         if (this.m_proxyHost == null) {
            var3 = SOAPClient.sendUDDIRequest(var2, var1);
         } else {
            var3 = SOAPClient.sendUDDIRequest(var2, this.m_proxyHost, this.m_proxyPort, var1);
         }

         Logger.debug("UDDIProxy received in return the following RAW response:\n[" + var3 + "]\n");
         Object var4 = UDDISOAPWrapper.createResponseFromSOAP(var3);
         this.handleErrorDispositions((UDDIResponse)var4);
         if (var4 instanceof FaultResponse) {
            Logger.debug("A FaultResponse was received");
            FaultResponse var5 = (FaultResponse)var4;
            DispositionReportResponse var6 = var5.getDisposition();
            this.handleErrorDispositions(var6);
            var4 = var6;
         }

         return (UDDIResponse)var4;
      } catch (IOException var7) {
         throw new FatalErrorException("Error sending request", var7);
      }
   }

   private void handleErrorDispositions(UDDIResponse var1) throws UDDIException {
      if (var1 instanceof ErrorDispositionReportResponse) {
         Logger.debug("An ErrorDispositionReportResponse was received");
         ErrorDispositionReportResponse var2 = (ErrorDispositionReportResponse)var1;
         UDDIException var3 = UDDIExceptionMapper.toException(var2);
         throw var3;
      }
   }

   public UDDIResponse processRequestFile(String var1) throws UDDIException {
      UDDIResponse var4;
      try {
         Logger.trace("+UDDIProxy.processRequestFile()");
         String var2 = Util.getFileContent(var1);
         UDDIResponse var3 = this.processRequestString(var2);
         var4 = var3;
      } catch (IOException var8) {
         throw new FatalErrorException("Error reading request from file: " + var1, var8);
      } finally {
         Logger.trace("-UDDIProxy.processRequestFile()");
      }

      return var4;
   }

   public UDDIResponse processRequestString(String var1) throws UDDIException {
      UDDIResponse var4;
      try {
         Logger.trace("+UDDIProxy.processRequestString()");
         UDDIRequest var2 = UDDISOAPWrapper.createRequest(var1);
         UDDIResponse var3 = this.execute(var2);
         var4 = var3;
      } finally {
         Logger.trace("-UDDIProxy.processRequestString()");
      }

      return var4;
   }

   public boolean isValidRequest(UDDIRequest var1) {
      if (var1 != null) {
         try {
            String var2 = var1.toSOAP();
            ParserWrapper.parseRequest(var2, true);
            return true;
         } catch (SchemaException var3) {
            Logger.error((Throwable)var3);
            return false;
         } catch (UDDIException var4) {
            Logger.error((Throwable)var4);
            return false;
         }
      } else {
         return false;
      }
   }

   private static void usage() {
      System.err.println("Usage:  java " + UDDIProxy.class.getName() + " <server_url> <filename_for_uddi_request> [<userid> <password>]");
      System.err.println("and set proxy information, if any, by passing -Dauddi.proxyHost=<proxyHost> -Dauddi.proxyPort=<proxyPort>");
      System.exit(1);
   }

   public static void main(String[] var0) throws Exception {
      if (var0.length < 1 || var0.length > 4) {
         usage();
      }

      UDDIProxy var1 = new UDDIProxy();
      String var2 = System.getProperty("logger.verbosity");
      if (var2 != null) {
         if (var2.equalsIgnoreCase("trace")) {
            var1.setDebugLevel(4);
         } else if (var2.equalsIgnoreCase("debug")) {
            var1.setDebugLevel(3);
         } else if (var2.equalsIgnoreCase("info")) {
            var1.setDebugLevel(2);
         } else if (var2.equalsIgnoreCase("error")) {
            var1.setDebugLevel(0);
         } else {
            var1.setDebugLevel(0);
         }
      }

      switch (var0.length) {
         case 3:
            var1.setCredential(var0[2], "");
            break;
         case 4:
            var1.setCredential(var0[2], var0[3]);
      }

      String var3 = System.getProperty("auddi.proxyHost");
      String var4 = System.getProperty("auddi.proxyPort");
      if (var3 != null && var4 != null) {
         int var5 = -1;

         try {
            var5 = Integer.parseInt(var4);
         } catch (NumberFormatException var8) {
            var8.printStackTrace();
            System.err.println("\nThe <auddi.proxyPort> value [" + var4 + "] is not a valid number.");
            System.exit(1);
         }

         var1.setProxy(var3, var5);
         Logger.info("Proxy values set to host " + var3 + " and port " + var5);
      } else {
         Logger.debug("Proxy values not set");
      }

      var1.setInquiryURL(var0[0]);
      var1.setPublishURL(var0[0]);
      Object var9 = null;

      try {
         var9 = var1.processRequestFile(var0[1]);
      } catch (UDDIException var7) {
         var9 = UDDIExceptionMapper.toDispositionReport(var7);
      }

      String var6 = null;
      if (var9 != null) {
         var6 = ((UDDIResponse)var9).toXML();
      }

      System.out.println("Response:\n\n\n" + var6 + "\n");
   }
}
