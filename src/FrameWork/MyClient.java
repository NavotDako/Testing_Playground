package FrameWork;

import com.experitest.client.Client;
import com.experitest.client.InternalException;
import com.experitest.client.MobileListener;
import com.experitest.client.log.ILogger;

import java.io.*;
import java.util.Date;
import java.util.Map;


public class MyClient {

    private Client client;
    Map<String, Command> commandMap = null;

    String deviceOS = "??";
    private String deviceName;

    public MyClient(Client client) {
        this.client = client;
        deviceName = client.getDeviceProperty("device.name");
        deviceOS = client.getDeviceProperty("device.os");

    }

    public void Finish(String command, String detail, long before) {
//        long time = System.currentTimeMillis() - before;
//        String stringToWrite = "??";
//        if (!commandMap.containsKey(command)) {
//            commandMap.put(command, new Command(command));
//        }
//        if (commandMap.containsKey(command)) {
//            commandMap.get(command).timeList.add(time);
//            commandMap.get(command).totalTime += time;
//            commandMap.get(command).avgTime = commandMap.get(command).totalTime / commandMap.get(command).timeList.size();
//
//            stringToWrite = String.format("%-10s%-30s%-30s%-5s%-50s%-10s%-10s%-10s\n", deviceOS, deviceName.substring(deviceName.indexOf(":") + 1), command, commandMap.get(command).timeList.size(), detail, time, "AVG", commandMap.get(command).avgTime);
//
//        }
//
//        try {
//            Write(stringToWrite);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public void Write(String stringToWrite) throws IOException {

        String reportName = deviceName.substring(deviceName.indexOf(":") + 1);
        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("Reports/" + reportName + ".txt", true)));
        //System.out.println(stringToWrite);
        writer.append(stringToWrite);
        writer.close();

    }

    public void launch(String activityURL, boolean instrument, boolean stopIfRunning) {
        long before = System.currentTimeMillis();
        if (deviceOS.contains("android")) {
            client.launch(activityURL, false, stopIfRunning);
        } else {
            client.launch(activityURL, instrument, stopIfRunning);
        }
        Finish("Launch", activityURL, before);
        client.capture("Launch Capture");
        if (activityURL.contains("http") && client.isElementFound("native", "//*[@text='cancel']", 0)) {
            client.click("native", "//*[@text='cancel']", 0, 1);
        }


    }

    public void setTestStatus(boolean status, String message) {
        client.setTestStatus(status, message);
    }

    public boolean listSelect(String sendRest, String sendNavigation, int delay, String textToIdentify, String color, int rounds, String sendonfind) {
        return client.listSelect(sendRest, sendNavigation, delay, textToIdentify, color, rounds, sendonfind);
    }

    public void longClick(String zone, String element, int index, int clickCount, int x, int y) {
        client.longClick(zone, element, index, clickCount, x, y);
    }

    public void multiClick(String zone, String element, int index, int fingerIndex) {
        client.multiClick(zone, element, index, fingerIndex);
    }

    public void multiClickCoordinate(int x, int y, int fingerIndex) {
        client.multiClickCoordinate(x, y, fingerIndex);
    }

    public void multiClickOffset(String zone, String element, int index, int x, int y, int fingerIndex) {
        client.multiClickOffset(zone, element, index, x, y, fingerIndex);
    }

    public void multiSwipe(String direction, int offset, int time, int fingerIndex) {
        client.multiSwipe(direction, offset, time, fingerIndex);
    }

    public void multiTouchDown(String zone, String element, int index, int fingerIndex) {
        client.multiTouchDown(zone, element, index, fingerIndex);
    }

    public void multiTouchDownCoordinate(int x, int y, int fingerIndex) {
        client.multiTouchDownCoordinate(x, y, fingerIndex);
    }

    public void multiTouchMove(String zone, String element, int index, int fingerIndex) {
        client.multiTouchMove(zone, element, index, fingerIndex);
    }

    public void multiTouchMoveCoordinate(int x, int y, int fingerIndex) {
        client.multiTouchMoveCoordinate(x, y, fingerIndex);
    }

    public void multiTouchUp(int fingerIndex) {
        client.multiTouchUp(fingerIndex);
    }

    public void multiWait(int time, int fingerIndex) {
        client.multiWait(time, fingerIndex);
    }

    public void openDevice() {
        client.openDevice();
    }

    public int p2cx(int percentage) {
        return client.p2cx(percentage);
    }

    public int p2cy(int percentage) {
        return client.p2cy(percentage);
    }

    public void performMultiGesture() {
        client.performMultiGesture();
    }

    public boolean pinch(boolean inside, int x, int y, int radius, boolean horizontal) {
        return client.pinch(inside, x, y, radius, horizontal);
    }

    public int portForward(int localPort, int remotePort) {
        return client.portForward(localPort, remotePort);
    }

    public void pressWhileNotFound(String zone, String elementtoclick, String elementtofind, int timeout, int delay) {
        client.pressWhileNotFound(zone, elementtoclick, elementtofind, timeout, delay);
    }

    public void pressWhileNotFound(String zone, String elementtoclick, int elementtoclickindex, String elementtofind, int elementtofindindex, int timeout, int delay) {
        client.pressWhileNotFound(zone, elementtoclick, elementtoclickindex, elementtofind, elementtofindindex, timeout, delay);
    }

    public void click(String zone, String element, int index, int count) {
        // client.syncElements(500,2000);
        long before = System.currentTimeMillis();
        client.click(zone, element, index, count);
        Finish("Click", zone + " : " + element, before);

    }

    public void click(String zone, String element, int index, int clickCount, int x, int y) {
        client.click(zone, element, index, clickCount, x, y);
    }

    public void clickCoordinate(int x, int y, int clickCount) {
        client.clickCoordinate(x, y, clickCount);
    }

    public void clickIn(String zone, String searchElement, int index, String direction, String clickElementZone, String clickElement, int width, int height) {
        client.clickIn(zone, searchElement, index, direction, clickElementZone, clickElement, width, height);
    }

    public void clickIn(String zone, String searchElement, int index, String direction, String clickElementZone, String clickElement, int clickElementIndex, int width, int height, int clickCount) {
        client.clickIn(zone, searchElement, index, direction, clickElementZone, clickElement, clickElementIndex, width, height, clickCount);
    }

    public void closeDevice() {
        client.closeDevice();
    }

    public void closeDeviceReflection() {
        client.closeDeviceReflection();
    }

    public void closeKeyboard() {
        client.closeKeyboard();
    }

    public String setReporter(String type, String reportFolder, String reportName) {
        String reportPath = client.setReporter(type, reportFolder, reportName);
        // client.startLoggingDevice(reportPath);
        return reportFolder;
    }

    public void setShowImageAsLink(boolean showImageAsLink) {
        client.setShowImageAsLink(showImageAsLink);
    }

    public void setShowImageInReport(boolean showImageInReport) {
        client.setShowImageInReport(showImageInReport);
    }

    public void setShowPassImageInReport(boolean showPassImageInReport) {
        client.setShowPassImageInReport(showPassImageInReport);
    }

    public void setShowReport(boolean showReport) {
        client.setShowReport(showReport);
    }

    public void setSimCard(String simCardName) {
        client.setSimCard(simCardName);
    }

    public void setSpeed(String speed) {
        client.setSpeed(speed);
    }

    public void setWebAutoScroll(boolean autoScroll) {
        client.setWebAutoScroll(autoScroll);
    }

    public void shake() {
        client.shake();
    }

    public void simulateCapture(String picturePath) {
        client.simulateCapture(picturePath);
    }

    public void sleep(int time) {
        client.sleep(time);
    }

    public void startAudioPlay(String audioFile) {
        client.startAudioPlay(audioFile);
    }

    public void startAudioRecording(String audioFile) {
        client.startAudioRecording(audioFile);
    }

    public void startLoggingDevice(String path) {
        client.startLoggingDevice(path);
    }

    public void startMonitor(String packageName) {
        client.startMonitor(packageName);
    }

    public void startMultiGesture(String name) {
        client.startMultiGesture(name);
    }

    public void startStepsGroup(String caption) {
        client.startStepsGroup(caption);
    }

    public void startTransaction(String name) {
        client.startTransaction(name);
    }

    public void startVideoRecord() {
        client.startVideoRecord();
    }

    public void stopAudioPlay() {
        client.stopAudioPlay();
    }

    public void stopAudioRecording() {
        client.stopAudioRecording();
    }

    public String stopLoggingDevice() {
        return client.stopLoggingDevice();
    }

    public void stopStepsGroup() {
        client.stopStepsGroup();
    }

    public String stopVideoRecord() {
        return client.stopVideoRecord();
    }

    public void swipe(String direction, int offset, int time) {
        client.swipe(direction, offset, time);
    }

    public boolean swipeWhileNotFound(String direction, int offset, int swipeTime, String zone, String elementtofind, int elementtofindindex, int delay, int rounds, boolean click) {
        return client.swipeWhileNotFound(direction, offset, swipeTime, zone, elementtofind, elementtofindindex, delay, rounds, click);
    }

    public boolean sync(int silentTime, int sensitivity, int timeout) {
        return client.sync(silentTime, sensitivity, timeout);
    }

    public boolean syncElements(int silentTime, int timeout) {
        return client.syncElements(silentTime, timeout);
    }

    public void textFilter(String color, int sensitivity) {
        client.textFilter(color, sensitivity);
    }

    public void touchDown(String zone, String element, int index) {
        client.touchDown(zone, element, index);
    }

    public void touchDownCoordinate(int x, int y) {
        client.touchDownCoordinate(x, y);
    }

    public void touchMove(String zone, String element, int index) {
        client.touchMove(zone, element, index);
    }

    public void touchMoveCoordinate(int x, int y) {
        client.touchMoveCoordinate(x, y);
    }

    public void touchUp() {
        client.touchUp();
    }

    public boolean uninstall(String application) {
        boolean result = client.uninstall(application);
        client.sleep(1000);
        return result;
    }

    public boolean install(String app, boolean instrument, boolean stopIfRunning) {
        long before = System.currentTimeMillis();
        boolean result = client.install(app, instrument, stopIfRunning);
        Finish("Install", app, before);
        client.sleep(500);
        return result;
    }

    public boolean isElementBlank(String zone, String element, int index, int colorGroups) {
        return client.isElementBlank(zone, element, index, colorGroups);
    }

    public boolean isElementFound(String zone, String element) {
        return client.isElementFound(zone, element);
    }

    public boolean isElementFound(String zone, String element, int index) {
        return client.isElementFound(zone, element, index);
    }

    public boolean isFoundIn(String zone, String searchElement, int index, String direction, String elementFindZone, String elementToFind, int width, int height) {
        return client.isFoundIn(zone, searchElement, index, direction, elementFindZone, elementToFind, width, height);
    }

    public String generateReport(boolean b) {
        //     client.stopLoggingDevice();
        String result = client.generateReport(b);

        //reportTheCommands();
        return result;
    }

    public String generateReport(boolean releaseClient, String propFilePath) {
        return client.generateReport(releaseClient, propFilePath);
    }

    public String[] getAllValues(String zone, String element, String property) {
        long before = System.currentTimeMillis();
        String[] values = client.getAllValues(zone, element, property);
        Finish("getAllValues", zone + " : " + element, before);
        return values;
    }

    public String getAllZonesWithElement(String element) {
        return client.getAllZonesWithElement(element);
    }

    public int getAvailableAgentPort() {
        return client.getAvailableAgentPort();
    }

    public int getAvailableAgentPort(String featureName) {
        return client.getAvailableAgentPort(featureName);
    }

    public String getConnectedDevices() {
        return client.getConnectedDevices();
    }

    public int getCoordinateColor(int x, int y) {
        return client.getCoordinateColor(x, y);
    }

    public String getCounter(String counterName) {
        return client.getCounter(counterName);
    }

    public String getCurrentApplicationName() {
        return client.getCurrentApplicationName();
    }

    public int getDefaultTimeout() {
        return client.getDefaultTimeout();
    }

    public String getDeviceLog() {
        return client.getDeviceLog();
    }

    public String getDeviceProperty(String key) {
        return client.getDeviceProperty(key);
    }

    public String getDevicesInformation() {
        return client.getDevicesInformation();
    }

    public int getElementCount(String zone, String element) {
        long before = System.currentTimeMillis();
        int value = client.getElementCount(zone, element);
        Finish("getElementCount", zone + " : " + element, before);
        return value;
    }

    public int getElementCountIn(String zoneName, String elementSearch, int index, String direction, String elementCountZone, String elementCount, int width, int height) {
        return client.getElementCountIn(zoneName, elementSearch, index, direction, elementCountZone, elementCount, width, height);
    }

    public String getInstalledApplications() {
        return client.getInstalledApplications();
    }

    public String getMonitorsData(String cSVfilepath) {
        return client.getMonitorsData(cSVfilepath);
    }

    public String[] getPickerValues(String zone, String pickerElement, int index, int wheelIndex) {
        return client.getPickerValues(zone, pickerElement, index, wheelIndex);
    }

    public String getProperty(String property) {
        return client.getProperty(property);
    }

    public String getSimCard() {
        return client.getSimCard();
    }

    public String[] getSimCards(boolean readyToUse) {
        return client.getSimCards(readyToUse);
    }

    public String getTableCellText(String zone, String headerElement, int headerIndex, String rowElement, int rowIndex, int width, int height) {
        return client.getTableCellText(zone, headerElement, headerIndex, rowElement, rowIndex, width, height);
    }

    public String getText(String zone) {
        long before = System.currentTimeMillis();
        String value = client.getText(zone);
        Finish("getText", zone, before);
        return value;
    }

    public String getTextIn(String zone, String element, int index, String direction, int width, int height) {
        return client.getTextIn(zone, element, index, direction, width, height);
    }

    public String getTextIn(String zone, String element, int index, String textZone, String direction, int width, int height) {
        return client.getTextIn(zone, element, index, textZone, direction, width, height);
    }

    public String getTextIn(String zone, String element, int index, String textZone, String direction, int width, int height, int xOffset, int yOffset) {
        return client.getTextIn(zone, element, index, textZone, direction, width, height, xOffset, yOffset);
    }

    public String getVisualDump(String type) {
        long before = System.currentTimeMillis();
        String value = client.getVisualDump(type);
        Finish("getVisualDump", type, before);
        return value;
    }

    public void hybridClearCache(boolean clearCookies, boolean clearCache) throws InternalException {
        client.hybridClearCache(clearCookies, clearCache);
    }

    public String hybridGetHtml(String webViewLocator, int index) {
        return client.hybridGetHtml(webViewLocator, index);
    }

    public String hybridRunJavascript(String webViewLocator, int index, String script) {
        return client.hybridRunJavascript(webViewLocator, index, script);
    }

    public void hybridSelect(String webViewLocator, int index, String method, String value, String select) {
        client.hybridSelect(webViewLocator, index, method, value, select);
    }

    public void hybridWaitForPageLoad(int timeout) {
        client.hybridWaitForPageLoad(timeout);
    }

    public boolean install(String path, boolean sign) {
        return client.install(path, sign);
    }

    public void reportTheCommands() {

        for (Map.Entry<String, Command> entry : commandMap.entrySet()) {
            int sum;
            if (entry.getValue().timeList.size() > 0) {
                sum = 0;
                for (Long t : entry.getValue().timeList)
                    sum += t;
                System.out.println("---------------" + deviceName + " - " + entry.getValue().commandName + " AVG - " + sum / entry.getValue().timeList.size() + " " + entry.getValue().commandName + " count - " + entry.getValue().timeList.size() + "-----------------");
            }
        }

    }

    public boolean reboot(int timeOut) {
        System.out.println(Thread.currentThread().getName() + " " + deviceName + " Rebooting...");
        long before = System.currentTimeMillis();
        boolean result = client.reboot(timeOut);
        Finish("Reboot", "NoZone", before);
        return result;
    }

    public void releaseClient() {
        client.releaseClient();
    }

    public void releaseDevice(String deviceName, boolean releaseAgent, boolean removeFromDeviceList, boolean releaseFromCloud) {
        client.releaseDevice(deviceName, releaseAgent, removeFromDeviceList, releaseFromCloud);
    }

    public void report(String message, boolean status) {
        client.report(message, status);
    }

    public void report(String pathToImage, String message, boolean status) {
        client.report(pathToImage, message, status);
    }

    public void rightClick(String zone, String element, int index, int clickCount, int x, int y) {
        client.rightClick(zone, element, index, clickCount, x, y);
    }

    public String run(String command) {
        return client.run(command);
    }

    public String runLayoutTest(String xml) {
        return client.runLayoutTest(xml);
    }

    public String runNativeAPICall(String zone, String element, int index, String script) {
        return client.runNativeAPICall(zone, element, index, script);
    }


    public void sendText(String text) {
        long before = System.currentTimeMillis();
        client.sendText(text);
        Finish("sendText", text, before);

    }


    public void sendWhileNotFound(String toSend, String zone, String elementtofind, int timeout, int delay) {
        client.sendWhileNotFound(toSend, zone, elementtofind, timeout, delay);
    }


    public void sendWhileNotFound(String toSend, String zone, String elementtofind, int elementtofindindex, int timeout, int delay) {
        client.sendWhileNotFound(toSend, zone, elementtofind, elementtofindindex, timeout, delay);
    }


    public void setApplicationTitle(String title) {
        client.setApplicationTitle(title);
    }


    public void setAuthenticationReply(String reply, int delay) {
        client.setAuthenticationReply(reply, delay);
    }


    public void setDefaultClickDownTime(int downTime) {
        client.setDefaultClickDownTime(downTime);
    }


    public String setDefaultTimeout(int newTimeout) {
        return client.setDefaultTimeout(newTimeout);
    }


    public void setDefaultWebView(String webViewLocator) {
        client.setDefaultWebView(webViewLocator);
    }


    public void setDevice(String device) {
        client.setDevice(device);
    }


    public void setDragStartDelay(int delay) {
        client.setDragStartDelay(delay);
    }


    public void setInKeyDelay(int delay) {
        client.setInKeyDelay(delay);
    }


    public void setKeyToKeyDelay(int delay) {
        client.setKeyToKeyDelay(delay);
    }


    public void setLanguage(String language) {
        client.setLanguage(language);
    }


    public void setLanguagePropertiesFile(String propertiesfile) {
        client.setLanguagePropertiesFile(propertiesfile);
    }


    public void setLocation(String latitude, String longitude) {
        client.setLocation(latitude, longitude);
    }

    public void setNetworkConditions(String profile, int duration) {
        client.setNetworkConditions(profile, duration);
    }


    public void setOcrIgnoreCase(boolean ignoreCase) {
        client.setOcrIgnoreCase(ignoreCase);
    }


    public void setOcrTrainingFilePath(String trainingPath) {
        client.setOcrTrainingFilePath(trainingPath);
    }


    public String setPickerValues(String zoneName, String elementName, int index, int wheelIndex, String value) {
        return client.setPickerValues(zoneName, elementName, index, wheelIndex, value);
    }


    public void setProjectBaseDirectory(String projectBaseDirectory) {
        client.setProjectBaseDirectory(projectBaseDirectory);
    }


    public void setProperty(String key, String value) {
        client.setProperty(key, value);
    }


    public void setRedToBlue(boolean redToBlue) {
        client.setRedToBlue(redToBlue);
    }


    public String setReporter(String reporterName, String directory) {
        return client.setReporter(reporterName, directory);
    }


    public void elementSendText(String zone, String element, int index, String text) {
        long before = System.currentTimeMillis();
        client.elementSendText(zone, element, index, text);
        Finish("elementSendText", zone + " : " + element, before);

        String check = client.elementGetText(zone, element, index);
        if (!check.equals(text)) {
            System.out.println("Couldn't find the text - " + text + " - in the element - " + element);
            //client.verifyElementFound("native","text=there is no real text - I'm only failing the test!!!", 100);
            client.report("Couldn't find the text - " + text + " - in the element - " + element, false);
        }
    }


    public String elementSetProperty(String zone, String element, int index, String property, String value) {
        return client.elementSetProperty(zone, element, index, property, value);
    }


    public void elementSwipe(String zone, String element, int index, String direction, int offset, int time) {
        long before = System.currentTimeMillis();
        client.elementSwipe(zone, element, index, direction, offset, time);
        Finish("elementSwipe", zone + ":" + element, before);

    }


    public boolean elementSwipeWhileNotFound(String componentZone, String componentElement, String direction, int offset, int swipeTime, String elementfindzone, String elementtofind, int elementtofindindex, int delay, int rounds, boolean click) {
        return client.elementSwipeWhileNotFound(componentZone, componentElement, direction, offset, swipeTime, elementfindzone, elementtofind, elementtofindindex, delay, rounds, click);
    }


    public void endTransaction(String name) {
        client.endTransaction(name);
    }


    public void exit() {
        client.exit();
    }


    public void extractLanguageFiles(String application, String directoryPath, boolean allowOverwrite) {
        client.extractLanguageFiles(application, directoryPath, allowOverwrite);
    }


    public void flick(String direction, int offset) {
        client.flick(direction, offset);
    }


    public void flickCoordinate(int x, int y, String direction) {
        client.flickCoordinate(x, y, direction);
    }


    public void flickElement(String zone, String element, int index, String direction) {
        client.flickElement(zone, element, index, direction);
    }


    public void forceTouch(String zone, String element, int index, int duration, int force, int dragDistanceX, int dragDistanceY, int dragDuration) {
        client.forceTouch(zone, element, index, duration, force, dragDistanceX, dragDistanceY, dragDuration);
    }


    public String generateReport() {
        return client.generateReport();
    }


    public void verifyElementFound(String zone, String element, int index) {
        long before = System.currentTimeMillis();
        client.verifyElementFound(zone, element, index);
        Finish("verifyElementFound", zone + " : " + element, before);
    }


    public void verifyElementNotFound(String zone, String element, int index) throws InternalException {
        client.verifyElementNotFound(zone, element, index);
    }


    public void verifyIn(String zone, String searchElement, int index, String direction, String elementFindZone, String elementToFind, int width, int height) throws InternalException {
        client.verifyIn(zone, searchElement, index, direction, elementFindZone, elementToFind, width, height);
    }


    public void waitForAudioPlayEnd(int timeout) {
        client.waitForAudioPlayEnd(timeout);
    }


    public boolean waitForElement(String zone, String element, int index, int timeout) {
        return client.waitForElement(zone, element, index, timeout);
    }


    public boolean waitForElementToVanish(String zone, String element, int index, int timeout) {
        return client.waitForElementToVanish(zone, element, index, timeout);
    }


    public boolean waitForWindow(String name, int timeout) {
        return client.waitForWindow(name, timeout);
    }


    public void setDisableConsole(boolean disableConsole) {
        client.setDisableConsole(disableConsole);
    }


    public void setClientDebugStatus(boolean debugStatus) {
        client.setClientDebugStatus(debugStatus);
    }


    public void setLogger(ILogger logger) {
        client.setLogger(logger);
    }


    public void addMobileListener(String type, String xpath, MobileListener listener) {
        client.addMobileListener(type, xpath, listener);
    }


    public Map<String, Object> getLastCommandResultMap() {
        return client.getLastCommandResultMap();
    }


    public boolean isThrowExceptionOnFail() {
        return client.isThrowExceptionOnFail();
    }


    public void setThrowExceptionOnFail(boolean throwExceptionOnFail) {
        client.setThrowExceptionOnFail(throwExceptionOnFail);
    }

    public String waitForDevice(String query, int timeOut) {
        if (!Runner.GRID) {
            client.waitForDevice(query, timeOut);
        } else {
            System.out.println("all good - " + Thread.currentThread().getName());
        }
        deviceName = client.getDeviceProperty("device.name");
        deviceOS = client.getDeviceProperty("device.os");
        return deviceName;
    }

    public void getRemoteFile(String remoteLocation, int timeout, String localFile) throws Exception {
        client.getRemoteFile(remoteLocation, timeout, localFile);
    }


    public String addDevice(String serialNumber, String deviceName) {
        return client.addDevice(serialNumber, deviceName);
    }


    public void applicationClearData(String packageName) {
        client.applicationClearData(packageName);
    }


    public boolean applicationClose(String packageName) {
        return client.applicationClose(packageName);
    }


    public String capture() {
        long before = System.currentTimeMillis();
        String value = client.capture();
        Finish("capture", "NoZone", before);
        return value;
    }


    public String capture(String line) {
        return client.capture(line);
    }


    public void captureElement(String name, int x, int y, int width, int height, int similarity) {
        client.captureElement(name, x, y, width, height, similarity);
    }


    public void clearAllSms() {
        client.clearAllSms();
    }


    public void clearDeviceLog() {
        client.clearDeviceLog();
    }


    public void clearLocation() {
        client.clearLocation();
    }

    public void collectSupportData(String zipDestination, String applicationPath, String device, String scenario, String expectedResult, String actualResult) {
        long before = System.currentTimeMillis();
        System.out.println(Thread.currentThread().getName() + " " + device + " - " + "SupportData - " + zipDestination);
        try {
            client.collectSupportData(zipDestination, "", device, "", "", "", true, true);

        } catch (Exception e1) {
            System.err.println(device + " - Can't get SupportData");
            e1.printStackTrace();
        }
        Finish("collectSupportData", "NoZone", before);
    }

    public void collectSupportData(String zipDestination, String applicationPath, String device, String scenario, String expectedResult, String actualResult, boolean withCloudData, boolean onlyLatestLogs) {

        long before = System.currentTimeMillis();
        System.out.println(Thread.currentThread().getName() + " " + device + " - " + "SupportData - " + zipDestination);
        client.collectSupportData(zipDestination, applicationPath, device, scenario, expectedResult, actualResult, withCloudData, onlyLatestLogs);
        Finish("collectSupportData", "NoZone", before);
    }

    public void deviceAction(String action) {
        client.deviceAction(action);
    }


    public void drag(String zone, String element, int index, int xOffset, int yOffset) {
        client.drag(zone, element, index, xOffset, yOffset);
    }


    public void dragCoordinates(int x1, int y1, int x2, int y2) {
        client.dragCoordinates(x1, y1, x2, y2);
    }


    public void dragCoordinates(int x1, int y1, int x2, int y2, int time) {
        client.dragCoordinates(x1, y1, x2, y2, time);
    }


    public void dragDrop(String zone, String dragElement, int dragIndex, String dropElement, int dropIndex) {
        client.dragDrop(zone, dragElement, dragIndex, dropElement, dropIndex);
    }


    public void drop() {
        client.drop();
    }


    public String elementGetProperty(String zone, String element, int index, String property) {
        return client.elementGetProperty(zone, element, index, property);
    }


    public int elementGetTableRowsCount(String tableLocator, int tableIndex, boolean visible) {
        return client.elementGetTableRowsCount(tableLocator, tableIndex, visible);
    }


    public int elementGetTableRowsCount(String zone, String tableLocator, int tableIndex, boolean visible) {
        return client.elementGetTableRowsCount(zone, tableLocator, tableIndex, visible);
    }


    public String elementGetTableValue(String rowLocator, int rowLocatorIndex, String columnLocator) {
        return client.elementGetTableValue(rowLocator, rowLocatorIndex, columnLocator);
    }


    public String elementGetText(String zone, String element, int index) {
        return client.elementGetText(zone, element, index);
    }


    public void elementListPick(String listZone, String listLocator, String elementZone, String elementLocator, int index, boolean click) {
        client.elementListPick(listZone, listLocator, elementZone, elementLocator, index, click);
    }


    public void elementListSelect(String listLocator, String elementLocator, int index, boolean click) {
        client.elementListSelect(listLocator, elementLocator, index, click);
    }


    public boolean elementListVisible(String listLocator, String elementLocator, int index) {
        return client.elementListVisible(listLocator, elementLocator, index);
    }


    public void elementScrollToTableRow(String tableLocator, int tableIndex, int rowIndex) {
        client.elementScrollToTableRow(tableLocator, tableIndex, rowIndex);
    }


    public void elementScrollToTableRow(String zone, String tableLocator, int tableIndex, int rowIndex) {
        client.elementScrollToTableRow(zone, tableLocator, tableIndex, rowIndex);
    }
}
