package com.therainbowville.minegasm.client;

//import io.github.blackspherefollower.buttplug4j.client.ButtplugClientDevice;
//import io.github.blackspherefollower.buttplug4j.connectors.jetty.websocket.client.ButtplugClientWSClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.util.List;
import java.util.Objects;

public class ToyController {
    private static final Logger LOGGER = LogManager.getLogger();
    //private static final ButtplugClientWSClient client = new ButtplugClientWSClient("Minegasm");
    //private static ButtplugClientDevice device = null;
    private static boolean shutDownHookAdded = false;
    public static String lastErrorMessage = "";
    public static boolean isConnected = false;
    public static double currentVibrationLevel = 0;

    public static boolean connectDevice() {
        /*try {
            client.disconnect();
            client.connect(new URI("ws://localhost:12345/buttplug"));

            device = null;
            client.startScanning();

            Thread.sleep(2000);
            client.requestDeviceList();

            LOGGER.info("Enumerating devices...");

            List<ButtplugClientDevice> devices = client.getDevices();

            int nDevices = devices.size();
            LOGGER.info(nDevices);

            if (nDevices < 1) {
                lastErrorMessage = "No device found";
            }

            for (ButtplugClientDevice dev : devices) {
                if (dev.getScalarVibrateCount() != 0) {
                    LOGGER.info(dev.getDisplayName());
                    device = dev;
                    try {
                        device.sendScalarVibrateCmd(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }

            if (Objects.nonNull(device) && !shutDownHookAdded) {
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    try {
                        LOGGER.info("Disconnecting devices...");
                        client.stopAllDevices();
                        client.disconnect();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }));

                shutDownHookAdded = true;
            }

            if (nDevices > 0) {
                isConnected = true;
            }
        } catch (Exception e) {
            lastErrorMessage = e.getMessage();
            e.printStackTrace();
        }

        return Objects.nonNull(device);*/
        return false;
    }

    public static void setVibrationLevel(double level) {
        /*if (Objects.isNull(device)) return;

        try {
            device.sendScalarVibrateCmd(level);
            currentVibrationLevel = level;
        } catch (Exception e) {
            e.printStackTrace();
       }*/
    }

    public static String getDeviceName() {
        //return (Objects.nonNull(device)) ? device.getDisplayName() : "<none>";
        return "";
    }

    public static long getDeviceId() {
        //return (Objects.nonNull(device)) ? device.getDeviceIndex() : -1;
        return 0;
    }

    public static String getLastErrorMessage() {
        return lastErrorMessage;
    }
}
