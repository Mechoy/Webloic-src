package weblogic.wtc.jatmi;

import com.bea.core.jatmi.common.ntrace;
import com.bea.core.jatmi.intf.TCAuthenticatedUser;
import com.bea.core.jatmi.intf.TCTask;
import java.io.DataOutputStream;
import java.io.IOException;

public class TPServiceRequest implements TCTask {
   private InvokeSvc how_to_do_them;
   private gwatmi mysession;
   private int protocol;
   private DataOutputStream output_stream;
   private tfmh myServiceRequest;
   String myLocalDomain;
   private BetaFeatures beta;
   TCAuthenticatedUser mySubject;
   private String myName;

   public TPServiceRequest(gwatmi var1, int var2, String var3, DataOutputStream var4, InvokeSvc var5, tfmh var6, BetaFeatures var7) throws TPException {
      if (var1 != null && var4 != null && var5 != null && var6 != null && var3 != null) {
         this.mysession = var1;
         this.protocol = var2;
         this.myLocalDomain = var3;
         this.output_stream = var4;
         this.how_to_do_them = var5;
         this.myServiceRequest = var6;
         this.beta = var7;
      } else {
         throw new TPException(4, "Invalid null parameter/" + var1 + "/" + var4 + "/" + var5 + "/" + var6 + "/" + var3);
      }
   }

   public int execute() {
      boolean var1 = ntrace.isTraceEnabled(4);
      if (var1) {
         ntrace.doTrace("[/TPServiceRequest/execute/");
      }

      int var9 = 0;
      int var10 = 0;
      int var11 = 0;
      dreqid var12 = null;
      int var14 = 0;
      tfmh var2 = this.myServiceRequest;
      if (var2.tdom == null) {
         if (var1) {
            ntrace.doTrace("]/TPServiceRequest/10/Unknown message format");
         }

         return 0;
      } else {
         TdomTcb var4 = (TdomTcb)var2.tdom.body;
         var12 = new dreqid(var4.get_reqid());
         int var13 = var4.get_convid();
         int var15 = var4.get_flag();
         if ((var15 & 4) != 0) {
            var14 |= 4;
         }

         if (var13 != -1) {
            var14 |= 1024;
            if ((var15 & 2048) != 0) {
               var14 |= 4096;
            } else {
               var14 |= 2048;
            }
         }

         TypedBuffer var5;
         if (var2.user == null) {
            var5 = null;
         } else {
            UserTcb var3 = (UserTcb)var2.user.body;
            var5 = var3.user_data;
         }

         if (var9 == 0) {
            if (this.how_to_do_them != null) {
               InvokeInfo var6 = new InvokeInfo(var4.get_service(), var5, var14, var2, var12, this.mysession.getUid(), var13);
               if (this.mySubject != null) {
                  var6.setTargetSubject(this.mySubject);
               }

               try {
                  if (var1) {
                     ntrace.doTrace("/servicethr/calling invoker/" + var6);
                  }

                  this.how_to_do_them.invoke(var6, this.mysession);
                  if (var1) {
                     ntrace.doTrace("/servicethr/invoker success/" + var6);
                  }

                  var9 = 0;
               } catch (TPException var21) {
                  if (var1) {
                     ntrace.doTrace("/servicethr/invoker threw TPException " + var21 + "/");
                  }

                  var9 = var21.gettperrno();
                  var11 = var21.gettpurcode();
                  var10 = var21.gettperrordetail();
               }
            } else {
               if (var1) {
                  ntrace.doTrace("/servicethr/no invoker/");
               }

               var9 = 6;
            }
         }

         if (var9 != 0) {
            if (var1) {
               ntrace.doTrace("/servicethr/sending failure/");
            }

            tfmh var7 = new tfmh(1);
            TdomTcb var8 = new TdomTcb(3, var12.reqid, 0, (String)null);
            var8.set_errdetail(var10);
            var8.set_tpurcode(var11);
            var8.set_diagnostic(var9);
            var7.tdom = new tcm((short)7, var8);
            synchronized(this.output_stream) {
               try {
                  if (this.protocol >= 15) {
                     var7.write_tfmh(this.output_stream, this.mysession.getCompressionThreshold());
                  } else {
                     var7.write_dom_65_tfmh(this.output_stream, this.myLocalDomain, this.protocol, this.mysession.getCompressionThreshold());
                  }

                  if (var1) {
                     ntrace.doTrace("/servicethr/failure sent/");
                  }
               } catch (IOException var19) {
                  if (var1) {
                     ntrace.doTrace("/servicethr/Could not send failure/" + var19);
                  }
               }
            }
         }

         if (var1) {
            ntrace.doTrace("]/TPServiceRequest/20/Success");
         }

         return 0;
      }
   }

   public void setTargetSubject(TCAuthenticatedUser var1) {
      boolean var2 = ntrace.isTraceEnabled(4);
      if (var2) {
         ntrace.doTrace("[/TPServiceRequest/setTargetSubject/");
      }

      this.mySubject = var1;
      if (var2) {
         ntrace.doTrace("]/TPServiceRequest/setTargetSubject/10");
      }

   }

   public void setTaskName(String var1) {
      this.myName = new String("TPServiceRequest$" + var1);
   }

   public String getTaskName() {
      return this.myName == null ? "TPServiceRequest$unknown" : this.myName;
   }
}
