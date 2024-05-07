package weblogic.uddi.client.structures.exception;

import weblogic.uddi.client.structures.response.DispositionReport;

public class UDDIException extends Exception {
   private String faultCode = null;
   private String faultString = null;
   private String faultActor = null;
   private DispositionReport dispositionReport = null;

   public String getFaultCode() {
      return this.faultCode;
   }

   public void setFaultCode(String var1) {
      this.faultCode = var1;
   }

   public String getFaultString() {
      return this.faultString;
   }

   public void setFaultString(String var1) {
      this.faultString = var1;
   }

   public String getFaultActor() {
      return this.faultActor;
   }

   public void setFaultActor(String var1) {
      this.faultActor = var1;
   }

   public DispositionReport getDispositionReport() {
      return this.dispositionReport;
   }

   public void setDispositionReport(DispositionReport var1) {
      this.dispositionReport = var1;
   }
}
