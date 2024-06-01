#include <Servo.h>
#define DELAY 3000      // Временная задержка
#define LED_PIN 13      // Пин светодиода
#define maxMessages 12  // Максимальное количество сообщений

//********************************************************************************************
//               !!!КЛАСС УЖЕ НЕ ИСПОЛЬЗУЕТСЯ, ВМЕСТО НЕГО - ManyMotorsMessages!!!
// Класс сообщения для сервопривода из последовательного порта
class MotorMessage
{
  private:
    int motorPort = -1;       // Порт сервопривода
    int relayPort = -1;       // Порт реле
    int degree = -1;          // Новое положение в микросекундах

    bool messageIsGet = 0;    // Флаг, что сообщение принято верное (по шаблону)

  public:

    // Конструктор поумолчанию
    MotorMessage()
    {
      
    }

    // Формат сообщений:
    // число#число#число
    bool getMessage()
    {
      bool result = 0;
      byte x, y;
      int a, b, c;

      if (Serial.available() > 0)
      {
      	a = Serial.parseInt();
      	Serial.println(a);

        x = Serial.read();
        Serial.println(x);

        b = Serial.parseInt();
        Serial.println(b);


        y = Serial.read();
        Serial.println(y);


        c = Serial.parseInt();
        //c = constrain(c, 500, 2500);
        Serial.println(c);

        if ((a > 0) && (b > 0) && (c > 0) && (x == 35) && (y == 35))
        {
          relayPort = a;
          motorPort = b;
          degree = c;
          messageIsGet = 1;

          result = 1;
        }
        else
        {
          relayPort = -1;
          motorPort = -1;
          degree = -1;
          messageIsGet = 0;
        }
             
      }

      return result;
    }

    // Использовать сервопривод по принятому сообщению
    void useMotor()
    {
      if (messageIsGet)
      {
        Servo servo1;

        //Serial.println(relayPort);
        //Serial.println(motorPort);
        //Serial.println(degree);
        //Serial.flush();

        pinMode(relayPort, OUTPUT); // Управление реле пин
        servo1.attach(motorPort);   // Управление серво пин
        
        //Serial.println(servo1.read());

        digitalWrite(relayPort, LOW);
        servo1.writeMicroseconds(degree);
        delay(DELAY);
        digitalWrite(relayPort, HIGH);
        servo1.detach();
      }
    }
};
//********************************************************************************************


