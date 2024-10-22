package org.firstinspires.ftc.teamcode.autonomous.newAuto.classes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.autonomous.newAuto.newAuto_enum;
import org.firstinspires.ftc.teamcode.autonomous.newAuto.newAuto_interface;

@Autonomous(name = "New Autonomous", group = "Autonomous")
public class NewAuto extends LinearOpMode implements newAuto_interface {
    final hardware hardware = new hardware();
    final calculations calculations = new calculations();
    private boolean isDriving = false;
    // Initialize motors and hardware components
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

    // Sets the robot telemetry data
    @Override
    public void telemetry() {
        telemetry.addLine("Code is running");
        String randomPun = hardware.puns[calculations.random.nextInt(hardware.puns.length)];
        telemetry.addData("Pun of the day", randomPun);
        if (isDriving){
            if(hardware.frontLeft.isBusy()) {
                telemetry.addData("Front left motor position", hardware.frontLeft.getCurrentPosition());
                telemetry.addData("Front right motor position", hardware.frontRight.getCurrentPosition());
                telemetry.addData("Back left motor position", hardware.backLeft.getCurrentPosition());
                telemetry.addData("Back right motor position", hardware.backRight.getCurrentPosition());
            }
            if(hardware.lift.isBusy()){
                telemetry.addData("Lift motor position", hardware.lift.getCurrentPosition());
            }else if(hardware.mantis.isBusy()){
                telemetry.addData("Mantis Motor Position", hardware.mantis.getCurrentPosition());
            }else if (hardware.hopper.isBusy()){
                telemetry.addData("Hopper Motor Position", hardware.hopper.getCurrentPosition());
                }
        }else{
            telemetry.addLine("Robot stopped");
        }
        telemetry.update();
    }

