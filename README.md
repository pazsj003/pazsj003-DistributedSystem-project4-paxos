
There 2 client jar and 5 server jar file 


client1.jar is for normal test:
1) Run all 5 servers
open 5 terminal and each terminal run server$.jar  $ is number for server
for example for  
terminal 1: 
java -jar server1.jar
terminal 2: 
java -jar server2.jar
terminal 3: 
java -jar server2.jar
terminal 4: 
java -jar server2.jar
terminal 5: 
java -jar server2.jar
2) run client1.jar
terminal 6: 
java -jar client1.jar localhost Server1




client2.jar is for test random shutdown server:

1) if realdy open 5 server in 5 terminal skip this step and go to step2, or do the same as 
terminal 1: 
java -jar server1.jar
terminal 2: 
java -jar server2.jar
terminal 3: 
java -jar server2.jar
terminal 4: 
java -jar server2.jar
terminal 5: 
java -jar server2.jar

2) run client1.jar
terminal 6: 
java -jar client1.jar localhost Server1




