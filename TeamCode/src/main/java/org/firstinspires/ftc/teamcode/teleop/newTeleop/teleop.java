package org.firstinspires.ftc.teamcode.teleop.newTeleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

// TeleOp annotation to register this OpMode with the FTC Driver Station
@TeleOp(name = "Into The Deep Teleop", group = "Teleop")
public class teleop extends LinearOpMode implements teleop_interface {
    // Instance of the hardware class to manage robot components
    final hardware hardware = new hardware();

    @Override
    public void initialize() {
        try{
            //DcMotor
            hardware.frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
            hardware.frontRight = hardwareMap.get(DcMotor.class, "frontRight");
            hardware.backLeft = hardwareMap.get(DcMotor.class, "backLeft");
            hardware.backRight = hardwareMap.get(DcMotor.class, "backRight");

            hardware.mantis = hardwareMap.get(DcMotor.class, "mantis");
            hardware.lift = hardwareMap.get(DcMotor.class, "lift");
            hardware.hopper = hardwareMap.get(DcMotor.class, "hopper");

            //Servos
            hardware.grabber = hardwareMap.get(Servo.class, "grabber");

            //Sensors
            hardware.colorSensor = hardwareMap.get(ColorSensor.class, "colorSensor");
            hardware.distanceSensorLeft = hardwareMap.get(DistanceSensor.class, "distanceSensorLeft");
            hardware.distanceSensorBack = hardwareMap.get(DistanceSensor.class, "distanceSensorBack");
            hardware.distanceSensorRight = hardwareMap.get(DistanceSensor.class, "distanceSensorRight");

            hardware.checkMotorInit();
        } catch (NullPointerException e) {
            telemetry.addLine("Initialization error: " + e.getMessage());
            telemetry.update();
        } catch (Exception e) {
            telemetry.addLine("An error occurred during initialization: " + e.getMessage());
            telemetry.update();
        }
    }

    @Override
    //TODO find direction
    public void setDirection() {
        // Set the direction of each motor
        hardware.frontLeft.setDirection(DcMotor.Direction.REVERSE); // Reverse front left motor
        hardware.frontRight.setDirection(DcMotor.Direction.FORWARD); // Forward front right motor
        hardware.backLeft.setDirection(DcMotor.Direction.REVERSE); // Reverse back left motor
        hardware.backRight.setDirection(DcMotor.Direction.FORWARD);// Forward back right motor

        hardware.lift.setDirection(DcMotor.Direction.FORWARD); // Forward lift motor
        hardware.mantis.setDirection(DcMotor.Direction.FORWARD); // Forward mantis motor
        hardware.hopper.setDirection(DcMotor.Direction.FORWARD); // Forward hopper motor
    }

    @Override
    public void telemetry() {
        // Provide feedback about the robot's state
        while(opModeInInit()) {
            telemetry.addLine("=== Robot Initialization ===");
            telemetry.addLine("Status: Initializing");
            telemetry.addLine("Press START when ready");
            telemetry.update();
        }
        whileMotorsBusy();
    }

    // Check if motors are busy and display telemetry
    @Override
    public void whileMotorsBusy() {
        telemetry.addLine("Code is running");
            if(hardware.frontLeft.isBusy()) {
                telemetry.addLine("=== Wheel ===");
                telemetry.addData("Front left motor position", hardware.frontLeft.getCurrentPosition());
                telemetry.addData("Front right motor position", hardware.frontRight.getCurrentPosition());
                telemetry.addData("Back left motor position", hardware.backLeft.getCurrentPosition());
                telemetry.addData("Back right motor position", hardware.backRight.getCurrentPosition());
            }else if(hardware.lift.isBusy()){
                telemetry.addLine("=== Lift ===");
                telemetry.addData("Lift motor position", hardware.lift.getCurrentPosition());
            }else if(hardware.mantis.isBusy()){
                telemetry.addLine("=== Mantis ===");
                telemetry.addData("Mantis Motor Position", hardware.mantis.getCurrentPosition());
            }else if (hardware.hopper.isBusy()){
                telemetry.addLine("=== Hopper Arm ===");
                telemetry.addData("Hopper Motor Position", hardware.hopper.getCurrentPosition());
            }else if (hardware.grabber.getPosition() != 0){
                telemetry.addLine("=== Grabber ===");
                telemetry.addData("Grabber Position", hardware.grabber.getPosition());
            }
        telemetry.update();
    }

