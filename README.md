Internet-Archive-for-DNS
===========================================================

##Background

As we know, IP addresses change from time to time. This fact brings some vulnerabilities like DNS cache poisoning. Transient
DNS failures are also possible when websites fails to renew their domain names.  
By recording the IP addresses of certain websites, we can trace the change history of DNS records and analyze their behavior or detect
malicious IP address change.

##Description

My task consists of 3 parts:  

1. Monitor  
   A monitor should be easy to deploy and able to keep querying for IP addresses at a given frequency. Also considering for load balancing, monitors should run in different locations.  
2. Server  
   A centralized server should be capable of accepting data from different sources which are Monitors, tagging them by IP addresses of Monitors and recording them to persistent storage.  
   A server should also accept requests from clients, search in database, compute and return answers to clients.
3. Client  
   A client sends requests to the server. The request are designed in given patterns.  

##System Design

1. High Level Collaboration  
 ![alt tag](https://github.com/yanzhewu/Internet-Archive-for-DNS/blob/master/Images/SequenceDiagramStorage.png)  
 
2.  
  2.1 Monitor  
      Use [dnsjava](http://www.dnsjava.org/) to query target websites which are [Alexa top 500](http://www.alexa.com/topsites)  
      Maintain a work queue which collaborates with multiple worker threads  
      Handle errors caused by query failures and retry them  
      Pack and send data collected to centralized server through TCP  
  2.2 Centralized Server  
      Keep accepting data from many Monitors  
      Unpack, label and store data in SQLite  
      Accept requests from clients, query in database and compute answers  
  2.3 Client  
      Send requests to server through TCP  
      Usage Examples:  
          Show Valid IPs of website during Time Point 1 to Time Point 2  
          Show the websites which are been monitored  
          Show the monitors which have recorded a particular IP  

##Project Overview

This is a full stack project but focus more on back-end.  

My Server can be easily deployed on any linux machine which has java 1.7 installed.  
I tested to launch 5 monitors on Amazon ec2 machines and they run well.  
Client can get answers from server remotely.  

##Technical Choices, Trade-offs and Other Concerns

1. Why [dnsjava](http://www.dnsjava.org/)  
   Right now, what I need are only valid IP addresses(To be specific, mostly [A records](https://en.wikipedia.org/wiki/List_of_DNS_record_types)) for certain websites. But in the future, other information like TTL, MX record or IPv6 records may become our concerns. So I construct my own DNS packet object from [dnsjava](http://www.dnsjava.org/) library to make it convenient for next iterations.  

2. Why multiple thread with work queue  
   There are 2 reasons for it.  
   2.1 The queries for DNS records may time out for many reasons, using only one thread will result in single point failure. Once a website times out, the program will crash.  
   2.2 Querying in parallel is much faster than querying websites one by one.(My experiment: time for thousands of websites reduce from more than 5 minutes to less than 1 second)  

3. Why my own DNS packet object  
   I need a class which implements serializable to be passed through TCP connection.  
   
4. Why SQLite  
   At the beginning, I used MySQL and tested the server on my own laptop, but when I tried to deploy it on the university linux machine, I found I do not have the account and permission to MySQL service. So I changed it to SQLite, it told me that the design must take the test environment into account.  

5. Next Steps  
   5.1 Show IP addresses for websites on map to analyze the load balancing behavior of websites  
   5.2 Modified DNS resolver to avoid DNS cache poisoning  
   5.3 Web browser extension which gives users a choice to go back to previous IP addresses and avoid transient DNS failure  
     
##Links  
<a href="52.8.114.250:3000" target="_blank">My Web App</a>  
[GitHub Profile](https://github.com/yanzhewu)  
[Linked Profile](https://www.linkedin.com/in/yanzhewu)  

##Result and Analysis of Output

1. Number of IPs per domain  
   ![alt tag](https://github.com/yanzhewu/Internet-Archive-for-DNS/blob/master/Images/8.png)  
  
2. Number of IPs per domain CDF  
   ![alt tag](https://github.com/yanzhewu/Internet-Archive-for-DNS/blob/master/Images/1.png)  
   
3. Distribution of Durations CDF
   ![alt tag](https://github.com/yanzhewu/Internet-Archive-for-DNS/blob/master/Images/2.png)
   
4. Mean Duration of IPs CDF
   ![alt tag](https://github.com/yanzhewu/Internet-Archive-for-DNS/blob/master/Images/3.png)  
   
5. Number of IPs vs Different Locations  
   ![alt tag](https://github.com/yanzhewu/Internet-Archive-for-DNS/blob/master/Images/4.png)  

6. Number of Distinct IPs vs Different Locations  
   ![alt tag](https://github.com/yanzhewu/Internet-Archive-for-DNS/blob/master/Images/5.png) 
   
7. Distribution of Number of IPs for Different Locations(Order by Num)
   ![alt tag](https://github.com/yanzhewu/Internet-Archive-for-DNS/blob/master/Images/6.png)

8. Distribution of Number of IPs for Different Locations(Order by Websites)
   ![alt tag](https://github.com/yanzhewu/Internet-Archive-for-DNS/blob/master/Images/7.png)
   