//============================================================================================
// Класс принятия и использования сообщений для реле и сервоприводов из последовательного порта.
//
// Передается сообщение по последовательному порту.
// Шаблон сообщения - число#число#число.
// Сообщений может быть много за один раз через символ &, т.е. РЕЛЕ и СЕРВОПРИВОДЫ будут
// активированы все из сообщения за один раз.
//
// Первое чило означает номер пина с РЕЛЕ. ОГРАНИЧЕНИЙ по номерам пинов нет!
//
// Второе число означает номер пина с СЕРВОПРИВОДМ, 
// но если номер пина равен 100, то сигнал на пин 100 не посылается
// т.к. УСЛОВНО (я уловился) считается, что этот пин для управления двигателями постоянного тока
// через реле, для которых не нужны управляющие сигналы на пин, т.е. просто открытие реле.
// Если в сообщении хотябы один пин сервопривода равен 100, то ВСЕ
// СЕРВОПРИВОДЫ в сообщении будут игнорироваться! Это сделано для применения гусениц.
// А именно, верменная задержка на выполнение разная, для гусениц она намного меньше, чем СЕРВО.
// И совместить долгое выполнение Сервопривода и быстрое исполнение гусениц тяжело.
// Поэтому совместить СЕРВОПРИВОД и обычный электромотор НЕ ВОЗМОЖНО в этой реализации.
// Только разные сообщения!
//
// Третье число - значение микросекунд для сервопривода (аналог градусов, только микросекунды).
// Если пин сервопривода равен 100, т.е. сервопривод не будет активирован, то значение микросекунд
// может быть любым. А так, обычно диапазон от 500 до 2500 мкс.
//
// Все числа в сообщении должны быть больше 0.
// Полсе получения сообщения его нужно применить.
// После применения все подключенные сервоприводы отключаются, кроме случая с пином 100
// ----------------------
// НАПРИМЕР:
// Сообщение: 2#4#2000
// Результат: пин 2 будет OUTPUT для РЕЛЕ, 
//            пин 4 будет активирован для СЕРВО и 
//            переведен в положение 2000
// ----------------------
// ЕЩЕ ПРИМЕР:
// Сообщение 6#7#1100&3#5#1900
// Результат: пины 6 и 3 будут OUTPU для двух РЕЛЕ
//            сервоприводы на пинах 7 и 5 будут активированы
//            оба сервопривода одновременно преместятся в положения 1100 и 1900 соответсвенно.
// ----------------------
// ЕЩЕ ПРИМЕР:
// Сообщение 41#100#1000&43#100#4444
// Результат: пины 41 и 43 будут OUTPU для двух РЕЛЕ
//            сервоприводы не будут активированы, т.к. пин 100 ДЛЯ СЕРВО
//            оба ЭЛЕКТРОДВИГАТЕЛЯ одновременно начнут вращение в течении 300 млсек.
// ----------------------
// ПРИМЕР НЕПРАВИЛЬНОЙ РАБОТЫ:
// Сообщение 6#7#1100&41#100#1900
// Результат: пины 6 и 41 будут OUTPU для двух РЕЛЕ
//            сервоприводы не будут активированы, т.к. встретился хотя бы одни пин 100 ДЛЯ СЕРВО
//            электродвигатель будет вращатся в течении 300 млсек, а сервопривод не будет вращатся никуда.
// ----------------------
class ManyMotorsMessages
{
  private:
    Servo servos[maxMessages];    // Сервоприводы

    //const int maxMessages = 12;   // Максимальное количество сообщений
    int motorPort [maxMessages];  // Порт сервопривода
    int relayPort [maxMessages];  // Порт реле
    int degree [maxMessages];     // Новое положение в микросекундах

    int countMessages;            // Количество принятых сообщений

  public:

    // Конструктор поумолчанию
    ManyMotorsMessages()
    {
      // Задаем начальные значения массивам
      for (int i = 0; i < maxMessages; i++)
      {
        motorPort[i] = -1;
        relayPort[i] = -1;
        degree[i] = -1;
      }
      countMessages = 0;
    }

    // Принять сообщения
    // Формат сообщений:
    // число#число#число
    bool getManyMessages()
    {
      bool result = 0;
      byte x, y, z;
      int a, b, c;

      // Проверяем, что в последовательном порту что-то есть и при этом мы не достигли предела сообщений (одновременно работающих серво)
      if ((Serial.available() > 0) && (countMessages < 12))
      {
        // Читаем порт реле
      	a = Serial.parseInt();
      	Serial.println(a);

        // Читаем #
        x = Serial.read();
        Serial.println(x);

        // Читаем порт серво
        b = Serial.parseInt();
        Serial.println(b);

        // Читаем #
        y = Serial.read();
        Serial.println(y);

        // Читаем новое значение поворота
        c = Serial.parseInt();
        //c = constrain(c, 500, 2500);
        Serial.println(c);

        // Если сообщение соответсвует шаблону: число#число#число
        if ((a > 0) && (b > 0) && (c > 0) && (x == 35) && (y == 35))
        {
          relayPort[countMessages] = a;
          motorPort[countMessages] = b;
          degree[countMessages] = c;
          countMessages += 1;

          // Проверяем, что дальше в последовательном порту
          z = Serial.peek();

          // Если там символ &
          if (z == 38)
          {
            // То читаем этот символ и повторяем заново прием сообщений
            z = Serial.read();
            Serial.println(z);

            return this->getManyMessages();
          }

          result = 1;
        }
      }

      //if (result)
      //  Serial.println(countMessages);

      return result;
    }

