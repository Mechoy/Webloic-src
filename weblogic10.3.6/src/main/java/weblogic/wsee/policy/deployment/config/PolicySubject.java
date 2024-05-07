package weblogic.wsee.policy.deployment.config;

public class PolicySubject {
   private static String WEB = "/WEBs/";
   private static String EJB = "/EJBs/";
   private static String SERVICE = "/WLSWEBSERVICEs/";
   private static String CLIENT = "/WLSWEBSERVICECLIENTs/";
   private static String PORT = "/PORTs/";
   private static String OPERATION = "/OPERATIONs/";
   private static String DELIMITER = "/";
   private String policySubject = null;
   private Boolean isClient = false;
   private Boolean isEJB = false;
   private Boolean isOperation = false;
   private String app = null;
   private String module = null;
   private String service = null;
   private String serviceRef = null;
   private String ejb = null;
   private String subject = null;
   private String operation = null;
   private String port = null;

   public PolicySubject(String var1) {
      this.policySubject = var1;
      if (var1 != null && var1.length() != 0) {
         this.init();
      } else {
         throw new IllegalArgumentException("Invalid policy subject pattern: " + var1);
      }
   }

   private void init() {
      int var1 = this.getNextIndex(0, WEB, EJB);
      int var2 = this.getNextIndex(var1, SERVICE, CLIENT);
      int var3 = this.getNextIndex(var2, PORT, OPERATION);
      this.isEJB = this.policySubject.contains(EJB);
      String var4 = this.isEJB ? EJB : WEB;
      this.isClient = this.policySubject.contains(CLIENT);
      String var5 = this.isClient ? CLIENT : SERVICE;
      this.isOperation = this.policySubject.contains(OPERATION);
      String var6 = PORT;
      this.app = this.getApp(this.policySubject.substring(0, var1));
      this.module = this.policySubject.substring(var1 + var4.length(), var2);
      this.service = this.policySubject.substring(var2 + var5.length(), var3);
      this.subject = this.policySubject.substring(var3 + var6.length());
      if (this.isOperation) {
         this.initOperation();
      } else {
         this.port = this.subject;
      }

      if (this.isClient) {
         this.initClient();
      }

   }

   private int getNextIndex(int var1, String var2, String var3) {
      int var4 = this.policySubject.indexOf(var2, var1);
      if (var4 == -1) {
         var4 = this.policySubject.indexOf(var3, var1);
         if (var4 == -1) {
            throw new IllegalArgumentException("Invalid policy subject pattern: " + this.policySubject);
         }
      }

      return var4;
   }

   private String getApp(String var1) {
      String[] var2 = var1.split(DELIMITER);
      if (var2 != null && var2.length == 4) {
         return var2[3];
      } else {
         throw new IllegalArgumentException("Invalid policy subject pattern: " + this.policySubject);
      }
   }

   private void initClient() {
      if (this.isEJB) {
         int var1 = this.service.indexOf(DELIMITER);
         if (var1 == -1) {
            throw new IllegalArgumentException("Invalid policy subject pattern: " + this.policySubject);
         }

         this.ejb = this.service.substring(0, var1);
         this.serviceRef = this.service.substring(var1 + DELIMITER.length());
      } else {
         this.serviceRef = this.service;
      }

      this.service = null;
   }

   private void initOperation() {
      int var1 = this.subject.indexOf(OPERATION);
      this.port = this.subject.substring(0, var1);
      this.operation = this.subject.substring(var1 + OPERATION.length());
      this.subject = this.operation;
   }

   public String getAppName() {
      return this.app;
   }

   public String getModuleName() {
      return this.module;
   }

   public String getServiceName() {
      return this.isClient ? null : this.service;
   }

   public String getServiceRefName() {
      return this.isClient ? this.serviceRef : null;
   }

   public String getEJBName() {
      return this.isClient ? this.ejb : null;
   }

   public String getSubjectName() {
      return this.subject;
   }

   public String getPortName() {
      return this.port;
   }

   public String getOperationName() {
      return this.operation;
   }

   public boolean isOperationType() {
      return this.isOperation;
   }

   public boolean isClientType() {
      return this.isClient;
   }
}
