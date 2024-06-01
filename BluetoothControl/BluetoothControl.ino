#include <SoftwareSerial.h>
#include <Servo.h>

#define DELAY 2500
#define OneStepMillisecForSrvo 10;
//#define MAX_COUNT_MOTORS 20;
const int MAX_COUNT_MOTORS = 30;

class Motor {
  protected:
    int PinRelay;

  public:
    Motor(int pinRelay)
    {
      this->PinRelay = pinRelay;
      pinMode(this->PinRelay, OUTPUT);
      MotorOff();
    }

    int OneStepPlusMicrosec()
    {
      return 0;
    }

    int OneStepMinusMicrosec()
    {
      return 0;
    }

    bool MotorOn()
    {
      digitalWrite(this->PinRelay, LOW);
      return true;
    }

    bool MotorOff()
    {
      digitalWrite(this->PinRelay, HIGH);
      return true;
    }

    int setCurrentMin()
    {
      return 0;
    }

    int setCurrentMax()
    {
      return 0;
    }

};

class ServoPrivod : public Motor {

  protected:
    Servo servo;

    int PinServo;
    //int PinRelay;
    int CurrentMicrosec;

    int MinMicrosec;
    int MaxMicrosec;

  public: 
    ServoPrivod(int pinControl, int pinRelay, int minMicrosec, int maxMicrosec) : Motor(pinRelay)
    {
      this->PinServo = pinControl;
      // this->PinRelay = pinRelay;
      servo.attach(pinControl);
      //pinMode(pinRelay, OUTPUT);
      this->MinMicrosec = minMicrosec;
      this->MaxMicrosec = maxMicrosec;
      this->CurrentMicrosec = minMicrosec;
      setCurrentMin();
      MotorOn();
      delay(DELAY);
      MotorOff();
    }

    int OneStepPlusMicrosec()
    {
      int newCurrent = -1;

      if (servo.attached())
      {
        newCurrent = this->CurrentMicrosec + OneStepMillisecForSrvo;

        if (newCurrent > this->MaxMicrosec)
          newCurrent = this->MaxMicrosec;
        
        this->CurrentMicrosec = newCurrent;

        //digitalWrite(this->PinRelay, LOW);
        //servo.writeMicroseconds(newCurrent);
        //delay(DELAY);
        //digitalWrite(this->PinRelay, HIGH);  
      }

      return newCurrent;
    }

    int OneStepMinusMicrosec()
    {
      int newCurrent = -1;

      if (servo.attached())
      {
        newCurrent = this->CurrentMicrosec - OneStepMillisecForSrvo;

        if (newCurrent < this->MinMicrosec)
          newCurrent = this->MinMicrosec;
        
        this->CurrentMicrosec = newCurrent;

        //digitalWrite(this->PinRelay, LOW);
        //servo.writeMicroseconds(newCurrent);
        //delay(DELAY);
        //digitalWrite(this->PinRelay, HIGH);
      }

      return newCurrent;
    }

    int setCurrentMin()
    {
      int newCurrent = -1;

      if (servo.attached())
      {
        newCurrent = this->MinMicrosec;
        this->CurrentMicrosec = newCurrent;

        //digitalWrite(this->PinRelay, LOW);
        //servo.writeMicroseconds(newCurrent);
        //delay(DELAY);
        //digitalWrite(this->PinRelay, HIGH);
      }
      return newCurrent;
    }

    bool MotorOn()
    {
      bool res = false;
      if (servo.attached())
      {
        digitalWrite(this->PinRelay, LOW);
        servo.writeMicroseconds(this->CurrentMicrosec);
      }
      return res;
    }

    bool MotorOff()
    {
      digitalWrite(this->PinRelay, HIGH);
      return true;
    }

    int setCurrentMax()
    {
      this->CurrentMicrosec = this->MaxMicrosec;
      return this->CurrentMicrosec;
    }
};


//***************************************************************
// class Robot {

