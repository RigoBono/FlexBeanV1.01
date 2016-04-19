int incomingByte = 0;

int hola[500]; // Aqui se guarda la info del adc 0
int i=0;      
int tdatos = 300;  //Cuantos datos se usan para la calibración inicial
int tdatos2 = 500;  //Cuantos datos se toman para obtener los pulsos
int baset2 = 4;    //Base de tiempo, en milisegundos. 
int limite = 650;  //En porcentaje el limite de corte de la señal normalizada
int prom = 2;      //La cantidad de datos a promediar

boolean calibrado=false;

int test22 = 0;

//Kalman
long double mean1 = 0.0;  
long double var1 = 0.0;
double Xest = 0.0;
double Phi1 = 1;
double H1 = 1;
double Pest1 = var1/1;
double Qs = var1/100;
double R1 = 0.1;
double K1 = 0.0;
double X1 = 0.0;
double P1 = 0.0;

void setup() {                
  Serial.begin(115200);
}

void loop() {
  
 
  for(i=0; i<=tdatos-1; i++){
    hola[i] = analogRead(A0);
    mean1 = mean1+hola[i];
    delay(10);
  }
  mean1 = mean1/tdatos;
  for(i=0; i<=tdatos-1; i++){
  var1 = var1+((hola[i]-mean1)*(hola[i]-mean1));
  }
  var1 = var1/tdatos;
  Pest1 = var1/1;
  Qs = var1/100;
  calibrado=true;
 
    for(i=0; i<=tdatos-1; i++){
     delay(10);
    }
    int vmax=0;
    for(i=0; i<=tdatos-1; i++){
      if(hola[i]>vmax) vmax = hola[i];
    }
    
     int vmin=1000;
    for(i=0; i<=tdatos-1; i++){
      if(hola[i]<vmin) vmin = hola[i];
    }
    
    for(i=0; i<=tdatos-1; i++){
      hola[i] = (int)(1000*(double)(hola[i]-vmin)/(vmax-vmin));
    }
      for(i=0; i<=tdatos-1; i++){
     delay(10);
    }
   
    for(i=0; i<=tdatos-1; i++){
      if(hola[i]<limite) hola[i] = 0;
    }
      for(i=0; i<=tdatos-1; i++){
     Serial.println(hola[i]);
     delay(10);
    }
    int bandera1 = 0;
    int bandera2 = 0;  
    for(i=0; i<=tdatos-1; i++){
      if(hola[i]>=limite && bandera1 ==0) {
        bandera1 = i;
        
      }
      else if(hola[i] >= limite && bandera1 != 0 && (i-bandera1) > 30 ) {
        bandera2 = i;
        break;
      }
      
    }
 
    
    double pulso = (double)60/((double)(bandera2-bandera1)/100);
    
  
   double pulso2 = 0.0;   // Variable que guarda el pulso
   int jj=0;
    while(1){
    
    //Revisa entrada
    if (Serial.available() > 0) {
                incomingByte = Serial.read();
                if(incomingByte==67)
                  Calibra();
        }
      
      bandera1 = 0;
     bandera2 = 0;  
    int A = 0;
    int B = 0;
    //test22 ++;
     for(i=0; i<=tdatos2-1; i++){
        A =micros();
       
        hola[i] = analogRead(A0);
        K1 = Pest1*H1/(H1*Pest1*H1+R1);
        X1 = Xest+K1*(hola[i]-H1*Xest);
        P1 = (1-K1*H1)*Pest1;
        Pest1 = Phi1*P1*Phi1+Qs;
        Xest = Phi1*X1;
        hola[i] = (int) X1;
        if(test22 != 10){
        hola[i] = (int)(1000*(double)(hola[i]-vmin)/(vmax-vmin));
        if(hola[i]<limite) hola[i] = 0;
        
        if(bandera1 == 0 && i>100) break;
        else if(hola[i]>=limite && bandera1 ==0) {
        bandera1 = i;
        
        }
        else if(hola[i] >= limite && bandera1 != 0 && ((i-bandera1) > 20 )) {
        bandera2 = i;
        break;
        }
        else if( bandera1 == 0 && bandera2 == 0 && i >100) { break;
        }
        }
        else {
            // hola[i] = analogRead(A0);
             
        }
      B = micros();
      delayMicroseconds(1000-(B-A));
      delay(baset2-1);
    }
    if(test22 != 10){
        
        pulso = (double)60/((double)(bandera2-bandera1)*baset2/1000);
       if(pulso>0 && pulso<200){
          jj++;
          pulso2 = pulso2+pulso;
          if(jj==prom) {
            //Serial.print("ppm: "); 
             Serial.println((double)pulso2/prom);
             pulso2=0;
             jj=0;
          } 
       }
       else Serial.println("3"); 
       
    }
    else {
      
       for(i=0; i<=tdatos2-1; i++){
     Serial.println(hola[i]);
     delay(10);
     test22 = 0;
    }
      
    }
    
      
    
    }
}

void Calibra(){
 for(i=0; i<=tdatos-1; i++){
    hola[i] = analogRead(A0);
    mean1 = mean1+hola[i];
    delay(10);
  }
  mean1 = mean1/tdatos;
  for(i=0; i<=tdatos-1; i++){
  var1 = var1+((hola[i]-mean1)*(hola[i]-mean1));
  }
  var1 = var1/tdatos;
  Pest1 = var1/1;
  Qs = var1/100;
  calibrado=true;
 
    for(i=0; i<=tdatos-1; i++){
     delay(10);
    }
    int vmax=0;
    for(i=0; i<=tdatos-1; i++){
      if(hola[i]>vmax) vmax = hola[i];
    }
    
     int vmin=1000;
    for(i=0; i<=tdatos-1; i++){
      if(hola[i]<vmin) vmin = hola[i];
    }
    
    for(i=0; i<=tdatos-1; i++){
      hola[i] = (int)(1000*(double)(hola[i]-vmin)/(vmax-vmin));
    }
      for(i=0; i<=tdatos-1; i++){
     delay(10);
    }
   
    for(i=0; i<=tdatos-1; i++){
      if(hola[i]<limite) hola[i] = 0;
    }
      for(i=0; i<=tdatos-1; i++){
     Serial.println(hola[i]);
     delay(10);
    }
    int bandera1 = 0;
    int bandera2 = 0;  
    for(i=0; i<=tdatos-1; i++){
      if(hola[i]>=limite && bandera1 ==0) {
        bandera1 = i;
        
      }
      else if(hola[i] >= limite && bandera1 != 0 && (i-bandera1) > 30 ) {
        bandera2 = i;
        break;
      }
      
    }
} 