    // Sets which direction the motor goes in when plugging in a positive power
    @Override
    public void setDirection() {
        hardware.frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        hardware.frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        hardware.backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        hardware.backRight.setDirection(DcMotorSimple.Direction.FORWARD);

        hardware.lift.setDirection(DcMotorSimple.Direction.FORWARD);
        hardware.mantis.setDirection(DcMotorSimple.Direction.FORWARD);
        hardware.hopper.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    // Sets the motor to run to a position set by the number of encoder ticks
    @Override
    public void runToPosition(DcMotor motor) {
        if (motor == null) {
            telemetry.addLine("Cannot reset encoder; motor is null.");
            telemetry.update();
            return; // Exit the method if motor is null
        }

        try {
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            telemetry.addLine("Resetting encoder for motors");
        } catch (Exception e) {
            telemetry.addLine("Error resetting encoder for motor");
            telemetry.update();
        }
    }

    @Override
    public void motorToPosition(){
        runToPosition(hardware.frontLeft);
        runToPosition(hardware.frontRight);
        runToPosition(hardware.backLeft);
        runToPosition(hardware.backRight);
        runToPosition(hardware.lift);
        runToPosition(hardware.mantis);
        runToPosition(hardware.hopper);
    }

    // Helper method to reset motor encoder
    @Override
    public void resetEncoder(DcMotor motor) {
        if (motor == null) {
            telemetry.addLine("Cannot reset encoder; motor is null.");
            telemetry.update();
            return; // Exit the method if motor is null
        }
        try {
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            telemetry.addLine("Resetting encoder for motors");
        } catch (Exception e) {
            telemetry.addLine("Error resetting encoder for motor");
            telemetry.update();
        }
    }
    // Reset the encoders for all motors
    @Override
    public void resetMotorEncoders() {
        resetEncoder(hardware.frontLeft);
        resetEncoder(hardware.frontRight);
        resetEncoder(hardware.backLeft);
        resetEncoder(hardware.backRight);
        resetEncoder(hardware.lift);
        resetEncoder(hardware.mantis);
        resetEncoder(hardware.hopper);
    }

    // Set the zero power behavior of the motor to BRAKE
    @Override
    public void setBrakes(DcMotor motor) {
        if (motor == null) {
            telemetry.addLine("Cannot set motor brakes; motor is null.");
            telemetry.update();
            return; // Exit the method if motor is null
        }
        try {
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            telemetry.addLine("Setting brakes for motors");
        } catch (Exception e) {
            telemetry.addLine("Error setting motor brakes");
            telemetry.update();
        }
    }

    // Sets the motors to brake
    @Override
    public void setMotorBrakes() {
        setBrakes(hardware.frontLeft);
        setBrakes(hardware.frontRight);
        setBrakes(hardware.backLeft);
        setBrakes(hardware.backRight);
        setBrakes(hardware.lift);
        setBrakes(hardware.mantis);
        setBrakes(hardware.hopper);
    }



    // Sets the robot's encoder position to go to
    public void setPosition(int frontLeftPos, int frontRightPos, int backLeftPos, int backRightPos) {
        hardware.frontLeft.setTargetPosition(frontLeftPos);
        hardware.frontRight.setTargetPosition(frontRightPos);
        hardware.backLeft.setTargetPosition(backLeftPos);
        hardware.backRight.setTargetPosition(backRightPos);
    }

    // Sets the motor speeds
    public void setSpeed(double frontLeftSpeed, double frontRightSpeed, double backRightSpeed, double backLeftSpeed) {
        hardware.frontLeft.setPower(frontLeftSpeed);
        hardware.frontRight.setPower(frontRightSpeed);
        hardware.backLeft.setPower(backLeftSpeed);
        hardware.backRight.setPower(backRightSpeed);
    }

    // While the motor is busy, run telemetry
    public boolean whileMotorsBusy() {
        while (opModeIsActive() && hardware.frontLeft.isBusy() && hardware.frontRight.isBusy() && hardware.backLeft.isBusy() && hardware.backRight.isBusy()) {
            telemetry.addData("Time Elapsed", "%.2f seconds", getRuntime());
            telemetry.update();
            isDriving = true;
        }
        return isDriving;
    }

    // The base for all other movement functions
    @Override
    public void base(int targetPosFL, int targetPosFR, int targetPosBL, int targetPosBR, double speedFL, double speedFR, double speedBL, double speedBR) {
        setPosition(targetPosFL, targetPosFR, targetPosBL, targetPosBR);
        motorToPosition();
        setSpeed(speedFL, speedFR, speedBL, speedBR);
        whileMotorsBusy();
        sleep(1000);
        resetMotorEncoders();
    }


    // Movement logic based on state
    @Override
    public void movement(newAuto_enum state, double inches, double speed) {
        int targetPos = (int) (inches * calculations.WHEEL_TICK_PER_INCH);
        isDriving = true;
        switch (state) {
            case FORWARD:
                base(targetPos, targetPos, targetPos, targetPos, speed, speed, speed, speed);
                break;
            case BACKWARD:
                base(-targetPos, -targetPos, -targetPos, -targetPos, -speed, -speed, -speed, -speed);
                break;
            case STRAFE_LEFT:
                base(-targetPos, targetPos, targetPos, -targetPos, -speed, speed, speed, -speed);
                break;
            case STRAFE_RIGHT:
                base(targetPos, -targetPos, -targetPos, targetPos, speed, -speed, -speed, speed);
                break;
        }
    }

    // Turning logic based on state
    @Override
    public void turn(newAuto_enum state, double degrees, double speed) {
        int targetPos = (int) (calculations.WHEEL_TICK_PER_DEGREE * degrees);
        switch (state) {
            case TURN_RIGHT:
                base(targetPos, targetPos, -targetPos, -targetPos, speed, speed, -speed, -speed);
                break;
            case TURN_LEFT:
                base(-targetPos, -targetPos, targetPos, targetPos, -speed, -speed, speed, speed);
                break;
        }
    }

    @Override
    public void arm(newAuto_enum motor, double inch, double speed) {
        int targetPos;
        switch(motor){
            case MANTIS:
                targetPos = (int) (calculations.MANTIS_TICK_PER_INCH * inch);
                hardware.mantis.setTargetPosition(targetPos);
                hardware.mantis.setPower(speed);
                break;
            case LIFT:
                targetPos = (int) (calculations.LIFT_TICK_PER_INCH * inch);
                hardware.lift.setTargetPosition(targetPos);
                hardware.lift.setPower(speed);
                break;
            case HOPPER:
                targetPos = (int) (calculations.HOPPER_TICK_PER_INCH * inch);
                hardware.hopper.setTargetPosition(targetPos);
                hardware.hopper.setPower(speed);
                break;
        }
    }



    @Override
    public boolean detectYellow(int blue, int red, int green) {
        return (blue < red) && (blue < green);

    }

    // Moves to an area based on a state given
    @Override
    public void moveTo(newAuto_enum state) {
        switch (state) {
            case START_POSITION:
                //Move forward turning radius plus an inch for error
                    movement(newAuto_enum.FORWARD, 5, calculations.DRIVE_SPEED);

                //Turn right 90degrees so that the color sensor is closest to the wall
                    movement(newAuto_enum.TURN_RIGHT, 90, calculations.DRIVE_SPEED);

                //Strafe right until the distance sensor is at DISTANCE_FROM_BLOCK inches from wall
                while(hardware.distanceSensorRight.getDistance(DistanceUnit.INCH) < calculations.DISTANCE_FROM_WALL){
                    movement(newAuto_enum.STRAFE_RIGHT, calculations.SMIDGEN, calculations.DRIVE_SPEED);
                }
                //Move backwards a set number of inches until you're near blocks //TODO find set number of inches
                    movement(newAuto_enum.BACKWARD, calculations.DISTANCE_TO_BLOCKS, calculations.DRIVE_SPEED);

                //Give back data
                telemetry.speak("Going to start position");
                telemetry.update();
                sleep(1000);
                break;
            case GO_TO_BASKET:
                movement(newAuto_enum.FORWARD, 5, calculations.DRIVE_SPEED);
                telemetry.speak("Heading towards the bucket");
                telemetry.update();
                sleep(1000);
                break;
            case END_POSITION:
                movement(newAuto_enum.BACKWARD, 3, calculations.DRIVE_SPEED);
                telemetry.speak("Going back to start position");
                telemetry.update();
                sleep(1000);
                break;
        }
    }



    // Main method to run the autonomous operation
    @Override
    public void runOpMode() throws InterruptedException {
        initialize();
        int red = hardware.colorSensor.red();
        int blue = hardware.colorSensor.blue();
        int green = hardware.colorSensor.green();
        setMotorBrakes();
        resetMotorEncoders();
        setDirection();
        waitForStart();
        while (opModeIsActive()) {
            telemetry();
            movement(newAuto_enum.FORWARD, (100.0 /6.0), 0.1);
        }
    }
}
