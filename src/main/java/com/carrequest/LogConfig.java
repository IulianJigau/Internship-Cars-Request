package com.carrequest;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogConfig {

    public static Logger setupLogger(Class<?> workingClass) {
        Logger logger = Logger.getLogger(workingClass.getName());

        try {
            FileHandler fileHandler = new FileHandler("RequestApp.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setUseParentHandlers(false);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to set up file handler", e);
        }
        return logger;
    }
}
