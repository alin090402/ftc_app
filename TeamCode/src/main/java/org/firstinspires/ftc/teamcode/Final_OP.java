/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="Final_OP", group="Linear Opmode")

public class Final_OP extends Functii {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        Initialize();
        waitForStart();
        runtime.reset();
        while (opModeIsActive()) {

                telemetry.addData("directie",  Gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle);
                telemetry.update();
                double x,y,t,z;
                x = gamepad1.left_stick_x;
                y = gamepad1.left_stick_y;
                t = gamepad2.left_stick_x;
                z = gamepad2.left_stick_y;
                if(gamepad1.left_bumper)
                {
                    RotateLeft(0.4,0);
                }
                else if(gamepad1.right_bumper)
                {
                    RotateRight(0.4,0);
                }
                else if(gamepad2.left_bumper) {
                    RotateLeft(0.4,0);
                }
                else if(gamepad2.right_bumper) {
                    RotateRight(0.4,0);
                }
                else if(x > 0.15 || x < -0.15 ||(y > 0.15 || y < -0.15 )) {
                    double maxX, maxY;
                    if (x > 0) maxX = x;
                    else maxX = -x;
                    if (y > 0) maxY = y;
                    else maxY = -y;
                    if (maxX > maxY) {
                        double power = -x / 2;
                        if(x >= -0.5 && x <= 0) power = 0.25;
                        if(x <= 0.5 && x >= 0) power = -0.25;
                        if(power > 0)
                            MoveRight(-power,0);
                        else MoveLeft(power,0);
                    } else {
                        double power = y / 2;
                        if(y >= -0.5 && y <= 0) power = -0.25;
                        if(y <= 0.5 && y >= 0) power = 0.25;
                        if(power > 0)
                            MoveFront(-power,0);
                        else MoveBack(power,0);
                    }
                } else if(t > 0.15 || t < -0.15 ||(z > 0.15 || z < -0.15 )) {
                    double maxX, maxY;
                    if (t > 0) maxX = t;
                    else maxX = -t;
                    if (z > 0) maxY = z;
                    else maxY = -z;
                    if (maxX > maxY) {
                        double power = -t / 2;
                        if (t >= -0.5 && t <= 0) power = 0.25;
                        if (t <= 0.5 && t >= 0) power = -0.25;
                        if(power > 0)
                            MoveRight(-power,0);
                        else MoveLeft(power,0);
                    } else {
                        double power = z / 2;
                        if (z >= -0.5 && z <= 0) power = -0.25;
                        if (z <= 0.5 && z >= 0) power = 0.25;
                        if(power > 0)
                            MoveFront(-power,0);
                        else MoveBack(power,0);
                    }
                }
                else StopMotors();

                    //brat telescopic
                y = gamepad1.right_stick_y;
                x = gamepad2.right_stick_y;

                if(y >= 0.1 || y <= -0.1)
                {
                    HexMotor.setPower(-y);
                }
                else if(x >= 0.1 || x <= -0.1)
                {
                    HexMotor.setPower(-x);
                }
                else {
                    HexMotor.setPower(0);
                }


                ///servo
                if(gamepad1.b)
                {
                    telemetry.addData("Brat", "SUS");
                    telemetry.update();
                    Servo.setPower(0.5);
                }
                else if(gamepad1.a )
                {
                    telemetry.addData("Brat", "JOS");
                    telemetry.update();
                    Servo.setPower(-0.7);
                }
                else if(gamepad2.b) {
                    telemetry.addData("Brat", "SUS");
                    telemetry.update();
                    Servo.setPower(0.5);
                }
                else if (gamepad2.a) {
                    telemetry.addData("Brat", "JOS");
                    telemetry.update();
                    Servo.setPower(-0.7);
                }

                if(gamepad1.x)
                {
                    telemetry.addData("Status", "Am apasat a frt");
                    telemetry.update();
                    Mascota.setPower(1.5);

                }
                else if(gamepad1.y)
                {
                    Mascota.setPower(-0.1);
                }
        }

    }
}