    @Override
    public void movement(double vertical, double strafe, double turn) {
        // Set power to each motor based on gamepad input for movement
        hardware.frontLeft.setPower(-vertical - strafe - turn); // Calculate power for front left motor
        hardware.frontRight.setPower(-vertical + strafe + turn); // Calculate power for front right motor
        hardware.backLeft.setPower(-vertical + strafe - turn); // Calculate power for back left motor
        hardware.backRight.setPower(-vertical - strafe + turn); // Calculate power for back right motor
    }

    // Control the robot's arm based on the state and speed
    @Override
    public void arm(teleop_enum state, double speed) {
        switch(state) {
            case LIFT:
                hardware.lift.setPower(speed); // Set lift motor power
                break;
            case MANTIS:
                hardware.mantis.setPower(speed); // Set mantis motor power
                break;
            case HOPPER:
                hardware.hopper.setPower(speed); // Set hopper motor power
                break;
            default:
                // Stop all motors if no valid state is provided
                hardware.lift.setPower(0);
                hardware.mantis.setPower(0);
                hardware.hopper.setPower(0);
                break;
        }
    }

    // Control the gripper's position
    @Override
    public void gripper(int pos) {
        hardware.grabber.setPosition(pos); // Set the position of the grabber servo
    }

    //TODO test arms

    // Method for controlling final movement with reduced speeds
    @Override
    public void finalMovement() {
        double reduction = 0.8; // Default speed reduction
        double turnReduction = 0.55; // Default turning speed reduction

        // Adjust speeds based on button presses
        if (gamepad1.a) {
            // Slow mode
            reduction = 0.4;
            turnReduction = 0.35;
        } else if (gamepad1.b) {
            // Fast mode
            reduction = 1;
            turnReduction = 1;
        } else if ((gamepad1.left_stick_button) || (gamepad1.right_stick_button)) {
            // Stop mode
            reduction = 0.0;
            turnReduction = 0.0;
        }

        // Apply movement to motors based on gamepad input
        double vertical = reduction * gamepad1.left_stick_y; // Vertical movement
        double turn = -reduction * gamepad1.left_stick_x; // Turning movement
        double strafe = -turnReduction * gamepad1.right_stick_x; // Strafe movement
        movement(vertical, strafe, turn); // Call movement method with calculated powers
    }

    // Method for controlling the arm based on gamepad input
    @Override
    public void finalArm() {
        teleop_enum state = null; // Initialize state
        double armSpeed = 0; // Initialize arm speed
        // Determine arm state and speed based on gamepad input
        if (Math.abs(gamepad2.left_stick_y) > 0) {
            state = teleop_enum.MANTIS; // Set state to MANTIS
            armSpeed = gamepad2.left_stick_y; // Use left stick Y for speed
        } else if (Math.abs(gamepad2.left_stick_x) > 0) {
            state = teleop_enum.HOPPER; // Set state to HOPPER
            armSpeed = gamepad2.left_stick_x; // Use left stick X for speed
        } else if (Math.abs(gamepad2.right_stick_y) > 0) {
            state = teleop_enum.LIFT; // Set state to LIFT
            armSpeed = gamepad2.right_stick_y; // Use right stick Y for speed
        }
        if (state != null) {
            arm(state, armSpeed); // Call arm method with determined state and speed
        }
    }

    // Method for controlling the gripper based on gamepad input
    @Override
    public void finalGrabber() {
        //TODO find open and close position
        int close = -200; // Position to close the gripper
        int open = 200; // Position to open the gripper
        // Control gripper based on button presses
        if (gamepad2.x) {
            gripper(close); // Close gripper
        } else if (gamepad2.y) {
            gripper(open); // Open gripper
        }
    }

    @Override
    public void runOpMode() {
        // Initialize the OpMode
        initialize(); // Initialize hardware
        setDirection(); // Set motor directions
        telemetry(); // Send initial telemetry data

        waitForStart(); // Wait for the start signal

        // Main loop for controlling the robot during teleop
        while (opModeIsActive()) {
            telemetry();
            finalMovement(); // Control robot movement
            finalArm(); // Control robot arm
            finalGrabber(); // Control gripper
        }
    }
}
