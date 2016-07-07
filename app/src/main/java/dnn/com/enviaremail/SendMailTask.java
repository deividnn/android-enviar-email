package dnn.com.enviaremail;

/**
 * Created by criare on 06/07/2016.
 */

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class SendMailTask extends AsyncTask {

    private ProgressDialog statusDialog;
    private Activity sendMailActivity;
    private String retorno;

    public SendMailTask(Activity activity) {
        sendMailActivity = activity;

    }

    protected void onPreExecute() {
        statusDialog = new ProgressDialog(sendMailActivity);
        statusDialog.setMessage("Getting ready...");
        statusDialog.setIndeterminate(false);
        statusDialog.setCancelable(false);
        statusDialog.show();
    }

    @Override
    protected Object doInBackground(Object... args) {
        try {
            Log.i("SendMailTask", "About to instantiate GMail...");
            publishProgress("Processando dados....");
            Mail androidEmail = new Mail(args[0].toString(),
                    args[1].toString(), (List) args[2], args[3].toString(),
                    args[4].toString(),
                    args[5].toString());
            publishProgress("Preparando email....");
            androidEmail.createEmailMessage();
            publishProgress("Enviando email....");
            Mail.Retorno r = androidEmail.sendEmail(sendMailActivity);
           if( r.isOk()) {
               publishProgress("Email enviado.");
               Log.i("SendMailTask", "Mail Sent.");
               retorno = "email enviado com sucesso";
           }else{
               retorno = "erro ao enviar email "+r.getErro();
           }
        } catch (Exception e) {
            publishProgress(e.getMessage());
            Log.e("SendMailTask", e.getMessage(), e);
            retorno = "erro ao enviar email "+e.toString();

        }
        return null;
    }

    @Override
    public void onProgressUpdate(Object... values) {
        statusDialog.setMessage(values[0].toString());

    }

    @Override
    public void onPostExecute(Object result) {
        statusDialog.dismiss();
        Toast.makeText(sendMailActivity,retorno,Toast.LENGTH_LONG).show();
    }

}