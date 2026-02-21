package com.cronical.client.capture;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;

import com.esotericsoftware.kryonet.Connection;

public class StartCapture {

    public static Capture cptr;

    public StartCapture(Connection con) {
        try {
            Robot robot = new Robot();
            Rectangle rect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            cptr = new Capture(robot, rect, con);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }
}