    // Использовать сервоприводы по принятым сообщениям
    void useManyMotors()
    {
      if (countMessages > 0)
      {
        //Serial.println(countMessages);

        int flagGusenicy = false; // Флаг, что хотябы один порт из сообщения равен 100
        for (int i = 0; i < countMessages; i++)
        {
          if (motorPort[i] == 100)
          {
            flagGusenicy = true;
            break;
          }
        }

        for (int i = 0; i < countMessages; i++)
        {
          pinMode(relayPort[i], OUTPUT); // Управление реле пин
          if (flagGusenicy == false)
            servos[i].attach(motorPort[i]);   // Управление серво пин

          //String g(servos[i].attached());
          //Serial.println(g);
        }
        
        for (int i = 0; i < countMessages; i++)
        {
          digitalWrite(relayPort[i], LOW);
        }


        int useDelay = DELAY;

        if (flagGusenicy == false)
        {
          for (int i = 0; i < countMessages; i++)
          {
            servos[i].writeMicroseconds(degree[i]);
          }
        }
        
        if (flagGusenicy == true)
          useDelay = 300;

        delay(useDelay);
        
        for (int i = 0; i < countMessages; i++)
          digitalWrite(relayPort[i], HIGH);

        for (int i = 0; i < countMessages; i++)
        {
           if (flagGusenicy == false)
            servos[i].detach();
          
          motorPort[i] = -1;
          relayPort[i] = -1;
          degree[i] = -1;
        }
        
        if (flagGusenicy == false)
          delay(2000);
      }

      countMessages = 0;
    }
};
//============================================================================================


//----------------------------------------------
// Функция тестирования, для работы не нужна!
// Подключение двух реле и двух сервоприводов одновременно.
// И одновременное управление ими.
int tmp = 2000;
Servo servoTMP [2];
void test()
{ 
  servoTMP[0].attach(5);   // Управление серво пин
  pinMode(2, OUTPUT);   // Управление реле пин
  servoTMP[1].attach(6);  // Управление серво пин
  pinMode(4, OUTPUT);   // Управление реле пин
  
  digitalWrite(2, LOW);
  digitalWrite(4, LOW);

  servoTMP[0].writeMicroseconds(tmp);
  servoTMP[1].writeMicroseconds(tmp);

  delay(DELAY);

  digitalWrite(2, HIGH);
  digitalWrite(4, HIGH);

  servoTMP[0].detach();
  servoTMP[1].detach();

  delay(1000);

  if (tmp == 2500)
    tmp -= 500;
  else if (tmp == 2000)
    tmp += 500;
}
//----------------------------------------------


// Объявление указателей на классы для принятия сообщений
MotorMessage* mm;                 // Больше не нужен!
ManyMotorsMessages* manyMessages; // АКТУАЛЬНЫЙ

void setup() {
  //открытие Serial-порта со скоростью 9600 бод/c
  Serial.begin(9600);

  // Создаем прием сообщений по последовательному порту
  mm = new MotorMessage();

  // АКТУАЛЬНЫЙ приём сообщений
  manyMessages = new ManyMotorsMessages();

  // Настройка пина со светодиодом в режим выхода
  //pinMode(LED_PIN, OUTPUT);
}

void loop() {

  /*
  // Старый прием сообщений!
  // Если есть сообщение
  if (mm->getMessage())
  {
    // Моргаем светодиодом
    //digitalWrite(LED_PIN, HIGH);
    //delay(1000);
    //digitalWrite(LED_PIN, LOW);
    //delay(1000);

    // Включаем реле и сервопривод
    mm->useMotor();
  }
  */

  //    2#5#1000&4#6#1000
  //    2#5#2500&4#6#2500

  // Актуальный пример принятия сообщений и использования их
  if (manyMessages->getManyMessages())
  {
    manyMessages->useManyMotors();
  }


  //test();
  
}
