package eu.livotov.labs.android.robotools.async;

import android.os.AsyncTask;
import android.os.Build;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 03/03/2013
 */
public abstract class RTAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result>
{
    private Throwable error;

    public RTAsyncTask()
    {
    }

    public abstract Result performExecutionThread(final Params... parameters) throws Exception;

    public abstract void onExecutionStarted();

    public abstract void onExecutionFinished(final Result result);

    public abstract void onExecutionFailed(final Throwable error);

    public abstract void onExecutionAborted();

    public void abort()
    {
        cancel(true);
    }

    protected Result doInBackground(final Params... parameters)
    {
        try
        {
            Thread.sleep(10);
            error = null;
            final Result res = performExecutionThread(parameters);

            if (isCancelled())
            {
                return null;
            } else
            {
                return res;
            }
        } catch (Throwable err)
        {
            error = err;
            return null;
        }
    }

    protected void onPreExecute()
    {
        onExecutionStarted();
    }

    protected void onCancelled(final Result result)
    {
        onExecutionAborted();
    }

    protected void onPostExecute(final Result result)
    {
        if (error != null)
        {
            onExecutionFailed(error);
        } else
        {
            onExecutionFinished(result);
        }
    }

    public AsyncTask<Params, Progress, Result> executeAsync(Params... params)
    {
        if (Build.VERSION.SDK_INT>=11)
        {
            return executeOnExecutor(THREAD_POOL_EXECUTOR, params);
        } else
        {
            return execute(params);
        }
    }
}
