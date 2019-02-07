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

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.sun.tools.javac.comp.Todo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

import java.util.List;

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

@Autonomous(name="Autonomus", group="Linear Opmode")
public class Autonom extends Functii {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        boolean Gasit = false;
        int Goldpoz = 0;
        int constD = 0;
        int parte = 0;
        double InitDirection = 0;
        Initialize();


        // Wait for the game to start (driver presses PLAY)
        while(!opModeIsActive() && !isStopRequested())
            runtime.reset();

        if (opModeIsActive()) { //Start autonomie


            InitDirection = Gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle + 180;
            Down(1, 2.7);

            MoveRight(0.18, 0.7);
            Rotate(InitDirection);
            MoveFront(0.399, 0.8);


            MoveRight(0.2, 1.8);
            Rotate(182.5);
            RotateLeft(0.1, 0.3);

            StartV_T();

            MoveLeft(0.16, 0);

            boolean Ok = true;
            Goldpoz = 1;
            double sec = runtime.seconds();
            double value = 0;
            while (Ok) {
                updatedRecognitions = tfod.getRecognitions();

                if (updatedRecognitions != null) {
                    telemetry.addData("# Object Detected", updatedRecognitions.size());
                    if(updatedRecognitions.size() == 1)
                        for (Recognition recognition : updatedRecognitions) {
                            //telemetry.addData("bottom", recognition.getBottom());
                            //telemetry.addData("top", recognition.getTop());

                            if (!recognition.getLabel().equals(LABEL_SILVER_MINERAL)) {
                                Ok = false;
                                break;
                            }

                            value = (recognition.getBottom() + recognition.getTop()) / 2;
                            if(value < 600) {
                                parte = 0;
                                telemetry.addData("pozitie", "stanga");
                            }
                            else if(value > 70){

                                if(parte == 0) Goldpoz++;
                                parte = 1;
                                telemetry.addData("pozitie","dreapta");
                            }
                        }
                }
                telemetry.update();
            }
            StopMotors();
            telemetry.addData("Pozitie gold: ", Goldpoz);
            telemetry.update();
            updatedRecognitions = tfod.getRecognitions();
            Recognition recognition = updatedRecognitions.get(updatedRecognitions.size() - 1);
            value  = (recognition.getBottom() + recognition.getTop() )/ 2;
            telemetry.addData("Poz pe camera", value);
            telemetry.update();
            //double size = recognition.getHeight();
            if(value > 750) MoveRight(0.2,0.5);
            else if(value < 450) MoveLeft(0.2, 0.5);


            /*sec = runtime.seconds();

            telemetry.addData("secunde", sec);

            if(sec <= 9.5) Goldpoz = 1;
            else if(sec <= 11.5) Goldpoz = 2;
                else Goldpoz = 3;*/



            if (tfod != null) {
                tfod.shutdown();
            }
              if(Goldpoz == 1) /// dreapta
            {
                Servo.setPower(-0.7);
                MoveFront(0.4, 1.2);
                Rotate(225);
                MoveFront(0.4, 1.3);
                Servo.setPower(0.5);
                MoveBack(0.35, 0.5);
                MoveLeft(0.35, 0.5);
                MoveFront(0.35, 0.72 );
                //Crater
                Rotate(315);
                MoveFront(0.38, 2.5);
                Servo.setPower(-0.3);
            }
            if(Goldpoz == 2) /// mijloc
            {
                Servo.setPower(-0.7);
                MoveFront(0.25, 2);
                Servo.setPower(0.5);
                //Cr ter

            }
            if(Goldpoz == 3) /// stanga
            {
                Servo.setPower(-0.7);
                MoveFront(0.4, 1.2);
                Rotate(140);
                MoveFront(0.4, 1.2);
                Servo.setPower(0.5);
                //Crater
                MoveBack(0.4,0.317);
                RotateLeft(0.4,1);
                Rotate(315);
                MoveFront(0.5, 2.5);
                Servo.setPower(-0.3);
            }


         //Se indreapta spre peretele din stanga
        /*MoveLeft(0.6, 1 + constD * (Goldpoz));
        RotateLeft(0.4, 0.5);
        MoveFront(0.6, 2);
        /** Centrul unei fete*/
                // Daca este fata din stanga bazei mergem dreapta daca nu mergem stanga
                /** Muta spre baza + Pune Figurina
                 *  Muta parcare + Mergi inainte pana robotul nu mai este paralel cu podeaua
                 */


        }
    }
}
