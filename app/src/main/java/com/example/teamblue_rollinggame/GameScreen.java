package com.example.teamblue_rollinggame;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
///The screen in which the game is played. Consists of moving platforms and a moving ball
public class GameScreen extends AppCompatActivity implements SensorEventListener {
    long startTime, currentTime;
    float rect_width = 80, rect_height = 80;
    float circ_radius = 40;
    //circles starting position
    float circ_x = 500, circ_y = 100;
    //velocity of circle
    float velo_x = 0, velo_y = 0;
    //variables to get accelerometer information
    float accel_x = 0, accel_z = 0;
    SensorManager sensMan;
    VelocityTracker velocity = VelocityTracker.obtain();
    String scoreKey = "com.example.teamblue_rollinggame.score";


    Sensor sens;
    TextView tv;
    int textValue = 0;
    Paint ballPaint;
    boolean touchBall = false;
    ///when the program starts
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //sets start time to the current system time
        startTime = System.currentTimeMillis();
        //sets colour of the ball
        ballPaint = new Paint();
        ballPaint.setColor(Color.GREEN);
        //Makes app full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //locks the device in portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        sensMan = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //sets sens to the accelerometer
        sens = sensMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //creates a drawable view to use
        final CustomDrawableView dv = new CustomDrawableView(this);
        //listens for touches
        dv.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // ... Respond to touch events
                float touchX, touchY;
                touchX = event.getX();
                touchY = event.getY();
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        //Can be a bit dodgy
                        if(dv.touchGround) {
                            velo_y -= 100;
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    default:
                        break;
                }


