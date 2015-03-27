# Internet-Archive-for-DNS
A practicum project to store and analyze the history of DNS records

System Design
  1. Monitor(Multiple  Clients Running From Different Locations)
       Use dnsjava to query target websites(Alexa top 500)
       Use work queue with multiple worker threads
       Handle the errors caused by query failures
       Retry queries
       Send data collected to Storage service through TCP
  2. Storage
       Accept data from multiple Monitor service
       Store data in SQLite
       Use RW lock to ensure the correctness of data
       Provide interfaces for Command Line Client to do queries
  3. Command Line Client
       Connect to a remote Storage service
       Usage: 
             Show Valid IPs of website during Time Point 1 to Time Point 2
             Show the websites which are been monitored
             Show the monitors which have recorded a particular IP
 
