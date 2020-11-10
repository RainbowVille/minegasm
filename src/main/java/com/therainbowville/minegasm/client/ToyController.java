package com.therainbowville.minegasm.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.metafetish.buttplug.client.ButtplugClientDevice;
import org.metafetish.buttplug.client.ButtplugWSClient;
import org.metafetish.buttplug.core.Messages.SingleMotorVibrateCmd;

import java.net.URI;
import java.util.List;
import java.util.Objects;

public class ToyController {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final ButtplugWSClient client = new ButtplugWSClient("Minegasm");
    private static ButtplugClientDevice device = null;
    private static int[] state = new int[24000];
    private static int currentStateIndex = 0;
    private static int currentState = 0;
    private static String lastErrorMessage = "";
    private static boolean shutDownHookAdded = false;

    public static boolean connectDevice() {
        try {
            device = null;
            client.Disconnect();
            client.Connect(new URI("ws://localhost:12345/buttplug"), true);
            client.startScanning();

            Thread.sleep(5000);
            client.requestDeviceList();

            LOGGER.info("Enumerating devices...");

            List<ButtplugClientDevice> devices = client.getDevices();

            int nDevices = devices.size();
            LOGGER.info(nDevices);

            if (nDevices < 1) {
                lastErrorMessage = "No device found";
            }

            for (ButtplugClientDevice dev : devices) {
                if (dev.allowedMessages.contains(SingleMotorVibrateCmd.class.getSimpleName())) {
                    LOGGER.info(dev.name);
                    device = dev;
                    try {
                        client.sendDeviceMessage(device, new SingleMotorVibrateCmd(device.index, 0, client.getNextMsgId()));
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
                        client.Disconnect();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }));

                shutDownHookAdded = true;
            }

        } catch (Exception e) {
            lastErrorMessage = e.getMessage();
            e.printStackTrace();
        }

        return Objects.nonNull(device);
    }

    public static void vibrate() {
        try {
            client.sendDeviceMessage(device, new SingleMotorVibrateCmd(device.index, (1.0 - Math.random()), client.getNextMsgId()));
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            try {
                                client.sendDeviceMessage(device, new SingleMotorVibrateCmd(device.index, 0, client.getNextMsgId()));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    Math.round(100 + 5000 * (1.0 - Math.random()))
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getDeviceName() {
        return (Objects.nonNull(device)) ? device.name : "<none>";
    }

    public static long getDeviceId() {
        return (Objects.nonNull(device)) ? device.index : -1;
    }

    public static String getLastErrorMessage() {
        return lastErrorMessage;
    }
}
