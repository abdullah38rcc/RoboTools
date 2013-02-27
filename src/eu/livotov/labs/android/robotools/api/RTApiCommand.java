package eu.livotov.labs.android.robotools.api;

import android.os.AsyncTask;
import eu.livotov.labs.android.robotools.net.RTPostParameter;

import java.util.List;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 29.01.13
 */
public abstract class RTApiCommand
{

    private Class resultClass;

    public RTApiCommand(Class resultClass) //todo: to be cleaned up once Oracle fix #5098163 since 2004 ! %=/
    {
        this.resultClass = resultClass;
    }

    public abstract String buildRequestUri();

    public abstract void buildRequestParameters(List<RTPostParameter> params);

    public abstract void buildRequestBody(StringBuffer body);

    public abstract RTApiRequestType getRequestType();

    public abstract RTApiClient getClient();

    public RTApiCommandResult parseServerResponseData(final String data) throws RTApiError
    {
        try
        {
            RTApiCommandResult response = (RTApiCommandResult) resultClass.newInstance();
            response.loadResponseData(this, data);
            return response;
        } catch (RTApiError bawError)
        {
            throw bawError;
        } catch (Throwable err)
        {
            throw new RTApiError(err);
        }
    }

    public RTApiCommandResult execute()
    {
        return getClient().execute(this);
    }

    public void execute(final RTApiCommandAsyncCallback callback)
    {
        new AsyncTask<RTApiCommandAsyncCallback, String, Object>()
        {
            protected void onPreExecute()
            {
                onPreAsyncExecute();
                callback.onBeforeCommandStart(RTApiCommand.this);
            }

            protected void onPostExecute(final Object result)
            {
                if (result instanceof RTApiCommandResult)
                {
                    onPostAsyncExecute((RTApiCommandResult) result);
                    callback.onCommandCompleted((RTApiCommandResult) result);
                } else
                {
                    if (result instanceof RTApiError)
                    {
                        onAsyncExecutionError((RTApiError) result);
                        callback.onCommandFailed((RTApiError) result);
                    } else
                    {
                        final RTApiError error = new RTApiError((Throwable)result);
                        onAsyncExecutionError(error);
                        callback.onCommandFailed(error);
                    }
                }
            }

            protected Object doInBackground(final RTApiCommandAsyncCallback... callbacks)
            {
                try
                {
                    return RTApiCommand.this.execute();
                } catch (Throwable err)
                {
                    return err;
                }
            }
        }.execute(callback);
    }

    public void onPreAsyncExecute()
    {

    }

    public void onPostAsyncExecute(RTApiCommandResult result)
    {

    }

    public void onAsyncExecutionError(RTApiError error)
    {

    }

}
