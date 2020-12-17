package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.T265Camera;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.sun.org.glassfish.gmbal.ParameterNames;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import static java.lang.Math.PI;
import static java.lang.Math.abs;

public class CameraDrive {
    private DcMotor[] motors;
    private double[] motorSpeeds;
    private double[] angles;
    private T265Camera camera;
    private T265Camera.Translation2d translation2d;
    private BNO055IMU imu;

    private Orientation gangles() { return imu.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS); }
    private Telemetry telemetry;

    public void setUp(DcMotor[] motors, double[] angles, T265Camera camera, BNO055IMU imu) {
        setUp(motors, angles, camera, imu, null);
    }

    public void setUp(DcMotor[] motors, double[] angles, T265Camera camera, BNO055IMU imu, Telemetry telemetry) {
        if (motors.length != angles.length) {
            throw new RuntimeException("Motor array length and angle array length are not the same! Check your code!");
        }
        this.motors = motors;
        this.motorSpeeds = new double[motors.length];
        this.angles = angles;
        this.camera = camera;
        this.imu = imu;
        this.telemetry = telemetry;
    }

    public void goToPosition(double moveX, double moveY, TeleAuto callback) {
        goToPosition(moveX, moveY, 1, callback);
    }

    public void goToPosition(double moveX, double moveY, double speed, TeleAuto callback) {
        double currentAngle = gangles().firstAngle;
        goTo(moveX, moveY, currentAngle, )
    }

    public void goToRotation(double theta, TeleAuto callback) { goToRotation(theta, 1, callback); }

    public void goToRotation(double theta, double speed, TeleAuto callback) {
        goTo(0, 0, theta, speed, callback);
    }

    public void goTo(double moveX, double moveY, double theta, double speed, TeleAuto callback) {
        // Create persistent variables
        boolean xComplete;
        boolean yComplete;
        boolean turnComplete;

        // Wrap theta to localTheta
        double localTheta = wrap(theta);
        while (callback.opModeIsActive()) {
            T265Camera.CameraUpdate up = camera.getLastReceivedCameraUpdate();

            this.translation2d = up.pose.getTranslation();

            double currentX = this.translation2d.getX();
            double currentY = this.translation2d.getY();
            double currentTheta = gangles().firstAngle;

            double deltaX = moveX - currentX;
            double deltaY = moveY - currentY;
            double deltaTheta = wrap(localTheta - currentTheta);

            xComplete = abs(deltaX) < 0.3;
            yComplete = abs(deltaY) < 0.3;
            turnComplete = abs(deltaTheta) < 0.05;

            if (xComplete && yComplete && turnComplete) {
                stopWheel();
                break;
            }

            double driveTheta = Math.atan2(xComplete ? 0 : deltaX, yComplete ? 0 : deltaY);
            driveTheta += gangles().firstAngle;

            double localSpeed = speed;
            if (abs(deltaX) < 5 && abs(deltaY) < 5) {
                localSpeed *= avg(abs(deltaX), abs(deltaY)) / 10;
            }
            localSpeed = clamp(0.2, 1, localSpeed);

            for (int i = 0; i < motors.length; i++) {
                motors[i].setPower(Math.sin(angles[i] - driveTheta) * localSpeed + deltaTheta);
                motorSpeeds[i] = Math.sin(angles[i] - driveTheta) * localSpeed + deltaTheta;
            }

            writeTelemetry(moveX, moveY, deltaX, deltaY, avg(abs(deltaX), abs(deltaY)), localSpeed, xComplete, yComplete);
            writeTelemetry(localTheta, deltaTheta, currentTheta);
        }
    }

    private void stopWheel() {
        for (DcMotor motor : motors) {
            motor.setPower(0);
        }
    }

    private double clamp(double min, double max, double value) {
        return Math.max(min, Math.min(value, max));
    }

    private double wrap(double theta) {
        double newTheta = theta;
        while(abs(newTheta) > PI) {
            if (newTheta < -PI) {
                newTheta += 2*PI;
            } else {
                newTheta -= 2*PI;
            }
        }
        return newTheta;
    }

    private double avg(double... inputs) {
        double output = 0;
        for (double input : inputs) {
            output += input;
        }
        output /= inputs.length;
        return output;
    }

    private void writeTelemetry(double moveX, double moveY, double deltaX, double deltaY, double avg, double speed, boolean xComplete, boolean yComplete) {
        if (telemetry == null) { return; }
        telemetry.addData("Current X", this.translation2d.getX());
        telemetry.addData("Current Y", this.translation2d.getY());
        telemetry.addData("Move X", moveX);
        telemetry.addData("Move Y", moveY);
        telemetry.addData("Delta X", deltaX);
        telemetry.addData("Delta Y", deltaY);
        telemetry.addData("Average", avg);
        telemetry.addData("Speed", speed);
        telemetry.addData("Heading", gangles().firstAngle);
        telemetry.addData("X Complete", xComplete);
        telemetry.addData("Y Complete", yComplete);
    }

    private void writeTelemetry(double localTheta, double deltaTheta, double firstAngle) {
        if (telemetry == null) { return; }
        telemetry.addData("Local Theta", localTheta);
        telemetry.addData("Delta Theta", deltaTheta);
        telemetry.addData("First Angle", firstAngle);
        telemetry.update();
    }
}

interface TeleAuto {
    boolean opModeIsActive();
}