//   public:
//     Robot()
//     {
//       ;
//     }

//     void PlusOneStep(Motor* motor)
//     {
//       motor->OneStepPlusMicrosec();
//     }

//     void MinusOneStep(Motor* motor)
//     {
//       motor->OneStepMinusMicrosec();
//     }

//     void PlusOneStep(int countMotors, ...)
//     {
//       bool res = false;
//       if (countMotors > 0)
//       {
//         res = true;
//         va_list motors;                     // указатель va_list
//         va_start(motors, countMotors);      // устанавливаем указатель
       
//         for(int i = 0; i < countMotors; i++)
//         {
//           va_arg(motors, Motor*) -> OneStepPlusMicrosec();  // получаем значение текущего параметра типа Motor
//         }
//         va_end(motors); // завершаем обработку параметров
//       }

//       return res;
//     }

//     void MinusOneStep(int countMotors, ...)
//     {
//       bool res = false;
//       if (countMotors > 0)
//       {
//         res = true;
//         va_list motors;                     // указатель va_list
//         va_start(motors, countMotors);      // устанавливаем указатель
       
//         for(int i = 0; i < countMotors; i++)
//         {
//           va_arg(motors, Motor*) -> OneStepMinusMicrosec();  // получаем значение текущего параметра типа Motor
//         }
//         va_end(motors); // завершаем обработку параметров
//       }

//       return res;
//     }

//     bool MotorsOn(int countMotors, ...)
//     {
//       bool res = false;
//       if (countMotors > 0)
//       {
//         res = true;

//         Motor* tempArray[MAX_COUNT_MOTORS]; // Временный массив всех моторов
//         va_list motors;                     // указатель va_list
//         va_start(motors, countMotors);      // устанавливаем указатель
       
//         for(int i = 0; i < countMotors; i++)
//         {
//             tempArray[i] = va_arg(motors, Motor*);  // получаем значение текущего параметра типа Motor
//             tempArray[i] -> MotorOn();
//         }
//         va_end(motors); // завершаем обработку параметров
        
//         // delay(DELAY);   // Временная задержка для работы всех моторов
        
//         // for(int i = 0; i < countMotors; i++)
//         // {
//         //     tempArray[i] -> MotorOff();
//         // }
//       }

//       return res;
//     }

//     bool MotorsOff(int countMotors, ...)
//     {
//       bool res = false;
//       if (countMotors > 0)
//       {
//         res = true;

//         Motor* tempArray[MAX_COUNT_MOTORS]; // Временный массив всех моторов
//         va_list motors;                     // указатель va_list
//         va_start(motors, countMotors);      // устанавливаем указатель
       
//         for(int i = 0; i < countMotors; i++)
//         {
//             tempArray[i] = va_arg(motors, Motor*);  // получаем значение текущего параметра типа Motor
//             tempArray[i] -> MotorOff();
//         }
//         va_end(motors); // завершаем обработку параметров
        
//         // delay(DELAY);   // Временная задержка для работы всех моторов
        
//         // for(int i = 0; i < countMotors; i++)
//         // {
//         //     tempArray[i] -> MotorOff();
//         // }
//       }

//       return res;
//     }
// };
//***************************************************************

int GetValue; // Принимаемый сигнал
int led = 13; // Номер пина светодиода на плате ардуино
//SoftwareSerial BTserial(17, 16);  // Соединение по блютуз

Motor* cat11;
Motor* cat12;
Motor* cat21;
Motor* cat22;
ServoPrivod* X;
ServoPrivod* Z1;
ServoPrivod* Z2;
ServoPrivod* Y;
ServoPrivod* Hand;
ServoPrivod* Upper;

