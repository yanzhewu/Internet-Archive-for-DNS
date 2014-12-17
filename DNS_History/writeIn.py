__author__ = 'wuyanzhe'

import pickle
import dns.name
import dns.message
import dns.query
import dns.flags
import dns

#list = ['a','b']
#pickle.dump(list,open("record.txt","wb"))
line = 'google.ca'
line = line.strip('\n')
domain = line
name_server = '8.8.8.8'
ADDITIONAL_RDCLASS = 65535
domain = dns.name.from_text(domain)
if not domain.is_absolute():
    domain = domain.concatenate(dns.name.root)
request = dns.message.make_query(domain, dns.rdatatype.ANY)
request.find_rrset(request.additional, dns.name.root, ADDITIONAL_RDCLASS,
                   dns.rdatatype.OPT, create=True, force_unique=True)
response_udp = dns.query.udp(request, name_server)
print(response_udp)

