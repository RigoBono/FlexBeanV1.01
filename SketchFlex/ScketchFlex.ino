void setup() {
  // put your setup code here, to run once:
  Serial.begin(115200);
}

void loop() {
  String prev="*"+String(analogRead(1))+"*";
  for(int i=0;i<50;i++){
    prev=prev+"|"+String(analogRead(0));
    delay(10);
  }
  prev=prev+"*";
  Serial.println(prev);


}
