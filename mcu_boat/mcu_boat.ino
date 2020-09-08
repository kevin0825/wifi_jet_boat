#include <SoftwareSerial.h>
#include <Servo.h>

#define WIFI_TX       9
#define WIFI_RX       8
#define SERVO         10    //舵机
#define PUMP          13    //水泵
SoftwareSerial wifi(WIFI_RX, WIFI_TX);   //RX, TX

String _comdata_wifi = "";             //for incoming wifi serial data
Servo myservo;                         //定义舵机变量名

void setup() {
     pinMode(PUMP,OUTPUT);
    digitalWrite(PUMP,HIGH);
    myservo.attach(SERVO);//定义舵机接口
    myservo.write(45);  //上电先回正
    delay(500);
  Serial.begin(9600);
  wifi.begin(115200); 
  Serial.println("system is ready!");

  wifi.println("AT+CWMODE=3\r\n");
  delay(500);
  wifi.println("AT+CIPMUX=1\r\n");
  delay(500);
  wifi.println("AT+CIPSERVER=1,5000\r\n");
  delay(500);
}

void loop() {
  getWifiSerialData();
  if(Serial.available()){
    String order = "";
    while (Serial.available()){
      char cc = (char)Serial.read();
      order += cc;
      delay(2);
    }
    order.trim();
    wifi.println(order);
  }
  if(_comdata_wifi!=""){
    Serial.println(_comdata_wifi);
     Serial.println("\r\n");
//     for(int a=0;a<_comdata_wifi.length();a++)
//     {
//     Serial.println(a); 
//    Serial.println(_comdata_wifi[a]);  
//     }
if((_comdata_wifi[2]=='+')&&(_comdata_wifi[3]=='I')&&(_comdata_wifi[4]=='P'))//MCU接收到的数据为+IPD时进入判断控制0\1来使小灯亮与灭
            {
                if((_comdata_wifi[5]=='D')&&(_comdata_wifi[8]==','))
               {  
                    switch(_comdata_wifi[11])
                   {
                       case '0':
                       {
                            digitalWrite(PUMP,LOW);//0 水泵启动，前进   
                            Serial.println("前进");            
                            //wifi.println("前进");
                            //wifi模块向pc端或手机端 发送"前进
                       }
                       break;
                       case '1':
                       {
                            digitalWrite(PUMP,HIGH);    //1 水泵停止，停止
                            //wifi.println("停止");  
                            Serial.println("停止");    
                            //wifi模块向pc端或手机端 发送"停止"
                       }
                       break;
                       case '2':
                       {
                             myservo.write(0);//左转
                             Serial.println("左转");
                             delay(900);
                       }
                       break;
                       case '3':
                       {
                             myservo.write(90);//右转
                             Serial.println("右转");
                             delay(900);
                       }
                       break;
                       case '4':
                       {
                             myservo.write(45);//回正
                             Serial.println("回正");
                             delay(450);
                       }
                       break;
                       default:
                       break;
                   } 
     }   
        }      

    _comdata_wifi = String("");
  }
}

void getWifiSerialData(){
  while (wifi.available() > 0){
    _comdata_wifi += char(wifi.read());   //get wifi data
    delay(4);
  }
}