void setup()
{
  Serial.begin(9600);
  //BTserial.begin(9600);   // Соединение по блютуз
  //pinMode(led, OUTPUT);

  //*****************************************
  // Модель робота
  cat11 = new Motor(41);
  cat12 = new Motor(42);
  cat21 = new Motor(43);
  cat22 = new Motor(44);
  X = new ServoPrivod(5, 50, 600, 2100);
  Z1 = new ServoPrivod(6, 51, 800, 1350);
  Z2 = new ServoPrivod(8, 52, 1150, 1800);
  Z1->setCurrentMax();
  Y = new ServoPrivod(10, 53, 1100, 1450);
  Hand = new ServoPrivod(4, 46, 500, 2500);
  Upper = new ServoPrivod(36, 37, 500, 2500);
  //*****************************************
}

void loop()
{
  //if (Serial.available())
  if (Serial.available())
  {
    GetValue =  Serial.read();
    Serial.println(GetValue);

    switch (GetValue)
    {
      case '0':
      {
        cat11->MotorOff();
        cat12->MotorOff();
        cat21->MotorOff();
        cat22->MotorOff();
        X->MotorOff();
        Z1->MotorOff();
        Z2->MotorOff();
        Y->MotorOff();
        Hand->MotorOff();
        Upper->MotorOff();
        Serial.println("Motors are off!");
        break;
      }

      case '1':
      {
        cat11->MotorOn();
        cat21->MotorOn();
        break;
      }

      case '2':
      {
        cat12->MotorOn();
        cat21->MotorOn();
        break;
      }

      case '3':
      {
        cat12->MotorOn();
        cat22->MotorOn();
        break;
      }

      case '4':
      {
        cat11->MotorOn();
        cat22->MotorOn();
        break;
      }

      case 'X':
      {
        do 
        {
          Serial.println(X->OneStepMinusMicrosec());
          X->MotorOn();
        } while(!Serial.available());
        break;
      }

      case 'x':
      {
        do 
        {
          Serial.println(X->OneStepPlusMicrosec());
          X->MotorOn();
        } while(!Serial.available());
        break;
      }

      case 'Y':
      {
        do 
        {
          Serial.println(Y->OneStepMinusMicrosec());
          Y->MotorOn();
        } while(!Serial.available());
        break;
      }

      case 'y':
      {
        do 
        {
          Serial.println(Y->OneStepPlusMicrosec());
          Y->MotorOn();
        } while(!Serial.available());
        break;
      }

      case 'Z':
      {
        do 
        {
          Serial.println(Z2->OneStepPlusMicrosec());
          Serial.println(Z1->OneStepMinusMicrosec());
          Z1->MotorOn();
          Z2->MotorOn();
        } while(!Serial.available());
        break;
      }

      case 'z':
      {
        do 
        {
          Serial.println(Z1->OneStepPlusMicrosec());
          Serial.println(Z2->OneStepMinusMicrosec());
          Z1->MotorOn();
          Z2->MotorOn();
        } while(!Serial.available());
        break;
      }

      case 'U':
      {
        do 
        {
          Serial.println(Upper->OneStepPlusMicrosec());
          Upper->MotorOn();
        } while(!Serial.available());
        break;
      }

      case 'u':
      {
        do 
        {
          Serial.println(Upper->OneStepMinusMicrosec());
          Upper->MotorOn();
        } while(!Serial.available());
        break;
      }

      case 'T':
      {
        do 
        {
          Serial.println(Hand->OneStepPlusMicrosec());
          Hand->MotorOn();
        } while(!Serial.available());
        break;
      }

      case 't':
      {
        do 
        {
          Serial.println(Hand->OneStepMinusMicrosec());
          Hand->MotorOn();
        } while(!Serial.available());
        break;
      }

      default:
      {
        cat11->MotorOff();
        cat12->MotorOff();
        cat21->MotorOff();
        cat22->MotorOff();
        X->MotorOff();
        Z1->MotorOff();
        Z2->MotorOff();
        Y->MotorOff();
        Hand->MotorOff();
        Upper->MotorOff();
        Serial.println("Motors are off!");
        break;
      }
    }
  }
}