                return true;
            }
        });
        setContentView(dv);


    }
    ///detects changes in accelerometer reading
    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor s = event.sensor;
        //if the change is in the accelerometer set the accel variables
        if (s.getType() == Sensor.TYPE_ACCELEROMETER) {
            accel_x = event.values[0];

            accel_z = event.values[2];
        }
    }
    ///when resumes
    @Override
    protected void onResume() {
        super.onResume();
        sensMan.registerListener(this, sens, SensorManager.SENSOR_DELAY_NORMAL);

    }
    ///unregisters the sensorlistener on pause
    @Override
    protected void onPause() {
        super.onPause();
        sensMan.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //donothing
    }
    ///when the gamescreen is closed the score is added to the hiscore screen
    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferences shared = this.getSharedPreferences("com.example.teamblue_rollinggame", Context.MODE_PRIVATE);
        Set<String> scores = shared.getStringSet(scoreKey, null);
        if (scores != null) {
            if (scores.size() >= 11) {
                boolean biggerScore = false;
                Integer newScore = textValue;
                List<Integer> sortedList = new ArrayList<Integer>();
                for (String s : scores) {
                    Integer test = null;
                    try {
                        test = Integer.parseInt(s);
                    } catch (NumberFormatException e) {

                    }
                    if (test != null) {
                        sortedList.add(test);
                        if (test < newScore)
                            biggerScore = true;
                    }

                }
                Collections.sort(sortedList);
                Collections.reverse(sortedList);

                //for (String s : scores) {

                //}
                if (biggerScore) {
                    String toBeRemoved = Integer.toString(sortedList.get(sortedList.size() - 1));
                    scores.remove(toBeRemoved);
                    scores.add(Integer.toString(newScore));
                }
            } else {
                scores.add(Integer.toString(textValue));
            }
            shared.edit().remove(scoreKey);
            shared.edit().putStringSet(scoreKey, scores).apply();
            shared.edit().commit();
        }
    }

    ///traps to make the game more interesting
    public class Traps{
    //vkb3 - Will implement class to add traps to the level

    }

    ///The ball that rolls around the screen
    public class Ball {
        float x, y, radius;
        Paint ballP;

        public Ball(float x, float y, float radius) {
            this.x = x;
            this.y = y;
            this.radius = radius;
        }
        ///draws the ball on the canvas
        public void draw(Canvas canvas) {
            ballP = new Paint();
            ballP.setColor(Color.GREEN);
            canvas.drawCircle(x, y, radius, ballP);
        }
        //setX method
        public void setX(float x) {
            this.x = x;
        }
        //setY method
        public void setY(float y) {
            this.y = y;
        }
        //set paint method
        public void setPaint(Paint p) {
            ballP = p;
        }
    }
    ///class for ground objects which the ball will roll on.
    public class Ground {
        float startX, startY, endX, endY;

        public Ground(float startX, float startY, float endX, float endY) {
            this.startX = startX;
            this.startY = startY;
            this.endX = endX;
            this.endY = endY;
        }
        //returns StartX
        public float getStartX() {
            return startX;
        }
        //returns startY
        public float getStartY() {
            return startY;
        }
        //returns endX
        public float getEndX() {
            return endX;
        }
        //returns endY
        public float getEndY() {
            return endY;
        }
        ///Draws the ground platforms on the canvas
        public void draw(Canvas canvas) {
            Paint ground = new Paint();
            Paint fill = new Paint();
            ground.setColor(Color.BLACK);
            fill.setColor(Color.GRAY);
            ground.setStrokeWidth(35);
            fill.setStrokeWidth(15);
            canvas.drawLine(startX, startY, endX, endY, ground);
            canvas.drawLine(startX + 10, startY, endX -10, endY, fill);
            move();
        }
        ///Makes the ground move
        private void move() {
            startX--;
            endX--;
        }

        public boolean test() {
            float bottomY = circ_y + circ_radius;
            float topY = circ_y - circ_radius;
            if (circ_x > startX && circ_x < endX && bottomY > startY && Math.abs(bottomY - startY) < 35) {
                circ_y = startY - circ_radius;
                velo_y = 0;
                return true;
            } else if (circ_x > startX && circ_x < endX && topY < startY && Math.abs(topY - startY) < 35) {
                circ_y = startY + circ_radius;
                velo_y = 0;
                return false;
            } else {
                return false;
            }
        }

    }
    //list of ground objects
    private ArrayList<Ground> gList;
    ///The view where the ball and the ground interact
    public class CustomDrawableView extends View {
        boolean touchGround = false;
        //constructs the drawable view with a ground platform
        public CustomDrawableView(Context context) {
            super(context);
            gList = new ArrayList<Ground>();
            gList.add(new Ground(0, 500, 600, 500));
        }


        protected void onDraw(Canvas canvas) {
            //increases score
            textValue++;
            final int fallSpeed = 3;
            //Different speed settings
            final double slowSpeedScale = 0.5;
            final double mediumSpeedScale = 0.8;
            final double fastSpeedScale = 1.2;

            currentTime = System.currentTimeMillis();
            if (currentTime - startTime > 3000) {
                startTime = currentTime;
                Random r = new Random();
                int rand = Math.abs(r.nextInt()) % 3;
                //creates a ground platform at one of three random positions
                switch (rand) {
                    case 0:
                        gList.add(new Ground(300, 600, 600, 600));
                        break;
                    case 1:
                        gList.add(new Ground(300, 500, 600, 500));
                        break;
                    case 2:
                        gList.add(new Ground(300, 400, 600, 400));
                        break;
                }
            }


            //tv.setText(Integer.toString(textValue));
            Paint p = new Paint();
            //Paint ground = new Paint();
            //ground.setColor(Color.BLACK);
            //ground.setStrokeWidth(20);
            //p.setColor(Color.BLACK);
            //canvas.drawLine(lineStartX, lineStartY, lineEndX, lineEndY, ground);

            p.setTextSize(60);
            canvas.drawText(Integer.toString(textValue), 50, 100, p);

            //canvas.drawRect(circ_x, circ_y, circ_x + rect_width, circ_y + rect_height, p);

            if (touchGround == false) {
                //Log.d("ACCEL", "VELO: "+velo_y);
                velo_x -= accel_x*slowSpeedScale;
                velo_y += fallSpeed;

                velo_x *= 0.9f;
                velo_y *= 0.9f;
                //Log.d("YPOS_OLD", ""+circ_y);
                circ_x += velo_x;
                circ_y += velo_y;
                //Log.d("YPOS_NEW", ""+circ_y);
            }

            touchGround = false;
            ArrayList<Ground> removeList = new ArrayList<Ground>();
            removeList.addAll(gList);
            //draws the ground platforms
            for (Ground g : gList) {
                g.draw(canvas);
                if (g.getEndX() < 0) {
                    removeList.remove(g);
                }
                if (g.test() == true)
                    touchGround = true;
            }
            gList = removeList;

            //keeps the ball from falling off the screen
            if (circ_x - circ_radius < 0) {
                circ_x = 0 + circ_radius;
                velo_x = 0;
            }
            //keeps the ball from falling off the screen

            if (circ_x + circ_radius > canvas.getWidth()) {
                circ_x = canvas.getWidth() - circ_radius;
                velo_x = 0;
            }
            //stops the ball leaving the top of the screen
            if (circ_y - circ_radius < 0) {
                circ_y = 0 + circ_radius;
                velo_y = 0;

            }
            //detects if the ball falls off the bottom and displays a toast
            if (circ_y + circ_radius > canvas.getHeight()) {
                circ_y = canvas.getHeight() - circ_radius;

                //Display "You died toast"
                Toast.makeText(getApplicationContext(), "You Died!" , Toast.LENGTH_SHORT ).show();

                finish();
                //velo_y = 0;
            }


            canvas.drawCircle(circ_x, circ_y, circ_radius, ballPaint);

            //Stops ball from rolling off the side of the screen.


            //test(lineStartX, lineStartY, lineEndX, lineEndY);

            invalidate();
        }
    }
}
