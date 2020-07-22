package http.server.util;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static http.server.util.ConcurrencyUtil.OsType.MAC;
import static http.server.util.ConcurrencyUtil.OsType.UNIX;
import static http.server.util.ConcurrencyUtil.OsType.WINDOWS;

//todo: stolen code. refactor this mess
public class ConcurrencyUtil {

    @SneakyThrows
    public static int getNcores() {
        OsType osType = getOsType();
        Process process = getProcess(osType);

        String line = getOutputLine(process);

        if (osType == MAC)
            return line.length() > 0 ? Integer.parseInt(line) : 0;

        if (osType == WINDOWS && line.contains("NumberOfCores"))
            return Integer.parseInt(line.split("=")[1]);

        if (osType == UNIX) {
            int numberOfCores = 0;
            int sockets = 0;
            if (line.contains("Core(s) per socket:")) {
                numberOfCores = Integer.parseInt(line.split("\\s+")[line.split("\\s+").length - 1]);
            }
            if (line.contains("Socket(s):")) {
                sockets = Integer.parseInt(line.split("\\s+")[line.split("\\s+").length - 1]);
            }
            return numberOfCores * sockets;
        }

        throw new IllegalStateException("something went wrong");
    }

    @SneakyThrows
    private static String getOutputLine(Process process) {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));

        String lastLine = null;
        String curLine;
        while (( curLine = reader.readLine()) != null) {
            lastLine=curLine;
        }
        return lastLine;
    }

    @SneakyThrows
    private static Process getProcess(OsType osType) {
        if (osType == MAC) {
            String[] cmd = {"/bin/sh", "-c", getCommandText(osType)};
            return Runtime.getRuntime().exec(cmd);
        }
        return Runtime.getRuntime().exec(getCommandText(osType));
    }


    private static String getCommandText(OsType osType) {
        switch (osType) {
            case MAC:
                return "sysctl -n machdep.cpu.core_count";
            case UNIX:
                return "lscpu";
            case WINDOWS:
                return "cmd /C WMIC CPU Get /Format:List";
        }
        throw new IllegalArgumentException("unsupported os");
    }

    public enum OsType {
        WINDOWS, MAC, UNIX
    }

    private static OsType getOsType() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) return WINDOWS;
        if (os.contains("mac")) return MAC;
        if (os.contains("nix")) return UNIX;
        throw new UnsupportedOperationException("OS is not supported");
    }
}

