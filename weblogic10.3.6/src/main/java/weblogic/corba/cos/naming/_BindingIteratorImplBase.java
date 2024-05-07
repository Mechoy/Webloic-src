package weblogic.corba.cos.naming;

import java.util.Hashtable;
import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.InvokeHandler;
import org.omg.CORBA.portable.ObjectImpl;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.ResponseHandler;
import org.omg.CosNaming.BindingHelper;
import org.omg.CosNaming.BindingHolder;
import org.omg.CosNaming.BindingListHelper;
import org.omg.CosNaming.BindingListHolder;

public abstract class _BindingIteratorImplBase extends ObjectImpl implements BindingIterator, InvokeHandler {
   private static Hashtable _methods = new Hashtable();
   private static String[] __ids;

   public OutputStream _invoke(String var1, InputStream var2, ResponseHandler var3) {
      OutputStream var4 = null;
      Integer var5 = (Integer)_methods.get(var1);
      if (var5 == null) {
         throw new BAD_OPERATION(0, CompletionStatus.COMPLETED_MAYBE);
      } else {
         switch (var5) {
            case 0:
               BindingHolder var9 = new BindingHolder();
               boolean var10 = false;
               var10 = this.next_one(var9);
               var4 = var3.createReply();
               var4.write_boolean(var10);
               BindingHelper.write(var4, var9.value);
               break;
            case 1:
               int var6 = var2.read_ulong();
               BindingListHolder var7 = new BindingListHolder();
               boolean var8 = false;
               var8 = this.next_n(var6, var7);
               var4 = var3.createReply();
               var4.write_boolean(var8);
               BindingListHelper.write(var4, var7.value);
               break;
            case 2:
               this.destroy();
               var4 = var3.createReply();
               break;
            default:
               throw new BAD_OPERATION(0, CompletionStatus.COMPLETED_MAYBE);
         }

         return var4;
      }
   }

   public String[] _ids() {
      return (String[])((String[])__ids.clone());
   }

   static {
      _methods.put("next_one", new Integer(0));
      _methods.put("next_n", new Integer(1));
      _methods.put("destroy", new Integer(2));
      __ids = new String[]{"IDL:weblogic/corba/cos/naming/BindingIterator:1.0", "IDL:omg.org/CosNaming/BindingIterator:1.0"};
   }
}
