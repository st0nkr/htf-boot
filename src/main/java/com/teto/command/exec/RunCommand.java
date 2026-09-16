package com.teto.command.exec;

import com.teto.IDuration;
import com.teto.IFile;
import com.teto.IStream;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import org.apache.commons.exec.*;
import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class RunCommand extends AbstractCommand<RunCommandResponse> implements IDuration , IStream,IFile {
    public int TIMEDOUT = -3;
    public int FAILED = -1;
    private String cmd;
    private Integer exitCode = 0;
    private Duration waitTime = days(3);
    private DefaultExecuteResultHandler resultHandler;
    private ExecuteWatchdog watchdog;

    public RunCommand(String cmd, Integer code, Duration waitTime) {
        this.cmd = cmd;
        this.exitCode = code;
        this.waitTime = waitTime;
    }


    @Override
    public Optional<RunCommandResponse> apply(Context ctx) {
        final AtomicInteger result = new AtomicInteger(0);
        final ByteArrayOutputStream stdout = byteArrayOutputStream(ctx);
        final ByteArrayOutputStream stderr = new ByteArrayOutputStream();
        final RunCommandResponse rsp = new RunCommandResponse();
        int exitValue = -1;
        CommandLine cmdLine;
        final AtomicReference<Long> pid = new AtomicReference<>(-1L);
        try {
            PumpStreamHandler psh = new PumpStreamHandler(stdout, stderr);
            if (resultHandler == null) {
                resultHandler = new DefaultExecuteResultHandler() {

                    @Override
                    public void onProcessComplete(final int exitValue) {
                        super.onProcessComplete(exitValue);
                        result.set(exitValue);
                    }

                    @Override
                    public void onProcessFailed(final ExecuteException e) {
                        super.onProcessFailed(e);
                        if (watchdog != null && watchdog.killedProcess()) {
                            result.set(TIMEDOUT);
                        } else {
                            result.set(FAILED);
                        }
                    }

                };
            }
            cmdLine = CommandLine.parse(cmd);
            //DefaultExecutor executor = DefaultExecutor.builder().get();
            DefaultExecutor executor = new DefaultExecutor() {
                public Process launch(final CommandLine command, final Map<String, String> env, final File dir) throws IOException {
                    Process process = super.launch(command, env, dir);
                    pid.set(process.pid());
                    // Do stuff with the PID here...
                    return process;
                }
            };
            if (exitCode != null) {
                executor.setExitValue(exitCode);
            }

            if (waitTime != null) {
                watchdog = ExecuteWatchdog
                        .builder()
                        .setTimeout(waitTime).get();
                executor.setWatchdog(watchdog);
            }
            executor.setStreamHandler(psh);

            executor.execute(cmdLine, resultHandler);
            resultHandler.waitFor();
            exitValue = resultHandler.getExitValue();
            rsp.setExitCode(exitValue);
            rsp.setOutput(stdout.toString());

        } catch(Error e) {

            rsp.setOutput(stdout.toString());
            rsp.setError(stderr.toString());
            rsp.setExitCode(exitCode);
        }
        catch(ExecuteException ee) {
            rsp.setOutput(stdout.toString());
            rsp.setError(stderr.toString());
            rsp.setExitCode(exitCode);
        }
        catch (Exception e) {
            rsp.setOutput(stdout.toString());
            rsp.setError(stderr.toString());
            rsp.setExitCode(exitCode);
        } finally {
            String so = stdout.toString();
            rsp.setOutput(stdout.toString());
            rsp.setError(stderr.toString());
            if(so == null || so.strip().length() < 1) {
                logger(this).warn("Appears as though stderr used as stdout by author :( ");
                logger(this).warn("It could also mean proxychains failed...beware");
                rsp.setOutput(stderr.toString());
            }

            rsp.setExitCode(exitCode);
            try {
                stdout.close();
                stderr.close();
            } catch(Exception e) {

            }
        }

        return optional(rsp);
    }

    private ByteArrayOutputStream byteArrayOutputStream(Context ctx) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream() {
            public void write(final byte[] b, final int off, final int len) {
                super.write(b,off,len);
            }

            public synchronized int write(final InputStream in) throws IOException {
                return super.write(in);
            }

            public synchronized void write(final int b) {
                super.write(b);
            }
            public synchronized void writeTo(final OutputStream out) throws IOException {
                super.writeTo(out);
            }

        };
        return bos;
    }

}