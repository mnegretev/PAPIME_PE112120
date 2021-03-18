using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class AcelerometerMove : MonoBehaviour
{
                        //THIS IS THE PLANE SCRIPT

    //Start is called before the first frame update
    void Start()
    {
        
    }

    // Update is called once per frame
    void Update()
    {
    	//To acces to acelerometer inputs

        float x = Input.acceleration.x; //We acces to x aceleration
        float y = Input.acceleration.y; //We acces to y aceleration

        //To move an object along an axis we use a function called:
        //transform.Translate("Xvalue", "Yvalue", "Zvalue") 

        //If we want to move the object along the X axis:
        //transform.Translate(XVlaue, y, z);

        //The object will move along X axis (positive and
        //negative) depending on the aceleration input.

        /*Time.deltaTime -- Use this funtion to run the game in any 
        computer without worrig about the execution velocity. However,
        we can write all the instructions inside this function:

        FixedUpdate()
        {
            //instructions
        }

        FixedUpdate funtion works as Time.deltaTime function, but we do not
        write it many times as Time.deltaTime.
        */

        transform.Translate(x*Time.deltaTime*20,0,0);   //Move the object along the X axis depending on the accelerometer readings
        transform.Translate(0,0,y*Time.deltaTime*20);   //Move the object along the Z axis depending on The accelerometer readings

        /*
        We are going to rotate the object around Z
        //axis, depending on the aceleration input:
        */
        //Rotation respect to the Z axis
        transform.Rotate(0,0,-x);
        //Rotation respect to the X axis
        transform.Rotate(y,0,0);
    }
}

