package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.T265Camera;

import static java.lang.Math.PI;

@Autonomous(name="CameraTest", group="autonomous")
public class CameraTest extends LinearOpMode implements TeleAuto {

    // Variables
    private DcMotor fl = null;
    private DcMotor fr = null;
    private DcMotor rl = null;
    private DcMotor rr = null;

    private BNO055IMU imu;

    private static T265Camera camera = null;

    private final CameraDrive drive = new CameraDrive();

    @Override
    public void runOpMode() {

        // Configure Hardware
        fl = hardwareMap.get(DcMotor.class, "front_left_motor");
        fr = hardwareMap.get(DcMotor.class, "front_right_motor");
        rl = hardwareMap.get(DcMotor.class, "back_left_motor");
        rr = hardwareMap.get(DcMotor.class, "back_right_motor");

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters params = new BNO055IMU.Parameters();
        params.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        params.arcade = true;
        imu.initialize(params);

        camera = new T265Camera(new T265Camera.Translation2d(), 0.1, hardwareMap.appContext);
        camera.setMode(T265Camera.CameraMode.ARCADE);

        // fl.setDirection(DcMotor.Direction.REVERSE);
        // rl.setDirection(DcMotor.Direction.REVERSE);
        fl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        final double QPI = PI/4;

        DcMotor[] motors = {fr, rr, rl, fl};
        double[] angles = {QPI, 3*QPI, 5*QPI, 7*QPI};

        drive.setUp(motors, angles, camera, imu, telemetry);

        waitForStart();
        telemetry.addData("Status", "Operational");
        telemetry.update();

        // Beginning of Autonomous
        if (opModeIsActive()) {
            camera.start();
            drive.goTo(-200, 132, PI, 1, this);
            camera.stop();
        }
    }
}
